package com.github.alexthe666.iceandfire.recipe;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.lang.reflect.Type;

public class DragonForgeRecipe {

    private final Ingredient input;
    private final Ingredient blood;
    private final ItemStack output;
    private final String dragonType;

    public DragonForgeRecipe(Ingredient input, Ingredient blood, ItemStack output, String dragonType) {
        this.input = input;
        this.blood = blood;
        this.output = output;
        this.dragonType = dragonType;
    }

    public Ingredient getInput() {

        return input;
    }

    public Ingredient getBlood() {
        return blood;
    }

    public ItemStack getOutput() {
        return output;
    }

    public String getDragonType() {
        return dragonType;
    }

    public static class Deserializer implements JsonDeserializer<DragonForgeRecipe> {

        @Override
        public DragonForgeRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = json.getAsJsonObject();
            String dragonType = GsonHelper.getAsString(jsonobject, "dragon_type");
            Ingredient input_left = Ingredient.EMPTY;
            if (jsonobject.has("input_left")) {
                input_left = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonobject, "input_left"));
            }
            Ingredient input_right = Ingredient.EMPTY;
            if (jsonobject.has("input_right")) {
                input_right = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonobject, "input_right"));
            }
            ItemStack result = ItemStack.EMPTY;
            if (jsonobject.has("result")) {
                result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonobject, "result"));
            }
            return new DragonForgeRecipe(input_left, input_right, result, dragonType);
        }

    }

}
