package com.github.alexthe666.iceandfire.world.gen.mixin;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Based on code from TelepathicGrunts RepurposedStructures
@Mixin(LakeFeature.class)
public class NoLakesInStructuresMixin {

    // TODO: morguldir: Revisit this later
    //@Inject(
    //    method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
    //    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;startsForFeature(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Ljava/util/stream/Stream;"),
    //    cancellable = true
    //)
    //private void iaf_noLakesInMausoleum(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
    //    if (!(context.level() instanceof WorldGenRegion))
    //        return;

    //
    //    context.level().registryAccess().
    //    SectionPos sectionPos = SectionPos.of(context.origin());
    //    WorldGenLevel level = context.level();
    //    StructureFeature<JigsawConfiguration> structure = IafWorldRegistry.MAUSOLEUM.get();
    //    if (level.startsForFeature(sectionPos, structure).findAny().isPresent()) {
    //        cir.setReturnValue(false);
    //    }
    //}
}