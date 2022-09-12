package com.github.alexthe666.iceandfire.client;

import net.minecraft.client.resources.language.I18n;

/*
    Legacy translations helper
 */
public class StatCollector {

    public static String translateToLocal(String s) {
        return I18n.get(s);
    }
}
