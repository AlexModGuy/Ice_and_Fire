package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DragonPosWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "iceandfire_dragonPositions";
    protected final Map<UUID, BlockPos> lastDragonPositions = new HashMap<>();
    private World world;
    private int tickCounter;

    public DragonPosWorldData() {
        super(IDENTIFIER);
    }

    public DragonPosWorldData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.setDirty();
    }

    public static DragonPosWorldData get(World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getLevel(world.dimension());

            DimensionSavedDataManager storage = overworld.getDataStorage();
            DragonPosWorldData data = storage.computeIfAbsent(DragonPosWorldData::new, IDENTIFIER);
            if (data != null) {
                data.world = world;
                data.setDirty();
            }
            return data;
        }
        return null;
    }

    public void addDragon(UUID uuid, BlockPos pos) {
        lastDragonPositions.put(uuid, pos);
        this.setDirty();
    }

    public void removeDragon(UUID uuid) {
        lastDragonPositions.remove(uuid);
        this.setDirty();
    }

    public BlockPos getDragonPos(UUID uuid) {
        return lastDragonPositions.get(uuid);
    }

    public void debug() {
        IceAndFire.LOGGER.warn(lastDragonPositions.toString());
    }


    public void tick() {
        ++this.tickCounter;
    }

    public void load(CompoundNBT nbt) {
        this.tickCounter = nbt.getInt("Tick");
        ListNBT nbttaglist = nbt.getList("DragonMap", 10);
        this.lastDragonPositions.clear();
        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundNBT CompoundNBT = nbttaglist.getCompound(i);
            UUID uuid = CompoundNBT.getUUID("DragonUUID");
            BlockPos pos = new BlockPos(CompoundNBT.getInt("DragonPosX"), CompoundNBT.getInt("DragonPosY"), CompoundNBT.getInt("DragonPosZ"));
            this.lastDragonPositions.put(uuid, pos);
        }
    }

    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("Tick", this.tickCounter);
        ListNBT nbttaglist = new ListNBT();
        for (Map.Entry<UUID, BlockPos> pair : lastDragonPositions.entrySet()) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putUUID("DragonUUID", pair.getKey());
            CompoundNBT.putInt("DragonPosX", pair.getValue().getX());
            CompoundNBT.putInt("DragonPosY", pair.getValue().getY());
            CompoundNBT.putInt("DragonPosZ", pair.getValue().getZ());
            nbttaglist.add(CompoundNBT);
        }
        compound.put("DragonMap", nbttaglist);
        return compound;
    }
}
