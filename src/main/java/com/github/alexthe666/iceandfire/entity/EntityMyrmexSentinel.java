package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMyrmexSentinel extends EntityMyrmexBase {

    public static final Animation ANIMATION_GRAB = Animation.create(15);
    public static final Animation ANIMATION_NIBBLE = Animation.create(10);
    public static final Animation ANIMATION_STING = Animation.create(25);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_sentinel_desert");
    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_sentinel_jungle");
    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_sentinel.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_sentinel.png");
    private static final ResourceLocation TEXTURE_DESERT_HIDDEN = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_sentinel_hidden.png");
    private static final ResourceLocation TEXTURE_JUNGLE_HIDDEN = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_sentinel_hidden.png");
    private static final DataParameter<Boolean> HIDING = EntityDataManager.defineId(EntityMyrmexSentinel.class, DataSerializers.BOOLEAN);
    public float holdingProgress;
    public float hidingProgress;
    public int visibleTicks = 0;
    public int daylightTicks = 0;

    public EntityMyrmexSentinel(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel1Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_SENTINEL.get(1) : MyrmexTrades.DESERT_SENTINEL.get(1);
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel2Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_SENTINEL.get(2) : MyrmexTrades.DESERT_SENTINEL.get(2);
    }

    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    protected int getExperienceReward(PlayerEntity player) {
        return 8;
    }

    public Entity getHeldEntity() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.getTarget();
        if (visibleTicks > 0) {
            visibleTicks--;
        } else {
            visibleTicks = 0;
        }
        if (attackTarget != null) {
            visibleTicks = 100;
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        boolean holding = getHeldEntity() != null;
        boolean hiding = isHiding() && !this.hasCustomer();
        if ((holding || this.isOnResin() || attackTarget != null) || visibleTicks > 0) {
            this.setHiding(false);
        }
        if (holding && holdingProgress < 20.0F) {
            holdingProgress += 1.0F;
        } else if (!holding && holdingProgress > 0.0F) {
            holdingProgress -= 1.0F;
        }
        if (hiding) {
            this.yRot = this.yRotO;
        }
        if (hiding && hidingProgress < 20.0F) {
            hidingProgress += 1.0F;
        } else if (!hiding && hidingProgress > 0.0F) {
            hidingProgress -= 1.0F;
        }
        if (this.getHeldEntity() != null) {
            this.setAnimation(ANIMATION_NIBBLE);
            if (this.getAnimationTick() == 5) {
                this.playBiteSound();
                this.getHeldEntity().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() / 6));
            }
        }
        if (this.getAnimation() == ANIMATION_GRAB && attackTarget != null && this.getAnimationTick() == 7) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() / 2));
                //Make sure it doesn't grab a dead dragon
                if (attackTarget instanceof EntityDragonBase) {
                    if (!((EntityDragonBase) attackTarget).isMobDead()) {
                        attackTarget.startRiding(this);
                    }
                } else {
                    attackTarget.startRiding(this);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_SLASH && attackTarget != null && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() <= 20) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(attackTarget.getBoundingBox())) {
                attackTarget.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) / 4);
            }
        }
        if (this.getAnimation() == ANIMATION_STING && (this.getAnimationTick() == 0 || this.getAnimationTick() == 10)) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && attackTarget != null && (this.getAnimationTick() == 6 || this.getAnimationTick() == 16)) {
            double dist = this.distanceToSqr(attackTarget);
            if (dist < 18) {
                attackTarget.hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
                attackTarget.addEffect(new EffectInstance(Effects.POISON, 100, 3));
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new MyrmexAIFindHidingSpot(this));
        this.goalSelector.addGoal(0, new MyrmexAITradePlayer(this));
        this.goalSelector.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.addGoal(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.addGoal(5, new MyrmexAIWander(this, 1D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new MyrmexAIDefendHive(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 4, true, true, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexSentinel.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof IMob);
            }
        }));
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HIDING, Boolean.valueOf(false));
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 60D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 3D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 64.0D)
            //ARMOR
            .add(Attributes.ARMOR, 12.0D);
    }

    @Override
    public AttributeModifierMap.MutableAttribute getConfigurableAttributes() {
        return bakeAttributes();
    }

    @Override
    public ResourceLocation getAdultTexture() {
        if (isHiding()) {
            return isJungle() ? TEXTURE_JUNGLE_HIDDEN : TEXTURE_DESERT_HIDDEN;

        } else {
            return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
        }
    }

    @Override
    public float getModelScale() {
        return 0.8F;
    }

    @Override
    public int getCasteImportance() {
        return 2;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hiding", this.isHiding());
        tag.putInt("DaylightTicks", daylightTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.setHiding(tag.getBoolean("Hiding"));
        this.daylightTicks = tag.getInt("DaylightTicks");
    }

    public boolean shouldLeaveHive() {
        return true;
    }

    public boolean shouldEnterHive() {
        return false;
    }

    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            yBodyRot = yRot;
            float radius = 1.25F;
            float extraY = 0.35F;
            if (this.getAnimation() == ANIMATION_GRAB) {
                int modTick = MathHelper.clamp(this.getAnimationTick(), 0, 10);
                radius = 3.25F - modTick * 0.2F;
                extraY = modTick * 0.035F;
            }
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            if (passenger.getBbHeight() >= 1.75F) {
                extraY = passenger.getBbHeight() - 2F;
            }
            passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        if (amount >= 1.0D && !this.getPassengers().isEmpty() && random.nextInt(2) == 0) {
            for (Entity entity : this.getPassengers()) {
                entity.stopRiding();
            }
        }
        visibleTicks = 300;
        this.setHiding(false);
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_SLASH && this.getAnimation() != ANIMATION_GRAB && this.getHeldEntity() == null) {
            if (this.getRandom().nextInt(2) == 0 && entityIn.getBbWidth() < 2F) {
                this.setAnimation(ANIMATION_GRAB);
            } else {
                this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_SLASH);
            }
            visibleTicks = 300;
            return true;
        }
        return false;
    }

    public boolean needsGaurding() {
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_SLASH, ANIMATION_STING, ANIMATION_GRAB, ANIMATION_NIBBLE};
    }

    public boolean canMove() {
        return super.canMove() && this.getHeldEntity() == null && !isHiding();
    }

    public boolean shouldRiderSit() {
        return false;
    }


    public boolean isHiding() {
        return this.entityData.get(HIDING).booleanValue();
    }

    public void setHiding(boolean hiding) {
        this.entityData.set(HIDING, hiding);
    }

    @Override
    public int getVillagerXp() {
        return 4;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }
}
