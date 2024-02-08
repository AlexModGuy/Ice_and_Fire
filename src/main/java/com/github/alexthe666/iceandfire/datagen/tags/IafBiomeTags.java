package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IafBiomeTags extends BiomeTagsProvider {
    private static final String prefix = "has_structure/";

    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(IceAndFire.MODID, prefix + "graveyard"));
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(IceAndFire.MODID, prefix + "gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(IceAndFire.MODID, prefix + "mausoleum"));

    public IafBiomeTags(final DataGenerator generator, @Nullable final ExistingFileHelper helper) {
        super(generator, IceAndFire.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(HAS_GRAVEYARD).addTag(BiomeTags.IS_OVERWORLD);

        tag(HAS_GORGON_TEMPLE)
                .addTag(BiomeTags.IS_BEACH)
                .addOptional(new ResourceLocation("byg", "dacite_shore"))
                .addOptional(new ResourceLocation("byg", "basalt_barrera"));

        tag(HAS_MAUSOLEUM)
                .addTag(Tags.Biomes.IS_SNOWY)
                .addOptional(new ResourceLocation("terralith", "wintry_forest"))
                .addOptional(new ResourceLocation("terralith", "wintry_lowlands"));
    }

    public static boolean isValidLandBiome(final Holder<Biome> biome) {
        return !biome.is(Tags.Biomes.IS_WATER);
    }

    @Override
    public @NotNull String getName() {
        return "Ice and Fire Biome Tags";
    }
}
