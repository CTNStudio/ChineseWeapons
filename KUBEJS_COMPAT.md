# Chinese Weapons：KubeJS 中文使用说明

适用环境：Minecraft Forge 1.20.1。

本模组提供 KubeJS 兼容支持，可以通过脚本修改武器的基础属性、攻击距离、技能概率，以及新增或删除本模组的武器铸造台配方。

KubeJS 兼容是可选功能。不安装 KubeJS 时，Chinese Weapons 仍然可以正常运行；安装 KubeJS 后，模组会自动启用下面的接口。

## 一、安装要求

需要在对应的 Forge 1.20.1 实例中安装：

- Chinese Weapons
- KubeJS 6（Forge 1.20.1 版本）
- Rhino（KubeJS 前置）
- Architectury API（KubeJS 前置）

当前源码按以下版本编译验证：

- KubeJS `2001.6.5-build.26`
- Rhino `2001.2.3-build.10` 或更高版本
- Architectury API `9.1.12` 或更高版本

KubeJS 本身要求 Rhino 和 Architectury API。它们不是 Chinese Weapons 的强制依赖，但只要安装了 KubeJS，就必须同时安装这两个前置。`mods.toml` 声明的 KubeJS 最低版本为 `2001.6.3`。

安装 KubeJS 后首次启动游戏或服务器，会自动生成 `kubejs` 文件夹。脚本放入这个文件夹内，不要放进 Chinese Weapons 的 JAR 文件中。

常用目录如下：

```text
kubejs/
├─ startup_scripts/       # 修改物品属性和技能参数
└─ server_scripts/        # 修改配方
```

## 二、修改物品属性

在以下位置新建文件：

```text
kubejs/startup_scripts/chineseweapons_items.js
```

示例：

```js
ItemEvents.modification(event => {
  event.modify('chineseweapons:iron_dagger_axe', item => {
    item.attackDamage = 10
    item.attackSpeed = -2.4
    item.maxDamage = 800
  })
})
```

常用字段：

```js
item.attackDamage = 10       // 攻击伤害
item.attackSpeed = -2.4      // 攻击速度
item.maxDamage = 800         // 最大耐久
```

物品 ID 必须写完整命名空间，例如：

```text
chineseweapons:iron_dagger_axe
chineseweapons:iron_glaive
chineseweapons:tang_dynasty_shield
```

修改 `startup_scripts` 后必须完全退出并重新启动游戏或服务器，单独执行 `/reload` 不会重新执行启动脚本。

## 三、修改武器技能参数

下面的接口由 Chinese Weapons 提供，写在 `startup_scripts` 中即可：

```js
ChineseWeapons.setReachBonus('物品ID', 数值)
ChineseWeapons.setHookDismountChance('物品ID', 概率)
ChineseWeapons.setHitEffectChance('物品ID', 概率)
ChineseWeapons.setGlaiveDismountChance('物品ID', 概率)
```

概率使用 `0.0` 到 `1.0` 的小数，不是直接写百分数：

```text
0.00 = 0%
0.30 = 30%
0.50 = 50%
1.00 = 100%
```

### 1. 戈右键钩取下马概率

```js
ChineseWeapons.setHookDismountChance(
  'chineseweapons:iron_dagger_axe',
  0.30
)
```

这表示：右键使用戈，钩取命中目标并成功造成伤害后，有 30% 概率让目标从坐骑上下来。

这个接口不会让左键普通攻击触发下马。目标没有骑乘生物时，也不会产生下马效果。默认概率也是 30%。

### 2. 命中特效概率

```js
ChineseWeapons.setHitEffectChance(
  'chineseweapons:iron_dagger_axe',
  0.80
)
```

表示使用该物品左键命中生物时，有 80% 概率施加模组的命中特效。戈和戟类物品都可以使用这个接口。

### 3. 戟类下马概率

```js
ChineseWeapons.setGlaiveDismountChance(
  'chineseweapons:iron_glaive',
  0.60
)
```

表示戟类攻击命中骑乘中的生物时，有 60% 概率使其下马。

### 4. 攻击距离

```js
ChineseWeapons.setReachBonus(
  'chineseweapons:iron_dagger_axe',
  3.0
)
```

这里填写的是在玩家原本实体攻击距离基础上增加的距离，不是最终总距离。`3.0` 表示额外增加 3 格。允许范围为 `0.0` 到 `64.0`。

### 5. 完整技能配置示例

```js
// kubejs/startup_scripts/chineseweapons_items.js

ItemEvents.modification(event => {
  event.modify('chineseweapons:iron_dagger_axe', item => {
    item.attackDamage = 10
    item.attackSpeed = -2.4
    item.maxDamage = 800
  })
})

// 戈右键钩取成功造成伤害后，30% 概率让骑手下马
ChineseWeapons.setHookDismountChance(
  'chineseweapons:iron_dagger_axe',
  0.30
)

// 左键命中后的特效概率为 80%
ChineseWeapons.setHitEffectChance(
  'chineseweapons:iron_dagger_axe',
  0.80
)

// 戟攻击命中骑手时，60% 概率使其下马
ChineseWeapons.setGlaiveDismountChance(
  'chineseweapons:iron_glaive',
  0.60
)
```

## 四、修改武器铸造台配方

在以下位置新建文件：

```text
kubejs/server_scripts/chineseweapons_recipes.js
```

### 新增配方

```js
ServerEvents.recipes(event => {
  event.recipes.chineseweapons.weapon_casting_shaped(
    'chineseweapons:iron_dagger_axe',
    [
      ' AB',
      'ABA',
      'B  '
    ],
    {
      A: 'minecraft:iron_ingot',
      B: 'minecraft:stick'
    },
    'minecraft:copper_ingot'
  ).id('kubejs:iron_dagger_axe_casting')
})
```

参数顺序固定为：

```text
输出物品、三行形状、材料键、主材料
```

上面示例的含义是：

- `A` 代表铁锭
- `B` 代表木棍
- 配方最后一个参数 `minecraft:copper_ingot` 是铸造台的主材料
- 结果为 1 个铁戈
- 配方 ID 为 `kubejs:iron_dagger_axe_casting`

图案必须是 3 行，每行 3 个字符；空位使用空格表示。材料键使用单个字符，且图案中出现的每个字符都要在键表中定义。

### 删除原有配方

```js
ServerEvents.recipes(event => {
  event.remove({
    type: 'chineseweapons:weapon_casting_shaped',
    id: 'chineseweapons:diamond_dagger_axe'
  })
})
```

配方 ID 可以通过 JEI 或 EMI 查看，也可以直接参考模组的 `data/chineseweapons/recipes` 文件夹。

### 替换配方材料或结果

KubeJS 的标准配方替换操作同样适用于 Chinese Weapons 的铸造台配方：

```js
ServerEvents.recipes(event => {
  event.replaceInput(
    { id: 'chineseweapons:iron_dagger_axe' },
    'minecraft:iron_ingot',
    'minecraft:gold_ingot'
  )

  event.replaceOutput(
    { id: 'chineseweapons:iron_dagger_axe' },
    'chineseweapons:iron_dagger_axe',
    'chineseweapons:iron_glaive'
  )
})
```

## 五、重载和生效方式

修改 `server_scripts` 后，在游戏或服务器中执行：

```text
/reload
```

修改 `startup_scripts` 后，必须重启游戏或服务器。

多人服务器中，配方脚本应放在服务器的 `kubejs/server_scripts` 目录，并重启或执行 `/reload`。客户端仍然需要安装与服务器匹配的 Chinese Weapons；如果客户端要显示物品，也需要安装该模组。

## 六、常见问题

### `ChineseWeapons is not defined`

确认 KubeJS 和 Chinese Weapons 都安装在当前使用的 Forge 1.20.1 实例中，并确认模组版本包含 KubeJS 兼容功能。修改启动脚本后重启游戏，不要只执行 `/reload`。

### 配方没有变化

确认文件位于 `kubejs/server_scripts`，配方类型写成：

```text
chineseweapons:weapon_casting_shaped
```

然后执行 `/reload`。如果是新增配方，确保配方 ID 使用自己的命名空间，例如 `kubejs:my_recipe`，不要覆盖已有 ID。

### 概率写成 30 以后报错或效果异常

概率必须写成小数 `0.30`，不能写成 `30`。有效范围是 `0.0` 到 `1.0`。

### 戈还是 100% 让骑手下马

请确认修改的是：

```js
ChineseWeapons.setHookDismountChance('chineseweapons:iron_dagger_axe', 0.30)
```

它只控制右键钩取命中并造成伤害后的下马概率。改完 `startup_scripts` 后必须完全重启游戏或服务器。

### 查看错误日志

脚本加载失败时，优先查看：

```text
logs/kubejs/startup_errors.txt
logs/kubejs/server_errors.txt
logs/latest.log
```
