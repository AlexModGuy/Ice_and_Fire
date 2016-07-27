package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class ItemScaleArmor extends ItemArmor {

	public EnumDragonArmor armor_type;
	public EnumDragonEgg eggType;

	public ItemScaleArmor(EnumDragonEgg eggType, EnumDragonArmor armorType, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
		super(material, renderIndex, slot);
		this.armor_type = armorType;
		this.eggType = eggType;
		this.setCreativeTab(IceAndFire.TAB);
	}

	@SideOnly(Side.CLIENT)
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
		return (ModelBiped) IceAndFire.PROXY.getArmorModel((armor_type.ordinal() > 3 ? 2 : 1));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/" + armor_type.name() + ".png";
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(eggType.color + StatCollector.translateToLocal("dragon." + eggType.toString().toLowerCase()));
	}
}
