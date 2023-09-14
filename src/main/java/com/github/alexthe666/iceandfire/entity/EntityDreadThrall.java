package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityDreadThrall extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHasArmorVariant {

    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_HEAD = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_CHEST = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_LEGS = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CUSTOM_ARMOR_FEET = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CUSTOM_ARMOR_INDEX = SynchedEntityData.defineId(EntityDreadThrall.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadThrall(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IDreadMob.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.targetSelector.addGoal(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(LivingEntity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 20.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 2.0D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 128.0D)
            //ARMOR
            .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CUSTOM_ARMOR_INDEX, Integer.valueOf(0));
        this.entityData.define(CUSTOM_ARMOR_HEAD, Boolean.valueOf(false));
        this.entityData.define(CUSTOM_ARMOR_CHEST, Boolean.valueOf(false));
        this.entityData.define(CUSTOM_ARMOR_LEGS, Boolean.valueOf(false));
        this.entityData.define(CUSTOM_ARMOR_FEET, Boolean.valueOf(false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = level().getBlockState(this.blockPosition().below());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, belowBlock), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getBoundingBox().minY, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
        if (this.getMainHandItem().getItem() == Items.BOW) {
            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BONE));
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, @NotNull DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(randomSource, difficulty);
        if (random.nextFloat() < 0.75F) {
            double chance = random.nextFloat();
            if (chance < 0.0025F) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_SWORD.get()));
            }
            if (chance < 0.01F) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            }
            if (chance < 0.1F) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            if (chance < 0.75F) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.DREAD_SWORD.get()));
            }
        }
        if (random.nextFloat() < 0.75F) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            setCustomArmorHead(random.nextInt(8) != 0);
        }
        if (random.nextFloat() < 0.75F) {
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            setCustomArmorChest(random.nextInt(8) != 0);
        }
        if (random.nextFloat() < 0.75F) {
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            setCustomArmorLegs(random.nextInt(8) != 0);
        }
        if (random.nextFloat() < 0.75F) {
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
            setCustomArmorFeet(random.nextInt(8) != 0);
        }
        setBodyArmorVariant(random.nextInt(8));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.populateDefaultEquipmentSlots(worldIn.getRandom(), difficultyIn);
        return data;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ArmorVariant", getBodyArmorVariant());
        compound.putBoolean("HasCustomHelmet", hasCustomArmorHead());
        compound.putBoolean("HasCustomChestplate", hasCustomArmorChest());
        compound.putBoolean("HasCustomLeggings", hasCustomArmorLegs());
        compound.putBoolean("HasCustomBoots", hasCustomArmorFeet());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setBodyArmorVariant(compound.getInt("ArmorVariant"));
        setCustomArmorHead(compound.getBoolean("HasCustomHelmet"));
        setCustomArmorChest(compound.getBoolean("HasCustomChestplate"));
        setCustomArmorLegs(compound.getBoolean("HasCustomLeggings"));
        setCustomArmorFeet(compound.getBoolean("HasCustomBoots"));
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    public boolean hasCustomArmorHead() {
        return this.entityData.get(CUSTOM_ARMOR_HEAD).booleanValue();
    }

    public void setCustomArmorHead(boolean head) {
        this.entityData.set(CUSTOM_ARMOR_HEAD, head);
    }

    public boolean hasCustomArmorChest() {
        return this.entityData.get(CUSTOM_ARMOR_CHEST).booleanValue();
    }

    public void setCustomArmorChest(boolean head) {
        this.entityData.set(CUSTOM_ARMOR_CHEST, head);
    }

    public boolean hasCustomArmorLegs() {
        return this.entityData.get(CUSTOM_ARMOR_LEGS).booleanValue();
    }

    public void setCustomArmorLegs(boolean head) {
        this.entityData.set(CUSTOM_ARMOR_LEGS, head);
    }

    public boolean hasCustomArmorFeet() {
        return this.entityData.get(CUSTOM_ARMOR_FEET).booleanValue();
    }

    public void setCustomArmorFeet(boolean head) {
        this.entityData.set(CUSTOM_ARMOR_FEET, head);
    }

    @Override
    public int getBodyArmorVariant() {
        return this.entityData.get(CUSTOM_ARMOR_INDEX).intValue();
    }

    @Override
    public void setBodyArmorVariant(int variant) {
        this.entityData.set(CUSTOM_ARMOR_INDEX, variant);
    }

    @Override
    public int getLegArmorVariant() {
        return 0;
    }

    @Override
    public void setLegArmorVariant(int variant) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SPAWN};
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean shouldFear() {
        return true;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.STRAY_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.STRAY_STEP, 0.15F, 1.0F);
    }

}