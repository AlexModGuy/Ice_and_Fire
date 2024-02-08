package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.datagen.IafStructurePieces;
import com.github.alexthe666.iceandfire.datagen.tags.IafBiomeTags;
import com.github.alexthe666.iceandfire.world.IafStructureTypes;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Map;
import java.util.Optional;

public class GraveyardStructure extends IafStructure {

    public static final Codec<GraveyardStructure> CODEC = RecordCodecBuilder.<GraveyardStructure>mapCodec(instance ->
            instance.group(GraveyardStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, GraveyardStructure::new)).codec();

    public GraveyardStructure(StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(config, startPool, startJigsawName, size, startHeight, projectStartToHeightmap, maxDistanceFromCenter);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(final GenerationContext context) {
        if (!IafConfig.generateGraveyards) {
            return Optional.empty();
        }

        return getLandPlacement(context);
    }

    @Override
    public StructureType<?> type() {
        return IafStructureTypes.GRAVEYARD.get();
    }

    public static GraveyardStructure buildStructureConfig(RegistryOps<JsonElement> registryOps) {
        Holder<StructureTemplatePool> startPool = registryOps.registry(Registry.TEMPLATE_POOL_REGISTRY).get().getOrCreateHolderOrThrow(IafStructurePieces.GRAVEYARD_START);

        return new GraveyardStructure(
                new Structure.StructureSettings(
                        registryOps.registry(Registry.BIOME_REGISTRY).get().getOrCreateTag(IafBiomeTags.HAS_GRAVEYARD),
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
