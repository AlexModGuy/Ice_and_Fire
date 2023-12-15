package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.entity.DragonType;
import net.minecraft.ChatFormatting;

public enum EnumDragonEgg {
    RED(ChatFormatting.DARK_RED, DragonType.FIRE), GREEN(ChatFormatting.DARK_GREEN, DragonType.FIRE),
    BRONZE(ChatFormatting.GOLD, DragonType.FIRE), GRAY(ChatFormatting.GRAY, DragonType.FIRE),
    BLUE(ChatFormatting.AQUA, DragonType.ICE), WHITE(ChatFormatting.WHITE, DragonType.ICE),
    SAPPHIRE(ChatFormatting.BLUE, DragonType.ICE), SILVER(ChatFormatting.DARK_GRAY, DragonType.ICE),
    ELECTRIC(ChatFormatting.DARK_BLUE, DragonType.LIGHTNING),
    AMYTHEST(ChatFormatting.LIGHT_PURPLE, DragonType.LIGHTNING), COPPER(ChatFormatting.GOLD, DragonType.LIGHTNING),
    BLACK(ChatFormatting.DARK_GRAY, DragonType.LIGHTNING);

    public final ChatFormatting color;
    public final DragonType dragonType;

    EnumDragonEgg(final ChatFormatting color, final DragonType dragonType) {
        this.color = color;
        this.dragonType = dragonType;
    }

    public static EnumDragonEgg byMetadata(int meta) {
        EnumDragonEgg i = values()[meta];
        return i == null ? RED : i;
    }
}
