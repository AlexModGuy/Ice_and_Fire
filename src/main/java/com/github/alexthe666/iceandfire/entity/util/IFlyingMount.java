package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.entity.player.PlayerEntity;

public interface IFlyingMount {

    PlayerEntity getRidingPlayer();

    double getFlightSpeedModifier();

    default boolean fliesLikeElytra() {
        return false;
    }

    boolean isFlying();

    default boolean up() {
        return false;
    }

    default boolean down() {
        return false;
    }

    default boolean isHovering() {
        return false;
    }
}
