# Chinese Weapons 原始单版本功能清单

> 基线：git commit `3010fe3`（`up 1.2.1.4 qv<`，迁移前最后一个 Forge 单版本提交）。
> 本文只依据该提交的 `src/main/java`（153 个 Java 文件）与 `src/main/resources`（956 个资源文件）静态阅读整理，**没有启动游戏、没有执行 AutoJS 断言**。文中所有“可测试断言”按源码数值设计，是否通过仍需在运行环境验证。
> 本文是文档任务，未修改任何 Java、资源或构建文件。

## 0. 阅读约定

- `{mat}` 在护甲/武器 id 中表示 `iron`、`golden`、`diamond`、`netherite` 之一。
- `{slot}` 表示 `helmet`、`chestplate`、`leggings`、`boots`。
- “攻击伤害”指物品栏提示中显示的最终值，已包含基础 1 点、Tier 加成和物品自身的 `attackDamageModifier`。
- “攻击速度”指最终值（`4.0 + attackSpeedModifier`），单位为次/秒。
- “耐久”指 `getMaxDamage` 的最终值。
- 源码路径均相对于 `3010fe3` 树，例如 `src/main/java/net/mirrorloong/chineseweapons/item/GuDuoItem.java:40-49`。
- 文中“分析发现”仅表示静态阅读结论，不是已修复项。

---

## 1. 总览

### 1.1 版本与入口

| 项目 | 值 |
|---|---|
| Minecraft | 1.20.1 |
| Loader | Forge 47.4.20（`forge_version_range=[47,)`） |
| Java | 17（`build.gradle` toolchain） |
| mod id | `chineseweapons` |
| group / package | `net.mirrorloong.chineseweapons` |
| mod 版本 | 1.2.1.4（`gradle.properties`）；`src/main/resources/META-INF/MANIFEST.MF` 里仍写 1.1.7，构建时会被 Gradle manifest 覆盖，见缺陷 D22 |
| 资源包格式 | `pack_format: 15` |
| 入口类 | `ChineseweaponsMod`（`@Mod(ChineseweaponsMod.MODID)`） |
| 入口构造 | `ChineseweaponsMod(FMLJavaModLoadingContext)`，把各个 `DeferredRegister` 挂到 mod event bus，并注册 `CHANNEL` 网络消息 |
| 客户端入口 | 无独立 client 主类；客户端逻辑由 `Dist.CLIENT` 的 `@Mod.EventBusSubscriber` 与 `initializeClient` 分散注册 |

`ChineseweaponsMod` 的注册顺序（`ChineseweaponsMod.java:69-88`）：

1. `DeferredRecipe.DeferredRecipeSer`（配方序列化器）
2. `DeferredRecipe.DeferredRecipe`（配方类型）
3. `DeferredGui.Deferred`（菜单类型）
4. `ChineseweaponsModBlocks.REGISTRY`
5. `ChineseweaponsModItems.REGISTRY`
6. `ChineseweaponsModTabs.REGISTRY`
7. `ChineseWeaponsModEffects.REGISTER`
8. `ChineseWeaponsModEnchantments.REGISTRY`（该注册表没有任何条目）
9. `ChineseweaponsModEntities.ENTITIES`
10. `ChineseweaponsMod.SOUNDS`
11. `ChineseweaponsMod.RECIPE_SERIALIZERS`（染料护甲特殊配方）
12. `ChineseweaponsModBlockEntities.REGISTRY`
13. `bus.addListener(ChineseweaponsMod::network)` 注册三个主通道消息
14. 仅客户端：`registerKeys` 注册 J 键

### 1.2 注册表与数量

| 注册表 | 类型 | 数量 | 备注 |
|---|---|---:|---|
| `minecraft:item` | Item | **202** | 198 个 `ChineseweaponsModItems` 直接注册 + 4 个 `BlockItem`（方块 id 同名） |
| `minecraft:block` | Block | **4** | 锻造台、远古宝石矿、深层远古宝石矿、远古宝石块 |
| `minecraft:block_entity_type` | BlockEntityType | **1** | `chineseweapons:armor_casting_table` |
| `minecraft:entity_type` | EntityType | **2** | `song_javelin`、`standing_shield` |
| `minecraft:mob_effect` | MobEffect | **2** | `bleed`、`cuts` |
| `minecraft:enchantment` | Enchantment | **0** | 预留的 `cavalry_buster_star` 被注释，未注册 |
| `minecraft:sound_event` | SoundEvent | **1** | `guduo` |
| `minecraft:recipe_type` | RecipeType | **1** | `weapon_casting_shaped` |
| `minecraft:recipe_serializer` | RecipeSerializer | **2** | `weapon_casting_shaped`、`crafting_special_dyeablearmor` |
| `minecraft:menu` | MenuType | **1** | `chineseweapons:gui.chineseweapons.weapons_casting_table`（id 中带点，属于非常规命名） |
| `minecraft:creative_mode_tab` | CreativeModeTab | **1** | `chineseweapons:chinese_weapons`，共 202 个条目 |

### 1.3 依赖

`build.gradle` 与 `META-INF/mods.toml` 声明的依赖：

| 依赖 | 版本/范围 | 作用域 | 性质 |
|---|---|---|---|
| Forge | `[47,)` | BOTH | 必需 |
| Minecraft | `[1.20.1,1.21)` | BOTH | 必需 |
| JEI | `15.21.0.148`（mods.toml 可选 `15.12.2,`） | BOTH | 可选；编译期 + 运行期 |
| KubeJS | `2001.6.5-build.26`（mods.toml `[2001.6.3,)`） | BOTH | 可选；编译期 + 运行期 |
| Rhino | `2001.2.3-build.10` | 运行期 | KubeJS 前置，仅 runtimeOnly |
| Architectury API | `9.2.14` | 运行期 | KubeJS 前置，仅 runtimeOnly |
| EMI | `1.1.24+1.20.1`（mods.toml `[1.1.0,)`） | CLIENT | 可选；compileOnly，运行时由玩家提供 |
| Patchouli | `1.20.1-84-FORGE`（mods.toml `[1.20.1-81-FORGE,)`） | BOTH | 可选；编译期 + 运行期 |
| Curios | 5.14.1（已注释） | — | **没有代码/依赖**；只有数据文件 `data/chineseweapons/curios/tags/items/head.json` |
| Better Combat | 无声明 | — | 只有 37 个 `weapon_attributes` 数据文件 |
| Epic Fight | 无声明 | — | 只有 37 个 `capabilities/weapons` 数据文件 |

`README_CN.md` 说明该版本为 Forge 1.20.1；`KUBEJS_COMPAT.md` 给出 KubeJS 使用说明（技能概率、攻击距离、配方 schema）。

### 1.4 客户端/服务端拆分

**通用/服务端主导：**

- `ChineseweaponsMod`：注册、网络、服务器 tick 队列。
- `init/*`：方块/物品/实体/效果/标签页/交易等注册。
- `block/*`、`block/ArmorCastingTableBlockEntity`：锻造台逻辑、配方匹配、进度、掉落。
- `recipes/*`、`network/*`（处理端）、`procedures/*`（战斗与规则）。
- `entity/*`：实体逻辑；但 `SongStandingShieldEntity.tick` 违反拆分，见 D01。

**客户端专用（`Dist.CLIENT` / `@OnlyIn` / `initializeClient`）：**

- `client/ChineseWeaponsClientEvents`：盾牌 `blocking` 物品属性、可染色护甲的 item color。
- `client/GlaiveClientEvents`、`client/GlaiveSpinAnimationHandler`、`client/GlaiveItemClientEvent`：戟旋转与突刺第一人称动画。
- `client/TangDynastyHengSaberClientEvents`：横刀突刺第一人称动画。
- `client/ArmorCastingTableBlockEntityCache`：按 `BlockPos` 缓存进度/是否有配方。
- `client/model/*`：12 个护甲模型 + 2 个实体模型。
- `init/ChineseweaponsModModels`：注册 14 个 `LayerDefinition`。
- `init/ChineseweaponsModEvents`：注册 2 个实体渲染器。
- `gui/recipe/WeaponsCastingTableScreen`：锻造台界面。
- `renderer/SongJavelinRenderer`、`renderer/SongStandingShieldRenderer`：实体渲染。
- `procedures/tips`：未安装 Patchouli 时的客户端提示。
- `SongJavelinItem.ClientExtensions`、`GuDuoItem.Client`、各护甲 `initializeClient`：第一人称/模型/压扁渲染。

**跨端但只在客户端执行的逻辑：**

- `GlaiveItemGetLostKillProcedure.tryStart` 在客户端发送 `GlaiveSpinStartedEvent` 并直接返回；服务端负责真实伤害。
- `GlaiveItemStopRunProcedure.getAnimationProgress`、`TangDynastyHengSabeCutsProcedures.getAnimationProgress` 由客户端 `RenderHandEvent` 读取静态 map。
- `WeaponsCastingTableTypeGuiMenu` 被标注为 `value = Dist.CLIENT`，但它同时被服务端 `ArmorcastingtableBlock.use` 构造，存在类加载风险（D32）；`stillValid` 永远 true（D03）。

---

## 2. 物品

### 2.1 物品族总表

| 族 | 条目数 | 说明 |
|---|---:|---|
| 护甲套件 | **132** | 7 个四材质套装 ×4 材质 ×4 件 = 112；唐/宋步兵甲各 4 件 = 8；睚眦/渤海/面甲头盔各 4 材质 = 12 |
| 近战武器 | **37** | 戈 6、戟 7、横刀 6、陌刀（Sky-Piercing Halberd）6、宋长矛 6、骨朵 6 |
| 盾 | **2** | 唐盾、宋立盾 |
| 马铠 | **4** | 铁/金/钻石/下界合金唐马铠 |
| 甲片/材料 | **14** | 铁/金/钻石甲片与甲片板、山文甲片与板、石甲片与板等 |
| 宝石 | **1** | `ancient_gem` |
| 锻石 | **8** | 8 个套装锻石 |
| 方块物品 | **4** | 锻造台、两种矿、宝石块 |
| 合计 | **202** | 与创造标签页条目数一致（`ChineseweaponsModTabs.java` 有 202 个 `tabData.accept`） |

### 2.2 护甲材料模板

所有可四材质套装（山文、早期/中期/晚期明光、陌刀步兵、黑锤、细鳞）共用以下材料模板；唐/宋步兵甲只用铁；睚眦/渤海头盔使用同模板的头盔行；面甲头盔有独立护甲值。

| 材质 | 护甲值（头/胸/腿/靴） | 耐久倍率 | 耐久（头/胸/腿/靴） | 韧性 | 击退抗性 | 附魔 | 修复材料 |
|---|---|---:|---|---:|---:|---:|---|
| 铁 `iron` | 3 / 6 / 7 / 3 | 16 | 208 / 240 / 256 / 176 | 0 | 0 | 10 | `minecraft:iron_ingot` |
| 金 `golden` | 2 / 4 / 6 / 3 | 15 | 195 / 225 / 240 / 165 | 1 | 0 | 26 | `minecraft:gold_ingot` |
| 钻石 `diamond` | 2 / 7 / 9 / 4 | 34 | 442 / 510 / 544 / 374 | 2 | 0 | 11 | `minecraft:diamond` |
| 下界合金 `netherite` | 2 / 7 / 9 / 4 | 38 | 494 / 570 / 608 / 418 | 3 | 0.1 | 16 | `minecraft:netherite_ingot` |

**覆盖值：**

| 套装/部件 | 铁 | 金 | 钻石 | 下界合金 |
|---|---|---|---|---|
| 陌刀步兵甲（footmen）四件 | 4 / 7 / 8 / 4 | 3 / 5 / 7 / 4 | 3 / 8 / 10 / 5 | 3 / 8 / 10 / 5 |
| 唐/宋步兵甲四件 | 4 / 7 / 8 / 4 | — | — | — |
| 面甲头盔（只有头盔） | 3 | 2 | 4 | 5 |

- 耐久公式：`new int[]{13,15,16,11}[slotIndex] * multiplier`（`GoldenfootmenarmorItem.java:33-70` 等）。
- 所有四材质护甲都为下界合金变体加 `fireResistant()`；金质 7 个套装的护甲与细鳞金质变体调用 `makesPiglinsNeutral` 返回 true（`Golden*armorItem.java`、`FinescalearmorItem.java:80-82`）；金睚眦/金渤海/金面甲没有该覆写。
- 护甲铁/金/钻石/下界合金的装备音效分别为 `ARMOR_EQUIP_IRON/GOLD/DIAMOND/NETHERITE`（`FinescalearmorItem.java:132-140`）。

### 2.3 护甲套装、部件 id 与套装规则

#### 2.3.1 完整套装

| 套装 | id 前缀 | 材料 | 件数 | 可染色 | 覆盖纹理 | 规则入口 |
|---|---|---|---:|---|---|---|
| 山文甲 | `<mat>_mountain_character_armor_` | 铁/金/钻石/下界合金 | 16 | 是 | `mountain_character_armor_color.png` | `ArmorFamily.MOUNTAIN_CHARACTER` |
| 早期唐明光甲 | `<mat>_early_tang_dynasty_ming_guang_armor_` | 四材质 | 16 | 是 | `early_tang_dynasty_ming_guang_armor_color.png` | `MING_GUANG`（early） |
| 中期唐明光甲 | `<mat>_middle_tang_dynasty_ming_guang_armor_` | 四材质 | 16 | 是 | `middle_tang_dynasty_ming_guang_armor_color.png` | `MING_GUANG`（middle） |
| 晚期唐明光甲 | `<mat>_late_tang_dynasty_ming_guang_armor_` | 四材质 | 16 | 是 | `late_tang_dynasty_ming_guang_armor_color.png` | `MING_GUANG`（late） |
| 陌刀步兵甲 | `<mat>_footmen_armor_` | 四材质 | 16 | 是 | `footmen_armor_color.png` | `FootmenarmorcompletetestingProcedure` |
| 黑锤甲 | `<mat>_black_chui_armor_` | 四材质 | 16 | 是 | `black_chui_armor_color.png` | `ArmorFamily.BLACK_CHUI` |
| 细鳞甲 | `<mat>_fine_scale_armor_` | 铁/金/钻石/下界合金 | 16 | 是 | `fine_scale_armor_color.png` | `ArmorFamily.FINE_SCALE` |
| 唐步兵甲 | `tang_dynasty_infantry_armor_` | 铁 | 4 | 是 | `tang_dynasty_infantry_armor_color.png` | 无套装战斗规则 |
| 宋步兵甲 | `song_dynasty_infantry_armor_` | 铁 | 4 | 是 | `song_dynasty_infantry_armor_color.png` | 无套装战斗规则 |

每套的四件 id 为：

```text
<prefix>helmet
<prefix>chestplate
<prefix>leggings
<prefix>boots
```

#### 2.3.2 单独头盔

| 头盔族 | id 模式 | 材质 | 可染色 | 特殊行为 |
|---|---|---|---|---|
| 睚眦盔 | `<mat>_suanni_helmet` | 四材质 | 否 | 只有自定义模型/纹理；可作为陌刀步兵全套的替代头盔，可作为明光套的替代头盔 |
| 渤海盔 | `<mat>_bohai_helmet` | 四材质 | 是 | 可作为陌刀步兵全套/明光套的替代头盔 |
| 面甲 | `<mat>_face_guard_helmet` | 四材质 | 否 | 蹲下触发“驱离怪物”主动技能（见 7.10） |

面甲护甲值：铁 3、金 2、钻石 4、下界合金 5；耐久同材料模板头盔（13×倍率）。

#### 2.3.3 套装效果（`ChineseWeaponsCombatEvents.java:87-214`）

| 套装/族 | 触发条件 | 效果 |
|---|---|---|
| 山文甲 | 受到剑/三叉戟伤害（不要求全套） | 伤害 `-3` |
| 细鳞甲 | 受到剑/三叉戟伤害 | 10% 概率伤害 ×0.8；全套时 30% |
| 细鳞甲 | 全套 + 甜浆果丛伤害 | 完全取消该次伤害 |
| 细鳞甲 | 全套 | 免疫骑乘拖拽（`tryStartMountedDrag` 直接返回 false） |
| 黑锤甲 | 任意件数 | 20% 使攻击者主手物品冷却 60 tick；全套 40% |
| 黑锤甲 | 任意件数 + 重锤砸击（mace smash） | 50% 伤害 ×0.5；全套 100% |
| 黑锤甲 | 任意件数 + 剑/三叉戟 | 30% 伤害 ×0.6；全套 60% |
| 黑锤甲 | 任意件数 | 10% 反射原伤害的 50%（`thorns` 伤害源）；全套 30% |
| 早期明光 | 任意件数 | 10% 使攻击者主手冷却 40 tick；全套 20% |
| 早期明光 | 重锤砸击 | 30% 伤害 ×0.8；全套 80% |
| 早期明光 | 全套 + 非火焰箭、非投掷三叉戟、伤害 >3 | 伤害 `-1` |
| 中期明光 | 剑/三叉戟 + 玩家蹲下 | 伤害 `-2` |
| 中期明光 | 全套 + 非火焰箭、非投掷三叉戟、伤害 >3 | 伤害 `-2` |
| 晚期明光 | 剑/三叉戟 + 玩家蹲下 | 伤害 `-2` |
| 晚期明光 | 全套 + 非火焰箭、非投掷三叉戟、伤害 >3 | 伤害 `-2` |
| 任一明光（早期/中期/晚期） | 全套 + 非火焰箭、非投掷三叉戟、伤害 ≤3 | 取消伤害，箭矢速度 ×10，并标记 `hurtMarked` |
| 任一明光 | 全套 | 允许用睚眦/渤海头盔替代明光头盔，仍算 4 件 |
| 任一明光全套 | 玩家骑乘时每 10 tick | 给坐骑 `Attributes.ARMOR` 加 `+2`（`MOUNT_ARMOR`，ADDITION）；该修饰符不会移除，见 D08 |
| 任一明光 | 全套 | 代码里存在 `+20%` 移速修饰符，但每 tick 调用 `updateMingGuangSpeed(player,false)` 将其移除，实际不生效，见 D07 |
| 陌刀步兵甲 | 全套 | 低血/濒死增益、冲刺碰撞、箭矢反弹、攻击者加成抵消等，见 7.11；其中事件类 `FootmenarmorcompletetestingProcedure` 未注册，箭矢反弹/攻击者加成实际不触发（D41） |
| 面甲 | 佩戴头盔 + 蹲下 | 驱离怪物，见 7.10 |

明光“替代头盔”的判定在 `countPieces`：先数路径包含 `early/middle/late_tang_dynasty_ming_guang_armor_` 的护甲件，若头盔是睚眦或渤海四材质之一，再额外 `+1`（`ChineseWeaponsCombatEvents.java:438-491`）。

#### 2.3.4 可染色护甲

实现 `DyeableItem` 的护甲：

- 山文、早期/中期/晚期明光、陌刀步兵、黑锤、细鳞、唐步兵、宋步兵、渤海头盔。
- 睚眦、面甲不实现 `DyeableItem`。
- NBT：`display.Color`（整数 RGB）。默认无颜色返回 `0xFFFFFF`。
- 染色配方：护甲 + 任意染料（可多染料）→ 取平均色；已有颜色会作为一个“染料项”参与平均；结果保留原物品 NBT（附魔等）。
- 水炼药锅右键：移除 `display.Color`，降低一层水位，播放 `GENERIC_SPLASH`。
- 纹理：客户端把“基础纹理 + 颜色遮罩”合成为动态纹理；染色时按遮罩亮度做明暗保留（`DyeableItem.java:24-278`）。

### 2.4 武器总表

所有武器都继承 `SwordItem`，因此默认 1 点基础伤害、可附魔、可剑 sweep（骨朵显式关闭）。`ReachSwordItem` 通过 `ForgeMod.ENTITY_REACH` 添加实体交互距离：

| 基类 | 额外实体距离 | 使用者 |
|---|---:|---|
| `FourBlockReachSwordItem` | +1.0 | 戟、横刀、陌刀、宋长矛 |
| `FiveBlockReachSwordItem` | +2.0 | 戈 |
| `SixBlockReachSwordItem` | +1.0（名字与实际不符） | 骨朵，见 D09 |

实际实体攻击距离 = 原版 3.0 + 上表加成。KubeJS `setReachBonus` 可覆盖每个物品的加成（`ReachSwordItem.java:20-64`、`WeaponScriptSettings.java:41-50`）。

#### 2.4.1 戈（Dagger Axe，6 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_dagger_axe` | 4 | 1.9 | 118 | 17 | `minecraft:planks` 标签 + 自身 |
| `stone_dagger_axe` | 5 | 1.5 | 262 | 7 | `forge:stone` 标签 + 自身 |
| `iron_dagger_axe` | 6 | 1.5 | 500 | 4 | 铁锭 + 自身 |
| `golden_dagger_axe` | 4 | 1.5 | 64 | 24 | 金锭 + 自身 |
| `diamond_dagger_axe` | 7 | 1.9 | 3601 | 12 | 钻石 + 自身 |
| `netherite_dagger_axe` | 8 | 1.9 | 3047 | 17 | 下界合金锭 + 自身；防火 |

特殊钩子（`DaggerAxeItemBase.java`、各 `*daggeraxeItem.java`）：

- 继承 `FiveBlockReachSwordItem`，实体距离 +2.0。
- 主手持有且 `inventoryTick` 时调用 `OffhandClearProcedure`，强制清空副手。
- `hurtEnemy`：调用 `DaggerAxeAndGlaiveItemjizhongProcedure`（80% 概率“割伤”）和 `DaggerAxeHurtFiyyProcedure`（空中命中额外效果）。
- 木戈覆写 `hasCraftingRemainingItem=true`、`getCraftingRemainingItem` 返回自身、`isRepairable=false`（合成后返还自身，但不可修理）。
- 右键/技能 J 见 7.2。

#### 2.4.2 戟（Glaive，7 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_glaive` | 7 | 1.8 | 83 | 18 | `minecraft:planks` 标签 + 自身 |
| `stone_glaive` | 8 | 1.6 | 185 | 8 | `forge:stone` 标签 + 自身 |
| `iron_glaive` | 9 | 1.4 | 350 | 5 | 铁锭 + 自身 |
| `golden_glaive` | 7 | 1.8 | 45 | 25 | 金锭 + 自身 |
| `diamond_glaive` | 10 | 1.2 | 2186 | 13 | 钻石 + 自身 |
| `netherite_glaive` | 11 | 1.0 | 2844 | 18 | 下界合金锭 + 自身；防火 |
| `green_loong_glaive` | 12 | 1.0 | 2855 | 20 | 仅自身；防火 |

特殊钩子：

- 继承 `GlaiveItemBase`，实体距离 +1.0。
- `use`：潜行 + 右键启动旋转斩（`GlaiveItemGetLostKillProcedure.tryStart`），非潜行交给默认逻辑。
- `interactLivingEntity`：右键生物执行“止跑”（`GlaiveItemStopRunProcedure`）。
- `hurtEnemy`：调用 80% 概率割伤。
- `inventoryTick`：主手时清空副手。
- `isGlaiveItem` 供其他逻辑判断 7 件戟。

#### 2.4.3 唐横刀（Tang Dynasty Heng Saber，6 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_tang_dynasty_heng_saber` | 5 | 1.8 | 59 | 15 | 木板标签（Tier WOOD） |
| `stone_tang_dynasty_heng_saber` | 6 | 1.6 | 131 | 5 | 石头（Tier STONE） |
| `iron_tang_dynasty_heng_saber` | 7 | 1.6 | 250 | 14 | 铁锭 |
| `golden_tang_dynasty_heng_saber` | 5 | 1.8 | 32 | 22 | 金锭 |
| `diamond_tang_dynasty_heng_saber` | 8 | 1.4 | 1561 | 10 | 钻石 |
| `netherite_tang_dynasty_heng_saber` | 9 | 1.4 | 2031 | 15 | 下界合金锭；防火 |

- 继承 `FourBlockReachSwordItem`，实体距离 +1.0。
- 无 `use` 覆写；双击右键技能由全局事件 `TangDynastyHengSabeCutsProcedures` 处理（7.7）。
- 耐久为原版 Tier 耐久，金横刀只有 32 点。

#### 2.4.4 陌刀（Sky-Piercing Halberd，6 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_sky_piercing_halberd` | 4 | 1.4 | 109 | 15 | 木板标签 |
| `stone_sky_piercing_halberd` | 5 | 1.4 | 181 | 5 | 石头 |
| `iron_sky_piercing_halberd` | 6 | 1.2 | 300 | 14 | 铁锭 |
| `golden_sky_piercing_halberd` | 4 | 1.4 | 82 | 22 | 金锭 |
| `diamond_sky_piercing_halberd` | 7 | 1.2 | 2061 | 10 | 钻石 |
| `netherite_sky_piercing_halberd` | 8 | 1.0 | 2081 | 15 | 下界合金锭；防火 |

- 继承 `FourBlockReachSwordItem`，实体距离 +1.0。
- 耐久 = 原版 Tier 耐久 + 50（`SkyPiercingHalberdItem.java:21-29`）。
- 潜行命中触发击退、流血、额外耐久、成就；骑乘命中可拖拽目标（7.6）。

#### 2.4.5 宋长矛（Song Javelin，6 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_song_javelin` | 1 | 1.7 | 79 | 15 | 木板标签 |
| `stone_song_javelin` | 3 | 1.7 | 151 | 5 | 石头 |
| `iron_song_javelin` | 5 | 1.7 | 270 | 14 | 铁锭 |
| `golden_song_javelin` | 1 | 1.7 | 52 | 22 | 金锭 |
| `diamond_song_javelin` | 7 | 1.7 | 1581 | 10 | 钻石 |
| `netherite_song_javelin` | 9 | 1.7 | 2051 | 15 | 下界合金锭；防火 |

- 继承 `FourBlockReachSwordItem`，实体距离 +1.0。
- **攻击伤害存在 Tier 加成重复计算**：构造参数使用 `displayedAttackDamage + tier.getAttackDamageBonus()`，`SwordItem` 又加一次 Tier 加成；上表是实际值（木/金 1，石 3，铁 5，钻石 7，下界合金 9）。见 D10。
- 可蓄力投掷；蓄力 ≥5 tick 才能释放，`charge = min(useTicks/20, 1)`，初速 = `1.7 × charge`；消耗 2 耐久 + 1 个物品；冷却 8 tick。
- 支持 Riptide（水中/雨中）、Loyalty（回返）、Channeling（雷暴召唤闪电）、Piercing（设计上支持，但投射物覆写 `onHitEntity` 未调用 super，实际穿透无效，见 D11）。
- 蓄力时自定义手臂姿态 + 第一人称变换（`SongJavelinItem.java:31-250`）。

#### 2.4.6 骨朵（Gu Duo，6 件）

| id | 攻击伤害 | 攻击速度 | 耐久 | 附魔 | 修复 |
|---|---:|---:|---:|---:|---|
| `wooden_guduo` | 5 | 1.2 | 89 | 15 | 木板标签 |
| `stone_guduo` | 6 | 1.2 | 154 | 5 | 石头 |
| `iron_guduo` | 7 | 1.4 | 574 | 14 | 铁锭 |
| `golden_guduo` | 5 | 1.4 | 87 | 22 | 金锭 |
| `diamond_guduo` | 8 | 1.6 | 1572 | 10 | 钻石 |
| `netherite_guduo` | 9 | 1.6 | 2048 | 15 | 下界合金锭；防火 |

- 继承 `SixBlockReachSwordItem`，但该基类实际只给 +1.0 实体距离（与四格基类相同），见 D09。
- 关闭剑 sweep（`canPerformSweepAttack=false`）。
- `hurtEnemy`：主手不在冷却时按状态分支：
  - 不在地面：下砸（`HurtFlyyy`），额外伤害、缓慢、压扁、下沉、SquashPacket、冷却默认 10 tick。
  - 在地面 + 潜行：尝试让骑乘目标下马（默认 30%），成功冷却 60 tick。
  - 其他：普通命中施加 60 tick 流血；若攻击者护甲 ≤4 再施加 60 tick 失明。
- 每次命中固定 `hurtAndBreak(2)`，随后 `super.hurtEnemy` 再扣 1，共 3 点耐久。
- 命中音效 `chineseweapons:guduo`（音量 0.2）。

### 2.5 盾

| id | 类型 | 耐久 | 修复 | 行为 |
|---|---|---:|---|---|
| `tang_dynasty_shield` | `ShieldItem` | 1456 | 橡木木板（仅 `Items.OAK_PLANKS`） | 右键举盾（`getUseDuration=72000`）；无额外逻辑 |
| `song_standing_shield` | `ShieldItem` | 1670 | 橡木木板（仅 `Items.OAK_PLANKS`） | 右键举盾；J 键/技能可放置为 `standing_shield` 实体、收回、旋转、木板修复 |

- 两盾都注册 `chineseweapons:blocking` 物品属性（`ChineseWeaponsClientEvents.java:25-44`）。
- 宋立盾的放置/收回/旋转/修复细节见 7.8，实体见第 4 节。

### 2.6 唐马铠

| id | 防御 | 纹理 |
|---|---:|---|
| `iron_tang_dynasty_horse_armor` | 8 | `textures/entities/horse/armor/iron_tang_dynasty_horse_armor.png` |
| `golden_tang_dynasty_horse_armor` | 11 | `.../golden_tang_dynasty_horse_armor.png` |
| `diamond_tang_dynasty_horse_armor` | 18 | `.../diamond_tang_dynasty_horse_armor.png` |
| `netherite_tang_dynasty_horse_armor` | 25 | `.../netherite_tang_dynasty_horse_armor.png` |

- 全部 `TangDynastyHorseItem extends HorseArmorItem`，`stacksTo(1)`、耐久 0、`canBeDepleted=false`、不可燃烧。
- 穿唐马铠的马免疫箭矢伤害并反弹箭（7.9）。

### 2.7 锻石（8 件）

| id | 工具提示 key |
|---|---|
| `early_tang_dynasty_ming_guang_armor_forging_stone` | `item.chineseweapons.early_tang_dynasty_ming_guang_armor_forging_stone.tile` |
| `footmen_armor_forging_stone` | `item.chineseweapons.footmen_armor_forging_stone.tile` |
| `tang_dynasty_infantry_armor_forging_stone` | 同名 `.tile` |
| `fine_scale_armor_forging_stone` | 同名 `.tile` |
| `late_tang_dynasty_ming_guang_armor_forging_stone` | 同名 `.tile` |
| `middle_tang_dynasty_ming_guang_armor_forging_stone` | 同名 `.tile` |
| `mountain_character_armor_forging_stone` | 同名 `.tile` |
| `black_chui_armor_forging_stone` | 同名 `.tile` |

8 个类都只继承 `Item`，在 `appendHoverText` 里追加一行翻译文本；没有右键/合成逻辑。村民盔甲匠 4 级交易出售（11.4）。

### 2.8 甲片与宝石

| id | 用途/来源 |
|---|---|
| `iron_armor_pieces` | 铁甲片，3 铁合成 9 个（`a a` + 中间空 + 底部 `a`，共 3 铁） |
| `iron_armor_pieces_plate` | 9 甲片 + 3 线 → 3 板 |
| `diamond_armor_pieces` | 3 钻石 → 9 个 |
| `diamond_armor_pieces_plate` | 9 甲片 + 3 线 → 3 板 |
| `golden_armor_pieces` | 3 金 → 9 个 |
| `golden_armor_pieces_plate` | 9 甲片 + 3 线 → 3 板 |
| `stone_armor_pieces` | 仅创造模式可得；没有配方（D18） |
| `stone_armor_pieces_plate` | 仅创造模式可得；没有配方（D18） |
| `iron_mountain_character_armor_pieces` | 4 铁 → 12 个（pattern `aaa` + ` a `） |
| `iron_mountain_character_armor_pieces_plate` | 9 山文甲片 + 3 线 → 3 板 |
| `diamond_mountain_character_armor_pieces` | 4 钻石 → 12 个 |
| `diamond_mountain_character_armor_pieces_plate` | 9 山文甲片 + 3 线 → 3 板 |
| `golden_mountain_character_armor_pieces` | 4 金 → 24 个（pattern `aaa` + ` a `） |
| `golden_mountain_character_armor_pieces_plate` | 9 山文甲片 + 3 线 → 3 板 |
| `ancient_gem` | 远古宝石矿掉落；9 个合成宝石块，宝石块可逆向分解 |

### 2.9 方块物品

`armor_casting_table`、`ancient_gem_ore`、`deepslate_ancient_gem_ore`、`ancient_gem_block` 通过 `BlockItem` 注册，id 与方块 id 相同；详见第 3 节。

### 2.10 创造模式标签页

- id `chineseweapons:chinese_weapons`
- 标题 key `item_group.chineseweapons.chinese_weapons`
- 图标 `iron_mountain_character_armor_helmet`
- 202 个条目，顺序：山文 → 早期明光 → 陌刀步兵 → 黑锤 → 中期明光 → 晚期明光 → 宋步兵 → 细鳞 → 唐步兵 → 睚眦/渤海/面甲 → 马铠 → 戈 → 戟 → 陌刀 → 长矛 → 骨朵 → 横刀 → 盾 → 锻造台/矿/宝石块 → 宝石/甲片/锻石。
- 注意：`golden_mountain_character_armor_pieces` 等在标签页中间，顺序不完全按材质分组；不影响注册 id。

---
## 3. 方块与方块实体

### 3.1 方块注册表

| id | 类 | 属性 | 行为/备注 |
|---|---|---|---|
| `armor_casting_table` | `ArmorcastingtableBlock` | `SoundType.STONE`、硬度 3、抗爆 10、`noOcclusion`、非红石导体、亮度 0、天空光可透 | 有方块实体；右键打开锻造台 GUI；破坏时掉落输入/已完成输出 |
| `ancient_gem_ore` | `AncientgemoreBlock` | 复制 `Blocks.DIAMOND_ORE`（含 `requiresCorrectToolForDrops`）、`getLightBlock=15` | 普通 `Block`，不是 `DropExperienceBlock`，**不掉经验**（D20）；掉落见 11.2 |
| `deepslate_ancient_gem_ore` | `DeepslateAncientgemoreBlock` | 同钻石矿属性、`getLightBlock=15` | 同上 |
| `ancient_gem_block` | `AncientGemBlock` | `SoundType.AMETHYST`、硬度 5、抗爆 10、`requiresCorrectToolForDrops`、`getLightBlock=15` | 9 宝石合成/分解 |

`armor_casting_table` 的碰撞/渲染形状由三个 box 组成（`ArmorcastingtableBlock.java:34-40`）：

- 顶部 `0,12,0 → 16,16,16`
- 底座 `0,0,0 → 16,4,16`
- 中部 `1,4,1 → 15,12,15`

`getVisualShape` 返回空，因此没有选择框；`propagatesSkylightDown=true`、`getLightBlock=0`。

### 3.2 方块实体 `chineseweapons:armor_casting_table`

`ArmorCastingTableBlockEntity`（`ArmorCastingTableBlockEntity.java`）：

| 项 | 值 |
|---|---|
| 输入容器 | `SimpleContainer(10)`：3×3 网格 9 格 + 主材料 1 格 |
| 输出容器 | `SimpleContainer(1)` |
| 进度 | `0..100` |
| 每步等待 | 随机 20–40 tick（`MIN_TICKS_PER_STEP=20`，`MAX_TICKS_PER_STEP=40`） |
| 每步进度 | 随机 15–35（`MIN_PROGRESS_PER_STEP=15`，`MAX_PROGRESS_PER_STEP=35`） |
| 完成阈值 | `progress >= 100` |
| 配方类型 | 遍历 `DeferredRecipe.WeaponCastingShapedType` 的所有配方，取第一个 `matches(input, level)` |
| 完成音效 | `ANVIL_PLACE`，音量 1、音高 0.6 |
| 中间音效 | `ANVIL_USE`，音量 0.8、音高 0.9–1.1 |
| 粒子 | 12 个 `CRIT` 围成半径 0.85 的圆 + 6 个 `ENCHANT` |
| 进度同步 | 仅当 `hasRecipe` 或 `progress` 变化时发送 `ArmorCastingTableBlockProgressPacket` 给 64 格内玩家 |
| 保存 NBT | `Progress`、`StepTimer`、`NextStepTicks`、`Input`（每个非空槽 `S<i>` 内含 `Slot`+物品）、`Output` |
| 破坏掉落 | 所有输入槽逐个掉落；只有 `isFinished()` 时才掉落输出，否则清空输出 |
| 客户端同步 | `setProgress(int)` 只改本地字段；GUI 进度条实际读 `ArmorCastingTableBlockEntityCache`（包更新） |

服务端 tick 逻辑（`ArmorCastingTableBlockEntity.java:64-134`）：

1. 找配方；若 `hasRecipe` 或 `progress` 变化，发同步包。
2. 已 `isFinished()`：若输出为空且配方存在，设置输出，然后返回。
3. 无配方：重置进度/计时器，清空输出。
4. 有配方：输出为空时写入配方结果；累计 `stepTimer`，达到 `nextStepTicks` 后增加随机进度；到 100 时播放 `ANVIL_PLACE` 并 `consumeInputs()`（每个输入槽减 1），否则播放 `ANVIL_USE` 并生成粒子。
5. 每次进度到 100 时都会 `consumeInputs()`；正常只触发一次，因为之后进入 `isFinished` 分支。

`tryConsumeInputs()`（`:239-252`）存在但未被调用；`onRecipeTaken()` 重置进度并在客户端发送 0/false。

### 3.3 锻造台 GUI

| 项 | 值 |
|---|---|
| MenuType id | `chineseweapons:gui.chineseweapons.weapons_casting_table` |
| Menu 类 | `WeaponsCastingTableTypeGuiMenu` |
| Screen 类 | `WeaponsCastingTableScreen` |
| 标题 key | `block.chineseweapons.armor_casting_table`（“Armor Forging Table”） |
| 背景纹理 | `chineseweapons:textures/gui/weapons_casting_table.png`，256×256 |
| 输入槽 | 9 个 3×3 槽 + 1 个主材料槽（菜单索引 0–9），输出槽（索引 10） |
| 玩家背包 | 27 个主背包槽（索引 11–37）+ 9 个快捷栏（索引 38–46） |
| 输出锁定 | `mayPickup` 仅当 `table.isFinished()` |
| 进度条 | 屏幕自定义绘制，读 `ArmorCastingTableBlockEntityCache` 的 0–100；有配方且未完成时在输出槽附近画灰色遮罩 |

槽位坐标（`WeaponsCastingTableTypeGuiMenu.java:36-76`）：

- 3×3：`x=0,18,36`，`y=0,18,36`，实际坐标为 `(y-2, x+17)`。
- 主材料：`(69, 53)`。
- 输出：`(115, 35)`。
- 玩家背包：`(col*18-19, 84+row*18)`；快捷栏 `y=142`。

**菜单索引与 `quickMoveStack` 不一致**：输出实际是索引 10，但 `quickMoveStack` 把 11 当输出、把 0–10 当输入、把 12–46 当背包；JEI 传输处理器写 0–9 输入/11–46 玩家槽（与实际布局一致），详见 D02。

`stillValid` 永远返回 true（D03），没有距离/方块检查。

### 3.4 锻造台相关配方

- 方块本体：`armor_casting_table.json`，`minecraft:crafting_shaped`，pattern `["aba","bab"]`，`a=iron_ingot`、`b=#minecraft:planks`，产出 1（实际 3 铁 + 3 木板）。
- 矿/宝石块/甲片配方见 6.2–6.4。
- 所有 `weapon_casting_shaped` 配方只能在锻造台匹配，不在工作台。

---

## 4. 实体

### 4.1 注册表

| id | 类 | 尺寸 | 分类 | 追踪 | 备注 |
|---|---|---|---:|---|---|
| `chineseweapons:song_javelin` | `SongJavelinProjectile extends AbstractArrow` | 0.5×0.5 | MISC | range 4，update 20 | 宋长矛投掷物 |
| `chineseweapons:standing_shield` | `SongStandingShieldEntity extends Entity` | 0.8×2.8 | MISC | 默认 | 宋立盾实体；`fireImmune` |

注册代码在 `ChineseweaponsModEntities.java:12-33`。注意 `standing_shield` 的 `build(String.valueOf(ResourceLocation.fromNamespaceAndPath(...)))` 传入的是带命名空间的字符串，但 `DeferredRegister` 的 key 仍是 `standing_shield`，实际 id 正确。

### 4.2 `SongJavelinProjectile`

| 项 | 值/行为 |
|---|---|
| 父类 | `AbstractArrow`，`pickup=ALLOWED` |
| 同步字段 | `ITEM_REGISTRY_NAME`（String），用于客户端选择纹理 `textures/entity/<item_path>.png` |
| 本地字段 | `weaponStack`、`loyaltyLevel`、`hasChanneling`、`isReturning`、`returnSoundPlayed`、`hovering`（不全部同步） |
| 构造 | 从物品复制 stack，读取 `Loyalty`/`Channeling`/`Piercing` 附魔并设置 |
| 命中生物 | 伤害 = `baseDamage + 2`；按 `knockback * 0.6 * motion` 追加击退；播放 `TRIDENT_HIT`；Channeling + 雷暴 → 闪电 |
| 命中非生物 | 按法线反射速度 ×0.4；伤害同样 `baseDamage + 2`；播放 `ARROW_HIT`；Channeling 可召雷 |
| 无 Loyalty | 命中后速度归零、恢复重力（但未设置 `inGround`，见 D11） |
| 有 Loyalty | `startReturning()`：关闭重力/碰撞、播放 `TRIDENT_RETURN`、以 `0.5 + 0.15×等级` 速度飞向主人；距离 ≤0.5 时尝试放入背包，满则悬停在主人头顶 2 格 |
| 命中方块 | 调用 `super.onHitBlock`（设置 `inGround`），播放 `TRIDENT_HIT_GROUND`；Channeling + 避雷针 + 露天 + 雷暴/下雨 → 召雷；Loyalty → 回返 |
| 掉地消失 | `tickDespawn` 在 `inGround && !isReturning` 时直接返回，因此**永不自然消失**（D11） |
| 拾取物 | `getPickupItem()` 返回 `weaponStack.copy()` |
| NBT | `Weapon`、`ItemName`、`Loyalty`、`Channeling`、`Returning`、`ReturnSoundPlayed`、`Hovering` |

伤害数值：`baseDamage = this.attackDamage * charge`，其中 `attackDamage = displayedAttackDamage + tier.getAttackDamageBonus()`；注册时 `displayedAttackDamage=0`，所以满蓄力 base 为 0/1/2/3/4（木/金 0，石 1，铁 2，钻石 3，下界合金 4），命中再加 2 → 满蓄力 2–6 点。`setCritArrow(charge>=0.9)` 因覆写 `onHitEntity` 未调用 super，实际不产生暴击加成（D11）。

### 4.3 `SongStandingShieldEntity`

| 项 | 值/行为 |
|---|---|
| 父类 | `Entity`，`noPhysics=false` |
| 同步字段 | `DATA_HP`（Float，初始 1）、`DATA_YAW`（Float，初始 0） |
| 存储物品 | `storedItemNbt`（CompoundTag），仅服务端 NBT，**不通过网络同步** |
| 放置 | `SongStandingShield.placeShield`：视线 5 格 ray trace，命中方块后生成实体；`moveTo(hit.x, hit.y, hit.z, yaw=playerYaw+180, 0)`；实体生命 = 物品最大耐久 − 当前损伤；物品 −1 |
| 生命 | `setHealth(hp)` 写 `DATA_HP`；若配置 `SONGSTANDINGSHIELD_ENDURANCEDISPLAY=true`（默认），设置自定义名 `entity.standing_shield.hp`，≤100 时红色 |
| 旋转 | 玩家潜行 + 右键：根据玩家在盾牌左/右决定 `±15°`（默认，可配置），播放 `WOODEN_BUTTON_CLICK_ON`；客户端 `zhuannimation` 做 15° 插值 |
| 修复 | 主手 `#minecraft:planks` 右键：生命 `<50` 时回满，否则 `+50` 上限封顶；消耗 1 木板；播放按钮声 |
| 受击 | 取消火焰/爆炸/投掷药水/区域效果云；播放 `SHIELD_BLOCK`；伤害除以 `(Unbreaking等级+1)`；生命 ≤0 时播放 `SHIELD_BREAK` 并 `discard()` |
| 碰撞 | `isPickable=true`、`canBeCollidedWith=true`、`isPushable=false`、`push` 空实现、不受流体推动 |
| 重力 | 只保留 Y 速度，`(vy-0.08)*0.98`，下限 -1；落地后归零、锁定 yaw |
| 爆炸 | `ChineseWeaponsCombatEvents.onExplosion` 收集 32 格内所有立盾，调用 `handleExplosion`：若爆炸在盾牌正面（dot>0），移除盾牌后方 4 格半径 4 的方块和实体（安全区） |
| 安全区 | 以盾牌前方 `BEHINDCENTER=4` 格为中心，8×8×8 的 AABB；`setCustomNameVisible` 由本地玩家是否在安全区决定 |
| 客户端渲染 | 5 张纹理：`<400`、`<700`、`<1000`、`<1300`、其余为完好；模型 `ModelSongStandingShield` |
| 保存 NBT | `StoredItem`、`HP`、`Yaw` |
| 服务端 tick | 见 D01：`tick()` 调用 `Minecraft.getInstance().player`，专用服务器会加载客户端类 |

### 4.4 实体数据/同步注意

- `SongJavelinProjectile` 自身新增同步字段只有物品注册名；伤害、Loyalty、Channeling、Piercing 等由服务端在生成时设置，客户端不依赖它们（`AbstractArrow` 父类仍同步其自带的暴击/穿透等字段）。
- `SongStandingShieldEntity` 同步 HP/Yaw；存储物品不同步，客户端无法显示盾牌内物品的附魔/名称（渲染也不需要）。
- 两个实体都在 `ChineseweaponsModEntities` 里以 `MobCategory.MISC` 注册，无生成蛋、无自然生成。

---

## 5. 效果与附魔

### 5.1 状态效果

| id | 类 | 颜色 | 触发频率 | 机制 | 来源/持续时间 |
|---|---|---|---:|---|---|
| `chineseweapons:bleed` | `BleedEffect` | `0x000000` | 每 20 tick | 每次触发：施加 `Weakness` 25 tick、`MovementSlowdown` 25 tick；若生命 ≥2，造成 1 点 `generic` 伤害 | 骨朵普通命中 60 tick；陌刀潜行命中 100 tick（5 秒）；若目标已有 bleed 则不重复施加陌刀 bleed |
| `chineseweapons:cuts` | `CutsEffect` | `0x000000` | 每 20 tick | 若实体在冲刺，额外 1 点伤害；随后 30% 概率再 1 点伤害（服务端判定）；即每秒约 1–2 点 | 戈/戟命中 120 tick（6 秒）；横刀突刺 60 tick；骨朵不施加 cuts |

- 两者都是 `HARMFUL`，没有自定义 `MobEffect` 图标以外的叠加规则；`applyEffectTick` 的 amplifier 参数未使用，因此高等级不会增强。
- `BleedEffect` 每次刷新 `Weakness`/`Slow` 为 25 tick（1.25 秒），只要 bleed 持续，减速/虚弱近似持续。
- `CutsEffect` 的 30% 判定使用 `entity.level().getRandom()`，客户端也会执行但 `hurt` 在客户端通常无效；权威伤害在服务端。
- 纹理：`assets/chineseweapons/textures/mob_effect/bleed.png`、`cuts.png`。

### 5.2 附魔

- **实际注册数量：0。**
- `ChineseWeaponsModEnchantments.REGISTRY` 被注册，但唯一一行 `cavalry_buster_star` 被注释（`ChineseWeaponsModEnchantments.java:12-14`）。
- 仍存在但未注册的代码：
  - `enchantment/CavalryBusterStar.java`：`Rarity.UNCOMMON`、`DAGGERAXEItem.DAGGER_AXE` 分类、主手、最大 3 级、`getMinCost=1+10×level`、`getMaxCost=6+10×level`。
  - `enchantment/DAGGERAXEItem.java`：通过 `EnchantmentCategory.create("DAGGER_AXE", ...)` 定义只允许 6 件戈的分类。
- 因此游戏内不存在 `chineseweapons:cavalry_buster_star`；`/enchant` 该 id 应失败。见 D12。

---

## 6. 配方

### 6.1 配方类型与序列化器

| 注册 id | 类型 | 类 | 说明 |
|---|---|---|---|
| `chineseweapons:weapon_casting_shaped` | `RecipeType<WCTRecipe>` | `WCTRecipe` | 锻造台配方；`toString()` 返回同 id |
| `chineseweapons:weapon_casting_shaped` | `RecipeSerializer<WCTRecipe>` | `WCTRecipeSer` | JSON/网络序列化 |
| `chineseweapons:crafting_special_dyeablearmor` | `RecipeSerializer<DyeableArmorRecipe>` | `SimpleCraftingRecipeSerializer` | 工作台染料护甲特殊配方 |

`DyeableArmorRecipe.SERIALIZER` 在 `ChineseweaponsMod.java:50-55` 注册，`RECIPE_SERIALIZERS.register(bus)` 在 `:83`。**任务提示中举例的“未注册的染料护甲序列化器”在 3010fe3 中不成立**；如果重构树里缺失，那是重构回归，不是原始版本缺陷。见 D13。

### 6.2 配方文件统计

`src/main/resources/data/chineseweapons/recipes` 共 **193** 个 JSON：

| type | 数量 | 内容 |
|---|---:|---|
| `chineseweapons:weapon_casting_shaped` | **137** | 护甲 101、武器 31、盾 2、马铠 3 |
| `minecraft:smithing_transform` | **38** | 钻石 → 下界合金：护甲 31、武器 6、马铠 1 |
| `minecraft:crafting_shaped` | **15** | 锻造台、宝石块、13 条甲片/板配方（含 1 个重复，见 D14） |
| `minecraft:crafting_shapeless` | **1** | 宝石块 → 9 宝石 |
| `chineseweapons:crafting_special_dyeablearmor` | **1** | `dyeable_armor.json`，只有 `type` |
| `forge:conditional` | **1** | 安装 Patchouli 时用书 + `#chineseweapons:armor_pieces` 合成 `patchouli:guide_book`，NBT `patchouli:book=chineseweapons:forging_notes` |

铸造配方细分（137）：

| 结果族 | 数量 | 备注 |
|---|---:|---|
| 护甲件 | 101 | 7 个四材质套装的铁/金/钻石各 3 材质 ×4 件 = 84；唐/宋步兵各 4；睚眦/渤海/面甲各 3（铁/金/钻石） |
| 戈 | 5 | 木/石/铁/金/钻石；下界合金走 smithing |
| 戟 | 6 | 木/石/铁/金/钻石/绿龙；下界合金走 smithing |
| 横刀 | 5 | 木/石/铁/金/钻石；下界合金走 smithing |
| 陌刀 | 5 | 木/石/铁/金/钻石；下界合金走 smithing |
| 宋长矛 | 5 | 木/石/铁/金/钻石；下界合金走 smithing |
| 骨朵 | 5 | 木/石/铁/金/钻石；下界合金走 smithing |
| 盾 | 2 | 唐盾、宋立盾 |
| 马铠 | 3 | 铁/金/钻石；下界合金走 smithing |

### 6.3 `weapon_casting_shaped` JSON 形状

示例（`wooden_glaive.json`）：

```json
{
  "type": "chineseweapons:weapon_casting_shaped",
  "pattern": [" AA", "ABA", "BA "],
  "key": {
    "A": { "tag": "minecraft:planks" },
    "B": { "item": "minecraft:stick" }
  },
  "main_material": { "item": "minecraft:gold_ingot" },
  "result": { "item": "chineseweapons:wooden_glaive", "count": 1 }
}
```

解析规则（`WCTRecipeSer.java:27-71`）：

- `pattern` 按行读取，**硬编码每行只取前 3 个字符**；所有现有 137 个配方都是 3 行 ×3 字符。
- `key` 用 `Ingredient.fromJson` 解析，空格自动映射 `Ingredient.of(Items.AIR)`。
- 槽位 1–9 按 pattern 行优先展开；第 10 个 Ingredient 来自 `main_material`。
- `result` 用 `ShapedRecipe.itemStackFromJson` 解析（支持 count）。
- 解析异常只记日志并返回 `null`，该配方静默缺失（D15）。
- `WCTRecipe.matches` 固定循环 10 次，按槽位顺序逐个 `Ingredient.test`（`WCTRecipe.java:25-33`）；没有 2×2/3×1 形状、没有镜像/偏移匹配。
- 网络同步发送 Ingredient 列表和结果；`toNetwork` 用 `writeItemStack`。

### 6.4 其他配方类别

**Smithing Transform（38）**：全部为 `diamond_* + minecraft:netherite_ingot → netherite_*`，`template` 为空/未指定；覆盖：

- 护甲：7 个四材质套装的 4 件 = 28；睚眦/渤海/面甲各 1 = 3；共 31。
- 武器：戈、戟、骨朵、陌刀、长矛、横刀各 1 = 6。
- 马铠 1。
- 注意：`netherite_fine_scale_armor_*` 的文件名带 `_smithing_table` 后缀，配方 id 也随之不同；功能相同。

**Shaped（15）**：

| 结果 | pattern | key | 数量 |
|---|---|---|---:|
| `armor_casting_table` | `aba` / `bab` | a=铁锭，b=木板标签 | 1 |
| `ancient_gem_block` | `aaa`×3 | a=远古宝石 | 1 |
| `iron_armor_pieces` | `a a` / `   ` / ` a ` | a=铁锭 | 9 |
| `iron_armor_pieces_plate` | `aaa` / `bbb` / `aaa` | a=铁甲片，b=线 | 3 |
| `diamond_armor_pieces` | 同上 | a=钻石 | 9 |
| `diamond_armor_pieces_plate` | 同上 | a=钻石甲片，b=线 | 3 |
| `golden_armor_pieces` | 同上 | a=金锭 | 9 |
| `golden_armor_pieces_plate` | 同上 | a=金甲片，b=线 | 3 |
| `iron_mountain_character_armor_pieces` | `aaa` / ` a ` | a=铁锭 | 12 |
| `iron_mountain_character_armor_pieces_plate` | `aaa`/`bbb`/`aaa` | a=山文甲片，b=线 | 3 |
| `iron_mountain_character_armor_piece_plate` | 同上 | 同上 | 3（与上一行重复，D14） |
| `diamond_mountain_character_armor_pieces` | `aaa` / ` a ` | a=钻石 | 12 |
| `diamond_mountain_character_armor_pieces_plate` | 同上 | a=山文甲片，b=线 | 3 |
| `golden_mountain_character_armor_pieces` | `aaa` / ` a ` | a=金锭 | 24 |
| `golden_mountain_character_armor_pieces_plate` | 同上 | a=山文甲片，b=线 | 3 |

**Shapeless（1）**：`ancient_gem_block` → 9 `ancient_gem`。

**锻造台方块配方**：`armor_casting_table` 本身是普通工作台配方；锻造台配方（137）不在工作台显示。

### 6.5 染料护甲特殊配方

- `DyeableArmorRecipe` 仅接受：恰好 1 件实现 `DyeableItem` 的护甲 + 至少 1 个 `DyeItem`，不允许其他物品。
- 输出：护甲复制（保留 NBT/附魔），颜色为所有染料（包括已有颜色）的逐通道平均，并按最大分量归一化到原亮度；写入 `display.Color`。
- `getRemainingItems` 清空染料（染料被消耗，不返还）。
- `canCraftInDimensions(width,height) >= 2`。
- 没有配方 JSON 的 pattern；`dyeable_armor.json` 只有 `type`，由 `SimpleCraftingRecipeSerializer` 读取。

### 6.6 重复配方检测

- 仅 `WCTRecipeSer` 有检测：`TRUE_REG_RECIPE` 静态 `HashMap<String, ResourceLocation>`，签名 = 9 个 pattern key 的物品/tag 字符串 + 主材料，**不包含输出**。
- 重复时只 `LOGGER.error("Duplicate recipe detected: ... - same ingredients but different output")`，仍然返回并注册新配方。
- `TRUE_REG_RECIPE` 是静态且**从不清空**，`/reload` 后相同配方会再次被当作重复并打印错误（D15）。
- 其他配方类别没有重复检测；`iron_mountain_character_armor_piece_plate.json` 与 `..._pieces_plate.json` 输出/材料完全相同，是实际存在的重复（D14）。

---

## 7. 战斗与玩法过程（procedures）

> 所有概率/阈值默认值来自 `WeaponScriptSettings` 调用点；KubeJS 可覆盖。触发端没有特别说明时均为服务端权威。

### 7.1 通用命中与副手规则

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 | 成就 |
|---|---|---|---|---|---|
| `DaggerAxeAndGlaiveItemjizhongProcedure` | 戈/戟 `hurtEnemy`（每次命中） | 80% 概率（`HIT_EFFECT` 默认 0.80）；服务端 | 无 | 目标获得 `cuts` 120 tick（6 秒）；若目标是 `ServerPlayer`，授予 `get_dagger_axe/dagger_axe_hook_has_cuts_xg` | 受害者成就 |
| `OffhandClearProcedure._362` | 主手持有戈/戟，每 tick `inventoryTick` | 主手 stack 与 tick 的 stack 为同一对象；服务端 | 每 tick | 把副手物品移入背包；背包满则掉落；副手清空 | 无 |
| `ChineseWeaponsCombatEvents.tryStartMountedDrag` | 陌刀/长矛命中，或由其他调用 | 主人骑乘、目标未骑乘、目标不是细鳞全套、目标未处于拖拽 | 5 秒（100 tick） | 目标 persistentData 写 `chineseweapons:tether_owner`、`chineseweapons:tether_until`；施加 `cuts` 100 tick；每 tick 拉向主人身后 1.5 格 | 无 |
| `tickTetheredEntity` | 被拖拽实体每 tick | 主人在线、存活、未超时；目标在 8 格内用速度拉，超过 8 格直接传送 | — | 目标速度被拉向主人；停止冲刺；Mob 停止寻路并清目标 | 无 |
| `tryEscapeTether` | 被拖拽实体跳跃 | 30% 概率 | — | 移除拖拽 NBT | 无 |
| `preventTetheredAttacks` | 被拖拽实体攻击 | 攻击者在拖拽状态 | — | 取消该次 `LivingAttackEvent` | 无 |

### 7.2 戈（Dagger Axe）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 | 成就 |
|---|---|---|---|---|---|
| 普通命中 | `hurtEnemy` | 服务端；先 `cuts` 80% | 无 | 额外 `cuts`；见 7.1 | 受害者 `...cuts_xg` |
| 空中命中 `DaggerAxeHurtFiyyProcedure` | `hurtEnemy` 且攻击者 `!onGround` | 主手非空、不在冷却；服务端 | 40 tick | 目标沿攻击者视线方向 `push(look.x,0.5,look.z)`，获得 `MovementSlowdown` 60 tick（amplifier 1），受 1 点 `player_attack` 伤害；主手 −2 耐久 | 无 |
| 钩取 `DaggerAxeItemFierceHookProcedure` | J 键/技能包，且主手是戈 | 沿视线 1..7 格逐格扫描 1×1×1 AABB；优先骑乘者/乘客；目标被拉近最多 4 格，保留 2 格距离；安全落点最多尝试 12 次、每次 +0.25 格；造成 2 点伤害、主手 −2 耐久 | 10 tick（成功才加） | 拉近目标；按概率下马；授予成就 | `get_dagger_axe/dagger_axe_hook`；目标是 Creeper 时 `...creeper` |
| 钩取下马概率 | 钩取命中且目标骑乘 | 陌刀步兵全套 10%；早期/中期明光全套 15%；晚期明光全套 10%；否则默认 30%（`HOOK_DISMOUNT`） | — | `stopRiding()` + `removeVehicle()` | 钩取成就 |
| 合成返还 | 木戈合成 | `hasCraftingRemainingItem=true`，`isRepairable=false` | — | 合成后返还自身；不能铁砧修理 | 无 |

### 7.3 戟（Glaive）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 | 成就 |
|---|---|---|---|---|---|
| 普通命中 | `hurtEnemy` | 80% `cuts` 120 tick | 无 | 见 7.1 | 受害者成就 |
| 旋转斩 `GlaiveItemGetLostKillProcedure.tryStart` | 潜行 + 右键，主手戟 | 非冷却；服务端总伤害 = `ATTACK_DAMAGE - 2`，分 3 次脉冲（t=0、11、22）；范围前方 3 格、左右各 1.5、上下各 1.5 | 20 tick（启动时加） | 3 次 `playerAttack` 伤害；消耗 3 耐久、5 点 exhaustion（`causeFoodExhaustion`）；客户端播放 30 tick 动画 | `get_glaive/use_gs` |
| 止跑 `GlaiveItemStopRunProcedure` | 右键生物（`interactLivingEntity`）或 J 键且 3 格内有生物 | 非潜行；目标为冲刺玩家 50%，其他生物 30%；服务端施加 | 30 tick + 2 耐久 | 目标获得 `MovementSlowdown` 40 tick、amplifier 255（近似定身） | 无 |
| 骑乘下马 | `ChineseweaponsMod.ForgeEvent.onLivingHurt` | 受害者骑乘、攻击者为玩家、主手任意 7 件戟 | 无 | 60% 概率（`GLAIVE_DISMOUNT` 默认 0.60）`stopRiding()` | 无 |
| 副手清理 | 每 tick | 主手戟 | 每 tick | 清空副手 | 无 |

动画：`GlaiveSpinAnimationHandler` 30 tick，三阶段（前进 0–0.18、下压 0.18–0.30、旋转 0.30–0.86、回收 0.86–1.0），旋转 -360°，缩放 1.25；`GlaiveItemStopRunProcedure` 的 46 tick 突刺动画由 `GlaiveItemClientEvent` 旋转 -30°、前移 3/16。

### 7.4 骨朵（Gu Duo）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 |
|---|---|---|---|---|
| 普通命中 `DFHurt` | `hurtEnemy`，攻击者在地面且不潜行，主手不在冷却 | 服务端 | 无（仅物品使用冷却由其他分支设置） | 目标 `bleed` 60 tick；若攻击者护甲值 ≤4（`GUDUO_ARMORBLINDNESS`），再 `Blindness` 60 tick |
| 下砸 `HurtFlyyy` | `hurtEnemy` 且攻击者不在地面 | 目标不在水：传送至目标当前位置 + 远离攻击者 1 格、Y+1.5，再向下 `0.3 + rand×(threshold-0.1)`（threshold 默认 1.0）；水中：氧气 -3、向下 -4；对目标造成 `攻击力 + 2` 的 `mobAttack` 伤害 | 默认 10 tick（`GUDUO_HURTSKILLTICK`） | 缓慢 100 tick、压扁动画（SquashPacket）、音效、向下推力；目标所有附近玩家收到压扁包（4096 平方距离内） |
| 下马 `HurtStopHo` | `hurtEnemy` 且攻击者潜行、目标骑乘 | 30% 概率（`GUDUO_STOPRIDING`） | 60 tick | `stopRiding`；对目标造成攻击力伤害；坐骑伤害分支因先 `stopRiding` 后检查 `getVehicle()` 而不可达（D40） |
| 耐久 | 每次 `hurtEnemy` | 固定 | — | `hurtAndBreak(2)` + `super` 的 1 = 3 |
| 压扁渲染 | `SquashPacket` | 客户端时间 0–300ms easeOutCubic，300–800ms easeInCubic，最小 Y 缩放 0.6 | — | `RenderLivingEvent.Pre` push + scale，Post pop |

### 7.5 宋长矛（Song Javelin）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 |
|---|---|---|---|---|
| 蓄力 | 右键 | 至少 5 tick；Riptide 时要求在水/雨中 | — | `startUsingItem`，自定义手臂姿态 |
| 投掷 | 松开右键 | `charge=min(useTicks/20,1)`；初速 `1.7×charge`；`shootFromRotation(player, xRot-50, yRot, 0, velocity, 1)` | 8 tick | 生成 `song_javelin`；消耗 2 耐久并 `shrink(1)`（创造模式不消耗/不掉耐久）；播放 `TRIDENT_THROW` |
| Riptide | 松开右键且 Riptide>0 且在水/雨中 | 速度 = 视线 ×`0.75×(level+1)` + 当前速度；`hurtMarked=true` | 20 tick | 玩家冲刺；播放 `TRIDENT_RIPTIDE_1/2/3`；消耗 2 耐久 |
| 近战命中 | `hurtEnemy` | 服务端 | 无 | 若玩家骑乘且目标未骑乘，启动拖拽（7.1） |
| 投掷物命中 | `SongJavelinProjectile.onHitEntity` | 伤害 `base+2`；`base = (0/1/2/3/4)×charge` | — | 击退、音效、Channeling 召雷；Loyalty 回返 |

### 7.6 陌刀（Sky-Piercing Halberd）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 | 成就 |
|---|---|---|---|---|---|
| 潜行命中 | `hurtEnemy` 且玩家潜行、目标存活 | 服务端；方向 = 玩家位置 − 目标位置（水平归一化）；距离过小则用玩家朝向；目标没有 bleed 才施加 | 无额外冷却 | `target.knockback(2.0, dx, dz)`；`bleed` 100 tick；主手额外 −2 耐久（加 super 共 3） | `get_sky_piercing_halberd/use_fangtian_hengzhan` |
| 骑乘拖拽 | 命中后 | 玩家骑乘、目标未骑乘、目标未被拖拽 | 5 秒 | 启动拖拽（7.1） | 无 |

### 7.7 唐横刀（Heng Saber）

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 | 成就 |
|---|---|---|---|---|---|
| 双击右键突刺 | `PlayerInteractEvent.RightClickItem`，主手 6 件横刀之一 | 第一次右键只记录时间；400 ms 内第二次右键触发；要求不在冷却、没有正在播放的突刺 | 10 tick + 2 耐久 | 前方 3 格、宽 1 格、高 2 格的 AABB 内所有生物：伤害 = 主手 `ATTACK_DAMAGE` 修饰符之和 + 2；施加 `cuts` 60 tick；排除玩家自己和已驯服且属于该玩家的马 | `get_ancient_gem/xiao_tang_dynasty_heng_saber` |
| 动画 | 突刺后 46 tick | `getAnimationProgress`：0–8 tick 伸出、8–38 保持、38–46 收回 | — | 第一人称前移 1 格、绕 X 轴 -60° | 无 |
| 中断 | 每 tick | 主手不再是开始时的物品、主手空、玩家挥动、副手非空 | — | 立即结束动画 | 无 |

注意：该事件在客户端和服务端都会触发；`executeStab` 没有 `level.isClientSide` 守卫，伤害在客户端调用 `hurt` 通常不生效，但动画 map 和冷却会被客户端写入；权威伤害来自服务端（D17）。

### 7.8 盾与立盾

| 过程 | 触发 | 公式/条件 | 冷却 | 效果 |
|---|---|---|---|---|
| 唐盾格挡 | 右键 | 原版 `ShieldItem` 使用逻辑 | — | 举盾 72000 tick；无自定义格挡事件 |
| 宋立盾放置 | J 键/技能包，主手/副手宋立盾，且 5 格内没有可收回的立盾实体 | 视线 5 格 ray trace 命中方块；`mayInteract` 检查；生成 `standing_shield` | 无 | 实体生命 = 物品最大耐久 − 损伤；物品 −1 |
| 宋立盾收回 | J 键，5 格内 ray trace 命中立盾实体 | 优先于戈/戟技能 | 无 | 取回存储物品并按实体剩余生命设置损伤；优先放回原手（空手时），否则背包/掉落；`discard` 实体 |
| 宋立盾旋转 | 潜行 + 右键实体（`interact`） | 主手；服务端 | — | 根据玩家在盾牌左/右旋转 ±15°（默认），播放按钮声 |
| 宋立盾修复 | 主手 `#minecraft:planks` 右键实体 | 生命 < 最大耐久；生命 <50 回满，否则 +50 | — | 消耗 1 木板，播放按钮声 |
| 立盾受击 | `hurt` | 火焰/爆炸/药水/区域效果云免疫；其他伤害除以 `Unbreaking+1` | — | 减血，0 时破碎并 `discard` |
| 立盾爆炸保护 | `ExplosionEvent.Detonate` | 爆炸在盾牌正面（dot>0）；安全区中心在盾前 4 格、半径 4 | — | 移除安全区内受影响方块和实体 |
| 立盾回收 | `ShieldActionPacket` | 射线命中实体，距离 5 | — | 见上“收回” |

### 7.9 唐马铠

`TangDynastyHorseJianProcedure`：

- `LivingHurtEvent`（HIGHEST）：受害者是 `Horse`，伤害直接实体是 `AbstractArrow`，且马铠是 `TangDynastyHorseItem` → 取消伤害。
- `ProjectileImpactEvent`（HIGHEST）：箭命中穿唐马铠的马 → `DENY`，箭速度反向 ×0.8 + 随机 X/Z ±0.15 + Y+0.1，`pickup=DISALLOWED`，`owner=null`。
- 不检查箭的主人，因此骑手自己的箭也会被反弹（D19）。

### 7.10 面甲（Face Guard）

`FaceGuardHelmetKHProcedures.execute` 由头盔 `inventoryTick` 调用（仅当头盔在护甲槽中）：

| 项 | 值 |
|---|---|
| 触发条件 | 玩家蹲下；服务端；头盔在护甲槽 |
| 冷却 | 60 tick（按 UUID 的 `COOLDOWN_MAP`） |
| 扫描 | 玩家包围盒 inflate 20；只取 `Monster`；距离平方 16–169（4–13 格） |
| 消耗 | 头盔 `hurtAndBreak(2)`；播放 `RAID_HORN` 音量 3 |
| 命中判定 | 玩家视线与“玩家眼睛→怪物”单位向量点积 >0.85 视为“看着”；看着 80%，否则 50% |
| 效果 | 清除目标/最后伤害来源；加入 60 tick 恐惧 map；以玩家位置为基准沿远离方向 25 格（`FACEGUARDHELMET_KH`）寻路，速度 1.5 |
| 恐惧维持 | `FaceGuardHelmetKHKeepFarProcedures.onLivingTick` 设计为 60 tick 内若目标是玩家则清目标，死亡/超时移除；但该类没有注册到事件总线（D42），因此恐惧维持实际不运行 |

### 7.11 陌刀步兵甲（Footmen Armor）

`FootmenarmorcompletetestingProcedure`，只有**陌刀步兵头盔**的 `inventoryTick` 每 tick 调用 `execute`；睚眦/渤海头盔虽然可被 `isFullFootmenArmorSet` 接受，但它们不调用 `execute`，因此替代头盔时低血/碰撞增益不生效（D43）：

| 条件 | 效果 |
|---|---|
| 全套（头盔可为陌刀步兵、睚眦、渤海；胸/腿/靴必须是陌刀步兵；缺钻石渤海，D05） | 才进入后续判定 |
| 生命 ≤30% 最大生命 且 主手不是戟 | `MovementSlowdown` 1 tick、amplifier 1（持续刷新，近似减速 II） |
| 生命 ≤3 | `DamageBoost` 100 tick II、`Absorption` 100 tick II、`MovementSpeed` 100 tick III |
| 冲刺且是玩家 | 对碰撞箱相交的生物造成 1 点 `player_attack` 伤害并沿视线推开（Y+0.5）；每次命中消耗 2 点 exhaustion（`causeFoodExhaustion`） |
| 受箭（`LivingHurtEvent`，直接实体是 `AbstractArrow`） | 设计为取消伤害、箭速度改为受害者视线 ×1.8、owner 改为受害者、`pickup=DISALLOWED`；但 `FootmenarmorcompletetestingProcedure` 没有注册到事件总线（D41），该分支实际不触发 |
| 攻击者是全套步兵甲且伤害类型为玩家/生物攻击 | 设计为 `setAmount(amount+2)` 后又 `setAmount(max(0, amount-2))`，净效果为 0（D04）；且同因未注册（D41）不触发 |
| 受害者生命 ≤3 且攻击者持戟 | 设计为攻击者主手戟冷却 40 tick；同因未注册（D41）不触发 |

### 7.12 明光/山文/黑锤/细鳞护甲规则

见 2.3.3。补充：

- `primaryArmorFamily` 取四件中“路径匹配最多”的族；只有该族计数 >0 才应用规则。混搭两套各 2 件时不会触发任何 4 件套效果。
- 明光族 = 早期/中期/晚期三种路径；头盔替代只对明光族生效。
- 黑锤/山文/细鳞的 `countPieces` 不做头盔替代。
- 护甲规则在 `LivingHurtEvent` 的 `HIGH` 优先级执行；拖拽攻击取消在 `LivingAttackEvent` 的 `HIGHEST`。
- 反射伤害使用 `DamageSource.thorns(defender)`，并用 `ThreadLocal` 防止递归。

### 7.13 染甲/洗色

| 过程 | 触发 | 效果 |
|---|---|---|
| 染料合成 | 工作台：1 件 `DyeableItem` 护甲 + ≥1 染料 | 平均颜色写入 `display.Color`；保留附魔/NBT；染料消耗 |
| 水锅洗色 | 右键 `WATER_CAULDRON`，手持带颜色的可染色护甲 | 移除 `display.Color`；水位 -1；播放 `GENERIC_SPLASH`；取消原版交互 |
| 工具提示 | 带颜色护甲 | 追加 `text.chineseweapons.color`（`#RRGGBB`，灰色） |

### 7.14 锻造台流程

见 3.2/3.3。补充：`ArmorCastingTableBlockEntity.findMatchingRecipe` 每次都遍历全部 137 个铸造配方，按注册顺序取第一个匹配；因此重复配方（相同输入）会固定使用先加载的那个。

### 7.15 其他

- `tips`：客户端首次登录且未安装 Patchouli 时，发送带链接的提示 `text.chineseweapons.tips.miss_patchouli`，链接到 CurseForge Patchouli 页面；只提示一次（静态 boolean）。
- `ChineseweaponsMod.ForgeEvent.onLivingHurt`：戟类攻击骑乘目标时 60% 下马（与 7.3 同）。
- `ChineseWeaponsCombatEvents.applyArmorTickRules`：每 tick 处理明光移速（实际关闭）和全套坐骑 +2 护甲（每 10 tick 一次）。

### 7.16 未生效/死代码过程

| 过程 | 设计触发 | 实际结果 |
|---|---|---|
| `ArmorcastingtableguipreventgivingawayitemsforfreeProcedure` | `ArmorcastingtableguiSlotMessage` 的槽位 1–10 变更 | `containerMenu instanceof Supplier` 永远 false，直接返回；且该包从未发送 |
| `ArmorcastingtableguioutputsynthesisjudgmentProcedure` | 同一包的槽位 11 | 同上，永远 early-return |
| `ArmorcastingtableguiSlotMessage` | 客户端槽位变更回调（设计） | GUI 代码没有发送该消息；旧通道的 message id 0 实际闲置 |
| `FootmenarmorcompletetestingProcedure.onLivingHurt` | `LivingHurtEvent` | 类未注册到事件总线，永不调用（D41） |
| `FaceGuardHelmetKHKeepFarProcedures.onLivingTick` | `LivingEvent.LivingTickEvent` | 类未注册到事件总线，永不调用（D42） |

---

## 8. 客户端

### 8.1 按键

| 项 | 值 |
|---|---|
| KeyMapping 字段 | `ChineseweaponsModKeyHandler.ACTION_KEY` |
| 翻译 key | `key.skill.text`（英文 “Place/Take Song Standing Shield”） |
| 默认键 | `GLFW_KEY_J` |
| 冲突上下文 | `KeyConflictContext.IN_GAME` |
| 分类 | `item_group.chineseweapons.chinese_weapons` |
| 触发 | 游戏中且没有打开界面时，每个 `consumeClick` 发送一个 `ShieldActionPacket` 到服务端 |
| 服务端行为 | 优先级：收回 5 格内立盾 → 戈钩取 → 3 格内戟止跑 → 主手宋立盾放置；详见 7.2/7.3/7.8 |

按键在 `FMLCommonSetupEvent` 之前由 `RegisterKeyMappingsEvent` 注册，仅在 `Dist.CLIENT` 执行（`ChineseweaponsMod.java:86-94`）。

### 8.2 物品属性

`ChineseWeaponsClientEvents.onClientSetup` 注册两个 `chineseweapons:blocking` 属性（`ChineseWeaponsClientEvents.java:25-44`）：

| 物品 | 属性 id | 值 |
|---|---|---|
| `tang_dynasty_shield` | `chineseweapons:blocking` | 正在使用且使用物品是该 stack → 1.0，否则 0.0 |
| `song_standing_shield` | `chineseweapons:blocking` | 同上 |

对应的物品模型 override 谓词写成 `chineseweapons:blocking`（`models/item/tang_dynasty_shield.json`、`song_standing_shield.json`），与注册的 id 一致；覆盖模型为 `custom/tang_dynasty_shield_on` / `custom/song_standing_shield_on`。

### 8.3 护甲模型与 LayerDefinition

`ChineseweaponsModModels` 注册 14 个 layer：

| 模型类 | 用途 | Layer id |
|---|---|---|
| `Modelmountain_character_armor` | 山文甲 | `chineseweapons:modelmountain_character_armor` |
| `Modelearly_tang_dynasty_ming_guang_armor` | 早期明光 | `chineseweapons:modelearly_tang_dynasty_ming_guang_armor` |
| `Modelmiddle_tang_dynasty_ming_guang_armor` | 中期明光 | `chineseweapons:modelmiddle_tang_dynasty_ming_guang_armor` |
| `Modellate_tang_dynasty_ming_guang_armor` | 晚期明光 | `chineseweapons:late_tang_dynasty_ming_guang_armor` |
| `Modelfootmen_armor` | 陌刀步兵 | `chineseweapons:modelfootmen_armor` |
| `Modelblack_chui_armor` | 黑锤 | `chineseweapons:modelblack_chui_armor` |
| `Modeltang_dynasty_infantry_armor` | 唐步兵 | `chineseweapons:tang_dynasty_infantry_armor` |
| `Modelfine_scale_armor` | 细鳞 | `chineseweapons:fine_scale_armor` |
| `Modelsuanni_helmet` | 睚眦盔 | `chineseweapons:suanni_helmet` |
| `Modelbohai_helmet` | 渤海盔 | `chineseweapons:bohai_helmet` |
| `Modelsong_dynasty_infantry_armor` | 宋步兵 | `chineseweapons:song_dynasty_infantry_armor` |
| `Modelface_guard` | 面甲 | `chineseweapons:face_guard` |
| `ModelSongJavelinModel` | 长矛投射物 | `chineseweapons:song_javelin` |
| `ModelSongStandingShield` | 立盾 | `chineseweapons:song_standing_shield` |

每个护甲件在 `initializeClient` 中按 `ArmorItem.Type` 组装 `HumanoidModel`：

- 头盔只放 `head`，其余部件为空 `ModelPart`。
- 胸甲放 `body` + 左右臂。
- 护腿放左右腿。
- 靴子放左右脚（细鳞模型用 `LeftBoots`/`RightBoots`）。
- 同时复制 `crouching`、`riding`、`young` 状态。

`ChineseweaponsModModels` 只注册 layer；实际 `EntityRenderersEvent.RegisterRenderers` 在 `ChineseweaponsModEvents`。

### 8.4 第一人称/手部动画

| 动画 | 触发 | 时长/参数 | 实现 |
|---|---|---|---|
| 戟旋转斩 | 客户端 `GlaiveSpinStartedEvent`（潜行右键时由 `tryStart` 在客户端 post） | 30 tick；前进 0–18%、下压 18–30%、旋转 30–86%、回收 86–100%；-360°，缩放 1.25；绕手部 pivot | `GlaiveSpinAnimationHandler` + `GlaiveClientEvents` |
| 戟止跑/突刺 | 右键生物或 J 键止跑 | 46 tick；0–8 伸出、8–38 保持、38–46 收回；绕 X -30°，前移 3/16 | `GlaiveItemClientEvent` + `GlaiveItemStopRunProcedure.getAnimationProgress` |
| 横刀突刺 | 双击右键 | 46 tick；0–8 伸出、8–38 保持、38–46 收回；前移 1 格、绕 X -60° | `TangDynastyHengSaberClientEvents` |
| 长矛蓄力 | 使用长矛 | 自定义 `HumanoidModel.ArmPose`（右臂 -180°/180°/-20°，左臂镜像）；第一人称额外位移/旋转/缩放 | `SongJavelinItem.ClientExtensions` |

动画状态都是客户端静态 map/字段；服务端只负责冷却与伤害。切换主手、挥动、副手非空会中断横刀动画。

### 8.5 实体渲染器

| 实体 | 渲染器 | 模型 | 纹理 |
|---|---|---|---|
| `song_javelin` | `SongJavelinRenderer` | `ModelSongJavelinModel` | `chineseweapons:textures/entity/<item_path>.png`（6 种长矛纹理） |
| `standing_shield` | `SongStandingShieldRenderer` | `ModelSongStandingShield` | 5 张：`song_standing_shield.png` + `_cracked_1..4.png`，按生命阈值 <400/<700/<1000/<1300 切换 |

- 长矛渲染器按实体 yaw/pitch 旋转，`renderToBuffer` 使用 `entity.getTextureLocation()`。
- 立盾渲染器 `getTextureLocation` 强转 `SongStandingShieldEntity`；阴影半径 0；渲染时按 `shieldYaw` 旋转、缩放 `(-1,-1,1)`、下移 1.5。
- 两者都在 `ChineseweaponsModEvents.registerEntityRenderers` 注册。

### 8.6 GUI 与颜色/染色

| 项 | 说明 |
|---|---|
| Screen 纹理 | `textures/gui/weapons_casting_table.png`，256×256 |
| 进度条 | 自定义 `fill` 绘制，读 `ArmorCastingTableBlockEntityCache`；有配方未完成时输出槽画灰遮罩 |
| 物品颜色 | `RegisterColorHandlersEvent.Item` 注册 `ItemColor`，`tintIndex==1` 且 `display.Color` 存在 → 返回颜色，否则白色 |
| 颜色过滤 | 按注册 id 路径匹配：`black_chui_armor_`、`_bohai_helmet`、`early/middle/late_tang_dynasty_ming_guang_armor_`、`fine_scale_armor_`、`footmen_armor_`、`mountain_character_armor_`、`song_dynasty_infantry_armor_`；**没有 `tang_dynasty_infantry_armor_`**（D16） |
| 护甲纹理 | `DyeableItem.getArmorTexture` 动态合并 base + overlay；染色时按亮度偏差调整 RGB；缓存 `TEXTURE_CACHE` |
| 工具提示 | `text.chineseweapons.color`（`#RRGGBB`） |
| 立盾 HP 名称 | `entity.standing_shield.hp`（`Remaining Durability: %d`），≤100 红色 |

`ArmorCastingTableBlockEntityCache` 是静态 `ConcurrentHashMap<BlockPos,Integer/Boolean>`；服务端进度包会更新它，Screen 只读它，不直接读 BE。

### 8.7 客户端提示

- `tips` 类只在客户端加载；首次登录且未安装 Patchouli 时发送带 `OPEN_URL` 点击事件的系统消息。
- 没有其他客户端配置界面或按键配置界面。

---

## 9. 网络

### 9.1 通道

| 通道 | id | 协议版本 | 用途 |
|---|---|---|---|
| `CHANNEL` | `chineseweapons:main` | `"1"`（客户端/服务端都要求 `"1"`） | 三个主消息 |
| `PACKET_HANDLER` | `chineseweapons:chineseweapons` | `"1"` | MCreator 生成的旧通道，仅注册了 `ArmorcastingtableguiSlotMessage` |

`CHANNEL` 在 `FMLCommonSetupEvent` 注册消息；`PACKET_HANDLER` 的 `messageID` 从 0 自增，通过 `ChineseweaponsMod.addNetworkMessage` 注册。

### 9.2 消息表

| 通道 | id | 消息类 | 方向 | 负载 | 处理效果 |
|---|---:|---|---|---|---|
| main | 0 | `GuDuoItem.SquashPacket` | 服务端 → 客户端 | `VarInt entityId` | 客户端把实体 id 与当前毫秒写入 `GuDuoItem.SQUASH`；渲染事件在 800ms 内按 `scaleY` 压扁实体 |
| main | 1 | `ShieldActionPacket` | 客户端 → 服务端 | 空（encode/decode 都是空） | J 键触发；先收回 5 格内立盾；否则戈钩取；否则戟止跑；否则放置宋立盾 |
| main | 2 | `ArmorCastingTableBlockProgressPacket` | 服务端 → 客户端 | `BlockPos pos` + `VarInt progress` + `boolean hasRecipe` | 更新客户端 BE 的 progress（如果存在）和 `ArmorCastingTableBlockEntityCache`；只发给 64 格内玩家 |
| legacy | 0 | `ArmorcastingtableguiSlotMessage` | 客户端 → 服务端（设计） | 6 个 int：`slotID, x, y, z, changeType, meta` | 按 slot 1–10 调用“防止免费拿走”过程，slot 11 调用“输出合成判断”；**该包从未被发送**（D06） |

### 9.3 网络注意

- `ShieldActionPacket` 没有字段，因此任何客户端都能触发；服务端会根据主手物品/附近实体决定行为，不会直接给物品。
- `ArmorCastingTableBlockProgressPacket` 的 handler 直接引用 `net.minecraft.client.Minecraft`；只在客户端接收时执行，专用服务器不会走该分支，但类加载风险需运行验证。
- 进度包在 `hasRecipe` 或 `progress` 变化时发送；如果 GUI 刚打开且服务端状态未变化，客户端可能依赖初始 0；`ArmorCastingTableBlockEntity` 没有在 `startOpen` 时主动同步。
- 旧通道 `PACKET_HANDLER` 与主通道同时存在，增加一个无用的协议握手；没有安全 token 或版本协商。

---

## 10. 世界生成

### 10.1 矿石

| 内容 | id/文件 | 值 |
|---|---|---|
| 配置特征 | `worldgen/configured_feature/ancient_gem_ore.json` | `minecraft:ore`，`size=16`，`discard_chance_on_air_exposure=0`，目标 `#forge:stone`，替换为 `chineseweapons:ancient_gem_ore` |
| 放置特征 | `worldgen/placed_feature/ancient_gem_ore.json` | `count=1`、`in_square`、`height_range` 均匀 `-12..15`、`biome` |
| 生物群系修饰符 | `forge/biome_modifier/ancient_gem_ore_biome_modifier.json` | `forge:add_features`，`biomes: forge:any`，`step: underground_ores` |
| 配置特征 | `.../deepslate_ancient_gem_ore.json` | `minecraft:ore`，`size=16`，目标 `#forge:stone`，替换为 `chineseweapons:deepslate_ancient_gem_ore` |
| 放置特征 | `.../deepslate_ancient_gem_ore.json` | `count=2`、`in_square`、`height_range` 均匀 `-32..2`、`biome` |
| 生物群系修饰符 | `forge/biome_modifier/deepslate_ancient_gem_ore_biome_modifier.json` | `forge:add_features`，`biomes: forge:any`，`step: underground_ores` |

### 10.2 特点与问题

- 两种矿都使用 `#forge:stone` 作为替换目标；深层矿并没有使用 `#forge:deepslate_ores` 或 `minecraft:deepslate_ore_replaceables`，因此深层变体也可能替换普通石头（取决于加载的 tag 内容），见 D21。
- 矿石方块是普通 `Block`，不是 `DropExperienceBlock`，**没有经验掉落**。
- 没有矿脉/大型矿脉、没有维度限制、没有生物群系黑名单；`forge:any` 表示所有生物群系。
- 远古宝石块 `ancient_gem_block` 不参与世界生成。
- 可用 `/place feature chineseweapons:ancient_gem_ore` 做静态验证，但它仍受 `#forge:stone` 目标约束。

---

## 11. 数据与资源

### 11.1 进度（advancements，26 个）

| id | 父进度 | 图标 | 条件 |
|---|---|---|---|
| `root` | — | `armor_casting_table` | 获得锻造台 |
| `get_ancient_gem` | root | `ancient_gem` | 获得远古宝石 |
| `get_ancient_gem/get_tang_dynasty_heng_saber` | get_ancient_gem | `diamond_tang_dynasty_heng_saber` | 获得任意横刀 |
| `get_ancient_gem/xiao_tang_dynasty_heng_saber` | 上一行 | `golden_tang_dynasty_heng_saber` | `impossible`；由横刀突刺代码授予 |
| `get_ancient_gem/get_tang_dynasty_shield` | get_ancient_gem | `tang_dynasty_shield` | 获得唐盾 |
| `get_armor_piece` | root | `diamond_armor_pieces` | 获得任意甲片/板 |
| `get_armor_piece/forging_stone` | get_armor_piece | `footmen_armor_forging_stone` | 获得任意锻石 |
| `get_armor_piece/get_bohai_hat` | forging_stone | `diamond_bohai_helmet` | 获得任意渤海盔 |
| `get_armor_piece/get_buren_armor` | forging_stone | `diamond_footmen_armor_helmet` | 同时获得陌刀步兵四件（四条件） |
| `get_armor_piece/get_early_mingguang` | forging_stone | `diamond_early_tang_dynasty_ming_guang_armor_helmet` | 早期明光四件 |
| `get_armor_piece/get_middle_mingguang` | get_early_mingguang | `diamond_middle_tang_dynasty_ming_guang_armor_helmet` | 中期明光四件 |
| `get_armor_piece/get_late_mingguang` | get_middle_mingguang | `diamond_late_tang_dynasty_ming_guang_armor_helmet` | 晚期明光四件；frame `challenge` |
| `get_armor_piece/get_shanwen_armor` | forging_stone | `diamond_mountain_character_armor_helmet` | 山文四件 |
| `get_armor_piece/get_suanni_helmet` | forging_stone | `diamond_suanni_helmet` | 任意睚眦盔 |
| `get_armor_piece/get_tang_horse_armor` | forging_stone | `diamond_tang_dynasty_horse_armor` | 任意唐马铠 |
| `get_armor_piece/get_wuchui_armor` | forging_stone | `diamond_black_chui_armor_helmet` | 黑锤四件 |
| `get_armor_piece/get_xilin_armor` | forging_stone | `diamond_fine_scale_armor_helmet` | 细鳞四件 |
| `get_dagger_axe` | root | `diamond_dagger_axe` | 获得任意戈 |
| `get_dagger_axe/dagger_axe_hook` | get_dagger_axe | `golden_dagger_axe` | `impossible`；钩取成功授予 |
| `get_dagger_axe/dagger_axe_hook_creeper` | get_dagger_axe | `creeper_head` | `impossible`；钩取 Creeper 授予；frame `goal` |
| `get_dagger_axe/dagger_axe_hook_has_cuts_xg` | get_dagger_axe | `enchanted_golden_apple` | `impossible`；被戈/戟命中并施加 cuts 的玩家获得 |
| `get_glaive` | root | `diamond_glaive` | 获得任意戟 |
| `get_glaive/use_gs` | get_glaive | `netherite_glaive` | `impossible`；旋转斩授予 |
| `get_glaive/get_green_loong_glaive` | get_glaive | `green_loong_glaive` | 获得绿龙戟；frame `challenge` |
| `get_sky_piercing_halberd` | root | `diamond_sky_piercing_halberd` | 获得任意陌刀 |
| `get_sky_piercing_halberd/use_fangtian_hengzhan` | get_sky_piercing_halberd | `golden_sky_piercing_halberd` | `impossible`；潜行命中授予 |

- 26 个进度里 20 个是 `inventory_changed`，6 个是 `impossible`（横刀突刺、戈钩取、Creeper、被割伤、戟旋转、陌刀潜行）。
- 所有 `inventory_changed` 条件都列出四材质/六材质的物品列表，不要求具体材质。
- 进度文件位于 `data/chineseweapons/advancements`。

### 11.2 战利品表（5 个文件，4 个有效）

| 文件 | 有效性 | 内容 |
|---|---|---|
| `loot_tables/blocks/ancient_gem_ore.json` | 有效 | 精准采集 → 矿石本身；否则 `ancient_gem` ×1，带 `fortune` 的 `ore_drops` 和 `explosion_decay` |
| `loot_tables/blocks/deepslate_ancient_gem_ore.json` | 有效 | 同上，掉落 `ancient_gem` |
| `loot_tables/blocks/ancient_gem_block.json` | 有效 | 掉落自身 |
| `loot_tables/blocks/armor_casting_table.json` | 有效 | 掉落自身 |
| `loot_tables/blocks/armorcastingtable.json` | **无效/重复** | 内容与上一行相同，但方块 id 是 `armor_casting_table`，该路径不会被使用（D18） |

- 矿石没有经验；`random_sequence` 分别为 `chineseweapons:blocks/<name>`。
- 精准采集分支没有 `silk_touch` 的 `match_tool` 之外条件；普通分支不检查工具，但方块 `requiresCorrectToolForDrops` 已在方块层限制。

### 11.3 标签

| 文件 | 内容 |
|---|---|
| `data/minecraft/tags/blocks/mineable/pickaxe.json` | 4 个方块：两种矿、锻造台、宝石块 |
| `data/minecraft/tags/blocks/needs_iron_tool.json` | 同上 4 个方块 |
| `data/minecraft/tags/items/swords.json` | 19 个：7 戟、6 戈、6 陌刀；**不含横刀/长矛/骨朵**（D23） |
| `data/minecraft/tags/items/netherite_ignores_lava.json` | 35 个条目；包含铁制唐步兵甲（与标签语义不符）、缺少下界合金长矛/骨朵/横刀/马铠/睚眦/渤海/面甲；即使 1.20.1 存在该标签，内容也不一致（D23） |
| `data/chineseweapons/tags/items/armor_pieces.json` | 7 个甲片/板：铁/钻石/金/石甲片、铁/钻石/金山文甲片 |
| `data/chineseweapons/curios/tags/items/head.json` | 4 个面甲值；其中 3 个没有命名空间（`iron_face_guard_helmet` 等），会被解析成 `minecraft:` 而失效（D24） |

### 11.4 村民交易

`ChineseweaponsModTrades` 给盔甲匠（`VillagerProfession.ARMORER`）4 级交易追加 8 条：

| 买入 | 卖出 | 最大次数 | 经验 | 价格倍率 |
|---|---|---:|---:|---:|
| 10 绿宝石 + 5 铁锭 | 早期明光锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 陌刀步兵锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 晚期明光锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 中期明光锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 山文锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 黑锤锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 唐步兵锻石 | 10 | 5 | 0.05 |
| 10 绿宝石 + 1 铁锭 | 细鳞锻石 | 10 | 5 | 0.05 |

- 8 个 `if (event.getType() == ARMORER)` 是重复判断，功能无影响。
- 全部在 `getTrades().get(4)`（大师级）添加；没有其他职业交易。

### 11.5 Curios

- `data/chineseweapons/curios/tags/items/head.json` 把 4 个面甲加入 `curios:head` 槽。
- 但 build.gradle 的 Curios 依赖被注释，mods.toml 也没有 Curios 依赖；没有 Curios 时该数据文件不会被加载。
- 3 个值缺少 `chineseweapons:` 命名空间，只有 `chineseweapons:diamond_face_guard_helmet` 有效（D24）。

### 11.6 `weapon_attributes`（Better Combat）

37 个 JSON，每个武器一个，全部只有一行 `parent`：

| 武器族 | parent | 数量 |
|---|---|---:|
| 戈 | `bettercombat:scythe` | 6 |
| 戟 | `bettercombat:glaive` | 7（含绿龙） |
| 骨朵 | `bettercombat:mace` | 6 |
| 陌刀 | `bettercombat:halberd` | 6 |
| 长矛 | `bettercombat:spear` | 6 |
| 横刀 | `bettercombat:sword` | 6 |

路径 `data/chineseweapons/weapon_attributes/<item>.json`。未声明 Better Combat 依赖；只有安装时生效。

### 11.7 `capabilities/weapons`（Epic Fight）

37 个 JSON，路径 `data/chineseweapons/capabilities/weapons/<item>.json`：

| type | 数量 | 属性 |
|---|---:|---|
| `epicfight:spear` | 19 | 戟 7 + 陌刀 6 + 长矛 6；长矛有 `one_hand`（armor_negation 8、impact 1.9、max_strikes 1）和 `two_hand`（0、1.3、3）；其他 spear 为 0/1.3/1 |
| `epicfight:greatsword` | 12 | 戈 6 + 骨朵 6；0/1.8/1（戈）、0/1.1/1（骨朵） |
| `epicfight:sword` | 6 | 横刀；0/1.1/1 |

同样未声明 Epic Fight 依赖，只有安装时生效。

### 11.8 Patchouli 手册

| 项 | 值 |
|---|---|
| book id | `chineseweapons:forging_notes` |
| book.json | `data/chineseweapons/patchouli_books/forging_notes/book.json` |
| 名称 key | `patchouli.chineseweapons.book.name` |
| 落地文本 | `patchouli.chineseweapons.book.landing_text` |
| 书纹理 | `chineseweapons:textures/gui/forging_notes.png` |
| 模型 | `chineseweapons:forging_notes` |
| 进度条颜色 | 前景 `FFFF55`，背景 `DDDDDD` |
| 创造标签 | `chineseweapons:chinese_weapons` |
| 分类 | 5 个：armor、materials、other、ovo、weapons |
| 条目 | 25 个：护甲 12、材料 2、其他 3、ovo 1、武器 7 |
| 页面类型 | spotlight 52、image 21、crafting 3、text 9、link 1 |
| 获取 | 安装 Patchouli 时，书 + `#chineseweapons:armor_pieces` 无序合成 `patchouli:guide_book`，NBT 指向本手册 |

`patchouli/PatchouliCompat.java` 与 `patchouli/AltarRecipeComponent.java` 都是 **0 字节空文件**，没有自定义页面/组件代码。

### 11.9 声音

| 项 | 值 |
|---|---|
| 注册 id | `chineseweapons:guduo`（`SoundEvent.createVariableRangeEvent`） |
| sounds.json | `guduo` → `chineseweapons:metal-pipe-clang` |
| 字幕 | `HYW`（不是 lang key，游戏内直接显示 `HYW`，D25） |
| 文件 | `assets/chineseweapons/sounds/metal-pipe-clang.ogg` |
| 使用 | 骨朵空中下砸，音量 0.2、音高 1.0 |

其他动作使用原版声音（`TRIDENT_THROW/HIT/HIT_GROUND/RETURN/RIPTIDE_*`、`SHIELD_BLOCK/BREAK`、`ANVIL_USE/PLACE`、`RAID_HORN` 等）。

### 11.10 语言文件

| 文件 | key 数 |
|---|---:|
| `en_us.json` | 396 |
| `zh_cn.json` | 399 |
| `zh_tw.json` | 388 |
| `lzh.json` | 388 |
| `ru_ru.json` | 386 |

关键 key：

- `item_group.chineseweapons.chinese_weapons`
- `key.skill.text`
- `block.chineseweapons.armor_casting_table`
- `effect.chineseweapons.bleed`、`effect.chineseweapons.cuts`
- `entity.chineseweapons.song_javelin`、`entity.chineseweapons.standing_shield`
- `text.chineseweapons.color`、`text.chineseweapons.tips.miss_patchouli`
- `jei.chineseweapons.category.weapons_casting_table_shaped`、`emi.category.chineseweapons.weapon_casting_shaped`
- 26 个进度的 `advancement.chineseweapons.*`
- Patchouli 的 `patchouli.chineseweapons.book.*`
- 8 个锻石的 `.tile` 提示

### 11.11 模型/纹理/数据规模

| 项 | 数量 |
|---|---:|
| 物品模型 `models/item/*.json` | 222 |
| 自定义物品模型 `models/custom/*.json` | 22 |
| 方块模型 `models/block/*.json` | 5（含 1 个无效重复） |
| 方块状态 `blockstates/*.json` | 5（含 1 个无效重复） |
| 纹理 `textures/**/*.png` | 348 |
| 护甲/马铠纹理 `textures/entities` | 57 |
| 实体纹理 `textures/entity` | 11（6 长矛 + 5 立盾） |
| GUI 纹理 | 3（锻造台、JEI、手册） |
| 音效文件 | 1 |
| 配方 JSON | 193 |
| 进度 JSON | 26 |
| 战利品表 JSON | 5 |
| 标签 JSON | 6 |
| 世界生成 JSON | 6（2 configured + 2 placed + 2 biome modifier） |
| Patchouli JSON | 31（1 book + 5 categories + 25 entries） |

### 11.12 根 `assets/minecraft`

仓库根存在 `assets/minecraft/models/item/shield.json` 与 `shield_blocking.json`，覆盖原版盾牌模型（`builtin/entity`，粒子纹理 `block/dark_oak_planks`）。这两个文件不在 `src/main/resources` 下，但会被资源包读取；本模组的唐盾/宋立盾使用自己的 `chineseweapons:custom/*` 模型，不依赖该覆盖。该覆盖可能影响原版盾牌外观，见 D26。

---

## 12. 兼容集成

### 12.1 KubeJS

**插件入口**：`resources/kubejs.plugins.txt` → `net.mirrorloong.chineseweapons.compat.kubejs.ChineseWeaponsKubeJSPlugin`。

| 能力 | 实现 |
|---|---|
| 类过滤 | `registerClasses` 允许 `net.mirrorloong.chineseweapons.compat.kubejs` |
| 全局绑定 | `event.add("ChineseWeapons", ChineseWeaponsKubeJSBindings.class)` |
| 配方 schema | `event.namespace("chineseweapons").register("weapon_casting_shaped", WeaponCastingRecipeSchema.SCHEMA)` |
| schema 字段 | `result`（OutputItem）、`pattern`（String[]）、`key`（TinyMap<Character,InputItem>）、`main_material`（InputItem） |
| 唯一输出 | `.uniqueOutputId(RESULT)` |
| 构造函数 | `(result, pattern, key, main_material)` |

**绑定方法（13 个）**：

| 方法 | 作用 | 底层 |
|---|---|---|
| `setReachBonus(itemId, bonus)` | 攻击距离加成 0–64 | `Skill`/`REACH_BONUSES` |
| `setHookDismountChance(itemId, chance)` | 戈钩取下马概率 | `Skill.HOOK_DISMOUNT` |
| `setHitEffectChance(itemId, chance)` | 命中 cuts 概率 | `Skill.HIT_EFFECT` |
| `setGlaiveDismountChance(itemId, chance)` | 戟命中骑手下马概率 | `Skill.GLAIVE_DISMOUNT` |
| `setGuduoArmorBlindnessChance(itemId, chance)` | 骨朵致盲的护甲阈值（命名是“概率”，实际是阈值） | `ALLWeapon.GUDUO_ARMORBLINDNESS` |
| `setGuduoHurtDownwardChance(itemId, chance)` | 骨朵下压格数 | `ALLWeapon.GUDUO_HURTDOWNWARD` |
| `setGuduoHurtSkillTickChance(itemId, chance)` | 骨朵下砸冷却 tick | `ALLWeapon.GUDUO_HURTSKILLTICK` |
| `setGuduoStopRidingChance(itemId, chance)` | 骨朵下马概率 | `Skill.GUDUO_STOPRIDING` |
| `setSongStandingShieldBehindCenterChance(chance)` | 立盾安全区中心距离 | `ALLWeapon.SONGSTANDINGSHIELD_BEHINDCENTER` |
| `setSongStandingShield_AngleChance(chance)` | 立盾旋转角度 | `ALLWeapon.SONGSTANDINGSHIELD_ANGLE` |
| `setSongStandingShieldEnduranceDisplay(value)` | 立盾耐久名称显示 | `Logic.SONGSTANDINGSHIELD_ENDURANCEDISPLAY`（字符串 `"true"/"false"`） |
| `setArmorGain(itemId, value)` | 该护甲是否参与套装计数 | `Logic.ARMOR_GAIN` |
| `setFaceGuardHelmetFarFromChance(itemId, chance)` | 面甲驱离格数 | `ALLWeapon.FACEGUARDHELMET_KH` |

**缺陷**：`setSongStandingShieldBehindCenterChance`、`setSongStandingShield_AngleChance`、`setSongStandingShieldEnduranceDisplay` 传入 `null` itemId；`WeaponScriptSettings.setArmorThreshold/setLogicChance` 直接 `ConcurrentHashMap.computeIfAbsent(null, ...)`，会抛 NPE（D27）。`getArmorThreshold/getLogicChance` 对 null 有保护，因此读取路径正常。

**KubeJS 文档**：`KUBEJS_COMPAT.md` 说明 `ItemEvents.modification` 可改 `attackDamage/attackSpeed/maxDamage`；配方写法为 `event.recipes.chineseweapons.weapon_casting_shaped(output, [3行图案], {key}, main_material)`；必须 3 行 ×3 字符，空格表示空位；`/reload` 只重载服务器脚本，启动脚本需重启。

### 12.2 JEI

`jei/WeaponsCastingTableRecipeJeiPlugin`：

- `@JeiPlugin`，plugin uid `chineseweapons:jei_plugin`。
- 配方类型 `chineseweapons:weapon_casting_shaped`，配方类 `WCTRecipe`。
- 分类 `WeaponsCastingTableRecipeJei`：157×62，背景 `textures/gui/jei/weapons_casting_table.png`，标题 key `jei.chineseweapons.category.weapons_casting_table_shaped`，图标锻造台。
- 输入槽：3×3（18 间隔，起点 3,3）+ 主材料槽（74,39）；输出槽（120,21）。
- 催化剂：锻造台。
- 传输处理器：菜单 `WeaponsCastingTableTypeGuiMenu`、类型 `DeferredGui.RegMenu`，输入槽 0–9（10 个）、玩家槽 11–46（36 个）；与菜单实际槽位一致，但 `quickMoveStack` 自身仍有错位（D02）。
- 只注册服务端配方列表的客户端镜像（`Minecraft.getInstance().getConnection().getRecipeManager()`）。

### 12.3 EMI

`compat/emi/ChineseWeaponsEmiPlugin`：

- `@EmiEntrypoint`。
- 分类 id `chineseweapons:weapon_casting_shaped`，图标/工作台锻造台。
- 遍历 `DeferredRecipe.WeaponCastingShapedType`，每个 `WCTRecipe` 转成 `WeaponCastingEmiRecipe`（157×62，背景同 JEI）。
- 输入：前 9 个 Ingredient 放 3×3（3,3 起），第 10 个放（74,39）；输出（120,21）。
- 只注册分类、工作台和配方，没有自定义传输处理器。

### 12.4 Patchouli

- 数据书 + 25 个条目 + 5 个分类；没有 Java 兼容代码（两个类都是 0 字节）。
- 书通过 `forge:conditional` 配方获得，条件 `forge:mod_loaded: patchouli`。
- 未安装 Patchouli 时客户端 `tips` 提示；`forge:conditional` 配方不会加载。

### 12.5 Curios / Better Combat / Epic Fight

- Curios：只有 `curios/tags/items/head.json`；无依赖、无代码。3 个值缺少命名空间。
- Better Combat：只有 37 个 `weapon_attributes` 数据；无依赖。
- Epic Fight：只有 37 个 `capabilities/weapons` 数据；无依赖。

---
## 13. 可测试断言（AutoJS 设计稿）

> 以下断言按 `3010fe3` 静态值设计，**尚未在游戏中执行**。AutoJS 暴露的 API 名称可能因版本不同而变化；建议用 `eval_js` 直接调用 Java 方法（`Item.of(...)`、`server.getRecipeManager()`、`player.getAttributeValue(...)`、`level.getBlockEntity(...)`、`entity.getPersistentData()` 等），并在每次断言后检查 `isError`、`ok`、`diagnostics`。涉及客户端渲染/按键的断言需在 client side 执行；涉及伤害、掉落、进度的断言需在 server side 执行。

### 13.1 注册与物品数值

| # | 层级 | 操作/断言 | 期望 |
|---|---|---|---|
| A01 | server | 遍历附录 A 的 202 个 id，`Item.of(id)` 或注册表查询 | 202 个全部存在，无 `minecraft:air` 替代 |
| A02 | client | 读取创造标签页 `chineseweapons:chinese_weapons` 的条目 | 数量 = 202；首项为 `iron_mountain_character_armor_helmet`，末项为 `tang_dynasty_infantry_armor_forging_stone` |
| A03 | server | `Item.of('chineseweapons:diamond_glaive')` 的 `maxDamage`；主手属性 `minecraft:generic.attack_damage`、`minecraft:generic.attack_speed`、`forge:entity_reach` | 2186；10；1.2；+1.0 |
| A04 | server | `netherite_guduo` | `maxDamage=2048`，攻击 9，速度 1.6，+1.0 实体距离，`canPerformSweepAttack=false` |
| A05 | server | `iron_song_javelin` | `maxDamage=270`；近战攻击 5；投掷物 `baseDamage` 在满蓄力为 2（命中再 +2） |
| A06 | server | `wooden_dagger_axe` | `maxDamage=118`，攻击 4，速度 1.9，+2.0 实体距离；木戈合成返还自身 |
| A07 | server | `iron_sky_piercing_halberd` | `maxDamage=300`，攻击 6，速度 1.2，+1.0 实体距离 |
| A08 | server | `golden_tang_dynasty_heng_saber` | `maxDamage=32`，攻击 5，速度 1.8 |
| A09 | server | 依次装备 `diamond_footmen_armor_helmet/chestplate` | 头盔护甲 3、耐久 442；胸甲 8、耐久 510 |
| A10 | server | 装备 `netherite_face_guard_helmet` | 护甲 5、耐久 494、韧性 3、击退抗性 0.1 |
| A11 | server | 全套 `iron_mountain_character_armor_*` / `diamond_mountain_character_armor_*` / `netherite_mountain_character_armor_*` | 护甲总值 19 / 22 / 22；下界合金额外韧性 3、KB 0.1 |
| A12 | server | `iron_tang_dynasty_horse_armor` 等的 `getDefense` | 8 / 11 / 18 / 25；`canBeDepleted=false` |
| A13 | server | `netherite_glaive`、`green_loong_glaive`、`netherite_sky_piercing_halberd` 的 `isFireResistant` | true；木/铁版本 false |
| A14 | server | 用木板修复 `wooden_glaive`，用铁锭修复 `iron_glaive` | `isValidRepairItem` 均为 true；用钻石修复铁戟为 false |
| A15 | client | `Item.of('chineseweapons:iron_bohai_helmet')` 与 `iron_suanni_helmet` 的 `DyeableItem` 判定 | 渤海实现 `DyeableItem`；睚眦不实现 |

### 13.2 配方与锻造台

| # | 层级 | 操作/断言 | 期望 |
|---|---|---|---|
| A16 | server | `getAllRecipesFor('chineseweapons:weapon_casting_shaped').size` | 137 |
| A17 | server | 统计 `minecraft:smithing_transform` / `crafting_shaped` / `crafting_shapeless` / `chineseweapons:crafting_special_dyeablearmor` / `forge:conditional` | 38 / 15 / 1 / 1 / 1 |
| A18 | server | 用 5 木板 + 2 木棍 + 主材料金锭 在锻造台匹配 `wooden_glaive` | `matches=true`，输出 1 `wooden_glaive` |
| A19 | server | `diamond_glaive + netherite_ingot` 的 smithing 配方 | 匹配，输出 `netherite_glaive` |
| A20 | server | 工作台放 1 件 `iron_black_chui_armor_chestplate` + 1 红色染料 | 染料配方匹配，输出 `display.Color` 为红色分量平均后的整数；保留原附魔 |
| A21 | server | 只放护甲不放染料，或放两件护甲 | `matches=false` |
| A22 | server | 染色护甲右键 `WATER_CAULDRON` | `display.Color` 被移除，水位 -1，播放 `GENERIC_SPLASH` |
| A23 | server | 放置 `armor_casting_table`，检查 `getBlockEntity` 类型 | `chineseweapons:armor_casting_table` |
| A24 | server | 右键锻造台 | 打开菜单 id `chineseweapons:gui.chineseweapons.weapons_casting_table`；Screen 为 `WeaponsCastingTableScreen` |
| A25 | server | 检查菜单槽位数量与索引 | 47 个槽；输出实际为索引 10（`quickMoveStack` 误以为 11，见 D02） |
| A26 | server | 放入 A18 的材料，等待至 `progress=100`（最坏约 280–300 tick） | 输出槽为 `wooden_glaive`；输入各减 1 |
| A27 | server | 取走输出 | 输出清空，`progress=0`，`onRecipeTaken` 已发送 0/false |
| A28 | server | 未完成时破坏锻造台 | 输入全部掉落；输出清空（未完成不掉落输出） |
| A29 | server | 完成时破坏锻造台 | 输入全部掉落，输出也掉落 |
| A30 | server | 用钻石镐挖 `ancient_gem_ore`；再用精准采集 | 前者掉 `ancient_gem`；后者掉矿石本身；均无经验 |
| A31 | server | `/reload` 两次，检查日志 | 出现 `Duplicate recipe detected`（静态 map 未清空导致的重复报告，D15） |

### 13.3 实体/盾/效果

| # | 层级 | 操作/断言 | 期望 |
|---|---|---|---|
| A32 | server | 用 `iron_song_javelin` 蓄力 20 tick 后松开 | 生成实体类型 `chineseweapons:song_javelin`；初速约 1.7；物品 -1、耐久 -2 |
| A33 | server | 给长矛附 Loyalty I，投出后等待回返 | 投掷物进入 `isReturning`，播放 `TRIDENT_RETURN`，最终物品回到背包或悬停头顶 |
| A34 | server | J 键/包放置 `song_standing_shield` | 生成 `chineseweapons:standing_shield`；实体生命 = 1670 - 物品损伤；物品 -1 |
| A35 | server | 再次 J 键对准立盾 | 实体 `discard`，物品回到空手/背包，损伤 = 1670 - 实体生命 |
| A36 | server | 潜行右键立盾 | yaw 改变 ±15°（默认），播放按钮声 |
| A37 | server | 主手木板右键受损立盾 | 生命 <50 回满，否则 +50；木板 -1 |
| A38 | server | 在立盾正面引爆 TNT | 盾后 4 格半径 4 内的方块/实体不受爆炸影响；盾牌受爆炸免疫 |
| A39 | server | 对目标施加 `chineseweapons:bleed` 100 tick，等待 20 tick | 目标生命 -1，获得 `Weakness` 与 `MovementSlowdown`；60 tick 后可能再次触发 |
| A40 | server | 对目标施加 `chineseweapons:cuts` 100 tick，目标冲刺 | 每秒至少 1 点，冲刺时额外 1 点；总伤害明显高于非冲刺 |
| A41 | server | `/enchant @s chineseweapons:cavalry_buster_star 1` | 失败/无此附魔（注册数为 0，D12） |
| A42 | server | 给穿唐马铠的马射箭 | 马无伤害；箭速度反向、owner=null、`pickup=DISALLOWED` |
| A43 | server | 玩家蹲下、面甲在头盔槽、4–13 格内生成僵尸 | 触发瞬间僵尸目标被清空并开始向远离玩家 25 格方向移动；头盔耐久 -2。由于恐惧维持未注册（D42），下一 AI tick 可能重新锁定玩家 |
| A44 | client | 骨朵空中命中后读取 `GuDuoItem.SQUASH` | 目标实体 id 出现在 map 中；800ms 内渲染 Y 缩放最小 0.6 |

### 13.4 战斗/护甲过程

| # | 层级 | 操作/断言 | 期望 |
|---|---|---|---|
| A45 | server | 潜行 + 右键持戟 | 冷却 20 tick；3 次伤害脉冲（t=0/11/22），总伤害 = 攻击力 -2；耐久 -3；获得 `get_glaive/use_gs` |
| A46 | server | 主手戈，7 格内有目标，按 J | 目标被拉近到约 2 格，受 2 点伤害；冷却 10 tick；获得 `get_dagger_axe/dagger_axe_hook` |
| A47 | server | 主手横刀，400ms 内右键两次 | 前方 3 格内目标受伤（攻击力修饰符和 +2），获得 `cuts` 60 tick；动画进度 >0；冷却 10 tick；获得 `get_ancient_gem/xiao_tang_dynasty_heng_saber` |
| A48 | server | 主手陌刀，潜行命中目标 | 目标被击退 2.0，获得 `bleed` 100 tick；主手额外 -2 耐久；获得 `get_sky_piercing_halberd/use_fangtian_hengzhan` |
| A49 | server | 主手骨朵，不在地面命中 | 目标受 `攻击力+2` 伤害、缓慢 100 tick、获得 `bleed` 60 tick；冷却约 10 tick；主手 -3 耐久 |
| A50 | server | 主手骨朵，地面潜行命中骑乘目标 | 30% 概率目标下马；成功冷却 60 tick；对目标造成一次攻击力伤害；坐骑伤害分支不可达（D40） |
| A51 | server | 主手戟/戈，副手放盾 | 下一 tick 副手清空，盾进入背包或掉落 |
| A52 | server | 玩家骑马，用陌刀/长矛命中未骑乘生物 | 目标 persistentData 出现 `chineseweapons:tether_until`，并被拉向玩家身后；5 秒后自动清除；跳跃 30% 提前清除 |
| A53 | server | 穿 1 件或多件山文甲受到剑伤害 | 最终伤害比无甲时少 3（该规则不要求全套） |
| A54 | server | 全套黑锤甲受到剑伤害，重复多次 | 约 30% 概率伤害 ×0.6；约 10% 概率反弹 50%（全套 60%/30%） |
| A55 | server | 全套明光甲受到 ≤3 点非火焰箭 | 伤害取消，箭速度 ×10；检查 `hurtMarked` |
| A56 | server | 全套步兵甲受到箭 | 静态代码下不取消（`onLivingHurt` 所在类未注册，D41）；若修复注册，应取消伤害、owner 改为受害者、速度改为受害者视线 ×1.8 |
| A57 | server | 全套步兵甲（陌刀步兵头盔）且生命 ≤3 | 获得力量 II、伤害吸收 II、速度 III，各 100 tick |
| A58 | server | 全套明光甲检查 `MOVEMENT_SPEED` 的 `MING_GUANG_SPEED_ID` | 不存在（速度效果实际未启用，D07） |
| A59 | server | 全套明光甲骑乘 10 tick | 坐骑护甲 +2；下马后修饰符仍在（D08） |

### 13.5 客户端/数据/集成

| # | 层级 | 操作/断言 | 期望 |
|---|---|---|---|
| A60 | client | 读取 `ACTION_KEY` | 默认 `GLFW_KEY_J`，翻译 key `key.skill.text`，分类 `item_group.chineseweapons.chinese_weapons` |
| A61 | client | 使用唐盾/宋立盾时读取 `chineseweapons:blocking` 属性 | 1.0；未使用时 0.0 |
| A62 | client | 染色护甲的 `ItemColor.getColor(stack,1)` | 等于 `display.Color`；未染色返回 `0xFFFFFF` |
| A63 | client | 检查 `ChineseweaponsModModels` 的 layer | 14 个 layer 全部注册，含 `song_javelin`、`song_standing_shield` |
| A64 | client | 触发戟旋转/横刀突刺后读取动画进度 | 旋转 30 tick、突刺 46 tick；进度最终回到 0 |
| A65 | server | 获得锻造台 | 授予 `chineseweapons:root` |
| A66 | server | 盔甲匠 4 级交易列表 | 至少 8 条本模组交易，均以绿宝石 + 铁锭换锻石 |
| A67 | server | 安装 Patchouli 后书 + `#chineseweapons:armor_pieces` 合成 | 得到 `patchouli:guide_book`，NBT `patchouli:book=chineseweapons:forging_notes` |
| A68 | server | `#minecraft:swords` 的模组条目 | 19 个；不含横刀/长矛/骨朵（D23） |
| A69 | server | `curios:head` 标签 | 只有 `chineseweapons:diamond_face_guard_helmet` 能解析到本模组；其余 3 个是 `minecraft:` 命名空间（D24） |
| A70 | server | `/place feature chineseweapons:ancient_gem_ore` | 在石头区域生成 `ancient_gem_ore`；深层变体同理 |
| A71 | server | KubeJS `ChineseWeapons.setReachBonus('chineseweapons:iron_glaive', 3.0)` 后读取实体距离 | 从 +1.0 变为 +3.0 |
| A72 | server | KubeJS `ChineseWeapons.setHitEffectChance('chineseweapons:iron_glaive', 0.99)` 后连续命中 | 绝大多数命中施加 `cuts`；`1.0` 与 `0.0` 都因范围校验抛 IllegalArgumentException |
| A73 | server | KubeJS 添加一条 `weapon_casting_shaped` 配方 | `getAllRecipesFor` 数量从 137 变为 138；删除后回到 137 |
| A74 | server | KubeJS `ChineseWeapons.setSongStandingShield_AngleChance(30)` | 抛 NPE（null itemId，D27），用于确认缺陷 |

---

## 14. 已知缺陷（分析发现，未修复）

> 以下全部是静态阅读结论，**不是本次文档任务的修复项**；修改代码不在任务范围内。标注“严重”表示可能导致专用服务器崩溃或核心功能不可用；“中”表示玩法/数据错误；“低”表示冗余、日志或资源问题。

| 编号 | 严重度 | 位置 | 问题 | 影响/验证建议 |
|---|---|---|---|---|
| D01 | 严重 | `entity/SongStandingShieldEntity.java:238-264` | 实体 `tick()` 在服务端分支调用 `Minecraft.getInstance().player` | 专用服务器没有 `net.minecraft.client.Minecraft`，立盾 tick 会触发 `NoClassDefFoundError`/崩溃。用 dedicated server 放置立盾即可复现 |
| D02 | 严重 | `gui/recipe/WeaponsCastingTableTypeGuiMenu.java:36-76, 87-135` | 9 个 3×3 槽（0–8）+ 主材料（9）+ 输出（10），但 `quickMoveStack` 把 11 当输出、0–10 当输入、12–46 当背包 | 输出槽快速移动会走错分支；索引 11（背包第一格）被当成输出；输入区包含输出槽。用 Shift 点击输出/背包第一格即可复现 |
| D03 | 中 | `WeaponsCastingTableTypeGuiMenu.java:145-148` | `stillValid` 永远 true | 玩家远离/方块被破坏后 GUI 仍可用；可能造成远程访问/状态不同步 |
| D04 | 中 | `procedures/armor/FootmenarmorcompletetestingProcedure.java:160-166` | 先 `setAmount(amount+2)`，再 `setAmount(max(0, amount-2))`，净变化为 0 | 步兵甲“攻击加成”实际不存在；断言的期望值应为无变化 |
| D05 | 中 | `FootmenarmorcompletetestingProcedure.java:18-64` | 头盔白名单包含金/铁/下界合金渤海，但**缺少钻石渤海** | 穿钻石渤海 + 三件步兵甲不判定为全套；与其他材质不一致 |
| D06 | 中 | `network/ArmorcastingtableguiSlotMessage.java`、`procedures/Armorcastingtablegui*Procedure.java` | 槽位包从未被发送；两个过程用 `containerMenu instanceof Supplier` / `Map` 判断，而 `AbstractContainerMenu` 既不是 `Supplier` 也不是 `Map` | 两个过程永远 early-return；相关“防白嫖/输出判断”是死代码。`tryConsumeInputs()` 也未被调用 |
| D07 | 中 | `procedures/armor/ChineseWeaponsCombatEvents.java:48-54, 216-235, 324-336` | 定义了 +20% 明光移速修饰符，但 `applyArmorTickRules` 每 tick 调用 `updateMingGuangSpeed(player,false)` | 明光套没有移速加成；断言 A58 可确认修饰符不存在 |
| D08 | 中 | `ChineseWeaponsCombatEvents.java:338-348` | 给坐骑加 `+2` 护甲后从不移除 | 下马/换甲后坐骑永久 +2 护甲，直到重载；应记录并清理 |
| D09 | 低 | `item/ReachSwordItem.java:40-56`、`item/GuDuoItem.java:31` | `SixBlockReachSwordItem` 构造仍传 `1.0D`，与 `FourBlockReachSwordItem` 相同 | 骨朵实际只有 +1.0 实体距离，不是“六格”；与命名/预期不符 |
| D10 | 中 | `item/SongJavelinItem.java:37-48` | `attackDamageModifier = displayedAttackDamage + tier.getAttackDamageBonus()`，`SwordItem` 又加一次 Tier 加成 | 长矛近战伤害被 Tier 加成重复计算（木/金 1、石 3、铁 5、钻石 7、下界 9）；木/金几乎无近战伤害 |
| D11 | 中 | `entity/SongJavelinProjectile.java:125-186, 279-285` | `onHitEntity` 未调用 `super.onHitEntity`；`inGround` 不设置；`tickDespawn` 在 `inGround && !isReturning` 时直接返回 | Piercing 附魔、暴击等 `AbstractArrow` 原生命中逻辑被绕过；落地投掷物永不自然消失，可积累实体；Loyalty + `pickup=ALLOWED` 存在复制/回返竞态风险 |
| D12 | 低 | `init/ChineseWeaponsModEnchantments.java:12-14` | 唯一附魔注册被注释 | `chineseweapons:cavalry_buster_star` 不存在；`CavalryBusterStar`/`DAGGERAXEItem` 是死代码 |
| D13 | —（澄清） | `ChineseweaponsMod.java:50-55, 83` | **任务提示所称“未注册染料护甲序列化器”不成立** | 序列化器已注册在 `chineseweapons:crafting_special_dyeablearmor`，JSON 也有对应 type；若重构树缺失，属于重构回归 |
| D14 | 低 | `data/chineseweapons/recipes/iron_mountain_character_armor_piece_plate.json` 与 `..._pieces_plate.json` | 两个文件配方完全相同（同材料、同输出） | 工作台会显示两条重复配方；不影响结果，但配方书冗余 |
| D15 | 中 | `recipes/WCTRecipeSer.java:25-71` | `TRUE_REG_RECIPE` 静态 map 从不清空；解析异常只记日志返回 null；签名不含输出 | `/reload` 重复打印 duplicate；坏配方静默消失；相同材料不同输出也会被当成重复 |
| D16 | 低 | `client/ChineseWeaponsClientEvents.java:46-72` | 颜色处理器路径过滤缺少 `tang_dynasty_infantry_armor_` | 唐步兵甲虽实现 `DyeableItem`，物品模型 tint 不会应用颜色；护甲纹理本身仍可由动态纹理染色 |
| D17 | 中 | `procedures/weapon/TangDynastyHengSabeCutsProcedures.java:64-138` | 右键事件在客户端与服务端都执行 `executeStab`，没有 `level.isClientSide` 守卫；也不区分主/副手 | 客户端会写动画 map/加冷却/尝试伤害；服务端权威但可能产生双端不一致；副手右键也可能触发主手技能 |
| D18 | 低 | `data/.../recipes`、`loot_tables/blocks/armorcastingtable.json`、`blockstates/armorcastingtable.json`、`models/item/armorcastingtable.json` | `stone_armor_pieces`/`stone_armor_pieces_plate` 没有配方；`armorcastingtable` 的 blockstate/loot/model 是不存在的方块 id 的重复文件 | 石甲片只能创造获得；重复文件不被读取，属于死资源 |
| D19 | 低 | `procedures/TangDynastyHorseJianProcedure.java:14-48` | 不检查箭的主人，所有 `AbstractArrow` 都被反弹 | 骑手/同队射中穿唐马铠的马也会被反弹；可能影响 PvP/队友 |
| D20 | 低 | `block/AncientgemoreBlock.java`、`DeepslateAncientgemoreBlock.java` | 使用普通 `Block`，不是 `DropExperienceBlock` | 挖矿无经验；与钻石矿手感不同 |
| D21 | 中 | `data/chineseweapons/worldgen/configured_feature/deepslate_ancient_gem_ore.json` | 深层矿的目标是 `#forge:stone`，不是深层石 tag | 深层矿可能替换普通石头；深层变体的分布不符合预期 |
| D22 | 低 | `src/main/resources/META-INF/MANIFEST.MF` | `Implementation-Version: 1.1.7`，`gradle.properties` 为 1.2.1.4 | 构建 manifest 会覆盖，通常无影响；直接读源 MANIFEST 时会误导 |
| D23 | 中 | `data/minecraft/tags/items/swords.json`、`netherite_ignores_lava.json` | `swords` 不含横刀/长矛/骨朵；`netherite_ignores_lava` 含铁制唐步兵甲、缺少多种下界合金物品 | 依赖 `#minecraft:swords` 的效果（部分模组/附魔）不覆盖横刀；lava 标签内容不一致，可能给铁甲错误防火语义 |
| D24 | 中 | `data/chineseweapons/curios/tags/items/head.json` | 4 个值中 3 个没有 `chineseweapons:` 命名空间 | 只有钻石面甲能进入 Curios 头槽；其余解析为 `minecraft:iron_face_guard_helmet` 等，失效 |
| D25 | 低 | `assets/chineseweapons/sounds.json` | `subtitle` 是字面量 `HYW`，不是翻译 key | 字幕直接显示 `HYW`；应使用 `subtitles.chineseweapons.guduo` 之类 |
| D26 | 低 | 仓库根 `assets/minecraft/models/item/shield.json`、`shield_blocking.json` | 全局覆盖原版盾牌模型；本模组盾牌并不使用该模型 | 可能改变原版盾牌外观/与其他资源包冲突；属于多余覆盖 |
| D27 | 严重 | `compat/kubejs/ChineseWeaponsKubeJSBindings.java:45-72`、`compat/WeaponScriptSettings.java:66-84` | 立盾相关三个绑定传 `null` itemId；`setArmorThreshold`/`setLogicChance` 对 `ConcurrentHashMap.computeIfAbsent(null, ...)` 会 NPE | KubeJS 调用 `setSongStandingShield_BehindCenter/ Angle/EnduranceDisplay` 会抛异常；断言 A74 可复现 |
| D28 | 中 | `procedures/armor/FaceGuardHelmetKHProcedures.java:38-62` | `hurtAndBreak(2, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()))` 用于头盔，但广播的是“正在使用的手” | 可能给主手物品播放损坏动画/触发错误事件；头盔本身仍掉耐久。`COOLDOWN_MAP` 和 `FEAR_MAP` 都不在退出时清理，长期服务器有泄漏 |
| D29 | 低 | `procedures/armor/DyeableItem.java:29, 133-253` | `TEXTURE_CACHE` 无上限；每种颜色/组合都注册新的 `DynamicTexture` | 大量染色会持续占用显存；无清理机制 |
| D30 | 中 | `recipes/WCTRecipeSer.java:38-53`、`recipes/WCTRecipe.java:25-33` | 解析硬编码每行 3 字符、9 个 pattern 位；`matches` 固定 10 次；空位用 `Ingredient.of(AIR)` | 任何非 3×3 的 KubeJS/数据包配方会得到 null Ingredient 或 NPE/永不匹配；KubeJS 文档也要求 3×3，但缺少校验/友好报错 |
| D31 | 低 | `entity/SongStandingShieldEntity.java:39-107` | `storedItemNbt` 不同步；`DATA_HP` 初始 1；`setHealthLogic` 在类初始化时读取配置 | 客户端无法读取存储物品；实体刚生成到首次 `setHealth` 之间 HP 为 1；运行期改配置不会生效 |
| D32 | 低 | `gui/recipe/WeaponsCastingTableTypeGuiMenu.java:20` | 菜单类标注 `@Mod.EventBusSubscriber(value=Dist.CLIENT)`，却被服务端方块逻辑构造 | 专用服务器加载该类时可能因 `MenuScreens` 客户端引用产生类加载风险；应拆分客户端注册 |
| D33 | 低 | `block/ArmorCastingTableBlockEntity.java:64-86` | 只在 `hasRecipe`/`progress` 变化时发同步包；新玩家进入 64 格不会收到当前状态 | 后进入的玩家 GUI 进度可能显示 0，直到下一次进度变化 |
| D34 | 低 | `entity/SongJavelinProjectile.java:44-62, 240-275`、`item/SongJavelinItem.java:105-131` | 投掷物 `pickup=ALLOWED`，Loyalty 又会把 `weaponStack` 加回背包 | 若玩家在回返途中拾取实体，存在多返回一份的风险；需运行验证 |
| D35 | 低 | `item/GuDuoItem.java:67-82` | 每次命中固定 `hurtAndBreak(2)`，再调用 `super.hurtEnemy` 扣 1 | 骨朵每次命中消耗 3 耐久，高于其他武器；与描述/平衡可能不符 |
| D36 | 低 | `procedures/weapon/DaggerAxeItemFierceHookProcedure.java:167-190` | 钩取伤害用 `new DamageSource(holder)`（无实体） | 伤害没有攻击者归属；护甲规则/仇恨/击杀归属可能不识别玩家 |
| D37 | 低 | `procedures/weapon/OffhandClearProcedure.java`、`GlaiveItemBase.java`、`DaggerAxeItemBase.java` | 主手戟/戈每 tick 清空副手 | 设计上强制双手，但会阻止副手盾/火把；且每 tick 检查有性能开销 |
| D38 | 低 | `item/GuDuoItem.java:34, 202-250` | `SQUASH` 静态 map 只在渲染时过期移除 | 实体死亡/卸载后条目可能残留；长期服务器轻微泄漏 |
| D39 | 低 | `block/ArmorCastingTableBlockEntity.java:97-122` | 进度到 100 时 `consumeInputs()` 每个槽减 1；若输入槽被部分占用/堆叠，可能一次消耗整组中的 1 个 | 与“每个材料消耗 1 个”一致，但若配方本意是消耗整套，需核对；当前行为是每槽 1 个 |
| D40 | 中 | `item/GuDuoItem.java:154-176` | `HurtStopHo` 先 `target.stopRiding()`，之后才检查 `target.getVehicle() != null` 并对坐骑造成伤害 | 坐骑伤害分支永远不可达；下马攻击实际只伤害骑手，不伤害坐骑 |
| D41 | 严重 | `procedures/armor/FootmenarmorcompletetestingProcedure.java:133-167` | 类上没有任何 `@Mod.EventBusSubscriber`，也没有 `MinecraftForge.EVENT_BUS.register`；`onLivingHurt` 的 `@SubscribeEvent` 不会生效 | 步兵甲的箭矢反弹、攻击者 +2/-2、低血时攻击者戟冷却等 `LivingHurtEvent` 逻辑全部不运行；只有头盔 tick 调用的 `execute`（低血/碰撞）仍工作 |
| D42 | 中 | `procedures/armor/FaceGuardHelmetKHKeepFarProcedures.java` | 同样没有注册到事件总线，`onLivingTick` 的 `@SubscribeEvent` 不会生效 | 面甲驱离后 60 tick 恐惧维持不运行；怪物可立即重新锁定玩家。`FEAR_MAP` 只写不读 |
| D43 | 中 | `item/*footmenarmorItem.java`、`FootmenarmorcompletetestingProcedure.java:18-64` | 只有 4 种陌刀步兵头盔的 `inventoryTick` 调用 `execute`；但 `isFullFootmenArmorSet` 允许睚眦/渤海头盔替代 | 使用睚眦/渤海替代头盔时，全套判定通过但低血/碰撞 tick 增益完全不触发；与“替代头盔”的设计不一致 |

---
## 附录 A：完整注册 id 清单（202 个物品 + 4 个方块）

### A.1 护甲（132 件）

以下 `{helmet,chestplate,leggings,boots}` 表示展开为四个完整 id；`{mat}` 按套装材料展开。

| 套装 | 前缀 | 材料 | 数量 |
|---|---|---|---:|
| 山文甲 | `{mat}_mountain_character_armor_{helmet,chestplate,leggings,boots}` | iron/golden/diamond/netherite | 16 |
| 早期明光甲 | `{mat}_early_tang_dynasty_ming_guang_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 中期明光甲 | `{mat}_middle_tang_dynasty_ming_guang_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 晚期明光甲 | `{mat}_late_tang_dynasty_ming_guang_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 陌刀步兵甲 | `{mat}_footmen_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 黑锤甲 | `{mat}_black_chui_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 细鳞甲 | `{mat}_fine_scale_armor_{helmet,chestplate,leggings,boots}` | 四材质 | 16 |
| 唐步兵甲 | `tang_dynasty_infantry_armor_{helmet,chestplate,leggings,boots}` | iron | 4 |
| 宋步兵甲 | `song_dynasty_infantry_armor_{helmet,chestplate,leggings,boots}` | iron | 4 |
| 睚眦盔 | `{mat}_suanni_helmet` | 四材质 | 4 |
| 渤海盔 | `{mat}_bohai_helmet` | 四材质 | 4 |
| 面甲 | `{mat}_face_guard_helmet` | 四材质 | 4 |

展开示例（每个前缀同理）：

```text
iron_mountain_character_armor_helmet
iron_mountain_character_armor_chestplate
iron_mountain_character_armor_leggings
iron_mountain_character_armor_boots
golden_mountain_character_armor_helmet
...
netherite_mountain_character_armor_boots
```

### A.2 武器（37 件）

**戈（6）**

```text
wooden_dagger_axe
stone_dagger_axe
iron_dagger_axe
golden_dagger_axe
diamond_dagger_axe
netherite_dagger_axe
```

**戟（7）**

```text
wooden_glaive
stone_glaive
iron_glaive
golden_glaive
diamond_glaive
netherite_glaive
green_loong_glaive
```

**横刀（6）**

```text
wooden_tang_dynasty_heng_saber
stone_tang_dynasty_heng_saber
iron_tang_dynasty_heng_saber
golden_tang_dynasty_heng_saber
diamond_tang_dynasty_heng_saber
netherite_tang_dynasty_heng_saber
```

**陌刀（6）**

```text
wooden_sky_piercing_halberd
stone_sky_piercing_halberd
iron_sky_piercing_halberd
golden_sky_piercing_halberd
diamond_sky_piercing_halberd
netherite_sky_piercing_halberd
```

**宋长矛（6）**

```text
wooden_song_javelin
stone_song_javelin
iron_song_javelin
golden_song_javelin
diamond_song_javelin
netherite_song_javelin
```

**骨朵（6）**

```text
wooden_guduo
stone_guduo
iron_guduo
golden_guduo
diamond_guduo
netherite_guduo
```

### A.3 盾（2）

```text
tang_dynasty_shield
song_standing_shield
```

### A.4 唐马铠（4）

```text
iron_tang_dynasty_horse_armor
golden_tang_dynasty_horse_armor
diamond_tang_dynasty_horse_armor
netherite_tang_dynasty_horse_armor
```

### A.5 甲片/宝石/锻石（23）

**甲片/板（14）**

```text
iron_armor_pieces
iron_armor_pieces_plate
diamond_armor_pieces
diamond_armor_pieces_plate
golden_armor_pieces
golden_armor_pieces_plate
stone_armor_pieces
stone_armor_pieces_plate
iron_mountain_character_armor_pieces
iron_mountain_character_armor_pieces_plate
diamond_mountain_character_armor_pieces
diamond_mountain_character_armor_pieces_plate
golden_mountain_character_armor_pieces
golden_mountain_character_armor_pieces_plate
```

**宝石（1）**

```text
ancient_gem
```

**锻石（8）**

```text
early_tang_dynasty_ming_guang_armor_forging_stone
footmen_armor_forging_stone
tang_dynasty_infantry_armor_forging_stone
fine_scale_armor_forging_stone
late_tang_dynasty_ming_guang_armor_forging_stone
middle_tang_dynasty_ming_guang_armor_forging_stone
mountain_character_armor_forging_stone
black_chui_armor_forging_stone
```

### A.6 方块物品（4）

```text
armor_casting_table
ancient_gem_ore
deepslate_ancient_gem_ore
ancient_gem_block
```

### A.7 方块注册（4，无独立物品）

```text
chineseweapons:armor_casting_table
chineseweapons:ancient_gem_ore
chineseweapons:deepslate_ancient_gem_ore
chineseweapons:ancient_gem_block
```

### A.8 其他注册 id

```text
实体：chineseweapons:song_javelin, chineseweapons:standing_shield
效果：chineseweapons:bleed, chineseweapons:cuts
音效：chineseweapons:guduo
配方类型：chineseweapons:weapon_casting_shaped
配方序列化器：chineseweapons:weapon_casting_shaped, chineseweapons:crafting_special_dyeablearmor
菜单：chineseweapons:gui.chineseweapons.weapons_casting_table
创造标签：chineseweapons:chinese_weapons
方块实体：chineseweapons:armor_casting_table
```

---

### 分析范围声明

- 本文只覆盖 `3010fe3` 的 `src/main/java` 与 `src/main/resources`，以及同提交的 `build.gradle`、`gradle.properties`、`KUBEJS_COMPAT.md`、`README_CN.md`、根 `assets/minecraft` 覆盖文件。
- 没有使用当前 `common/`/`targets/` 重构代码作为基线。
- 所有数值来自静态源码/JSON；没有运行游戏，不能把本文当作运行时验证记录。可测试断言需要在 Forge 1.20.1 客户端/专用服务器中通过 AutoJS 实际执行后，才能标记为“已验证”。

---

## 15. 类别功能计数（收尾）

| 类别 | 数量 | 明细 |
|---|---:|---|
| 注册物品 | **202** | 护甲 132 + 武器 37 + 盾 2 + 马铠 4 + 材料 23 + 方块物品 4 |
| 护甲 | **132 件 / 42 个变体族** | 9 个套装（7 个四材质 + 2 个单材质）= 30 个套装-材质变体；3 个头盔族 ×4 材质 = 12 个头盔变体；共 30+12=42 族，132 件 |
| 武器 | **37** | 戈 6、戟 7、横刀 6、陌刀 6、长矛 6、骨朵 6 |
| 盾 | **2** | 唐盾、宋立盾 |
| 马铠 | **4** | 铁/金/钻石/下界合金 |
| 材料/宝石/锻石 | **23** | 甲片/板 14 + 宝石 1 + 锻石 8 |
| 方块 | **4** | 锻造台、远古宝石矿、深层矿、宝石块 |
| 方块实体 | **1** | 锻造台 |
| 实体 | **2** | 长矛投射物、立盾 |
| 状态效果 | **2** | 流血、割伤 |
| 附魔 | **0** | `cavalry_buster_star` 未注册 |
| 配方类型 / 序列化器 | **1 / 2** | 锻造台类型 + 两个序列化器（铸造、染料） |
| 配方 JSON | **193** | 铸造 137、Smithing 38、Shaped 15、Shapeless 1、染料特殊 1、条件 1 |
| 进度 | **26** | 20 个 `inventory_changed` + 6 个 `impossible` |
| 战利品表 | **5 个文件 / 4 个有效** | 两矿、宝石块、锻造台；`armorcastingtable.json` 无效重复 |
| 标签 | **6** | 4 个 `minecraft` + 甲片 + Curios 头 |
| 世界生成 | **6 个文件** | 2 configured + 2 placed + 2 biome modifier；2 种矿 |
| 网络包 | **4** | 3 个主通道（Squash、ShieldAction、Progress）+ 1 个未发送的旧槽位包 |
| 程序/规则类 | **17** | `procedures` 下 17 个文件（含 `DyeableItem` 接口、`tips`） |
| 客户端 | 2 实体渲染器、12 护甲模型、14 layer、1 按键、2 item property、4 手部/第一人称动画、1 GUI、1 颜色处理器 | — |
| 兼容集成 | **4 个代码集成** | KubeJS、JEI、EMI、Patchouli；另 Better Combat 37、Epic Fight 37、Curios 1 个标签为纯数据 |
| 数据驱动兼容 | **37 + 37 + 1** | Better Combat 武器属性 37、Epic Fight capabilities 37、Curios 头标签 1 |
| Patchouli | **31 个 JSON** | 1 book + 5 categories + 25 entries；页面 52 spotlight / 21 image / 3 crafting / 9 text / 1 link |
| 语言文件 | **5** | en_us 396、zh_cn 399、zh_tw 388、lzh 388、ru_ru 386 key |
| 物品模型/纹理 | **222 / 22 / 348** | 物品模型、自定义模型、纹理总数 |
| 按键绑定 | **1** | J |
| GUI | **1** | 锻造台 Screen |
| 可测试断言 | **74** | 本文第 13 节 A01–A74 |

