package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.IafFeatureBiomeModifier;
import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class IafBiomeModifierSerializers {
    public static ResourceKey<BiomeModifier> FEATURES = createKey("iaf_features");

    public static ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(IceAndFire.MODID, name));
    }

    public static Map<ResourceLocation, BiomeModifier> gather(RegistryOps<JsonElement> registryOps) {
        List<ResourceKey<PlacedFeature>> features = List.of(
                IafPlacedFeatures.PLACED_FIRE_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_ICE_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_FIRE_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_ICE_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_CYCLOPS_CAVE,
                IafPlacedFeatures.PLACED_PIXIE_VILLAGE,
                IafPlacedFeatures.PLACED_SIREN_ISLAND,
                IafPlacedFeatures.PLACED_HYDRA_CAVE,
                IafPlacedFeatures.PLACED_MYRMEX_HIVE_DESERT,
                IafPlacedFeatures.PLACED_MYRMEX_HIVE_JUNGLE,
                IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I,
                IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS,
                IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT,
                IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD,
                IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS,
                IafPlacedFeatures.PLACED_SILVER_ORE,
                IafPlacedFeatures.PLACED_SAPPHIRE_ORE,
                IafPlacedFeatures.PLACED_FIRE_LILY,
                IafPlacedFeatures.PLACED_FROST_LILY,
                IafPlacedFeatures.PLACED_LIGHTNING_LILY
        );

        List<Holder<PlacedFeature>> holders = new ArrayList<>();
        features.forEach(feature -> holders.add(registryOps.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(feature)));

        return Map.of(FEATURES.location(), new IafFeatureBiomeModifier(HolderSet.direct(holders)));
    }
}
