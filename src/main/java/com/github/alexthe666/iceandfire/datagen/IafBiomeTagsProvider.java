package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Map;

public class IafBiomeTagsProvider extends BiomeTagsProvider {
    public IafBiomeTagsProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        Map<Pair<String, SpawnBiomeData>, TagKey<Biome>> biomeTags = Map.ofEntries(
                Map.entry(BiomeConfig.gorgonTempleBiomes, IafWorldRegistry.HAS_GORGON_TEMPLE),
                Map.entry(BiomeConfig.mausoleumBiomes, IafWorldRegistry.HAS_MAUSOLEUM),
                Map.entry(BiomeConfig.graveyardBiomes, IafWorldRegistry.HAS_GRAVEYARD)
        );
        ForgeRegistries.BIOMES.getValues().forEach((biome -> {
            Holder<Biome> biomeHolder = Holder.direct(biome);

            biomeTags.forEach((biomeData, biomeTagKey) -> {
                if (BiomeConfig.test(biomeData, biomeHolder))
                    tag(biomeTagKey).add(biome);
            });
        }));
    }
}
