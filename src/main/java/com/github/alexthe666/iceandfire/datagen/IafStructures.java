package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumStructure;
import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Map;

public class IafStructures {
    public static final ResourceKey<Structure> GRAVEYARD = registerKey("graveyard");
    public static final ResourceKey<Structure> MAUSOLEUM = registerKey("mausoleum");
    public static final ResourceKey<Structure> GORGON_TEMPLE = registerKey("gorgon_temple");

    public static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }

    public static Map<ResourceLocation, Structure> gather(RegistryOps<JsonElement> registryOps) {
        return Map.of(
                GRAVEYARD.location(), GraveyardStructure.buildStructureConfig(registryOps),
                MAUSOLEUM.location(), MausoleumStructure.buildStructureConfig(registryOps),
                GORGON_TEMPLE.location(), GorgonTempleStructure.buildStructureConfig(registryOps)
        );
    }
}
