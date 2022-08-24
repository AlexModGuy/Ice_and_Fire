package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FlyingAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, int chance, boolean checkSight,
        boolean onlyNearby, @Nullable final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

    @Override
    protected AxisAlignedBB getTargetSearchArea(double targetDistance) {
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
