package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenMyrmexHive extends WorldGenerator {

    private static final IBlockState DESERT_RESIN = ModBlocks.myrmex_resin.getDefaultState();
    private static final IBlockState STICKY_DESERT_RESIN = ModBlocks.myrmex_resin_sticky.getDefaultState();
    private static final IBlockState JUNGLE_RESIN = ModBlocks.myrmex_resin.getDefaultState().withProperty(BlockMyrmexResin.VARIANT, BlockMyrmexResin.EnumType.JUNGLE);
    private static final IBlockState STICKY_JUNGLE_RESIN = ModBlocks.myrmex_resin_sticky.getDefaultState().withProperty(BlockMyrmexResin.VARIANT, BlockMyrmexResin.EnumType.JUNGLE);

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int down = Math.max(15, position.getY() - 20 + rand.nextInt(10));
        BlockPos undergroundPos = new BlockPos(position.getX(), down, position.getZ());
        generateMainRoom(worldIn, rand, undergroundPos);
        return false;
    }

    private void generateMainRoom(World world, Random rand, BlockPos position) {
        IBlockState resin = isJungleBiome(world, position) ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = isJungleBiome(world, position) ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphere(world, rand, position, 14, 7, resin, sticky_resin);
        generateSphere(world, rand, position, 12, 5, Blocks.AIR.getDefaultState());

        generatePath(world, rand, position.offset(EnumFacing.NORTH, 9), 15 + rand.nextInt(10), EnumFacing.NORTH, 100);
        generatePath(world, rand, position.offset(EnumFacing.SOUTH, 9), 15 + rand.nextInt(10), EnumFacing.SOUTH, 100);
        generatePath(world, rand, position.offset(EnumFacing.WEST, 9), 15 + rand.nextInt(10), EnumFacing.WEST, 100);
        generatePath(world, rand, position.offset(EnumFacing.EAST, 9), 15 + rand.nextInt(10), EnumFacing.EAST, 100);
    }

    private void generatePath(World world, Random rand, BlockPos offset, int length, EnumFacing direction, int roomChance) {
        if(roomChance == 0){
            return;
        }
        if(rand.nextInt(100) < roomChance){
            for(int i = 0; i < length; i++){
                generateCircle(world, rand, offset.offset(direction, i), 3, 5, direction);
            }
            generateRoom(world, rand, offset.offset(direction, length), 7, 4, roomChance / 2, direction);
        }
    }

    private void generateRoom(World world, Random rand, BlockPos position, int size, int height, int roomChance, EnumFacing direction) {
        IBlockState resin = isJungleBiome(world, position) ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = isJungleBiome(world, position) ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphere(world, rand, position, size + 2, height + 2, resin, sticky_resin);
        generateSphere(world, rand, position, size, height - 1, Blocks.AIR.getDefaultState());
        if(rand.nextInt(4) == 0 && direction.getOpposite() != EnumFacing.NORTH){
            generatePath(world, rand, position.offset(EnumFacing.NORTH, size - 2), 5 + rand.nextInt(20), EnumFacing.NORTH, roomChance);
        }
        if(rand.nextInt(4) == 0 && direction.getOpposite() != EnumFacing.SOUTH) {
            generatePath(world, rand, position.offset(EnumFacing.SOUTH, size - 2), 5 + rand.nextInt(20), EnumFacing.SOUTH, roomChance);
        }
        if(rand.nextInt(4) == 0 && direction.getOpposite() != EnumFacing.WEST) {
            generatePath(world, rand, position.offset(EnumFacing.WEST, size - 2), 5 + rand.nextInt(20), EnumFacing.WEST, roomChance);
        }
        if(rand.nextInt(4) == 0 && direction.getOpposite() != EnumFacing.EAST) {
            generatePath(world, rand, position.offset(EnumFacing.EAST, size - 2), 5 + rand.nextInt(20), EnumFacing.EAST, roomChance);
        }
    }



    private void generateCircle(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        IBlockState resin = isJungleBiome(world, position) ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = isJungleBiome(world, position) ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Math.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState());
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        radius += 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        if(!world.isAirBlock(position.add(0, x, z))){
                            world.setBlockState(position.add(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin);
                        }
                    } else {
                        if(!world.isAirBlock(position.add(x, z, 0))) {
                            world.setBlockState(position.add(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin);
                        }
                    }
                }
            }
        }
    }

    public void generateSphere(World world, Random rand, BlockPos position, int size, int height, IBlockState fill) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)) && !world.isAirBlock(blockpos)) {
                world.setBlockState(blockpos, fill, 3);
            }
        }
    }

    public void generateSphere(World world, Random rand, BlockPos position, int size, int height, IBlockState fill, IBlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)) && !world.isAirBlock(blockpos)) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 3);
            }
        }
    }

    private static boolean isJungleBiome(World world, BlockPos position) {
        Biome biome = world.getBiome(position);
        return biome.topBlock != Blocks.SAND && biome.fillerBlock != Blocks.SAND && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);
    }
}
