package com.github.alexthe666.iceandfire.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.RollBuffer;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonAge;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonAttackOnCollide;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonBreathFire;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonDefend;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonEatItem;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonFollow;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonHunt;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonStarve;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonWander;
import com.github.alexthe666.iceandfire.enums.EnumOrder;
import com.github.alexthe666.iceandfire.message.MessageDragonUpdate;

public abstract class EntityDragonBase extends EntityTameable implements IAnimatedEntity, IRangedAttackMob, IInvBasic{

	private static final DataParameter<Integer> DRAGON_COLOR = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_GENDER = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_SLEEPING = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_TIER = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_AGE_TICK = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_HUNGER = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_HUNGER_TICK = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_ORDER = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_CURRENT_ATTACK = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_HOVERING = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_FLYING = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_HEAD = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_NECK = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_BODY = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DRAGON_TAIL = EntityDataManager.<Integer>createKey(EntityDragonBase.class, DataSerializers.VARINT);
	public float minSize;
	public float maxSize;
	public EnumOrder currentOrder;
	public double[][] ringBuffer = new double[64][3];
	public int ringBufferIndex = -1;
	private Animation currentAnimation;
	private int animTick;
	public ChainBuffer tailbuffer = new ChainBuffer();
	public RollBuffer rollbuffer = new RollBuffer(1);
	public int attackTick;
	public int flameTick;
	public AnimalChest inv;
	public static Animation animation_flame1 = Animation.create(1, 70);
	public static Animation animation_bite1 = Animation.create(2, 45);
	public List<Class> blacklist = new ArrayList<Class>();
	public float hoverProgress;
	public float flightProgress;
	public float fireBreathProgress;
	public boolean enableFlight = false;
	public BlockPos airTarget;
	private float angle;
	public boolean isBreathingFire;
	public int ticksTillStopFire;
	public boolean hasAgeChanged;

	public EntityDragonBase(World worldIn) {
		super(worldIn);
		currentAnimation = NO_ANIMATION;
		blacklist.add(EntityArmorStand.class);
		blacklist.add(EntityDragonEgg.class);
		blacklist.add(EntityDragonSkull.class);
		this.currentOrder = EnumOrder.WANDER;
		this.tasks.addTask(0, new EntityAIDragonAge(this));
		this.tasks.addTask(0, new EntityAIDragonStarve(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		//this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(4, new EntityAIDragonAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIDragonBreathFire(this, 1.0D, 20, 1, 15.0F));
		this.tasks.addTask(6, new EntityAIDragonWander(this));
		this.tasks.addTask(7, new EntityAIDragonFollow(this, 10, 2));
		this.tasks.addTask(8, new EntityAIDragonDefend(this));
		this.tasks.addTask(9, new EntityAIDragonEatItem(this));
		this.targetTasks.addTask(1, new EntityAIDragonHunt(this, EntityLivingBase.class, true, true));
		this.setHunger(50);
		this.setHealth(10F);
		initInv();
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.register(DRAGON_COLOR, 0);
		this.dataWatcher.register(DRAGON_GENDER, 0);
		this.dataWatcher.register(DRAGON_SLEEPING, 0);
		this.dataWatcher.register(DRAGON_TIER, 0);
		this.dataWatcher.register(DRAGON_AGE, 0);
		this.dataWatcher.register(DRAGON_AGE_TICK, 0);
		this.dataWatcher.register(DRAGON_HUNGER, 30);
		this.dataWatcher.register(DRAGON_HUNGER_TICK, 300);
		this.dataWatcher.register(DRAGON_ORDER, 0);
		this.dataWatcher.register(DRAGON_CURRENT_ATTACK, 0);
		this.dataWatcher.register(DRAGON_HOVERING, 0);
		this.dataWatcher.register(DRAGON_FLYING, 0);
		this.dataWatcher.register(DRAGON_HEAD, 0);
		this.dataWatcher.register(DRAGON_NECK, 0);
		this.dataWatcher.register(DRAGON_BODY, 0);
		this.dataWatcher.register(DRAGON_TAIL, 0);
	}

	public void initInv(){
		AnimalChest animalchest = this.inv;
		this.inv = new AnimalChest("dragonInv", 5);
		this.inv.setCustomName(this.getName());

		if (animalchest != null)
		{
			animalchest.removeInventoryChangeListener(this);
			int i = Math.min(animalchest.getSizeInventory(), this.inv.getSizeInventory());

			for (int j = 0; j < i; ++j)
			{
				ItemStack itemstack = animalchest.getStackInSlot(j);

				if (itemstack != null)
				{
					this.inv.setInventorySlotContents(j, itemstack.copy());
				}
			}
		}
		this.inv.addInventoryChangeListener(this);
		refreshInv();
	}

	public void refreshInv(){
		this.setCurrentArmor(0, getArmorIndex(this.inv.getStackInSlot(1)));
		this.setCurrentArmor(1, getArmorIndex(this.inv.getStackInSlot(2)));
		this.setCurrentArmor(2, getArmorIndex(this.inv.getStackInSlot(3)));
		this.setCurrentArmor(3, getArmorIndex(this.inv.getStackInSlot(4)));
	}

	public void setCurrentArmor(int stack, int armorType){
		switch(stack){
		case 0:
			this.getDataManager().set(DRAGON_HEAD, armorType);
			break;
		case 1:
			this.getDataManager().set(DRAGON_NECK, armorType);
			break;
		case 2:
			this.getDataManager().set(DRAGON_BODY, armorType);
			break;
		case 3:
			this.getDataManager().set(DRAGON_TAIL, armorType);
			break;
		}
	}


	public int getArmorIndex(ItemStack stack)
	{
		if (stack == null)
		{
			return 0;
		}
		else
		{
			Item item = stack.getItem();
			return item == ModItems.dragon_armor_iron ? 1 : 0;
		}
	}

	public float getDragonSize()
	{
		float step;
		step = (this.maxSize - this.minSize) / (125);

		if (this.getDragonAge() > 125)
		{
			return this.minSize + (step * 125);
		}

		return this.minSize + (step * this.getDragonAge());
	}

	public boolean increaseDragonAge()
	{
		hasAgeChanged = true;

		if (this.getDragonAge() < 125)
		{
			this.setDragonAge(this.getDragonAge() + 1);
			this.tellAge();
			return true;
		}
		return false;
	}
	
	public void breakBlock(float hardness)
	{
		if(!worldObj.isRemote){
			if(this.getEntityBoundingBox() != null){
				for (int a = (int) Math.round(this.getEntityBoundingBox().minX) - 1; a <= (int) Math.round(this.getEntityBoundingBox().maxX) + 1; a++)
				{
					for (int b = (int) Math.round(this.getEntityBoundingBox().minY) + 1; (b <= (int) Math.round(this.getEntityBoundingBox().maxY) + 3) && (b <= 127); b++)
					{
						for (int c = (int) Math.round(this.getEntityBoundingBox().minZ) - 1; c <= (int) Math.round(this.getEntityBoundingBox().maxZ) + 1; c++)
						{
							BlockPos pos = new  BlockPos(a, b, c);
							if (!(worldObj.getBlockState(pos).getBlock() instanceof BlockBush) && !(worldObj.getBlockState(pos).getBlock() instanceof BlockLiquid) && worldObj.getBlockState(pos).getBlock() != Blocks.bedrock)
							{
								this.motionX *= 0.6D;
								this.motionZ *= 0.6D;
								worldObj.destroyBlock(pos, true);
							}
						}
					}
				}
			}
		}

		/*	for (int a = (int) Math.round(this.getBoundingBox().minX) - 1; a <= (int) Math.round(this.getBoundingBox().maxX) + 1; a++)
		{
			for (int b = (int) Math.round(this.getBoundingBox().minY) + 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 3) && (b <= 127); b++)
			{
				for (int c = (int) Math.round(this.getBoundingBox().minZ) - 1; c <= (int) Math.round(this.getBoundingBox().maxZ) + 1; c++)
				{

					BlockPos pos = new  BlockPos(a, b, c);
					if (!(worldObj.getBlockState(pos).getBlock() instanceof BlockBush) && !(worldObj.getBlockState(pos).getBlock() instanceof BlockLiquid) && worldObj.getBlockState(pos).getBlock() != Blocks.bedrock)
					{
						this.motionX *= 0.6D;
						this.motionZ *= 0.6D;
						worldObj.destroyBlock(pos, true);
					}
				}
			}
		}*/
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	public void setScaleForAge(boolean par1)
	{
		this.setScale(this.getDragonSize() / 2);
		hasAgeChanged = false;
	}
	public void increaseDragonAgeTick()
	{
		this.setDragonAgeTick(this.getDragonAgeTick() + 1);
	}

	public void onLivingUpdate(){
		super.onLivingUpdate();
		breakBlock(5);
		float prevHoverProgress = hoverProgress;
		float prevFlightProgress = flightProgress;
		float prevFireBreathProgress = fireBreathProgress;
		if(hasAgeChanged){
			setScaleForAge(false);
		}
		if(enableFlight && !worldObj.isRemote && getRNG().nextInt(150) == 0 && !this.isHovering() && !this.isFlying() && this.currentOrder == EnumOrder.WANDER){
			this.setHovering(true);
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)0, hoverProgress, prevHoverProgress));
		}

		if (!worldObj.isRemote && isHovering() && hoverProgress < 20.0F)
		{
			hoverProgress += 0.5F;
		}
		else if (!worldObj.isRemote && !isHovering() && hoverProgress > 0.0F)
		{
			hoverProgress -= 0.5F;
		}

		if(hoverProgress != prevHoverProgress && !worldObj.isRemote){
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)0, hoverProgress, prevHoverProgress));
		}

		if (!worldObj.isRemote && isFlying() && flightProgress < 20.0F)
		{
			flightProgress += 0.5F;
		}
		else if (!worldObj.isRemote && !isFlying() && flightProgress > 0.0F)
		{
			flightProgress -= 0.5F;
		}

		if(flightProgress != prevFlightProgress && !worldObj.isRemote){
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)1, flightProgress, prevFlightProgress));
		}

		if (!worldObj.isRemote && isBreathingFire && fireBreathProgress < 10.0F)
		{
			fireBreathProgress += 0.5F;
		}
		else if (!worldObj.isRemote && !isBreathingFire && fireBreathProgress > 0.0F)
		{
			fireBreathProgress -= 0.5F;
		}

		if(fireBreathProgress != prevFireBreathProgress && !worldObj.isRemote){
//			IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)2, fireBreathProgress, prevFireBreathProgress));
		}

		if (!worldObj.isRemote && isHovering() && this.isOffGround(9, true) &&  hoverProgress == 20)
		{
			this.setHovering(false);
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)0, hoverProgress, prevHoverProgress));
			this.setFlying(true);
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)1, flightProgress, prevFlightProgress));
		}
		if(this.getPassengers().isEmpty()){
			if(ticksTillStopFire > 0 && !worldObj.isRemote){
				ticksTillStopFire--;
			}else if(ticksTillStopFire == 0 && isBreathingFire && !worldObj.isRemote){
				isBreathingFire = false;
			}
		}

		if (!worldObj.isRemote && isHovering())
		{
			//this.moveEntity(0, 0.17F, 0);
		}

		if(this.onGround && this.isFlying() && !worldObj.isRemote){
			this.setFlying(false);
			//IceAndFire.channel.sendToAll(new MessageDragonUpdate(this.getEntityId(), (byte)1, flightProgress, prevFlightProgress));
		}

		if(this.isFlying() && this.airTarget == null){
			this.flyAround();
		}

		if(this.airTarget != null){
			this.flyTowardsTarget();
		}

		if(attackTick != 0 && attackTick < 25){
			attackTick++;
		}

		if(flameTick != 0 && flameTick < 30){
			flameTick++;
			if(getAttackTarget() != null)
				this.getLookHelper().setLookPosition(getAttackTarget().posX, getAttackTarget().posY + (double)getAttackTarget().getEyeHeight(), getAttackTarget().posZ, 10.0F, (float)getVerticalFaceSpeed());	
		}

		if(attackTick == 25){
			attackTick = 0;
			float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
			if(getAttackTarget() != null)
				getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), f);
			else{
				List<EntityLivingBase> list = this.getEntityLivingBaseNearby(posX, posY, posZ, getDragonSize() * 1.8F);
				for (Entity prey1 : list)
				{
					prey1.attackEntityFrom(DamageSource.causeMobDamage(this), f);
					this.faceEntity(prey1, 360, 0);

				}
			}
		} 

		if(flameTick >= 20){
			if(getAttackTarget() != null && this.getControllingRider() != null)
				shootFire(getAttackTarget());
			else if(!this.getPassengers().isEmpty() && ticksTillStopFire > 0){
				float headPosX = (float) (this.posX + 1.8F * this.getDragonSize() * Math.cos((this.rotationYaw + 90) * Math.PI/180));
				float headPosZ = (float) (this.posZ + 1.8F * this.getDragonSize() * Math.sin((this.rotationYaw + 90) * Math.PI/180));
				float headPosY = (float) (this.posY + 0.5 * this.getDragonSize());
				double d1 = 0D;
				//Vec3d vec3 = this.getControllingRider().func_181014_aG();
				double d2 = this.getControllingRider().getLook(1.0F).xCoord;
				double d3 = this.getControllingRider().getLook(1.0F).yCoord;
				double d4 = this.getControllingRider().getLook(1.0F).zCoord;
				this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(this), 0);
				EntityDragonFire entitylargefireball = new EntityDragonFire(this.getControllingRider().worldObj, this, d2, d3, d4);
				entitylargefireball.setLocationAndAngles(headPosX, headPosY, headPosZ, this.getControllingRider().rotationYaw, this.getControllingRider().rotationPitch);
				entitylargefireball.setSizes(this.getDragonSize() * 0.25F, this.getDragonSize() * 0.25F);
				this.worldObj.spawnEntityInWorld(entitylargefireball);
			}
		}
		if(isBreathingFire && this.getControllingRider() == null && getAttackTarget() == null){
			isBreathingFire = false;
		}
		if(this.isTamed() && this.getAttackTarget() != null && isHungry()){
			if(this.getAttackTarget() == this.getOwner()){
				this.setAttackTarget((EntityLivingBase)null);
			}
		}
		if (motionY < 0.0D && (isFlying() || isHovering()))
		{
			motionY *= -1.0D;
		}
		if (!this.isSitting())
		{
			if (this.isTeen() || this.isAdult())
			{
				if (!worldObj.isRemote)
				{
					if (rand.nextInt(400) == 0){
					}
				}
			}
		}

	}

	public Entity getControllingRider(){
		if(this.getPassengers().isEmpty()){
			return null;
		}else{
			if(this.getPassengers().get(0) != null){
				return this.getPassengers().get(0);
			}
		}
		return null;

	}

	private int getFallAttackHeight() {
		return 25;
	}
	public void updatePassenger(Entity entity)
	{
		super.updatePassenger(entity);
		if (this.getControllingRider() != null && entity == this.getControllingRider())
		{
			rotationYaw = renderYawOffset;
			float radius = 0.4F *  (0.7F * getDragonSize()) * - 3;
			float angle = (0.01745329251F * this.renderYawOffset) + 3.15F;
			double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
			double extraZ = (double) (radius * MathHelper.cos(angle));
			double extraY = 0.4F *  (1.9F * getDragonSize());
			this.getControllingRider().setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
			this.bobPrey(this.getControllingRider(), 0.1F, 0.3F, this.ticksExisted, 1);


		}

	}

	public boolean isSitting()
	{
		return currentOrder == EnumOrder.SIT || currentOrder == EnumOrder.SLEEP;
	}

	protected void destroyItem(EntityPlayer player, ItemStack stack)
	{
		if(stack != null){
			if (!player.capabilities.isCreativeMode)
			{
				--stack.stackSize;

				if (stack.stackSize <= 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
				}
			}
		}
	}
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setByte("Color", (byte)this.getColor());
		tag.setByte("Gender", (byte)this.getGender());
		tag.setByte("Sleeping", (byte)this.getSleeping());
		tag.setByte("Tier", (byte)this.getTier());
		tag.setByte("DragonAge", (byte)this.getDragonAge());
		tag.setByte("DragonAgeTick", (byte)this.getDragonAgeTick());
		tag.setByte("Hunger", (byte)this.getHunger());
		tag.setByte("HungerTick", (byte)this.getHungerTick());
		tag.setByte("Order", (byte)this.currentOrder.ordinal());
		tag.setByte("CurrentAttack", (byte)this.getCurrentAttack());
		tag.setBoolean("Hovering", this.isHovering());
		tag.setBoolean("Flying", this.isFlying());

		if (this.inv.getStackInSlot(1) != null)
		{
			tag.setTag("HeadItem", this.inv.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
		}
		if (this.inv.getStackInSlot(2) != null)
		{
			tag.setTag("NeckItem", this.inv.getStackInSlot(2).writeToNBT(new NBTTagCompound()));
		}
		if (this.inv.getStackInSlot(3) != null)
		{
			tag.setTag("BodyItem", this.inv.getStackInSlot(3).writeToNBT(new NBTTagCompound()));
		}
		if (this.inv.getStackInSlot(4) != null)
		{
			tag.setTag("TailItem", this.inv.getStackInSlot(4).writeToNBT(new NBTTagCompound()));
		}
	}


	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		this.setColor(tag.getByte("Color"));
		this.setGender(tag.getByte("Gender"));
		this.setSleeping(tag.getByte("Sleeping"));
		this.setTier(tag.getByte("Tier"));
		this.setDragonAge(tag.getByte("DragonAge"));
		this.setDragonAgeTick(tag.getByte("DragonAgeTick"));
		this.setHunger(tag.getByte("Hunger"));
		this.setHungerTick(tag.getByte("HungerTick"));
		this.currentOrder = EnumOrder.values()[tag.getByte("Order")];
		this.setCurrentAttack(tag.getByte("CurrentAttack"));
		this.setHovering(tag.getBoolean("Hovering"));
		this.setFlying(tag.getBoolean("Flying"));
		ItemStack itemstack;
		if (tag.hasKey("HeadItem", 10))
		{
			itemstack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("HeadItem"));

			if (itemstack != null)
			{
				this.inv.setInventorySlotContents(1, itemstack);
			}
		}
		if (tag.hasKey("NeckItem", 10))
		{
			itemstack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("NeckItem"));

			if (itemstack != null)
			{
				this.inv.setInventorySlotContents(2, itemstack);
			}
		}
		if (tag.hasKey("BodyItem", 10))
		{
			itemstack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("BodyItem"));

			if (itemstack != null)
			{
				this.inv.setInventorySlotContents(3, itemstack);
			}
		}
		if (tag.hasKey("TailItem", 10))
		{
			itemstack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("TailItem"));

			if (itemstack != null)
			{
				this.inv.setInventorySlotContents(4, itemstack);
			}
		}
	}

	public void onUpdate(){
		super.onUpdate();
		if(this.getControllingRider() != null){
			if(this.getControllingRider() instanceof EntityPlayer && this.currentOrder != EnumOrder.WANDER){
				this.currentOrder = EnumOrder.WANDER;
			}
		}
		rotationYaw = renderYawOffset;
		repelEntities(this.posX, this.posY, this.posZ, 0.5F * this.getDragonSize());

		if(this.getAttackTarget() != null && this.getAnimation() == this.NO_ANIMATION && (this.getControllingRider() == null || this.getControllingRider() != null && !(this.getControllingRider() instanceof EntityPlayer))){
			float d = this.getDistanceToEntity(getAttackTarget());
			if(d <= 1.78F * this.getDragonSize()){
				if(this.getAnimation() != animation_bite1){
					this.setAnimation(animation_bite1);
				}
				this.attackTick = 1;

			}else{
				if(this.getRNG().nextInt(30) == 0){
					this.flameTick = 1;
					isBreathingFire = true;
				}
			}
		}
		tailbuffer.calculateChainSwingBuffer(70F, 5, 4, this);
		if(this.isOffGround(5, true)){
			rollbuffer.calculateChainRollBuffer(50F, 5, 4, this);
		}else{
			rollbuffer.resetRotations();
		}

		AnimationHandler.INSTANCE.updateAnimations(this);
	}

	private void shootFire(EntityLivingBase attackTarget) {
		float headPosX = (float) (posX + 1.8F * getDragonSize() * Math.cos((rotationYaw + 90) * Math.PI/180));
		float headPosZ = (float) (posZ + 1.8F * getDragonSize() * Math.sin((rotationYaw + 90) * Math.PI/180));
		float headPosY = (float) (posY + 0.5 * getDragonSize());
		double d1 = 0D;
		Vec3d vec3 = this.getLook(1.0F);
		double d2 = attackTarget.posX - (headPosX + vec3.xCoord * d1);
		double d3 = attackTarget.getEntityBoundingBox().minY + (double)(attackTarget.height / 2.0F) - (0.5D + headPosY + (double)(this.height / 2.0F));
		double d4 = attackTarget.posZ - (headPosZ + vec3.zCoord * d1);
		worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1008, new BlockPos(this), 0);
		EntityDragonFire entitylargefireball = new EntityDragonFire(worldObj, this, d2, d3, d4);
		entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
		entitylargefireball.setSizes(this.getDragonSize() * 0.25F, this.getDragonSize() * 0.25F);
		worldObj.spawnEntityInWorld(entitylargefireball);
	}

	public void spawnLandingParticles(){
		if(this.onGround){
			for (int l = 0; l < 4; ++l)
			{
				int i = MathHelper.floor_double(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
				int j = MathHelper.floor_double(this.posY);
				int k = MathHelper.floor_double(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));

				if(this.rand.nextInt(10) ==1){
					for (int a = 0; a < 360; a += 4) {
						double ang = a * Math.PI / 180D;
						worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX - MathHelper.sin((float) ang) * 3, posY + 0.1D, posZ + MathHelper.cos((float) ang) * 3, 0, 0, 0, new int[]{Block.getIdFromBlock(this.worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock())});
					}
				}
			}
		}
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		ItemStack itemstack = player.getHeldItemMainhand();
		if(itemstack == null){
			if(player.isSneaking()){
				this.currentOrder = this.currentOrder.next();
				if (this.currentOrder == EnumOrder.SIT || this.currentOrder == EnumOrder.SLEEP)
				{
					this.getNavigator().clearPathEntity();
					this.setSitting(true);
				}
				else
				{
					this.setSitting(false);
				}
				if(worldObj.isRemote){
					if(this.currentOrder == EnumOrder.WANDER){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonWanderName")));
						}else{
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.dragonWander")));
						}
					}
					if(this.currentOrder == EnumOrder.FOLLOW){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonFollowName")));
						}else{
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.dragonFollow")));
						}
					}
					if(this.currentOrder == EnumOrder.SIT){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonSitName")));
						}else{
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.dragonSit")));
						}
					}
					if(this.currentOrder == EnumOrder.SLEEP){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonSleepName")));
						}else{
							player.addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.dragonSleep")));
						}
					}
				}
				return true;
			}else{
				player.openGui(IceAndFire.instance, 0, this.worldObj, getEntityId(), 0, 0);
			}
		}else if(itemstack != null && itemstack.getItem() != null){
			Item item = itemstack.getItem();
			if(item == Items.stick && this.getStage() > 2){
				this.mountDragon(player);
				return true;
			}
			if(item instanceof ItemFood){
				boolean isWolfFood = ((ItemFood)item).isWolfsFavoriteMeat();
				if(isWolfFood){
					this.increaseDragonAge();
					this.updateSize();
					this.destroyItem(player, getHeldItemMainhand());
					this.heal(6);
					float radius = 0.4F *  (0.7F * getDragonSize()) * - 3;
					float angle = (0.01745329251F * this.renderYawOffset) + 3.15F;
					double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
					double extraZ = (double) (radius * MathHelper.cos(angle));
					double extraY = 0.4F *  (1.9F * getDragonSize());
					this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, extraX + (this.rand.nextDouble() - 0.5D), extraY + (this.rand.nextDouble() - 0.5D), extraZ + (this.rand.nextDouble() - 0.5D), 0, 0.05D, 0, new int[] {Item.getIdFromItem(itemstack.getItem()), itemstack.getMetadata()});
					return true;

				}
			}

		}
		return false;
	}

	public void tellOtherPlayersAge(){
		int acctualStage = getStage();
		if(this.worldObj.getClosestPlayerToEntity(this, 16) != null && worldObj.isRemote){
			if(this.hasCustomName()){
				worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonGrownName") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
			}else{
				worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new TextComponentString(StatCollector.translateToLocal("message.iceandfire.dragonGrown") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
			}	
		}
	}

	public double[] getMovementOffsets(int x, float y)
	{
		if (this.getHealth() <= 0.0F)
		{
			y = 0.0F;
		}

		y = 1.0F - y;
		int j = this.ringBufferIndex - x * 1 & 63;
		int k = this.ringBufferIndex - x * 1 - 1 & 63;
		double[] adouble = new double[3];
		double d0 = this.ringBuffer[j][0];
		double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[k][0] - d0);
		adouble[0] = d0 + d1 * (double)y;
		d0 = this.ringBuffer[j][1];
		d1 = this.ringBuffer[k][1] - d0;
		adouble[1] = d0 + d1 * (double)y;
		adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * (double)y;
		return adouble;
	}

	public boolean increaseHunger(int var1)
	{
		if (this.getHunger() >= 100)
		{
			return false;
		}

		this.setHunger(this.getHunger() + var1);
		if(new Random().nextInt(3) == 0){
			this.increaseDragonAge();
		}

		if (this.getHunger() > 100)
		{
			this.setHunger(100);
		}

		///this.worldObj.playSoundAtEntity(this, "random.eat",
		//		this.getSoundVolume(), this.getSoundPitch());
		this.worldObj.playSound(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, SoundEvents.entity_generic_eat, this.getSoundCategory(), 2.5F, 1.0F, false);

		return true;
	}
	public void decreaseHunger()
	{
		if (this.getHunger() > 0)
		{
			this.setHunger(this.getHunger() - 1);
		}
	}
	public void decreaseHungerTick()
	{
		if (this.getHungerTick() > 0)
		{
			this.setHungerTick(this.getHungerTick() - 1);
		}
	}

	public int getColor()
	{
        return ((Integer)this.getDataManager().get(DRAGON_COLOR)).intValue();
	}

	public void setColor(int i)
	{
        this.getDataManager().set(DRAGON_COLOR, Integer.valueOf(i));
	}

	public int getGender()
	{
        return ((Integer)this.getDataManager().get(DRAGON_GENDER)).intValue();
	}

	public void setGender(int i)
	{
        this.getDataManager().set(DRAGON_GENDER, Integer.valueOf(i));
	}

	public int getSleeping()
	{
        return ((Integer)this.getDataManager().get(DRAGON_SLEEPING)).intValue();
	}

	public void setSleeping(int i)
	{
        this.getDataManager().set(DRAGON_SLEEPING, Integer.valueOf(i));
	}

	public int getTier()
	{
        return ((Integer)this.getDataManager().get(DRAGON_TIER)).intValue();
	}

	public void setTier(int i)
	{
        this.getDataManager().set(DRAGON_TIER, Integer.valueOf(i));
	}

	public int getDragonAge()
	{
        return ((Integer)this.getDataManager().get(DRAGON_AGE)).intValue();
	}

	public void setDragonAge(int i)
	{
        this.getDataManager().set(DRAGON_AGE, Integer.valueOf(i));
		this.updateSize();
	}

	public int getDragonAgeTick()
	{
        return ((Integer)this.getDataManager().get(DRAGON_AGE_TICK)).intValue();
	}

	public void setDragonAgeTick(int i)
	{
        this.getDataManager().set(DRAGON_AGE_TICK, Integer.valueOf(i));
	}

	public int getHunger()
	{
        return ((Integer)this.getDataManager().get(DRAGON_HUNGER)).intValue();
	}

	public void setHunger(int i)
	{
        this.getDataManager().set(DRAGON_HUNGER, Integer.valueOf(i));
	}

	public int getHungerTick()
	{
        return ((Integer)this.getDataManager().get(DRAGON_HUNGER_TICK)).intValue();
	}

	public void setHungerTick(int i)
	{
        this.getDataManager().set(DRAGON_HUNGER_TICK, Integer.valueOf(i));
	}

	private void setCurrentAttack(int i) {
        this.getDataManager().set(DRAGON_CURRENT_ATTACK, Integer.valueOf(i));
	}

	private int getCurrentAttack() {
        return ((Integer)this.getDataManager().get(DRAGON_CURRENT_ATTACK)).intValue();
	}
	
	public boolean isHovering()
	{
		return (((Integer)this.getDataManager().get(DRAGON_HOVERING)).intValue() & 1) != 0;
	}

	public boolean isFlying()
	{
		return (((Integer)this.getDataManager().get(DRAGON_FLYING)).intValue() & 1) != 0;
	}

	public void setHovering(boolean i)
	{
		int b0 = ((Integer)this.getDataManager().get(DRAGON_HOVERING)).intValue();

		if (i)
		{
	        this.getDataManager().set(DRAGON_HOVERING, Integer.valueOf((b0 | 1)));
		}
		else
		{
	        this.getDataManager().set(DRAGON_HOVERING, Integer.valueOf((b0 & -2)));
		}
	}

	public void setFlying(boolean i)
	{
		int b0 = ((Integer)this.getDataManager().get(DRAGON_FLYING)).intValue();

		if (i)
		{
	        this.getDataManager().set(DRAGON_FLYING, Integer.valueOf((b0 | 1)));
		}
		else
		{
	        this.getDataManager().set(DRAGON_FLYING, Integer.valueOf((b0 & -2)));
		}
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	private void mountDragon(EntityPlayer player) {
		player.rotationYaw = this.rotationYaw;
		player.rotationPitch = this.rotationPitch;

		if (!this.worldObj.isRemote) {
            player.startRiding(this);
		}
	}

	protected void repelEntity(Entity entity, double posX, double posY, double posZ, float radius)
	{
		double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
		entity.motionX = -0.1 * Math.cos(angle);
		entity.motionZ = -0.1 * Math.sin(angle);
	}

	protected void repelEntities(double posX, double posY, double posZ, float radius)
	{
		List<EntityLivingBase> nearestEntities = getEntityLivingBaseNearby(posX, posY, posZ, radius);
		for (Entity entity : nearestEntities)
		{
			if (entity == this.getControllingRider()) continue;
			double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
			entity.motionX = -0.1 * Math.cos(angle);
			entity.motionZ = -0.1 * Math.sin(angle);
		}
	}

	public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius)
	{
		List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(distanceX, distanceY, distanceZ));
		ArrayList<EntityLivingBase> listEntityLivingBase = new ArrayList<EntityLivingBase>();
		for (Entity entityNeighbor : list)
		{
			if (entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius && entityNeighbor.posY >= posY && entityNeighbor.posY <= posY + distanceY)
				listEntityLivingBase.add((EntityLivingBase) entityNeighbor);
		}
		return listEntityLivingBase;
	}

	public double getAngleBetweenEntities(Entity first, Entity second)
	{
		return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
	}

	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data)
	{
		this.onSpawn();
		return super.onInitialSpawn(difficulty, data);
	}

	public abstract void onSpawn();

	public boolean isAdult()
	{
		return this.getDragonAge() >= 75;
	}

	public boolean isTeen()
	{
		return  this.getDragonAge() > 20 && this.getDragonAge() < 75;
	}
	public boolean isChild()
	{
		return this.getDragonAge() <= 20;
	}

	public abstract String getTexture();

	public void updateSize(){
		jump();
	}
	public void tellAge(){
		if(this.getDragonAge() == 6 || this.getDragonAge() == 13 || this.getDragonAge() == 76 || this.getDragonAge() == 101){
			this.tellOtherPlayersAge();
		}
	}
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);

	}
	public boolean isHungry() {
		return isDeadlyHungry() ? false : this.getHunger() >= 50;
	}
	private boolean isDeadlyHungry() {
		return this.getHunger() >= 15;
	}
	public int getStage() {
		if(this.getDragonAge() < 6){
			return 1;
		}
		else if(this.getDragonAge() > 5 && this.getDragonAge() < 13){
			return 2;
		}
		else if(this.getDragonAge() > 12 && this.getDragonAge() < 76){
			return 3;
		}
		else if(this.getDragonAge() > 75 && this.getDragonAge() < 101){
			return 4;
		}
		else if(this.getDragonAge() > 100 && this.getDragonAge() < 126){
			return 5;
		}else{
			return 1;
		}
	}

	public void land(){
		spawnLandingParticles();
		if(getAttackTarget() != null && !this.isAirBorne){
			this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), 8);
		}
	}

	public void moveEntityWithHeading(float x, float z)
	{
		if(this.currentOrder == EnumOrder.SIT || this.currentOrder == EnumOrder.SLEEP){

		}else{
			if (this.getControllingRider() != null && this.getControllingRider() instanceof EntityLivingBase)
			{
				this.prevRotationYaw = this.rotationYaw = this.getControllingRider().rotationYaw;
				this.rotationPitch = this.getControllingRider().rotationPitch * 0.5F;
				this.setRotation(this.rotationYaw, this.rotationPitch);
				this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
				x = ((EntityLivingBase)this.getControllingRider()).moveStrafing * 0.5F;
				z = ((EntityLivingBase)this.getControllingRider()).moveForward;

				if (z <= 0.0F)
				{
					z *= 0.25F;
				}

				this.stepHeight = 1.0F;
				this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

				if (!this.worldObj.isRemote)
				{
					this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
					super.moveEntityWithHeading(x, z);
				}

				if (this.onGround)
				{

				}

				this.prevLimbSwingAmount = this.limbSwingAmount;
				double d1 = this.posX - this.prevPosX;
				double d0 = this.posZ - this.prevPosZ;
				float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

				if (f4 > 1.0F)
				{
					f4 = 1.0F;
				}

				this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
				this.limbSwing += this.limbSwingAmount;
			}
			else
			{
				this.stepHeight = 0.5F;
				this.jumpMovementFactor = 0.02F;
				super.moveEntityWithHeading(x, z);
			}
		}
	}

	public EntityLivingBase closestEntity(){
		return (EntityLivingBase)this.worldObj.findNearestEntityWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand((double)16, 3.0D, 16), this);
	}

	public void bobPrey(Entity entity, float speed, float degree, float f, float f1)
	{
		float bob = -(float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
		entity.posY += bob;
	}

	public void setNextAttack(){
		if(this.getCurrentAttack() == 0){
			this.setCurrentAttack(1);

		}else{
			this.setCurrentAttack(0);

		}
		/*if(this.getAITarget() != null){
			int choice = rand.nextInt(3);
			if(this.getStage() > 2 && choice == 0){
				this.setCurrentAttack(2);
			}else if(this.getDistance(this.getAITarget().posX, this.getAITarget().posY, this.getAITarget().posZ) > 3 && choice == 1){
				this.setCurrentAttack(1);
			}else{
				this.setCurrentAttack(0);
			}
		}*/

	}

	public void setAnimationTick(int tick) {
		animTick = tick;
	}

	public int getAnimationTick() {
		return animTick;
	}

	@Override
	public void setAnimation(Animation animation) {
		currentAnimation = animation;
	}

	@Override
	public Animation getAnimation() {
		return currentAnimation;
	}

	public final Animation[] getAnimations() {
		return new Animation[]{this.NO_ANIMATION, this.animation_flame1, this.animation_bite1};
	}

	public boolean canAttackMob(EntityLivingBase targetEntity) {
		return width > targetEntity.width && !this.blacklist.contains(targetEntity.getClass());
	}


	public void attackEntityWithRangedAttack(EntityLivingBase entity, float f){
		if(this.getAnimation() == NO_ANIMATION){
			setNextAttack();
		}
	}

	public boolean shouldSetFire(EntityLivingBase entitylivingbase) {
		return this.getCurrentAttack() == 1;
	}

	public int pickUpFood(ItemStack stack) {
		return 0;
	}

	public double getAttackDistance() {
		//100 95
		return width / 2;
	}

	public static boolean isAllowedInSlot(int slot, ItemStack stack){
		if(stack.getItem() == ModItems.dragon_armor_iron && stack.getMetadata() + 1 == slot){
			return true;
		}else{
			return false;
		}
	}

	public boolean canBeSteered()
	{
		return true;
	}

	public boolean isOffGround(int blocks, boolean forAge){
		BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
		boolean b = false;
		for(int i = 0; i < blocks * (forAge ? this.isAdult() ? 1 : (this.isTeen() ? 0.5F : 0.33F) : 1); i++){
			b = worldObj.isAirBlock(pos.down(i));
		}
		return  b && !onGround;
	}

	public boolean isDirectPathBetweenPoints(net.minecraft.util.math.Vec3d vec1, net.minecraft.util.math.Vec3d vec2)
	{
		RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(vec1, new Vec3d(vec2.xCoord, vec2.yCoord + (double)this.height * 0.5D, vec2.zCoord), false, true, false);
		return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
	}

	public void flyAround()
	{
		if (airTarget != null && !worldObj.isAirBlock(airTarget))
			airTarget = null;

		if (airTarget == null || rand.nextInt(150) == 0 || this.getDistanceSq((int) posX, (int) posY,(int) posZ) < 10F)
			airTarget = new BlockPos((int) posX + rand.nextInt(90) - rand.nextInt(60), (int) posY + rand.nextInt(60) - 2, (int) posZ + rand.nextInt(90) - rand.nextInt(60));

		flyTowardsTarget();
	}
	public void flyTowardsTarget()
	{
		if (airTarget != null && isDirectPathBetweenPoints(this.getPositionVector(), new Vec3d(airTarget.getX(), airTarget.getY(), airTarget.getZ())) && this.isOffGround(1, false) && isFlying())
		{
			double targetX = airTarget.getX() + 0.5D - posX;
			double targetY = airTarget.getY() + 1D - posY;
			double targetZ = airTarget.getZ() + 0.5D - posZ;
			moveEntity(motionX + (Math.signum(targetX) * 0.5D - motionX) * flightSpeed(), motionY + (Math.signum(targetY) * 0.5D - motionY) * flightSpeed(), motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * flightSpeed());
			float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) + 45.0F;
			float rotation = MathHelper.wrapAngleTo180_float(angle - rotationYaw);
			moveForward = 0.5F;
			rotationYaw += rotation;
		}
	}

	private double flightSpeed() {
		return 0.10000000149011612D;
	}
}
