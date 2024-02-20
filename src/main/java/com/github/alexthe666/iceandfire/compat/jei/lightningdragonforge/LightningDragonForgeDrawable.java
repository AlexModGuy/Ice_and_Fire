package com.github.alexthe666.iceandfire.compat.jei.lightningdragonforge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class LightningDragonForgeDrawable implements IDrawable {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/dragonforge_lightning.png");

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public void draw(@NotNull GuiGraphics ms, int xOffset, int yOffset) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexturedModalRect(ms, xOffset, yOffset, 3, 4, 170, 79);
        int scaledProgress = (Minecraft.getInstance().player.tickCount % 100) * 128 / 100;
        this.drawTexturedModalRect(ms, xOffset + 9, yOffset + 19, 0, 166, scaledProgress, 38);
    }

    public void drawTexturedModalRect(GuiGraphics ms, int x, int y, int textureX, int textureY, int width, int height) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        Matrix4f matrix4f = ms.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, x, y + height, 0).uv((float) (textureX) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y + height, 0).uv((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y, 0).uv((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
        bufferbuilder.vertex(matrix4f, x, y, 0).uv((float) (textureX) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
        tessellator.end();
    }
}