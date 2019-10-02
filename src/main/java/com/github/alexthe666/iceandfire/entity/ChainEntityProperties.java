package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageAddChainedEntity;
import com.github.alexthe666.iceandfire.message.MessageRemoveChainedEntity;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChainEntityProperties extends EntityProperties<EntityLivingBase> {

    public List<Entity> connectedEntities = new ArrayList<>();
    public boolean alreadyIgnoresCamera = false;
    public boolean wasJustDisconnected = false;
    private List<UUID> connectedEntityUUID = new ArrayList<UUID>();

    @Override
    public int getTrackingTime() {
        return 20;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (UUID uuid : connectedEntityUUID) {
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
        updateConnectedEntities(getEntity());
    }

    public void clearChained() {
        this.connectedEntities.clear();
        this.connectedEntityUUID.clear();
    }

    public boolean isChained() {
        return !connectedEntityUUID.isEmpty();
    }

    @Override
    public void init() {
        updateConnectedEntities(getEntity());
        alreadyIgnoresCamera = getEntity().ignoreFrustumCheck;
    }

    public void addChain(Entity parent, Entity entity) {
        minimizeLists();
        if (!connectedEntityUUID.contains(entity.getUniqueID())) {
            connectedEntityUUID.add(entity.getUniqueID());
        }
        updateConnectedEntities(parent);
    }

    public void removeChain(Entity us, Entity entity) {
        minimizeLists();
        connectedEntityUUID.remove(entity.getUniqueID());
        connectedEntities.remove(entity);
        if (entity != null && !entity.world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageRemoveChainedEntity(us.getEntityId(), entity.getEntityId()));
        }
        wasJustDisconnected = true;
    }


    public List<Entity> getCurrentConnectedEntities() {
        return connectedEntities;
    }

    public void minimizeLists() {
        List<UUID> noDupesUUID = new ArrayList();
        for (UUID uuid : connectedEntityUUID) {
            if (!noDupesUUID.contains(uuid)) {
                noDupesUUID.add(uuid);
            }
        }
        connectedEntityUUID = noDupesUUID;
        List<Entity> noDupesEntity = new ArrayList();
        List<UUID> addedUUIDs = new ArrayList<UUID>();
        for (Entity entity : connectedEntities) {
            if (!addedUUIDs.contains(entity.getUniqueID())) {
                addedUUIDs.add(entity.getUniqueID());
                noDupesEntity.add(entity);
            }
        }
        connectedEntities = noDupesEntity;
    }

    public void updateConnectedEntities(Entity toUpdate) {
        connectedEntities.clear();
        List<UUID> addedUUIDs = new ArrayList<UUID>();
        if (toUpdate != null) {
            World world = toUpdate.world;
            if (!connectedEntityUUID.isEmpty() && world != null && !world.isRemote) {
                minimizeLists();
                for (UUID uuid : connectedEntityUUID) {
                    if (world.getMinecraftServer() != null) {
                        Entity entity = world.getMinecraftServer().getEntityFromUuid(uuid);
                        if (entity != null) {
                            if (!addedUUIDs.contains(entity.getUniqueID())) {
                                addedUUIDs.add(entity.getUniqueID());
                                IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageAddChainedEntity(toUpdate.getEntityId(), entity.getEntityId()));
                                connectedEntities.add(entity);
                            }
                        }
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

    public boolean isConnectedToEntity(Entity parent, Entity entity) {
        updateConnectedEntities(parent);
        return this.connectedEntities.contains(entity);
    }
}
