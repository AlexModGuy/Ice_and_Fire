package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
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
    private final Map<BlockPos, Direction> entrances = Maps.newHashMap();
    private final Map<BlockPos, Direction> entranceBottoms = Maps.newHashMap();
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

    public void addOrRenewAgressor(LivingEntity LivingEntityIn, int agressiveLevel) {
        for (HiveAggressor hive$villageaggressor : this.villageAgressors) {
            if (hive$villageaggressor.agressor == LivingEntityIn) {
                hive$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }

        this.villageAgressors.add(new HiveAggressor(LivingEntityIn, this.tickCounter, agressiveLevel));
    }

    @Nullable
    public LivingEntity findNearestVillageAggressor(LivingEntity LivingEntityIn) {
        double d0 = Double.MAX_VALUE;
        int previousAgressionLevel = 0;
        HiveAggressor hive$villageaggressor = null;
        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            HiveAggressor hive$villageaggressor1 = this.villageAgressors.get(i);
            double d1 = hive$villageaggressor1.agressor.getDistanceSq(LivingEntityIn);
            int agressionLevel = hive$villageaggressor1.agressionLevel;

            if (d1 <= d0 || agressionLevel > previousAgressionLevel) {
                hive$villageaggressor = hive$villageaggressor1;
                d0 = d1;
            }
            previousAgressionLevel = agressionLevel;
        }

        return hive$villageaggressor == null ? null : hive$villageaggressor.agressor;
    }

    public PlayerEntity getNearestTargetPlayer(LivingEntity villageDefender, World world) {
        double d0 = Double.MAX_VALUE;
        PlayerEntity PlayerEntity = null;

        for (UUID s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationTooLowToFight(s)) {
                PlayerEntity PlayerEntity1 = world.getPlayerEntityByUUID(s);

                if (PlayerEntity1 != null) {
                    double d1 = PlayerEntity1.getDistanceSq(villageDefender);

                    if (d1 <= d0) {
                        PlayerEntity = PlayerEntity1;
                        d0 = d1;
                    }
                }
            }
        }

        return PlayerEntity;
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
            return PlayerEntity.getOfflineUUID(name);
        GameProfile profile = this.world.getMinecraftServer().getPlayerProfileCache().getGameProfileForUsername(name);
        return profile == null ? PlayerEntity.getOfflineUUID(name) : profile.getId();
    }

    public int modifyPlayerReputation(UUID playerName, int reputation) {
        int i = this.getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, 0, 100);
        if (this.hasOwner && playerName.equals(ownerUUID)) {
            j = 100;
        }
        PlayerEntity player = null;
        try {
            player = world.getPlayerEntityByUUID(playerName);
        } catch (Exception e) {
            IceAndFire.logger.warn("Myrmex Hive could not find player with associated UUID");
        }
        if (player != null) {
            if (j - i != 0) {
                player.sendStatusMessage(new TranslationTextComponent(j - i >= 0 ? "myrmex.message.raised_reputation" : "myrmex.message.lowered_reputation", Math.abs(j - i), j), true);
            }
            if (i < 25 && j >= 25) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.peaceful"), false);
            }
            if (i >= 25 && j < 25) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.hostile"), false);
            }
            if (i < 50 && j >= 50) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.trade"), false);
            }
            if (i >= 50 && j < 50) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.no_trade"), false);
            }
            if (i < 75 && j >= 75) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.can_use_staff"), false);
            }
            if (i >= 75 && j < 75) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.cant_use_staff"), false);
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
    public void readVillageDataFromNBT(CompoundNBT compound) {
        this.numMyrmex = compound.getInt("PopSize");
        this.reproduces = compound.getBoolean("Reproduces");
        this.hasOwner = compound.getBoolean("HasOwner");
        this.ownerUUID = compound.getUniqueId("OwnerUUID");
        this.colonyName = compound.getString("ColonyName");
        this.villageRadius = compound.getInt("Radius");
        this.lastAddDoorTimestamp = compound.getInt("Stable");
        this.tickCounter = compound.getInt("Tick");
        this.noBreedTicks = compound.getInt("MTick");
        this.center = new BlockPos(compound.getInt("CX"), compound.getInt("CY"), compound.getInt("CZ"));
        this.centerHelper = new BlockPos(compound.getInt("ACX"), compound.getInt("ACY"), compound.getInt("ACZ"));
        NBTTagList nbttaglist = compound.getTagList("Doors", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            CompoundNBT CompoundNBT = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")), CompoundNBT.getInt("IDX"), CompoundNBT.getInt("IDZ"), CompoundNBT.getInt("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }
        NBTTagList hiveMembers = compound.getTagList("HiveMembers", 10);
        this.myrmexList.clear();
        for (int i = 0; i < hiveMembers.tagCount(); ++i) {
            CompoundNBT CompoundNBT = hiveMembers.getCompoundTagAt(i);
            this.myrmexList.add(CompoundNBT.getUniqueId("MyrmexUUID"));
        }
        NBTTagList foodRoomList = compound.getTagList("FoodRooms", 10);
        this.foodRooms.clear();
        for (int i = 0; i < foodRoomList.tagCount(); ++i) {
            CompoundNBT CompoundNBT = foodRoomList.getCompoundTagAt(i);
            this.foodRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NBTTagList babyRoomList = compound.getTagList("BabyRooms", 10);
        this.babyRooms.clear();
        for (int i = 0; i < babyRoomList.tagCount(); ++i) {
            CompoundNBT CompoundNBT = babyRoomList.getCompoundTagAt(i);
            this.babyRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NBTTagList miscRoomList = compound.getTagList("MiscRooms", 10);
        this.miscRooms.clear();
        for (int i = 0; i < miscRoomList.tagCount(); ++i) {
            CompoundNBT CompoundNBT = miscRoomList.getCompoundTagAt(i);
            this.miscRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NBTTagList entrancesList = compound.getTagList("Entrances", 10);
        this.entrances.clear();
        for (int i = 0; i < entrancesList.tagCount(); ++i) {
            CompoundNBT CompoundNBT = entrancesList.getCompoundTagAt(i);
            this.entrances.put(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")), Direction.byHorizontalIndex(CompoundNBT.getInt("Facing")));
        }

        NBTTagList entranceBottomsList = compound.getTagList("EntranceBottoms", 10);
        this.entranceBottoms.clear();
        for (int i = 0; i < entranceBottomsList.tagCount(); ++i) {
            CompoundNBT CompoundNBT = entranceBottomsList.getCompoundTagAt(i);
            this.entranceBottoms.put(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")), Direction.byHorizontalIndex(CompoundNBT.getInt("Facing")));
        }
        hiveUUID = compound.getUniqueId("HiveUUID");
        NBTTagList nbttaglist1 = compound.getTagList("Players", 10);
        for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
            CompoundNBT CompoundNBT1 = nbttaglist1.getCompoundTagAt(j);

            if (CompoundNBT1.hasKey("UUID")) {
                this.playerReputation.put(UUID.fromString(CompoundNBT1.getString("UUID")), Integer.valueOf(CompoundNBT1.getInt("S")));
            } else {
                //World is never set here, so this will always be offline UUIDs, sadly there is no way to convert this.
                this.playerReputation.put(findUUID(CompoundNBT1.getString("Name")), Integer.valueOf(CompoundNBT1.getInt("S")));
            }
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(CompoundNBT compound) {
        compound.putInt("PopSize", this.numMyrmex);
        compound.setBoolean("Reproduces", this.reproduces);
        compound.setBoolean("HasOwner", this.hasOwner);
        if (this.ownerUUID != null) {
            compound.setUniqueId("OwnerUUID", this.ownerUUID);
        }
        compound.setString("ColonyName", this.colonyName);
        compound.putInt("Radius", this.villageRadius);
        compound.putInt("Stable", this.lastAddDoorTimestamp);
        compound.putInt("Tick", this.tickCounter);
        compound.putInt("MTick", this.noBreedTicks);
        compound.putInt("CX", this.center.getX());
        compound.putInt("CY", this.center.getY());
        compound.putInt("CZ", this.center.getZ());
        compound.putInt("ACX", this.centerHelper.getX());
        compound.putInt("ACY", this.centerHelper.getY());
        compound.putInt("ACZ", this.centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();
        for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", villagedoorinfo.getDoorBlockPos().getX());
            CompoundNBT.putInt("Y", villagedoorinfo.getDoorBlockPos().getY());
            CompoundNBT.putInt("Z", villagedoorinfo.getDoorBlockPos().getZ());
            CompoundNBT.putInt("IDX", villagedoorinfo.getInsideOffsetX());
            CompoundNBT.putInt("IDZ", villagedoorinfo.getInsideOffsetZ());
            CompoundNBT.putInt("TS", villagedoorinfo.getLastActivityTimestamp());
            nbttaglist.appendTag(CompoundNBT);
        }
        NBTTagList hiveMembers = new NBTTagList();
        for (UUID memberUUID : this.myrmexList) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.setUniqueId("MyrmexUUID", memberUUID);
            hiveMembers.appendTag(CompoundNBT);
        }
        compound.setTag("HiveMembers", hiveMembers);
        NBTTagList foodRoomList = new NBTTagList();
        for (BlockPos pos : this.foodRooms) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            foodRoomList.appendTag(CompoundNBT);
        }
        compound.setTag("FoodRooms", foodRoomList);
        NBTTagList babyRoomList = new NBTTagList();
        for (BlockPos pos : this.babyRooms) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            babyRoomList.appendTag(CompoundNBT);
        }
        compound.setTag("BabyRooms", babyRoomList);
        NBTTagList miscRoomList = new NBTTagList();
        for (BlockPos pos : this.miscRooms) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            miscRoomList.appendTag(CompoundNBT);
        }
        compound.setTag("MiscRooms", miscRoomList);
        NBTTagList entrancesList = new NBTTagList();
        for (Map.Entry<BlockPos, Direction> entry : this.entrances.entrySet()) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", entry.getKey().getX());
            CompoundNBT.putInt("Y", entry.getKey().getY());
            CompoundNBT.putInt("Z", entry.getKey().getZ());
            CompoundNBT.putInt("Facing", entry.get().getHorizontalIndex());
            entrancesList.appendTag(CompoundNBT);
        }
        compound.setTag("Entrances", entrancesList);

        NBTTagList entranceBottomsList = new NBTTagList();
        for (Map.Entry<BlockPos, Direction> entry : this.entranceBottoms.entrySet()) {
            CompoundNBT CompoundNBT = new CompoundNBT();
            CompoundNBT.putInt("X", entry.getKey().getX());
            CompoundNBT.putInt("Y", entry.getKey().getY());
            CompoundNBT.putInt("Z", entry.getKey().getZ());
            CompoundNBT.putInt("Facing", entry.get().getHorizontalIndex());
            entranceBottomsList.appendTag(CompoundNBT);
        }
        compound.setTag("EntranceBottoms", entranceBottomsList);
        compound.setUniqueId("HiveUUID", this.hiveUUID);
        compound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (UUID s : this.playerReputation.keySet()) {
            CompoundNBT CompoundNBT1 = new CompoundNBT();

            try {
                {
                    CompoundNBT1.setString("UUID", s.toString());
                    CompoundNBT1.putInt("S", this.playerReputation.get(s).intValue());
                    nbttaglist1.appendTag(CompoundNBT1);
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

    public void addRoomWithMessage(PlayerEntity player, BlockPos center, WorldGenMyrmexHive.RoomType roomType) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD) {
            if (!this.foodRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.foodRooms.add(center);
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.added_food_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);

            }
        } else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY) {
            if (!this.babyRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.babyRooms.add(center);
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.added_nursery_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            }
        } else if (!this.miscRooms.contains(center) && !allCurrentRooms.contains(center)) {
            this.miscRooms.add(center);
            player.sendStatusMessage(new TranslationTextComponent("myrmex.message.added_misc_room", center.getX(), center.getY(), center.getZ()), false);
        } else {
            player.sendStatusMessage(new TranslationTextComponent("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
        }
    }

    public void addEnteranceWithMessage(PlayerEntity player, boolean bottom, BlockPos center, Direction facing) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (bottom) {
            if (allCurrentRooms.contains(center)) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                this.getEntranceBottoms().put(center, facing);
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.added_enterance_bottom", center.getX(), center.getY(), center.getZ()), false);
            }
        } else {
            if (allCurrentRooms.contains(center)) {
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            } else {
                this.getEntrances().put(center, facing);
                player.sendStatusMessage(new TranslationTextComponent("myrmex.message.added_enterance_surface", center.getX(), center.getY(), center.getZ()), false);
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
        Map.Entry<BlockPos, Direction> closest = getClosestEntrance(entity);
        if (closest != null) {
            if (randomize) {
                BlockPos pos = closest.getKey().offset(closest.get(), random.nextInt(7) + 7).up(4);
                return pos.add(10 - random.nextInt(20), 0, 10 - random.nextInt(20));
            } else {
                return closest.getKey().offset(closest.get(), 3);
            }
        }
        return entity.getPosition();
    }

    public BlockPos getClosestEntranceBottomToEntity(Entity entity, Random random) {
        Map.Entry<BlockPos, Direction> closest = null;
        for (Map.Entry<BlockPos, Direction> entry : this.entranceBottoms.entrySet()) {
            if (closest == null || closest.getKey().distanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ()) > entry.getKey().distanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ())) {
                closest = entry;
            }
        }
        return closest != null ? closest.getKey() : entity.getPosition();
    }

    public PlayerEntity getOwner(World world) {
        if (hasOwner) {
            return world.getPlayerEntityByUUID(ownerUUID);
        }
        return null;
    }

    public Map<BlockPos, Direction> getEntrances() {
        return entrances;
    }

    public Map<BlockPos, Direction> getEntranceBottoms() {
        return entranceBottoms;
    }

    private Map.Entry<BlockPos, Direction> getClosestEntrance(Entity entity) {
        Map.Entry<BlockPos, Direction> closest = null;
        for (Map.Entry<BlockPos, Direction> entry : this.entrances.entrySet()) {
            if (closest == null || closest.getKey().distanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ()) > entry.getKey().distanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ())) {
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
        return this.numMyrmex < Math.min(IafConfig.myrmexColonySize, roomCount * 9) && reproduces;
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
        public LivingEntity agressor;
        public int agressionTime;
        public int agressionLevel;

        HiveAggressor(LivingEntity agressorIn, int agressionTimeIn, int agressionLevel) {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
            this.agressionLevel = agressionLevel;
        }
    }
}
