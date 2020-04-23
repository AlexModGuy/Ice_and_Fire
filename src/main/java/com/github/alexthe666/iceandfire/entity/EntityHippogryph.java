package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityHippogryph extends EntityTameable implements ISyncMount, IAnimatedEntity, IDragonFlute, IVillagerFear, IAnimalFear, IDropArmor, IFlyingMount {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "hippogryph"));
    private static final int FLIGHT_CHANCE_PER_TICK = 1200;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SADDLE = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARMOR = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CHESTED = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVERING = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityHippogryph.class, DataSerializers.VARINT);
    public static Animation ANIMATION_EAT;
    public static Animation ANIMATION_SPEAK;
    public static Animation ANIMATION_SCRATCH;
    public static Animation ANIMATION_BITE;
    public HippogryphInventory hippogryphInventory;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer roll_buffer;
    public float sitProgress;
    public float hoverProgress;
    public float flyProgress;
    public int spacebarTicks;
    public BlockPos airTarget;
    public int airBorneCounter;
    public BlockPos homePos;
    public boolean hasHomePosition = false;
    private boolean isSitting;
    private boolean isHovering;
    private boolean isFlying;
    private int animationTick;
    private Animation currentAnimation;
    private int flyTicks;
    private int hoverTicks;
    private boolean hasChestVarChanged = false;
    private int navigatorType = -1;
    private boolean isOverAir;
    public int feedings = 0;

    public EntityHippogryph(World worldIn) {
        super(worldIn);
        ANIMATION_EAT = Animation.create(25);
        ANIMATION_SPEAK = Animation.create(15);
        ANIMATION_SCRATCH = Animation.create(25);
        ANIMATION_BITE = Animation.create(20);
        initHippogryphInv();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            roll_buffer = new IFChainBuffer();
        }
        this.setSize(1.7F, 1.6F);
        this.stepHeight = 1;
    }

    protected void switchNavigator(){
        if(this.isBeingRidden() && this.isOverAir()){
            if(navigatorType != 1){
                this.moveHelper = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
                this.navigator = new PathNavigateFlyingCreature(this, world);
                navigatorType = 1;
            }
        }
        if(!this.isBeingRidden() || !this.isOverAir()){
            if(navigatorType != 0){
                this.moveHelper = new EntityMoveHelper(this);
                this.navigator = new PathNavigateGround(this, world);
                navigatorType = 0;
            }
        }
    }

    protected boolean isOverAir() {
        return isOverAir;
    }

    private boolean isOverAirLogic() {
        return world.isAirBlock(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1, this.posZ));
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 10;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new DragonAIRide(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(3, new HippogryphAIAttackMelee(this, 1.5D, true));
        this.tasks.addTask(4, new HippogryphAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAITempt(this, 1.0D, Items.RABBIT, false));
        this.tasks.addTask(5, new EntityAITempt(this, 1.0D, Items.COOKED_RABBIT, false));
        this.tasks.addTask(6, new HippogryphAIAirTarget(this));
        this.tasks.addTask(7, new HippogryphAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new HippogryphAITargetItems(this, false));
        this.targetTasks.addTask(5, new HippogryphAITarget(this, EntityLivingBase.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && !(entity instanceof AbstractHorse) && DragonUtils.isAlive((EntityLivingBase) entity);
            }
        }));

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(ARMOR, Integer.valueOf(0));
        this.dataManager.register(SADDLE, Boolean.valueOf(false));
        this.dataManager.register(CHESTED, Boolean.valueOf(false));
        this.dataManager.register(HOVERING, Boolean.valueOf(false));
        this.dataManager.register(FLYING, Boolean.valueOf(false));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.dataManager.register(COMMAND, Integer.valueOf(0));

    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    public boolean canPassengerSteer() {
        return false;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            this.rotationYaw = passenger.rotationYaw;
        }
        passenger.setPosition(this.posX, this.posY + 1.05F, this.posZ);
    }

    private void initHippogryphInv() {
        HippogryphInventory animalchest = this.hippogryphInventory;
        this.hippogryphInventory = new HippogryphInventory("hippogryphInventory", 18, this);
        this.hippogryphInventory.setCustomName(this.getName());
        if (animalchest != null) {
            int i = Math.min(animalchest.getSizeInventory(), this.hippogryphInventory.getSizeInventory());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.hippogryphInventory.setInventorySlotContents(j, itemstack.copy());
                }
            }

            if (world.isRemote) {
                ItemStack saddle = animalchest.getStackInSlot(0);
                ItemStack chest = animalchest.getStackInSlot(1);
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(animalchest.getStackInSlot(2))));
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

    public int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.iron_hippogryph_armor) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.gold_hippogryph_armor) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.diamond_hippogryph_armor) {
            return 3;
        }
        return 0;
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        String s = TextFormatting.getTextWithoutFormattingCodes(player.getName());
        boolean isDev = s.equals("Alexthe666") || s.equals("Raptorfarian");
        if (this.isTamed() && this.isOwner(player)) {
            if (itemstack != null && itemstack.getItem() == Items.DYE && itemstack.getMetadata() == 1 && this.getEnumVariant() != EnumHippogryphTypes.ALEX && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.ALEX);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.world.spawnParticle(EnumParticleTypes.REDSTONE, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0, 0, 0);
                }
                return true;
            }
            if (itemstack != null && itemstack.getItem() == Items.DYE && itemstack.getMetadata() == 7 && this.getEnumVariant() != EnumHippogryphTypes.RAPTOR && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.RAPTOR);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0, 0, 0);
                }
                return true;
            }
            if (itemstack != null && itemstack.getItem() == Items.RABBIT_STEW && this.getGrowingAge() == 0 && !isInLove()) {
                this.setInLove(player);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return true;
            }
            if (itemstack != null && itemstack.getItem() == Items.STICK) {
                if (player.isSneaking()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.sendStatusMessage(new TextComponentTranslation("hippogryph.command.remove_home"), true);
                        return true;
                    } else {
                        BlockPos pos = new BlockPos(this);
                        this.homePos = pos;
                        this.hasHomePosition = true;
                        player.sendStatusMessage(new TextComponentTranslation("hippogryph.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                        return true;
                    }
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 1) {
                        this.setCommand(0);
                    }
                    player.sendStatusMessage(new TextComponentTranslation("hippogryph.command." + (this.getCommand() == 1 ? "sit" : "stand")), true);

                }
                return true;
            }
            if (itemstack != null && itemstack.getItem() == Items.SPECKLED_MELON && this.getEnumVariant() != EnumHippogryphTypes.DODO) {
                this.setEnumVariant(EnumHippogryphTypes.DODO);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                for (int i = 0; i < 20; i++) {
                    this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0, 0, 0);
                }
                return true;
            }
            if (itemstack != null && itemstack.getItem() instanceof ItemFood && ((ItemFood) itemstack.getItem()).isWolfsFavoriteMeat() && this.getHealth() < this.getMaxHealth()) {
                this.heal(5);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
                for (int i = 0; i < 3; i++) {
                    this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0, 0, 0, Item.getIdFromItem(itemstack.getItem()));
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return true;
            }
            if (itemstack.isEmpty()) {
                if (player.isSneaking()) {
                    this.openGUI(player);
                    return true;
                } else if (this.isSaddled() && !this.isChild() && !player.isRiding()) {
                    player.startRiding(this, true);
                    return true;
                }
            }
        }
        return super.processInteract(player, hand);
    }


    public void openGUI(EntityPlayer playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            playerEntity.openGui(IceAndFire.INSTANCE, 4, this.world, this.getEntityId(), 0, 0);
        }
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

    public boolean dismount() {
        return (dataManager.get(CONTROL_STATE).byteValue() >> 3 & 1) == 1;
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

    public void dismount(boolean dismount) {
        setStateField(3, dismount);
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
        dataManager.set(CONTROL_STATE, Byte.valueOf(state));
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

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Chested", this.isChested());
        compound.setBoolean("Saddled", this.isSaddled());
        compound.setBoolean("Hovering", this.isHovering());
        compound.setBoolean("Flying", this.isFlying());
        compound.setInteger("Armor", this.getArmor());
        compound.setInteger("Feedings", feedings);
        if (hippogryphInventory != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < this.hippogryphInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.hippogryphInventory.getStackInSlot(i);
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
        compound.setBoolean("HasHomePosition", this.hasHomePosition);
        if (homePos != null && this.hasHomePosition) {
            compound.setInteger("HomeAreaX", homePos.getX());
            compound.setInteger("HomeAreaY", homePos.getY());
            compound.setInteger("HomeAreaZ", homePos.getZ());
        }
        compound.setInteger("Command", this.getCommand());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setArmor(compound.getInteger("Armor"));
        feedings = compound.getInteger("Feedings");
        if (hippogryphInventory != null) {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                this.hippogryphInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        } else {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                this.initHippogryphInv();
                this.hippogryphInventory.setInventorySlotContents(j, new ItemStack(nbttagcompound));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(nbttagcompound)));
                ItemStack saddle = hippogryphInventory.getStackInSlot(0);
                ItemStack chest = hippogryphInventory.getStackInSlot(1);
                if (world.isRemote) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(hippogryphInventory.getStackInSlot(2))));
                }
            }
        }
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInteger("HomeAreaX") != 0 && compound.getInteger("HomeAreaY") != 0 && compound.getInteger("HomeAreaZ") != 0) {
            homePos = new BlockPos(compound.getInteger("HomeAreaX"), compound.getInteger("HomeAreaY"), compound.getInteger("HomeAreaZ"));
        }
        this.setCommand(compound.getInteger("Command"));
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public EnumHippogryphTypes getEnumVariant() {
        return EnumHippogryphTypes.values()[this.getVariant()];
    }

    public void setEnumVariant(EnumHippogryphTypes variant) {
        this.setVariant(variant.ordinal());
    }

    public boolean isSaddled() {
        return Boolean.valueOf(this.dataManager.get(SADDLE).booleanValue());
    }

    public void setSaddled(boolean saddle) {
        this.dataManager.set(SADDLE, Boolean.valueOf(saddle));
    }

    public boolean isChested() {
        return Boolean.valueOf(this.dataManager.get(CHESTED).booleanValue());
    }

    public void setChested(boolean chested) {
        this.dataManager.set(CHESTED, Boolean.valueOf(chested));
        this.hasChestVarChanged = true;
    }

    public boolean isSitting() {
        if (world.isRemote) {
            boolean isSitting = (this.dataManager.get(TAMED).byteValue() & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        }
        return isSitting;
    }

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


    public boolean isHovering() {
        if (world.isRemote) {
            return this.isHovering = Boolean.valueOf(this.dataManager.get(HOVERING).booleanValue());
        }
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        this.dataManager.set(HOVERING, Boolean.valueOf(hovering));
        if (!world.isRemote) {
            this.isHovering = Boolean.valueOf(hovering);
        }
    }

    public boolean isRidingPlayer(EntityPlayer player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Nullable
    public EntityPlayer getRidingPlayer() {
        if(this.getControllingPassenger() instanceof EntityPlayer){
            return (EntityPlayer)this.getControllingPassenger();
        }
        return null;
    }

    @Override
    public double getFlightSpeedModifier() {
        return 1;
    }

    public boolean isFlying() {
        if (world.isRemote) {
            return this.isFlying = Boolean.valueOf(this.dataManager.get(FLYING).booleanValue());
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, Boolean.valueOf(flying));
        if (!world.isRemote) {
            this.isFlying = Boolean.valueOf(flying);
        }
    }

    public int getArmor() {
        return Integer.valueOf(this.dataManager.get(ARMOR).intValue());
    }

    public void setArmor(int armorType) {
        this.dataManager.set(ARMOR, Integer.valueOf(armorType));
        double armorValue = 0;
        switch (armorType) {
            case 1:
                armorValue = 10;
                break;
            case 2:
                armorValue = 20;
                break;
            case 3:
                armorValue = 30;
        }
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(armorValue);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    public boolean canMove() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (properties != null && properties.isStone) {
            return false;
        }
        return !this.isSitting() && this.getControllingPassenger() == null && sitProgress == 0;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEnumVariant(EnumHippogryphTypes.getBiomeType(world.getBiome(this.getPosition())));
        return livingdata;
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if (this.isBeingRidden() && dmg.getTrueSource() != null && this.getControllingPassenger() != null && dmg.getTrueSource() == this.getControllingPassenger()) {
            return false;
        }
        return super.attackEntityFrom(dmg, i);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
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

    public void playLivingSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playLivingSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOGRYPH_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return IafSoundRegistry.HIPPOGRYPH_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOGRYPH_DIE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityHippogryph.ANIMATION_EAT, EntityHippogryph.ANIMATION_BITE, EntityHippogryph.ANIMATION_SPEAK, EntityHippogryph.ANIMATION_SCRATCH};
    }

    public boolean shouldDismountInWater(Entity rider) {
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) this.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    @SideOnly(Side.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            down(IafKeybindRegistry.dragon_down.isKeyDown());
            attack(IafKeybindRegistry.dragon_strike.isKeyDown());
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
    public void travel(float strafe, float forward, float vertical) {
        if (!this.canMove() && !this.isBeingRidden()) {
            strafe = 0;
            forward = 0;
            super.travel(strafe, forward, vertical);
            return;
        }
        super.travel(strafe, forward, vertical);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_SCRATCH && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
        } else {
            return true;
        }
        return false;
    }

    public EntityItem createEgg(EntityHippogryph partner) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        ItemStack stack = new ItemStack(IafItemRegistry.hippogryph_egg);
        EntityItem egg = new EntityItem(this.world, i, j, k, stack);
        return egg;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        switchNavigator();
        if(world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer){
            this.setAttackTarget(null);
        }
        if (!this.world.isRemote) {
            if (this.isSitting() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setSitting(false);
            }
            if (!this.isSitting() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setSitting(true);
            }
            if (this.isSitting()) {
                this.getNavigator().clearPath();
            }
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 8) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_SCRATCH && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 8) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                this.getAttackTarget().isAirBorne = true;
                float f = MathHelper.sqrt(0.5 * 0.5 + 0.5 * 0.5);
                this.getAttackTarget().motionX /= 2.0D;
                this.getAttackTarget().motionZ /= 2.0D;
                this.getAttackTarget().motionX -= 0.5 / (double) f * 4;
                this.getAttackTarget().motionZ -= 0.5 / (double) f * 4;

                if (this.getAttackTarget().onGround) {
                    this.getAttackTarget().motionY /= 2.0D;
                    this.getAttackTarget().motionY += 4;

                    if (this.getAttackTarget().motionY > 0.4000000059604645D) {
                        this.getAttackTarget().motionY = 0.4000000059604645D;
                    }
                }
            }
        }
        if (!world.isRemote && !this.isOverAir() && this.getNavigator().noPath() && this.getAttackTarget() != null && this.getAttackTarget().posY - 3 > this.posY && this.getRNG().nextInt(15) == 0 && this.canMove() && !this.isHovering() && !this.isFlying()) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (properties != null && properties.isStone) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isOverAir()) {
            airBorneCounter++;
        } else {
            airBorneCounter = 0;
        }
        if (hasChestVarChanged && hippogryphInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!hippogryphInventory.getStackInSlot(i).isEmpty()) {
                    if (!world.isRemote) {
                        this.entityDropItem(hippogryphInventory.getStackInSlot(i), 1);
                    }
                    hippogryphInventory.removeStackFromSlot(i);
                }
            }
            hasChestVarChanged = false;
        }
        if (this.isOverAir() && this.airTarget != null) {
            this.setFlying(true);
        }
        if (this.isFlying() && this.ticksExisted % 40 == 0 || this.isFlying() && this.isSitting()) {
            this.setFlying(true);
        }
        if (!this.canMove() && this.getAttackTarget() != null) {
            this.setAttackTarget(null);
        }
        if (!this.canMove()) {
            this.getNavigator().clearPath();

        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        boolean sitting = isSitting() && !isHovering() && !isFlying();
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }

        boolean hovering = isHovering();
        if (hovering && hoverProgress < 20.0F) {
            hoverProgress += 0.5F;
        } else if (!hovering && hoverProgress > 0.0F) {
            hoverProgress -= 0.5F;
        }
        boolean flying = this.isFlying() || !this.isHovering() && airBorneCounter > 10;
        if (flying && flyProgress < 20.0F) {
            flyProgress += 0.5F;
        } else if (!flying && flyProgress > 0.0F) {
            flyProgress -= 0.5F;
        }
        if ((flying || hovering) && ticksExisted % 20 == 0 && this.isOverAir()) {
            this.playSound(SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundVolume() * (IceAndFire.CONFIG.dragonFlapNoiseDistance / 2), 0.6F + this.rand.nextFloat() * 0.6F * this.getSoundPitch());
        }
        if (!this.isOverAir() && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isHovering()) {
            if (this.isSitting()) {
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
                    this.hoverTicks = 0;
                    this.flyTicks = 0;
                }
            }
        }
        if (this.isSitting()) {
            this.getNavigator().clearPath();
        }
        if (!this.isFlying() && !this.isHovering() && this.airTarget != null && !this.isOverAir()) {
            this.airTarget = null;
        }
        if (this.isFlying() && this.airTarget == null && !this.isOverAir() && this.getControllingPassenger() == null) {
            this.setFlying(false);
        }

        if (this.isFlying() && getAttackTarget() == null) {
            flyAround();
        } else if (getAttackTarget() != null) {
            flyTowardsTarget();
        }
        if (!this.isOverAir() && flyTicks != 0) {
            flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand() && this.getControllingPassenger() == null) {
            this.setFlying(false);
            this.setHovering(false);
            if (!this.isOverAir()) {
                flyTicks = 0;
                this.setFlying(false);
            }
        }
        if (this.isFlying()) {
            this.flyTicks++;
        }
        if ((this.isHovering() || this.isFlying()) && this.isSitting()) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if ((properties == null || properties != null && !properties.isStone) && (!world.isRemote && this.getRNG().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isSitting() && !this.isFlying() && this.getPassengers().isEmpty() && !this.isChild() && !this.isHovering() && !this.isSitting() && this.canMove() && !this.isOverAir() || this.posY < -1)) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (getAttackTarget() != null && !this.getPassengers().isEmpty() && this.getOwner() != null && this.getPassengers().contains(this.getOwner())) {
            this.setAttackTarget(null);
        }
    }

    public boolean doesWantToLand() {
        return this.flyTicks > 2000 || down() || flyTicks > 40 && this.flyProgress == 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        isOverAir = this.isOverAirLogic();
        if (world.isRemote) {
            this.updateClientControls();
        }
        if (this.up()) {
            if (this.airBorneCounter == 0) {
                this.motionY += 1;
            }
            if (!this.isFlying() && !this.isHovering()) {
                this.spacebarTicks += 2;
            }
        } else if (this.dismount()) {
            if (this.isFlying() || this.isHovering()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer) {

            EntityLivingBase target = DragonUtils.riderLookingAtEntity(this, (EntityPlayer) this.getControllingPassenger(), 3);
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_SCRATCH) {
                this.setAnimation(this.getRNG().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            }
            if (target != null && this.getAnimationTick() >= 10 && this.getAnimationTick() < 13) {
                target.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().isSneaking()) {
            this.getControllingPassenger().dismountRidingEntity();
        }
        if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && this.isOverAir() && Math.max(Math.abs(motionZ), Math.abs(motionX)) < 0.1F) {
            this.setHovering(true);
            this.setFlying(false);
        }
        if (this.isHovering() && !this.isFlying() && this.getControllingPassenger() != null && this.isOverAir() && Math.max(Math.abs(motionZ), Math.abs(motionX)) > 0.1F) {
            this.setFlying(true);
            this.setHovering(false);
        }
        if (this.spacebarTicks > 0) {
            this.spacebarTicks--;
        }
        if (this.spacebarTicks > 10 && this.getOwner() != null && this.getPassengers().contains(this.getOwner()) && !this.isFlying() && !this.isHovering()) {
            this.setHovering(true);
        }
        if (world.isRemote) {
            roll_buffer.calculateChainFlapBuffer(35, 8, 6, this);
        }
        if (this.getAttackTarget() != null && this.getRidingEntity() == null && this.getAttackTarget().isDead || this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityDragonBase && ((EntityDragonBase) this.getAttackTarget()).isDead) {
            this.setAttackTarget(null);
        }
    }

    public void flyAround() {
        if (airTarget != null && this.isFlying()) {
            if (!isTargetInAir() || flyTicks > 6000 || !this.isFlying()) {
                airTarget = null;
            }
            flyTowardsTarget();
        }
    }

    public boolean isTargetBlocked(Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(this.getPosition()), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                return !world.isAirBlock(pos);
            }
        }
        return false;
    }

    public void flyTowardsTarget() {
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) > 3) {
            double targetX = airTarget.getX() + 0.5D - posX;
            double targetY = Math.min(airTarget.getY(), 256) + 1D - posY;
            double targetZ = airTarget.getZ() + 0.5D - posZ;
            motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * 2;
            motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * 2;
            motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * 2;
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
        if (airTarget != null && this.isFlying() && this.doesWantToLand()) {
            this.setFlying(false);
            this.setHovering(false);
        }
    }

    protected boolean isTargetInAir() {
        return airTarget != null && ((world.getBlockState(airTarget).getMaterial() == Material.AIR) || world.getBlockState(airTarget).getMaterial() == Material.AIR);
    }

    public float getDistanceSquared(Vec3d vec3d) {
        float f = (float) (this.posX - vec3d.x);
        float f1 = (float) (this.posY - vec3d.y);
        float f2 = (float) (this.posZ - vec3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    public boolean replaceItemInInventory(int inventorySlot, @Nullable ItemStack itemStackIn) {
        int j = inventorySlot - 500 + 2;
        if (j >= 0 && j < this.hippogryphInventory.getSizeInventory()) {
            this.hippogryphInventory.setInventorySlotContents(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (hippogryphInventory != null && !this.world.isRemote) {
            for (int i = 0; i < hippogryphInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = hippogryphInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    public void refreshInventory() {
        ItemStack saddle = this.hippogryphInventory.getStackInSlot(0);
        ItemStack chest = this.hippogryphInventory.getStackInSlot(1);
        this.setSaddled(saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
        this.setChested(chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty());
        this.setArmor(getIntFromArmor(this.hippogryphInventory.getStackInSlot(2)));
        if (this.world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(this.hippogryphInventory.getStackInSlot(2))));
        }
    }

    @Override
    public void onHearFlute(EntityPlayer player) {
        if (this.isTamed() && this.isOwner(player)) {
            if (this.isFlying() || this.isHovering()) {
                this.airTarget = null;
                this.setFlying(false);
                this.setHovering(false);
            }
        }
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    public void dropArmor() {
        if (hippogryphInventory != null && !this.world.isRemote) {
            for (int i = 0; i < hippogryphInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = hippogryphInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public class HippogryphInventory extends InventoryBasic {

        public HippogryphInventory(String inventoryTitle, int slotCount, EntityHippogryph hippogryph) {
            super(inventoryTitle, false, slotCount);
            this.addInventoryChangeListener(new HippogryphInventoryListener(hippogryph));
        }


    }

    class HippogryphInventoryListener implements IInventoryChangedListener {

        EntityHippogryph hippogryph;

        public HippogryphInventoryListener(EntityHippogryph hippogryph) {
            this.hippogryph = hippogryph;
        }

        @Override
        public void onInventoryChanged(IInventory invBasic) {
            hippogryph.refreshInventory();
        }
    }
}
