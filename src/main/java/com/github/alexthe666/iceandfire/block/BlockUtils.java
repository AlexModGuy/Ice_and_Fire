package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockUtils {

    public static boolean isDreadBlock(IBlockState state){
        Block block = state.getBlock();
        return block instanceof IDreadBlock || block == ModBlocks.dread_stone || block == ModBlocks.dread_stone_bricks || block == ModBlocks.dread_stone_bricks_chiseled
                || block == ModBlocks.dread_stone_bricks_cracked || block == ModBlocks.dread_stone_bricks_mossy || block == ModBlocks.dread_stone_tile
                || block == ModBlocks.dreadwood_planks || block == ModBlocks.dread_stone_bricks_stairs;
    }

    public static boolean canSnowUpon(IBlockState state){
        return !isDreadBlock(state) && state.getBlock() != ModBlocks.dragon_ice_spikes;
    }
}
