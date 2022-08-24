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
            public boolean test(LivingEntity attacker, LivingEntity target) {
                return EntityGorgon.isEntityLookingAt(target, CockatriceAIAggroLook.this.cockatrice,
                    EntityCockatrice.VIEW_RADIUS)
                    && CockatriceAIAggroLook.this.cockatrice.distanceTo(target) < getFollowDistance();
            }

        };
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (cockatrice.isTame())
            return false;
        this.player = this.cockatrice.level.getNearestPlayer(predicate, this.cockatrice.getX(),
            this.cockatrice.getY(), this.cockatrice.getZ());
        return this.player != null;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.player = null;
        super.stop();
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (this.player != null && !this.player.isCreative() && !this.player.isSpectator()) {
            if (!EntityGorgon.isEntityLookingAt(this.player, this.cockatrice, 0.4F)) {
                return false;
            } else {
                this.cockatrice.lookAt(this.player, 10.0F, 10.0F);
                if (!this.cockatrice.isTame()) {
                    this.cockatrice.setTargetedEntity(this.player.getId());
                    this.cockatrice.setTarget(this.player);
                }
                return true;
            }
        } else {
            return this.targetMob != null && this.targetMob.isAlive() || super.canContinueToUse();
        }
    }
}
