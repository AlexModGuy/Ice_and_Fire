package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafBiomeTagGenerator;
import com.github.alexthe666.iceandfire.datagen.IafStructurePieces;
import com.github.alexthe666.iceandfire.world.IafStructureTypes;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Map;
import java.util.Optional;

public class MausoleumStructure extends IafStructure {

    public static final Codec<MausoleumStructure> CODEC = RecordCodecBuilder.<MausoleumStructure>mapCodec(instance ->
            instance.group(MausoleumStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, MausoleumStructure::new)).codec();

    public MausoleumStructure(StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(config, startPool, startJigsawName, size, startHeight, projectStartToHeightmap, maxDistanceFromCenter);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        if (!IafConfig.generateMausoleums)
            return Optional.empty();

        ChunkPos pos = pContext.chunkPos();
        BlockPos blockpos = pos.getMiddleBlockPosition(1);

        if (!isBiomeValid(pContext, BiomeConfig.mausoleumBiomes, blockpos))
            return Optional.empty();

        Optional<Structure.GenerationStub> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        pContext, // Used for JigsawPlacement to get all the proper behaviors done.
                        this.startPool, // The starting pool to use to create the structure layout from
                        this.startJigsawName, // Can be used to only spawn from one Jigsaw block. But we don't need to worry about this.
                        this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
                        blockpos, // Where to spawn the structure.
                        false, // "useExpansionHack" This is for legacy villages to generate properly. You should keep this false always.
                        this.projectStartToHeightmap, // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
                        // Here, blockpos's y value is 60 which means the structure spawn 60 blocks above terrain height.
                        // Set this to false for structure to be place only at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        this.maxDistanceFromCenter);

        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return IafStructureTypes.MAUSOLEUM.get();
    }

    public static MausoleumStructure buildStructureConfig(RegistryOps<JsonElement> registryOps) {
        Holder<StructureTemplatePool> startPool = registryOps.registry(Registry.TEMPLATE_POOL_REGISTRY).get().getOrCreateHolderOrThrow(IafStructurePieces.MAUSOLEUM_START);

        return new MausoleumStructure(
                new Structure.StructureSettings(
                        registryOps.registry(Registry.BIOME_REGISTRY).get().getOrCreateTag(IafBiomeTagGenerator.HAS_MAUSOLEUM),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                startPool,
                Optional.empty(),
                1,
                ConstantHeight.ZERO,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                16
        );
    }
}
