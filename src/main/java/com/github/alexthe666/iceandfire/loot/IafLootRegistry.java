package com.github.alexthe666.iceandfire.loot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class IafLootRegistry {

    public static LootItemFunctionType CUSTOMIZE_TO_DRAGON;
    public static LootItemFunctionType CUSTOMIZE_TO_SERPENT;

    private static LootItemFunctionType register(String p_237451_0_, Serializer<? extends LootItemFunction> p_237451_1_) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(p_237451_0_), new LootItemFunctionType(p_237451_1_));
    }

    public static void init() {
        CUSTOMIZE_TO_DRAGON = register("iceandfire:customize_to_dragon", new CustomizeToDragon.Serializer());
        CUSTOMIZE_TO_SERPENT = register("iceandfire:customize_to_sea_serpent", new CustomizeToSeaSerpent.Serializer());
    }

}
