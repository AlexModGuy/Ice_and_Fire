package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
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
    private static final DataParameter<Boolean> HIDING = EntityDataManager.createKey(EntityMyrmexSentinel.class, DataSerializers.BOOLEAN);
    public float holdingProgress;
    public float hidingProgress;
    public int visibleTicks = 0;
    public int daylightTicks = 0;

    public EntityMyrmexSentinel(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return 8;
    }

    public Entity getHeldEntity() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void livingTick() {
        super.livingTick();
        if (visibleTicks > 0) {
            visibleTicks--;
        } else {
            visibleTicks = 0;
        }
        if (this.canSeeSky()) {
            this.daylightTicks++;
        } else {
            this.daylightTicks = 0;
        }
        boolean holding = getHeldEntity() != null;
        boolean hiding = isHiding() && !this.hasCustomer();
        if (holding || this.isOnResin() || this.getAttackTarget() != null || visibleTicks > 0) {
            this.setHiding(false);
        }
        if (holding && holdingProgress < 20.0F) {
            holdingProgress += 1.0F;
        } else if (!holding && holdingProgress > 0.0F) {
            holdingProgress -= 1.0F;
        }
        if (hiding) {
            this.rotationYaw = this.prevRotationYaw;
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
                this.getHeldEntity().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue() / 6));
            }
        }
        if (this.getAnimation() == ANIMATION_GRAB && this.getAttackTarget() != null && this.getAnimationTick() == 7) {
            this.playStingSound();
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue() / 2));
                this.getAttackTarget().startRiding(this);
            }
        }
        if (this.getAnimation() == ANIMATION_SLASH && this.getAttackTarget() != null && this.getAnimationTick() % 5 == 0 && this.getAnimationTick() <= 20) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()) / 4);
            }
        }
        if (this.getAnimation() == ANIMATION_STING && (this.getAnimationTick() == 0 || this.getAnimationTick() == 10)) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && (this.getAnimationTick() == 6 || this.getAnimationTick() == 16)) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 18) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.POISON, 100, 3));
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new MyrmexAIFindHidingSpot(this));
        this.goalSelector.addGoal(0, new MyrmexAITradePlayer(this));
        this.goalSelector.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
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


    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIDING, Boolean.valueOf(false));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IafConfig.myrmexBaseAttackStrength * 3D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
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
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putBoolean("Hiding", this.isHiding());
        tag.putInt("DaylightTicks", daylightTicks);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.setHiding(tag.getBoolean("Hiding"));
        this.daylightTicks = tag.getInt("DaylightTicks");
    }

    public boolean shouldLeaveHive() {
        return true;
    }

    public boolean shouldEnterHive() {
        return false;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            float radius = 1.25F;
            float extraY = 0.35F;
            if (this.getAnimation() == ANIMATION_GRAB) {
                int modTick = MathHelper.clamp(this.getAnimationTick(), 0, 10);
                radius = 3.25F - modTick * 0.2F;
                extraY = modTick * 0.035F;
            }
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            if (passenger.getHeight() >= 1.75F) {
                extraY = passenger.getHeight() - 2F;
            }
            passenger.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (amount >= 1.0D && !this.getPassengers().isEmpty() && rand.nextInt(2) == 0) {
            for (Entity entity : this.getPassengers()) {
                entity.stopRiding();
            }
        }
        visibleTicks = 300;
        this.setHiding(false);
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_SLASH && this.getAnimation() != ANIMATION_GRAB && this.getHeldEntity() == null) {
            if (this.getRNG().nextInt(2) == 0 && entityIn.getWidth() < 2F) {
                this.setAnimation(ANIMATION_GRAB);
            } else {
                this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_STING : ANIMATION_SLASH);
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
        return this.dataManager.get(HIDING).booleanValue();
    }

    public void setHiding(boolean hiding) {
        this.dataManager.set(HIDING, hiding);
    }

    @Override
    public int getXp() {
        return 0;
    }

    @Override
    public boolean func_213705_dZ() {
        return false;
    }
}
