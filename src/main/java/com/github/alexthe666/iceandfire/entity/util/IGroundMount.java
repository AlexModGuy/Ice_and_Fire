package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.entity.player.PlayerEntity;

public interface IGroundMount {
    PlayerEntity getRidingPlayer();
    double getRideSpeedModifier();
}
