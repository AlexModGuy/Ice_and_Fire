package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChainProperties {

    private static final String CHAIN_TO_TAG = "ChainOwnerIaf";
    private static final String CHAIN_TO_ENTITY_ID_TAG = "ChainOwnerIDIaf";
    private static final String CHAIN_DATA = "ChainDataIaf";

    public static void attachChain(LivingEntity chained, Entity chainedTo) {
        if (isChainedTo(chained, chainedTo)) {
            return;
        }
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(chained);
        ListNBT chainData = getOrCreateChainData(entityData);
        CompoundNBT currentChainData = new CompoundNBT();
        currentChainData.putUniqueId(CHAIN_TO_TAG, chainedTo.getUniqueID());
        currentChainData.putInt(CHAIN_TO_ENTITY_ID_TAG, chainedTo.getEntityId());

        chainData.add(currentChainData);
        entityData.put(CHAIN_DATA, chainData);
        updateData(chained, entityData);
    }

    public static CompoundNBT getConnectedEntityChainData(LivingEntity host, Entity target) {
        ListNBT chainData = getOrCreateChainData(host);
        if (chainData.isEmpty())
            return null;
        return getConnectedEntityChainData(chainData, target);
    }

    @Nullable
    public static CompoundNBT getConnectedEntityChainData(ListNBT chainData, Entity entity) {
        for (int i = 0; i < chainData.size(); i++) {
            CompoundNBT nbt = (CompoundNBT) chainData.get(i);
            if (nbt.contains(CHAIN_TO_TAG) && nbt.getUniqueId(CHAIN_TO_TAG).equals(entity.getUniqueID()))
                return nbt;
        }
        return null;
    }

    public static boolean hasChainData(LivingEntity entity) {
        return !getOrCreateChainData(entity).isEmpty();
    }

    public static boolean hasEntityData(ListNBT chainData, Entity entity) {
        return getConnectedEntityChainData(chainData, entity) != null;
    }

    private static ListNBT getOrCreateChainData(LivingEntity entity) {
        return getOrCreateChainData(CitadelEntityData.getOrCreateCitadelTag(entity));
    }

    private static ListNBT getOrCreateChainData(CompoundNBT entityData) {
        //TODO: Look at type
        if (entityData.contains(CHAIN_DATA, 9)) {
            return entityData.getList(CHAIN_DATA, 10);
        }
        return new ListNBT();
    }

    private static void updateData(LivingEntity entity) {
        updateData(entity, CitadelEntityData.getOrCreateCitadelTag(entity));
    }

    private static void updateData(LivingEntity entity, CompoundNBT nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.world.isRemote()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getEntityId()));
        }
    }

    public static void removeChain(LivingEntity entity, Entity connectedTo) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        ListNBT chainData = getOrCreateChainData(entityData);
        int dataIndex = -1;
        for (int i = 0; i < chainData.size(); i++) {
            CompoundNBT nbt = (CompoundNBT) chainData.get(i);
            if (nbt.contains(CHAIN_TO_TAG) && nbt.getUniqueId(CHAIN_TO_TAG).equals(connectedTo.getUniqueID())) {
                //TODO: might be able to remove in loop
                dataIndex = i;
                break;
            }
        }
        if (dataIndex != -1) {
            chainData.remove(dataIndex);
            entityData.put(CHAIN_DATA, chainData);
            updateData(entity, entityData);
        }
    }

    public static List<Entity> getChainedTo(LivingEntity chained) {
        ListNBT chainData = getOrCreateChainData(chained);
        List<Entity> chainedTo = new ArrayList<>();
        if (chainData.isEmpty()) {
            return chainedTo;
        }
        for (int i = 0; i < chainData.size(); i++) {
            CompoundNBT lassoedTag = (CompoundNBT) chainData.get(i);
            if (chained.world.isRemote() && lassoedTag.contains(CHAIN_TO_ENTITY_ID_TAG)) {
                int id = lassoedTag.getInt(CHAIN_TO_ENTITY_ID_TAG);
                if (id != -1) {
                    Entity found = chained.world.getEntityByID(id);
                    if (found != null) {
                        chainedTo.add(found);
                    } else {
                        UUID uuid = lassoedTag.getUniqueId(CHAIN_TO_TAG);
                        if (uuid != null) {
                            if (chained.world.getPlayerByUuid(uuid) != null)
                                chainedTo.add(chained.world.getPlayerByUuid(uuid));
                        }
                    }
                }
            } else if (chained.world instanceof ServerWorld) {
                UUID uuid = lassoedTag.getUniqueId(CHAIN_TO_TAG);
                if (uuid != null) {
                    Entity found = ((ServerWorld) chained.world).getEntityByUuid(uuid);
                    if (found != null) {
                        lassoedTag.putInt(CHAIN_TO_ENTITY_ID_TAG, found.getEntityId());
                        chainedTo.add(found);

                    }
                }
            }
        }
        return chainedTo;
    }

    public static void clearChainData(LivingEntity chained) {
        CompoundNBT entityData = CitadelEntityData.getOrCreateCitadelTag(chained);
        entityData.put(CHAIN_DATA, new ListNBT());
        updateData(chained, entityData);
    }

    public static boolean isChainedTo(LivingEntity chained, Entity chainedTo) {
        if (chainedTo == null) {
            return false;
        }
        return getChainedTo(chained).contains(chainedTo);
    }

    public static void tickChain(LivingEntity chained) {
        if (!chained.world.isRemote()) {
            //TODO: there must be a better way of updating this preferably only once
            updateData(chained);
        }
        List<Entity> chainedToList = getChainedTo(chained);
        for (Entity chainedOwner : chainedToList) {
            if (chainedOwner != null) {
                double distance = chained.getDistance(chainedOwner);
                if (distance > 7) {
                    double d0 = (chainedOwner.getPosX() - chained.getPosX()) / distance;
                    double d1 = (chainedOwner.getPosY() - chained.getPosY()) / distance;
                    double d2 = (chainedOwner.getPosZ() - chained.getPosZ()) / distance;
                    chained.setMotion(chained.getMotion().add(d0 * Math.abs(d0) * 0.4D, d1 * Math.abs(d1) * 0.2D, d2 * Math.abs(d2) * 0.4D));
                }
            }
        }
    }

}
