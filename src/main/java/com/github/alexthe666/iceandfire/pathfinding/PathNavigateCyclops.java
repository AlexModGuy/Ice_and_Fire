package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class PathNavigateCyclops extends GroundPathNavigator {
    public BlockPos targetPosition;
    private final EntityCyclops cyclops;
    private int ticksAtLastPos;
    private Vector3d lastPosCheck = Vector3d.ZERO;
    private Vector3d timeoutCachedNode = Vector3d.ZERO;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;

    public PathNavigateCyclops(EntityCyclops LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
        this.cyclops = LivingEntityIn;
    }

    protected PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new WalkNodeProcessor();
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
        Vector3d vector3d = this.getTempMobPos();
        int i = this.path.getNodeCount();
        for (int j = this.path.getNextNodeIndex(); j < this.path.getNodeCount(); ++j) {
            if ((double) this.path.getNode(j).y != Math.floor(vector3d.y)) {
                i = j;
                break;
            }
        }

        this.maxDistanceToWaypoint = this.mob.getBbWidth();
        Vector3d Vector3d1 = Vector3d.atCenterOf(this.path.getNextNodePos());
        float distX = MathHelper.abs((float) (this.mob.getX() - (Vector3d1.x + 0.5D)));
        float distZ = MathHelper.abs((float) (this.mob.getZ() - (Vector3d1.z + 0.5D)));
        float distY = (float) Math.abs(this.mob.getY() - Vector3d1.y);

        if (distX < this.maxDistanceToWaypoint && distZ < this.maxDistanceToWaypoint && distY < this.mob.getBbHeight()) {
            this.path.setNextNodeIndex(this.path.getNextNodeIndex() + 1);
        }

        int k = MathHelper.ceil(this.mob.getBbWidth());
        int l = MathHelper.ceil(this.mob.getBbHeight());
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.path.getNextNodeIndex(); --j1) {
            if (this.canMoveDirectly(vector3d, this.path.getEntityPosAtNode(this.mob, j1), k, l, i1)) {
                this.path.setNextNodeIndex(j1);
                break;
            }
        }

        this.doStuckDetection(vector3d);
    }

    protected void doStuckDetection(Vector3d positionVec3) {
        if (this.tick - this.ticksAtLastPos > 100) {
            if (positionVec3.distanceToSqr(this.lastPosCheck) < 2.25D) {
                this.stop();
            }

            this.ticksAtLastPos = this.tick;
            this.lastPosCheck = positionVec3;
        }

        if (this.path != null && !this.path.isDone()) {
            Vector3d vector3d = Vector3d.atCenterOf(this.path.getNextNodePos());

            if (vector3d.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vector3d;
                double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
                this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / (double) this.mob.getSpeed() * 1000.0D : 0.0D;
            }

            if (this.timeoutLimit > 0.0D && (double) this.timeoutTimer > this.timeoutLimit * 3.0D) {
                this.timeoutCachedNode = Vector3d.ZERO;
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
                        PathNodeType pathnodetype;
                        try {
                            pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y - 1, l, this.mob, sizeX, sizeY, sizeZ, true, true);
                        } catch (Exception e) {
                            pathnodetype = PathNodeType.BLOCKED;
                        }
                        if (pathnodetype == PathNodeType.WATER) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.OPEN) {
                            return false;
                        }
                        try {
                            pathnodetype = this.nodeEvaluator.getBlockPathType(this.level, k, y, l, this.mob, sizeX, sizeY, sizeZ, true, true);
                        } catch (Exception e) {
                            pathnodetype = PathNodeType.BLOCKED;
                        }
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
                if (this.level.getBlockState(blockpos).getMaterial().blocksMotion()) {
                    return false;
                }
            }
        }

        return true;
    }
}