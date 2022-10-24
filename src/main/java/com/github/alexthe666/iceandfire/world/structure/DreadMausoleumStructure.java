package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class DreadMausoleumStructure extends StructureFeature<JigsawConfiguration> {

    public DreadMausoleumStructure() {
        super(JigsawConfiguration.CODEC, DreadMausoleumStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context)
    {
       if (IafConfig.generateMausoleums) {
           Rotation rotation = Rotation.getRandom(ThreadLocalRandom.current());
           int i = 5;
           int j = 5;
           if (rotation == Rotation.CLOCKWISE_90) {
               i = -5;
           } else if (rotation == Rotation.CLOCKWISE_180) {
               i = -5;
               j = -5;
           } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
               j = -5;
           }

           ChunkPos pos = context.chunkPos();
           ChunkGenerator generator = context.chunkGenerator();

           int k = pos.x + 7;
           int l = pos.z + 7;
           int i1 = generator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
           int j1 = generator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
           int k1 = generator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
           int l1 = generator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
           int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
           BlockPos blockpos = new BlockPos(pos.x * 16 + 8, i2 + 1, pos.z * 16 + 8);

           // All a structure has to do is call this method to turn it into a jigsaw based structure!
           // No manual pieces class needed.

           return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, true);
       }
       return Optional.empty();
    }
}
