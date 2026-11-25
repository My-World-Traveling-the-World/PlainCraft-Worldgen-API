package net.shiroha233.roadweaver.api.outgoing;

import java.util.List;
import java.util.Objects;

/** 地图只观察已发布的道路中心线；保留施工高度，不把二维地图坐标冒充真实世界坐标。 */
public final class RoadOverlayApi {
    public enum State { UNAVAILABLE, EMPTY, READY }
    public enum Kind { ROAD, BRIDGE, TUNNEL, CROSSING }
    public record Point(int x,int y,int z) {}
    public record Line(Kind kind,List<Point> points) {
        public Line {Objects.requireNonNull(kind);points=List.copyOf(points);}
    }
    public record Snapshot(State state,long revision,boolean truncated,List<Line> lines) {
        public Snapshot {Objects.requireNonNull(state);lines=List.copyOf(lines);}
        public static Snapshot unavailable(){return new Snapshot(State.UNAVAILABLE,0,false,List.of());}
    }
    @FunctionalInterface public interface Provider {
        /** 不加载存档或区块；预算是最多输出顶点数，截断必须明确标记。 */
        Snapshot peek(Object world,int minX,int minZ,int maxX,int maxZ,int maxVertices);
    }
    private static volatile Provider provider;
    private static volatile java.util.function.Predicate<Object> permission=player->false;
    private RoadOverlayApi() {}
    public static synchronized void register(Provider next,java.util.function.Predicate<Object> mayView) {
        Objects.requireNonNull(next);
        if(provider!=null&&provider!=next)throw new IllegalStateException("道路地图提供方重复注册");
        permission=Objects.requireNonNull(mayView);provider=next;
    }
    /** 地图消费者复用道路服务端权限，不能因换地图而绕过服主限制。 */
    public static boolean isAllowed(Object serverPlayer){return provider!=null&&permission.test(serverPlayer);}
    public static Snapshot peek(Object world,int minX,int minZ,int maxX,int maxZ,int maxVertices) {
        if(maxVertices<2)throw new IllegalArgumentException("地图预算必须容纳一条线段");
        var current=provider;
        return current==null?Snapshot.unavailable():current.peek(world,minX,minZ,maxX,maxZ,maxVertices);
    }
}
