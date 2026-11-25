# 世界生成 API 开发指南与交接

## 2026-11-25 · 三维道路地图契约（1.3.0）

1. **改动**：新增RoadOverlayApi，提供真实XYZ、道路/桥/隧道/交叉类型、不可变快照、单次输出顶点预算和服务端权限判据。地图适配不依赖道路内部实现；原WorldgenPreviewApi及所有存档格式不变。
2. **验证**：previewContractTest共11项检查；named/intermediary契约、开发件、正式build及独立验包通过。新契约被真实Diorama隔离客户端消费；不把合成提供方当成实际路网验收。
3. **未验证**：不提供远端服务器网络同步，也不验证真实整合包所有路线的视觉贴合、桥隧实际产物或帧率。
4. **负控**：Line保留调用方可变列表→清空原列表后契约测试实际红；git checkout精确恢复。相邻Diorama项目build/mutation-mutable-map-line.log记录失败。
5. **下一步**：联机地图另做经服务端授权的数据协议；依然禁止地图观察隐式规划、加载世界或恢复存档。完整整合包中新旧世界切换及大路网窗口需实测。
6. **历史与回退**：基线906b3a6，行为新增；下游道路最低前置版本同步到1.3.0。地图功能与原远景契约独立，可分别回退对应消费方。部署与提交以本轮最终记录为准。

## 2026-11-25 · 只读远景契约（1.2.0）

1. **改动**：新增WorldgenPreviewApi。提供方按当前世界句柄返回UNAVAILABLE/EMPTY/READY、GEOMETRY/BLOCKS与不可变区间；查询不得规划、加载或读盘。完成通知仅表达真实落块结束，消费者在自己的预算内刷新。四个玩法模组使用统一契约，前置不依赖SeedGen或任何玩法实现。
2. **验证**：外置Gradle8.8/JDK21，previewContractTest六项通过；contractJar、devModJar、intermediary契约与正式build成功，verifyStandaloneApi为PASS。真实Fabric隔离服务器已消费契约完成WWOO合成和THT成功写入验证。
3. **未验证**：未验收完整整合包所有远景画面、任意服务器同步和所有模组特征。接口并不承诺GEOMETRY等同最终桥隧或河岸方块；不能据此宣布原有道路规划失败或长城收口问题修复。
4. **负控**：将runs的List.copyOf改成保留可变原列表，契约测试实际AssertionError；git checkout精确恢复后六项与构建重新通过。日志build/preview-mutation-mutable-snapshot.log与preview-api-verified.log。
5. **下一步**：消费方继续按实际施工和预测层分开验收；未命中返回未知，禁止为了远景隐式扩大世界生成。需要联机远景时另设计经授权的服务器数据传输，不能只按种子猜测私有计划。
6. **历史与回退**：基线1aaa7cb，行为新增。不改RoadData、台账及配置格式，消费方最低版本同步1.2.0。649f111已推送GitHub main；1.2.0已部署WAYFARING，SHA256为6fd538c66048240f4dfc99d41bea6b2a7e729eb2d2287ce12008a7f6b27eaa29。旧包备份及六包清单在相邻plaincraft-voxyseedgen-l10n/build/deployment-backups/preview-1.2.0-20261125-162224，未改玩家存档。

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
