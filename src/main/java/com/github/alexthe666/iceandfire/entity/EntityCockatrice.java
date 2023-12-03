package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCockatrice extends TamableAnimal implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHasCustomizableAttributes {

    public static final Animation ANIMATION_JUMPAT = Animation.create(30);
    public static final Animation ANIMATION_WATTLESHAKE = Animation.create(20);
    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    public static final Animation ANIMATION_EAT = Animation.create(20);
    public static final float VIEW_RADIUS = 0.6F;
    private static final EntityDataAccessor<Boolean> HEN = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STARING = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAMING_PLAYER = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAMING_LEVEL = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityCockatrice.class, EntityDataSerializers.INT);
    private final CockatriceAIStareAttack aiStare;
    private final MeleeAttackGoal aiMelee;
    public float sitProgress;
    public float stareProgress;
    public int ticksStaring = 0;
    public HomePosition homePos;
    public boolean hasHomePosition = false;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isSitting;
    private boolean isStaring;
    private boolean isMeleeMode = false;
    private LivingEntity targetedEntity;
    private int clientSideAttackTime;

    public EntityCockatrice(EntityType<EntityCockatrice> type, Level worldIn) {
        super(type, worldIn);
        // Fix for some mods causing weird crashes
        this.lookControl = new IAFLookHelper(this);
        aiStare = new CockatriceAIStareAttack(this, 1.0D, 0, 15.0F);
        aiMelee = new EntityAIAttackMeleeNoCooldown(this, 1.5D, false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.cockatriceMaxHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.4D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            //ARMOR
            .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.cockatriceMaxHealth);
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new CockatriceAIFollowOwner(this, 1.0D, 7.0F, 2.0F));
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LivingEntity.class, 14.0F, 1.0D, 1.0D, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                if (entity instanceof Player) {
                    return !((Player) entity).isCreative() && !entity.isSpectator();
                } else {
                    return ServerEvents.doesScareCockatrice(entity) && !ServerEvents.isChicken(entity);
                }
            }
        }));
        this.goalSelector.addGoal(4, new CockatriceAIWander(this, 1.0D));
        this.goalSelector.addGoal(5, new CockatriceAIAggroLook(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new CockatriceAITargetItems(this, false));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(5, new CockatriceAITarget(this, LivingEntity.class, true, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                if (entity instanceof Player) {
                    return !((Player) entity).isCreative() && !entity.isSpectator();
                } else {
                    return ((entity instanceof Enemy) && EntityCockatrice.this.isTame() && !(entity instanceof Creeper) && !(entity instanceof ZombifiedPiglin) && !(entity instanceof EnderMan) ||
                        ServerEvents.isCockatriceTarget(entity) && !ServerEvents.isChicken(entity));
                }
            }
        }));
    }

    @Override
    public boolean hasRestriction() {
        return this.hasHomePosition &&
            this.getCommand() == 3 &&
            getHomeDimensionName().equals(DragonUtils.getDimensionName(this.level()))
            || super.hasRestriction();
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    public @NotNull BlockPos getRestrictCenter() {
        return this.hasHomePosition && this.getCommand() == 3 && homePos != null ? homePos.getPosition() : super.getRestrictCenter();
    }

    @Override
    public float getRestrictRadius() {
        return 30.0F;
    }

    public String getHomeDimensionName() {
        return this.homePos == null ? "" : homePos.getDimension();
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (ServerEvents.isChicken(entityIn)) {
            return true;
        }
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() != null && ServerEvents.doesScareCockatrice(source.getEntity())) {
            damage *= 5;
        }
        if (source == this.level().damageSources().inWall()) {
            return false;
        }
        return super.hurt(source, damage);
    }

    private boolean canUseStareOn(Entity entity) {
        return (!(entity instanceof IBlacklistedFromStatues) || ((IBlacklistedFromStatues) entity).canBeTurnedToStone()) && !ServerEvents.isCockatriceTarget(entity);
    }

    private void switchAI(boolean melee) {
        if (melee) {
            this.goalSelector.removeGoal(aiStare);
            if (aiMelee != null) {
                this.goalSelector.addGoal(2, aiMelee);
            }
            this.isMeleeMode = true;
        } else {
            this.goalSelector.removeGoal(aiMelee);
            if (aiStare != null) {
                this.goalSelector.addGoal(2, aiStare);
            }
            this.isMeleeMode = false;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.isStaring()) {
            return false;
        }
        if (this.getRandom().nextBoolean()) {
            if (this.getAnimation() != ANIMATION_JUMPAT && this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_JUMPAT);
            }
            return false;
        } else {
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_JUMPAT) {
                this.setAnimation(ANIMATION_BITE);
            }
            return false;
        }

    }

    public boolean canMove() {
        return !this.isOrderedToSit() && !(this.getAnimation() == ANIMATION_JUMPAT && this.getAnimationTick() < 7);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEN, Boolean.FALSE);
        this.entityData.define(STARING, Boolean.FALSE);
        this.entityData.define(TARGET_ENTITY, 0);
        this.entityData.define(TAMING_PLAYER, 0);
        this.entityData.define(TAMING_LEVEL, 0);
        this.entityData.define(COMMAND, 0);
    }

    public boolean hasTargetedEntity() {
        return this.entityData.get(TARGET_ENTITY).intValue() != 0;
    }

    public boolean hasTamingPlayer() {
        return this.entityData.get(TAMING_PLAYER).intValue() != 0;
    }

    @Nullable
    public Entity getTamingPlayer() {
        if (!this.hasTamingPlayer()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(TAMING_PLAYER).intValue());
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.level().getEntity(this.entityData.get(TAMING_PLAYER).intValue());
        }
    }

    public void setTamingPlayer(int entityId) {
        this.entityData.set(TAMING_PLAYER, entityId);
    }

    @Nullable
    public LivingEntity getTargetedEntity() {
        boolean blindness = this.hasEffect(MobEffects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasEffect(MobEffects.BLINDNESS) || EntityGorgon.isBlindfolded(this.getTarget());
        if (blindness) {
            return null;
        }
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(TARGET_ENTITY).intValue());
                if (entity instanceof LivingEntity) {
                    this.targetedEntity = (LivingEntity) entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public void setTargetedEntity(int entityId) {
        this.entityData.set(TARGET_ENTITY, entityId);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hen", this.isHen());
        tag.putBoolean("Staring", this.isStaring());
        tag.putInt("TamingLevel", this.getTamingLevel());
        tag.putInt("TamingPlayer", this.entityData.get(TAMING_PLAYER).intValue());
        tag.putInt("Command", this.getCommand());
        tag.putBoolean("HasHomePosition", this.hasHomePosition);
        if (homePos != null && this.hasHomePosition) {
            homePos.write(tag);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHen(tag.getBoolean("Hen"));
        this.setStaring(tag.getBoolean("Staring"));
        this.setTamingLevel(tag.getInt("TamingLevel"));
        this.setTamingPlayer(tag.getInt("TamingPlayer"));
        this.setCommand(tag.getInt("Command"));
        this.hasHomePosition = tag.getBoolean("HasHomePosition");
        if (hasHomePosition && tag.getInt("HomeAreaX") != 0 && tag.getInt("HomeAreaY") != 0 && tag.getInt("HomeAreaZ") != 0) {
            homePos = new HomePosition(tag, this.level());
        }
        this.setConfigurableAttributes();
    }

    @Override
    public boolean isOrderedToSit() {
        if (level().isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        super.setSwimming(sitting);
        if (!level().isClientSide) {
            this.isSitting = sitting;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHen(this.getRandom().nextBoolean());
        return spawnDataIn;
    }


    public boolean isHen() {
        return this.entityData.get(HEN).booleanValue();
    }

    public void setHen(boolean hen) {
        this.entityData.set(HEN, hen);
    }

    public int getTamingLevel() {
        return this.entityData.get(TAMING_LEVEL).intValue();
    }

    public void setTamingLevel(int level) {
        this.entityData.set(TAMING_LEVEL, level);
    }

    public int getCommand() {
        return this.entityData.get(COMMAND).intValue();
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    public boolean isStaring() {
        if (level().isClientSide) {
            return this.isStaring = this.entityData.get(STARING).booleanValue();
        }
        return isStaring;
    }

    public void setStaring(boolean staring) {
        this.entityData.set(STARING, staring);
        if (!level().isClientSide) {
            this.isStaring = staring;
        }
    }

    public void forcePreyToLook(Mob mob) {
        mob.getLookControl().setLookAt(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ(), (float) mob.getMaxHeadYRot(), (float) mob.getMaxHeadXRot());
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        Item itemInHand = stackInHand.getItem();

        if (stackInHand.getItem() == Items.NAME_TAG || itemInHand == Items.LEAD || itemInHand == Items.POISONOUS_POTATO) {
            return super.mobInteract(player, hand);
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (stackInHand.is(IafItemTags.HEAL_COCKATRICE)) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(8);
                    this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                    stackInHand.shrink(1);
                }
                return InteractionResult.SUCCESS;
            } else if (stackInHand.isEmpty()) {
                if (player.isShiftKeyDown()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.displayClientMessage(Component.translatable("cockatrice.command.remove_home"), true);
                        return InteractionResult.SUCCESS;
                    } else {
                        BlockPos pos = this.blockPosition();
                        this.homePos = new HomePosition(pos, this.level());
                        this.hasHomePosition = true;
                        player.displayClientMessage(Component.translatable("cockatrice.command.new_home", pos.getX(), pos.getY(), pos.getZ(), homePos.getDimension()), true);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 3) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("cockatrice.command." + this.getCommand()), true);
                    this.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.FAIL;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.getTarget();
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && attackTarget instanceof Player) {
            this.setTarget(null);
        }
        if (this.isOrderedToSit() && this.getCommand() != 1) {
            this.setOrderedToSit(false);
        }
        if (this.isOrderedToSit() && attackTarget != null) {
            this.setTarget(null);
        }
        if (attackTarget != null && this.isAlliedTo(attackTarget)) {
            this.setTarget(null);
        }
        if (!level().isClientSide) {
            if (attackTarget == null || !attackTarget.isAlive()) {
                this.setTargetedEntity(0);
            } else if (this.isStaring() || this.shouldStareAttack(attackTarget)) {
                this.setTargetedEntity(attackTarget.getId());
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && attackTarget != null && this.getAnimationTick() == 7) {
            double dist = this.distanceToSqr(attackTarget);
            if (dist < 8) {
                attackTarget.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_JUMPAT && attackTarget != null) {
            double dist = this.distanceToSqr(attackTarget);
            double d0 = attackTarget.getX() - this.getX();
            double d1 = attackTarget.getZ() - this.getZ();
            float leap = Mth.sqrt((float) (d0 * d0 + d1 * d1));
            // FIXME :: Unused
//            if (dist <= 16.0D && this.isOnGround() && this.getAnimationTick() > 7 && this.getAnimationTick() < 12) {
//                Vec3 Vector3d = this.getDeltaMovement();
//                Vec3 Vector3d1 = new Vec3(attackTarget.getX() - this.getX(), 0.0D, attackTarget.getZ() - this.getZ());
//                if (Vector3d1.lengthSqr() > 1.0E-7D) {
//                    Vector3d1 = Vector3d1.normalize().scale(0.4D).add(Vector3d.scale(0.2D));
//                }
//            }
            if (dist < 4 && this.getAnimationTick() > 10) {
                attackTarget.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                if ((double) leap >= 1.0E-4D) {
                    attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(d0 / (double) leap * 0.800000011920929D + this.getDeltaMovement().x * 0.20000000298023224D, 0, d1 / (double) leap * 0.800000011920929D + this.getDeltaMovement().z * 0.20000000298023224D));
                }
            }
        }
        boolean sitting = isOrderedToSit();
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }

        boolean staring = isStaring();
        if (staring && stareProgress < 20.0F) {
            stareProgress += 0.5F;
        } else if (!staring && stareProgress > 0.0F) {
            stareProgress -= 0.5F;
        }
        if (!level().isClientSide) {
            if (staring) {
                ticksStaring++;
            } else {
                ticksStaring = 0;
            }
        }
        if (!level().isClientSide && staring && (attackTarget == null || this.shouldMelee())) {
            this.setStaring(false);
        }
        if (attackTarget != null) {
            this.getLookControl().setLookAt(attackTarget.getX(), attackTarget.getY() + (double) attackTarget.getEyeHeight(), attackTarget.getZ(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
            if (!shouldMelee() && attackTarget instanceof Mob) {
                forcePreyToLook((Mob) attackTarget);
            }
        }
        boolean blindness = this.hasEffect(MobEffects.BLINDNESS) || attackTarget != null && attackTarget.hasEffect(MobEffects.BLINDNESS);
        if (blindness) {
            this.setStaring(false);
        }
        if (!this.level().isClientSide && !blindness && attackTarget != null && EntityGorgon.isEntityLookingAt(this, attackTarget, VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(attackTarget, this, VIEW_RADIUS) && !EntityGorgon.isBlindfolded(attackTarget)) {
            if (!shouldMelee()) {
                if (!this.isStaring()) {
                    this.setStaring(true);
                } else {
                    int attackStrength = this.getFriendsCount(attackTarget);
                    if (this.level().getDifficulty() == Difficulty.HARD) {
                        attackStrength++;
                    }
                    attackTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 10, 2 + Math.min(1, attackStrength)));
                    attackTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, Math.min(4, attackStrength)));
                    attackTarget.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                    if (attackStrength >= 2 && attackTarget.tickCount % 40 == 0) {
                        attackTarget.hurt(this.level().damageSources().wither(), attackStrength - 1);
                    }
                    attackTarget.setLastHurtByMob(this);
                    if (!this.isTame() && attackTarget instanceof Player) {
                        this.setTamingPlayer(attackTarget.getId());
                        this.setTamingLevel(this.getTamingLevel() + 1);
                        if (this.getTamingLevel() % 100 == 0) {
                            this.level().broadcastEntityEvent(this, (byte) 46);
                        }
                        if (this.getTamingLevel() >= 1000) {
                            this.level().broadcastEntityEvent(this, (byte) 45);
                            if (this.getTamingPlayer() instanceof Player)
                                this.tame((Player) this.getTamingPlayer());
                            this.setTarget(null);
                            this.setTamingPlayer(0);
                            this.setTargetedEntity(0);
                        }
                    }
                }
            }
        }
        if (!this.level().isClientSide && attackTarget == null && this.getRandom().nextInt(300) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_WATTLESHAKE);
        }
        if (!this.level().isClientSide) {
            if (shouldMelee() && !this.isMeleeMode) {
                switchAI(true);
            }
            if (!shouldMelee() && this.isMeleeMode) {
                switchAI(false);
            }
        }

        if (this.level().isClientSide && this.getTargetedEntity() != null && EntityGorgon.isEntityLookingAt(this, this.getTargetedEntity(), VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(this.getTargetedEntity(), this, VIEW_RADIUS) && this.isStaring()) {
            if (this.hasTargetedEntity()) {
                if (this.clientSideAttackTime < this.getAttackDuration()) {
                    ++this.clientSideAttackTime;
                }

                LivingEntity livingEntity = this.getTargetedEntity();

                if (livingEntity != null) {
                    this.getLookControl().setLookAt(livingEntity, 90.0F, 90.0F);
                    this.getLookControl().tick();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = livingEntity.getX() - this.getX();
                    double d1 = livingEntity.getY() + (double) (livingEntity.getBbHeight() * 0.5F) - (this.getY() + (double) this.getEyeHeight());
                    double d2 = livingEntity.getZ() - this.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.random.nextDouble();

                    while (d4 < d3) {
                        d4 += 1.8D - d5 + this.random.nextDouble() * (1.7D - d5);
                        this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + d0 * d4, this.getY() + d1 * d4 + (double) this.getEyeHeight(), this.getZ() + d2 * d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private int getFriendsCount(LivingEntity attackTarget) {
        if (this.getTarget() == null) {
            return 0;
        }
        float dist = IafConfig.cockatriceChickenSearchLength;
        List<EntityCockatrice> list = level().getEntitiesOfClass(EntityCockatrice.class, this.getBoundingBox().expandTowards(dist, dist, dist));
        int i = 0;
        for (EntityCockatrice cockatrice : list) {
            if (!cockatrice.is(this) && cockatrice.getTarget() != null && cockatrice.getTarget() == this.getTarget()) {
                boolean bothLooking = EntityGorgon.isEntityLookingAt(cockatrice, cockatrice.getTarget(), VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(cockatrice.getTarget(), cockatrice, VIEW_RADIUS);
                if (bothLooking) {
                    i++;
                }
            }
        }
        return i;
    }

    public float getAttackAnimationScale(float f) {
        return ((float) this.clientSideAttackTime + f) / (float) this.getAttackDuration();
    }

    public boolean shouldStareAttack(Entity entity) {
        return this.distanceTo(entity) > 5;
    }

    public int getAttackDuration() {
        return 80;
    }

    private boolean shouldMelee() {
        boolean blindness = this.hasEffect(MobEffects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasEffect(MobEffects.BLINDNESS);
        if (this.getTarget() != null) {
            return this.distanceTo(this.getTarget()) < 4D || ServerEvents.isCockatriceTarget(this.getTarget()) || blindness || !this.canUseStareOn(this.getTarget());
        }
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 motionVec) {
        if (!this.canMove() && !this.isVehicle()) {
            motionVec = motionVec.multiply(0, 1, 0);
        }
        super.travel(motionVec);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
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
        return new Animation[]{NO_ANIMATION, ANIMATION_JUMPAT, ANIMATION_WATTLESHAKE, ANIMATION_BITE, ANIMATION_SPEAK, ANIMATION_EAT};
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
        return this.level().clip(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.COCKATRICE_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.COCKATRICE_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.COCKATRICE_DIE;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 45) {
            this.playEffect(true);
        } else if (id == 46) {
            this.playEffect(false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void playEffect(boolean play) {
        ParticleOptions enumparticletypes = ParticleTypes.HEART;

        if (!play) {
            enumparticletypes = ParticleTypes.DAMAGE_INDICATOR;
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(enumparticletypes, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
        }
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
