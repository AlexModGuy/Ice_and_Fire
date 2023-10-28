package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public abstract class EntityMyrmexBase extends Animal implements IAnimatedEntity, Merchant, ICustomSizeNavigator, IPassabilityNavigator, IHasCustomizableAttributes {

    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> GROWTH_STAGE = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> VARIANT = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation TEXTURE_DESERT_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_larva.png");
    private static final ResourceLocation TEXTURE_DESERT_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_pupa.png");
    private static final ResourceLocation TEXTURE_JUNGLE_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_larva.png");
    private static final ResourceLocation TEXTURE_JUNGLE_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_pupa.png");
    private final SimpleContainer villagerInventory = new SimpleContainer(8);
    public boolean isEnteringHive = false;
    public boolean isBeingGuarded = false;
    protected int growthTicks = 1;
    @Nullable
    protected MerchantOffers offers;
    private int waitTicks = 0;
    private int animationTick;
    private Animation currentAnimation;
    private MyrmexHive hive;
    private int timeUntilReset;
    private boolean leveledUp;
    @Nullable
    private Player customer;


    public EntityMyrmexBase(EntityType<? extends EntityMyrmexBase> t, Level worldIn) {
        super(t, worldIn);
        this.navigation = createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    private static boolean isJungleBiome(Level world, BlockPos position) {
        return BiomeConfig.test(BiomeConfig.jungleMyrmexBiomes, world.getBiome(position));
    }

    public static boolean haveSameHive(EntityMyrmexBase myrmex, Entity entity) {
        if (entity instanceof EntityMyrmexBase) {
            if (myrmex.getHive() != null && ((EntityMyrmexBase) entity).getHive() != null) {
                if (myrmex.isJungle() == ((EntityMyrmexBase) entity).isJungle()) {
                    return myrmex.getHive().getCenter() == ((EntityMyrmexBase) entity).getHive().getCenter();
                }
            }

        }
        if (entity instanceof EntityMyrmexEgg) {
            return myrmex.isJungle() == ((EntityMyrmexEgg) entity).isJungle();
        }
        return false;
    }

    public static boolean isEdibleBlock(BlockState blockState) {
        return blockState.is(BlockTags.create(IafTagRegistry.MYRMEX_HARVESTABLES));
    }

    public static int getRandomCaste(Level world, RandomSource random, boolean royal) {
        float rand = random.nextFloat();
        if (royal) {
            if (rand > 0.9) {
                return 2;//royal
            } else if (rand > 0.75) {
                return 3;//sentinel
            } else if (rand > 0.5) {
                return 1;//soldier
            } else {
                return 0;//worker
            }
        } else {
            if (rand > 0.8) {
                return 3;//sentinel
            } else if (rand > 0.6) {
                return 1;//soldier
            } else {
                return 0;//worker
            }
        }
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean canMove() {
        return this.getGrowthStage() > 1;
    }

    @Override
    public boolean isBaby() {
        return this.getGrowthStage() < 2;
    }

    @Override
    protected void customServerAiStep() {
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.level().broadcastEntityEvent(this, (byte) 14);
            this.getHive().setWorld(this.level());
        }
        super.customServerAiStep();
    }

    @Override
    public int getExperienceReward() {
        return (this.getCasteImportance() * 7) + this.level().random.nextInt(3);
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float i) {
        if (dmg == this.level().damageSources().inWall() && this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getGrowthStage() < 2) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }
        return super.hurt(dmg, i);
    }

    @Override
    protected float getJumpPower() {
        return 0.52F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        return this.level().getBlockState(pos.below()).getBlock() instanceof BlockMyrmexResin ? 10.0F : super.getWalkTargetValue(pos);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, getBbWidth(), getBbHeight());
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level(), type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte) 0);
        this.entityData.define(GROWTH_STAGE, 2);
        this.entityData.define(VARIANT, Boolean.FALSE);
    }

    @Override
    public void tick() {
        super.tick();
        this.setMaxUpStep(1);
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        if (this.getGrowthStage() < 2 && this.getVehicle() != null && this.getVehicle() instanceof EntityMyrmexBase) {
            float yaw = this.getVehicle().getYRot();
            this.setYRot(yaw);
            this.yHeadRot = yaw;
            this.yBodyRot = 0;
            this.yBodyRotO = 0;
        }
        if (!this.level().isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision && (this.onGround() || !this.verticalCollision));
        }
        if (this.getGrowthStage() < 2) {
            growthTicks++;
            if (growthTicks == IafConfig.myrmexLarvaTicks) {
                this.setGrowthStage(this.getGrowthStage() + 1);
                growthTicks = 0;
            }
        }
        if (!this.level().isClientSide && this.getGrowthStage() < 2 && this.getRandom().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }

        if (this.getTarget() != null && !(this.getTarget() instanceof Player) && this.getNavigation().isDone()) {
            this.setTarget(null);
        }
        if (this.getTarget() != null && (haveSameHive(this, this.getTarget()) ||
            this.getTarget() instanceof TamableAnimal && !canAttackTamable((TamableAnimal) this.getTarget()) ||
            this.getTarget() instanceof Player && this.getHive() != null && !this.getHive().isPlayerReputationLowEnoughToFight(this.getTarget().getUUID()))) {
            this.setTarget(null);
        }
        if (this.getWaitTicks() > 0) {
            this.setWaitTicks(this.getWaitTicks() - 1);
        }
        if (this.getHealth() < this.getMaxHealth() && this.tickCount % 500 == 0 && this.isOnResin()) {
            this.heal(1);
            this.level().broadcastEntityEvent(this, (byte) 76);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GrowthStage", this.getGrowthStage());
        tag.putInt("GrowthTicks", growthTicks);
        tag.putBoolean("Variant", this.isJungle());
        if (this.getHive() != null) {
            tag.putUUID("HiveUUID", this.getHive().hiveUUID);
        }
        MerchantOffers merchantoffers = this.getOffers();
        if (!merchantoffers.isEmpty()) {
            tag.put("Offers", merchantoffers.createTag());
        }

        ListTag listnbt = new ListTag();

        for (int i = 0; i < this.villagerInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.villagerInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundTag()));
            }
        }
        tag.put("Inventory", listnbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGrowthStage(tag.getInt("GrowthStage"));
        this.growthTicks = tag.getInt("GrowthTicks");
        this.setJungleVariant(tag.getBoolean("Variant"));
        if (tag.hasUUID("HiveUUID")) {
            this.setHive(MyrmexWorldData.get(level()).getHiveFromUUID(tag.getUUID("HiveUUID")));
        }
        if (tag.contains("Offers", 10)) {
            this.offers = new MerchantOffers(tag.getCompound("Offers"));
        }

        ListTag listnbt = tag.getList("Inventory", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.villagerInventory.addItem(itemstack);
            }
        }
        this.setConfigurableAttributes();

    }


    public boolean canAttackTamable(TamableAnimal tameable) {
        if (tameable.getOwner() != null && this.getHive() != null) {
            return this.getHive().isPlayerReputationLowEnoughToFight(tameable.getOwnerUUID());
        }
        return true;
    }

    public BlockPos getPos() {
        return this.blockPosition();
    }

    public int getGrowthStage() {
        return this.entityData.get(GROWTH_STAGE).intValue();
    }

    public void setGrowthStage(int stage) {
        this.entityData.set(GROWTH_STAGE, stage);
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public void setWaitTicks(int waitTicks) {
        this.waitTicks = waitTicks;
    }

    public boolean isJungle() {
        return this.entityData.get(VARIANT).booleanValue();
    }

    public void setJungleVariant(boolean isJungle) {
        this.entityData.set(VARIANT, isJungle);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.entityData.get(CLIMBING).byteValue();

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(CLIMBING, b0);
    }

    @Override
    public boolean onClimbable() {
        if (this.getNavigation() instanceof AdvancedPathNavigate) {
            //Make sure the entity can only climb when it's on or below the path. This prevents the entity from getting stuck
            if (((AdvancedPathNavigate) this.getNavigation()).entityOnAndBelowPath(this, new Vec3(1.1, 0, 1.1)))
                return true;
        }
        return super.onClimbable();
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
        return new Animation[]{ANIMATION_PUPA_WIGGLE};
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        if (this.getHive() == null || livingBase == null || livingBase instanceof Player && this.getHive().isPlayerReputationLowEnoughToFight(livingBase.getUUID())) {
            super.setLastHurtByMob(livingBase);
        }
        if (this.getHive() != null && livingBase != null) {
            this.getHive().addOrRenewAgressor(livingBase, this.getImportance());
        }
        if (this.getHive() != null && livingBase != null) {
            if (livingBase instanceof Player) {
                int i = -5 * this.getCasteImportance();
                this.getHive().setWorld(this.level());
                this.getHive().modifyPlayerReputation(livingBase.getUUID(), i);
                if (this.isAlive()) {
                    this.level().broadcastEntityEvent(this, (byte) 13);
                }
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        if (this.getHive() != null) {
            Entity entity = cause.getEntity();
            if (entity != null) {
                this.getHive().setWorld(this.level());
                this.getHive().modifyPlayerReputation(entity.getUUID(), -15);
            }
        }
        this.resetCustomer();
        super.die(cause);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!shouldHaveNormalAI()) {
            return InteractionResult.PASS;
        }
        boolean flag2 = itemstack.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get() || itemstack.getItem() == IafItemRegistry.MYRMEX_DESERT_STAFF.get();

        if (flag2) {
            this.onStaffInteract(player, itemstack);
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }
        boolean flag = itemstack.getItem() == Items.NAME_TAG || itemstack.getItem() == Items.LEAD;
        if (flag) {
            return super.mobInteract(player, hand);
        } else if (this.getGrowthStage() >= 2 && this.isAlive() && !this.isBaby() && !player.isShiftKeyDown()) {
            if (this.getOffers().isEmpty()) {
                return super.mobInteract(player, hand);
            } else {
                if (!this.level().isClientSide && (this.getTarget() == null || !this.getTarget().equals(player)) && hand == InteractionHand.MAIN_HAND) {
                    if (this.getHive() != null && !this.getHive().isPlayerReputationTooLowToTrade(player.getUUID())) {
                        this.setTradingPlayer(player);
                        this.openTradingScreen(player, this.getDisplayName(), 1);
                        return InteractionResult.SUCCESS;
                    }
                }

                return InteractionResult.PASS;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void onStaffInteract(Player player, ItemStack itemstack) {
        if (itemstack.getTag() == null) {
            return;
        }
        UUID staffUUID = itemstack.getTag().hasUUID("HiveUUID") ? itemstack.getTag().getUUID("HiveUUID") : null;
        if (level().isClientSide) {
            return;
        }
        if (!player.isCreative()) {
            if ((this.getHive() != null && !this.getHive().canPlayerCommandHive(player.getUUID()))) {
                return;
            }
        }
        if (this.getHive() == null) {
            player.displayClientMessage(Component.translatable("myrmex.message.null_hive"), true);

        } else {
            if (staffUUID != null && staffUUID.equals(this.getHive().hiveUUID)) {
                player.displayClientMessage(Component.translatable("myrmex.message.staff_already_set"), true);
            } else {
                this.getHive().setWorld(this.level());
                EntityMyrmexQueen queen = this.getHive().getQueen();
                BlockPos center = this.getHive().getCenterGround();
                if (queen != null && queen.hasCustomName()) {
                    player.displayClientMessage(Component.translatable("myrmex.message.staff_set_named", queen.getName(), center.getX(), center.getY(), center.getZ()), true);
                } else {
                    player.displayClientMessage(Component.translatable("myrmex.message.staff_set_unnamed", center.getX(), center.getY(), center.getZ()), true);
                }
                itemstack.getTag().putUUID("HiveUUID", this.getHive().hiveUUID);
            }

        }

    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHive(MyrmexWorldData.get(level()).getNearestHive(this.blockPosition(), 400));
        if (this.getHive() != null) {
            this.setJungleVariant(isJungleBiome(level(), this.getHive().getCenter()));
        } else {
            this.setJungleVariant(random.nextBoolean());
        }
        return spawnDataIn;
    }

    public abstract boolean shouldLeaveHive();

    public abstract boolean shouldEnterHive();

    @Override
    public float getScale() {
        return this.getGrowthStage() == 0 ? 0.5F : this.getGrowthStage() == 1 ? 0.75F : 1F;
    }

    public abstract ResourceLocation getAdultTexture();

    public abstract float getModelScale();

    public ResourceLocation getTexture() {
        if (this.getGrowthStage() == 0) {
            return isJungle() ? TEXTURE_JUNGLE_LARVA : TEXTURE_DESERT_LARVA;
        } else if (this.getGrowthStage() == 1) {
            return isJungle() ? TEXTURE_JUNGLE_PUPA : TEXTURE_DESERT_PUPA;
        } else {
            return getAdultTexture();
        }
    }

    public MyrmexHive getHive() {
        return hive;
    }

    public void setHive(MyrmexHive newHive) {
        hive = newHive;
        if (hive != null) {
            hive.addMyrmex(this);
        }
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (!haveSameHive(this, entityIn)) {
            entityIn.push(this);
        }
    }

    public boolean canSeeSky() {
        return level().canSeeSkyFromBelowWater(this.blockPosition());
    }

    public boolean isOnResin() {
        int d0 = this.getBlockY() - 1;
        BlockPos blockpos = new BlockPos(this.getBlockX(), d0, this.getBlockZ());
        while (level().isEmptyBlock(blockpos) && blockpos.getY() > 1) {
            blockpos = blockpos.below();
        }
        BlockState BlockState = this.level().getBlockState(blockpos);
        return BlockState.getBlock() instanceof BlockMyrmexResin || BlockState.getBlock() instanceof BlockMyrmexConnectedResin;
    }

    public boolean isInNursery() {
        if (getHive() != null && getHive().getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition()) != null) {
            return false;
        }
        if (getHive() != null) {
            BlockPos nursery = getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition());
            return Math.sqrt(this.distanceToSqr(nursery.getX(), nursery.getY(), nursery.getZ())) < 45;
        }
        return false;
    }

    public boolean isInHive() {
        if (getHive() != null) {
            for (BlockPos pos : getHive().getAllRooms()) {
                if (isCloseEnoughToTarget(MyrmexHive.getGroundedPos(level(), pos), 50))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 motion) {
        if (!this.canMove()) {
            super.travel(Vec3.ZERO);
            return;
        }
        super.travel(motion);
    }

    public int getImportance() {
        if (this.getGrowthStage() < 2) {
            return 1;
        }
        return getCasteImportance();
    }

    public abstract int getCasteImportance();

    public boolean needsGaurding() {
        return true;
    }

    public boolean shouldMoveThroughHive() {
        return true;
    }

    public boolean shouldWander() {
        return this.getHive() == null;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 76) {
            this.playVillagerEffect();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.MYRMEX_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.MYRMEX_DIE;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(IafSoundRegistry.MYRMEX_WALK, 0.16F * this.getMyrmexPitch() * (this.getRandom().nextFloat() * 0.6F + 0.4F), 1.0F);
    }

    protected void playBiteSound() {
        this.playSound(IafSoundRegistry.MYRMEX_BITE, this.getMyrmexPitch(), 1.0F);
    }

    protected void playStingSound() {
        this.playSound(IafSoundRegistry.MYRMEX_STING, this.getMyrmexPitch(), 0.6F);
    }

    protected void playVillagerEffect() {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
        }
    }

    public float getMyrmexPitch() {
        return getBbWidth();
    }

    public boolean shouldHaveNormalAI() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public AABB getAttackBounds() {
        float size = this.getScale() * 0.65F;
        return this.getBoundingBox().inflate(1.0F + size, 1.0F + size, 1.0F + size);
    }

    @Override
    @Nullable
    public Player getTradingPlayer() {
        return this.customer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.customer = player;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.populateTradeData();
        }

        return this.offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers offers) {
    }

    @Override
    public void overrideXp(int xpIn) {
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.onVillagerTrade(offer);
    }

    protected void onVillagerTrade(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.getHive().setWorld(this.level());
            this.getHive().modifyPlayerReputation(this.getTradingPlayer().getUUID(), 1);
        }
    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack stack) {
        if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getVillagerYesNoSound(!stack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }

    }

    @Override
    public @NotNull SoundEvent getNotifyTradeSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    protected SoundEvent getVillagerYesNoSound(boolean getYesSound) {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    public void playCelebrateSound() {
    }

    protected void resetCustomer() {
        this.setTradingPlayer(null);
    }

    @Override
    @Nullable
    public Entity changeDimension(@NotNull ServerLevel server, net.minecraftforge.common.util.@NotNull ITeleporter teleporter) {
        this.resetCustomer();
        return super.changeDimension(server, teleporter);
    }

    public SimpleContainer getVillagerInventory() {
        return this.villagerInventory;
    }


    @Override
    public @NotNull ItemStack equipItemIfPossible(@NotNull ItemStack stack) {
        ItemStack superStack = super.equipItemIfPossible(stack);
        if (ItemStack.isSameItem(superStack, stack) && ItemStack.matches(superStack, stack)) {
            return stack;
        } else {
            EquipmentSlot inventorySlot = stack.getEquipmentSlot();
            int i = inventorySlot.getIndex() - 300;
            if (i >= 0 && i < this.villagerInventory.getContainerSize()) {
                this.villagerInventory.setItem(i, stack);
                return stack;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    protected void addTrades(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (newTrades.length > maxNumbers) {
            while (set.size() < maxNumbers) {
                set.add(this.random.nextInt(newTrades.length));
            }
        } else {
            for (int i = 0; i < newTrades.length; ++i) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            VillagerTrades.ItemListing villagertrades$itrade = newTrades[integer];
            MerchantOffer merchantoffer = villagertrades$itrade.getOffer(this, this.random);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
            }
        }

    }

    private void levelUp() {
        this.populateTradeData();
    }

    protected abstract VillagerTrades.ItemListing[] getLevel1Trades();

    protected abstract VillagerTrades.ItemListing[] getLevel2Trades();

    protected void populateTradeData() {
        VillagerTrades.ItemListing[] level1 = getLevel1Trades();
        VillagerTrades.ItemListing[] level2 = getLevel2Trades();
        if (level1 != null && level2 != null) {
            MerchantOffers merchantoffers = this.getOffers();
            this.addTrades(merchantoffers, level1, 5);
            int i = this.random.nextInt(level2.length);
            int j = this.random.nextInt(level2.length);
            int k = this.random.nextInt(level2.length);
            int rolls = 0;
            while ((j == i) && rolls < 100) {
                j = this.random.nextInt(level2.length);
                rolls++;
            }
            rolls = 0;
            while ((k == i || k == j) && rolls < 100) {
                k = this.random.nextInt(level2.length);
                rolls++;
            }
            VillagerTrades.ItemListing rareTrade1 = level2[i];
            VillagerTrades.ItemListing rareTrade2 = level2[j];
            VillagerTrades.ItemListing rareTrade3 = level2[k];
            MerchantOffer merchantoffer1 = rareTrade1.getOffer(this, this.random);
            if (merchantoffer1 != null) {
                merchantoffers.add(merchantoffer1);
            }
            MerchantOffer merchantoffer2 = rareTrade2.getOffer(this, this.random);
            if (merchantoffer2 != null) {
                merchantoffers.add(merchantoffer2);
            }
            MerchantOffer merchantoffer3 = rareTrade3.getOffer(this, this.random);
            if (merchantoffer3 != null) {
                merchantoffers.add(merchantoffer3);
            }
        }
    }

    public boolean isCloseEnoughToTarget(BlockPos target, double distanceSquared) {
        if (target != null) {
            return this.distanceToSqr(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D) <= distanceSquared;
        }
        return false;
    }

    //if the path created couldn't reach the destination or if the entity isn't close enough to the targetBlock
    public boolean pathReachesTarget(PathResult path, BlockPos target, double distanceSquared) {
        return !path.failedToReachDestination()
            && (this.isCloseEnoughToTarget(target, distanceSquared) || this.getNavigation().getPath() == null || !this.getNavigation().getPath().isDone());
    }

    @Override
    public boolean isSmallerThanBlock() {
        return false;
    }

    @Override
    public float getXZNavSize() {
        return getBbWidth() / 2;
    }

    @Override
    public int getYNavSize() {
        return (int) getBbHeight() / 2;
    }

    @Override
    public int maxSearchNodes() {
        return IafConfig.maxDragonPathingNodes;
    }

    @Override
    public boolean isBlockExplicitlyPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    @Override
    public boolean isBlockExplicitlyNotPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return state.getBlock() instanceof LeavesBlock;
    }
}
