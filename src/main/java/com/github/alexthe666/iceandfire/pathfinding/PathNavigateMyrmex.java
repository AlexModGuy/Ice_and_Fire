package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.EntityLiving;
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
        this.nodeProcessor = new NodeProcessorDragon();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor);
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
    }

    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();

        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if ((double) this.currentPath.getPathPointFromIndex(j).y != Math.floor(vec3d.y)) {
                i = j;
                break;
            }
        }

        this.maxDistanceToWaypoint = this.entity.width * 2;
        Vec3d vec3d1 = this.currentPath.getCurrentPos();

        if (MathHelper.abs((float) (this.entity.posX - (vec3d1.x + 0.5D))) < this.maxDistanceToWaypoint && MathHelper.abs((float) (this.entity.posZ - (vec3d1.z + 0.5D))) < this.maxDistanceToWaypoint && Math.abs(this.entity.posY - vec3d1.y) < 1.0D) {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }

        int k = MathHelper.ceil(this.entity.width);
        int l = MathHelper.ceil(this.entity.height);
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1) {
            if (this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.entity, j1), k, l, i1)) {
                this.currentPath.setCurrentPathIndex(j1);
                break;
            }
        }

        this.checkForStuck(vec3d);
    }

}