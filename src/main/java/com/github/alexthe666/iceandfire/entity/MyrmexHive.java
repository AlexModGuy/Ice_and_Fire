package com.github.alexthe666.iceandfire.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MyrmexHive {
    private World world;
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.<VillageDoorInfo>newArrayList();
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private final Map<UUID, Integer> playerReputation = Maps.<UUID, Integer>newHashMap();
    private final List<HiveAggressor> villageAgressors = Lists.<HiveAggressor>newArrayList();
    private int numIronGolems;

    public MyrmexHive(){}

    public MyrmexHive(World worldIn) {
        this.world = worldIn;
    }

    public void setWorld(World worldIn) {
        this.world = worldIn;
    }

    public void tick(int tickCounterIn) {
        this.tickCounter = tickCounterIn;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();

        if (tickCounterIn % 20 == 0) {
            this.updateNumVillagers();
        }

        if (tickCounterIn % 30 == 0) {
            this.updateNumIronGolems();
        }

        int i = this.numVillagers / 10;

        if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.world.rand.nextInt(7000) == 0) {
            Vec3d vec3d = this.findRandomSpawnPos(this.center, 2, 4, 2);

            if (vec3d != null) {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.world);
                entityirongolem.setPosition(vec3d.x, vec3d.y, vec3d.z);
                this.world.spawnEntity(entityirongolem);
                ++this.numIronGolems;
            }
        }
    }

    private Vec3d findRandomSpawnPos(BlockPos pos, int x, int y, int z) {
        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos = pos.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);

            if (this.isBlockPosWithinSqVillageRadius(blockpos) && this.isAreaClearAround(new BlockPos(x, y, z), blockpos)) {
                return new Vec3d((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
            }
        }

        return null;
    }

    /**
     * Checks to see if the volume around blockLocation is clear and able to fit blockSize
     */
    private boolean isAreaClearAround(BlockPos blockSize, BlockPos blockLocation) {
        if (!this.world.getBlockState(blockLocation.down()).isTopSolid()) {
            return false;
        } else {
            int i = blockLocation.getX() - blockSize.getX() / 2;
            int j = blockLocation.getZ() - blockSize.getZ() / 2;

            for (int k = i; k < i + blockSize.getX(); ++k) {
                for (int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l) {
                    for (int i1 = j; i1 < j + blockSize.getZ(); ++i1) {
                        if (this.world.getBlockState(new BlockPos(k, l, i1)).isNormalCube()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void updateNumIronGolems() {
        List<EntityIronGolem> list = this.world.<EntityIronGolem>getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4), (double) (this.center.getZ() - this.villageRadius), (double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4), (double) (this.center.getZ() + this.villageRadius)));
        this.numIronGolems = list.size();
    }

    private void updateNumVillagers() {
        List<EntityVillager> list = this.world.<EntityVillager>getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4), (double) (this.center.getZ() - this.villageRadius), (double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4), (double) (this.center.getZ() + this.villageRadius)));
        this.numVillagers = list.size();

        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    /**
     * Actually get num village door info entries, but that boils down to number of doors. Called by
     * EntityAIVillagerMate and VillageSiege
     */
    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers() {
        return this.numVillagers;
    }

    /**
     * Checks to see if the distance squared between this BlockPos and the center of this Village is less than this
     * Village's villageRadius squared
     */
    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos) {
        return this.center.distanceSq(pos) < (double) (this.villageRadius * this.villageRadius);
    }

    /**
     * called only by class EntityAIMoveThroughVillage
     */
    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo getNearestDoor(BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList) {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j < i) {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }

        return villagedoorinfo;
    }

    /**
     * Returns {@link VillageDoorInfo VillageDoorInfo} from given block position
     */
    public VillageDoorInfo getDoorInfo(BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList) {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j > 256) {
                j = j * 1000;
            } else {
                j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
            }

            if (j < i) {
                BlockPos blockpos = villagedoorinfo1.getDoorBlockPos();
                EnumFacing enumfacing = villagedoorinfo1.getInsideDirection();

                if (this.world.getBlockState(blockpos.offset(enumfacing, 1)).getBlock().isPassable(this.world, blockpos.offset(enumfacing, 1)) && this.world.getBlockState(blockpos.offset(enumfacing, -1)).getBlock().isPassable(this.world, blockpos.offset(enumfacing, -1)) && this.world.getBlockState(blockpos.up().offset(enumfacing, 1)).getBlock().isPassable(this.world, blockpos.up().offset(enumfacing, 1)) && this.world.getBlockState(blockpos.up().offset(enumfacing, -1)).getBlock().isPassable(this.world, blockpos.up().offset(enumfacing, -1))) {
                    villagedoorinfo = villagedoorinfo1;
                    i = j;
                }
            }
        }

        return villagedoorinfo;
    }

    /**
     * if door not existed in this village, null will be returned
     */
    @Nullable
    public VillageDoorInfo getExistedDoor(BlockPos doorBlock) {
        if (this.center.distanceSq(doorBlock) > (double) (this.villageRadius * this.villageRadius)) {
            return null;
        } else {
            for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
                if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
                    return villagedoorinfo;
                }
            }

            return null;
        }
    }

    public void addVillageDoorInfo(VillageDoorInfo doorInfo) {
        this.villageDoorInfoList.add(doorInfo);
        this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = doorInfo.getLastActivityTimestamp();
    }

    /**
     * Returns true, if there is not a single village door left. Called by VillageCollection
     */
    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn) {
        for (HiveAggressor hive$villageaggressor : this.villageAgressors) {
            if (hive$villageaggressor.agressor == entitylivingbaseIn) {
                hive$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }

        this.villageAgressors.add(new HiveAggressor(entitylivingbaseIn, this.tickCounter));
    }

    @Nullable
    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
        double d0 = Double.MAX_VALUE;
        HiveAggressor hive$villageaggressor = null;

        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            HiveAggressor hive$villageaggressor1 = this.villageAgressors.get(i);
            double d1 = hive$villageaggressor1.agressor.getDistanceSq(entitylivingbaseIn);

            if (d1 <= d0) {
                hive$villageaggressor = hive$villageaggressor1;
                d0 = d1;
            }
        }

        return hive$villageaggressor == null ? null : hive$villageaggressor.agressor;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;

        for (UUID s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationTooLow(s)) {
                EntityPlayer entityplayer1 = this.world.getPlayerEntityByUUID(s);

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

    private void removeDeadAndOutOfRangeDoors() {
        boolean flag = false;
        boolean flag1 = this.world.rand.nextInt(50) == 0;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoorinfo = iterator.next();

            if (flag1) {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }

            if (world.isBlockLoaded(villagedoorinfo.getDoorBlockPos())) // Forge: check that the door block is loaded to avoid loading chunks
                if (!this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoorinfo.getLastActivityTimestamp()) > 1200) {
                    this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
                    flag = true;
                    villagedoorinfo.setIsDetachedFromVillageFlag(true);
                    iterator.remove();
                }
        }

        if (flag) {
            this.updateVillageRadiusAndCenter();
        }
    }

    private boolean isWoodDoor(BlockPos pos) {
        IBlockState iblockstate = this.world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block instanceof BlockDoor) {
            return iblockstate.getMaterial() == Material.WOOD;
        } else {
            return false;
        }
    }

    private void updateVillageRadiusAndCenter() {
        int i = this.villageDoorInfoList.size();

        if (i == 0) {
            this.center = BlockPos.ORIGIN;
            this.villageRadius = 0;
        } else {
            this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
            int j = 0;

            for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
                j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
            }

            this.villageRadius = Math.max(32, (int) Math.sqrt((double) j) + 1);
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

    /**
     * Modify a players reputation in the village. Use positive values to increase reputation and negative values to
     * decrease. <br>Note that a players reputation is clamped between -30 and 10
     */
    @Deprecated //Hasn't worked since 1.9, use UUID version below.
    public int modifyPlayerReputation(String playerName, int reputation) {
        return this.modifyPlayerReputation(findUUID(playerName), reputation);
    }

    public int modifyPlayerReputation(UUID playerName, int reputation) {
        int i = this.getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, -30, 10);
        this.playerReputation.put(playerName, Integer.valueOf(j));
        return j;
    }

    /**
     * Return whether this player has a too low reputation with this village.
     */
    @Deprecated //Hasn't worked since 1.9, use UUID version below.
    public boolean isPlayerReputationTooLow(String playerName) {
        return this.isPlayerReputationTooLow(findUUID(playerName));
    }

    public boolean isPlayerReputationTooLow(UUID uuid) {
        return this.getPlayerReputation(uuid) <= -15;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound compound) {
        this.numVillagers = compound.getInteger("PopSize");
        this.villageRadius = compound.getInteger("Radius");
        this.numIronGolems = compound.getInteger("Golems");
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
        compound.setInteger("PopSize", this.numVillagers);
        compound.setInteger("Radius", this.villageRadius);
        compound.setInteger("Golems", this.numIronGolems);
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

        compound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (UUID s : this.playerReputation.keySet()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            try {
                {
                    nbttagcompound1.setString("UUID", s.toString());
                    nbttagcompound1.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            } catch (RuntimeException var9) {
                ;
            }
        }

        compound.setTag("Players", nbttaglist1);
    }

    /**
     * Prevent villager breeding for a fixed interval of time
     */
    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }

    /**
     * Return whether villagers mating refractory period has passed
     */
    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int defaultReputation) {
        for (UUID s : this.playerReputation.keySet()) {
            this.modifyPlayerReputation(s, defaultReputation);
        }
    }

    class HiveAggressor {
        public EntityLivingBase agressor;
        public int agressionTime;

        HiveAggressor(EntityLivingBase agressorIn, int agressionTimeIn) {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
        }
    }
}