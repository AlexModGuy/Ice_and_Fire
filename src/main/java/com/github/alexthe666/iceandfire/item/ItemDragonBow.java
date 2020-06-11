package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDragonBow extends BowItem implements ICustomRendered {

    public ItemDragonBow() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxDamage(584));
        this.setRegistryName(IceAndFire.MODID, "dragonbone_bow");
    }

    protected boolean func_185058_h_(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.DRAGONBONE_ARROW;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
