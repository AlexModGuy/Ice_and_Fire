package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.item.IafArmorMaterial;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public enum EnumDragonArmor {

    armor_red(12, EnumDragonEgg.RED),
    armor_bronze(13, EnumDragonEgg.BRONZE),
    armor_green(14, EnumDragonEgg.GREEN),
    armor_gray(15, EnumDragonEgg.GRAY),
    armor_blue(12, EnumDragonEgg.BLUE),
    armor_white(13, EnumDragonEgg.WHITE),
    armor_sapphire(14, EnumDragonEgg.SAPPHIRE),
    armor_silver(15, EnumDragonEgg.SILVER),
    armor_electric(12, EnumDragonEgg.ELECTRIC),
    armor_amythest(13, EnumDragonEgg.AMYTHEST),
    armor_copper(14, EnumDragonEgg.COPPER),
    armor_black(15, EnumDragonEgg.BLACK);

    public CustomArmorMaterial material;
    public int armorId;
    public EnumDragonEgg eggType;
    public RegistryObject<Item> helmet;
    public RegistryObject<Item> chestplate;
    public RegistryObject<Item> leggings;
    public RegistryObject<Item> boots;
    public CustomArmorMaterial armorMaterial;

    EnumDragonArmor(int armorId, EnumDragonEgg eggType) {
        this.armorId = armorId;
        this.eggType = eggType;
    }

    public static void initArmors() {
        for (int i = 0; i < EnumDragonArmor.values().length; i++) {
            EnumDragonArmor.values()[i].armorMaterial = new IafArmorMaterial("iceandfire:armor_dragon_scales" + (i + 1), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ARMOR_EQUIP_CHAIN, 2);
            String sub = EnumDragonArmor.values()[i].name();

            int finalI = i;
            EnumDragonArmor.values()[finalI].helmet = IafItemRegistry.registerItem(sub + "_helmet", () ->
                new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.HELMET));
            EnumDragonArmor.values()[finalI].chestplate = IafItemRegistry.registerItem(sub + "_chestplate", () ->
                new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.CHESTPLATE));
            EnumDragonArmor.values()[finalI].leggings = IafItemRegistry.registerItem(sub + "_leggings", () ->
                new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.LEGGINGS));
            EnumDragonArmor.values()[finalI].boots = IafItemRegistry.registerItem(sub + "_boots", () ->
                new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.BOOTS));
        }
    }

    public static Item getScaleItem(EnumDragonArmor armor) {
        return switch (armor) {
            case armor_red -> IafItemRegistry.DRAGONSCALES_RED.get();
            case armor_bronze -> IafItemRegistry.DRAGONSCALES_BRONZE.get();
            case armor_green -> IafItemRegistry.DRAGONSCALES_GREEN.get();
            case armor_gray -> IafItemRegistry.DRAGONSCALES_GRAY.get();
            case armor_blue -> IafItemRegistry.DRAGONSCALES_BLUE.get();
            case armor_white -> IafItemRegistry.DRAGONSCALES_WHITE.get();
            case armor_sapphire -> IafItemRegistry.DRAGONSCALES_SAPPHIRE.get();
            case armor_silver -> IafItemRegistry.DRAGONSCALES_SILVER.get();
            case armor_electric -> IafItemRegistry.DRAGONSCALES_ELECTRIC.get();
            case armor_amythest -> IafItemRegistry.DRAGONSCALES_AMYTHEST.get();
            case armor_copper -> IafItemRegistry.DRAGONSCALES_COPPER.get();
            case armor_black -> IafItemRegistry.DRAGONSCALES_BLACK.get();
            default -> IafItemRegistry.DRAGONSCALES_RED.get();
        };
    }
}
