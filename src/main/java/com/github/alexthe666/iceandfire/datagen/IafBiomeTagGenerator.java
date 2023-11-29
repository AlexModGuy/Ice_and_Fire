package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;


public class IafBiomeTagGenerator extends BiomeTagsProvider {
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/mausoleum"));
    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/graveyard"));


    public IafBiomeTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(HAS_GRAVEYARD).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_MAUSOLEUM).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_GORGON_TEMPLE).addTag(BiomeTags.IS_OVERWORLD);
    }

    @Override
    public String getName() {
        return "Ice and Fire Biome Tags";
    }
}
