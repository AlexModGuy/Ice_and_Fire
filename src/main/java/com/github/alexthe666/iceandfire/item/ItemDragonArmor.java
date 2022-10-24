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

public class ItemDragonArmor extends Item {

    public int type;
    public int dragonSlot;
    public String name;

    public ItemDragonArmor(int type, int dragonSlot) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        this.name = this.getRegistryName().getPath();
        this.setRegistryName(IceAndFire.MODID, name + "_" + getNameForSlot(dragonSlot));
        // TODO: fix registry name here
    }

    public String getDescriptionId() {
        return "item.iceandfire." + name;
    }


    static String getNameForSlot(int slot){
        return switch (slot) {
            case 0 -> "head";
            case 1 -> "neck";
            case 2 -> "body";
            case 3 -> "tail";
            default -> "";
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String words;
        switch (dragonSlot) {
            default:
                words = "dragon.armor_head";
                break;
            case 1:
                words = "dragon.armor_neck";
                break;
            case 2:
                words = "dragon.armor_body";
                break;
            case 3:
                words = "dragon.armor_tail";
                break;
        }
        tooltip.add(new TranslatableComponent(words).withStyle(ChatFormatting.GRAY));
    }
}
