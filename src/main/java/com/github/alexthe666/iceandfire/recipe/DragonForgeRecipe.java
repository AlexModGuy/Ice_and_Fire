package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.citadel.client.model.container.JsonUtils;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;


public class DragonForgeRecipe implements Recipe<TileEntityDragonforge> {
    private final Ingredient input;
    private final Ingredient blood;
    private final ItemStack result;
    private final String dragonType;
    private final int cookTime;
    private final ResourceLocation recipeId;

    public DragonForgeRecipe(ResourceLocation recipeId, Ingredient input, Ingredient blood, ItemStack result, String dragonType, int cookTime) {
        this.recipeId = recipeId;
        this.input = input;
        this.blood = blood;
        this.result = result;
        this.dragonType = dragonType;
        this.cookTime = cookTime;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getBlood() {
        return blood;
    }

    public int getCookTime() {
        return cookTime;
    }

    public String getDragonType() {
        return dragonType;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(TileEntityDragonforge inv, @NotNull Level worldIn) {
        return this.input.test(inv.getItem(0)) && this.blood.test(inv.getItem(1)) && this.dragonType.equals(inv.getTypeID());
    }

    public boolean isValidInput(ItemStack stack) {
        return this.input.test(stack);
    }

    public boolean isValidBlood(ItemStack blood) {
        return this.blood.test(blood);
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    public @NotNull ItemStack getResultItem() {
        return result;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull TileEntityDragonforge dragonforge, RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return IafRecipeSerializers.DRAGONFORGE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return IafRecipeRegistry.DRAGON_FORGE_TYPE.get();
    }

    public static class Serializer extends NewRegistryEvent implements RecipeSerializer<DragonForgeRecipe> {
        @Override
        public @NotNull DragonForgeRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String dragonType = JsonUtils.getString(json, "dragon_type");
            Ingredient input = Ingredient.fromJson(JsonUtils.getJsonObject(json, "input"));
            Ingredient blood = Ingredient.fromJson(JsonUtils.getJsonObject(json, "blood"));
            int cookTime = JsonUtils.getInt(json, "cook_time");
            ItemStack result = ShapedRecipe.itemStackFromJson(JsonUtils.getJsonObject(json, "result"));
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        @Override
        public DragonForgeRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int cookTime = buffer.readInt();
            String dragonType = buffer.readUtf();
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient blood = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DragonForgeRecipe recipe) {
            buffer.writeInt(recipe.cookTime);
            buffer.writeUtf(recipe.dragonType);
            recipe.input.toNetwork(buffer);
            recipe.blood.toNetwork(buffer);
            buffer.writeItemStack(recipe.result, true);
        }
    }

}
