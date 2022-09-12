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

    public ItemDragonArmor(int type, int dragonSlot, String name) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        this.name = name;
        this.setRegistryName(IceAndFire.MODID, name + "_" + getNameForSlot(dragonSlot));

    }

    public String getDescriptionId() {
        return "item.iceandfire." + name;
    }


    private String getNameForSlot(int slot){
        switch (slot){
            case 0:
                return "head";
            case 1:
                return "neck";
            case 2:
                return "body";
            case 3:
                return "tail";
        }
        return "";
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
