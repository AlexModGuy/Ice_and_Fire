package com.github.alexthe666.iceandfire.client.gui.bestiary;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class PageButton extends GuiButton {
    private final boolean right;
    private int page;
    private int lastpage = 1;

    public PageButton(int id, int xPos, int yPos, boolean right, int bookpage) {
        super(id, xPos, yPos, 34, 24, "");
        this.right = right;
        page = bookpage;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled) {
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
            int i = 0;
            int j = 32;
            if (flag) {
                i += 23;
            }

            if (!this.right) {
                j += 13;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, width, height, 23, 13);
        }
    }
}