package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem {
    private static final Predicate<ItemStack> DRAGON_ARROWS = stack -> stack.is(IafItemTags.DRAGON_ARROWS);

    public ItemDragonBow() {
        super(new Item.Properties().durability(584));
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return DRAGON_ARROWS;
    }
}
