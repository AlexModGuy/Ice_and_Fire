package com.github.alexthe666.iceandfire.pathfinding;

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

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new NodeProcessorDragon();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
    }
}