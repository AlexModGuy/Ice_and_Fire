package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.core.ModBlocks;
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

    private void generateMainRoom(World world, Random rand, BlockPos position){
        IBlockState resin = isJungleBiome(world, position) ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = isJungleBiome(world, position) ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateCircle(world, rand, position, 14, 7, resin, sticky_resin);
        generateCircle(world, rand, position, 12, 5, Blocks.AIR.getDefaultState());
        generatePath(world, rand, position, EnumFacing.NORTH);
        generatePath(world, rand, position, EnumFacing.SOUTH);
        generatePath(world, rand, position, EnumFacing.WEST);
        generatePath(world, rand, position, EnumFacing.EAST);
    }

    private void generatePath(World world, Random rand, BlockPos position, EnumFacing direction){
        IBlockState resin = isJungleBiome(world, position) ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = isJungleBiome(world, position) ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int i2 = 2;
        int length = 25;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = i2 + ySize;
        float f = (float) (j + k + length) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.down(k).offset(direction.rotateY(), -j).offset(direction, -length), position.up(k).offset(direction.rotateYCCW(), j).offset(direction, length))) {
            if (blockpos.distanceSq(position) <= (double) (f * f)) {
                world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    public void generateCircle(World world, Random rand, BlockPos position, int size, int height, IBlockState fill){
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))) {
                world.setBlockState(blockpos, fill, 3);
            }
        }
    }

    public void generateCircle(World world, Random rand, BlockPos position, int size, int height, IBlockState fill, IBlockState fill2){
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 3);
            }
        }
    }

    private static boolean isJungleBiome(World world, BlockPos position){
        Biome biome = world.getBiome(position);
        return biome.topBlock != Blocks.SAND && biome.fillerBlock != Blocks.SAND && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);
    }
}
