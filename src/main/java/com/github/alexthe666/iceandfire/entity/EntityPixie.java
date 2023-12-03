package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityPixie extends TamableAnimal {

    public static final float[][] PARTICLE_RGB = new float[][]{new float[]{1F, 0.752F, 0.792F}, new float[]{0.831F, 0.662F, 1F}, new float[]{0.513F, 0.843F, 1F}, new float[]{0.654F, 0.909F, 0.615F}, new float[]{0.996F, 0.788F, 0.407F}};
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityPixie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityPixie.class, EntityDataSerializers.INT);

    public static final int STEAL_COOLDOWN = 3000;

    public MobEffect[] positivePotions = new MobEffect[]{MobEffects.DAMAGE_BOOST, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.LUCK, MobEffects.DIG_SPEED};
    public MobEffect[] negativePotions = new MobEffect[]{MobEffects.WEAKNESS, MobEffects.CONFUSION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.UNLUCK, MobEffects.DIG_SLOWDOWN};
    public boolean slowSpeed = false;
    public int ticksUntilHouseAI;
    public int ticksHeldItemFor;
    private BlockPos housePos;
    public int stealCooldown = 0;
    private boolean isSitting;

    public EntityPixie(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new EntityPixie.AIMoveControl(this);
        this.xpReward = 3;
        this.setDropChance(EquipmentSlot.MAINHAND, 0F);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, double x, double z, RandomSource rand) {
        BlockPos pos = BlockPos.containing(x, entity.getBlockY(), z);
        for (int yDown = 0; yDown < 3; yDown++) {
            if (!world.isEmptyBlock(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static BlockPos findAHouse(Entity entity, Level world) {
        for (int xSearch = -10; xSearch < 10; xSearch++) {
            for (int ySearch = -10; ySearch < 10; ySearch++) {
                for (int zSearch = -10; zSearch < 10; zSearch++) {
                    if (world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) != null && world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) instanceof TileEntityPixieHouse house) {
                        if (!house.hasPixie) {
                            return entity.blockPosition().offset(xSearch, ySearch, zSearch);
                        }
                    }
                }
            }
        }
        return entity.blockPosition();
    }

    public boolean isPixieSitting() {
        if (level().isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            this.setOrderedToSit(isSitting);
            return isSitting;
        }
        return this.isSitting;
    }

    public void setPixieSitting(boolean sitting) {
        if (!level().isClientSide) {
            this.isSitting = sitting;
            this.setInSittingPose(sitting);
        }
        byte b0 = this.entityData.get(DATA_FLAGS_ID).byteValue();
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }
    }

    @Override
    public boolean isOrderedToSit() {
        return this.isPixieSitting();
    }

    @Override
    public int getExperienceReward() {
        return 3;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.level().isClientSide && this.getRandom().nextInt(3) == 0 && !this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.spawnAtLocation(this.getItemInHand(InteractionHand.MAIN_HAND), 0);
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.stealCooldown = STEAL_COOLDOWN;
            return true;
        }
        if (this.isOwnerClose() && ((source.getEntity() != null && source == this.level().damageSources().fallingBlock(source.getEntity())) || source == this.level().damageSources().inWall() || this.getOwner() != null && source.getEntity() == this.getOwner())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        boolean invulnerable = super.isInvulnerableTo(source);
        if (!invulnerable) {
            Entity owner = this.getOwner();
            if (owner != null && source.getEntity() == owner) {
                return true;
            }
        }
        return invulnerable;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        if (!this.level().isClientSide && !this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.spawnAtLocation(this.getItemInHand(InteractionHand.MAIN_HAND), 0);
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.die(cause);
        //if (cause.getTrueSource() instanceof PlayerEntity) {
        //	((PlayerEntity) cause.getTrueSource()).addStat(ModAchievements.killPixie);
        //}
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, 0);
        this.entityData.define(COMMAND, 0);
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (this.getOwner() != entityIn) {
            entityIn.push(this);
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.isOwnedBy(player)) {

            if (player.getItemInHand(hand).is(IafItemTags.HEAL_PIXIE) && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                player.getItemInHand(hand).shrink(1);
                this.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
                return InteractionResult.SUCCESS;
            } else {

                // make pixie sit via a check in livingTick() like Hippogryphs work
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 1) {
                    this.setCommand(0);
                }

                return InteractionResult.SUCCESS;
            }
        } else if (player.getItemInHand(hand).getItem() == IafBlockRegistry.JAR_EMPTY.get().asItem() && !this.isTame()) {
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            Block jar = IafBlockRegistry.JAR_PIXIE_0.get();
            switch (this.getColor()) {
                case 0:
                    jar = IafBlockRegistry.JAR_PIXIE_0.get();
                    break;
                case 1:
                    jar = IafBlockRegistry.JAR_PIXIE_1.get();
                    break;
                case 2:
                    jar = IafBlockRegistry.JAR_PIXIE_2.get();
                    break;
                case 3:
                    jar = IafBlockRegistry.JAR_PIXIE_3.get();
                    break;
                case 4:
                    jar = IafBlockRegistry.JAR_PIXIE_4.get();
                    break;
            }
            ItemStack stack = new ItemStack(jar, 1);
            if (!level().isClientSide) {
                if (!this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    this.spawnAtLocation(this.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
                    this.stealCooldown = STEAL_COOLDOWN;
                }

                this.spawnAtLocation(stack, 0.0F);
            }
            //player.addStat(ModAchievements.jarPixie);
            this.remove(RemovalReason.DISCARDED);
        }
        return super.mobInteract(player, hand);
    }

    public void flipAI(boolean flee) {
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PixieAIFollowOwner(this, 1.0D, 2.0F, 4.0F));
        this.goalSelector.addGoal(2, new PixieAIPickupItem<>(this, false));
        this.goalSelector.addGoal(2, new PixieAIFlee<>(this, Player.class, 10, (Predicate<Player>) entity -> true));
        this.goalSelector.addGoal(2, new PixieAISteal(this, 1.0D));
        this.goalSelector.addGoal(3, new PixieAIMoveRandom(this));
        this.goalSelector.addGoal(4, new PixieAIEnterHouse(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.random.nextInt(5));
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        if (dataTag != null) {
            System.out.println("EntityPixie spawned with dataTag: " + dataTag);
        }

        return spawnDataIn;
    }

    private boolean isBeyondHeight() {
        if (this.getY() > this.level().getMaxBuildHeight()) {
            return true;
        }
        BlockPos height = this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.blockPosition());
        int maxY = 20 + height.getY();
        return this.getY() > maxY;
    }

    public int getCommand() {
        return this.entityData.get(COMMAND).intValue();
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
        this.setPixieSitting(command == 1);
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {

            // NOTE: This code was taken from EntityHippogryph basically same idea
            if (this.isPixieSitting() && this.getCommand() != 1) {
                this.setPixieSitting(false);
            }
            if (!this.isPixieSitting() && this.getCommand() == 1) {
                this.setPixieSitting(true);
            }
            if (this.isPixieSitting()) {
                this.getNavigation().stop();
            }
        }

        if (stealCooldown > 0) {
            stealCooldown--;
        }
        if (!this.getMainHandItem().isEmpty() && !this.isTame()) {
            ticksHeldItemFor++;
        } else {
            ticksHeldItemFor = 0;
        }


        if (!this.isPixieSitting() && !this.isBeyondHeight()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, 0.08, 0));
        }
        if (level().isClientSide) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(), PARTICLE_RGB[this.getColor()][0], PARTICLE_RGB[this.getColor()][1], PARTICLE_RGB[this.getColor()][2]);
        }
        if (ticksUntilHouseAI > 0) {
            ticksUntilHouseAI--;
        }
        if (!level().isClientSide) {
            if (housePos != null && this.distanceToSqr(Vec3.atCenterOf(housePos)) < 1.5F && level().getBlockEntity(housePos) != null && level().getBlockEntity(housePos) instanceof TileEntityPixieHouse house) {
                if (house.hasPixie) {
                    this.housePos = null;
                } else {
                    house.hasPixie = true;
                    house.pixieType = this.getColor();
                    house.pixieItems.set(0, this.getItemInHand(InteractionHand.MAIN_HAND));
                    house.tamedPixie = this.isTame();
                    house.pixieOwnerUUID = this.getOwnerUUID();
                    IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(housePos.asLong(), true, this.getColor()));
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
        if (this.getOwner() != null && this.isOwnerClose() && this.tickCount % 80 == 0) {
            this.getOwner().addEffect(new MobEffectInstance(positivePotions[this.getColor()], 100, 0, false, false));
        }
        //PlayerEntity player = world.getClosestPlayerToEntity(this, 25);
        //if (player != null) {
        //	player.addStat(ModAchievements.findPixie);
        //}
    }

    public int getColor() {
        return Mth.clamp(this.getEntityData().get(COLOR).intValue(), 0, 4);
    }

    public void setColor(int color) {
        this.getEntityData().set(COLOR, color);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setColor(compound.getInt("Color"));

        this.stealCooldown = compound.getInt("StealCooldown");
        this.ticksHeldItemFor = compound.getInt("HoldingTicks");

        this.setPixieSitting(compound.getBoolean("PixieSitting"));
        this.setCommand(compound.getInt("Command"));

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Color", this.getColor());
        compound.putInt("Command", this.getCommand());
        compound.putInt("StealCooldown", this.stealCooldown);
        compound.putInt("HoldingTicks", this.ticksHeldItemFor);
        compound.putBoolean("PixieSitting", this.isPixieSitting());
        super.addAdditionalSaveData(compound);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    public void setHousePosition(BlockPos blockPos) {
        this.housePos = blockPos;
    }

    public BlockPos getHousePos() {
        return housePos;
    }

    public boolean isOwnerClose() {
        return this.isTame() && this.getOwner() != null && this.distanceToSqr(this.getOwner()) < 100;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.PIXIE_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.PIXIE_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.PIXIE_DIE;
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

    class AIMoveControl extends MoveControl {
        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
        }

        @Override
        public void tick() {
            float speedMod = 1;
            if (EntityPixie.this.slowSpeed) {
                speedMod = 2F;
            }
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (EntityPixie.this.horizontalCollision) {
                    EntityPixie.this.setYRot(this.mob.getYRot() + 180.0F);
                    speedMod = 0.1F;
                    BlockPos target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.level(), EntityPixie.this.getX() + EntityPixie.this.random.nextInt(15) - 7, EntityPixie.this.getZ() + EntityPixie.this.random.nextInt(15) - 7, EntityPixie.this.random);
                    this.wantedX = target.getX();
                    this.wantedY = target.getY();
                    this.wantedZ = target.getZ();
                }
                double d0 = this.wantedX - EntityPixie.this.getX();
                double d1 = this.wantedY - EntityPixie.this.getY();
                double d2 = this.wantedZ - EntityPixie.this.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = Math.sqrt(d3);

                if (d3 < EntityPixie.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    EntityPixie.this.setDeltaMovement(EntityPixie.this.getDeltaMovement().multiply(0.5D, 0.5D, 0.5D));
                } else {
                    EntityPixie.this.setDeltaMovement(EntityPixie.this.getDeltaMovement().add(d0 / d3 * 0.05D * this.speedModifier * speedMod, d1 / d3 * 0.05D * this.speedModifier * speedMod, d2 / d3 * 0.05D * this.speedModifier * speedMod));

                    if (EntityPixie.this.getTarget() == null) {
                        EntityPixie.this.setYRot(-((float) Mth.atan2(EntityPixie.this.getDeltaMovement().x, EntityPixie.this.getDeltaMovement().z)) * (180F / (float) Math.PI));
                        EntityPixie.this.yBodyRot = EntityPixie.this.getYRot();
                    } else {
                        double d4 = EntityPixie.this.getTarget().getX() - EntityPixie.this.getX();
                        double d5 = EntityPixie.this.getTarget().getZ() - EntityPixie.this.getZ();
                        EntityPixie.this.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
                        EntityPixie.this.yBodyRot = EntityPixie.this.getYRot();
                    }
                }
            }
        }
    }


}