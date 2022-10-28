package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class IafBiomeTagsProvider extends BiomeTagsProvider {
    public IafBiomeTagsProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ForgeRegistries.BIOMES.getValues().forEach((biome -> {
            tag(IafWorldRegistry.HAS_GORGON_TEMPLE).add(biome);
            tag(IafWorldRegistry.HAS_MAUSOLEUM).add(biome);
            tag(IafWorldRegistry.HAS_GRAVEYARD).add(biome);
        }));
        //tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
        //tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
        //tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
    }
}
