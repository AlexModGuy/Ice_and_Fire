package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeShinyScales extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private int potionLength;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int scaleCount = 0;
        boolean water = false;
        for(int i = 0; i < 9; i++){
            if(inv.getStackInSlot(i).getItem() == ModItems.shiny_scales){
                scaleCount++;
            }
        }
        for(int i = 0; i < 9; i++){
            if(inv.getStackInSlot(i).getItem() == Items.POTIONITEM){
                water = true;
            }
        }
        potionLength = scaleCount;
        return water && scaleCount > 0 && scaleCount <= 2;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", potionLength == 1 ? "water_breathing" : "long_water_breathing");
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack stack = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", potionLength == 1 ? "water_breathing" : "long_water_breathing");
        stack.setTagCompound(tag);
        return stack;
    }
}
