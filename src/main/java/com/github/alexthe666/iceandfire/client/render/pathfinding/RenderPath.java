package com.github.alexthe666.iceandfire.client.render.pathfinding;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ConcurrentModificationException;
import java.util.function.Supplier;

public class RenderPath {
    public static final RenderBuffers renderBuffers = new RenderBuffers();
    private static final MultiBufferSource.BufferSource renderBuffer = renderBuffers.bufferSource();
    public static final Supplier<VertexConsumer> BORDER_LINE_RENDERER = () -> renderBuffer.getBuffer(MRenderTypes.customLineRenderer());
    public static final Supplier<VertexConsumer> PATH_RENDERER = () -> renderBuffer.getBuffer(MRenderTypes.customPathRenderer());
    public static final Supplier<VertexConsumer> PATH_TEXT_RENDERER = () -> renderBuffer.getBuffer(MRenderTypes.customPathTextRenderer());

    /**
     * Render debugging information for the pathfinding system.
     *
     * @param frame       entity movement weight.
     * @param matrixStack the matrix stack to apply to.
     */
    public static void debugDraw(final double frame, final PoseStack matrixStack) {
        if (Pathfinding.lastDebugNodesNotVisited.isEmpty() || Pathfinding.lastDebugNodesPath.isEmpty() || Pathfinding.lastDebugNodesVisited.isEmpty()) {
            return;
        }

        final Vec3 vec = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        final double dx = vec.x();
        final double dy = vec.y();
        final double dz = vec.z();

        //TODO: Switch to updated minecolonies code with synchronized

        matrixStack.pushPose();
        matrixStack.translate(-dx, -dy, -dz);

        try {
            for (final MNode n : Pathfinding.lastDebugNodesNotVisited) {
                debugDrawNode(n, 1.0F, 0F, 0F, matrixStack);
            }

            for (final MNode n : Pathfinding.lastDebugNodesVisited) {
                debugDrawNode(n, 0F, 0F, 1.0F, matrixStack);
            }

            for (final MNode n : Pathfinding.lastDebugNodesPath) {
                if (n.isReachedByWorker()) {
                    debugDrawNode(n, 1F, 0.4F, 0F, matrixStack);
                } else {
                    debugDrawNode(n, 0F, 1.0F, 0F, matrixStack);
                }
            }
        } catch (final ConcurrentModificationException exc) {
            IceAndFire.LOGGER.catching(exc);
        }

        matrixStack.popPose();
    }

    private static void debugDrawNode(final MNode n, final float r, final float g, final float b, final PoseStack matrixStack) {
        matrixStack.pushPose();
        matrixStack.translate((double) n.pos.getX() + 0.375, (double) n.pos.getY() + 0.375, (double) n.pos.getZ() + 0.375);

        final Entity entity = Minecraft.getInstance().getCameraEntity();
        final double dx = n.pos.getX() - entity.getX();
        final double dy = n.pos.getY() - entity.getY();
        final double dz = n.pos.getZ() - entity.getZ();
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) <= 5D) {
            renderDebugText(n, matrixStack);
        }

        matrixStack.scale(0.25F, 0.25F, 0.25F);

        final VertexConsumer vertexBuffer = PATH_RENDERER.get();

        final Matrix4f matrix4f = matrixStack.last().pose();
        //  X+
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();

        //  X-
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();

        //  Z-
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();

        //  Z+
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();

        //  Y+
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 1.0f, 1.0f).color(r, g, b, 1.0f).endVertex();

        //  Y-
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 0.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 0.0f).color(r, g, b, 1.0f).endVertex();
        vertexBuffer.vertex(matrix4f, 1.0f, 0.0f, 1.0f).color(r, g, b, 1.0f).endVertex();

        if (n.parent != null) {
            matrixStack.pushPose();
            final Matrix4f lineMatrix = matrixStack.last().pose();

            final float pdx = n.parent.pos.getX() - n.pos.getX() + 0.125f;
            final float pdy = n.parent.pos.getY() - n.pos.getY() + 0.125f;
            final float pdz = n.parent.pos.getZ() - n.pos.getZ() + 0.125f;

            final VertexConsumer buffer = BORDER_LINE_RENDERER.get();

            buffer.vertex(lineMatrix, 0.5f, 0.5f, 0.5f).color(0.75F, 0.75F, 0.75F, 1.0F).endVertex();
            buffer.vertex(lineMatrix, pdx / 0.25f, pdy / 0.25f, pdz / 0.25f).color(0.75F, 0.75F, 0.75F, 1.0F).endVertex();
            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    private static void renderDebugText(final MNode n, final PoseStack matrixStack) {
        final String s1 = String.format("F: %.3f [%d]", n.getCost(), n.getCounterAdded());
        final String s2 = String.format("G: %.3f [%d]", n.getScore(), n.getCounterVisited());
        final Font fontrenderer = Minecraft.getInstance().font;

        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.75F, 0.0F);

        final EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        matrixStack.mulPose(renderManager.cameraOrientation());
        matrixStack.scale(-0.014F, -0.014F, 0.014F);
        matrixStack.translate(0.0F, 18F, 0.0F);

        final int i = Math.max(fontrenderer.width(s1), fontrenderer.width(s2)) / 2;

        final Matrix4f matrix4f = matrixStack.last().pose();

        final VertexConsumer vertexBuffer = PATH_TEXT_RENDERER.get();
        vertexBuffer.vertex(matrix4f, (-i - 1), -5.0f, 0.0f).color(0.0F, 0.0F, 0.0F, 0.7F).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertexBuffer.vertex(matrix4f, (-i - 1), 12.0f, 0.0f).color(0.0F, 0.0F, 0.0F, 0.7F).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertexBuffer.vertex(matrix4f, (i + 1), 12.0f, 0.0f).color(0.0F, 0.0F, 0.0F, 0.7F).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertexBuffer.vertex(matrix4f, (i + 1), -5.0f, 0.0f).color(0.0F, 0.0F, 0.0F, 0.7F).normal(0.0f, 1.0f, 0.0f).endVertex();

        matrixStack.pushPose();

        final Matrix4f textMatrix4f = matrixStack.last().pose();
        final MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        matrixStack.translate(0.0F, -5F, -0.1F);
        fontrenderer.drawInBatch(s1, -fontrenderer.width(s1) / 2.0f, 0, 0xFFFFFFFF, false, textMatrix4f, buffer, false, 0, 15728880);
        matrixStack.translate(0.0F, 8F, -0.1F);
        fontrenderer.drawInBatch(s2, -fontrenderer.width(s2) / 2.0f, 0, 0xFFFFFFFF, false, textMatrix4f, buffer, false, 0, 15728880);

        matrixStack.translate(0.0F, -8F, -0.1F);
        fontrenderer.drawInBatch(s1, -fontrenderer.width(s1) / 2.0f, 0, 0xFFFFFFFF, false, textMatrix4f, buffer, false, 0, 15728880);
        matrixStack.translate(0.0F, 8F, -0.1F);
        fontrenderer.drawInBatch(s2, -fontrenderer.width(s2) / 2.0f, 0, 0xFFFFFFFF, false, textMatrix4f, buffer, false, 0, 15728880);
        buffer.endBatch();

        matrixStack.popPose();
        matrixStack.popPose();
    }


}
