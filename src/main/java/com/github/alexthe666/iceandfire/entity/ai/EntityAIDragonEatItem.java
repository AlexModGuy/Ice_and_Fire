package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemFood;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class EntityAIDragonEatItem extends EntityAIBase{

	private EntityDragonBase dragon;
	private EntityItem fooditem;
	private double destX;
	private double destY;
	private double destZ;
	private PathEntity path;
	protected final EntityAIDragonEatItem.Sorter theNearestAttackableTargetSorter;
	protected Predicate targetEntitySelector;


	public EntityAIDragonEatItem(EntityDragonBase dragon){
		this.dragon = dragon;
		this.theNearestAttackableTargetSorter = new EntityAIDragonEatItem.Sorter(dragon);
		this.targetEntitySelector = new Predicate()
		{
			public boolean isSuitable(Entity i)
			{
				return EntityAIDragonEatItem.this.isSuitableTarget(i);
			}

			public boolean apply(Object obj)
			{
				return this.isSuitable((Entity)obj);
			}
		}
		;
	}

	protected boolean isSuitableTarget(Entity entity) {
		if (!this.dragon.isWithinHomeDistanceFromPosition(new BlockPos(entity)))
		{
			return false;
		}
		else
		{		
			return true;
		}
	}

	public boolean isInterruptible()
	{
		return true;
	}

	public boolean shouldExecute(){
		fooditem = getNearestItem(10);
		if(fooditem != null){
			this.destX = fooditem.posX;
			this.destY = fooditem.posY;
			this.destZ = fooditem.posZ;
			path = this.dragon.getNavigator().getPathToXYZ(this.destX, this.destY, this.destZ);	//this.dragon.getNavigator().tryMoveToXYZ(this.destX, this.destY, this.destZ, 1.0D);
			return path != null;
		}
		return false;
	}

	@Override
	public boolean continueExecuting()
	{
		return fooditem != null || this.dragon.getNavigator().noPath();
	}

	@Override
	public void updateTask()
	{
		double d = Math.sqrt(Math.pow(this.dragon.posX - this.destX, 2.0D) + Math.pow(this.dragon.posZ - this.destZ, 2.0D));
		if (this.fooditem != null && d < 10  && this.fooditem.isEntityAlive())
		{
			this.dragon.getNavigator().setPath(this.path, 1.0F);
			if (d < 2.5)
			{                
				int i = this.dragon.pickUpFood(this.fooditem.getEntityItem());

				if (i > 0)
				{
					this.fooditem.getEntityItem().stackSize = i;
					resetTask();
				}
				else
				{
					this.fooditem.setDead();
					resetTask();
				}
			}
		}

		/*if (this.fooditem != null && this.fooditem.isEntityAlive())
		{
			int i = this.dragon.pickUpFood(this.fooditem.getEntityItem());

			if (i > 0)
			{
				this.fooditem.getEntityItem().stackSize = i;
				endTask();
			}
			else
			{
				this.fooditem.setDead();
				endTask();
			}
		}*/

	}

	public void resetTask()
	{
		this.dragon.getNavigator().clearPathEntity();
		fooditem = null;
	}
	private EntityItem getNearestItem(int range)
	{
		List nearbyItems = dragon.worldObj.getEntitiesWithinAABB(EntityItem.class, dragon.getEntityBoundingBox().expand(range, range, range), Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
		if(!nearbyItems.isEmpty()){
			Iterator iterateNearbyItems = nearbyItems.iterator();
			EntityItem entityItem = null;

			while (iterateNearbyItems.hasNext())
			{

				EntityItem entityItem1 = (EntityItem) iterateNearbyItems.next();

				if (entityItem1.getEntityItem() != null && dragon.getDistanceSqToEntity(entityItem1) < range)
				{
					if(entityItem1.getEntityItem().getItem() != null){
						if(entityItem1.getEntityItem().getItem() instanceof ItemFood && ((ItemFood)entityItem1.getEntityItem().getItem()).isWolfsFavoriteMeat()){
							entityItem = entityItem1;
						}
					}
				}
			}
			return entityItem;
		}
		return null;
	}

	public static class Sorter implements Comparator
	{
		private final Entity theEntity;

		public Sorter(Entity p_i1662_1_)
		{
			this.theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_)
		{
			double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		public int compare(Object p_compare_1_, Object p_compare_2_)
		{
			return this.compare((Entity)p_compare_1_, (Entity)p_compare_2_);
		}


	}
	protected double getTargetDistance()
	{
		IAttributeInstance iattributeinstance = this.dragon.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
}
