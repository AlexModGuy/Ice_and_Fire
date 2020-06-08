package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AbstractHorse;

import java.util.List;

public class DreadAIMountDragon extends Goal {
    private final EntityDreadQueen knight;
    private EntityBlackFrostDragon horse;

    public DreadAIMountDragon(EntityDreadQueen knight) {
        this.knight = knight;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.knight.isRiding()) {
            return false;
        } else {
            List<EntityBlackFrostDragon> list = this.knight.world.getEntitiesWithinAABB(EntityBlackFrostDragon.class, this.knight.getBoundingBox().grow(32.0D, 7.0D, 32.0D));

            if (list.isEmpty()) {
                return false;
            } else {
                for (EntityBlackFrostDragon entityirongolem : list) {
                   if(!entityirongolem.isBeingRidden()){
                       this.horse = entityirongolem;
                       break;
                   }
                }

                return this.horse != null;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.knight.isRiding() && this.horse != null && !this.horse.isBeingRidden();
    }

    public void startExecuting() {
        this.horse.getNavigator().clearPath();
    }

    public void resetTask() {
        this.horse = null;
        this.knight.getNavigator().clearPath();
    }

    public void tick() {
        this.knight.getLookController().setLookPositionWithEntity(this.horse, 30.0F, 30.0F);

        this.knight.getNavigator().tryMoveToLivingEntity(this.horse, 1.2D);
        if (this.knight.getDistanceSq(this.horse) < this.horse.getRenderSize() * 0.66D) {
            this.knight.getNavigator().clearPath();
            this.horse.setCommanderId(this.knight.getUniqueID());
            this.knight.startRiding(horse);
        }
    }
}