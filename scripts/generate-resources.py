#!/usr/bin/env python3
"""Generate per-loader data pack / resource pack trees for modern Minecraft versions.

The canonical data lives in ``common/src/main/resources`` and is written for the
1.20.1 layout.  This script rewrites that tree for the 1.21.1 and 26.1 data pack
formats (NeoForge and Fabric variants) and writes the result to a profile's
output directory, normally ``targets/<target>/build/generated/resources/main``.

Profiles are small JSON files in ``scripts/resource-profiles``.  They select the
format family, the loader and the handful of loader-specific switches (extra
tags, entity-component wrappers, equipment asset naming, ...).

Usage::

    python scripts/generate-resources.py --profile 26.1-neoforge
    python scripts/generate-resources.py --all
    python scripts/generate-resources.py --profile 1.21.1-fabric --output /tmp/out
    python scripts/generate-resources.py --profile 26.1-fabric --check /tmp/golden

The generator only uses the Python standard library.  It reads the shared item
and armour tables from common Java sources because those tables own the registry
ids, armour material names and horse armour defence values that data pack ids
must match.  It never writes to the common tree.
"""

from __future__ import annotations

import argparse
import json
import re
import shutil
import sys
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parent
COMMON_RESOURCES = REPO_ROOT / "common" / "src" / "main" / "resources"
COMMON_JAVA = REPO_ROOT / "common" / "src" / "main" / "java" / "net" / "mirrorloong" / "chineseweapons"
PROFILE_DIR = SCRIPT_DIR / "resource-profiles"

MOD_ID = "chineseweapons"
MOD_ASSETS = f"assets/{MOD_ID}"
MOD_DATA = f"data/{MOD_ID}"

# Resource paths owned by the target (build metadata, mixin configs, vanilla
# model overrides) and therefore never generated.
HAND_OWNED = {
    "pack.mcmeta",
    "fabric.mod.json",
    "chineseweapons.mixins.json",
    "META-INF/neoforge.mods.toml",
    "META-INF/mods.toml",
}


# ---------------------------------------------------------------------------
# profile loading


def list_profiles() -> list[str]:
    return sorted(p.stem for p in PROFILE_DIR.glob("*.json"))


def load_profile(name: str) -> dict:
    path = PROFILE_DIR / f"{name}.json"
    if not path.is_file():
        raise SystemExit(f"unknown profile {name!r}; available: {', '.join(list_profiles())}")
    profile = json.loads(path.read_text(encoding="utf-8"))
    profile["name"] = name
    profile.setdefault("output", f"targets/{name}/build/generated/resources/main")
    profile.setdefault("features", {})
    return profile


def feature(profile: dict, key: str, default=None):
    return profile["features"].get(key, default)


# ---------------------------------------------------------------------------
# small helpers


def read_json(path: Path):
    return json.loads(path.read_text(encoding="utf-8-sig"))


def dump_json(obj) -> bytes:
    """Canonical JSON: two-space indent, UTF-8, trailing newline, LF."""
    return (json.dumps(obj, indent=2, ensure_ascii=False) + "\n").encode("utf-8")


def rel(path: Path) -> str:
    return path.relative_to(COMMON_RESOURCES).as_posix()


def split_java_args(text: str) -> list[str]:
    """Split a Java argument list on top-level commas, keeping quoted strings intact."""
    args, current, quoted, escaped = [], [], False, False
    for ch in text:
        if quoted:
            current.append(ch)
            if escaped:
                escaped = False
            elif ch == "\\":
                escaped = True
            elif ch == '"':
                quoted = False
            continue
        if ch == '"':
            quoted = True
            current.append(ch)
        elif ch == ",":
            args.append("".join(current).strip())
            current = []
        else:
            current.append(ch)
    if current:
        args.append("".join(current).strip())
    return args


def unquote(value: str) -> str:
    value = value.strip()
    if len(value) >= 2 and value[0] == '"' and value[-1] == '"':
        return value[1:-1]
    return value


def id_path(namespaced: str) -> str:
    """``minecraft:stone`` -> ``stone`` (default namespace ``minecraft``)."""
    return namespaced.split(":", 1)[1] if ":" in namespaced else namespaced


# ---------------------------------------------------------------------------
# Java-backed catalogs (single source of truth for registry ids)


def _java(name: str) -> str:
    return (COMMON_JAVA / name).read_text(encoding="utf-8")


def registered_items() -> list[str]:
    src = _java("init/ChineseweaponsModItems.java")
    return re.findall(r'REGISTRY\.register\("([^"]+)"', src)


def registered_blocks() -> list[str]:
    src = _java("init/ChineseweaponsModBlocks.java")
    return re.findall(r'REGISTRY\.register\("([^"]+)"', src)


def armor_definitions() -> list[dict]:
    """Parse the shared armour table: id, repair material, texture and asset name."""
    src = _java("content/ArmorDefinitions.java")
    sets = []
    for body in re.findall(r"=\s*armor\(\s*(.*?)\);", src, re.S):
        args = split_java_args(body)
        if len(args) != 20:
            raise SystemExit(f"unexpected armor() arity: {len(args)} in {body[:80]!r}")
        sets.append({
            "id": unquote(args[0]),
            "material": unquote(args[1]),
            "texture": unquote(args[12]),
            "material_name": unquote(args[19]),
        })
    if len(sets) != 42:
        raise SystemExit(f"expected 42 armour definitions, parsed {len(sets)}")
    return sets


def horse_armor_items() -> list[dict]:
    src = _java("init/ChineseweaponsModItems.java")
    pattern = re.compile(r'REGISTRY\.register\("([^"]+)",\s*\(\) -> [\s\S]*?horseArmor\((\d+),\s*"([^"]+)"\)')
    return [
        {"id": item_id, "defense": int(defense), "texture": texture}
        for item_id, defense, texture in pattern.findall(src)
    ]


def repair_item(material: str) -> str:
    return {
        "iron": "minecraft:iron_ingot",
        "golden": "minecraft:gold_ingot",
        "diamond": "minecraft:diamond",
        "netherite": "minecraft:netherite_ingot",
    }[material]


# ---------------------------------------------------------------------------
# 1.21.1 / 26.1 shared data pack rewrites


def rename_key_in_place(obj: dict, old: str, new: str) -> dict:
    return {(new if key == old else key): value for key, value in obj.items()}


def holder_ingredient(value):
    """26.1 ingredients use a plain id string, tags use ``#namespace:path``."""
    if isinstance(value, list):
        return [holder_ingredient(entry) for entry in value]
    if isinstance(value, dict):
        if "item" in value:
            return value["item"]
        if "tag" in value:
            return "#" + value["tag"]
        raise SystemExit(f"unsupported ingredient shape: {value!r}")
    return value


def result_1211(result: dict) -> dict:
    """1.21.1 vanilla recipe results: ``count`` first, then the renamed ``id``.

    The custom weapon casting serializer keeps the legacy ``{"item": ..., "count": ...}``
    shape (see the target's ``WCTRecipe.RESULT_CODEC``), so it is not rewritten here.
    """
    out = {}
    if "count" in result:
        out["count"] = result["count"]
    if "item" in result:
        out["id"] = result["item"]
    elif "id" in result:
        out["id"] = result["id"]
    for key, value in result.items():
        if key not in ("count", "item", "id"):
            out[key] = value
    return out


def result_261(result: dict) -> dict:
    """26.1 results are ``ItemStackTemplate`` objects ordered id, count."""
    out = {}
    if "item" in result:
        out["id"] = result["item"]
    elif "id" in result:
        out["id"] = result["id"]
    for key in ("count", "components"):
        if key in result:
            out[key] = result[key]
    return out


def transform_recipe(recipe: dict, profile: dict) -> dict:
    family = profile["family"]
    recipe_type = recipe.get("type", "")
    if family == "1.21.1":
        if recipe_type == "minecraft:crafting_shaped" or recipe_type == "minecraft:smithing_transform" \
                or recipe_type == "minecraft:crafting_shapeless":
            if isinstance(recipe.get("result"), dict):
                recipe["result"] = result_1211(recipe["result"])
        return recipe
    # 26.1: every ingredient becomes a holder-set string, results become templates.
    for key in ("main_material", "template", "base", "addition"):
        if key in recipe:
            recipe[key] = holder_ingredient(recipe[key])
    if "ingredients" in recipe:
        recipe["ingredients"] = holder_ingredient(recipe["ingredients"])
    if "key" in recipe:
        recipe["key"] = {symbol: holder_ingredient(value) for symbol, value in recipe["key"].items()}
    if isinstance(recipe.get("result"), dict):
        recipe["result"] = result_261(recipe["result"])
    return recipe


def unwrap_conditional_recipe(recipe: dict, profile: dict) -> dict:
    """Converts the 1.20.1 ``forge:conditional`` book recipe into a NeoForge conditional recipe."""
    entry = recipe.get("recipes", [{}])[0]
    inner = entry.get("recipe", {})
    conditions = entry.get("conditions", [])
    converted_conditions = []
    for condition in conditions:
        condition = dict(condition)
        if condition.get("type") == "forge:mod_loaded":
            condition["type"] = "neoforge:mod_loaded"
        converted_conditions.append(condition)
    out = {"type": inner.get("type", "minecraft:crafting_shapeless")}
    out["neoforge:conditions"] = converted_conditions
    for key, value in inner.items():
        if key == "type":
            continue
        if key == "result" and isinstance(value, dict):
            result = result_1211(value)
            if "nbt" in result:
                result = rename_key_in_place(result, "nbt", "components")
            out[key] = result
        else:
            out[key] = value
    return out


def transform_advancement(advancement: dict, profile: dict) -> dict:
    display = advancement.get("display")
    if isinstance(display, dict):
        icon = display.get("icon")
        if isinstance(icon, dict) and "item" in icon:
            display["icon"] = rename_key_in_place(icon, "item", "id")
        if profile["family"] == "26.1":
            background = display.get("background")
            if isinstance(background, str):
                display["background"] = re.sub(r":textures/(.+)\.png$", r":\1", background)
    return advancement


def enchantment_predicate(entries: list) -> list:
    return [
        {"enchantments": entry.get("enchantment", entry.get("enchantments")), "levels": entry["levels"]}
        for entry in entries
    ]


def transform_loot_table(table: dict, profile: dict) -> dict:
    def walk(node):
        if isinstance(node, dict):
            if node.get("condition") == "minecraft:match_tool" and isinstance(node.get("predicate"), dict):
                predicate = node["predicate"]
                if "enchantments" in predicate:
                    inner = {"minecraft:enchantments": enchantment_predicate(predicate["enchantments"])}
                    if profile["family"] == "26.1" and feature(profile, "lootPredicateComponents"):
                        node["predicate"] = {"components": {"predicates": inner}}
                    else:
                        node["predicate"] = {"predicates": inner}
            for value in node.values():
                walk(value)
        elif isinstance(node, list):
            for value in node:
                walk(value)

    walk(table)
    return table


def qualify_tag_values(tag: dict) -> dict:
    values = []
    for value in tag.get("values", []):
        if isinstance(value, str):
            bare = value.lstrip("#")
            if ":" not in bare:
                value = value[: len(value) - len(bare)] + f"{MOD_ID}:{bare}"
        values.append(value)
    if "values" in tag:
        tag["values"] = values
    return tag


def transform_worldgen_configured_feature(name: str, feature_json: dict, profile: dict) -> dict:
    for target in feature_json.get("config", {}).get("targets", []):
        target_def = target.get("target")
        if not isinstance(target_def, dict):
            continue
        if profile["family"] == "26.1":
            if feature(profile, "namespacedPredicateType"):
                target_def["predicate_type"] = "minecraft:tag_match"
            target_def["tag"] = (
                "minecraft:deepslate_ore_replaceables" if name.startswith("deepslate")
                else "minecraft:stone_ore_replaceables"
            )
        else:
            target_def["tag"] = feature(profile, "worldgenStoneTag", "minecraft:stone_ore_replaceables")
    return feature_json


# ---------------------------------------------------------------------------
# 1.21.1 item models


def separate_transform_models(profile: dict) -> dict[str, bytes]:
    out = {}
    for path in sorted((COMMON_RESOURCES / MOD_ASSETS / "models" / "item").glob("*.json")):
        model = read_json(path)
        if model.get("loader") != "forge:separate_transforms":
            continue
        base = model.get("base", {})
        if profile["loader"] == "neoforge":
            model["loader"] = "neoforge:separate_transforms"
            out[f"{MOD_ASSETS}/models/item/{path.name}"] = dump_json(model)
        else:
            # Fabric has no separate_transforms loader: flatten to the base model.
            flattened = {"gui_light": model.get("gui_light", "front"), "parent": base["parent"]}
            if "textures" in base:
                flattened["textures"] = base["textures"]
            out[f"{MOD_ASSETS}/models/item/{path.name}"] = dump_json(flattened)
    return out


# ---------------------------------------------------------------------------
# 26.1 item definitions, models, equipment and textures


def item_definition(item_id: str, model_path: str, profile: dict) -> bytes:
    model = read_json(model_path)
    item_model = f"{MOD_ID}:item/{item_id}"
    if item_id in registered_blocks():
        return dump_json({"model": {"type": "minecraft:model", "model": model.get("parent", item_model)}})
    if model.get("loader") == "forge:separate_transforms":
        base = model.get("base", {})
        gui = model.get("perspectives", {}).get("gui", {})
        if profile["loader"] == "neoforge":
            fallback = f"{MOD_ID}:item/{item_id}_hand"
        elif "textures" in base:
            fallback = f"{MOD_ID}:item/{item_id}_3d"
        else:
            fallback = base["parent"]
        return dump_json({
            "model": {
                "type": "minecraft:select",
                "property": "minecraft:display_context",
                "cases": [{
                    "when": "gui",
                    "model": {"type": "minecraft:model", "model": f"{MOD_ID}:item/{item_id}_gui"},
                }],
                "fallback": {"type": "minecraft:model", "model": fallback},
            }
        })
    if "overrides" in model and str(model.get("parent", "")).startswith(f"{MOD_ID}:custom/"):
        base = model["parent"]
        false_model = base if feature(profile, "shieldBaseModel") == "custom" else item_model
        return dump_json({
            "model": {
                "type": "minecraft:condition",
                "property": "minecraft:using_item",
                "on_true": {"type": "minecraft:model", "model": f"{base}_on"},
                "on_false": {"type": "minecraft:model", "model": false_model},
            }
        })
    return dump_json({"model": {"type": "minecraft:model", "model": item_model}})


def weapon_models(profile: dict) -> dict[str, bytes]:
    out = {}
    for item_id in registered_items():
        model_path = COMMON_RESOURCES / MOD_ASSETS / "models" / "item" / f"{item_id}.json"
        if not model_path.is_file():
            raise SystemExit(f"missing common item model for registered item {item_id}")
        model = read_json(model_path)
        if model.get("loader") != "forge:separate_transforms":
            continue
        base = model.get("base", {})
        gui = model.get("perspectives", {}).get("gui", {})
        out[f"{MOD_ASSETS}/models/item/{item_id}_gui.json"] = dump_json(gui)
        if profile["loader"] == "neoforge":
            hand = {"gui_light": model.get("gui_light", "front"), "parent": base["parent"]}
            if "textures" in base:
                hand["textures"] = base["textures"]
            out[f"{MOD_ASSETS}/models/item/{item_id}_hand.json"] = dump_json(hand)
        elif "textures" in base:
            inline = {"parent": base["parent"], "textures": base["textures"]}
            out[f"{MOD_ASSETS}/models/item/{item_id}_3d.json"] = dump_json(inline)
    return out


def equipment_asset_name(definition: dict, profile: dict) -> str:
    if profile["loader"] == "neoforge":
        if definition["id"].endswith("_bohai_helmet"):
            return definition["id"]
        return definition["material_name"]
    name = definition["id"]
    if name.endswith("_face_guard_helmet"):
        name = name[: -len("_helmet")]
    return name


def equipment_assets(profile: dict) -> dict[str, bytes]:
    out = {}
    for definition in armor_definitions():
        asset = equipment_asset_name(definition, profile)
        texture = f"{MOD_ID}:{asset}"
        layers = {"humanoid": [{"texture": texture}]}
        if feature(profile, "humanoidBabyLayer"):
            layers["humanoid_baby"] = [{"texture": texture}]
        layers["humanoid_leggings"] = [{"texture": texture}]
        out[f"{MOD_ASSETS}/equipment/{asset}.json"] = dump_json({"layers": layers})
    for horse in horse_armor_items():
        if profile["loader"] == "neoforge":
            asset = f"horse_armor_{horse['defense']}"
        else:
            asset = Path(horse["texture"]).stem
        out[f"{MOD_ASSETS}/equipment/{asset}.json"] = dump_json(
            {"layers": {"horse_body": [{"texture": f"{MOD_ID}:{asset}"}]}}
        )
    return out


def equipment_textures(profile: dict) -> dict[str, bytes]:
    out = {}
    for definition in armor_definitions():
        source = COMMON_RESOURCES / MOD_ASSETS / id_path(definition["texture"])
        if not source.is_file():
            raise SystemExit(f"missing armour texture {definition['texture']}")
        name = equipment_asset_name(definition, profile) + source.suffix
        data = source.read_bytes()
        out[f"{MOD_ASSETS}/textures/entity/equipment/humanoid/{name}"] = data
        out[f"{MOD_ASSETS}/textures/entity/equipment/humanoid_leggings/{name}"] = data
    for horse in horse_armor_items():
        source = COMMON_RESOURCES / MOD_ASSETS / id_path(horse["texture"])
        if profile["loader"] == "neoforge":
            name = f"horse_armor_{horse['defense']}{source.suffix}"
        else:
            name = source.name
        out[f"{MOD_ASSETS}/textures/entity/equipment/horse_body/{name}"] = source.read_bytes()
    return out


def armor_repair_tags(profile: dict) -> dict[str, bytes]:
    if not feature(profile, "armorRepairTags"):
        return {}
    out = {}
    for definition in armor_definitions():
        asset = equipment_asset_name(definition, profile)
        out[f"{MOD_DATA}/tags/item/armor_repair/{asset}.json"] = dump_json(
            {"values": [repair_item(definition["material"])]}
        )
    return out


# ---------------------------------------------------------------------------
# bundle assembly


def collect_data_files(profile: dict) -> dict[str, bytes]:
    out = {}
    family = profile["family"]
    for path in sorted((COMMON_RESOURCES / MOD_DATA / "recipes").glob("*.json")):
        name = path.stem
        if family == "1.21.1" and name == "forging_notes":
            if feature(profile, "conditionalRecipe") == "neoforge":
                out[f"{MOD_DATA}/recipe/{name}.json"] = dump_json(unwrap_conditional_recipe(read_json(path), profile))
            continue
        if name == "dyeable_armor" and not feature(profile, "dyeableArmorRecipe"):
            continue
        if name == "forging_notes":
            continue
        out[f"{MOD_DATA}/recipe/{name}.json"] = dump_json(transform_recipe(read_json(path), profile))
    for path in sorted((COMMON_RESOURCES / MOD_DATA / "advancements").rglob("*.json")):
        name = path.relative_to(COMMON_RESOURCES / MOD_DATA / "advancements").as_posix()
        out[f"{MOD_DATA}/advancement/{name}"] = dump_json(transform_advancement(read_json(path), profile))
    for path in sorted((COMMON_RESOURCES / MOD_DATA / "loot_tables").rglob("*.json")):
        name = path.relative_to(COMMON_RESOURCES / MOD_DATA / "loot_tables").as_posix()
        if name == "blocks/armorcastingtable.json":
            continue  # legacy 1.20.1 block id, not registered in modern versions
        out[f"{MOD_DATA}/loot_table/{name}"] = dump_json(transform_loot_table(read_json(path), profile))
    for path in sorted((COMMON_RESOURCES / MOD_DATA / "tags" / "items").rglob("*.json")):
        name = path.relative_to(COMMON_RESOURCES / MOD_DATA / "tags" / "items").as_posix()
        out[f"{MOD_DATA}/tags/item/{name}"] = dump_json(qualify_tag_values(read_json(path)))
    for path in sorted((COMMON_RESOURCES / "data" / "minecraft" / "tags").rglob("*.json")):
        name = path.relative_to(COMMON_RESOURCES / "data" / "minecraft" / "tags").as_posix()
        if name.startswith("blocks/"):
            name = "block/" + name[len("blocks/"):]
        elif name.startswith("items/"):
            name = "item/" + name[len("items/"):]
        if name == "item/netherite_ignores_lava.json" and not feature(profile, "netheriteIgnoresLavaTag"):
            continue
        out[f"data/minecraft/tags/{name}"] = dump_json(qualify_tag_values(read_json(path)))
    for path in sorted((COMMON_RESOURCES / MOD_DATA / "worldgen" / "configured_feature").glob("*.json")):
        out[f"{MOD_DATA}/worldgen/configured_feature/{path.name}"] = dump_json(
            transform_worldgen_configured_feature(path.stem, read_json(path), profile)
        )
    return out


def collect_loader_extras(profile: dict) -> dict[str, bytes]:
    out = {}
    family = profile["family"]
    if family == "1.21.1":
        if feature(profile, "curiosTag"):
            path = COMMON_RESOURCES / MOD_DATA / "curios" / "tags" / "items" / "head.json"
            out["data/curios/tags/item/head.json"] = dump_json(qualify_tag_values(read_json(path)))
    if feature(profile, "biomeModifier") == "neoforge":
        strip = "_biome_modifier" if feature(profile, "biomeModifierStripSuffix") else ""
        for path in sorted((COMMON_RESOURCES / MOD_DATA / "forge" / "biome_modifier").glob("*.json")):
            modifier = read_json(path)
            modifier["type"] = "neoforge:add_features"
            modifier["biomes"] = "#minecraft:is_overworld"
            name = path.stem[: -len(strip)] if strip and path.stem.endswith(strip) else path.stem
            out[f"{MOD_DATA}/neoforge/biome_modifier/{name}.json"] = dump_json(modifier)
    if feature(profile, "emitPlacedFeatures"):
        for path in sorted((COMMON_RESOURCES / MOD_DATA / "worldgen" / "placed_feature").glob("*.json")):
            out[f"{MOD_DATA}/worldgen/placed_feature/{path.name}"] = dump_json(read_json(path))
    for name, payload in profile.get("extraFiles", {}).items():
        out[name] = dump_json(payload)
    return out


def generate(profile: dict) -> dict[str, bytes]:
    family = profile["family"]
    out = collect_data_files(profile)
    out.update(collect_loader_extras(profile))
    if family == "1.21.1":
        out.update(separate_transform_models(profile))
    elif family == "26.1":
        for item_id in registered_items():
            model_path = COMMON_RESOURCES / MOD_ASSETS / "models" / "item" / f"{item_id}.json"
            if not model_path.is_file():
                raise SystemExit(f"missing common item model for registered item {item_id}")
            out[f"{MOD_ASSETS}/items/{item_id}.json"] = item_definition(item_id, model_path, profile)
        for block_id in registered_blocks():
            model_path = COMMON_RESOURCES / MOD_ASSETS / "models" / "item" / f"{block_id}.json"
            out[f"{MOD_ASSETS}/items/{block_id}.json"] = item_definition(block_id, model_path, profile)
        out.update(weapon_models(profile))
        out.update(equipment_assets(profile))
        out.update(equipment_textures(profile))
        out.update(armor_repair_tags(profile))
    else:
        raise SystemExit(f"unsupported profile family {family!r}")
    return out


def write_tree(files: dict[str, bytes], output: Path) -> None:
    if output.exists():
        shutil.rmtree(output)
    for name, data in files.items():
        target = output / name
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(data)


# ---------------------------------------------------------------------------
# golden comparison


def is_hand_owned(name: str) -> bool:
    return name in HAND_OWNED or name.startswith("assets/minecraft/models/item/shield")


def walk_tree(base: Path) -> dict[str, Path]:
    result = {}
    for path in base.rglob("*"):
        if path.is_file():
            result[path.relative_to(base).as_posix()] = path
    return result


def check_against(files: dict[str, bytes], golden: Path, profile: dict) -> int:
    golden_files = walk_tree(golden)
    generated = set(files)
    expected = {name for name in golden_files if not is_hand_owned(name)}
    missing = sorted(expected - generated)
    extra = sorted(generated - expected)
    semantic = []
    byte = []
    for name in sorted(generated & expected):
        ours = files[name]
        theirs = golden_files[name].read_bytes()
        if ours == theirs:
            continue
        try:
            if json.loads(ours.decode("utf-8-sig")) == json.loads(theirs.decode("utf-8-sig")):
                byte.append(name)
                continue
        except (json.JSONDecodeError, UnicodeDecodeError):
            pass
        semantic.append(name)
    print(f"[{profile['name']}] golden files={len(expected)} generated={len(generated)}")
    print(f"  missing={len(missing)} extra={len(extra)} semantic-mismatch={len(semantic)} formatting-only={len(byte)}")
    for name in missing:
        print("   MISSING  ", name)
    for name in extra:
        print("   EXTRA    ", name)
    for name in semantic:
        print("   DIFF     ", name)
    if byte:
        print("   formatting-only differences:")
        for name in byte:
            print("     ", name)
    return 1 if (missing or extra or semantic) else 0


def manifest_for(files: dict[str, bytes]) -> dict:
    import hashlib

    entries = {}
    for name, data in files.items():
        if is_hand_owned(name):
            continue
        if name.endswith(".json"):
            try:
                obj = json.loads(data.decode("utf-8-sig"))
                payload = json.dumps(obj, sort_keys=True, separators=(",", ":"), ensure_ascii=False).encode()
            except json.JSONDecodeError:
                payload = data
        else:
            payload = data
        entries[name] = hashlib.sha256(payload).hexdigest()
    return {"files": dict(sorted(entries.items()))}


def manifest_path(profile: dict) -> Path:
    return SCRIPT_DIR / "resource-golden" / f"{profile['name']}.json"


def check_manifest(files: dict[str, bytes], profile: dict) -> int:
    path = manifest_path(profile)
    if not path.is_file():
        print(f"[{profile['name']}] no golden manifest at {path}")
        return 1
    manifest = json.loads(path.read_text(encoding="utf-8"))["files"]
    current = manifest_for(files)["files"]
    missing = sorted(set(manifest) - set(current))
    extra = sorted(set(current) - set(manifest))
    changed = sorted(name for name in set(manifest) & set(current) if manifest[name] != current[name])
    print(f"[{profile['name']}] golden manifest {path.name}: {len(manifest)} files")
    print(f"  missing={len(missing)} extra={len(extra)} changed={len(changed)}")
    for name in missing:
        print("   MISSING  ", name)
    for name in extra:
        print("   EXTRA    ", name)
    for name in changed:
        print("   CHANGED  ", name)
    return 1 if (missing or extra or changed) else 0


def main(argv: list[str]) -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--profile", action="append", default=[], help="profile name (repeatable)")
    parser.add_argument("--all", action="store_true", help="generate every profile")
    parser.add_argument("--output", help="override the profile output directory")
    parser.add_argument("--check", metavar="DIR", help="compare the generated tree with a golden directory")
    parser.add_argument("--write-golden", metavar="DIR",
                        help="write scripts/resource-golden/<profile>.json from DIR (one-time fixture refresh)")
    parser.add_argument("--check-golden", action="store_true", help="compare against the committed golden manifest")
    parser.add_argument("--list", action="store_true", help="list profiles")
    args = parser.parse_args(argv)

    if args.list:
        for name in list_profiles():
            print(name)
        return 0

    names = list_profiles() if args.all else args.profile
    if not names:
        parser.error("pass --profile NAME, --all or --list")
    if args.output and len(names) > 1:
        parser.error("--output can only be used with a single --profile")

    status = 0
    for name in names:
        profile = load_profile(name)
        files = generate(profile)
        if args.write_golden:
            golden_tree = walk_tree(Path(args.write_golden))
            manifest = manifest_for({name: path.read_bytes() for name, path in golden_tree.items()})
            out = manifest_path(profile)
            out.parent.mkdir(parents=True, exist_ok=True)
            out.write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")
            print(f"[{name}] wrote golden manifest {out} ({len(manifest['files'])} files)")
            continue
        output = Path(args.output) if args.output else REPO_ROOT / profile["output"]
        write_tree(files, output)
        print(f"[{name}] generated {len(files)} files -> {output}")
        if args.check:
            status |= check_against(files, Path(args.check), profile)
        if args.check_golden:
            status |= check_manifest(files, profile)
    return status


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
