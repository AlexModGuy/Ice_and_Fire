package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemGenericFood extends Item {
    private int healAmount;
    private float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, String name) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static final Food createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, EffectInstance potion) {
        Food.Builder builder = new Food.Builder();
        builder.hunger(amount);
        builder.saturation(saturation);
        if (isWolfFood) {
            builder.meat();
        }
        if (eatFast) {
            builder.fastToEat();
        }
        if (alwaysEdible) {
            builder.setAlwaysEdible();
        }
        if (potion != null) {
            builder.effect(potion, 1.0F);
        }
        return builder.build();
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        this.onFoodEaten(stack, worldIn, LivingEntity);
        return super.onItemUseFinish(stack, worldIn, LivingEntity);
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
    }
}
