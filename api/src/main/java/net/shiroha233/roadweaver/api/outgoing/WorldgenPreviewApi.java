package net.shiroha233.roadweaver.api.outgoing;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/** 远景只消费已存在的计划，不能因查询而生成区块、读盘或启动规划。 */
public final class WorldgenPreviewApi {
    public enum State { UNAVAILABLE, EMPTY, READY }
    public enum Fidelity { GEOMETRY, BLOCKS }
    public record Run(int localX, int localZ, int fromY, int toY, String blockState) {
        public Run {
            if (localX < 0 || localX > 15 || localZ < 0 || localZ > 15 || fromY >= toY)
                throw new IllegalArgumentException("远景列必须在区块内且高度区间非空");
            Objects.requireNonNull(blockState);
        }
    }
    public record Chunk(State state, Fidelity fidelity, String revision, List<Run> runs) {
        public Chunk {
            Objects.requireNonNull(state);Objects.requireNonNull(fidelity);Objects.requireNonNull(revision);
            runs = List.copyOf(runs);
            if (state != State.READY && !runs.isEmpty()) throw new IllegalArgumentException("不可用或空计划不得带体素");
        }
        public static Chunk unavailable() { return new Chunk(State.UNAVAILABLE,Fidelity.BLOCKS,"",List.of()); }
    }
    @FunctionalInterface public interface Provider {
        /** world必须是当前世界句柄；不允许按种子或显示名跨存档借用。 */
        Chunk peek(Object world, int chunkX, int chunkZ);
    }
    @FunctionalInterface public interface Listener {
        /** 仅实际施工完成后发布，接收方应合并同区块通知并在自己的预算内刷新。 */
        void completed(Object world, int chunkX, int chunkZ);
    }
    private static final Map<String,Provider> PROVIDERS = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<Listener> LISTENERS = new CopyOnWriteArrayList<>();
    private static final System.Logger LOGGER=System.getLogger("plaincraft-worldgen-api");
    private WorldgenPreviewApi() {}
    public static void register(String id, Provider provider) {
        Objects.requireNonNull(id);Objects.requireNonNull(provider);
        var previous = PROVIDERS.putIfAbsent(id,provider);
        if (previous != null && previous != provider) throw new IllegalStateException("远景提供方重复："+id);
    }
    public static Chunk peek(String id,Object world,int chunkX,int chunkZ) {
        var provider=PROVIDERS.get(id);
        return provider == null ? Chunk.unavailable() : Objects.requireNonNull(provider.peek(world,chunkX,chunkZ));
    }
    public static void addListener(Listener listener) { LISTENERS.addIfAbsent(Objects.requireNonNull(listener)); }
    public static void removeListener(Listener listener) { LISTENERS.remove(listener); }
    public static void completed(Object world,int chunkX,int chunkZ) {
        // 可选远景消费者异常不能撤销实际已完成的施工；消费者应自行记录诊断。
        for (var listener:LISTENERS) try { listener.completed(world,chunkX,chunkZ); }
        catch (RuntimeException | LinkageError failure) {
            LOGGER.log(System.Logger.Level.WARNING,"远景完成通知失败，不撤销实际施工",failure);
        }
    }
}
