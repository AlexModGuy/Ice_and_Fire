package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticleDreadPortal extends ParticleFlame {
    private static final ResourceLocation SNOWFLAKE = new ResourceLocation("iceandfire:textures/particles/snowflake_0.png");
    private static final ResourceLocation SNOWFLAKE_BIG = new ResourceLocation("iceandfire:textures/particles/snowflake_1.png");

    private boolean big;

    public ParticleDreadPortal(World world, double x, double y, double z, double motX, double motY, double motZ, float size) {
        super(world, x, y, z, motX, motY, motZ);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setParticleTextureIndex(rand.nextInt(8));
        big = rand.nextBoolean();
    }

    public void setParticleTextureIndex(int particleTextureIndex) {
        this.particleTextureIndexX = particleTextureIndex % 4;
        this.particleTextureIndexY = Math.min(2, particleTextureIndex / 4);
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        particleScale = 0.125F * (this.particleMaxAge - (this.particleAge));
        float f3 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f4 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f5 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        float width = particleScale * 0.09F;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[]{new Vec3d((double) (-rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (-rotationYZ * width - rotationXZ * width)), new Vec3d((double) (-rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (-rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (rotationYZ * width - rotationXZ * width))};
        GlStateManager.enableBlend();
        GlStateManager.enableNormalize();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        float f8 = (float) Math.PI / 2 + this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
        float f9 = MathHelper.cos(f8 * 0.5F);
        float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
        float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
        float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
        Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

        for (int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
        }
        if (big) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(SNOWFLAKE_BIG);
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(SNOWFLAKE);
        }
        GlStateManager.disableLighting();
        double currentMinU = 0.25D * particleTextureIndexX;
        double currentMaxU = currentMinU + 0.25D;
        double currentMinV = 0.25D * particleTextureIndexY;
        double currentMaxV = currentMinV + 0.25D;
        float alpha = 1;
        GL11.glPushMatrix();
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        buffer.pos((double) f3 + avec3d[0].x, (double) f4 + avec3d[0].y, (double) f5 + avec3d[0].z).tex(0, 1).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[1].x, (double) f4 + avec3d[1].y, (double) f5 + avec3d[1].z).tex(1, 1).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[2].x, (double) f4 + avec3d[2].y, (double) f5 + avec3d[2].z).tex(1, 0).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        buffer.pos((double) f3 + avec3d[3].x, (double) f4 + avec3d[3].y, (double) f5 + avec3d[3].z).tex(0, 0).color(1, 1, 1, alpha).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
        GL11.glPopMatrix();
        GlStateManager.disableBlend();
    }

    public int getBrightnessForRender(float partialTick) {
        return 240;
    }

    public int getFXLayer() {
        return 3;
    }


}
