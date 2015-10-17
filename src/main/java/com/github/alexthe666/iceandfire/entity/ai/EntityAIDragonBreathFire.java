package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EntityAIDragonBreathFire extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final EntityDragonBase rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float maxAttackDistance;

    public EntityAIDragonBreathFire(EntityDragonBase dragon, double movespeed, int idk, float maxRangedAttackTime)
    {
        this(dragon, movespeed, idk, idk, maxRangedAttackTime);
    }

    public EntityAIDragonBreathFire(EntityDragonBase dragon, double movespeed, int idk, int maxRangedAttackTime, float maxAttackDistance)
    {
        this.rangedAttackTime = -1;

        if (!(dragon instanceof EntityLivingBase))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = dragon;
            this.entityHost = (EntityLiving)dragon;
            this.entityMoveSpeed = movespeed;
            this.field_96561_g = idk;
            this.maxRangedAttackTime = maxRangedAttackTime;
            this.field_96562_i = maxAttackDistance;
            this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
            this.setMutexBits(3);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null || !((EntityDragonBase)entityHost).shouldSetFire(entitylivingbase))
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (flag)
        {
            ++this.field_75318_f;
        }
        else
        {
            this.field_75318_f = 0;
        }

        if (d0 <= (double)this.maxAttackDistance && this.field_75318_f >= 20)
        {
            this.entityHost.getNavigator().clearPathEntity();
        }
        else
        {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0)
        {
            if (d0 > (double)this.maxAttackDistance || !flag)
            {
                return;
            }

            f = MathHelper.sqrt_double(d0) / this.field_96562_i;
            float f1 = MathHelper.clamp_float(f, 0.1F, 1.0F);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
            this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = MathHelper.sqrt_double(d0) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
        }
    }
}