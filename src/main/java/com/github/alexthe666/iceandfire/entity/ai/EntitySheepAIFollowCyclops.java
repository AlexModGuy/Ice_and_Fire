package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntitySheepAIFollowCyclops extends Goal {
    Animal childAnimal;
    EntityCyclops cyclops;
    double moveSpeed;
    private int delayCounter;

    public EntitySheepAIFollowCyclops(Animal animal, double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        List<EntityCyclops> list = this.childAnimal.level().getEntitiesOfClass(EntityCyclops.class, this.childAnimal.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));
        EntityCyclops cyclops = null;
        double d0 = Double.MAX_VALUE;

        for (EntityCyclops cyclops1 : list) {
            final double d1 = this.childAnimal.distanceToSqr(cyclops1);

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


    @Override
    public boolean canContinueToUse() {
        if (this.cyclops.isAlive()) {
            return false;
        } else {
            final double d0 = this.childAnimal.distanceToSqr(this.cyclops);
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }


    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.cyclops = null;
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = this.adjustedTickDelay(10);
            if (this.childAnimal.distanceToSqr(this.cyclops) > 10) {
                Path path = getPathToLivingEntity(this.childAnimal, this.cyclops);
                if (path != null) {
                    this.childAnimal.getNavigation().moveTo(path, this.moveSpeed);

                }
            }
        }
    }

    public Path getPathToLivingEntity(Animal entityIn, EntityCyclops cyclops) {
        PathNavigation navi = entityIn.getNavigation();
        Vec3 Vector3d = DefaultRandomPos.getPosTowards(entityIn, 2, 7, cyclops.position(), (float) Math.PI / 2F);
        if (Vector3d != null) {
            BlockPos blockpos = BlockPos.containing(Vector3d);
            return navi.createPath(blockpos, 0);
        }
        return null;
    }
}