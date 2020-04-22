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
        return this == IceAndFire.TAB_ITEMS ? new ItemStack(IafItemRegistry.dragon_skull) : new ItemStack(IafBlockRegistry.dragonscale_red);
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }
}
