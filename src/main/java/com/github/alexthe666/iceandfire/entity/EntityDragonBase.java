package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModAchievements;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.message.MessageDragonArmor;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import fossilsarcheology.api.EnumDiet;
import fossilsarcheology.api.FoodMappings;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class EntityDragonBase extends EntityTameable implements IAnimatedEntity, IDragonFlute {

    private static final int FLIGHT_CHANCE_PER_TICK = 1250;
    private static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> AGE_TICKS = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> GENDER = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIREBREATHING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVERING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> HEAD_ARMOR = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> NECK_ARMOR = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BODY_ARMOR = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TAIL_ARMOR = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> MODEL_DEAD = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DEATH_STAGE = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityDragonBase.class, DataSerializers.BYTE);
    public static Animation ANIMATION_EAT;
    public static Animation ANIMATION_SPEAK;
    public static Animation ANIMATION_BITE;
    public static Animation ANIMATION_SHAKEPREY;
    public double minimumDamage;
    public double maximumDamage;
    public double minimumHealth;
    public double maximumHealth;
    public double minimumSpeed;
    public double maximumSpeed;
    public EnumDiet diet;
    public float sleepProgress;
    public float hoverProgress;
    public float flyProgress;
    public float fireBreathProgress;
    public int fireStopTicks;
    public int flyTicks;
    public float modelDeadProgress;
    public float ridingProgress;
    public ContainerHorseChest dragonInv;
    public boolean isDaytime;
    public boolean attackDecision;
    public int animationCycle;
    public BlockPos airTarget;
    public BlockPos homeArea;
    @SideOnly(Side.CLIENT)
    public RollBuffer roll_buffer;
    public int spacebarTicks;
    public int spacebarTickCounter;
    public float[][] growth_stages;
    public boolean isFire = this instanceof EntityFireDragon;
    protected int flyHovering;
    private boolean isSleeping;
    private boolean isSitting;
    private boolean isHovering;
    private boolean isFlying;
    private boolean isBreathingFire;
    private int fireTicks;
    private int hoverTicks;
    private boolean isModelDead;
    private int animationTick;
    private Animation currentAnimation;
    private ItemStackHandler itemHandler = null;

    public EntityDragonBase(World world, EnumDiet diet, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
        super(world);
        this.diet = diet;
        this.minimumDamage = minimumDamage;
        this.maximumDamage = maximumDamage;
        this.minimumHealth = minimumHealth;
        this.maximumHealth = maximumHealth;
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        ANIMATION_EAT = Animation.create(20);
        updateAttributes();
        initDragonInv();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            roll_buffer = new RollBuffer();
        }
    }

    private void initDragonInv() {
        ContainerHorseChest animalchest = this.dragonInv;
        this.dragonInv = new ContainerHorseChest("dragonInventory", 4);
        this.dragonInv.setCustomName(this.getName());
        if (animalchest != null) {
            int i = Math.min(animalchest.getSizeInventory(), this.dragonInv.getSizeInventory());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.dragonInv.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }
        //this.updateDragonSlots();
        this.itemHandler = new ItemStackHandler(4) {
            public void onContentsChanged() {
                int dragonArmorHead = EntityDragonBase.this.getArmorInSlot(0);
                int dragonArmorNeck = EntityDragonBase.this.getArmorInSlot(1);
                int dragonArmorBody = EntityDragonBase.this.getArmorInSlot(2);
                int dragonArmorTail = EntityDragonBase.this.getArmorInSlot(3);
                EntityDragonBase.this.updateDragonSlots();
                if (EntityDragonBase.this.ticksExisted > 20) {
                    if (dragonArmorHead != EntityDragonBase.this.getIntFromArmor(EntityDragonBase.this.dragonInv.getStackInSlot(0))) {
                        EntityDragonBase.this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
                    }
                    if (dragonArmorNeck != EntityDragonBase.this.getIntFromArmor(EntityDragonBase.this.dragonInv.getStackInSlot(1))) {
                        EntityDragonBase.this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
                    }
                    if (dragonArmorBody != EntityDragonBase.this.getIntFromArmor(EntityDragonBase.this.dragonInv.getStackInSlot(2))) {
                        EntityDragonBase.this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
                    }
                    if (dragonArmorTail != EntityDragonBase.this.getIntFromArmor(EntityDragonBase.this.dragonInv.getStackInSlot(3))) {
                        EntityDragonBase.this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
                    }
                }
            }
        };
        if (world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 0, this.getIntFromArmor(this.dragonInv.getStackInSlot(0))));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 1, this.getIntFromArmor(this.dragonInv.getStackInSlot(1))));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 2, this.getIntFromArmor(this.dragonInv.getStackInSlot(2))));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 3, this.getIntFromArmor(this.dragonInv.getStackInSlot(3))));
        }
    }

    private void updateDragonSlots() {
            this.setArmorInSlot(0, getIntFromArmor(this.dragonInv.getStackInSlot(0)));
            this.setArmorInSlot(1, getIntFromArmor(this.dragonInv.getStackInSlot(1)));
            this.setArmorInSlot(2, getIntFromArmor(this.dragonInv.getStackInSlot(2)));
            this.setArmorInSlot(3, getIntFromArmor(this.dragonInv.getStackInSlot(3)));
    }

    public void openGUI(EntityPlayer playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            playerEntity.openGui(IceAndFire.INSTANCE, 0, this.world, this.getEntityId(), 0, 0);
        }
    }

    protected void onDeathUpdate() {
        this.deathTime = 0;
        this.setModelDead(true);
        if (this.getDeathStage() >= this.getAgeInDays() / 5) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            this.setDead();
            for (int k = 0; k < 40; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1, new int[0]);
                }
            }
            for (int k = 0; k < 3; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                if (isFire) {
                    if (world.isRemote) {
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1, new int[0]);
                    }
                } else {
                    IceAndFire.PROXY.spawnParticle("snowflake", this.world, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
                }
            }
        }
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 5 + this.world.rand.nextInt(this.getDragonStage() / 2);
    }

    public int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == ModItems.dragon_armor_iron) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == ModItems.dragon_armor_gold) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == ModItems.dragon_armor_diamond) {
            return 3;
        }
        return 0;
    }

    @Override
    public boolean isAIDisabled() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        return this.isModelDead() || properties.isStone;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HUNGER, 0);
        this.dataManager.register(AGE_TICKS, 0);
        this.dataManager.register(GENDER, false);
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(FIREBREATHING, false);
        this.dataManager.register(HOVERING, false);
        this.dataManager.register(FLYING, false);
        this.dataManager.register(HEAD_ARMOR, 0);
        this.dataManager.register(NECK_ARMOR, 0);
        this.dataManager.register(BODY_ARMOR, 0);
        this.dataManager.register(TAIL_ARMOR, 0);
        this.dataManager.register(DEATH_STAGE, 0);
        this.dataManager.register(MODEL_DEAD, false);
        this.dataManager.register(CONTROL_STATE, (byte) 0);
    }

    public boolean up() {
        return (dataManager.get(CONTROL_STATE) & 1) == 1;
    }

    public boolean down() {
        return (dataManager.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (dataManager.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    public boolean strike() {
        return (dataManager.get(CONTROL_STATE) >> 3 & 1) == 1;
    }

    public boolean dismount() {
        return (dataManager.get(CONTROL_STATE) >> 4 & 1) == 1;
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
        byte prevState = dataManager.get(CONTROL_STATE);
        if (newState) {
            dataManager.set(CONTROL_STATE, (byte) (prevState | (1 << i)));
        } else {
            dataManager.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    public byte getControlState() {
        return dataManager.get(CONTROL_STATE);
    }

    public void setControlState(byte state) {
        dataManager.set(CONTROL_STATE, state);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Hunger", this.getHunger());
        compound.setInteger("AgeTicks", this.getAgeInTicks());
        compound.setBoolean("Gender", this.isMale());
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Sleeping", this.isSleeping());
        compound.setBoolean("FireBreathing", this.isBreathingFire());
        compound.setBoolean("AttackDecision", attackDecision);
        compound.setBoolean("Hovering", this.isHovering());
        compound.setBoolean("Flying", this.isFlying());
        compound.setBoolean("Sitting", this.isSitting());
        compound.setInteger("ArmorHead", this.getArmorInSlot(0));
        compound.setInteger("ArmorNeck", this.getArmorInSlot(1));
        compound.setInteger("ArmorBody", this.getArmorInSlot(2));
        compound.setInteger("ArmorTail", this.getArmorInSlot(3));
        compound.setInteger("DeathStage", this.getDeathStage());
        compound.setBoolean("ModelDead", this.isModelDead());
        compound.setFloat("DeadProg", this.modelDeadProgress);
        if (dragonInv != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < this.dragonInv.getSizeInventory(); ++i) {
                ItemStack itemstack = this.dragonInv.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }
            compound.setTag("Items", nbttaglist);
        }
        if (this.getCustomNameTag() != null && !this.getCustomNameTag().isEmpty()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }
        if (homeArea != null) {
            compound.setInteger("HomeAreaX", homeArea.getX());
            compound.setInteger("HomeAreaY", homeArea.getY());
            compound.setInteger("HomeAreaZ", homeArea.getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setHunger(compound.getInteger("Hunger"));
        this.setAgeInTicks(compound.getInteger("AgeTicks"));
        this.setGender(compound.getBoolean("Gender"));
        this.setVariant(compound.getInteger("Variant"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setSitting(compound.getBoolean("Sitting"));
        this.setBreathingFire(compound.getBoolean("FireBreathing"));
        this.attackDecision = compound.getBoolean("AttackDecision");
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setArmorInSlot(0, compound.getInteger("ArmorHead"));
        this.setArmorInSlot(1, compound.getInteger("ArmorNeck"));
        this.setArmorInSlot(2, compound.getInteger("ArmorBody"));
        this.setArmorInSlot(3, compound.getInteger("ArmorTail"));
        if (dragonInv != null) {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initDragonInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                if (j <= 4) {
                    this.dragonInv.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                }
            }
        } else {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initDragonInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                this.initDragonInv();
                this.dragonInv.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(nbttagcompound)));

                if (world.isRemote) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 0, this.getIntFromArmor(new ItemStack(nbttagcompound))));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 1, this.getIntFromArmor(new ItemStack(nbttagcompound))));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 2, this.getIntFromArmor(new ItemStack(nbttagcompound))));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), 3, this.getIntFromArmor(new ItemStack(nbttagcompound))));
                }
            }
        }

        this.setDeathStage(compound.getInteger("DeathStage"));
        this.setModelDead(compound.getBoolean("ModelDead"));
        this.modelDeadProgress = compound.getFloat("DeadProg");
        if (!compound.getString("CustomName").isEmpty()) {
            this.setCustomNameTag(compound.getString("CustomName"));
        }
        if (compound.getInteger("HomeAreaX") != 0 && compound.getInteger("HomeAreaY") != 0 && compound.getInteger("HomeAreaZ") != 0) {
            homeArea = new BlockPos(compound.getInteger("HomeAreaX"), compound.getInteger("HomeAreaY"), compound.getInteger("HomeAreaZ"));
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
        return this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer && this.getControllingPassenger().getUniqueID().equals(player.getUniqueID());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);

    }

    private void updateAttributes() {
        double healthStep = (maximumHealth - minimumHealth) / (125);
        double attackStep = (maximumDamage - minimumDamage) / (125);
        double speedStep = (maximumSpeed - minimumSpeed) / (125);
        if (this.getAgeInDays() <= 125) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.round(minimumHealth + (healthStep * this.getAgeInDays())));
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.round(minimumDamage + (attackStep * this.getAgeInDays())));
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(minimumSpeed + (speedStep * this.getAgeInDays()));

        }
    }

    public int getHunger() {
        return this.dataManager.get(HUNGER);
    }

    public void setHunger(int hunger) {
        this.dataManager.set(HUNGER, Math.min(100, hunger));
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

    public boolean isHovering() {
        if (world.isRemote) {
            return this.isHovering = this.dataManager.get(HOVERING);
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
            return this.isFlying = this.dataManager.get(FLYING);
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
        if (!world.isRemote) {
            this.isFlying = flying;
        }
    }

    public void setGender(boolean male) {
        this.dataManager.set(GENDER, male);
    }

    public boolean isSleeping() {
        if (world.isRemote) {
            boolean isSleeping = this.dataManager.get(SLEEPING);
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
            boolean breathing = this.dataManager.get(FIREBREATHING);
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
            boolean isSitting = (this.dataManager.get(TAMED) & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

    @Override
    public void setSitting(boolean sitting) {
        super.setSitting(sitting);
        if (!world.isRemote) {
            this.isSitting = sitting;
        }
    }

    public int getArmorInSlot(int i) {
        switch (i) {
            default:
                return this.dataManager.get(HEAD_ARMOR);
            case 1:
                return this.dataManager.get(NECK_ARMOR);
            case 2:
                return this.dataManager.get(BODY_ARMOR);
            case 3:
                return this.dataManager.get(TAIL_ARMOR);
        }
    }

    public void riderShootFire(Entity controller) {
    }

    public void setArmorInSlot(int i, int armorType) {
        switch (i) {
            case 0:
                this.dataManager.set(HEAD_ARMOR, armorType);
                break;
            case 1:
                this.dataManager.set(NECK_ARMOR, armorType);
                break;
            case 2:
                this.dataManager.set(BODY_ARMOR, armorType);
                break;
            case 3:
                this.dataManager.set(TAIL_ARMOR, armorType);
                break;
        }
        if (world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonArmor(this.getEntityId(), i, armorType));
        }
    }

    public boolean canMove() {
        return !this.isSitting() && !this.isSleeping() && this.getControllingPassenger() == null && !this.isModelDead() && sleepProgress == 0;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int lastDeathStage = this.getAgeInDays() / 5;
        if (this.isModelDead() && this.getDeathStage() < lastDeathStage) {
            player.addStat(ModAchievements.dragonHarvest, 1);
            if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GLASS_BOTTLE && this.getDeathStage() < lastDeathStage / 2) {
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }
                this.setDeathStage(this.getDeathStage() + 1);
                player.inventory.addItemStackToInventory(new ItemStack(this instanceof EntityFireDragon ? ModItems.fire_dragon_blood : ModItems.ice_dragon_blood, 1));
                return true;
            } else {
                if (this.getDeathStage() == lastDeathStage - 1) {
                    ItemStack skull = new ItemStack(ModItems.dragon_skull, 1, this.isFire ? 0 : 1);
                    skull.setTagCompound(new NBTTagCompound());
                    skull.getTagCompound().setInteger("Stage", this.getDragonStage());
                    skull.getTagCompound().setInteger("DragonType", 0);
                    skull.getTagCompound().setInteger("DragonAge", this.getAgeInDays());
                    this.setDeathStage(this.getDeathStage() + 1);
                    if (!world.isRemote) {
                        this.entityDropItem(skull, 1);
                    }
                    this.setDead();
                } else if (this.getDeathStage() == (int) (lastDeathStage / 2) - 1) {
                    ItemStack heart = new ItemStack(this instanceof EntityFireDragon ? ModItems.fire_dragon_heart : ModItems.ice_dragon_heart, 1);
                    ItemStack egg = new ItemStack(this.getVariantEgg(this.rand.nextInt(3)), 1);
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
        } else if (!this.isModelDead()) {
            if (this.isOwner(player)) {
                if (!stack.isEmpty()) {
                    if (this.isBreedingItem(stack) && this.isAdult()) {
                        this.consumeItemFromStack(player, stack);
                        this.setInLove(player);
                        return true;
                    }
                    if (stack.getItem() != null) {
                        int itemFoodAmount = FoodMappings.INSTANCE.getItemFoodAmount(stack.getItem(), diet);
                        if (itemFoodAmount > 0) {
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
                        if (stack.getItem() == ModItems.dragon_meal) {
                            this.growDragon(1);
                            this.setHunger(this.getHunger() + 20);
                            this.setHealth(Math.min(this.getMaxHealth(), (int) (this.getMaxHealth() / 3)));
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
                        if (stack.getItem() == ModItems.dragon_stick) {
                            this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, this.getSoundVolume(), this.getSoundPitch());
                            this.setSitting(!this.isSitting());
                            if (world.isRemote) {
                                player.sendMessage(new TextComponentTranslation("dragon.command." + (this.isSitting() ? "sit" : "stand")));
                            }
                            return true;
                        }

                        if (stack.getItem() == ModItems.dragon_horn) {
                            this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 3, 1.25F);
                            ItemStack stack1 = new ItemStack(this.isFire ? ModItems.dragon_horn_fire : ModItems.dragon_horn_ice);
                            stack1.setTagCompound(new NBTTagCompound());
                            this.writeEntityToNBT(stack1.getTagCompound());
                            player.setHeldItem(hand, stack1);
                            this.setDead();
                            return true;
                        }
                    }
                } else {
                    if (!player.isSneaking()) {
                        if (this.getDragonStage() > 2) {
                            player.setSneaking(false);
                            player.startRiding(this, true);
                            player.addStat(ModAchievements.dragonRide, 1);
                            this.setSleeping(false);
                        } else if (this.isRiding()) {
                            this.dismountRidingEntity();
                        } else if (this.getDragonStage() < 2) {
                            this.startRiding(player, true);

                        }

                        return true;
                    } else if (stack.isEmpty() && player.isSneaking()) {
                        this.openGUI(player);
                        return true;
                    }
                }
            }
        }
        return super.processInteract(player, hand);

    }

    private ItemStack getRandomDrop() {
        int chance = this.rand.nextInt(99) + 1;
        Item flesh = isFire ? ModItems.fire_dragon_flesh : ModItems.ice_dragon_flesh;
        if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
            this.playSound(SoundEvents.ENTITY_SKELETON_AMBIENT, 1, 1);
            return new ItemStack(ModItems.dragonbone, 1 + this.rand.nextInt(1 + (this.getAgeInDays() / 25)));
        } else {
            this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
            if (chance >= 0 && chance < 40) {
                return new ItemStack(flesh, 1 + this.rand.nextInt(1 + (this.getAgeInDays() / 25)));
            }
            if (chance >= 40 && chance < 100) {
                return new ItemStack(this.getVariantScale(this.getVariant()), 1 + this.rand.nextInt(1 + (this.getAgeInDays() / 5)));
            }
        }
        return ItemStack.EMPTY;
    }

    public void eatFoodBonus(ItemStack stack) {

    }

    public void growDragon(int ageInDays) {
        this.setAgeInDays(this.getAgeInDays() + ageInDays);
        this.setScaleForAge(false);
        if (this.getAgeInDays() % 25 == 0) {
            for (int i = 0; i < this.getRenderSize() * 4; i++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float f = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX) + this.getEntityBoundingBox().minX);
                float f1 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) + this.getEntityBoundingBox().minY);
                float f2 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ) + this.getEntityBoundingBox().minZ);
                if (world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, f, f1, f2, motionX, motionY, motionZ, new int[]{});
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
            float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
            float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
            float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
            if (world.isRemote) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, headPosX, headPosZ, headPosY, motionX, motionY, motionZ, new int[]{Item.getIdFromItem(item)});
            }
        }
    }

    public boolean isDaytime() {
       /* if (!this.firstUpdate && this.world != null) {
            if (world.isRemote) {
                return isDaytime;
            } else {
                IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDaytime(this.getEntityId(), this.world.isDaytime()));
                return this.world.isDaytime();
            }
        } else {*/
            return this.world.isDaytime();

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if(properties != null && properties.isStone){
            this.setFlying(false);
            this.setHovering(false);
            return;
        }
        if (this.isFlying() && this.ticksExisted % 40 == 0 || this.isFlying() && this.isSleeping()) {
            this.setFlying(false);
            this.setFlying(true);
            this.setSleeping(false);
        }
        if (!this.canMove() && this.getAttackTarget() != null) {
            this.setAttackTarget(null);
        }
        if (!this.canMove()) {
            this.getNavigator().clearPathEntity();

        }
        if (this.getControllingPassenger() != null) {
            if (motionY > 0.5) {
                this.motionY = 0.5;
            }
            if (motionY < -0.5) {
                this.motionY = -0.5;
            }
        } else {
            if (motionY > 0.8) {
                this.motionY = 0.5;
            }
            if (motionY < -0.8) {
                this.motionY = -0.8;
            }
            if (motionY > 1) {
                this.motionY = 0;
            }
        }
        this.updateCheckPlayer();
        AnimationHandler.INSTANCE.updateAnimations(this);

        if ((this.isFlying() || this.isHovering()) && !this.isModelDead()) {
                if (animationCycle < 15) {
                    animationCycle++;
                } else {
                    animationCycle = 0;
                }
            if (animationCycle == 12) {
                this.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundVolume() * IceAndFire.CONFIG.dragonFlapNoiseDistance, 0.4F + this.rand.nextFloat() * 0.3F * this.getSoundPitch());
            }
            if (animationCycle > 12 && animationCycle < 15) {
                for (int i = 0; i < this.getRenderSize(); i++) {
                    for (int i1 = 0; i1 < 20; i1++) {
                        double motionX = getRNG().nextGaussian() * 0.07D;
                        double motionY = getRNG().nextGaussian() * 0.07D;
                        double motionZ = getRNG().nextGaussian() * 0.07D;
                        float radius = 0.75F * (0.7F * getRenderSize() / 3) * -3;
                        float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                        double extraZ = (double) (radius * MathHelper.cos(angle));
                        double extraY = 0.8F;

                        IBlockState iblockstate = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX + extraX), MathHelper.floor(this.posY + extraY) - 1, MathHelper.floor(this.posZ + extraZ)));
                        if (iblockstate.getMaterial() != Material.AIR) {
                            if (world.isRemote) {
                                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.posX + extraX, this.posY + extraY, this.posZ + extraZ, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                            }
                        }
                    }
                }
            }
            if (this.isModelDead() && animationCycle != 0) {
                animationCycle = 0;
            }
        }
        if (this.isModelDead() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }

        boolean sleeping = isSleeping() && !isHovering() && !isFlying();
        if (sleeping && sleepProgress < 20.0F) {
            sleepProgress += 0.5F;
        } else if (!sleeping && sleepProgress > 0.0F) {
            sleepProgress -= 0.5F;
        }
        boolean fireBreathing = isBreathingFire();
        if (fireBreathing && fireBreathProgress < 20.0F) {
            fireBreathProgress += 0.5F;
        } else if (!fireBreathing && fireBreathProgress > 0.0F) {
            fireBreathProgress -= 0.5F;
        }
        boolean hovering = isHovering();
        if (hovering && hoverProgress < 20.0F) {
            hoverProgress += 0.5F;
        } else if (!hovering && hoverProgress > 0.0F) {
            hoverProgress -= 0.5F;
        }
        boolean flying = this.isFlying() || !this.onGround && !this.isHovering() && this.airTarget != null;
        if (flying && flyProgress < 20.0F) {
            flyProgress += 0.5F;
        } else if (!flying && flyProgress > 0.0F) {
            flyProgress -= 0.5F;
        }
        boolean modeldead = isModelDead();
        if (modeldead && modelDeadProgress < 20.0F) {
            modelDeadProgress += 0.5F;
        } else if (!modeldead && modelDeadProgress > 0.0F) {
            modelDeadProgress -= 0.5F;
        }
        boolean riding = isRiding() && this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityPlayer;
        if (riding && ridingProgress < 20.0F) {
            ridingProgress += 0.5F;
        } else if (!riding && ridingProgress > 0.0F) {
            ridingProgress -= 0.5F;
        }
        if (this.isModelDead()) {
            return;
        }
        if (this.onGround && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.getControllingPassenger() != null && !this.onGround && (this.isFlying() || this.isHovering())) {
            this.motionY *= 0D;
        }
        if (this.isHovering()) {
            if (this.isSleeping()) {
                this.setHovering(false);
            }
            this.hoverTicks++;
            if (this.doesWantToLand()) {
                this.motionY -= 0.25D;
            } else {
                if (this.getControllingPassenger() == null) {
                    this.motionY += 0.08;
                }
                if (this.hoverTicks > 40) {
                    if (!this.isChild()) {
                        this.setFlying(true);
                    }
                    this.setHovering(false);
                    this.flyHovering = 0;
                    this.hoverTicks = 0;
                    this.flyTicks = 0;
                }
            }

            if (flyHovering == 0) {
                // move upwards
            }
            if (flyHovering == 1) {
                // move down
            }
            if (flyHovering == 2) {
                // stay still
            }
        }
        if (this.isSleeping()) {
            this.getNavigator().clearPathEntity();
        }
        if (!this.isFlying() && !this.isHovering() && this.airTarget != null && this.onGround) {
            this.airTarget = null;
        }
        if (this.isFlying() && this.airTarget == null && this.onGround && this.getControllingPassenger() == null) {
            this.setFlying(false);
        }

        if (this.isFlying() && getAttackTarget() == null) {
            flyAround();
        } else if (getAttackTarget() != null) {
            flyTowardsTarget();
        }
        if (this.onGround && flyTicks != 0) {
            flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand()) {
            this.setFlying(false);
            this.setHovering(!this.onGround);
            if (this.onGround) {
                flyTicks = 0;
                this.setFlying(false);
            }
            //this.motionY -= 0.26D;
        }
        if (this.isFlying()) {
            this.flyTicks++;
        }
        if ((this.isHovering() || this.isFlying()) && this.isSleeping()) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if ((properties == null || properties != null && !properties.isStone) && !world.isRemote && this.getRNG().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isSitting() && !this.isFlying() && this.getPassengers().isEmpty() && !this.isChild() && !this.isHovering() && !this.isSleeping() && this.canMove() && this.onGround) {
            this.setHovering(true);
            this.setSleeping(false);
            this.setSitting(false);
            this.flyHovering = 0;
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (getAttackTarget() != null && !this.getPassengers().isEmpty() && this.getOwner() != null && this.getPassengers().contains(this.getOwner())) {
            this.setAttackTarget(null);
        }
        this.setAgeInTicks(this.getAgeInTicks() + 1);
        if (this.getAgeInTicks() % 24000 == 0) {
            this.updateAttributes();
            this.setScale(this.getRenderSize());
        }
        if (this.getAgeInTicks() % 1200 == 0) {
            if (this.getHunger() > 0) {
                this.setHunger(this.getHunger() - 1);
            }
        }
        if ((!this.attackDecision || this.getRNG().nextInt(750) == 0) && this.getDragonStage() < 2) {
            this.attackDecision = true;
            for (int i = 0; i < 5; i++) {
                float radiusAdd = i * 0.15F;
                float headPosX = (float) (posX + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Math.cos((rotationYaw + 90) * Math.PI / 180));
                float headPosZ = (float) (posZ + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Math.sin((rotationYaw + 90) * Math.PI / 180));
                float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
                if (this.isFire && world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, headPosX, headPosY, headPosZ, 0, 0, 0);
                } else if (world.isRemote) {
                    IceAndFire.PROXY.spawnParticle("snowflake", this.world, headPosX, headPosY, headPosZ, 0, 0, 0);
                }
            }
            if (this.isFire) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
            } else {
                this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1, 1);
            }
        }
        if (this.isBreathingFire()) {
            this.fireTicks++;
            if (fireTicks > this.getDragonStage() * 12 || this.getOwner() != null && this.getPassengers().contains(this.getOwner()) && this.fireStopTicks <= 0) {
                this.setBreathingFire(false);
                this.attackDecision = true;
                fireTicks = 0;
            }
            if (fireStopTicks > 0 && this.getOwner() != null && this.getPassengers().contains(this.getOwner())) {
                fireStopTicks--;
            }
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public boolean isActuallyBreathingFire() {
        return this.fireTicks > 20 && this.isBreathingFire();
    }

    public boolean doesWantToLand() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        return this.flyTicks > 6000 || down() || flyTicks > 40 && this.flyProgress == 0 || properties != null && properties.isStone;
    }

    public abstract String getVariantName(int variant);

    public abstract String getTexture();

    public abstract String getTextureOverlay();

    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            if (this.getControllingPassenger() == null || this.getControllingPassenger() != passenger) {
                updatePreyInMouth(passenger);
            } else {
                if (this.isModelDead()) {
                    passenger.dismountRidingEntity();
                }
                float speed_walk = 0.2F;
                float speed_idle = 0.05F;
                float speed_fly = 0.35F;
                float degree_walk = 0.5F;
                float degree_idle = 0.5F;
                float degree_fly = 0.5F;
                //this.walk(BodyLower, speed_fly, (float) (degree_fly * 0.15), false, 0, 0, entity.ticksExisted, 1);
                //this.walk(BodyUpper, speed_fly, (float) (degree_fly * -0.15), false, 0, 0, entity.ticksExisted, 1);
                renderYawOffset = rotationYaw;
                this.rotationYaw = passenger.rotationYaw;
                float hoverAddition = hoverProgress * 0.02F;
                float flyAddition = -flyProgress * 0.03F;
                float flyBody = Math.max(flyProgress, hoverProgress) * 0.0065F;
                float radius = 0.7F * ((0.3F - flyBody) * getRenderSize()) + ((this.getRenderSize() / 3) * flyAddition * 0.0065F);
                float angle = (0.01745329251F * this.renderYawOffset);
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                double extraZ = (double) (radius * MathHelper.cos(angle));
                float bob0 = hoverProgress > 0 || flyProgress > 0 ? this.bob(-speed_fly, degree_fly * 5, false, this.ticksExisted, -0.0625F) : 0;
                float bob1 = this.bob(speed_walk * 2, degree_walk * 1.7F, false, this.limbSwing, this.limbSwingAmount * -0.0625F);
                float bob2 = this.bob(speed_idle, degree_idle * 1.3F, false, this.ticksExisted, -0.0625F);
                double extraY_pre = 0.8F;
                double extraY = ((extraY_pre - (hoverAddition) + (flyAddition)) * (this.getRenderSize() / 3)) - (0.35D * (1 - (this.getRenderSize() / 30))) + bob0 + bob1 + bob2;
                passenger.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
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

    private void updatePreyInMouth(Entity prey) {
        if (getControllingPassenger() != null && prey == getControllingPassenger()) {
            return;
        }
        if (this.getAnimation() == this.ANIMATION_SHAKEPREY && this.getAnimationTick() > 55 && prey != null || this.getAnimation() == NO_ANIMATION) {
            prey.attackEntityFrom(DamageSource.causeMobDamage(this), ((EntityLivingBase) prey).getMaxHealth() * 2);
            this.attackDecision = !this.attackDecision;
            prey.dismountRidingEntity();
        }
        renderYawOffset = rotationYaw;
        this.rotationYaw = prey.rotationYaw;
        prey.setPosition(this.posX, this.posY + this.getMountedYOffset() + prey.getYOffset(), this.posZ);
        float modTick_0 = this.getAnimationTick() - 15;
        float modTick_1 = this.getAnimationTick() > 15 ? 6 * MathHelper.sin((float) (Math.PI + (modTick_0 * 0.3F))) : 0;
        float modTick_2 = this.getAnimationTick() > 20 ? 10 : this.getAnimationTick() - 10;
        float radius = 0.75F * (0.7F * getRenderSize() / 3) * -3;
        float angle = (0.01745329251F * this.renderYawOffset) + 3.15F + (modTick_1 * 1.75F) * 0.05F;
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        double extraY = 0.1F * ((getRenderSize() / 3) + (modTick_2 * 0.15 * (getRenderSize() / 3)));
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

        if (dmg == DamageSource.IN_WALL || dmg == DamageSource.FALLING_BLOCK) {
                return false;
        }
        float damageReductionHead = getIntFromArmor(this.dragonInv.getStackInSlot(0)) / 3 * 0.2F;
        float damageReductionNeck = getIntFromArmor(this.dragonInv.getStackInSlot(0)) / 3 * 0.2F;
        float damageReductionBody = getIntFromArmor(this.dragonInv.getStackInSlot(0)) / 3 * 0.3F;
        float damageReductionTail = getIntFromArmor(this.dragonInv.getStackInSlot(0)) / 3 * 0.2F;
        if (i > 0) {
            this.setSitting(false);
            this.setSleeping(false);
        }
        i -= damageReductionHead + damageReductionNeck + damageReductionBody + damageReductionTail;
        return super.attackEntityFrom(dmg, i);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            this.updateClientControls();
        }
        if (this.isModelDead()) {
            return;
        }
        if (this.up()) {
            if (!this.isFlying() && !this.isHovering()) {
                this.spacebarTicks += 2;
            }
            if (this.isFlying() || this.isHovering()) {
                this.motionY += 0.4D;
            }
        } else if (this.dismount()) {
            if (this.isFlying() || this.isHovering()) {
                this.motionY -= 0.4D;
                if (this.onGround) {
                    this.setFlying(false);
                    this.setHovering(false);
                }
            }
        }
        if (!this.dismount() && (this.isFlying() || this.isHovering())) {
            this.motionY += 0.01D;
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getDragonStage() > 1) {
            this.setBreathingFire(true);
            this.riderShootFire(this.getControllingPassenger());
            this.fireStopTicks = 10;
        }
        if (this.strike() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer) {

            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.getRenderSize() / 2, this.getRenderSize() / 2, this.getRenderSize() / 2));
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(this));
            if (!list.isEmpty()) {
                entity = list.get(0);
                if (entity.getUniqueID().compareTo(this.getControllingPassenger().getUniqueID()) != 0) {
                    if (this.getAnimation() != this.ANIMATION_BITE) {
                        this.setAnimation(this.ANIMATION_BITE);
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                        this.attackDecision = false;
                    }
                }
            }

        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().isSneaking()) {
            this.getControllingPassenger().dismountRidingEntity();
        }
        if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && !this.onGround && Math.max(Math.abs(motionZ), Math.abs(motionX)) < 0.1F) {
            this.setHovering(true);
            this.setFlying(false);
        }
        if ((this.isFlying() || this.isHovering()) && this.isInWater()) {
            //this.motionY += 0.2;
        }
        if (this.isHovering() && !this.isFlying() && this.getControllingPassenger() != null && !this.onGround && Math.max(Math.abs(motionZ), Math.abs(motionX)) > 0.1F) {
            this.setFlying(true);
            this.setHovering(false);
        }
        if (this.spacebarTicks > 0) {
            this.spacebarTicks--;
        }
        if (this.spacebarTicks > 20 && this.getOwner() != null && this.getPassengers().contains(this.getOwner()) && !this.isFlying() && !this.isHovering()) {
            this.setHovering(true);
        }
        if (world.isRemote) {
            roll_buffer.calculateChainFlapBuffer(50, 10, 4, this);

        }
        if (this.getAttackTarget() != null && this.getRidingEntity() == null && this.getAttackTarget().isDead || this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityDragonBase && ((EntityDragonBase) this.getAttackTarget()).isDead) {
            this.setAttackTarget(null);
        }
        if (!world.isRemote && !this.isInWater() && !this.isSleeping() && this.onGround && !this.isFlying() && !this.isHovering() && this.getAttackTarget() == null && !this.isDaytime() && this.getRNG().nextInt(250) == 0 && this.getAttackTarget() == null && this.getPassengers().isEmpty()) {
            this.setSleeping(true);
        }
        if (this.isSleeping() && (this.isFlying() || this.isHovering() || this.isInWater() || (this.world.canBlockSeeSky(new BlockPos(this)) && this.isDaytime() && !this.isTamed() || this.isDaytime() && this.isTamed()) || this.getAttackTarget() != null || !this.getPassengers().isEmpty())) {
            this.setSleeping(false);
        }
    }

    @Override
    public void setScaleForAge(boolean par1) {
        this.setScale(Math.min(this.getRenderSize() * 0.3F, 7F));
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
        Entity entity = this.getRidingEntity();
        if (this.isRiding() && entity.isDead) {
            this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();
            if (this.isRiding()) {
                this.updateRiding(entity);
            }
        }
    }

    public void updateRiding(Entity riding) {
        if (riding.isPassenger(this) && riding instanceof EntityPlayer) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 2 ? 0F : 0.4F) + (((EntityPlayer) riding).isElytraFlying() ? 2 : 0);
            float angle = (0.01745329251F * ((EntityPlayer) riding).renderYawOffset) + (i == 1 ? -90 : i == 0 ? 90 : 0);
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            double extraY = (riding.isSneaking() ? 1.2D : 1.4D) + (i == 2 ? 0.4D : 0D);
            this.rotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.rotationYawHead = ((EntityPlayer) riding).rotationYawHead;
            this.prevRotationYaw = ((EntityPlayer) riding).rotationYawHead;
            this.setPosition(riding.posX + extraX, riding.posY + extraY, riding.posZ + extraZ);
            if (this.getControlState() == 1 << 4 || ((EntityPlayer) riding).isElytraFlying()) {
                this.dismountRidingEntity();
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
            return this.NO_ANIMATION;
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
        if (!this.isSleeping() && !this.isModelDead()) {
            if (this.getAnimation() == this.NO_ANIMATION) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.playLivingSound();
        }
    }

    protected void playHurtSound(DamageSource source) {
        if (!this.isModelDead()) {
            if (this.getAnimation() == this.NO_ANIMATION) {
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
        if (otherAnimal instanceof EntityDragonBase && super.canMateWith(otherAnimal)) {
            EntityDragonBase dragon = (EntityDragonBase) otherAnimal;
            if (this.isMale() && !dragon.isMale() || !this.isMale() && dragon.isMale()) {
                return true;
            }
        }
        return false;
    }

    public EntityDragonEgg createEgg(EntityDragonBase ageable) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        EntityDragonEgg dragon = new EntityDragonEgg(this.world);
        dragon.setType(EnumDragonEgg.byMetadata(new Random().nextInt(3) + (this.isFire ? 0 : 4)));
        dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        return dragon;
    }

    public void flyAround() {
        if (airTarget != null) {
            if (!isTargetInAir() || flyTicks > 6000 || !this.isFlying()) {
                airTarget = null;
            }
            flyTowardsTarget();
        }
    }

    public boolean isTargetBlocked(Vec3d target){
        if(target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(this.getPosition()), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (!world.isAirBlock(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void flyTowardsTarget() {
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) > 3) {
            double targetX = airTarget.getX() + 0.5D - posX;
            double targetY = Math.min(airTarget.getY(), 256) + 1D - posY;
            double targetZ = airTarget.getZ() + 0.5D - posZ;
            motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * getFlySpeed();
            motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * getFlySpeed();
            motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * getFlySpeed();
            float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
            float rotation = MathHelper.wrapDegrees(angle - rotationYaw);
            moveForward = 0.5F;
            prevRotationYaw = rotationYaw;
            rotationYaw += rotation;
            if (!this.isFlying()) {
                this.setFlying(true);
            }
        } else {
            this.airTarget = null;
        }
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) < 3 && this.doesWantToLand()) {
            this.setFlying(false);
            this.setHovering(true);
            this.flyHovering = 1;
        }
    }

    private double getFlySpeed() {
        return 2 + (this.getAgeInDays() / 125) * 2;
    }

    protected boolean isTargetInAir() {
        return airTarget != null && ((world.getBlockState(airTarget).getMaterial() == Material.AIR) || (this instanceof EntityIceDragon && (world.getBlockState(airTarget).getMaterial() == Material.WATER || world.getBlockState(airTarget).getMaterial() == Material.AIR)));
    }

    public float getDistanceSquared(Vec3d vec3d) {
        float f = (float) (this.posX - vec3d.x);
        float f1 = (float) (this.posY - vec3d.y);
        float f2 = (float) (this.posZ - vec3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }


    public boolean replaceItemInInventory(int inventorySlot, @Nullable ItemStack itemStackIn) {
        int j = inventorySlot - 500 + 2;
        if (j >= 0 && j < this.dragonInv.getSizeInventory()) {
            this.dragonInv.setInventorySlotContents(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) itemHandler;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing) {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    public abstract Item getVariantScale(int variant);

    public abstract Item getVariantEgg(int variant);

    @SideOnly(Side.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            down(mc.gameSettings.keyBindSneak.isKeyDown());
            attack(ModKeys.dragon_fireAttack.isKeyDown());
            strike(ModKeys.dragon_strike.isKeyDown());
            dismount(ModKeys.dragon_down.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState));
            }
        }
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            dismount(ModKeys.dragon_down.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState));
            }
        }
    }

    public boolean canBeSteered() {
        return true;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!this.canMove() && !this.isBeingRidden()) {
            strafe = 0;
            forward = 0;
            super.moveEntityWithHeading(strafe, forward);
            return;
        }
        if (this.isBeingRidden() && this.canBeSteered()) {
            EntityLivingBase controller = (EntityLivingBase) this.getControllingPassenger();
            if (controller != null) {
                strafe = controller.moveStrafing * 0.5F;
                forward = controller.moveForward;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }
                if (this.isFlying() || this.isHovering()) {
                    motionX *= 1.06;
                    motionZ *= 1.06;
                }
                jumpMovementFactor = 0.05F;
                this.setAIMoveSpeed(onGround ? (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() : (float) getFlySpeed());
            }
            if (this.isInWater() && this.isInsideOfMaterial(Material.AIR) && motionY > 1) {
                //this.motionY = 0;
            }
        }
        if (this.isInWater() && this.isTamed()) {
            // this.motionY += 0.02;
        }
        super.moveEntityWithHeading(strafe, forward);
    }

    public void updateCheckPlayer() {
        double checklength = this.getEntityBoundingBox().getAverageEdgeLength() * 3;
        EntityPlayer player = world.getClosestPlayerToEntity(this, checklength);
        if (!this.isTamed() && this.isSleeping()) {
            if (player != null && !this.isOwner(player) && !player.capabilities.isCreativeMode) {
                this.setSleeping(false);
                this.setSitting(false);
                this.setAttackTarget(player);
            }
        }
        EntityPlayer player1 = world.getClosestPlayerToEntity(this, (this.getRenderSize() / 2) + 15);
        if (player1 != null) {
            player1.addStat(ModAchievements.dragonEncounter, 1);
        }
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) this.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        if (entityLivingIn instanceof EntityPlayer) {
            ((EntityPlayer) entityLivingIn).addStat(ModAchievements.dragonKill, 1);
        }
    }

    public void onDeath(DamageSource cause) {
        if (cause.getTrueSource() != null) {
            if (cause.getTrueSource() instanceof EntityPlayer) {
                ((EntityPlayer) cause.getTrueSource()).addStat(ModAchievements.dragonSlayer, 1);
            }
        }
        super.onDeath(cause);
        if (dragonInv != null && !this.world.isRemote) {
            for (int i = 0; i < dragonInv.getSizeInventory(); ++i) {
                ItemStack itemstack = dragonInv.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public void onHearFlute(EntityPlayer player) {
        if(this.isTamed() && this.isOwner(player)) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
    }
}