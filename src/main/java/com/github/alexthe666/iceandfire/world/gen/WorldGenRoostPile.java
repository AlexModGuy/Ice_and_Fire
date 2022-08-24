package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;
import java.util.stream.Collectors;

public class WorldGenRoostPile {
    private final Block block;

    public WorldGenRoostPile(Block block) {
        this.block = block;
    }

    public boolean generate(IWorld worldIn, Random rand, BlockPos position) {
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
