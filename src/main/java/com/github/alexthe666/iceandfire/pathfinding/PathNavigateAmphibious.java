package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class PathNavigateAmphibious extends PathNavigator {
    private boolean shouldAvoidSun;

    public PathNavigateAmphibious(CreatureEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
        this.nodeEvaluator.setCanFloat(true);
    }

    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new WalkNodeProcessor();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    protected boolean canUpdatePath() {
        return this.mob.isOnGround() || this.canFloat() && this.isInLiquid() || this.mob.isPassenger();
    }

    protected Vector3d getTempMobPos() {
        return new Vector3d(this.mob.getX(), this.getPathablePosY(), this.mob.getZ());
    }

    public Path createPath(BlockPos pos, int i) {
        if (this.level.getBlockState(pos).getMaterial() == Material.AIR) {
            BlockPos blockpos;

            for (blockpos = pos.below(); blockpos.getY() > 0 && this.level.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.below()) {
            }

            if (blockpos.getY() > 0) {
                return super.createPath(blockpos.above(), i);
            }

            while (blockpos.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(blockpos).getMaterial() == Material.AIR) {
                blockpos = blockpos.above();
            }

            pos = blockpos;
        }

        if (!this.level.getBlockState(pos).getMaterial().isSolid()) {
            return super.createPath(pos, i);
        } else {
            BlockPos blockpos1;

            for (blockpos1 = pos.above(); blockpos1.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.above()) {
            }

            return super.createPath(blockpos1, i);
        }
    }

    public Path createPath(Entity entityIn, int i) {
        return this.createPath(entityIn.blockPosition(), i);
    }

    private int getPathablePosY() {
        if (this.mob.isInWater() && this.canFloat()) {
            int i = (int) this.mob.getBoundingBox().minY;
            Block block = this.level.getBlockState(new BlockPos(MathHelper.floor(this.mob.getX()), i, MathHelper.floor(this.mob.getZ()))).getBlock();
            int j = 0;

            while (block == Blocks.WATER) {
                ++i;
                block = this.level.getBlockState(new BlockPos(MathHelper.floor(this.mob.getX()), i, MathHelper.floor(this.mob.getZ()))).getBlock();
                ++j;

                if (j > 16) {
                    return (int) this.mob.getBoundingBox().minY;
                }
            }

            return i;
        } else {
            return (int) (this.mob.getBoundingBox().minY + 0.5D);
        }
    }

    protected void removeSunnyPath() {
        if (this.shouldAvoidSun) {
            if (this.level.canSeeSky(new BlockPos(MathHelper.floor(this.mob.getX()), (int) (this.mob.getBoundingBox().minY + 0.5D), MathHelper.floor(this.mob.getZ())))) {
                return;
            }

            for (int i = 0; i < this.path.getNodeCount(); ++i) {
                PathPoint pathpoint = this.path.getNode(i);

                if (this.level.canSeeSky(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.path.truncateNodes(i - 1);
                    return;
                }
            }
        }
    }

    protected boolean canMoveDirectly(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
        int i = MathHelper.floor(posVec31.x);
        int j = MathHelper.floor(posVec31.z);
        double d0 = posVec32.x - posVec31.x;
        double d1 = posVec32.z - posVec31.z;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D) {
            return false;
        } else {
            double d3 = 1.0D / Math.sqrt(d2);
            d0 = d0 * d3;
            d1 = d1 * d3;
            sizeX = sizeX + 2;
            sizeZ = sizeZ + 2;

            if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                return false;
            } else {
                sizeX = sizeX - 2;
                sizeZ = sizeZ - 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) i - posVec31.x;
                double d7 = (double) j - posVec31.z;

                if (d0 >= 0.0D) {
                    ++d6;
                }

                if (d1 >= 0.0D) {
                    ++d7;
                }

                d6 = d6 / d0;
                d7 = d7 / d1;
                int k = d0 < 0.0D ? -1 : 1;
                int l = d1 < 0.0D ? -1 : 1;
                int i1 = MathHelper.floor(posVec32.x);
                int j1 = MathHelper.floor(posVec32.z);
                int k1 = i1 - i;
                int l1 = j1 - j;

                while (k1 * k > 0 || l1 * l > 0) {
                    if (d6 < d7) {
                        d6 += d4;
                        i += k;
                        k1 = i1 - i;
                    } else {
                        d7 += d5;
                        j += l;
                        l1 = j1 - j;
                    }

                    if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vector3d vec31, double p_179683_8_, double p_179683_10_) {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;

        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeZ; ++l) {
                    double d0 = (double) k + 0.5D - vec31.x;
                    double d1 = (double) l + 0.5D - vec31.z;

                    if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
                        PathNodeType pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y - 1, l, this.mob, sizeX, sizeY, sizeZ, true, true);
                        if (pathnodetype == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.OPEN) {
                            return false;
                        }

                        pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y, l, this.mob, sizeX, sizeY, sizeZ, true, true);
                        float f = this.mob.getPathfindingMalus(pathnodetype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /**
     * Returns true if an entity does not collide with any solid blocks at the position.
     */
    private boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vector3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos blockpos : BlockPos.betweenClosedStream(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)).collect(Collectors.toList())) {
            double d0 = (double) blockpos.getX() + 0.5D - p_179692_7_.x;
            double d1 = (double) blockpos.getZ() + 0.5D - p_179692_7_.z;

            if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
                Block block = this.level.getBlockState(blockpos).getBlock();

                if (this.level.getBlockState(blockpos).getMaterial().blocksMotion()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setBreakDoors(boolean canBreakDoors) {
        this.nodeEvaluator.setCanOpenDoors(canBreakDoors);
    }

    public boolean getEnterDoors() {
        return this.nodeEvaluator.canPassDoors();
    }

    public void setEnterDoors(boolean enterDoors) {
        this.nodeEvaluator.setCanPassDoors(enterDoors);
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    public void setCanFloat(boolean canSwim) {
        this.nodeEvaluator.setCanFloat(canSwim);
    }

    public void setAvoidSun(boolean avoidSun) {
        this.shouldAvoidSun = avoidSun;
    }
}