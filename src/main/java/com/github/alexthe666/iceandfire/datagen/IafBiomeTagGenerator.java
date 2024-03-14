package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IafBiomeTagGenerator extends BiomeTagsProvider {
    public static final TagKey<Biome> HAS_GORGON_TEMPLE = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/gorgon_temple"));
    public static final TagKey<Biome> HAS_MAUSOLEUM = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/mausoleum"));
    public static final TagKey<Biome> HAS_GRAVEYARD = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "has_structure/graveyard"));

    public static final TagKey<Biome> BLACKLIST_FIRE_DRAGON_CAVE = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "blacklist/fire_dragon_cave"));
    public static final TagKey<Biome> BLACKLIST_ICE_DRAGON_CAVE = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "blacklist/fire_ice_cave"));
    public static final TagKey<Biome> BLACKLIST_LIGHTNING_DRAGON_CAVE = TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(IceAndFire.MODID, "blacklist/fire_lightning_cave"));


    public IafBiomeTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(HAS_GRAVEYARD).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_MAUSOLEUM).addTag(BiomeTags.IS_OVERWORLD);
        tag(HAS_GORGON_TEMPLE).addTag(BiomeTags.IS_OVERWORLD);

        tag(BLACKLIST_FIRE_DRAGON_CAVE).addOptionalTag(new ResourceLocation("alexscaves", "alexs_caves_biomes"));
        tag(BLACKLIST_ICE_DRAGON_CAVE).addOptionalTag(new ResourceLocation("alexscaves", "alexs_caves_biomes"));
        tag(BLACKLIST_LIGHTNING_DRAGON_CAVE).addOptionalTag(new ResourceLocation("alexscaves", "alexs_caves_biomes"));
    }

    @Override
    public String getName() {
        return "Ice and Fire Biome Tags";
    }
}
