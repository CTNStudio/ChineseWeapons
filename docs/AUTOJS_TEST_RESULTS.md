# AutoJS 功能验证记录（2026-11-06）

基线功能清单见 [FEATURES.md](FEATURES.md)，它依据原始单版本提交 `3010fe3` 静态整理。
本文记录重构后各 target 在真实运行游戏里的验证结果。**没有跑到的项目明确标为 BLOCKED，不按通过处理。**

## 1. 环境与方法

- 用各 target 自己的 wrapper 启动 `runServer`，AutoJS 端点 `http://127.0.0.1:25585/mcp`。
- AutoJS 版本来自本机 `D:/work/AutoJS/targets/*/build/libs/`，通过本地 Maven 布局 `D:/work/autojs-maven` 提供给各 target。
- 每次测试先 `status` 确认 `server` side 可用，再用 `eval_js` 执行断言，检查 `ok`/`isError`/返回值。
- 服务器 EULA 沿用本机其他测试实例已接受的 Minecraft EULA；测试世界放在各 target 的 `run/` 下。
- 客户端/GUI/渲染未验证：本机 dev client 缺少 LWJGL natives（`lwjgl.dll`），无法启动客户端。
- 测试脚本：`D:/work/tmp/checks.py`（临时脚本，未入库）；功能清单与断言语义见 FEATURES.md 第 13 节。

## 2. 结果矩阵

| Target | 运行时状态 | 结果 |
| --- | --- | --- |
| fabric-1.20.1 | 启动成功，server side 可用 | **25/25 通过**（含配方匹配、铸造台、流血 tick） |
| fabric-1.21.1 | 启动成功，server side 可用 | **20/20 通过**（耐久修复后） |
| fabric-26.1 | 已修复方块/装备资产/物品 id，启动推进到数据加载；配方解码报 `Item ... does not have components yet` | BLOCKED |
| neoforge-1.21.1 | 生产安装 + AutoJS 启动成功 | **6/6 基础检查通过** |
| neoforge-26.1 | 未启动；预计同时受 AT 与 26.1 item id 影响 | BLOCKED |
| forge-1.20.1 | 生产安装 + reobf jar + AutoJS 生产 jar 启动成功；server side 可用 | **基础 6/6 通过**（注册数量、方块实体、实体召唤） |
| forge-1.19.2 | 生产安装 + AutoJS 启动成功 | **6/6 基础检查通过** |
| forge-1.18.2 | 生产安装 + AutoJS 启动成功 | **6/6 基础检查通过** |

## 3. Fabric 1.20.1：通过项

以下 25 项全部通过：

1. 物品注册数 = 202，全部 `chineseweapons:` 且非 air。
2. 方块注册数 = 4。
3. 实体类型 = 2。
4. 状态效果 = 2。
5. 共享创造标签条目 = 202。
6. `diamond_glaive` 最大耐久 = 2186。
7. `netherite_guduo` 最大耐久 = 2048。
8. `iron_song_javelin` 最大耐久 = 270。
9. `wooden_dagger_axe` 最大耐久 = 118。
10. `iron_sky_piercing_halberd` 最大耐久 = 300。
11. `golden_tang_dynasty_heng_saber` 最大耐久 = 32。
12. `iron_footmen_armor_helmet` 护甲 4、耐久 176。
13. `netherite_face_guard_helmet` 护甲 4（原始数组 `[5,7,9,4]` 的 helmet 项）。
14. `weapon_casting_shaped` 配方数 = 137。
15. `crafting_special_dyeablearmor` 序列化器已注册（重构中曾丢失，测试前已恢复）。
16. 放置 `armor_casting_table` 后存在 `ArmorCastingTableBlockEntity`。
17. 召唤 `chineseweapons:standing_shield` 成功。
18. 召唤 `chineseweapons:song_javelin` 成功。
19. 对僵尸施加 `bleed` + `cuts` 成功，活跃效果数 = 2。
20. 进度数（`chineseweapons:` 命名空间）= 26。
21. `minecraft:swords` 中本模组条目 = 19。
22. `standing_shield` 在服务端 tick 不再加载客户端 `Minecraft` 类（D01 修复后回归测试）。
23. `wooden_glaive` 自定义配方用 5 木板 + 2 木棍 + 金锭匹配成功，输出 `wooden_glaive`。
24. 锻造台放入同一材料后连续 tick 至 `progress=100`，输出槽得到 1 个 `wooden_glaive`。
25. 流血效果在加载区块内按 20 tick 造成 1 点伤害，并附带虚弱/缓慢（血量 20→14，效果持续递减）。

## 4. Fabric 1.21.1：通过项与失败项

通过：物品/方块/实体/效果数量、创造标签、glaive/saber 耐久、护甲、137 条铸造配方、染料序列化器、方块实体、两种实体、双效果、26 个进度、19 个 swords 标签条目。

初测时 3 项自定义耐久失败（gu duo 2031、javelin 250、halberd 250）。原因是 1.21.1 的 `TieredItem` 构造函数把 `MAX_DAMAGE` 写成 `Tier#getUses()`，覆盖了 `Properties.durability(...)`。

已修复：新增 target 侧 `WeaponTier.withUses(base, uses)` 包装 Tier，让 `TieredItem` 写入正确的耐久；修复后 Fabric 1.21.1 **20/20 通过**（2048/270/300）。

## 5. 测试中发现并已修复的问题

| # | 问题 | 影响 | 处理 |
| --- | --- | --- | --- |
| 1 | 重构后 `crafting_special_dyeablearmor` 序列化器未注册 | 所有版本的染料护甲配方无法加载 | 在 8 个 target 的 `DeferredRecipe` 中恢复注册；Fabric 1.20.1 运行验证通过 |
| 2 | Fabric `FabricArmorItem` 在类签名中引用客户端 `HumanoidModel`/`ArmorModelProvider`/`ArmorTextureProvider` | Fabric 1.20.1/1.21.1 专用服务器启动崩溃（`Cannot load ... client ...`） | 物品不再实现客户端接口；模型/贴图逻辑移到客户端 `ArmorRenderer` + `DyeableArmorTextures` |
| 3 | `DyeableItem` 直接引用 `Minecraft`/`DynamicTexture` | Fabric 专用服务器加载护甲时崩溃 | 拆出客户端专用 `DyeableArmorTextures`，共享接口只保留颜色数据 |
| 4 | `SongStandingShieldEntity` 服务端 tick 调用 `Minecraft.getInstance().player`（原始 D01） | 专用服务器放置立盾崩溃 | 改为服务端 `level().players()` + 安全区判断，6 个 target 同步修复 |
| 5 | `fabric-1.21.1` 的 AutoJS hot-debug 被注释 | 无法运行测试 | 重新启用；Fabric API 从 `0.116.15` 提升到 `0.116.17+1.21.1` 以满足 AutoJS 1.0.1 |
| 6 | 26.1 方块构造前未设置 `Block id` | Fabric 26.1 启动崩溃 | 4 个方块构造函数接收 `ResourceKey<Block>` 并 `Properties.setId`；Fabric/NeoForge 26.1 已改 |
| 7 | Fabric 26.1 装备资产用了 `EquipmentAssets.createId`，被加上 `minecraft:` 前缀 | 启动崩溃 `Non [a-z0-9/._-] character ...` | 改为 `ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(...))` |

## 6. 未解决的运行时阻塞

1. **26.1 数据初始化**：物品/方块/装备资产的 id 问题已修复，服务器已能完成注册与资源加载，但在配方解码时报 `Item chineseweapons:... does not have components yet`，随后 `Failed to initialize server`。需要继续核对 26.1 `ItemStackTemplate`/组件绑定与自定义 recipe codec 的初始化顺序。
2. **NeoForge dev run**：补 `accesstransformers-11.0.2.jar` 后不再报“library missing”，但 Bootstraplauncher 需要 `net.neoforged.accesstransformer.parser` 模块；需要把 `at-parser` 放进 module path（或用完整 AT 发布构件/生产安装）。
3. **Forge dev run**：运行期缺少 `com.mojang.serialization.DynamicOps`/`oshi` 等 MC 运行时库；1.20.1 已通过生产安装验证，1.19.2/1.18.2 仍待按同一方式安装。
4. **客户端/GUI/渲染**：本机 dev client 无法加载 LWJGL natives；按键、模型、屏幕、粒子、颜色等 FEATURES 第 13.5 节断言均未执行。
5. **玩法级断言**：没有在线玩家，依赖真实玩家的钩子（右键、潜行、攻击、KubeJS 交互、交易、进度授予）未执行；本轮覆盖注册、数值、配方匹配、锻造台、实体、效果和标签。

## 7. 结论

- 运行时验证目前覆盖 **Fabric 1.20.1（25/25）** 和 **Fabric 1.21.1（20/20）**；其余 6 个 target 因环境/26.1 数据初始化问题被阻塞，不能视为功能健全。
- 测试发现并修复了 10 个会直接导致服务器崩溃、配方失效或数值错误的问题；26.1 配方组件初始化和 NeoForge/Forge dev 运行环境仍未解决。
- 在修复 26.1 item id、NeoForge AT、Forge 运行时依赖和客户端 natives 之前，不应把其余版本标记为已验证。

## 8. 最新修复后的状态（追加）

- 1.21.1 自定义耐久已修复：`WeaponTier.withUses` 包装 Tier，Fabric 1.21.1 的 gu duo/javelin/halberd 恢复为 2048/270/300，20/20 通过。
- 26.1 已修复：方块 `Properties.setId`、装备资产命名空间、物品 `Properties.setId`、方块物品/普通物品工厂。服务器现在能完成注册和资源加载，但在自定义配方 JSON 解码时报 `Item chineseweapons:... does not have components yet`，随后初始化失败。
- NeoForge 1.21.1 补 `accesstransformers-11.0.2.jar` 后，启动器要求 `net.neoforged.accesstransformer.parser` 模块；需要把 `at-parser` 放入 module path，而不是仅放 classpath。
- Forge 1.20.1 已改用生产安装并通过基础 AutoJS 检查；1.18.2/1.19.2 尚未安装生产实例。
- Forge 1.20.1 已改用生产服务端安装（Forge installer + reobf jar + AutoJS forge 1.0.0 + Rhino 2001.2.2），AutoJS 端点可用；`findItems` 202、`findBlocks` 4、`findEntities` 2、锻造台方块实体、立盾/标枪召唤全部通过。
- 修复了 Forge 1.20.1 入口构造函数：从 `ChineseWeaponsForge(FMLJavaModLoadingContext)` 改为无参 + `FMLJavaModLoadingContext.get()`，否则 Forge 报 `NoSuchMethodException: <init>()`。
- Forge dev run 还需要补齐 MC 运行时库（`datafixerupper`、`authlib`、`oshi`、`netty` 等）；这些是本地 `minecraft-dependencies` 镜像缺项，不是模组代码缺陷。生产安装可绕过，代价是需要安装器下载。
- Forge 1.19.2/1.18.2 生产服务端已跑通；同时修复了 1.19.2/1.18.2 的 38 条 `minecraft:smithing_transform` 配方（该类型 1.20 才存在），改为 `minecraft:smithing` 并加入 target 资源，否则这两个版本会大量配方解析失败。
- NeoForge 1.21.1 生产服务端已跑通（NeoForge installer + reobf jar + AutoJS + Rhino）；本地镜像预载 neoforge/neoform/安装器依赖后可完成安装。
- 26.1 组件绑定已尝试：服务器越过 `Item id not set` 和配方组件错误，进入 damage_type 标签解析阶段，当前报缺失 `minecraft:damage_type/is_fire` 标签；需把组件绑定延后到标签可用之后或改用延迟组件。
- 八个 target 与 `common` 在当前代码上均构建通过。

## 9. Fabric 1.20.1 客户端/玩家测试

- 客户端 `runClient --args="--quickPlaySingleplayer testworld"` 进入世界，AutoJS client+server 均可用，`AutoJS.background(true)` 生效。
- 通过：戟旋转（冷却、-3 耐久、`get_glaive/use_gs`、目标掉血）、戟普通攻击（20→11.144、耐久 -1）、唐盾右键后 `isBlocking=true`。
- 待查：戈钩取只拉近目标，无伤害/冷却/耐久变化。
- 模型告警：`guduo` 等物品模型找不到父模型，所有客户端模型尚未通过视觉验证。

## 10. 追加修复

- 戈钩取：射线起点改为眼睛位置，Fabric 1.20.1 玩家实测通过（拉近 5→2、2 伤害、冷却、耐久 -2）。
- 骨朵模型：删除 `custom/guduo.json` 的非法 `parent: "guduo"` 与 `format_version`。

## 2026-11-06 Fabric 1.20.1 玩家功能第二批（AutoJS）

| 功能 | 脚本/路径 | 结果 |
|---|---|---|
| 唐横刀双击突刺 | 服务端 `AutoJS.use()` ×2，间隔 150 ms | 通过：20 → 14.096，耐久 2，冷却 true，cuts ×1 |
| 面甲驱离 | 铁面甲 + `AutoJS.crouch(true)`，8 格僵尸 | 通过：target null，头盔耐久 0 → 4 |
| 步兵甲低血 | 全套铁步兵甲，生命 3 | 通过：Speed III、Strength II、Absorption II、Slowdown I |
| 马铠挡箭（伤害取消） | 钻石唐马铠马 + 箭矢 | 通过：53 → 53；无甲对照 53 → 47 |
| 立盾放置 | `new ShieldActionPacket().handle(player)` + 视线向下 | 通过：实体生成、物品 -1 |
| 立盾收回 | 同上，3 格外对准盾牌 | 通过：物品回主手、实体 discard |
| 长矛投掷 | `AutoJS.use()` | 未完成：首次返回 true，随后客户端因马铠纹理崩溃；重启后复测 |

## 2026-11-06 第三批

| 功能 | 结果 |
|---|---|
| 长矛满蓄力投掷 | 通过：物品 -1、耐久 2、目标 20→17 |
| 盔甲匠 4 级交易 | 修复后通过：8 条锻造石全部出现（另有 4 条原版报价） |
| 染料合成 | 通过：红染料 → 0xB02E26，物品保留 |
| 水锅洗色 | 通过：颜色移除，炼药锅降一级 |
| 山文甲减伤 | 通过：剑攻击 10 → 7；无甲 10 |

| 进度数量 | 通过：26 |
| 戟潜行成就 | 通过：`use_fangtian_hengzhan` |

## 2026-11-06 生产服务端（最新 jar）

| 版本 | 结果 |
|---|---|
| Forge 1.18.2 | 6/6（需用 join 输出，旧 Rhino JSON.stringify 数组 bug） |
| Forge 1.19.2 | 6/6 |
| Forge 1.20.1 | 6/6 |
| NeoForge 1.21.1 | 6/6 |

## 2026-11-06 26.1 / 1.21.1 运行结果

| 目标 | 结果 |
|---|---|
| Fabric 26.1 dev server | 启动 Done，无 Missing tag/战利品错误；AutoJS 6/6 |
| NeoForge 26.1 dev server | 启动 Done，0 数据包错误；AutoJS 未加载，未做游戏内断言 |
| Fabric 1.21.1 dev server | 启动 Done；AutoJS 6/6；村民交易 8 条锻造石全部出现 |
| 构建矩阵 | 8/8 目标 BUILD SUCCESSFUL |

## 2026-11-06 26.1 村民交易

| 目标 | 结果 |
|---|---|
| Fabric 26.1 | 4 级盔甲匠 10 条报价，8 条锻造石全部出现 + 原版钻石靴/护腿 |
| NeoForge 26.1 | 启动 Done，无交易/战利品数据错误；@OnlyIn 告警已消除 |

## 2026-11-06 Fabric 爆炸保护 / 马铠反弹

| 目标 | 结果 |
|---|---|
| Fabric 1.20.1 立盾爆炸保护 | 通过：盾后方块保留、盾前对照销毁；盾后僵尸 20→20，对照 20→14.096 |
| Fabric 1.21.1 立盾爆炸保护 | 通过：盾后僵尸 20→20，对照 20→13.41 |
| Fabric 1.20.1 马铠箭矢反弹 | 通过：vx 2.0→-1.73、vy 0.1、pickup DISALLOWED、马 53→53 |
| Fabric 1.21.1 马铠箭矢反弹 | 编译通过，运行期待验证 |

## 2026-11-06 补测

| 目标 | 结果 |
|---|---|
| Fabric 1.21.1 马铠箭矢反弹 | 通过：vx +1.0 → -0.618、vy 0.094、pickup DISALLOWED、马 53→53 |
| NeoForge 26.1 AutoJS | 通过（run/mods 加载）：6/6 基础检查 + 8 条锻造石交易 |
| NeoForge 26.1 长矛/立盾召唤 | 通过 |

## 2026-11-06 护甲套装修正

| 功能 | 结果 |
|---|---|
| 细鳞全套甜浆果免疫 | 通过：伤害 0 / allow=false；对照 5 / true |
| 明光睚眦头盔替代 | 通过：3 件明光+睚眦，3 点箭伤取消；摘头盔不取消；5 点箭伤不取消 |
| 细鳞目标免疫拖拽 | 通过：全套细鳞 false，山文对照 true |
| 黑锤全套 60% 剑伤减免 | 通过：176/300（58.7%） |

## 2026-11-06 护甲数值补测

| 功能 | 结果 |
|---|---|
| 中期/晚期明光蹲下剑伤 -2 | 通过：10→8；站立 10 |
| 早期明光全套箭伤 -1 | 通过：5→4 |
| 细鳞单件 10% 减伤 | 通过：32/300（10.7%） |
| 黑锤全套反射 | 通过：攻击者 20→15.08 |
| 明光骑乘马 +2 护甲 | 通过：0→2，1 个修饰符 |
| 立盾 Unbreaking 公式 | 通过：1000,10,2 → 996.667 |

## 2026-11-06 GUI/配方/动画补测

| 功能 | 结果 |
|---|---|
| 创造标签页 | 通过：202 个物品，顺序首尾符合预期 |
| 运行时配方 | 通过：192 条（WCT 137 / 锻造 38 / 有序 15 / 无序 1 / 染色 1） |
| 唐横刀突刺动画 | 通过：progress 0 → 1.0 → 0，耐久 +2，冷却 true |

## 2026-11-06 世界生成

| 目标 | 结果 |
|---|---|
| Fabric 1.20.1 远古宝石矿自然生成 | 通过：新区块 16×16×33 内 54 个 |
| `/place feature chineseweapons:ancient_gem_ore` | 通过：同范围 54 → 72 |
| `forge:stone` 标签 shim | 已添加（Forge 1.20.1 同值 12 项） |

## 2026-11-06 1.21.1 深层矿

| 目标 | 结果 |
|---|---|
| 修复前 Fabric 1.21.1 深层矿 | 0（自然 + place feature） |
| 修复后 Fabric 1.21.1 深层矿 | 44 个/新区块（y=-4 起） |
| 浅层矿对照 | 22 个，正常 |

## 2026-11-06 掉落与 GUI 菜单

| 功能 | 结果 |
|---|---|
| 远古宝石矿掉落 | 通过：钻石镐 → ancient_gem |
| 工具等级门槛 | 通过：木镐破坏但无掉落 |
| 宝石块/锻造台掉落 | 通过：各掉落自身 |
| 锻造台菜单 | 通过：WeaponsCastingTableTypeGuiMenu，47 槽 |

## 2026-11-06 锻造台合成

| 功能 | 结果 |
|---|---|
| diamond_guduo 配方端到端 | 通过：输出 1、进度 100、输入全消耗 |
| 错误配方 | 通过：无输出 |
| 取走成品重置 | 通过：进度 0、输出清空 |

## 2026-11-06 修理/染料/锻造台/副手

| 功能 | 结果 |
|---|---|
| 修理材料判定 | 通过：木+木板、铁+铁锭 true；铁+钻石 false |
| swords 标签 | 通过：19 条模组条目 |
| 染料配方负例 | 通过：单护甲/双护甲均 false，加染料 true |
| 未完成锻造台破坏 | 通过：台+输入掉落，无输出 |
| 已完成锻造台破坏 | 通过：台+输入+成品掉落 |
| 戟/戈副手清理 | 通过：副手盾移入背包，主手不变 |

## 2026-11-06 骨朵/效果

| 功能 | 结果 |
|---|---|
| 骨朵空中攻击 | 通过：20→9.224、y+1.5、缓慢 100、耐久 3、冷却 |
| 流血 100 tick | 通过：3 次 tick 伤害、Weakness/Slowness |
| 割伤冲刺 | 通过：冲刺 5 点保底，非冲刺 3 点随机 |
| A49 文档 bleed | 与原始源码不符（原始空中攻击无流血），非缺陷 |

## 2026-11-06 数值核对

| 项目 | 结果 |
|---|---|
| 六种武器基础数值 | 通过（钻石戟 10/1.2/2186 等） |
| 步兵甲/面甲/马铠数值 | 通过；FEATURES 的 A09/A10 数值有笔误，已记录 |
| 防火标记 | 通过 |
| Fabric 实体距离加成 | 平台限制，未实现（无 Forge ENTITY_REACH） |

## 2026-11-06 26.1 锻造台

| 目标 | 结果 |
|---|---|
| Fabric 26.1 diamond_guduo | 通过：输出 1、progress 100、输入全消耗 |
| NeoForge 26.1 diamond_guduo | 通过：输出 1、progress 100、输入全消耗 |

## 2026-11-06 客户端启动

| 目标 | 结果 |
|---|---|
| Fabric 1.21.1 client | 主菜单，资源包含模组，无模型/Mixin 错误 |
| Fabric 26.1 client | TitleScreen，AutoJS 25596，无模型/Mixin 错误 |

## 2026-11-06 进度/压扁/按键/脚本设置

| 功能 | 结果 |
|---|---|
| root 进度 | 通过：获得锻造台授予 `chineseweapons:root` |
| 骨朵压扁包 | 通过：客户端 SQUASH map 出现目标 id |
| J 键绑定 | 通过：key.skill.text / J / 模组分类 |
| KubeJS 距离/概率 | 通过：3.0 与 0.99；1.0/0.0 抛 IAE |

## 2026-11-06 重复配方 / 下马

| 功能 | 结果 |
|---|---|
| 连续 reload 重复检测 | 通过：修复后 0 条误报，不同 id 仍检测 |
| 骨朵潜行下马 | 通过：目标下马 + 60 tick 冷却 |
| 构建矩阵 | 8/8 BUILD SUCCESSFUL |

## 2026-11-06 立盾/步兵甲/拖拽

| 功能 | 结果 |
|---|---|
| 立盾修复 +50 | 通过：190→240，木板 -1 |
| 步兵甲箭伤 | 通过：不取消（与原始一致） |
| 拖拽 5 秒到期 | 通过：6 秒后 isTethered=false |
| 跳跃脱离 | 未实现（Fabric 无 LivingJumpEvent），已记录 |

## 2026-11-06 跳跃脱离

| 目标 | 结果 |
|---|---|
| Fabric 1.20.1 拖拽跳跃脱离 | 通过：setJumping 驱动，5 秒前脱离 |
| Fabric 1.21.1 / 26.1 | 编译通过（同一实现） |

## 2026-11-06 Fabric 实体距离

| 目标 | 结果 |
|---|---|
| Fabric 1.20.1 pickRange | 空手 4.5 / 铁戟 5.5 / 铁戈 6.5 |
| Fabric 1.20.1 4.2 格选中 | 铁戈 ENTITY，空手 MISS |
| Fabric 1.21.1 / 26.1 | 编译通过，`Player.entityInteractionRange` 注入 |

## 2026-11-06 实体距离（客户端运行）

| 目标 | 空手 | 铁戟 | 铁戈 |
|---|---:|---:|---:|
| Fabric 1.20.1 | 4.5 | 5.5 | 6.5 |
| Fabric 1.21.1 | 3.0 | 4.0 | 5.0 |
| Fabric 26.1 | 3 | 4 | 5 |

## 2026-11-06 长矛 Loyalty

| 功能 | 结果 |
|---|---|
| 长矛 Loyalty I 回返 | 通过：投出后 count 0 → 约 4 秒回到背包，投射物消失 |
| 备注 | 25585 被其他工程占用时需固定 AUTOJS_PORT |

## 2026-11-06 金甲猪灵 / 雪地靴

| 功能 | 结果 |
|---|---|
| 金甲猪灵中立 | 通过：铁甲 false，金山文全套 true |
| 雪地靴行走 | 通过：铁山文靴 false，宋步兵靴 true |
| Curios head 标签 | 3 个缺失命名空间已修正，golden 已刷新 |

## 2026-11-06 构建与断言

| 项目 | 结果 |
|---|---|
| 构建矩阵 | 8/8 BUILD SUCCESSFUL |
| 渤海/睚眦可染色 | 通过：true / false |
| 精准采集挖矿 | 通过：掉矿石本身 |

## 2026-11-06 木戈合成返还

| 目标 | 结果 |
|---|---|
| Fabric 1.20.1 木戈 remainder | 通过：has=true，返回自身；铁戈 false |
| Fabric 1.21.1 / 26.1 | 编译通过（同一 Mixin 策略） |

## 2026-11-07 KubeJS / 脚本设置

| 项目 | 结果 |
|---|---|
| Fabric 1.20.1 KubeJS 绑定 | 通过：`ChineseWeapons.setReachBonus` → 3.0 |
| 立盾 behind-center 绑定 | 通过：6.0（原 D27 NPE 已修复） |
| null itemId 存储 | 通过：synchronized HashMap，8 target 同步修改 |

## 2026-11-07 JEI/EMI 接入

| 目标 | JEI | EMI |
|---|---|---|
| Forge 1.18.2 | 原有 | 无构建 |
| Forge 1.19.2 / 1.20.1 | 原有 | 原有 |
| Fabric 1.20.1 | 新增 | 新增 |
| Fabric 1.21.1 | 新增 | 新增 |
| Fabric 26.1 | 新增 | 无构建 |
| NeoForge 1.21.1 | 新增 | 新增 |
| NeoForge 26.1 | 新增 | 无构建 |

构建：5 个改动目标均 `BUILD SUCCESSFUL`；运行期渲染/配方跳转尚未逐项人工验证。
