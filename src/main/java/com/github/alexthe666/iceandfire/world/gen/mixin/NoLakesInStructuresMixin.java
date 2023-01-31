package com.github.alexthe666.iceandfire.world.gen.mixin;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.LakesFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

// Based on code from TelepathicGrunts RepurposedStructures
@Mixin(LakesFeature.class)
public class NoLakesInStructuresMixin {

    @Inject(
            method = "generate(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/BlockStateFeatureConfig;)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/ISeedReader;func_241827_a(Lnet/minecraft/util/math/SectionPos;Lnet/minecraft/world/gen/feature/structure/Structure;)Ljava/util/stream/Stream;"),
            cancellable = true
    )
    private void iaf_noLakesInMausoleum(ISeedReader serverWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, BlockStateFeatureConfig singleStateFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.from(blockPos);
        Structure<NoFeatureConfig> structure = IafWorldRegistry.MAUSOLEUM_RO.get();
        if (serverWorldAccess.func_241827_a(sectionPos, structure).findAny().isPresent()) {
             cir.setReturnValue(false);
        }
    }
}