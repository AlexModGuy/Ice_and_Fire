package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIWatchClosestIgnoreRider;
import com.github.alexthe666.iceandfire.entity.ai.FlyingAITarget;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIGetInWater;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IMultipartEntity;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class EntitySeaSerpent extends AnimalEntity implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(15);
    public static final Animation ANIMATION_ROAR = Animation.create(40);
    public static final int TIME_BETWEEN_JUMPS = 170;
    public static final int TIME_BETWEEN_ROARS = 300;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> JUMPING = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATHING = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ANCIENT = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final Predicate NOT_SEA_SERPENT = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((LivingEntity) entity);
        }
    };
    public int swimCycle;
    public float orbitRadius = 0.0F;
    @OnlyIn(Dist.CLIENT)
    public IFChainBuffer roll_buffer;
    @OnlyIn(Dist.CLIENT)
    public IFChainBuffer tail_buffer;
    @OnlyIn(Dist.CLIENT)
    public IFChainBuffer head_buffer;
    @OnlyIn(Dist.CLIENT)
    public IFChainBuffer pitch_buffer;
    @Nullable
    public BlockPos orbitPos = null;
    public float jumpProgress = 0.0F;
    public float wantJumpProgress = 0.0F;
    public float jumpRot = 0.0F;
    public float breathProgress = 0.0F;
    //true  = melee, false = ranged
    public boolean attackDecision = false;
    private int animationTick;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[9];
    private float lastScale;
    private boolean isLandNavigator;
    private SwimBehavior swimBehavior = SwimBehavior.WANDER;
    private boolean changedSwimBehavior = false;
    private int ticksCircling;
    private boolean isArcing = false;
    private float arcingYAdditive = 0F;
    private int ticksSinceJump = 0;
    private int ticksSinceRoar = 0;
    private int ticksJumping = 0;
    private boolean isBreathing;

    public EntitySeaSerpent(EntityType t, World worldIn) {
        super(t, worldIn);
        switchNavigator(true);
        this.ignoreFrustumCheck = true;
        resetParts(1.0F);
        if (worldIn.isRemote) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            tail_buffer = new IFChainBuffer();
            head_buffer = new IFChainBuffer();
        }
    }

    public static BlockPos getPositionRelativeToSeafloor(EntitySeaSerpent entity, World world, double x, double z, Random rand) {
        BlockPos pos;
        BlockPos topY = new BlockPos(x, entity.getPosY(), z);
        BlockPos bottomY = new BlockPos(x, entity.getPosY(), z);
        while (isWaterBlock(world, topY) && topY.getY() < world.getHeight()) {
            topY = topY.up();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.down();
        }
        if (entity.ticksSinceJump > TIME_BETWEEN_JUMPS) {
            return topY.up((int) Math.ceil(3 * entity.getSeaSerpentScale()));

        }
        if (entity.ticksSinceRoar > TIME_BETWEEN_ROARS || entity.getAnimation() == ANIMATION_ROAR) {
            return topY.down();
        }
        for (int tries = 0; tries < 5; tries++) {
            pos = new BlockPos(x, bottomY.getY() + 1 + rand.nextInt(Math.max(1, topY.getY() - bottomY.getY() - 2)), z);
            if (isWaterBlock(world, pos)) {
                return pos;
            }
        }
        return entity.getPosition();
    }

    public static BlockPos getPositionInOrbit(EntitySeaSerpent entity, World world, BlockPos orbit, Random rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 5 * entity.getSeaSerpentScale();
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = new BlockPos(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    public static BlockPos getPositionPreyArc(EntitySeaSerpent entity, BlockPos target, World world) {
        float radius = 10 * entity.getSeaSerpentScale();
        entity.renderYawOffset = entity.rotationYaw;
        float angle = (0.01745329251F * entity.renderYawOffset);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double signum = Math.signum(target.getY() + 0.5F - entity.getPosY());
        BlockPos pos = new BlockPos(target.getX() + extraX, target.getY() + entity.rand.nextInt(5) * signum, target.getZ() + extraZ);
        entity.isArcing = true;
        return clampBlockPosToWater(entity, world, pos);
    }

    private static BlockPos clampBlockPosToWater(Entity entity, World world, BlockPos pos) {
        BlockPos topY = new BlockPos(pos.getX(), entity.getPosY(), pos.getZ());
        BlockPos bottomY = new BlockPos(pos.getX(), entity.getPosY(), pos.getZ());
        while (isWaterBlock(world, topY) && topY.getY() < world.getHeight()) {
            topY = topY.up();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.down();
        }
        return new BlockPos(pos.getX(), MathHelper.clamp(pos.getY(), bottomY.getY() + 1, topY.getY() - 1), pos.getZ());
    }

    public static boolean isWaterBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SeaSerpentAIGetInWater(this, 1.0D));
        this.goalSelector.addGoal(1, new EntitySeaSerpent.AISwimBite());
        this.goalSelector.addGoal(1, new EntitySeaSerpent.AISwimWander());
        this.goalSelector.addGoal(1, new EntitySeaSerpent.AISwimCircle());
        this.goalSelector.addGoal(2, new SeaSerpentAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(3, new EntityAIWatchClosestIgnoreRider(this, LivingEntity.class, 6.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new FlyingAITarget(this, LivingEntity.class, 0, true, false, NOT_SEA_SERPENT));
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return this.isAncient() ? 30 : 15;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveController = new MovementController(this);
            this.navigator = new GroundPathNavigator(this, world);
            this.navigator.setCanSwim(true);
            this.isLandNavigator = true;
        } else {
            this.moveController = new EntitySeaSerpent.SwimmingMoveHelper();
            this.navigator = new SwimmerPathNavigator(this, world);
            this.isLandNavigator = false;
        }
    }

    public boolean isDirectPathBetweenPoints(BlockPos pos) {
        Vec3d vec3d = new Vec3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
        Vec3d vec3d1 = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;

    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IafConfig.dragonTargetSearchLength));
    }

    public void resetParts(float scale) {
        clearParts();
        segments = new EntityMutlipartPart[9];
        for (int i = 0; i < segments.length; i++) {
            if (i > 3) {
                segments[i] = new EntityDeathwormPart(this, (2F - ((i + 1) * 0.55F)) * scale, 0, 0, 0.5F * scale, 0.5F * scale, 1);
            } else {
                segments[i] = new EntityDeathwormPart(this, (1.8F - (i * 0.5F)) * scale, 0, 0, 0.45F * scale, 0.4F * scale, 1);
            }
        }
    }

    public void onUpdateParts() {
        for (EntityMutlipartPart entity : segments) {
            if (entity != null) {
                entity.setParent(this);
                if(!entity.shouldContinuePersisting()){
                    world.addEntity(entity);
                }
            }
        }
    }

    private void clearParts() {
        for (EntityMutlipartPart entity : segments) {
            if (entity != null) {
                entity.remove();
            }
        }
    }

    public void remove() {
        clearParts();
        super.remove();
    }

    @Override
    public float getRenderScale() {
        if (this.getSeaSerpentScale() != lastScale) {
            resetParts(this.getSeaSerpentScale());
        }
        lastScale = this.getSeaSerpentScale();
        return this.getSeaSerpentScale();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        onUpdateParts();
        if (this.isInWater()) {
            spawnParticlesAroundEntity(ParticleTypes.BUBBLE, this, (int) this.getSeaSerpentScale());
            for (Entity entity : segments) {
                spawnParticlesAroundEntity(ParticleTypes.BUBBLE, entity, (int) this.getSeaSerpentScale());
            }
        }
        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void spawnParticlesAroundEntity(IParticleData type, Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            double x = entity.getPosX() + (double) (this.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth();
            double y = entity.getPosY() + 0.5D + (double) (this.rand.nextFloat() * entity.getHeight());
            double z = entity.getPosZ() + (double) (this.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth();
            if (this.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER) {
                this.world.addParticle(type, x, y, z, 0, 0, 0);
            }
        }
    }

    private void spawnSlamParticles(IParticleData type) {
        for (int i = 0; i < this.getSeaSerpentScale() * 3; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = 1.25F * getSeaSerpentScale();
                float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);
                if (world.isRemote) {
                    world.addParticle(type, true, this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(0F));
        this.dataManager.register(JUMPING, false);
        this.dataManager.register(BREATHING, false);
        this.dataManager.register(ANCIENT, false);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("TicksSinceRoar", ticksSinceRoar);
        compound.putFloat("Scale", this.getSeaSerpentScale());
        compound.putBoolean("JumpingOutOfWater", this.isJumpingOutOfWater());
        compound.putBoolean("AttackDecision", attackDecision);
        compound.putBoolean("Breathing", this.isBreathing());
        compound.putBoolean("Ancient", this.isAncient());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
        ticksSinceRoar = compound.getInt("TicksSinceRoar");
        this.setSeaSerpentScale(compound.getFloat("Scale"));
        this.setJumpingOutOfWater(compound.getBoolean("JumpingOutOfWater"));
        attackDecision = compound.getBoolean("AttackDecision");
        this.setBreathing(compound.getBoolean("Breathing"));
        this.setAncient(compound.getBoolean("Ancient"));
    }

    private void updateAttributes() {
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.25D, 0.15D * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.max(4, IafConfig.seaSerpentAttackStrength * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(10, IafConfig.seaSerpentBaseHealth * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.heal(30F * this.getSeaSerpentScale());
    }

    private float getAncientModifier() {
        return this.isAncient() ? 1.5F : 1.0F;
    }

    public float getSeaSerpentScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    private void setSeaSerpentScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
        this.updateAttributes();
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public boolean isJumpingOutOfWater() {
        return this.dataManager.get(JUMPING).booleanValue();
    }

    public void setJumpingOutOfWater(boolean jump) {
        this.dataManager.set(JUMPING, jump);
    }

    public boolean isAncient() {
        return this.dataManager.get(ANCIENT).booleanValue();
    }

    public void setAncient(boolean ancient) {
        this.dataManager.set(ANCIENT, ancient);
    }

    public boolean isBreathing() {
        if (world.isRemote) {
            boolean breathing = this.dataManager.get(BREATHING).booleanValue();
            this.isBreathing = breathing;
            return breathing;
        }
        return isBreathing;
    }

    public void setBreathing(boolean breathing) {
        this.dataManager.set(BREATHING, breathing);
        if (!world.isRemote) {
            this.isBreathing = breathing;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void livingTick() {
        super.livingTick();
        if (!world.isRemote) {
            if (isJumpingOutOfWater() && swimBehavior == SwimBehavior.WANDER && shouldStopJumping()) {
                this.setMotion(this.getMotion().add(0, -0.25D, 0));
                if (this.isInWater()) {
                    this.setJumpingOutOfWater(false);
                    this.ticksSinceJump = 0;
                }
            }
            if (world.getDifficulty() == Difficulty.PEACEFUL && this.getAttackTarget() instanceof PlayerEntity) {
                this.setAttackTarget(null);
            }
        }
        boolean breathing = isBreathing() && this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_ROAR;
        boolean jumping = !this.isInWater() && !this.onGround && this.getMotion().y >= 0;
        boolean wantJumping = false; //(ticksSinceJump > TIME_BETWEEN_JUMPS) && this.isInWater();
        boolean ground = !isInWater() && this.onGround;
        boolean prevJumping = this.isJumpingOutOfWater();
        this.ticksSinceRoar++;
        this.ticksSinceJump++;
        if (this.ticksSinceRoar > TIME_BETWEEN_ROARS && isAtSurface() && this.getAnimation() != ANIMATION_BITE && jumpProgress == 0 && !isJumpingOutOfWater()) {
            this.setAnimation(ANIMATION_ROAR);
            this.ticksSinceRoar = 0;
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 1) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_ROAR, this.getSoundVolume() + 1, 1);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_BITE, this.getSoundVolume(), 1);
        }
        if (isJumpingOutOfWater()) {
            ticksJumping++;
        } else {
            ticksJumping = 0;
        }
        if (isJumpingOutOfWater() && isWaterBlock(world, new BlockPos(this).up(2))) {
            setJumpingOutOfWater(false);
        }
        if (!isJumpingOutOfWater() && !isWaterBlock(world, new BlockPos(this).up()) && (ticksSinceJump > TIME_BETWEEN_JUMPS || this.getAttackTarget() != null)) {
            ticksSinceJump = 0;
            setJumpingOutOfWater(true);
        }
        if (this.swimCycle < 38) {
            this.swimCycle += 2;
        } else {
            this.swimCycle = 0;
        }
        if (breathing && breathProgress < 20.0F) {
            breathProgress += 0.5F;
        } else if (!breathing && breathProgress > 0.0F) {
            breathProgress -= 0.5F;
        }
        if (jumping && jumpProgress < 10.0F) {
            jumpProgress += 0.5F;
        } else if (!jumping && jumpProgress > 0.0F) {
            jumpProgress -= 0.5F;
        }
        if (wantJumping && wantJumpProgress < 10.0F) {
            wantJumpProgress += 2F;
        } else if (!wantJumping && wantJumpProgress > 0.0F) {
            wantJumpProgress -= 2F;
        }
        if (this.isJumpingOutOfWater() && jumpRot < 1.0F) {
            jumpRot += 0.1F;
        } else if (!this.isJumpingOutOfWater() && jumpRot > 0.0F) {
            jumpRot -= 0.1F;
        }
        if (prevJumping != this.isJumpingOutOfWater() && !this.isJumpingOutOfWater()) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_SPLASH, 5F, 0.75F);
            spawnSlamParticles(ParticleTypes.FIREWORK);
            spawnSlamParticles(ParticleTypes.BUBBLE);
            spawnSlamParticles(ParticleTypes.BUBBLE);
            spawnSlamParticles(ParticleTypes.BUBBLE);
            this.doSplashDamage();
        }
        if (!ground && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            switchNavigator(true);
        }
        renderYawOffset = rotationYaw;
        rotationPitch = MathHelper.clamp((float) this.getMotion().y * 20F, -90, 90);
        if (world.isRemote) {
            pitch_buffer.calculateChainWaveBuffer(90, 10, 10F, 0.5F, this);

            if (!jumping) {
                tail_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
                head_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
            }
        }
        if (changedSwimBehavior) {
            changedSwimBehavior = false;
        }
        if (!world.isRemote) {
            if (attackDecision) {
                this.setBreathing(false);
            }
            if (this.getAttackTarget() != null && this.getAnimation() != ANIMATION_ROAR) {
                if (!attackDecision) {
                    if (!this.getAttackTarget().isInWater() || !this.isDirectPathBetweenPoints(this.getAttackTarget().getPosition()) || this.getDistanceSq(this.getAttackTarget()) < 60 * this.getSeaSerpentScale()) {
                        attackDecision = true;
                    }
                    if (!attackDecision) {
                        shoot(this.getAttackTarget());
                    }
                } else {
                    if (this.getDistanceSq(this.getAttackTarget()) > 500 * this.getSeaSerpentScale()) {
                        attackDecision = false;
                    }
                }
            } else {
                this.setBreathing(false);
            }
            if (this.swimBehavior == SwimBehavior.CIRCLE) {
                ticksCircling++;
            } else {
                ticksCircling = 0;
            }
            if (this.getAttackTarget() != null) {
                if (this.isInWater()) {
                    if (this.attackDecision) {
                        if (isPreyAtSurface() && this.getDistanceSq(this.getAttackTarget()) < 200 * getSeaSerpentScale()) {
                            this.swimBehavior = SwimBehavior.JUMP;
                        } else {
                            this.swimBehavior = SwimBehavior.ATTACK;
                        }
                    } else {
                        this.swimBehavior = SwimBehavior.ATTACK;
                    }
                } else if (this.onGround) {
                    this.swimBehavior = SwimBehavior.ATTACK;
                } else {
                    this.swimBehavior = SwimBehavior.WANDER;
                }
            } else {
                if (this.swimBehavior == SwimBehavior.JUMP && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 200 * getSeaSerpentScale()) {
                    this.swimBehavior = SwimBehavior.WANDER;
                    this.ticksSinceJump = 0;
                    setJumpingOutOfWater(false);
                }
            }
            if (this.swimBehavior != SwimBehavior.JUMP && this.swimBehavior != SwimBehavior.ATTACK && this.ticksSinceJump > TIME_BETWEEN_JUMPS) {
                this.swimBehavior = SwimBehavior.JUMP;
            }
            if (swimBehavior != SwimBehavior.ATTACK) {
                arcingYAdditive = 0;
            }
            if (swimBehavior == SwimBehavior.JUMP && this.getMotion().y < 0 && !this.isInWater()) {
                this.swimBehavior = SwimBehavior.WANDER;
                this.ticksSinceJump = 0;
                setJumpingOutOfWater(false);
            }
            if (this.swimBehavior == SwimBehavior.ATTACK && this.getAttackTarget() != null && !this.getAttackTarget().isInWater()) {
                this.swimBehavior = SwimBehavior.WANDER;
                this.faceEntity(this.getAttackTarget(), 360, 360);
            }
            if (this.swimBehavior == SwimBehavior.ATTACK && (this.getAttackTarget() == null || !isDirectPathBetweenPoints(new BlockPos(this.getAttackTarget())))) {
                this.swimBehavior = SwimBehavior.WANDER;
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && (this.isTouchingMob(this.getAttackTarget()) || this.getDistanceSq(this.getAttackTarget()) < 50)) {
            this.hurtMob(this.getAttackTarget());
        }
        breakBlock();
        if (!world.isRemote && this.isPassenger() && this.getLowestRidingEntity() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity) this.getLowestRidingEntity();
            boat.remove();
            this.stopRiding();
        }
    }

    private void doSplashDamage() {
        double getWidth = 2D * this.getSeaSerpentScale();
        List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(getWidth, getWidth * 0.5D, getWidth), NOT_SEA_SERPENT);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
                destroyBoat(entity);
                double xRatio = this.getPosX() - entity.getPosX();
                double zRatio = this.getPosZ() - entity.getPosZ();
                float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
                float strength = 0.3F * this.getSeaSerpentScale();
                entity.setMotion(entity.getMotion().mul(0.5D, 1D, 0.5D));
                entity.setMotion(entity.getMotion().add(xRatio / (double) f * (double) strength, strength, zRatio / (double) f * (double) strength));
            }
        }

    }

    public void destroyBoat(Entity sailor) {
        if (sailor.getRidingEntity() != null && sailor.getRidingEntity() instanceof BoatEntity && !world.isRemote) {
            BoatEntity boat = (BoatEntity) sailor.getRidingEntity();
            boat.remove();
            if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                for (int i = 0; i < 3; ++i) {
                    boat.entityDropItem(new ItemStack(Item.getItemFromBlock(boat.getBoatType().asPlank())), 0.0F);
                }
                for (int j = 0; j < 2; ++j) {
                    boat.entityDropItem(new ItemStack(Items.STICK));
                }
            }
        }
    }

    private boolean isPreyAtSurface() {
        if (this.getAttackTarget() != null) {
            BlockPos pos = new BlockPos(this.getAttackTarget());
            return !isWaterBlock(world, pos.up((int) Math.ceil(this.getAttackTarget().getHeight())));
        }
        return false;
    }

    private void hurtMob(LivingEntity entity) {
        if (this.getAnimation() == ANIMATION_BITE && entity != null && this.getAnimationTick() > 6) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
            EntitySeaSerpent.this.attackDecision = this.getRNG().nextBoolean();
        }
    }

    public void moveJumping() {
        float velocity = 0.5F;
        double x = -MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F);
        double z = MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F);
        float f = MathHelper.sqrt(x * x + z * z);
        x = x / (double) f;
        z = z / (double) f;
        x = x * (double) velocity;
        z = z * (double) velocity;
        this.setMotion(x, this.getMotion().y, z);
    }

    private boolean isTouchingMob(Entity entity) {
        if (this.getBoundingBox().expand(this.getSeaSerpentScale() * 3, this.getSeaSerpentScale() * 3, this.getSeaSerpentScale() * 3).intersects(entity.getBoundingBox())) {
            return true;
        }
        for (Entity segment : segments) {
            if (segment.getBoundingBox().expand(this.getSeaSerpentScale() * 2, this.getSeaSerpentScale() * 2, this.getSeaSerpentScale() * 2).intersects(entity.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void breakBlock() {
        if (IafConfig.seaSerpentGriefing) {
            for (int a = (int) Math.round(this.getBoundingBox().minX) - 2; a <= (int) Math.round(this.getBoundingBox().maxX) + 2; a++) {
                for (int b = (int) Math.round(this.getBoundingBox().minY) - 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getBoundingBox().minZ) - 2; c <= (int) Math.round(this.getBoundingBox().maxZ) + 2; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = world.getBlockState(pos);
                        IFluidState fluidState = world.getFluidState(pos);
                        Block block = state.getBlock();
                        if (state.getMaterial() != Material.AIR && (state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.LEAVES) && fluidState.isEmpty()) {
                            if (block != Blocks.AIR) {
                                if (!world.isRemote) {
                                    world.destroyBlock(pos, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRNG().nextInt(7));
        boolean ancient = this.getRNG().nextInt(16) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + this.getRNG().nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + this.getRNG().nextFloat() * 4.0F);
        }
        return spawnDataIn;
    }

    public void onWorldSpawn(Random random) {
        this.setVariant(random.nextInt(6));
        boolean ancient = random.nextInt(15) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + random.nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + random.nextFloat() * 4.0F);
        }
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
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
        return new Animation[]{ANIMATION_BITE, ANIMATION_ROAR, ANIMATION_SPEAK};
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.SEA_SERPENT_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.SEA_SERPENT_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.SEA_SERPENT_DIE;
    }

    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    private boolean canMove() {
        return true;
    }

    public boolean wantsToJump() {
        return this.ticksSinceJump > TIME_BETWEEN_JUMPS && this.swimBehavior == SwimBehavior.JUMP;
    }

    private boolean shouldStopJumping() {
        return ticksJumping > 30 || !isWaterBlock(world, world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(this)).down(1));
    }

    public boolean isAtSurface() {
        BlockPos pos = new BlockPos(this);
        return isWaterBlock(world, pos.down()) && !isWaterBlock(world, pos.up());
    }

    private void shoot(LivingEntity entity) {
        if (!this.attackDecision) {
            if (!this.isInWater()) {
                this.setBreathing(false);
                this.attackDecision = true;
            }
            if (this.isBreathing()) {
                if (this.ticksExisted % 40 == 0) {
                    this.playSound(IafSoundRegistry.SEA_SERPENT_BREATH, 4, 1);
                }
                if (this.ticksExisted % 5 == 0) {
                    rotationYaw = renderYawOffset;
                    float f1 = 0;
                    float f2 = 0;
                    float f3 = 0;
                    float headPosX = f1 + (float) (this.segments[0].getPosX() + 0.3F * getSeaSerpentScale() * Math.cos((rotationYaw + 90) * Math.PI / 180));
                    float headPosZ = f2 + (float) (this.segments[0].getPosZ() + 0.3F * getSeaSerpentScale() * Math.sin((rotationYaw + 90) * Math.PI / 180));
                    float headPosY = f3 + (float) (this.segments[0].getPosY() + 0.2F * getSeaSerpentScale());
                    double d2 = entity.getPosX() - headPosX;
                    double d3 = entity.getPosY() - headPosY;
                    double d4 = entity.getPosZ() - headPosZ;
                    EntitySeaSerpentBubbles entitylargefireball = new EntitySeaSerpentBubbles(IafEntityRegistry.SEA_SERPENT_BUBBLES, world, this, d2, d3, d4);
                    float size = 0.8F;
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                    if (!world.isRemote && entity.isAlive()) {
                        world.addEntity(entitylargefireball);
                    }
                    if (!entity.isAlive() || entity == null) {
                        this.setBreathing(false);
                        this.attackDecision = this.getRNG().nextBoolean();
                    }
                }
            } else {
                this.setBreathing(true);
            }
        }
        this.faceEntity(entity, 360, 360);
    }

    public EnumSeaSerpent getEnum() {
        switch (this.getVariant()) {
            default:
                return EnumSeaSerpent.BLUE;
            case 1:
                return EnumSeaSerpent.BRONZE;
            case 2:
                return EnumSeaSerpent.DEEPBLUE;
            case 3:
                return EnumSeaSerpent.GREEN;
            case 4:
                return EnumSeaSerpent.PURPLE;
            case 5:
                return EnumSeaSerpent.RED;
            case 6:
                return EnumSeaSerpent.TEAL;
        }
    }

    @Override
    public void travel(Vec3d motionVec) {
        if (this.swimBehavior == SwimBehavior.JUMP && this.isJumpingOutOfWater() && this.getAttackTarget() == null) {
            // moveJumping();
        }
        float f4;
        if (this.isServerWorld()) {
            float f5;
            if (this.isInWater()) {
                this.moveRelative(0.1F, motionVec);
                f4 = 0.6F;
                float d0 = (float) EnchantmentHelper.getDepthStriderModifier(this);
                if (d0 > 3.0F) {
                    d0 = 3.0F;
                }
                if (!this.onGround) {
                    d0 *= 0.5F;
                }
                if (d0 > 0.0F) {
                    f4 += (0.54600006F - f4) * d0 / 3.0F;
                }
                this.move(MoverType.SELF, motionVec);
                this.setMotion(this.getMotion().mul(0.9 * f4, 0.9 * f4, 0.9 * f4));
            } else {
                super.travel(motionVec);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.getPosX() - this.prevPosX;
        double deltaZ = this.getPosZ() - this.prevPosZ;
        double deltaY = this.getPosY() - this.prevPosY;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public void onKillEntity(LivingEntity entity) {
        super.onKillEntity(entity);
        this.attackDecision = this.getRNG().nextBoolean();
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public int getMaxFallHeight() {
        return 1000;
    }

    enum SwimBehavior {
        CIRCLE,
        WANDER,
        ATTACK,
        JUMP,
        NONE
    }

    public class SwimmingMoveHelper extends MovementController {
        private EntitySeaSerpent serpent = EntitySeaSerpent.this;

        public SwimmingMoveHelper() {
            super(EntitySeaSerpent.this);
        }

        @Override
        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                double d0 = this.posX - EntitySeaSerpent.this.getPosX();
                double d1 = this.posY - EntitySeaSerpent.this.getPosY();
                double d2 = this.posZ - EntitySeaSerpent.this.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);
                if (d3 < 6 && EntitySeaSerpent.this.getAttackTarget() == null) {
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.WANDER && EntitySeaSerpent.this.rand.nextInt(20) == 0) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.CIRCLE;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE && EntitySeaSerpent.this.rand.nextInt(5) == 0 && ticksCircling > 150) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.WANDER;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                }
                if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP && !EntitySeaSerpent.this.isInWater() && !onGround) {
                    EntitySeaSerpent.this.ticksSinceJump = 0;
                }
                int dist = EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP ? 10 : 3;
                if (d3 < dist && EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP && EntitySeaSerpent.this.shouldStopJumping() && EntitySeaSerpent.this.getAttackTarget() == null) {
                    this.action = MovementController.Action.WAIT;
                    EntitySeaSerpent.this.setMotion(EntitySeaSerpent.this.getMotion().mul(0.5D, 1, 0.5D));
                    if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                        EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                        EntitySeaSerpent.this.ticksSinceJump = 0;
                        EntitySeaSerpent.this.setJumpingOutOfWater(false);
                    }
                } else {
                    EntitySeaSerpent.this.setMotion(EntitySeaSerpent.this.getMotion().add(d0 / d3 * 0.5D * this.speed, d1 / d3 * 0.5D * this.speed, d2 / d3 * 0.5D * this.speed));
                    float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                    EntitySeaSerpent.this.rotationPitch = f1;
                    if (!EntitySeaSerpent.this.isArcing) {
                        if (EntitySeaSerpent.this.getAttackTarget() == null) {
                            EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(EntitySeaSerpent.this.getMotion().x, EntitySeaSerpent.this.getMotion().z)) * (180F / (float) Math.PI);
                            EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                        } else if (EntitySeaSerpent.this.swimBehavior != SwimBehavior.JUMP) {
                            double d4 = EntitySeaSerpent.this.getAttackTarget().getPosX() - EntitySeaSerpent.this.getPosX();
                            double d5 = EntitySeaSerpent.this.getAttackTarget().getPosZ() - EntitySeaSerpent.this.getPosZ();
                            EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                            EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                        }
                    }
                }
            }
        }
    }

    public class AISwimWander extends Goal {
        BlockPos target;

        public AISwimWander() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.WANDER && EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.JUMP || !EntitySeaSerpent.this.canMove() || EntitySeaSerpent.this.getAttackTarget() != null) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater()) {
                BlockPos gen = generateTarget();
                if (gen != null) {
                    target = gen;
                    EntitySeaSerpent.this.orbitPos = null;
                    return EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP || !EntitySeaSerpent.this.getMoveHelper().isUpdating();
                }
            }
            return false;
        }

        protected BlockPos generateTarget() {
            if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.getPosX(), EntitySeaSerpent.this.getPosZ(), EntitySeaSerpent.this.rand);
                return pos.up(3 * (int) Math.ceil(EntitySeaSerpent.this.getSeaSerpentScale()));
            }
            for (int i = 0; i < 5; i++) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.getPosX() + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.getPosZ() + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                if (EntitySeaSerpent.isWaterBlock(world, pos) && EntitySeaSerpent.this.isDirectPathBetweenPoints(pos) || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                    return pos;
                }
            }
            return null;
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return true;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            if (target == null)
                target = generateTarget();
            if (target != null && (EntitySeaSerpent.isWaterBlock(world, target) || EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.JUMP) && EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
                EntitySeaSerpent.this.moveController.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntitySeaSerpent.this.getAttackTarget() == null) {
                    EntitySeaSerpent.this.getLookController().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AISwimCircle extends Goal {
        BlockPos target;

        public AISwimCircle() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.CIRCLE || !EntitySeaSerpent.this.canMove()) {
                return false;
            }
            if (!EntitySeaSerpent.this.getMoveHelper().isUpdating()) {
                EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                return false;
            }
            if (EntitySeaSerpent.this.isInWater() && !EntitySeaSerpent.this.isJumpingOutOfWater()) {
                BlockPos gen = generateTarget();
                if (gen != null) {
                    EntitySeaSerpent.this.orbitPos = gen;
                    target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
                    return EntitySeaSerpent.this.isDirectPathBetweenPoints(target);
                } else {
                    EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                }
            }
            return false;
        }

        protected BlockPos generateTarget() {
            for (int i = 0; i < 5; i++) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.getPosX() + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.getPosZ() + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                if (EntitySeaSerpent.isWaterBlock(world, pos) && EntitySeaSerpent.this.isDirectPathBetweenPoints(pos)) {
                    return pos;
                }
            }
            return null;
        }


        public boolean shouldContinueExecuting() {
            if (target != null && !EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
                return false;
            }
            return EntitySeaSerpent.this.getAttackTarget() == null && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE;
        }

        public void tick() {
            if (EntitySeaSerpent.this.getDistanceSq(new Vec3d(target)) < 25) {
                target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
            }
            if (EntitySeaSerpent.isWaterBlock(world, target) && EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
                EntitySeaSerpent.this.moveController.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntitySeaSerpent.this.getAttackTarget() == null) {
                    EntitySeaSerpent.this.getLookController().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AISwimBite extends Goal {
        public int max_distance = 1000;
        BlockPos target;
        boolean secondPhase = false;
        boolean isOver = false;

        public AISwimBite() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public void resetTask() {
            target = null;
            secondPhase = false;
            isOver = false;
            EntitySeaSerpent.this.isArcing = false;
            EntitySeaSerpent.this.arcingYAdditive = 0;

        }

        public boolean shouldExecute() {
            if (!EntitySeaSerpent.this.attackDecision && EntitySeaSerpent.this.getAttackTarget() != null && EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) < 300) {
                return false;
            }
            if (EntitySeaSerpent.this.swimBehavior != SwimBehavior.ATTACK && EntitySeaSerpent.this.swimBehavior != SwimBehavior.JUMP || !EntitySeaSerpent.this.canMove() || EntitySeaSerpent.this.getAttackTarget() == null) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater() && EntitySeaSerpent.this.getAttackTarget() != null) {
                target = new BlockPos(EntitySeaSerpent.this.getAttackTarget());
                EntitySeaSerpent.this.orbitPos = null;
                secondPhase = false;
                EntitySeaSerpent.this.isArcing = false;
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return true;
        }

        public boolean shouldContinueExecuting() {
            if (isOver) {
                return false;
            }
            if (!EntitySeaSerpent.this.attackDecision && EntitySeaSerpent.this.getAttackTarget() != null && EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) < 300) {
                resetTask();
                return false;
            }
            if (secondPhase) {
                if (EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.getDistanceSq(target.getX(), target.getY(), target.getZ()) > max_distance || isOver) {
                    EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                    resetTask();
                    return false;
                } else {
                    return true;
                }
            } else {
                return EntitySeaSerpent.this.getAttackTarget() != null;
            }
        }

        public void tick() {
            if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                if (EntitySeaSerpent.this.getAttackTarget() != null) {
                    if (EntitySeaSerpent.this.isInWater()) {
                        target = new BlockPos(EntitySeaSerpent.this.getAttackTarget()).up((int) Math.ceil(3 * EntitySeaSerpent.this.getSeaSerpentScale()));
                    }
                }
            } else {
                if (EntitySeaSerpent.this.getAttackTarget() == null || !EntitySeaSerpent.this.getAttackTarget().isAlive()) {
                    this.secondPhase = true;
                } else {
                    double d0 = EntitySeaSerpent.this.getAttackTarget().getPosX() - EntitySeaSerpent.this.getPosX();
                    double d1 = EntitySeaSerpent.this.getAttackTarget().getPosZ() - EntitySeaSerpent.this.getPosZ();
                    double d2 = d0 * d0 + d1 * d1;
                    d2 = MathHelper.sqrt(d2);
                    EntitySeaSerpent.this.arcingYAdditive = (secondPhase ? 1 : -1) * (float) d2;
                }
                if (!secondPhase) {
                    target = new BlockPos(EntitySeaSerpent.this.getAttackTarget());
                    if (!EntitySeaSerpent.this.attackDecision) {
                        if (EntitySeaSerpent.this.getAttackTarget() != null) {
                            if (EntitySeaSerpent.this.getDistanceSq(new Vec3d(target)) > 10 * EntitySeaSerpent.this.getSeaSerpentScale()) {
                                EntitySeaSerpent.this.setBreathing(true);
                            } else {
                                EntitySeaSerpent.this.attackDecision = true;
                            }
                        }
                    } else {
                        if (EntitySeaSerpent.this.getAttackTarget() != null && (EntitySeaSerpent.this.getDistanceSq(new Vec3d(target)) < 30 * EntitySeaSerpent.this.getSeaSerpentScale() || EntitySeaSerpent.this.isTouchingMob(EntitySeaSerpent.this.getAttackTarget()))) {
                            EntitySeaSerpent.this.setAnimation(ANIMATION_BITE);
                        }
                        if (EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.getDistanceSq(new Vec3d(target)) < 30 * EntitySeaSerpent.this.getSeaSerpentScale()) {
                            target = null;
                            secondPhase = true;
                        }
                    }
                }
                if (secondPhase) {
                    if (EntitySeaSerpent.this.getAttackTarget() != null) {
                        if (EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) > max_distance || target != null && EntitySeaSerpent.this.getDistanceSq(new Vec3d(target)) < 5) {
                            resetTask();
                            isOver = true;
                        } else {
                            target = EntitySeaSerpent.getPositionPreyArc(EntitySeaSerpent.this, new BlockPos(EntitySeaSerpent.this.getAttackTarget()), world);
                        }
                    }
                }
            }

            if (target != null) {
                if (EntitySeaSerpent.isWaterBlock(world, target) || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                    EntitySeaSerpent.this.moveController.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.5D);
                    if (EntitySeaSerpent.this.getAttackTarget() == null) {
                        EntitySeaSerpent.this.getLookController().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                    }
                }
            }

        }
    }
}
