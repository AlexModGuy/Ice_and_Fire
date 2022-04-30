package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class DreadLichAIStrife extends Goal {

    private final EntityDreadLich entity;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private int attackCooldown;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public DreadLichAIStrife(EntityDreadLich mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public void setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.isStaffInHand();
    }

    protected boolean isStaffInHand() {
        return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == IafItemRegistry.LICH_STAFF;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isStaffInHand();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.seeTime = 0;
        this.entity.resetActiveHand();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.entity.getAttackTarget();

        if (LivingEntity != null) {
            double d0 = this.entity.getDistanceSq(LivingEntity.getPosX(), LivingEntity.getBoundingBox().minY, LivingEntity.getPosZ());
            boolean flag = this.entity.getEntitySenses().canSee(LivingEntity);
            boolean flag1 = this.seeTime > 0;

            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (d0 <= this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entity.getNavigator().tryMoveToEntityLiving(LivingEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if (this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if (this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > this.maxAttackDistance * 0.75F) {
                    this.strafingBackwards = false;
                } else if (d0 < this.maxAttackDistance * 0.25F) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(LivingEntity, 30.0F, 30.0F);
            } else {
                this.entity.getLookController().setLookPositionWithEntity(LivingEntity, 30.0F, 30.0F);
            }

            if (!flag && this.seeTime < -60) {
                this.entity.resetActiveHand();
            } else if (flag) {
                this.entity.resetActiveHand();
                ((IRangedAttackMob) this.entity).attackEntityWithRangedAttack(LivingEntity, 0);
            }
        }
    }
}
