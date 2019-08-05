package com.github.alexthe666.iceandfire.compat.jei.icedragonforge;

import com.github.alexthe666.iceandfire.compat.jei.IceAndFireJEIPlugin;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeDrawable;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeRecipeWrapper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class IceDragonForgeCatagory implements IRecipeCategory<IceDragonForgeRecipeWrapper> {

    public IceDragonForgeDrawable drawable;

    public IceDragonForgeCatagory() {
        drawable = new IceDragonForgeDrawable();
    }

    @Override
    public String getUid() {
        return IceAndFireJEIPlugin.ICE_DRAGON_FORGE_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("iceandfire.ice_dragon_forge");
    }

    @Override
    public String getModName() {
        return "iceandfire";
    }

    @Override
    public IDrawable getBackground() {
        return drawable;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IceDragonForgeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 64, 29);
        guiItemStacks.init(1, true, 82, 29);
        guiItemStacks.init(2, false, 144, 30);
        guiItemStacks.set(ingredients);
    }
}
