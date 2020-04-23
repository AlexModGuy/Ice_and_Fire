package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityDreadThrall extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dread_thrall"));
    private static final DataParameter<Boolean> CUSTOM_ARMOR_HEAD = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CUSTOM_ARMOR_CHEST = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CUSTOM_ARMOR_LEGS = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CUSTOM_ARMOR_FEET = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> CUSTOM_ARMOR_INDEX = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.VARINT);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    private int animationTick;
    private Animation currentAnimation;

    public EntityDreadThrall(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {IDreadMob.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new DreadAITargetNonDread(this, EntityLivingBase.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CUSTOM_ARMOR_INDEX, Integer.valueOf(0));
        this.dataManager.register(CUSTOM_ARMOR_HEAD, Boolean.valueOf(false));
        this.dataManager.register(CUSTOM_ARMOR_CHEST, Boolean.valueOf(false));
        this.dataManager.register(CUSTOM_ARMOR_LEGS, Boolean.valueOf(false));
        this.dataManager.register(CUSTOM_ARMOR_FEET, Boolean.valueOf(false));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            Block belowBlock = world.getBlockState(this.getPosition().down()).getBlock();
            if (belowBlock != Blocks.AIR) {
                for (int i = 0; i < 5; i++){
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getEntityBoundingBox().minY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(belowBlock));
                }
            }
        }
        if(this.getHeldItemMainhand().getItem() == Items.BOW){
            this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BONE));
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (rand.nextFloat() < 0.75F) {
            double chance = rand.nextFloat();
            if (chance < 0.0025F) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.dragonsteel_ice_sword));
            }
            if (chance < 0.01F) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            }
            if (chance < 0.1F) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            if (chance < 0.75F) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.dread_sword));
            }
        }
        if (rand.nextFloat() < 0.75F) {
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            setCustomArmorHead(rand.nextInt(8) != 0);
        }
        if (rand.nextFloat() < 0.75F) {
            this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            setCustomArmorChest(rand.nextInt(8) != 0);
        }
        if (rand.nextFloat() < 0.75F) {
            this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            setCustomArmorLegs(rand.nextInt(8) != 0);
        }
        if (rand.nextFloat() < 0.75F) {
            this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
            setCustomArmorFeet(rand.nextInt(8) != 0);
        }
        setArmorVariant(rand.nextInt(8));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        this.setAnimation(ANIMATION_SPAWN);
        this.setEquipmentBasedOnDifficulty(difficulty);
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

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ArmorVariant", getArmorVariant());
        compound.setBoolean("HasCustomHelmet", hasCustomArmorHead());
        compound.setBoolean("HasCustomChestplate", hasCustomArmorChest());
        compound.setBoolean("HasCustomLeggings", hasCustomArmorLegs());
        compound.setBoolean("HasCustomBoots", hasCustomArmorFeet());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setArmorVariant(compound.getInteger("ArmorVariant"));
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
        return this.dataManager.get(CUSTOM_ARMOR_HEAD).booleanValue();
    }

    public void setCustomArmorHead(boolean head) {
        this.dataManager.set(CUSTOM_ARMOR_HEAD, head);
    }

    public boolean hasCustomArmorChest() {
        return this.dataManager.get(CUSTOM_ARMOR_CHEST).booleanValue();
    }

    public void setCustomArmorChest(boolean head) {
        this.dataManager.set(CUSTOM_ARMOR_CHEST, head);
    }

    public boolean hasCustomArmorLegs() {
        return this.dataManager.get(CUSTOM_ARMOR_LEGS).booleanValue();
    }

    public void setCustomArmorLegs(boolean head) {
        this.dataManager.set(CUSTOM_ARMOR_LEGS, head);
    }

    public boolean hasCustomArmorFeet() {
        return this.dataManager.get(CUSTOM_ARMOR_FEET).booleanValue();
    }

    public void setCustomArmorFeet(boolean head) {
        this.dataManager.set(CUSTOM_ARMOR_FEET, head);
    }

    public int getArmorVariant() {
        return this.dataManager.get(CUSTOM_ARMOR_INDEX).intValue();
    }

    public void setArmorVariant(int variant) {
        this.dataManager.set(CUSTOM_ARMOR_INDEX, variant);
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

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_STRAY_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_STRAY_STEP, 0.15F, 1.0F);
    }

}