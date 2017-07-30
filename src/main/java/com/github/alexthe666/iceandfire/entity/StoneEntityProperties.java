package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

public class StoneEntityProperties extends EntityProperties {

    public boolean isStone;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setBoolean("TurnedToStone", isStone);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        this.isStone = compound.getBoolean("TurnedToStone");
    }

    @Override
    public void init() {
        isStone = false;
    }

    @Override
    public String getID() {
        return "Ice And Fire - Stone Property Tracker";
    }

    @Override
    public Class getEntityClass() {
        return EntityLiving.class;
    }
}
