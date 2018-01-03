package com.github.alexthe666.iceandfire.client.gui.bestiary;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ChangePageButton extends GuiButton {
	private final boolean right;
	public int lastpage = 1;
	private int page;

	public ChangePageButton(int id, int x, int y, boolean right, int bookpage) {
		super(id, x, y, 23, 13, "");
		this.right = right;
		page = bookpage;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.enabled) {
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
			int i = 0;
			int j = 64;
			if (flag) {
				i += 23;
			}

			if (!this.right) {
				j += 13;
			}

			this.drawTexturedModalRect(this.x, this.y, i, j, width, height);
		}
	}
}