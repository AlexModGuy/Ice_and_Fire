package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;
import java.util.List;

import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;

public class DreadAIRideHorse extends Goal {
    private final EntityDreadKnight knight;
    private AbstractHorseEntity horse;

    public DreadAIRideHorse(EntityDreadKnight knight) {
        this.knight = knight;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.knight.isPassenger()) {
            return false;
        } else {
            List<AbstractHorseEntity> list = this.knight.world.getEntitiesWithinAABB(AbstractHorseEntity.class,
                this.knight.getBoundingBox().grow(16.0D, 7.0D, 16.0D), entity -> !entity.isBeingRidden());

            if (list.isEmpty()) {
                return false;
            } else {
                this.horse = list.get(0);
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.knight.isPassenger() && this.horse != null && !this.horse.isBeingRidden();
    }

    @Override
    public void startExecuting() {
        this.horse.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
        this.horse = null;
        this.knight.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        this.knight.getLookController().setLookPositionWithEntity(this.horse, 30.0F, 30.0F);

        this.knight.getNavigator().tryMoveToEntityLiving(this.horse, 1.2D);

        if (this.knight.getDistanceSq(this.horse) < 4.0D) {
            this.horse.setHorseTamed(true);
            this.knight.getNavigator().clearPath();
            this.knight.startRiding(horse);
        }
    }
}