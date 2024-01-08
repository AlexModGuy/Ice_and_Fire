package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDragonEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    protected static final EntityDataAccessor<java.util.Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DRAGON_TYPE = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRAGON_AGE = SynchedEntityData.defineId(EntityDragonEgg.class, EntityDataSerializers.INT);

    public EntityDragonEgg(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
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
            IceAndFire.LOGGER.error("An error occurred while trying to read the NBT data of a dragon egg", e);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setEggType(EnumDragonEgg.values()[tag.getInt("Color")]);
        this.setDragonAge(tag.getInt("DragonAge"));
        String s;

        if (tag.contains("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            UUID converedUUID = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s1);
            s = converedUUID == null ? s1 : converedUUID.toString();
        }
        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DRAGON_TYPE, 0);
        this.getEntityData().define(DRAGON_AGE, 0);
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
        return EnumDragonEgg.values()[this.getEntityData().get(DRAGON_TYPE)];
    }

    public void setEggType(EnumDragonEgg newtype) {
        this.getEntityData().set(DRAGON_TYPE, newtype.ordinal());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null && super.isInvulnerableTo(i);
    }

    public int getDragonAge() {
        return this.getEntityData().get(DRAGON_AGE);
    }

    public void setDragonAge(int i) {
        this.getEntityData().set(DRAGON_AGE, i);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.setAirSupply(200);
            updateEggCondition();
        }
    }

    public void updateEggCondition() {
        DragonType dragonType = getEggType().dragonType;

        if (dragonType == DragonType.FIRE) {
            if (level().getBlockState(blockPosition()).isBurning(level(), blockPosition())) {
                setDragonAge(getDragonAge() + 1);
            }
        } else if (dragonType == DragonType.ICE) {
            BlockState state = level().getBlockState(blockPosition());

            if (state.is(Blocks.WATER) && getRandom().nextInt(500) == 0) {
                level().setBlockAndUpdate(blockPosition(), IafBlockRegistry.EGG_IN_ICE.get().defaultBlockState());
                level().playLocalSound(getX(), getY() + getEyeHeight(), getZ(), SoundEvents.GLASS_BREAK, getSoundSource(), 2.5F, 1.0F, false);

                if (level().getBlockEntity(blockPosition()) instanceof TileEntityEggInIce eggInIce) {
                    eggInIce.type = getEggType();
                    eggInIce.ownerUUID = getOwnerId();
                }

                remove(Entity.RemovalReason.DISCARDED);
            }
        } else if (dragonType == DragonType.LIGHTNING) {
            BlockPos.MutableBlockPos mutablePosition = new BlockPos.MutableBlockPos(getX(), getY(), getZ());
            boolean isRainingAt = level().isRainingAt(mutablePosition) || level().isRainingAt(mutablePosition.set(getX(), getY() + (double) getBbHeight(), getZ()));

            if (level().canSeeSky(blockPosition().above()) && isRainingAt) {
                setDragonAge(getDragonAge() + 1);
            }
        }

        if (getDragonAge() > IafConfig.dragonEggTime) {
            level().setBlockAndUpdate(blockPosition(), Blocks.AIR.defaultBlockState());
            EntityDragonBase dragon = dragonType.getEntity().create(level());

            if (hasCustomName()) {
                dragon.setCustomName(getCustomName());
            }

            if (dragonType == DragonType.LIGHTNING) {
                dragon.setVariant(getEggType().ordinal() - 8);
            } else {
                dragon.setVariant(getEggType().ordinal());
            }

            dragon.setGender(getRandom().nextBoolean());
            dragon.setPos(blockPosition().getX() + 0.5, blockPosition().getY() + 1, blockPosition().getZ() + 0.5);
            dragon.setHunger(50);

            if (!level().isClientSide()) {
                level().addFreshEntity(dragon);
            }

            if (hasCustomName()) { // FIXME :: Why do this again?
                dragon.setCustomName(getCustomName());
            }

            dragon.setTame(true);
            dragon.setOwnerUUID(getOwnerId());

            if (dragonType == DragonType.LIGHTNING) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level());
                bolt.setPos(getX(), getY(), getZ());
                bolt.setVisualOnly(true);

                if (!level().isClientSide()) {
                    level().addFreshEntity(bolt);
                }

                level().playLocalSound(getX(), getY() + getEyeHeight(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, getSoundSource(), 2.5F, 1.0F, false);
            } else if (dragonType == DragonType.FIRE) {
                level().playLocalSound(getX(), getY() + getEyeHeight(), getZ(), SoundEvents.FIRE_EXTINGUISH, getSoundSource(), 2.5F, 1.0F, false);
            }

            level().playLocalSound(getX(), getY() + getEyeHeight(), getZ(), IafSoundRegistry.EGG_HATCH, getSoundSource(), 2.5F, 1.0F, false);
            remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {

    }

    @Override
    public boolean hurt(@NotNull DamageSource var1, float var2) {
        if (var1.is(DamageTypeTags.IS_FIRE) && getEggType().dragonType == DragonType.FIRE)
            return false;
        if (!this.level().isClientSide && !var1.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !isRemoved()) {
            this.spawnAtLocation(this.getItem().getItem(), 1);
        }
        this.remove(RemovalReason.KILLED);
        return true;
    }

    private ItemStack getItem() {
        return switch (getEggType().ordinal()) {
            default -> new ItemStack(IafItemRegistry.DRAGONEGG_RED.get());
            case 1 -> new ItemStack(IafItemRegistry.DRAGONEGG_GREEN.get());
            case 2 -> new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE.get());
            case 3 -> new ItemStack(IafItemRegistry.DRAGONEGG_GRAY.get());
            case 4 -> new ItemStack(IafItemRegistry.DRAGONEGG_BLUE.get());
            case 5 -> new ItemStack(IafItemRegistry.DRAGONEGG_WHITE.get());
            case 6 -> new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE.get());
            case 7 -> new ItemStack(IafItemRegistry.DRAGONEGG_SILVER.get());
            case 8 -> new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC.get());
            case 9 -> new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST.get());
            case 10 -> new ItemStack(IafItemRegistry.DRAGONEGG_COPPER.get());
            case 11 -> new ItemStack(IafItemRegistry.DRAGONEGG_BLACK.get());
        };
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(Player player) {
        this.setOwnerId(player.getUUID());
    }

    @Override
    public boolean isMobDead() {
        return true;
    }
}
