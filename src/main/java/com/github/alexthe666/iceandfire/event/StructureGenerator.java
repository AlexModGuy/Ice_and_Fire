package com.github.alexthe666.iceandfire.event;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.github.alexthe666.iceandfire.structures.WorldUtils;

public class StructureGenerator implements IWorldGenerator{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenerator(new BlockPos(chunkX, 0, chunkZ));
			int e = chunkX + random.nextInt(16);
			int l = chunkZ + random.nextInt(16);
			BlockPos blockpos = new BlockPos(e, world.getChunksLowestHorizon(e, 1), l);
			
			for(float i = 0; i < 20; i += 0.5) {
				for(float j = 0; j < 20; j += 0.5)
					for(int k = 0; k < 70; k ++)
						WorldUtils.setBlock(world, (int)Math.floor(blockpos.getX() + Math.sin(j) * i), blockpos.getY() + k, (int)Math.floor(blockpos.getZ() + Math.sin(j) * i), glacierBlock(random), 0, 3);

		}
	}
	
	public Block glacierBlock(Random rand){
		int selection = rand.nextInt(100);
		if(selection >= 0 && selection <= 10){
			return Blocks.ice;
		}
		if(selection > 20 && selection <= 80){
			return Blocks.packed_ice;
		}
		if(selection > 90 && selection <= 100){
			return Blocks.gravel;
		}else{
			return Blocks.gravel;
		}
	}

}
