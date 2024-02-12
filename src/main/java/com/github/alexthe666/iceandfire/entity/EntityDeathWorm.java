package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormLand;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormSand;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.PositionImpl;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityDeathWorm extends TamableAnimal implements ISyncMount, ICustomCollisions, IBlacklistedFromStatues, IAnimatedEntity, IVillagerFear, IAnimalFear, IGroundMount, IHasCustomizableAttributes, ICustomMoveController {

    public static final ResourceLocation TAN_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_tan");
    public static final ResourceLocation WHITE_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_white");
    public static final ResourceLocation RED_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_red");
    public static final ResourceLocation TAN_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_tan_giant");
    public static final ResourceLocation WHITE_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_white_giant");
    public static final ResourceLocation RED_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_red_giant");
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> JUMP_TICKS = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> WORM_AGE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> HOME = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.BLOCK_POS);
    public static Animation ANIMATION_BITE = Animation.create(10);

    public ChainBuffer tail_buffer;
    public float jumpProgress;
    public float prevJumpProgress;
    private int animationTick;
    private boolean willExplode = false;
    private int ticksTillExplosion = 60;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[6];
    private boolean isSandNavigator;
    private final float prevScale = 0.0F;
    private final LookControl lookHelper;
    private int growthCounter = 0;
    private Player thrower;
    public DeathwormAITargetItems targetItemsGoal;

    public EntityDeathWorm(EntityType<EntityDeathWorm> type, Level worldIn) {
        super(type, worldIn);
        setPathfindingMalus(BlockPathTypes.OPEN, 2.0f); // FIXME :: Death worms are trying to go upwards -> figure out why (or if this really helps)
        setPathfindingMalus(BlockPathTypes.WATER, 4.0f);
        setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0f);
        this.lookHelper = new IAFLookHelper(this);
        this.noCulling = true;
        if (worldIn.isClientSide) {
            tail_buffer = new ChainBuffer();
        }
        this.setMaxUpStep(1F);
        this.switchNavigator(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EntityGroundAIRide<>(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new DeathWormAIAttack(this));
        this.goalSelector.addGoal(3, new DeathWormAIJump(this, 12));
        this.goalSelector.addGoal(4, new DeathWormAIFindSandTarget(this, 10));
        this.goalSelector.addGoal(5, new DeathWormAIGetInSand(this, 1.0D));
        this.goalSelector.addGoal(6, new DeathWormAIWander(this, 1));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, targetItemsGoal = new DeathwormAITargetItems(this, false, false));
        this.targetSelector.addGoal(5, new DeathWormAITarget(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity input) {
                if (EntityDeathWorm.this.isTame()) {
                    return input instanceof Monster;
                } else if (input != null) {
                    if (input.isInWater() || !DragonUtils.isAlive(input) || isOwnedBy(input)) {
                        return false;
                    }

                    if (input instanceof Player || input instanceof Animal) {
                        return true;
                    }

                    return IafConfig.deathWormAttackMonsters;
                }

                return false;
            }
        }));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
                //HEALTH
                .add(Attributes.MAX_HEALTH, IafConfig.deathWormMaxHealth)
                //SPEED
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                //ATTACK
                .add(Attributes.ATTACK_DAMAGE, IafConfig.deathWormAttackStrength)
                //FOLLOW RANGE
                .add(Attributes.FOLLOW_RANGE, IafConfig.deathWormTargetSearchLength)
                //ARMOR
                .add(Attributes.ARMOR, 3);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(6, IafConfig.deathWormMaxHealth));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(1, IafConfig.deathWormAttackStrength));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(IafConfig.deathWormTargetSearchLength);
    }

    @Override
    public @NotNull LookControl getLookControl() {
        return this.lookHelper;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean getCanSpawnHere() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        this.level().getBlockState(blockpos.below()).is(BlockTags.SAND);
        return this.level().getBlockState(blockpos.below()).is(BlockTags.SAND)
                && this.level().getMaxLocalRawBrightness(blockpos) > 8;
    }

    public void onUpdateParts() {
        addSegmentsToWorld();
        // FIXME :: Unused
//        if (isSandBelow()) {
//            int i = Mth.floor(this.getX());
//            int j = Mth.floor(this.getY() - 1);
//            int k = Mth.floor(this.getZ());
//            BlockPos blockpos = new BlockPos(i, j, k);
//            BlockState BlockState = this.level.getBlockState(blockpos);
//
//            if (level.isClientSide) {
//                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, BlockState), this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getSurface((int) Math.floor(this.getPosX()), (int) Math.floor(this.getPosY()), (int) Math.floor(this.getPosZ())) + 0.5F, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
//            }
//        }
    }

    @Override
    public int getExperienceReward() {
        return this.getScale() > 3 ? 20 : 10;
    }

    public void initSegments(float scale) {
        segments = new EntityMutlipartPart[7];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new EntitySlowPart(this, (-0.8F - (i * 0.8F)) * scale, 0, 0, 0.7F * scale, 0.7F * scale, 1);
            segments[i].copyPosition(this);
            segments[i].setParent(this);
        }
    }

    private void addSegmentsToWorld() {
        for (EntityMutlipartPart entity : segments) {
            EntityUtil.updatePart(entity, this);
        }
    }

    private void clearSegments() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.kill();
                entity.remove(RemovalReason.KILLED);
            }
        }
    }

    public void setExplosive(boolean explosive, Player thrower) {
        this.willExplode = true;
        this.ticksTillExplosion = 60;
        this.thrower = thrower;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            this.playSound(this.getScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
        }
        if (this.getRandom().nextInt(3) == 0 && this.getScale() > 1 && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, entityIn.getX(), entityIn.getY(), entityIn.getZ()))) {
                BlockLaunchExplosion explosion = new BlockLaunchExplosion(level(), this, entityIn.getX(), entityIn.getY(), entityIn.getZ(), this.getScale());
                explosion.explode();
                explosion.finalizeExplosion(true);
            }
        }
        return false;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        clearSegments();
        super.die(cause);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        switch (this.getVariant()) {
            case 0:
                return this.getScale() > 3 ? TAN_GIANT_LOOT : TAN_LOOT;
            case 1:
                return this.getScale() > 3 ? RED_GIANT_LOOT : RED_LOOT;
            case 2:
                return this.getScale() > 3 ? WHITE_GIANT_LOOT : WHITE_LOOT;
        }
        return null;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SCALE, 1F);
        this.entityData.define(CONTROL_STATE, (byte) 0);
        this.entityData.define(WORM_AGE, 10);
        this.entityData.define(HOME, BlockPos.ZERO);
        this.entityData.define(JUMP_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("GrowthCounter", this.growthCounter);
        compound.putFloat("Scale", this.getDeathwormScale());
        compound.putInt("WormAge", this.getWormAge());
        compound.putLong("WormHome", this.getWormHome().asLong());
        compound.putBoolean("WillExplode", this.willExplode);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.growthCounter = compound.getInt("GrowthCounter");
        this.setDeathWormScale(compound.getFloat("Scale"));
        this.setWormAge(compound.getInt("WormAge"));
        this.setWormHome(BlockPos.of(compound.getLong("WormHome")));
        this.willExplode = compound.getBoolean("WillExplode");
        this.setConfigurableAttributes();
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = entityData.get(CONTROL_STATE).byteValue();
        if (newState) {
            entityData.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            entityData.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return entityData.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        entityData.set(CONTROL_STATE, state);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public int getWormJumping() {
        return this.entityData.get(JUMP_TICKS);
    }

    public void setWormJumping(int jump) {
        this.entityData.set(JUMP_TICKS, jump);
    }

    public BlockPos getWormHome() {
        return this.entityData.get(HOME);
    }

    public void setWormHome(BlockPos home) {
        if (home instanceof BlockPos) {
            this.entityData.set(HOME, home);
        }
    }

    public int getWormAge() {
        return Math.max(1, entityData.get(WORM_AGE).intValue());
    }

    public void setWormAge(int age) {
        this.entityData.set(WORM_AGE, age);
    }

    @Override
    public float getScale() {
        return Math.min(this.getDeathwormScale() * (this.getWormAge() / 5F), 7F);
    }

    public float getDeathwormScale() {
        return this.entityData.get(SCALE).floatValue();
    }

    public void setDeathWormScale(float scale) {
        this.entityData.set(SCALE, scale);
        this.updateAttributes();
        clearSegments();
        if (!this.level().isClientSide) {
            initSegments(scale * (this.getWormAge() / 5F));
            IceAndFire.sendMSGToAll(new MessageDeathWormHitbox(this.getId(), scale * (this.getWormAge() / 5F)));
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.getRandom().nextInt(3));
        float size = 0.25F + (float) (Math.random() * 0.35F);
        this.setDeathWormScale(this.getRandom().nextInt(20) == 0 ? size * 4 : size);
        return spawnDataIn;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger)) {
            this.setYBodyRot(passenger.getYRot());
            float radius = -0.5F * this.getScale();
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            passenger.setPos(this.getX() + extraX, this.getY() + this.getEyeHeight() - 0.55F, this.getZ() + extraZ);
        }
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player) {
                Player player = (Player) passenger;
                return player;
            }
        }
        return null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.getWormAge() > 4 && player.getVehicle() == null && player.getMainHandItem().getItem() == Items.FISHING_ROD && player.getOffhandItem().getItem() == Items.FISHING_ROD && !this.level().isClientSide) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    private void switchNavigator(boolean inSand) {
        if (inSand) {
            this.moveControl = new EntityDeathWorm.SandMoveHelper();
            this.navigation = new PathNavigateDeathWormSand(this, level());
            this.isSandNavigator = true;
        } else {
            this.moveControl = new MoveControl(this);
            this.navigation = new PathNavigateDeathWormLand(this, level());
            this.isSandNavigator = false;
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FALLING_BLOCK)) {
            return false;
        }
        if (this.isVehicle() && source.getEntity() != null && this.getControllingPassenger() != null && source.getEntity() == this.getControllingPassenger()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void move(@NotNull MoverType typeIn, @NotNull Vec3 pos) {
        super.move(typeIn, pos);
    }

    @Override
    public @NotNull Vec3 collide(@NotNull Vec3 vec) {
        return ICustomCollisions.getAllowedMovementForEntity(this, vec);
    }

    @Override
    public boolean isInWall() {
        if (this.isInSand()) {
            return false;
        } else {
            return super.isInWall();
        }
    }


    @Override
    protected void moveTowardsClosestSpace(double x, double y, double z) {
        PositionImpl blockpos = new PositionImpl(x, y, z);
        Vec3i vec3i = new Vec3i((int) Math.round(blockpos.x()), (int) Math.round(blockpos.y()), (int) Math.round(blockpos.z()));
        Vec3 vector3d = new Vec3(x - blockpos.x(), y - blockpos.y(), z - blockpos.z());
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        Direction direction = Direction.UP;
        double d0 = Double.MAX_VALUE;

        for (Direction direction1 : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP}) {
            blockpos$mutable.setWithOffset(vec3i, direction1);
            if (!this.level().getBlockState(blockpos$mutable).isCollisionShapeFullBlock(this.level(), blockpos$mutable)
                    || level().getBlockState(blockpos$mutable).is(BlockTags.SAND)) {
                double d1 = vector3d.get(direction1.getAxis());
                double d2 = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0D - d1 : d1;
                if (d2 < d0) {
                    d0 = d2;
                    direction = direction1;
                }
            }
        }

        float f = this.random.nextFloat() * 0.2F + 0.1F;
        float f1 = (float) direction.getAxisDirection().getStep();
        Vec3 vector3d1 = this.getDeltaMovement().scale(0.75D);
        if (direction.getAxis() == Direction.Axis.X) {
            this.setDeltaMovement(f1 * f, vector3d1.y, vector3d1.z);
        } else if (direction.getAxis() == Direction.Axis.Y) {
            this.setDeltaMovement(vector3d1.x, f1 * f, vector3d1.z);
        } else if (direction.getAxis() == Direction.Axis.Z) {
            this.setDeltaMovement(vector3d1.x, vector3d1.y, f1 * f);
        }
    }

    private void updateAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.2D, 0.15D * this.getScale()));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(1, IafConfig.deathWormAttackStrength * this.getScale()));
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(6, IafConfig.deathWormMaxHealth * this.getScale()));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(IafConfig.deathWormTargetSearchLength);
        this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel world, @NotNull LivingEntity entity) {
        if (this.isTame()) {
            this.heal(14);
            return false;
        }
        return true;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
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
    public void aiStep() {
        super.aiStep();
        prevJumpProgress = jumpProgress;
        if (this.getWormJumping() > 0 && jumpProgress < 5F) {
            jumpProgress++;
        }
        if (this.getWormJumping() == 0 && jumpProgress > 0F) {
            jumpProgress--;
        }
        if (this.isInSand() && this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.05, 0));
        }
        if (this.getWormJumping() > 0) {
            float f2 = (float) -((float) this.getDeltaMovement().y * (double) (180F / (float) Math.PI));
            this.setXRot(f2);
            if (this.isInSand() || this.onGround()) {
                this.setWormJumping(this.getWormJumping() - 1);
            }
        }
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        if (this.getTarget() != null && (!this.getTarget().isAlive() || !DragonUtils.isAlive(this.getTarget()))) {
            this.setTarget(null);
        }
        if (this.willExplode) {
            if (this.ticksTillExplosion == 0) {
                boolean b = !MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, this.getX(), this.getY(), this.getZ()));
                if (b) {
                    level().explode(this.thrower, this.getX(), this.getY(), this.getZ(), 2.5F * this.getScale(), false, Level.ExplosionInteraction.MOB);
                }
                this.thrower = null;
            } else {
                this.ticksTillExplosion--;
            }
        }
        if (this.tickCount == 1) {
            initSegments(this.getScale());
        }
        if (isInSandStrict()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.08D, 0));
        }
        if (growthCounter > 1000 && this.getWormAge() < 5) {
            growthCounter = 0;
            this.setWormAge(Math.min(5, this.getWormAge() + 1));
            this.clearSegments();
            this.heal(15);
            this.setDeathWormScale(this.getDeathwormScale());
            if (level().isClientSide) {
                for (int i = 0; i < 10 * this.getScale(); i++) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getSurface((int) Math.floor(this.getX()), (int) Math.floor(this.getY()), (int) Math.floor(this.getZ())) + 0.5F, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                    /*
                    for (int j = 0; j < segments.length; j++) {
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, segments[j].getPosX() + (double) (this.rand.nextFloat() * segments[j].getWidth() * 2.0F) - (double) segments[j].getWidth(), this.getSurface((int) Math.floor(segments[j].getPosX()), (int) Math.floor(segments[j].getPosY()), (int) Math.floor(segments[j].getPosZ())) + 0.5F, segments[j].getPosZ() + (double) (this.rand.nextFloat() * segments[j].getWidth() * 2.0F) - (double) segments[j].getWidth(), this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D);
                    }
                     */
                }
            }
        }
        if (this.getWormAge() < 5) {
            growthCounter++;
        }
        if (this.getControllingPassenger() != null && this.getTarget() != null) {
            this.getNavigation().stop();
            this.setTarget(null);
        }
        //this.faceEntity(this.getAttackTarget(), 10.0F, 10.0F);
           /* if (dist >= 4.0D * getRenderScale() && dist <= 16.0D * getRenderScale() && (this.isInSand() || this.onGround)) {
                this.setWormJumping(true);
                double d0 = this.getAttackTarget().getPosX() - this.getPosX();
                double d1 = this.getAttackTarget().getPosZ() - this.getPosZ();
                float leap = MathHelper.sqrt(d0 * d0 + d1 * d1);
                if ((double) leap >= 1.0E-4D) {
                    this.setMotion(this.getMotion().add(d0 / (double) leap * 0.5D, 0.15F, d1 / (double) leap * 0.5D));
                }
                this.setAnimation(ANIMATION_BITE);
            }*/
        if (this.getTarget() != null && this.distanceTo(this.getTarget()) < Math.min(4, 4D * getScale()) && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            this.getTarget().hurt(this.level().damageSources().mobAttack(this), f);
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.4F, 0));
        }

    }

    public int getWormBrightness(boolean sky) {
        Vec3 vec3 = this.getEyePosition(1.0F);
        BlockPos eyePos = BlockPos.containing(vec3);
        while (eyePos.getY() < 256 && !level().isEmptyBlock(eyePos)) {
            eyePos = eyePos.above();
        }
        int light = this.level().getBrightness(sky ? LightLayer.SKY : LightLayer.BLOCK, eyePos.above());
        return light;
    }

    public int getSurface(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        while (!level().isEmptyBlock(pos)) {
            pos = pos.above();
        }
        return pos.getY();
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.getScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_IDLE : IafSoundRegistry.DEATHWORM_IDLE;
    }


    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.getScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_HURT : IafSoundRegistry.DEATHWORM_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return this.getScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_DIE : IafSoundRegistry.DEATHWORM_DIE;
    }

    @Override
    public void tick() {
        super.tick();
        refreshDimensions();
        onUpdateParts();
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity target = DragonUtils.riderLookingAtEntity(this, this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.playSound(this.getScale() > 3 ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1, 1);
                if (this.getRandom().nextInt(3) == 0 && this.getScale() > 1) {
                    float radius = 1.5F * this.getScale();
                    float angle = (0.01745329251F * this.yBodyRot);
                    double extraX = radius * Mth.sin((float) (Math.PI + angle));
                    double extraZ = radius * Mth.cos(angle);
                    BlockLaunchExplosion explosion = new BlockLaunchExplosion(level(), this, this.getX() + extraX, this.getY() - this.getEyeHeight(), this.getZ() + extraZ, this.getScale() * 0.75F);
                    explosion.explode();
                    explosion.finalizeExplosion(true);
                }
            }
            if (target != null) {
                target.hurt(this.level().damageSources().mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.isInSand()) {
            BlockPos pos = new BlockPos(this.getBlockX(), this.getSurface(this.getBlockX(), this.getBlockY(), this.getBlockZ()), this.getBlockZ()).below();
            BlockState state = level().getBlockState(pos);
            if (state.isSolidRender(level(), pos)) {
                if (level().isClientSide) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getSurface((int) Math.floor(this.getX()), (int) Math.floor(this.getY()), (int) Math.floor(this.getZ())) + 0.5F, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
                }
            }
            if (this.tickCount % 10 == 0) {
                this.playSound(SoundEvents.SAND_BREAK, 1, 0.5F);
            }
        }
        if (this.up() && this.onGround()) {
            this.jumpFromGround();
        }
        boolean inSand = isInSand() || this.getControllingPassenger() == null;
        if (inSand && !this.isSandNavigator) {
            switchNavigator(true);
        }
        if (!inSand && this.isSandNavigator) {
            switchNavigator(false);
        }
        if (level().isClientSide) {
            tail_buffer.calculateChainSwingBuffer(90, 20, 5F, this);
        }

        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean up() {
        return (entityData.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    public boolean dismountIAF() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (entityData.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        setStateField(0, up);
    }

    @Override
    public void down(boolean down) {

    }

    @Override
    public void dismount(boolean dismount) {
        setStateField(1, dismount);
    }

    @Override
    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {

    }

    public boolean isSandBelow() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY() + 1);
        int k = Mth.floor(this.getZ());
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState BlockState = this.level().getBlockState(blockpos);
        return BlockState.is(BlockTags.SAND);
    }

    public boolean isInSand() {
        return this.getControllingPassenger() == null && isInSandStrict();
    }

    public boolean isInSandStrict() {
        return level().getBlockState(blockPosition()).is(BlockTags.SAND);
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
        return new Animation[]{ANIMATION_BITE};
    }

    public Entity[] getWormParts() {
        return segments;
    }

    @Override
    public int getMaxHeadYRot() {
        return 10;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean canPassThrough(BlockPos pos, BlockState state, VoxelShape shape) {
        return level().getBlockState(pos).is(BlockTags.SAND);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isRidingPlayer(Player player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUUID().equals(player.getUUID());
    }

    @Override
    @Nullable
    public Player getRidingPlayer() {
        if (this.getControllingPassenger() instanceof Player) {
            return (Player) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getRideSpeedModifier() {
        return isInSand() ? 1.5F : 1F;
    }

    public double processRiderY(double y) {
        return this.isInSand() ? y + 0.2F : y;
    }

    public class SandMoveHelper extends MoveControl {
        private final EntityDeathWorm worm = EntityDeathWorm.this;

        public SandMoveHelper() {
            super(EntityDeathWorm.this);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                double d1 = this.wantedY - this.worm.getY();
                double d2 = this.wantedZ - this.worm.getZ();
                Vec3 Vector3d = new Vec3(this.wantedX - worm.getX(), this.wantedY - worm.getY(), this.wantedZ - worm.getZ());
                double d0 = Vector3d.length();
                if (d0 < (double) 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                } else {
                    this.speedModifier = 1.0F;
                    worm.setDeltaMovement(worm.getDeltaMovement().add(Vector3d.scale(this.speedModifier * 0.05D / d0)));
                    Vec3 Vector3d1 = worm.getDeltaMovement();
                    worm.setYRot(-((float) Mth.atan2(Vector3d1.x, Vector3d1.z)) * (180F / (float) Math.PI));
                }

            }
        }
    }
}
