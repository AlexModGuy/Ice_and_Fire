package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.VillageHouseProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class IafProcessors {
    public static StructureProcessorType<DreadRuinProcessor> DREADRUINPROCESSOR = () -> DreadRuinProcessor.CODEC;
    public static StructureProcessorType<GorgonTempleProcessor> GORGONTEMPLEPROCESSOR = () -> GorgonTempleProcessor.CODEC;
    public static StructureProcessorType<GraveyardProcessor> GRAVEYARDPROCESSOR = () -> GraveyardProcessor.CODEC;
    public static StructureProcessorType<VillageHouseProcessor> VILLAGEHOUSEPROCESSOR = () -> VillageHouseProcessor.CODEC;

    public static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(IceAndFire.MODID, "dread_mausoleum_processor"), DREADRUINPROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(IceAndFire.MODID, "gorgon_temple_processor"), GORGONTEMPLEPROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(IceAndFire.MODID, "graveyard_processor"), GRAVEYARDPROCESSOR);
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(IceAndFire.MODID, "village_house_processor"), VILLAGEHOUSEPROCESSOR);
    }
}
