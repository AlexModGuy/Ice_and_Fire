package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderFrozenState {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/block/frosted_ice_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/block/frosted_ice_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/block/frosted_ice_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/block/frosted_ice_3.png");

    public static void render(LivingEntity entity, MatrixStack matrixStack) {
        float sideExpand = -0.125F;
        float sideExpandY = 0.325F;
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(-entity.getWidth() / 2F - sideExpand, 0, -entity.getWidth() / 2F - sideExpand,
            entity.getWidth() / 2F + sideExpand, entity.getHeight() + sideExpandY, entity.getWidth() / 2F + sideExpand);
        matrixStack.push();
        matrixStack.push();
        RenderSystem.enableDepthTest();
        Minecraft.getInstance().getTextureManager().bindTexture(getIceTexture(FrozenProperties.ticksUntilUnfrozen(entity)));
        renderMovingAABB(axisalignedbb1, matrixStack);
        RenderSystem.disableDepthTest();
        matrixStack.pop();
        matrixStack.pop();
    }

    private static ResourceLocation getIceTexture(int ticksFrozen) {
        if (ticksFrozen < 100) {
            if (ticksFrozen < 50) {
                if (ticksFrozen < 20) {
                    return TEXTURE_3;
                }
                return TEXTURE_2;
            }
            return TEXTURE_1;
        }
        return TEXTURE_0;
    }

    public static void renderMovingAABB(AxisAlignedBB boundingBox, MatrixStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        IVertexBuilder vertexbuffer = tessellator.getBuffer().getVertexBuilder();
        BufferBuilder buffer = tessellator.getBuffer();
        float f3 = 0;
        Matrix4f matrix4f = stack.getLast().getMatrix();
        float maxX = (float) boundingBox.maxX * 0.425F;
        float minX = (float) boundingBox.minX * 0.425F;
        float maxY = (float) boundingBox.maxY * 0.425F;
        float minY = (float) boundingBox.minY * 0.425F;
        float maxZ = (float) boundingBox.maxZ * 0.425F;
        float minZ = (float) boundingBox.minZ * 0.425F;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
    }
}
