package com.github.alexthe666.iceandfire.compat.jei;

import com.github.alexthe666.iceandfire.block.IaFBlockRegistry;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeCatagory;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeCatagory;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.item.IaFItemRegistry;
import com.github.alexthe666.iceandfire.recipe.IaFRecipeRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class IceAndFireJEIPlugin implements IModPlugin {

    public static final String FIRE_DRAGON_FORGE_ID = "iceandfire.fire_dragon_forge";
    public static final String ICE_DRAGON_FORGE_ID = "iceandfire.ice_dragon_forge";

    private static void addDescription(IModRegistry registry, ItemStack stack) {
        registry.addIngredientInfo(stack, ItemStack.class, stack.getTranslationKey() + ".jei_desc");
    }

    @SuppressWarnings("deprecation")
    public void register(IModRegistry registry) {
        registry.addRecipes(IaFRecipeRegistry.FIRE_FORGE_RECIPES, FIRE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new FireDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new FireDragonForgeFactory(), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IaFBlockRegistry.dragonforge_fire_core_disabled), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IaFBlockRegistry.dragonforge_fire_core), FIRE_DRAGON_FORGE_ID);

        registry.addRecipes(IaFRecipeRegistry.ICE_FORGE_RECIPES, ICE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new IceDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new IceDragonForgeFactory(), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IaFBlockRegistry.dragonforge_ice_core_disabled), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IaFBlockRegistry.dragonforge_ice_core), ICE_DRAGON_FORGE_ID);
        addDescription(registry, new ItemStack(IaFItemRegistry.fire_dragon_blood));
        addDescription(registry, new ItemStack(IaFItemRegistry.ice_dragon_blood));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_red));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_bronze));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_gray));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_green));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_blue));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_white));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_sapphire));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragonegg_silver));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragon_skull));
        addDescription(registry, new ItemStack(IaFItemRegistry.dragon_skull, 1, 1));
        addDescription(registry, new ItemStack(IaFItemRegistry.fire_stew));
        addDescription(registry, new ItemStack(IaFItemRegistry.frost_stew));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
        for (ItemStack stack : IaFRecipeRegistry.BANNER_ITEMS) {
            registry.addIngredientInfo(stack, ItemStack.class, "item.iceandfire.custom_banner.jei_desc");
        }
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new FireDragonForgeCatagory());
        registry.addRecipeCategories(new IceDragonForgeCatagory());
    }

    public class FireDragonForgeFactory implements IRecipeWrapperFactory<DragonForgeRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(DragonForgeRecipe recipe) {
            return new FireDragonForgeRecipeWrapper(recipe);
        }
    }

    public class IceDragonForgeFactory implements IRecipeWrapperFactory<DragonForgeRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(DragonForgeRecipe recipe) {
            return new IceDragonForgeRecipeWrapper(recipe);
        }
    }
}
