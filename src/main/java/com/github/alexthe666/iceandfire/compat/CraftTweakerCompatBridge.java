package com.github.alexthe666.iceandfire.compat;

import com.github.alexthe666.iceandfire.compat.craftweaker.CraftTweakerCompat;
import net.minecraftforge.fml.common.Loader;

public class CraftTweakerCompatBridge {
    private static final String COMPAT_MOD_ID = "crafttweaker";

    public static void loadTweakerCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            CraftTweakerCompat.preInit();
        }
    }
}
