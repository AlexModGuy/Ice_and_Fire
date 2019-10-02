package com.github.alexthe666.iceandfire.asm;

import com.github.alexthe666.iceandfire.patcher.IceAndFireRuntimePatcher;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.Name("iceandfire")
@IFMLLoadingPlugin.TransformerExclusions({"com.github.alexthe666.iceandfire.asm"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1002)
public class IceAndFirePlugin implements IFMLLoadingPlugin {

	public static boolean runtimeDeobfEnabled = false;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{IceAndFireRuntimePatcher.class.getName()};
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
		runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
