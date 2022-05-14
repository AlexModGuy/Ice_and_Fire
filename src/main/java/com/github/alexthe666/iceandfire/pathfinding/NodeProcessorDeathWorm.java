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
        return this.openPoint(MathHelper.floor(this.entity.getBoundingBox().minX), MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getBoundingBox().minZ));
    }

    @Override
    public FlaggedPathPoint func_224768_a(double x, double y, double z) {
        return new FlaggedPathPoint(this.openPoint(MathHelper.floor(x - 0.4), MathHelper.floor(y + 0.5D), MathHelper.floor(z - 0.4)));
    }

    @Override
    public PathNodeType determineNodeType(IBlockReader blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        return this.getFloorNodeType(blockaccessIn, x, y, z);
    }

    public PathNodeType getFloorNodeType(IBlockReader worldIn, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (!isPassable(worldIn, blockpos.down()) && (blockstate.isAir() || isPassable(worldIn, blockpos))) {
            return PathNodeType.BREACH;
        } else {
            return isPassable(worldIn, blockpos) ? PathNodeType.WATER : PathNodeType.BLOCKED;
        }
    }

    public int func_222859_a(PathPoint[] p_222859_1_, PathPoint p_222859_2_) {
        int i = 0;

        for(Direction direction : Direction.values()) {
            PathPoint pathpoint = this.getSandNode(p_222859_2_.x + direction.getXOffset(), p_222859_2_.y + direction.getYOffset(), p_222859_2_.z + direction.getZOffset());
            if (pathpoint != null && !pathpoint.visited) {
                p_222859_1_[i++] = pathpoint;
            }
        }

        return i;
    }

    @Nullable
    private PathPoint getSandNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != PathNodeType.BREACH && pathnodetype != PathNodeType.WATER ? null : this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        for (int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i) {
            for (int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j) {
                for (int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k) {
                    BlockState blockstate = this.blockaccess.getBlockState(blockpos$mutable.setPos(i, j, k));
                    if (!isPassable(this.blockaccess, blockpos$mutable.down()) && (blockstate.isAir() || isPassable(this.blockaccess, blockpos$mutable))) {
                        return PathNodeType.BREACH;
                    }

                }
            }
        }

        BlockState blockstate1 = this.blockaccess.getBlockState(blockpos$mutable);
        return isPassable(blockstate1) ? PathNodeType.WATER : PathNodeType.BLOCKED;
    }


    private boolean isPassable(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.SAND || world.getBlockState(pos).getMaterial() == Material.AIR;
    }

    private boolean isPassable(BlockState state) {
        return state.getMaterial() == Material.SAND || state.getMaterial() == Material.AIR;
    }
}