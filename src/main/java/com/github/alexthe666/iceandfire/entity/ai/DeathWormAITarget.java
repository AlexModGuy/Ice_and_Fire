package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.google.common.base.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

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
            if (target instanceof Player && !deathworm.isOwnedBy(target)) {
                return !deathworm.isTame();
            } else if (deathworm.isOwnedBy(target)) {
                return false;
            }

            if (target instanceof Monster && deathworm.getWormAge() > 2) {
                if (target instanceof PathfinderMob) {
                    return deathworm.getWormAge() > 3;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected @NotNull AABB getTargetSearchArea(double targetDistance) {
        return this.deathworm.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}