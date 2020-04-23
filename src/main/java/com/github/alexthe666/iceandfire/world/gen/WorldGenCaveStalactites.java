package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenCaveStalactites {
    private Block block;

    public WorldGenCaveStalactites(Block block) {
        this.block = block;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);
        for (int i = 0; i < height; i++) {
            if (i < height / 2) {
                worldIn.setBlockState(position.down(i).north(), block.getDefaultState());
                worldIn.setBlockState(position.down(i).east(), block.getDefaultState());
                worldIn.setBlockState(position.down(i).south(), block.getDefaultState());
                worldIn.setBlockState(position.down(i).west(), block.getDefaultState());
            }
            worldIn.setBlockState(position.down(i), block.getDefaultState());
        }
        return true;
    }
}
