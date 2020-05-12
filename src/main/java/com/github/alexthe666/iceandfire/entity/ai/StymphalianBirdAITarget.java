package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class StymphalianBirdAITarget<T extends LivingEntity> extends TargetGoal<T> {
    private EntityStymphalianBird bird;

    public StymphalianBirdAITarget(EntityStymphalianBird entityIn, Class<T> classTarget, boolean checkSight) {
        super(entityIn, classTarget, 0, checkSight, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return !EntityGorgon.isStoneMob(entity) && (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() || entity instanceof EntityVillager || entity instanceof EntityGolem || entity instanceof EntityAnimal && IafConfig.stympahlianBirdAttackAnimals);
            }
        });
        this.bird = entityIn;
    }


    @Override
    public boolean shouldExecute() {
        boolean supe = super.shouldExecute();
        if (targetEntity != null && bird.getVictor() != null && bird.getVictor().getUniqueID().equals(targetEntity.getUniqueID())) {
            return false;
        }
        return supe && this.targetEntity != null && !this.targetEntity.getClass().equals(this.bird.getClass());
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.bird.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }
}