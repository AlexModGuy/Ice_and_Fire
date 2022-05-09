package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderFrozenState {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/block/frosted_ice_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/block/frosted_ice_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/block/frosted_ice_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/block/frosted_ice_3.png");

    public static void render(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int light) {
        float sideExpand = -0.125F;
        float sideExpandY = 0.325F;
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(-entity.getWidth() / 2F - sideExpand, 0, -entity.getWidth() / 2F - sideExpand,
            entity.getWidth() / 2F + sideExpand, entity.getHeight() + sideExpandY, entity.getWidth() / 2F + sideExpand);
        matrixStack.push();
        renderMovingAABB(axisalignedbb1, matrixStack, bufferIn, entity, light, 255);
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

    public static void renderMovingAABB(AxisAlignedBB boundingBox, MatrixStack stack, IRenderTypeBuffer bufferIn, LivingEntity entity, int light, int alpha) {
        RenderType rendertype = IafRenderType.getIce(getIceTexture(FrozenProperties.ticksUntilUnfrozen(entity)));
        IVertexBuilder vertexbuffer = bufferIn.getBuffer(rendertype);
        Matrix4f matrix4f = stack.getLast().getMatrix();
        float maxX = (float) boundingBox.maxX * 0.425F;
        float minX = (float) boundingBox.minX * 0.425F;
        float maxY = (float) boundingBox.maxY * 0.425F;
        float minY = (float) boundingBox.minY * 0.425F;
        float maxZ = (float) boundingBox.maxZ * 0.425F;
        float minZ = (float) boundingBox.minZ * 0.425F;

        float maxU = maxZ - minZ;
        float maxV = maxY - minY;
        float minU = minZ - maxZ;
        float minV = minY - maxY;
        // X+
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1.0F, 0.0F, 0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(1.0F, 0.0F, 0F).endVertex();

        // X-
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(-1.0F, 0.0F, 0.0F).endVertex();


        maxU = maxX - minX;
        maxV = maxY - minY;
        minU = minX - maxX;
        minV = minY - maxY;
        // Z-
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, -1.0F).endVertex();

        // Z+
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 0.0F, 1.0F).endVertex();


        maxU = maxZ - minZ;
        maxV = maxX - minX;
        minU = minZ - maxZ;
        minV = minX - maxX;
        // Y+
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        // Y-
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, minV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, alpha).tex(maxU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, alpha).tex(minU, maxV).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(0.0F, -1.0F, 0.0F).endVertex();
    }
}
