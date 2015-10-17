package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**Part for Dragon's Mouth*/
public class EntityDragonMouth extends Entity
{
	public final EntityDragonBase entityDragonObj;
	public final String partName;

	public EntityDragonMouth(EntityDragonBase parent, String partName)
	{
		super(parent.worldObj);
		this.setSize(0.2F, 0.2F);
		entityDragonObj = parent;
		this.partName = partName;
	}

	public void breathFire(Entity entity){

		EntityLivingBase entitylivingbase = this.entityDragonObj.getAttackTarget();
		double d0 = 64.0D;
		if(entitylivingbase != null){
			if (entitylivingbase.getDistanceSqToEntity(this.entityDragonObj) < d0 * d0 && this.entityDragonObj.canEntityBeSeen(entitylivingbase))
			{
				World world = this.entityDragonObj.worldObj;
                double d1 = 0D;
                Vec3 vec3 = this.entityDragonObj.getLook(1.0F);
                double d2 = entitylivingbase.posX - (this.posX + vec3.xCoord * d1);
                double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.posY + (double)(this.height / 2.0F));
                double d4 = entitylivingbase.posZ - (this.posZ + vec3.zCoord * d1);
                world.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(this), 0);
                EntityDragonFire entitylargefireball = new EntityDragonFire(world, this.entityDragonObj, d2, d3, d4);
                entitylargefireball.posX = this.posX + vec3.xCoord * d1;
                entitylargefireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
                entitylargefireball.posZ = this.posZ + vec3.zCoord * d1;
                world.spawnEntityInWorld(entitylargefireball);
			}
		}
	}



	protected void entityInit() {}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	 protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	 protected void writeEntityToNBT(NBTTagCompound tagCompound) {}

	 /**
	  * Returns true if other Entities should be prevented from moving through this Entity.
	  */
	 public boolean canBeCollidedWith()
	 {
		 return false;
	 }

	 /**
	  * Called when the entity is attacked.
	  */
	 public boolean attackEntityFrom(DamageSource source, float amount)
	 {
		 return false;
	 }

	 /**
	  * Returns true if Entity argument is equal to this Entity
	  */
	 public boolean isEntityEqual(Entity entityIn)
	 {
		 return this == entityIn || entityDragonObj == entityIn;
	 }
}