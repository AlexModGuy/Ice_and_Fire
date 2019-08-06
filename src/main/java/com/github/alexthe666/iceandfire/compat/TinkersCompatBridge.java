package com.github.alexthe666.iceandfire.compat;

import com.github.alexthe666.iceandfire.compat.tinkers.TinkersCompat;
import com.github.alexthe666.iceandfire.compat.tinkers.TinkersCompatClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Loader;

public class TinkersCompatBridge {
    private static final String COMPAT_MOD_ID = "tconstruct";

    public static void loadTinkersCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            TinkersCompat.register();
        }
    }

    public static void loadTinkersClientCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            TinkersCompatClient.preInit();
        }
    }

    public static void loadTinkersClientModels(ModelRegistryEvent event) {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            TinkersCompatClient.registerModels(event);
        }
    }

    public static void loadTinkersPostInitCompat() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            TinkersCompat.post();
        }
    }
}
