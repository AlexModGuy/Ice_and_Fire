package com.github.alexthe666.iceandfire.access;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.util.Map;

@IFMLLoadingPlugin.Name("iceandfire")
@MCVersion("1.11.2")
@TransformerExclusions({ "com.github.alexthe666.iceandfire.access", "net.ilexiconn.llibrary.server.asm" })
@IFMLLoadingPlugin.SortingIndex(1002)
public class IceAndFireForgeLoading implements IFMLLoadingPlugin {
    public static boolean development;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { IceAndFireRuntimePatcher.class.getCanonicalName(), IceAndFireTransformer.class.getCanonicalName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        IceAndFireForgeLoading.development = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
