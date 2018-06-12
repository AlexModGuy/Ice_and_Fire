package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.SirenEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleSirenAppearance extends ParticleMobAppearance {

    private EntityLivingBase entity;

    public ParticleSirenAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleMaxAge = 60;

    }


    public void onUpdate() {
        super.onUpdate();

        if (this.entity == null) {
            SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(Minecraft.getMinecraft().player, SirenEntityProperties.class);
            EntitySiren siren = new EntitySiren(this.world);
            if(sirenProps != null && sirenProps.getClosestSiren(Minecraft.getMinecraft().player.world, Minecraft.getMinecraft().player) != null){
                siren = sirenProps.getClosestSiren(Minecraft.getMinecraft().player.world, Minecraft.getMinecraft().player);
            }
            this.entity = siren;
        }
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (this.entity != null) {
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
            float f = 0.42553192F;
            float f1 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float f2 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.pushMatrix();
            float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * (float) Math.PI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f3);
            GlStateManager.translate(0.0F, 1.8F, 0.0F);
            GlStateManager.rotate(180.0F - entityIn.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(60.0F - 150.0F * f1 - entityIn.rotationPitch, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.8F, -1.5F);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            GlStateManager.rotate((entity.ticksExisted % 90) * 4, 0.0F, 1.0F, 0.0F);

            this.entity.rotationYaw = 0.0F;
            this.entity.rotationYawHead = 0.0F;
            this.entity.prevRotationYaw = 0.0F;
            this.entity.prevRotationYawHead = 0.0F;
            rendermanager.renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }
}
