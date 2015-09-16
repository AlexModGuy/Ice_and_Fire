package com.github.alexthe666.iceandfire.entity;


import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonAge;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonDefend;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonFollow;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonStarve;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIDragonWander;
import com.github.alexthe666.iceandfire.enums.EnumOrder;

public abstract class EntityDragonBase extends EntityTameable{

	public float animation_frame;
	public double baseHealth;
	public double baseDamage;
	public double baseSpeed;
	public double maxHealth;
	public double maxDamage;
	public double maxSpeed;
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

	public EntityDragonBase(World worldIn) {
		super(worldIn);
		this.currentOrder = EnumOrder.WANDER;
		((PathNavigateGround)this.getNavigator()).func_179690_a(true);
		this.mouth = new EntityDragonMouth(this, "head");
		this.tasks.addTask(0, new EntityAIDragonAge(this));
		this.tasks.addTask(0, new EntityAIDragonStarve(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySheep.class, true));
		this.tasks.addTask(4, new EntityAIDragonWander(this));
		this.tasks.addTask(5, new EntityAIDragonFollow(this, 10, 2));
		this.tasks.addTask(6, new EntityAIDragonDefend(this));

		this.setHunger(50);
		this.setHealth(10F);
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
		if (this.getDragonAge() < 124)
		{
			this.setDragonAge(this.getDragonAge() + 1);
			this.tellAge();
			return true;
		}

		return false;
	}
	public void breakBlock(float hardness)
	{

		for (int a = (int) Math.round(this.getBoundingBox().minX) - 1; a <= (int) Math.round(this.getBoundingBox().maxX) + 1; a++)
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
		}
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
		if(this.getSleeping() == 0 && this.currentOrder == EnumOrder.SLEEP){
			this.setSleeping(1);
		}
		if(this.getSleeping() == 1 && this.isTamed() && this.currentOrder != EnumOrder.SLEEP){
			this.setSleeping(0);
		}

		if(this.getSleeping() == 1){
			if(worldObj.getClosestPlayerToEntity(this, 12) != null){
				if(!worldObj.getClosestPlayerToEntity(this, 12).isSneaking() && !this.isTamed()){
					this.setSleeping(0);
					this.setAttackTarget(worldObj.getClosestPlayerToEntity(this, 12));
				}
			}
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
				if (this.isAdult())
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
						if (getAttackTarget() != null)
						{
							currentTarget = new BlockPos((int) getAttackTarget().posX, ((int) getAttackTarget().posY + getAttackTarget().getEyeHeight()), (int) getAttackTarget().posZ);
							setFlying(false);
							flyTowardsTarget();
						}
					}
				}
			}

		if(this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) > 1){
			this.getAttackTarget().mountEntity(this);
		}
	}

	public void updateRiderPosition()
	{
		super.updateRiderPosition();
		if (this.riddenByEntity != null)
		{
			if (this.riddenByEntity instanceof EntityAnimal)
			{
				rotationYaw = renderYawOffset;
				((EntityAnimal) this.riddenByEntity).rotationYawHead = this.riddenByEntity.rotationYaw;
				float radius = 0.4F * this.getCreatureLength();
				float angle = (float) (0.01745329251F * this.renderYawOffset + (0.05 *   this.getCreatureLength() * Math.cos(this.ticksExisted * 0.6 + 1)));
				this.riddenByEntity.rotationYaw = (float) (angle * (180 / Math.PI) - 150.0F);
				((EntityAnimal) this.riddenByEntity).renderYawOffset = (float) (angle * (180 / Math.PI) - 150.0F);
				if(this.getDragonSize() > 2){
					
					this.riddenByEntity.setPosition(this.posX + 0.5, this.posY - 0F, this.posZ + 0);
				}else{
					this.riddenByEntity.setPosition(this.posX + 0.5, this.posY - 0F, this.posZ + 0);
				}
				this.setFlying(true);
				this.motionX = 0;
				this.motionZ = 0;
			}
		}
		/*  if (this.riddenByEntity != null)
	        {
	            if (this.riddenByEntity instanceof EntityPlayer)
	            {
	                rotationYaw = renderYawOffset;
	                ((EntityGallimimus) this.riddenByEntity).rotationYawHead = this.riddenByEntity.rotationYaw;
	                float shakeProgress = shakePrey.getAnimationProgressSinSqrt();
	                float radius = 0.4F * this.getCreatureLength();
	                float angle = (float) (0.01745329251F  this.renderYawOffset + (0.05  this.getCreatureLength()  shakeProgress  Math.cos(frame * 0.6 + 1)));
	                this.riddenByEntity.rotationYaw = (float) (angle * (180 / Math.PI) - 150.0F);
	                ((EntityGallimimus) this.riddenByEntity).renderYawOffset = (float) (angle * (180 / Math.PI) - 150.0F);
	                double extraY = this.getCreatureHeight()  (0.425 - shakeProgress  0.21);
	                if (getAnimationTick() > 30)
	                {
	                    extraY += 0.38  Math.sin((getAnimationTick() - 30)  0.2) * getCreatureHeight();
	                    radius -= 0.001  (getAnimationTick() - 30)  (getAnimationTick() - 30) * this.getCreatureLength();
	                }
	                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
	                double extraZ = (double) (radius * MathHelper.cos(angle));
	                this.riddenByEntity.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
	            }
	            else
	            {
	                this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	            }
	    }*/
	}


	private float getCreatureLength() {
		return width;
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

	}
	public void onUpdate(){
		super.onUpdate();
		animation_frame++;
		handleConfusingMaths();
		worldObj.spawnParticle(EnumParticleTypes.FLAME, mouth.posX, mouth.posY, mouth.posZ, 0, 0, 0, new int[0]);
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
				System.out.println("sittin");
			}
			else
			{
				this.setSitting(false);
				System.out.println("not sittin");

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
		if(item != null){
			if(item.getItem() != null){

			}
		}
		return true;
	}
	public void tellOtherPlayersAge(){
		int acctualStage = getStage() + 1;
		if(this.worldObj.getClosestPlayerToEntity(this, 16) != null && worldObj.isRemote){
			if(this.hasCustomName()){
				worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.knownAs") + this.getCustomNameTag() + StatCollector.translateToLocal("message.iceandfire.dragonGrownName") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
			}else{
				worldObj.getClosestPlayerToEntity(this, 16).addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.iceandfire.dragonGrown") + acctualStage + StatCollector.translateToLocal("message.iceandfire.dragonGrownEnd")));
			}	
		}
	}

	public void handleConfusingMaths(){
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
		this.mouth.setLocationAndAngles(this.posX + (double)(f12 *  -width/1.6 * f2), this.posY + height/2, this.posZ - (double)(f13 * -width/1.6 * f2), 0.0F, 0.0F);

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
		return this.getDragonAge() >= 100;
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
		if(this.getDragonAge() == 25 || this.getDragonAge() == 50 || this.getDragonAge() == 75 || this.getDragonAge() == 100 || this.getDragonAge() == 125){
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
	}
	public boolean isHungry() {
		return isDeadlyHungry() ? false : this.getHunger() >= 50;
	}
	private boolean isDeadlyHungry() {
		return this.getHunger() >= 15;
	}
	public int getStage() {
		if(this.getDragonAge() < 26){
			return 1;
		}
		else if(this.getDragonAge() > 25 && this.getDragonAge() < 51){
			return 2;
		}
		else if(this.getDragonAge() > 50 && this.getDragonAge() < 76){
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
		if(this.isFlying && checkXmotion() && checkZmotion()){
			float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			motionX = (double)(-1 * 0.1 * f2);
			motionZ = (double)( 0.1 * f3);
			this.landing = true;
		}else{
			landing = false;
		}
	}
	public boolean checkXmotion(){
		return motionX >= 0.05||motionX <= 0.05;
	}
	public boolean checkZmotion(){
		return motionZ >= 0.05||motionZ <= 0.05;
	}

	public EntityLivingBase closestEntity(){
		return (EntityLivingBase)this.worldObj.findNearestEntityWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand((double)16, 3.0D, 16), this);
	}

}
