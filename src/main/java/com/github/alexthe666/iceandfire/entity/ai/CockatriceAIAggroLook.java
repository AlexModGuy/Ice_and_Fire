package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class CockatriceAIAggroLook extends EntityAINearestAttackableTarget<EntityPlayer> {
    private final EntityCockatrice cockatrice;
    private EntityPlayer player;
    private int aggroTime;
    private int teleportTime;

    public CockatriceAIAggroLook(EntityCockatrice p_i45842_1_) {
        super(p_i45842_1_, EntityPlayer.class, false);
        this.cockatrice = p_i45842_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if(cockatrice.isTamed()){
            return false;
        }
        double d0 = this.getTargetDistance();
        this.player = this.cockatrice.world.getNearestAttackablePlayer(this.cockatrice.posX, this.cockatrice.posY, this.cockatrice.posZ, d0, d0, (Function) null, new Predicate<EntityPlayer>() {
            public boolean apply(@Nullable EntityPlayer p_apply_1_) {
                return p_apply_1_ != null && EntityGorgon.isEntityLookingAt(p_apply_1_, CockatriceAIAggroLook.this.cockatrice, EntityCockatrice.VIEW_RADIUS);
            }
        });
        return this.player != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.aggroTime = 5;
        this.teleportTime = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.player = null;
        super.resetTask();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (this.player != null) {
            if (!EntityGorgon.isEntityLookingAt(this.player, this.cockatrice, 0.4F)) {
                return false;
            } else {
                this.cockatrice.faceEntity(this.player, 10.0F, 10.0F);
                if(!this.cockatrice.isTamed()){
                    this.cockatrice.setTargetedEntity(this.player.getEntityId());
                    this.cockatrice.setAttackTarget(this.player);
                }
                return true;
            }
        } else {
            return this.targetEntity != null && ((EntityPlayer) this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
        }
    }
}
