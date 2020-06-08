package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPhasesThroughBlock {

    boolean canPhaseThroughBlock(World world, BlockPos pos);
}
