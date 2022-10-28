package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.world.item.Item;

import java.util.Locale;

public class ItemTrollLeather extends Item {

    public ItemTrollLeather(EnumTroll troll) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }
}
