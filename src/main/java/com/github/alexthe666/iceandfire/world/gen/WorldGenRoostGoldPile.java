package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.stream.Collectors;

public class WorldGenRoostGoldPile {
    private final Block block;

    public WorldGenRoostGoldPile(Block block) {
        this.block = block;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int radius = rand.nextInt(3);
        int layers = radius;
        for (int i = 0; i < layers; i++) {
            int j = radius - i;
            int l = radius - i;
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.above(i);
            for (BlockPos blockpos : BlockPos.betweenClosedStream(up.offset(-j, 0, -l), up.offset(j, 0, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
                if (blockpos.distSqr(position) <= (double) (f * f)) {
                    blockpos = worldIn.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, blockpos);
                    if (block instanceof BlockGoldPile) {
                        if (worldIn.isEmptyBlock(blockpos)) {
                            worldIn.setBlock(blockpos, block.defaultBlockState().setValue(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)), 2);
                            if (worldIn.getBlockState(blockpos.below()).getBlock() instanceof BlockGoldPile) {
                                worldIn.setBlock(blockpos.below(), block.defaultBlockState().setValue(BlockGoldPile.LAYERS, 8), 2);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
