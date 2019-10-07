package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateDragon extends PathNavigateGround {
    public BlockPos targetPosition;
    private EntityDragonBase dragon;

    public PathNavigateDragon(EntityDragonBase entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
        this.dragon = entitylivingIn;
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new NodeProcessorDragon();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor);
    }

    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    public Path getPathToEntityLiving(Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn);

        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
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

        this.maxDistanceToWaypoint = this.dragon.getRenderSize() / 3;
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

    public void clearPath() {
        super.clearPath();
    }
}