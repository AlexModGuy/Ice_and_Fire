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
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityGhost extends MonsterEntity implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IBlacklistedFromStatues, IHasCustomizableAttributes {

    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EntityGhost.class, DataSerializers.INT);
    private static final DataParameter<Boolean> CHARGING = EntityDataManager.defineId(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DAYTIME_MODE = EntityDataManager.defineId(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WAS_FROM_CHEST = EntityDataManager.defineId(EntityGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DAYTIME_COUNTER = EntityDataManager.defineId(EntityGhost.class, DataSerializers.INT);
    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;


    public EntityGhost(EntityType<EntityGhost> type, World worldIn) {
        super(type, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(type, this);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
        this.moveControl = new MoveHelper(this);
    }


    protected ResourceLocation getDefaultLootTable() {
        return this.wasFromChest() ? LootTables.EMPTY : this.getType().getDefaultLootTable();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GHOST_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.GHOST_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GHOST_DIE;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
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
    public AttributeModifierMap.MutableAttribute getConfigurableAttributes() {
        return bakeAttributes();
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != Effects.POISON && potioneffectIn.getEffect() != Effects.WITHER && super.canBeAffected(potioneffectIn);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.isFire() || source == DamageSource.IN_WALL || source == DamageSource.CACTUS
            || source == DamageSource.DROWN || source == DamageSource.FALLING_BLOCK || source == DamageSource.ANVIL || source == DamageSource.SWEET_BERRY_BUSH;
    }

    protected PathNavigator createNavigation(World worldIn) {
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


    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
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
    protected void doPush(Entity entity) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new GhostAICharge(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F, 1.0F) {
            public boolean canContinueToUse() {
                if (this.lookAt != null && this.lookAt instanceof PlayerEntity && ((PlayerEntity) this.lookAt).isCreative()) {
                    return false;
                }
                return super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.6D) {
            public boolean canUse() {
                interval = 60;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, PlayerEntity.class, 10, false, false, new Predicate<Entity>() {
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

    public void aiStep() {
        super.aiStep();
        this.noPhysics = true;
        if (!level.isClientSide) {
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
                this.setDeltaMovement(Vector3d.ZERO);
                this.setDaytimeCounter(this.getDaytimeCounter() + 1);
                if(getDaytimeCounter() >= 100){
                    this.setInvisible(true);
                }
            }else{
                this.setInvisible(this.hasEffect(Effects.INVISIBILITY));
                this.setDaytimeCounter(0);
            }
        } else {
            if (this.getAnimation() == ANIMATION_SCARE && this.getAnimationTick() == 3 && !this.isHauntedShoppingList() && random.nextInt(3) == 0) {
                this.playSound(IafSoundRegistry.GHOST_JUMPSCARE, this.getSoundVolume(), this.getVoicePitch());
                if (level.isClientSide) {
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

    public boolean isNoAi() {
        return this.isDaytimeMode() || super.isNoAi();
    }

    public boolean isSilent() {
        return this.isDaytimeMode() || super.isSilent();
    }

    protected boolean isSunBurnTick() {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double) Math.round(this.getY()), this.getZ())).above() : new BlockPos(this.getX(), (double) Math.round(this.getY() + 4), this.getZ());
            return f > 0.5F && this.level.canSeeSky(blockpos);
        }

        return false;
    }

    public boolean isNoGravity() {
        return true;
    }

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack != null && itemstack.getItem() == IafItemRegistry.MANUSCRIPT && !this.isHauntedShoppingList()) {
            this.setColor(-1);
            this.playSound(IafSoundRegistry.BESTIARY_PAGE, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void travel(Vector3d vec) {
        float f4;
        if (this.isDaytimeMode()) {
            super.travel(Vector3d.ZERO);
            return;
        }
        super.travel(vec);
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
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
        this.getEntityData().define(COLOR, Integer.valueOf(0));
        this.getEntityData().define(CHARGING, false);
        this.getEntityData().define(IS_DAYTIME_MODE, false);
        this.getEntityData().define(WAS_FROM_CHEST, false);
        this.getEntityData().define(DAYTIME_COUNTER, Integer.valueOf(0));
    }

    public int getColor() {
        return MathHelper.clamp(this.getEntityData().get(COLOR).intValue(), -1, 2);
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
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.setColor(compound.getInt("Color"));
        this.setDaytimeMode(compound.getBoolean("DaytimeMode"));
        this.setDaytimeCounter(compound.getInt("DaytimeCounter"));
        this.setFromChest(compound.getBoolean("FromChest"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Color", this.getColor());
        compound.putBoolean("DaytimeMode", this.isDaytimeMode());
        compound.putInt("DaytimeCounter", this.getDaytimeCounter());
        compound.putBoolean("FromChest", this.wasFromChest());
        super.addAdditionalSaveData(compound);
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

    class MoveHelper extends MovementController {
        EntityGhost ghost;

        public MoveHelper(EntityGhost ghost) {
            super(ghost);
            this.ghost = ghost;
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vec3d = new Vector3d(this.getWantedX() - ghost.getX(), this.getWantedY() - ghost.getY(), this.getWantedZ() - ghost.getZ());
                double d0 = vec3d.length();
                double edgeLength = ghost.getBoundingBox().getSize();
                if (d0 < edgeLength) {
                    this.operation = Action.WAIT;
                    ghost.setDeltaMovement(ghost.getDeltaMovement().scale(0.5D));
                } else {
                    ghost.setDeltaMovement(ghost.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.5D * 0.05D / d0)));
                    if (ghost.getTarget() == null) {
                        Vector3d vec3d1 = ghost.getDeltaMovement();
                        ghost.yRot = -((float) MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI);
                        ghost.yBodyRot = ghost.yRot;
                    } else {
                        double d4 = ghost.getTarget().getX() - ghost.getX();
                        double d5 = ghost.getTarget().getZ() - ghost.getZ();
                        ghost.yRot = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        ghost.yBodyRot = ghost.yRot;
                    }
                }
            }
        }
    }
}
