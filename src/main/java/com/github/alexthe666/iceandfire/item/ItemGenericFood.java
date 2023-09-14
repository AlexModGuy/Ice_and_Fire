package com.github.alexthe666.iceandfire.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemGenericFood extends Item {
    private final int healAmount;
    private final float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, int stackSize) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).stacksTo(stackSize));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static final FoodProperties createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, MobEffectInstance potion) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(amount);
        builder.saturationMod(saturation);
        if (isWolfFood) {
            builder.meat();
        }
        if (eatFast) {
            builder.fast();
        }
        if (alwaysEdible) {
            builder.alwaysEat();
        }
        if (potion != null) {
            builder.effect(potion, 1.0F);
        }
        return builder.build();
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity LivingEntity) {
        this.onFoodEaten(stack, worldIn, LivingEntity);
        return super.finishUsingItem(stack, worldIn, LivingEntity);
    }

    public void onFoodEaten(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
    }
}
