package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;

public class DragonAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private EntityDragonBase dragon;

    public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        this.dragon = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if(dragon.getCommand() == 1 || dragon.getCommand() == 2 || dragon.isSleeping()){
            return false;
        }
        if (super.shouldExecute() && this.target != null && !this.target.getClass().equals(this.dragon.getClass())) {
            float dragonSize = Math.max(this.dragon.getWidth(), this.dragon.getWidth() * (dragon.getRenderSize() / 3));
            if (dragonSize >= this.target.getWidth()) {
                if (this.target instanceof PlayerEntity && !dragon.isTamed()) {
                    return true;
                }
                if (this.target instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) this.target;
                    if (dragon.getOwner() != null && this.dragon.getOwner() != null && this.dragon.isOwner(dragon.getOwner())) {
                        return false;
                    }
                    return !dragon.isModelDead();
                }
                if (this.target instanceof PlayerEntity && dragon.isTamed()) {
                    return false;
                } else {
                    if (!dragon.isOwner(this.target) && FoodUtils.getFoodPoints(this.target) > 0 && dragon.canMove() && (dragon.getHunger() < 90 || !dragon.isTamed() && this.target instanceof PlayerEntity)) {
                        if (dragon.isTamed()) {
                            return DragonUtils.canTameDragonAttack(dragon, this.target);
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.dragon.getBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    protected double getTargetDistance() {
        IAttributeInstance iattributeinstance = this.goalOwner.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 64.0D : iattributeinstance.getValue();
    }
}