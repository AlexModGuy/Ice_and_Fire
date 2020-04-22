package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDeathwormArmor extends ItemArmor {

    public ItemDeathwormArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
        super(material, renderIndex, slot);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey(name);
        this.setRegistryName(IceAndFire.MODID, gameName);
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 5 : 4);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (this.getArmorMaterial() == IafItemRegistry.red_deathworm) {
            return "iceandfire:textures/models/armor/armor_deathworm_red" + (renderIndex == 2 ? "_legs.png" : ".png");
        } else if (this.getArmorMaterial() == IafItemRegistry.white_deathworm) {
            return "iceandfire:textures/models/armor/armor_deathworm_white" + (renderIndex == 2 ? "_legs.png" : ".png");
        } else {
            return "iceandfire:textures/models/armor/armor_deathworm_yellow" + (renderIndex == 2 ? "_legs.png" : ".png");
        }
    }
}
