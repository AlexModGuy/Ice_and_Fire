package com.github.alexthe666.iceandfire.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGeneric extends Item {
    int description = 0;

    public ItemGeneric() {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    public ItemGeneric(int textLength) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.description = textLength;
    }

    public ItemGeneric(int textLength, boolean hide) {
        super(new Item.Properties());
        this.description = textLength;
    }

    public ItemGeneric(int textLength, int stacksize) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(stacksize));
        this.description = textLength;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        if (this == IafItemRegistry.CREATIVE_DRAGON_MEAL.get()) {
            return true;
        } else {
            return super.isFoil(stack);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (description > 0) {
            for (int i = 0; i < description; i++) {
                tooltip.add(Component.translatable(this.getDescriptionId() + ".desc_" + i).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
