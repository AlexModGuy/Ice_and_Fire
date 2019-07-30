package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDragonsteelArmor extends ItemArmor {

	private ArmorMaterial material;

	public ItemDragonsteelArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
		super(material, renderIndex, slot);
		this.material = material;
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey(name);
		this.setRegistryName(IceAndFire.MODID, gameName);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if(material == ModItems.dragonsteel_fire_armor){
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 11 : 10);
		}else{
			return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 13 : 12);
		}
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if(material == ModItems.dragonsteel_fire_armor) {
			return "iceandfire:textures/models/armor/armor_dragonsteel_fire" + (renderIndex == 2 ? "_legs.png" : ".png");
		}else{
			return "iceandfire:textures/models/armor/armor_dragonsteel_ice" + (renderIndex == 2 ? "_legs.png" : ".png");
		}
	}
}
