package com.github.alexthe666.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class FoodUtils {

    public static int getFoodPoints(Entity entity){
        int foodPoints = Math.round(entity.width * entity.height * 10);
        if(entity instanceof EntityAgeable){
            return foodPoints;
        }
        return 0;
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly){
        if(item != null && item != ItemStack.EMPTY && item.getItem() != null && item.getItem() instanceof ItemFood){
            int food = (int)(((ItemFood)item.getItem()).getHealAmount(ItemStack.EMPTY) * 2.5);
            if(!meatOnly){
                return food;
            }else if(((ItemFood)item.getItem()).isWolfsFavoriteMeat()){
                return food;
            }
        }
        return 0;
    }
}
