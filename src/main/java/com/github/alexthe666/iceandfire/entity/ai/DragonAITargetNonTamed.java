package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class DragonAITargetNonTamed<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityDragonBase dragon;

    public DragonAITargetNonTamed(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 5, checkSight, false, targetSelector);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canUse() {
        if (dragon.isTame()) {
            return false;
        }

        if (dragon.lookingForRoostAIFlag) {
            return false;
        }

        boolean canUse = super.canUse();
        boolean isSleeping = dragon.isSleeping();

        if (canUse) {
            if (isSleeping && target instanceof Player) {
                return dragon.distanceToSqr(target) <= 16;
            }

            return !isSleeping;
        }

        return false;
    }

    @Override
    protected @NotNull AABB getTargetSearchArea(double targetDistance) {
        return this.dragon.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }

    @Override
    protected double getFollowDistance() {
        AttributeInstance iattributeinstance = this.mob.getAttribute(Attributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 128.0D : iattributeinstance.getValue();
    }
}