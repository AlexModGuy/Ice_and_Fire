package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MiscPlayerProperties extends EntityProperties<EntityPlayer> {

	public boolean hasDismountedDragon;

	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("DismountedDragon", hasDismountedDragon);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.hasDismountedDragon = compound.getBoolean("DismountedDragon");
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
