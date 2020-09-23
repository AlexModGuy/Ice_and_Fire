package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;

public class EntityAIWatchClosestIgnoreRider extends LookAtGoal {
    LivingEntity entity;

    public EntityAIWatchClosestIgnoreRider(MobEntity entity, Class<? extends LivingEntity> type, float dist) {
        super(entity, type, dist);
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && closestEntity != null && isRidingOrBeingRiddenBy(closestEntity, entity);
    }

    public static boolean isRidingOrBeingRiddenBy(Entity first, Entity entityIn) {
        for(Entity entity : first.getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }

            if (isRidingOrBeingRiddenBy(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

}
