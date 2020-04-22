package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockUtils {

    public static boolean isDreadBlock(IBlockState state){
        Block block = state.getBlock();
        return block instanceof IDreadBlock || block == IaFBlockRegistry.dread_stone || block == IaFBlockRegistry.dread_stone_bricks || block == IaFBlockRegistry.dread_stone_bricks_chiseled
                || block == IaFBlockRegistry.dread_stone_bricks_cracked || block == IaFBlockRegistry.dread_stone_bricks_mossy || block == IaFBlockRegistry.dread_stone_tile
                || block == IaFBlockRegistry.dreadwood_planks || block == IaFBlockRegistry.dread_stone_bricks_stairs;
    }

    public static boolean canSnowUpon(IBlockState state){
        return !isDreadBlock(state) && state.getBlock() != IaFBlockRegistry.dragon_ice_spikes;
    }
}
