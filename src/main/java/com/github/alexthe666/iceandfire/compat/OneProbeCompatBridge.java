package com.github.alexthe666.iceandfire.compat;

import com.github.alexthe666.iceandfire.compat.theoneprobe.IceAndFireOneProbeCompat;
import net.minecraftforge.fml.common.Loader;

public class OneProbeCompatBridge {
    private static final String COMPAT_MOD_ID = "theoneprobe";

    public static void loadPreInit() {
        if (Loader.isModLoaded(COMPAT_MOD_ID)) {
            IceAndFireOneProbeCompat.register();
        }
    }


}
