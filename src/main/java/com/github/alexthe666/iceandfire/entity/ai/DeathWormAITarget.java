package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class DeathWormAITarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private EntityDeathWorm deathworm;

    public DeathWormAITarget(EntityDeathWorm entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 20, checkSight, false, targetSelector);
        this.deathworm = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.deathworm.getClass())) {
            if (this.targetEntity instanceof EntityPlayer && !deathworm.isOwner(this.targetEntity)) {
                return !deathworm.isTamed();
            } else {
                if (!deathworm.isOwner(this.targetEntity)) {
                    return true;
                }
                if (this.targetEntity instanceof EntityMob) {
                    return true;
                }
            }
        }
        return false;
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.deathworm.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }
}