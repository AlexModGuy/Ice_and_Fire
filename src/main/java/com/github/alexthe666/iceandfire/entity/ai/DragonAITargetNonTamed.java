package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.AxisAlignedBB;

public class DragonAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private EntityDragonBase dragon;

    public DragonAITargetNonTamed(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.setMutexBits(1);
        this.dragon = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        return !dragon.isTamed() && !dragon.isSleeping() && super.shouldExecute();
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.dragon.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    protected double getTargetDistance() {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 128.0D : iattributeinstance.getAttributeValue();
    }
}