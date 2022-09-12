package com.github.alexthe666.iceandfire.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class IafWorldData extends SavedData {

    private static final String IDENTIFIER = "iceandfire_general";
    protected BlockPos lastGeneratedDangerousStructure = BlockPos.ZERO;
    private Level world;
    private int tickCounter;

    public IafWorldData() {
    }

    public IafWorldData(Level world) {
        this.world = world;
        this.setDirty();
    }

    public IafWorldData(CompoundTag compoundTag) {
        this.load(compoundTag);
    }


    public static IafWorldData get(Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());

            DimensionDataStorage storage = overworld.getDataStorage();
            IafWorldData data = storage.computeIfAbsent(IafWorldData::new, IafWorldData::new, IDENTIFIER);
            if (data != null) {
                data.world = world;
                data.setDirty();
            }
            return data;
        }
        return null;
    }

    public void setLastGeneratedDangerousStructure(BlockPos pos) {
        lastGeneratedDangerousStructure = pos;
        this.setDirty();
    }


    public BlockPos getLastGeneratedDangerousStructure() {
        return lastGeneratedDangerousStructure;
    }


    public void tick() {
        ++this.tickCounter;
    }

    public IafWorldData load(CompoundTag nbt) {
        this.tickCounter = nbt.getInt("Tick");
        lastGeneratedDangerousStructure = new BlockPos(nbt.getInt("LastX"), nbt.getInt("LastY"), nbt.getInt("LastZ"));
        return this;
    }

    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Tick", this.tickCounter);
        compound.putInt("LastX", lastGeneratedDangerousStructure.getX());
        compound.putInt("LastY", lastGeneratedDangerousStructure.getY());
        compound.putInt("LastZ", lastGeneratedDangerousStructure.getZ());
        return compound;
    }
}
