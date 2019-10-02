package com.github.alexthe666.iceandfire.compat.jei.icedragonforge;

import com.github.alexthe666.iceandfire.compat.jei.IceAndFireJEIPlugin;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class IceDragonForgeRecipeHandler implements IRecipeHandler<IceDragonForgeRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return DragonForgeRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(IceDragonForgeRecipeWrapper recipe) {
        return IceAndFireJEIPlugin.ICE_DRAGON_FORGE_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(IceDragonForgeRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(IceDragonForgeRecipeWrapper recipe) {
        return true;
    }
}
