package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.google.common.base.Predicates;

public class EntityAIDragonHunt extends EntityAINearestAttackableTarget
{
	private EntityDragonBase dragon;
	private final Class targetClass;

	public EntityAIDragonHunt(EntityDragonBase mob, Class prey, boolean see, boolean isFire)
	{
		super(mob, prey, see);
		this.dragon = mob;
		this.targetClass = prey;
	}


	public boolean shouldExecute()
	{

		if (this.taskOwner.getRNG().nextInt(10) != 0)
		{
			return false;
		}
		else
		{
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, IEntitySelector.NOT_SPECTATING));
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty())
			{
				return false;
			}
			else
			{
				this.targetEntity = (EntityLivingBase)list.get(0);

				if(!(targetEntity instanceof EntityFireDragon) && dragon.canAttackMob(targetEntity)){
				
					return true;
				}else{
					return false;
				}
			}
		}
	}
	

}