package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityPixie extends TameableEntity {

    public static final float[][] PARTICLE_RGB = new float[][]{new float[]{1F, 0.752F, 0.792F}, new float[]{0.831F, 0.662F, 1F}, new float[]{0.513F, 0.843F, 1F}, new float[]{0.654F, 0.909F, 0.615F}, new float[]{0.996F, 0.788F, 0.407F}};
    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EntityPixie.class, DataSerializers.INT);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.defineId(EntityPixie.class, DataSerializers.INT);

    public static final int STEAL_COOLDOWN = 3000;

    public Effect[] positivePotions = new Effect[]{Effects.DAMAGE_BOOST, Effects.JUMP, Effects.MOVEMENT_SPEED, Effects.LUCK, Effects.DIG_SPEED};
    public Effect[] negativePotions = new Effect[]{Effects.WEAKNESS, Effects.CONFUSION, Effects.MOVEMENT_SLOWDOWN, Effects.UNLUCK, Effects.DIG_SLOWDOWN};
    public boolean slowSpeed = false;
    public int ticksUntilHouseAI;
    public int ticksHeldItemFor;
    private BlockPos housePos;
    public int stealCooldown = 0;
    private boolean isSitting;

    public EntityPixie(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new EntityPixie.AIMoveControl(this);
        this.xpReward = 3;
        this.setDropChance(EquipmentSlotType.MAINHAND, 0F);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.getY(), z);
        for (int yDown = 0; yDown < 3; yDown++) {
            if (!world.isEmptyBlock(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static BlockPos findAHouse(Entity entity, World world) {
        for (int xSearch = -10; xSearch < 10; xSearch++) {
            for (int ySearch = -10; ySearch < 10; ySearch++) {
                for (int zSearch = -10; zSearch < 10; zSearch++) {
                    if (world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) != null && world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) instanceof TileEntityPixieHouse) {
                        TileEntityPixieHouse house = (TileEntityPixieHouse) world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch));
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
        if (level.isClientSide) {
            boolean isSitting = (this.entityData.get(DATA_FLAGS_ID).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            this.setOrderedToSit(isSitting);
            return isSitting;
        }
        return this.isSitting;
    }

    public void setPixieSitting(boolean sitting) {
        if (!level.isClientSide) {
            this.isSitting = sitting;
            this.setInSittingPose(sitting);
        }
        byte b0 = this.entityData.get(DATA_FLAGS_ID).byteValue();
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    public boolean isOrderedToSit() {
        return this.isPixieSitting();
    }

    protected int getExperienceReward(PlayerEntity player) {
        return 3;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide && this.getRandom().nextInt(3) == 0 && !this.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
            this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0);
            this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            this.stealCooldown = STEAL_COOLDOWN;
            return true;
        }
        if (this.isOwnerClose() && (source == DamageSource.FALLING_BLOCK || source == DamageSource.IN_WALL || this.getOwner() != null && source.getEntity() == this.getOwner())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean invulnerable = super.isInvulnerableTo(source);
        if (!invulnerable) {
            Entity owner = this.getOwner();
            if (owner != null && source.getEntity() == owner) {
                return true;
            }
        }
        return invulnerable;
    }

    public void die(DamageSource cause) {
        if (!this.level.isClientSide && !this.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
            this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0);
            this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.die(cause);
        //if (cause.getTrueSource() instanceof PlayerEntity) {
        //	((PlayerEntity) cause.getTrueSource()).addStat(ModAchievements.killPixie);
        //}
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, Integer.valueOf(0));
        this.entityData.define(COMMAND, Integer.valueOf(0));
    }

    protected void doPush(Entity entityIn) {
        if (this.getOwner() != entityIn) {
            entityIn.push(this);
        }
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (this.isOwnedBy(player)) {

            if (player.getItemInHand(hand).getItem() == Items.SUGAR && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                player.getItemInHand(hand).shrink(1);
                this.playSound(IafSoundRegistry.PIXIE_TAUNT, 1F, 1F);
                return ActionResultType.SUCCESS;
            } else {

                // make pixie sit via a check in livingTick() like Hippogryphs work
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 1) {
                    this.setCommand(0);
                }

                return ActionResultType.SUCCESS;
            }
        } else if (player.getItemInHand(hand).getItem() == IafBlockRegistry.JAR_EMPTY.asItem() && !this.isTame()) {
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            Block jar = IafBlockRegistry.JAR_PIXIE_0;
            switch (this.getColor()) {
                case 0:
                    jar = IafBlockRegistry.JAR_PIXIE_0;
                    break;
                case 1:
                    jar = IafBlockRegistry.JAR_PIXIE_1;
                    break;
                case 2:
                    jar = IafBlockRegistry.JAR_PIXIE_2;
                    break;
                case 3:
                    jar = IafBlockRegistry.JAR_PIXIE_3;
                    break;
                case 4:
                    jar = IafBlockRegistry.JAR_PIXIE_4;
                    break;
            }
            ItemStack stack = new ItemStack(jar, 1);
            if (!level.isClientSide) {
                if (!this.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
                    this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0.0F);
                    this.stealCooldown = STEAL_COOLDOWN;
                }

                this.spawnAtLocation(stack, 0.0F);
            }
            //player.addStat(ModAchievements.jarPixie);
            this.remove();
        }
        return super.mobInteract(player, hand);
    }

    public void flipAI(boolean flee) {
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PixieAIFollowOwner(this, 1.0D, 2.0F, 4.0F));
        this.goalSelector.addGoal(2, new PixieAIPickupItem(this, false));
        this.goalSelector.addGoal(2, new PixieAIFlee(this, PlayerEntity.class, 10, new Predicate<PlayerEntity>() {
            @Override
            public boolean apply(@Nullable PlayerEntity entity) {
                return true;
            }
        }));
        this.goalSelector.addGoal(2, new PixieAISteal(this, 1.0D));
        this.goalSelector.addGoal(3, new PixieAIMoveRandom(this));
        this.goalSelector.addGoal(4, new PixieAIEnterHouse(this));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.random.nextInt(5));
        this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        if (dataTag != null) {
            System.out.println("EntityPixie spawned with dataTag: " + dataTag);
        }

        return spawnDataIn;
    }

    private boolean isBeyondHeight() {
        if (this.getY() > this.level.getMaxBuildHeight()) {
            return true;
        }
        BlockPos height = this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.blockPosition());
        int maxY = 20 + height.getY();
        return this.getY() > maxY;
    }

    public int getCommand() {
        return Integer.valueOf(this.entityData.get(COMMAND).intValue());
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, Integer.valueOf(command));
        this.setPixieSitting(command == 1);
    }


    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {

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
        } else {
        }
        if (level.isClientSide) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(), PARTICLE_RGB[this.getColor()][0], PARTICLE_RGB[this.getColor()][1], PARTICLE_RGB[this.getColor()][2]);
        }
        if (ticksUntilHouseAI > 0) {
            ticksUntilHouseAI--;
        }
        if (!level.isClientSide) {
            if (housePos != null && this.distanceToSqr(Vector3d.atCenterOf(housePos)) < 1.5F && level.getBlockEntity(housePos) != null && level.getBlockEntity(housePos) instanceof TileEntityPixieHouse) {
                TileEntityPixieHouse house = (TileEntityPixieHouse) level.getBlockEntity(housePos);
                if (house.hasPixie) {
                    this.housePos = null;
                } else {
                    house.hasPixie = true;
                    house.pixieType = this.getColor();
                    house.pixieItems.set(0, this.getItemInHand(Hand.MAIN_HAND));
                    house.tamedPixie = this.isTame();
                    house.pixieOwnerUUID = this.getOwnerUUID();
                    IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(housePos.asLong(), true, this.getColor()));
                    this.remove();
                }
            }
        }
        if (this.getOwner() != null && this.isOwnerClose() && this.tickCount % 80 == 0) {
            this.getOwner().addEffect(new EffectInstance(positivePotions[this.getColor()], 100, 0, false, false));
        }
        //PlayerEntity player = world.getClosestPlayerToEntity(this, 25);
        //if (player != null) {
        //	player.addStat(ModAchievements.findPixie);
        //}
    }

    public int getColor() {
        return MathHelper.clamp(this.getEntityData().get(COLOR).intValue(), 0, 4);
    }

    public void setColor(int color) {
        this.getEntityData().set(COLOR, color);
    }


    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.setColor(compound.getInt("Color"));

        this.stealCooldown = compound.getInt("StealCooldown");
        this.ticksHeldItemFor = compound.getInt("HoldingTicks");

        this.setPixieSitting(compound.getBoolean("PixieSitting"));
        this.setCommand(compound.getInt("Command"));

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Color", this.getColor());
        compound.putInt("Command", this.getCommand());
        compound.putInt("StealCooldown", this.stealCooldown);
        compound.putInt("HoldingTicks", this.ticksHeldItemFor);
        compound.putBoolean("PixieSitting", this.isPixieSitting());
        super.addAdditionalSaveData(compound);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
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

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.PIXIE_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return IafSoundRegistry.PIXIE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.PIXIE_DIE;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TameableEntity) {
                return ((TameableEntity) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }
    
    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
        }

        public void tick() {
            float speedMod = 1;
            if (EntityPixie.this.slowSpeed) {
                speedMod = 2F;
            }
            if (this.operation == MovementController.Action.MOVE_TO) {
                if (EntityPixie.this.horizontalCollision) {
                    EntityPixie.this.yRot += 180.0F;
                    speedMod = 0.1F;
                    BlockPos target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.level, EntityPixie.this.getX() + EntityPixie.this.random.nextInt(15) - 7, EntityPixie.this.getZ() + EntityPixie.this.random.nextInt(15) - 7, EntityPixie.this.random);
                    this.wantedX = target.getX();
                    this.wantedY = target.getY();
                    this.wantedZ = target.getZ();
                }
                double d0 = this.wantedX - EntityPixie.this.getX();
                double d1 = this.wantedY - EntityPixie.this.getY();
                double d2 = this.wantedZ - EntityPixie.this.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityPixie.this.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    EntityPixie.this.setDeltaMovement(EntityPixie.this.getDeltaMovement().multiply(0.5D, 0.5D, 0.5D));
                } else {
                    EntityPixie.this.setDeltaMovement(EntityPixie.this.getDeltaMovement().add(d0 / d3 * 0.05D * this.speedModifier * speedMod, d1 / d3 * 0.05D * this.speedModifier * speedMod, d2 / d3 * 0.05D * this.speedModifier * speedMod));

                    if (EntityPixie.this.getTarget() == null) {
                        EntityPixie.this.yRot = -((float) MathHelper.atan2(EntityPixie.this.getDeltaMovement().x, EntityPixie.this.getDeltaMovement().z)) * (180F / (float) Math.PI);
                        EntityPixie.this.yBodyRot = EntityPixie.this.yRot;
                    } else {
                        double d4 = EntityPixie.this.getTarget().getX() - EntityPixie.this.getX();
                        double d5 = EntityPixie.this.getTarget().getZ() - EntityPixie.this.getZ();
                        EntityPixie.this.yRot = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityPixie.this.yBodyRot = EntityPixie.this.yRot;
                    }
                }
            }
        }
    }


}