package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemModArmor extends ArmorItem {

    public ItemModArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }

    public String getDescriptionId(ItemStack stack) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                return "item.iceandfire.air_pods";
            }
        }
        return super.getDescriptionId(stack);
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.material == IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_desert_layer_2" : "myrmex_desert_layer_1") + ".png";
        }
        if (this.material == IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "myrmex_jungle_layer_2" : "myrmex_jungle_layer_1") + ".png";
        }
        if (this.material == IafItemRegistry.SHEEP_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "sheep_disguise_layer_2" : "sheep_disguise_layer_1") + ".png";
        }
        if (this.material == IafItemRegistry.EARPLUGS_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/earplugs_layer_1.png";
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this == IafItemRegistry.EARPLUGS.get()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 4 && calendar.get(5) == 1) {
                tooltip.add(new TranslatableComponent("item.iceandfire.air_pods.desc").withStyle(ChatFormatting.GREEN));
            }
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
