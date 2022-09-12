package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.world.entity.player.Player;

public interface IFlyingMount {

    Player getRidingPlayer();

    double getFlightSpeedModifier();

    default boolean fliesLikeElytra() {
        return false;
    }

    boolean isFlying();

    default boolean isGoingUp() {
        return false;
    }

    default boolean isGoingDown() {
        return false;
    }

    default boolean isHovering() {
        return false;
    }

    default double getYSpeedMod(){ return 10; }
}
