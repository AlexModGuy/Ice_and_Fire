package com.github.alexthe666.iceandfire.enums;

import java.util.Map;

import net.minecraft.util.text.TextFormatting;

import com.google.common.collect.Maps;

public enum EnumDragonEgg {
	RED(0, TextFormatting.DARK_RED, true),
	GREEN(1, TextFormatting.DARK_GREEN, true),
	BRONZE(2, TextFormatting.GOLD, true),
	GRAY(3, TextFormatting.GRAY, true),
	BLUE(4, TextFormatting.AQUA, false),
	WHITE(5, TextFormatting.WHITE, false),
	SAPPHIRE(6, TextFormatting.BLUE, false),
	SILVER(7, TextFormatting.DARK_GRAY, false);

	
    private static final Map META_LOOKUP = Maps.newHashMap();
	public int meta;
	public TextFormatting color;
	public boolean isFire;
	private EnumDragonEgg(int meta, TextFormatting color, boolean isFire){
		this.meta = meta;
		this.color = color;
		this.isFire = isFire;
	}
	
	 public static EnumDragonEgg byMetadata(int meta)
     {
	    	EnumDragonEgg i = (EnumDragonEgg)META_LOOKUP.get(Integer.valueOf(meta));
         return i == null ? RED : i;
     }
	 
	 static
     {
         EnumDragonEgg[] var0 = values();
         int var1 = var0.length;

         for (int var2 = 0; var2 < var1; ++var2)
         {
        	 EnumDragonEgg var3 = var0[var2];
             META_LOOKUP.put(Integer.valueOf(var3.meta), var3);
         }
     }
}
