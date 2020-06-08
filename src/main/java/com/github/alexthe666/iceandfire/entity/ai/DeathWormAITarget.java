package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class DeathWormAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private EntityDeathWorm deathworm;

    public DeathWormAITarget(EntityDeathWorm entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.deathworm = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute() && this.target != null && !this.target.getClass().equals(this.deathworm.getClass())) {
            if (this.target instanceof PlayerEntity && !deathworm.isOwner(this.target)) {
                return !deathworm.isTamed();
            } else {
                if (!deathworm.isOwner(this.target)) {
                    return true;
                }
                if (this.target instanceof MonsterEntity && deathworm.getWormAge() > 2) {
                    if (this.target instanceof CreatureEntity) {
                        return deathworm.getWormAge() > 3;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.deathworm.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }
}