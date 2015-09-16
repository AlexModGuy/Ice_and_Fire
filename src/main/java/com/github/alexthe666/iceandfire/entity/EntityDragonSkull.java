package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.core.ModItems;

public class EntityDragonSkull extends EntityLiving{

	public final float minSize = 0.3F;
	public final float maxSize = 8.58F;
	private float field_98056_d = -1.0F;
	private float field_98057_e;
	public EntityDragonSkull(World worldIn) {
		super(worldIn);
		this.setSize(1.45F, 0.65F);
		this.ignoreFrustumCheck = true;
		//setScale(this.getDragonAge());
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10);
	}


	private boolean isAIDisabled()
	{
		return true;
	}


	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, 0);
		this.dataWatcher.addObject(21, 0);
		this.dataWatcher.addObject(22, 0);
		this.dataWatcher.addObject(23, 0);

	}

	public int getType()
	{
		return this.dataWatcher.getWatchableObjectInt(20);
	}

	public void setType(int var1)
	{
		this.dataWatcher.updateObject(20, var1);
	}

	public int getStage()
	{
		return this.dataWatcher.getWatchableObjectInt(21);
	}

	public void setStage(int var1)
	{
		this.dataWatcher.updateObject(21, var1);
	}

	public int getDragonAge()
	{
		return this.dataWatcher.getWatchableObjectInt(22);
	}

	public void setDragonAge(int var1)
	{
		this.dataWatcher.updateObject(22, var1);
	}

	public int getMouthState()
	{
		return this.dataWatcher.getWatchableObjectInt(23);
	}

	public void setMouthState(int var1)
	{
		this.dataWatcher.updateObject(23, var1);
	}
	
	public String getHurtSound(){
		return "none";	
	}

	@Override
	public boolean attackEntityFrom(DamageSource var1, float var2)
	{
		this.turnIntoItem();
		return super.attackEntityFrom(var1, var2);
	}

	public void turnIntoItem(){
		ItemStack stack = new ItemStack(ModItems.dragon_skull, 1);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Stage", this.getStage());
		stack.getTagCompound().setInteger("DragonType", this.getType());
		stack.getTagCompound().setInteger("DragonAge", this.getDragonAge());
		if(!this.worldObj.isRemote)
			this.entityDropItem(stack, 0.0F);
		this.setDead();

	}

	public boolean interact(EntityPlayer player)
	{
		if (player.isSneaking())
		{
			this.rotationYaw = player.rotationYaw;
			}else{
			if(this.getMouthState() == 0){
				this.setMouthState(1);
			}
			if(this.getMouthState() == 1){
				this.setMouthState(0);
			}
		}
		return super.interact(player);
	}
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.setType(compound.getInteger("Type"));
		this.setStage(compound.getInteger("Stage"));
		this.setDragonAge(compound.getInteger("DragonAge"));
		this.setMouthState(compound.getInteger("MouthState"));

		super.readEntityFromNBT(compound);
	}
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Type", this.getType());
		compound.setInteger("Stage", this.getStage());
		compound.setInteger("DragonAge", this.getDragonAge());
		compound.setInteger("MouthState", this.getMouthState());

		super.writeEntityToNBT(compound);
	}

	public float getDragonSize()
	{
		float step;
		step = (minSize - maxSize) / (125);

		if (this.getDragonAge() > 125)
		{
			return this.minSize + (step * 125);
		}

		return this.minSize + (step * this.getDragonAge());
	}


	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	public boolean canBePushed()
	{
		return false;
	}

	protected void collideWithEntity(Entity entity) {}

}
