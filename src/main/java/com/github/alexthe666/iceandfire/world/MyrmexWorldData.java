package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Iterator;
import java.util.List;

public class MyrmexWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "iceandfire_myrmex";

    private World world;
    private final List<BlockPos> villagerPositionsList = Lists.<BlockPos>newArrayList();
    private final List<MyrmexHive> villageList = Lists.<MyrmexHive>newArrayList();
    private int tickCounter;

    public MyrmexWorldData(String name) {
        super(name);
    }

    public MyrmexWorldData(World worldIn) {
        super(fileNameForProvider(worldIn.provider));
        this.world = worldIn;
        this.markDirty();
    }

    public void setWorldsForAll(World worldIn) {
        this.world = worldIn;

        for (MyrmexHive village : this.villageList) {
            village.setWorld(worldIn);
        }
    }

    public void addToVillagerPositionList(BlockPos pos) {
        if (this.villagerPositionsList.size() <= 64) {
            if (!this.positionInList(pos)) {
                this.villagerPositionsList.add(pos);
            }
        }
    }

    /**
     * Runs a single tick for the village collection
     */
    public void tick() {
        ++this.tickCounter;

        for (MyrmexHive village : this.villageList) {
            village.tick(this.tickCounter);
        }

        this.removeAnnihilatedVillages();

        if (this.tickCounter % 400 == 0) {
            this.markDirty();
        }
    }

    private void removeAnnihilatedVillages() {
        Iterator<MyrmexHive> iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            MyrmexHive village = iterator.next();

            if (village.isAnnihilated()) {
                iterator.remove();
                this.markDirty();
            }
        }
    }

    public List<MyrmexHive> getVillageList() {
        return this.villageList;
    }

    public MyrmexHive getNearestVillage(BlockPos doorBlock, int radius) {
        MyrmexHive village = null;
        double d0 = 3.4028234663852886E38D;

        for (MyrmexHive village1 : this.villageList) {
            double d1 = village1.getCenter().distanceSq(doorBlock);

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

    private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation) {
        int i = 0;

        for (int j = 1; j <= 5; ++j) {
            if (this.world.canSeeSky(centerPos.offset(direction, j))) {
                ++i;

                if (i >= limitation) {
                    return i;
                }
            }
        }

        return i;
    }

    private boolean positionInList(BlockPos pos) {
        for (BlockPos blockpos : this.villagerPositionsList) {
            if (blockpos.equals(pos)) {
                return true;
            }
        }

        return false;
    }

    private boolean isWoodDoor(BlockPos doorPos) {
        IBlockState iblockstate = this.world.getBlockState(doorPos);
        Block block = iblockstate.getBlock();

        if (block instanceof BlockDoor) {
            return iblockstate.getMaterial() == Material.WOOD;
        } else {
            return false;
        }
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglist = nbt.getTagList("Villages", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            MyrmexHive village = new MyrmexHive();
            village.readVillageDataFromNBT(nbttagcompound);
            this.villageList.add(village);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();

        for (MyrmexHive village : this.villageList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            village.writeVillageDataToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("Villages", nbttaglist);
        return compound;
    }

    public static String fileNameForProvider(WorldProvider provider) {
        return "myrmexHives" + provider.getDimensionType().getSuffix();
    }

    public static MyrmexWorldData get(World world) {
        MyrmexWorldData data = (MyrmexWorldData)world.loadData(MyrmexWorldData.class, IDENTIFIER);
        if (data == null) {
            data = new MyrmexWorldData(world);
            world.setData(IDENTIFIER, data);
        }
        return data;
    }

    public static void addHive(World world, MyrmexHive hive){
        get(world).villageList.add(hive);
    }
}
