package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiIceAndFireConfig extends GuiConfig {
    public GuiIceAndFireConfig(GuiScreen parent) {
        super(parent, new ConfigElement(IceAndFire.config.getCategory("all")).getChildElements(), IceAndFire.MODID, false, false, "Ice And Fire Confg");
        titleLine2 = IceAndFire.config.getConfigFile().getAbsolutePath();
    }

   @Override
    public void onGuiClosed(){
        super.onGuiClosed();
    }
}