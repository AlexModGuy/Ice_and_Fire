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
import com.github.alexthe666.iceandfire.structures.StructureUtils;

public class StructureGenerator implements IWorldGenerator{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenerator(new BlockPos(chunkX, 10, chunkZ));
		if (BiomeDictionary.isBiomeOfType(biome, Type.PLAINS))
		{
			int k = chunkX + random.nextInt(16) + 8;
			int l = chunkZ+ random.nextInt(16) + 8;
			BlockPos blockpos1 = world.getHorizon(new BlockPos(k, 0, l));
			for(int i = 0; i <= 16; i++){
				if(world.getBlockState(blockpos1).getBlock() != Blocks.ice && world.getBlockState(blockpos1).getBlock() != Blocks.packed_ice){
				//StructureUtils.setBlock(world, blockpos1.getX(), blockpos1.getY() + i, blockpos1.getZ(), StructureUtils.iceOrPackedIce(), 0, 3);
				}
			}
		}
	}

}
