package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import java.util.stream.Collectors;

public class WorldGenRoostPile {
    private final Block block;

    public WorldGenRoostPile(Block block) {
        this.block = block;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int radius = rand.nextInt(4);
        int layers = radius;
        for (int i = 0; i < layers; i++) {
            int j = radius - i;
            int l = radius - i;
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.above(i);
            for (BlockPos blockpos : BlockPos.betweenClosedStream(up.offset(-j, 0, -l), up.offset(j, 0, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= (double) (f * f)) {
                    worldIn.setBlock(blockpos, block.defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
