package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemModArmor extends ArmorItem {

    public ItemModArmor(ArmorMaterial material, EquipmentSlotType slot, String name) {
        super(material, slot, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    public String getTranslationKey(ItemStack stack) {
        if (this == IafItemRegistry.EARPLUGS) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                return "item.iceandfire.air_pods";
            }
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this == IafItemRegistry.EARPLUGS) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                tooltip.add(new TranslationTextComponent("item.iceandfire.air_pods.desc").applyTextStyle(TextFormatting.GREEN));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
