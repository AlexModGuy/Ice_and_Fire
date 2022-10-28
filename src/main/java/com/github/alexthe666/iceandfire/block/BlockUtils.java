package com.github.alexthe666.iceandfire.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtils {

    public static boolean isDreadBlock(BlockState state) {
        Block block = state.getBlock();
        return block instanceof IDreadBlock || block == IafBlockRegistry.DREAD_STONE.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_CHISELED.get()
                || block == IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get() || block == IafBlockRegistry.DREAD_STONE_TILE.get()
                || block == IafBlockRegistry.DREADWOOD_PLANKS.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_STAIRS.get();
    }

    public static boolean canSnowUpon(BlockState state) {
        return !isDreadBlock(state) && state.getBlock() != IafBlockRegistry.DRAGON_ICE_SPIKES.get();
    }
}
