package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class MyrmexAIAttackPlayers extends NearestAttackableTargetGoal {

    private final EntityMyrmexBase myrmex;

    @SuppressWarnings("unchecked")
    public MyrmexAIAttackPlayers(EntityMyrmexBase myrmex) {
        super(myrmex, Player.class, 10, true, true, new Predicate<Player>() {

            @Override
            public boolean test(Player entity) {
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
