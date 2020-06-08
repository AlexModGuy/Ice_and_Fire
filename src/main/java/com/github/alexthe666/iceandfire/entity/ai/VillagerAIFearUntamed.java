package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.IVillagerFear;
import com.google.common.base.Predicates;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Predicate;

public class VillagerAIFearUntamed extends AvoidEntityGoal<LivingEntity> {

    public VillagerAIFearUntamed(CreatureEntity entityIn, Class<LivingEntity> avoidClass, float distance, double nearSpeedIn, double farSpeedIn, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, avoidClass, distance, nearSpeedIn, farSpeedIn, targetPredicate);
    }
}
