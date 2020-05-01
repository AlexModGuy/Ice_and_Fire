package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class ParticleDragonFrost extends ParticleFlame {

    private static final ResourceLocation SNOWFLAKE = new ResourceLocation("iceandfire:textures/particles/snowflake_0.png");
    private static final ResourceLocation SNOWFLAKE_BIG = new ResourceLocation("iceandfire:textures/particles/snowflake_1.png");
    private float dragonSize;
    private double initialX;
    private double initialY;
    private double initialZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int touchedTime = 0;
    private float speedBonus;
    @Nullable
    private EntityDragonBase dragon;
    private boolean big;

    @OnlyIn(Dist.CLIENT)
    public ParticleDragonFrost(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float dragonSize) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.particleMaxAge = 30;
        this.initialX = xCoordIn;
        this.initialY = yCoordIn;
        this.initialZ = zCoordIn;
        targetX = xCoordIn + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 1.75F * dragonSize);
        targetY = yCoordIn + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 1.75F * dragonSize);
        targetZ = zCoordIn + (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 1.75F * dragonSize);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.dragonSize = dragonSize;
        this.speedBonus = rand.nextFloat() * 0.015F;
        this.setParticleTextureIndex(rand.nextInt(8));
        big = rand.nextBoolean();
    }

    public ParticleDragonFrost(World world, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase, int startingAge) {
        this(world, x, y, z, motX, motY, motZ, MathHelper.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F));
        this.dragon = entityDragonBase;
        this.targetX = dragon.burnParticleX + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F;
        this.targetY = dragon.burnParticleY + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F;
        this.targetZ = dragon.burnParticleZ + (double) ((this.rand.nextFloat() - this.rand.nextFloat())) * 3.5F;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.particleAge = startingAge;
    }

    public void setParticleTextureIndex(int particleTextureIndex) {
        this.particleTextureIndexX = particleTextureIndex % 4;
        this.particleTextureIndexY = Math.min(2, particleTextureIndex / 4);
    }

    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (particleAge > (dragon == null ? 10 : 30)) {
            this.setExpired();
        }
        particleScale = 5F * dragonSize;

        float f3 = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float f4 = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float f5 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
        float distX = (float) (this.initialX - this.posX);
        float distZ = (float) (this.initialZ - this.posZ);
        float width = particleScale * 0.09F;
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[]{new Vec3d((double) (-rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (-rotationYZ * width - rotationXZ * width)), new Vec3d((double) (-rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (-rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width + rotationXY * width), (double) (rotationZ * width), (double) (rotationYZ * width + rotationXZ * width)), new Vec3d((double) (rotationX * width - rotationXY * width), (double) (-rotationZ * width), (double) (rotationYZ * width - rotationXZ * width))};
        GlStateManager.enableBlend();
        GlStateManager.enableNormalize();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
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
            Minecraft.getInstance().getTextureManager().bindTexture(SNOWFLAKE_BIG);
        } else {
            Minecraft.getInstance().getTextureManager().bindTexture(SNOWFLAKE);

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
        float f = 0;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);

        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public int getFXLayer() {
        return 3;
    }

    public void onUpdate() {
        super.onUpdate();
        if (dragon == null) {
            float distX = (float) (this.initialX - this.posX);
            float distZ = (float) (this.initialZ - this.posZ);
            this.motionX += distX * -0.01F * dragonSize * rand.nextFloat();
            this.motionZ += distZ * -0.01F * dragonSize * rand.nextFloat();
            this.motionY += 0.015F * rand.nextFloat();
        } else {
            double d2 = this.targetX - initialX;
            double d3 = this.targetY - initialY;
            double d4 = this.targetZ - initialZ;
            double dist = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            float speed = 0.015F + speedBonus;
            this.motionX += d2 * speed;
            this.motionY += d3 * speed;
            this.motionZ += d4 * speed;
            if (touchedTime > 3) {
                this.setExpired();
            }
        }
    }

    public void move(double x, double y, double z) {
        double d0 = y;
        double origX = x;
        double origZ = z;

        if (this.canCollide) {
            List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(x, y, z));

            for (AxisAlignedBB axisalignedbb : list) {
                y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list) {
                x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
            }

            this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list) {
                z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
        } else {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        }

        this.resetPositionToBB();
        this.onGround = d0 != y && d0 < 0.0D;

        if (origX != x) {
            this.motionX = 0.0D;
        }

        if (origZ != z) {
            this.motionZ = 0.0D;
        }
    }

}
