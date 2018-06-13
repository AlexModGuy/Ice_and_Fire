package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class SirenEntityProperties extends EntityProperties<EntityLivingBase> {

	public boolean isCharmed;

	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("CharmedBySiren", isCharmed);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.isCharmed = compound.getBoolean("CharmedBySiren");
	}

	@Override
	public void init() {
		isCharmed = false;
	}

	public EntitySiren getClosestSiren(World world, EntityLivingBase player){
		if(player instanceof EntityPlayer && ((EntityPlayer) player).isCreative()){
			return null;
		}
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(EntitySiren.SEARCH_RANGE, EntitySiren.SEARCH_RANGE, EntitySiren.SEARCH_RANGE);
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, aabb);
		Collections.sort(entities, new EntityAINearestAttackableTarget.Sorter(player));
		for(Entity entity : entities){
			if(entity instanceof EntitySiren && !((EntitySiren) entity).isAgressive() && !((EntitySiren) entity).isDead){
				return (EntitySiren)entity;
			}
		}
		return null;
	}

	@Override
	public String getID() {
		return "Ice And Fire - Siren Property Tracker";
	}

	@Override
	public Class<EntityLivingBase> getEntityClass() {
		return EntityLivingBase.class;
	}
}
