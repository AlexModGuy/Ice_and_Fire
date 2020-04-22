package com.github.alexthe666.iceandfire.world.dimension;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WorldProviderDreadLands extends WorldProvider {

    @Override
    public void init() {
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderSingle(IafWorldRegistry.DREADLANDS_BIOME);
        this.nether = false;
    }

    @Override
    public DimensionType getDimensionType() {
        return IafWorldRegistry.DREADLANDS_DIM;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
        float cestialAngle = cameraEntity.world.getCelestialAngle(partialTicks);
        float f = MathHelper.clamp(MathHelper.cos(cestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0.2F, 1F);
        float transitional = 1.25F * (f - 0.2F);
        float nightR = 0.004F;
        float nightG = 0.05F;
        float nightB = 0.072F;
        float dayR = 0.78F;
        float dayG = 0.8F;
        float dayB = 0.81F;
        return new Vec3d((double) nightR + (dayR - nightR) * transitional, (double) nightG + (dayG - nightG) * transitional, (double) nightB + (dayB - nightB) * transitional);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.clamp(MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0.3F, 1F);
        float dayR = 0.71F;
        float dayG = 0.80F;
        float dayB = 0.81F;

        float nightR = 0.02F;
        float nightG = 0.25F;
        float nightB = 0.36F;

        float transitional = 1.25F * (f - 0.2F);
        return new Vec3d((double) nightR + (dayR - nightR) * transitional, (double) nightG + (dayG - nightG) * transitional, (double) nightB + (dayB - nightB) * transitional);
    }

    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int vanillaTime = (int) (worldTime % 24000L);
        float f;
        if(vanillaTime < 2200){
            int spedUpTime = (int) (worldTime * 6 % 24000.0F);
            f = ((float) spedUpTime + partialTicks) / 24000.0F - 0.25F;
        }else{
            int spedUpTime = (int) (((worldTime * 0.49583333333F) + 12100) % 24000L);
            f = ((float) spedUpTime + partialTicks) / 24000.0F - 0.25F;
        }

        if (f < 0.0F) {
            f++;
        }

        if (f > 1.0F) {
            f--;
        }
        float f1 = 1.0F - (float) ((Math.cos((double) f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }


    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderDreadLands(this.world, this.world.getSeed());
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getWeatherRenderer() {
        return (net.minecraftforge.client.IRenderHandler) IceAndFire.PROXY.getDreadlandsRender(1);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer() {
        return (net.minecraftforge.client.IRenderHandler) IceAndFire.PROXY.getDreadlandsRender(0);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getCloudRenderer() {
        return (net.minecraftforge.client.IRenderHandler) IceAndFire.PROXY.getDreadlandsRender(2);
    }

}