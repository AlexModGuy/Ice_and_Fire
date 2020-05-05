package com.github.alexthe666.iceandfire.compat.jei;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeCatagory;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeCatagory;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeRecipeHandler;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeRecipeWrapper;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
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
        registry.addRecipes(IafRecipeRegistry.FIRE_FORGE_RECIPES, FIRE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new FireDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new FireDragonForgeFactory(), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.dragonforge_fire_core_disabled), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE), FIRE_DRAGON_FORGE_ID);

        registry.addRecipes(IafRecipeRegistry.ICE_FORGE_RECIPES, ICE_DRAGON_FORGE_ID);
        registry.addRecipeHandlers(new IceDragonForgeRecipeHandler());
        registry.handleRecipes(DragonForgeRecipe.class, new IceDragonForgeFactory(), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE), ICE_DRAGON_FORGE_ID);
        addDescription(registry, new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD));
        addDescription(registry, new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_RED));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GRAY));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GREEN));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLUE));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_WHITE));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SILVER));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL, 1, 1));
        addDescription(registry, new ItemStack(IafItemRegistry.FIRE_STEW));
        addDescription(registry, new ItemStack(IafItemRegistry.FROST_STEW));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item));
        }
        for (ItemStack stack : IafRecipeRegistry.BANNER_ITEMS) {
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
