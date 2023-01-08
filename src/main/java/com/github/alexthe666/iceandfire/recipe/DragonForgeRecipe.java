package com.github.alexthe666.iceandfire.recipe;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.lang.reflect.Type;

public class DragonForgeRecipe implements IRecipe<TileEntityDragonforge> {
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
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean matches(TileEntityDragonforge inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(0)) && this.blood.test(inv.getStackInSlot(1)) && this.dragonType.equals(inv.getTypeID());
    }

    public boolean isValidInput(ItemStack stack) {
        return this.input.test(stack);
    }

    public boolean isValidBlood(ItemStack blood) {
        return this.blood.test(blood);
    }

    @Override
    public ItemStack getCraftingResult(TileEntityDragonforge inv) {
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return IafRecipeRegistry.DRAGONFORGE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return IafRecipeRegistry.DRAGON_FORGE_TYPE;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DragonForgeRecipe> {
        public DragonForgeRecipe read(ResourceLocation recipeId, JsonObject json) {
            String dragonType = JSONUtils.getString(json, "dragon_type");
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
            Ingredient blood = Ingredient.deserialize(JSONUtils.getJsonObject(json, "blood"));
            int cookTime = JSONUtils.getInt(json, "cook_time");
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        public DragonForgeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int cookTime = buffer.readInt();
            String dragonType = buffer.readString();
            Ingredient input = Ingredient.read(buffer);
            Ingredient blood = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        public void write(PacketBuffer buffer, DragonForgeRecipe recipe) {
            buffer.writeInt(recipe.cookTime);
            buffer.writeString(recipe.dragonType);
            recipe.input.write(buffer);
            recipe.blood.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }

}
