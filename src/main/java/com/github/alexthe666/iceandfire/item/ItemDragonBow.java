package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem implements ICustomRendered {
    public static final Predicate<ItemStack> DRAGON_ARROWS = (stack) -> {
        ITag<Item> tag = ItemTags.getAllTags().getTag(IafTagRegistry.DRAGON_ARROWS);
        return stack.getItem().is(tag);
    };

    public ItemDragonBow() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).durability(584));
        this.setRegistryName(IceAndFire.MODID, "dragonbone_bow");
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return DRAGON_ARROWS;
    }
}
