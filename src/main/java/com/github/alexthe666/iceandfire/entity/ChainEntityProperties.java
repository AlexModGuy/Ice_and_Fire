package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageAddChainedEntity;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.message.MessageRemoveChainedEntity;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChainEntityProperties extends EntityProperties<EntityLivingBase> {

	private List<UUID> connectedEntityUUID = new ArrayList<UUID>();
	public List<Entity> connectedEntities = new ArrayList<>();
	public boolean alreadyIgnoresCamera = false;

	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList nbttaglist = new NBTTagList();
		for(UUID uuid : connectedEntityUUID){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setUniqueId("UUID", uuid);
			nbttaglist.appendTag(nbttagcompound);
		}
		compound.setTag("ConnectedEntities", nbttaglist);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagList nbttaglist = compound.getTagList("ConnectedEntities", 0);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			connectedEntityUUID.add(nbttagcompound.getUniqueId("UUID"));
		}
	}

	public void clearChained(){
		this.connectedEntityUUID.clear();
		this.connectedEntities.clear();
	}

	public boolean isChained(){
		return !connectedEntityUUID.isEmpty();
	}

	@Override
	public void init() {
		updateConnectedEntities();
		if(getEntity().ignoreFrustumCheck){
			alreadyIgnoresCamera = true;
		}else{
			alreadyIgnoresCamera = false;
		}
	}

	public void addChain(Entity entity){
		if(!connectedEntityUUID.contains(entity.getPersistentID())) {
			connectedEntityUUID.add(entity.getPersistentID());
		}
		updateConnectedEntities();
	}

	public void removeChain(Entity entity){
		connectedEntityUUID.remove(entity.getPersistentID());
		connectedEntities.remove(entity);
		if(!entity.world.isRemote){
			IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageRemoveChainedEntity(getEntity().getEntityId(), entity.getEntityId()));

		}
		updateConnectedEntities();
	}


	public List<Entity> getCurrentConnectedEntities(){
		return connectedEntities;
	}

	public void updateConnectedEntities(){
		if(!connectedEntityUUID.isEmpty() && getWorld() != null && !getWorld().isRemote) {
			for (UUID uuid : connectedEntityUUID) {
				if(getWorld() != null && getWorld().getMinecraftServer() != null) {
					Entity entity = getWorld().getMinecraftServer().getEntityFromUuid(uuid);
					if (entity != null && !connectedEntities.contains(entity)) {
						connectedEntities.add(entity);
						IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageAddChainedEntity(getEntity().getEntityId(), entity.getEntityId()));
					}
				}
			}
		}
	}

	@Override
	public String getID() {
		return "Ice And Fire - Chain Property Tracker";
	}

	@Override
	public Class<EntityLivingBase> getEntityClass() {
		return EntityLivingBase.class;
	}

	public boolean isConnectedToEntity(Entity entity) {
		updateConnectedEntities();
		return this.connectedEntities.contains(entity);
	}
}
