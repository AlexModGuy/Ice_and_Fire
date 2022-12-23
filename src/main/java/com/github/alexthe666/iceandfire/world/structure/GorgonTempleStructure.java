package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
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

public class GorgonTempleStructure extends StructureFeature<JigsawConfiguration> {

    public GorgonTempleStructure() {
        super(JigsawConfiguration.CODEC, GorgonTempleStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    static class postPlacement implements PostPlacementProcessor {

        @Override
        public void afterPlace(WorldGenLevel p_192438_, StructureFeatureManager p_192439_, ChunkGenerator p_192440_, Random p_192441_, BoundingBox p_192442_, ChunkPos p_192443_, PiecesContainer p_192444_) {
        }
    }
   /*
    public int getSize() {
        return 4;
    }

    protected int getSeedModifier() {
        return 123456789;
    }

    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 8;// Math.max(IafConfig.spawnGorgonsChance, 2);
    }

    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 4; //Math.max(IafConfig.spawnGorgonsChance / 2, 1);
    }*/


    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context)
    {
        if (!IafConfig.spawnGorgons) {
            return Optional.empty();
        }
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
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        LevelHeightAccessor height = context.heightAccessor();

        int k = pos.x + 7;
        int l = pos.z + 7;
        int i1 = chunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, height);
        int j1 = chunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
        int k1 = chunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, height);
        int l1 = chunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
        int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
        BlockPos blockpos = new BlockPos(pos.x * 16 + 8, i2 + 2, pos.z * 16 + 8);
        blockpos = pos.getMiddleBlockPosition(i2 + 2);


        context = Pool.replaceContext(context, new JigsawConfiguration(
                        context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOrCreateHolder(Pool.gorgon_pool),
                        3 // Depth of jigsaw branches. Gorgon temple has a depth of 3. (start top -> bottom -> gorgon)
                )
        );

        // All a structure has to do is call this method to turn it into a jigsaw based structure!
        // No manual pieces class needed.
        return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, false);

        //TODO: Is this needed?
        //TODO: morguldir: I hope not!
        //this.createBoundingBox();
    }
}
