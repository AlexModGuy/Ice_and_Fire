package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateAmphibious;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityHippocampus extends TameableEntity implements ISyncMount, IAnimatedEntity, IDropArmor {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "hippocampus"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityHippocampus.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SADDLE = EntityDataManager.createKey(EntityHippocampus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARMOR = EntityDataManager.createKey(EntityHippocampus.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CHESTED = EntityDataManager.createKey(EntityHippocampus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityHippocampus.class, DataSerializers.BYTE);
    public static Animation ANIMATION_SPEAK;
    public float onLandProgress;
    @OnlyIn(Dist.CLIENT)
    public ChainBuffer tail_buffer;
    public EntityHippocampus.HippocampusInventory hippocampusInventory;
    public float sitProgress;
    public int airBorneCounter;
    private int animationTick;
    private Animation currentAnimation;
    private boolean isLandNavigator;
    private boolean isSitting;
    private boolean hasChestVarChanged = false;

    public EntityHippocampus(World worldIn) {
        super(worldIn);
        this.stepHeight = 1;
        this.spawnableBlock = Blocks.WATER;
        ANIMATION_SPEAK = Animation.create(15);
        this.switchNavigator(true);
        this.goalSelector.addGoal(0, new HippocampusAIRide(this));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, Item.getItemFromBlock(Blocks.SPONGE), false));
        this.goalSelector.addGoal(0, new AquaticAITempt(this, 1.0D, Items.PRISMARINE_CRYSTALS, false));
        this.goalSelector.addGoal(1, new AquaticAIFindWaterTarget(this, 10, true));
        this.goalSelector.addGoal(2, new AquaticAIGetInWater(this, 1.0D));
        this.goalSelector.addGoal(3, new HippocampusAIWander(this, 1));
        this.goalSelector.addGoal(4, new EntityAIMate(this, 1.0D));
        if (FMLCommonHandler.instance().getSide().isClient()) {
            tail_buffer = new ChainBuffer();
        }
        initHippocampusInv();
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return 2;
    }

    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos.down()).getMaterial() == Material.WATER ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getBoundingBox(), this);
    }

    public boolean isPushedByWater() {
        return false;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveController = new EntityMoveHelper(this);
            this.navigator = new PathNavigateAmphibious(this, world);
            this.isLandNavigator = true;
        } else {
            this.moveController = new EntityHippocampus.SwimmingMoveHelper();
            this.navigator = new PathNavigateSwimmer(this, world);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(ARMOR, Integer.valueOf(0));
        this.dataManager.register(SADDLE, Boolean.valueOf(false));
        this.dataManager.register(CHESTED, Boolean.valueOf(false));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
    }

    private void initHippocampusInv() {
        EntityHippocampus.HippocampusInventory animalchest = this.hippocampusInventory;
        this.hippocampusInventory = new EntityHippocampus.HippocampusInventory("hippocampusInventory", 18, this);
        this.hippocampusInventory.setCustomName(this.getName());
        if (animalchest != null) {
            int i = Math.min(animalchest.getSizeInventory(), this.hippocampusInventory.getSizeInventory());
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = animalchest.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.hippocampusInventory.setInventorySlotContents(j, itemstack.copy());
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
            if (passenger instanceof PlayerEntity && this.getAttackTarget() != passenger) {
                PlayerEntity player = (PlayerEntity) passenger;
                return player;
            }
        }
        return null;
    }

    public int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.IRON_HORSE_ARMOR) {
            return 1;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            return 2;
        }
        if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
            return 3;
        }
        return 0;
    }

    public boolean replaceItemInInventory(int inventorySlot, @Nullable ItemStack itemStackIn) {
        int j = inventorySlot - 500 + 2;
        if (j >= 0 && j < this.hippocampusInventory.getSizeInventory()) {
            this.hippocampusInventory.setInventorySlotContents(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (hippocampusInventory != null && !this.world.isRemote) {
            for (int i = 0; i < hippocampusInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = hippocampusInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
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

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            renderYawOffset = rotationYaw;
            this.rotationYaw = passenger.rotationYaw;
        }
        double ymod1 = this.onLandProgress * -0.02;
        passenger.setPosition(this.getPosX(), this.getPosY() + 0.6F + ymod1, this.getPosZ());
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (getControllingPassenger() != null && getControllingPassenger() instanceof LivingEntity && this.ticksExisted % 20 == 0) {
            ((LivingEntity) getControllingPassenger()).addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 30, 0, true, false));
        }
        if (!this.onGround) {
            airBorneCounter++;
        } else {
            airBorneCounter = 0;
        }
        if (world.isRemote) {
            this.updateClientControls();
        }
        if (world.isRemote) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 1F, this);
        }
        if (this.up()) {
            if (!this.isInWater() && this.airBorneCounter == 0 && this.onGround) {
                this.jump();
            } else if (this.isInWater()) {
                this.motionY += 0.4D;
            }
        }
        if (this.down()) {
            this.motionY -= 0.4D;
        }
        if (this.isInWater() && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!this.isInWater() && !this.isLandNavigator) {
            switchNavigator(true);
        }
        boolean inWater = !this.isInWater();
        if (inWater && onLandProgress < 20.0F) {
            onLandProgress += 1F;
        } else if (!inWater && onLandProgress > 0.0F) {
            onLandProgress -= 1F;
        }
        boolean sitting = isSitting();
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }
        if (hasChestVarChanged && hippocampusInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!hippocampusInventory.getStackInSlot(i).isEmpty()) {
                    if (!world.isRemote) {
                        this.entityDropItem(hippocampusInventory.getStackInSlot(i), 1);
                    }
                    hippocampusInventory.removeStackFromSlot(i);
                }
            }
            hasChestVarChanged = false;
        }
    }

    public boolean up() {
        return (Byte.valueOf(dataManager.get(CONTROL_STATE).byteValue()) & 1) == 1;
    }

    public boolean down() {
        return (Byte.valueOf(dataManager.get(CONTROL_STATE).byteValue()) >> 1 & 1) == 1;
    }

    public boolean dismount() {
        return (Byte.valueOf(dataManager.get(CONTROL_STATE).byteValue()) >> 2 & 1) == 1;
    }


    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    public boolean getCanSpawnHere() {
        return this.getPosY() > 30 && this.getPosY() < (double) this.world.getSeaLevel();
    }

    @Override
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.putInt("Variant", this.getVariant());
        compound.setBoolean("Chested", this.isChested());
        compound.setBoolean("Saddled", this.isSaddled());
        compound.putInt("Armor", this.getArmor());
        if (hippocampusInventory != null) {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < this.hippocampusInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.hippocampusInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    CompoundNBT CompoundNBT = new CompoundNBT();
                    CompoundNBT.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(CompoundNBT);
                    nbttaglist.appendTag(CompoundNBT);
                }
            }
            compound.setTag("Items", nbttaglist);
        }
        if (this.getCustomNameTag() != null && !this.getCustomNameTag().isEmpty()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }

    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setArmor(compound.getInt("Armor"));
        if (hippocampusInventory != null) {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initHippocampusInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompoundTagAt(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.hippocampusInventory.setInventorySlotContents(j, new ItemStack(CompoundNBT));
            }
        } else {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            this.initHippocampusInv();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                CompoundNBT CompoundNBT = nbttaglist.getCompoundTagAt(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initHippocampusInv();
                this.hippocampusInventory.setInventorySlotContents(j, new ItemStack(CompoundNBT));
                //this.setArmorInSlot(j, this.getIntFromArmor(ItemStack.loadItemStackFromNBT(CompoundNBT)));
                ItemStack saddle = hippocampusInventory.getStackInSlot(0);
                ItemStack chest = hippocampusInventory.getStackInSlot(1);
                if (world.isRemote) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(hippocampusInventory.getStackInSlot(2))));
                }
            }
        }
    }


    public boolean isSaddled() {
        return this.dataManager.get(SADDLE).booleanValue();
    }

    public void setSaddled(boolean saddle) {
        this.dataManager.set(SADDLE, saddle);
    }

    public boolean isChested() {
        return this.dataManager.get(CHESTED).booleanValue();
    }

    public void setChested(boolean chested) {
        this.dataManager.set(CHESTED, chested);
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

    public int getArmor() {
        return this.dataManager.get(ARMOR).intValue();
    }

    public void setArmor(int armorType) {
        this.dataManager.set(ARMOR, armorType);
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
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(armorValue);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT).intValue();
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, @Nullable ILivingEntityData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(6));
        return livingdata;
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
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, ANIMATION_SPEAK};
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        if (ageable instanceof EntityHippocampus) {
            EntityHippocampus hippo = new EntityHippocampus(this.world);
            hippo.setVariant(this.getRNG().nextBoolean() ? this.getVariant() : ((EntityHippocampus) ageable).getVariant());
            return hippo;
        }
        return null;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return super.isInWater() || this.isInsideOfMaterial(Material.WATER) || this.isInsideOfMaterial(Material.CORAL);
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        float f4;
        if (this.isSitting()) {
            super.travel(0, 0, 0);
            return;
        }
        if (this.isServerWorld()) {
            float f5;
            if (this.isInWater()) {
                this.moveRelative(strafe, vertical, forward, 0.1F);
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
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double) f4;
                this.motionX *= 0.900000011920929D;
                this.motionY *= 0.900000011920929D;
                this.motionY *= (double) f4;
                this.motionZ *= 0.900000011920929D;
                this.motionZ *= (double) f4;
            } else {
                super.travel(strafe, vertical, forward);
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

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.PRISMARINE_CRYSTALS;
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

    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack != null && itemstack.getItem() == Items.PRISMARINE_CRYSTALS && this.getGrowingAge() == 0 && !isInLove()) {
            this.setSitting(false);
            this.setInLove(player);
            this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return true;
        }
        if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 0) {
            if (!world.isRemote) {
                this.heal(5);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
                for (int i = 0; i < 3; i++) {
                    this.world.spawnParticle(ParticleTypes.ITEM_CRACK, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.height), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), 0, 0, 0, Item.getIdFromItem(itemstack.getItem()));
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            if (!this.isTamed() && this.getRNG().nextInt(3) == 0) {
                this.setTamedBy(player);
                for (int i = 0; i < 6; i++) {
                    this.world.spawnParticle(ParticleTypes.HEART, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.height), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), 0, 0, 0);
                }
            }
            return true;

        }
        if (isOwner(player) && itemstack != null && itemstack.getItem() == Items.PRISMARINE_CRYSTALS && this.getGrowingAge() == 0 && !isInLove()) {
            this.setSitting(false);
            this.setInLove(player);
            this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return true;
        }
        if (isOwner(player) && itemstack != null && itemstack.getItem() == Items.STICK) {
            this.setSitting(!this.isSitting());
            return true;
        }
        if (isOwner(player) && itemstack.isEmpty()) {
            if (player.isSneaking()) {
                this.openGUI(player);
                return true;
            } else if (this.isSaddled() && !this.isChild() && !player.isRiding()) {
                player.startRiding(this, true);
                return true;
            }
        }
        return super.processInteract(player, hand);
    }

    public void openGUI(PlayerEntity playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity))) {
            playerEntity.openGui(IceAndFire.INSTANCE, 5, this.world, this.getEntityId(), 0, 0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getInstance();
        if (this.isRidingPlayer(mc.player)) {
            byte previousState = getControlState();
            up(mc.gameSettings.keyBindJump.isKeyDown());
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            down(IafKeybindRegistry.dragon_down.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
            }
        }
        if (this.getRidingEntity() != null && this.getRidingEntity() == mc.player) {
            byte previousState = getControlState();
            dismount(mc.gameSettings.keyBindSneak.isKeyDown());
            down(IafKeybindRegistry.dragon_down.isKeyDown());
            byte controlState = getControlState();
            if (controlState != previousState) {
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(this.getEntityId(), controlState, posX, posY, posZ));
            }
        }
    }

    public void up(boolean up) {
        setStateField(0, up);
    }

    public void down(boolean down) {
        setStateField(1, down);
    }

    public void dismount(boolean dismount) {
        setStateField(2, dismount);
    }

    public void refreshInventory() {
        ItemStack saddle = this.hippocampusInventory.getStackInSlot(0);
        ItemStack chest = this.hippocampusInventory.getStackInSlot(1);
        this.setSaddled(saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
        this.setChested(chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty());
        this.setArmor(getIntFromArmor(this.hippocampusInventory.getStackInSlot(2)));
        if (this.world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 1, chest != null && chest.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !chest.isEmpty() ? 1 : 0));
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.getEntityId(), 2, this.getIntFromArmor(this.hippocampusInventory.getStackInSlot(2))));
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOCAMPUS_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return IafSoundRegistry.HIPPOCAMPUS_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOCAMPUS_DIE;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    public void dropArmor() {
        if (hippocampusInventory != null && !this.world.isRemote) {
            for (int i = 0; i < hippocampusInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = hippocampusInventory.getStackInSlot(i);
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
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean canPassengerSteer() {
        return false;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    public boolean isRidingPlayer(PlayerEntity player) {
        return getRidingPlayer() != null && player != null && getRidingPlayer().getUniqueID().equals(player.getUniqueID());
    }

    @Nullable
    public PlayerEntity getRidingPlayer() {
        if(this.getControllingPassenger() instanceof PlayerEntity){
            return (PlayerEntity)this.getControllingPassenger();
        }
        return null;
    }

    public double getRideSpeedModifier() {
        return this.isInWater() ? 1.2F : 0.55F;
    }

    class SwimmingMoveHelper extends EntityMoveHelper {
        private EntityHippocampus hippo = EntityHippocampus.this;

        public SwimmingMoveHelper() {
            super(EntityHippocampus.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if(this.hippo.isBeingRidden()){
                double flySpeed = 0.8F * hippo.getRideSpeedModifier();
                Vec3d dragonVec = hippo.getPositionVector();
                Vec3d moveVec = new Vec3d(posX, posY, posZ);
                Vec3d normalized = moveVec.subtract(dragonVec).normalize();
                double dist = dragonVec.distanceTo(moveVec);
                hippo.motionX = normalized.x * flySpeed;
                hippo.motionY = normalized.y * flySpeed;
                hippo.motionZ = normalized.z * flySpeed;
                if (dist > 2.5E-7) {
                    float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
                    hippo.rotationYaw = limitAngle(hippo.rotationYaw, yaw, 5);
                    entity.setAIMoveSpeed((float)(speed));
                }
                hippo.move(MoverType.SELF, hippo.motionX, hippo.motionY, hippo.motionZ);
            }else if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.hippo.getNavigator().noPath()) {
                double distanceX = this.getPosX() - this.hippo.getPosX();
                double distanceY = this.getPosY() - this.hippo.getPosY();
                double distanceZ = this.getPosZ() - this.hippo.getPosZ();
                double distance = Math.abs(distanceX * distanceX + distanceZ * distanceZ);
                double distanceWithY = (double) MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distanceY = distanceY / distanceWithY;
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.hippo.rotationYaw = this.limitAngle(this.hippo.rotationYaw, angle, 30.0F);
                this.hippo.setAIMoveSpeed(1F);
                this.hippo.motionY += (double) this.hippo.getAIMoveSpeed() * distanceY * 0.1D;
                if (distance < (double) Math.max(1.0F, this.entity.getWidth())) {
                    float f = this.hippo.rotationYaw * 0.017453292F;
                    this.hippo.motionX -= (double) (MathHelper.sin(f) * 0.35F);
                    this.hippo.motionZ += (double) (MathHelper.cos(f) * 0.35F);
                }
            } else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                if (this.entity.onGround) {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            } else {
                this.hippo.setAIMoveSpeed(0.0F);
            }
        }
    }

    public class HippocampusInventory extends ContainerHorseChest {

        public HippocampusInventory(String inventoryTitle, int slotCount, EntityHippocampus hippocampus) {
            super(inventoryTitle, slotCount);
            this.addInventoryChangeListener(new EntityHippocampus.HippocampusInventoryListener(hippocampus));
        }
    }

    class HippocampusInventoryListener implements IInventoryChangedListener {
        EntityHippocampus hippocampus;

        public HippocampusInventoryListener(EntityHippocampus hippocampus) {
            this.hippocampus = hippocampus;
        }

        @Override
        public void onInventoryChanged(IInventory invBasic) {
            hippocampus.refreshInventory();
        }
    }
}
