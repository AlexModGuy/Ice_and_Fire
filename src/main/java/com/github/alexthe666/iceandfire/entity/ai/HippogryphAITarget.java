package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class HippogryphAITarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.hippogryph = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.width >= this.targetEntity.width) {
                if(this.targetEntity instanceof EntityPlayer && !hippogryph.isOwner(this.targetEntity)){
                    return !hippogryph.isTamed();
                }else {
                    if (!hippogryph.isOwner(this.targetEntity) && hippogryph.canMove() && this.targetEntity instanceof EntityAnimal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}