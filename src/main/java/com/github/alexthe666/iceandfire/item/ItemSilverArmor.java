package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ItemSilverArmor extends ArmorItem {

    public ItemSilverArmor(IArmorMaterial material, EquipmentSlotType slot, String name) {
        super(material, slot, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) IceAndFire.PROXY.getArmorModel(slot == EquipmentSlotType.LEGS ? 15 : 14);
    }

}