package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonArmor extends Item implements ICustomRendered {

    public int type;
    public int dragonSlot;
    public String name;

    public ItemDragonArmor(int type, int dragonSlot, String name) {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        this.name = name + "_" + dragonSlot;
        this.setRegistryName(IceAndFire.MODID, this.name);

    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
        tooltip.add(new TranslationTextComponent(words).applyTextStyle(TextFormatting.GRAY));
    }
}
