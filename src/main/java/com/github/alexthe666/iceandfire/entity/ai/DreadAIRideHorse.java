package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;

public class DreadAIRideHorse extends Goal {
    private final EntityDreadKnight knight;
    private AbstractHorse horse;

    @Nonnull
    private List<AbstractHorse> list = IAFMath.emptyAbstractHorseEntityList;

    public DreadAIRideHorse(EntityDreadKnight knight) {
        this.knight = knight;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.knight.isPassenger()) {
            list = IAFMath.emptyAbstractHorseEntityList;
            return false;
        } else {

            if (this.knight.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
                list = this.knight.level().getEntitiesOfClass(AbstractHorse.class,
                    this.knight.getBoundingBox().inflate(16.0D, 7.0D, 16.0D), entity -> !entity.isVehicle());

            if (list.isEmpty()) {
                return false;
            } else {
                this.horse = list.get(0);
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.knight.isPassenger() && this.horse != null && !this.horse.isVehicle();
    }

    @Override
    public void start() {
        this.horse.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.horse = null;
        this.knight.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.knight.getLookControl().setLookAt(this.horse, 30.0F, 30.0F);

        this.knight.getNavigation().moveTo(this.horse, 1.2D);

        if (this.knight.distanceToSqr(this.horse) < 4.0D) {
            this.horse.setTamed(true);
            this.knight.getNavigation().stop();
            this.knight.startRiding(horse);
        }
    }
}