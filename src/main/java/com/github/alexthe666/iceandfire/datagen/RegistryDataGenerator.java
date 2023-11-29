package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

// TODO :: 1.19.2
//public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
//    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
//            .add(Registries.CONFIGURED_FEATURE, IafConfiguredFeatures::bootstrap)
//            .add(Registries.PLACED_FEATURE, IafPlacedFeatures::bootstrap)
//            .add(Registries.STRUCTURE, IafStructures::bootstrap)
//            .add(Registries.STRUCTURE_SET, IafStructureSets::bootstrap)
//            .add(Registries.PROCESSOR_LIST, IafProcessorLists::bootstrap)
//            .add(Registries.TEMPLATE_POOL, IafStructurePieces::bootstrap)
//            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, IafBiomeModifierSerializers::bootstrap);
//
//    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
//        super(output, provider, BUILDER, Set.of("minecraft", IceAndFire.MODID));
//    }
//}

