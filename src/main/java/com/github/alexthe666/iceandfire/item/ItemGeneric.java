package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGeneric extends Item {
    int description = 0;

    public ItemGeneric() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }

    public ItemGeneric(int textLength) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.description = textLength;
    }

    public ItemGeneric(int textLength, boolean hide) {
        super(new Item.Properties());
        this.description = textLength;
    }

    public ItemGeneric(int textLength, int stacksize) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.description = textLength;
    }

    public boolean isFoil(ItemStack stack) {
        if (this == IafItemRegistry.CREATIVE_DRAGON_MEAL.get()) {
            return true;
        } else {
            return super.isFoil(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (description > 0) {
            for (int i = 0; i < description; i++) {
                tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".desc_" + i).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
