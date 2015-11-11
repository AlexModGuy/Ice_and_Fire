package com.github.alexthe666.iceandfire.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.github.alexthe666.iceandfire.item.ItemBestiary;

public enum EnumBestiaryPages {
	INTRO, FIREDRAGONS, FIREEGGS, ICEDRAGONS, ICEEGGS, DRAGONSCALES, DRAGONBONES; 

	public static List<Integer> getList(int[] containedpages){
		List<Integer> intList = new ArrayList<Integer>();
		for (int index = 0; index < containedpages.length; index++)
		{
			intList.add(containedpages[index]);
		}
		return intList;
	}

	public static int[] fromList(List<Integer> containedpages){
		int[] pages = new int[containedpages.size()];
		for(int i = 0;i < pages.length;i++)
			pages[i] = containedpages.get(i);
		return pages;
	}

	public static List<EnumBestiaryPages> containedPages(List<Integer> pages){
		Iterator itr = pages.iterator();
		List<EnumBestiaryPages> list = new ArrayList<EnumBestiaryPages>();
		while(itr.hasNext()) {
			list.add(EnumBestiaryPages.values()[(Integer)itr.next()]);
		}
		return list;
	}

	public static List<Integer> enumToInt(List<EnumBestiaryPages> pages){
		Iterator itr = pages.iterator();
		List<Integer> list = new ArrayList<Integer>();
		while(itr.hasNext()) {
			list.add(EnumBestiaryPages.values()[((EnumBestiaryPages)itr.next()).ordinal()].ordinal());
		}
		return list;
	}
	public static EnumBestiaryPages getRand(){
		return EnumBestiaryPages.values()[new Random().nextInt(EnumBestiaryPages.values().length)];

	}
	public static void addPage(EnumBestiaryPages page, ItemStack book){
		if(book.getItem() instanceof ItemBestiary){
			NBTTagCompound tag = book.getTagCompound();
			List<EnumBestiaryPages> enumlist = containedPages(getList(tag.getIntArray("Pages")));
			if(!enumlist.contains(page)){
				enumlist.add(page);
			}
			tag.setIntArray("Pages", fromList(enumToInt(enumlist)));
		}
	}
}
