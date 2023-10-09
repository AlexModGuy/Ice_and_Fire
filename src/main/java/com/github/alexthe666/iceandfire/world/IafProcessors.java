package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.VillageHouseProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IafProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, IceAndFire.MODID);

    public static final RegistryObject<StructureProcessorType<DreadRuinProcessor>> DREADRUINPROCESSOR = registerProcessor("dread_mausoleum_processor", () -> () -> DreadRuinProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<GorgonTempleProcessor>> GORGONTEMPLEPROCESSOR = registerProcessor("gorgon_temple_processor", () -> () -> GorgonTempleProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<GraveyardProcessor>> GRAVEYARDPROCESSOR = registerProcessor("graveyard_processor", () -> () -> GraveyardProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<VillageHouseProcessor>> VILLAGEHOUSEPROCESSOR = registerProcessor("village_house_processor", () -> () -> VillageHouseProcessor.CODEC);

    private static <P extends StructureProcessor> RegistryObject<StructureProcessorType<P>> registerProcessor(String name, Supplier<StructureProcessorType<P>> processor) {
        return PROCESSORS.register(name, processor);
    }
}
