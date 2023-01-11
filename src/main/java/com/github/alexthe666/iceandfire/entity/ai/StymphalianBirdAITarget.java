package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.base.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class StymphalianBirdAITarget extends NearestAttackableTargetGoal<LivingEntity> {
    private final EntityStymphalianBird bird;

    public StymphalianBirdAITarget(EntityStymphalianBird entityIn, Class<LivingEntity> classTarget, boolean checkSight) {
        super(entityIn, classTarget, 0, checkSight, false, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return !EntityGorgon.isStoneMob(entity) && (entity instanceof Player && !((Player) entity).isCreative() || entity instanceof AbstractVillager || entity instanceof AbstractGolem || entity instanceof Animal && IafConfig.stympahlianBirdAttackAnimals);
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
    protected @NotNull AABB getTargetSearchArea(double targetDistance) {
        return this.bird.getBoundingBox().inflate(targetDistance, targetDistance, targetDistance);
    }
}