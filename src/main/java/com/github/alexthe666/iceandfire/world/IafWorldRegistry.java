package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.BiomeDreadLands;
import com.github.alexthe666.iceandfire.world.BiomeGlacier;
import com.github.alexthe666.iceandfire.world.dimension.WorldProviderDreadLands;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;

public class IafWorldRegistry {

    public static DimensionType DREADLANDS_DIM;
    public static Biome GLACIER_BIOME = new BiomeGlacier();
    public static Biome DREADLANDS_BIOME = new BiomeDreadLands();

    public static void init() {
       // DREADLANDS_DIM = DimensionType.register("Dreadlands", "_dreadlands", IceAndFire.CONFIG.dreadlandsDimensionId, WorldProviderDreadLands.class, false);
       // DimensionManager.registerDimension(IceAndFire.CONFIG.dreadlandsDimensionId, DREADLANDS_DIM);
    }
}
