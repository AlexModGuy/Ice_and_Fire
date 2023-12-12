package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
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
        boolean canUse = super.canUse();

        if (canUse && target != null && target.getType() != (IafEntityRegistry.DEATH_WORM.get())) {
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
        // Increasing the y-range too much makes it target entities in caves etc., which will be unreachable (thus no target will be set)
        return this.deathworm.getBoundingBox().inflate(targetDistance, 6, targetDistance);
    }
}