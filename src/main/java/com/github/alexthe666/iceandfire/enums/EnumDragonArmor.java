package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum EnumDragonArmor {

    armor_red(12, EnumDragonEgg.RED),
    armor_bronze(13, EnumDragonEgg.BRONZE),
    armor_green(14, EnumDragonEgg.GREEN),
    armor_gray(15, EnumDragonEgg.GRAY),
    armor_blue(12, EnumDragonEgg.BLUE),
    armor_white(13, EnumDragonEgg.WHITE),
    armor_sapphire(14, EnumDragonEgg.SAPPHIRE),
    armor_silver(15, EnumDragonEgg.SILVER);

    public ArmorMaterial material;
    public int armorId;
    public EnumDragonEgg eggType;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_helmet")
    public Item helmet;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_chestplate")
    public Item chestplate;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_leggings")
    public Item leggings;
    @GameRegistry.ObjectHolder(IceAndFire.MODID + ":armor_dragon_boots")
    public Item boots;
    public ArmorMaterial armorMaterial;

    EnumDragonArmor(int armorId, EnumDragonEgg eggType) {
        this.armorId = armorId;
        this.eggType = eggType;
    }

    public static void initArmors() {
        for (int i = 0; i < EnumDragonArmor.values().length; i++) {
            EnumDragonArmor.values()[i].armorMaterial = EnumHelper.addArmorMaterial("DragonScales" + (i + 1), "iceandfire:armor_dragon_scales" + (i + 1), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2);
            EnumDragonArmor.values()[i].helmet = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 0, EquipmentSlotType.HEAD).setTranslationKey("iceandfire.dragonHelmet");
            EnumDragonArmor.values()[i].chestplate = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 1, EquipmentSlotType.CHEST).setTranslationKey("iceandfire.dragonChestplate");
            EnumDragonArmor.values()[i].leggings = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 2, EquipmentSlotType.LEGS).setTranslationKey("iceandfire.dragonLeggings");
            EnumDragonArmor.values()[i].boots = new ItemScaleArmor(EnumDragonArmor.values()[i].eggType, EnumDragonArmor.values()[i], EnumDragonArmor.values()[i].armorMaterial, 3, EquipmentSlotType.FEET).setTranslationKey("iceandfire.dragonBoots");
            EnumDragonArmor.values()[i].helmet.setRegistryName(EnumDragonArmor.values()[i].name() + "_helmet");
            //GameRegistry.register(EnumDragonArmor.values()[i].helmet);
            EnumDragonArmor.values()[i].chestplate.setRegistryName(EnumDragonArmor.values()[i].name() + "_chestplate");
            //GameRegistry.register(EnumDragonArmor.values()[i].chestplate);
            EnumDragonArmor.values()[i].leggings.setRegistryName(EnumDragonArmor.values()[i].name() + "_leggings");
            //GameRegistry.register(EnumDragonArmor.values()[i].leggings);
            EnumDragonArmor.values()[i].boots.setRegistryName(EnumDragonArmor.values()[i].name() + "_boots");
            //GameRegistry.register(EnumDragonArmor.values()[i].boots);
        }
    }

    public static Item getScaleItem(EnumDragonArmor armor) {
        switch (armor) {
            case armor_red:
                return IafItemRegistry.DRAGONSCALES_RED;
            case armor_bronze:
                return IafItemRegistry.DRAGONSCALES_BRONZE;
            case armor_green:
                return IafItemRegistry.DRAGONSCALES_GREEN;
            case armor_gray:
                return IafItemRegistry.DRAGONSCALES_GRAY;
            case armor_blue:
                return IafItemRegistry.DRAGONSCALES_BLUE;
            case armor_white:
                return IafItemRegistry.DRAGONSCALES_WHITE;
            case armor_sapphire:
                return IafItemRegistry.DRAGONSCALES_SAPPHIRE;
            case armor_silver:
                return IafItemRegistry.DRAGONSCALES_SILVER;
            default:
                return IafItemRegistry.DRAGONSCALES_RED;
        }
    }

    public static Item getEggItem(EnumDragonArmor armor) {
        switch (armor) {
            case armor_red:
                return IafItemRegistry.DRAGONEGG_RED;
            case armor_bronze:
                return IafItemRegistry.DRAGONEGG_BRONZE;
            case armor_green:
                return IafItemRegistry.DRAGONEGG_GREEN;
            case armor_gray:
                return IafItemRegistry.DRAGONEGG_GRAY;
            default:
                return IafItemRegistry.DRAGONEGG_RED;
        }
    }
}
