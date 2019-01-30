package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAIWatchClosestIgnoreRider extends EntityAIWatchClosest {
    EntityLiving entity;

    public EntityAIWatchClosestIgnoreRider(EntityLiving entity, Class<EntityLivingBase> type, float dist) {
        super(entity, type, dist);
    }

    public boolean shouldExecute(){
        return super.shouldExecute() && closestEntity != null && closestEntity.isRidingOrBeingRiddenBy(entity);
    }
}
