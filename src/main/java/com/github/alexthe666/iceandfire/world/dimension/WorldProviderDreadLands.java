package com.github.alexthe666.iceandfire.world.dimension;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModWorld;
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
        this.biomeProvider = new BiomeProviderSingle(ModWorld.DREADLANDS_BIOME);
        this.nether = false;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModWorld.DREADLANDS_DIM;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
        float cestialAngle = cameraEntity.world.getCelestialAngle(partialTicks);
        float f = MathHelper.clamp(MathHelper.cos(cestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0.2F, 1F);
        float bright = 1.0F * f;
        float f1 = 0.02F;
        float f2 = 0.25F;
        float f3 = 0.36F;
        return new Vec3d((double) f1 * bright, (double) f2 * bright, (double) f3 * bright);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.clamp(MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0.3F, 1F);
        float f1 = 0.71F;
        float f2 = 0.80F;
        float f3 = 0.81F;

        float f4 = 0.02F;
        float f5 = 0.25F;
        float f6 = 0.36F;
        float r = f4 - f * 0.1F;
        float g = f5 - f * 0.1F;
        float b = f6 - f * 0.1F;

        return new Vec3d((double) r, (double) g, (double) b);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderDreadLands(this.world, this.world.getSeed());
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return super.calculateCelestialAngle(worldTime, partialTicks);
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
    public net.minecraftforge.client.IRenderHandler getWeatherRenderer()
    {
        return (net.minecraftforge.client.IRenderHandler) IceAndFire.PROXY.getDreadlandsRender(1);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer()
    {
        return (net.minecraftforge.client.IRenderHandler) IceAndFire.PROXY.getDreadlandsRender(0);
    }

}