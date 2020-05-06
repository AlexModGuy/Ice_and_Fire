package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HippogryphAIAttackMelee extends EntityAIBase {
    protected final int attackInterval = 20;
    protected EntityCreature attacker;
    protected int attackTick;
    World world;
    double speedTowardsTarget;
    boolean longMemory;
    Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public HippogryphAIAttackMelee(EntityCreature creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.world = creature.world;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        LivingEntity LivingEntity = this.attacker.getAttackTarget();

        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isEntityAlive()) {
            return false;
        } else {
            if (canPenalize) {
                if (--this.delayCounter <= 0) {
                    this.path = this.attacker.getNavigator().getPathToLivingEntity(LivingEntity);
                    this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                    return this.path != null;
                } else {
                    return true;
                }
            }
            this.path = this.attacker.getNavigator().getPathToLivingEntity(LivingEntity);

            if (this.path != null) {
                return true;
            } else {
                return this.getAttackReachSqr(LivingEntity) >= this.attacker.getDistanceSq(LivingEntity.posX, LivingEntity.getEntityBoundingBox().minY, LivingEntity.posZ);
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        LivingEntity LivingEntity = this.attacker.getAttackTarget();

        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isEntityAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigator().noPath();
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(LivingEntity))) {
            return false;
        } else {
            return !(LivingEntity instanceof PlayerEntity) || !((PlayerEntity) LivingEntity).isSpectator() && !((PlayerEntity) LivingEntity).isCreative();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        LivingEntity LivingEntity = this.attacker.getAttackTarget();

        if (LivingEntity instanceof PlayerEntity && (((PlayerEntity) LivingEntity).isSpectator() || ((PlayerEntity) LivingEntity).isCreative())) {
            this.attacker.setAttackTarget(null);
        }

        this.attacker.getNavigator().clearPath();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        LivingEntity LivingEntity = this.attacker.getAttackTarget();
        if (LivingEntity != null) {
            this.attacker.getLookHelper().setLookPositionWithEntity(LivingEntity, 30.0F, 30.0F);
            double d0 = this.attacker.getDistanceSq(LivingEntity.posX, LivingEntity.getEntityBoundingBox().minY, LivingEntity.posZ);
            --this.delayCounter;

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(LivingEntity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || LivingEntity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = LivingEntity.posX;
                this.targetY = LivingEntity.getEntityBoundingBox().minY;
                this.targetZ = LivingEntity.posZ;
                this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);

                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.attacker.getNavigator().getPath() != null) {
                        net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                        if (finalPathPoint != null && LivingEntity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }

                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }

                if (!this.attacker.getNavigator().tryMoveToLivingEntity(LivingEntity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);
            this.checkAndPerformAttack(LivingEntity, d0);
        }
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);

        if (distToEnemySqr <= d0) {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(enemy);
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double) (this.attacker.width * 4.0F * this.attacker.width * 4.0F + attackTarget.width);
    }
}