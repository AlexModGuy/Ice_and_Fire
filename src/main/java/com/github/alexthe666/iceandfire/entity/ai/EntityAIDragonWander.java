package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumOrder;

public class EntityAIDragonWander extends EntityAIBase
{
    private EntityDragonBase dragon;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public EntityAIDragonWander(EntityDragonBase dragon)
    {
        this.dragon = dragon;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.dragon.currentOrder == null)
        {
            this.dragon.currentOrder = EnumOrder.WANDER;
        }

        if (this.dragon.getRNG().nextInt(120) != 0)
        {
            return false;
        }
        else if (this.dragon.isTamed() && this.dragon.currentOrder != EnumOrder.WANDER)
        {
            return false;
        }
        else
        {
            Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.dragon, 10, 7);

            if (vec3 == null)
            {
                return false;
            }
            else
            {
                this.xPosition = vec3.xCoord;
                this.yPosition = vec3.yCoord;
                this.zPosition = vec3.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.dragon.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.dragon.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1);
    }
}
