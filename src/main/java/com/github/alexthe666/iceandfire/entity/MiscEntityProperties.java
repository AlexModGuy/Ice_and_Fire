package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class MiscEntityProperties extends EntityProperties<EntityLivingBase> {

    public boolean hasDismountedDragon;
    public int deathwormLungeTicks = 0;
    public int prevDeathwormLungeTicks = 0;
    public int specialWeaponDmg = 0;
    public boolean deathwormLaunched = false;
    public boolean deathwormReceded = false;
    public boolean isBeingGlaredAt = false;
    public List<Entity> glarers = new ArrayList<>();
    public List<Entity> entitiesWeAreGlaringAt = new ArrayList<>();
    public int inLoveTicks;

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setBoolean("DismountedDragon", hasDismountedDragon);
        compound.setInteger("GauntletDamage", specialWeaponDmg);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        this.hasDismountedDragon = compound.getBoolean("DismountedDragon");
        this.specialWeaponDmg = compound.getInteger("GauntletDamage");
    }

    @Override
    public void init() {
        hasDismountedDragon = false;
    }

    @Override
    public String getID() {
        return "Ice And Fire - Player Property Tracker";
    }

    @Override
    public Class<EntityLivingBase> getEntityClass() {
        return EntityLivingBase.class;
    }
}
