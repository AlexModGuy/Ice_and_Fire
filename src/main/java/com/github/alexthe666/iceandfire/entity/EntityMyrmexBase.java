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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public abstract class EntityMyrmexBase extends AnimalEntity implements IAnimatedEntity, IMerchant, ICustomSizeNavigator, IPassabilityNavigator, IHasCustomizableAttributes {

    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.defineId(EntityMyrmexBase.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> GROWTH_STAGE = EntityDataManager.defineId(EntityMyrmexBase.class, DataSerializers.INT);
    private static final DataParameter<Boolean> VARIANT = EntityDataManager.defineId(EntityMyrmexBase.class, DataSerializers.BOOLEAN);
    private static final ResourceLocation TEXTURE_DESERT_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_larva.png");
    private static final ResourceLocation TEXTURE_DESERT_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_pupa.png");
    private static final ResourceLocation TEXTURE_JUNGLE_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_larva.png");
    private static final ResourceLocation TEXTURE_JUNGLE_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_pupa.png");
    private final Inventory villagerInventory = new Inventory(8);
    public boolean isEnteringHive = false;
    public boolean isBeingGuarded = false;
    protected int growthTicks = 1;
    private int waitTicks = 0;
    @Nullable
    protected MerchantOffers offers;
    private int animationTick;
    private Animation currentAnimation;
    private MyrmexHive hive;
    private int timeUntilReset;
    private boolean leveledUp;
    @Nullable
    private PlayerEntity customer;


    public EntityMyrmexBase(EntityType<? extends EntityMyrmexBase> t, World worldIn) {
        super(t, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(t, this);
        this.maxUpStep = 1;
        this.flyingSpeed = 0.2f;
        this.navigation = createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
        //this.moveController = new GroundMoveHelper(this);
    }
    private static boolean isJungleBiome(World world, BlockPos position) {
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
        return BlockTags.getAllTags().getTag(IafTagRegistry.MYRMEX_HARVESTABLES).contains(blockState.getBlock());
    }

    public static int getRandomCaste(World world, Random random, boolean royal) {
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

    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
    }

    public boolean canMove() {
        return this.getGrowthStage() > 1;
    }

    public boolean isBaby() {
        return this.getGrowthStage() < 2;
    }

    protected void customServerAiStep() {
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }
                this.addEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.level.broadcastEntityEvent(this, (byte) 14);
            this.getHive().setWorld(this.level);
        }
        super.customServerAiStep();
    }

    protected int getExperienceReward(PlayerEntity player) {
        return (this.getCasteImportance() * 7) + this.level.random.nextInt(3);
    }

    @Override
    public boolean hurt(DamageSource dmg, float i) {
        if (dmg == DamageSource.IN_WALL && this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getGrowthStage() < 2) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }
        return super.hurt(dmg, i);
    }

    protected float getJumpPower() {
        return 0.52F;
    }

    public float getWalkTargetValue(BlockPos pos) {
        return this.level.getBlockState(pos.below()).getBlock() instanceof BlockMyrmexResin ? 10.0F : super.getWalkTargetValue(pos);
    }

    protected PathNavigator createNavigation(World worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, 1, 1);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level, type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, Byte.valueOf((byte) 0));
        this.entityData.define(GROWTH_STAGE, Integer.valueOf(2));
        this.entityData.define(VARIANT, Boolean.valueOf(false));
    }

    public void tick() {
        super.tick();
        this.maxUpStep = 1;
        if (level.getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof PlayerEntity) {
            this.setTarget(null);
        }
        if (this.getGrowthStage() < 2 && this.getVehicle() != null && this.getVehicle() instanceof EntityMyrmexBase) {
            float yaw = this.getVehicle().yRot;
            this.yRot = yaw;
            this.yHeadRot = yaw;
            this.yBodyRot = 0;
            this.yBodyRotO = 0;
        }
        if (!this.level.isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision && (this.isOnGround() || !this.verticalCollision));
        }
        if (this.getGrowthStage() < 2) {
            growthTicks++;
            if (growthTicks == IafConfig.myrmexLarvaTicks) {
                this.setGrowthStage(this.getGrowthStage() + 1);
                growthTicks = 0;
            }
        }
        if (!this.level.isClientSide && this.getGrowthStage() < 2 && this.getRandom().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }

        if (this.getTarget() != null && !(this.getTarget() instanceof PlayerEntity) && this.getNavigation().isDone()) {
            this.setTarget(null);
        }
        if (this.getTarget() != null && (haveSameHive(this, this.getTarget()) ||
            this.getTarget() instanceof TameableEntity && !canAttackTamable((TameableEntity) this.getTarget()) ||
            this.getTarget() instanceof PlayerEntity && this.getHive() != null && !this.getHive().isPlayerReputationLowEnoughToFight(this.getTarget().getUUID()))) {
            this.setTarget(null);
        }
        if (this.getWaitTicks() > 0) {
            this.setWaitTicks(this.getWaitTicks() - 1);
        }
        if (this.getHealth() < this.getMaxHealth() && this.tickCount % 500 == 0 && this.isOnResin()) {
            this.heal(1);
            this.level.broadcastEntityEvent(this, (byte) 76);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
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

        ListNBT listnbt = new ListNBT();

        for (int i = 0; i < this.villagerInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.villagerInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundNBT()));
            }
        }
        tag.put("Inventory", listnbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.setGrowthStage(tag.getInt("GrowthStage"));
        this.growthTicks = tag.getInt("GrowthTicks");
        this.setJungleVariant(tag.getBoolean("Variant"));
        if (tag.hasUUID("HiveUUID")) {
            this.setHive(MyrmexWorldData.get(level).getHiveFromUUID(tag.getUUID("HiveUUID")));
        }
        if (tag.contains("Offers", 10)) {
            this.offers = new MerchantOffers(tag.getCompound("Offers"));
        }

        ListNBT listnbt = tag.getList("Inventory", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.villagerInventory.addItem(itemstack);
            }
        }

    }

    public boolean canAttackTamable(TameableEntity tameable) {
        if (tameable.getOwner() != null && this.getHive() != null) {
            return this.getHive().isPlayerReputationLowEnoughToFight(tameable.getOwnerUUID());
        }
        return true;
    }

    public World getLevel() {
        return this.level;
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

    public void setWaitTicks(int waitTicks) {
        this.waitTicks = waitTicks;
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public boolean isJungle() {
        return this.entityData.get(VARIANT).booleanValue();
    }

    public void setJungleVariant(boolean isJungle) {
        this.entityData.set(VARIANT, isJungle);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
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

        this.entityData.set(CLIMBING, Byte.valueOf(b0));
    }

    public boolean onClimbable() {
        if (this.getNavigation() instanceof AdvancedPathNavigate) {
            //Make sure the entity can only climb when it's on or below the path. This prevents the entity from getting stuck
            if (((AdvancedPathNavigate) this.getNavigation()).entityOnAndBelowPath(this, new Vector3d(1.1, 0, 1.1)))
                return true;
        }
        return super.onClimbable();
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
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

    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        if (this.getHive() == null || livingBase == null || livingBase instanceof PlayerEntity && this.getHive().isPlayerReputationLowEnoughToFight(livingBase.getUUID())) {
            super.setLastHurtByMob(livingBase);
        }
        if (this.getHive() != null && livingBase != null) {
            this.getHive().addOrRenewAgressor(livingBase, this.getImportance());
        }
        if (this.getHive() != null && livingBase != null) {
            if (livingBase instanceof PlayerEntity) {
                int i = -5 * this.getCasteImportance();
                this.getHive().setWorld(this.level);
                this.getHive().modifyPlayerReputation(livingBase.getUUID(), i);
                if (this.isAlive()) {
                    this.level.broadcastEntityEvent(this, (byte) 13);
                }
            }
        }
    }

    public void die(DamageSource cause) {
        if (this.getHive() != null) {
            Entity entity = cause.getEntity();
            if (entity != null) {
                this.getHive().setWorld(this.level);
                this.getHive().modifyPlayerReputation(entity.getUUID(), -15);
            }
        }
        this.resetCustomer();
        super.die(cause);
    }

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!shouldHaveNormalAI()) {
            return ActionResultType.PASS;
        }
        boolean flag2 = itemstack.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF || itemstack.getItem() == IafItemRegistry.MYRMEX_DESERT_STAFF;

        if (flag2) {
            this.onStaffInteract(player, itemstack);
            player.swing(hand);
            return ActionResultType.SUCCESS;
        }
        boolean flag = itemstack.getItem() == Items.NAME_TAG || itemstack.getItem() == Items.LEAD;
        if (flag) {
            return super.mobInteract(player, hand);
        } else if (this.getGrowthStage() >= 2 && this.isAlive() && !this.isBaby() && !player.isShiftKeyDown()) {
            if (this.getOffers().isEmpty()) {
                return super.mobInteract(player, hand);
            } else {
                if (!this.level.isClientSide && (this.getTarget() == null || !this.getTarget().equals(player)) && hand == Hand.MAIN_HAND) {
                    if (this.getHive() != null && !this.getHive().isPlayerReputationTooLowToTrade(player.getUUID())) {
                        this.setTradingPlayer(player);
                        this.openTradingScreen(player, this.getDisplayName(), 1);
                        return ActionResultType.SUCCESS;
                    }
                }

                return ActionResultType.PASS;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void onStaffInteract(PlayerEntity player, ItemStack itemstack) {
        if (itemstack.getTag() == null) {
            return;
        }
        UUID staffUUID = itemstack.getTag().hasUUID("HiveUUID") ? itemstack.getTag().getUUID("HiveUUID") : null;
        if (level.isClientSide) {
            return;
        }
        if (!player.isCreative()) {
            if ((this.getHive() != null && !this.getHive().canPlayerCommandHive(player.getUUID()))) {
                return;
            }
        }
        if (this.getHive() == null) {
            player.displayClientMessage(new TranslationTextComponent("myrmex.message.null_hive"), true);

        } else {
            if (staffUUID != null && staffUUID.equals(this.getHive().hiveUUID)) {
                player.displayClientMessage(new TranslationTextComponent("myrmex.message.staff_already_set"), true);
            } else {
                this.getHive().setWorld(this.level);
                EntityMyrmexQueen queen = this.getHive().getQueen();
                BlockPos center = this.getHive().getCenterGround();
                if (queen != null && queen.hasCustomName()) {
                    player.displayClientMessage(new TranslationTextComponent("myrmex.message.staff_set_named", queen.getName(), center.getX(), center.getY(), center.getZ()), true);
                } else {
                    player.displayClientMessage(new TranslationTextComponent("myrmex.message.staff_set_unnamed", center.getX(), center.getY(), center.getZ()), true);
                }
                itemstack.getTag().putUUID("HiveUUID", this.getHive().hiveUUID);
            }

        }

    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHive(MyrmexWorldData.get(level).getNearestHive(this.blockPosition(), 400));
        if (this.getHive() != null) {
            this.setJungleVariant(isJungleBiome(level, this.getHive().getCenter()));
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

    protected void doPush(Entity entityIn) {
        if (!haveSameHive(this, entityIn)) {
            entityIn.push(this);
        }
    }

    public boolean canSeeSky() {
        return level.canSeeSkyFromBelowWater(this.blockPosition());
    }

    public boolean isOnResin() {
        double d0 = this.getY() - 1;
        BlockPos blockpos = new BlockPos(this.getX(), d0, this.getZ());
        while (level.isEmptyBlock(blockpos) && blockpos.getY() > 1) {
            blockpos = blockpos.below();
        }
        BlockState BlockState = this.level.getBlockState(blockpos);
        return BlockState.getBlock() instanceof BlockMyrmexResin || BlockState.getBlock() instanceof BlockMyrmexConnectedResin;
    }

    public boolean isInNursery() {
        if (getHive() != null && getHive().getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition()) != null) {
            return false;
        }
        if (getHive() != null) {
            BlockPos nursery = getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.blockPosition());
            return MathHelper.sqrt(this.distanceToSqr(nursery.getX(), nursery.getY(), nursery.getZ())) < 45;
        }
        return false;
    }

    public boolean isInHive(){
        if (getHive() != null) {
            for (BlockPos pos : getHive().getAllRooms()) {
                if (isCloseEnoughToTarget(MyrmexHive.getGroundedPos(getLevel(), pos), 50))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void travel(Vector3d motion) {
        if (!this.canMove()) {
            super.travel(Vector3d.ZERO);
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

    public void handleEntityEvent(byte id) {
        if (id == 76) {
            this.playVillagerEffect();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.MYRMEX_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.MYRMEX_DIE;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(IafSoundRegistry.MYRMEX_WALK, 0.16F * this.getMyrmexPitch() * (this.getRandom().nextFloat() * 0.6F + 0.4F), 1.0F);
    }

    protected void playBiteSound() {
        this.playSound(IafSoundRegistry.MYRMEX_BITE, 1.0F * this.getMyrmexPitch(), 1.0F);
    }

    protected void playStingSound() {
        this.playSound(IafSoundRegistry.MYRMEX_STING, 1.0F * this.getMyrmexPitch(), 0.6F);
    }

    protected void playVillagerEffect() {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
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

    public AxisAlignedBB getAttackBounds() {
        float size = this.getScale() * 0.65F;
        return this.getBoundingBox().inflate(1.0F + size, 1.0F + size, 1.0F + size);
    }

    @Nullable
    public PlayerEntity getTradingPlayer() {
        return this.customer;
    }

    public void setTradingPlayer(@Nullable PlayerEntity player) {
        this.customer = player;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.populateTradeData();
        }

        return this.offers;
    }

    public void overrideOffers(@Nullable MerchantOffers offers) {
    }

    public void overrideXp(int xpIn) {
    }

    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.onVillagerTrade(offer);
    }

    protected void onVillagerTrade(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.getHive().setWorld(this.level);
            this.getHive().modifyPlayerReputation(this.getTradingPlayer().getUUID(), 1);
        }
    }

    public void notifyTradeUpdated(ItemStack stack) {
        if (!this.level.isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getVillagerYesNoSound(!stack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }

    }

    public SoundEvent getNotifyTradeSound() {
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

    @Nullable
    public Entity changeDimension(ServerWorld server, net.minecraftforge.common.util.ITeleporter teleporter) {
        this.resetCustomer();
        return super.changeDimension(server, teleporter);
    }

    public Inventory getVillagerInventory() {
        return this.villagerInventory;
    }

    public boolean setSlot(int inventorySlot, ItemStack itemStackIn) {
        if (super.setSlot(inventorySlot, itemStackIn)) {
            return true;
        } else {
            int i = inventorySlot - 300;
            if (i >= 0 && i < this.villagerInventory.getContainerSize()) {
                this.villagerInventory.setItem(i, itemStackIn);
                return true;
            } else {
                return false;
            }
        }
    }

    protected void addTrades(MerchantOffers givenMerchantOffers, VillagerTrades.ITrade[] newTrades, int maxNumbers) {
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
            VillagerTrades.ITrade villagertrades$itrade = newTrades[integer];
            MerchantOffer merchantoffer = villagertrades$itrade.getOffer(this, this.random);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
            }
        }

    }

    private void levelUp() {
        this.populateTradeData();
    }

    protected abstract VillagerTrades.ITrade[] getLevel1Trades();

    protected abstract VillagerTrades.ITrade[] getLevel2Trades();

    protected void populateTradeData() {
        VillagerTrades.ITrade[] level1 = getLevel1Trades();
        VillagerTrades.ITrade[] level2 = getLevel2Trades();
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
            VillagerTrades.ITrade rareTrade1 = level2[i];
            VillagerTrades.ITrade rareTrade2 = level2[j];
            VillagerTrades.ITrade rareTrade3 = level2[k];
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

    public boolean isSmallerThanBlock() {
        return false;
    }

    public float getXZNavSize() {
        return getBbWidth() / 2;
    }

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
        return state.getMaterial() == Material.LEAVES;
    }
}
