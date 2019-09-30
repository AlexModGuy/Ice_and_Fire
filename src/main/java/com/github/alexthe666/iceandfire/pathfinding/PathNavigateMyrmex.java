package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.ai.ExperimentalWalkNodeProcessor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathNavigateMyrmex extends PathNavigateGround {
    public BlockPos targetPosition;

    public PathNavigateMyrmex(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
    }
}