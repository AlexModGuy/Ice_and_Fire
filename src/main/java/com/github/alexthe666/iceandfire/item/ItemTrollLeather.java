package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.item.Item;

public class ItemTrollLeather extends Item {

    public ItemTrollLeather(EnumTroll troll) {
        this.setTranslationKey("iceandfire.troll_leather_" + troll.name().toLowerCase());
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setRegistryName(IceAndFire.MODID, "troll_leather_" + troll.name().toLowerCase());
    }
}
