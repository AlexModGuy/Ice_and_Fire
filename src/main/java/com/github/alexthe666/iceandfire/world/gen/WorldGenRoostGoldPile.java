package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.BlockSilverPile;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenRoostGoldPile {
    private Block block;

    public WorldGenRoostGoldPile(Block block) {
        this.block = block;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int radius = rand.nextInt(3);
        int layers = radius;
        for (int i = 0; i < layers; i++) {
            int j = radius - i;
            int l = radius - i;
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.up(i);
            for (BlockPos blockpos : BlockPos.getAllInBox(up.add(-j, 0, -l), up.add(j, 0, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    blockpos = worldIn.getHeight(blockpos);
                    if (block instanceof BlockGoldPile) {
                        if (worldIn.isAirBlock(blockpos)) {
                            worldIn.setBlockState(blockpos, block.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)));
                            if (worldIn.getBlockState(blockpos.down()).getBlock() instanceof BlockGoldPile) {
                                worldIn.setBlockState(blockpos.down(), block.getDefaultState().withProperty(BlockGoldPile.LAYERS, 8));
                            }
                        }
                    }
                    if (block instanceof BlockSilverPile) {
                        if (worldIn.isAirBlock(blockpos)) {
                            worldIn.setBlockState(blockpos, block.getDefaultState().withProperty(BlockSilverPile.LAYERS, 1 + rand.nextInt(7)));
                            if (worldIn.getBlockState(blockpos.down()).getBlock() instanceof BlockGoldPile) {
                                worldIn.setBlockState(blockpos.down(), block.getDefaultState().withProperty(BlockSilverPile.LAYERS, 8));
                            }
                        }
                    }

                }
            }
        }
        return true;
    }
}
