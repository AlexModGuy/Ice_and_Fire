package com.github.alexthe666.iceandfire.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GraveyardStructure extends StructureFeature<JigsawConfiguration> {

    public GraveyardStructure() {
        super(JigsawConfiguration.CODEC, GraveyardStructure::createPiecesGenerator, new placementProcessor());
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static class placementProcessor implements PostPlacementProcessor {
        @Override
        public void afterPlace(WorldGenLevel level, StructureFeatureManager structureFeatureManager, ChunkGenerator generator, Random random, BoundingBox box, ChunkPos pos, PiecesContainer container) {
            container.pieces().forEach(structurePiece -> {
                // Raises the bottom part of the bounding box up by 3.
                // This is done so that the land terraforming code places land at the right height for the graveyard.
                structurePiece.getBoundingBox().minY += 3;
            });
            container.calculateBoundingBox();
        }
    }
    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context)
    {
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
        LevelHeightAccessor height = context.heightAccessor();

        context = Pool.replaceContext(context, new JigsawConfiguration(
                context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOrCreateHolder(Pool.graveyard_pool),
                5 // Depth of jigsaw branches. Can be set to any number greater than 1 but won't change anything as this is a single piece Jigsaw Structure.
                )
        );

        int k = pos.getBlockX(7);
        int l = pos.getBlockZ(7);
        int i1 = generator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, height);
        int j1 = generator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
        int k1 = generator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, height);
        int l1 = generator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
        int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(i2 - 2);
        // All a structure has to do is call this method to turn it into a jigsaw based structure!
        // No manual pieces class needed.
        return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, false);
    }
}
