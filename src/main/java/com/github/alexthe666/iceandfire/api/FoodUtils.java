package com.github.alexthe666.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class FoodUtils {

    public static int getFoodPoints(Entity entity) {
        int foodPoints = Math.round(entity.width * entity.height * 10);
        if (entity instanceof EntityAgeable) {
            return foodPoints;
        }
        if (entity instanceof PlayerEntity) {
            return 15;
        }
        return 0;
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        if (item != null && item != ItemStack.EMPTY && item.getItem() != null && item.getItem() instanceof ItemFood) {
            int food = (((ItemFood) item.getItem()).getHealAmount(item) * 10);
            if (!meatOnly) {
                return food;
            } else if (((ItemFood) item.getItem()).isWolfsFavoriteMeat()) {
                return food;
            } else if (includeFish && item.getItem() == Items.FISH) {
                return food;
            }
        }
        return 0;
    }

    public static boolean isSeeds(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemSeeds && item != Items.NETHER_WART) {
            return true;
        }
        NonNullList<ItemStack> listAllseed = OreDictionary.getOres("listAllseed");
        NonNullList<ItemStack> listAllSeeds = OreDictionary.getOres("listAllSeeds");
        NonNullList<ItemStack> seed = OreDictionary.getOres("seed");
        NonNullList<ItemStack> seeds = OreDictionary.getOres("seeds");
        return listAllseed.contains(stack) || listAllSeeds.contains(stack) || seed.contains(stack) || seeds.contains(stack);
    }
}
