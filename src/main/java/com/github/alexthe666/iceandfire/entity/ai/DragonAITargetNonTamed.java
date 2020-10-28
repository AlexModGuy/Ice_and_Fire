package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

public class DragonAITargetNonTamed<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private EntityDragonBase dragon;

    public DragonAITargetNonTamed(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        return !dragon.isTamed() && !dragon.isSleeping() && super.shouldExecute();
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.dragon.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    protected double getTargetDistance() {
        ModifiableAttributeInstance iattributeinstance = this.goalOwner.getAttribute(Attributes.field_233819_b_);
        return iattributeinstance == null ? 128.0D : iattributeinstance.getValue();
    }
}