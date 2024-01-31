package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;
import java.util.function.Supplier;

public class DragonSteelTier {

    public static final TagKey<Block> DRAGONSTEEL_TIER_TAG = BlockTags.create(new ResourceLocation("iceandfire:needs_dragonsteel"));
    public static final Tier DRAGONSTEEL_TIER_FIRE = createTierWithRepairItem(() -> Ingredient.of(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get()), "dragonsteel_tier_fire");
    public static final Tier DRAGONSTEEL_TIER_ICE = createTierWithRepairItem(() -> Ingredient.of(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get()), "dragonsteel_tier_ice");
    public static final Tier DRAGONSTEEL_TIER_LIGHTNING = createTierWithRepairItem(() -> Ingredient.of(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get()), "dragonsteel_tier_lightning");
    //FIXME: Probably shouldn't be called dragonsteel
    public static final Tier DRAGONSTEEL_TIER_DREAD_QUEEN = createTierWithRepairItem(() -> Ingredient.of(), "dragonsteel_tier_dread_queen");

    private static Tier createTierWithRepairItem(Supplier<Ingredient> ingredient, String name) {
        return TierSortingRegistry.registerTier(
            new ForgeTier(4, 8000, 10, 21, 10, DRAGONSTEEL_TIER_TAG, ingredient),
            new ResourceLocation(IceAndFire.MODID, name),
            List.of(Tiers.NETHERITE), List.of());
    }


}
