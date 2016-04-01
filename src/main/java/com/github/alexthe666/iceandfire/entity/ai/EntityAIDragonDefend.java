package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumOrder;

public class EntityAIDragonDefend extends EntityAITarget
{
    EntityDragonBase dragon;
    EntityLivingBase theOwnerAttacker;
    private int field_142051_e;
    private static final String __OBFID = "CL_00001624";

    public EntityAIDragonDefend(EntityDragonBase dragon)
    {
        super(dragon, false);
        this.dragon = dragon;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if(!dragon.getPassengers().isEmpty()){
        	 if(dragon.getPassengers().contains(dragon.getOwner())){
             	return false;
             }	
        }

        if (!this.dragon.isTamed())
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase) this.dragon.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theOwnerAttacker = entitylivingbase.getAITarget();
                int i =  entitylivingbase.getRevengeTimer();
                return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.dragon.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	 if(dragon.worldObj.getClosestPlayerToEntity(dragon, 16) != null){
         	if(dragon.worldObj.getClosestPlayerToEntity(dragon, 16) == dragon.getOwner()){
         		if(dragon.currentOrder == EnumOrder.SLEEP || dragon.currentOrder == EnumOrder.SIT){
         			dragon.currentOrder = EnumOrder.WANDER;
         		}
         	}
         }
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entitylivingbase = (EntityLivingBase) this.dragon.getOwner();

        if (entitylivingbase != null)
        {
            this.field_142051_e = entitylivingbase.getRevengeTimer();
        }
       

        super.startExecuting();
    }
}