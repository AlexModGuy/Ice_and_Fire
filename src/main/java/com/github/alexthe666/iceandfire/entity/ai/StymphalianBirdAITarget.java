package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class StymphalianBirdAITarget extends NearestAttackableTargetGoal<LivingEntity> {
    private final EntityStymphalianBird bird;

    public StymphalianBirdAITarget(EntityStymphalianBird entityIn, Class<LivingEntity> classTarget, boolean checkSight) {
        super(entityIn, classTarget, 0, checkSight, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return !EntityGorgon.isStoneMob(entity) && (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative() || entity instanceof AbstractVillagerEntity || entity instanceof GolemEntity || entity instanceof AnimalEntity && IafConfig.stympahlianBirdAttackAnimals);
            }
        });
        this.bird = entityIn;
    }


    @Override
    public boolean canUse() {
        boolean supe = super.canUse();
        if (target != null && bird.getVictor() != null && bird.getVictor().getUUID().equals(target.getUUID())) {
            return false;
        }
        return supe && target != null && !target.getClass().equals(this.bird.getClass());
    }

    @Override
    protected AxisAlignedBB getTargetSearchArea(double targetDistance) {
        return this.bird.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}