package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.entity.DragonType;

import net.minecraft.util.text.TextFormatting;

public enum EnumDragonEgg {

    RED(TextFormatting.DARK_RED, DragonType.FIRE), GREEN(TextFormatting.DARK_GREEN, DragonType.FIRE),
    BRONZE(TextFormatting.GOLD, DragonType.FIRE), GRAY(TextFormatting.GRAY, DragonType.FIRE),
    BLUE(TextFormatting.AQUA, DragonType.ICE), WHITE(TextFormatting.WHITE, DragonType.ICE),
    SAPPHIRE(TextFormatting.BLUE, DragonType.ICE), SILVER(TextFormatting.DARK_GRAY, DragonType.ICE),
    ELECTRIC(TextFormatting.DARK_BLUE, DragonType.LIGHTNING),
    AMYTHEST(TextFormatting.LIGHT_PURPLE, DragonType.LIGHTNING), COPPER(TextFormatting.GOLD, DragonType.LIGHTNING),
    BLACK(TextFormatting.DARK_GRAY, DragonType.LIGHTNING);

    public TextFormatting color;
    public DragonType dragonType;

    EnumDragonEgg(TextFormatting color, DragonType dragonType) {
        this.color = color;
        this.dragonType = dragonType;
    }

    public static EnumDragonEgg byMetadata(int meta) {
        EnumDragonEgg i = values()[meta];
        return i == null ? RED : i;
    }
}
