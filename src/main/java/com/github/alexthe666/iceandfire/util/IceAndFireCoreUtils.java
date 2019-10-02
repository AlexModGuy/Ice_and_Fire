package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.entity.ISyncMount;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketVehicleMove;

public class IceAndFireCoreUtils {

    public static double getMoveThreshold(NetHandlerPlayServer e){
        return e.player.getLowestRidingEntity() instanceof ISyncMount ? 1.0D : 0.0625D;
    }

    public static double getFastestEntityMotionSpeed(NetHandlerPlayServer e){
        return e.player.getLowestRidingEntity() instanceof ISyncMount ? 1000.0D : 100.0D;
    }
}
