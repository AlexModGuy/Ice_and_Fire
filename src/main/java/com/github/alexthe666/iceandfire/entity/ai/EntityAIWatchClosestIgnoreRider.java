package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAIWatchClosestIgnoreRider extends EntityAIWatchClosest {
    EntityLiving entity;

    public EntityAIWatchClosestIgnoreRider(EntityLiving entity, Class<LivingEntity> type, float dist) {
        super(entity, type, dist);
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && closestEntity != null && closestEntity.isRidingOrBeingRiddenBy(entity);
    }
}
