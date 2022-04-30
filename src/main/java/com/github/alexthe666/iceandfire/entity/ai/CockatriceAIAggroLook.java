package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class CockatriceAIAggroLook extends NearestAttackableTargetGoal<PlayerEntity> {

    private final EntityCockatrice cockatrice;
    private PlayerEntity player;
    private final EntityPredicate predicate;

    public CockatriceAIAggroLook(EntityCockatrice endermanIn) {
        super(endermanIn, PlayerEntity.class, false);
        this.cockatrice = endermanIn;
        this.predicate = new EntityPredicate() {

            @Override
            public boolean canTarget(LivingEntity attacker, LivingEntity target) {
                return EntityGorgon.isEntityLookingAt(target, CockatriceAIAggroLook.this.cockatrice,
                    EntityCockatrice.VIEW_RADIUS)
                    && CockatriceAIAggroLook.this.cockatrice.getDistance(target) < getTargetDistance();
            }

        };
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (cockatrice.isTamed())
            return false;
        this.player = this.cockatrice.world.getClosestPlayer(predicate, this.cockatrice.getPosX(),
            this.cockatrice.getPosY(), this.cockatrice.getPosZ());
        return this.player != null;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void resetTask() {
        this.player = null;
        super.resetTask();
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (this.player != null && !this.player.isCreative() && !this.player.isSpectator()) {
            if (!EntityGorgon.isEntityLookingAt(this.player, this.cockatrice, 0.4F)) {
                return false;
            } else {
                this.cockatrice.faceEntity(this.player, 10.0F, 10.0F);
                if (!this.cockatrice.isTamed()) {
                    this.cockatrice.setTargetedEntity(this.player.getEntityId());
                    this.cockatrice.setAttackTarget(this.player);
                }
                return true;
            }
        } else {
            return this.target != null && this.target.isAlive() || super.shouldContinueExecuting();
        }
    }
}
