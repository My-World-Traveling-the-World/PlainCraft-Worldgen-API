# 云游四海世界生成 API

**PlainCraft Worldgen API** · 道路、长城与兼容河流模组的共用前置。

`Minecraft 1.21.1` · `Fabric` · `Java 21+`

[下载](https://github.com/My-World-Traveling-the-World/PlainCraft-Worldgen-API/releases) · [版本更新](CHANGELOG.md) · [开发指南](HANDOFF.md) · [MIT 许可](LICENSE)

| 接口 | 用途 |
| --- | --- |
| 生成就绪通知 | 协调河流、长城和道路的预热顺序。 |
| 结构预留与道路查询 | 让兼容模组交换保护范围和道路计划。 |
| 运行负载信号 | 为区块预生成、巡航等兼容模组提供调度信息。 |

实际生成由各玩法模组负责。

## 安装

使用 **Fabric Loader 0.16.10+**，把正式 `plaincraft-worldgen-api-<版本>.jar` 放进 `mods`。单装一个玩法模组和三者合装都只需一份前置。升级时替换旧 `roadweaver-api-fabric-*.jar`，两个发行名称使用同一个模组 ID `roadweaver_api`。
