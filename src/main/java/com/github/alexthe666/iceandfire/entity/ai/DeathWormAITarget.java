package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;

public class DeathWormAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityDeathWorm deathworm;

    public DeathWormAITarget(EntityDeathWorm entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.deathworm = entityIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (super.canUse() && target != null
            && !target.getClass().isAssignableFrom(this.deathworm.getClass())) {
            if (target instanceof PlayerEntity && !deathworm.isOwnedBy(target)) {
                return !deathworm.isTame();
            } else if (deathworm.isOwnedBy(target)) {
                return false;
            }

            if (target instanceof MonsterEntity && deathworm.getWormAge() > 2) {
                if (target instanceof CreatureEntity) {
                    return deathworm.getWormAge() > 3;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected AxisAlignedBB getTargetSearchArea(double targetDistance) {
        return this.deathworm.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}