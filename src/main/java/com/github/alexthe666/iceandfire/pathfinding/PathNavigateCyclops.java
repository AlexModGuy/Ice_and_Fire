package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Collectors;

public class PathNavigateCyclops extends GroundPathNavigation {
    public BlockPos targetPosition;
    private final EntityCyclops cyclops;
    private int ticksAtLastPos;
    private Vec3 lastPosCheck = Vec3.ZERO;
    private Vec3 timeoutCachedNode = Vec3.ZERO;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;

    public PathNavigateCyclops(EntityCyclops LivingEntityIn, Level worldIn) {
        super(LivingEntityIn, worldIn);
        this.cyclops = LivingEntityIn;
    }

    protected PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, i);
    }

    public Path createPath(BlockPos pos, int i) {
        this.targetPosition = pos;
        return super.createPath(pos, i);
    }

    public Path createPath(Entity entityIn, int i) {
        this.targetPosition = entityIn.blockPosition();
        return super.createPath(entityIn, i);
    }

    public boolean moveTo(Entity entityIn, double speedIn) {
        Path path = this.createPath(entityIn, 0);
        if (path != null) {
            return this.moveTo(path, speedIn);
        } else {
            this.targetPosition = entityIn.blockPosition();
            this.speedModifier = speedIn;
            return true;
        }
    }

    protected void followThePath() {
        Vec3 vector3d = this.getTempMobPos();
        int i = this.path.getNodeCount();
        for (int j = this.path.getNextNodeIndex(); j < this.path.getNodeCount(); ++j) {
            if ((double) this.path.getNode(j).y != Math.floor(vector3d.y)) {
                i = j;
                break;
            }
        }

        this.maxDistanceToWaypoint = this.mob.getBbWidth();
        Vec3 Vector3d1 = Vec3.atCenterOf(this.path.getNextNodePos());
        float distX = Mth.abs((float) (this.mob.getX() - (Vector3d1.x + 0.5D)));
        float distZ = Mth.abs((float) (this.mob.getZ() - (Vector3d1.z + 0.5D)));
        float distY = (float) Math.abs(this.mob.getY() - Vector3d1.y);

        if (distX < this.maxDistanceToWaypoint && distZ < this.maxDistanceToWaypoint && distY < this.mob.getBbHeight()) {
            this.path.setNextNodeIndex(this.path.getNextNodeIndex() + 1);
        }

        int k = Mth.ceil(this.mob.getBbWidth());
        int l = Mth.ceil(this.mob.getBbHeight());
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.path.getNextNodeIndex(); --j1) {
            if (this.canMoveDirectly(vector3d, this.path.getEntityPosAtNode(this.mob, j1))) {
                this.path.setNextNodeIndex(j1);
                break;
            }
        }

        this.doStuckDetection(vector3d);
    }

    protected void doStuckDetection(Vec3 positionVec3) {
        if (this.tick - this.ticksAtLastPos > 100) {
            if (positionVec3.distanceToSqr(this.lastPosCheck) < 2.25D) {
                this.stop();
            }

            this.ticksAtLastPos = this.tick;
            this.lastPosCheck = positionVec3;
        }

        if (this.path != null && !this.path.isDone()) {
            Vec3 vector3d = Vec3.atCenterOf(this.path.getNextNodePos());

            if (vector3d.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vector3d;
                double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
                this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / (double) this.mob.getSpeed() * 1000.0D : 0.0D;
            }

            if (this.timeoutLimit > 0.0D && (double) this.timeoutTimer > this.timeoutLimit * 3.0D) {
                this.timeoutCachedNode = Vec3.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0D;
                this.stop();
            }

            this.lastTimeoutCheck = System.currentTimeMillis();
        }
    }

    public void stop() {
        super.stop();
    }

    @Override
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
        double sizeX = this.mob.getBbHeight();
        double sizeY = this.mob.getBbWidth();
        double sizeZ = posVec32.z;
        int i = Mth.floor(posVec31.x);
        int j = Mth.floor(posVec31.z);
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
                int i1 = Mth.floor(posVec32.x);
                int j1 = Mth.floor(posVec32.z);
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

    private boolean isSafeToStandAt(int x, int y, int z, double sizeX, double sizeY, double sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_) {
        int i = Mth.floor(x - sizeX / 2);
        int j = Mth.floor(z - sizeZ / 2);

        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeZ; ++l) {
                    double d0 = (double) k + 0.5D - vec31.x;
                    double d1 = (double) l + 0.5D - vec31.z;

                    if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
                        BlockPathTypes pathnodetype;
                        try {
                            pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y - 1, l, this.mob, Mth.floor(sizeX), Mth.floor(sizeY), Mth.floor(sizeZ), true, true);
                        } catch (Exception e) {
                            pathnodetype = BlockPathTypes.BLOCKED;
                        }
                        if (pathnodetype == BlockPathTypes.WATER) {
                            return false;
                        }

                        if (pathnodetype == BlockPathTypes.LAVA) {
                            return false;
                        }

                        if (pathnodetype == BlockPathTypes.OPEN) {
                            return false;
                        }
                        try {
                            pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y, l, this.mob, Mth.floor(sizeX), Mth.floor(sizeY), Mth.floor(sizeZ), true, true);
                        } catch (Exception e) {
                            pathnodetype = BlockPathTypes.BLOCKED;
                        }
                        float f = this.mob.getPathfindingMalus(pathnodetype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathnodetype == BlockPathTypes.DAMAGE_FIRE || pathnodetype == BlockPathTypes.DANGER_FIRE || pathnodetype == BlockPathTypes.DAMAGE_OTHER) {
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
    private boolean isPositionClear(int x, int y, int z, double sizeX, double sizeY, double sizeZ, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos blockpos : BlockPos.betweenClosedStream(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)).collect(Collectors.toList())) {
            double d0 = (double) blockpos.getX() + 0.5D - p_179692_7_.x;
            double d1 = (double) blockpos.getZ() + 0.5D - p_179692_7_.z;

            if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
                if (this.level.getBlockState(blockpos).getMaterial().blocksMotion()) {
                    return false;
                }
            }
        }

        return true;
    }
}