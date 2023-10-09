package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.DreadAITargetNonDread;
import com.github.alexthe666.iceandfire.entity.ai.DreadLichAIStrife;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
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
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityDreadLich extends EntityDreadMob implements IAnimatedEntity, IVillagerFear, IAnimalFear, RangedAttackMob {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDreadLich.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MINION_COUNT = SynchedEntityData.defineId(EntityDreadLich.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_SPAWN = Animation.create(40);
    public static Animation ANIMATION_SUMMON = Animation.create(15);
    private final DreadLichAIStrife aiArrowAttack = new DreadLichAIStrife(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.0D, false);
    private int animationTick;
    private Animation currentAnimation;
    private int fireCooldown = 0;
    private int minionCooldown = 0;

    public EntityDreadLich(EntityType<? extends EntityDreadMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static boolean canLichSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        BlockPos blockpos = pos.below();
        return reason == MobSpawnType.SPAWNER || worldIn.getBlockState(blockpos).isValidSpawn(worldIn, blockpos, typeIn) && randomIn.nextInt(IafConfig.lichSpawnChance) == 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
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
            .add(Attributes.MAX_HEALTH, 50.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 1.0D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 128.0D)
            //ARMOR
            .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(MINION_COUNT, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() == ANIMATION_SPAWN && this.getAnimationTick() < 30) {
            BlockState belowBlock = level().getBlockState(this.blockPosition().below());
            if (belowBlock.getBlock() != Blocks.AIR) {
                for (int i = 0; i < 5; i++) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, belowBlock), this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getBoundingBox().minY, this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            this.setDeltaMovement(0, this.getDeltaMovement().y, this.getDeltaMovement().z);

        }
        if (this.level().isClientSide && this.getAnimation() == ANIMATION_SUMMON) {
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            float f = this.yBodyRot * 0.017453292F + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, d0, d1, d2);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, this.getX() - (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double) f2 * 0.6D, d0, d1, d2);
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        if (minionCooldown > 0) {
            minionCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(IafItemRegistry.LICH_STAFF.get()));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setAnimation(ANIMATION_SPAWN);
        this.populateDefaultEquipmentSlots(worldIn.getRandom(), difficultyIn);
        this.setVariant(random.nextInt(5));
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("MinionCount", this.getMinionCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setMinionCount(compound.getInt("MinionCount"));
        this.setCombatTask();
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getMinionCount() {
        return this.entityData.get(MINION_COUNT).intValue();
    }

    public void setMinionCount(int minions) {
        this.entityData.set(MINION_COUNT, minions);
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

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {
        super.setItemSlot(slotIn, stack);

        if (!this.level().isClientSide && slotIn == EquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.level()!= null && !this.level().isClientSide) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.getItem() == IafItemRegistry.LICH_STAFF.get()) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        boolean flag = false;
        if (this.getMinionCount() < 5 && minionCooldown == 0) {
            this.setAnimation(ANIMATION_SUMMON);
            this.playSound(IafSoundRegistry.DREAD_LICH_SUMMON, this.getSoundVolume(), this.getVoicePitch());
            Mob minion = getRandomNewMinion();
            int x = (int) (this.getX()) - 5 + random.nextInt(10);
            int z = (int) (this.getZ()) - 5 + random.nextInt(10);
            double y = getHeightFromXZ(x, z);
            minion.moveTo(x + 0.5D, y, z + 0.5D, this.getYRot(), this.getXRot());
            minion.setTarget(target);
            Level currentLevel = level();
            if (currentLevel instanceof ServerLevelAccessor) {
                minion.finalizeSpawn((ServerLevelAccessor) currentLevel, currentLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            if (minion instanceof EntityDreadMob) {
                ((EntityDreadMob) minion).setCommanderId(this.getUUID());
            }
            if (!currentLevel.isClientSide) {
                currentLevel.addFreshEntity(minion);
            }
            minionCooldown = 100;
            this.setMinionCount(this.getMinionCount() + 1);
            flag = true;
        }
        if (fireCooldown == 0 && !flag) {
            this.swing(InteractionHand.MAIN_HAND);
            this.playSound(SoundEvents.ZOMBIE_INFECT, this.getSoundVolume(), this.getVoicePitch());
            EntityDreadLichSkull skull = new EntityDreadLichSkull(IafEntityRegistry.DREAD_LICH_SKULL.get(), level(), this,
                6);
            double d0 = target.getX() - this.getX();
            double d1 = target.getBoundingBox().minY + target.getBbHeight() * 2 - skull.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt((float) (d0 * d0 + d2 * d2));
            skull.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 0.0F, 14 - this.level().getDifficulty().getId() * 4);
            this.level().addFreshEntity(skull);
            fireCooldown = 100;
        }
    }

    private Mob getRandomNewMinion() {
        float chance = random.nextFloat();
        if (chance > 0.5F) {
            return new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL.get(), level());
        } else if (chance > 0.35F) {
            return new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL.get(), level());
        } else if (chance > 0.15F) {
            return new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST.get(), level());
        } else {
            return new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER.get(), level());
        }
    }

    private double getHeightFromXZ(int x, int z) {
        BlockPos thisPos = new BlockPos(x, (int) (this.getY() + 7), z);
        while (level().isEmptyBlock(thisPos) && thisPos.getY() > 2) {
            thisPos = thisPos.below();
        }
        double height = thisPos.getY() + 1.0D;
        return height;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isAlliedTo(entityIn);
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