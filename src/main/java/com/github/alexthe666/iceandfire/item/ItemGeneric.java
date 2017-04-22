package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModAchievements;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemGeneric extends Item {
    public ItemGeneric(String gameName, String name) {
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName(name);
        GameRegistry.registerItem(this, gameName);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;

            if (this == ModItems.manuscript) {
                player.addStat(ModAchievements.manuscript, 1);
            }
        }
    }
}
