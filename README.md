# Chinese Weapons

A mod of Chinese classic armors

- mod id：chineseweapons
- Java 包名 / Gradle group：net.mirrorloong.chineseweapons
- 作者：CTN-STUDIO,Xiris177,Prizmarin,EndYigjie,sf.,BaiGave,spake404,MCcaptain5412,Slime&#95;Dragon,Ku00115
- 声明的许可证：All Rights Reserved, The detailed content can be found in the guidebook or on the official website of CTN Studio

## 项目状态

项目已从 Forge 1.20.1 单版本工程迁移到 `common + targets/<loader>-<version>` 结构。common 只保留 SPI、内容定义、注册表、共享玩法规则（`rules/*`）和版本中立逻辑；loader 入口、具体内容构造、事件订阅、网络、客户端渲染等实现位于各 target。common 通过 `net.mirrorloong.chineseweapons.spi`（`Platform`、`Registration`、`Network`、`ContentFactory`、`GameplayCompat`）获取目标平台能力。

当前状态：**八个 target 均已通过 `clean build` 并生成 jar（构建验证）。尚未启动游戏或使用 AutoJS 做运行时验证**，因此注册时序、网络、配方加载、渲染和数据包格式均未确认。功能清单、安装说明、截图和发布说明由维护者补充。

以下矩阵在初始化时从实际 target 目录及 `ci.properties` 生成。CI 启用仅表示会参与验证，不等于已验证支持；后续增删版本或改变工具链时同步维护本表。

| Target | Gradle JVM | CI 配置 | 项目验证状态 |
| --- | --- | --- | --- |
| fabric-1.20.1 | JDK 17 | true | Build 通过；运行未验证 |
| fabric-1.21.1 | JDK 21 | true | Build 通过；运行未验证 |
| fabric-26.1 | JDK 25 | true | Build 通过；运行未验证 |
| forge-1.18.2 | JDK 21 | true | Build 通过；运行未验证 |
| forge-1.19.2 | JDK 21 | true | Build 通过；运行未验证 |
| forge-1.20.1 | JDK 21 | true | Build 通过；运行未验证（参考实现） |
| neoforge-1.21.1 | JDK 21 | true | Build 通过；运行未验证 |
| neoforge-26.1 | JDK 25 | true | Build 通过；运行未验证 |

## 已确认的迁移限制

- **数据与资源格式**：`common/src/main/resources` 是唯一的 1.20.1 权威资源。1.21.1/26.1 的 `recipe`、`loot_table`、`advancement`、`tags`、`worldgen`、`assets/<ns>/items`、`equipment` 与装备纹理由下文「资源生成管线」在构建时生成，target 源码树中不再保留这些副本；1.18.2/1.19.2/1.20.1 仍直接使用 common 资源。common 的旧目录仍会打进现代版本 jar，但不会被读取。生成结果只做 JSON/构建/基准哈希校验，**未做数据包加载验证**。Fabric 1.20.1/1.21.1 因原版无 Forge `separate_transforms` 等价物，37 个武器模型被压平为 GUI 模型；26.1 Fabric 将武器 3D 模型内联为 `_3d`，NeoForge 使用 `_hand` 中间模型。
- **可选集成**：JEI 已接入全部 8 个 target；EMI 覆盖 Forge 1.19.2/1.20.1、Fabric 1.20.1/1.21.1、NeoForge 1.21.1（1.18.2 与 26.1 无对应 EMI 构建）；KubeJS 覆盖 Forge 1.19.2/1.20.1 与 Fabric 1.20.1。所有集成均为 compile-only，未安装对应 mod 时不会加载。
- **平台专属渲染**：Forge 的 `IClientItemExtensions`/`initializeClient` 没有跨 loader 等价物。Fabric 目标使用各自的 armor renderer；26.1 取消 3D 盔甲模型钩子，改为 equipment asset 纹理，原 Blockbench 几何与染色贴图合并不再生效。
- **热调试**：`fabric-1.21.1` 的 `hot-debug.gradle` 因本机无法获取对应 AutoJS 坐标而暂时注释；其余 target 保留。`verifyHotDebug` 通过不等于启动验证。
- **26.1 差异**：盔甲几何改为 equipment asset 纹理（无 Blockbench 几何、无染色贴图合并）；部分 Forge 专属钩子（岩浆行走、猪灵中立、爆炸保护事件等）不再存在。
- **原 1.20.1 遗留问题**：`data/chineseweapons/recipes/dyeable_armor.json` 指向的 `crafting_special_dyeablearmor` 序列化器从未注册，所有版本都保留该问题。

## 开发与构建

开发规范见 [AGENTS.md](AGENTS.md)。共享业务与资源位于 `common/`；loader / Minecraft 差异位于 `targets/<loader>-<version>/`。所有 target 直接编译 common 原源码，不引用共享 jar，也不等待复制到 build 目录。

- 单版本：IDEA 打开对应 target 目录，使用该目录的 wrapper 和矩阵声明的 JDK。
- 全版本：IDEA 打开 `idea-workspace/`；当前主版本为 **neoforge-1.21.1**，配置的 Gradle JVM 为 **JDK 21**。该值来自 target 描述文件，仍须核对工作区 wrapper 与所用 JDK 的兼容性。
- 工作区只完整导入主 target。其他版本、文档、脚本及根配置作为仓库浏览内容展示，不承诺非主版本的 API 补全。common 源码只归属主 target，不额外关联根工程、common 或其他完整 target。
- 切换主版本修改 `idea-workspace/gradle.properties` 的 `workspace_main_target` 后重新加载 Gradle。composite build 共用工作区的 Gradle/JVM，不使用被包含 target 的 wrapper；若不兼容，独立打开 target 或调整工作区工具链。
- 普通 common 内容修改无需运行源码同步任务；资源修改会被生成任务自动纳入。变更 Gradle 配置或 `commonJava` 排除规则后需重新加载 Gradle。

## 资源生成管线（1.21.1 / 26.1）

`common/src/main/resources` 是 1.20.1 布局的权威副本。四个现代 target（`1.21.1-fabric`、`1.21.1-neoforge`、`26.1-fabric`、`26.1-neoforge`）在 `processResources` 之前运行 `scripts/generate-resources.py`，把转换结果写入 `build/generated/resources/<profile>`，并将该目录放在资源源集最前面（重复路径以生成结果为准）。因此：

- target 的 `src/main/resources` 只保留 metadata 与 target 专属覆盖：`fabric.mod.json`、`chineseweapons.mixins.json`、`META-INF/neoforge.mods.toml`、`pack.mcmeta`，以及 26.1 的 `assets/minecraft/models/item/shield*.json`（原版模型覆盖）。其余 recipe/advancement/loot/tags/worldgen/items/equipment/模型/装备纹理均由生成器产出，不要再手工复制。
- 生成任务：`./gradlew generateResources`（`processResources` 自动依赖）。`./gradlew checkGeneratedResources` 会把当前输出与 `scripts/resource-golden/<profile>.json`（迁移前手写树的语义哈希基准）比对。基准仅在确认资源内容有意变化后刷新。
- 独立使用（Python 3，无第三方依赖）：
  ```bash
  python scripts/generate-resources.py --list
  python scripts/generate-resources.py --all
  python scripts/generate-resources.py --profile 26.1-neoforge --check-golden
  python scripts/generate-resources.py --profile 1.21.1-fabric --output /tmp/out --check /path/to/reference-tree
  python scripts/generate-resources.py --profile 26.1-neoforge --write-golden /path/to/reference-tree  # 仅在确认内容有意变化后刷新基准
  ```
  Gradle 默认调用 `python`；解释器不同时传 `-PresourceGeneratorPython=<path>`。
- profile 位于 `scripts/resource-profiles/*.json`，字段 `family`（`1.21.1`/`26.1`）、`loader`、`output` 和 `features`。loader 差异（NeoForge `neoforge:conditions` / biome modifier / `armor_repair` 标签 / `_hand` 模型 vs Fabric `c:` 标签 / `_3d` 模型 / equipment 命名）通过这些开关表达，不要在 target 里复制资源。
- 转换范围：目录单数化（`recipe`/`advancement`/`loot_table`/`tags/item|block`）、recipe `result` 的 `id`、26.1 holder-set 字符串与 `ItemStackTemplate`、loot 的 enchantment predicate、advancement icon/background、worldgen 矿石目标标签、NeoForge 条件与 biome modifier、26.1 `assets/<ns>/items/*.json`、equipment asset + `textures/entity/equipment/*`、按装备 id 修复 `armor_repair` 标签。
- 已知差异：Fabric 的 1.21.1/26.1 旧手写树保留了 CRLF 与原始缩进，生成器统一输出 LF + 两空格；JSON 语义完全一致，`26.1-neoforge` 与旧树逐字节一致。资源生成不改变已注册 id、标签值、纹理路径或 pack format。

在所选 target 目录执行：

```powershell
.\gradlew.bat clean build
.\gradlew.bat generateResources
.\gradlew.bat checkGeneratedResources
.\gradlew.bat verifyHotDebug
.\gradlew.bat runClient
# 或 runServer；Minecraft EULA 必须由用户阅读并接受
```

在仓库根可执行 `./gradlew -p common build` 单独检查共享层，但不能替代目标矩阵验证。根 `-Ptarget=<目录名> build` 调用该 target 的独立 wrapper；`-PallTargets=true build` 自动选择已启用且 `ci.java` 不高于当前 Gradle JVM 的目标，并打印跳过项。最低 JDK 筛选不保证旧 wrapper 支持任意新版 JDK，必要时分别构建。根 `build` 默认只验证 common。

> `common` 现在只保留 SPI、内容定义、注册表、共享玩法规则和版本中立逻辑（50 个 Java 文件）。具体内容构造（装备/武器/特殊物品/方块/方块实体/实体/效果/音效/创造模式标签页）、事件订阅、配方、网络包和 GUI 实现由各 target 提供。`rules/*` 承载战斗、套装、武器技能、面甲、染料和铸造台等纯算法；target 的 `procedures/*` 是事件适配层，只读取事件数据并调用规则与 `spi.GameplayCompat`。为让 `-p common build` 继续可用，common 的独立构建会把 `targets/forge-1.20.1/src/main/java` 作为参考实现一起编译；它仍是编译检查，不等于任何 target 的运行验证。

### common 排除数量

所有 target 的 `commonJava { exclude ... }` 已清空为 0：

| Target | commonJava 排除数 |
| --- | ---: |
| forge-1.20.1 | 0 |
| forge-1.19.2 | 0 |
| forge-1.18.2 | 0 |
| fabric-1.20.1 | 0 |
| fabric-1.21.1 | 0 |
| fabric-26.1 | 0 |
| neoforge-1.21.1 | 0 |
| neoforge-26.1 | 0 |

新增版本差异时禁止再往 `commonJava` 加 exclude：应扩展 `spi.ContentFactory`/`Platform`/`Network`，把纯数据（数值、id、顺序）放回 common，或把具体实现完整放进 target；不要按材质/套装逐个 fork 物品类。

## AutoJS 热调试与热执行

运行时调试必须使用 AutoJS，不得仅凭源码推断、临时日志或构建成功宣称游戏行为正确。

每个 target 的 `debug_*` 属性声明来自 Sighs Maven 的 AutoJS、Rhino 及可用的 KubeJS。具体版本和模式以该 target 配置为准；没有 `debug_kubejs` 时仅用 Rhino，不假设存在 KubeJS 事件或 `kubejs_log`。调试依赖仅用于开发游戏运行，不嵌入发布 jar，不成为玩家端必需依赖。

1. 使用相应 JDK 执行 `verifyHotDebug`，再启动 target 的游戏实例。
2. 默认 `GET http://127.0.0.1:25585/health` 探活，通过 MCP 客户端连接 `http://127.0.0.1:25585/mcp`，先调用 `status` 确认 side 和引擎模式。
3. 用 `eval_js` 执行短脚本，例如 server side 的 `AutoJS.snapshot()`；检查 `isError`、`ok`、`diagnostics`，触发行为后回读状态并形成断言。界面和输入用 client，权威世界修改用 server。
4. 修改脚本用 `reload_scripts`；KubeJS 模式结合 `kubejs_log` 检查错误，Rhino 模式重建作用域后重新注入验证脚本。配方、标签等按需另做 `/reload`。
5. Java、Mixin 和启动期注册内容不能靠脚本热重载替换，需构建重启后再用 AutoJS 验证。

配置位于游戏目录 `config/autojs.json`；多实例用 `AUTOJS_PORT` 分配不同端口。端点默认可执行任意 Java，只用于本机开发，不得暴露公网或玩家生产环境。共享机器配置 token，请求带 `Authorization: Bearer <token>`，不提交凭据。完整操作手册可从 AutoJS 发布 jar 提取 `SKILL.md`。仓库或端点不可用时明确记录阻塞，不用本地替代 jar 冒充远端验证。

## 发布与维护

- curseforge: project ID not configured in root gradle.properties; environment overrides and credentials remain unverified.
- modrinth: project ID not configured in root gradle.properties; environment overrides and credentials remain unverified.

发布前验证承诺支持的每个 target：构建、检查 jar 的 metadata/共享类/资源、使用 AutoJS 做最小 client 和 dedicated server 行为验证，并记录实际范围。初始化没有运行这些验证，不得把上表的待验证状态当作兼容性承诺。

- [发布指南](docs/PUBLISHING.md)
- [维护流程](docs/MAINTENANCE_WORKFLOW.md)
- [自动发现与新增 target](docs/CI_TARGET_DISCOVERY.md)
- [版本迁移参考](docs/version-differences/README.md)

工程来源：[开发框架](https://github.com/Tower-of-Sighs/SighsTemple)。本 README 与 AGENTS.md 已转为项目文档，后续由项目维护者更新，不再运行初始化脚本覆盖。
