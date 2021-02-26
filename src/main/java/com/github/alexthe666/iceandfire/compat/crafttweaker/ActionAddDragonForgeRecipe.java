package com.github.alexthe666.iceandfire.compat.crafttweaker;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;

import java.util.List;

public class ActionAddDragonForgeRecipe implements IUndoableAction {

    private final List<DragonForgeRecipe> recipeList;
    private final IItemStack input;
    private final IItemStack output;
    private final IItemStack blood;
    private DragonForgeRecipe recipe;

    public ActionAddDragonForgeRecipe(List<DragonForgeRecipe> recipeList, IItemStack input, IItemStack output, IItemStack blood) {
        this.recipeList = recipeList;
        this.input = input;
        this.output = output;
        this.blood = blood;
    }

    @Override
    public void apply() {
        recipe = new DragonForgeRecipe(input.getInternal(), blood.getInternal(), output.getInternal());
        recipeList.add(recipe);
    }

    @Override
    public String describe() {
        return "Adding Dragon Forge Recipe for: " + output.getCommandString() + " with input: " + input.getCommandString() + " and blood: " + input.getCommandString();
    }


    @Override
    public void undo() {
        recipeList.remove(recipe);
    }

    @Override
    public String describeUndo() {
        return "Undoing addition of Dragon Forge Recipe for: " + output.getCommandString() + " with input: " + input.getCommandString() + " and blood: " + input.getCommandString();

    }

}
