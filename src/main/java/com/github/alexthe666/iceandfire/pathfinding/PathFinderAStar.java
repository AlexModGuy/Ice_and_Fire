package com.github.alexthe666.iceandfire.pathfinding;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.Set;

public class PathFinderAStar  extends PathFinder {

    private final PathHeap path = new PathHeap();
    private final Set<PathPoint> closedSet = Sets.<PathPoint>newHashSet();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;
    private EntityLivingBase entity;

    public PathFinderAStar(NodeProcessor processor, EntityLivingBase entity) {
        super(processor);
        this.entity = entity;
        this.nodeProcessor = processor;
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, (double) ((float) targetPos.getX() + 0.5F), (double) ((float) targetPos.getY() + 0.5F), (double) ((float) targetPos.getZ() + 0.5F), maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, double x, double y, double z, float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, entitylivingIn);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(worldIn, pathpoint, pathpoint1, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }

    public boolean isDirectPathBetweenPoints(Vec3d start, Vec3d target) {
        RayTraceResult rayTrace = entity.world.rayTraceBlocks(start.add(0.5, 0.5, 0.5), target.add(0.5, 0.5, 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            if (sidePos.distanceSq(target.x, target.y, target.z) < 4 || pos.distanceSq(target.x, target.y, target.z) < 4) {
                return true;
            } else {
                return rayTrace.typeOfHit == RayTraceResult.Type.MISS;
            }
        }
        return true;
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        BlockPos startPos = new BlockPos(pathFrom.x, pathFrom.y, pathFrom.z);
        BlockPos endPos = new BlockPos(pathTo.x, pathTo.y, pathTo.z);
        if(isDirectPathBetweenPoints(new Vec3d(startPos), new Vec3d(endPos))){
            return new Path(new PathPoint[]{pathFrom, pathTo});
        }
        AStar aStar = new AStar(startPos, endPos, (int)(maxDistance * 100));
        BlockPos[] poses = aStar.getPath(worldIn);
        PathPoint[] points = new PathPoint[poses.length];
        for (int i = 0; i < poses.length; i++) {
           // this.entity.world.setBlockState(poses[i].down(), Blocks.GOLD_BLOCK.getDefaultState());
            points[i] = new PathPoint(poses[i].getX(), poses[i].getY(), poses[i].getZ());
        }
        //this.entity.world.setBlockState(endPos.down(), Blocks.DIAMOND_BLOCK.getDefaultState());
        return new Path(points);
    }
}