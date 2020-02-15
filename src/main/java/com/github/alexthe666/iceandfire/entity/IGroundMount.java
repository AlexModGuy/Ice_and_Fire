package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface IGroundMount {
    EntityPlayer getRidingPlayer();
    double getRideSpeedModifier();
}
