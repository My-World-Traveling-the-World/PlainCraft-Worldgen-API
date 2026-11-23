# 世界生成 API 开发指南与交接

[项目介绍](README.md) · [版本更新](CHANGELOG.md)

## 构建和验证

使用 JDK 21 和 Gradle 8.8。前置可以独立构建。

```powershell
& $gradle :api:contractJar :api-fabric:devModJar :api-fabric:contractIntermediaryJar :api-fabric:build --daemon --console=plain
```

`api-fabric/build/libs/plaincraft-worldgen-api-<版本>.jar` 为正式成品。各模块 `build/contract/` 保存无版本开发件。named 契约用于 Mojang 映射编译，dev mod 用于开发启动，intermediary 用于生产映射烟测。下游默认读取同级仓库，可用 `-PworldgenApiProjectDir=<路径>` 修改。

`api/` 维护公开 Java 契约，`api-fabric/` 负责 Fabric 打包。保留 `roadweaver_api` 和 `net.shiroha233.roadweaver.api` 的兼容边界。运行 `verifyStandaloneApi` 检查类与元数据，正式发包标签 `v<版本>` 需与 Mod 版本吻合。玩法实现及其测试由各消费方维护。

## 当前交接 · 2026-11-23

1. **改动**：拆分玩家说明、版本历史及开发指南。
2. **验证**：构建通过，`verifyStandaloneApi` 输出 `[API-STANDALONE] PASS plaincraft-worldgen-api-1.1.2.jar`。
3. **未验证**：合装新世界的完整预热和实际落块。
4. **负控**：本轮沿用独立验包，未新增接口与守卫。
5. **下一步**：API 变动时复测各玩法单装、三者合装以及缺前置时的错误提示。
6. **历史与回退**：[前置 v1.1.2](https://github.com/My-World-Traveling-the-World/PlainCraft-Worldgen-API/releases/tag/v1.1.2)。
