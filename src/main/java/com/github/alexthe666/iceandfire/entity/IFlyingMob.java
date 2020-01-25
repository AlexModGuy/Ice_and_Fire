package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface IFlyingMob {

    EntityPlayer getRidingPlayer();
    double getFlightSpeedModifier();
    default boolean fliesLikeElytra() { return false; }
    boolean isFlying();
}
