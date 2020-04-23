package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityMyrmexEgg extends EntityLiving implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Boolean> MYRMEX_TYPE = EntityDataManager.createKey(EntityMyrmexEgg.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> MYRMEX_AGE = EntityDataManager.createKey(EntityMyrmexEgg.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MYRMEX_CASTE = EntityDataManager.createKey(EntityMyrmexEgg.class, DataSerializers.VARINT);
    public UUID hiveUUID;

    public EntityMyrmexEgg(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.setSize(0.45F, 0.55F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Jungle", this.isJungle());
        tag.setInteger("MyrmexAge", this.getMyrmexAge());
        tag.setInteger("MyrmexCaste", this.getMyrmexCaste());
        tag.setUniqueId("HiveUUID", hiveUUID == null ? hiveUUID = UUID.randomUUID() : hiveUUID);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setJungle(tag.getBoolean("Jungle"));
        this.setMyrmexAge(tag.getInteger("MyrmexAge"));
        this.setMyrmexCaste(tag.getInteger("MyrmexCaste"));
        hiveUUID = tag.getUniqueId("hiveUUID");
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(MYRMEX_TYPE, false);
        this.getDataManager().register(MYRMEX_AGE, Integer.valueOf(0));
        this.getDataManager().register(MYRMEX_CASTE, Integer.valueOf(0));
    }


    public boolean isJungle() {
        return this.getDataManager().get(MYRMEX_TYPE).booleanValue();
    }

    public void setJungle(boolean jungle) {
        this.getDataManager().set(MYRMEX_TYPE, jungle);
    }

    public int getMyrmexAge() {
        return this.getDataManager().get(MYRMEX_AGE).intValue();
    }

    public void setMyrmexAge(int i) {
        this.getDataManager().set(MYRMEX_AGE, i);
    }

    public int getMyrmexCaste() {
        return this.getDataManager().get(MYRMEX_CASTE).intValue();
    }

    public void setMyrmexCaste(int i) {
        this.getDataManager().set(MYRMEX_CASTE, i);
    }

    public boolean canSeeSky() {
        return world.canBlockSeeSky(new BlockPos(this));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!canSeeSky()) {
            this.setMyrmexAge(this.getMyrmexAge() + 1);
        }
        if (this.getMyrmexAge() > IceAndFire.CONFIG.myrmexEggTicks) {
            this.setDead();
            EntityMyrmexBase myrmex;
            switch (this.getMyrmexCaste()) {
                default:
                    myrmex = new EntityMyrmexWorker(world);
                    break;
                case 1:
                    myrmex = new EntityMyrmexSoldier(world);
                    break;
                case 2:
                    myrmex = new EntityMyrmexRoyal(world);
                    break;
                case 3:
                    myrmex = new EntityMyrmexSentinel(world);
                    break;
                case 4:
                    myrmex = new EntityMyrmexQueen(world);
                    break;
            }
            myrmex.setJungleVariant(this.isJungle());
            myrmex.setGrowthStage(0);
            myrmex.setPositionAndRotation(this.posX, this.posY, this.posZ, 0, 0);
            if (myrmex instanceof EntityMyrmexQueen) {
                MyrmexHive hive = new MyrmexHive(world, this.getPosition(), 100);
                EntityPlayer player = world.getClosestPlayerToEntity(this, 30);
                if (player != null) {
                    hive.hasOwner = true;
                    hive.ownerUUID = player.getUniqueID();
                    if (!world.isRemote) {
                        hive.modifyPlayerReputation(player.getUniqueID(), 100);
                    }
                }
                MyrmexWorldData.addHive(world, hive);
                myrmex.setHive(hive);


            } else {
                MyrmexHive hive = MyrmexWorldData.get(world).getHiveFromUUID(hiveUUID);
                if (!world.isRemote && hive != null && this.getDistanceSq(hive.getCenter()) < 2000) {
                    myrmex.setHive(hive);
                }
            }

            if (!world.isRemote) {
                world.spawnEntity(myrmex);
            }
            this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, IafSoundRegistry.DRAGON_HATCH, this.getSoundCategory(), 2.5F, 1.0F, false);
        }
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float var2) {
        if (dmg == DamageSource.IN_WALL || dmg == DamageSource.FALL) {
            return false;
        }
        if (!world.isRemote && !dmg.canHarmInCreative()) {
            this.entityDropItem(this.getItem(), 0);
        }
        this.setDead();
        return super.attackEntityFrom(dmg, var2);
    }

    private ItemStack getItem() {
        return new ItemStack(this.isJungle() ? IafItemRegistry.myrmex_jungle_egg : IafItemRegistry.myrmex_desert_egg, 1, this.getMyrmexCaste());
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(EntityPlayer player) {
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public boolean isInNursery() {
        MyrmexHive hive = MyrmexWorldData.get(this.world).getNearestHive(new BlockPos(this), 100);
        if (hive != null && hive.getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRNG(), this.getPosition()) != null) {
            return false;
        }
        if (hive != null) {
            BlockPos nursery = hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRNG(), this.getPosition());
            return this.getDistanceSqToCenter(nursery) < 45;
        }
        return false;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
