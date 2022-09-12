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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class GorgonTempleStructure extends StructureFeature<NoneFeatureConfiguration> {

    public GorgonTempleStructure(Codec<NoneFeatureConfiguration> p_i51440_1_) {
        super(p_i51440_1_);
    }

    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public String getFeatureName() {
        return IceAndFire.MODID + ":gorgon_temple";
    }

    public StructureStartFactory getStartFactory() {
        return GorgonTempleStructure.Start::new;
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

    public static class Start extends StructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> structure, ChunkPos chunkPos, int refCount, long seed) {
            super(structure, chunkPos, refCount, seed);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistries, ChunkGenerator chunkGenerator, StructureManager templateManager, ChunkPos pos, Biome biome, NoneFeatureConfiguration config, LevelHeightAccessor height) {
            if (IafConfig.spawnGorgons) {
                Rotation rotation = Rotation.getRandom(this.random);
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

                int k = pos.x + 7;
                int l = pos.z + 7;
                int i1 = chunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, height);
                int j1 = chunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
                int k1 = chunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, height);
                int l1 = chunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, height);
                int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
                BlockPos blockpos = new BlockPos(pos.x * 16 + 8, i2 + 2, pos.z * 16 + 8);

                // All a structure has to do is call this method to turn it into a jigsaw based structure!
                // No manual pieces class needed.
                JigsawPlacement.addPieces(
                    dynamicRegistries,
                    new JigsawConfiguration(() -> dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(new ResourceLocation(IceAndFire.MODID, "gorgon_temple/top_pool")),
                        3), // Depth of jigsaw branches. Gorgon temple has a depth of 3. (start top -> bottom -> gorgon)
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    templateManager,
                    blockpos,
                    this,
                    this.random,
                    false,
                    false,
                    height);
                //TODO: Is this needed?
                this.createBoundingBox();
            }
        }
    }
}
