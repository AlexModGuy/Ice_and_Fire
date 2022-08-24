package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class MyrmexAIAttackPlayers extends NearestAttackableTargetGoal {

    private final EntityMyrmexBase myrmex;

    @SuppressWarnings("unchecked")
    public MyrmexAIAttackPlayers(EntityMyrmexBase myrmex) {
        super(myrmex, PlayerEntity.class, 10, true, true, new Predicate<PlayerEntity>() {

            @Override
            public boolean test(PlayerEntity entity) {
                return entity != null && (myrmex.getHive() == null
                    || myrmex.getHive().isPlayerReputationLowEnoughToFight(entity.getUUID()));
            }
        });
        this.myrmex = myrmex;
    }

    @Override
    public boolean canUse() {
        return myrmex.shouldHaveNormalAI() && super.canUse();
    }
}
