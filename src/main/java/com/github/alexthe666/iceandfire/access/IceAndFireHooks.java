package com.github.alexthe666.iceandfire.access;

import net.minecraft.entity.player.EntityPlayer;

public class IceAndFireHooks {
    /**
     * Called when a player is sneaking.
     */
    public static void dismount(EntityPlayer player) {
        if (player.getRidingEntity() != null) {
            if (player.getRidingEntity() instanceof EntityDragonBase) {
                player.setSneaking(false);
            } else {
                player.dismountRidingEntity();
            }
        }
    }
}
