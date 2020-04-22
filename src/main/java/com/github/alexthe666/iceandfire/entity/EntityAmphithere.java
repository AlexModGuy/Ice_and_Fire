package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityAmphithere extends EntityTameable implements ISyncMount, IAnimatedEntity, IPhasesThroughBlock, IFlapable, IDragonFlute, IFlyingMount {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "amphithere"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FLAP_TICKS = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CONTROL_STATE = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.VARINT);
    public static Animation ANIMATION_BITE = Animation.create(15);
    public static Animation ANIMATION_BITE_RIDER = Animation.create(15);
    public static Animation ANIMATION_WING_BLAST = Animation.create(30);
    public static Animation ANIMATION_TAIL_WHIP = Animation.create(30);
    public static Animation ANIMATION_SPEAK = Animation.create(10);
    public float flapProgress;
    public float groundProgress = 0;
    public float sitProgress = 0;
    public float diveProgress = 0;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer roll_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer tail_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer pitch_buffer;
    @Nullable
    public BlockPos orbitPos = null;
    public float orbitRadius = 0.0F;
    public boolean isFallen;
    public BlockPos homePos;
    public boolean hasHomePosition = false;
    protected FlightBehavior flightBehavior = FlightBehavior.WANDER;
    protected int ticksCircling = 0;
    private int animationTick;
    private Animation currentAnimation;
    private int flapTicks = 0;
    private int flightCooldown = 0;
    private int ticksFlying = 0;
    private boolean isFlying;
    private boolean changedFlightBehavior = false;
    private int ticksStill = 0;
    private int ridingTime = 0;
    private boolean isSitting;
    /*
          0 = ground/walking
          1 = ai flight
          2 = controlled flight
       */
    private int navigatorType = 0;

    public EntityAmphithere(World worldIn) {
        super(worldIn);
        this.setSize(2.5F, 1.25F);
        this.stepHeight = 1;
        if (FMLCommonHandler.instance().getSide().isClient()) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            tail_buffer = new IFChainBuffer();
        }
        switchNavigator(0);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.posY, z);
        for (int yDown = 0; yDown < 6 + rand.nextInt(6); yDown++) {
            if (!world.isAirBlock(pos.down(yDown))) {
                return pos.up(yDown);
            }
        }
        return pos;
    }

    public static BlockPos getPositionInOrbit(EntityAmphithere entity, World world, BlockPos orbit, Random rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 10;
        if (entity.getCommand() == 2) {
            if (entity.getOwner() != null) {
                orbit = entity.getOwner().getPosition().up(7);
                radius = 5;
            }
        } else if (entity.hasHomePosition) {
            orbit = entity.homePos.up(30);
            radius = 30;
        }
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        BlockPos radialPos = new BlockPos(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        //world.setBlockState(radialPos.down(4), Blocks.QUARTZ_BLOCK.getDefaultState());
        // world.setBlockState(orbit.down(4), Blocks.GOLD_BLOCK.getDefaultState());
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    public float getBlockPathWeight(BlockPos pos) {
        if (this.isFlying()) {
            if (world.isAirBlock(pos)) {
                return 10F;
            } else {
                return 0F;
            }
        } else {
            return super.getBlockPathWeight(pos);
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        if (itemstack != null && itemstack.getItem() == Items.COOKIE) {
            if (this.getGrowingAge() == 0 && !isInLove()) {
                this.setSitting(false);
                this.setInLove(player);
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            return true;
        }
        if (itemstack != null && itemstack.getItem() == Items.DYE && itemstack.getItemDamage() == EnumDyeColor.BROWN.getDyeDamage() && this.getHealth() < this.getMaxHealth()) {
            this.heal(5);
            this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return true;
        }
        if (!super.processInteract(player, hand)) {
            if (itemstack != null && itemstack.getItem() == IafItemRegistry.dragon_stick && this.isOwner(player)) {
                if (player.isSneaking()) {
                    BlockPos pos = new BlockPos(this);
                    this.homePos = pos;
                    this.hasHomePosition = true;
                    player.sendStatusMessage(new TextComponentTranslation("amphithere.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                    return true;
                }
                return true;
            }
            if (player.isSneaking() && this.isOwner(player)) {
                if (player.getHeldItem(hand).isEmpty()) {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() > 2) {
                        this.setCommand(0);
                    }
                    player.sendStatusMessage(new TextComponentTranslation("amphithere.command." + this.getCommand()), true);
                    this.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
                    return true;
                }
                return true;
            } else if ((!this.isTamed() || this.isOwner(player)) && !this.isChild()) {
                player.startRiding(this);
                return true;
            }

        }
        return true;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new DragonAIRide(this));
        this.tasks.addTask(0, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AmphithereAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new AmphithereAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(3, new AmphithereAIFleePlayer(this, 32.0F, 0.8D, 1.8D));
        this.tasks.addTask(3, new AIFlyWander());
        this.tasks.addTask(3, new AIFlyCircle());
        this.tasks.addTask(3, new AILandWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosestIgnoreRider(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new AmphithereAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new AmphithereAITargetItems(this, false));
    }

    public boolean isStill() {
        return Math.abs(this.motionX) < 0.05 && Math.abs(this.motionZ) < 0.05;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateClimber(this, world);
            this.navigatorType = 0;
        } else if(navigatorType == 1) {
            this.moveHelper = new EntityAmphithere.FlyMoveHelper(this);
            this.navigator = new PathNavigateFlyingCreature(this, world);
            this.navigatorType = 1;
        }else{
            this.moveHelper = new IafDragonFlightManager.PlayerFlightMoveHelper(this);
            this.navigator = new PathNavigateFlyingCreature(this, world);
            this.navigatorType = 2;
        }
    }

    public boolean onLeaves() {
        IBlockState state = world.getBlockState(this.getPosition().down());
        return state.getBlock().isLeaves(state, world, this.getPosition().down());
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (!this.isTamed() && this.isFlying() && !onGround && source.isProjectile() && !world.isRemote) {
            this.isFallen = true;
        }
        if(source.getTrueSource() instanceof EntityLivingBase && this.isTamed() && this.isOwner((EntityLivingBase) source.getTrueSource())){
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger) && this.isTamed()) {
            this.rotationYaw = passenger.rotationYaw;
            //renderYawOffset = rotationYaw;
        }
        if (!this.world.isRemote && !this.isTamed() && passenger instanceof EntityPlayer && this.getAnimation() == NO_ANIMATION && rand.nextInt(15) == 0) {
            this.setAnimation(ANIMATION_BITE_RIDER);
        }
        if (!this.world.isRemote && this.getAnimation() == ANIMATION_BITE_RIDER && this.getAnimationTick() == 6 && !this.isTamed()) {
            passenger.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
        }
        float pitch_forward = 0;
        if (this.rotationPitch > 0 && this.isFlying()) {
            pitch_forward = (rotationPitch / 45F) * 0.45F;
        } else {
            pitch_forward = 0;
        }
        float scaled_ground = this.groundProgress * 0.1F;
        float radius = (this.isTamed() ? 0.5F : 0.3F) - scaled_ground * 0.5F + pitch_forward;
        float angle = (0.01745329251F * this.renderYawOffset);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        passenger.setPosition(this.posX + extraX, this.posY + 0.7F - scaled_ground * 0.14F + pitch_forward, this.posZ + extraZ);

    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.COOKIE;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer) {
            this.setAttackTarget(null);
        }
        if(this.isInWater() && this.isJumping){
            this.motionY += 0.1D;
        }
        if(this.isChild() && this.getAttackTarget() != null){
            this.setAttackTarget(null);
        }
        if (this.isInLove()) {
            this.setFlying(false);
        }
        if (this.isSitting() && this.getAttackTarget() != null) {
            this.setAttackTarget(null);
        }
        boolean flapping = this.isFlapping();
        boolean flying = this.isFlying() && this.isOverAir() || (this.isOverAir() && !onLeaves());
        boolean diving = flying && this.motionY <= -0.1F || this.isFallen;
        boolean sitting = isSitting() && !isFlying();
        boolean notGrounded = flying || this.getAnimation() == ANIMATION_WING_BLAST;
        if (!world.isRemote) {
            if (this.isSitting() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setSitting(false);
            }
            if (!this.isSitting() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setSitting(true);
            }
            if (this.isSitting()) {
                this.getNavigator().clearPath();
                this.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
            }
            if (flying) {
                ticksFlying++;
            } else {
                ticksFlying = 0;
            }
        }
        if(isFlying() && this.onGround){
            this.setFlying(false);
        }
        if (sitting && sitProgress < 20.0F) {
            sitProgress += 0.5F;
        } else if (!sitting && sitProgress > 0.0F) {
            sitProgress -= 0.5F;
        }
        if (flightCooldown > 0) {
            flightCooldown--;
        }
        if (!world.isRemote) {
            if (this.flightBehavior == FlightBehavior.CIRCLE) {
                ticksCircling++;
            } else {
                ticksCircling = 0;
            }
        }
        if (this.getUntamedRider() != null && !this.isTamed()) {
            ridingTime++;
        }
        if (this.getUntamedRider() == null) {
            ridingTime = 0;
        }
        if (!this.isTamed() && ridingTime > IceAndFire.CONFIG.amphithereTameTime && this.getUntamedRider() != null && this.getUntamedRider() instanceof EntityPlayer) {
            this.world.setEntityState(this, (byte) 45);
            this.setTamedBy((EntityPlayer) this.getUntamedRider());
        }
        if (world.isRemote) {
            this.updateClientControls();
        }
        if (isStill()) {
            this.ticksStill++;
        } else {
            this.ticksStill = 0;
        }
        if (!this.isFlying() && !this.isChild() && ((this.onGround && this.rand.nextInt(200) == 0 && flightCooldown == 0 && this.getPassengers().isEmpty() && !this.isAIDisabled() && canMove()) || this.posY < -1)) {
            this.motionY += 0.5F;
            this.setFlying(true);
        }
        if (this.getControllingPassenger() != null && this.isFlying() && !this.onGround) {
            this.rotationPitch = this.getControllingPassenger().rotationPitch / 2;

            if (this.getControllingPassenger().rotationPitch > 25 && this.motionY > -1.0F) {
                if (this.motionY > 0) {
                    this.motionY = 0;
                }
                this.motionY -= 0.1D;
            }
            if (this.getControllingPassenger().rotationPitch < -25 && this.motionY < 1.0F) {
                if (this.motionY < 0) {
                    this.motionY = 0;
                }
                this.motionY += 0.1D;
            }
        }
        if (notGrounded && groundProgress > 0.0F) {
            groundProgress -= 2F;
        } else if (!notGrounded && groundProgress < 20.0F) {
            groundProgress += 2F;
        }
        if (diving && diveProgress < 20.0F) {
            diveProgress += 1F;
        } else if (!diving && diveProgress > 0.0F) {
            diveProgress -= 1F;
        }
        if (this.isFlying()) {
            this.motionY += 0.08D;
        }
        if (this.isFallen && this.flightBehavior != FlightBehavior.NONE) {
            this.flightBehavior = FlightBehavior.NONE;
        }
        if (this.flightBehavior == FlightBehavior.NONE && this.getControllingPassenger() == null && this.isFlying()) {
            this.motionY -= 0.3F;
        }
        if (this.isFlying() && !this.onGround && this.isFallen && this.getControllingPassenger() == null) {
            this.motionY -= 0.2F;
            this.rotationPitch = Math.max(this.rotationPitch + 5, 75);
        }
        if (this.isFallen && this.onGround) {
            this.setFlying(false);
            if (this.isTamed()) {
                flightCooldown = 50;
            } else {
                flightCooldown = 12000;
            }
            this.isFallen = false;
        }
        if (flying && this.isOverAir()) {
            if(this.getRidingPlayer() == null && this.navigatorType != 1) {
                switchNavigator(1);
            }
            if(this.getRidingPlayer() != null && this.navigatorType != 2){
                switchNavigator(2);
            }
        }
        if (!flying && this.navigatorType != 0) {
            switchNavigator(0);
        }
        if ((this.hasHomePosition || this.getCommand() == 2) && this.flightBehavior == FlightBehavior.WANDER) {
            this.flightBehavior = FlightBehavior.CIRCLE;
        }
        if (flapping && flapProgress < 10.0F) {
            flapProgress += 1F;
        } else if (!flapping && flapProgress > 0.0F) {
            flapProgress -= 1F;
        }
        if (flapTicks > 0) {
            flapTicks--;
        }
        renderYawOffset = rotationYaw;
        if (world.isRemote) {
            if (!onGround) {
                roll_buffer.calculateChainFlapBuffer(this.isBeingRidden() ? 55 : 90, 1, 10F, 0.5F, this);
                pitch_buffer.calculateChainPitchBuffer(90, 10, 10F, 0.5F, this);
            }
            tail_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
        }
        if (changedFlightBehavior) {
            changedFlightBehavior = false;
        }
        if (!flapping && (this.motionY > 0.15F || this.motionY > 0 && this.ticksExisted % 200 == 0) && this.isOverAir()) {
            flapWings();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean isFlapping() {
        return flapTicks > 0;
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

    public void flapWings() {
        this.flapTicks = 20;
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

    @Nullable
    public Entity getUntamedRider() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof EntityPlayer) {
                return passenger;
            }
        }
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.amphithereMaxHealth);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.amphithereAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(FLYING, false);
        this.dataManager.register(FLAP_TICKS, Integer.valueOf(0));
        this.dataManager.register(CONTROL_STATE, Byte.valueOf((byte) 0));
        this.dataManager.register(COMMAND, Integer.valueOf(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Flying", this.isFlying());
        compound.setInteger("FlightCooldown", flightCooldown);
        compound.setInteger("RidingTime", ridingTime);
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
        this.setFlying(compound.getBoolean("Flying"));
        flightCooldown = compound.getInteger("FlightCooldown");
        ridingTime = compound.getInteger("RidingTime");
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (hasHomePosition && compound.getInteger("HomeAreaX") != 0 && compound.getInteger("HomeAreaY") != 0 && compound.getInteger("HomeAreaZ") != 0) {
            homePos = new BlockPos(compound.getInteger("HomeAreaX"), compound.getInteger("HomeAreaY"), compound.getInteger("HomeAreaZ"));
        }
        this.setCommand(compound.getInteger("Command"));
    }

    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.world.getBlockState(blockpos.down()).getBlock();
        return this.world.canBlockSeeSky(blockpos.up());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 7) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 10) {
                this.getAttackTarget().knockBack(this, 0.6F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_WING_BLAST && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if ((this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_BITE_RIDER) && this.getAnimationTick() == 1) {
            this.playSound(IafSoundRegistry.AMPHITHERE_BITE, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_WING_BLAST && this.getAttackTarget() != null && this.getAnimationTick() > 5 && this.getAnimationTick() < 22) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 25) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() / 2));
                this.getAttackTarget().isAirBorne = true;
                float f = MathHelper.sqrt(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D);
                this.getAttackTarget().motionX /= 2.0D;
                this.getAttackTarget().motionZ /= 2.0D;
                this.getAttackTarget().motionX -= 0.5 / (double) f;
                this.getAttackTarget().motionZ -= 0.5 / (double) f;

                if (this.getAttackTarget().onGround) {
                    this.getAttackTarget().motionY /= 2.0D;
                    this.getAttackTarget().motionY += 4;

                    if (this.getAttackTarget().motionY > 0.4000000059604645D) {
                        this.getAttackTarget().motionY = 0.4000000059604645D;
                    }
                }
            }
        }
        if (this.getAnimation() == ANIMATION_TAIL_WHIP && this.getAttackTarget() != null && this.getAnimationTick() == 7) {
            double dist = this.getDistanceSq(this.getAttackTarget());
            if (dist < 10) {
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
        if (world.isRemote) {
            this.updateClientControls();
        }
        if (this.up() && !world.isRemote) {
            if (!this.isFlying()) {
                this.motionY += 1F;
                this.setFlying(true);
            }
        }
        if (!this.isOverAir() && this.isFlying() && ticksFlying > 25) {
            this.setFlying(false);
        }
        if (this.dismount()) {
            if (this.isFlying()) {
                if (this.onGround) {
                    this.setFlying(false);
                }
            }
        }
        if (this.getUntamedRider() != null && this.getUntamedRider().isSneaking()) {
            MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this.getUntamedRider(), MiscEntityProperties.class);
            if (properties != null) {
                properties.hasDismountedDragon = true;
            }
            this.getUntamedRider().dismountRidingEntity();
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer) {
            EntityLivingBase target = DragonUtils.riderLookingAtEntity(this, (EntityPlayer) this.getControllingPassenger(), 2.5D);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
            }
            if (target != null) {
                target.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            }
        }
        if (this.getAttackTarget() != null && this.isOwner(this.getAttackTarget())) {
            this.setAttackTarget(null);
        }
        if (this.getAttackTarget() != null && this.onGround && this.isFlying() && ticksFlying > 40) {
            this.setFlying(false);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_TAIL_WHIP && this.getAnimation() != ANIMATION_WING_BLAST && this.getControllingPassenger() == null) {
            if (rand.nextBoolean()) {
                this.setAnimation(ANIMATION_BITE);
            } else {
                this.setAnimation(this.getRNG().nextBoolean() || this.isFlying() ? ANIMATION_WING_BLAST : ANIMATION_TAIL_WHIP);
            }
            return true;
        }
        return false;
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

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
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

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.AMPHITHERE_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.AMPHITHERE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.AMPHITHERE_DIE;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
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
        return new Animation[]{ANIMATION_BITE, ANIMATION_BITE_RIDER, ANIMATION_WING_BLAST, ANIMATION_TAIL_WHIP, ANIMATION_SPEAK};
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

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 40;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntityAmphithere amphithere = new EntityAmphithere(world);
        amphithere.setVariant(this.getVariant());
        return amphithere;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return 10;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(5));
        return livingdata;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean canPhaseThroughBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos);
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

    public boolean canMove() {
        return this.getControllingPassenger() == null && sitProgress == 0 && !this.isSitting();
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 45) {
            this.playEffect();
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected void playEffect() {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;

        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(enumparticletypes, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
        }
    }

    @Override
    public void onHearFlute(EntityPlayer player) {
        if (!this.onGround && this.isTamed()) {
            this.isFallen = true;
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

    @Override
    public boolean canPassengerSteer() {
        return false;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    public double getFlightSpeedModifier() {
        return 0.555D;
    }

    @Override
    public boolean fliesLikeElytra() {
        return !this.onGround;
    }

    private boolean isOverAir(){
        return world.isAirBlock(this.getPosition().down());
    }

    public enum FlightBehavior {
        CIRCLE,
        WANDER,
        NONE
    }

    class AILandWander extends EntityAIWander {
        public AILandWander(EntityCreature creature, double speed) {
            super(creature, speed, 10);
        }

        public boolean shouldExecute() {
            return this.entity.onGround && super.shouldExecute() && ((EntityAmphithere) this.entity).canMove();
        }
    }

    class AIFlyWander extends EntityAIBase {
        BlockPos target;

        public AIFlyWander() {
            this.setMutexBits(0);
        }

        public boolean shouldExecute() {
            if (EntityAmphithere.this.flightBehavior != FlightBehavior.WANDER || !EntityAmphithere.this.canMove()) {
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
                EntityAmphithere.this.orbitPos = null;
                return (!EntityAmphithere.this.getMoveHelper().isUpdating() || EntityAmphithere.this.ticksStill >= 50);
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(Entity e) {
            RayTraceResult rayTrace = world.rayTraceBlocks(e.getPositionVector(), new Vec3d(target).add(0.5, 0.5, 0.5), false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos sidePos = rayTrace.getBlockPos();
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (world.isAirBlock(pos) || world.isAirBlock(sidePos) || world.getBlockState(pos).getMaterial() == Material.LEAVES || world.getBlockState(sidePos).getMaterial() == Material.LEAVES) {
                    return true;
                } else {
                    return rayTrace.typeOfHit != RayTraceResult.Type.MISS;
                }
            }
            return true;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            if (!isDirectPathBetweenPoints(EntityAmphithere.this)) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
            }
            if (EntityAmphithere.this.world.isAirBlock(target)) {
                EntityAmphithere.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getAttackTarget() == null) {
                    EntityAmphithere.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AIFlyCircle extends EntityAIBase {
        BlockPos target;

        public AIFlyCircle() {
            this.setMutexBits(0);
        }

        public boolean shouldExecute() {
            if (EntityAmphithere.this.flightBehavior != FlightBehavior.CIRCLE || !EntityAmphithere.this.canMove()) {
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                EntityAmphithere.this.orbitPos = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, world, EntityAmphithere.this.orbitPos, EntityAmphithere.this.rand);
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints() {
            RayTraceResult rayTrace = world.rayTraceBlocks(EntityAmphithere.this.getPositionVector(), new Vec3d(target).add(0.5, 0.5, 0.5), false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos sidePos = rayTrace.getBlockPos();
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (world.isAirBlock(pos) || world.isAirBlock(sidePos) || world.getBlockState(pos).getMaterial() == Material.LEAVES || world.getBlockState(sidePos).getMaterial() == Material.LEAVES) {
                    return true;
                } else {
                    return rayTrace.typeOfHit != RayTraceResult.Type.MISS;
                }
            }
            return true;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            if (!isDirectPathBetweenPoints()) {
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, world, EntityAmphithere.this.orbitPos, EntityAmphithere.this.rand);
            }
            if (EntityAmphithere.this.world.isAirBlock(target)) {
                EntityAmphithere.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getAttackTarget() == null) {
                    EntityAmphithere.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class FlyMoveHelper extends EntityMoveHelper {
        public FlyMoveHelper(EntityAmphithere entity) {
            super(entity);
            this.speed = 1.75F;
        }

        public void onUpdateMoveHelper() {
            if (!EntityAmphithere.this.canMove()) {
                return;
            }
            if (EntityAmphithere.this.collidedHorizontally) {
                EntityAmphithere.this.rotationYaw += 180.0F;
                this.speed = 0.1F;
                BlockPos target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(15) - 7, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(15) - 7, EntityAmphithere.this.rand);
                this.posX = target.getX();
                this.posY = target.getY();
                this.posZ = target.getZ();
            }
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {

                double d0 = this.posX - EntityAmphithere.this.posX;
                double d1 = this.posY - EntityAmphithere.this.posY;
                double d2 = this.posZ - EntityAmphithere.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);
                if (d3 < 6 && EntityAmphithere.this.getAttackTarget() == null) {
                    if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.WANDER && EntityAmphithere.this.rand.nextInt(30) == 0) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.CIRCLE;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                    if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.CIRCLE && EntityAmphithere.this.rand.nextInt(5) == 0 && ticksCircling > 150) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.WANDER;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                    if (EntityAmphithere.this.hasHomePosition && EntityAmphithere.this.flightBehavior != FlightBehavior.NONE || EntityAmphithere.this.getCommand() == 2) {
                        EntityAmphithere.this.flightBehavior = FlightBehavior.CIRCLE;
                    }
                }
                if (d3 < 1 && EntityAmphithere.this.getAttackTarget() == null) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityAmphithere.this.motionX *= 0.5D;
                    EntityAmphithere.this.motionY *= 0.5D;
                    EntityAmphithere.this.motionZ *= 0.5D;
                } else {
                    EntityAmphithere.this.motionX += d0 / d3 * 0.5D * this.speed;
                    EntityAmphithere.this.motionY += d1 / d3 * 0.5D * this.speed;
                    EntityAmphithere.this.motionZ += d2 / d3 * 0.5D * this.speed;
                    float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                    this.entity.rotationPitch = f1;
                    if (EntityAmphithere.this.getAttackTarget() == null) {
                        EntityAmphithere.this.rotationYaw = -((float) MathHelper.atan2(EntityAmphithere.this.motionX, EntityAmphithere.this.motionZ)) * (180F / (float) Math.PI);
                        EntityAmphithere.this.renderYawOffset = EntityAmphithere.this.rotationYaw;
                    } else {
                        double d4 = EntityAmphithere.this.getAttackTarget().posX - EntityAmphithere.this.posX;
                        double d5 = EntityAmphithere.this.getAttackTarget().posZ - EntityAmphithere.this.posZ;
                        EntityAmphithere.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityAmphithere.this.renderYawOffset = EntityAmphithere.this.rotationYaw;
                    }
                }
            }
        }
    }
}
