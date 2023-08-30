package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.stream.Collectors;

public class WorldGenRoostBoulder {

    private final Block block;
    private final int startRadius;
    private final boolean replaceAir;

    public WorldGenRoostBoulder(Block blockIn, int startRadiusIn, boolean replaceAir) {
        this.block = blockIn;
        this.startRadius = startRadiusIn;
        this.replaceAir = replaceAir;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        while (true) {
            label50:
            {
                if (position.getY() > 3) {
                    if (worldIn.isEmptyBlock(position.below())) {
                        break label50;
                    }

                    Block block = worldIn.getBlockState(position.below()).getBlock();

                    if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE) {
                        break label50;
                    }
                }

                if (position.getY() <= 3) {
                    return false;
                }

                int i1 = this.startRadius;

                for (int i = 0; i1 >= 0 && i < 3; ++i) {
                    int j = i1 + rand.nextInt(2);
                    int k = i1 + rand.nextInt(2);
                    int l = i1 + rand.nextInt(2);
                    float f = (float) (j + k + l) * 0.333F + 0.5F;

                    for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                        if (blockpos.distSqr(position) <= (double) (f * f) && (replaceAir || worldIn.getBlockState(blockpos).canOcclude())) {
                            worldIn.setBlock(blockpos, this.block.defaultBlockState(), 2);
                        }
                    }

                    position = position.offset(-(i1 + 1) + rand.nextInt(2 + i1 * 2), -rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
                }

                return true;
            }
            position = position.below();
        }
    }
}