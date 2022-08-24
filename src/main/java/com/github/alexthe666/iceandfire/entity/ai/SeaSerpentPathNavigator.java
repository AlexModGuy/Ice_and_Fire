package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class SeaSerpentPathNavigator  extends PathNavigator {

    public SeaSerpentPathNavigator(MobEntity entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new SwimNodeProcessor(true);
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected Vector3d getTempMobPos() {
        return new Vector3d(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
    }

    @Override
    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            Vector3d lvt_1_2_;
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                lvt_1_2_ = this.path.getNextEntityPos(this.mob);
                if (MathHelper.floor(this.mob.getX()) == MathHelper.floor(lvt_1_2_.x) && MathHelper.floor(this.mob.getY()) == MathHelper.floor(lvt_1_2_.y) && MathHelper.floor(this.mob.getZ()) == MathHelper.floor(lvt_1_2_.z)) {
                    this.path.advance();
                }
            }

            DebugPacketSender.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                lvt_1_2_ = this.path.getNextEntityPos(this.mob);
                this.mob.getMoveControl().setWantedPosition(lvt_1_2_.x, lvt_1_2_.y, lvt_1_2_.z, this.speedModifier);
            }
        }
    }

    @Override
    protected void followThePath() {
        if (this.path != null) {
            Vector3d entityPos = this.getTempMobPos();
            final float entityWidth = this.mob.getBbWidth();
            float lvt_3_1_ = entityWidth > 0.75F ? entityWidth / 2.0F : 0.75F - entityWidth / 2.0F;
            Vector3d lvt_4_1_ = this.mob.getDeltaMovement();
            if (Math.abs(lvt_4_1_.x) > 0.2D || Math.abs(lvt_4_1_.z) > 0.2D) {
                lvt_3_1_ = (float) (lvt_3_1_ * lvt_4_1_.length() * 6.0D);
            }
            Vector3d lvt_6_1_ = Vector3d.atCenterOf(this.path.getNextNodePos());
            if (Math.abs(this.mob.getX() - lvt_6_1_.x) < lvt_3_1_
                && Math.abs(this.mob.getZ() - lvt_6_1_.z) < lvt_3_1_
                && Math.abs(this.mob.getY() - lvt_6_1_.y) < lvt_3_1_ * 2.0F) {
                this.path.advance();
            }

            for (int lvt_7_1_ = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); lvt_7_1_ > this.path.getNextNodeIndex(); --lvt_7_1_) {
                lvt_6_1_ = this.path.getEntityPosAtNode(this.mob, lvt_7_1_);
                if (lvt_6_1_.distanceToSqr(entityPos) <= 36.0D
                    && this.canMoveDirectly(entityPos, lvt_6_1_, 0, 0, 0)) {
                    this.path.setNextNodeIndex(lvt_7_1_);
                    break;
                }
            }

            this.doStuckDetection(entityPos);
        }
    }

    @Override
    protected void doStuckDetection(Vector3d positionVec3) {
        if (this.tick - this.lastStuckCheck > 100) {
            if (positionVec3.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
                this.stop();
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = positionVec3;
        }

        if (this.path != null && !this.path.isDone()) {
            Vector3i lvt_2_1_ = this.path.getNextNodePos();
            if (lvt_2_1_.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = lvt_2_1_;
                final double lvt_3_1_ = positionVec3.distanceTo(Vector3d.atCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.mob.getSpeed() > 0.0F
                    ? lvt_3_1_ / this.mob.getSpeed() * 100.0D
                    : 0.0D;
            }

            if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
                this.timeoutCachedNode = Vector3i.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0D;
                this.stop();
            }

            this.lastTimeoutCheck = Util.getMillis();
        }

    }

    @Override
    protected boolean canMoveDirectly(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
        Vector3d lvt_6_1_ = new Vector3d(posVec32.x, posVec32.y + this.mob.getBbHeight() * 0.5D, posVec32.z);
        return this.level.clip(new RayTraceContext(posVec31, lvt_6_1_, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.mob)).getType() == RayTraceResult.Type.MISS;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.level.getBlockState(pos).isSolidRender(this.level, pos);
    }

    @Override
    public void setCanFloat(boolean canSwim) {
    }
}
