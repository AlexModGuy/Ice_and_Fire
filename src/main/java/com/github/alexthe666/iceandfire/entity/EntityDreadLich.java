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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
    }

    protected void initEntityAI() {
        this.goalSelector.addGoal(1, new EntityAISwimming(this));
        this.goalSelector.addGoal(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, new Class[] {IDreadMob.class}));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new DreadAITargetNonDread(this, LivingEntity.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity);
            }
        }));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(MINION_COUNT, Integer.valueOf(0));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            Block belowBlock = world.getBlockState(this.getPosition().down()).getBlock();
            if (belowBlock != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.world.spawnParticle(ParticleTypes.BLOCK_CRACK, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getBoundingBox().minY, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, Block.getIdFromBlock(belowBlock));
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
            IceAndFire.PROXY.spawnParticle("dread_torch", this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, d0, d1, d2);
            IceAndFire.PROXY.spawnParticle("dread_torch", this.getPosX() - (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double) f2 * 0.6D, d0, d1, d2);
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
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(IafItemRegistry.LICH_STAFF));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, @Nullable ILivingEntityData livingdata) {
        ILivingEntityData data = super.onInitialSpawn(difficulty, livingdata);
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
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("MinionCount", this.getMinionCount());
    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setMinionCount(compound.getInt("MinionCount"));
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

    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.world.isRemote && slotIn == EquipmentSlotType.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.goalSelector.removeTask(this.aiAttackOnCollide);
            this.goalSelector.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() == IafItemRegistry.LICH_STAFF) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.playSound(IafSoundRegistry.DREAD_LICH_SUMMON, this.getSoundVolume(), this.getSoundPitch());
            LivingEntity minion = getRandomNewMinion();
            int x = (int) (this.getPosX()) - 5 + rand.nextInt(10);
            int z = (int) (this.getPosZ()) - 5 + rand.nextInt(10);
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
            this.swingArm(Hand.MAIN_HAND);
            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(world, this, 6);
            double d0 = target.getPosX() - this.getPosX();
            double d1 = target.getBoundingBox().minY + (double) (target.height * 2) - skull.getPosY();
            double d2 = target.getPosZ() - this.getPosZ();
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            skull.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 0.0F, (float) (14 - this.world.getDifficulty().getId() * 4));
            this.world.spawnEntity(skull);
            fireCooldown = 100;
        }
    }

    private LivingEntity getRandomNewMinion() {
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
        BlockPos thisPos = new BlockPos(x, this.getPosY() + 7, z);
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