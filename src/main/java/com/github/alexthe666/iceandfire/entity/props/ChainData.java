package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainData {
    private final List<Entity> chainedTo = new ArrayList<>();
    private final List<Integer> chainedToIds = new ArrayList<>();
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
        chainedData.putIntArray("chainedTo", chainedToIds);

        tag.put("chainedData", chainedData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedTo");

        isInitialized = false;
        chainedToIds.clear();

        for (int i = 0; i < loadedChainedToIds.length; i++) {
            chainedToIds.add(loadedChainedToIds[i]);
        }
    }

    private void initializeChainedTo(final Level level) {
        chainedTo.clear();

        for (int id : chainedToIds) {
            Entity entity = level.getEntity(id);

            if (entity != null) {
                chainedTo.add(entity);
            } else {
                System.out.println("entity was not null [" + id + "]");
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
