package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DreadWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "iceandfire_dread";
    private final List<BlockPos> allOverworldPortalLocations = Lists.newArrayList();
    private final List<BlockPos> activeOverworldPortalLocations = Lists.newArrayList();
    private World world;
    private int tickCounter;

    public DreadWorldData(String name) {
        super(name);
    }

    public DreadWorldData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.markDirty();
    }

    public static DreadWorldData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        DreadWorldData instance = (DreadWorldData) storage.getOrLoadData(DreadWorldData.class, IDENTIFIER);

        if (instance == null) {
            instance = new DreadWorldData(world);
            storage.setData(IDENTIFIER, instance);
        }
        instance.markDirty();
        return instance;
    }


    public void setWorldsForAll(World worldIn) {
        this.world = worldIn;
    }

    public void tick() {
        ++this.tickCounter;
        //this.removeAnnihilatedHives();
    }

    public void removePortalAtPos(BlockPos pos) {
        Iterator<BlockPos> iterator = this.allOverworldPortalLocations.iterator();
        while (iterator.hasNext()) {
            BlockPos portalPos = iterator.next();
            if (portalPos.equals(pos)) {
                iterator.remove();
                this.markDirty();
            }
        }
        Iterator<BlockPos> iterator2 = this.activeOverworldPortalLocations.iterator();
        while (iterator2.hasNext()) {
            BlockPos portalPos = iterator2.next();
            if (portalPos.equals(pos)) {
                iterator.remove();
                this.markDirty();
            }
        }
    }

    public void addPortalLocation(BlockPos pos){
        if(!this.allOverworldPortalLocations.contains(pos)){
            this.allOverworldPortalLocations.add(pos);
        }
    }

    public void activatePortalAt(BlockPos pos){
        if(!this.allOverworldPortalLocations.contains(pos)){
            this.allOverworldPortalLocations.add(pos);
        }
        if(!this.activeOverworldPortalLocations.contains(pos)){
            this.activeOverworldPortalLocations.add(pos);
        }
    }


    public List<BlockPos> getAllActivePortals() {
        return this.allOverworldPortalLocations;
    }

    public List<BlockPos> getAllPortals() {
        return this.activeOverworldPortalLocations;
    }

    public BlockPos getExitPortalFromDreadlands(BlockPos queenLoc) {
        BlockPos pos = null;
        double d0 = 3.4028234663852886E38D;

        for (BlockPos activePosLoc : this.activeOverworldPortalLocations) {
            double d1 = activePosLoc.distanceSq(queenLoc);
            if (d1 < d0) {
                pos = activePosLoc;
                d0 = d1;
            }
        }
        return pos;
    }

    public void debug() {
        for (BlockPos pos : this.activeOverworldPortalLocations) {
            IceAndFire.logger.debug(pos);
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglistAll = nbt.getTagList("AllPortals", 10);
        for (int i = 0; i < nbttaglistAll.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglistAll.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z"));
            this.allOverworldPortalLocations.add(pos);
        }
        NBTTagList nbttaglistActive = nbt.getTagList("ActivePortals", 10);
        for (int i = 0; i < nbttaglistActive.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglistActive.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z"));
            this.activeOverworldPortalLocations.add(pos);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglistAll = new NBTTagList();
        for (BlockPos pos : this.allOverworldPortalLocations) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", pos.getX());
            nbttagcompound.setInteger("Y", pos.getY());
            nbttagcompound.setInteger("Z", pos.getZ());
            nbttaglistAll.appendTag(nbttagcompound);
        }
        compound.setTag("AllPortals", nbttaglistAll);

        NBTTagList nbttaglistActive = new NBTTagList();
        for (BlockPos pos : this.activeOverworldPortalLocations) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", pos.getX());
            nbttagcompound.setInteger("Y", pos.getY());
            nbttagcompound.setInteger("Z", pos.getZ());
            nbttaglistActive.appendTag(nbttagcompound);
        }
        compound.setTag("ActivePortals", nbttaglistActive);

        return compound;
    }
}
