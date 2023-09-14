package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem {
    public static final Predicate<ItemStack> DRAGON_ARROWS = (stack)
        -> stack.is(ForgeRegistries.ITEMS.tags().createTagKey(IafTagRegistry.DRAGON_ARROWS));

    public ItemDragonBow() {
        super(new Item.Properties().durability(584));
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return DRAGON_ARROWS;
    }
}
