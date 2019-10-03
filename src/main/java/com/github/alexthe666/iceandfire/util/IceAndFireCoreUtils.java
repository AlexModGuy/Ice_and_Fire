package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ISyncMount;
import net.minecraft.network.NetHandlerPlayServer;

public class IceAndFireCoreUtils {

    private static int index = 0;
    public static double getMoveThreshold(NetHandlerPlayServer e) {
        return IceAndFire.CONFIG.dragonMovedWronglyFix && e.player.getLowestRidingEntity() instanceof ISyncMount ? 1.5D : 0.0625D;
    }

    public static double getFastestEntityMotionSpeed(NetHandlerPlayServer e) {
        return IceAndFire.CONFIG.dragonMovedWronglyFix && e.player.getLowestRidingEntity() instanceof ISyncMount ? 1000.0D : 100.0D;
    }
}
