package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.EntityUtils;
import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class DragonAITarget extends EntityAINearestAttackableTarget<EntityLivingBase> {
    private EntityDragonBase dragon;


    public DragonAITarget(EntityDragonBase entityIn, boolean checkSight) {
        super(entityIn, EntityLivingBase.class, checkSight, false);
        this.dragon = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (!dragon.canMoveWithoutSleeping()) {
            return false;
        }
        return super.shouldExecute();
    }

    @Override
    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles) {
        if (super.isSuitableTarget(target, includeInvincibles)) {
            if (target != null && EntityUtils.isEntityAlive(target) && !dragon.getClass().equals(target.getClass())) {
                if (dragon.isOwnersPet(target)) {
                    return false;
                }
                float dragonSize = Math.max(this.dragon.width, this.dragon.width * (dragon.getRenderSize() / 3));
                if (dragonSize >= target.width) {
                    if (target instanceof EntityPlayer && dragon.isTamed()) {
                        return false;
                    } else {
                        if (FoodUtils.getFoodPoints(target) > 0
                                && (dragon.getHunger() < 90 || !dragon.isTamed() && target instanceof EntityPlayer)) {
                            return !dragon.isTamed() || DragonUtils.canTameDragonAttack(dragon, target);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.dragon.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }
}