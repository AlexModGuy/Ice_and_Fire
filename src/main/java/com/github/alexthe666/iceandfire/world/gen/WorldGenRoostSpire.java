package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

public class WorldGenRoostSpire {

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int height = 5 + rand.nextInt(5);
        Direction bumpDirection = Direction.NORTH;
        for (int i = 0; i < height; i++) {
            worldIn.setBlock(position.above(i), IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState(), 2);
            if (rand.nextBoolean()) {
                bumpDirection = bumpDirection.getClockWise();
            }
            int offset = 1;
            if (i < 4) {
                worldIn.setBlock(position.above(i).north(), IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState(), 2);
                worldIn.setBlock(position.above(i).south(), IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState(), 2);
                worldIn.setBlock(position.above(i).east(), IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState(), 2);
                worldIn.setBlock(position.above(i).west(), IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState(), 2);
                offset = 2;
            }
            if (i < height - 2) {
                worldIn.setBlock(position.above(i).relative(bumpDirection, offset), IafBlockRegistry.CRACKLED_COBBLESTONE.get().defaultBlockState(), 2);
            }
        }
        return true;
    }
}