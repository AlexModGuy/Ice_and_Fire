package com.github.alexthe666.iceandfire.enums;

import com.google.common.collect.Maps;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public enum EnumDragonEgg {
    RED(0, TextFormatting.DARK_RED, true), GREEN(1, TextFormatting.DARK_GREEN, true), BRONZE(2, TextFormatting.GOLD, true), GRAY(3, TextFormatting.GRAY, true),
    BLUE(4, TextFormatting.AQUA, false), WHITE(5, TextFormatting.WHITE, false), SAPPHIRE(6, TextFormatting.BLUE, false), SILVER(7, TextFormatting.DARK_GRAY, false);

    private static final Map<Integer, EnumDragonEgg> META_LOOKUP = Maps.newHashMap();

    static {
        EnumDragonEgg[] var0 = values();
        int var1 = var0.length;

        for (EnumDragonEgg var3 : var0) {
            META_LOOKUP.put(var3.meta, var3);
        }
    }

    public int meta;
    public TextFormatting color;
    public boolean isFire;

    EnumDragonEgg(int meta, TextFormatting color, boolean isFire) {
        this.meta = meta;
        this.color = color;
        this.isFire = isFire;
    }

    public static EnumDragonEgg byMetadata(int meta) {
        EnumDragonEgg i = META_LOOKUP.get(meta);
        return i == null ? RED : i;
    }
}
