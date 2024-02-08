package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChainData {
    public final List<Entity> chainedTo = new ArrayList<>();

    // These lists are only for the sync (and therefor are cleared once it happened)
    private final List<Integer> chainedToIds = new ArrayList<>();
    private final List<UUID> chainedToUUIDs = new ArrayList<>();

    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickChain(final LivingEntity entity) {
        if (!isInitialized) {
            initialize(entity.getLevel());
        }

        for (Entity chain : chainedTo) {
            double distance = chain.distanceTo(entity);

            if (distance > 7) {
                double x = (chain.getX() - entity.getX()) / distance;
                double y = (chain.getY() - entity.getY()) / distance;
                double z = (chain.getZ() - entity.getZ()) / distance;
                entity.setDeltaMovement(entity.getDeltaMovement().add(x * Math.abs(x) * 0.4D, y * Math.abs(y) * 0.2D, z * Math.abs(z) * 0.4D));
            }
        }
    }

    public void clearChains() {
        chainedTo.clear();
        triggerClientUpdate = true;
    }

    public void attachChain(final Entity chain) {
        if (chainedToIds.contains(chain.getId())) {
            return;
        }

        chainedTo.add(chain);
        triggerClientUpdate = true;
    }

    public void removeChain(final Entity chain) {
        chainedTo.remove(chain);
        triggerClientUpdate = true;
    }

    public boolean isChainedTo(final Entity toCheck) {
        if (chainedTo.isEmpty()) {
            return false;
        }

        return chainedTo.contains(toCheck);
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag chainedData = new CompoundTag();
        ListTag uuids = new ListTag();
        int[] ids = new int[chainedTo.size()];

        for (int i = 0; i< chainedTo.size(); i++) {
            Entity entity = chainedTo.get(i);

            ids[i] = entity.getId();
            uuids.add(NbtUtils.createUUID(entity.getUUID()));
        }

        chainedData.putIntArray("chainedToIds", ids);
        chainedData.put("chainedToUUIDs", uuids);
        tag.put("chainedData", chainedData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedToIds");
        ListTag uuids = chainedData.getList("chainedToUUIDs", ListTag.TAG_INT_ARRAY);

        isInitialized = false;
        chainedToIds.clear();
        chainedToUUIDs.clear();

        for (int loadedChainedToId : loadedChainedToIds) {
            chainedToIds.add(loadedChainedToId);
        }

        for (Tag uuid : uuids) {
            chainedToUUIDs.add(NbtUtils.loadUUID(uuid));
        }
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }

    private void initialize(final Level level) {
        chainedTo.clear();

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (level instanceof ServerLevel serverLevel) {
            for (UUID uuid : chainedToUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);

                if (entity != null) {
                    chainedTo.add(entity);
                }
            }

            chainedToUUIDs.clear();
            triggerClientUpdate = true;
        } else {
            for (int id : chainedToIds) {
                if (id == -1) {
                    continue;
                }

                Entity entity = level.getEntity(id);

                if (entity != null) {
                    chainedTo.add(entity);
                }
            }
        }

        chainedToIds.clear();
        isInitialized = true;
    }
}
