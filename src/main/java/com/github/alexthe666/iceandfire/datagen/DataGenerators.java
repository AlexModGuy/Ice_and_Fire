package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.BannerPatternTagGenerator;
import com.github.alexthe666.iceandfire.datagen.tags.POITagGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent // TODO :: 1.19.2
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
//        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, provider);
//        generator.addProvider(event.includeServer(), datapackProvider);
        generator.addProvider(event.includeServer(), new BannerPatternTagGenerator(generator, helper));
        generator.addProvider(event.includeServer(), new POITagGenerator(generator, helper));
//        generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
//                Component.literal("Resources for Ice and Fire"),
//                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
//                Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
        generator.addProvider(event.includeServer(), new IafBiomeTagGenerator(generator, helper));
//        generator.addProvider(event.includeClient(), new AtlasGenerator(generator, helper));

    }
}
