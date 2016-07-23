package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;

public class WorldGenFireDragonRoosts extends WorldGenerator {

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        burnGround(worldIn, rand, position, 8);
        return true;
    }

    public static void burnGround(World world, Random rand, BlockPos position, int radius){
        for (int i = 0; radius >= 0 && i < 3; ++i) {
            int j = radius + rand.nextInt(4);
            int k = (radius + rand.nextInt(4));
            int l = radius + rand.nextInt(4);
            float f = (float)(j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double)(f * f)) {
                    IBlockState state = world.getBlockState(blockpos);
                    if(state.getMaterial() == Material.GRASS){
                        world.setBlockState(blockpos, ModBlocks.charedGrass.getDefaultState());
                    }
                    else if(state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT){
                        world.setBlockState(blockpos, ModBlocks.charedDirt.getDefaultState());
                    }
                    else if(state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL){
                        world.setBlockState(blockpos, ModBlocks.charedGravel.getDefaultState());
                    }
                    else if(state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getUnlocalizedName().contains("cobblestone"))){
                        world.setBlockState(blockpos, ModBlocks.charedCobblestone.getDefaultState());
                    }
                    else if(state.getMaterial() == Material.ROCK){
                        world.setBlockState(blockpos, ModBlocks.charedStone.getDefaultState());
                    }
                    else if(state.getBlock() == Blocks.GRASS_PATH){
                        world.setBlockState(blockpos, ModBlocks.charedStone.getDefaultState());
                    }else{
                        world.setBlockState(blockpos, ModBlocks.ash.getDefaultState());
                    }
                }
            }
        }
    }
}
