import os
import json

# Epic Fight
EPICFIGHT_TYPES = [
    ("axe", "斧（单手）"),
    ("fist", "拳（双持）"),
    ("hoe", "锄（单手）"),
    ("pickaxe", "镐（单手）"),
    ("shovel", "锹（单手）"),
    ("sword", "剑（双持）"),
    ("spear", "矛（单/双手）"),
    ("greatsword", "大剑（双手）"),
    ("uchigatana", "打刀（双手）"),
    ("tachi", "太刀（双手）"),
    ("longsword", "长剑（双手）"),
    ("dagger", "匕首（双持）"),
    ("bow", "弓（双手）"),
    ("crossbow", "弩（双手）"),
    ("trident", "三叉戟（单手）"),
    ("shield", "盾（双持）"),
]

# BetterCombat
BETTERCOMBAT_PRESETS = [
    ("anchor", "锚"),
    ("axe", "斧"),
    ("battlestaff", "战棍"),
    ("claw", "爪"),
    ("claymore", "双手大剑"),
    ("coral_blade", "珊瑚刃"),
    ("cutlass", "弯刀"),
    ("dagger", "匕首"),
    ("double_axe", "双刃斧"),
    ("fist", "拳套"),
    ("glaive", "长刀/关刀"),
    ("halberd", "戟"),
    ("hammer", "锤"),
    ("heavy_axe", "重斧"),
    ("katana", "武士刀"),
    ("lance", "骑枪"),
    ("mace", "钉头锤"),
    ("pickaxe", "镐"),
    ("rapier", "刺剑"),
    ("scythe", "镰刀"),
    ("sickle", "镰"),
    ("soul_knife", "魂刃"),
    ("spear", "矛"),
    ("staff", "法杖"),
    ("sword", "剑"),
    ("trident", "三叉戟"),
    ("twin_blade", "双刃"),
    ("wand", "魔杖"),
]

MATERIALS = ["wooden", "stone", "golden", "iron", "diamond", "netherite"]


def choose_one(title, options):
    print(f"\n=== {title} ===")
    for i, (key, cn) in enumerate(options, 1):
        print(f"  {i}. {key}  ({cn})")
    while True:
        raw = input("请输入序号: ").strip()
        if raw.isdigit() and 1 <= int(raw) <= len(options):
            return options[int(raw) - 1][0]
        print("无效输入，请重新输入")


def choose_mode():
    print("\nEpic Fight or BetterCombat 超级无敌宇宙霹雳适配(bushi")
    print("  1. 全部生成")
    print("  2. 仅生成 Epic Fight")
    print("  3. 仅生成 BetterCombat")
    while True:
        raw = input("请输入序号: ").strip()
        if raw in ("1", "2", "3"):
            return raw
        print("无效输入，请重新输入")


def build_epicfight_json(type_name, is_spear=False):
    if is_spear:
        data = {
            "type": f"epicfight:{type_name}",
            "attributes": {
                "one_hand": {
                    "armor_negation": 8.0,
                    "impact": 1.9,
                    "max_strikes": 1
                },
                "two_hand": {
                    "armor_negation": 0.0,
                    "impact": 1.3,
                    "max_strikes": 3
                }
            }
        }
    else:
        data = {
            "type": f"epicfight:{type_name}",
            "attributes": {
                "common": {
                    "armor_negation": 0.0,
                    "impact": 1.1,
                    "max_strikes": 1
                }
            }
        }
    return json.dumps(data, indent=2, ensure_ascii=False)


def build_bettercombat_json(parent_name):
    data = {
        "parent": f"bettercombat:{parent_name}"
    }
    return json.dumps(data, indent=2, ensure_ascii=False)


def main():
    mode = choose_mode()

    item_name = input("\n请输入通用物品名: ").strip()
    if not item_name:
        print("物品名不能为空")
        return

    epic_type = None
    bc_parent = None

    if mode in ("1", "2"):
        epic_type = choose_one("请选择 Epic Fight 动画 (type)", EPICFIGHT_TYPES)
    if mode in ("1", "3"):
        bc_parent = choose_one("请选择 BetterCombat 动画 (parent)", BETTERCOMBAT_PRESETS)

    base_dir = os.getcwd()
    bc_dir = os.path.join(base_dir, "weapon_attributes")
    ef_dir = os.path.join(base_dir, "capabilities", "weapons")

    if mode in ("1", "3"):
        os.makedirs(bc_dir, exist_ok=True)
    if mode in ("1", "2"):
        os.makedirs(ef_dir, exist_ok=True)

    for mat in MATERIALS:
        full_name = f"{mat}_{item_name}.json"

        if mode in ("1", "2"):
            ef_path = os.path.join(ef_dir, full_name)
            with open(ef_path, "w", encoding="utf-8") as f:
                f.write(build_epicfight_json(epic_type, is_spear=(epic_type == "spear")))
            print(f"[Epic Fight] 已生成: {ef_path}")

        if mode in ("1", "3"):
            bc_path = os.path.join(bc_dir, full_name)
            with open(bc_path, "w", encoding="utf-8") as f:
                f.write(build_bettercombat_json(bc_parent))
            print(f"[BetterCombat] 已生成: {bc_path}")

    print("\n全部完成！")
    print(f" - BetterCombat 目录: {bc_dir}")
    print(f" - Epic Fight 目录: {ef_dir}")


if __name__ == "__main__":
    main()