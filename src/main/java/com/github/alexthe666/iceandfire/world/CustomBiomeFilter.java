package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.datagen.IafBiomeTagGenerator;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
    Some worldgen mods / datapacks split biomes between cave and surface<br>
    By default dragon caves (or any underground generation) would only check against the cave biome, not the surface biome)<br>
    Since the passed y position will be the lowest point of the world (e.g. -64)
*/
public class CustomBiomeFilter extends PlacementFilter {
    private static final CustomBiomeFilter INSTANCE = new CustomBiomeFilter();
    public static Codec<CustomBiomeFilter> CODEC = Codec.unit(() -> INSTANCE);

    private CustomBiomeFilter() { /* Nothing to do */ }

    public static CustomBiomeFilter biome() {
        return INSTANCE;
    }

    protected boolean shouldPlace(final PlacementContext context, @NotNull final RandomSource random, @NotNull final BlockPos position) {
        PlacedFeature placedfeature = context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
        AtomicReference<TagKey<Biome>> blacklistReference = new AtomicReference<>();

        placedfeature.feature().unwrapKey().ifPresent(key -> {
            switch (key.location().getPath()) {
                case "fire_dragon_cave" -> blacklistReference.set(IafBiomeTagGenerator.BLACKLIST_FIRE_DRAGON_CAVE);
                case "ice_dragon_cave" -> blacklistReference.set(IafBiomeTagGenerator.BLACKLIST_ICE_DRAGON_CAVE);
                case "lightning_dragon_cave" -> blacklistReference.set(IafBiomeTagGenerator.BLACKLIST_LIGHTNING_DRAGON_CAVE);
            };
        });

        TagKey<Biome> blacklist = blacklistReference.get();
        Holder<Biome> biome = context.getLevel().getBiome(position);

        if (blacklist != null && biome.is(blacklist)) {
            // This is the underground biome (where the cave feature will actually be placed) - meaning if it's in the blacklist there is no need to do further checks
            return false;
        }

        boolean hasFeature = context.generator().getBiomeGenerationSettings(biome).hasFeature(placedfeature);

        if (!hasFeature) {
            biome = context.getLevel().getBiome(context.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position));
            hasFeature = context.generator().getBiomeGenerationSettings(biome).hasFeature(placedfeature);

            if (hasFeature && /* No need to check if the feature is not going to be placed anyway */ blacklist != null && biome.is(blacklist)) {
                return false;
            }
        }

        return hasFeature;
    }

    public @NotNull PlacementModifierType<?> type() {
        return IafPlacementFilterRegistry.CUSTOM_BIOME_FILTER.get();
    }
}
