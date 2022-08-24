package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
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
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDragonEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    protected static final DataParameter<java.util.Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(EntityDragonEgg.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.defineId(EntityDragonEgg.class, DataSerializers.INT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.defineId(EntityDragonEgg.class, DataSerializers.INT);

    public EntityDragonEgg(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", (byte) this.getEggType().ordinal());
        tag.putInt("DragonAge", this.getDragonAge());
        try {
            if (this.getOwnerId() == null) {
                tag.putString("OwnerUUID", "");
            } else {
                tag.putString("OwnerUUID", this.getOwnerId().toString());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.setEggType(EnumDragonEgg.values()[tag.getInt("Color")]);
        this.setDragonAge(tag.getInt("DragonAge"));
        String s;

        if (tag.contains("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            UUID converedUUID = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DRAGON_TYPE, Integer.valueOf(0));
        this.getEntityData().define(DRAGON_AGE, Integer.valueOf(0));
        this.getEntityData().define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, java.util.Optional.ofNullable(p_184754_1_));
    }

    public EnumDragonEgg getEggType() {
        return EnumDragonEgg.values()[this.getEntityData().get(DRAGON_TYPE).intValue()];
    }

    public void setEggType(EnumDragonEgg newtype) {
        this.getEntityData().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null && super.isInvulnerableTo(i);
    }

    public int getDragonAge() {
        return this.getEntityData().get(DRAGON_AGE).intValue();
    }

    public void setDragonAge(int i) {
        this.getEntityData().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            this.setAirSupply(200);
            getEggType().dragonType.updateEggCondition(this);
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
    public boolean hurt(DamageSource var1, float var2) {
        if (!level.isClientSide && !var1.isBypassInvul() && !removed) {
            this.spawnAtLocation(this.getItem().getItem(), 1);
        }
        this.remove();
        return true;
    }

    private ItemStack getItem() {
        switch (getEggType().ordinal()) {
            default:
                return new ItemStack(IafItemRegistry.DRAGONEGG_RED);
            case 1:
                return new ItemStack(IafItemRegistry.DRAGONEGG_GREEN);
            case 2:
                return new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE);
            case 3:
                return new ItemStack(IafItemRegistry.DRAGONEGG_GRAY);
            case 4:
                return new ItemStack(IafItemRegistry.DRAGONEGG_BLUE);
            case 5:
                return new ItemStack(IafItemRegistry.DRAGONEGG_WHITE);
            case 6:
                return new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE);
            case 7:
                return new ItemStack(IafItemRegistry.DRAGONEGG_SILVER);
            case 8:
                return new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC);
            case 9:
                return new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST);
            case 10:
                return new ItemStack(IafItemRegistry.DRAGONEGG_COPPER);
            case 11:
                return new ItemStack(IafItemRegistry.DRAGONEGG_BLACK);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(PlayerEntity player) {
        this.setOwnerId(player.getUUID());
    }

    @Override
    public boolean isMobDead() {
        return true;
    }
}
