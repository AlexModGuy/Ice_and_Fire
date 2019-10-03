package com.github.alexthe666.iceandfire.patcher;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.util.IceAndFireCoreUtils;
import net.ilexiconn.llibrary.server.asm.InsnPredicate;
import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketVehicleMove;

public class IceAndFireRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        this.patchClass(NetHandlerPlayServer.class)
                .patchMethod("processVehicleMove", CPacketVehicleMove.class, void.class)
                .apply(Patch.REPLACE_NODE, new InsnPredicate.Ldc().cst(0.0625D), method -> {
                    method.var(ALOAD, 0);
                    method.method(INVOKESTATIC, IceAndFireCoreUtils.class, "getMoveThreshold", NetHandlerPlayServer.class, double.class);
                })
                .apply(Patch.REPLACE_NODE, new InsnPredicate.Ldc().cst(100.0D), method -> {
                    method.var(ALOAD, 0);
                    method.method(INVOKESTATIC, IceAndFireCoreUtils.class, "getFastestEntityMotionSpeed", NetHandlerPlayServer.class, double.class);
                }).pop();
    }
}
