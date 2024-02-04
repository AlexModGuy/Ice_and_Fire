package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.block.BlockSeaSerpentScales;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafArmorMaterial;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentArmor;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public enum EnumSeaSerpent {
    BLUE(ChatFormatting.BLUE),
    BRONZE(ChatFormatting.GOLD),
    DEEPBLUE(ChatFormatting.DARK_BLUE),
    GREEN(ChatFormatting.DARK_GREEN),
    PURPLE(ChatFormatting.DARK_PURPLE),
    RED(ChatFormatting.DARK_RED),
    TEAL(ChatFormatting.AQUA);

    public String resourceName;
    public ChatFormatting color;
    public CustomArmorMaterial armorMaterial;
    public RegistryObject<Item> scale;
    public RegistryObject<Item> helmet;
    public RegistryObject<Item> chestplate;
    public RegistryObject<Item> leggings;
    public RegistryObject<Item> boots;
    public RegistryObject<Block> scaleBlock;

    EnumSeaSerpent(ChatFormatting color) {
        this.resourceName = this.name().toLowerCase(Locale.ROOT);
        this.color = color;
    }


    public static void initArmors() {
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
            color.armorMaterial = new IafArmorMaterial("iceandfire:sea_serpent_scales_" + color.resourceName, 30, new int[]{4, 8, 7, 4}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 2.5F);
            color.scaleBlock = IafBlockRegistry.register("sea_serpent_scale_block_" + color.resourceName, () -> new BlockSeaSerpentScales(color.resourceName, color.color));
            color.scale = IafItemRegistry.registerItem("sea_serpent_scales_" + color.resourceName, () ->
                new ItemSeaSerpentScales(color.resourceName, color.color));
            color.helmet = IafItemRegistry.registerItem("tide_" + color.resourceName + "_helmet", () ->
                new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.HELMET));
            color.chestplate = IafItemRegistry.registerItem("tide_" + color.resourceName + "_chestplate", () ->
                new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.CHESTPLATE));
            color.leggings = IafItemRegistry.registerItem("tide_" + color.resourceName + "_leggings", () ->
                new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.LEGGINGS));
            color.boots = IafItemRegistry.registerItem("tide_" + color.resourceName + "_boots", () ->
                new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.BOOTS));
        }
    }
}
