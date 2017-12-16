package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

import java.util.List;

public class EntitySheepAIFollowCyclops extends EntityAIBase {
    EntityAnimal childAnimal;
    EntityCyclops cyclops;
    double moveSpeed;
    private int delayCounter;

    public EntitySheepAIFollowCyclops(EntityAnimal animal, double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    public boolean shouldExecute() {
            List<EntityCyclops> list = this.childAnimal.world.<EntityCyclops>getEntitiesWithinAABB(EntityCyclops.class, this.childAnimal.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
            EntityCyclops cyclops = null;
            double d0 = Double.MAX_VALUE;

            for (EntityCyclops cyclops1 : list) {
                double d1 = this.childAnimal.getDistanceSqToEntity(cyclops1);

                if (d1 <= d0) {
                    d0 = d1;
                    cyclops = cyclops1;
                }
            }

            if (cyclops == null) {
                return false;
            } else if (d0 < 10.0D) {
                return false;
            } else {
                this.cyclops = cyclops;
                return true;
            }
    }


    public boolean shouldContinueExecuting() {
        if (!this.cyclops.isEntityAlive()) {
            return false;
        } else {
            double d0 = this.childAnimal.getDistanceSqToEntity(this.cyclops);
            if(d0 < 10){
                return false;
            }
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }


    public void startExecuting() {
        this.delayCounter = 0;
    }

    public void resetTask() {
        this.cyclops = null;
    }

    public void updateTask() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving(this.cyclops, this.moveSpeed);
        }
    }
}