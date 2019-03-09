package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class SandNodeProcessor extends NodeProcessor {
    public PathPoint getStart() {
        return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getEntityBoundingBox().minZ));
    }

    /**
     * Returns PathPoint for given coordinates
     */
    public PathPoint getPathPointToCoords(double x, double y, double z) {
        return this.openPoint(MathHelper.floor(x - (double) (0.4)), MathHelper.floor(y + 0.5D), MathHelper.floor(z - (double) (0.4)));
    }

    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            PathPoint pathpoint = this.getWaterNode(currentPoint.x + enumfacing.getXOffset(), currentPoint.y + enumfacing.getYOffset(), currentPoint.z + enumfacing.getZOffset());

            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
        }

        return i;
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        return PathNodeType.OPEN;
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
        return PathNodeType.OPEN;
    }

    @Nullable
    private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype == PathNodeType.OPEN ? this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_) : null;
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        return PathNodeType.OPEN;
    }
}