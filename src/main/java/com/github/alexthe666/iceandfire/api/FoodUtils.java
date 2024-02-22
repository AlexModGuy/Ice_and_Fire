package com.github.alexthe666.iceandfire.api;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class FoodUtils {
    public static int getFoodPoints(final Entity entity) {
        int foodPoints = Math.round(entity.getBbWidth() * entity.getBbHeight() * 10);

        if (entity instanceof AgeableMob) {
            return foodPoints;
        }

        if (entity instanceof Player) {
            return 15;
        }

        return 0;
    }

    public static int getFoodPoints(final ItemStack stack, boolean meatOnly, boolean includeFish) {
        return getFoodPoints(null, stack, meatOnly, includeFish);
    }

    public static int getFoodPoints(final @Nullable LivingEntity livingEntity, final ItemStack stack, boolean meatOnly, boolean includeFish) {
        if (stack == null) {
            return 0;
        }

        FoodProperties foodProperties = stack.getFoodProperties(livingEntity);

        if (foodProperties != null) {
            int nutrition = foodProperties.getNutrition() * 10;

            if (!meatOnly) {
                return nutrition;
            } else if (foodProperties.isMeat()) {
                return nutrition;
            } else if (includeFish && stack.is(ItemTags.FISHES)) {
                return nutrition;
            }
        }

        return 0;
    }
}
