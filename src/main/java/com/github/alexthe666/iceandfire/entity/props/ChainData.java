package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChainData {
    public @Nullable List<Entity> chainedTo;

    // These lists are only for the sync (and therefor are cleared once it happened)
    private @Nullable List<Integer> chainedToIds;
    private @Nullable List<UUID> chainedToUUIDs;

    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickChain(final LivingEntity entity) {
        if (!isInitialized) {
            initialize(entity.level());
        }

        if (chainedTo == null) {
            return;
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

    public List<Entity> getChainedTo() {
        return Objects.requireNonNullElse(chainedTo, Collections.emptyList());
    }

    public void clearChains() {
        if (chainedTo == null) {
            return;
        }

        chainedTo = null;
        triggerClientUpdate = true;
    }

    public void attachChain(final Entity chain) {
        if (chainedTo == null) {
            chainedTo = new ArrayList<>();
        } else if (chainedTo.contains(chain)) {
            return;
        }

        chainedTo.add(chain);
        triggerClientUpdate = true;
    }

    public void removeChain(final Entity chain) {
        if (chainedTo == null) {
            return;
        }

        chainedTo.remove(chain);
        triggerClientUpdate = true;

        if (chainedTo.isEmpty()) {
            chainedTo = null;
        }
    }

    public boolean isChainedTo(final Entity toCheck) {
        if (chainedTo == null || chainedTo.isEmpty()) {
            return false;
        }

        return chainedTo.contains(toCheck);
    }

    public void serialize(final CompoundTag tag) {
        CompoundTag chainedData = new CompoundTag();
        ListTag uuids = new ListTag();

        if (chainedTo != null) {
            int[] ids = new int[chainedTo.size()];

            for (int i = 0; i < chainedTo.size(); i++) {
                Entity entity = chainedTo.get(i);

                ids[i] = entity.getId();
                uuids.add(NbtUtils.createUUID(entity.getUUID()));
            }

            chainedData.putIntArray("chainedToIds", ids);
            chainedData.put("chainedToUUIDs", uuids);
        }

        tag.put("chainedData", chainedData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedToIds");
        ListTag uuids = chainedData.getList("chainedToUUIDs", ListTag.TAG_INT_ARRAY);

        isInitialized = false;

        if (loadedChainedToIds.length > 0) {
            chainedToIds = new ArrayList<>();

            for (int loadedChainedToId : loadedChainedToIds) {
                chainedToIds.add(loadedChainedToId);
            }
        } else {
            chainedToIds = null;
        }

        if (!uuids.isEmpty()) {
            chainedToUUIDs = new ArrayList<>();

            for (Tag uuid : uuids) {
                chainedToUUIDs.add(NbtUtils.loadUUID(uuid));
            }
        } else {
            chainedToUUIDs = null;
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
        List<Entity> entities = new ArrayList<>();

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (chainedToUUIDs != null && level instanceof ServerLevel serverLevel) {
            for (UUID uuid : chainedToUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);

                if (entity != null) {
                    entities.add(entity);
                }
            }

            triggerClientUpdate = true;
        } else if (chainedToIds != null) {
            for (int id : chainedToIds) {
                if (id == -1) {
                    continue;
                }

                Entity entity = level.getEntity(id);

                if (entity != null) {
                    entities.add(entity);
                }
            }
        }

        if (!entities.isEmpty()) {
            chainedTo = entities;
        } else {
            chainedTo = null;
        }

        chainedToIds = null;
        chainedToUUIDs = null;
        isInitialized = true;
    }
}
