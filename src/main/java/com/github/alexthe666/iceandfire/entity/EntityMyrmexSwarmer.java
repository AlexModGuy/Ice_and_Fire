package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityMyrmexSwarmer extends EntityMyrmexRoyal {

    private static final DataParameter<Optional<UUID>> SUMMONER_ID = EntityDataManager.defineId(EntityMyrmexSwarmer.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> TICKS_ALIVE = EntityDataManager.defineId(EntityMyrmexSwarmer.class, DataSerializers.INT);

    public EntityMyrmexSwarmer(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new EntityMyrmexRoyal.FlyMoveHelper(this);
        this.navigation = createNavigator(level, AdvancedPathNavigate.MovementType.FLYING);
        switchNavigator(false);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 5)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 2)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            //ARMOR
            .add(Attributes.ARMOR, 0D);
    }

    protected int getExperienceReward(PlayerEntity player) {
        return 0;
    }

    protected void switchNavigator(boolean onLand) {
    }

    protected double attackDistance() {
        return 25;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MyrmexAIFollowSummoner(this, 1.0D, 10.0F, 5.0F));
        this.goalSelector.addGoal(2, new AIFlyAtTarget());
        this.goalSelector.addGoal(3, new AIFlyRandom());
        this.goalSelector.addGoal(4, new EntityAIAttackMeleeNoCooldown(this, 1.0D, true));
        this.goalSelector.addGoal(5, new MyrmexAIWander(this, 1D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MyrmexAISummonerHurtByTarget(this));
        this.targetSelector.addGoal(3, new MyrmexAISummonerHurtTarget(this));
    }

    protected void doPush(Entity entityIn) {
        if (entityIn instanceof EntityMyrmexSwarmer) {
            super.doPush(entityIn);
        }
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONER_ID, Optional.empty());
        this.entityData.define(TICKS_ALIVE, 0);
    }

    @Nullable
    public LivingEntity getSummoner() {
        try {
            UUID uuid = this.getSummonerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        if (this.getSummonerUUID() == null || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() == null) {
            return false;
        }
        if (entityIn instanceof TameableEntity) {
            UUID ownerID = ((TameableEntity) entityIn).getOwnerUUID();
            return ownerID != null && ownerID.equals(this.getSummonerUUID());
        }
        return entityIn.getUUID().equals(this.getSummonerUUID()) || entityIn instanceof EntityMyrmexSwarmer && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID() != null && ((EntityMyrmexSwarmer) entityIn).getSummonerUUID().equals(this.getSummonerUUID());
    }

    public void setSummonerID(@Nullable UUID uuid) {
        this.entityData.set(SUMMONER_ID, Optional.ofNullable(uuid));
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getSummonerUUID() == null) {
            compound.putString("SummonerUUID", "");
        } else {
            compound.putString("SummonerUUID", this.getSummonerUUID().toString());
        }
        compound.putInt("SummonTicks", this.getTicksAlive());

    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        String s = "";
        if (compound.hasUUID("SummonerUUID")) {
            s = compound.getString("SummonerUUID");
        }
        if (!s.isEmpty()) {
            try {
                this.setSummonerID(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
        this.setTicksAlive(compound.getInt("SummonTicks"));
    }

    public void setSummonedBy(PlayerEntity player) {
        this.setSummonerID(player.getUUID());
    }

    @Nullable
    public UUID getSummonerUUID() {
        return (UUID) ((Optional) this.entityData.get(SUMMONER_ID)).orElse(null);
    }

    public int getTicksAlive() {
        return this.entityData.get(TICKS_ALIVE).intValue();
    }

    public void setTicksAlive(int ticks) {
        this.entityData.set(TICKS_ALIVE, ticks);
    }

    public void aiStep() {
        super.aiStep();
        setFlying(true);
        boolean flying = this.isFlying() && !this.onGround;
        setTicksAlive(getTicksAlive() + 1);
        if (flying) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08D, 0));
            if (this.moveControl.getWantedY() > this.getY()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.08D, 0));
            }
        }
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.2D, 0));
        }
        if (this.getTarget() != null) {
            this.moveControl.setWantedPosition(this.getTarget().getX(), this.getTarget().getBoundingBox().minY, this.getTarget().getZ(), 1.0F);
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                this.setAnimation(random.nextBoolean() ? ANIMATION_BITE : ANIMATION_STING);
            }
        }
        if (this.getTicksAlive() > 1800) {
            this.kill();
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            double dist = this.distanceToSqr(this.getTarget());
            if (dist < attackDistance()) {
                this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playStingSound();
            double dist = this.distanceToSqr(this.getTarget());
            if (dist < attackDistance()) {
                this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2));
                this.getTarget().addEffect(new EffectInstance(Effects.POISON, 70, 1));
            }
        }
    }

    public int getGrowthStage() {
        return 2;
    }

    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        return null;
    }

    @Override
    public float getModelScale() {
        return 0.25F;
    }

    public boolean shouldHaveNormalAI() {
        return false;
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }

    public boolean isBreedingSeason() {
        return false;
    }

    public boolean shouldAttackEntity(LivingEntity attacker, LivingEntity LivingEntity) {
        return !isAlliedTo(attacker);
    }
}
