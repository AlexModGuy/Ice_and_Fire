package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiPodium extends ContainerScreen<ContainerPodium> {

    public static final ResourceLocation PODUIM_TEXTURE = new ResourceLocation("iceandfire:textures/gui/podium.png");

    public GuiPodium(ContainerPodium container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.ySize = 133;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
        if (container != null) {
            String s = I18n.format("block.iceandfire.podium");
            this.getMinecraft().fontRenderer.drawString(ms, s, this.xSize / 2 - this.getMinecraft().fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        }
        this.getMinecraft().fontRenderer.drawString(ms, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(PODUIM_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }

}