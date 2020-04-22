package com.github.alexthe666.iceandfire.compat.craftweaker;

import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.iceandfire.recipes")
public class CraftTweakerCompat {

    public static void preInit() {
        CraftTweakerAPI.registerClass(CraftTweakerCompat.class);
    }

    @ZenMethod
    public static void addFireDragonForgeRecipe(IItemStack iinput, IItemStack ibloodinput, IItemStack ioutput) {
        IafRecipeRegistry.FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(CraftTweakerMC.getItemStack(iinput), CraftTweakerMC.getItemStack(ibloodinput), CraftTweakerMC.getItemStack(ioutput)));
    }

    @ZenMethod
    public static void addIceDragonForgeRecipe(IItemStack iinput, IItemStack ibloodinput, IItemStack ioutput) {
        IafRecipeRegistry.ICE_FORGE_RECIPES.add(new DragonForgeRecipe(CraftTweakerMC.getItemStack(iinput), CraftTweakerMC.getItemStack(ibloodinput), CraftTweakerMC.getItemStack(ioutput)));
    }

    @ZenMethod
    public static void removeFireDragonForgeRecipe(IItemStack ioutput) {
        ItemStack output = CraftTweakerMC.getItemStack(ioutput).copy();
        output.setCount(1);
        IafRecipeRegistry.FIRE_FORGE_RECIPES.removeIf(recipe -> recipe.getOutput().copy().isItemEqual(output));
    }

    @ZenMethod
    public static void removeIceDragonForgeRecipe(IItemStack ioutput) {
        ItemStack output = CraftTweakerMC.getItemStack(ioutput).copy();
        output.setCount(1);
        IafRecipeRegistry.ICE_FORGE_RECIPES.removeIf(recipe -> recipe.getOutput().copy().isItemEqual(output));
    }

}
