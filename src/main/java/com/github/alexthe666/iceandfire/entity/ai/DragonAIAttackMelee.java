package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class DragonAIAttackMelee extends Goal {
    protected EntityDragonBase dragon;
    private int attackTick;
    private final boolean longMemory;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private final double speedTowardsTarget;

    public DragonAIAttackMelee(EntityDragonBase dragon, double speedIn, boolean useLongMemory) {
        this.dragon = dragon;
        this.longMemory = useLongMemory;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.dragon.getTarget();
        if (!(this.dragon.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else if (!dragon.canMove() || dragon.isHovering() || dragon.isFlying()) {
            return false;
        } else {
            ((AdvancedPathNavigate) this.dragon.getNavigation()).moveToLivingEntity(livingEntity, speedTowardsTarget);
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!(this.dragon.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        LivingEntity livingEntity = this.dragon.getTarget();
        if (livingEntity != null && !livingEntity.isAlive()) {
            this.stop();
            return false;
        }

        return livingEntity != null && livingEntity.isAlive() && !dragon.isFlying() && !dragon.isHovering();
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.dragon.getTarget();
        if (LivingEntity instanceof Player && (LivingEntity.isSpectator() || ((Player) LivingEntity).isCreative())) {
            this.dragon.setTarget(null);
        }
        this.dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity entity = this.dragon.getTarget();
        if(delayCounter > 0){
            delayCounter--;
        }
        if (entity != null) {
            if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                this.stop();
                return;
            }

            ((AdvancedPathNavigate) this.dragon.getNavigation()).moveToLivingEntity(entity, speedTowardsTarget);

            final double d0 = this.dragon.distanceToSqr(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
            final double d1 = this.getAttackReachSqr(entity);
            --this.delayCounter;
            if ((this.longMemory || this.dragon.getSensing().hasLineOfSight(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.dragon.getRandom().nextFloat() < 0.05F)) {
                this.targetX = entity.getX();
                this.targetY = entity.getBoundingBox().minY;
                this.targetZ = entity.getZ();
                this.delayCounter = 4 + this.dragon.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.dragon.getNavigation().getPath() != null) {
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.dragon.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && entity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
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
                if (this.dragon.canMove()) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);

            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.dragon.swing(InteractionHand.MAIN_HAND);
                this.dragon.doHurtTarget(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.dragon.getBbWidth() * 2.0F * this.dragon.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }
}