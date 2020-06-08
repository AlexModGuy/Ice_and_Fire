package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;

public class EntityAIWatchClosestIgnoreRider extends LookAtGoal {
    LivingEntity entity;

    public EntityAIWatchClosestIgnoreRider(MobEntity entity, Class<? extends LivingEntity> type, float dist) {
        super(entity, type, dist);
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && closestEntity != null && closestEntity.isRidingOrBeingRiddenBy(entity);
    }
}
