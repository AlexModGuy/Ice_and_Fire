package com.github.alexthe666.iceandfire.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IceDragonPathNavigate extends PathNavigate {
    public IceDragonPathNavigate(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.isInLiquid() ? new SwimNodeProcessor() : nodeProcessor);
    }

    protected boolean canNavigate() {
        return this.theEntity.onGround || this.isInLiquid() || this.theEntity.isRiding();
    }

    protected Vec3d getEntityPosition() {
        return new Vec3d(this.theEntity.posX, this.theEntity.posY + (double)this.theEntity.height * 0.5D, this.theEntity.posZ);
    }

    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        float f = this.theEntity.width * this.theEntity.width;
        int i = 6;

        if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)f) {
            this.currentPath.incrementPathIndex();
        }

        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
            Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.theEntity, j);

            if (vec3d1.squareDistanceTo(vec3d) <= 36.0D && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(j);
                break;
            }
        }

        this.checkForStuck(vec3d);
    }

    protected void removeSunnyPath()
    {
        super.removeSunnyPath();
    }

    protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
        if(this.isInLiquid()) {
            RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.xCoord, posVec32.yCoord + (double) this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
            return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }else{
            int i = MathHelper.floor_double(posVec31.xCoord);
            int j = MathHelper.floor_double(posVec31.zCoord);
            double d0 = posVec32.xCoord - posVec31.xCoord;
            double d1 = posVec32.zCoord - posVec31.zCoord;
            double d2 = d0 * d0 + d1 * d1;
            if (d2 < 1.0E-8D) {
                return false;
            } else {
                double d3 = 1.0D / Math.sqrt(d2);
                d0 = d0 * d3;
                d1 = d1 * d3;
                sizeX = sizeX + 2;
                sizeZ = sizeZ + 2;

                if (!this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                    return false;
                } else {
                    sizeX = sizeX - 2;
                    sizeZ = sizeZ - 2;
                    double d4 = 1.0D / Math.abs(d0);
                    double d5 = 1.0D / Math.abs(d1);
                    double d6 = (double)i - posVec31.xCoord;
                    double d7 = (double)j - posVec31.zCoord;
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
                    int i1 = MathHelper.floor_double(posVec32.xCoord);
                    int j1 = MathHelper.floor_double(posVec32.zCoord);
                    int k1 = i1 - i;
                    int l1 = j1 - j;

                    while (k1 * k > 0 || l1 * l > 0) {
                        if (d6 < d7) {
                            d6 += d4;
                            i += k;
                            k1 = i1 - i;
                        }
                        else {
                            d7 += d5;
                            j += l;
                            l1 = j1 - j;
                        }

                        if (!this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d vec31, double p_179683_8_, double p_179683_10_) {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;
        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeZ; ++l) {
                    double d0 = (double)k + 0.5D - vec31.xCoord;
                    double d1 = (double)l + 0.5D - vec31.zCoord;

                    if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
                        PathNodeType pathnodetype = this.nodeProcessor.getPathNodeType(this.worldObj, k, y - 1, l, this.theEntity, sizeX, sizeY, sizeZ, true, true);
                        if (pathnodetype == PathNodeType.WATER) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.OPEN) {
                            return false;
                        }

                        pathnodetype = this.nodeProcessor.getPathNodeType(this.worldObj, k, y, l, this.theEntity, sizeX, sizeY, sizeZ, true, true);
                        float f = this.theEntity.getPathPriority(pathnodetype);

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

    public boolean canEntityStandOnPos(BlockPos pos) {
        return !this.worldObj.getBlockState(pos).isFullBlock();
    }

    public Path getPathToPos(BlockPos pos) {
        if (this.worldObj.getBlockState(pos).getMaterial() == Material.AIR) {
            BlockPos blockpos;
            for (blockpos = pos.down(); blockpos.getY() > 0 && this.worldObj.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down()) {
                ;
            }
            if (blockpos.getY() > 0) {
                return super.getPathToPos(blockpos.up());
            }
            while (blockpos.getY() < this.worldObj.getHeight() && this.worldObj.getBlockState(blockpos).getMaterial() == Material.AIR) {
                blockpos = blockpos.up();
            }
            pos = blockpos;
        }

        if (!this.worldObj.getBlockState(pos).getMaterial().isSolid()) {
            return super.getPathToPos(pos);
        }
        else {
            BlockPos blockpos1;
            for (blockpos1 = pos.up(); blockpos1.getY() < this.worldObj.getHeight() && this.worldObj.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.up()) {
                ;
            }
            return super.getPathToPos(blockpos1);
        }
    }

    private boolean isPositionClear(int x, int y, int z, int targetX, int targetY, int targetZ, Vec3d vec, double multi1, double multi2){
        for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + targetX - 1, y + targetY - 1, z + targetZ - 1))) {
            double d0 = (double)blockpos.getX() + 0.5D - vec.xCoord;
            double d1 = (double)blockpos.getZ() + 0.5D - vec.zCoord;
            if (d0 * multi1 + d1 * multi2 >= 0.0D) {
                Block block = this.worldObj.getBlockState(blockpos).getBlock();
                if (!block.isPassable(this.worldObj, blockpos)) {
                    return false;
                }
            }
        }

        return true;
    }
}