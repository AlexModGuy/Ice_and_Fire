package com.github.alexthe666.iceandfire.event;

import java.util.Random;

import com.github.alexthe666.iceandfire.structures.WorldGenFireDragonCave;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;

public class StructureGenerator implements IWorldGenerator {

	private static final WorldGenFireDragonCave FIRE_DRAGON_CAVE = new WorldGenFireDragonCave();
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkZ * 16) + random.nextInt(16);
		BlockPos height = getHeight(world, new BlockPos(x, 0, z));
		if (IceAndFire.CONFIG.generateDragonSkeletons) {
			if (BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.DRY) && BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.SANDY) && random.nextInt(250) == 0) {
				EntityFireDragon firedragon = new EntityFireDragon(world);
				firedragon.setPosition(x, height.getY() + 1, z);
				int dragonage = 10 + random.nextInt(100);
				firedragon.growDragon(dragonage);
				firedragon.setDeathStage(dragonage / 2);
				firedragon.modelDeadProgress = 20;
				firedragon.setModelDead(true);
				firedragon.rotationYaw = random.nextInt(360);
				if (!world.isRemote) {
					world.spawnEntityInWorld(firedragon);
				}
			}
		}
		if (IceAndFire.CONFIG.generateDragonDens) {
			boolean isHills = BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.HILLS) || BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.MOUNTAIN);
			if (!BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.COLD) && !BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.SNOWY) || isHills) {
				if(random.nextInt(isHills ? 180 : 360) == 0){
					int newY = 30 + (isHills ? random.nextInt(90) : random.nextInt(10));
					BlockPos pos = new BlockPos(x, newY, z);
					if(!world.canBlockSeeSky(pos)){
						FIRE_DRAGON_CAVE.generate(world, random, pos);
					}
				}
			}
		}
		if (IceAndFire.CONFIG.generateSilverOre) {
			for (int silverAmount = 0; silverAmount < 2; silverAmount++) {
				int oreHeight = random.nextInt(32);
				int xOre = (chunkX * 16) + random.nextInt(16);
				int zOre = (chunkZ * 16) + random.nextInt(16);
				new WorldGenMinable(ModBlocks.silverOre.getDefaultState(), 4 + random.nextInt(4)).generate(world, random, new BlockPos(xOre, oreHeight, zOre));
			}
		}
		if (IceAndFire.CONFIG.generateSapphireOre) {
			if (BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(height), Type.SNOWY)) {
				int count = 3 + random.nextInt(6);
				for (int sapphireAmount = 0; sapphireAmount < count; sapphireAmount++) {
					int oreHeight = random.nextInt(28) + 4;
					int xOre = (chunkX * 16) + random.nextInt(16);
					int zOre = (chunkZ * 16) + random.nextInt(16);
					BlockPos pos = new BlockPos(xOre, oreHeight, zOre);
					IBlockState state = world.getBlockState(pos);
					if (state.getBlock().isReplaceableOreGen(state, world, pos, BlockMatcher.forBlock(Blocks.STONE))) {
						world.setBlockState(pos, ModBlocks.sapphireOre.getDefaultState());
					}
				}
			}
		}
	}

	public static BlockPos getHeight(World world, BlockPos pos) {
		for (int y = 0; y < 256; y++) {
			BlockPos pos1 = pos.up(y);
			if (world.getBlockState(pos1.up()).getBlock() == Blocks.AIR && world.getBlockState(pos1.down()).getBlock() != Blocks.AIR) {
				return pos1;
			}
		}
		return pos;
	}
}
