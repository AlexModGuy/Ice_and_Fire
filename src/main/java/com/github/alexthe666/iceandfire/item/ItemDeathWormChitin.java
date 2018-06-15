package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemDeathWormChitin extends Item {
    public ItemDeathWormChitin() {
        this.setCreativeTab(IceAndFire.TAB);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("deathworm_chitin");
        this.setRegistryName(IceAndFire.MODID, "deathworm_chitin");
    }

    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 2 ? "item.iceandfire.deathworm_chitin_red" : stack.getMetadata() == 1 ? "item.iceandfire.deathworm_chitin_white" :"item.iceandfire.deathworm_chitin_yellow";
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 2));
        }
    }
}
