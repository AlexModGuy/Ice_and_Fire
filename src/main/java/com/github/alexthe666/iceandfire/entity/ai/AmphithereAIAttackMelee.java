package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class AmphithereAIAttackMelee extends Goal {
    protected EntityAmphithere attacker;
    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    protected int attackTick;
    /**
     * The speed with which the mob will approach the target
     */
    double speedTowardsTarget;
    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    boolean longMemory;
    /**
     * The PathEntity of our entity.
     */
    Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;

    public AmphithereAIAttackMelee(EntityAmphithere amphithere, double speedIn, boolean useLongMemory) {
        this.attacker = amphithere;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.attacker.getTarget();
        if (!attacker.canMove()) {
            return false;
        }
        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isAlive()) {
            return false;
        } else {
            if (canPenalize) {
                if (--this.delayCounter <= 0) {
                    this.path = this.attacker.getNavigation().createPath(LivingEntity, 0);
                    this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                    return this.path != null;
                } else {
                    return true;
                }
            }
            this.path = this.attacker.getNavigation().createPath(LivingEntity, 0);

            if (this.path != null) {
                return true;
            } else {
                return this.getAttackReachSqr(LivingEntity) >= this.attacker.distanceToSqr(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
            }
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        LivingEntity living = this.attacker.getTarget();

        if (living == null) {
            return false;
        } else if (!living.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(living.blockPosition())) {
            return false;
        } else {
            return !(living instanceof Player) || !living.isSpectator() && !((Player) living).isCreative();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        if (attacker.isFlying()) {
            this.attacker.getMoveControl().setWantedPosition(this.targetX, this.targetY, this.targetZ, 0.1F);
        } else {
            this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        }
        this.delayCounter = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        LivingEntity LivingEntity = this.attacker.getTarget();

        if (LivingEntity instanceof Player && (LivingEntity.isSpectator() || ((Player) LivingEntity).isCreative())) {
            this.attacker.setTarget(null);
        }

        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.attacker.getTarget();
        if (attacker.isFlying()) {
            this.attacker.getMoveControl().setWantedPosition(LivingEntity.getX(), LivingEntity.getY() + LivingEntity.getEyeHeight(), LivingEntity.getZ(), 0.1D);
        }
        this.attacker.getLookControl().setLookAt(LivingEntity, 30.0F, 30.0F);
        double d0 = this.attacker.distanceToSqr(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
        --this.delayCounter;

        if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(LivingEntity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || LivingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
            this.targetX = LivingEntity.getX();
            this.targetY = LivingEntity.getBoundingBox().minY;
            this.targetZ = LivingEntity.getZ();
            this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);

            if (this.canPenalize) {
                this.delayCounter += failedPathFindingPenalty;
                if (this.attacker.getNavigation().getPath() != null) {
                    net.minecraft.world.level.pathfinder.Node finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
                    if (finalPathPoint != null && LivingEntity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
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

            if (!this.attacker.getNavigation().moveTo(LivingEntity, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(LivingEntity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0) {
            this.attackTick = 20;
            this.attacker.swing(InteractionHand.MAIN_HAND);
            this.attacker.doHurtTarget(enemy);
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }
}