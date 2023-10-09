package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonScales extends Item {
    EnumDragonEgg type;

    public ItemDragonScales(EnumDragonEgg type) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.type = type;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "item.iceandfire.dragonscales";
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dragon." + type.toString().toLowerCase()).withStyle(type.color));
    }

}
