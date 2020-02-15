package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDreadSpike extends WorldGenerator {

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        worldIn.setBlockState(position, ModBlocks.dread_stone.getDefaultState(), 2);

        for (int i = 0; i < 35; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
            blockpos = worldIn.getHeight(blockpos);
            worldIn.setBlockState(blockpos, ModBlocks.dread_stone.getDefaultState(), 2);
        }
        return true;
    }
}