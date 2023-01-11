package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MyrmexWorldData extends SavedData {

    private static final String IDENTIFIER = "iceandfire_myrmex";
    private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
    private final List<MyrmexHive> hiveList = Lists.newArrayList();
    private Level world;
    private int tickCounter;

    public MyrmexWorldData() {
    }

    public MyrmexWorldData(Level world) {
        this.world = world;
        this.setDirty();
    }

    public MyrmexWorldData(CompoundTag compoundTag) {
        this.load(compoundTag);
    }

    public static MyrmexWorldData get(Level world) {
        if (world instanceof ServerLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());

            DimensionDataStorage storage = overworld.getDataStorage();
            MyrmexWorldData data = storage.computeIfAbsent(MyrmexWorldData::new, MyrmexWorldData::new, IDENTIFIER);
            if (data != null) {
                data.world = world;
                data.setDirty();
            }
            return data;
        }
        //If the world is ClientLevel just return empty non significant data object
        return new MyrmexWorldData();
    }

    public static void addHive(Level world, MyrmexHive hive) {
        get(world).hiveList.add(hive);
    }

    public void setWorldsForAll(Level worldIn) {
        this.world = worldIn;
        for (MyrmexHive village : this.hiveList) {
            village.setWorld(worldIn);
        }
    }

    public void tick() {
        ++this.tickCounter;
        for (MyrmexHive hive : this.hiveList) {
            hive.tick(this.tickCounter, world);
        }
        //this.removeAnnihilatedHives();

    }

    private void removeAnnihilatedHives() {
        Iterator<MyrmexHive> iterator = this.hiveList.iterator();

        while (iterator.hasNext()) {
            MyrmexHive village = iterator.next();

            if (village.isAnnihilated()) {
                iterator.remove();
                this.setDirty();
            }
        }
    }

    public List<MyrmexHive> getHivelist() {
        return this.hiveList;
    }

    public MyrmexHive getNearestHive(BlockPos doorBlock, int radius) {
        MyrmexHive village = null;
        double d0 = 3.4028234663852886E38D;

        for (MyrmexHive village1 : this.hiveList) {
            double d1 = village1.getCenter().distSqr(doorBlock);

            if (d1 < d0) {
                float f = (float) (radius + village1.getVillageRadius());

                if (d1 <= (double) (f * f)) {
                    village = village1;
                    d0 = d1;
                }
            }
        }

        return village;
    }

    private boolean positionInList(BlockPos pos) {
        for (BlockPos blockpos : this.villagerPositionsList) {
            if (blockpos.equals(pos)) {
                return true;
            }
        }

        return false;
    }

    public void debug() {
        for (MyrmexHive hive : this.hiveList) {
            IceAndFire.LOGGER.warn(hive.toString());
        }
    }

    public void load(CompoundTag nbt) {
        this.tickCounter = nbt.getInt("Tick");
        ListTag nbttaglist = nbt.getList("Hives", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundTag CompoundNBT = nbttaglist.getCompound(i);
            MyrmexHive village = new MyrmexHive();
            village.readVillageDataFromNBT(CompoundNBT);
            this.hiveList.add(village);
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compound) {
        compound.putInt("Tick", this.tickCounter);
        ListTag nbttaglist = new ListTag();

        for (MyrmexHive village : this.hiveList) {
            CompoundTag CompoundNBT = new CompoundTag();
            village.writeVillageDataToNBT(CompoundNBT);
            nbttaglist.add(CompoundNBT);
        }

        compound.put("Hives", nbttaglist);
        return compound;
    }

    public MyrmexHive getHiveFromUUID(UUID id) {
        for (MyrmexHive hive : this.hiveList) {
            if (hive.hiveUUID != null && hive.hiveUUID.equals(id)) {
                return hive;
            }
        }
        return null;
    }
}
