package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class FrozenEntityProperties extends EntityProperties<LivingEntity> {

    public boolean isFrozen;
    public int ticksUntilUnfrozen;

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(CompoundNBT compound) {
        compound.setBoolean("IsFrozen", isFrozen);
        compound.putInt("TicksUntilUnfrozen", ticksUntilUnfrozen);
    }

    @Override
    public void loadNBTData(CompoundNBT compound) {
        this.isFrozen = compound.getBoolean("IsFrozen");
        this.ticksUntilUnfrozen = compound.getInt("TicksUntilUnfrozen");
    }

    @Override
    public void init() {
        isFrozen = false;
        ticksUntilUnfrozen = 0;
    }

    @Override
    public String getID() {
        return "Ice And Fire - Frozen Property Tracker";
    }

    @Override
    public Class<LivingEntity> getEntityClass() {
        return LivingEntity.class;
    }

    public void setFrozenFor(int frozenFor) {
        if (!(this.getEntity() instanceof EntityIceDragon)) {
            this.isFrozen = true;
            this.ticksUntilUnfrozen = frozenFor;
        }
    }
}
