package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.google.common.base.Predicate;
import fossilsarcheology.api.FoodMappings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class DragonAITarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private EntityDragonBase dragon;

    public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.dragon = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.dragon.getClass())) {
            if (this.dragon.width >= this.targetEntity.width) {
                if (!dragon.isOwner(this.targetEntity) && FoodMappings.INSTANCE.getEntityFoodAmount(this.targetEntity.getClass(), this.dragon.diet) > 0 && dragon.canMove() && dragon.getHunger() < 90) {
                    return true;
                }
            }
        }
        return false;
    }
}