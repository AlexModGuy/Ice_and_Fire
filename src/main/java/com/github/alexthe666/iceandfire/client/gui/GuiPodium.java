package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPodium extends ContainerScreen<ContainerPodium> {

    private PlayerInventory playerInventory;
    private IInventory podiumInventory;
    public static final ResourceLocation PODUIM_TEXTURE = new ResourceLocation("iceandfire:textures/gui/podium.png");

    public GuiPodium(ContainerPodium container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.podiumInventory = container.podium;
        this.ySize = 133;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int p_230451_2_, int p_230451_3_) {
        if (podiumInventory != null) {
            String s = I18n.format("block.iceandfire.podium");
            this.getMinecraft().fontRenderer.drawString(ms, s, this.xSize / 2 - this.getMinecraft().fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        }
        this.getMinecraft().fontRenderer.drawString(ms, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }


    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderHoveredTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(PODUIM_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
    }

}