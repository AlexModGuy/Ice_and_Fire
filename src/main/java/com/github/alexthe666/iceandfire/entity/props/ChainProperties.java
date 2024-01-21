package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChainProperties {

    private static final String CHAIN_TO_TAG = "ChainOwnerIaf";
    private static final String CHAIN_TO_ENTITY_ID_TAG = "ChainOwnerIDIaf";
    private static final String CHAIN_DATA = "ChainDataIaf";

    // FIXME: All of these hashmap optimizations are temporary to resolve performance issues, ideally we create a different system
    private static HashMap<CompoundTag, Boolean> containsChainData = new HashMap<>();
    public static void attachChain(LivingEntity chained, Entity chainedTo) {
        if (isChainedTo(chained, chainedTo)) {
            return;
        }
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(chained);
        ListTag chainData = getOrCreateChainData(entityData);
        CompoundTag currentChainData = new CompoundTag();
        currentChainData.putUUID(CHAIN_TO_TAG, chainedTo.getUUID());
        currentChainData.putInt(CHAIN_TO_ENTITY_ID_TAG, chainedTo.getId());

        chainData.add(currentChainData);
        entityData.put(CHAIN_DATA, chainData);
        updateData(chained, entityData);
    }

    public static CompoundTag getConnectedEntityChainData(LivingEntity host, Entity target) {
        ListTag chainData = getOrCreateChainData(host);
        if (chainData.isEmpty())
            return null;
        return getConnectedEntityChainData(chainData, target);
    }

    @Nullable
    public static CompoundTag getConnectedEntityChainData(ListTag chainData, Entity entity) {
        for (int i = 0; i < chainData.size(); i++) {
            CompoundTag nbt = (CompoundTag) chainData.get(i);
            if (nbt.contains(CHAIN_TO_TAG) && nbt.getUUID(CHAIN_TO_TAG).equals(entity.getUUID()))
                return nbt;
        }
        return null;
    }

    public static boolean hasChainData(LivingEntity entity) {
        return !getOrCreateChainData(entity).isEmpty();
    }

    public static boolean hasEntityData(ListTag chainData, Entity entity) {
        return getConnectedEntityChainData(chainData, entity) != null;
    }

    private static ListTag getOrCreateChainData(LivingEntity entity) {
        return getOrCreateChainData(CitadelEntityData.getOrCreateCitadelTag(entity));
    }

    private static ListTag getOrCreateChainData(CompoundTag entityData) {
        //TODO: Look at type
        if (containsChainData.containsKey(entityData) && containsChainData.getOrDefault(entityData, false) && entityData.contains(CHAIN_DATA, 9)) {
            return entityData.getList(CHAIN_DATA, 10);
        }
        else if (entityData.contains(CHAIN_DATA, 9)) {
            containsChainData.put(entityData, true);
            return entityData.getList(CHAIN_DATA, 10);
        } else {
            containsChainData.put(entityData, false);
            return new ListTag();
        }
    }

    public static void updateData(LivingEntity entity) {
        updateData(entity, CitadelEntityData.getOrCreateCitadelTag(entity));
    }

    private static void updateData(LivingEntity entity, CompoundTag nbt) {
        CitadelEntityData.setCitadelTag(entity, nbt);
        if (!entity.level.isClientSide()) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", nbt, entity.getId()));
        }
    }

    public static void removeChain(LivingEntity entity, Entity connectedTo) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(entity);
        ListTag chainData = getOrCreateChainData(entityData);
        int dataIndex = -1;
        for (int i = 0; i < chainData.size(); i++) {
            CompoundTag nbt = (CompoundTag) chainData.get(i);
            if (nbt.contains(CHAIN_TO_TAG) && nbt.getUUID(CHAIN_TO_TAG).equals(connectedTo.getUUID())) {
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
        ListTag chainData = getOrCreateChainData(chained);
        List<Entity> chainedTo = new ArrayList<>();
        if (chainData.isEmpty()) {
            return chainedTo;
        }
        for (int i = 0; i < chainData.size(); i++) {
            CompoundTag lassoedTag = (CompoundTag) chainData.get(i);
            if (chained.level.isClientSide() && lassoedTag.contains(CHAIN_TO_ENTITY_ID_TAG)) {
                int id = lassoedTag.getInt(CHAIN_TO_ENTITY_ID_TAG);
                if (id != -1) {
                    Entity found = chained.level.getEntity(id);
                    if (found != null) {
                        chainedTo.add(found);
                    } else {
                        UUID uuid = lassoedTag.getUUID(CHAIN_TO_TAG);
                        if (uuid != null) {
                            if (chained.level.getPlayerByUUID(uuid) != null)
                                chainedTo.add(chained.level.getPlayerByUUID(uuid));
                        }
                    }
                }
            } else if (chained.level instanceof ServerLevel) {
                UUID uuid = lassoedTag.getUUID(CHAIN_TO_TAG);
                if (uuid != null) {
                    Entity found = ((ServerLevel) chained.level).getEntity(uuid);
                    if (found != null) {
                        lassoedTag.putInt(CHAIN_TO_ENTITY_ID_TAG, found.getId());
                        chainedTo.add(found);

                    }
                }
            }
        }
        return chainedTo;
    }

    public static void clearChainData(LivingEntity chained) {
        CompoundTag entityData = CitadelEntityData.getOrCreateCitadelTag(chained);
        entityData.put(CHAIN_DATA, new ListTag());
        updateData(chained, entityData);
    }

    public static boolean isChainedTo(LivingEntity chained, Entity chainedTo) {
        if (chainedTo == null) {
            return false;
        }
        return getChainedTo(chained).contains(chainedTo);
    }

    public static void tickChain(LivingEntity chained) {
        List<Entity> chainedToList = getChainedTo(chained);
        for (Entity chainedOwner : chainedToList) {
            if (chainedOwner != null) {
                double distance = chained.distanceTo(chainedOwner);
                if (distance > 7) {
                    double d0 = (chainedOwner.getX() - chained.getX()) / distance;
                    double d1 = (chainedOwner.getY() - chained.getY()) / distance;
                    double d2 = (chainedOwner.getZ() - chained.getZ()) / distance;
                    chained.setDeltaMovement(chained.getDeltaMovement().add(d0 * Math.abs(d0) * 0.4D, d1 * Math.abs(d1) * 0.2D, d2 * Math.abs(d2) * 0.4D));
                }
            }
        }
    }

}
