package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem implements ICustomRendered {
    public static final Predicate<ItemStack> DRAGON_ARROWS = (p_220002_0_) -> {
        Tag<Item> tag = ItemTags.getCollection().getOrCreate(IafTagRegistry.DRAGON_ARROWS);
        return p_220002_0_.getItem().isIn(ItemTags.ARROWS);
    };

    public ItemDragonBow() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxDamage(584));
        this.setRegistryName(IceAndFire.MODID, "dragonbone_bow");
    }


    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return DRAGON_ARROWS;
    }

    protected boolean func_185058_h_(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.DRAGONBONE_ARROW;
    }
}
