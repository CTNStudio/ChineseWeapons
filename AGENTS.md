# Chinese Weapons 开发规范

项目 mod id 为 chineseweapons，Java 包名 / Gradle group 为 net.mirrorloong.chineseweapons。功能与目标配置见 [README](README.md)。本工程已完成初始化并从 Forge 1.20.1 单版本迁移到 common + targets 结构；不得再次运行初始化脚本覆盖维护者编辑的文档。迁移的构建结果与已知限制以 README 为准，禁止把构建成功表述为运行时验证。

## 迁移状态与共享契约

- 八个 target（forge-1.18.2/1.19.2/1.20.1、fabric-1.20.1/1.21.1/26.1、neoforge-1.21.1/26.1）目前均通过 `clean build`；游戏启动、AutoJS、数据包加载和渲染均未验证。
- common 通过 `net.mirrorloong.chineseweapons.spi` 使用目标能力：`Platform`（注册、网络、菜单、客户端标记）、`Registration`/`RegistryBuilder`/`RegistryEntry`（内容注册）、`Network`/`ModPacket`/`PacketDirection`（自定义包）。`ChineseWeapons.bootstrap(Platform)` 是共享启动入口，target 在 mod 构造期设置实现并调用。
- 目标实现放 target：Forge/NeoForge 用 DeferredRegister、SimpleChannel/payload，Fabric 用 Registry/回调/payload。不要把这些实现塞回 common。
- 只有确认某文件在当前 target 无法编译或语义不同，才将其复制到 target 同 FQCN 并加入该 target 的 `commonJava { exclude }`；为跨版本重复的业务逻辑应优先抽回 common（例如数值/数据结构），只留下 API 适配。
- `RegistryEntry.id()` 返回共享值类型 `spi.ResourceId`（namespace/path），不再引用 `ResourceLocation`/`Identifier`；26.1 的 `spi/RegistryEntry` fork 已删除。`Registration` 同时提供 `ResourceKey` 与 `builder(String registryId)` 两种入口，后者由各 target 映射到自己的注册表常量。
- 具体内容构造（装备/武器/特殊物品/方块/方块实体类型/实体类型/效果/音效/创造模式标签页）由 `spi.ContentFactory` 实现，放在各 target；common 的 `init/*` 只持有注册 id、定义记录和标签页条目顺序。
- `commonJava` 排除预算是每 target 少于 10 项，且只允许 recipes/network/gui 等无法用 SPI 表达的文件；当前矩阵见 README。新增版本差异应优先扩展 `ContentFactory`/`Platform`/`Network`，把纯数据（数值、id、顺序）放回 common，禁止按材质/套装逐个 fork 物品类。
- 事件型 procedure 目前位于各 target（事件总线和伤害/组件 API 差异过大）；common 只保留与平台无关的定义和 SPI。若要把某段逻辑抽回 common，必须先确认它在所有 target 的 API 下可编译，并同步移除对应 target 实现。
- `common/src/main/resources` 保持 1.20.1 布局；1.21.1/26.1 的 target 已各自提供迁移后的 recipe/loot_table/advancement/tags/worldgen/items/equipment 资源。common 的旧目录仍会打进这些 jar 但不会被读取；不要在 target 中重复同路径资源，也不要声称数据包已通过运行时验证。

## 架构与修改边界

- 默认将没有确证 loader / Minecraft 版本差异的代码与资源放在 `common/`，允许引用 Minecraft 和 loader 类型；平台类型依赖本身不是拆分理由。
- 只有阅读实际源码/API 确证差异后，才将相应实现移到 target；差异记录在 target 的 `commonJava { exclude 'path/to/File.java' }`，只排除共享来源文件，不排除 target 的替代实现。
- `shared.gradle` 直接将 common 原文件接入 target 源码集。禁止复制源码到 build、依赖 common jar、重复添加 common 源码根或使用 `project(':common')`。
- 不以运行时版本判断、反射分发或 classpath 同名类覆盖隐藏兼容性问题。每个产物只对应一个 loader 和 Minecraft 版本，不发布 universal jar。
- 单 target 改动限定在该目标；common 改动需验证所有受影响目标。不得顺手修改其他版本或未经验证统一 API。
- 共享资源放 common；metadata、Mixin 配置、access widener / transformer 和版本专属资源放 target。同路径资源明确唯一归属，不依赖覆盖顺序。
- 共用模组元数据放根 `gradle.properties`，版本、loader、JDK 和 `debug_*` 放 target 属性。普通本地 jar 放目标 `libs/`，不假定存在传递依赖。
- 插件和运行配置归 target；通用源码接入、热调试、发布逻辑分别复用 `gradle/target-conventions/shared.gradle`、`hot-debug.gradle`、`publish.gradle`。

## IDE 与构建

- 单版本开发独立打开目标目录，使用其 wrapper 与 `ci.properties` 指定的 JDK。
- 全版本浏览打开 `idea-workspace/`，当前配置主版本为 neoforge-1.21.1（JDK 21）；修改主版本以工作区属性为准。主模块唯一管理 common，浏览模块覆盖文档、根配置、脚本和其他 target，不承诺非主版本 API 补全。
- 不额外关联独立 common、根构建或其他完整 target；浏览根排除主 target、common/src、缓存及运行产物。common 内容修改应直接反映到目标索引，构建配置与排除规则修改后重新加载 Gradle。
- 工作区 composite build 共用工作区 Gradle/JVM，不使用被包含目标的 wrapper；切换前检查兼容性，非主版本用独立 wrapper 验证。
- 根和 CI 自动扫描 targets，禁止维护固定版本名单。`ci.enabled` 是验证配置，不是兼容性证据。新增目标遵循 [自动发现指南](docs/CI_TARGET_DISCOVERY.md)。
- 标准验证：target 目录执行 `clean build`；共享层可执行根 `./gradlew -p common build`，它会连同 `targets/forge-1.20.1/src/main/java` 参考实现一起编译，但不是目标矩阵验证。产物需检查 metadata、共享类、资源及版本范围。

## 必须使用 AutoJS 热调试

- 游戏运行时调试与行为验证必须用 AutoJS：先 `verifyHotDebug` 解析依赖，再启动实例，通过 `status` 确认可用 side 和模式，用 `eval_js` 触发行为并回读真实状态、执行可重复断言。
- 每次检查 `isError`、`ok`、`diagnostics`；不能以无异常、构建成功或静态推测代替运行验证。记录 target、引擎、side、脚本和实际返回结果。
- server side 负责权威世界状态；client side 负责 UI/输入。需要后台调试时使用 `AutoJS.background(true)`，结束后恢复。
- 修改脚本使用 `reload_scripts`；有 KubeJS 时配合 `kubejs_log`，仅 Rhino 时重建作用域后重新注入脚本，不假定存在 KubeJS 事件。配方、标签、战利品表按需另做 `/reload`。
- Java/Mixin 改动和新增启动期注册内容需重新构建、重启，再用 AutoJS 验证；脚本重载不是 Java 热替换。
- AutoJS、Rhino、可用的 KubeJS 仅进入开发游戏运行配置，不进入 common 编译依赖、发布 jar 或玩家必需 metadata。Forge 生产 jar 使用重映射配置并显式补齐依赖，不直接加入普通 runtimeOnly。
- 默认端点 `http://127.0.0.1:25585/mcp`，`/health` 探活；配置在游戏目录 `config/autojs.json`，多实例使用 `AUTOJS_PORT`。从 AutoJS jar 提取 `SKILL.md` 查详细用法，不依赖维护者机器上的绝对路径。
- 任意代码执行端点只供本机开发，禁止暴露公网或玩家生产环境；共享环境设置 token，不提交凭据，不操作无关世界/文件。首次 Minecraft EULA 必须由用户阅读并接受。
- 仓库不可访问、依赖缺失、游戏或端点不能启动时记录具体阻塞，不能静默改用本地替代物或伪称验证完成。

## 发布、文档与交付

- 发布前逐目标完成构建与对应 client/server 行为验证，检查最终 jar；发布平台 ID 和凭据按 [发布指南](docs/PUBLISHING.md) 配置，token 只用环境变量。
- `mod_license` 元数据声明不自动替换 LICENSE 正文，维护者需核对正文、依赖和素材授权。不凭许可证参数声称完成法律授权检查。
- 修改功能、支持范围、依赖、工具链或工作区配置后同步 README、AGENTS.md 与相关 docs；文档归维护者管理，不自动重写整个文件。
- 不提交密钥、IDE 缓存、运行世界和构建输出。交付说明区分已验证、未验证和阻塞；不要将模板历史测试作为本项目的验证记录。
- 日常维护与迁移流程见 [维护指南](docs/MAINTENANCE_WORKFLOW.md)；版本/API 差异见 [迁移参考](docs/version-differences/README.md)。
