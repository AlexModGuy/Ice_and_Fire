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
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class ChainEntityProperties extends EntityProperties<EntityLivingBase> {

	private List<UUID> connectedEntityUUID = new ArrayList<UUID>();
	public List<Entity> connectedEntities = new ArrayList<>();
	public boolean alreadyIgnoresCamera = false;
	public boolean wasJustDisconnected = false;

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
		NBTTagList nbttaglist = compound.getTagList("ConnectedEntities", Constants.NBT.TAG_COMPOUND);
		this.connectedEntityUUID = new ArrayList<UUID>();
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			connectedEntityUUID.add(nbttagcompound.getUniqueId("UUID"));
		}
		updateConnectedEntities();
	}

	public void clearChained(){
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
		minimizeLists();
		if(!connectedEntityUUID.contains(entity.getUniqueID())) {
			connectedEntityUUID.add(entity.getUniqueID());
		}
		updateConnectedEntities();
	}

	public void removeChain(Entity entity){
		minimizeLists();
		connectedEntityUUID.remove(entity.getUniqueID());
		connectedEntities.remove(entity);
		if(!entity.world.isRemote){
			IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageRemoveChainedEntity(getEntity().getEntityId(), entity.getEntityId()));
		}
		wasJustDisconnected = true;
	}


	public List<Entity> getCurrentConnectedEntities(){
		return connectedEntities;
	}

	private void minimizeLists(){
		List<UUID> noDupesUUID = new ArrayList();
		for(UUID uuid : connectedEntityUUID){
			if(!noDupesUUID.contains(uuid)){
				noDupesUUID.add(uuid);
			}
		}
		connectedEntityUUID = noDupesUUID;
		List<Entity> noDupesEntity = new ArrayList();
		for(Entity entity : connectedEntities){
			if(!noDupesEntity.contains(entity)){
				noDupesEntity.add(entity);
			}
		}
		connectedEntities = noDupesEntity;
	}

	public void updateConnectedEntities(){
		connectedEntities.clear();
		if(getEntity() != null) {
			World world = getEntity().world;
			if (!connectedEntityUUID.isEmpty() && world != null && !world.isRemote) {
				minimizeLists();
				for (UUID uuid : connectedEntityUUID) {
					if (world.getMinecraftServer() != null) {
						Entity entity = world.getMinecraftServer().getEntityFromUuid(uuid);
						if (entity != null) {
							IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageAddChainedEntity(getEntity().getEntityId(), entity.getEntityId()));
							if (!connectedEntities.contains(entity)) {
								connectedEntities.add(entity);
							}
						}
					}
				}
			}
		}
	}

	public void updateClients(){
		updateConnectedEntities();
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
