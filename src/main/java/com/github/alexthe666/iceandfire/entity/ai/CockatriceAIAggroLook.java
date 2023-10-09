package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class CockatriceAIAggroLook extends NearestAttackableTargetGoal<Player> {

    private final EntityCockatrice cockatrice;
    private final TargetingConditions predicate;
    private Player player;

    public CockatriceAIAggroLook(EntityCockatrice cockatriceIn) {
        super(cockatriceIn, Player.class, false);
        this.cockatrice = cockatriceIn;
        Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (target) -> {
            return EntityGorgon.isEntityLookingAt(target, this.cockatrice,
                EntityCockatrice.VIEW_RADIUS) && cockatrice.distanceTo(target) < getFollowDistance();
        };
        this.predicate = TargetingConditions.forCombat().range(25.0D).selector(LIVING_ENTITY_SELECTOR);
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canUse() {
        if (cockatrice.isTame())
            return false;
        this.player = this.cockatrice.level().getNearestPlayer(predicate, this.cockatrice.getX(),
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
