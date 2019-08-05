package com.github.alexthe666.iceandfire.compat.jei.firedragonforge;

import com.github.alexthe666.iceandfire.compat.jei.IceAndFireJEIPlugin;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class FireDragonForgeRecipeHandler implements IRecipeHandler<FireDragonForgeRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return DragonForgeRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(FireDragonForgeRecipeWrapper recipe) {
        return IceAndFireJEIPlugin.FIRE_DRAGON_FORGE_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(FireDragonForgeRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(FireDragonForgeRecipeWrapper recipe) {
        return true;
    }
}
