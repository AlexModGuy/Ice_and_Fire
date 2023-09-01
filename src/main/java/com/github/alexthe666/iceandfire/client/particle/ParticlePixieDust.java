package com.github.alexthe666.iceandfire.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticlePixieDust extends TextureSheetParticle {
    private static final ResourceLocation PIXIE_DUST = new ResourceLocation("iceandfire:textures/particles/pixie_dust.png");
    float reddustParticleScale;

    public ParticlePixieDust(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected ParticlePixieDust(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float scale, float red, float green, float blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        float f = (float) Math.random() * 0.4F + 0.6F;
        this.rCol = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * red * f;
        this.gCol = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * green * f;
        this.bCol = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * blue * f;
        this.quadSize *= scale;
        this.reddustParticleScale = this.quadSize;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.lifetime = (int) ((float) this.lifetime * scale);
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 inerp = renderInfo.getPosition();
        float scaley = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
        scaley = Mth.clamp(scaley, 0.0F, 1.0F);
        this.quadSize = this.reddustParticleScale * scaley;

        float width = quadSize * 0.09F;
        if (age > this.getLifetime()) {
            this.remove();
        }

        Vec3 Vector3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - Vector3d.x());
        float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - Vector3d.y());
        float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - Vector3d.z());
        Quaternionf quaternion;
        if (this.roll == 0.0F) {
            quaternion = renderInfo.rotation();
        } else {
            quaternion = new Quaternionf(renderInfo.rotation());
            float f3 = Mth.lerp(partialTicks, this.oRoll, this.roll);
            quaternion.mul(Axis.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1 = quaternion.transform(vector3f1);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f = quaternion.transform(vector3f);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        RenderSystem.setShaderTexture(0, PIXIE_DUST);
        int j = this.getLightColor(partialTicks);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuilder();
        vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        vertexbuffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        Tesselator.getInstance().end();
    }


    @Override
    public int getLightColor(float partialTick) {
        return 240;
    }

    public void onUpdate() {
        this.xo = x;
        this.yo = y;
        this.zo = z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.move(this.xd, this.yd, this.zd);

        if (this.y == this.yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }

        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}
