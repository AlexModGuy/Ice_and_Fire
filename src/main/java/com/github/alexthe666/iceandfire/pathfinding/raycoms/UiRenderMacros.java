package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Our replacement for GuiComponent.
 * TODO: move to shared lib
 */
public class UiRenderMacros
{
    public static final double HALF_BIAS = 0.5;

    public static void drawLineRectGradient(final PoseStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int argbColorStart,
                                            final int argbColorEnd)
    {
        drawLineRectGradient(ps, x, y, w, h, argbColorStart, argbColorEnd, 1);
    }

    public static void drawLineRectGradient(final PoseStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int argbColorStart,
                                            final int argbColorEnd,
                                            final int lineWidth)
    {
        drawLineRectGradient(ps,
                x,
                y,
                w,
                h,
                (argbColorStart >> 16) & 0xff,
                (argbColorEnd >> 16) & 0xff,
                (argbColorStart >> 8) & 0xff,
                (argbColorEnd >> 8) & 0xff,
                argbColorStart & 0xff,
                argbColorEnd & 0xff,
                (argbColorStart >> 24) & 0xff,
                (argbColorEnd >> 24) & 0xff,
                lineWidth);
    }

    public static void drawLineRectGradient(final PoseStack ps,
                                            final int x,
                                            final int y,
                                            final int w,
                                            final int h,
                                            final int redStart,
                                            final int redEnd,
                                            final int greenStart,
                                            final int greenEnd,
                                            final int blueStart,
                                            final int blueEnd,
                                            final int alphaStart,
                                            final int alphaEnd,
                                            final int lineWidth)
    {
        if (lineWidth < 1 || (alphaStart == 0 && alphaEnd == 0))
        {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        if (alphaStart != 255 || alphaEnd != 255)
        {
            RenderSystem.enableBlend();
        }
        else
        {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        Tesselator.getInstance().end();

        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    public static void drawLineRect(final PoseStack ps, final int x, final int y, final int w, final int h, final int argbColor)
    {
        drawLineRect(ps, x, y, w, h, argbColor, 1);
    }

    public static void drawLineRect(final PoseStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int argbColor,
                                    final int lineWidth)
    {
        drawLineRect(ps,
                x,
                y,
                w,
                h,
                (argbColor >> 16) & 0xff,
                (argbColor >> 8) & 0xff,
                argbColor & 0xff,
                (argbColor >> 24) & 0xff,
                lineWidth);
    }

    public static void drawLineRect(final PoseStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int red,
                                    final int green,
                                    final int blue,
                                    final int alpha,
                                    final int lineWidth)
    {
        if (lineWidth < 1 || alpha == 0)
        {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        if (alpha != 255)
        {
            RenderSystem.enableBlend();
        }
        else
        {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).endVertex();
        Tesselator.getInstance().end();

        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w - lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + lineWidth, y + h - lineWidth, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    public static void fill(final PoseStack ps, final int x, final int y, final int w, final int h, final int argbColor)
    {
        fill(ps, x, y, w, h, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void fill(final PoseStack ps,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int red,
                            final int green,
                            final int blue,
                            final int alpha)
    {
        if (alpha == 0)
        {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        if (alpha != 255)
        {
            RenderSystem.enableBlend();
        }
        else
        {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }


    public static void fillGradient(final PoseStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int argbColorStart,
                                    final int argbColorEnd)
    {
        fillGradient(ps,
                x,
                y,
                w,
                h,
                (argbColorStart >> 16) & 0xff,
                (argbColorEnd >> 16) & 0xff,
                (argbColorStart >> 8) & 0xff,
                (argbColorEnd >> 8) & 0xff,
                argbColorStart & 0xff,
                argbColorEnd & 0xff,
                (argbColorStart >> 24) & 0xff,
                (argbColorEnd >> 24) & 0xff);
    }

    public static void fillGradient(final PoseStack ps,
                                    final int x,
                                    final int y,
                                    final int w,
                                    final int h,
                                    final int redStart,
                                    final int redEnd,
                                    final int greenStart,
                                    final int greenEnd,
                                    final int blueStart,
                                    final int blueEnd,
                                    final int alphaStart,
                                    final int alphaEnd)
    {
        if (alphaStart == 0 && alphaEnd == 0)
        {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        if (alphaStart != 255 || alphaEnd != 255)
        {
            RenderSystem.enableBlend();
        }
        else
        {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    public static void hLine(final PoseStack ps, final int x, final int xEnd, final int y, final int argbColor)
    {
        line(ps, x, y, xEnd, y, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void hLine(final PoseStack ps,
                             final int x,
                             final int xEnd,
                             final int y,
                             final int red,
                             final int green,
                             final int blue,
                             final int alpha)
    {
        line(ps, x, y, xEnd, y, red, green, blue, alpha);
    }

    public static void vLine(final PoseStack ps, final int x, final int y, final int yEnd, final int argbColor)
    {
        line(ps, x, y, x, yEnd, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void vLine(final PoseStack ps,
                             final int x,
                             final int y,
                             final int yEnd,
                             final int red,
                             final int green,
                             final int blue,
                             final int alpha)
    {
        line(ps, x, y, x, yEnd, red, green, blue, alpha);
    }

    public static void line(final PoseStack ps, final int x, final int y, final int xEnd, final int yEnd, final int argbColor)
    {
        line(ps, x, y, xEnd, yEnd, (argbColor >> 16) & 0xff, (argbColor >> 8) & 0xff, argbColor & 0xff, (argbColor >> 24) & 0xff);
    }

    public static void line(final PoseStack ps,
                            final int x,
                            final int y,
                            final int xEnd,
                            final int yEnd,
                            final int red,
                            final int green,
                            final int blue,
                            final int alpha)
    {
        if (alpha == 0)
        {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        if (alpha != 255)
        {
            RenderSystem.enableBlend();
        }
        else
        {
            RenderSystem.disableBlend();
        }

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, xEnd, yEnd, 0).color(red, green, blue, alpha).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    public static void blit(final PoseStack ps, final ResourceLocation rl, final int x, final int y, final int w, final int h)
    {
        blit(ps, rl, x, y, w, h, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public static void blit(final PoseStack ps,
                            final ResourceLocation rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int u,
                            final int v,
                            final int mapW,
                            final int mapH)
    {
        blit(ps, rl, x, y, w, h, (float) u / mapW, (float) v / mapH, (float) (u + w) / mapW, (float) (v + h) / mapH);
    }

    public static void blit(final PoseStack ps,
                            final ResourceLocation rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final int u,
                            final int v,
                            final int uW,
                            final int vH,
                            final int mapW,
                            final int mapH)
    {
        blit(ps, rl, x, y, w, h, (float) u / mapW, (float) v / mapH, (float) (u + uW) / mapW, (float) (v + vH) / mapH);
    }

    public static void blit(final PoseStack ps,
                            final ResourceLocation rl,
                            final int x,
                            final int y,
                            final int w,
                            final int h,
                            final float uMin,
                            final float vMin,
                            final float uMax,
                            final float vMax)
    {
        Minecraft.getInstance().getTextureManager().bindForSetup(rl);
        RenderSystem.setShaderTexture(0, rl);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        final Matrix4f m = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(m, x, y, 0).uv(uMin, vMin).endVertex();
        buffer.vertex(m, x, y + h, 0).uv(uMin, vMax).endVertex();
        buffer.vertex(m, x + w, y + h, 0).uv(uMax, vMax).endVertex();
        buffer.vertex(m, x + w, y, 0).uv(uMax, vMin).endVertex();
        Tesselator.getInstance().end();
    }

    /**
     * Draws texture without scaling so one texel is one pixel, using repeatable texture center.
     * TODO: Nightenom - rework to better algoritm from pgr, also texture extensions?
     *
     * @param ps            MatrixStack
     * @param rl            image ResLoc
     * @param x             start target coords [pixels]
     * @param y             start target coords [pixels]
     * @param width         target rendering box [pixels]
     * @param height        target rendering box [pixels]
     * @param u             texture start offset [texels]
     * @param v             texture start offset [texels]
     * @param uWidth        texture rendering box [texels]
     * @param vHeight       texture rendering box [texels]
     * @param textureWidth  texture file size [texels]
     * @param textureHeight texture file size [texels]
     * @param uRepeat       offset relative to u, v [texels], smaller than uWidth
     * @param vRepeat       offset relative to u, v [texels], smaller than vHeight
     * @param repeatWidth   size of repeatable box in texture [texels], smaller than or equal uWidth - uRepeat
     * @param repeatHeight  size of repeatable box in texture [texels], smaller than or equal vHeight - vRepeat
     */
    protected static void blitRepeatable(final PoseStack ps,
                                         final ResourceLocation rl,
                                         final int x, final int y,
                                         final int width, final int height,
                                         final int u, final int v,
                                         final int uWidth, final int vHeight,
                                         final int textureWidth, final int textureHeight,
                                         final int uRepeat, final int vRepeat,
                                         final int repeatWidth, final int repeatHeight)
    {
        if (uRepeat < 0 || vRepeat < 0 || uRepeat >= uWidth || vRepeat >= vHeight || repeatWidth < 1 || repeatHeight < 1
                || repeatWidth > uWidth - uRepeat || repeatHeight > vHeight - vRepeat)
        {
            throw new IllegalArgumentException("Repeatable box is outside of texture box");
        }

        final int repeatCountX = Math.max(1, Math.max(0, width - (uWidth - repeatWidth)) / repeatWidth);
        final int repeatCountY = Math.max(1, Math.max(0, height - (vHeight - repeatHeight)) / repeatHeight);

        final Matrix4f mat = ps.last().pose();
        final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(Mode.TRIANGLES, DefaultVertexFormat.POSITION_TEX);

        // main
        for (int i = 0; i < repeatCountX; i++)
        {
            final int uAdjust = i == 0 ? 0 : uRepeat;
            final int xStart = x + uAdjust + i * repeatWidth;
            final int w = Math.min(repeatWidth + uRepeat - uAdjust, width - (uWidth - uRepeat - repeatWidth));
            final float minU = (float) (u + uAdjust) / textureWidth;
            final float maxU = (float) (u + uAdjust + w) / textureWidth;

            for (int j = 0; j < repeatCountY; j++)
            {
                final int vAdjust = j == 0 ? 0 : vRepeat;
                final int yStart = y + vAdjust + j * repeatHeight;
                final int h = Math.min(repeatHeight + vRepeat - vAdjust, height - (vHeight - vRepeat - repeatHeight));
                final float minV = (float) (v + vAdjust) / textureHeight;
                final float maxV = (float) (v + vAdjust + h) / textureHeight;

                populateBlitTriangles(buffer, mat, xStart, xStart + w, yStart, yStart + h, minU, maxU, minV, maxV);
            }
        }

        final int xEnd = x + Math.min(uRepeat + repeatCountX * repeatWidth, width - (uWidth - uRepeat - repeatWidth));
        final int yEnd = y + Math.min(vRepeat + repeatCountY * repeatHeight, height - (vHeight - vRepeat - repeatHeight));
        final int uLeft = width - (xEnd - x);
        final int vLeft = height - (yEnd - y);
        final float restMinU = (float) (u + uWidth - uLeft) / textureWidth;
        final float restMaxU = (float) (u + uWidth) / textureWidth;
        final float restMinV = (float) (v + vHeight - vLeft) / textureHeight;
        final float restMaxV = (float) (v + vHeight) / textureHeight;

        // bot border
        for (int i = 0; i < repeatCountX; i++)
        {
            final int uAdjust = i == 0 ? 0 : uRepeat;
            final int xStart = x + uAdjust + i * repeatWidth;
            final int w = Math.min(repeatWidth + uRepeat - uAdjust, width - uLeft);
            final float minU = (float) (u + uAdjust) / textureWidth;
            final float maxU = (float) (u + uAdjust + w) / textureWidth;

            populateBlitTriangles(buffer, mat, xStart, xStart + w, yEnd, yEnd + vLeft, minU, maxU, restMinV, restMaxV);
        }

        // left border
        for (int j = 0; j < repeatCountY; j++)
        {
            final int vAdjust = j == 0 ? 0 : vRepeat;
            final int yStart = y + vAdjust + j * repeatHeight;
            final int h = Math.min(repeatHeight + vRepeat - vAdjust, height - vLeft);
            float minV = (float) (v + vAdjust) / textureHeight;
            float maxV = (float) (v + vAdjust + h) / textureHeight;

            populateBlitTriangles(buffer, mat, xEnd, xEnd + uLeft, yStart, yStart + h, restMinU, restMaxU, minV, maxV);
        }

        // bot left corner
        populateBlitTriangles(buffer, mat, xEnd, xEnd + uLeft, yEnd, yEnd + vLeft, restMinU, restMaxU, restMinV, restMaxV);

        Minecraft.getInstance().getTextureManager().bindForSetup(rl);
        RenderSystem.setShaderTexture(0, rl);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator.getInstance().end();
    }

    public static void populateFillTriangles(final Matrix4f m,
                                             final BufferBuilder buffer,
                                             final int x,
                                             final int y,
                                             final int w,
                                             final int h,
                                             final int red,
                                             final int green,
                                             final int blue,
                                             final int alpha)
    {
        buffer.vertex(m, x, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(red, green, blue, alpha).endVertex();
    }

    public static void populateFillGradientTriangles(final Matrix4f m,
                                                     final BufferBuilder buffer,
                                                     final int x,
                                                     final int y,
                                                     final int w,
                                                     final int h,
                                                     final int redStart,
                                                     final int redEnd,
                                                     final int greenStart,
                                                     final int greenEnd,
                                                     final int blueStart,
                                                     final int blueEnd,
                                                     final int alphaStart,
                                                     final int alphaEnd)
    {
        buffer.vertex(m, x, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x + w, y, 0).color(redStart, greenStart, blueStart, alphaStart).endVertex();
        buffer.vertex(m, x, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(redEnd, greenEnd, blueEnd, alphaEnd).endVertex();
    }

    public static void populateBlitTriangles(final BufferBuilder buffer,
                                             final Matrix4f mat,
                                             final float xStart,
                                             final float xEnd,
                                             final float yStart,
                                             final float yEnd,
                                             final float uMin,
                                             final float uMax,
                                             final float vMin,
                                             final float vMax)
    {
        buffer.vertex(mat, xStart, yStart, 0).uv(uMin, vMin).endVertex();
        buffer.vertex(mat, xStart, yEnd, 0).uv(uMin, vMax).endVertex();
        buffer.vertex(mat, xEnd, yStart, 0).uv(uMax, vMin).endVertex();
        buffer.vertex(mat, xEnd, yStart, 0).uv(uMax, vMin).endVertex();
        buffer.vertex(mat, xStart, yEnd, 0).uv(uMin, vMax).endVertex();
        buffer.vertex(mat, xEnd, yEnd, 0).uv(uMax, vMax).endVertex();
    }

    /**
     * Render an entity on a GUI.
     * @param poseStack matrix
     * @param x horizontal center position
     * @param y vertical bottom position
     * @param scale scaling factor
     * @param headYaw adjusts look rotation
     * @param yaw adjusts body rotation
     * @param pitch adjusts look rotation
     * @param entity the entity to render
     */
    public static void drawEntity(final PoseStack poseStack, final int x, final int y, final double scale,
                                  final float headYaw, final float yaw, final float pitch, final Entity entity)
    {
        // INLINE: vanilla from InventoryScreen
        final LivingEntity livingEntity = (entity instanceof LivingEntity) ? (LivingEntity) entity : null;
        final Minecraft mc = Minecraft.getInstance();
        if (entity.level() == null) return; // this was entity.setLevel, not sure why cuz sus, dont care if entity has no level
        poseStack.pushPose();
        poseStack.translate((float) x, (float) y, 1050.0F);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        poseStack.translate(0.0D, 0.0D, 1000.0D);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        final Quaternionf pitchRotation = Axis.XP.rotationDegrees(pitch);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(pitchRotation);
        final float oldYaw = entity.getYRot();
        final float oldPitch = entity.getXRot();
        final float oldYawOffset = livingEntity == null ? 0F : livingEntity.yBodyRot;
        final float oldPrevYawHead = livingEntity == null ? 0F : livingEntity.yHeadRotO;
        final float oldYawHead = livingEntity == null ? 0F : livingEntity.yHeadRot;
        entity.setYRot(180.0F + (float) headYaw);
        entity.setXRot(-pitch);
        if (livingEntity != null)
        {
            livingEntity.yBodyRot = 180.0F + yaw;
            livingEntity.yHeadRot = entity.getYRot();
            livingEntity.yHeadRotO = entity.getYRot();
        }
        Lighting.setupForEntityInInventory();
        final EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        pitchRotation.conjugate();
        dispatcher.overrideCameraOrientation(pitchRotation);
        dispatcher.setRenderShadow(false);
        final MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, buffers, 0x00F000F0));
        buffers.endBatch();
        dispatcher.setRenderShadow(true);
        entity.setYRot(oldYaw);
        entity.setXRot(oldPitch);
        if (livingEntity != null)
        {
            livingEntity.yBodyRot = oldYawOffset;
            livingEntity.yHeadRotO = oldPrevYawHead;
            livingEntity.yHeadRot = oldYawHead;
        }
        poseStack.popPose();
        Lighting.setupFor3DItems();
    }
}
