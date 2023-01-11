package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FlyingAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public FlyingAITarget(Mob creature, Class<T> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(Mob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(Mob creature, Class<T> classTarget, int chance, boolean checkSight,
                          boolean onlyNearby, @Nullable final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

    @Override
    protected @NotNull AABB getTargetSearchArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }

    @Override
    public boolean canUse() {
        if (mob instanceof EntitySeaSerpent && (((EntitySeaSerpent) mob).isJumpingOutOfWater() || !mob.isInWater())) {
            return false;
        }
        return super.canUse();
    }

}
