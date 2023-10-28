package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GhostAICharge;
import com.github.alexthe666.iceandfire.entity.ai.GhostPathNavigator;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityGhost extends Monster implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IBlacklistedFromStatues, IHasCustomizableAttributes {

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DAYTIME_MODE = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WAS_FROM_CHEST = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DAYTIME_COUNTER = SynchedEntityData.defineId(EntityGhost.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;


    public EntityGhost(EntityType<EntityGhost> type, Level worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
        this.moveControl = new MoveHelper(this);
    }


    @Override
    protected @NotNull ResourceLocation getDefaultLootTable() {
        return this.wasFromChest() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GHOST_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.GHOST_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GHOST_DIE;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.ghostMaxHealth)
            //FOLLOW_RANGE
            .add(Attributes.FOLLOW_RANGE, 64D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.15D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.ghostAttackStrength)
            //ARMOR
            .add(Attributes.ARMOR, 1D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.ghostMaxHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.ghostAttackStrength);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != MobEffects.POISON && potioneffectIn.getEffect() != MobEffects.WITHER && super.canBeAffected(potioneffectIn);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return super.isInvulnerableTo(source) || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CACTUS)
            || source.is(DamageTypes.DROWN) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypes.SWEET_BERRY_BUSH);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return new GhostPathNavigator(this, worldIn);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean moving) {
        this.entityData.set(CHARGING, moving);
    }

    public boolean isDaytimeMode() {
        return this.entityData.get(IS_DAYTIME_MODE);
    }

    public void setDaytimeMode(boolean moving) {
        this.entityData.set(IS_DAYTIME_MODE, moving);
    }

    public boolean wasFromChest() {
        return this.entityData.get(WAS_FROM_CHEST);
    }

    public void setFromChest(boolean moving) {
        this.entityData.set(WAS_FROM_CHEST, moving);
    }


    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new GhostAICharge(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F) {
            @Override
            public boolean canContinueToUse() {
                if (this.lookAt != null && this.lookAt instanceof Player && ((Player) this.lookAt).isCreative()) {
                    return false;
                }
                return super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D) {
            @Override
            public boolean canUse() {
                interval = 60;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity.isAlive();
            }
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && DragonUtils.isVillager(entity);
            }
        }));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.noPhysics = true;
        if (!level().isClientSide) {
            boolean day = isSunBurnTick() && !this.wasFromChest();
            if (day) {
                if (!this.isDaytimeMode()) {
                    this.setAnimation(ANIMATION_SCARE);
                }
                this.setDaytimeMode(true);
            } else {
                this.setDaytimeMode(false);
                this.setDaytimeCounter(0);
            }
            if (isDaytimeMode()) {
                this.setDeltaMovement(Vec3.ZERO);
                this.setDaytimeCounter(this.getDaytimeCounter() + 1);
                if(getDaytimeCounter() >= 100){
                    this.setInvisible(true);
                }
            }else{
                this.setInvisible(this.hasEffect(MobEffects.INVISIBILITY));
                this.setDaytimeCounter(0);
            }
        } else {
            if (this.getAnimation() == ANIMATION_SCARE && this.getAnimationTick() == 3 && !this.isHauntedShoppingList() && random.nextInt(3) == 0) {
                this.playSound(IafSoundRegistry.GHOST_JUMPSCARE, this.getSoundVolume(), this.getVoicePitch());
                if (level().isClientSide) {
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Ghost_Appearance, this.getX(), this.getY(), this.getZ(), this.getId(), 0, 0);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_HIT && this.getTarget() != null) {
            if (this.distanceTo(this.getTarget()) < 1.4D && this.getAnimationTick() >= 4 && this.getAnimationTick() < 6) {
                this.playSound(IafSoundRegistry.GHOST_ATTACK, this.getSoundVolume(), this.getVoicePitch());
                this.doHurtTarget(this.getTarget());
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean isNoAi() {
        return this.isDaytimeMode() || super.isNoAi();
    }

    @Override
    public boolean isSilent() {
        return this.isDaytimeMode() || super.isSilent();
    }

    @Override
    protected boolean isSunBurnTick() {
        if (this.level().isDay() && !this.level().isClientSide) {
            float f = this.level().getBrightness(LightLayer.BLOCK, this.blockPosition());
            BlockPos blockpos = this.getVehicle() instanceof Boat ? (new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ())).above() : new BlockPos(this.getBlockX(), this.getBlockY() + 4, this.getBlockZ());
            return f > 0.5F && this.level().canSeeSky(blockpos);
        }

        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack != null && itemstack.getItem() == IafItemRegistry.MANUSCRIPT.get() && !this.isHauntedShoppingList()) {
            this.setColor(-1);
            this.playSound(IafSoundRegistry.BESTIARY_PAGE, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        float f4;
        if (this.isDaytimeMode()) {
            super.travel(Vec3.ZERO);
            return;
        }
        super.travel(vec);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.random.nextInt(3));
        if (random.nextInt(200) == 0) {
            this.setColor(-1);
        }

        return spawnDataIn;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(COLOR, 0);
        this.getEntityData().define(CHARGING, false);
        this.getEntityData().define(IS_DAYTIME_MODE, false);
        this.getEntityData().define(WAS_FROM_CHEST, false);
        this.getEntityData().define(DAYTIME_COUNTER, 0);
    }

    public int getColor() {
        return Mth.clamp(this.getEntityData().get(COLOR).intValue(), -1, 2);
    }

    public void setColor(int color) {
        this.getEntityData().set(COLOR, color);
    }

    public int getDaytimeCounter() {
        return this.getEntityData().get(DAYTIME_COUNTER).intValue();
    }

    public void setDaytimeCounter(int counter) {
        this.getEntityData().set(DAYTIME_COUNTER, counter);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setColor(compound.getInt("Color"));
        this.setDaytimeMode(compound.getBoolean("DaytimeMode"));
        this.setDaytimeCounter(compound.getInt("DaytimeCounter"));
        this.setFromChest(compound.getBoolean("FromChest"));

        this.setConfigurableAttributes();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Color", this.getColor());
        compound.putBoolean("DaytimeMode", this.isDaytimeMode());
        compound.putInt("DaytimeCounter", this.getDaytimeCounter());
        compound.putBoolean("FromChest", this.wasFromChest());

    }

    public boolean isHauntedShoppingList() {
        return this.getColor() == -1;
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
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_SCARE, ANIMATION_HIT};
    }


    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return false;
    }

    class MoveHelper extends MoveControl {
        EntityGhost ghost;

        public MoveHelper(EntityGhost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 vec3d = new Vec3(this.getWantedX() - ghost.getX(), this.getWantedY() - ghost.getY(), this.getWantedZ() - ghost.getZ());
                double d0 = vec3d.length();
                double edgeLength = ghost.getBoundingBox().getSize();
                if (d0 < edgeLength) {
                    this.operation = Operation.WAIT;
                    ghost.setDeltaMovement(ghost.getDeltaMovement().scale(0.5D));
                } else {
                    ghost.setDeltaMovement(ghost.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.5D * 0.05D / d0)));
                    if (ghost.getTarget() == null) {
                        Vec3 vec3d1 = ghost.getDeltaMovement();
                        ghost.setYRot(-((float) Mth.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI));
                        ghost.yBodyRot = ghost.getYRot();
                    } else {
                        double d4 = ghost.getTarget().getX() - ghost.getX();
                        double d5 = ghost.getTarget().getZ() - ghost.getZ();
                        ghost.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
                        ghost.yBodyRot = ghost.getYRot();
                    }
                }
            }
        }
    }
}
