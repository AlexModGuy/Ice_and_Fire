package com.github.alexthe666.iceandfire.compat.jei.icedragonforge;

import com.github.alexthe666.iceandfire.compat.jei.IceAndFireJEIPlugin;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IceDragonForgeCategory implements IRecipeCategory<DragonForgeRecipe> {

    public IceDragonForgeDrawable drawable;

    public IceDragonForgeCategory() {
        drawable = new IceDragonForgeDrawable();
    }

    @Override
    public @NotNull RecipeType<DragonForgeRecipe> getRecipeType() {
        return IceAndFireJEIPlugin.ICE_DRAGON_FORGE_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("iceandfire.ice_dragon_forge");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return drawable;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder recipeLayoutBuilder, @NotNull DragonForgeRecipe dragonForgeRecipe, @NotNull IFocusGroup focuses) {
        Level level = Minecraft.getInstance().level;
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 64, 29)
                .addIngredients(dragonForgeRecipe.getInput());
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 82, 29)
                .addIngredients(dragonForgeRecipe.getBlood());
        if (level != null) {
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 144, 30)
                    .addItemStack(dragonForgeRecipe.getResultItem());
        }
    }
}
