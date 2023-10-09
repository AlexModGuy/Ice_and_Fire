package com.github.alexthe666.iceandfire.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class IafStructurePieces {
    public static final ResourceKey<StructureTemplatePool> GRAVEYARD_START = createKey("graveyard/start_pool");
    public static final ResourceKey<StructureTemplatePool> MAUSOLEUM_START = createKey("mausoleum/start_pool");
    public static final ResourceKey<StructureTemplatePool> GORGON_TEMPLE_START = createKey("gorgon_temple/start_pool");
    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation("iceandfire", name));
    }
    public static void registerGraveyard(BootstapContext<StructureTemplatePool> pContext) {
        HolderGetter<StructureProcessorList> processorListHolderGetter = pContext.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.GRAVEYARD_PROCESSORS);
        HolderGetter<StructureTemplatePool> templatePoolHolderGetter = pContext.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> fallback = templatePoolHolderGetter.getOrThrow(Pools.EMPTY);
        pContext.register(GRAVEYARD_START, new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:graveyard/graveyard_top", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));
        // We don't need direct access to this so register it here
        pContext.register(createKey("graveyard/bottom_pool"), new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:graveyard/graveyard_bottom", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));
    }

    public static void registerMausoleum(BootstapContext<StructureTemplatePool> pContext) {
        HolderGetter<StructureProcessorList> processorListHolderGetter = pContext.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.MAUSOLEUM_PROCESSORS);
        HolderGetter<StructureTemplatePool> templatePoolHolderGetter = pContext.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> fallback = templatePoolHolderGetter.getOrThrow(Pools.EMPTY);
        pContext.register(MAUSOLEUM_START, new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:mausoleum/building", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));
    }

    public static void registerGorgonTemple(BootstapContext<StructureTemplatePool> pContext) {
        HolderGetter<StructureProcessorList> processorListHolderGetter = pContext.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.GORGON_TEMPLE_PROCESSORS);
        HolderGetter<StructureTemplatePool> templatePoolHolderGetter = pContext.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> fallback = templatePoolHolderGetter.getOrThrow(Pools.EMPTY);
        pContext.register(GORGON_TEMPLE_START, new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:gorgon_temple/building", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));
        // We don't need direct access to this so register it here
        pContext.register(createKey("gorgon_temple/bottom_pool"), new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:gorgon_temple/basement", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));
        pContext.register(createKey("gorgon_temple/gorgon_pool"), new StructureTemplatePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.single("iceandfire:gorgon_temple/gorgon", graveyardProcessor), 1)), StructureTemplatePool.Projection.RIGID));

    }
    public static void bootstrap(BootstapContext<StructureTemplatePool> pContext) {
        registerGraveyard(pContext);
        registerMausoleum(pContext);
        registerGorgonTemple(pContext);
    }
}
