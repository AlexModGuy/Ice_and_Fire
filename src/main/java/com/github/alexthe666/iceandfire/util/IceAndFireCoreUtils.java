package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.entity.ISyncMount;
import net.minecraft.network.NetHandlerPlayServer;

public class IceAndFireCoreUtils {

    public static double getMoveThreshold(NetHandlerPlayServer e) {
        return e.player.getLowestRidingEntity() instanceof ISyncMount ? 2.0D : 0.0625D;
    }

    public static double getFastestEntityMotionSpeed(NetHandlerPlayServer e) {
        return e.player.getLowestRidingEntity() instanceof ISyncMount ? 1000.0D : 100.0D;
    }
}
