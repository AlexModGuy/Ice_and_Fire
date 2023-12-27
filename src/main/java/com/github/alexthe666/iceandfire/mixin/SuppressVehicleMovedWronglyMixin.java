package com.github.alexthe666.iceandfire.mixin;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//TODO: The real solution would be to properly fix the move vehicle moved wrongly stuff, but this is a quick fix for now
@Mixin(ServerGamePacketListenerImpl.class)
public class SuppressVehicleMovedWronglyMixin {

    @Shadow public ServerPlayer player;
    @WrapOperation(method = "handleMoveVehicle(Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)V", at = @At( value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", ordinal = 1, remap = false))
    private void iaf_lowerVehicleMovedWronglyLoggingLevel(Logger instance, String s, Object[] o, Operation<Void> original) {
        if (this.player.getRootVehicle() instanceof EntityHippocampus) {
            instance.debug(s,o);
        } else {
            instance.warn(s,o);
        }
    }

}
