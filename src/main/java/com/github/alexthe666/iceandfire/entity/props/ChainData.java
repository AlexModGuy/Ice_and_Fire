package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ChainData {
    public final List<Entity> chainedTo = new ArrayList<>();

    private final List<Integer> chainedToIds = new ArrayList<>();
    private boolean isInitialized;

    public boolean tickChain(final LivingEntity entity) {
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

        return false;
    }

    public void clearChains(final Entity capabilityHolder) {
        chainedTo.clear();
        chainedToIds.clear();
        CapabilityHandler.syncEntityData(capabilityHolder);
    }

    public void attachChain(final Entity capabilityHolder, final Entity chain) {
        chainedTo.add(chain);
        chainedToIds.add(chain.getId());
        CapabilityHandler.syncEntityData(capabilityHolder);
    }

    public void removeChain(final Entity capabilityHolder, final Entity chain) {
        chainedToIds.remove(chain.getId());
        chainedTo.remove(chain);
        CapabilityHandler.syncEntityData(capabilityHolder);
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

    public void serialize(final CompoundTag tag) {
        CompoundTag chainedData = new CompoundTag();
        chainedData.putIntArray("chainedTo", chainedToIds);

        tag.put("chainedData", chainedData);
    }

    public void deserialize(final CompoundTag tag) {
        CompoundTag chainedData = tag.getCompound("chainedData");
        ListTag chains = chainedData.getList("chainedData", ListTag.TAG_INT_ARRAY);

        chainedTo.clear();
        isInitialized = false;

        for (int i = 0; i < chains.size(); i++) {
            chainedToIds.add(chains.getInt(i));
        }
    }

    private void initializeChainedTo(final Level level) {
        for (int id : chainedToIds) {
            Entity entity = level.getEntity(id);

            if (entity != null) {
                chainedTo.add(entity);
            }
        }

        isInitialized = true;
    }
}
