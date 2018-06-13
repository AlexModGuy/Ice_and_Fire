package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiIceAndFireConfig extends GuiConfig {
    public GuiIceAndFireConfig(GuiScreen parent) {
        super(parent, new ConfigElement(IceAndFire.configFile.getCategory("all")).getChildElements(), IceAndFire.MODID, false, false, "Ice And Fire Confg");
        titleLine2 = IceAndFire.configFile.getConfigFile().getAbsolutePath();
    }

    @Override
    public void initGui() {
        super.initGui();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }
}