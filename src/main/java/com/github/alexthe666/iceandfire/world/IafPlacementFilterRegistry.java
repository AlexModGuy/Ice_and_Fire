package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class IafPlacementFilterRegistry {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, IceAndFire.MODID);

    public static RegistryObject<PlacementModifierType<CustomBiomeFilter>> CUSTOM_BIOME_FILTER = PLACEMENT_MODIFIER_TYPES.register("biome_extended", () -> () -> CustomBiomeFilter.CODEC);
}
