package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem {
    public static final Predicate<ItemStack> DRAGON_ARROWS = (stack)
            -> stack.is(ForgeRegistries.ITEMS.tags().createTagKey(IafTagRegistry.DRAGON_ARROWS));

    public ItemDragonBow() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).durability(584));
        this.setRegistryName(IceAndFire.MODID, "dragonbone_bow");
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return DRAGON_ARROWS;
    }
}
