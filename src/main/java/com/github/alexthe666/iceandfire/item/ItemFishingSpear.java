package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.item.Item;

public class ItemFishingSpear extends Item {

    public ItemFishingSpear() {
        super(new Item.Properties().durability(64).tab(IceAndFire.TAB_ITEMS));
    }
}
