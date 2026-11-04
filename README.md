# Chinese Weapons

A mod of Chinese classic armors

- mod id：chineseweapons
- Java 包名 / Gradle group：net.mirrorloong.chineseweapons
- 作者：CTN-STUDIO,Xiris177,Prizmarin,EndYigjie,sf.,BaiGave,spake404,MCcaptain5412,Slime&#95;Dragon,Ku00115
- 声明的许可证：All Rights Reserved, The detailed content can be found in the guidebook or on the official website of CTN Studio

## 项目状态

项目已从 Forge 1.20.1 单版本工程迁移到 `common + targets/<loader>-<version>` 结构。原 1.20.1 源码与资源位于 `common/`；loader 入口、注册、网络、客户端钩子及经源码确证的版本差异实现位于各 target。common 通过 `net.mirrorloong.chineseweapons.spi`（`Platform`、`Registration`、`Network`）获取目标平台能力，target 只保留具体实现。

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

- **数据与资源格式**：`common/src/main/resources` 仍保留 1.20.1 布局，1.21.1/26.1 的 target 已各自提供迁移后的 `recipe/`、`loot_table/`、`advancement/`、`tags/`、`worldgen/`、`assets/<ns>/items/`、`equipment/` 等资源；common 的旧目录会一起打进 jar，但在这些版本中不被读取，属于惰性数据。迁移结果只做了 JSON/构建校验，**未加载数据包验证**。Fabric 1.20.1/1.21.1 因原版无 Forge `separate_transforms` 等价物，37 个武器模型被压平为 GUI 模型，外观与原版 Forge 目标不同。
- **可选集成**：JEI / EMI / KubeJS / Patchouli 只在存在可用坐标的 target 中保留；1.18.2 省略 KubeJS 与 EMI，1.19.2 省略 Patchouli，Fabric 与 1.21.1+ 未接入这些集成。
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
- 普通 common 内容修改无需运行源码同步任务；变更 Gradle 配置或 `commonJava` 排除规则后需重新加载 Gradle。

在所选 target 目录执行：

```powershell
.\gradlew.bat clean build
.\gradlew.bat verifyHotDebug
.\gradlew.bat runClient
# 或 runServer；Minecraft EULA 必须由用户阅读并接受
```

在仓库根可执行 `./gradlew -p common build` 单独检查共享层，但不能替代目标矩阵验证。根 `-Ptarget=<目录名> build` 调用该 target 的独立 wrapper；`-PallTargets=true build` 自动选择已启用且 `ci.java` 不高于当前 Gradle JVM 的目标，并打印跳过项。最低 JDK 筛选不保证旧 wrapper 支持任意新版 JDK，必要时分别构建。根 `build` 默认只验证 common。

> `common` 现在只保留 SPI、内容定义、注册表和版本中立逻辑（42 个 Java 文件）。具体内容构造（装备/武器/特殊物品/方块/方块实体/实体/效果/音效/创造模式标签页）和事件型 procedure 由各 target 提供。为让 `-p common build` 继续可用，common 的独立构建会把 `targets/forge-1.20.1/src/main/java` 作为参考实现一起编译；它仍是编译检查，不等于任何 target 的运行验证。

### common 排除数量

每个 target 的 `commonJava { exclude ... }` 只允许保留 recipes/network/gui 等少量 API 差异文件：

| Target | commonJava 排除数 |
| --- | --- |
| forge-1.20.1 | 0 |
| forge-1.19.2 | 8 |
| forge-1.18.2 | 8 |
| fabric-1.20.1 | 1 |
| fabric-1.21.1 | 4 |
| fabric-26.1 | 5 |
| neoforge-1.21.1 | 4 |
| neoforge-26.1 | 5 |

新增 target 时保持该上限：优先扩展 `spi.ContentFactory`/`Platform`/`Network` 或把纯数据放回 common，不要恢复按材质逐个 fork 的物品类。

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
