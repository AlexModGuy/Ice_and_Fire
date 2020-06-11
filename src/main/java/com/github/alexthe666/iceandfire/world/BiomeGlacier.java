package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class BiomeGlacier extends Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.SNOW_BLOCK.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState());

    public BiomeGlacier() {
        super((new Biome.Builder()).surfaceBuilder(IafWorldRegistry.GLACIER_SURFACE_BUILDER, SURFACE_BUILDER_CONFIG).precipitation(Biome.RainType.SNOW).category(Biome.Category.ICY).depth(0.425F).scale(0.35F).temperature(-1.0F).downfall(1.5F).waterColor(0X084668).waterFogColor(0X0A5B87).parent((String)null));
        this.setRegistryName(IceAndFire.MODID, "glacier");
    }
}
