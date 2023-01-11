package com.github.alexthe666.iceandfire.world.gen.mixin;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// Based on code from TelepathicGrunts RepurposedStructures
@Mixin(LakeFeature.class)
public class NoLakesInStructuresMixin {

    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void iaf_noLakesInMausoleum(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (!(context.level() instanceof WorldGenRegion))
            return;

        for (var structure : List.of(IafWorldRegistry.MAUSOLEUM_CF, IafWorldRegistry.GORGON_TEMPLE_CF, IafWorldRegistry.GRAVEYARD_CF)) {
            var structureStart = context.level().getChunk(context.origin()).getStartForFeature(structure.value());
            if (structureStart != null && structureStart.isValid())
                cir.setReturnValue(false);
        }
    }
}