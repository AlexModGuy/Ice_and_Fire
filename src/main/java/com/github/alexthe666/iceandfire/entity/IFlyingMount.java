package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface IFlyingMount {

    EntityPlayer getRidingPlayer();
    double getFlightSpeedModifier();
    default boolean fliesLikeElytra() { return false; }
    boolean isFlying();
    default boolean up(){ return false; }
    default boolean down(){ return false; }
    default boolean isHovering(){ return false; }
}
