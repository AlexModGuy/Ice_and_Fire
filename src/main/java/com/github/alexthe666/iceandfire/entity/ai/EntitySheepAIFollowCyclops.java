package com.github.alexthe666.iceandfire.entity.ai;

import java.util.List;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
            List<EntityCyclops> list = this.childAnimal.world.<EntityCyclops>getEntitiesWithinAABB(EntityCyclops.class, this.childAnimal.getEntityBoundingBox().grow(16.0D, 8.0D, 16.0D));
            EntityCyclops cyclops = null;
            double d0 = Double.MAX_VALUE;

            for (EntityCyclops cyclops1 : list) {
                double d1 = this.childAnimal.getDistanceSq(cyclops1);

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
            double d0 = this.childAnimal.getDistanceSq(this.cyclops);
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
            Path path = getPathToEntityLiving(this.childAnimal, this.cyclops);
            if(path != null){
                this.childAnimal.getNavigator().setPath(path, this.moveSpeed);

            }
        }
    }

    public Path getPathToEntityLiving(EntityAnimal entityIn, EntityCyclops cyclops) {
        PathNavigate navi = entityIn.getNavigator();
        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(entityIn, 2, 7, new Vec3d(cyclops.posX, cyclops.posY, cyclops.posZ));
        if(vec3d != null){
            BlockPos blockpos = new BlockPos(vec3d);
            return navi.getPathToPos(blockpos);
        }
        return null;
    }
}