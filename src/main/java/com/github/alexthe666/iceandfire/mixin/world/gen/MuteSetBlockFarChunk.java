package com.github.alexthe666.iceandfire.mixin.world.gen;

import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @deprecated to be replaced in 1.19
 */
@Deprecated
@Mixin(WorldGenRegion.class)
public abstract class MuteSetBlockFarChunk {
    @Redirect(
            method = "ensureCanWrite",
            at = @At(value = "INVOKE", target="Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V")
    )
    private void iaf_noSetBlockError(String pError) {

    }
}
