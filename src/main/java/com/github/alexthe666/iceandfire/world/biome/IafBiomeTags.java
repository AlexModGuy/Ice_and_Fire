package com.github.alexthe666.iceandfire.world.biome;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IafBiomeTags {
    static DeferredRegister<Biome> deferredRegister = DeferredRegister.create(ForgeRegistries.BIOMES, IceAndFire.MODID);

    public static final TagKey<Biome> HAS_MAUSOLEUM = create("has_structure/mausoleum");
    public static final TagKey<Biome> HAS_GRAVEYARD = create("has_structure/graveyard");
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = create("has_structure/gorgon_temple");

    private static TagKey<Biome> create(String location) {
        return deferredRegister.createTagKey(new ResourceLocation(location));
    }
}
