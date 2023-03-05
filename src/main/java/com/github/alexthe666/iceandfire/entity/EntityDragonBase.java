package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.block.IDragonProof;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.client.model.util.LegSolverQuadruped;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.props.ChainProperties;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import com.github.alexthe666.iceandfire.item.ItemSummoningCrystal;
import com.github.alexthe666.iceandfire.message.MessageDragonSetBurnBlock;
import com.github.alexthe666.iceandfire.message.MessageStartRidingMob;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathingStuckHandler;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class EntityDragonBase extends TameableEntity implements IPassabilityNavigator, ISyncMount, IFlyingMount, IMultipartEntity, IAnimatedEntity, IDragonFlute, IDeadMob, IVillagerFear, IAnimalFear, IDropArmor, IHasCustomizableAttributes, ICustomSizeNavigator, ICustomMoveController, IInventoryChangedListener {

    public static final int FLIGHT_CHANCE_PER_TICK = 1500;
    protected static final DataParameter<Boolean> SWIMMING = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> AGE_TICKS = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIREBREATHING = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVERING = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MODEL_DEAD = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DEATH_STAGE = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> TACKLE = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AGINGDISABLED = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DRAGON_PITCH = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> CRYSTAL_BOUND = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> CUSTOM_POSE = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.STRING);
    public static Animation ANIMATION_FIRECHARGE;
    public static Animation ANIMATION_EAT;
    public static Animation ANIMATION_SPEAK;
    public static Animation ANIMATION_BITE;
    public static Animation ANIMATION_SHAKEPREY;
    public static Animation ANIMATION_WINGBLAST;
    public static Animation ANIMATION_ROAR;
    public static Animation ANIMATION_EPIC_ROAR;
    public static Animation ANIMATION_TAILWHACK;
    public DragonType dragonType;
    public double minimumDamage;
    public double maximumDamage;
    public double minimumHealth;
    public double maximumHealth;
    public double minimumSpeed;
    public double maximumSpeed;
    public double minimumArmor;
    public double maximumArmor;
    public float sitProgress;
    public float sleepProgress;
    public float hoverProgress;
    public float flyProgress;
    public float fireBreathProgress;
    public float diveProgress;
    public float prevDiveProgress;
    public float prevFireBreathProgress;
    public int fireStopTicks;
    public int flyTicks;
    public float modelDeadProgress;
    public float prevModelDeadProgress;
    public float ridingProgress;
    public float tackleProgress;
    /*
    0 = sit
    1 = sleep
    2 = hover
    3 = fly
    4 = fireBreath
    5 = riding
    6 = tackle
     */
    public boolean isSwimming;
    public float prevSwimProgress;
    public float swimProgress;
    public int ticksSwiming;
    public int swimCycle;
    public float[] prevAnimationProgresses = new float[10];
    public boolean isDaytime;
    public int flightCycle;
    public HomePosition homePos;
    public boolean hasHomePosition = false;
    public IFChainBuffer roll_buffer;
    public IFChainBuffer pitch_buffer;
    public IFChainBuffer pitch_buffer_body;
    public ReversedBuffer turn_buffer;
    public ChainBuffer tail_buffer;
    public int spacebarTicks;
    public float[][] growth_stages;
    public LegSolverQuadruped legSolver;
    public int walkCycle;
    public BlockPos burningTarget;
    public int burnProgress;
    public double burnParticleX;
    public double burnParticleY;
    public double burnParticleZ;
    public float prevDragonPitch;
    public IafDragonAttacks.Air airAttack;
    public IafDragonAttacks.Ground groundAttack;
    public boolean usingGroundAttack = true;
    public IafDragonLogic logic;
    public int hoverTicks;
    public int tacklingTicks;
    public int ticksStill;
    /*
        0 = ground/walking
        1 = ai flight
        2 = controlled flight
     */
    public int navigatorType;
    public Inventory inventory;
    public String prevArmorResLoc = "0|0|0|0";
    public String armorResLoc = "0|0|0|0";
    public IafDragonFlightManager flightManager;
    public boolean lookingForRoostAIFlag = false;
    protected int flyHovering;
    protected boolean hasHadHornUse = false;
    protected int fireTicks;
    protected int blockBreakCounter;
    private int prevFlightCycle;
    private boolean isModelDead;
    private int animationTick;
    private Animation currentAnimation;
    private float lastScale;

    private EntityDragonPart headPart;
    private EntityDragonPart neckPart;
    private EntityDragonPart rightWingUpperPart;
    private EntityDragonPart rightWingLowerPart;
    private EntityDragonPart leftWingUpperPart;
    private EntityDragonPart leftWingLowerPart;
    private EntityDragonPart tail1Part;
    private EntityDragonPart tail2Part;
    private EntityDragonPart tail3Part;
    private EntityDragonPart tail4Part;
    private boolean isOverAir;

    private LazyOptional<?> itemHandler = null;

    public EntityDragonBase(EntityType t, World world, DragonType type, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
        super(t, world);
        IHasCustomizableAttributes.applyAttributesForEntity(t, this);

        this.dragonType = type;
        this.minimumDamage = minimumDamage;
        this.maximumDamage = maximumDamage;
        this.minimumHealth = minimumHealth;
        this.maximumHealth = maximumHealth;
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.minimumArmor = 1D;
        this.maximumArmor = 20D;
        ANIMATION_EAT = Animation.create(20);
        this.createInventory();
        if (world.isRemote) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            pitch_buffer_body = new IFChainBuffer();
            turn_buffer = new ReversedBuffer();
            tail_buffer = new ChainBuffer();
        }
        legSolver = new LegSolverQuadruped(0.3F, 0.35F, 0.2F, 1.45F, 1.0F);
        this.flightManager = new IafDragonFlightManager(this);
        this.logic = createDragonLogic();
        this.ignoreFrustumCheck = true;
        switchNavigator(0);
        randomizeAttacks();
        resetParts(1);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.func_233666_p_()
                //HEALTH
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                //SPEED
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D)
                //ATTACK
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1)
                //FOLLOW RANGE
                .createMutableAttribute(Attributes.FOLLOW_RANGE, Math.min(2048, IafConfig.dragonTargetSearchLength))
                //ARMOR
                .createMutableAttribute(Attributes.ARMOR, 4);
    }

    @Override
    public AttributeModifierMap.MutableAttribute getAttributes() {
        return bakeAttributes();
    }

    @Override
    public BlockPos getHomePosition() {
        return this.homePos == null ? super.getHomePosition() : homePos.getPosition();
    }

    @Override
    public float getMaximumHomeDistance() {
        return IafConfig.dragonWanderFromHomeDistance;
    }

    public String getHomeDimensionName() {
        return this.homePos == null ? "" : homePos.getDimension();
    }

    @Override
    public boolean detachHome() {
        return this.hasHomePosition &&
                getHomeDimensionName().equals(DragonUtils.getDimensionName(this.world))
                || super.detachHome();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DragonAIRide<>(this));
        this.goalSelector.addGoal(1, new SitGoal(this));
        this.goalSelector.addGoal(2, new DragonAIMate(this, 1.0D));
        this.goalSelector.addGoal(3, new DragonAIReturnToRoost(this, 1.0D));
        this.goalSelector.addGoal(4, new DragonAIEscort(this, 1.0D));
        this.goalSelector.addGoal(5, new DragonAIAttackMelee(this, 1.5D, false));
        this.goalSelector.addGoal(6, new AquaticAITempt(this, 1.0D, IafItemRegistry.FIRE_STEW, false));
        this.goalSelector.addGoal(7, new DragonAIWander(this, 1.0D));
        this.goalSelector.addGoal(8, new DragonAIWatchClosest(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new DragonAILookIdle(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new DragonAITargetNonTamed(this, LivingEntity.class, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) && DragonUtils.canHostilesTarget(entity) && entity.getType() != EntityDragonBase.this.getType() && EntityDragonBase.this.shouldTarget(entity) && DragonUtils.isAlive(entity);
            }
        }));
        this.targetSelector.addGoal(5, new DragonAITarget(this, LivingEntity.class, true, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return entity instanceof LivingEntity && DragonUtils.canHostilesTarget(entity) && entity.getType() != EntityDragonBase.this.getType() && EntityDragonBase.this.shouldTarget(entity) && DragonUtils.isAlive(entity);
            }
        }));
        this.targetSelector.addGoal(6, new DragonAITargetItems(this, false));
    }

    protected abstract boolean shouldTarget(Entity entity);

    public void resetParts(float scale) {
        removeParts();
        headPart = new EntityDragonPart(this, 1.55F * scale, 0, 0.6F * scale, 0.5F * scale, 0.35F * scale, 1.5F);
        headPart.copyLocationAndAnglesFrom(this);
        headPart.setParent(this);
        neckPart = new EntityDragonPart(this, 0.85F * scale, 0, 0.7F * scale, 0.5F * scale, 0.2F * scale, 1);
        neckPart.copyLocationAndAnglesFrom(this);
        neckPart.setParent(this);
        rightWingUpperPart = new EntityDragonPart(this, scale, 90, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        rightWingUpperPart.copyLocationAndAnglesFrom(this);
        rightWingUpperPart.setParent(this);
        rightWingLowerPart = new EntityDragonPart(this, 1.4F * scale, 100, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        rightWingLowerPart.copyLocationAndAnglesFrom(this);
        rightWingLowerPart.setParent(this);
        leftWingUpperPart = new EntityDragonPart(this, scale, -90, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        leftWingUpperPart.copyLocationAndAnglesFrom(this);
        leftWingUpperPart.setParent(this);
        leftWingLowerPart = new EntityDragonPart(this, 1.4F * scale, -100, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        leftWingLowerPart.copyLocationAndAnglesFrom(this);
        leftWingLowerPart.setParent(this);
        tail1Part = new EntityDragonPart(this, -0.75F * scale, 0, 0.6F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail1Part.copyLocationAndAnglesFrom(this);
        tail1Part.setParent(this);
        tail2Part = new EntityDragonPart(this, -1.15F * scale, 0, 0.45F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail2Part.copyLocationAndAnglesFrom(this);
        tail2Part.setParent(this);
        tail3Part = new EntityDragonPart(this, -1.5F * scale, 0, 0.35F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail3Part.copyLocationAndAnglesFrom(this);
        tail3Part.setParent(this);
        tail4Part = new EntityDragonPart(this, -1.95F * scale, 0, 0.25F * scale, 0.45F * scale, 0.3F * scale, 1.5F);
        tail4Part.copyLocationAndAnglesFrom(this);
        tail4Part.setParent(this);
    }

    public void removeParts() {
        if (headPart != null) {
            headPart.remove();
            headPart = null;
        }
        if (neckPart != null) {
            neckPart.remove();
            neckPart = null;
        }
        if (rightWingUpperPart != null) {
            rightWingUpperPart.remove();
            rightWingUpperPart = null;
        }
        if (rightWingLowerPart != null) {
            rightWingLowerPart.remove();
            rightWingLowerPart = null;
        }
        if (leftWingUpperPart != null) {
            leftWingUpperPart.remove();
            leftWingUpperPart = null;
        }
        if (leftWingLowerPart != null) {
            leftWingLowerPart.remove();
            leftWingLowerPart = null;
        }
        if (tail1Part != null) {
            tail1Part.remove();
            tail1Part = null;
        }
        if (tail2Part != null) {
            tail2Part.remove();
            tail2Part = null;
        }
        if (tail3Part != null) {
            tail3Part.remove();
            tail3Part = null;
        }
        if (tail4Part != null) {
            tail4Part.remove();
            tail4Part = null;
        }
    }

    public void updateParts() {
        if (headPart != null) {
            if (!headPart.shouldContinuePersisting()) {
                world.addEntity(headPart);
            }
            headPart.setParent(this);
        }
        if (neckPart != null) {
            if (!neckPart.shouldContinuePersisting()) {
                world.addEntity(neckPart);
            }
            neckPart.setParent(this);
        }
        if (rightWingUpperPart != null) {
            if (!rightWingUpperPart.shouldContinuePersisting()) {
                world.addEntity(rightWingUpperPart);
            }
            rightWingUpperPart.setParent(this);
        }
        if (rightWingLowerPart != null) {
            if (!rightWingLowerPart.shouldContinuePersisting()) {
                world.addEntity(rightWingLowerPart);
            }
            rightWingLowerPart.setParent(this);
        }
        if (leftWingUpperPart != null) {
            if (!leftWingUpperPart.shouldContinuePersisting()) {
                world.addEntity(leftWingUpperPart);
            }
            leftWingUpperPart.setParent(this);
        }
        if (leftWingLowerPart != null) {
            if (!leftWingLowerPart.shouldContinuePersisting()) {
                world.addEntity(leftWingLowerPart);
            }
            leftWingLowerPart.setParent(this);
        }
        if (tail1Part != null) {
            if (!tail1Part.shouldContinuePersisting()) {
                world.addEntity(tail1Part);
            }
            tail1Part.setParent(this);
        }
        if (tail2Part != null) {
            if (!tail2Part.shouldContinuePersisting()) {
                world.addEntity(tail2Part);
            }
            tail2Part.setParent(this);
        }
        if (tail3Part != null) {
            if (!tail3Part.shouldContinuePersisting()) {
                world.addEntity(tail3Part);
            }
            tail3Part.setParent(this);
        }
        if (tail4Part != null) {
            if (!tail4Part.shouldContinuePersisting()) {
                world.addEntity(tail4Part);
            }
            tail4Part.setParent(this);
        }
    }

    protected void updateBurnTarget() {
        if (burningTarget != null && !this.isSleeping() && !this.isModelDead() && !this.isChild()) {
            float maxDist = 115 * this.getDragonStage();
            boolean flag = false;
            if (world.getTileEntity(burningTarget) instanceof TileEntityDragonforgeInput && ((TileEntityDragonforgeInput) world.getTileEntity(burningTarget)).isAssembled()
                    && this.getDistanceSq(burningTarget.getX() + 0.5D, burningTarget.getY() + 0.5D, burningTarget.getZ() + 0.5D) < maxDist && canPositionBeSeen(burningTarget.getX() + 0.5D, burningTarget.getY() + 0.5D, burningTarget.getZ() + 0.5D)) {
                this.getLookController().setLookPosition(burningTarget.getX() + 0.5D, burningTarget.getY() + 0.5D, burningTarget.getZ() + 0.5D, 180F, 180F);
                this.breathFireAtPos(burningTarget);
                this.setBreathingFire(true);
            } else {
                if (!world.isRemote) {
                    IceAndFire.sendMSGToAll(new MessageDragonSetBurnBlock(this.getEntityId(), true, burningTarget));
                }
                burningTarget = null;
            }
        }
    }

    protected abstract void breathFireAtPos(BlockPos burningTarget);

    protected PathingStuckHandler createStuckHandler() {
        return PathingStuckHandler.createStuckHandler();
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.WALKING);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, createStuckHandler());
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler) {
        return createNavigator(worldIn, type, stuckHandler, 4f, 4f);
    }

    protected PathNavigator createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, world, type, width, height);
        this.navigator = newNavigator;
        newNavigator.setCanSwim(true);
        newNavigator.getNodeProcessor().setCanOpenDoors(true);
        return newNavigator;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveController = new IafDragonFlightManager.GroundMoveHelper(this);
            this.navigator = createNavigator(world, AdvancedPathNavigate.MovementType.WALKING, createStuckHandler().withTeleportSteps(5));
            this.navigatorType = 0;
            this.setFlying(false);
            this.setHovering(false);
        } else if (navigatorType == 1) {
            this.moveController = new IafDragonFlightManager.FlightMoveHelper(this);
            this.navigator = createNavigator(world, AdvancedPathNavigate.MovementType.FLYING);
            this.navigatorType = 1;
        } else {
            this.moveController = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
            this.navigator = createNavigator(world, AdvancedPathNavigate.MovementType.FLYING);
            this.navigatorType = 2;
        }
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        breakBlock();
    }

    public boolean canDestroyBlock(BlockPos pos, BlockState state) {
        return state.getBlock().canEntityDestroy(state, world, pos, this);
    }

    @Override
    public boolean isMobDead() {
        return this.isModelDead();
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 30 * this.getDragonStage() / 5;
    }

    public void openInventory(PlayerEntity player) {
        if (!this.world.isRemote)
            NetworkHooks.openGui((ServerPlayerEntity) player, getMenuProvider());
        IceAndFire.PROXY.setReferencedMob(this);
    }

    public INamedContainerProvider getMenuProvider() {
        return new SimpleNamedContainerProvider((containerId, playerInventory, player) -> new ContainerDragon(containerId, inventory, playerInventory, this), this.getDisplayName());
    }

    @Override
    public int getTalkInterval() {
        return 90;
    }

    @Override
    protected void onDeathUpdate() {
        this.deathTime = 0;
        this.setModelDead(true);
        this.removePassengers();
        if (this.getDeathStage() >= this.getAgeInDays() / 5) {
            this.remove();
            for (int k = 0; k < 40; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (world.isRemote) {
                    this.world.addParticle(ParticleTypes.CLOUD, this.getPosX() + this.rand.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), this.getPosY() + this.rand.nextFloat() * this.getHeight(), this.getPosZ() + this.rand.nextFloat() * this.getWidth() * 2.0F - this.getWidth(), d2, d0, d1);
                }
            }
            spawnDeathParticles();
        }
    }

    protected void spawnDeathParticles() {
    }

    protected void spawnBabyParticles() {
    }

    @Override
    public void remove() {
        removeParts();
        super.remove();
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        switch (this.getDragonStage()) {
            case 2:
                return 20;
            case 3:
                return 150;
            case 4:
                return 300;
            case 5:
                return 650;
            default:
                return 5;
        }
    }

    public int getArmorOrdinal(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor) {
            ItemDragonArmor armorItem = (ItemDragonArmor) stack.getItem();
            return armorItem.type + 1;
        }
        return 0;
    }

    @Override
    public boolean isAIDisabled() {
        return this.isModelDead() || super.isAIDisabled();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HUNGER, 0);
        this.dataManager.register(AGE_TICKS, 0);
        this.dataManager.register(GENDER, false);
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(FIREBREATHING, false);
        this.dataManager.register(HOVERING, false);
        this.dataManager.register(FLYING, false);
        this.dataManager.register(DEATH_STAGE, 0);
        this.dataManager.register(MODEL_DEAD, false);
        this.dataManager.register(CONTROL_STATE, (byte) 0);
        this.dataManager.register(TACKLE, false);
        this.dataManager.register(AGINGDISABLED, false);
        this.dataManager.register(COMMAND, 0);
        this.dataManager.register(DRAGON_PITCH, 0F);
        this.dataManager.register(CRYSTAL_BOUND, false);
        this.dataManager.register(CUSTOM_POSE, "");
    }

    @Override
    public boolean isGoingUp() {
        return (dataManager.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean isAttacking() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    public boolean isStriking() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
    }

    public boolean isDismounting() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 4 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        setStateField(0, up);
    }

    @Override
    public void down(boolean down) {
        setStateField(1, down);
    }

    @Override
    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {
        setStateField(3, strike);
    }

    @Override
    public void dismount(boolean dismount) {
        setStateField(4, dismount);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = dataManager.get(CONTROL_STATE);
        if (newState) {
            dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return dataManager.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        dataManager.set(CONTROL_STATE, state);
    }

    public int getCommand() {
        return this.dataManager.get(COMMAND);
    }

    public void setCommand(int command) {
        this.dataManager.set(COMMAND, command);
        this.setSitting(command == 1);
    }

    public float getDragonPitch() {
        return dataManager.get(DRAGON_PITCH);
    }

    public void setDragonPitch(float pitch) {
        dataManager.set(DRAGON_PITCH, pitch);
    }

    public void incrementDragonPitch(float pitch) {
        dataManager.set(DRAGON_PITCH, getDragonPitch() + pitch);
    }

    public void decrementDragonPitch(float pitch) {
        dataManager.set(DRAGON_PITCH, getDragonPitch() - pitch);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Hunger", this.getHunger());
        compound.putInt("AgeTicks", this.getAgeInTicks());
        compound.putBoolean("Gender", this.isMale());
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("TamedDragon", this.isTamed());
        compound.putBoolean("FireBreathing", this.isBreathingFire());
        compound.putBoolean("AttackDecision", usingGroundAttack);
        compound.putBoolean("Hovering", this.isHovering());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("DeathStage", this.getDeathStage());
        compound.putBoolean("ModelDead", this.isModelDead());
        compound.putFloat("DeadProg", this.modelDeadProgress);
        compound.putBoolean("Tackle", this.isTackling());
        compound.putBoolean("HasHomePosition", this.hasHomePosition);
        compound.putString("CustomPose", this.getCustomPose());
        if (homePos != null && this.hasHomePosition) {
            homePos.write(compound);
        }
        compound.putBoolean("AgingDisabled", this.isAgingDisabled());
        compound.putInt("Command", this.getCommand());
        if (inventory != null) {
            ListNBT nbttaglist = new ListNBT();
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = inventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    CompoundNBT CompoundNBT = new CompoundNBT();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.write(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putBoolean("CrystalBound", this.isBoundToCrystal());
        if (this.hasCustomName()) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.getCustomName()));
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setHunger(compound.getInt("Hunger"));
        this.setAgeInTicks(compound.getInt("AgeTicks"));
        this.setGender(compound.getBoolean("Gender"));
        this.setVariant(compound.getInt("Variant"));
        this.setQueuedToSit(compound.getBoolean("Sleeping"));
        this.setTamed(compound.getBoolean("TamedDragon"));
        this.setBreathingFire(compound.getBoolean("FireBreathing"));
        this.usingGroundAttack = compound.getBoolean("AttackDecision");
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setDeathStage(compound.getInt("DeathStage"));
        this.setModelDead(compound.getBoolean("ModelDead"));
        this.modelDeadProgress = compound.getFloat("DeadProg");
        this.setCustomPose(compound.getString("CustomPose"));
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            homePos = new HomePosition(compound, this.world);
        }
        this.setTackling(compound.getBoolean("Tackle"));
        this.setAgingDisabled(compound.getBoolean("AgingDisabled"));
        this.setCommand(compound.getInt("Command"));
        if (inventory != null) {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (INBT inbt : nbttaglist) {
                CompoundNBT CompoundNBT = (net.minecraft.nbt.CompoundNBT) inbt;
                int j = CompoundNBT.getByte("Slot") & 255;
                if (j <= 4) {
                    inventory.setInventorySlotContents(j, ItemStack.read(CompoundNBT));
                }
            }
        } else {
            ListNBT nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (INBT inbt : nbttaglist) {
                CompoundNBT CompoundNBT = (net.minecraft.nbt.CompoundNBT) inbt;
                int j = CompoundNBT.getByte("Slot") & 255;
                inventory.setInventorySlotContents(j, ItemStack.read(CompoundNBT));
            }
        }
        this.setCrystalBound(compound.getBoolean("CrystalBound"));
        if (compound.contains("CustomName", 8) && !compound.getString("CustomName").startsWith("TextComponent")) {
            this.setCustomName(ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName")));
        }
    }

    public int getInventorySize() {
        return 5;
    }

    protected void createInventory() {
        Inventory tempInventory = this.inventory;
        this.inventory = new Inventory(this.getInventorySize());
        if (tempInventory != null) {
            tempInventory.removeListener(this);
            int i = Math.min(tempInventory.getSizeInventory(), this.inventory.getSizeInventory());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = tempInventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateContainerEquipment();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    protected void updateContainerEquipment() {
        if (!this.world.isRemote) {
            updateAttributes();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }

    public boolean hasInventoryChanged(Inventory pInventory) {
        return this.inventory != pInventory;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof PlayerEntity && this.getAttackTarget() != passenger) {
                PlayerEntity player = (PlayerEntity) passenger;
                if (this.isTamed() && this.getOwnerId() != null && this.getOwnerId().equals(player.getUniqueID())) {
                    return player;
                }
            }
        }
        return null;
    }

    public boolean isRidingPlayer(PlayerEntity player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Override
    @Nullable
    public PlayerEntity getRidingPlayer() {
        if (this.getControllingPassenger() instanceof PlayerEntity) {
            return (PlayerEntity) this.getControllingPassenger();
        }
        return null;
    }

    protected void updateAttributes() {
        prevArmorResLoc = armorResLoc;
        final int armorHead = this.getArmorOrdinal(this.getItemStackFromSlot(EquipmentSlotType.HEAD));
        final int armorNeck = this.getArmorOrdinal(this.getItemStackFromSlot(EquipmentSlotType.CHEST));
        final int armorLegs = this.getArmorOrdinal(this.getItemStackFromSlot(EquipmentSlotType.LEGS));
        final int armorFeet = this.getArmorOrdinal(this.getItemStackFromSlot(EquipmentSlotType.FEET));
        armorResLoc = dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
        IceAndFire.PROXY.updateDragonArmorRender(armorResLoc);

        double age = 125F;
        if (this.getAgeInDays() <= 125) age = this.getAgeInDays();
        final double healthStep = (maximumHealth - minimumHealth) / 125F;
        final double attackStep = (maximumDamage - minimumDamage) / 125F;
        final double speedStep = (maximumSpeed - minimumSpeed) / 125F;
        final double armorStep = (maximumArmor - minimumArmor) / 125F;

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.round(minimumHealth + (healthStep * age)));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.round(minimumDamage + (attackStep * age)));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(minimumSpeed + (speedStep * age));
        final double baseValue = minimumArmor + (armorStep * age);
        this.getAttribute(Attributes.ARMOR).setBaseValue(baseValue);
        if (!this.world.isRemote) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            this.getAttribute(Attributes.ARMOR).applyPersistentModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Dragon armor bonus", calculateArmorModifier(), AttributeModifier.Operation.ADDITION));
        }
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IafConfig.dragonTargetSearchLength));
    }

    public int getHunger() {
        return this.dataManager.get(HUNGER);
    }

    public void setHunger(int hunger) {
        this.dataManager.set(HUNGER, MathHelper.clamp(hunger, 0, 100));
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getAgeInDays() {
        return this.dataManager.get(AGE_TICKS) / 24000;
    }

    public void setAgeInDays(int age) {
        this.dataManager.set(AGE_TICKS, age * 24000);
    }

    public int getAgeInTicks() {
        return this.dataManager.get(AGE_TICKS);
    }

    public void setAgeInTicks(int age) {
        this.dataManager.set(AGE_TICKS, age);
    }

    public int getDeathStage() {
        return this.dataManager.get(DEATH_STAGE);
    }

    public void setDeathStage(int stage) {
        this.dataManager.set(DEATH_STAGE, stage);
    }

    public boolean isMale() {
        return this.dataManager.get(GENDER);
    }

    public boolean isModelDead() {
        if (world.isRemote) {
            return this.isModelDead = this.dataManager.get(MODEL_DEAD);
        }
        return isModelDead;
    }

    public void setModelDead(boolean modeldead) {
        this.dataManager.set(MODEL_DEAD, modeldead);
        if (!world.isRemote) {
            this.isModelDead = modeldead;
        }
    }

    @Override
    public boolean isHovering() {
        return this.dataManager.get(HOVERING);
    }

    public void setHovering(boolean hovering) {
        this.dataManager.set(HOVERING, hovering);
    }

    @Override
    public boolean isFlying() {
        return this.dataManager.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
    }

    public boolean useFlyingPathFinder() {
        return isFlying();
    }

    public void setGender(boolean male) {
        this.dataManager.set(GENDER, male);
    }

    @Override
    public boolean isSleeping() {
        return this.dataManager.get(SLEEPING);
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    public boolean isBreathingFire() {
        return this.dataManager.get(FIREBREATHING);
    }

    public void setBreathingFire(boolean breathing) {
        this.dataManager.set(FIREBREATHING, breathing);
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    public boolean isQueuedToSit() {
        return (this.dataManager.get(TAMED).byteValue() & 1) != 0;
    }

    @Override
    public void setQueuedToSit(boolean sleeping) {
        this.dataManager.set(SLEEPING, sleeping);
        if (sleeping)
            this.getNavigator().clearPath();
    }

    @Override
    public void setSitting(boolean sitting) {
        byte b0 = this.dataManager.get(TAMED);
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 | 1)));
            this.getNavigator().clearPath();
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public String getCustomPose() {
        return this.dataManager.get(CUSTOM_POSE);
    }

    public void setCustomPose(String customPose) {
        this.dataManager.set(CUSTOM_POSE, customPose);
        modelDeadProgress = 20f;
    }

    public void riderShootFire(Entity controller) {
    }

    @Override
    public void onKillEntity(ServerWorld world, LivingEntity entity) {
        this.setHunger(this.getHunger() + FoodUtils.getFoodPoints(entity));
    }

    private double calculateArmorModifier() {
        double val = 1D;
        final EquipmentSlotType[] slots = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
        for (EquipmentSlotType slot : slots) {
            switch (getArmorOrdinal(getItemStackFromSlot(slot))) {
                case 1:
                    val += 2D;
                    break;
                case 2:
                case 4:
                    val += 3D;
                    break;
                case 3:
                    val += 5D;
                    break;
                case 5:
                case 6:
                case 8:
                    val += 10D;
                    break;
                case 7:
                    val += 1.5D;
                    break;
            }
        }
        return val;
    }

    public boolean canMove() {
        return !this.isQueuedToSit() && !this.isSleeping() &&
                this.getControllingPassenger() == null && !this.isPassenger() &&
                !this.isModelDead() && sleepProgress == 0 &&
                this.getAnimation() != ANIMATION_SHAKEPREY;
    }

    public boolean isFuelingForge() {
        return burningTarget != null && world.getTileEntity(burningTarget) instanceof TileEntityDragonforgeInput;
    }

    @Override
    public boolean isAlive() {
        if (this.isModelDead())
            return !this.removed;
        return super.isAlive();
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int lastDeathStage = Math.min(this.getAgeInDays() / 5, 25);
        if (stack.getItem() == IafItemRegistry.DRAGON_DEBUG_STICK) {
            logic.debug();
            return ActionResultType.SUCCESS;
        }
        if (this.isModelDead() && this.getDeathStage() < lastDeathStage && player.isAllowEdit()) {
            if (!world.isRemote && !stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GLASS_BOTTLE && this.getDeathStage() < lastDeathStage / 2 && IafConfig.dragonDropBlood) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                this.setDeathStage(this.getDeathStage() + 1);
                player.inventory.addItemStackToInventory(new ItemStack(this.getBloodItem(), 1));
                return ActionResultType.SUCCESS;
            } else if (!world.isRemote && stack.isEmpty() && IafConfig.dragonDropSkull) {
                if (this.getDeathStage() >= lastDeathStage - 1) {
                    ItemStack skull = getSkull().copy();
                    skull.setTag(new CompoundNBT());
                    skull.getTag().putInt("Stage", this.getDragonStage());
                    skull.getTag().putInt("DragonType", 0);
                    skull.getTag().putInt("DragonAge", this.getAgeInDays());
                    this.setDeathStage(this.getDeathStage() + 1);
                    if (!world.isRemote) {
                        this.entityDropItem(skull, 1);
                    }
                    this.remove();
                } else if (this.getDeathStage() == (lastDeathStage / 2) - 1 && IafConfig.dragonDropHeart) {
                    ItemStack heart = new ItemStack(this.getHeartItem(), 1);
                    ItemStack egg = new ItemStack(this.getVariantEgg(this.rand.nextInt(4)), 1);
                    if (!world.isRemote) {
                        this.entityDropItem(heart, 1);
                        if (!this.isMale() && this.getDragonStage() > 3) {
                            this.entityDropItem(egg, 1);
                        }
                    }
                    this.setDeathStage(this.getDeathStage() + 1);
                } else {
                    this.setDeathStage(this.getDeathStage() + 1);
                    ItemStack drop = getRandomDrop();
                    if (!drop.isEmpty() && !world.isRemote) {
                        this.entityDropItem(drop, 1);
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int lastDeathStage = this.getAgeInDays() / 5;
        if (stack.getItem() == IafItemRegistry.DRAGON_DEBUG_STICK) {
            logic.debug();
            return ActionResultType.SUCCESS;
        }
        if (!this.isModelDead()) {
            if (stack.getItem() == IafItemRegistry.CREATIVE_DRAGON_MEAL) {
                this.setTamed(true);
                this.setTamedBy(player);
                this.setHunger(this.getHunger() + 20);
                this.heal(Math.min(this.getHealth(), (int) (this.getMaxHealth() / 2)));
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                this.spawnItemCrackParticles(stack.getItem());
                this.spawnItemCrackParticles(Items.BONE);
                this.spawnItemCrackParticles(Items.BONE_MEAL);
                this.eatFoodBonus(stack);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (this.isBreedingItem(stack) && this.isAdult()) {
                this.setGrowingAge(0);
                this.consumeItemFromStack(player, stack);
                this.setInLove(player);
                return ActionResultType.SUCCESS;
            }
            if (this.isOwner(player)) {
                if (stack.getItem() == getSummoningCrystal() && !ItemSummoningCrystal.hasDragon(stack)) {
                    this.setCrystalBound(true);
                    CompoundNBT compound = stack.getTag();
                    if (compound == null) {
                        compound = new CompoundNBT();
                        stack.setTag(compound);
                    }
                    CompoundNBT dragonTag = new CompoundNBT();
                    dragonTag.putUniqueId("DragonUUID", this.getUniqueID());
                    if (this.getCustomName() != null) {
                        dragonTag.putString("CustomName", this.getCustomName().getString());
                    }
                    compound.put("Dragon", dragonTag);
                    this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1, 1);
                    player.swingArm(hand);
                    return ActionResultType.SUCCESS;
                }
                this.setTamedBy(player);
                if (stack.getItem() == IafItemRegistry.DRAGON_HORN) {
                    return super.getEntityInteractionResult(player, hand);
                }
                if (stack.isEmpty() && !player.isSneaking()) {
                    if (!world.isRemote) {
                        final int dragonStage = this.getDragonStage();
                        if (dragonStage < 2) {
                            if (player.getPassengers().size() >= 3)
                                return ActionResultType.FAIL;
                            this.startRiding(player, true);
                            IceAndFire.sendMSGToAll(new MessageStartRidingMob(this.getEntityId(), true, true));
                        } else if (dragonStage > 2 && !player.isPassenger()) {
                            player.setSneaking(false);
                            player.startRiding(this, true);
                            IceAndFire.sendMSGToAll(new MessageStartRidingMob(this.getEntityId(), true, false));
                            this.setQueuedToSit(false);
                        }
                        this.getNavigator().clearPath();
                    }
                    return ActionResultType.SUCCESS;
                } else if (stack.isEmpty() && player.isSneaking()) {
                    this.openInventory(player);
                    return ActionResultType.SUCCESS;
                } else {
                    int itemFoodAmount = FoodUtils.getFoodPoints(stack, true, dragonType.isPiscivore());
                    if (itemFoodAmount > 0 && (this.getHunger() < 100 || this.getHealth() < this.getMaxHealth())) {
                        //this.growDragon(1);
                        this.setHunger(this.getHunger() + itemFoodAmount);
                        this.setHealth(Math.min(this.getMaxHealth(), (int) (this.getHealth() + (itemFoodAmount / 10))));
                        this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                        this.spawnItemCrackParticles(stack.getItem());
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    }
                    final Item stackItem = stack.getItem();
                    if (stackItem == IafItemRegistry.DRAGON_MEAL) {
                        this.growDragon(1);
                        this.setHunger(this.getHunger() + 20);
                        this.heal(Math.min(this.getHealth(), (int) (this.getMaxHealth() / 2)));
                        this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                        this.spawnItemCrackParticles(stackItem);
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.BONE_MEAL);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    } else if (stackItem == IafItemRegistry.SICKLY_DRAGON_MEAL && !this.isAgingDisabled()) {
                        this.setHunger(this.getHunger() + 20);
                        this.heal(this.getMaxHealth());
                        this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundVolume(), this.getSoundPitch());
                        this.spawnItemCrackParticles(stackItem);
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.BONE_MEAL);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.setAgingDisabled(true);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return ActionResultType.SUCCESS;
                    } else if (stackItem == IafItemRegistry.DRAGON_STAFF) {
                        if (player.isSneaking()) {
                            if (this.hasHomePosition) {
                                this.hasHomePosition = false;
                                player.sendStatusMessage(new TranslationTextComponent("dragon.command.remove_home"), true);
                                return ActionResultType.SUCCESS;
                            } else {
                                BlockPos pos = this.getPosition();
                                this.homePos = new HomePosition(pos, this.world);
                                this.hasHomePosition = true;
                                player.sendStatusMessage(new TranslationTextComponent("dragon.command.new_home", pos.getX(), pos.getY(), pos.getZ(), homePos.getDimension()), true);
                                return ActionResultType.SUCCESS;
                            }
                        } else {
                            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
                            if (!world.isRemote) {
                                this.setCommand(this.getCommand() + 1);
                                if (this.getCommand() > 2) {
                                    this.setCommand(0);
                                }
                            }
                            String commandText = "stand";
                            if (this.getCommand() == 1) {
                                commandText = "sit";
                            } else if (this.getCommand() == 2) {
                                commandText = "escort";
                            }
                            player.sendStatusMessage(new TranslationTextComponent("dragon.command." + commandText), true);
                            return ActionResultType.SUCCESS;
                        }
                    }
                }
            }
        }
        return super.getEntityInteractionResult(player, hand);

    }

    protected abstract IItemProvider getHeartItem();

    protected abstract Item getBloodItem();

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    private ItemStack getRandomDrop() {
        ItemStack stack = getItemFromLootTable();
        if (stack.getItem() == IafItemRegistry.DRAGON_BONE) {
            this.playSound(SoundEvents.ENTITY_SKELETON_AMBIENT, 1, 1);
        } else {
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
        }
        return stack;
    }

    public boolean canPositionBeSeen(final double x, final double y, final double z) {
        final RayTraceResult result = this.world.rayTraceBlocks(new RayTraceContext(new Vector3d(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ()), new Vector3d(x, y, z), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        final double dist = result.getHitVec().squareDistanceTo(x, y, z);
        return dist <= 1.0D || result.getType() == RayTraceResult.Type.MISS;
    }

    public abstract ResourceLocation getDeadLootTable();

    public ItemStack getItemFromLootTable() {
        LootTable loottable = this.world.getServer().getLootTableManager().getLootTableFromLocation(getDeadLootTable());
        LootContext.Builder lootcontext$builder = this.getLootContextBuilder(false, DamageSource.GENERIC);
        for (ItemStack itemstack : loottable.generate(lootcontext$builder.build(LootParameterSets.ENTITY))) {
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public void eatFoodBonus(ItemStack stack) {

    }


    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    public void growDragon(final int ageInDays) {
        if (this.isAgingDisabled()) {
            return;
        }
        this.setAgeInDays(this.getAgeInDays() + ageInDays);
        this.resetPositionToBB();
        if (world.isRemote) {
            if (this.getAgeInDays() % 25 == 0) {
                for (int i = 0; i < this.getRenderSize() * 4; i++) {
                    final float f = (float) (getRNG().nextFloat() * (this.getBoundingBox().maxX - this.getBoundingBox().minX) + this.getBoundingBox().minX);
                    final float f1 = (float) (getRNG().nextFloat() * (this.getBoundingBox().maxY - this.getBoundingBox().minY) + this.getBoundingBox().minY);
                    final float f2 = (float) (getRNG().nextFloat() * (this.getBoundingBox().maxZ - this.getBoundingBox().minZ) + this.getBoundingBox().minZ);
                    final double motionX = getRNG().nextGaussian() * 0.07D;
                    final double motionY = getRNG().nextGaussian() * 0.07D;
                    final double motionZ = getRNG().nextGaussian() * 0.07D;

                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, f, f1, f2, motionX, motionY, motionZ);
                }
            }
        }
        if (this.getDragonStage() >= 2)
            this.dismount();
        this.updateAttributes();
    }

    public void spawnItemCrackParticles(Item item) {
        for (int i = 0; i < 15; i++) {
            final double motionX = getRNG().nextGaussian() * 0.07D;
            final double motionY = getRNG().nextGaussian() * 0.07D;
            final double motionZ = getRNG().nextGaussian() * 0.07D;
            final Vector3d headVec = this.getHeadPosition();
            if (!world.isRemote) {
                ((ServerWorld) this.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(item)), headVec.x, headVec.y, headVec.z, 1, motionX, motionY, motionZ, 0.1);
            } else {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(item)), headVec.x, headVec.y, headVec.z, motionX, motionY, motionZ);
            }
        }
    }

    public boolean isTimeToWake() {
        return this.world.isDaytime() || this.getCommand() == 2;
    }

    private boolean isStuck() {
        return !this.isChained() && !this.isTamed() && (!this.getNavigator().noPath() && (this.getNavigator().getPath() == null || this.getNavigator().getPath().getFinalPathPoint() != null && this.getDistanceSq(this.getNavigator().getPath().getFinalPathPoint().x, this.getNavigator().getPath().getFinalPathPoint().y, this.getNavigator().getPath().getFinalPathPoint().z) > 15)) && ticksStill > 80 && !this.isHovering() && canMove();
    }

    protected boolean isOverAir() {
        return isOverAir;
    }

    private boolean isOverAirLogic() {
        return world.isAirBlock(new BlockPos(this.getPosX(), this.getBoundingBox().minY - 1, this.getPosZ()));
    }

    public boolean isDiving() {
        return false;//isFlying() && motionY < -0.2;
    }

    public boolean isBeyondHeight() {
        if (this.getPosY() > this.world.getHeight()) {
            return true;
        }
        return this.getPosY() > IafConfig.maxDragonFlight;
    }

    private int calculateDownY() {
        if (this.getNavigator().getPath() != null) {
            Path path = this.getNavigator().getPath();
            Vector3d p = path.getVectorFromIndex(this, Math.min(path.getCurrentPathLength() - 1, path.getCurrentPathIndex() + 1));
            if (p.y < this.getPosY() - 1) {
                return -1;
            }
        }
        return 1;
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0 || IafConfig.dragonBreakBlockCooldown == 0) {
            --this.blockBreakCounter;
            if (!this.isIceInWater() && (this.blockBreakCounter == 0 || IafConfig.dragonBreakBlockCooldown == 0) && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                if (IafConfig.dragonGriefing != 2 && (!this.isTamed() || IafConfig.tamedDragonGriefing)) {
                    if (!isModelDead() && this.getDragonStage() >= 3 && (this.canMove() || this.getControllingPassenger() != null)) {
                        final int bounds = 1;//(int)Math.ceil(this.getRenderSize() * 0.1);
                        final int flightModifier = isFlying() && this.getAttackTarget() != null ? -1 : 1;
                        final int yMinus = calculateDownY();
                        BlockPos.getAllInBox(
                                (int) Math.floor(this.getBoundingBox().minX) - bounds,
                                (int) Math.floor(this.getBoundingBox().minY) + yMinus,
                                (int) Math.floor(this.getBoundingBox().minZ) - bounds,
                                (int) Math.floor(this.getBoundingBox().maxX) + bounds,
                                (int) Math.floor(this.getBoundingBox().maxY) + bounds + flightModifier,
                                (int) Math.floor(this.getBoundingBox().maxZ) + bounds
                        ).forEach(pos -> {
                            if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, pos.getX(), pos.getY(), pos.getZ())))
                                return;
                            final BlockState state = world.getBlockState(pos);
                            final float hardness = IafConfig.dragonGriefing == 1 || this.getDragonStage() <= 3 ? 2.0F : 5.0F;
                            if (isBreakable(pos, state, hardness)) {
                                this.setMotion(this.getMotion().mul(0.6F, 1, 0.6F));
                                if (!world.isRemote) {
                                    if (rand.nextFloat() <= IafConfig.dragonBlockBreakingDropChance && DragonUtils.canDropFromDragonBlockBreak(state)) {
                                        world.destroyBlock(pos, true);
                                    } else {
                                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    protected boolean isBreakable(BlockPos pos, BlockState state, float hardness) {
        return state.getMaterial().blocksMovement() && !state.isAir() && state.getFluidState().isEmpty() && !state.getShape(world, pos).isEmpty() && state.getBlockHardness(world, pos) >= 0F && state.getBlockHardness(world, pos) <= hardness && DragonUtils.canDragonBreak(state.getBlock()) && this.canDestroyBlock(pos, state);
    }

    @Override
    public boolean isBlockExplicitlyPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        if (!isModelDead() && this.getDragonStage() >= 3) {
            if (IafConfig.dragonGriefing != 2 && (!this.isTamed() || IafConfig.tamedDragonGriefing) && pos.getY() >= this.getPosY()) {
                return isBreakable(pos, state, IafConfig.dragonGriefing == 1 || this.getDragonStage() <= 3 ? 2.0F : 5.0F);
            }
        }
        return false;
    }

    @Override
    public boolean isBlockExplicitlyNotPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    protected boolean isIceInWater() {
        return false;
    }

    public void spawnGroundEffects() {
        if (world.isRemote) {
            for (int i = 0; i < this.getRenderSize(); i++) {
                for (int i1 = 0; i1 < 20; i1++) {
                    final float radius = 0.75F * (0.7F * getRenderSize() / 3) * -3;
                    final float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                    final double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                    final double extraY = 0.8F;
                    final double extraZ = radius * MathHelper.cos(angle);
                    final BlockPos ground = getGround(new BlockPos(MathHelper.floor(this.getPosX() + extraX), MathHelper.floor(this.getPosY() + extraY) - 1, MathHelper.floor(this.getPosZ() + extraZ)));
                    final BlockState BlockState = this.world.getBlockState(ground);
                    if (BlockState.getMaterial() != Material.AIR) {
                        final double motionX = getRNG().nextGaussian() * 0.07D;
                        final double motionY = getRNG().nextGaussian() * 0.07D;
                        final double motionZ = getRNG().nextGaussian() * 0.07D;

                        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, BlockState), true, this.getPosX() + extraX, ground.getY() + extraY, this.getPosZ() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    private BlockPos getGround(BlockPos blockPos) {
        while (world.isAirBlock(blockPos) && blockPos.getY() > 1) {
            blockPos = blockPos.down();
        }
        return blockPos;
    }

    public void fall(float distance, float damageMultiplier) {

    }

    public boolean isActuallyBreathingFire() {
        return this.fireTicks > 20 && this.isBreathingFire();
    }

    public boolean doesWantToLand() {
        return this.flyTicks > 6000 || isGoingDown() || flyTicks > 40 && this.flyProgress == 0 || this.isChained() && flyTicks > 100;
    }

    public abstract String getVariantName(int variant);

    @Override
    public boolean shouldRiderSit() {
        return this.getControllingPassenger() != null;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            if (this.getControllingPassenger() == null || !this.getControllingPassenger().getUniqueID().equals(passenger.getUniqueID())) {
                updatePreyInMouth(passenger);
            } else {
                if (this.isModelDead()) {
                    passenger.stopRiding();
                }

                this.rotationYaw = passenger.rotationYaw;
                this.setRotationYawHead(passenger.getRotationYawHead());
                this.rotationPitch = passenger.rotationPitch;
                Vector3d riderPos = this.getRiderPosition();
                passenger.setPosition(riderPos.x, riderPos.y + passenger.getHeight(), riderPos.z);
            }
        }
    }

    private float bob(float speed, float degree, boolean bounce, float f, float f1) {
        final double a = MathHelper.sin(f * speed) * f1 * degree;
        float bob = (float) (a - f1 * degree);
        if (bounce) {
            bob = (float) -Math.abs(a);
        }
        return bob * this.getRenderSize() / 3;
    }

    protected void updatePreyInMouth(Entity prey) {
        if (this.getAnimation() != ANIMATION_SHAKEPREY) {
            this.setAnimation(ANIMATION_SHAKEPREY);
        }
        if (this.getAnimation() == ANIMATION_SHAKEPREY && this.getAnimationTick() > 55 && prey != null) {
            prey.attackEntityFrom(DamageSource.causeMobDamage(this), prey instanceof PlayerEntity ? 17F : (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 4);
            prey.stopRiding();
        }
        renderYawOffset = rotationYaw;
        final float modTick_0 = this.getAnimationTick() - 25;
        final float modTick_1 = this.getAnimationTick() > 25 && this.getAnimationTick() < 55 ? 8 * MathHelper.clamp(MathHelper.sin((float) (Math.PI + modTick_0 * 0.25)), -0.8F, 0.8F) : 0;
        final float modTick_2 = this.getAnimationTick() > 30 ? 10 : Math.max(0, this.getAnimationTick() - 20);
        final float radius = 0.75F * (0.6F * getRenderSize() / 3) * -3;
        final float angle = (0.01745329251F * this.renderYawOffset) + 3.15F + (modTick_1 * 2F) * 0.015F;
        final double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        final double extraZ = radius * MathHelper.cos(angle);
        final double extraY = modTick_2 == 0 ? 0 : 0.035F * ((getRenderSize() / 3) + (modTick_2 * 0.5 * (getRenderSize() / 3)));
        prey.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
    }

    public int getDragonStage() {
        final int age = this.getAgeInDays();
        if (age >= 100) {
            return 5;
        } else if (age >= 75) {
            return 4;
        } else if (age >= 50) {
            return 3;
        } else if (age >= 25) {
            return 2;
        } else {
            return 1;
        }
    }

    public boolean isTeen() {
        return getDragonStage() < 4 && getDragonStage() > 2;
    }

    @Override
    public boolean isAdult() {
        return getDragonStage() >= 4;
    }

    @Override
    public boolean isChild() {
        return getDragonStage() < 2;
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setGender(this.getRNG().nextBoolean());
        final int age = this.getRNG().nextInt(80) + 1;
        this.growDragon(age);
        this.setVariant(new Random().nextInt(4));
        this.setQueuedToSit(false);
        this.updateAttributes();
        final double healthStep = (maximumHealth - minimumHealth) / 125;
        this.heal((Math.round(minimumHealth + (healthStep * age))));
        this.usingGroundAttack = true;
        this.setHunger(50);
        return spawnDataIn;
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if (this.isModelDead() && dmg != DamageSource.OUT_OF_WORLD) {
            return false;
        }
        if (this.isBeingRidden() && dmg.getTrueSource() != null && this.getControllingPassenger() != null && dmg.getTrueSource() == this.getControllingPassenger()) {
            return false;
        }

        if ((dmg.damageType.contains("arrow") || getRidingEntity() != null && dmg.getTrueSource() != null && dmg.getTrueSource().isEntityEqual(this.getRidingEntity())) && this.isPassenger()) {
            return false;
        }

        if (dmg == DamageSource.IN_WALL || dmg == DamageSource.FALLING_BLOCK || dmg == DamageSource.CRAMMING) {
            return false;
        }
        if (!world.isRemote && dmg.getTrueSource() != null && this.getRNG().nextInt(4) == 0) {
            this.roar();
        }
        if (i > 0) {
            if (this.isSleeping()) {
                this.setQueuedToSit(false);
                if (!this.isTamed()) {
                    if (dmg.getTrueSource() instanceof PlayerEntity) {
                        this.setAttackTarget((PlayerEntity) dmg.getTrueSource());
                    }
                }
            }
        }
        return super.attackEntityFrom(dmg, i);

    }


    @Override
    public void recalculateSize() {
        super.recalculateSize();
        final float scale = Math.min(this.getRenderSize() * 0.35F, 7F);
//        double prevX = getPosX();
//        double prevY = getPosY();
//        double prevZ = getPosZ();
//        float localWidth = this.getWidth();
//        if (this.getWidth() > localWidth && !this.firstUpdate && !this.world.isRemote) {
//            this.setPosition(prevX, prevY, prevZ);
//        }
        if (scale != lastScale) {
            resetParts(this.getRenderSize() / 3);
        }
        lastScale = scale;
    }

    @Override
    public void tick() {
        super.tick();
        recalculateSize();
        updateParts();
        this.prevDragonPitch = getDragonPitch();
        world.getProfiler().startSection("dragonLogic");
        this.stepHeight = 1.2F;
        isOverAir = isOverAirLogic();
        logic.updateDragonCommon();
        if (this.isModelDead()) {
            if (!world.isRemote && world.isAirBlock(new BlockPos(this.getPosX(), this.getBoundingBox().minY, this.getPosZ())) && this.getPosY() > -1) {
                this.move(MoverType.SELF, new Vector3d(0, -0.2F, 0));
            }
            this.setBreathingFire(false);

            float dragonPitch = this.getDragonPitch();
            if (dragonPitch > 0) {
                dragonPitch = Math.min(0, dragonPitch - 5);
                this.setDragonPitch(dragonPitch);
            }
            if (dragonPitch < 0) {
                this.setDragonPitch(Math.max(0, dragonPitch + 5));
            }
        } else {
            if (world.isRemote) {
                logic.updateDragonClient();
            } else {
                logic.updateDragonServer();
                logic.updateDragonAttack();
            }
        }
        world.getProfiler().endSection();
        world.getProfiler().startSection("dragonFlight");
        // Update the flight mgr as needed
        if (useFlyingPathFinder() && !world.isRemote) {
            this.flightManager.update();
        }
        world.getProfiler().endSection();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.prevModelDeadProgress = this.modelDeadProgress;
        this.prevDiveProgress = this.diveProgress;
        prevAnimationProgresses[0] = this.sitProgress;
        prevAnimationProgresses[1] = this.sleepProgress;
        prevAnimationProgresses[2] = this.hoverProgress;
        prevAnimationProgresses[3] = this.flyProgress;
        prevAnimationProgresses[4] = this.fireBreathProgress;
        prevAnimationProgresses[5] = this.ridingProgress;
        prevAnimationProgresses[6] = this.tackleProgress;
        if (world.getDifficulty() == Difficulty.PEACEFUL && this.getAttackTarget() instanceof PlayerEntity) {
            this.setAttackTarget(null);
        }
        if (this.isModelDead()) {
            if (this.isBeingRidden()) {
                this.removePassengers();
            }

            this.setHovering(false);
            this.setFlying(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (animationTick > this.getAnimation().getDuration() && !world.isRemote) {
            animationTick = 0;
        }
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return this.getType().getSize().scale(this.getRenderScale());
    }

    @Override
    public float getRenderScale() {
        return Math.min(this.getRenderSize() * 0.35F, 7F);
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public float getRenderSize() {
        final int stage = this.getDragonStage() - 1;
        final float step = (growth_stages[stage][1] - growth_stages[stage][0]) / 25;
        if (this.getAgeInDays() > 125) {
            return growth_stages[stage][0] + (step * 25);
        }
        return growth_stages[stage][0] + (step * this.getAgeFactor());
    }

    private int getAgeFactor() {
        return (this.getDragonStage() > 1 ? this.getAgeInDays() - (25 * (this.getDragonStage() - 1)) : this.getAgeInDays());
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        this.getLookController().setLookPositionWithEntity(entityIn, 30.0F, 30.0F);
        if (this.isTackling() || this.isModelDead()) {
            return false;
        }

        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));

        if (flag) {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public void updateRidden() {
        Entity entity = this.getRidingEntity();
        if (this.isPassenger() && !entity.isAlive()) {
            this.stopRiding();
        } else {
            this.setMotion(0, 0, 0);
            this.tick();
            if (this.isPassenger()) {
                this.updateRiding(entity);
            }
        }
    }

    public void updateRiding(Entity riding) {
        if (riding != null && riding.isPassenger(this) && riding instanceof PlayerEntity) {
            final int i = riding.getPassengers().indexOf(this);
            final float radius = (i == 2 ? -0.2F : 0.5F) + (((PlayerEntity) riding).isElytraFlying() ? 2 : 0);
            final float angle = (0.01745329251F * ((PlayerEntity) riding).renderYawOffset) + (i == 1 ? 90 : i == 0 ? -90 : 0);
            final double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            final double extraZ = radius * MathHelper.cos(angle);
            final double extraY = (riding.isSneaking() ? 1.2D : 1.4D) + (i == 2 ? 0.4D : 0D);
            this.rotationYawHead = ((PlayerEntity) riding).rotationYawHead;
            this.prevRotationYaw = ((PlayerEntity) riding).rotationYawHead;
            this.setPosition(riding.getPosX() + extraX, riding.getPosY() + extraY, riding.getPosZ() + extraZ);
            if ((this.getControlState() == 1 << 4 || ((PlayerEntity) riding).isElytraFlying()) && !riding.isPassenger()) {
                this.stopRiding();
                if (world.isRemote) {
                    IceAndFire.sendMSGToServer(new MessageStartRidingMob(this.getEntityId(), false, true));
                }

            }

        }
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
        if (this.isModelDead()) {
            return NO_ANIMATION;
        }
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (this.isModelDead()) {
            return;
        }
        currentAnimation = animation;
    }

    @Override
    public void playAmbientSound() {
        if (!this.isSleeping() && !this.isModelDead() && !this.world.isRemote) {
            if (this.getAnimation() == this.NO_ANIMATION) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.playAmbientSound();
        }
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        if (!this.isModelDead()) {
            if (this.getAnimation() == this.NO_ANIMATION && !this.world.isRemote) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.playHurtSound(source);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT};
    }

    @Override
    public AgeableEntity createChild(ServerWorld serverWorld, AgeableEntity ageable) {
        return null;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        if (otherAnimal instanceof EntityDragonBase && otherAnimal != this && otherAnimal.getClass() == this.getClass()) {
            EntityDragonBase dragon = (EntityDragonBase) otherAnimal;
            return this.isMale() && !dragon.isMale() || !this.isMale() && dragon.isMale();
        }
        return false;
    }

    public EntityDragonEgg createEgg(EntityDragonBase ageable) {
        EntityDragonEgg dragon = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), this.world);
        dragon.setEggType(EnumDragonEgg.byMetadata(new Random().nextInt(4) + getStartMetaForType()));
        dragon.setPosition(MathHelper.floor(this.getPosX()) + 0.5, MathHelper.floor(this.getPosY()) + 1, MathHelper.floor(this.getPosZ()) + 0.5);
        return dragon;
    }

    public int getStartMetaForType() {
        return 0;
    }

    public boolean isTargetBlocked(Vector3d target) {
        if (target != null) {
            final BlockRayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(this.getPositionVec().add(0, this.getEyeHeight(), 0), target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            final BlockPos sidePos = rayTrace.getPos();
            if (!world.isAirBlock(sidePos)) {
                return true;
            } else if (!world.isAirBlock(new BlockPos(rayTrace.getHitVec()))) {
                return true;
            }
            return rayTrace.getType() == RayTraceResult.Type.BLOCK;
        }
        return false;
    }

    private double getFlySpeed() {
        return (2 + (this.getAgeInDays() / 125) * 2) * (this.isTackling() ? 2 : 1);
    }

    public boolean isTackling() {
        return this.dataManager.get(TACKLE);
    }

    public void setTackling(boolean tackling) {
        this.dataManager.set(TACKLE, tackling);
    }

    public boolean isAgingDisabled() {
        return this.dataManager.get(AGINGDISABLED);
    }

    public void setAgingDisabled(boolean isAgingDisabled) {
        this.dataManager.set(AGINGDISABLED, isAgingDisabled);
    }

    public boolean isBoundToCrystal() {
        return this.dataManager.get(CRYSTAL_BOUND);
    }

    public void setCrystalBound(boolean crystalBound) {
        this.dataManager.set(CRYSTAL_BOUND, crystalBound);
    }

    public float getDistanceSquared(Vector3d Vector3d) {
        final float f = (float) (this.getPosX() - Vector3d.x);
        final float f1 = (float) (this.getPosY() - Vector3d.y);
        final float f2 = (float) (this.getPosZ() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    public abstract Item getVariantScale(int variant);

    public abstract Item getVariantEgg(int variant);

    public abstract Item getSummoningCrystal();

    @Override
    public boolean canPassengerSteer() {
        return false;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    public boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F || isQueuedToSit() && !this.isBeingRidden() || this.isModelDead() || this.isPassenger();
    }

    @Override
    public void travel(Vector3d Vector3d) {
        if (this.getAnimation() == ANIMATION_SHAKEPREY || !this.canMove() && !this.isBeingRidden() || this.isQueuedToSit()) {
            if (this.getNavigator().getPath() != null) {
                this.getNavigator().clearPath();
            }
            Vector3d = new Vector3d(0, 0, 0);
        }
        super.travel(Vector3d);
    }

    @Override
    public void move(MoverType typeIn, Vector3d pos) {
        if (this.isQueuedToSit() && !this.isBeingRidden()) {
            pos = new Vector3d(0, pos.getY(), 0);
        }
        super.move(typeIn, pos);

    }

    public void updateCheckPlayer() {
        final double checkLength = this.getBoundingBox().getAverageEdgeLength() * 3;
        final PlayerEntity player = world.getClosestPlayer(this, checkLength);
        if (this.isSleeping()) {
            if (player != null && !this.isOwner(player) && !player.isCreative()) {
                this.setQueuedToSit(false);
                this.setSitting(false);
                this.setAttackTarget(player);
            }
        }
    }

    public boolean isDirectPathBetweenPoints(Vector3d vec1, Vector3d vec2) {
        final BlockRayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(vec1, new Vector3d(vec2.x, vec2.y + (double) this.getHeight() * 0.5D, vec2.z), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        return rayTrace.getType() != RayTraceResult.Type.BLOCK;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }

    @Override
    public void onHearFlute(PlayerEntity player) {
        if (this.isTamed() && this.isOwner(player)) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
    }

    public abstract SoundEvent getRoarSound();

    public void roar() {
        if (EntityGorgon.isStoneMob(this) || this.isModelDead()) {
            return;
        }
        if (rand.nextBoolean()) {
            if (this.getAnimation() != ANIMATION_EPIC_ROAR) {
                this.setAnimation(ANIMATION_EPIC_ROAR);
                this.playSound(this.getRoarSound(), this.getSoundVolume() + 3 + Math.max(0, this.getDragonStage() - 2), this.getSoundPitch() * 0.7F);
            }
            if (this.getDragonStage() > 3) {
                final int size = (this.getDragonStage() - 3) * 30;
                final List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(size, size, size));
                for (final Entity entity : entities) {
                    final boolean isStrongerDragon = entity instanceof EntityDragonBase && ((EntityDragonBase) entity).getDragonStage() >= this.getDragonStage();
                    if (entity instanceof LivingEntity && !isStrongerDragon) {
                        LivingEntity living = (LivingEntity) entity;
                        if (this.isOwner(living) || this.isOwnersPet(living)) {
                            living.addPotionEffect(new EffectInstance(Effects.STRENGTH, 50 * size));
                        } else {
                            if (living.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() != IafItemRegistry.EARPLUGS) {
                                living.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 50 * size));
                            }
                        }
                    }
                }
            }
        } else {
            if (this.getAnimation() != ANIMATION_ROAR) {
                this.setAnimation(ANIMATION_ROAR);
                this.playSound(this.getRoarSound(), this.getSoundVolume() + 2 + Math.max(0, this.getDragonStage() - 3), this.getSoundPitch());
            }
            if (this.getDragonStage() > 3) {
                final int size = (this.getDragonStage() - 3) * 30;
                final List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(size, size, size));
                for (final Entity entity : entities) {
                    final boolean isStrongerDragon = entity instanceof EntityDragonBase && ((EntityDragonBase) entity).getDragonStage() >= this.getDragonStage();
                    if (entity instanceof LivingEntity && !isStrongerDragon) {
                        LivingEntity living = (LivingEntity) entity;
                        if (this.isOwner(living) || this.isOwnersPet(living)) {
                            living.addPotionEffect(new EffectInstance(Effects.STRENGTH, 30 * size));
                        } else {
                            living.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 30 * size));
                        }
                    }
                }
            }
        }
    }

    private boolean isOwnersPet(LivingEntity living) {
        return this.isTamed() && this.getOwner() != null && living instanceof TameableEntity && ((TameableEntity) living).getOwner() != null && this.getOwner().isEntityEqual(((TameableEntity) living).getOwner());
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vector3d vec1, Vector3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(new RayTraceContext(vec1, vec2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        return movingobjectposition.getType() != RayTraceResult.Type.BLOCK;
    }

    public boolean shouldRenderEyes() {
        return !this.isSleeping() && !this.isModelDead() && !this.isBlinking() && !EntityGorgon.isStoneMob(this);
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    @Override
    public void dropArmor() {

    }

    public boolean isChained() {
        return ChainProperties.hasChainData(this);
    }

    @Override
    protected void dropLoot(DamageSource damageSourceIn, boolean attackedRecently) {
    }

    public RayTraceResult rayTraceRider(Entity rider, double blockReachDistance, float partialTicks) {
        Vector3d Vector3d = rider.getEyePosition(partialTicks);
        Vector3d Vector3d1 = rider.getLook(partialTicks);
        Vector3d Vector3d2 = Vector3d.add(Vector3d1.x * blockReachDistance, Vector3d1.y * blockReachDistance, Vector3d1.z * blockReachDistance);
        return this.world.rayTraceBlocks(new RayTraceContext(Vector3d, Vector3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
    }

    public Vector3d getRiderPosition() {
        final float sitProg = this.sitProgress * 0.015F;
        final float deadProg = this.modelDeadProgress * -0.02F;
        final float hoverProg = this.hoverProgress * 0.03F;
        final float flyProg = this.flyProgress * 0.01F;
        final float sleepProg = this.sleepProgress * -0.025F;
        final float extraAgeScale = this.getRenderScale() * 0.2F;
        float pitchX = 0F;
        float pitchY = 0F;
        final float dragonPitch = getDragonPitch();
        if (dragonPitch > 0) {
            pitchX = Math.min(dragonPitch / 90, 0.3F);
            pitchY = -(dragonPitch / 90) * 2F;
        } else if (dragonPitch < 0) {//going up
            pitchY = (dragonPitch / 90) * 0.1F;
            pitchX = Math.max(dragonPitch / 90, -0.7F);
        }
        final float xzMod = (0.15F + pitchX) * getRenderSize() + extraAgeScale;
        final float headPosX = (float) (getPosX() + (xzMod) * MathHelper.cos((float) ((rotationYaw + 90) * Math.PI / 180)));
        final float headPosY = (float) (getPosY() + (0.7F + sitProg + hoverProg + deadProg + sleepProg + flyProg + pitchY) * getRenderSize() * 0.3F + extraAgeScale);
        final float headPosZ = (float) (getPosZ() + (xzMod) * MathHelper.sin((float) ((rotationYaw + 90) * Math.PI / 180)));
        return new Vector3d(headPosX, headPosY, headPosZ);
    }

    @Override
    public void onKillCommand() {
        this.remove();
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        // Workaround to make sure dragons won't be attacked when dead
        if (this.isModelDead())
            return true;
        if (this.isTamed()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TameableEntity) {
                return ((TameableEntity) entityIn).isOwner(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isOnSameTeam(entityIn);
            }
        }

        return super.isOnSameTeam(entityIn);
    }

    public Vector3d getHeadPosition() {
        final float sitProg = this.sitProgress * 0.015F;
        final float deadProg = this.modelDeadProgress * -0.02F;
        final float hoverProg = this.hoverProgress * 0.03F;
        final float flyProg = this.flyProgress * 0.01F;
        int tick = 0;
        if (this.getAnimationTick() < 10) {
            tick = this.getAnimationTick();
        } else if (this.getAnimationTick() > 50) {
            tick = 60 - this.getAnimationTick();
        } else {
            tick = 10;
        }
        final float epicRoarProg = this.getAnimation() == ANIMATION_EPIC_ROAR ? tick * 0.1F : 0;
        final float sleepProg = this.sleepProgress * -0.025F;
        float pitchMulti = 0F;
        float pitchAdjustment = 0F;
        float pitchMinus = 0F;
        final float dragonPitch = -getDragonPitch();
        if (this.isFlying() || this.isHovering()) {
            pitchMulti = MathHelper.sin((float) Math.toRadians(dragonPitch));
            pitchAdjustment = 1.2F;
            pitchMulti *= 2.1F * Math.abs(dragonPitch) / 90;
            if (pitchMulti > 0) {
                pitchMulti *= 1.5F - pitchMulti * 0.5F;
            }
            if (pitchMulti < 0) {
                pitchMulti *= 1.3F - pitchMulti * 0.1F;
            }
            pitchMinus = 0.3F * Math.abs(dragonPitch / 90);
            if (dragonPitch >= 0) {
                pitchAdjustment = 0.6F * Math.abs(dragonPitch / 90);
                pitchMinus = 0.95F * Math.abs(dragonPitch / 90);
            }
        }
        final float flightXz = 1.0F + flyProg + hoverProg;
        final float xzMod = (1.7F * getRenderSize() * 0.3F * flightXz) + getRenderSize() * (0.3F * MathHelper.sin((float) ((dragonPitch + 90) * Math.PI / 180)) * pitchAdjustment - pitchMinus - hoverProg * 0.45F);
        final float headPosX = (float) (getPosX() + (xzMod) * MathHelper.cos((float) ((rotationYaw + 90) * Math.PI / 180)));
        final float headPosY = (float) (getPosY() + (0.7F + sitProg + hoverProg + deadProg + epicRoarProg + sleepProg + flyProg + pitchMulti) * getRenderSize() * 0.3F);
        final float headPosZ = (float) (getPosZ() + (xzMod) * MathHelper.sin((float) ((rotationYaw + 90) * Math.PI / 180)));
        return new Vector3d(headPosX, headPosY, headPosZ);
    }

    public abstract void stimulateFire(double burnX, double burnY, double burnZ, int syncType);

    public void randomizeAttacks() {
        this.airAttack = IafDragonAttacks.Air.values()[getRNG().nextInt(IafDragonAttacks.Air.values().length)];
        this.groundAttack = IafDragonAttacks.Ground.values()[getRNG().nextInt(IafDragonAttacks.Ground.values().length)];

    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosionIn, IBlockReader worldIn, BlockPos pos, BlockState blockStateIn, float explosionPower) {
        return !(blockStateIn.getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(blockStateIn.getBlock());
    }

    public void tryScorchTarget() {
        LivingEntity entity = this.getAttackTarget();
        if (entity != null) {
            final float distX = (float) (entity.getPosX() - this.getPosX());
            final float distZ = (float) (entity.getPosZ() - this.getPosZ());
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    rotationYaw = renderYawOffset;
                    if (this.ticksExisted % 5 == 0) {
                        this.playSound(IafSoundRegistry.FIREDRAGON_BREATH, 4, 1);
                    }
                    stimulateFire(this.getPosX() + distX * this.fireTicks / 40, entity.getPosY(), this.getPosZ() + distZ * this.fireTicks / 40, 1);
                }
            } else {
                this.setBreathingFire(true);
            }
        }
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity LivingEntityIn) {
        super.setAttackTarget(LivingEntityIn);
        this.flightManager.onSetAttackTarget(LivingEntityIn);
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (this.isTamed() && target instanceof TameableEntity) {
            TameableEntity tamableTarget = (TameableEntity) target;
            UUID targetOwner = tamableTarget.getOwnerId();
            if (targetOwner != null && targetOwner.equals(this.getOwnerId())) {
                return false;
            }
        }
        return super.shouldAttackEntity(target, owner);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && DragonUtils.isAlive(target);
    }

    public boolean isPart(Entity entityHit) {
        return headPart != null && headPart.isEntityEqual(entityHit) || neckPart != null && neckPart.isEntityEqual(entityHit) ||
                leftWingLowerPart != null && leftWingLowerPart.isEntityEqual(entityHit) || rightWingLowerPart != null && rightWingLowerPart.isEntityEqual(entityHit) ||
                leftWingUpperPart != null && leftWingUpperPart.isEntityEqual(entityHit) || rightWingUpperPart != null && rightWingUpperPart.isEntityEqual(entityHit) ||
                tail1Part != null && tail1Part.isEntityEqual(entityHit) || tail2Part != null && tail2Part.isEntityEqual(entityHit) ||
                tail3Part != null && tail3Part.isEntityEqual(entityHit) || tail4Part != null && tail4Part.isEntityEqual(entityHit);
    }

    @Override
    public double getFlightSpeedModifier() {
        return IafConfig.dragonFlightSpeedMod;
    }

    public boolean isAllowedToTriggerFlight() {
        return (this.hasFlightClearance() && this.onGround || this.isInWater()) && !this.isQueuedToSit() && this.getPassengers().isEmpty() && !this.isChild() && !this.isSleeping() && this.canMove();
    }

    public BlockPos getEscortPosition() {
        return this.getOwner() != null ? new BlockPos(this.getOwner().getPositionVec()) : this.getPosition();
    }

    public boolean shouldTPtoOwner() {
        return this.getOwner() != null && this.getDistance(this.getOwner()) > 10;
    }

    public boolean isSkeletal() {
        return this.getDeathStage() >= (this.getAgeInDays() / 5) / 2;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean writeUnlessPassenger(CompoundNBT compound) {
        return this.writeUnlessRemoved(compound);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (soundIn == SoundEvents.ENTITY_GENERIC_EAT || soundIn == this.getAmbientSound() || soundIn == this.getHurtSound(null) || soundIn == this.getDeathSound() || soundIn == this.getRoarSound()) {
            if (!this.isSilent() && this.headPart != null) {
                this.world.playSound(null, this.headPart.getPosX(), this.headPart.getPosY(), this.headPart.getPosZ(), soundIn, this.getSoundCategory(), volume, pitch);
            }
        } else {
            super.playSound(soundIn, volume, pitch);
        }
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public boolean hasFlightClearance() {
        BlockPos topOfBB = new BlockPos(this.getPosX(), this.getBoundingBox().maxY, this.getPosZ());
        for (int i = 1; i < 4; i++) {
            if (!world.isAirBlock(topOfBB.up(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItemStackFromSlot(final EquipmentSlotType slotIn) {
        switch (slotIn) {
            case OFFHAND:
                return inventory.getStackInSlot(0);
            case HEAD:
                return inventory.getStackInSlot(1);
            case CHEST:
                return inventory.getStackInSlot(2);
            case LEGS:
                return inventory.getStackInSlot(3);
            case FEET:
                return inventory.getStackInSlot(4);
            default:
                return super.getItemStackFromSlot(slotIn);
        }
    }

    @Override
    public void setItemStackToSlot(final EquipmentSlotType slotIn, final ItemStack stack) {
        switch (slotIn) {
            case OFFHAND:
                inventory.setInventorySlotContents(0, stack);
                break;
            case HEAD:
                inventory.setInventorySlotContents(1, stack);
                break;
            case CHEST:
                inventory.setInventorySlotContents(2, stack);
                break;
            case LEGS:
                inventory.setInventorySlotContents(3, stack);
                break;
            case FEET:
                inventory.setInventorySlotContents(4, stack);
                break;
            default:
                super.getItemStackFromSlot(slotIn);
        }
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch();
    }

    public SoundEvent getBabyFireSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    protected boolean isPlayingAttackAnimation() {
        return this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_SHAKEPREY || this.getAnimation() == ANIMATION_WINGBLAST ||
                this.getAnimation() == ANIMATION_TAILWHACK;
    }

    protected IafDragonLogic createDragonLogic() {
        return new IafDragonLogic(this);
    }

    protected int getFlightChancePerTick() {
        return FLIGHT_CHANCE_PER_TICK;
    }

    @Override
    public void onRemovedFromWorld() {
        if (IafConfig.chunkLoadSummonCrystal) {
            if (this.isBoundToCrystal()) {
                DragonPosWorldData data = DragonPosWorldData.get(world);
                if (data != null) {
                    data.addDragon(this.getUniqueID(), this.getPosition());
                }
            }
        }
        super.onRemovedFromWorld();
    }

    @Override
    public int maxSearchNodes() {
        return (int) this.getAttribute(Attributes.FOLLOW_RANGE).getValue();
    }

    @Override
    public boolean isSmallerThanBlock() {
        return false;
    }

    @Override
    public float getXZNavSize() {
        return Math.max(1.4F, this.getWidth() / 2.0F);
    }

    @Override
    public int getYNavSize() {
        return MathHelper.ceil(this.getHeight());
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        if (!this.world.isRemote) {
            updateAttributes();
        }
    }
}
