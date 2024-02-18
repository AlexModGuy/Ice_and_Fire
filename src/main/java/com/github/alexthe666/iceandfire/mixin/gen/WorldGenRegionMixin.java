package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/** Avoid log spam for dragon caves (some blocks can exceed the writable worldgen area due to their size) */
@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @Inject(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void skipLog(final BlockPos position, final CallbackInfoReturnable<Boolean> callback) {
        if (this.currentlyGenerating != null && this.currentlyGenerating.get().contains(IceAndFire.MODID)) {
            callback.setReturnValue(false);
        }
    }

    @Shadow @Nullable private Supplier<String> currentlyGenerating;
}
