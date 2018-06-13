package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class ChickenEntityProperties extends EntityProperties<EntityAnimal> {

	public int timeUntilNextEgg;

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
		timeUntilNextEgg = 4 *(this.getEntity().getRNG().nextInt(6000) + 6000);
	}

	@Override
	public String getID() {
		return "Ice And Fire - Chicken Property Tracker";
	}

	@Override
	public Class<EntityAnimal> getEntityClass() {
		return EntityAnimal.class;
	}
}
