package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonScales extends Item {
    EnumDragonEgg type;

    public ItemDragonScales(EnumDragonEgg type) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.type = type;
    }

    public String getDescriptionId() {
        return "item.iceandfire.dragonscales";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("dragon." + type.toString().toLowerCase()).withStyle(type.color));
    }

}
