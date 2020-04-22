package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockUtils {

    public static boolean isDreadBlock(IBlockState state){
        Block block = state.getBlock();
        return block instanceof IDreadBlock || block == IafBlockRegistry.dread_stone || block == IafBlockRegistry.dread_stone_bricks || block == IafBlockRegistry.dread_stone_bricks_chiseled
                || block == IafBlockRegistry.dread_stone_bricks_cracked || block == IafBlockRegistry.dread_stone_bricks_mossy || block == IafBlockRegistry.dread_stone_tile
                || block == IafBlockRegistry.dreadwood_planks || block == IafBlockRegistry.dread_stone_bricks_stairs;
    }

    public static boolean canSnowUpon(IBlockState state){
        return !isDreadBlock(state) && state.getBlock() != IafBlockRegistry.dragon_ice_spikes;
    }
}
