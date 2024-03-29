package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static com.github.alexthe666.iceandfire.world.structure.Pool.dread_pool;
import static com.github.alexthe666.iceandfire.world.structure.Pool.replaceContext;

public class DreadMausoleumStructure extends StructureFeature<JigsawConfiguration> {

    public DreadMausoleumStructure() {
        super(JigsawConfiguration.CODEC, DreadMausoleumStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    public static JigsawConfiguration config(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        return new JigsawConfiguration(
            context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOrCreateHolder(dread_pool),
            5
        );
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG) || !IafConfig.generateMausoleums)
            return Optional.empty();

        context = replaceContext(context, config(context));

        ChunkGenerator chunkGenerator = context.chunkGenerator();
        ChunkPos pos = context.chunkPos();
        LevelHeightAccessor height = context.heightAccessor();
        Rotation rotation = Rotation.getRandom(ThreadLocalRandom.current());
        int xOffset = 5;
        int yOffset = 5;
        if (rotation == Rotation.CLOCKWISE_90) {
            xOffset = -5;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            xOffset = -5;
            yOffset = -5;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            yOffset = -5;
        }

        int x = pos.getMiddleBlockX();
        int z = pos.getMiddleBlockZ();
        int y1 = chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, height);
        int y2 = chunkGenerator.getFirstOccupiedHeight(x, z + yOffset, Heightmap.Types.WORLD_SURFACE_WG, height);
        int y3 = chunkGenerator.getFirstOccupiedHeight(x + xOffset, z, Heightmap.Types.WORLD_SURFACE_WG, height);
        int y4 = chunkGenerator.getFirstOccupiedHeight(x + xOffset, z + yOffset, Heightmap.Types.WORLD_SURFACE_WG, height);
        int yMin = Math.min(Math.min(y1, y2), Math.min(y3, y4));
        BlockPos blockpos = pos.getMiddleBlockPosition(yMin + 1);

        // All a structure has to do is call this method to turn it into a jigsaw based structure!
        // No manual pieces class needed.

        return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, false);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public boolean canGenerate(@NotNull RegistryAccess p_197172_, @NotNull ChunkGenerator p_197173_, @NotNull BiomeSource p_197174_, @NotNull StructureManager p_197175_, long p_197176_, @NotNull ChunkPos p_197177_, @NotNull JigsawConfiguration p_197178_, @NotNull LevelHeightAccessor p_197179_, @NotNull Predicate<Holder<Biome>> p_197180_) {
        return super.canGenerate(p_197172_, p_197173_, p_197174_, p_197175_, p_197176_, p_197177_, p_197178_, p_197179_, p_197180_);
    }
}
