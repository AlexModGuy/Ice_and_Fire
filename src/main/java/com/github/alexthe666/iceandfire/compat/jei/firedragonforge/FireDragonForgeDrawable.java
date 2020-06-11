package com.github.alexthe666.iceandfire.compat.jei.firedragonforge;

import com.mojang.blaze3d.platform.GlStateManager;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class FireDragonForgeDrawable implements IDrawable {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public void draw(int xOffset, int yOffset) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(xOffset, yOffset, 3, 4, 170, 79);
        int scaledProgress = (Minecraft.getInstance().player.ticksExisted % 100) * 128 / 100;
        this.drawTexturedModalRect(xOffset + 9, yOffset + 19, 0, 166, scaledProgress, 38);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) 0).tex((float) ((float) (textureX + 0) * 0.00390625F), (float) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) 0).tex((float) ((float) (textureX + width) * 0.00390625F), (float) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) 0).tex((float) ((float) (textureX + width) * 0.00390625F), (float) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) 0).tex((float) ((float) (textureX + 0) * 0.00390625F), (float) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}