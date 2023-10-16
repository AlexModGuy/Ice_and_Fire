package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.VillageHouseProcessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

public class IafProcessorLists {
    public static final ResourceKey<StructureProcessorList> GRAVEYARD_PROCESSORS = createKey("graveyard_processors");
    public static final ResourceKey<StructureProcessorList> MAUSOLEUM_PROCESSORS = createKey("mausoleum_processors");
    public static final ResourceKey<StructureProcessorList> GORGON_TEMPLE_PROCESSORS = createKey("gorgon_temple_processors");
    public static final ResourceKey<StructureProcessorList> HOUSE_PROCESSOR = createKey("village_house_processor");

    private static ResourceKey<StructureProcessorList> createKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation(IceAndFire.MODID, name));
    }

    private static void register(BootstapContext<StructureProcessorList> pContext, ResourceKey<StructureProcessorList> pKey, List<StructureProcessor> pProcessors) {
        pContext.register(pKey, new StructureProcessorList(pProcessors));
    }

    public static void bootstrap(BootstapContext<StructureProcessorList> pContext) {
        register(pContext, GRAVEYARD_PROCESSORS, ImmutableList.of(GraveyardProcessor.INSTANCE));
        register(pContext, MAUSOLEUM_PROCESSORS, ImmutableList.of(DreadRuinProcessor.INSTANCE));
        register(pContext, GORGON_TEMPLE_PROCESSORS, ImmutableList.of(GorgonTempleProcessor.INSTANCE));

        RuleProcessor mossify = new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState())));
        register(pContext, HOUSE_PROCESSOR, ImmutableList.of(mossify, VillageHouseProcessor.INSTANCE));
    }
}
