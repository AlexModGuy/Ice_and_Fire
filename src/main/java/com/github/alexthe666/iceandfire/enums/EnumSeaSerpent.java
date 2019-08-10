package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockSeaSerpentScales;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentArmor;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumSeaSerpent {
    BLUE(TextFormatting.BLUE),
    BRONZE(TextFormatting.GOLD),
    DEEPBLUE(TextFormatting.DARK_BLUE),
    GREEN(TextFormatting.DARK_GREEN),
    PURPLE(TextFormatting.DARK_PURPLE),
    RED(TextFormatting.DARK_RED),
    TEAL(TextFormatting.AQUA);

    public String resourceName;
    public TextFormatting color;
    public ItemArmor.ArmorMaterial armorMaterial;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_scale")
    public Item scale;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_helmet")
    public Item helmet;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_chestplate")
    public Item chestplate;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_leggings")
    public Item leggings;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_boots")
    public Item boots;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":sea_serpent_scale_block")
    public Block scaleBlock;
    EnumSeaSerpent(TextFormatting color){
        this.resourceName = this.name().toLowerCase();
        this.color = color;
    }


    public static void initArmors() {
        for(EnumSeaSerpent color :EnumSeaSerpent.values()){
            color.armorMaterial  = EnumHelper.addArmorMaterial("SeaSerpentScales" + color.resourceName, "iceandfire:sea_serpent_scales_" + color.resourceName, 30, new int[]{4, 8, 7, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2.5F);
            color.scale = new ItemSeaSerpentScales(color.resourceName, color.color);
            color.helmet = new ItemSeaSerpentArmor(color, color.armorMaterial, 0, EntityEquipmentSlot.HEAD).setTranslationKey("iceandfire.sea_serpent_helmet");
            color.chestplate = new ItemSeaSerpentArmor(color, color.armorMaterial, 1, EntityEquipmentSlot.CHEST).setTranslationKey("iceandfire.sea_serpent_chestplate");
            color.leggings = new ItemSeaSerpentArmor(color, color.armorMaterial, 2, EntityEquipmentSlot.LEGS).setTranslationKey("iceandfire.sea_serpent_leggings");
            color.boots = new ItemSeaSerpentArmor(color, color.armorMaterial, 3, EntityEquipmentSlot.FEET).setTranslationKey("iceandfire.sea_serpent_boots");
            color.helmet.setRegistryName("tide_" + color.resourceName + "_helmet");
            color.chestplate.setRegistryName("tide_" + color.resourceName + "_chestplate");
            color.leggings.setRegistryName("tide_" + color.resourceName + "_leggings");
            color.boots.setRegistryName("tide_" + color.resourceName + "_boots");
        }
    }
}
