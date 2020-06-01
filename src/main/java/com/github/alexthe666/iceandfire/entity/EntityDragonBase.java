package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.client.model.util.LegSolverQuadruped;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.item.ItemSummoningCrystal;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.message.MessageDragonSetBurnBlock;
import com.github.alexthe666.iceandfire.message.MessageStartRidingMob;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDragon;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class EntityDragonBase extends EntityTameable implements ISyncMount, IFlyingMount, IMultipartEntity, IAnimatedEntity, IDragonFlute, IDeadMob, IVillagerFear, IAnimalFear, IDropArmor {

    public static final int FLIGHT_CHANCE_PER_TICK = 1500;
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
    public float ridingProgress;
    public float tackleProgress;
    public boolean isDaytime;
    public int flightCycle;
    public BlockPos homePos;
    public boolean hasHomePosition = false;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer roll_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer pitch_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer pitch_buffer_body;
    @SideOnly(Side.CLIENT)
    public ReversedBuffer turn_buffer;
    @SideOnly(Side.CLIENT)
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
    public InventoryBasic dragonInventory;
    public String prevArmorResLoc = "0|0|0|0";
    public String armorResLoc = "0|0|0|0";
    protected int flyHovering;
    protected IafDragonFlightManager flightManager;
    protected boolean hasHadHornUse = false;
    protected int fireTicks;
    protected int blockBreakCounter;
    private int prevFlightCycle;
    private boolean isSleeping;
    private boolean isSitting;
    private boolean isHovering;
    private boolean isFlying;
    private boolean isBreathingFire;
    private boolean isTackling;
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

    public EntityDragonBase(World world, DragonType type, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
        super(world);
        initInventory();
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
        updateAttributes();
        if (FMLCommonHandler.instance().getSide().isClient()) {
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

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new DragonAIRide<>(this));
        this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(2, new DragonAIMate(this, 1.0D));
        this.tasks.addTask(3, new DragonAIEscort(this, 1.0D));
        this.tasks.addTask(4, new DragonAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(5, new AquaticAITempt(this, 1.0D, IafItemRegistry.fire_stew, false));
        this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
        this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(7, new DragonAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new DragonAITargetNonTamed<>(this, EntityPlayer.class, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return DragonUtils.canHostilesTarget(entity) && !entity.isCreative();
            }
        }));
        this.targetTasks.addTask(5, new DragonAITarget<>(this, EntityLivingBase.class, true, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.targetTasks.addTask(6, new DragonAITargetItems(this, false));
    }

    public void resetParts(float scale) {
        removeParts();
        headPart = new EntityDragonPart(this, 1.55F * scale, 0, 0.6F * scale, 0.5F * scale, 0.35F * scale, 1.5F);
        neckPart = new EntityDragonPart(this, 0.85F * scale, 0, 0.7F * scale, 0.5F * scale, 0.2F * scale, 1);
        rightWingUpperPart = new EntityDragonPart(this, 1F * scale, 90, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        rightWingLowerPart = new EntityDragonPart(this, 1.4F * scale, 100, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        leftWingUpperPart = new EntityDragonPart(this, 1F * scale, -90, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        leftWingLowerPart = new EntityDragonPart(this, 1.4F * scale, -100, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        tail1Part = new EntityDragonPart(this, -0.75F * scale, 0, 0.6F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail2Part = new EntityDragonPart(this, -1.15F * scale, 0, 0.45F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail3Part = new EntityDragonPart(this, -1.5F * scale, 0, 0.35F * scale, 0.35F * scale, 0.35F * scale, 1);
        tail4Part = new EntityDragonPart(this, -1.95F * scale, 0, 0.25F * scale, 0.45F * scale, 0.3F * scale, 1.5F);
    }

    public void removeParts() {
        if (headPart != null) {
            world.removeEntityDangerously(headPart);
            headPart = null;
        }
        if (neckPart != null) {
            world.removeEntityDangerously(neckPart);
            neckPart = null;
        }
        if (rightWingUpperPart != null) {
            world.removeEntityDangerously(rightWingUpperPart);
            rightWingUpperPart = null;
        }
        if (rightWingLowerPart != null) {
            world.removeEntityDangerously(rightWingLowerPart);
            rightWingLowerPart = null;
        }
        if (leftWingUpperPart != null) {
            world.removeEntityDangerously(leftWingUpperPart);
            leftWingUpperPart = null;
        }
        if (leftWingLowerPart != null) {
            world.removeEntityDangerously(leftWingLowerPart);
            leftWingLowerPart = null;
        }
        if (tail1Part != null) {
            world.removeEntityDangerously(tail1Part);
            tail1Part = null;
        }
        if (tail2Part != null) {
            world.removeEntityDangerously(tail2Part);
            tail2Part = null;
        }
        if (tail3Part != null) {
            world.removeEntityDangerously(tail3Part);
            tail3Part = null;
        }
        if (tail4Part != null) {
            world.removeEntityDangerously(tail4Part);
            tail4Part = null;
        }
    }

    public void updateParts() {
        if (headPart != null) {
            headPart.onUpdate();
        }
        if (neckPart != null) {
            neckPart.onUpdate();
        }
        if (rightWingUpperPart != null) {
            rightWingUpperPart.onUpdate();
        }
        if (rightWingLowerPart != null) {
            rightWingLowerPart.onUpdate();
        }
        if (leftWingUpperPart != null) {
            leftWingUpperPart.onUpdate();
        }
        if (leftWingLowerPart != null) {
            leftWingLowerPart.onUpdate();
        }
        if (tail1Part != null) {
            tail1Part.onUpdate();
        }
        if (tail2Part != null) {
            tail2Part.onUpdate();
        }
        if (tail3Part != null) {
            tail3Part.onUpdate();
        }
        if (tail4Part != null) {
            tail4Part.onUpdate();
        }
    }

    protected void updateBurnTarget() {
        if (burningTarget != null && !this.isSleeping() && !this.isModelDead() && !this.isChild()) {
            if (world.getTileEntity(burningTarget) != null && world.getTileEntity(burningTarget) instanceof TileEntityDragonforgeInput && this.getDistanceSq(burningTarget) < 300) {
                this.getLookHelper().setLookPosition(burningTarget.getX() + 0.5D, burningTarget.getY() + 0.5D, burningTarget.getZ() + 0.5D, 180F, 180F);
                this.breathFireAtPos(burningTarget);
                this.setBreathingFire(true);
            } else {
                if (!world.isRemote) {
                    IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDragonSetBurnBlock(this.getEntityId(), true, burningTarget));
                }
                burningTarget = null;
            }
        }
    }

    protected abstract void breathFireAtPos(BlockPos burningTarget);

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateDragon(this, world);
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveHelper = new IafDragonFlightManager.GroundMoveHelper(this);
            this.navigator = createNavigator(world);
            this.navigatorType = 0;
        } else if (navigatorType == 1) {
            this.moveHelper = new IafDragonFlightManager.FlightMoveHelper(this);
            this.navigator = new PathNavigateFlyingCreature(this, world);
            this.navigatorType = 1;
        } else {
            this.moveHelper = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
            this.navigator = new PathNavigateFlyingCreature(this, world);
            this.navigatorType = 2;
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
        breakBlock();
    }

    public boolean canDestroyBlock(BlockPos pos) {
        return world.getBlockState(pos).getBlock().canEntityDestroy(world.getBlockState(pos), world, pos, this);
    }

    public boolean isMobDead() {
        return this.isModelDead();
    }

    public int getHorizontalFaceSpeed() {
        return 10 * this.getDragonStage() / 5;
    }

    public void openGUI(EntityPlayer playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            playerEntity.openGui(IceAndFire.INSTANCE, 0, this.world, this.getEntityId(), 0, 0);
        }
    }

    public int getTalkInterval() {
        return 90;
    }

    protected void onDeathUpdate() {
        this.deathTime = 0;
        if (!this.isModelDead()) {
            if (!this.world.isRemote && this.recentlyHit > 0) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
        }
        this.setModelDead(true);

        if (this.getDeathStage() >= this.getAgeInDays() / 5) {
            this.setDead();
            for (int k = 0; k < 40; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
                }
            }
            spawnDeathParticles();
        }
    }

    protected void spawnDeathParticles() {
    }

    protected void spawnBabyParticles() {
    }

    public void setDead() {
        removeParts();
        super.setDead();
    }

    protected int getExperiencePoints(EntityPlayer player) {
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
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_iron) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_gold) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_diamond) {
            return 3;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_silver) {
            return 4;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_dragonsteel_fire) {
            return 5;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.dragon_armor_dragonsteel_ice) {
            return 6;
        }
        return 0;
    }

    @Override
    public boolean isAIDisabled() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        return this.isModelDead() || properties == null || properties.isStone || super.isAIDisabled();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HUNGER, Integer.valueOf(0));
        this.dataManager.register(AGE_TICKS, Integer.valueOf(0));
        this.dataManager.register(GENDER, Boolean.valueOf(false));
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SLEEPING, Boolean.valueOf(false));
        this.dataManager.register(FIREBREATHING, Boolean.valueOf(false));
        this.dataManager.register(HOVERING, Boolean.valueOf(false));
        this.dataManager.register(FLYING, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STAGE, Integer.valueOf(0));
        this.dataManager.register(MODEL_DEAD, Boolean.valueOf(false));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.dataManager.register(TACKLE, Boolean.valueOf(false));
        this.dataManager.register(AGINGDISABLED, Boolean.valueOf(false));
        this.dataManager.register(COMMAND, Integer.valueOf(0));
        this.dataManager.register(DRAGON_PITCH, Float.valueOf(0));
        this.dataManager.register(CRYSTAL_BOUND, Boolean.valueOf(false));
    }

    public boolean up() {
        return (dataManager.get(CONTROL_STATE).byteValue() & 1) == 1;
    }

    public boolean down() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 2 & 1) == 1;
    }

    public boolean strike() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
    }

    public boolean dismount() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 4 & 1) == 1;
    }

    public void up(boolean up) {
        setStateField(0, up);
    }

    public void down(boolean down) {
        setStateField(1, down);
    }

    public void attack(boolean attack) {
        setStateField(2, attack);
    }

    public void strike(boolean strike) {
        setStateField(3, strike);
    }

    public void dismount(boolean dismount) {
        setStateField(4, dismount);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = dataManager.get(CONTROL_STATE).byteValue();
        if (newState) {
            dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getControlState() {
        return dataManager.get(CONTROL_STATE).byteValue();
    }

    public void setControlState(byte state) {
        dataManager.set(CONTROL_STATE, state);
    }

    public int getCommand() {
        return Integer.valueOf(this.dataManager.get(COMMAND).intValue());
    }

    public void setCommand(int command) {
        this.dataManager.set(COMMAND, Integer.valueOf(command));
        if (command == 1) {
            this.setSitting(true);
        } else {
            this.setSitting(false);
        }
    }

    public float getDragonPitch() {
        return dataManager.get(DRAGON_PITCH).floatValue();
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
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Hunger", this.getHunger());
        compound.setInteger("AgeTicks", this.getAgeInTicks());
        compound.setBoolean("Gender", this.isMale());
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Sleeping", this.isSleeping());
        compound.setBoolean("TamedDragon", this.isTamed());
        compound.setBoolean("FireBreathing", this.isBreathingFire());
        compound.setBoolean("AttackDecision", usingGroundAttack);
        compound.setBoolean("Hovering", this.isHovering());
        compound.setBoolean("Flying", this.isFlying());
        compound.setInteger("DeathStage", this.getDeathStage());
        compound.setBoolean("ModelDead", this.isModelDead());
        compound.setFloat("DeadProg", this.modelDeadProgress);
        compound.setBoolean("Tackle", this.isTackling());
        if (this.getCustomNameTag() != null && !this.getCustomNameTag().isEmpty()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }
        compound.setBoolean("HasHomePosition", this.hasHomePosition);
        if (homePos != null && this.hasHomePosition) {
            compound.setInteger("HomeAreaX", homePos.getX());
            compound.setInteger("HomeAreaY", homePos.getY());
            compound.setInteger("HomeAreaZ", homePos.getZ());
        }
        compound.setBoolean("AgingDisabled", this.isAgingDisabled());
        compound.setInteger("Command", this.getCommand());
        if (dragonInventory != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < dragonInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = dragonInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            compound.setTag("Items", nbttaglist);
        }
        compound.setBoolean("CrystalBound", this.isBoundToCrystal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setHunger(compound.getInteger("Hunger"));
        this.setAgeInTicks(compound.getInteger("AgeTicks"));
        this.setGender(compound.getBoolean("Gender"));
        this.setVariant(compound.getInteger("Variant"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setTamed(compound.getBoolean("TamedDragon"));
        this.setBreathingFire(compound.getBoolean("FireBreathing"));
        this.usingGroundAttack = compound.getBoolean("AttackDecision");
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setDeathStage(compound.getInteger("DeathStage"));
        this.setModelDead(compound.getBoolean("ModelDead"));
        this.modelDeadProgress = compound.getFloat("DeadProg");
        if (!compound.getString("CustomName").isEmpty()) {
            this.setCustomNameTag(compound.getString("CustomName"));
        }
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInteger("HomeAreaX") != 0 && compound.getInteger("HomeAreaY") != 0 && compound.getInteger("HomeAreaZ") != 0) {
            homePos = new BlockPos(compound.getInteger("HomeAreaX"), compound.getInteger("HomeAreaY"), compound.getInteger("HomeAreaZ"));
        }
        this.setTackling(compound.getBoolean("Tackle"));
        this.setAgingDisabled(compound.getBoolean("AgingDisabled"));
        this.setCommand(compound.getInteger("Command"));
        if (dragonInventory != null) {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                if (j <= 4) {
                    dragonInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                }
            }
        } else {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initInventory();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                dragonInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
        this.setCrystalBound(compound.getBoolean("CrystalBound"));
    }

    private void initInventory() {
        dragonInventory = new InventoryBasic("dragonInventory", false, 5);
        dragonInventory.setCustomName(this.getName());
        if (dragonInventory != null) {
            for (int j = 0; j < dragonInventory.getSizeInventory(); ++j) {
                ItemStack itemstack = dragonInventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    dragonInventory.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof EntityPlayer && this.getAttackTarget() != passenger) {
                EntityPlayer player = (EntityPlayer) passenger;
                if (this.isTamed() && this.getOwnerId() != null && this.getOwnerId().equals(player.getUniqueID())) {
                    return player;
                }
            }
        }
        return null;
    }

    public boolean isRidingPlayer(EntityPlayer player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Nullable
    public EntityPlayer getRidingPlayer() {
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            return (EntityPlayer) this.getControllingPassenger();
        }
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.dragonTargetSearchLength));
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);

    }

    protected void updateAttributes() {
        prevArmorResLoc = armorResLoc;
        int armorHead = this.getArmorOrdinal(this.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        int armorNeck = this.getArmorOrdinal(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        int armorLegs = this.getArmorOrdinal(this.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
        int armorFeet = this.getArmorOrdinal(this.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        armorResLoc = dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
        IceAndFire.PROXY.updateDragonArmorRender(armorResLoc);
        double healthStep = (maximumHealth - minimumHealth) / (125);
        double attackStep = (maximumDamage - minimumDamage) / (125);
        double speedStep = (maximumSpeed - minimumSpeed) / (125);
        double armorStep = (maximumArmor - minimumArmor) / (125);
        if (this.getAgeInDays() <= 125) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.round(minimumHealth + (healthStep * this.getAgeInDays())));
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.round(minimumDamage + (attackStep * this.getAgeInDays())));
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(minimumSpeed + (speedStep * this.getAgeInDays()));
            double oldValue = minimumArmor + (armorStep * this.getAgeInDays());
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(oldValue + calculateArmorModifier());
        }
    }

    public int getHunger() {
        return this.dataManager.get(HUNGER).intValue();
    }

    public void setHunger(int hunger) {
        this.dataManager.set(HUNGER, MathHelper.clamp(hunger, 0, 100));
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public int getAgeInDays() {
        return this.dataManager.get(AGE_TICKS).intValue() / 24000;
    }

    public void setAgeInDays(int age) {
        this.dataManager.set(AGE_TICKS, age * 24000);
    }

    public int getAgeInTicks() {
        return this.dataManager.get(AGE_TICKS).intValue();
    }

    public void setAgeInTicks(int age) {
        this.dataManager.set(AGE_TICKS, age);
    }

    public int getDeathStage() {
        return this.dataManager.get(DEATH_STAGE).intValue();
    }

    public void setDeathStage(int stage) {
        this.dataManager.set(DEATH_STAGE, stage);
    }

    public boolean isMale() {
        return this.dataManager.get(GENDER).booleanValue();
    }

    public boolean isModelDead() {
        if (world.isRemote) {
            return this.isModelDead = Boolean.valueOf(this.dataManager.get(MODEL_DEAD).booleanValue());
        }
        return isModelDead;
    }

    public void setModelDead(boolean modeldead) {
        this.dataManager.set(MODEL_DEAD, modeldead);
        if (!world.isRemote) {
            this.isModelDead = modeldead;
        }
    }

    public boolean isHovering() {
        if (world.isRemote) {
            return this.isHovering = this.dataManager.get(HOVERING).booleanValue();
        }
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        this.dataManager.set(HOVERING, hovering);
        if (!world.isRemote) {
            this.isHovering = hovering;
        }
    }

    public boolean isFlying() {
        if (world.isRemote) {
            return this.isFlying = this.dataManager.get(FLYING).booleanValue();
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
        if (!world.isRemote) {
            this.isFlying = flying;
        }
    }

    public boolean useFlyingPathFinder() {
        return isFlying();
    }

    public void setGender(boolean male) {
        this.dataManager.set(GENDER, male);
    }

    public boolean isSleeping() {
        if (world.isRemote) {
            boolean isSleeping = this.dataManager.get(SLEEPING).booleanValue();
            this.isSleeping = isSleeping;
            return isSleeping;
        }
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.dataManager.set(SLEEPING, sleeping);
        if (!world.isRemote) {
            this.isSleeping = sleeping;
        }
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    public boolean isBreathingFire() {
        if (world.isRemote) {
            boolean breathing = this.dataManager.get(FIREBREATHING).booleanValue();
            this.isBreathingFire = breathing;
            return breathing;
        }
        return isBreathingFire;
    }

    public void setBreathingFire(boolean breathing) {
        this.dataManager.set(FIREBREATHING, breathing);
        if (!world.isRemote) {
            this.isBreathingFire = breathing;
        }
    }

    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    public boolean isSitting() {
        if (world.isRemote) {
            boolean isSitting = (this.dataManager.get(TAMED).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    @Override
    public void setSitting(boolean sitting) {
        if (!world.isRemote) {
            this.isSitting = sitting;
        }
        byte b0 = this.dataManager.get(TAMED).byteValue();
        if (sitting) {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataManager.set(TAMED, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public void riderShootFire(Entity controller) {
    }

    @Override
    public void onKillEntity(EntityLivingBase entity) {
        super.onKillEntity(entity);
        this.setHunger(this.getHunger() + FoodUtils.getFoodPoints(entity));
    }

    private double calculateArmorModifier() {
        double val = 1D;
        EntityEquipmentSlot[] slots = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
        for (EntityEquipmentSlot slot : slots) {
            switch (getArmorOrdinal(getItemStackFromSlot(slot))) {
                case 1:
                    val += 2D;
                    break;
                case 2:
                    val += 3D;
                    break;
                case 3:
                    val += 5D;
                    break;
                case 4:
                    val += 3D;
                    break;
                case 5:
                    val += 10D;
                    break;
                case 6:
                    val += 10D;
                    break;
            }
        }
        return val;
    }

    public boolean canMove() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (properties != null && properties.isStone) {
            return false;
        }
        return !this.isSitting() && !this.isSleeping() && this.getControllingPassenger() == null && !this.isModelDead() && sleepProgress == 0 && this.getAnimation() != ANIMATION_SHAKEPREY;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == IafItemRegistry.dragon_horn) {
            return false;
        }
        int lastDeathStage = this.getAgeInDays() / 5;
        if (stack.getItem() == IafItemRegistry.dragon_debug_stick) {
            logic.debug();
            return true;
        }
        if (this.isModelDead() && this.getDeathStage() < lastDeathStage && player.capabilities.allowEdit) {
            if (!world.isRemote && !stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GLASS_BOTTLE && this.getDeathStage() < lastDeathStage / 2 && IceAndFire.CONFIG.dragonDropBlood) {
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
                this.setDeathStage(this.getDeathStage() + 1);
                player.inventory.addItemStackToInventory(new ItemStack(this instanceof EntityFireDragon ? IafItemRegistry.fire_dragon_blood : IafItemRegistry.ice_dragon_blood, 1));
                return true;
            } else if (!world.isRemote && stack.isEmpty() && IceAndFire.CONFIG.dragonDropSkull) {
                if (this.getDeathStage() == lastDeathStage - 1) {
                    ItemStack skull = getSkull().copy();
                    skull.setTagCompound(new NBTTagCompound());
                    skull.getTagCompound().setInteger("Stage", this.getDragonStage());
                    skull.getTagCompound().setInteger("DragonType", 0);
                    skull.getTagCompound().setInteger("DragonAge", this.getAgeInDays());
                    this.setDeathStage(this.getDeathStage() + 1);
                    if (!world.isRemote) {
                        this.entityDropItem(skull, 1);
                    }
                    this.setDead();
                } else if (this.getDeathStage() == (lastDeathStage / 2) - 1 && IceAndFire.CONFIG.dragonDropHeart) {
                    ItemStack heart = new ItemStack(this instanceof EntityFireDragon ? IafItemRegistry.fire_dragon_heart : IafItemRegistry.ice_dragon_heart, 1);
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
            return true;
        }
        if (!this.isModelDead()) {
            if (stack.getItem() == IafItemRegistry.creative_dragon_meal) {
                this.setTamedBy(player);
                this.setHunger(this.getHunger() + 20);
                this.heal(Math.min(this.getHealth(), (int) (this.getMaxHealth() / 2)));
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                this.spawnItemCrackParticles(stack.getItem());
                this.spawnItemCrackParticles(Items.BONE);
                this.spawnItemCrackParticles(Items.DYE);
                this.eatFoodBonus(stack);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return true;
            }
            if (this.isBreedingItem(stack) && this.isAdult()) {
                this.setGrowingAge(0);
                this.consumeItemFromStack(player, stack);
                this.setInLove(player);
                return true;
            }
            if (this.isOwner(player)) {
                if(stack.getItem() == getSummoningCrystal() && !ItemSummoningCrystal.hasDragon(stack)){
                    this.setCrystalBound(true);
                    NBTTagCompound compound = stack.getTagCompound();
                    if (compound == null) {
                        compound = new NBTTagCompound();
                        stack.setTagCompound(compound);
                    }
                    NBTTagCompound dragonTag = new NBTTagCompound();
                    dragonTag.setUniqueId("DragonUUID", this.getUniqueID());
                    dragonTag.setString("CustomName", this.getCustomNameTag());
                    compound.setTag("Dragon", dragonTag);
                    this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1, 1);
                    player.swingArm(hand);
                    return true;
                }
                this.setTamedBy(player);
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
                if (stack.isEmpty() && !player.isSneaking()) {
                    if (this.getDragonStage() < 2) {
                        this.startRiding(player, true);
                    }
                    if (!hasHadHornUse && this.getDragonStage() > 2 && !player.isRiding()) {
                        player.setSneaking(false);
                        if(!world.isRemote){
                            player.startRiding(this, true);
                        }
                        if (world.isRemote) {
                            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStartRidingMob(this.getEntityId(), true));
                        }
                        this.setSleeping(false);
                    }
                    return true;
                } else if (stack.isEmpty() && player.isSneaking()) {
                    this.openGUI(player);
                    return true;
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
                        return true;
                    }
                    if (stack.getItem() == IafItemRegistry.dragon_meal) {
                        this.growDragon(1);
                        this.setHunger(this.getHunger() + 20);
                        this.heal(Math.min(this.getHealth(), (int) (this.getMaxHealth() / 2)));
                        this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
                        this.spawnItemCrackParticles(stack.getItem());
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.DYE);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return true;
                    }
                    if (stack.getItem() == IafItemRegistry.sickly_dragon_meal && !this.isAgingDisabled()) {
                        this.setHunger(this.getHunger() + 20);
                        this.heal(this.getMaxHealth());
                        this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundVolume(), this.getSoundPitch());
                        this.spawnItemCrackParticles(stack.getItem());
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.DYE);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.setAgingDisabled(true);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return true;
                    }
                    if (stack.getItem() == IafItemRegistry.dragon_stick) {
                        if (player.isSneaking()) {
                            if (this.hasHomePosition) {
                                this.hasHomePosition = false;
                                player.sendStatusMessage(new TextComponentTranslation("dragon.command.remove_home"), true);
                                return true;
                            } else {
                                BlockPos pos = new BlockPos(this);
                                this.homePos = pos;
                                this.hasHomePosition = true;
                                player.sendStatusMessage(new TextComponentTranslation("dragon.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                                return true;
                            }
                        } else {
                            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
                            this.setCommand(this.getCommand() + 1);
                            if (this.getCommand() > 2) {
                                this.setCommand(0);
                            }
                            String commandText = "stand";
                            if (this.getCommand() == 1) {
                                commandText = "sit";
                            }
                            if (this.getCommand() == 2) {
                                commandText = "escort";
                            }
                            player.sendStatusMessage(new TextComponentTranslation("dragon.command." + commandText), true);
                            return true;
                        }
                    }
                }
            }
        }
        return super.processInteract(player, hand);

    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    private ItemStack getRandomDrop() {
        ItemStack stack = getItemFromLootTable();
        if (stack.getItem() == IafItemRegistry.dragonbone) {
            this.playSound(SoundEvents.ENTITY_SKELETON_AMBIENT, 1, 1);
        } else {
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
        }
        return stack;
    }

    public boolean canPositionBeSeen(double x, double y, double z) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(x, y, z), false, true, false) == null;
    }

    public abstract ResourceLocation getDeadLootTable();

    public ItemStack getItemFromLootTable() {
        LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(getDeadLootTable());
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world)).withLootedEntity(this).withDamageSource(DamageSource.GENERIC);
        if (this.attackingPlayer != null) {
            lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
        }
        List<ItemStack> loot = loottable.generateLootForPools(this.rand, lootcontext$builder.build());
        if (loot.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return loot.get(0);
        }
    }

    public void eatFoodBonus(ItemStack stack) {

    }

    protected void despawnEntity() {
        if (!IceAndFire.CONFIG.canDragonsDespawn) {
            super.despawnEntity();
        }
    }

    public void growDragon(int ageInDays) {
        if (this.isAgingDisabled()) {
            return;
        }
        this.setAgeInDays(this.getAgeInDays() + ageInDays);
        this.setScaleForAge(false);
        this.resetPositionToBB();
        if (this.getAgeInDays() % 25 == 0) {
            for (int i = 0; i < this.getRenderSize() * 4; i++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float f = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX) + this.getEntityBoundingBox().minX);
                float f1 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) + this.getEntityBoundingBox().minY);
                float f2 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ) + this.getEntityBoundingBox().minZ);
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, f, f1, f2, motionX, motionY, motionZ);
                }
            }
        }
        this.updateAttributes();
    }

    public void spawnItemCrackParticles(Item item) {
        for (int i = 0; i < 15; i++) {
            double motionX = getRNG().nextGaussian() * 0.07D;
            double motionY = getRNG().nextGaussian() * 0.07D;
            double motionZ = getRNG().nextGaussian() * 0.07D;
            Vec3d headVec = this.getHeadPosition();
            if (world.isRemote) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, headVec.x, headVec.y, headVec.z, motionX, motionY, motionZ, Item.getIdFromItem(item));
            }
        }
    }

    public boolean isDaytime() {
        return this.world.isDaytime();
    }

    private boolean isStuck() {
        return !this.isChained() && !this.isTamed() && (!this.getNavigator().noPath() && (this.getNavigator().getPath() == null || this.getNavigator().getPath().getFinalPathPoint() != null && this.getDistanceSq(new BlockPos(this.getNavigator().getPath().getFinalPathPoint().x, this.getNavigator().getPath().getFinalPathPoint().y, this.getNavigator().getPath().getFinalPathPoint().z)) > 15)) && ticksStill > 80 && !this.isHovering() && canMove();
    }

    protected boolean isOverAir() {
        return isOverAir;
    }

    private boolean isOverAirLogic() {
        return world.isAirBlock(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1, this.posZ));
    }

    public boolean isDiving() {
        return false;//isFlying() && motionY < -0.2;
    }

    public boolean isBeyondHeight() {
        if (this.posY > this.world.getHeight()) {
            return true;
        }
        return this.posY > IceAndFire.CONFIG.maxDragonFlight;
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0 || IceAndFire.CONFIG.dragonBreakBlockCooldown == 0) {
            --this.blockBreakCounter;
            int bounds = 1;//(int)Math.ceil(this.getRenderSize() * 0.1);
            int flightModifier = isFlying() && this.getAttackTarget() != null ? -1 : 1;
            if ((this.blockBreakCounter == 0 || IceAndFire.CONFIG.dragonBreakBlockCooldown == 0) && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                if (IceAndFire.CONFIG.dragonGriefing != 2 && (!this.isTamed() || IceAndFire.CONFIG.tamedDragonGriefing)) {
                    float hardness = IceAndFire.CONFIG.dragonGriefing == 1 || this.getDragonStage() <= 3 ? 2.0F : 5.0F;
                    if (!isModelDead() && this.getDragonStage() >= 3 && (this.canMove() || this.getControllingPassenger() != null)) {
                        for (int a = (int) Math.floor(this.getEntityBoundingBox().minX) - bounds; a <= (int) Math.ceil(this.getEntityBoundingBox().maxX) + bounds; a++) {
                            for (int b = (int) Math.floor(this.getEntityBoundingBox().minY) + flightModifier; (b <= (int) Math.ceil(this.getEntityBoundingBox().maxY) + bounds + 1) && (b <= 127); b++) {
                                for (int c = (int) Math.floor(this.getEntityBoundingBox().minZ) - bounds; c <= (int) Math.ceil(this.getEntityBoundingBox().maxZ) + bounds; c++) {
                                    if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, a, b, c))) continue;
                                    BlockPos pos = new BlockPos(a, b, c);
                                    IBlockState state = world.getBlockState(pos);
                                    if (state.getMaterial().blocksMovement() && state.getBlockHardness(world, pos) >= 0F && state.getBlockHardness(world, pos) <= hardness && DragonUtils.canDragonBreak(state.getBlock()) && this.canDestroyBlock(pos)) {
                                        this.motionX *= 0.6D;
                                        this.motionZ *= 0.6D;

                                        if (!world.isRemote) {
                                            world.destroyBlock(pos, rand.nextFloat() <= IceAndFire.CONFIG.dragonBlockBreakingDropChance && DragonUtils.canDropFromDragonBlockBreak(state));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void spawnGroundEffects() {
        for (int i = 0; i < this.getRenderSize(); i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = 0.75F * (0.7F * getRenderSize() / 3) * -3;
                float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);
                BlockPos ground = getGround(new BlockPos(MathHelper.floor(this.posX + extraX), MathHelper.floor(this.posY + extraY) - 1, MathHelper.floor(this.posZ + extraZ)));
                IBlockState iblockstate = this.world.getBlockState(ground);
                if (iblockstate.getMaterial() != Material.AIR) {
                    if (world.isRemote) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.posX + extraX, ground.getY() + extraY, this.posZ + extraZ, motionX, motionY, motionZ, Block.getStateId(iblockstate));
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
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        return this.flyTicks > 6000 || down() || flyTicks > 40 && this.flyProgress == 0 || properties != null && properties.isStone || this.isChained() && flyTicks > 100 || this.airAttack == IafDragonAttacks.Air.TACKLE && this.getAttackTarget() != null;
    }

    public abstract String getVariantName(int variant);

    public boolean shouldRiderSit() {
        return this.getControllingPassenger() != null;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            if (this.getControllingPassenger() == null || !this.getControllingPassenger().getUniqueID().equals(passenger.getUniqueID())) {
                updatePreyInMouth(passenger);
            } else {
                if (this.isModelDead()) {
                    passenger.dismountRidingEntity();
                }
                renderYawOffset = rotationYaw;
                this.rotationYaw = passenger.rotationYaw;
                Vec3d riderPos = this.getRiderPosition();
                passenger.setPosition(riderPos.x, riderPos.y + passenger.height, riderPos.z);
                this.stepHeight = 1;
            }
        }
    }

    private float bob(float speed, float degree, boolean bounce, float f, float f1) {
        float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
        if (bounce) {
            bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
        }
        return bob * this.getRenderSize() / 3;
    }

    protected void updatePreyInMouth(Entity prey) {
        this.setAnimation(ANIMATION_SHAKEPREY);
        if (this.getAnimation() == ANIMATION_SHAKEPREY && this.getAnimationTick() > 55 && prey != null) {
            prey.attackEntityFrom(DamageSource.causeMobDamage(this), prey instanceof EntityPlayer ? 17F : (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 4);
            prey.dismountRidingEntity();
        }
        renderYawOffset = rotationYaw;
        float modTick_0 = this.getAnimationTick() - 25;
        float modTick_1 = this.getAnimationTick() > 25 && this.getAnimationTick() < 55 ? 8 * MathHelper.clamp(MathHelper.sin((float) (Math.PI + modTick_0 * 0.25)), -0.8F, 0.8F) : 0;
        float modTick_2 = this.getAnimationTick() > 30 ? 10 : Math.max(0, this.getAnimationTick() - 20);
        float radius = 0.75F * (0.6F * getRenderSize() / 3) * -3;
        float angle = (0.01745329251F * this.renderYawOffset) + 3.15F + (modTick_1 * 2F) * 0.015F;
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double extraY = modTick_2 == 0 ? 0 : 0.035F * ((getRenderSize() / 3) + (modTick_2 * 0.5 * (getRenderSize() / 3)));
        prey.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
    }

    public int getDragonStage() {
        int age = this.getAgeInDays();
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

    public boolean isAdult() {
        return getDragonStage() >= 4;
    }

    public boolean isChild() {
        return getDragonStage() < 2;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setGender(this.getRNG().nextBoolean());
        int age = this.getRNG().nextInt(80) + 1;
        this.growDragon(age);
        this.setVariant(new Random().nextInt(4));
        this.setSleeping(false);
        this.updateAttributes();
        double healthStep = (maximumHealth - minimumHealth) / (125);
        this.heal((Math.round(minimumHealth + (healthStep * age))));
        this.usingGroundAttack = true;
        this.setHunger(50);
        return livingdata;
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if (this.isModelDead()) {
            return false;
        }
        if (this.isBeingRidden() && dmg.getTrueSource() != null && this.getControllingPassenger() != null && dmg.getTrueSource() == this.getControllingPassenger()) {
            return false;
        }

        if ((dmg.damageType.contains("arrow") || getRidingEntity() != null && dmg.getTrueSource() != null && dmg.getTrueSource().isEntityEqual(this.getRidingEntity())) && this.isRiding()) {
            return false;
        }

        if (dmg == DamageSource.IN_WALL || dmg == DamageSource.FALLING_BLOCK) {
            return false;
        }

        if (!world.isRemote && dmg.getTrueSource() != null && this.getRNG().nextInt(4) == 0) {
            this.roar();
        }
        if (i > 0) {
            if(this.isSleeping()){
                this.setSleeping(false);
                if(!this.isTamed()){
                    if(dmg.getTrueSource() instanceof EntityPlayer){
                        this.setAttackTarget((EntityPlayer)dmg.getTrueSource());
                    }
                }
            }
        }
        return super.attackEntityFrom(dmg, i);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        updateParts();
        this.prevDragonPitch = getDragonPitch();
        this.setScaleForAge(true);
        if (world.isRemote) {
            this.updateClientControls();
        }

        world.profiler.startSection("dragonLogic");
        isOverAir = isOverAirLogic();
        logic.updateDragonCommon();
        if (this.isModelDead()) {
            if(!world.isRemote && world.isAirBlock(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) && this.posY > -1){
                this.move(MoverType.SELF, 0, -0.2F, 0);
            }
            this.setBreathingFire(false);
        }else {
            if (world.isRemote) {
                logic.updateDragonClient();
            } else {
                logic.updateDragonServer();
                logic.updateDragonAttack();
            }
        }
        world.profiler.endSection();
        world.profiler.startSection("dragonFlight");
        if (isFlying() && !world.isRemote) {
            this.flightManager.update();
        }
        world.profiler.endSection();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.stepHeight = this.getDragonStage() > 1 ? 1.5F : 1F;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer) {
            this.setAttackTarget(null);
        }
        if(this.isBeingRidden() && this.isModelDead()){
            this.removePassengers();
        }
        if(this.isModelDead()){
            this.setHovering(false);
            this.setFlying(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (animationTick > this.getAnimation().getDuration() && !world.isRemote) {
            animationTick = 0;
        }
    }

    @Override
    public void setScaleForAge(boolean par1) {
        float scale = Math.min(this.getRenderSize() * 0.35F, 7F);
        double prevX = posX;
        double prevY = posY;
        double prevZ = posZ;
        float localWidth = this.width;
        this.setScale(scale);
        if (this.width > localWidth && !this.firstUpdate && !this.world.isRemote) {
            this.setPosition(prevX, prevY, prevZ);
        }
        if (scale != lastScale) {
            resetParts(this.getRenderSize() / 3);
        }
        lastScale = scale;
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    public float getRenderSize() {
        float step = (growth_stages[this.getDragonStage() - 1][1] - growth_stages[this.getDragonStage() - 1][0]) / 25;
        if (this.getAgeInDays() > 125) {
            return growth_stages[this.getDragonStage() - 1][0] + ((step * 25));
        }
        return growth_stages[this.getDragonStage() - 1][0] + ((step * this.getAgeFactor()));
    }

    private int getAgeFactor() {
        return (this.getDragonStage() > 1 ? this.getAgeInDays() - (25 * (this.getDragonStage() - 1)) : this.getAgeInDays());
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        this.getLookHelper().setLookPositionWithEntity(entityIn, 30.0F, 30.0F);
        if (this.isTackling()) {
            return false;
        }
        if (this.isModelDead()) {
            return false;
        }
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag) {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    public void updateRidden() {
        super.updateRidden();
        Entity entity = this.getRidingEntity();
        if (this.isRiding() && entity.isDead) {
            this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            if (this.isRiding()) {
                this.updateRiding(entity);
            }
        }
    }

    public void updateRiding(Entity riding) {
        if (riding != null && riding.isPassenger(this) && riding instanceof EntityPlayer) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 2 ? -0.2F : 0.5F) + (((EntityPlayer) riding).isElytraFlying() ? 2 : 0);
            float angle = (0.01745329251F * ((EntityPlayer) riding).renderYawOffset) + (i == 1 ? 90 : i == 0 ? -90 : 0);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            double extraY = (riding.isSneaking() ? 1.2D : 1.4D) + (i == 2 ? 0.4D : 0D);
            this.rotationYawHead = ((EntityPlayer) riding).rotationYawHead;
            this.prevRotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.setPositionAndRotation(riding.posX + extraX, riding.posY + extraY, riding.posZ + extraZ, ((EntityPlayer) riding).rotationYawHead, 0);
            if ((riding.isSneaking() || ((EntityPlayer) riding).isElytraFlying()) && !riding.isRiding()) {
                this.dismountRidingEntity();
                if(world.isRemote){
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), this.getControlState(), posX, posY, posZ));
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

    public void playLivingSound() {
        if (!this.isSleeping() && !this.isModelDead() && !this.world.isRemote) {
            if (this.getAnimation() == this.NO_ANIMATION) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.playLivingSound();
        }
    }

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
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        if (otherAnimal instanceof EntityDragonBase && otherAnimal != this && otherAnimal.getClass() == this.getClass()) {
            EntityDragonBase dragon = (EntityDragonBase) otherAnimal;
            return this.isMale() && !dragon.isMale() || !this.isMale() && dragon.isMale();
        }
        return false;
    }

    public EntityDragonEgg createEgg(EntityDragonBase ageable) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        EntityDragonEgg dragon = new EntityDragonEgg(this.world);
        dragon.setType(EnumDragonEgg.byMetadata(MathHelper.clamp(getRNG().nextInt(4), 0, 3) + getStartMetaForType()));
        dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        return dragon;
    }

    public int getStartMetaForType() {
        return 0;
    }

    public boolean isTargetBlocked(Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(this.getPosition()), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos sidePos = rayTrace.getBlockPos();
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (!world.isAirBlock(sidePos)) {
                    return true;
                } else if (!world.isAirBlock(pos)) {
                    return true;
                }
                return rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.BLOCK;
            }
        }
        return false;
    }

    private double getFlySpeed() {
        return (2 + (this.getAgeInDays() / 125) * 2) * (this.isTackling() ? 2 : 1);
    }

    public boolean isTackling() {
        if (world.isRemote) {
            boolean tackling = this.dataManager.get(TACKLE).booleanValue();
            this.isTackling = tackling;
            return tackling;
        }
        return isTackling;
    }

    public void setTackling(boolean tackling) {
        this.dataManager.set(TACKLE, tackling);
        if (!world.isRemote) {
            this.isTackling = tackling;
        }
    }

    public boolean isAgingDisabled() {
        return this.dataManager.get(AGINGDISABLED).booleanValue();
    }

    public void setAgingDisabled(boolean isAgingDisabled) {
        this.dataManager.set(AGINGDISABLED, isAgingDisabled);
    }


    public boolean isBoundToCrystal() {
        return this.dataManager.get(CRYSTAL_BOUND).booleanValue();
    }

    public void setCrystalBound(boolean crystalBound) {
        this.dataManager.set(CRYSTAL_BOUND, crystalBound);
    }


    public float getDistanceSquared(Vec3d vec3d) {
        float f = (float) (this.posX - vec3d.x);
        float f1 = (float) (this.posY - vec3d.y);
        float f2 = (float) (this.posZ - vec3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    public abstract Item getVariantScale(int variant);

    public abstract Item getVariantEgg(int variant);

    public abstract Item getSummoningCrystal();

    @SideOnly(Side.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            down(IafKeybindRegistry.dragon_down.isKeyDown());
            attack(IafKeybindRegistry.dragon_fireAttack.isKeyDown());
            strike(IafKeybindRegistry.dragon_strike.isKeyDown());
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
            }
        }
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
            }
        }
    }

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
        return this.getHealth() <= 0.0F || isSitting() && !this.isBeingRidden() || this.isModelDead();
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (this.getAnimation() == ANIMATION_SHAKEPREY || !this.canMove() && !this.isBeingRidden() || this.isSitting()) {
            strafe = 0;
            forward = 0;
            vertical = 0;
            moveVertical = 0;
            moveStrafing = 0;
            moveForward = 0;
            super.travel(strafe, forward, vertical);
            return;
        }
        super.travel(strafe, forward, vertical);
    }

    public void updateCheckPlayer() {
        double checklength = this.getEntityBoundingBox().getAverageEdgeLength() * 3;
        EntityPlayer player = world.getClosestPlayerToEntity(this, checklength);
        if (this.isSleeping()) {
            if (player != null && !this.isOwner(player) && !player.isCreative()) {
                this.setSleeping(false);
                this.setSitting(false);
                this.setAttackTarget(player);
            }
        }
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) this.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public void onDeath(DamageSource cause) {
        if (cause.getTrueSource() != null) {
            //if (cause.getTrueSource() instanceof EntityPlayer) {
            //	((EntityPlayer) cause.getTrueSource()).addStat(ModAchievements.dragonSlayer, 1);
            //}
        }
        super.onDeath(cause);
    }

    @Override
    public void onHearFlute(EntityPlayer player) {
        if (this.isTamed() && this.isOwner(player)) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
    }

    public abstract SoundEvent getRoarSound();

    public void roar() {
        if (EntityGorgon.isStoneMob(this)) {
            return;
        }
        if (rand.nextBoolean()) {
            if (this.getAnimation() != ANIMATION_EPIC_ROAR) {
                this.setAnimation(ANIMATION_EPIC_ROAR);
                this.playSound(this.getRoarSound(), this.getSoundVolume() + 3 + Math.max(0, this.getDragonStage() - 2), this.getSoundPitch() * 0.7F);
            }
            if (this.getDragonStage() > 3) {
                int size = (this.getDragonStage() - 3) * 30;
                List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(size, size, size));
                for (Entity entity : entities) {
                    boolean isStrongerDragon = entity instanceof EntityDragonBase && ((EntityDragonBase) entity).getDragonStage() >= this.getDragonStage();
                    if (entity instanceof EntityLivingBase && !isStrongerDragon) {
                        EntityLivingBase living = (EntityLivingBase) entity;
                        if (this.isOwner(living) || this.isOwnersPet(living)) {
                            living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 50 * size));
                        } else {
                            if (living.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != IafItemRegistry.earplugs) {
                                living.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 50 * size));
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
                int size = (this.getDragonStage() - 3) * 30;
                List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(size, size, size));
                for (Entity entity : entities) {
                    boolean isStrongerDragon = entity instanceof EntityDragonBase && ((EntityDragonBase) entity).getDragonStage() >= this.getDragonStage();
                    if (entity instanceof EntityLivingBase && !isStrongerDragon) {
                        EntityLivingBase living = (EntityLivingBase) entity;
                        if (this.isOwner(living) || this.isOwnersPet(living)) {
                            living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30 * size));
                        } else {
                            living.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 30 * size));
                        }
                    }
                }
            }
        }
    }

    private boolean isOwnersPet(EntityLivingBase living) {
        return this.isTamed() && this.getOwner() != null && living instanceof EntityTameable && ((EntityTameable) living).getOwner() != null && this.getOwner().isEntityEqual(((EntityTameable) living).getOwner());
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = entity.world.rayTraceBlocks(vec1, vec2, false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public void processArrows() {
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox());
        for (Entity entity : entities) {
            if (entity instanceof EntityArrow) {

            }
        }
    }

    public boolean shouldRenderEyes() {
        return !this.isSleeping() && !this.isModelDead() && !this.isBlinking() && !EntityGorgon.isStoneMob(this);
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    public void dropArmor() {

    }

    public boolean isChained() {
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(this, ChainEntityProperties.class);
        return chainProperties != null && chainProperties.isChained();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRender(ICamera camera) {
        boolean render = false;
        return inFrustrum(camera, headPart) || inFrustrum(camera, neckPart) ||
                inFrustrum(camera, leftWingLowerPart) || inFrustrum(camera, rightWingLowerPart) ||
                inFrustrum(camera, leftWingUpperPart) || inFrustrum(camera, rightWingUpperPart) ||
                inFrustrum(camera, tail1Part) || inFrustrum(camera, tail2Part) ||
                inFrustrum(camera, tail3Part) || inFrustrum(camera, tail4Part);
    }

    private boolean inFrustrum(ICamera camera, Entity entity) {
        return camera != null && entity != null && camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
    }

    public RayTraceResult rayTraceRider(Entity rider, double blockReachDistance, float partialTicks) {
        Vec3d vec3d = rider.getPositionEyes(partialTicks);
        Vec3d vec3d1 = rider.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return this.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    public Vec3d getRiderPosition() {
        float sitProg = this.sitProgress * 0.015F;
        float deadProg = this.modelDeadProgress * -0.02F;
        float hoverProg = this.hoverProgress * 0.03F;
        float flyProg = this.flyProgress * 0.01F;
        float sleepProg = this.sleepProgress * -0.025F;
        float extraAgeScale = (Math.max(0, this.getAgeInDays() - 75) / 75F) * 1.65F;
        float pitchX = 0;
        float pitchY = 0;
        float dragonPitch = getDragonPitch();
        if (dragonPitch > 0) {
            pitchX = Math.min(dragonPitch / 90, 0.3F);
            pitchY = -(dragonPitch / 90) * 2F;
        }
        if (dragonPitch < 0) {//going up
            pitchY = (dragonPitch / 90) * 0.1F;
            pitchX = Math.max(dragonPitch / 90, -0.7F);
        }
        float xzMod = (0.15F + pitchX) * getRenderSize() + extraAgeScale;
        float headPosX = (float) (posX + (xzMod) * Math.cos((rotationYaw + 90) * Math.PI / 180));
        float headPosY = (float) (posY + (0.7F + sitProg + hoverProg + deadProg + sleepProg + flyProg + pitchY) * getRenderSize() * 0.3F + extraAgeScale);
        float headPosZ = (float) (posZ + (xzMod) * Math.sin((rotationYaw + 90) * Math.PI / 180));
        return new Vec3d(headPosX, headPosY, headPosZ);
    }

    public void onKillCommand() {
        this.setDead();
        this.setDeathStage(this.getAgeInDays() / 5);
        this.setModelDead(false);
    }

    public Vec3d getHeadPosition() {
        float sitProg = this.sitProgress * 0.015F;
        float deadProg = this.modelDeadProgress * -0.02F;
        float hoverProg = this.hoverProgress * 0.03F;
        float flyProg = this.flyProgress * 0.01F;
        int tick = 0;
        if (this.getAnimationTick() < 10) {
            tick = this.getAnimationTick();
        } else if (this.getAnimationTick() > 50) {
            tick = 60 - this.getAnimationTick();
        } else {
            tick = 10;
        }
        float epicRoarProg = this.getAnimation() == ANIMATION_EPIC_ROAR ? tick * 0.1F : 0;
        float sleepProg = this.sleepProgress * -0.025F;
        float pitchMulti = 0;
        float pitchAdjustment = 0;
        float pitchMinus = 0;
        float dragonPitch = -getDragonPitch();
        if (this.isFlying() || this.isHovering()) {
            pitchMulti = (float) Math.sin(Math.toRadians(dragonPitch));
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
        float xzMod = 1.9F * getRenderSize() * 0.3F + getRenderSize() * (0.3F * (float) Math.sin((dragonPitch + 90) * Math.PI / 180) * pitchAdjustment - pitchMinus);
        float headPosX = (float) (posX + (xzMod) * Math.cos((rotationYaw + 90) * Math.PI / 180));
        float headPosY = (float) (posY + (0.7F + sitProg + hoverProg + deadProg + epicRoarProg + sleepProg + flyProg + pitchMulti) * getRenderSize() * 0.3F);
        float headPosZ = (float) (posZ + (xzMod) * Math.sin((rotationYaw + 90) * Math.PI / 180));
        return new Vec3d(headPosX, headPosY, headPosZ);
    }

    public abstract void stimulateFire(double burnX, double burnY, double burnZ, int syncType);

    public void randomizeAttacks() {
        this.airAttack = IafDragonAttacks.Air.values()[getRNG().nextInt(IafDragonAttacks.Air.values().length)];
        this.groundAttack = IafDragonAttacks.Ground.values()[getRNG().nextInt(IafDragonAttacks.Ground.values().length)];

    }

    public void tryScorchTarget() {
        EntityLivingBase entity = this.getAttackTarget();
        if (entity != null) {
            float distX = (float) (entity.posX - this.posX);
            float distZ = (float) (entity.posZ - this.posZ);
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    rotationYaw = renderYawOffset;
                    if (this.ticksExisted % 5 == 0) {
                        this.playSound(IafSoundRegistry.FIREDRAGON_BREATH, 4, 1);
                    }
                    stimulateFire(this.posX + distX * this.fireTicks / 40, entity.posY, this.posZ + distZ * this.fireTicks / 40, 1);
                }
            } else {
                this.setBreathingFire(true);
            }
        }
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        this.flightManager.onSetAttackTarget(entitylivingbaseIn);
    }

    public boolean isPart(Entity entityHit) {
        return headPart != null && headPart.isEntityEqual(entityHit) || neckPart != null && neckPart.isEntityEqual(entityHit) ||
                leftWingLowerPart != null && leftWingLowerPart.isEntityEqual(entityHit) || rightWingLowerPart != null && rightWingLowerPart.isEntityEqual(entityHit) ||
                leftWingUpperPart != null && leftWingUpperPart.isEntityEqual(entityHit) || rightWingUpperPart != null && rightWingUpperPart.isEntityEqual(entityHit) ||
                tail1Part != null && tail1Part.isEntityEqual(entityHit) || tail2Part != null && tail2Part.isEntityEqual(entityHit) ||
                tail3Part != null && tail3Part.isEntityEqual(entityHit) || tail4Part != null && tail4Part.isEntityEqual(entityHit);
    }

    public double getFlightSpeedModifier() {
        return 1;
    }

    public boolean isAllowedToTriggerFlight() {
        return this.hasFlightClearance() && !this.isSitting() && this.getPassengers().isEmpty() && !this.isChild() && !this.isSleeping() && this.canMove() && this.onGround;
    }

    public BlockPos getEscortPosition() {
        return this.getOwner() != null ? this.getOwner().getPosition() : this.getPosition();
    }

    public boolean shouldTPtoOwner() {

        return this.getOwner() != null && this.getDistance(this.getOwner()) > 10;
    }

    public boolean isSkeletal() {
        return this.getDeathStage() >= (this.getAgeInDays() / 5) / 2;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public boolean writeToNBTOptional(NBTTagCompound compound) {
        String s = this.getEntityString();
        compound.setString("id", s);
        this.writeToNBT(compound);
        return true;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (soundIn == SoundEvents.ENTITY_GENERIC_EAT || soundIn == this.getAmbientSound() || soundIn == this.getHurtSound(null) || soundIn == this.getDeathSound() || soundIn == this.getRoarSound()) {
            if (!this.isSilent() && this.headPart != null) {
                this.world.playSound(null, this.headPart.posX, this.headPart.posY, this.headPart.posZ, soundIn, this.getSoundCategory(), volume, pitch);
            }
        } else {
            super.playSound(soundIn, volume, pitch);
        }
    }

    public boolean hasFlightClearance() {
        BlockPos topOfBB = new BlockPos(this.posX, this.getEntityBoundingBox().maxY, this.posZ);
        for (int i = 1; i < 4; i++) {
            if (!world.isAirBlock(topOfBB.up(i))) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        if (slotIn == EntityEquipmentSlot.OFFHAND) {
            return dragonInventory.getStackInSlot(0);
        } else if (slotIn == EntityEquipmentSlot.HEAD) {
            return dragonInventory.getStackInSlot(1);
        } else if (slotIn == EntityEquipmentSlot.CHEST) {
            return dragonInventory.getStackInSlot(2);
        } else if (slotIn == EntityEquipmentSlot.LEGS) {
            return dragonInventory.getStackInSlot(3);
        } else if (slotIn == EntityEquipmentSlot.FEET) {
            return dragonInventory.getStackInSlot(4);
        }
        return super.getItemStackFromSlot(slotIn);
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        if (slotIn == EntityEquipmentSlot.OFFHAND) {
            dragonInventory.setInventorySlotContents(0, stack);
        } else if (slotIn == EntityEquipmentSlot.HEAD) {
            dragonInventory.setInventorySlotContents(1, stack);
        } else if (slotIn == EntityEquipmentSlot.CHEST) {
            dragonInventory.setInventorySlotContents(2, stack);
        } else if (slotIn == EntityEquipmentSlot.LEGS) {
            dragonInventory.setInventorySlotContents(3, stack);
        } else if (slotIn == EntityEquipmentSlot.FEET) {
            dragonInventory.setInventorySlotContents(4, stack);
        } else {
            super.getItemStackFromSlot(slotIn);
        }
        updateAttributes();
    }

    public float getSoundPitch() {
        return super.getSoundPitch();
    }

    public SoundEvent getBabyFireSound() {
        return SoundEvents.BLOCK_FIRE_EXTINGUISH;
    }

    protected boolean isPlayingAttackAnimation(){
        return this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_SHAKEPREY || this.getAnimation() == ANIMATION_WINGBLAST ||
                this.getAnimation() == ANIMATION_TAILWHACK;
    }

    protected IafDragonLogic createDragonLogic(){
        return new IafDragonLogic(this);
    }

    protected int getFlightChancePerTick(){
        return FLIGHT_CHANCE_PER_TICK;
    }

    public void onRemovedFromWorld() {
        if(IceAndFire.CONFIG.chunkLoadSummonCrystal) {
            if (this.isBoundToCrystal()) {
                DragonPosWorldData.get(world).addDragon(this.getUniqueID(), this.getPosition());
            }
        }
        super.onRemovedFromWorld();
    }
}