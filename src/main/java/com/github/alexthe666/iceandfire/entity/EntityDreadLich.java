package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.ai.DreadLichAIStrife;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityDreadLich extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, IRangedAttackMob {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dread_lich"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDreadLich.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MINION_COUNT = EntityDataManager.createKey(EntityDreadLich.class, DataSerializers.VARINT);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_SUMMON = Animation.create(15);
    private final DreadLichAIStrife aiArrowAttack = new DreadLichAIStrife(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.0D, false);
    private int animationTick;
    private Animation currentAnimation;
    private int fireCooldown = 0;
    private int minionCooldown = 0;

    public EntityDreadLich(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(MINION_COUNT, Integer.valueOf(0));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            Block belowBlock = world.getBlockState(this.getPosition().down()).getBlock();
            if (belowBlock != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.getEntityBoundingBox().minY, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(belowBlock));
                }
            }
        }
        if (this.world.isRemote && this.getAnimation() == ANIMATION_SUMMON) {
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            IceAndFire.PROXY.spawnParticle("dread_torch", this.posX + (double) f1 * 0.6D, this.posY + 1.8D, this.posZ + (double) f2 * 0.6D, d0, d1, d2);
            IceAndFire.PROXY.spawnParticle("dread_torch", this.posX - (double) f1 * 0.6D, this.posY + 1.8D, this.posZ - (double) f2 * 0.6D, d0, d1, d2);
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (minionCooldown > 0) {
            minionCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.lich_staff));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        this.setAnimation(ANIMATION_SPAWN);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setVariant(rand.nextInt(5));
        this.setCombatTask();
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("MinionCount", this.getMinionCount());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setMinionCount(compound.getInteger("MinionCount"));
        this.setCombatTask();
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getMinionCount() {
        return this.dataManager.get(MINION_COUNT).intValue();
    }

    public void setMinionCount(int minions) {
        this.dataManager.set(MINION_COUNT, minions);
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SPAWN, ANIMATION_SUMMON};
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
    public Entity getCommander() {
        return null;
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() == IafItemRegistry.lich_staff) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.playSound(IafSoundRegistry.DREAD_LICH_SUMMON, this.getSoundVolume(), this.getSoundPitch());
            EntityLiving minion = getRandomNewMinion();
            int x = (int) (this.posX) - 5 + rand.nextInt(10);
            int z = (int) (this.posZ) - 5 + rand.nextInt(10);
            double y = getHeightFromXZ(x, z);
            minion.setLocationAndAngles(x + 0.5D, y, z + 0.5D, this.rotationYaw, this.rotationPitch);
            minion.setAttackTarget(target);
            minion.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this)), null);
            if (minion instanceof EntityDreadMob) {
                ((EntityDreadMob) minion).setCommanderId(this.getUniqueID());
            }
            if (!world.isRemote) {
                world.spawnEntity(minion);
            }
            minionCooldown = 100;
            this.setMinionCount(this.getMinionCount() + 1);
            flag = true;
        }
        if (fireCooldown == 0 && !flag) {
            this.swingArm(EnumHand.MAIN_HAND);
            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(world, this, 6);
            double d0 = target.posX - this.posX;
            double d1 = target.getEntityBoundingBox().minY + (double) (target.height * 2) - skull.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            skull.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 0.0F, (float) (14 - this.world.getDifficulty().getId() * 4));
            this.world.spawnEntity(skull);
            fireCooldown = 100;
        }
    }

    private EntityLiving getRandomNewMinion() {
        float chance = rand.nextFloat();
        if (chance > 0.5F) {
            return new EntityDreadThrall(world);
        } else if (chance > 0.35F) {
            return new EntityDreadGhoul(world);
        } else if (chance > 0.15F) {
            return new EntityDreadBeast(world);
        } else {
            return new EntityDreadScuttler(world);
        }
    }

    private double getHeightFromXZ(int x, int z) {
        BlockPos thisPos = new BlockPos(x, this.posY + 7, z);
        while (world.isAirBlock(thisPos) && thisPos.getY() > 2) {
            thisPos = thisPos.down();
        }
        double height = thisPos.getY() + 1.0D;
        AxisAlignedBB bb = world.getBlockState(thisPos).getCollisionBoundingBox(world, thisPos);
        if (bb != null) {
            height = thisPos.getY() + bb.maxY;
        }
        return height;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isOnSameTeam(entityIn);
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