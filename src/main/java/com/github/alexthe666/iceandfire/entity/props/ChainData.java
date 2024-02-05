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
    private final List<Entity> chainedTo = new ArrayList<>();
    private final List<Integer> chainedToIds = new ArrayList<>();
    private final List<UUID> chainedToUUIDs = new ArrayList<>();
    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickChain(final LivingEntity entity) {
        if (!isInitialized) {
            initializeChainedTo(entity.getLevel());
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
        chainedToIds.clear();
        triggerClientUpdate = true;
    }

    public void attachChain(final Entity chain) {
        if (chainedToIds.contains(chain.getId())) {
            return;
        }

        chainedTo.add(chain);
        chainedToIds.add(chain.getId());
        triggerClientUpdate = true;
    }

    public void removeChain(final Entity chain) {
        chainedToIds.remove(Integer.valueOf(chain.getId())); // remove by value not by index
        chainedTo.remove(chain);
        triggerClientUpdate = true;
    }

    public boolean isChainedTo(final Entity toCheck) {
        if (chainedTo.isEmpty()) {
            return false;
        }

        for (Entity chain : chainedTo) {
            if (chain.getUUID().equals(toCheck.getUUID())) {
                return true;
            }
        }

        return false;
    }

    public List<Entity> getChainedTo() {
        /* TODO
            There was a case of ConcurrentModificationException
            (likely due to the packet not being enqueued initially)
            Keeping this for now in case we need to return a copy of the list instead
        */
        return chainedTo;
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag chainedData = new CompoundTag();
        chainedData.putIntArray("chainedToIds", chainedToIds);

        ListTag uuidList = new ListTag();

        for (Entity entity : chainedTo) {
            uuidList.add(NbtUtils.createUUID(entity.getUUID()));
        }

        chainedData.put("chainedToUUIDs", uuidList);
        tag.put("chainedData", chainedData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedToIds");

        isInitialized = false;
        chainedToIds.clear();
        chainedToUUIDs.clear();

        for (int loadedChainedToId : loadedChainedToIds) {
            chainedToIds.add(loadedChainedToId);
        }

        ListTag uuids = chainedData.getList("chainedToUUIDs", ListTag.TAG_INT_ARRAY);

        for (Tag uuid : uuids) {
            chainedToUUIDs.add(NbtUtils.loadUUID(uuid));
        }
    }

    private void initializeChainedTo(final Level level) {
        chainedTo.clear();

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (level instanceof ServerLevel serverLevel) {
            chainedToIds.clear();

            for (UUID uuid : chainedToUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);

                if (entity != null) {
                    chainedTo.add(entity);
                    chainedToIds.add(entity.getId());
                }
            }

            triggerClientUpdate = true;
        } else {
            for (int id : chainedToIds) {
                Entity entity = level.getEntity(id);

                if (entity != null) {
                    chainedTo.add(entity);
                }
            }
        }

        isInitialized = true;
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }
}
