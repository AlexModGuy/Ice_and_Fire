package com.github.alexthe666.iceandfire.entity.ai;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

public class FlyingAITarget extends NearestAttackableTargetGoal<LivingEntity> {

    public FlyingAITarget(MobEntity creature, Class<LivingEntity> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(MobEntity creature, Class<LivingEntity> classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(MobEntity creature, Class<LivingEntity> classTarget, int chance, boolean checkSight,
        boolean onlyNearby, @Nullable final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

    @Override
    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    @Override
    public boolean shouldExecute() {
        if (goalOwner instanceof EntitySeaSerpent && (((EntitySeaSerpent) goalOwner).isJumpingOutOfWater() || !goalOwner.isInWater())) {
            return false;
        }
        return super.shouldExecute();
    }

}
