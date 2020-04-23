package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.ai.DreadAIRideHorse;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityDreadKnight extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dread_knight"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDreadThrall.class, DataSerializers.VARINT);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    private int animationTick;
    private Animation currentAnimation;
    public static final ItemStack SHIELD = generateShield();

    public EntityDreadKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    private static ItemStack generateShield() {
        NBTTagList patterns = new NBTTagList();
        NBTTagCompound currentPattern = new NBTTagCompound();
        currentPattern.setString("Pattern", "iceandfire.dread");
        currentPattern.setInteger("Color", EnumDyeColor.WHITE.getDyeDamage());
        patterns.appendTag(currentPattern);
        ItemStack banner = ItemBanner.makeBanner(EnumDyeColor.CYAN, patterns);
        ItemStack shield = new ItemStack(Items.SHIELD, 1);
        shield.setTagCompound(banner.getTagCompound());
        return shield;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new DreadAIRideHorse(this));
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
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
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.dread_knight_sword));
        if(rand.nextBoolean()){
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, SHIELD.copy());
        }
        setArmorVariant(rand.nextInt(3));
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
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setArmorVariant(compound.getInteger("ArmorVariant"));
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    public int getArmorVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setArmorVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
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

    public double getYOffset() {
        return -0.6D;
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

    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.75F;
    }
}