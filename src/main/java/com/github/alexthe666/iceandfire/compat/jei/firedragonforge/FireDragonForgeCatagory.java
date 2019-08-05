package com.github.alexthe666.iceandfire.compat.jei.firedragonforge;

import com.github.alexthe666.iceandfire.compat.jei.IceAndFireJEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class FireDragonForgeCatagory implements IRecipeCategory<FireDragonForgeRecipeWrapper> {

    public FireDragonForgeDrawable drawable;

    public FireDragonForgeCatagory() {
        drawable = new FireDragonForgeDrawable();
    }

    @Override
    public String getUid() {
        return IceAndFireJEIPlugin.FIRE_DRAGON_FORGE_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("iceandfire.fire_dragon_forge");
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
    public void setRecipe(IRecipeLayout recipeLayout, FireDragonForgeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 64, 29);
        guiItemStacks.init(1, true, 82, 29);
        guiItemStacks.init(2, false, 144, 30);
        guiItemStacks.set(ingredients);
    }
}
