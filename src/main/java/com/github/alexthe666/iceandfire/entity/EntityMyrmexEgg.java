package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityMyrmexEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Boolean> MYRMEX_TYPE = EntityDataManager.defineId(EntityMyrmexEgg.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> MYRMEX_AGE = EntityDataManager.defineId(EntityMyrmexEgg.class, DataSerializers.INT);
    private static final DataParameter<Integer> MYRMEX_CASTE = EntityDataManager.defineId(EntityMyrmexEgg.class, DataSerializers.INT);
    public UUID hiveUUID;

    public EntityMyrmexEgg(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Jungle", this.isJungle());
        tag.putInt("MyrmexAge", this.getMyrmexAge());
        tag.putInt("MyrmexCaste", this.getMyrmexCaste());
        tag.putUUID("HiveUUID", hiveUUID == null ? hiveUUID = UUID.randomUUID() : hiveUUID);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.setJungle(tag.getBoolean("Jungle"));
        this.setMyrmexAge(tag.getInt("MyrmexAge"));
        this.setMyrmexCaste(tag.getInt("MyrmexCaste"));
        hiveUUID = tag.getUUID("HiveUUID");
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(MYRMEX_TYPE, false);
        this.getEntityData().define(MYRMEX_AGE, Integer.valueOf(0));
        this.getEntityData().define(MYRMEX_CASTE, Integer.valueOf(0));
    }


    public boolean isJungle() {
        return this.getEntityData().get(MYRMEX_TYPE).booleanValue();
    }

    public void setJungle(boolean jungle) {
        this.getEntityData().set(MYRMEX_TYPE, jungle);
    }

    public int getMyrmexAge() {
        return this.getEntityData().get(MYRMEX_AGE).intValue();
    }

    public void setMyrmexAge(int i) {
        this.getEntityData().set(MYRMEX_AGE, i);
    }

    public int getMyrmexCaste() {
        return this.getEntityData().get(MYRMEX_CASTE).intValue();
    }

    public void setMyrmexCaste(int i) {
        this.getEntityData().set(MYRMEX_CASTE, i);
    }

    public boolean canSeeSky() {
        return level.canSeeSkyFromBelowWater(this.blockPosition());
    }

    @Override
    public void tick() {
        super.tick();
        if (!canSeeSky()) {
            this.setMyrmexAge(this.getMyrmexAge() + 1);
        }
        if (this.getMyrmexAge() > IafConfig.myrmexEggTicks) {
            this.remove();
            EntityMyrmexBase myrmex;
            switch (this.getMyrmexCaste()) {
                default:
                    myrmex = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(), level);
                    break;
                case 1:
                    myrmex = new EntityMyrmexSoldier(IafEntityRegistry.MYRMEX_SOLDIER.get(), level);
                    break;
                case 2:
                    myrmex = new EntityMyrmexRoyal(IafEntityRegistry.MYRMEX_ROYAL.get(), level);
                    break;
                case 3:
                    myrmex = new EntityMyrmexSentinel(IafEntityRegistry.MYRMEX_SENTINEL.get(), level);
                    break;
                case 4:
                    myrmex = new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(), level);
                    break;
            }
            myrmex.setJungleVariant(this.isJungle());
            myrmex.setGrowthStage(0);
            myrmex.absMoveTo(this.getX(), this.getY(), this.getZ(), 0, 0);
            if (myrmex instanceof EntityMyrmexQueen) {
                MyrmexHive hive = new MyrmexHive(level, this.blockPosition(), 100);
                PlayerEntity player = level.getNearestPlayer(this, 30);
                if (player != null) {
                    hive.hasOwner = true;
                    hive.ownerUUID = player.getUUID();
                    if (!level.isClientSide) {
                        hive.modifyPlayerReputation(player.getUUID(), 100);
                    }
                }
                MyrmexWorldData.addHive(level, hive);
                myrmex.setHive(hive);


            } else {
                if (MyrmexWorldData.get(level) != null) {
                    MyrmexHive hive;
                    if (this.hiveUUID == null) {
                        hive = MyrmexWorldData.get(level).getNearestHive(this.blockPosition(), 400);
                    } else {
                        hive = MyrmexWorldData.get(level).getHiveFromUUID(hiveUUID);
                    }
                    if (!level.isClientSide && hive != null && Math.sqrt(this.distanceToSqr(hive.getCenter().getX(), hive.getCenter().getY(), hive.getCenter().getZ())) < 2000) {
                        myrmex.setHive(hive);
                    }
                }
            }

            if (!level.isClientSide) {
                level.addFreshEntity(myrmex);
            }
            this.level.playLocalSound(this.getX(), this.getY() + this.getEyeHeight(), this.getZ(), IafSoundRegistry.EGG_HATCH, this.getSoundSource(), 2.5F, 1.0F, false);
        }
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public boolean hurt(DamageSource dmg, float var2) {
        if (dmg == DamageSource.IN_WALL || dmg == DamageSource.FALL) {
            return false;
        }
        if (!level.isClientSide && !dmg.isBypassInvul()) {
            this.spawnAtLocation(this.getItem(), 0);
        }
        this.remove();
        return super.hurt(dmg, var2);
    }

    private ItemStack getItem() {
        ItemStack egg = new ItemStack(this.isJungle() ? IafItemRegistry.MYRMEX_JUNGLE_EGG : IafItemRegistry.MYRMEX_DESERT_EGG, 1);
        CompoundNBT newTag = new CompoundNBT();
        newTag.putInt("EggOrdinal", this.getMyrmexCaste());
        egg.setTag(newTag);
        return egg;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public HandSide getMainArm() {
        return null;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(PlayerEntity player) {
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public boolean isInNursery() {
        MyrmexHive hive = MyrmexWorldData.get(this.level).getNearestHive(this.blockPosition(), 100);
        if (hive != null && hive.getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition()) != null) {
            return false;
        }
        if (hive != null) {
            BlockPos nursery = hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition());
            return this.distanceToSqr(nursery.getX(), nursery.getY(), nursery.getZ()) < 2025;
        }
        return false;
    }
}
