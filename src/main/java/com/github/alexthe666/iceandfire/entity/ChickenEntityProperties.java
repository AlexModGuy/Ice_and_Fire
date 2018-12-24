package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class ChickenEntityProperties extends EntityProperties<EntityAnimal> {

	public int timeUntilNextEgg = 1;
	private static Random rand = new Random();
	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("TimeUntilNextEgg", timeUntilNextEgg);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.timeUntilNextEgg = compound.getInteger("TimeUntilNextEgg");
	}

	@Override
	public void init() {
		timeUntilNextEgg = generateTime();
	}

	@Override
	public String getID() {
		return "Ice And Fire - Chicken Property Tracker";
	}

	@Override
	public Class<EntityAnimal> getEntityClass() {
		return EntityAnimal.class;
	}

	public int generateTime(){
		return rand.nextInt(6000) + 6000;
	}
}
