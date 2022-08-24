package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlaggedPathPoint;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class NodeProcessorDeathWorm extends NodeProcessor {

    public PathPoint getStart() {
        return this.getNode(MathHelper.floor(this.mob.getBoundingBox().minX), MathHelper.floor(this.mob.getBoundingBox().minY + 0.5D), MathHelper.floor(this.mob.getBoundingBox().minZ));
    }

    @Override
    public FlaggedPathPoint getGoal(double x, double y, double z) {
        return new FlaggedPathPoint(this.getNode(MathHelper.floor(x - 0.4), MathHelper.floor(y + 0.5D), MathHelper.floor(z - 0.4)));
    }

    @Override
    public PathNodeType getBlockPathType(IBlockReader blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        return this.getBlockPathType(blockaccessIn, x, y, z);
    }

    public PathNodeType getBlockPathType(IBlockReader worldIn, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (!isPassable(worldIn, blockpos.below()) && (blockstate.isAir() || isPassable(worldIn, blockpos))) {
            return PathNodeType.BREACH;
        } else {
            return isPassable(worldIn, blockpos) ? PathNodeType.WATER : PathNodeType.BLOCKED;
        }
    }

    public int getNeighbors(PathPoint[] p_222859_1_, PathPoint p_222859_2_) {
        int i = 0;

        for (Direction direction : Direction.values()) {
            PathPoint pathpoint = this.getSandNode(p_222859_2_.x + direction.getStepX(), p_222859_2_.y + direction.getStepY(), p_222859_2_.z + direction.getStepZ());
            if (pathpoint != null && !pathpoint.closed) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    @Nullable
    private PathPoint getSandNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != PathNodeType.BREACH && pathnodetype != PathNodeType.WATER ? null : this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for (int i = p_186327_1_; i < p_186327_1_ + this.entityWidth; ++i) {
            for (int j = p_186327_2_; j < p_186327_2_ + this.entityHeight; ++j) {
                for (int k = p_186327_3_; k < p_186327_3_ + this.entityDepth; ++k) {
                    BlockState blockstate = this.level.getBlockState(blockpos$mutable.set(i, j, k));
                    if (!isPassable(this.level, blockpos$mutable.below()) && (blockstate.isAir() || isPassable(this.level, blockpos$mutable))) {
                        return PathNodeType.BREACH;
                    }

                }
            }
        }

        BlockState blockstate1 = this.level.getBlockState(blockpos$mutable);
        return isPassable(blockstate1) ? PathNodeType.WATER : PathNodeType.BLOCKED;
    }


    private boolean isPassable(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.SAND || world.getBlockState(pos).getMaterial() == Material.AIR;
    }

    private boolean isPassable(BlockState state) {
        return state.getMaterial() == Material.SAND || state.getMaterial() == Material.AIR;
    }
}