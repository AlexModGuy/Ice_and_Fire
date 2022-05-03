package com.github.alexthe666.iceandfire.entity.ai;

import java.util.function.Predicate;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;

import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class MyrmexAIAttackPlayers extends NearestAttackableTargetGoal {

    private EntityMyrmexBase myrmex;

    @SuppressWarnings("unchecked")
    public MyrmexAIAttackPlayers(EntityMyrmexBase myrmex) {
        super(myrmex, PlayerEntity.class, 10, true, true, new Predicate<PlayerEntity>() {

            @Override
            public boolean test(PlayerEntity entity) {
                return entity != null && (myrmex.getHive() == null
                    || myrmex.getHive().isPlayerReputationLowEnoughToFight(entity.getUniqueID()));
            }
        });
        this.myrmex = myrmex;
    }

    @Override
    public boolean shouldExecute() {
        return myrmex.shouldHaveNormalAI() && super.shouldExecute();
    }
}
