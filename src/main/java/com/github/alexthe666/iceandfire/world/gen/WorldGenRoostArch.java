package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenRoostArch {
    private Block block;

    public WorldGenRoostArch(Block block) {
        this.block = block;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);
        int width = Math.min(3, height - 2);
        EnumFacing direction = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length - 1)];
        boolean diagonal = rand.nextBoolean();
        for (int i = 0; i < height; i++) {
            worldIn.setBlockState(position.up(i), block.getDefaultState());
        }
        BlockPos offsetPos = position;
        int placedWidths = 0;
        for (int i = 0; i < width; i++) {
            offsetPos = position.up(height).offset(direction, i);
            if (diagonal) {
                offsetPos = position.up(height).offset(direction, i).offset(direction.rotateY(), i);
            }
            if (placedWidths < width - 1 || rand.nextBoolean()) {
                worldIn.setBlockState(offsetPos, block.getDefaultState());
            }
            placedWidths++;
        }
        while (worldIn.isAirBlock(offsetPos.down()) && offsetPos.getY() > 0) {
            worldIn.setBlockState(offsetPos.down(), block.getDefaultState());
            offsetPos = offsetPos.down();
        }
        return true;
    }
}
