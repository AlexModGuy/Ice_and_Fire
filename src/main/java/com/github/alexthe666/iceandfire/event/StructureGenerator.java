package com.github.alexthe666.iceandfire.event;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;

public class StructureGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkX * 16) + random.nextInt(16);
		if (BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(world.getHeight(new BlockPos(x, 0, z))), Type.DRY) &&  BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(world.getHeight(new BlockPos(x, 0, z))), Type.SANDY) && random.nextInt(4) == 0) {
			System.out.println(world.getHeight(new BlockPos(x, 10, z)));
			EntityFireDragon firedragon = new EntityFireDragon(world);
			firedragon.setPosition(x, world.getHeight(new BlockPos(x, 0, z)).getY() + 1, z);
			int dragonage = 10 + random.nextInt(100);
			firedragon.growDragon(dragonage);
			firedragon.setDeathStage(dragonage / 2);
			firedragon.setModelDead(true);
			firedragon.modelDeadProgress = 20;
			firedragon.rotationYaw = -180 + random.nextInt(360);
			if(!world.isRemote){
				world.spawnEntityInWorld(firedragon);
			}
		}
	}
}
