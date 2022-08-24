package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class ItemGenericFood extends Item {
    private final int healAmount;
    private final float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, String name) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).tab(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, String name, int stackSize) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).stacksTo(stackSize).tab(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static final Food createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, EffectInstance potion) {
        Food.Builder builder = new Food.Builder();
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

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        this.onFoodEaten(stack, worldIn, LivingEntity);
        return super.finishUsingItem(stack, worldIn, LivingEntity);
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
    }
}
