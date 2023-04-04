package com.github.alexthe666.iceandfire.entity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockEggInIce;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.effect.LightningBoltEntity;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.github.alexthe666.iceandfire.entity.DragonType.*;

public class EntityDragonEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    protected static final DataParameter<java.util.Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityDragonEgg.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.createKey(EntityDragonEgg.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.createKey(EntityDragonEgg.class, DataSerializers.VARINT);

    public EntityDragonEgg(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.func_233666_p_()
                //HEALTH
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
                //SPEED
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("Color", (byte) this.getEggType().ordinal());
        tag.putInt("DragonAge", this.getDragonAge());
        try{
            if (this.getOwnerId() == null) {
                tag.putString("OwnerUUID", "");
            } else {
                tag.putString("OwnerUUID", this.getOwnerId().toString());
            }
        }catch (Exception e){

        }
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.setEggType(EnumDragonEgg.values()[tag.getInt("Color")]);
        this.setDragonAge(tag.getInt("DragonAge"));
        String s;

        if (tag.contains("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            UUID converedUUID = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(DRAGON_TYPE, Integer.valueOf(0));
        this.getDataManager().register(DRAGON_AGE, Integer.valueOf(0));
        this.getDataManager().register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, java.util.Optional.ofNullable(p_184754_1_));
    }

    public EnumDragonEgg getEggType() {
        return EnumDragonEgg.values()[this.getDataManager().get(DRAGON_TYPE).intValue()];
    }

    public void setEggType(EnumDragonEgg newtype) {
        this.getDataManager().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getTrueSource() != null && super.isInvulnerableTo(i);
    }

    public int getDragonAge() {
        return this.getDataManager().get(DRAGON_AGE).intValue();
    }

    public void setDragonAge(int i) {
        this.getDataManager().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote()) {
            this.setAir(200);
            updateEggCondition();
        }
    }

    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2) {
        if (!world.isRemote && !var1.canHarmInCreative() && !removed) {
            this.entityDropItem(this.getItem().getItem(), 1);
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
    public boolean canBePushed() {
        return false;
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(PlayerEntity player) {
        this.setOwnerId(player.getUniqueID());
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public void updateEggCondition() {

        BlockPos pos = new BlockPos(getPositionVec());
        if (getEggType().dragonType == FIRE) {
            if (world.getBlockState(pos).getMaterial() == Material.FIRE) {
                setDragonAge(getDragonAge() + 1);
            }
            if (getDragonAge() > IafConfig.dragonEggTime) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                EntityFireDragon dragon = new EntityFireDragon(world);
                if (hasCustomName()) {
                    dragon.setCustomName(getCustomName());
                }
                dragon.setVariant(getEggType().ordinal());
                dragon.setGender(getRNG().nextBoolean());
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setHunger(50);
                if (!world.isRemote) {
                    world.addEntity(dragon);
                }
                if (hasCustomName()) {
                    dragon.setCustomName(getCustomName());
                }
                dragon.setTamed(true);
                dragon.setOwnerId(getOwnerId());
                world.playSound(getPosX(), getPosY() + getEyeHeight(), getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, getSoundCategory(), 2.5F, 1.0F, false);
                world.playSound(getPosX(), getPosY() + getEyeHeight(), getPosZ(), IafSoundRegistry.EGG_HATCH, getSoundCategory(), 2.5F, 1.0F, false);
                remove();
            }
        }
        if (getEggType().dragonType == ICE) {
            if (world.getBlockState(pos).getMaterial() == Material.WATER && getRNG().nextInt(500) == 0) {
                world.setBlockState(pos, IafBlockRegistry.EGG_IN_ICE.getDefaultState());
                world.playSound(getPosX(), getPosY() + getEyeHeight(), getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, getSoundCategory(), 2.5F, 1.0F, false);
                if (world.getBlockState(pos).getBlock() instanceof BlockEggInIce) {
                    ((TileEntityEggInIce) world.getTileEntity(pos)).type = getEggType();
                    ((TileEntityEggInIce) world.getTileEntity(pos)).ownerUUID = getOwnerId();
                }
                remove();
            }
        }
        if (getEggType().dragonType == LIGHTNING) {
            boolean flag;
            BlockPos.Mutable blockpos$pooledmutable = new BlockPos.Mutable(getPosX(), getPosY(), getPosZ()) ;
            flag = world.isRainingAt(blockpos$pooledmutable) || world.isRainingAt(blockpos$pooledmutable.setPos(getPosX(), getPosY() + (double)size.height, getPosZ()));
            if (world.canSeeSky(getPosition().up()) && flag) {
                setDragonAge(getDragonAge() + 1);
            }
            if (getDragonAge() > IafConfig.dragonEggTime) {
                EntityLightningDragon dragon = new EntityLightningDragon(world);
                if (hasCustomName()) {
                    dragon.setCustomName(getCustomName());
                }
                dragon.setVariant(getEggType().ordinal() - 8);
                dragon.setGender(getRNG().nextBoolean());
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setHunger(50);
                if (!world.isRemote) {
                    world.addEntity(dragon);
                }
                if (hasCustomName()) {
                    dragon.setCustomName(getCustomName());
                }
                dragon.setTamed(true);
                dragon.setOwnerId(getOwnerId());
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
                lightningboltentity.setPosition(getPosX(), getPosY(), getPosZ());
                lightningboltentity.setEffectOnly(true);
                if(!world.isRemote){
                    world.addEntity(lightningboltentity);
                }
                world.playSound(getPosX(), getPosY() + getEyeHeight(), getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, getSoundCategory(), 2.5F, 1.0F, false);
                world.playSound(getPosX(), getPosY() + getEyeHeight(), getPosZ(), IafSoundRegistry.EGG_HATCH, getSoundCategory(), 2.5F, 1.0F, false);
                remove();


            }
        }
    }
}