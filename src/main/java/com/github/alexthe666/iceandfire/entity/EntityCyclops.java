package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateCyclops;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityCyclops extends Monster implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHumanoid, IHasCustomizableAttributes {

    private static final EntityDataAccessor<Boolean> BLINDED = SynchedEntityData.defineId(EntityCyclops.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityCyclops.class, EntityDataSerializers.INT);
    public static Animation ANIMATION_STOMP;
    public static Animation ANIMATION_EATPLAYER;
    public static Animation ANIMATION_KICK;
    public static Animation ANIMATION_ROAR;
    public EntityCyclopsEye eyeEntity;
    private int animationTick;
    private Animation currentAnimation;

    public EntityCyclops(EntityType<EntityCyclops> type, Level worldIn) {
        super(type, worldIn);
        this.setMaxUpStep(2.5F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, 0.0F);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(40);
        ANIMATION_KICK = Animation.create(20);
        ANIMATION_ROAR = Animation.create(30);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.cyclopsMaxHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.cyclopsAttackStrength)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 32D)
            //ARMOR
            .add(Attributes.ARMOR, 20.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.cyclopsMaxHealth);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return new PathNavigateCyclops(this, level());
    }

    @Override
    public int getExperienceReward() {
        return 40;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new CyclopsAIAttackMelee(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                if (EntityGorgon.isStoneMob(entity))
                    return false;
                if (!DragonUtils.isAlive(entity))
                    return false;
                if (entity instanceof WaterAnimal)
                    return false;
                if (entity instanceof Player) {
                    Player playerEntity = (Player) entity;
                    if (playerEntity.isCreative() || playerEntity.isSpectator())
                        return false;
                }
                if (entity instanceof EntityCyclops)
                    return false;
                if (entity instanceof Animal) {
                    if (!(entity instanceof Wolf || entity instanceof PolarBear || entity instanceof EntityDragonBase)) {
                        return false;
                    }
                }
                return !ServerEvents.isSheep(entity);
            }
        }));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, true, new Predicate<Player>() {
            @Override
            public boolean apply(@Nullable Player entity) {
                return entity != null && !(entity.isCreative() || entity.isSpectator());
            }
        }));
        this.targetSelector.addGoal(3, new CyclopsAITargetSheepPlayers(this, Player.class, true));
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (!ServerEvents.isSheep(entityIn)) {
            entityIn.push(this);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        int attackDescision = this.getRandom().nextInt(3);
        if (attackDescision == 0) {
            this.setAnimation(ANIMATION_STOMP);
            return true;
        } else if (attackDescision == 1) {
            if (!entityIn.hasPassenger(this)
                && entityIn.getBbWidth() < 1.95F
                && !(entityIn instanceof EntityDragonBase)
                && !entityIn.getType().is((ForgeRegistries.ENTITY_TYPES.tags().createTagKey(IafTagRegistry.CYCLOPS_UNLIFTABLES)))) {
                this.setAnimation(ANIMATION_EATPLAYER);
                entityIn.stopRiding();
                entityIn.startRiding(this, true);
            } else {
                this.setAnimation(ANIMATION_STOMP);
            }
            return true;
        } else {
            this.setAnimation(ANIMATION_KICK);
            return true;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BLINDED, Boolean.FALSE);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Blind", this.isBlinded());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBlinded(compound.getBoolean("Blind"));
        this.setVariant(compound.getInt("Variant"));
        this.setConfigurableAttributes();
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean isBlinded() {
        return this.entityData.get(BLINDED).booleanValue();
    }

    public void setBlinded(boolean blind) {
        this.entityData.set(BLINDED, blind);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger)) {
            passenger.setDeltaMovement(0, passenger.getDeltaMovement().y, 0);
            this.setAnimation(ANIMATION_EATPLAYER);
            double raiseUp = this.getAnimationTick() < 10 ? 0 : Math.min((this.getAnimationTick() * 3 - 30) * 0.2, 5.2F);
            float pullIn = this.getAnimationTick() < 15 ? 0 : Math.min((this.getAnimationTick() - 15) * 0.15F, 0.75F);
            yBodyRot = getYRot();
            this.setYRot(0);
            float radius = -2.75F + pullIn;
            float angle = (0.01745329251F * this.yBodyRot) + 3.15F;
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            double extraY = raiseUp;
            passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
            if (this.getAnimationTick() == 32) {
                passenger.hurt(this.level().damageSources().mobAttack(this), (float) IafConfig.cyclopsBiteStrength);
                passenger.stopRiding();
            }
        }
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.getAnimation() == ANIMATION_EATPLAYER) {
            super.travel(vec.multiply(0, 0, 0));
            return;
        }
        super.travel(vec);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (eyeEntity == null) {
            eyeEntity = new EntityCyclopsEye(this, 0.2F, 0, 7.4F, 1.2F, 0.6F, 1);
            eyeEntity.copyPosition(this);
        }
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        if (this.isBlinded() && this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 6) {
            this.setTarget(null);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.CYCLOPS_BLINDED, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_EATPLAYER && this.getAnimationTick() == 25) {
            this.playSound(IafSoundRegistry.CYCLOPS_BITE, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 12D && this.getAnimationTick() == 14) {
            this.getTarget().hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_KICK && this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 14D && this.getAnimationTick() == 12) {
            this.getTarget().hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            if (this.getTarget() != null)
                this.getTarget().knockback(2, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());

        }
        if (this.getAnimation() != ANIMATION_EATPLAYER && this.getTarget() != null && !this.getPassengers().isEmpty() && this.getPassengers().contains(this.getTarget())) {
            this.setAnimation(ANIMATION_EATPLAYER);
        }
        if (this.getAnimation() == NO_ANIMATION && this.getTarget() != null && this.getRandom().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 14) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRandom().nextGaussian() * 0.07D;
                double motionY = getRandom().nextGaussian() * 0.07D;
                double motionZ = getRandom().nextGaussian() * 0.07D;
                float radius = 0.75F * -2F;
                float angle = (0.01745329251F * this.yBodyRot) + i1 * 1F;
                double extraX = radius * Mth.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * Mth.cos(angle);

                BlockState BlockState = this.level().getBlockState(BlockPos.containing(this.getX() + extraX, this.getY() + extraY - 1, this.getZ() + extraZ));
                if (BlockState.isAir()) {
                    if (level().isClientSide) {
                        level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockState), this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }

        AnimationHandler.INSTANCE.updateAnimations(this);

        if (eyeEntity == null) {
            eyeEntity = new EntityCyclopsEye(this, 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);
            eyeEntity.copyPosition(this);
        }

        EntityUtil.updatePart(eyeEntity, this);

        breakBlock();
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(4));
        return spawnDataIn;
    }

    public void breakBlock() {
        if (IafConfig.cyclopsGriefing) {
            for (int a = (int) Math.round(this.getBoundingBox().minX) - 1; a <= (int) Math.round(this.getBoundingBox().maxX) + 1; a++) {
                for (int b = (int) Math.round(this.getBoundingBox().minY) + 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getBoundingBox().minZ) - 1; c <= (int) Math.round(this.getBoundingBox().maxZ) + 1; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = level().getBlockState(pos);
                        Block block = state.getBlock();
                        if (!state.isAir() && !state.getShape(level(), pos).isEmpty() && !(block instanceof BushBlock) && block != Blocks.BEDROCK && (state.getBlock() instanceof LeavesBlock || state.is(BlockTags.LOGS))) {
                            this.getDeltaMovement().scale(0.6D);
                            if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, a, b, c))) continue;
                            if (block != Blocks.AIR) {
                                if (!level().isClientSide) {
                                    level().destroyBlock(pos, true);
                                }
                            }
                        }
                    }
                }
            }
        }
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
    public void remove(Entity.@NotNull RemovalReason reason) {
        if (eyeEntity != null) {
            eyeEntity.remove(reason);
        }
        super.remove(reason);
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
        return new Animation[]{NO_ANIMATION, ANIMATION_STOMP, ANIMATION_EATPLAYER, ANIMATION_KICK, ANIMATION_ROAR};
    }

    public boolean isBlinking() {
        return this.tickCount % 50 > 40 && !this.isBlinded();
    }


    public void onHitEye(DamageSource source, float damage) {
        if (!this.isBlinded()) {
            this.setBlinded(true);
            this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(6F);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            this.setAnimation(ANIMATION_ROAR);
            this.hurt(source, damage * 3);
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.CYCLOPS_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.CYCLOPS_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.CYCLOPS_DIE;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return !this.isBlinded();
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

}
