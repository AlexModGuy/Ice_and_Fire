package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleSirenMusic extends Particle {

    float noteParticleScale;
    float colorScale;

    public ParticleSirenMusic(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double color, double motionY, double motionZ) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, motionY, motionZ);
        this.colorScale = (float) color;
        this.particleRed = MathHelper.sin((colorScale / 24 + 0.0F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleGreen = MathHelper.sin((colorScale / 24 + 0.33333334F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleBlue = MathHelper.sin((colorScale / 24 + 0.6666667F) * ((float) Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleScale *= 1.75F;
        this.noteParticleScale = this.particleScale;
        this.particleMaxAge = 15;
        this.motionX = 0.05;
        this.motionY = motionY;
        this.motionZ = 0.05;
        this.setParticleTextureIndex(64);
    }

    public void onUpdate() {
        colorScale += 0.25;
        if (colorScale > 25) {
            colorScale = 0;
        }
        this.particleRed = MathHelper.sin((colorScale / 24 + 0.0F) * ((float) Math.PI * 2F)) * 0.5F + 0.35F;
        this.particleGreen = MathHelper.sin((colorScale / 24 + 0.33333334F) * ((float) Math.PI * 2F)) * 0.5F + 0.35F;
        this.particleBlue = MathHelper.sin((colorScale / 24 + 0.6666667F) * ((float) Math.PI * 2F)) * 0.5F + 0.35F;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }


    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.noteParticleScale * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }


}