package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class EntityAIWatchClosestIgnoreRider extends LookAtPlayerGoal {
    LivingEntity entity;

    public EntityAIWatchClosestIgnoreRider(Mob entity, Class<? extends LivingEntity> type, float dist) {
        super(entity, type, dist);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && lookAt != null && isRidingOrBeingRiddenBy(lookAt, entity);
    }

    public static boolean isRidingOrBeingRiddenBy(Entity first, Entity entityIn) {
        for (Entity entity : first.getPassengers()) {
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
