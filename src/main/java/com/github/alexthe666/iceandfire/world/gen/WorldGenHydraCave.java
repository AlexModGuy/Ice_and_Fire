package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.entity.EntityHydra;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;

public class WorldGenHydraCave extends WorldGenerator {

    public static final ResourceLocation HYDRA_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "hydra_cave"));
    protected static final WorldGenSwamp SWAMP_FEATURE = new WorldGenSwamp();

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i1 = 8;
        int i2 = i1 - 2;
        int dist = 6;
        if (worldIn.isAirBlock(position.add(i1 - dist, -3, -i1 + dist)) || worldIn.isAirBlock(position.add(i1 - dist, -3, i1 - dist)) || worldIn.isAirBlock(position.add(-i1 + dist, -3, -i1 + dist)) || worldIn.isAirBlock(position.add(-i1 + dist, -3, i1 - dist))) {
            return false;
        }

        {
            int ySize = rand.nextInt(2);
            int j = i1 + rand.nextInt(2);
            int k = 5 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;


            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                boolean doorwayX = blockpos.getX() >= position.getX() - 2 + rand.nextInt(2) && blockpos.getX() <= position.getX() + 2 + rand.nextInt(2);
                boolean doorwayZ = blockpos.getZ() >= position.getZ() - 2 + rand.nextInt(2) && blockpos.getZ() <= position.getZ() + 2 + rand.nextInt(2);
                boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.getY() > position.getY() || blockpos.getY() > position.getY() + k - (1 + rand.nextInt(2));
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest) && worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position) >= 0 && isNotInDoorway) {
                        worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 3);
                        if (worldIn.getBlockState(position.down()).getBlock() == Blocks.GRASS) {
                            worldIn.setBlockState(blockpos.down(), Blocks.DIRT.getDefaultState(), 3);
                        }
                        if (rand.nextInt(4) == 0) {
                            worldIn.setBlockState(blockpos.up(), Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
                        }
                        if (rand.nextInt(9) == 0) {
                            SWAMP_FEATURE.generate(worldIn, rand, blockpos.up());
                        }

                    }
                    if (blockpos.getY() == position.getY()) {
                        worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 3);
                    }
                    if (blockpos.getY() <= position.getY() - 1 && !worldIn.isBlockFullCube(blockpos)) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 3);

                    }
                }
            }


        }
        {
            int ySize = rand.nextInt(2);
            int j = i2 + rand.nextInt(2);
            int k = 4 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && blockpos.getY() > position.getY()) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);

                    }
                }
            }
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k + 8, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && blockpos.getY() == position.getY()) {
                    if (rand.nextInt(30) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(1), Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(blockpos.up(1)).getBlock() instanceof BlockChest) {
                            TileEntity tileentity1 = worldIn.getTileEntity(blockpos.up(1));
                            if (tileentity1 instanceof TileEntityChest && !tileentity1.isInvalid()) {
                                ((TileEntityChest) tileentity1).setLootTable(HYDRA_CHEST, rand.nextLong());
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(45) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                        TileEntity tileentity1 = worldIn.getTileEntity(blockpos.up(1));
                        if (tileentity1 instanceof TileEntitySkull && !tileentity1.isInvalid()) {
                            int rot = MathHelper.floor((double)(rand.nextFloat() * 360.0F) + 0.5D) & 15;
                            ((TileEntitySkull) tileentity1).setSkullRotation(rot);
                        }
                        continue;
                    }
                    if (rand.nextInt(35) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false));
                        for(EnumFacing facing : EnumFacing.values()){
                            if(rand.nextFloat() < 0.3F && facing != EnumFacing.DOWN){
                                worldIn.setBlockState(blockpos.up().offset(facing), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false));
                            }
                        }
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
                        continue;
                    }
                    if (rand.nextInt(15) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.getDefaultState() : Blocks.RED_MUSHROOM.getDefaultState());
                        continue;
                    }
                }
            }
        }
        EntityHydra hydra = new EntityHydra(worldIn);
        hydra.setVariant(rand.nextInt(3));
        hydra.setHomePosAndDistance(position, 15);
        hydra.setPositionAndRotation(position.getX() + 0.5, position.getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        worldIn.spawnEntity(hydra);
        return true;
    }

    private boolean isTouchingAir(World worldIn, BlockPos pos) {
        boolean isTouchingAir = true;
        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            if (!worldIn.isAirBlock(pos.offset(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }
}
