package com.github.alexthe666.iceandfire.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.structures.BlockMeta;
import com.github.alexthe666.iceandfire.structures.StructureUtils;

public class EntityDragonFire extends Entity
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	public EntityDragonFire(World worldIn)
	{
		super(worldIn);
		this.setSize(0.1F, 0.1F);
	}

	protected void entityInit() {}

	/**
	 * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
	 * length * 64 * renderDistanceWeight Args: distance
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return distance < d1 * d1;
	}
	public EntityDragonFire(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn);
		this.shootingEntity = shooter;
		this.setSize(0.1F, 0.1F);
		this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = this.motionY = this.motionZ = 0.0D;
		accelX += this.rand.nextGaussian() * 0.4D;
		accelY += this.rand.nextGaussian() * 0.4D;
		accelZ += this.rand.nextGaussian() * 0.4D;
		double d3 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d3 * 0.1D;
		this.accelerationY = accelY / d3 * 0.1D;
		this.accelerationZ = accelZ / d3 * 0.1D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{

		this.setSize(width + 0.0000005F * ticksExisted, height + 0.0000005F * ticksExisted);
		for (int i = 0; i < 2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + (this.rand.nextDouble() - 0.5D) * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
		}


		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this))))
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();

			if (this.inGround)
			{
				if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
				{
					++this.ticksAlive;

					if (this.ticksAlive == 600)
					{
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.ticksAlive = 0;
				this.ticksInAir = 0;
			}
			else
			{
				++this.ticksInAir;
			}

			Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
			vec3 = new Vec3(this.posX, this.posY, this.posZ);
			vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (movingobjectposition != null)
			{
				vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity)list.get(i);

				if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25))
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f, (double)f, (double)f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null)
					{
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null)
			{
				this.onImpact(movingobjectposition);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

			for (this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f2 = this.getMotionFactor();

			if (this.isInWater())
			{
				for (int j = 0; j < 4; ++j)
				{
					float f3 = 0.25F;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
				}

				f2 = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double)f2;
			this.motionY *= (double)f2;
			this.motionZ *= (double)f2;
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the original motion.
	 */
	protected float getMotionFactor()
	{
		return 0.95F;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movingObject){
		if (!this.worldObj.isRemote)
		{
			boolean flag;

			if (movingObject.entityHit != null)
			{
				flag = movingObject.entityHit.attackEntityFrom(DamageSource.onFire, 5.0F);

				if (flag)
				{
					this.func_174815_a(this.shootingEntity, movingObject.entityHit);

					if (!movingObject.entityHit.isImmuneToFire())
					{
						movingObject.entityHit.setFire(5);
					}
				}
			}
			else
			{
				flag = true;

				if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
				{
					flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
				}

				if (flag)
				{
					BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit).down();

					if (this.worldObj.isAirBlock(blockpos))
					{
						StructureUtils.setBlock(worldObj, blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.getImpactBlock(worldObj.getBlockState(blockpos).getBlock()).getBlock(),
								this.getImpactBlock(worldObj.getBlockState(blockpos).getBlock()).getMeta(), 3);
					}
					spawnCrater(worldObj, blockpos, this.getImpactBlock(worldObj.getBlockState(blockpos).getBlock()));
				}
			}

			this.setDead();
		}
	}

	private void spawnCrater(World world, BlockPos pos, BlockMeta blockmeta){
		if(shootingEntity != null && shootingEntity instanceof EntityDragonBase){
			switch(((EntityDragonBase)shootingEntity).getStage()){
			default:
				this.generateSphere(world, 0, pos, blockmeta);
			case 3:
				this.generateSphere(world, 0, pos, blockmeta);
				break;
			case 4:
				this.generateSphere(world, 2, pos, blockmeta);
				break;
			case 5:
				this.generateSphere(world, 3, pos, blockmeta);
				break;
			}
		}
	}
	private BlockMeta getImpactBlock(Block block) {
		if(shootingEntity != null && shootingEntity instanceof EntityDragonBase){
			switch(((EntityDragonBase)shootingEntity).getStage()){
			default:

				if(block.getMaterial() == Material.grass){
					return new BlockMeta(Blocks.dirt, 1);
				}
				if(block.getMaterial() == Material.air){
					return new BlockMeta(Blocks.fire, 0);
				}
				break;

			case 2:
				if(block.getMaterial() == Material.grass){
					return new BlockMeta(Blocks.dirt, 1);
				}
				if(block.getMaterial() == Material.air){
					return new BlockMeta(Blocks.fire, 0);
				}
				break;

			case 3:
				if(block.getMaterial() == Material.grass){
					return new BlockMeta(ModBlocks.charedGrass, 0);
				}
				if(block.getMaterial() == Material.air){
					return new BlockMeta(ModBlocks.charedDirt, 0);
				}
				break;
			case 4:
				if(block.getMaterial() == Material.grass){
					return new BlockMeta(ModBlocks.charedGrass, 0);
				}
				if(block.getMaterial() == Material.air){
					return new BlockMeta(ModBlocks.charedDirt, 0);
				}
				break;
			case 5:
				if(block.getMaterial() == Material.grass){
					return new BlockMeta(ModBlocks.charedGrass, 0);
				}
				if(block.getMaterial() == Material.air){
					return new BlockMeta(ModBlocks.charedDirt, 0);
				}
				break;
			}
		}
		return new BlockMeta(Blocks.air, 0);
	}

	public void generateSphere(World world, int radius, BlockPos blockpos, BlockMeta block){
		for(float i = 0; i < radius; i += 0.5) {
			for(float j = 0; j < 2 * Math.PI * i; j += 0.5)
				StructureUtils.setBlock(world, (int)Math.floor(blockpos.getX() + Math.sin(j) * i), blockpos.getY() + radius, (int)Math.floor(blockpos.getZ() + Math.cos(j) * i), block.getBlock(), block.getMeta(), 3);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setShort("xTile", (short)this.xTile);
		tagCompound.setShort("yTile", (short)this.yTile);
		tagCompound.setShort("zTile", (short)this.zTile);
		ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
		tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		tagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		this.xTile = tagCompund.getShort("xTile");
		this.yTile = tagCompund.getShort("yTile");
		this.zTile = tagCompund.getShort("zTile");

		if (tagCompund.hasKey("inTile", 8))
		{
			this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
		}
		else
		{
			this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
		}

		this.inGround = tagCompund.getByte("inGround") == 1;

		if (tagCompund.hasKey("direction", 9))
		{
			NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
			this.motionX = nbttaglist.getDouble(0);
			this.motionY = nbttaglist.getDouble(1);
			this.motionZ = nbttaglist.getDouble(2);
		}
		else
		{
			this.setDead();
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return true;
	}

	public float getCollisionBorderSize()
	{
		return 1.0F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			this.setBeenAttacked();

			if (source.getEntity() != null)
			{
				Vec3 vec3 = source.getEntity().getLookVec();

				if (vec3 != null)
				{
					this.motionX = vec3.xCoord;
					this.motionY = vec3.yCoord;
					this.motionZ = vec3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}

				if (source.getEntity() instanceof EntityLivingBase)
				{
					this.shootingEntity = (EntityLivingBase)source.getEntity();
				}

				return true;
			}
			else
			{
				return false;
			}
		}
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}
}