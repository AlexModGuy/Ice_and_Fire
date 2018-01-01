package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticlePixieDust extends ParticleRedstone {

	public ParticlePixieDust(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float colorR, float colorG, float colorB) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, colorR, colorG, colorB);
		this.particleAlpha = 1F;
	}

	public int getBrightnessForRender(float f) {
		return 6000;
	}
}
