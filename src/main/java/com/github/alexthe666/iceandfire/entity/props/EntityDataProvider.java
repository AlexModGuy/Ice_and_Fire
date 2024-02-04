package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EntityDataProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Map<Integer, LazyOptional<EntityData>> SERVER_CACHE = new HashMap<>();
    public static final Map<Integer, LazyOptional<EntityData>> CLIENT_CACHE = new HashMap<>();

    private final EntityData data = new EntityData();
    private final LazyOptional<EntityData> instance = LazyOptional.of(() -> data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull final Capability<T> capability, @Nullable final Direction side) {
        return capability == CapabilityHandler.ENTITY_DATA_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(final CompoundTag tag) {
        instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).deserialize(tag);
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).serialize();
    }

    public static LazyOptional<EntityData> getCapability(final Entity entity) {
        if (entity instanceof LivingEntity) {
            int key = entity.getId();

            Map<Integer, LazyOptional<EntityData>> sidedCache = entity.level().isClientSide() ? CLIENT_CACHE : SERVER_CACHE;
            LazyOptional<EntityData> capability = sidedCache.get(key);

            if (capability == null) {
                capability = entity.getCapability(CapabilityHandler.ENTITY_DATA_CAPABILITY);
                capability.addListener(ignored -> sidedCache.remove(key));

                if (capability.isPresent()) {
                    sidedCache.put(key, capability);
                }
            }

            return capability;
        }

        return LazyOptional.empty();
    }

    public static void removeCachedEntry(final Entity entity) {
        if (entity instanceof LivingEntity) {
            int key = entity.getId();

            if (entity.level().isClientSide()) {
                if (entity == CapabilityHandler.getLocalPlayer()) {
                    // Can trigger on death or when player leaves the game (this is when we want to actually clear)
                    CLIENT_CACHE.clear();
                } else {
                    CLIENT_CACHE.remove(key);
                }
            } else {
                SERVER_CACHE.remove(key);
            }
        }
    }
}
