# 当前测试进度（上下文恢复用）

## 已完成

| 环境 | 启动方式 | 结果 |
| --- | --- | --- |
| Fabric 1.20.1 dev server | `targets/fabric-1.20.1` → `runServer` | 25/25 通过（注册/数值/配方匹配/锻造台/流血） |
| Fabric 1.21.1 dev server | `targets/fabric-1.21.1` → `runServer` | 20/20 通过（耐久已修复） |
| Forge 1.18.2 生产 | `D:/work/forge-prod-1182` | 6/6 基础检查通过 |
| Forge 1.19.2 生产 | `D:/work/forge-prod-1192` | 6/6 基础检查通过 |
| Forge 1.20.1 生产 | `D:/work/forge-prod-1201` | 6/6 基础检查通过 |
| NeoForge 1.21.1 生产 | `D:/work/nf-prod-1211` | 6/6 基础检查通过 |
| Fabric 1.20.1 客户端 | `targets/fabric-1.20.1` → `runClient` | 客户端能启动，AutoJS client 端点可用；quickPlay 世界未完成，待继续 |

## 生产服务端目录

- Forge 1.18.2: `D:/work/forge-prod-1182`（JDK17）
- Forge 1.19.2: `D:/work/forge-prod-1192`（JDK17）
- Forge 1.20.1: `D:/work/forge-prod-1201`（JDK17）
- NeoForge 1.21.1: `D:/work/nf-prod-1211`（JDK21）
- mods 内已放：模组 reobf jar、AutoJS 对应版本、Rhino 对应版本；`eula.txt=true`。
- 启动示例（Windows 路径）：
  - `"C:/Users/28217/.gradle/jdks/eclipse_adoptium-17-amd64-windows.2/bin/java.exe" @user_jvm_args.txt @libraries/net/minecraftforge/forge/<ver>/win_args.txt nogui`
  - NeoForge 用 `libraries/net/neoforged/neoforge/21.1.234/win_args.txt`。

## 当前阻塞

1. **26.1 组件绑定**：手动 `DATA_COMPONENT_INITIALIZERS.build(provider)` 在 mod init 时因 `minecraft:damage_type/is_fire` 标签缺失失败。需要延后到标签加载后绑定，或按 item 用其 `Properties.componentInitializer` + 可用标签的 provider 绑定。
2. **Fabric 1.20.1 客户端**：`--quickPlaySingleplayer world` 未确认进入世界；需要检查 `/tmp/client-fab1201.log`，或用 AutoJS client 驱动菜单创建/加载世界。
3. **NeoForge 26.1 / Fabric 26.1**：受 26.1 组件/标签阻塞。
4. **玩家交互/GUI**：Fabric client 可用后，用 `AutoJS.background(true)` + 点击路径测试。

## 客户端约定

- 启动前 `run/options.txt` 已写 `soundCategory_master:0.0`。
- 启动后先 `AutoJS.background(true)`，不抢窗口。

## 常用工具

- MCP 调用：`python /tmp/mcp.py '<js>'`（返回 JSON）。
- 冒烟脚本：`D:/work/tmp/checks.py`（Fabric 1.20.1/1.21.1 直接 MC API 版）。
- 生产服务端基础检查：`/tmp/nf-smoke.js`（`AutoJS.findItems/findBlocks/findEntities`）。
- 镜像 init：`-I /tmp/mirror-init.gradle`；AutoJS 本地 Maven：`D:/work/autojs-maven`。

## 下一步

1. 修复 26.1 组件绑定（最好用 `ServerLifecycleEvents`/`TagsUpdated` 后绑定，或按 item 绑定）。
2. Fabric 1.20.1 客户端进世界，执行客户端/玩家断言（按键、模型、GUI、右键、潜行、攻击、交易）。
3. 把结果写回 `docs/AUTOJS_TEST_RESULTS.md`。

## 2026-11-06 追加：Fabric 1.20.1 客户端 + 玩家测试

- 客户端启动命令：`runClient --args="--quickPlaySingleplayer testworld"`，已复制服务端 `run/world` 到 `run/saves/testworld`；进入世界后执行 `AutoJS.background(true)`，无窗口干扰。
- 服务端与客户端 side 同时可用，玩家 `Player824`。
- 通过：
  - 戟旋转：右键后进入冷却，耐久 -3，授予 `get_glaive/use_gs`，僵尸掉血。
  - 普通戟攻击：`AutoJS.attack` 返回 true，僵尸 20 → 11.144，耐久 +1。
  - 唐盾：右键后 1 tick 起 `isBlocking=true`，`useItem=tang_dynasty_shield`。
- 待查：
  - 戈（dagger axe）钩取：目标被拉近约 2.3 格，但僵尸未掉血、冷却未触发、耐久未变；检查 `GameplayCompat.hurt` 在 Fabric 上的返回值/伤害源。
- 客户端模型警告：`minecraft:guduo` 等几个模型找不到 `minecraft:models/guduo.json`，疑似 1.20.1 Fabric 模型路径/父模型问题，待查。

## 2026-11-06 追加：戈钩取与模型修复

- 戈钩取修复：`DaggerAxeRules.hook` 的射线起点从玩家脚部改为 `getEyePosition()`。修复前目标在 5 格处被拉到 2 格但不掉血、无冷却；修复后：
  - 距离 5.0 → 2.0
  - 僵尸 20 → 18.08（2 伤害）
  - 冷却 true
  - 耐久 +2
- `common/.../models/custom/guduo.json` 删除了 Blockbench 的 `parent: "guduo"` 与 `format_version`；此前 Fabric 客户端报 `minecraft:models/guduo.json` 缺失。需要重新构建/重启客户端确认视觉。

## 2026-11-06 追加：Fabric 1.20.1 玩家功能测试（第二批）

测试环境：Fabric 1.20.1 客户端 + 内置服务端，Player511，`AutoJS.background(true)`，静音。**世界暂停问题**：客户端默认 `pauseOnLostFocus=true`，后台运行失焦时会打开 `PauseScreen` 并停止 tick；已在运行期 `Client.options.pauseOnLostFocus=false` 并在 `run/options.txt` 确认该值，之后世界正常后台 tick。

通过：

- 唐横刀双击右键：服务端连续两次 `AutoJS.use()`（间隔 150 ms）：僵尸 20 → 14.096，耐久 +2，冷却 true，`cuts` 效果 1 个。
- 面甲：佩戴铁面甲头盔 + 潜行，8 格僵尸被驱离（target 清空）；头盔耐久自然减少 4（两次触发 ×2）。注意玩家死亡/暂停会中断判定。
- 陌刀步兵甲低血规则：全套铁步兵甲 + 生命 3：出现 `MovementSlowdown I`、`MovementSpeed III`、`Strength II`、`Absorption II`（各 86 tick 或 1 tick 刷新）。
- 唐马铠挡箭：装备钻石唐马铠的马 53 → 53（伤害取消）；无甲对照马 53 → 47。Fabric 1.20.1 没有 Forge `ProjectileImpactEvent` 等价物，箭矢反弹未实现（代码注释已说明），仅验证伤害取消。
- 宋立盾放置/收回：服务端直接调用 `ShieldActionPacket.handle`（与 J 键同一处理逻辑）：放置后实体生成、主手数量 0；站到 3 格外收回后物品回到主手、实体 `discard`。
- 客户端 -> 服务端 `ShieldActionPacket` 发送链路可用（发送无异常；因服务端朝向被客户端每 tick 同步，直接发送时射线未命中，改用同 eval 内反射设置朝向）。

本轮修复：

- `targets/fabric-1.20.1/.../TangDynastyHorseItem.java`：1.20.1 `HorseArmorItem` 构造器会把纹理字符串再拼成 `textures/entity/horse/armor/horse_armor_<texture>.png`，原先传 `ResourceLocation.toString()` 导致渲染马铠时 `ResourceLocationException` 客户端崩溃。改为传入 path 并覆写 `getTexture()` 返回模组自己的 ResourceLocation。

AutoJS/Rhino 注意：

- 1.20.1 下 `getGameTime`、`getXRot`、`setXRot`、`clearFire` 直接调用会报 `Cannot find function`，需用 `getClass().getMethod(...).invoke` 反射（float 参数传 `java.lang.Float`）。
- 等待 tick 必须让服务端线程返回、在脚本外等待；脚本内 `Thread.sleep` 会阻塞服务端线程，世界不 tick。

待重测：长矛投掷（首次 use 返回 true，但客户端随后因马铠纹理崩溃；需重启后复测）。

## 2026-11-06 追加：长矛/交易/染色/套装规则

通过：

- 长矛投掷：客户端 `keyUse` 按住 2 秒后松开，物品 -1、耐久 +2，6 格僵尸 20 → 17（满蓄力伤害受攻击力限制），投掷物命中后消失。
- 村民交易：原 Fabric 仅有 `TradeOfferHelper`，它只是把交易追加进原版随机池，`updateTrades` 只随机取 2 条，导致 8 条锻造石交易大多不出现。新增 `VillagerMixin`（`@Inject(method="updateTrades", at=TAIL)`）在 4 级盔甲匠上补齐全部 8 条并去重；实测 12 条报价 = 8 条锻造石 + 4 条原版（`updateTrades` 被调用两次导致原版重复，正常升级流程不会）。
- 染色配方：`DyeableArmorRecipe.matches/assemble` 用 `TransientCraftingContainer` + 红染料验证，结果物品保留、颜色写入 `0xB02E26`、剩余物品 4 格。
- 水锅洗色：`DyeableItemCleanColor.onRightClickBlock` 清色成功，`water_cauldron[level=1]` → `cauldron`。
- 山文甲减伤：`ChineseWeaponsCombatEvents.modifyDamageAmount` 对剑攻击者 10 点伤害，穿山文胸甲返回 7，无甲返回 10。

本轮修复：

- `common/.../rules/JavelinRules.java`: 投掷冷却从 `stack.shrink(1)` 之后移到之前，避免空栈把冷却加到 `minecraft:air` 上。
- `targets/fabric-1.20.1`: 新增 `fabric/FabricTrades.java`（8 条报价工厂）与 `mixin/VillagerMixin.java`，并注册到 `chineseweapons.mixins.json`。

已知仍缺：

- fabric-1.21.1 仍用旧的 `TradeOfferHelper` 注册，受同样的“随机取 2 条”影响；需要同款 `VillagerMixin`。
- fabric-26.1 / neoforge-26.1 尚无村民交易代码。

## 2026-11-06 追加：立盾与护甲事件规则

通过：

- 立盾修复/旋转：`interact`（主手）木板修复 20 → 1670、木板 2 → 1；潜行 + 右键旋转 yaw 180° → 195°（+15°）。
- 立盾受击：通用伤害 100 → 90；火焰/爆炸伤害免疫；生命 5 再受 100 伤害后实体 `discard`。
- 早期明光全套 + 箭矢：3 点箭伤被完全取消（20 → 20）；5 点箭伤减为 1.28（20 → 18.72）。
- 陌刀骑乘拖拽：骑乘状态下 `tryStartMountedDrag` 返回 true，6 格僵尸 1 秒内被拉到 2.32 格，`isTethered` 为 true。

发现缺口（Fabric）：

- 立盾爆炸保护（安全区移除方块/实体）在 Fabric 1.20.1/1.21.1 没有实现；Forge/NeoForge 通过 `ExplosionEvent.Detonate` 实现，Fabric 需要 Mixin 进 `Explosion`。
- 唐马铠箭矢反弹同样仅 Forge/NeoForge 实现。

Fabric 1.21.1 构建：`build` 成功（JDK 21 在 `C:/Users/28217/.jdks/temurin-21.0.10`，`.gradle/jdks` 下没有 21）。

## 2026-11-06 追加：构建矩阵与进度

- Fabric 1.20.1：`build` 成功；Forge 1.18.2 / 1.19.2 / 1.20.1：`build` 成功；Fabric 1.21.1：`build` 成功（JDK 21：`C:/Users/28217/.jdks/temurin-21.0.10`）；NeoForge 1.21.1：`build` 成功。
- 26.1 两个 target 交给子代理修复组件绑定，尚未回报。
- 进度/结果文档：`docs/PROGRESS.md`、`docs/AUTOJS_TEST_RESULTS.md`、功能基线 `docs/FEATURES.md`。

Fabric 1.20.1 追加通过：

- 进度 26 个（`chineseweapons:` 前缀，含 20 inventory_changed + 6 impossible）。
- 戟潜行命中授予 `chineseweapons:get_sky_piercing_halberd/use_fangtian_hengzhan`。

## 2026-11-06 追加：生产服务端复测（最新 jar）

- Forge 1.20.1：6/6（items 202、blocks 4、entities 2、block entity、立盾召唤、长矛召唤）。
- Forge 1.19.2：6/6。
- Forge 1.18.2：6/6。注意该版本 AutoJS 的 Rhino `JSON.stringify` 对 JS 数组只输出第一个元素（`[1,2,3]` → `[1.0]`），不是模组问题；用 `checks.join("|")` 读取。
- NeoForge 1.21.1：6/6。
- 生产 jar 已更新：`forge-prod-1182/1192/1201`、`nf-prod-1211` 的 `mods/chineseweapons.jar`。

## 2026-11-06 追加：26.1 阻塞修复与运行验证

26.1 数据组件阻塞（子代理确证并修复）：

- 根因：26.1 的 `Item.Properties.fireResistant()` 已改为 `delayedComponent`，组件的初始化解引用 `minecraft:is_fire` 标签；该标签只有在数据包标签加载后可用。模组 init 里手动 `BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(provider)` 既过早又多余——原版会在 `ReloadableServerResources.loadResources` / 客户端 `RegistryDataCollector.updateComponents` 中自动完成绑定。
- 修复：`fabric-26.1` / `neoforge-26.1` 入口删除全局 build 与相关 import，仅留注释禁止恢复。
- 连带修复：Fabric 26.1 `WCTRecipe`/`WCTRecipeSer` 结果改用 `ItemStackTemplate`（组件绑定前 recipe 就反序列化，`ItemStack.CODEC` 会校验组件已绑定）；NeoForge 26.1 `GuDuoItem`/`SongJavelinItem` 的 private Properties 未调用 `ItemIds.apply`，导致注册期 NPE。
- 资源：`scripts/resource-profiles/26.1-fabric.json` 的 `lootPredicateComponents` 从 true 改为 false（26.1 没有 `minecraft:predicates` 组件，两个矿的战利品表此前被丢弃）。生成产物已确认使用 `predicates`。
- 追加修复：Fabric 26.1 `SongJavelinProjectile` 的 `getPickupItem`/`getDefaultPickupItem` 在父类构造期被调用时 `weaponStack` 仍为 null，`/summon chineseweapons:song_javelin` 抛 NPE；两个方法加 null 守卫。NeoForge 26.1 的 `getPickupItem` 同样加守卫。

运行验证：

- Fabric 26.1 dev server（AUTOJS_PORT=25591）：`Done`，无 `Missing tag`、无战利品表错误；AutoJS 6/6（items 202、blocks 4、entities 2、block entity、立盾召唤、长矛召唤）。
- NeoForge 26.1 dev server：`Done`，0 数据包错误；该 run 配置未加载 AutoJS（hot-debug 的 additionalRuntimeClasspath 未包含），因此只验证到启动与数据加载。仅有 `@OnlyIn` 弃用告警。
- Fabric 1.21.1 dev server（AUTOJS_PORT=25593）：`Done`；AutoJS 6/6；村民交易 10 条（8 条锻造石 + 2 条原版），证明新 `VillagerMixin` 在 1.21.1 生效。

构建矩阵（全部 exit 0）：fabric-1.20.1、fabric-1.21.1、fabric-26.1、forge-1.18.2、forge-1.19.2、forge-1.20.1、neoforge-1.21.1、neoforge-26.1。

剩余已知缺口：

- Fabric 1.20.1/1.21.1 无立盾爆炸安全区（需要 Mixin 进 `Explosion`；Forge/NeoForge 用 `ExplosionEvent.Detonate`）。
- Fabric 1.20.1/1.21.1 无唐马铠箭矢反弹（Fabric 无 `ProjectileImpactEvent`），仅伤害取消。
- fabric-26.1 / neoforge-26.1 尚无村民交易代码（1.20.1/1.21.1 Fabric 与 Forge/NeoForge 1.21.1 已有）。
- NeoForge 26.1 的 AutoJS 开发运行配置未生效，未做游戏内断言。

补充：26.1 村民交易不是代码 API，而是数据驱动（`VillagerProfession.tradeSetsByLevel` → `TradeSet` / `villager_trade` JSON；26.1 Fabric API 已无 `TradeOfferHelper`）。补齐 8 条锻造石交易需要为 26.1 生成 `data/chineseweapons/villager_trade/...` 交易文件，并用 `data/minecraft/tags/villager_trade/armorer/level_4.json`（`replace:false`，合并标签）挂到盔甲匠 4 级；这属于 26.1 资源生成管线的新能力，尚未实现。

## 2026-11-06 追加：26.1 村民交易（数据驱动）与 @OnlyIn

26.1 的交易已改为 `TradeSet` + `villager_trade` 数据驱动（26.1 Fabric API 也没有 `TradeOfferHelper`），为两个 26.1 profile 生成：

- `data/chineseweapons/villager_trade/forging_stone/*.json` ×8（10 绿宝石 + 1/5 铁锭 → 对应锻造石，`max_uses=10`、`xp=5`、`reputation_discount=0.05`）。
- `data/minecraft/tags/villager_trade/armorer/level_4.json`（`replace:false`，把 8 条并入原版 tag）。
- `data/minecraft/trade_set/armorer/level_4.json`（`amount` 从 2 提到 11；合并后池子正好 10 条，实际取 10 条 = 8 自定义 + 2 原版）。
- 生成器新增 `villagerTrades` feature（两个 26.1 profile 打开），golden manifest 已按有意变更刷新。

实测 Fabric 26.1：4 级盔甲匠 10 条报价，8 条锻造石全部出现 + 原版钻石靴/护腿。NeoForge 26.1 启动 `Done`，无交易/战利品数据错误。

另：NeoForge 26.1 的 `procedures/tips` 去掉 `@OnlyIn`（26.1 已移除运行期成员剥离，保留只会产生告警；类本身仍由 `@EventBusSubscriber(value=Dist.CLIENT)` 限制在客户端）。

## 2026-11-06 追加：Fabric 立盾爆炸保护与马铠箭矢反弹

Fabric 1.20.1 / 1.21.1 新增两个 Mixin（之前只有 Forge/NeoForge 的事件实现）：

- `ExplosionMixin`
  - `explode()` HEAD：把爆炸中心压入 ThreadLocal，供伤害判定使用；
  - `explode()` RETURN：把位于盾牌爆炸安全区（`ShieldRules.explosionSafeZone`，盾后 4 格、半边长 4）的方块从 `toBlow` 移除，再清理 ThreadLocal；
  - `getSeenPercent` HEAD：安全区内的实体曝光设为 0（消除击退与大部分伤害）；
  - `LivingEntityMixin` 在 `hurt` 入口对 `IS_EXPLOSION` 伤害再次查询安全区并返回 0，消除剩余的 1 点基础伤害。
  - 安全区判定抽到 `compat/StandingShieldExplosionProtection`，避免 Mixin 内非 private static 方法（会触发 Mixin 应用失败）。
- `ArrowMixin`
  - `AbstractArrow.onHitEntity` HEAD：目标是穿唐马铠的马时取消原版命中，速度反向 ×0.8 + 随机 X/Z ±0.15 + Y+0.1，`owner=null`、`pickup=DISALLOWED`。
  - 1.21.1 用 `getBodyArmorItem()`，1.20.1 用 `getArmor()`。

实测：

- Fabric 1.20.1：爆炸后盾后石头保留、盾前对照变空气；盾后僵尸 20 → 20，盾前对照 20 → 14.096。
- Fabric 1.21.1：同样场景，盾后僵尸 20 → 20，盾前对照 20 → 13.41，方块结果一致。
- Fabric 1.20.1 箭矢反弹：调试日志确认 `vx 2.0 → -1.73`、`vy=0.1`、`pickup=DISALLOWED`，马 53 → 53。
- 1.21.1 的反弹代码已编译通过，尚未做运行期断言。

注意：不要在客户端运行时执行 `gradlew build`，Gradle 会重写 `build/classes`，运行中的客户端会因 `NoClassDefFoundError` 崩溃。

## 2026-11-06 追加：1.21.1 箭矢反弹与 NeoForge 26.1 AutoJS

- Fabric 1.21.1 马铠箭矢反弹运行期验证：箭矢 `vx +1.0 → -0.618`、`vy 0.094`、`pickup=DISALLOWED`，穿甲马 53 → 53。说明 1.21.1 的 `getBodyArmorItem()` 分支生效。
- NeoForge 26.1 的 ModDevGradle `runServer` 没有把 `hotDebugRuntime` 放进运行类路径（`verifyHotDebug` 能解析，但游戏 Mod List 里没有 AutoJS）。本机解决方式：把 `AutoJS-neoforge-26.1`、Rhino、KubeJS 等 jar 复制到 `targets/neoforge-26.1/run/mods/`，再启动 dev server；AutoJS MCP 正常监听 25592。
- NeoForge 26.1（AutoJS）实测：items 202、blocks 4、entities 2、方块实体、立盾召唤、长矛召唤全部通过；4 级盔甲匠 10 条报价、8 条锻造石。26.1 的 NPE 修复（长矛 `weaponStack`）在 NeoForge 端同样有效。
- 专用服务器无玩家时实体所在的区块不会 tick：测试前需 `lvl.setChunkForced(x,z,true)`，否则箭矢/实体位置不更新。

## 2026-11-06 追加：护甲套装修正规则实测（Fabric 1.20.1）

- 细鳞全套 + 甜浆果：`modifyDamageAmount` 返回 0，`allowDamage` 返回 false（取消）；只穿山文胸甲对照返回 5 / true。
- 明光头盔替代：3 件早期明光 + 铁睚眦头盔，3 点箭伤 `allowDamage` 返回 false（取消）；摘掉头盔返回 true；5 点箭伤返回 true（不取消）。说明 `countPieces` 的睚眦/渤海替代计数生效。
- 细鳞目标免疫骑乘拖拽：给僵尸穿全套细鳞后 `tryStartMountedDrag` 返回 false；穿山文胸甲的对照僵尸返回 true。
- 黑锤全套：300 次剑伤害采样中 176 次被减为 60%（58.7%，设计 60%），无异常。

## 2026-11-06 追加：护甲数值规则与立盾耐久公式

- 中期明光全套 + 蹲下剑伤：10 → 8；站立 10。晚期明光同样 10 → 8 / 10。
- 早期明光全套 + 箭伤：5 → 4（-1）；2 点箭伤 `modifyDamageAmount` 返回 2，取消逻辑由 `allowDamage` 负责。
- 细鳞单件 + 剑伤：300 次采样 32 次减为 80%（10.7%，设计 10%）。
- 黑锤全套反射：20 次剑伤采样后攻击者僵尸 20 → 15.08（30% 概率反射 50% 原伤）。
- 明光全套骑乘：马 `Attributes.ARMOR` 0 → 2，出现 1 个 `ming_guang_mount_armour` 修饰符（每 10 tick 刷新）。
- 立盾 Unbreaking 公式：`ShieldRules.damagedHealth(1000,10,2)` = 996.667；无 Unbreaking 为 990。

## 2026-11-06 追加：创造标签页 / 配方加载 / 唐横刀动画

- 创造标签页 `chineseweapons:chinese_weapons`：`buildContents` 后 202 个物品，首个为 `iron_mountain_character_armor_helmet`（山文铁头盔），最后一个为 `tang_dynasty_infantry_armor_forging_stone`。
- 运行时配方分布（仅 `chineseweapons` 命名空间，共 192）：`WCTRecipe` 137、`SmithingTransformRecipe` 38、`ShapedRecipe` 15、`ShapelessRecipe` 1、`DyeableArmorRecipe` 1。`forging_notes` 的 Forge conditional 配方在 Fabric 上被跳过（启动日志已提示），所以比资源文件总数 193 少 1。
- 唐横刀动画：直接调用 `TangSaberRules.rightClick` 三次（先重置双击状态，再 50 ms、150 ms 两次）后，`animationProgress` 0 → 0.5 s 后 1.0 → 再 2.5 s 后 0；耐久 +2、冷却 true。
- Rhino 仍不能直接调用 `getUUID`/`getCooldowns`/`getGameTime`/`getXRot` 等继承方法，需反射。

## 2026-11-06 追加：Fabric 1.20.1 世界生成标签兼容

- 两个 `configured_feature` 的矿脉替换目标都是 `forge:stone`。Forge 1.18.2–1.20.1 自带该标签，1.21.1/26.1 由生成器改成 `minecraft:stone_ore_replaceables` / `c:stones` / deepslate 标签；只有 Fabric 1.20.1 直接使用 common 的 1.20.1 资源，生产环境（只有 Fabric API）没有 `forge:stone`，会导致矿脉替换目标为空。
- 新增 `targets/fabric-1.20.1/src/main/resources/data/forge/tags/blocks/stone.json`，值与 Forge 1.20.1 的 `forge:stone` 一致（stone/granite/diorite/andesite/deepslate/tuff 及其 polished/infested 变体）。
- 实测：强制加载新区块后自然生成 54 个远古宝石矿（16×16×33 范围，y -16..16）；`/place feature chineseweapons:ancient_gem_ore` 后同一范围 54 → 72（+18），说明标签与 placed feature 都正常。
- 该 shim 只加在 Fabric 1.20.1；1.21.1/26.1 继续使用生成器产出的 vanilla/common 标签。

## 2026-11-06 追加：1.21.1 深层远古宝石矿标签修复

- 发现：1.21.1 生成器把两种矿脉都写成 `minecraft:stone_ore_replaceables`，而深层矿的生成高度是 y -32..2，地形以 deepslate 为主；`stone_ore_replaceables` 不含 deepslate，导致深层矿 0 生成。
- 实测修复前 Fabric 1.21.1：新区块自然生成 0、`/place feature` 后仍 0；浅层矿 22 正常。
- 修改 `scripts/generate-resources.py`：非 26.1 分支中，`deepslate_*` 使用 `minecraft:deepslate_ore_replaceables`，其余仍用 profile 的 `worldgenStoneTag`。
- 刷新 1.21.1-fabric / 1.21.1-neoforge 的 golden manifest；Fabric 1.21.1 重建后实测：新区块自然生成 44 个深层矿（首个 y=-4），深层矿恢复。
- NeoForge 1.21.1 重新构建并更新 `nf-prod-1211/mods/chineseweapons.jar`。

## 2026-11-06 追加：方块掉落与锻造台菜单

- 远古宝石矿（钻石镐）→ 掉落 `chineseweapons:ancient_gem`。
- 远古宝石矿（木镐，工具等级不足）→ 方块被破坏但不掉落物品，工具门槛生效。
- 远古宝石块 → 掉落自身；锻造台 → 掉落自身。
- 右键锻造台（直接调用 `BlockState.use`）打开 `WeaponsCastingTableTypeGuiMenu`，槽位 47（11 个台面槽 + 36 玩家背包），菜单创建链路正常。

## 2026-11-06 追加：锻造台端到端合成

- 在锻造台槽位 1/2/5 放钻石、4/6 放木棍、9 放铁锭（`diamond_guduo` 配方）后，等待约 8 秒：输出槽得到 1 个 `diamond_guduo`，进度 100，10 个输入槽全部消耗。
- 错误配方（10 个泥土）：输出保持空。
- 调用 `onRecipeTaken()` 后进度 0、输出清空，符合“玩家取走成品后重置”的流程。
- 菜单槽位 47（11 台面 + 36 背包）此前已验证。

## 2026-11-06 追加：修理、染料负例、锻造台破坏与副手清理

- 修复材料：木戟 + 木板 true、铁戟 + 铁锭 true、铁戟 + 钻石 false。
- `#minecraft:swords` 中本模组条目 = 19（不含横刀/长矛/骨朵），符合基线缺陷 D23。
- 染料配方负例：只放护甲 false、放两件护甲 false；护甲 + 染料 true。
- 未完成锻造台被破坏：掉落锻造台 + 输入物品（钻石、木棍），无输出。
- 已完成锻造台（progress=100 且输出已生成）被破坏：掉落锻造台 + 输入 + `diamond_guduo`。
- 主手铁戟 + 副手唐盾：下一 tick 副手清空，盾进入背包（`clearOffhand`），主手不变。
- 注意：测试中途玩家死亡会让 `inventoryTick` 等玩家逻辑停摆，相关用例需先确认 `isDeadOrDying=false` 并复活。

## 2026-11-06 追加：骨朵空中攻击 / 流血 / 割伤

- 骨朵空中命中（`setOnGround(false)` 后攻击）：目标 20 → 9.224（攻击力 9 + 2），目标 y +1.5，获得缓慢 100 tick，主手耐久 +3，冷却 true。**没有流血**——核对原始 `3010fe3` 的 `HurtFlyyy` 同样只加缓慢，FEATURES 表 A49 中“bleed 60 tick”是文档笔误，不是移植缺陷。
- 流血（bleed 100 tick）：2.5 秒后僵尸 20 → 17（每 20 tick 1 点），同时获得 Weakness/Slowness（25 tick，逐 tick 刷新），剩余时长 45。
- 割伤（cuts 100 tick）：5 秒后非冲刺僵尸 20 → 17（30% 随机），冲刺僵尸 20 → 15（每 tick 保底 1 点），冲刺额外伤害生效。

## 2026-11-06 追加：武器/护甲数值核对

实测 Fabric 1.20.1（`getDefaultAttributeModifiers` + `getMaxDamage`）：

| 物品 | 实际值 |
|---|---|
| diamond_glaive | 攻击 10、速度 1.2、耐久 2186 |
| netherite_guduo | 9 / 1.6 / 2048 |
| iron_song_javelin | 5 / 1.7 / 270 |
| iron_dagger_axe | 9 / 1.5 / 500 |
| iron_sky_piercing_halberd | 6 / 1.2 / 300 |
| golden_tang_dynasty_heng_saber | 5 / 1.8 / 32 |
| diamond_footmen 头盔/胸甲 | 护甲 5/10，耐久 374/544 |
| netherite_face_guard_helmet | 护甲 4、耐久 418、韧性 3、击退抗性 0.1 |
| 唐马铠 铁/金/钻/下界 | 防御 8/11/18/25，`canBeDepleted=false` |
| 下界合金戟 / 木戟 | 防火 true / false |

结论与文档修正：

- `docs/FEATURES.md` 的戈攻击列（A06 等）少算了 Tier 加成：原始 `3010fe3` 的构造函数是 `tierBonus + attackDamageModifier`，木戈实际 7、铁戈 9（不是 4/6）。
- `docs/FEATURES.md` A09 的步兵甲数值用了错误的槽位系数（442/510 其实是靴/腿）；实际钻石步兵头盔 5/374、胸甲 10/544。
- A10 面甲护甲应为 4（原版数组 `{5,7,9,4}`，按 feet/legs/chest/head 索引）。
- Fabric 1.20.1 没有 Forge 的 `ENTITY_REACH` 属性，`ReachSwordItem` 明确只保留 `entityReachBonus()` 不施加修饰符；因此 A03/A04/A06 的 +1.0/+2.0 实体距离在 Fabric 上不成立（平台限制，已在代码注释说明）。

## 2026-11-06 追加：26.1 两加载器锻造台合成

- Fabric 26.1：放好 `diamond_guduo` 图案（槽 1/2/5 钻石、4/6 木棍、9 铁锭）后，手动推进 `ArmorCastingTableBlockEntity.serverTick`：输出 `diamond_guduo`、progress 100、10 个输入槽全部消耗。说明 `WCTRecipe` 的 `ItemStackTemplate` 结果路径正常。
- NeoForge 26.1：同样流程输出 `diamond_guduo`、progress 100、输入全消耗。
- 专用服务器无玩家时，强制加载区块也不会推进方块实体 tick；测试用脚本直接循环调用 `serverTick`。真实游戏内有玩家时由区块 ticker 正常驱动。
- 26.1 运行时查询配方要用 `RecipeManager.getRecipes().values()`（26.1 返回 `RecipeMap`，不是集合）。

## 2026-11-06 追加：1.21.1 / 26.1 客户端启动验证

- Fabric 1.21.1 客户端：资源重载包含 `chineseweapons`，Sound engine 正常启动，日志无 `Unable to load model`/`MixinApplyError`，到达主菜单。
- Fabric 26.1 客户端（AUTOJS_PORT=25596）：到达 `TitleScreen`，`AutoJS.where()` 返回 “at the main menu”，无模型/Mixin 错误。
- 26.1 客户端的脚本全局不再有 `Client`，改用 `AutoJS.ui()/where()/snapshot()/side()`；服务端仍是 `AutoJS.server()` 等。
- 同时运行多个客户端时必须显式给每个实例设置 `AUTOJS_PORT`，否则后启动的实例 MCP 绑定失败（不影响游戏本身）。

## 2026-11-06 追加：进度/压扁/按键/KubeJS 设置

- 获得 `armor_casting_table` 后授予 `chineseweapons:root`（同批已有 `get_glaive`、`get_ancient_gem/get_tang_dynasty_shield`）。
- 骨朵空中命中后客户端 `GuDuoRules.SQUASH` 出现目标实体 id（并保留旧 id），压扁包链路正常。
- 客户端按键：`key.skill.text`，分类 `item_group.chineseweapons.chinese_weapons`，默认 `key.keyboard.j`。
- KubeJS 设置类直接调用：
  - `setReachBonus(iron_glaive, 3.0)` → `getReachBonus` 返回 3.0；
  - `setChance(iron_glaive, HIT_EFFECT, 0.99)` → 0.99；
  - `1.0` / `0.0` 均抛 `IllegalArgumentException`（范围校验与基线一致）。
- AutoJS 的 Rhino 作用域不暴露 KubeJS 的 `ChineseWeapons` 全局，测试直接调用 `WeaponScriptSettings`；KubeJS 正式使用时仍走同一方法。
- A62 的 `ItemColors` 是客户端私有字段，Rhino 反射受限，未做运行期断言（染色数值逻辑已由 DyeableItem 测试覆盖）。

## 2026-11-06 追加：重复配方误报修复 / 骨朵下马

- `RecipeDuplicates.check` 增加同 id 短路：资源重载时同一配方会再次解析，原先会把“上一次加载的自己”当成重复并输出大量错误。修复后连续 `/reload` 两次，日志 0 条 `Duplicate recipe detected`，仍保留“不同 id 相同材料”的真实重复检测。
- 该修改在 common，8 个 target 全部重新构建成功；生产目录的 Forge 1.18.2/1.19.2/1.20.1、NeoForge 1.21.1 jar 已同步更新。
- 骨朵潜行命中骑乘目标：多次攻击后目标成功下马，主手进入 60 tick 冷却；与原始 `HurtStopHo` 行为一致。

## 2026-11-06 追加：立盾修复阈值 / 步兵甲箭伤 / 拖拽到期

- 立盾修复：生命 190（≥50）用木板 → 240（+50），木板 -1；此前已测 <50 直接回满。
- 步兵甲全套 + 5 点箭伤：`allowDamage` 返回 true，不取消伤害，与原始未注册事件类（D41）一致。
- 骑乘拖拽：`tryStartMountedDrag` 成功、`isTethered=true`；等待 6 秒后自动清除（设计 5 秒）。
- 已知差异：原始 `LivingJumpEvent` 的 30% 跳跃脱离在 Fabric 端未实现（无对应事件，当前只在 5 秒到期时清除）；不阻塞玩法，记录待补。

## 2026-11-06 追加：Fabric 拖拽跳跃脱离补齐

- 目标：原始 `LivingJumpEvent` 的 30% 跳跃脱离在 Fabric 上缺失。
- 实现：Fabric 1.20.1/1.21.1/26.1 的 `LivingEntityMixin` 新增 `jumpFromGround` 注入，调用目标事件类的 `tryEscapeTether(LivingEntity)`，按 30% 概率移除 TETHERS 条目。
- 实测 Fabric 1.20.1：直接调用可清除；用 `setJumping(true)` 驱动牛起跳，10 次内提前脱离（早于 5 秒到期）。Fabric 1.20.1/1.21.1/26.1 均构建成功。

## 2026-11-06 追加：Fabric 实体距离加成

- 之前 `ReachSwordItem` 只保留 `entityReachBonus()` 不施加，Fabric 武器没有设计中的 +1/+2 实体距离。
- Fabric 1.20.1：新增客户端 `MultiPlayerGameModeMixin`（`getPickRange` 加持有武器距离）与 `GameRendererMixin`（把 3 格攻击上限 `9.0` 改为 `(3+bonus)²`），并注册到 mixins.json 的 `client` 列表。
- Fabric 1.21.1/26.1：新增 `PlayerReachMixin`，在 `Player.entityInteractionRange()` 返回值上加持有武器距离（1.21.1 读 `getReachBonus()`，26.1 读 `entityReachBonus()`）。
- 实测 Fabric 1.20.1：空手 pickRange 4.5；铁戟 5.5；铁戈 6.5。4.2 格处僵尸：持戈时 crosshair `ENTITY`，空手时 `MISS`，攻击距离上限已生效。
- Fabric 1.21.1/26.1 构建成功；运行期需进入世界后在客户端读取 `Player.entityInteractionRange()`。

## 2026-11-06 追加：Fabric 实体距离（1.21.1 / 26.1 运行验证）

- Fabric 1.21.1 客户端（进入 testworld）：空手 `entityInteractionRange=3.0`，铁戟 4.0，铁戈 5.0。
- Fabric 26.1 客户端（进入 testworld）：空手 3，铁戟 4，铁戈 5。
- 1.21.1/26.1 客户端脚本统一用 `AutoJS.player()`（无 `Client` 全局）；客户端默认 `pauseOnLostFocus=true` 会停在 PauseScreen 导致物品不同步，测试前需 `Minecraft.getInstance().options.pauseOnLostFocus=false; setScreen(null)`。
- 1.21.1 客户端加入世界时会输出 `Unknown recipe category: chineseweapons:weapon_casting_shaped/<recipe>`：这是 1.21.1 `ClientRecipeBook` 只为原版配方类型提供分类，自定义配方类型没有分类入口，属于原版限制，不影响功能。

## 2026-11-06 追加：长矛 Loyalty 运行验证

- 通过 NBT 手动写入 `Enchantments:[{id:"minecraft:loyalty",lvl:1}]`（`ItemStack.enchant` 在 Rhino 下不生效），投出铁长矛：1 秒时物品已被消耗（count 0），约 4 秒后物品回到背包（count 1），投射物消失，Loyalty 回返链路正常。
- 注意：本机 25585 端口可能被其他工程（UniversalArmory）的 AutoJS 占用；多客户端/多工程并行时应给本工程固定 `AUTOJS_PORT`（本次用 25598），否则会连到别人的游戏实例。

## 2026-11-06 追加：Fabric 金甲猪灵中立 / 雪地靴 / Curios 标签

- Fabric 1.20.1/1.21.1 新增 `PiglinAiMixin`（`isWearingGold`）与 26.1 版（`isWearingSafeArmor`）：检查护甲定义里的 `piglinNeutral`，金甲套装现在能让猪灵中立。
  实测：铁山文胸甲 `isWearingGold=false`，全套金山文 `true`。
- 新增 `PowderSnowBlockMixin`（`canEntityWalkOnPowderSnow`）：雪地靴定义 `snowWalk` 生效。
  实测：铁山文靴 false，宋步兵靴 true。
- 修正 `data/chineseweapons/curios/tags/items/head.json` 中 3 个缺少命名空间的条目（原 D24），并刷新 1.21.1/26.1 四个 profile 的 golden manifest。
- Fabric 1.20.1/1.21.1/26.1 构建通过；`FabricArmorItem` 的注释同步更新为“由 Mixin 读取”。

## 2026-11-06 追加：8/8 构建与其余断言

- 全量构建：fabric-1.20.1/1.21.1/26.1、forge-1.18.2/1.19.2/1.20.1、neoforge-1.21.1/26.1 全部 `BUILD SUCCESSFUL`。
- A15：铁渤海头盔 `isDyeable=true`，铁睚眦头盔 `false`（FabricArmorItem 读取 definition）。
- A30：NBT 写入精准采集 I 的钻石镐挖远古宝石矿，掉落矿石本身（`chineseweapons:ancient_gem_ore`），精准采集生效。
- 26.1 的 `LivingEntity#getArmorSlots` 已移除，猪灵 Mixin 改为逐个 `EquipmentSlot` 读取 `getItemBySlot`。

## 2026-11-06 追加：木戈合成返还自身（Fabric）

- 新增 `ItemMixin`：
  - 1.20.1/1.21.1：`hasCraftingRemainingItem` / `getCraftingRemainingItem` 对 `wooden_dagger_axe` 返回 true/自身；
  - 26.1：`getCraftingRemainder` 返回 `new ItemStackTemplate(自身)`。
- 实测 Fabric 1.20.1：木戈 `hasCraftingRemainingItem=true`、`getCraftingRemainingItem()==自身`；铁戈 false。
- Fabric 1.20.1/1.21.1/26.1 均构建成功。
- 同步更新 `FabricDaggerAxeItem` 中“Fabric 无法实现”的旧注释。

## 2026-11-07 追加：Fabric 1.20.1 KubeJS 集成 / 脚本设置空键修复

- Fabric 1.20.1 新增 KubeJS 插件（从 Forge 1.20.1 移植 `compat/kubejs/*` + `kubejs.plugins.txt`），build.gradle 通过 `debug_kubejs` 坐标提供 `modCompileOnly`。
- 实测：KubeJS server script 中 `ChineseWeapons` 可用；`setReachBonus('chineseweapons:iron_glaive', 3.0)` 后读取为 3.0；`setSongStandingShieldBehindCenterChance(6.0)` 后 `getArmorThreshold(null, ...)` 为 6.0。
- 修复 `WeaponScriptSettings` 的 null 键问题：四个缓存从 `ConcurrentHashMap` 改为 `Collections.synchronizedMap(new HashMap<>())`，getter 允许 `item == null`；原先立盾/面甲的 null itemId 绑定会 NPE（D27），现在可用。该修改覆盖 8 个 target。
- 其余加载器的 KubeJS 插件仍未实现：Fabric 1.21.1/26.1 缺少对应 KubeJS 依赖；NeoForge 1.21.1 的 KubeJS 2101 已改用 `dev.latvian.mods.kubejs.plugin.KubeJSPlugin` 新 API，需要单独移植；Forge 1.18.2 也没有。

## 2026-11-07 追加：JEI/EMI 全目标接入 / 锻造台 shift-click 修复

- JEI 已接入全部 8 个 target（compile-only，不打包 JEI 类）：
  - Fabric 1.20.1 用 `maven.modrinth:jei:15.20.0.104`，通过 `modCompileOnly` 编译；
  - Fabric 1.21.1 用 `mezz.jei:jei-1.21.1-common-api:19.70.0.469`（Modrinth 的通用版本号会解析到 Forge 包，故改用 API 包）；
  - Fabric 26.1 用 `maven.modrinth:jei:29.53.0.115`；
  - NeoForge 1.21.1 用 `maven.modrinth:jei:19.70.0.469`；NeoForge 26.1 用 `maven.modrinth:jei:29.53.0.115-neoforge`；
  - 26.1 客户端没有完整配方表，JEI 插件读取 `mezz.jei.common.Internal.getClientSyncedRecipes()`，并由 NeoForge `OnDatapackSyncEvent` 请求同步（NeoForge 26.1）。
- EMI 接入：Forge 1.19.2/1.20.1（原有）、Fabric 1.20.1/1.21.1、NeoForge 1.21.1。Fabric 通过 `fabric.mod.json` 的 `emi` entrypoint，NeoForge 通过 `@EmiEntrypoint` 扫描。1.18.2 与 26.1 没有可用的 EMI 构建，未接入。
- 修复 8 个 target 的 `WeaponsCastingTableTypeGuiMenu#quickMoveStack` 差一错误（原 D02）：输出槽实际为 10、输入槽 0–9、背包 11–46；现在 shift-click 输出/输入会在正确槽位间移动。
- 修复 `WeaponScriptSettings` 的 null itemId NPE（D27）并补充 Fabric 1.20.1 KubeJS 插件（见上一节）。
