package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;

public class ItemDragonArrow extends ArrowItem {
    public ItemDragonArrow() {
        super(new Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName("iceandfire:dragonbone_arrow");
    }
}
