package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.item.ItemBestiary;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public enum EnumBestiaryPages {

    INTRODUCTION(2), FIREDRAGON(4), FIREDRAGONEGG(1), ICEDRAGON(4), ICEDRAGONEGG(1), TAMEDDRAGONS(3), MATERIALS(2), ALCHEMY(0), VILLAGERS(0);

    public int pages;

    EnumBestiaryPages(int pages) {
        this.pages = pages;
    }

    public static List<Integer> toList(int[] containedpages) {
        List<Integer> intList = new ArrayList<Integer>();
        for (int containedpage : containedpages) {
            intList.add(containedpage);
        }
        return intList;
    }

    public static int[] fromList(List<Integer> containedpages) {
        int[] pages = new int[containedpages.size()];
        for (int i = 0; i < pages.length; i++)
            pages[i] = containedpages.get(i);
        return pages;
    }

    public static List<EnumBestiaryPages> containedPages(List<Integer> pages) {
        Iterator itr = pages.iterator();
        List<EnumBestiaryPages> list = new ArrayList<EnumBestiaryPages>();
        while (itr.hasNext()) {
            list.add(EnumBestiaryPages.values()[(Integer) itr.next()]);
        }
        return list;
    }

    public static boolean hasAllPages(ItemStack book) {
        List<EnumBestiaryPages> allPages = new ArrayList<EnumBestiaryPages>();
        for (int i = 0; i < EnumBestiaryPages.values().length; i++) {
            allPages.add(EnumBestiaryPages.values()[i]);
        }
        List<EnumBestiaryPages> pages = containedPages(EnumBestiaryPages.toList(book.getTagCompound().getIntArray("Pages")));
        for (EnumBestiaryPages page : allPages) {
            if (!pages.contains(page)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static List<Integer> enumToInt(List<EnumBestiaryPages> pages) {
        Iterator itr = pages.iterator();
        List<Integer> list = new ArrayList<Integer>();
        while (itr.hasNext()) {
            list.add(EnumBestiaryPages.values()[((EnumBestiaryPages) itr.next()).ordinal()].ordinal());
        }
        return list;
    }

    public static EnumBestiaryPages getRand() {
        return EnumBestiaryPages.values()[new Random().nextInt(EnumBestiaryPages.values().length)];

    }

    public static void addRandomPage(ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            if (!possiblePages(book).isEmpty()) {
                addPage(possiblePages(book).get(new Random().nextInt(possiblePages(book).size())), book);
            }
        }
    }

    public static List<EnumBestiaryPages> possiblePages(ItemStack book) {

        if (book.getItem() instanceof ItemBestiary) {

            Random rand = new Random();
            NBTTagCompound tag = book.getTagCompound();
            List<EnumBestiaryPages> allPages = new ArrayList<EnumBestiaryPages>();
            for (EnumBestiaryPages page : EnumBestiaryPages.values()) {
                allPages.add(page);
            }
            List<EnumBestiaryPages> containedPages = containedPages(toList(tag.getIntArray("Pages")));
            List<EnumBestiaryPages> possiblePages = new ArrayList<EnumBestiaryPages>();
            Iterator itr = allPages.iterator();
            while (itr.hasNext()) {
                EnumBestiaryPages page = (EnumBestiaryPages) itr.next();
                if (!containedPages.contains(page)) {
                    possiblePages.add(page);
                }
            }
            return possiblePages;
        }
        return null;
    }


    public static void addPage(EnumBestiaryPages page, ItemStack book) {
        if (book.getItem() instanceof ItemBestiary) {
            NBTTagCompound tag = book.getTagCompound();
            List<EnumBestiaryPages> enumlist = containedPages(toList(tag.getIntArray("Pages")));
            if (!enumlist.contains(page)) {
                enumlist.add(page);
            }
            tag.setIntArray("Pages", fromList(enumToInt(enumlist)));
        }
    }
}
