package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.materials.Material;

public class MaterialWeezer extends Material {

    public boolean hide = true;

    public MaterialWeezer(String weezer, int i, boolean b) {
        super(weezer, i, b);
    }

    public boolean isHidden() {
        return hide;
    }

    public void setVisible() {
        hide = false;
    }

    public void setHidden() {
        hide = true;
    }
}
