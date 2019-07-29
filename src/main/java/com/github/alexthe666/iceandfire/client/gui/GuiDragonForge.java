package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiDragonForge extends GuiContainer {
	private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
	private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
	private final InventoryPlayer playerInventory;
	private IInventory tileFurnace;
	private boolean ice;

	public GuiDragonForge(InventoryPlayer playerInv, IInventory furnaceInv) {
		super(new ContainerDragonForge(playerInv, furnaceInv));
		this.playerInventory = playerInv;
		this.tileFurnace = furnaceInv;
		if(tileFurnace instanceof TileEntityDragonforge){
			this.ice = !((TileEntityDragonforge) tileFurnace).isFire;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (tileFurnace != null) {
			String s = I18n.format(ice ? "tile.iceandfire.dragonforge_ice_core.name" : "tile.iceandfire.dragonforge_fire_core.name");
			this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		}
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if(ice){
			this.mc.getTextureManager().bindTexture(TEXTURE_ICE);
		}else{
			this.mc.getTextureManager().bindTexture(TEXTURE_FIRE);
		}
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1 = this.func_175381_h(126);
		this.drawTexturedModalRect(k + 12, l + 23, 0, 166, i1, 38);
	}

	private int func_175381_h(int p_175381_1_) {
		int j = this.tileFurnace.getField(0);
		return j != 0 ? j * p_175381_1_ / ((TileEntityDragonforge) tileFurnace).getMaxCookTime() : 0;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}