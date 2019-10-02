package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.IVillagerFear;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityTameable;

public class VillagerAIFearUntamed extends EntityAIAvoidEntity<EntityLivingBase> {

    public VillagerAIFearUntamed(EntityCreature entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public VillagerAIFearUntamed(EntityCreature entityIn, Class<EntityLivingBase> classToAvoidIn, Predicate<EntityLivingBase> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidTargetSelectorIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public boolean shouldExecute() {
        boolean should = super.shouldExecute();
        if (should && this.closestLivingEntity != null) {
            if (closestLivingEntity instanceof EntityTameable && ((EntityTameable) closestLivingEntity).isTamed()) {
                return false;
            }
            if (closestLivingEntity instanceof IVillagerFear && !((IVillagerFear) closestLivingEntity).shouldFear()) {
                return false;
            }
        }
        return should;
    }
}
