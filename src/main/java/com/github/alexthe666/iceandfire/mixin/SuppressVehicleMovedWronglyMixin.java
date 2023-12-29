package com.github.alexthe666.iceandfire.mixin;

//TODO: The real solution would be to properly fix the move vehicle moved wrongly stuff, but this is a quick fix for now
//@Mixin(ServerGamePacketListenerImpl.class)
public class SuppressVehicleMovedWronglyMixin {

    /*@Shadow
    public ServerPlayer player;

    @WrapOperation(method = "handleMoveVehicle(Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)V", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", ordinal = 1, remap = false))
    private void iaf_lowerVehicleMovedWronglyLoggingLevel(Logger instance, String s, Object[] o, Operation<Void> original) {
        if (this.player.getRootVehicle() instanceof EntityHippocampus) {
            instance.debug(s, o);
        } else if (this.player.getRootVehicle() instanceof EntityAmphithere) {
            instance.debug(s, o);
        } else {
            instance.warn(s, o);
        }
    }*/

}
