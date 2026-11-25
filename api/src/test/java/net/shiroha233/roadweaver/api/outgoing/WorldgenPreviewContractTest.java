package net.shiroha233.roadweaver.api.outgoing;

import java.util.*;

/** 无游戏实例执行公开契约，证明世界隔离、不可变快照及订阅撤销。 */
public final class WorldgenPreviewContractTest {
    public static void main(String[] args) {
        Object a=new Object(),b=new Object();var writes=new ArrayList<WorldgenPreviewApi.Run>();
        writes.add(new WorldgenPreviewApi.Run(15,0,-60,-59,"minecraft:air"));
        var snapshot=new WorldgenPreviewApi.Chunk(WorldgenPreviewApi.State.READY,
                WorldgenPreviewApi.Fidelity.BLOCKS,"v1",writes);writes.clear();
        require(snapshot.runs().size()==1,"调用方清表不能改已发布结果");
        WorldgenPreviewApi.register("contract",(world,x,z)->world==a?snapshot:WorldgenPreviewApi.Chunk.unavailable());
        require(WorldgenPreviewApi.peek("contract",a,-1,0)==snapshot,"当前世界读不到计划");
        require(WorldgenPreviewApi.peek("contract",b,-1,0).state()==WorldgenPreviewApi.State.UNAVAILABLE,"跨世界串计划");
        require(WorldgenPreviewApi.peek("missing",a,0,0).state()==WorldgenPreviewApi.State.UNAVAILABLE,"未安装不能冒充空计划");
        int[] count={0};WorldgenPreviewApi.Listener listener=(world,x,z)->{require(world==a,"事件身份错误");count[0]++;};
        WorldgenPreviewApi.addListener(listener);WorldgenPreviewApi.addListener(listener);
        WorldgenPreviewApi.completed(a,1,2);require(count[0]==1,"同订阅重复通知");
        WorldgenPreviewApi.removeListener(listener);WorldgenPreviewApi.completed(a,1,2);require(count[0]==1,"撤销仍通知");
        System.out.println("PREVIEW-CONTRACT PASS tests=6");
    }
    private static void require(boolean condition,String message) {if(!condition)throw new AssertionError(message);}
}
