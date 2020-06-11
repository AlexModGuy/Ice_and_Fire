package com.github.alexthe666.iceandfire.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class IafWorldRegistry {

    public static Biome GLACIER_BIOME = new BiomeGlacier();
    public static SurfaceBuilderGlacier GLACIER_SURFACE_BUILDER = new SurfaceBuilderGlacier(SurfaceBuilderConfig::deserialize);

    static {
        GLACIER_SURFACE_BUILDER.setRegistryName("iceandfire:glacier_surface");
    }
}
