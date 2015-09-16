package com.github.alexthe666.iceandfire.enums;

import java.util.Map;

import net.minecraft.item.ItemFishFood;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Maps;

public enum EnumDragonEgg {
	RED(0, EnumChatFormatting.DARK_RED, true),
	GREEN(1, EnumChatFormatting.DARK_GREEN, true),
	BRONZE(2, EnumChatFormatting.GOLD, true),
	GRAY(3, EnumChatFormatting.GRAY, true),
	BLUE(4, EnumChatFormatting.AQUA, false),
	WHITE(5, EnumChatFormatting.WHITE, false),
	SAPPHIRE(5, EnumChatFormatting.BLUE, false),
	SILVER(5, EnumChatFormatting.DARK_GRAY, false);

	
    private static final Map META_LOOKUP = Maps.newHashMap();
	public int meta;
	public EnumChatFormatting color;
	public boolean isFire;
	private EnumDragonEgg(int meta, EnumChatFormatting color, boolean isFire){
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
