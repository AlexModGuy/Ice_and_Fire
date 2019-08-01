package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MiscPlayerProperties extends EntityProperties<EntityPlayer> {

	public boolean hasDismountedDragon;
	public int deathwormLungeTicks = 0;
	public int prevDeathwormLungeTicks = 0;
	public int gauntletDamage = 0;
	public boolean deathwormLaunched = false;
	public boolean deathwormReceded = false;

	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("DismountedDragon", hasDismountedDragon);
		compound.setInteger("GauntletDamage", gauntletDamage);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.hasDismountedDragon = compound.getBoolean("DismountedDragon");
		this.gauntletDamage = compound.getInteger("GauntletDamage");
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
	public Class<EntityPlayer> getEntityClass() {
		return EntityPlayer.class;
	}
}
