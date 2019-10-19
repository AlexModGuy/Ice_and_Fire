package com.github.alexthe666.iceandfire.client.render;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector4f;
import java.lang.reflect.Field;
import java.util.Random;

public class RenderDreadlandsAurora extends IRenderHandler {
    private static final ResourceLocation AURORA_TEXTURES = new ResourceLocation("iceandfire:textures/environment/dread_aurora.png");


    private static Field CLOUD_TICK_COUNTER;
    private static Field SKY_VBO;
    private static Field SKY_2_VBO;
    private static Field STAR_VBO;
    private static Field SKY_LIST;
    private static Field SKY_2_LIST;
    private static Field STAR_LIST;

    private int skyboxList = -1;

    static {
        CLOUD_TICK_COUNTER = ReflectionHelper.findField(RenderGlobal.class, "cloudTickCounter", "field_72773_u");
        SKY_VBO = ReflectionHelper.findField(RenderGlobal.class, "skyVBO", "field_175012_t");
        SKY_2_VBO = ReflectionHelper.findField(RenderGlobal.class, "sky2VBO", "field_175011_u");
        STAR_VBO = ReflectionHelper.findField(RenderGlobal.class, "starVBO", "field_175013_s");
        SKY_LIST = ReflectionHelper.findField(RenderGlobal.class, "glSkyList", "field_72771_w");
        SKY_2_LIST = ReflectionHelper.findField(RenderGlobal.class, "glSkyList2", "field_72781_x");
        STAR_LIST = ReflectionHelper.findField(RenderGlobal.class, "starGLCallList", "field_72772_v");
    }

    
    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        this.renderClouds(mc);
    }

    public void renderClouds(Minecraft mc) {
        if (this.skyboxList == -1) {
            GlStateManager.pushMatrix();
            this.skyboxList = GLAllocation.generateDisplayLists(1);
            GlStateManager.glNewList(this.skyboxList, GL11.GL_COMPILE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            double scale = 1.0 / 3.0;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-8.0, 8.0, -8.0).tex(0.0, 0.5).endVertex();
            buffer.pos(8.0, 8.0, -8.0).tex(scale, 0.5).endVertex();
            buffer.pos(8.0, -8.0, -8.0).tex(scale, 1.0).endVertex();
            buffer.pos(-8.0, -8.0, -8.0).tex(0.0, 1.0).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-8.0, 8.0, 8.0).tex(scale * 3.0F, 0.5).endVertex();
            buffer.pos(8.0, 8.0, 8.0).tex(scale * 2.0F, 0.5).endVertex();
            buffer.pos(8.0, -8.0, 8.0).tex(scale * 2.0F, 1.0).endVertex();
            buffer.pos(-8.0, -8.0, 8.0).tex(scale * 3.0F, 1.0).endVertex();
            tessellator.draw();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-8.0, 8.0, -8.0).tex(1.0, 0.0).endVertex();
            buffer.pos(-8.0, 8.0, 8.0).tex(1.0 - scale, 0.0).endVertex();
            buffer.pos(-8.0, -8.0, 8.0).tex(1.0 - scale, 0.5).endVertex();
            buffer.pos(-8.0, -8.0, -8.0).tex(1.0, 0.5).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(8.0, 8.0, -8.0).tex(scale, 0.5).endVertex();
            buffer.pos(8.0, 8.0, 8.0).tex(scale * 2.0F, 0.5).endVertex();
            buffer.pos(8.0, -8.0, 8.0).tex(scale * 2.0F, 1.0).endVertex();
            buffer.pos(8.0, -8.0, -8.0).tex(scale, 1.0).endVertex();
            tessellator.draw();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-8.0, 8.0, 8.0).tex(scale * 2.0F, 0.0).endVertex();
            buffer.pos(8.0, 8.0, 8.0).tex(scale * 2.0F, 0.5).endVertex();
            buffer.pos(8.0, 8.0, -8.0).tex(scale, 0.5).endVertex();
            buffer.pos(-8.0, 8.0, -8.0).tex(scale, 0.0).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-8.0, -8.0, 8.0).tex(scale, 0.5).endVertex();
            buffer.pos(8.0, -8.0, 8.0).tex(scale, 0.0).endVertex();
            buffer.pos(8.0, -8.0, -8.0).tex(0.0, 0.0).endVertex();
            buffer.pos(-8.0, -8.0, -8.0).tex(0.0, 0.5).endVertex();
            tessellator.draw();

            GlStateManager.glEndList();
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();

        mc.renderEngine.bindTexture(AURORA_TEXTURES);

        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableFog();

        float partialTicks = LLibrary.PROXY.getPartialTicks();
        GlStateManager.scale(9.0F, 9.0F, 9.0F);

        float color = 1.0F - mc.world.getStarBrightness(partialTicks) * 1.6F;

        GlStateManager.rotate((this.getCloudTickCounter() + partialTicks) * 0.005F, 0.0F, 1.0F, 0.0F);
        GlStateManager.color(color, color, color, color * 1.0F);

        GlStateManager.callList(this.skyboxList);

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private net.minecraft.client.renderer.vertex.VertexBuffer getSkyVBO() {
        try {
            return (net.minecraft.client.renderer.vertex.VertexBuffer) SKY_VBO.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private net.minecraft.client.renderer.vertex.VertexBuffer getSky2VBO() {
        try {
            return (net.minecraft.client.renderer.vertex.VertexBuffer) SKY_2_VBO.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private net.minecraft.client.renderer.vertex.VertexBuffer getStarVBO() {
        try {
            return (net.minecraft.client.renderer.vertex.VertexBuffer) STAR_VBO.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getSkyCallist() {
        try {
            return (Integer) SKY_LIST.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getSky2CallList() {
        try {
            return (Integer) SKY_2_LIST.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getStarCallList() {
        try {
            return (Integer) STAR_LIST.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getCloudTickCounter() {
        try {
            return (Integer) CLOUD_TICK_COUNTER.get(Minecraft.getMinecraft().renderGlobal);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
}