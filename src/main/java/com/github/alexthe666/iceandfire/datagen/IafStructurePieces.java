package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.Map;

public class IafStructurePieces {
    public static final ResourceKey<StructureTemplatePool> GRAVEYARD_START = createKey("graveyard/start_pool");
    public static final ResourceKey<StructureTemplatePool> MAUSOLEUM_START = createKey("mausoleum/start_pool");
    public static final ResourceKey<StructureTemplatePool> GORGON_TEMPLE_START = createKey("gorgon_temple/start_pool");

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, new ResourceLocation("iceandfire", name));
    }

    public static Map<ResourceLocation, StructureTemplatePool> gather(RegistryOps<JsonElement> registryOps) {
        Holder<StructureTemplatePool> fallback = registryOps.registry(Registry.TEMPLATE_POOL_REGISTRY).get().getOrCreateHolderOrThrow(Pools.EMPTY);

        Holder<StructureProcessorList> graveyardProcessors = registryOps.registry(Registry.PROCESSOR_LIST_REGISTRY).get().getOrCreateHolderOrThrow(IafProcessorLists.GRAVEYARD_PROCESSORS);
        StructureTemplatePool graveyardStartPool = new StructureTemplatePool(GRAVEYARD_START.location(), fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "graveyard/graveyard_top", graveyardProcessors), 1)), StructureTemplatePool.Projection.RIGID);
        ResourceLocation graveyardBottom = createKey("graveyard/bottom_pool").location();
        StructureTemplatePool graveyardBottomPool = new StructureTemplatePool(graveyardBottom, fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "graveyard/graveyard_bottom", graveyardProcessors), 1)), StructureTemplatePool.Projection.RIGID);

        Holder<StructureProcessorList> mausoleumProcessors = registryOps.registry(Registry.PROCESSOR_LIST_REGISTRY).get().getOrCreateHolderOrThrow(IafProcessorLists.MAUSOLEUM_PROCESSORS);
        StructureTemplatePool mausoleumStartPool = new StructureTemplatePool(MAUSOLEUM_START.location(), fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "mausoleum/building", mausoleumProcessors), 1)), StructureTemplatePool.Projection.RIGID);

        Holder<StructureProcessorList> gorgonTempleProcessors = registryOps.registry(Registry.PROCESSOR_LIST_REGISTRY).get().getOrCreateHolderOrThrow(IafProcessorLists.GORGON_TEMPLE_PROCESSORS);
        StructureTemplatePool gorgonTempleStartPool = new StructureTemplatePool(GORGON_TEMPLE_START.location(), fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "gorgon_temple/building", gorgonTempleProcessors), 1)), StructureTemplatePool.Projection.RIGID);
        ResourceLocation gorgonTempleBottom = createKey("gorgon_temple/bottom_pool").location();
        StructureTemplatePool gorgonTempleBottomPool = new StructureTemplatePool(gorgonTempleBottom, fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "gorgon_temple/basement", gorgonTempleProcessors), 1)), StructureTemplatePool.Projection.RIGID);
        ResourceLocation gorgonTempleGorgon = createKey("gorgon_temple/gorgon_pool").location();
        StructureTemplatePool gorgonTempleGorgonPool = new StructureTemplatePool(gorgonTempleGorgon, fallback.value().getName(), List.of(Pair.of(StructurePoolElement.single(IceAndFire.MODID + ":" + "gorgon_temple/gorgon", gorgonTempleProcessors), 1)), StructureTemplatePool.Projection.RIGID);

        return Map.of(
                GRAVEYARD_START.location(), graveyardStartPool,
                graveyardBottom, graveyardBottomPool,
                MAUSOLEUM_START.location(), mausoleumStartPool,
                GORGON_TEMPLE_START.location(), gorgonTempleStartPool,
                gorgonTempleBottom, gorgonTempleBottomPool,
                gorgonTempleGorgon, gorgonTempleGorgonPool
        );
    }
}
