package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class WorldGenRoostArch {
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final Block block;

    public WorldGenRoostArch(Block block) {
        this.block = block;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);
        int width = Math.min(3, height - 2);
        Direction direction = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
        boolean diagonal = rand.nextBoolean();
        for (int i = 0; i < height; i++) {
            worldIn.setBlock(position.above(i), block.defaultBlockState(), 2);
        }
        BlockPos offsetPos = position;
        int placedWidths = 0;
        for (int i = 0; i < width; i++) {
            offsetPos = position.above(height).relative(direction, i);
            if (diagonal) {
                offsetPos = position.above(height).relative(direction, i).relative(direction.getClockWise(), i);
            }
            if (placedWidths < width - 1 || rand.nextBoolean()) {
                worldIn.setBlock(offsetPos, block.defaultBlockState(), 2);
            }
            placedWidths++;
        }
        while (worldIn.isEmptyBlock(offsetPos.below()) && offsetPos.getY() > 0) {
            worldIn.setBlock(offsetPos.below(), block.defaultBlockState(), 2);
            offsetPos = offsetPos.below();
        }
        return true;
    }
}
