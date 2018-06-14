package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.inventory.ContainerHippocampus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHippocampus extends GuiContainer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");
	private final IInventory playerInventory;
	private final IInventory hippogryphInv;
	private final EntityHippocampus hippogryph;
	private float mousePosx;
	private float mousePosY;

	public GuiHippocampus(IInventory playerInv, EntityHippocampus hippogryph) {
		super(new ContainerHippocampus(hippogryph, Minecraft.getMinecraft().player));
		this.playerInventory = playerInv;
		this.hippogryphInv = hippogryph.hippocampusInventory;
		this.hippogryph = hippogryph;
		this.allowUserInput = false;
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(this.hippogryphInv.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (hippogryph.isChested()) {
			this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 5 * 18, 54);
		}
		GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, (float) (i + 51) - this.mousePosx, (float) (j + 75 - 50) - this.mousePosY, this.hippogryph);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mousePosx = (float) mouseX;
		this.mousePosY = (float) mouseY;
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

	}
}