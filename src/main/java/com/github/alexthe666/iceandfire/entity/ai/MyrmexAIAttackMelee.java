package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class MyrmexAIAttackMelee extends Goal {
    protected EntityMyrmexBase myrmex;
    private int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private PathResult attackPath;

    public MyrmexAIAttackMelee(EntityMyrmexBase dragon, double speedIn, boolean useLongMemory) {
        this.myrmex = dragon;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        if (LivingEntity instanceof Player && this.myrmex.getHive() != null) {
            if (!this.myrmex.getHive().isPlayerReputationLowEnoughToFight(LivingEntity.getUUID())) {
                return false;
            }
        }
        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isAlive()) {
            return false;
        } else if (!myrmex.canMove()) {
            return false;
        } else {

            attackPath = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToLivingEntity(LivingEntity, speedTowardsTarget);
            if (this.attackPath != null) {
                return true;
            } else {
                return this.getAttackReachSqr(LivingEntity) >= this.myrmex.distanceToSqr(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (this.myrmex.getLastHurtMob() != null && this.myrmex.getLastHurtMob().isAlive()) {
            LivingEntity = this.myrmex.getLastHurtMob();
        }
        if (LivingEntity != null && !LivingEntity.isAlive() || !(this.myrmex.getNavigation() instanceof AdvancedPathNavigate)) {
            this.stop();
            return false;
        }

        return LivingEntity != null && (LivingEntity.isAlive() && (!(LivingEntity instanceof Player) || !LivingEntity.isSpectator() && !((Player) LivingEntity).isCreative()));
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (LivingEntity instanceof Player && (LivingEntity.isSpectator() || ((Player) LivingEntity).isCreative())) {
            this.myrmex.setTarget(null);
            this.myrmex.setLastHurtMob(null);
        }
    }

    @Override
    public void tick() {
        LivingEntity entity = this.myrmex.getTarget();
        if (entity != null) {
            this.myrmex.getNavigation().moveTo(entity, speedTowardsTarget);
            final double d0 = this.myrmex.distanceToSqr(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
            final double d1 = this.getAttackReachSqr(entity);
            --this.delayCounter;
            if ((this.longMemory || this.myrmex.getSensing().hasLineOfSight(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.myrmex.getRandom().nextFloat() < 0.05F)) {
                this.targetX = entity.getX();
                this.targetY = entity.getBoundingBox().minY;
                this.targetZ = entity.getZ();
                this.delayCounter = 4 + this.myrmex.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.myrmex.getNavigation().getPath() != null) {
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.myrmex.getNavigation().getPath().getEndNode();
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
                if (this.myrmex.canMove()) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);

            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.myrmex.swing(InteractionHand.MAIN_HAND);
                this.myrmex.doHurtTarget(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.myrmex.getBbWidth() * 2.0F * this.myrmex.getBbWidth() * 2.0F + attackTarget.getBbWidth();
    }
}