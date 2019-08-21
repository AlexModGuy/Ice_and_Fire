package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.pathfinding.PathFinderAStar;
import com.github.alexthe666.iceandfire.pathfinding.PathFinderMyrmex;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateMyrmex extends PathNavigateGround {
    public BlockPos targetPosition;

    public PathNavigateMyrmex(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new ExperimentalWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinderMyrmex(this.nodeProcessor, this.entity);
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
    }
}