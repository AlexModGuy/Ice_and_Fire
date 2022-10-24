package com.github.alexthe666.iceandfire.world.biome;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class IafBiomeTagsProvider extends BiomeTagsProvider {
    IafBiomeTagsProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
        tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
        tag(IafBiomeTags.HAS_GORGON_TEMPLE).add(OverworldBiomes.jungle());
    }
}
