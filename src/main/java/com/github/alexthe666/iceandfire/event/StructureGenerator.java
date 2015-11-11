package com.github.alexthe666.iceandfire.event;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.github.alexthe666.iceandfire.structures.WorldUtils;

public class StructureGenerator implements IWorldGenerator{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenerator(new BlockPos(chunkX, 0, chunkZ));
			int e = chunkX + random.nextInt(16) + 8;
			int l = chunkZ+ random.nextInt(16) + 8;
			BlockPos blockpos2 = new BlockPos(e, 0, l);
			BlockPos blockpos = world.getHorizon(blockpos2);

			for(float i = 0; i < 20; i += 0.5) {
				for(float j = 0; j < 2 * 9; j += 0.5)
					for(int k = 0; k < 70 + random.nextInt(20); k ++)
						WorldUtils.setBlock(world, (int)Math.floor(blockpos.getX() + Math.sin(j) * i), blockpos.getY() + k, (int)Math.floor(blockpos.getZ() + Math.cos(j) * i), Blocks.ice, 0, 3);

		}
	}

}
