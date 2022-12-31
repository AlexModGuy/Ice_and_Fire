package com.github.alexthe666.iceandfire.event;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class WorldGenUtils {

    private static boolean canHeightSkipBlock(BlockPos pos, LevelAccessor world) {
        BlockState state = world.getBlockState(pos);
        return state.is(BlockTags.LOGS) || !state.getFluidState().isEmpty();
    }

    public static BlockPos degradeSurface(LevelAccessor world, BlockPos surface) {
        while ((!world.getBlockState(surface).canOcclude() || canHeightSkipBlock(surface, world)) && surface.getY() > 1) {
            surface = surface.below();
        }
        return surface;
    }
}
