package com.github.alexthe666.iceandfire.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.iceandfire.recipes")
public class CraftTweakerCompat {
    private static void addDragonForgeRecipe(List<DragonForgeRecipe> recipeList, IItemStack input, IItemStack blood, IItemStack output) {
        recipeList.add(new DragonForgeRecipe(input.getInternal(), blood.getInternal(), output.getInternal()));
    }
    
    @ZenCodeType.Method
    public static void addFireDragonForgeRecipe(IItemStack input, IItemStack blood, IItemStack output) {
        addDragonForgeRecipe(IafRecipeRegistry.FIRE_FORGE_RECIPES, input, blood, output);
    }

    @ZenCodeType.Method
    public static void addIceDragonForgeRecipe(IItemStack input, IItemStack blood, IItemStack output) {
        addDragonForgeRecipe(IafRecipeRegistry.ICE_FORGE_RECIPES, input, blood, output);
    }

    @ZenCodeType.Method
    public static void addLightningDragonForgeRecipe(IItemStack input, IItemStack blood, IItemStack output) {
        addDragonForgeRecipe(IafRecipeRegistry.LIGHTNING_FORGE_RECIPES, input, blood, output);
    }


    private static void removeDragonForgeRecipe(List<DragonForgeRecipe> recipeList, IItemStack output) {
        recipeList.removeIf(recipe -> recipe.getOutput().isItemEqual(output.getInternal()));
    }

    @ZenCodeType.Method
    public static void removeFireDragonForgeRecipe(IItemStack output) {
        removeDragonForgeRecipe(IafRecipeRegistry.FIRE_FORGE_RECIPES, output);
    }

    @ZenCodeType.Method
    public static void removeIceDragonForgeRecipe(IItemStack output) {
        removeDragonForgeRecipe(IafRecipeRegistry.ICE_FORGE_RECIPES, output);
    }

    @ZenCodeType.Method
    public static void removeLightningDragonForgeRecipe(IItemStack output) {
        removeDragonForgeRecipe(IafRecipeRegistry.LIGHTNING_FORGE_RECIPES, output);
    }
}
