package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.DragonFireEvent;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageDragonSyncFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityIceDragon extends EntityDragonBase {
    public static final ResourceLocation FEMALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/ice_dragon_female");
    public static final ResourceLocation MALE_LOOT = new ResourceLocation("iceandfire", "entities/dragon/ice_dragon_male");
    public static final ResourceLocation SKELETON_LOOT = new ResourceLocation("iceandfire", "entities/dragon/ice_dragon_skeleton");

    public EntityIceDragon(Level worldIn) {
        this(IafEntityRegistry.ICE_DRAGON.get(), worldIn);
    }

    public EntityIceDragon(EntityType<?> t, Level worldIn) {
        super(t, worldIn, DragonType.ICE, 1, 1 + IafConfig.dragonAttackDamage, IafConfig.dragonHealth * 0.04, IafConfig.dragonHealth, 0.15F, 0.4F);
        ANIMATION_SPEAK = Animation.create(20);
        ANIMATION_BITE = Animation.create(35);
        ANIMATION_SHAKEPREY = Animation.create(65);
        ANIMATION_TAILWHACK = Animation.create(40);
        ANIMATION_FIRECHARGE = Animation.create(25);
        ANIMATION_WINGBLAST = Animation.create(50);
        ANIMATION_ROAR = Animation.create(40);
        ANIMATION_EPIC_ROAR = Animation.create(60);
    }

    @Override
    protected boolean shouldTarget(Entity entity) {
        if (entity instanceof EntityDragonBase && !this.isTame()) {
            return entity.getType() != this.getType() && this.getBbWidth() >= entity.getBbWidth() && !((EntityDragonBase) entity).isMobDead();
        }
        return entity instanceof Player || DragonUtils.isDragonTargetable(entity, IafTagRegistry.ICE_DRAGON_TARGETS) || entity instanceof WaterAnimal || !this.isTame() && DragonUtils.isVillager(entity);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SWIMMING, Boolean.FALSE);
    }

    @Override
    public String getVariantName(int variant) {
        switch (variant) {
            default:
                return "blue_";
            case 1:
                return "white_";
            case 2:
                return "sapphire_";
            case 3:
                return "silver_";
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public Item getVariantScale(int variant) {
        switch (variant) {
            default:
                return IafItemRegistry.DRAGONSCALES_BLUE.get();
            case 1:
                return IafItemRegistry.DRAGONSCALES_WHITE.get();
            case 2:
                return IafItemRegistry.DRAGONSCALES_SAPPHIRE.get();
            case 3:
                return IafItemRegistry.DRAGONSCALES_SILVER.get();
        }
    }

    @Override
    public Item getVariantEgg(int variant) {
        switch (variant) {
            default:
                return IafItemRegistry.DRAGONEGG_BLUE.get();
            case 1:
                return IafItemRegistry.DRAGONEGG_WHITE.get();
            case 2:
                return IafItemRegistry.DRAGONEGG_SAPPHIRE.get();
            case 3:
                return IafItemRegistry.DRAGONEGG_SILVER.get();
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }


    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Swimming", this.isSwimming());
        compound.putInt("SwimmingTicks", this.ticksSwiming);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSwimming(compound.getBoolean("Swimming"));
        this.ticksSwiming = compound.getInt("SwimmingTicks");
    }

/*    @Override
    public boolean canBeControlledByRider() {
        return true;
    }*/

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        this.getLookControl().setLookAt(entityIn, 30.0F, 30.0F);
        if (!this.isPlayingAttackAnimation()) {
            switch (groundAttack) {
                case BITE:
                    this.setAnimation(ANIMATION_BITE);
                    break;
                case TAIL_WHIP:
                    this.setAnimation(ANIMATION_TAILWHACK);
                    break;
                case SHAKE_PREY:
                    boolean flag = false;
                    if (new Random().nextInt(2) == 0 && isDirectPathBetweenPoints(this, this.position().add(0, this.getBbHeight() / 2, 0), entityIn.position().add(0, entityIn.getBbHeight() / 2, 0)) &&
                        entityIn.getBbWidth() < this.getBbWidth() * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
                        this.setAnimation(ANIMATION_SHAKEPREY);
                        flag = true;
                        entityIn.startRiding(this);
                    }
                    if (!flag) {
                        groundAttack = IafDragonAttacks.Ground.BITE;
                        this.setAnimation(ANIMATION_BITE);
                    }
                    break;
                case WING_BLAST:
                    this.setAnimation(ANIMATION_WINGBLAST);
                    break;
            }
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity attackTarget = this.getTarget();
        if (!level().isClientSide && this.isInLava() && this.isAllowedToTriggerFlight() && !this.isModelDead()) {
            this.setHovering(true);
            this.setInSittingPose(false);
            this.setOrderedToSit(false);
            this.flyHovering = 0;
            this.flyTicks = 0;
        }
        if (!level().isClientSide && attackTarget != null) {
            if (this.getBoundingBox().inflate(0 + this.getRenderSize() * 0.33F, 0 + this.getRenderSize() * 0.33F, 0 + this.getRenderSize() * 0.33F).intersects(attackTarget.getBoundingBox())) {
                doHurtTarget(attackTarget);
            }
            if (this.groundAttack == IafDragonAttacks.Ground.FIRE && (usingGroundAttack || this.onGround())) {
                shootIceAtMob(attackTarget);
            }
            if (this.airAttack == IafDragonAttacks.Air.TACKLE && !usingGroundAttack && this.distanceToSqr(attackTarget) < 100) {
                double difX = attackTarget.getX() - this.getX();
                double difY = attackTarget.getY() + attackTarget.getBbHeight() - this.getY();
                double difZ = attackTarget.getZ() - this.getZ();
                this.setDeltaMovement(this.getDeltaMovement().add(difX * 0.1D, difY * 0.1D, difZ * 0.1D));

                if (this.getBoundingBox().inflate(1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F).intersects(attackTarget.getBoundingBox())) {
                    doHurtTarget(attackTarget);
                    usingGroundAttack = true;
                    randomizeAttacks();
                    setFlying(false);
                    setHovering(false);
                }
            }
        }
        boolean swimming = isInWater();
        this.prevSwimProgress = swimProgress;
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 0.5F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        if (this.isInWater() && !this.isSwimming() && (!this.isFlying() && !this.isHovering() || this.flyTicks > 100)) {
            this.setSwimming(true);
            this.setHovering(false);
            this.setFlying(false);
            this.flyTicks = 0;
            this.ticksSwiming = 0;
        }
        if ((!this.isInWater() || this.isHovering() || this.isFlying()) && this.isSwimming()) {
            this.setSwimming(false);
            this.ticksSwiming = 0;
        }
        if (this.isSwimming() && !this.isModelDead()) {
            ticksSwiming++;
            if (this.isInWater() && (ticksSwiming > 4000 || this.getTarget() != null && this.isInWater() != this.getTarget().isInWater()) && !this.isBaby() && !this.isHovering() && !this.isFlying()) {
                this.setHovering(true);
                this.jumpFromGround();
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.8D, 0.0D));
                this.setSwimming(false);
            }
        }
        if (!level().isClientSide && this.getControllingPassenger() == null && (this.isHovering() && !this.isFlying() && this.isInWater())) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.2D, 0.0D));
        }
        if (swimCycle < 48) {
            swimCycle += 2;
        } else {
            swimCycle = 0;
        }
        if (this.isModelDead() && swimCycle != 0) {
            swimCycle = 0;
        }
    }

    @Override
    public void riderShootFire(Entity controller) {
        if (this.getRandom().nextInt(5) == 0 && !this.isBaby()) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                this.setAnimation(ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 15) {
                setYRot(yBodyRot);
                Vec3 headVec = this.getHeadPosition();
                this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                double d2 = controller.getLookAngle().x;
                double d3 = controller.getLookAngle().y;
                double d4 = controller.getLookAngle().z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(
                    IafEntityRegistry.ICE_DRAGON_CHARGE.get(), level(), this, d2, d3, d4);
                float size = this.isBaby() ? 0.4F : this.shouldDropLoot() ? 1.3F : 0.8F;
                entitylargefireball.setPos(headVec.x, headVec.y, headVec.z);
                if (!level().isClientSide) {
                    level().addFreshEntity(entitylargefireball);
                }

            }
        } else {
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    setYRot(yBodyRot);
                    if (this.tickCount % 5 == 0) {
                        this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                    }
                    HitResult mop = rayTraceRider(controller, 10 * this.getDragonStage(), 1.0F);
                    if (mop != null) {
                        stimulateFire(mop.getLocation().x, mop.getLocation().y, mop.getLocation().z, 1);
                    }
                }
            } else {
                this.setBreathingFire(true);
            }
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this);
    }

    @Override
    public void onInsideBubbleColumn(boolean pDownwards) {
        // Disable bubble column drag for elder dragons
        if (this.getDragonStage() < 2) {
            super.onInsideBubbleColumn(pDownwards);
        }
    }

    @Override
    public void onAboveBubbleCol(boolean pDownwards) {
        // Disable bubble column drag for elder dragons
        if (this.getDragonStage() < 2) {
            super.onAboveBubbleCol(pDownwards);
        }
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        float flyingSpeed;
        if (/* Always false on the server */ this.isInWater()) {
            // In water special
            if (this.isEffectiveAi() && this.getControllingPassenger() == null) {
                // Ice dragons swim faster
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
                if (this.getTarget() == null) {
//                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
                }
            } else if (allowLocalMotionControl && this.getControllingPassenger() != null && !isHovering() && !isFlying()) {
                LivingEntity rider = (LivingEntity) this.getControllingPassenger();

                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
                // Bigger difference in speed for young and elder dragons
                float waterSpeedMod =  (float) (0.42f + 0.1 * Mth.map(speed, this.minimumSpeed, this.maximumSpeed, 0f, 1.5f));
                speed *= waterSpeedMod;
                speed *= rider.isSprinting() ? 1.5f : 1.0f;

                float vertical = 0f;
                if (isGoingUp() && !isGoingDown()) {
                    vertical = 1f;
                } else if (isGoingDown() && !isGoingUp()) {
                    vertical = -1f;
                } else if (isGoingUp() && isGoingDown() && isControlledByLocalInstance()) {
                    // Try floating
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1.0f, 0.5f, 1.0f));
                }

                Vec3 travelVector = new Vec3(
                        rider.xxa,
                        vertical,
                        rider.zza
                );
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed(speed);

                    this.moveRelative(this.getSpeed(), travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());

                    Vec3 currentMotion = this.getDeltaMovement();
                    if (this.horizontalCollision) {
                        currentMotion = new Vec3(currentMotion.x, 0.2D, currentMotion.z);
                    }
                    this.setDeltaMovement(currentMotion.scale(0.9D));

                    this.calculateEntityAnimation(false);
                } else {
                    this.setDeltaMovement(Vec3.ZERO);
                }
                this.tryCheckInsideBlocks();
            } else {
                super.travel(pTravelVector);
            }

        }
        // Over water special
        else if (allowLocalMotionControl && this.getControllingPassenger() != null && !isHovering() && !isFlying()
                && this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getFluidState().is(FluidTags.WATER)) {
            // Movement when walking on the water, mainly used for not slowing down when jumping out of water
            LivingEntity rider = (LivingEntity) this.getControllingPassenger();

            double forward = rider.zza;
            double strafing = rider.xxa;
            // Inherit y motion for dropping
            double vertical = pTravelVector.y;
            float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);

            float groundSpeedModifier = (float) (1.8F * this.getFlightSpeedModifier());
            speed *= groundSpeedModifier;
            // Try to match the original riding speed
//            forward *= speed;
            // Faster sprint
            forward *= rider.isSprinting() ? 1.2f : 1.0f;
            // Slower going back
            forward *= rider.zza > 0 ? 1.0f : 0.2f;
            // Slower going sideway
            strafing *= 0.05f;

            if (this.isControlledByLocalInstance()) {
                flyingSpeed = speed * 0.1F;
                this.setSpeed(speed);

                // Vanilla walking behavior includes going up steps
                super.travel(new Vec3(strafing, vertical, forward));

                Vec3 currentMotion = this.getDeltaMovement();
                if (this.horizontalCollision) {
                    currentMotion = new Vec3(currentMotion.x, 0.2D, currentMotion.z);
                }
                this.setDeltaMovement(currentMotion.scale(1.0D));
            } else {
                this.setDeltaMovement(Vec3.ZERO);
            }
            this.tryCheckInsideBlocks();
//            this.updatePitch(this.yOld - this.getY());
            return;
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public ResourceLocation getDeadLootTable() {
        if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
            return SKELETON_LOOT;
        } else {
            return isMale() ? MALE_LOOT : FEMALE_LOOT;
        }
    }

    private void shootIceAtMob(LivingEntity entity) {
        if (this.usingGroundAttack && this.groundAttack == IafDragonAttacks.Ground.FIRE || !this.usingGroundAttack && (this.airAttack == IafDragonAttacks.Air.SCORCH_STREAM || this.airAttack == IafDragonAttacks.Air.HOVER_BLAST)) {
            if (this.usingGroundAttack && this.getRandom().nextInt(5) == 0 || !this.usingGroundAttack && this.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                    this.setAnimation(ANIMATION_FIRECHARGE);
                } else if (this.getAnimationTick() == 15) {
                    setYRot(yBodyRot);
                    Vec3 headVec = this.getHeadPosition();
                    double d2 = entity.getX() - headVec.x;
                    double d3 = entity.getY() - headVec.y;
                    double d4 = entity.getZ() - headVec.z;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                    this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                    EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(
                        IafEntityRegistry.ICE_DRAGON_CHARGE.get(), level(), this, d2, d3, d4);
                    float size = this.isBaby() ? 0.4F : this.shouldDropLoot() ? 1.3F : 0.8F;
                    entitylargefireball.setPos(headVec.x, headVec.y, headVec.z);
                    if (!level().isClientSide) {
                        level().addFreshEntity(entitylargefireball);
                    }
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathingFire(false);
                        this.usingGroundAttack = true;
                    }
                }
            } else {
                if (this.isBreathingFire()) {
                    if (this.isActuallyBreathingFire()) {
                        setYRot(yBodyRot);
                        if (this.tickCount % 5 == 0) {
                            this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                        }
                        stimulateFire(entity.getX(), entity.getY(), entity.getZ(), 1);
                        if (!entity.isAlive() || entity == null) {
                            this.setBreathingFire(false);
                            this.usingGroundAttack = true;
                        }
                    }
                } else {
                    this.setBreathingFire(true);
                }
            }
        }
        this.lookAt(entity, 360, 360);
    }

    @Override
    public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireEvent(this, burnX, burnY, burnZ))) return;
        if (syncType == 1 && !level().isClientSide) {
            //sync with client
            IceAndFire.sendMSGToAll(new MessageDragonSyncFire(this.getId(), burnX, burnY, burnZ, 0));
        }
        if (syncType == 2 && level().isClientSide) {
            //sync with server
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.getId(), burnX, burnY, burnZ, 0));
        }
        if (syncType == 3 && !level().isClientSide) {
            //sync with client, fire bomb
            IceAndFire.sendMSGToAll(new MessageDragonSyncFire(this.getId(), burnX, burnY, burnZ, 5));
        }
        if (syncType == 4 && level().isClientSide) {
            //sync with server, fire bomb
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.getId(), burnX, burnY, burnZ, 5));
        }
        if (/* Only trigger for bomb */ syncType > 2 && syncType < 6) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                this.setAnimation(ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 20) {
                setYRot(yBodyRot);
                Vec3 headVec = this.getHeadPosition();
                double d2 = burnX - headVec.x;
                double d3 = burnY - headVec.y;
                double d4 = burnZ - headVec.z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d3 = d3 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                d4 = d4 + this.random.nextGaussian() * 0.007499999832361937D * inaccuracy;
                this.playSound(IafSoundRegistry.FIREDRAGON_BREATH, 4, 1);
                EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(
                    IafEntityRegistry.ICE_DRAGON_CHARGE.get(), level(), this, d2, d3, d4);
                // FIXME :: Unused
                // float size = this.isBaby() ? 0.4F : this.shouldDropLoot() ? 1.3F : 0.8F;
                entitylargefireball.setPos(headVec.x, headVec.y, headVec.z);
                if (!level().isClientSide) {
                    level().addFreshEntity(entitylargefireball);
                }
            }
            return;
        }
        this.getNavigation().stop();
        this.burnParticleX = burnX;
        this.burnParticleY = burnY;
        this.burnParticleZ = burnZ;
        Vec3 headPos = getHeadPosition();
        double d2 = burnX - headPos.x;
        double d3 = burnY - headPos.y;
        double d4 = burnZ - headPos.z;
        double distance = Math.max(2.5F * this.distanceToSqr(burnX, burnY, burnZ), 0);
        double conqueredDistance = burnProgress / 40D * distance;
        int increment = (int) Math.ceil(conqueredDistance / 100);
        int particleCount = this.getDragonStage() <= 3 ? 6 : 3;
        for (int i = 0; i < conqueredDistance; i += increment) {
            double progressX = headPos.x + d2 * (i / (float) distance);
            double progressY = headPos.y + d3 * (i / (float) distance);
            double progressZ = headPos.z + d4 * (i / (float) distance);
            if (canPositionBeSeen(progressX, progressY, progressZ)) {
                if (level().isClientSide && random.nextInt(particleCount) == 0) {
                    IceAndFire.PROXY.spawnDragonParticle(EnumParticles.DragonIce, headPos.x, headPos.y, headPos.z, 0, 0, 0, this);
                }
            } else {
                if (!level().isClientSide) {
                    HitResult result = this.level().clip(new ClipContext(new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(progressX, progressY, progressZ), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    Vec3 vec3 = result.getLocation();
                    BlockPos pos = BlockPos.containing(vec3);
                    IafDragonDestructionManager.destroyAreaBreath(level(), pos, this);
                }
            }
        }
        if (burnProgress >= 40D && canPositionBeSeen(burnX, burnY, burnZ)) {
            double spawnX = burnX + (random.nextFloat() * 3.0) - 1.5;
            double spawnY = burnY + (random.nextFloat() * 3.0) - 1.5;
            double spawnZ = burnZ + (random.nextFloat() * 3.0) - 1.5;
            if (!level().isClientSide) {
                IafDragonDestructionManager.destroyAreaBreath(level(), BlockPos.containing(spawnX, spawnY, spawnZ), this);
            }
        }
    }

    @Override
    public boolean isSwimming() {
        if (level().isClientSide) {
            boolean swimming = this.entityData.get(SWIMMING).booleanValue();
            this.isSwimming = swimming;
            return swimming;
        }
        return isSwimming;
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.entityData.set(SWIMMING, swimming);
        if (!level().isClientSide) {
            this.isSwimming = swimming;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_IDLE : this.shouldDropLoot() ? IafSoundRegistry.ICEDRAGON_ADULT_IDLE : IafSoundRegistry.ICEDRAGON_CHILD_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_HURT : this.shouldDropLoot() ? IafSoundRegistry.ICEDRAGON_ADULT_HURT : IafSoundRegistry.ICEDRAGON_CHILD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_DEATH : this.shouldDropLoot() ? IafSoundRegistry.ICEDRAGON_ADULT_DEATH : IafSoundRegistry.ICEDRAGON_CHILD_DEATH;
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_ROAR : this.shouldDropLoot() ? IafSoundRegistry.ICEDRAGON_ADULT_ROAR : IafSoundRegistry.ICEDRAGON_CHILD_ROAR;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityIceDragon.ANIMATION_TAILWHACK, EntityIceDragon.ANIMATION_FIRECHARGE, EntityIceDragon.ANIMATION_WINGBLAST, EntityIceDragon.ANIMATION_ROAR};
    }

    @Override
    public boolean isFood(@Nullable ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.FROST_STEW.get();
    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                setYRot(yBodyRot);
                if (this.tickCount % 5 == 0) {
                    this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                }
                stimulateFire(burningTarget.getX() + 0.5F, burningTarget.getY() + 0.5F, burningTarget.getZ() + 0.5F, 1);
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    @Override
    public double getFlightSpeedModifier() {
        return super.getFlightSpeedModifier() * (this.isInWater() ? 0.3F : 1F);
    }

    @Override
    public boolean isAllowedToTriggerFlight() {
        return super.isAllowedToTriggerFlight() && !this.isInWater();
    }

    @Override
    protected void spawnDeathParticles() {
        if (this.level().isClientSide) {
            for (int k = 0; k < 10; ++k) {
                double d2 = this.random.nextGaussian() * 0.02D;
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                IceAndFire.PROXY.spawnParticle(EnumParticles.Snowflake, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
            }
        }
    }

    @Override
    protected void spawnBabyParticles() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                float radiusAdd = i * 0.15F;
                float headPosX = (float) (getX() + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Mth.cos((float) ((getYRot() + 90) * Math.PI / 180)));
                float headPosZ = (float) (getZ() + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Mth.sin((float) ((getYRot() + 90) * Math.PI / 180)));
                float headPosY = (float) (getY() + 0.5 * getRenderSize() * 0.3F);
                IceAndFire.PROXY.spawnParticle(EnumParticles.DragonIce, headPosX, headPosY, headPosZ, 0, 0, 0);
            }
        }
    }

    //Required for proper egg drop
    @Override
    public int getStartMetaForType() {
        return 4;
    }

    @Override
    public SoundEvent getBabyFireSound() {
        return SoundEvents.BOTTLE_FILL_DRAGONBREATH;
    }

    @Override
    public ItemStack getSkull() {
        return new ItemStack(IafItemRegistry.DRAGON_SKULL_ICE.get());
    }

    @Override
    public boolean useFlyingPathFinder() {
        return (this.isFlying() || this.isInWater()) && this.getControllingPassenger() == null;
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItemRegistry.SUMMONING_CRYSTAL_ICE.get();
    }

    @Override
    public Item getBloodItem() {
        return IafItemRegistry.ICE_DRAGON_BLOOD.get();
    }

    @Override
    public Item getFleshItem() {
        return IafItemRegistry.ICE_DRAGON_FLESH.get();
    }

    @Override
    public ItemLike getHeartItem() {
        return IafItemRegistry.ICE_DRAGON_HEART.get();
    }
}
