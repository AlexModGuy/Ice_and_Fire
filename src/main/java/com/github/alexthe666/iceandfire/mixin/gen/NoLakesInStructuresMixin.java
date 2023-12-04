package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.datagen.IafStructures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
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
        if(!(context.level() instanceof WorldGenRegion)) {
            return;
        }
        Registry<Structure> configuredStructureFeatureRegistry = context.level().registryAccess().registryOrThrow(Registries.STRUCTURE);
        StructureManager structureManager = (context.level()).getLevel().structureManager();
        var availableStructures  = List.of(configuredStructureFeatureRegistry.getOptional(IafStructures.MAUSOLEUM),configuredStructureFeatureRegistry.getOptional(IafStructures.GRAVEYARD),configuredStructureFeatureRegistry.getOptional(IafStructures.GORGON_TEMPLE));
        for (var structure : availableStructures) {
            if (structure.isPresent() && structureManager.getStructureAt(context.origin(), structure.get()).isValid()) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}