package net.mirrorloong.chineseweapons.client;

import net.minecraft.core.BlockPos;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArmorCastingTableBlockEntityCache {
    public static final Map<BlockPos, Integer> PROGRESS = new ConcurrentHashMap<>();
    public static final Map<BlockPos, Boolean> HAS_RECIPE = new ConcurrentHashMap<>();

    public static void put(BlockPos pos, int progress) {
        PROGRESS.put(pos.immutable(), progress);
    }

    public static int get(BlockPos pos) {
        return PROGRESS.getOrDefault(pos, 0);
    }

    public static void putHasRecipe(BlockPos pos, boolean b) {
        HAS_RECIPE.put(pos.immutable(), b);
    }

    public static boolean hasRecipe(BlockPos pos) {
        return HAS_RECIPE.getOrDefault(pos, false);
    }
}