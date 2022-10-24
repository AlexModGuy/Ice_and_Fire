package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

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
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public void setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.isStaffInHand();
    }

    protected boolean isStaffInHand() {
        return !this.entity.getMainHandItem().isEmpty() && this.entity.getMainHandItem().getItem() == IafItemRegistry.LICH_STAFF.get();
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.entity.getNavigation().isDone()) && this.isStaffInHand();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.entity.stopUsingItem();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.entity.getTarget();

        if (LivingEntity != null) {
            final double d0 = this.entity.distanceToSqr(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
            final boolean flag = this.entity.getSensing().hasLineOfSight(LivingEntity);
            final boolean flag1 = this.seeTime > 0;

            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (d0 <= this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.entity.getNavigation().moveTo(LivingEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if (this.entity.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if (this.entity.getRandom().nextFloat() < 0.3D) {
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

                this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.lookAt(LivingEntity, 30.0F, 30.0F);
            } else {
                this.entity.getLookControl().setLookAt(LivingEntity, 30.0F, 30.0F);
            }

            if (!flag && this.seeTime < -60) {
                this.entity.stopUsingItem();
            } else if (flag) {
                this.entity.stopUsingItem();
                ((RangedAttackMob) this.entity).performRangedAttack(LivingEntity, 0);
            }
        }
    }
}
