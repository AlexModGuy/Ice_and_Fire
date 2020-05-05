package com.github.alexthe666.iceandfire.misc;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {


    public CreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return this == IceAndFire.TAB_ITEMS ? new ItemStack(IafItemRegistry.DRAGON_SKULL) : new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED);
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }
}
