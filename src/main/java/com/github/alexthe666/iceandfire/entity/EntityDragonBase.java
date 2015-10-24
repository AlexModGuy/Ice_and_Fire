package com.github.alexthe666.iceandfire.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.ilexiconn.llibrary.client.model.modelbase.ChainBuffer;
import net.ilexiconn.llibrary.common.animation.Animation;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
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
import com.github.alexthe666.iceandfire.message.MessageDragonMotion;
import com.github.alexthe666.iceandfire.misc.AnimationTicker;

public abstract class EntityDragonBase extends EntityTameable implements IAnimated, IRangedAttackMob, IInvBasic{

	public float minSize;
	public float maxSize;
	public EnumOrder currentOrder;
	public double[][] ringBuffer = new double[64][3];
	public int ringBufferIndex = -1;
	public boolean isFlying;
	private boolean landing;
	public BlockPos currentTarget;
	//For fire breathing
	public EntityDragonMouth mouth;
	private Animation currentAnimation;
	private int animTick;
	public ChainBuffer tailbuffer = new ChainBuffer(5);
	private int attackTick;
	private int flameTick;
	private AnimalChest inv;
	public static Animation animation_roar1 = new Animation(1, 50);
	public static Animation animation_flame1 = new Animation(2, 45);

	@SideOnly(Side.CLIENT)
	public float motionSpeedX;
	@SideOnly(Side.CLIENT)
	public float motionSpeedY;
	@SideOnly(Side.CLIENT)
	public float motionSpeedZ;
	public List<Class> blacklist = new ArrayList<Class>();

	public EntityDragonBase(World worldIn) {
		super(worldIn);
		blacklist.add(EntityArmorStand.class);
		blacklist.add(EntityDragonEgg.class);
		blacklist.add(EntityDragonSkull.class);
		this.currentOrder = EnumOrder.WANDER;
		//((PathNavigateGround)this.getNavigator()).func_179690_a(true);
		this.mouth = new EntityDragonMouth(this, "head");
		//initInv();
		this.tasks.addTask(0, new EntityAIDragonAge(this));
		this.tasks.addTask(0, new EntityAIDragonStarve(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAIDragonAttackOnCollide(this, EntityLiving.class, 1.0D, false));
		this.tasks.addTask(3, new EntityAIDragonBreathFire(this, 1.0D, 20, 1, 15.0F));
		this.tasks.addTask(4, new EntityAIDragonWander(this));
		this.tasks.addTask(5, new EntityAIDragonFollow(this, 10, 2));
		this.tasks.addTask(6, new EntityAIDragonDefend(this));
		this.tasks.addTask(7, new EntityAIDragonEatItem(this));
		this.targetTasks.addTask(3, new EntityAIDragonHunt(this, EntityLiving.class, true, true));
		this.setHunger(50);
		this.setHealth(10F);
	}

	/*public void initInv(){
		AnimalChest animalchest = this.inv;
		this.inv = new AnimalChest("dragonInv", 4);
		this.inv.setCustomName(this.getName());

		if (animalchest != null)
		{
			animalchest.func_110132_b(this);
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
		this.inv.func_110134_a(this);
		this.setHeadArmorStack(this.inv.getStackInSlot(0));
		this.setNeckArmorStack(this.inv.getStackInSlot(1));
		this.setBodyArmorStack(this.inv.getStackInSlot(3));
		this.setTailArmorStack(this.inv.getStackInSlot(4));

	}
	
	public void onInventoryChanged(InventoryBasic p_76316_1_)
	{
		int i = this.func_110241_cb();
		boolean flag = this.isHorseSaddled();
		this.setHeadArmorStack(this.inv.getStackInSlot(0));
		this.setNeckArmorStack(this.inv.getStackInSlot(1));
		this.setBodyArmorStack(this.inv.getStackInSlot(3));
		this.setTailArmorStack(this.inv.getStackInSlot(4));

		if (this.ticksExisted > 20)
		{
			if (i == 0 && i != this.func_110241_cb())
			{
				this.playSound("mob.horse.armor", 0.5F, 1.0F);
			}
			else if (i != this.func_110241_cb())
			{
				this.playSound("mob.horse.armor", 0.5F, 1.0F);
			}
		}
	}
	private void setHeadArmorStack(ItemStack stackInSlot) {
		// TODO Auto-generated method stub
		
	}
	private void setNeckArmorStack(ItemStack stackInSlot) {
		// TODO Auto-generated method stub
		
	}
	private void setBodyArmorStack(ItemStack stackInSlot) {
		// TODO Auto-generated method stub
		
	}
	private void setTailArmorStack(ItemStack stackInSlot) {
		// TODO Auto-generated method stub
		
	}
	private int getHorseArmorIndex(ItemStack stack)
	{
		if (stack == null)
		{
			return 0;
		}
		else
		{
			Item item = stack.getItem();
			return item == Items.iron_horse_armor ? 1 : (item == Items.golden_horse_armor ? 2 : (item == Items.diamond_horse_armor ? 3 : 0));
		}
	}
	*/
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
			if (this.getDragonAge() < 124)
			{
				this.setDragonAge(this.getDragonAge() + 1);
				this.tellAge();
				System.out.println(this.getHealth());

				return true;
			}

			return false;
		}
		public void breakBlock(float hardness)
		{
			if(this.getBoundingBox() != null){
				for (int a = (int) Math.round(this.getBoundingBox().minX) - 1; a <= (int) Math.round(this.getBoundingBox().maxX) + 1; a++)
				{
					for (int b = (int) Math.round(this.getBoundingBox().minY) + 1; (b <= (int) Math.round(this.getBoundingBox().maxY) + 3) && (b <= 127); b++)
					{
						for (int c = (int) Math.round(this.getBoundingBox().minZ) - 1; c <= (int) Math.round(this.getBoundingBox().maxZ) + 1; c++)
						{

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
			this.setScale(this.getDragonSize());
		}
		public void increaseDragonAgeTick()
		{
			this.setDragonAgeTick(this.getDragonAgeTick() + 1);
		}

		public void onLivingUpdate(){
			super.onLivingUpdate();

			if(attackTick != 0 && attackTick > 40){
				attackTick++;
			}
			if(flameTick != 0 && flameTick > 40){
				flameTick++;
			}
			if(this.getRNG().nextInt(50) == 0){
				if(this.getAnimation() != null && this.getAnimation().animationId == 0){
					this.setAnimation(animation_roar1);
				}
			}
			int sleepCounter = 0;
			int wakeCounter = 0;

			if(this.getStage() > 2 && !this.isTamed() || this.getStage() > 2 && this.getHunger() < 60){
				breakBlock(5);
			}

			if(this.getSleeping() == 0 && this.currentOrder == EnumOrder.SLEEP){
				//this.setSleeping(1);
				sleepCounter = 1;
			}
			if(this.getSleeping() == 1 && this.currentOrder != EnumOrder.SLEEP){
				//this.setSleeping(0);
				wakeCounter = 1;
			}

			if(wakeCounter < 0 && wakeCounter > 40){
				wakeCounter++;
			}
			if(wakeCounter == 40){
				this.setSleeping(0);
			}

			if(sleepCounter < 0 && sleepCounter > 40){
				sleepCounter++;
			}
			if(sleepCounter == 40){
				this.setSleeping(1);
			}
			if(this.getSleeping() == 1){
			}

			if(this.isTamed() && this.getAttackTarget() != null && isHungry()){
				if(this.getAttackTarget() == this.getOwnerEntity()){
					this.setAttackTarget((EntityLivingBase)null);
				}
			}
			//land();
			if (motionY < 0.0D)
			{
				motionY *= 0.6D;
			}
			if (!this.isSitting())
			{
				if (this.isTeen() || this.isAdult())
				{
					if (!worldObj.isRemote)
					{
						if (rand.nextInt(400) == 0){
							if (!isFlying){
								setFlying(true);
							}else{
								setFlying(false);
							}
						}
						if (isFlying && getAttackTarget() == null)
						{
							flyAround();
						}
						/*if (getAttackTarget() != null)
						{
							currentTarget = new BlockPos((int) getAttackTarget().posX, ((int) getAttackTarget().posY + getAttackTarget().getEyeHeight()), (int) getAttackTarget().posZ);
							setFlying(false);
							flyTowardsTarget();
						}*/
					}
				}
			}

			if(this.getAITarget() != null && this.getCurrentAttack() == 2){
				if(!isFlying){
					this.setFlying(true);
				}else{
					currentTarget = new BlockPos((int) getAttackTarget().posX, ((int) getAttackTarget().posY + getAttackTarget().getEyeHeight() + this.getFallAttackHeight()), (int) getAttackTarget().posZ);
					flyTowardsTarget();
					if(this.getDistance(currentTarget.getX(), this.posY, currentTarget.getZ()) < 5F){
						this.land();
					}
				}

			}

		}

		private int getFallAttackHeight() {
			return 25;
		}
		public void updateRiderPosition()
		{
			super.updateRiderPosition();
			/*
		if (this.riddenByEntity != null)
		{
			rotationYaw = renderYawOffset;
			float radius = 0.4F * this.getCreatureLength() - 3;
			float angle = (0.01745329251F * this.renderYawOffset) + 3.15F;
			((EntityLivingBase)this.riddenByEntity).rotationYaw = (float) (angle * (180 / Math.PI) - 150.0F);
			double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
			double extraZ = (double) (radius * MathHelper.cos(angle));
			this.riddenByEntity.setPosition(this.posX + extraX, this.posY + 2, this.posZ + extraZ);
			this.bobPrey(riddenByEntity, 0.3F, 0.3F, this.ticksExisted, 1);


		}
			 */
		}

		public boolean isSitting()
		{
			return currentOrder == EnumOrder.SIT || currentOrder == EnumOrder.SLEEP;
		}
		public boolean canBePushed()
		{
			return false;
		}

		public void flyTowardsTarget()
		{
			if (currentTarget != null)
			{
				double targetX = currentTarget.getX() + 0.5D - posX;
				double targetY = currentTarget.getY() + 1D - posY;
				double targetZ = currentTarget.getZ() + 0.5D - posZ;
				motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.10000000149011612D;
				motionY += (Math.signum(targetY) * 0.699999988079071D - motionY) * 0.10000000149011612D;
				motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.10000000149011612D;
				float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
				float rotation = MathHelper.wrapAngleTo180_float(angle
						- rotationYaw);
				moveForward = 0.5F;
				rotationYaw += rotation;
			}

		}

		public void flyAround()
		{
			if (currentTarget != null){
				if (!worldObj.isAirBlock(currentTarget) || currentTarget.getY() < 1){
					currentTarget = null;
				}
			}
			if (currentTarget == null || rand.nextInt(30) == 0 || this.getDistance(currentTarget.getX(), currentTarget.getY(), currentTarget.getZ()) < 10F){
				currentTarget = new BlockPos((int) posX + rand.nextInt(90)
						- rand.nextInt(60), (int) posY + rand.nextInt(60),
						(int) posZ + rand.nextInt(90) - rand.nextInt(60));
			}
			flyTowardsTarget();
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

		}

		public void onUpdate(){
			super.onUpdate();
			tailbuffer.calculateChainSwingBuffer(70F, 5, 4, this);
			IceAndFire.channel.sendToAll(new MessageDragonMotion(this.getEntityId(), (float)motionX, (float)motionY, (float)motionZ));
			AnimationTicker.tickAnimations(this);
			if(this.isFlying){
				setRelitiveEntityPosition(mouth, 1.6F, 0.95F);
			}else{
				setRelitiveEntityPosition(mouth, 1.6F, 2.0F);			
			}
			//tester
			//this.worldObj.spawnParticle(EnumParticleTypes.FLAME, mouth.posX, mouth.posY, mouth.posZ, 0, 0, 0, new int[]{0});

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

		public void setFlying(boolean state)
		{
			isFlying = state;
		}

		public boolean interact(EntityPlayer player)
		{

			ItemStack item = player.inventory.getCurrentItem();
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
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonWanderName")));
						}else{
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonWander")));
						}
					}
					if(this.currentOrder == EnumOrder.FOLLOW){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonFollowName")));
						}else{
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonFollow")));
						}
					}
					if(this.currentOrder == EnumOrder.SIT){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonSitName")));
						}else{
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonSit")));
						}
					}
					if(this.currentOrder == EnumOrder.SLEEP){
						if(this.hasCustomName()){
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonSleepName")));
						}else{
							player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonSleep")));
						}
					}
				}

			}else if(!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == player)){
				player.mountEntity(this);

			}
			if(item == null){
				player.openGui(IceAndFire.instance, 0, this.worldObj, getEntityId(), 0, 0);
			}
			return true;
		}
		public void tellOtherPlayersAge(){
			int acctualStage = getStage();
			if(this.worldObj.getClosestPlayerToEntity(this, 16) != null && worldObj.isRemote){
				if(this.hasCustomName()){
					worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonGrownName") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
				}else{
					worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonGrown") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
				}	
			}
		}

		public void setRelitiveEntityPosition(Entity entity, float entityX, float entityY){
			if (this.ringBufferIndex < 0)
			{
				for (int i = 0; i < this.ringBuffer.length; ++i)
				{
					this.ringBuffer[i][0] = (double)this.rotationYaw;
					this.ringBuffer[i][1] = this.posY;
				}
			}

			if (++this.ringBufferIndex == this.ringBuffer.length)
			{
				this.ringBufferIndex = 0;
			}

			this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
			this.ringBuffer[this.ringBufferIndex][1] = this.posY;
			this.mouth.onUpdate();

			double[] adouble1 = this.getMovementOffsets(5, 1.0F);
			double[] adouble = this.getMovementOffsets(0, 1.0F);
			float f1 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
			float f2 = MathHelper.cos(f1);
			float f10 = -MathHelper.sin(f1);
			float f12 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			float f13 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			entity.setLocationAndAngles(this.posX + (double)(f12 *  -width/entityX * f2), this.posY + height/entityY, this.posZ - (double)(f13 * -width/entityX * f2), 0.0F, 0.0F);

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

			this.worldObj.playSoundAtEntity(this, "random.eat",
					this.getSoundVolume(), this.getSoundPitch());
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
			return this.dataWatcher.getWatchableObjectInt(18);
		}

		public void setColor(int i)
		{
			this.dataWatcher.updateObject(18, i);
		}

		public int getGender()
		{
			return this.dataWatcher.getWatchableObjectInt(19);
		}

		public void setGender(int i)
		{
			this.dataWatcher.updateObject(19, i);
		}

		public int getSleeping()
		{
			return this.dataWatcher.getWatchableObjectInt(20);
		}

		public void setSleeping(int i)
		{
			this.dataWatcher.updateObject(20, i);
		}
		public int getTier()
		{
			return this.dataWatcher.getWatchableObjectInt(21);
		}

		public void setTier(int i)
		{
			this.dataWatcher.updateObject(21, i);
		}
		public int getDragonAge()
		{
			return this.dataWatcher.getWatchableObjectInt(22);
		}

		public void setDragonAge(int i)
		{
			this.dataWatcher.updateObject(22, i);
			this.updateSize();
		}

		public int getDragonAgeTick()
		{
			return this.dataWatcher.getWatchableObjectInt(23);
		}

		public void setDragonAgeTick(int i)
		{
			this.dataWatcher.updateObject(23, i);
		}
		public int getHunger()
		{
			return this.dataWatcher.getWatchableObjectInt(24);
		}
		public void setHunger(int i)
		{
			this.dataWatcher.updateObject(24, i);
		}
		public int getHungerTick()
		{
			return this.dataWatcher.getWatchableObjectInt(25);
		}
		private int getCurrentAttack() {
			return this.dataWatcher.getWatchableObjectInt(26);
		}
		private void setCurrentAttack(int i) {
			this.dataWatcher.updateObject(26, i);

		}
		protected void entityInit()
		{
			super.entityInit();
			this.dataWatcher.addObject(18, 0);
			this.dataWatcher.addObject(19, 0);
			this.dataWatcher.addObject(20, 0);
			this.dataWatcher.addObject(21, 0);
			this.dataWatcher.addObject(22, 0);
			this.dataWatcher.addObject(23, 0);
			this.dataWatcher.addObject(24, 30);
			this.dataWatcher.addObject(25, 300);
			this.dataWatcher.addObject(26, 0);

		}
		@Override
		protected boolean canDespawn()
		{
			return false;
		}
		public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData data)
		{
			this.onSpawn();
			return super.func_180482_a(difficulty, data);
		}

		public abstract void onSpawn();

		public void setHungerTick(int i)
		{
			this.dataWatcher.updateObject(25, i);
		}
		public boolean isAdult()
		{
			return this.getDragonAge() >= 75;
		}

		public boolean isTeen()
		{
			return  this.getDragonAge() > 50 && this.getDragonAge() < 100;
		}
		public boolean isChild()
		{
			return this.getDragonAge() <= 50;
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
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
			getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);

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
				this.motionX *= 0D;
				this.motionZ *= 0D;
			}else{
				super.moveEntityWithHeading(x, z);
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

		public boolean attackEntityAsMob(Entity entity)
		{

			if(this.getAnimation().animationId == 0){
				this.setAnimation(animation_flame1);
			}

			float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
			int i = 0;

			if (entity instanceof EntityLivingBase)
			{
				f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)entity).getCreatureAttribute());
				i += EnchantmentHelper.getKnockbackModifier(this);
			}

			boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

			if (flag)
			{
				if (i > 0)
				{
					entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
					this.motionX *= 0.6D;
					this.motionZ *= 0.6D;
				}

				int j = EnchantmentHelper.getFireAspectModifier(this);

				if (j > 0)
				{
					entity.setFire(j * 4);
				}

				this.func_174815_a(this, entity);
			}

			setNextAttack();
			return flag;
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

		public final Animation[] animations() {
			return new Animation[]{this.animation_none, this.animation_roar1, this.animation_flame1};
		}

		public boolean canAttackMob(EntityLivingBase targetEntity) {
			return this.getDragonAge() > targetEntity.width || this.blacklist.contains(targetEntity.getClass());
		}


		public void attackEntityWithRangedAttack(EntityLivingBase entity, float f){

			if(this.getAnimation() != animation_flame1){
				this.setAnimation(animation_flame1);
			}
			this.mouth.breathFire(entity);
			setNextAttack();
		}

		public boolean shouldSetFire(EntityLivingBase entitylivingbase) {
			return this.getCurrentAttack() == 1;
		}

		public int pickUpFood(ItemStack stack) {
			return 0;
		}

	}
