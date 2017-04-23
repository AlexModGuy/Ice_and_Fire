package com.github.alexthe666.iceandfire.access;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.*;

import java.util.Map;

@MCVersion("1.10.2")
@TransformerExclusions({"com.github.alexthe666.iceandfire.access."})
public class IceAndFireForgeLoading implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{IceAndFireTransformer.class.getCanonicalName()};
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

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
