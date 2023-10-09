package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class IafStructures {

    public static final ResourceKey<Structure> GRAVEYARD = registerKey("graveyard");
    public static final ResourceKey<Structure> MAUSOLEUM = registerKey("mausoleum");
    public static final ResourceKey<Structure> GORGON_TEMPLE = registerKey("gorgon_temple");

    public static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(IceAndFire.MODID, name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        context.register(GRAVEYARD, GraveyardStructure.buildStructureConfig(context));
        context.register(MAUSOLEUM, MausoleumStructure.buildStructureConfig(context));
        context.register(GORGON_TEMPLE, GorgonTempleStructure.buildStructureConfig(context));
    }
}
