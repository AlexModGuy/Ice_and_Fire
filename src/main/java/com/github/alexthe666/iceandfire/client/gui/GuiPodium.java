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
    protected void func_230451_b_(MatrixStack ms, int p_230451_2_, int p_230451_3_) {
        if (podiumInventory != null) {
            String s = I18n.format("block.iceandfire.podium");
            this.getMinecraft().fontRenderer.func_238421_b_(ms, s, this.xSize / 2 - this.getMinecraft().fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        }
        this.getMinecraft().fontRenderer.func_238421_b_(ms, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }


    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(p_230430_1_);
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(PODUIM_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
    }

}