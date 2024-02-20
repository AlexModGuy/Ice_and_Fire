package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
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

import javax.annotation.Nullable;

public class ParticleDragonFlame extends TextureSheetParticle {

    private static final ResourceLocation DRAGONFLAME = new ResourceLocation("iceandfire:textures/particles/dragon_flame.png");
    private final float dragonSize;
    private final double initialX;
    private final double initialY;
    private final double initialZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private final int touchedTime = 0;
    private final float speedBonus;
    @Nullable
    private EntityDragonBase dragon;

    public ParticleDragonFlame(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float dragonSize) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.initialX = xCoordIn;
        this.initialY = yCoordIn;
        this.initialZ = zCoordIn;
        targetX = xCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        targetY = yCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        targetZ = zCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        this.setPos(x, y, z);
        this.dragonSize = dragonSize;
        this.speedBonus = random.nextFloat() * 0.015F;
    }

    public ParticleDragonFlame(ClientLevel world, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase, int startingAge) {
        this(world, x, y, z, motX, motY, motZ, Mth.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F));
        this.dragon = entityDragonBase;
        this.targetX = dragon.burnParticleX + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetY = dragon.burnParticleY + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetZ = dragon.burnParticleZ + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.x = x;
        this.y = y;
        this.z = z;
        this.age = startingAge;
    }


    @Override
    public int getLifetime() {
        return dragon == null ? 10 : 30;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        //TODO: use buffer stuff
        Vec3 inerp = renderInfo.getPosition();
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
        RenderSystem.setShaderTexture(0, DRAGONFLAME);
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

    @Override
    public void tick() {
        super.tick();

        if (dragon == null) {
            float distX = (float) (this.initialX - x);
            float distZ = (float) (this.initialZ - z);
            this.xd += distX * -0.01F * dragonSize * random.nextFloat();
            this.zd += distZ * -0.01F * dragonSize * random.nextFloat();
            this.yd += 0.015F * random.nextFloat();
        } else {
            double d2 = this.targetX - initialX;
            double d3 = this.targetY - initialY;
            double d4 = this.targetZ - initialZ;
            double dist = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            float speed = 0.015F + speedBonus;
            this.xd += d2 * speed;
            this.yd += d3 * speed;
            this.zd += d4 * speed;
            if (touchedTime > 3) {
                this.remove();
            }
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}
