package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IndexPageButton extends GuiButton {

	public IndexPageButton(int id, int x, int y, String buttonText) {
		super(id, x, y, 160, 32, buttonText);
		this.width = 160;
		this.height = 32;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.visible) {
			FontRenderer fontrenderer = (FontRenderer) IceAndFire.PROXY.getFontRenderer();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			this.drawTexturedModalRect(this.x, this.y, 0, this.hovered ? 32 : 0, this.width, this.height);
			fontrenderer.drawString(this.displayString, (float) (this.x + this.width / 2 - fontrenderer.getStringWidth(this.displayString) / 2), (float) this.y + (this.height - 8) / 2, this.hovered ? 0XFAE67D : 0X303030, false);
		}
	}
}
