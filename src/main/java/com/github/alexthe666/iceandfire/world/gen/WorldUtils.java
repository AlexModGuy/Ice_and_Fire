package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldUtils {

    public static Random rand = new Random();

    @Deprecated
    public static void setBlock(World world, int x, int y, int z, Block block, int meta, int flags) {
        BlockPos pos = new BlockPos(x, y, z);
        world.setBlockState(pos, block.getStateFromMeta(meta), flags);
    }

    @Deprecated
    public static Block getBlock(World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return world.getBlockState(pos).getBlock();
    }

    @Deprecated
    public static int getBlockMeta(World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
    }

    public void generateSphere(World world, int radius, BlockPos blockpos, Block block, int meta) {
        for (float i = 0; i < radius; i += 0.5) {
            for (float j = 0; j < 2 * Math.PI * i; j += 0.5)
                for (float k = 0; k < 2 * Math.PI * i; k += 0.5)
                    WorldUtils.setBlock(world, (int) Math.floor(blockpos.getX() + Math.sin(j) * i), (int) Math.floor(blockpos.getY() + Math.sin(k) * i), (int) Math.floor(blockpos.getZ() + Math.cos(j) * i), block, meta, 3);
        }
    }
}
