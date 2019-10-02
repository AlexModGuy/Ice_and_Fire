package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class FrozenEntityProperties extends EntityProperties<EntityLivingBase> {

    public boolean isFrozen;
    public int ticksUntilUnfrozen;

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setBoolean("IsFrozen", isFrozen);
        compound.setInteger("TicksUntilUnfrozen", ticksUntilUnfrozen);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        this.isFrozen = compound.getBoolean("IsFrozen");
        this.ticksUntilUnfrozen = compound.getInteger("TicksUntilUnfrozen");
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
    public Class<EntityLivingBase> getEntityClass() {
        return EntityLivingBase.class;
    }

    public void setFrozenFor(int frozenFor) {
        if (!(this.getEntity() instanceof EntityIceDragon)) {
            this.isFrozen = true;
            this.ticksUntilUnfrozen = frozenFor;
        }
    }
}
