# 云游四海世界生成 API / PlainCraft Worldgen API

Minecraft 1.21.1 Fabric 前置，为 RoadWeaver 重制版、云游四海长城和兼容的河流模组提供共同接口。它传递世界生成就绪状态、结构预留和道路信息，本身不生成地形、道路或建筑。

## 安装

需要 Minecraft 1.21.1、Fabric Loader 0.16.10 或以上、Java 21 或以上。从 Releases 下载 `plaincraft-worldgen-api-<版本>.jar` 放进 `mods`。任何一个玩法模组可以单独搭配此前置，合装也只放一份。升级请用新 JAR 替换旧的 `roadweaver-api-fabric-*.jar`：两个发行名称的模组 ID 都是 `roadweaver_api`。

## 开发

此前置可在没有任何玩法仓库的情况下构建。使用 JDK 21 和 Gradle 8.8：

```text
gradle :api:contractJar :api-fabric:devModJar :api-fabric:contractIntermediaryJar :api-fabric:build
```

`api-fabric/build/libs/plaincraft-worldgen-api-<版本>.jar` 是玩家安装件；两个模块 `build/contract/` 下的无版本 JAR 分别用于开发编译、开发启动和正式映射测试。在三个玩法仓库中，默认查找同级 `plaincraft-worldgen-api`，可用 `-PworldgenApiProjectDir=<路径>` 修改。Java 包和已有公开签名沿用 `net.shiroha233.roadweaver.api`。

## License

MIT. The Fabric library provides shared integration contracts; install it once alongside any supported gameplay mod. It is independent of Minecraft generation logic in the consuming mods.
