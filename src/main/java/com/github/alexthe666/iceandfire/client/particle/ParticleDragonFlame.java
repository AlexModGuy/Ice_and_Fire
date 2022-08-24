package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class ParticleDragonFlame extends SpriteTexturedParticle {

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

    public ParticleDragonFlame(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float dragonSize) {
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

    public ParticleDragonFlame(ClientWorld world, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase, int startingAge) {
        this(world, x, y, z, motX, motY, motZ, MathHelper.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F));
        this.dragon = entityDragonBase;
        this.targetX = dragon.burnParticleX + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetY = dragon.burnParticleY + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetZ = dragon.burnParticleZ + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.x = x;
        this.y = y;
        this.z = z;
        this.age = startingAge;
    }


    public int getLifetime() {
        return dragon == null ? 10 : 30;
    }

    @Override
    public void render(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Vector3d inerp = renderInfo.getPosition();
        if (age > this.getLifetime()) {
            this.remove();
        }

        Vector3d Vector3d = renderInfo.getPosition();
        float f = (float) (MathHelper.lerp(partialTicks, this.xo, this.x) - Vector3d.x());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.yo, this.y) - Vector3d.y());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.zo, this.z) - Vector3d.z());
        Quaternion quaternion;
        if (this.roll == 0.0F) {
            quaternion = renderInfo.rotation();
        } else {
            quaternion = new Quaternion(renderInfo.rotation());
            float f3 = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
            quaternion.mul(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        Minecraft.getInstance().getTextureManager().bind(DRAGONFLAME);
        int j = this.getLightColor(partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuilder();
        vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE);
        vertexbuffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        vertexbuffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        Tessellator.getInstance().end();
    }

    public int getLightColor(float partialTick) {
        return 240;
    }

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
            double dist = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
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
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }
}
