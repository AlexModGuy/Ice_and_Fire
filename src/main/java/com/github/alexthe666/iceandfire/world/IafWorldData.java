package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.TypedFeature;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IafWorldData extends SavedData {
    public enum FeatureType {
        SURFACE,
        UNDERGROUND,
        OCEAN
    }

    private static final String IDENTIFIER = IceAndFire.MODID + "_general";
    private static final Map<FeatureType, List<Pair<String, BlockPos>>> LAST_GENERATED = new HashMap<>();

    public IafWorldData() { /* Nothing to do */ }

    public IafWorldData(final CompoundTag tag) {
        this.load(tag);
    }

    public static IafWorldData get(final Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());
            DimensionDataStorage storage = overworld.getDataStorage();
            IafWorldData data = storage.computeIfAbsent(IafWorldData::new, IafWorldData::new, IDENTIFIER);
            data.setDirty();

            return data;
        }

        return null;
    }

    public boolean check(final TypedFeature feature, final BlockPos position, final String id) {
        return check(feature.getFeatureType(), position, id);
    }

    public boolean check(final FeatureType type, final BlockPos position, final String id) {
        List<Pair<String, BlockPos>> entries = LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList<>());

        boolean canGenerate = true;
        Pair<String, BlockPos> toRemove = null;

        for (Pair<String, BlockPos> entry : entries) {
            if (entry.getFirst().equals(id)) {
                toRemove = entry;
            }

            canGenerate = position.distSqr(entry.getSecond()) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
        }

        if (toRemove != null) {
            entries.remove(toRemove);
        }

        entries.add(Pair.of(id, position));

        return canGenerate;
    }

    public IafWorldData load(final CompoundTag tag) {
        FeatureType[] types = FeatureType.values();

        for (FeatureType type : types) {
            ListTag list = tag.getList(type.toString(), ListTag.TAG_COMPOUND);

            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                String id = entry.getString("id");
                BlockPos position = NbtUtils.readBlockPos(entry.getCompound("position"));
                LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList<>()).add(Pair.of(id, position));
            }
        }

        return this;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull final CompoundTag tag) {
        LAST_GENERATED.forEach((key, value) -> {
            ListTag listTag = new ListTag();

            value.forEach(entry -> {
                CompoundTag subTag = new CompoundTag();
                subTag.putString("id", entry.getFirst());
                subTag.put("position", NbtUtils.writeBlockPos(entry.getSecond()));

                listTag.add(subTag);
            });

            tag.put(key.toString(), listTag);
        });

        return tag;
    }
}
