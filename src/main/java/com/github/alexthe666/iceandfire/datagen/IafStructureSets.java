package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class IafStructureSets {

    public static final ResourceKey<StructureSet> GRAVEYARD = registerKey("graveyard");
    public static final ResourceKey<StructureSet> MAUSOLEUM = registerKey("mausoleum");
    public static final ResourceKey<StructureSet> GORGON_TEMPLE = registerKey("gorgon_temple");


    private static ResourceKey<StructureSet> registerKey(String name) {
        return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }

//    public static void bootstrap(BootstapContext<StructureSet> context) {
//        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
//        context.register(GRAVEYARD, new StructureSet(structures.getOrThrow(IafStructures.GRAVEYARD), new RandomSpreadStructurePlacement(28, 8, RandomSpreadType.LINEAR, 44712661)));
//        context.register(MAUSOLEUM, new StructureSet(structures.getOrThrow(IafStructures.MAUSOLEUM), new RandomSpreadStructurePlacement(32, 12, RandomSpreadType.LINEAR, 14200531)));
//        context.register(GORGON_TEMPLE, new StructureSet(structures.getOrThrow(IafStructures.GORGON_TEMPLE), new RandomSpreadStructurePlacement(32, 12, RandomSpreadType.LINEAR, 76489509)));
//    }
}
