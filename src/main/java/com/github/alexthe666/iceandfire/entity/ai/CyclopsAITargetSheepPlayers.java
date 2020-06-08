package com.github.alexthe666.iceandfire.entity.ai;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import java.util.function.Predicate;


public class CyclopsAITargetSheepPlayers<T extends LivingEntity> extends NearestAttackableTargetGoal {

    public CyclopsAITargetSheepPlayers(MobEntity goalOwnerIn, Class targetClassIn, boolean checkSight) {
        super(goalOwnerIn, targetClassIn, 0, checkSight, true, new Predicate<LivingEntity>() {
            @Override
            public boolean test (LivingEntity livingEntity){
                return false; //TODO Sheep hunt cyclops
            }
        });
    }


}