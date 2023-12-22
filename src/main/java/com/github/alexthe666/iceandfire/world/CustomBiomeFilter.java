package com.github.alexthe666.iceandfire.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

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
        boolean hasFeature = context.generator().getBiomeGenerationSettings(context.getLevel().getBiome(position)).hasFeature(placedfeature);

        if (!hasFeature) {
            // TODO :: In theory this could cause a fire dragon cave to spawn in an Terralith ice cave if said cave spawns below a desert or sth.
            hasFeature = context.generator().getBiomeGenerationSettings(context.getLevel().getBiome(context.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, position))).hasFeature(placedfeature);
        }

        return hasFeature;
    }

    public @NotNull PlacementModifierType<?> type() {
        return IafPlacementFilterRegistry.CUSTOM_BIOME_FILTER.get();
    }
}
