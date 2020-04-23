package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class MyrmexHive {
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
    private final List<BlockPos> foodRooms = Lists.newArrayList();
    private final List<BlockPos> babyRooms = Lists.newArrayList();
    private final List<BlockPos> miscRooms = Lists.newArrayList();
    private final List<BlockPos> allRooms = Lists.newArrayList();
    private final Map<BlockPos, EnumFacing> entrances = Maps.newHashMap();
    private final Map<BlockPos, EnumFacing> entranceBottoms = Maps.newHashMap();
    private final Map<UUID, Integer> playerReputation = Maps.newHashMap();
    private final List<HiveAggressor> villageAgressors = Lists.newArrayList();
    private final List<UUID> myrmexList = Lists.newArrayList();
    public UUID hiveUUID;
    public String colonyName = "";
    public boolean reproduces = true;
    public boolean hasOwner = false;
    public UUID ownerUUID = null;
    private World world;
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numMyrmex;
    private int noBreedTicks;

    public MyrmexHive() {
        this.hiveUUID = UUID.randomUUID();
    }

    public MyrmexHive(World worldIn) {
        this.world = worldIn;
        this.hiveUUID = UUID.randomUUID();
    }

    public MyrmexHive(World worldIn, BlockPos center, int radius) {
        this.world = worldIn;
        this.center = center;
        this.villageRadius = radius;
        this.hiveUUID = UUID.randomUUID();
    }

    public static BlockPos getGroundedPos(World world, BlockPos pos) {
        BlockPos current = pos;
        while (world.isAirBlock(current.down()) && current.getY() > 0) {
            current = current.down();
        }
        return current;
    }

    public void setWorld(World worldIn) {
        this.world = worldIn;
    }

    public void tick(int tickCounterIn, World world) {
        this.tickCounter++;
        this.removeDeadAndOldAgressors();
        if (tickCounter % 20 == 0) {
            this.updateNumMyrmex(world);
        }
    }

    private void updateNumMyrmex(World world) {
        this.numMyrmex = this.myrmexList.size();
        if (this.numMyrmex == 0) {
            this.playerReputation.clear();
        }
    }

    public EntityMyrmexQueen getQueen() {
        List<EntityMyrmexQueen> allQueens = world.getEntities(EntityMyrmexQueen.class, EntitySelectors.NOT_SPECTATING);
        List<EntityMyrmexQueen> ourQueens = new ArrayList<>();
        for (EntityMyrmexQueen queen : allQueens) {
            if (queen.getHive().equals(this)) {
                ourQueens.add(queen);
            }
        }
        return ourQueens.isEmpty() ? null : ourQueens.get(0);
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public BlockPos getCenterGround() {
        return getGroundedPos(this.world, this.center);
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public int getNumMyrmex() {
        return this.numMyrmex;
    }

    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos) {
        return this.center.distanceSq(pos) < (double) (this.villageRadius * this.villageRadius);
    }

    public boolean isAnnihilated() {
        return false;
    }

    public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn, int agressiveLevel) {
        for (HiveAggressor hive$villageaggressor : this.villageAgressors) {
            if (hive$villageaggressor.agressor == entitylivingbaseIn) {
                hive$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }

        this.villageAgressors.add(new HiveAggressor(entitylivingbaseIn, this.tickCounter, agressiveLevel));
    }

    @Nullable
    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
        double d0 = Double.MAX_VALUE;
        int previousAgressionLevel = 0;
        HiveAggressor hive$villageaggressor = null;
        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            HiveAggressor hive$villageaggressor1 = this.villageAgressors.get(i);
            double d1 = hive$villageaggressor1.agressor.getDistanceSq(entitylivingbaseIn);
            int agressionLevel = hive$villageaggressor1.agressionLevel;

            if (d1 <= d0 || agressionLevel > previousAgressionLevel) {
                hive$villageaggressor = hive$villageaggressor1;
                d0 = d1;
            }
            previousAgressionLevel = agressionLevel;
        }

        return hive$villageaggressor == null ? null : hive$villageaggressor.agressor;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender, World world) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;

        for (UUID s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationTooLowToFight(s)) {
                EntityPlayer entityplayer1 = world.getPlayerEntityByUUID(s);

                if (entityplayer1 != null) {
                    double d1 = entityplayer1.getDistanceSq(villageDefender);

                    if (d1 <= d0) {
                        entityplayer = entityplayer1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityplayer;
    }

    private void removeDeadAndOldAgressors() {
        Iterator<HiveAggressor> iterator = this.villageAgressors.iterator();

        while (iterator.hasNext()) {
            HiveAggressor hive$villageaggressor = iterator.next();

            if (!hive$villageaggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - hive$villageaggressor.agressionTime) > 300) {
                iterator.remove();
            }
        }
    }

    public int getPlayerReputation(UUID playerName) {
        Integer integer = this.playerReputation.get(playerName);
        return integer == null ? 0 : integer.intValue();
    }

    private UUID findUUID(String name) {
        if (this.world == null || this.world.getMinecraftServer() == null)
            return EntityPlayer.getOfflineUUID(name);
        GameProfile profile = this.world.getMinecraftServer().getPlayerProfileCache().getGameProfileForUsername(name);
        return profile == null ? EntityPlayer.getOfflineUUID(name) : profile.getId();
    }

    public int modifyPlayerReputation(UUID playerName, int reputation) {
        int i = this.getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, 0, 100);
        if (this.hasOwner && playerName.equals(ownerUUID)) {
            j = 100;
        }
        EntityPlayer player = null;
        try {
            player = world.getPlayerEntityByUUID(playerName);
        } catch (Exception e) {
            IceAndFire.logger.warn("Myrmex Hive could not find player with associated UUID");
        }
        if (player != null) {
            if (j - i != 0) {
                player.sendStatusMessage(new TextComponentTranslation(j - i >= 0 ? "myrmex.message.raised_reputation" : "myrmex.message.lowered_reputation", Math.abs(j - i), j), true);
            }
            if (i < 25 && j >= 25) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.peaceful"), false);
            }
            if (i >= 25 && j < 25) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.hostile"), false);
            }
            if (i < 50 && j >= 50) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.trade"), false);
            }
            if (i >= 50 && j < 50) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.no_trade"), false);
            }
            if (i < 75 && j >= 75) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.can_use_staff"), false);
            }
            if (i >= 75 && j < 75) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.cant_use_staff"), false);
            }
        }

        this.playerReputation.put(playerName, Integer.valueOf(j));
        return j;
    }

    public boolean isPlayerReputationTooLowToTrade(UUID uuid) {
        return this.getPlayerReputation(uuid) < 50;
    }

    public boolean canPlayerCommandHive(UUID uuid) {
        return this.getPlayerReputation(uuid) >= 75;
    }

    public boolean isPlayerReputationTooLowToFight(UUID uuid) {
        return this.getPlayerReputation(uuid) < 25;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound compound) {
        this.numMyrmex = compound.getInteger("PopSize");
        this.reproduces = compound.getBoolean("Reproduces");
        this.hasOwner = compound.getBoolean("HasOwner");
        this.ownerUUID = compound.getUniqueId("OwnerUUID");
        this.colonyName = compound.getString("ColonyName");
        this.villageRadius = compound.getInteger("Radius");
        this.lastAddDoorTimestamp = compound.getInteger("Stable");
        this.tickCounter = compound.getInteger("Tick");
        this.noBreedTicks = compound.getInteger("MTick");
        this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
        this.centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
        NBTTagList nbttaglist = compound.getTagList("Doors", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }
        NBTTagList hiveMembers = compound.getTagList("HiveMembers", 10);
        this.myrmexList.clear();
        for (int i = 0; i < hiveMembers.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = hiveMembers.getCompoundTagAt(i);
            this.myrmexList.add(nbttagcompound.getUniqueId("MyrmexUUID"));
        }
        NBTTagList foodRoomList = compound.getTagList("FoodRooms", 10);
        this.foodRooms.clear();
        for (int i = 0; i < foodRoomList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = foodRoomList.getCompoundTagAt(i);
            this.foodRooms.add(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")));
        }
        NBTTagList babyRoomList = compound.getTagList("BabyRooms", 10);
        this.babyRooms.clear();
        for (int i = 0; i < babyRoomList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = babyRoomList.getCompoundTagAt(i);
            this.babyRooms.add(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")));
        }
        NBTTagList miscRoomList = compound.getTagList("MiscRooms", 10);
        this.miscRooms.clear();
        for (int i = 0; i < miscRoomList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = miscRoomList.getCompoundTagAt(i);
            this.miscRooms.add(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")));
        }
        NBTTagList entrancesList = compound.getTagList("Entrances", 10);
        this.entrances.clear();
        for (int i = 0; i < entrancesList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = entrancesList.getCompoundTagAt(i);
            this.entrances.put(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), EnumFacing.byHorizontalIndex(nbttagcompound.getInteger("Facing")));
        }

        NBTTagList entranceBottomsList = compound.getTagList("EntranceBottoms", 10);
        this.entranceBottoms.clear();
        for (int i = 0; i < entranceBottomsList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = entranceBottomsList.getCompoundTagAt(i);
            this.entranceBottoms.put(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), EnumFacing.byHorizontalIndex(nbttagcompound.getInteger("Facing")));
        }
        hiveUUID = compound.getUniqueId("HiveUUID");
        NBTTagList nbttaglist1 = compound.getTagList("Players", 10);
        for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);

            if (nbttagcompound1.hasKey("UUID")) {
                this.playerReputation.put(UUID.fromString(nbttagcompound1.getString("UUID")), Integer.valueOf(nbttagcompound1.getInteger("S")));
            } else {
                //World is never set here, so this will always be offline UUIDs, sadly there is no way to convert this.
                this.playerReputation.put(findUUID(nbttagcompound1.getString("Name")), Integer.valueOf(nbttagcompound1.getInteger("S")));
            }
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NBTTagCompound compound) {
        compound.setInteger("PopSize", this.numMyrmex);
        compound.setBoolean("Reproduces", this.reproduces);
        compound.setBoolean("HasOwner", this.hasOwner);
        if (this.ownerUUID != null) {
            compound.setUniqueId("OwnerUUID", this.ownerUUID);
        }
        compound.setString("ColonyName", this.colonyName);
        compound.setInteger("Radius", this.villageRadius);
        compound.setInteger("Stable", this.lastAddDoorTimestamp);
        compound.setInteger("Tick", this.tickCounter);
        compound.setInteger("MTick", this.noBreedTicks);
        compound.setInteger("CX", this.center.getX());
        compound.setInteger("CY", this.center.getY());
        compound.setInteger("CZ", this.center.getZ());
        compound.setInteger("ACX", this.centerHelper.getX());
        compound.setInteger("ACY", this.centerHelper.getY());
        compound.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();
        for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getLastActivityTimestamp());
            nbttaglist.appendTag(nbttagcompound);
        }
        NBTTagList hiveMembers = new NBTTagList();
        for (UUID memberUUID : this.myrmexList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setUniqueId("MyrmexUUID", memberUUID);
            hiveMembers.appendTag(nbttagcompound);
        }
        compound.setTag("HiveMembers", hiveMembers);
        NBTTagList foodRoomList = new NBTTagList();
        for (BlockPos pos : this.foodRooms) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", pos.getX());
            nbttagcompound.setInteger("Y", pos.getY());
            nbttagcompound.setInteger("Z", pos.getZ());
            foodRoomList.appendTag(nbttagcompound);
        }
        compound.setTag("FoodRooms", foodRoomList);
        NBTTagList babyRoomList = new NBTTagList();
        for (BlockPos pos : this.babyRooms) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", pos.getX());
            nbttagcompound.setInteger("Y", pos.getY());
            nbttagcompound.setInteger("Z", pos.getZ());
            babyRoomList.appendTag(nbttagcompound);
        }
        compound.setTag("BabyRooms", babyRoomList);
        NBTTagList miscRoomList = new NBTTagList();
        for (BlockPos pos : this.miscRooms) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", pos.getX());
            nbttagcompound.setInteger("Y", pos.getY());
            nbttagcompound.setInteger("Z", pos.getZ());
            miscRoomList.appendTag(nbttagcompound);
        }
        compound.setTag("MiscRooms", miscRoomList);
        NBTTagList entrancesList = new NBTTagList();
        for (Map.Entry<BlockPos, EnumFacing> entry : this.entrances.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", entry.getKey().getX());
            nbttagcompound.setInteger("Y", entry.getKey().getY());
            nbttagcompound.setInteger("Z", entry.getKey().getZ());
            nbttagcompound.setInteger("Facing", entry.getValue().getHorizontalIndex());
            entrancesList.appendTag(nbttagcompound);
        }
        compound.setTag("Entrances", entrancesList);

        NBTTagList entranceBottomsList = new NBTTagList();
        for (Map.Entry<BlockPos, EnumFacing> entry : this.entranceBottoms.entrySet()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", entry.getKey().getX());
            nbttagcompound.setInteger("Y", entry.getKey().getY());
            nbttagcompound.setInteger("Z", entry.getKey().getZ());
            nbttagcompound.setInteger("Facing", entry.getValue().getHorizontalIndex());
            entranceBottomsList.appendTag(nbttagcompound);
        }
        compound.setTag("EntranceBottoms", entranceBottomsList);
        compound.setUniqueId("HiveUUID", this.hiveUUID);
        compound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (UUID s : this.playerReputation.keySet()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            try {
                {
                    nbttagcompound1.setString("UUID", s.toString());
                    nbttagcompound1.setInteger("S", this.playerReputation.get(s).intValue());
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            } catch (RuntimeException var9) {
            }
        }

        compound.setTag("Players", nbttaglist1);
    }

    public void addRoom(BlockPos center, WorldGenMyrmexHive.RoomType roomType) {
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD && !this.foodRooms.contains(center)) {
            this.foodRooms.add(center);
        } else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY && !this.babyRooms.contains(center)) {
            this.babyRooms.add(center);
        } else if (!this.miscRooms.contains(center) && !this.miscRooms.contains(center)) {
            this.miscRooms.add(center);
        }
        this.allRooms.add(center);
    }

    public void addRoomWithMessage(EntityPlayer player, BlockPos center, WorldGenMyrmexHive.RoomType roomType) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD) {
            if (!this.foodRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.foodRooms.add(center);
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.added_food_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);

            }
        } else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY) {
            if (!this.babyRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.babyRooms.add(center);
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.added_nursery_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            }
        } else if (!this.miscRooms.contains(center) && !allCurrentRooms.contains(center)) {
            this.miscRooms.add(center);
            player.sendStatusMessage(new TextComponentTranslation("myrmex.message.added_misc_room", center.getX(), center.getY(), center.getZ()), false);
        } else {
            player.sendStatusMessage(new TextComponentTranslation("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
        }
    }

    public void addEnteranceWithMessage(EntityPlayer player, boolean bottom, BlockPos center, EnumFacing facing) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (bottom) {
            if (allCurrentRooms.contains(center)) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                this.getEntranceBottoms().put(center, facing);
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.added_enterance_bottom", center.getX(), center.getY(), center.getZ()), false);
            }
        } else {
            if (allCurrentRooms.contains(center)) {
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                this.getEntrances().put(center, facing);
                player.sendStatusMessage(new TextComponentTranslation("myrmex.message.added_enterance_surface", center.getX(), center.getY(), center.getZ()), false);
            }
        }
    }

    public List<BlockPos> getRooms(WorldGenMyrmexHive.RoomType roomType) {
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD) {
            return foodRooms;
        } else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY) {
            return babyRooms;
        } else {
            return miscRooms;
        }
    }

    public List<BlockPos> getAllRooms() {
        allRooms.clear();
        allRooms.add(center);
        allRooms.addAll(foodRooms);
        allRooms.addAll(babyRooms);
        allRooms.addAll(miscRooms);
        return allRooms;
    }

    public BlockPos getRandomRoom(Random random, BlockPos returnPos) {
        List<BlockPos> rooms = getAllRooms();
        return rooms.isEmpty() ? returnPos : rooms.get(random.nextInt(Math.max(rooms.size() - 1, 1)));
    }

    public BlockPos getRandomRoom(WorldGenMyrmexHive.RoomType roomType, Random random, BlockPos returnPos) {
        List<BlockPos> rooms = getRooms(roomType);
        return rooms.isEmpty() ? returnPos : rooms.get(random.nextInt(Math.max(rooms.size() - 1, 1)));
    }

    public BlockPos getClosestEntranceToEntity(Entity entity, Random random, boolean randomize) {
        Map.Entry<BlockPos, EnumFacing> closest = getClosestEntrance(entity);
        if (closest != null) {
            if (randomize) {
                BlockPos pos = closest.getKey().offset(closest.getValue(), random.nextInt(7) + 7).up(4);
                return pos.add(10 - random.nextInt(20), 0, 10 - random.nextInt(20));
            } else {
                return closest.getKey().offset(closest.getValue(), 3);
            }
        }
        return entity.getPosition();
    }

    public BlockPos getClosestEntranceBottomToEntity(Entity entity, Random random) {
        Map.Entry<BlockPos, EnumFacing> closest = null;
        for (Map.Entry<BlockPos, EnumFacing> entry : this.entranceBottoms.entrySet()) {
            if (closest == null || closest.getKey().distanceSq(entity.posX, entity.posY, entity.posZ) > entry.getKey().distanceSq(entity.posX, entity.posY, entity.posZ)) {
                closest = entry;
            }
        }
        return closest != null ? closest.getKey() : entity.getPosition();
    }

    public EntityPlayer getOwner(World world) {
        if (hasOwner) {
            return world.getPlayerEntityByUUID(ownerUUID);
        }
        return null;
    }

    public Map<BlockPos, EnumFacing> getEntrances() {
        return entrances;
    }

    public Map<BlockPos, EnumFacing> getEntranceBottoms() {
        return entranceBottoms;
    }

    private Map.Entry<BlockPos, EnumFacing> getClosestEntrance(Entity entity) {
        Map.Entry<BlockPos, EnumFacing> closest = null;
        for (Map.Entry<BlockPos, EnumFacing> entry : this.entrances.entrySet()) {
            if (closest == null || closest.getKey().distanceSq(entity.posX, entity.posY, entity.posZ) > entry.getKey().distanceSq(entity.posX, entity.posY, entity.posZ)) {
                closest = entry;
            }
        }
        return closest;
    }

    public void setDefaultPlayerReputation(int defaultReputation) {
        for (UUID s : this.playerReputation.keySet()) {
            this.modifyPlayerReputation(s, defaultReputation);
        }
    }

    public boolean repopulate() {
        int roomCount = this.getAllRooms().size();
        return this.numMyrmex < Math.min(IceAndFire.CONFIG.myrmexColonySize, roomCount * 9) && reproduces;
    }

    public void addMyrmex(EntityMyrmexBase myrmex) {
        if (!this.myrmexList.contains(myrmex.getUniqueID())) {
            this.myrmexList.add(myrmex.getUniqueID());
        }
    }

    public void removeRoom(BlockPos pos) {
        this.foodRooms.remove(pos);
        this.miscRooms.remove(pos);
        this.babyRooms.remove(pos);
        this.allRooms.remove(pos);
        this.getEntrances().remove(pos);
        this.getEntranceBottoms().remove(pos);
    }

    public String toString() {
        return "MyrmexHive(x=" + this.center.getX() + ",y=" + this.center.getY() + ",z=" + this.center.getZ() + "), population=" + this.getNumMyrmex() + "\nUUID: " + hiveUUID;
    }

    public boolean equals(MyrmexHive hive) {
        return this.hiveUUID.equals(hive.hiveUUID);
    }

    class HiveAggressor {
        public EntityLivingBase agressor;
        public int agressionTime;
        public int agressionLevel;

        HiveAggressor(EntityLivingBase agressorIn, int agressionTimeIn, int agressionLevel) {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
            this.agressionLevel = agressionLevel;
        }
    }
}
