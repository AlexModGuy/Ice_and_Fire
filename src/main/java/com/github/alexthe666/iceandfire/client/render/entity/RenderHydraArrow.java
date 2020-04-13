package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityHydraArrow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderHydraArrow extends Render {
    private static final ResourceLocation TEXTURES = new ResourceLocation("iceandfire:textures/models/misc/hydra_arrow.png");

    public RenderHydraArrow(RenderManager render) {
        super(render);
    }

    public void doRender(EntityHydraArrow entity, double x, double y, double z, float yaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuffer();
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = (0 + i * 10) / 32.0F;
        float f3 = (5 + i * 10) / 32.0F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = (5 + i * 10) / 32.0F;
        float f7 = (10 + i * 10) / 32.0F;
        float f8 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f9 = entity.arrowShake - partialTicks;

        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f8, f8, f8);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f8, 0.0F, 0.0F);
        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex(f4, f6).endVertex();
        BufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex(f5, f6).endVertex();
        BufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex(f5, f7).endVertex();
        BufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex(f4, f7).endVertex();
        tessellator.draw();
        GL11.glNormal3f(-f8, 0.0F, 0.0F);
        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex(f4, f6).endVertex();
        BufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex(f5, f6).endVertex();
        BufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex(f5, f7).endVertex();
        BufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex(f4, f7).endVertex();
        tessellator.draw();

        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f8);
            BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            BufferBuilder.pos(-8.0D, -2.0D, 0.0D).tex(f, f2).endVertex();
            BufferBuilder.pos(8.0D, -2.0D, 0.0D).tex(f1, f2).endVertex();
            BufferBuilder.pos(8.0D, 2.0D, 0.0D).tex(f1, f3).endVertex();
            BufferBuilder.pos(-8.0D, 2.0D, 0.0D).tex(f, f3).endVertex();
            tessellator.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityHydraArrow arrow) {
        return TEXTURES;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityHydraArrow) entity);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks) {
        this.doRender((EntityHydraArrow) entity, x, y, z, f, partialTicks);
    }
}