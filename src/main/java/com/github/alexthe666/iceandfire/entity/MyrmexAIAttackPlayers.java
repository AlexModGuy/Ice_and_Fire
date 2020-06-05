package com.github.alexthe666.iceandfire.entity;

import com.google.common.base.Predicate;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class MyrmexAIAttackPlayers extends NearestAttackableTargetGoal {
    private EntityMyrmexBase myrmex;

    public MyrmexAIAttackPlayers(EntityMyrmexBase myrmex) {
        super(myrmex, PlayerEntity.class, 10, true, true, new Predicate<PlayerEntity>() {
            public boolean apply(@Nullable PlayerEntity entity) {
                return entity != null && (myrmex.getHive() == null || myrmex.getHive().isPlayerReputationTooLowToFight(entity.getUniqueID()));
            }
        });
        this.myrmex = myrmex;
    }

    public boolean shouldExecute() {
        return myrmex.shouldHaveNormalAI() && super.shouldExecute();
    }
}
