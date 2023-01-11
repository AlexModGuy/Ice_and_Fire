package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class DragonAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityDragonBase dragon;

    public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 3, checkSight, false, targetSelector);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canUse() {
        if (dragon.getCommand() == 1 || dragon.getCommand() == 2 || dragon.isSleeping()) {
            return false;
        }
        if (!dragon.isTame() && dragon.lookingForRoostAIFlag) {
            return false;
        }
        if (target != null && !target.getClass().equals(this.dragon.getClass())) {
            if (!super.canUse())
                return false;

            final float dragonSize = Math.max(this.dragon.getBbWidth(), this.dragon.getBbWidth() * dragon.getRenderSize());
            if (dragonSize >= target.getBbWidth()) {
                if (target instanceof Player && !dragon.isTame()) {
                    return true;
                }
                if (target instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) target;
                    if (dragon.getOwner() != null && this.dragon.getOwner() != null && this.dragon.isOwnedBy(dragon.getOwner())) {
                        return false;
                    }
                    return !dragon.isModelDead();
                }
                if (target instanceof Player && dragon.isTame()) {
                    return false;
                } else {
                    if (!dragon.isOwnedBy(target) && FoodUtils.getFoodPoints(target) > 0 && dragon.canMove() && (dragon.getHunger() < 90 || !dragon.isTame() && target instanceof Player)) {
                        if (dragon.isTame()) {
                            return DragonUtils.canTameDragonAttack(dragon, target);
                        } else {
                            return true;
                        }
                    }
                }
            }
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
        return iattributeinstance == null ? 64.0D : iattributeinstance.getValue();
    }
}