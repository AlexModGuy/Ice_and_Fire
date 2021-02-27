package com.github.alexthe666.iceandfire.compat.crafttweaker;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;

import java.util.ArrayList;
import java.util.List;

public class ActionRemoveDragonForgeRecipe implements IUndoableAction {

    private final List<DragonForgeRecipe> recipeList;
    private final IItemStack output;
    private final List<DragonForgeRecipe> removedRecipes;

    public ActionRemoveDragonForgeRecipe(List<DragonForgeRecipe> recipeList, IItemStack output) {
        this.recipeList = recipeList;
        this.output = output;
        this.removedRecipes = new ArrayList<>();
    }

    @Override
    public void apply() {
        for (DragonForgeRecipe forgeRecipe : recipeList) {
            if (this.output.matches(new MCItemStackMutable(forgeRecipe.getOutput()))) {
                removedRecipes.add(forgeRecipe);
            }
        }
        recipeList.removeAll(removedRecipes);
    }

    @Override
    public String describe() {
        return "Removing Dragon Forge Recipes for: " + output.getCommandString();
    }


    @Override
    public void undo() {
        recipeList.addAll(removedRecipes);
    }

    @Override
    public String describeUndo() {
        return "Undoing removal of Dragon Forge Recipes for: " + output.getCommandString();

    }

}
