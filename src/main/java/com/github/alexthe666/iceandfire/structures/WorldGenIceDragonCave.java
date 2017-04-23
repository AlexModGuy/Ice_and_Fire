package com.github.alexthe666.iceandfire.structures;

import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;

public class WorldGenIceDragonCave extends WorldGenerator {

    public static final ResourceLocation ICEDRAGON_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "ice_dragon_cave"));

    public static void setGoldPile(World world, BlockPos pos) {
        int chance = new Random().nextInt(99) + 1;
        if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
            return;
        }

        if (chance < 60) {
            world.setBlockState(pos, ModBlocks.silverPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
        } else if (chance > 60 && chance < 62) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 3);
            if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
                TileEntity tileentity1 = world.getTileEntity(pos);
                if (tileentity1 instanceof TileEntityChest && !((TileEntityChest) tileentity1).isInvalid()) {
                    ((TileEntityChest) tileentity1).setLootTable(ICEDRAGON_CHEST, new Random().nextLong());
                }
            }
        }
    }

    public static void setOres(World world, BlockPos pos) {
        boolean vien_chance = new Random().nextInt(IceAndFire.CONFIG.oreToStoneRatioForDragonCaves + 1) == 0;
        if (vien_chance) {
            int chance = new Random().nextInt(199) + 1;
            if (chance < 30) {
                world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState(), 3);
            }
            if (chance > 30 && chance < 40) {
                world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState(), 3);
            }
            if (chance > 40 && chance < 50) {
                world.setBlockState(pos, ModBlocks.silverOre.getDefaultState(), 3);
            }
            if (chance > 50 && chance < 60) {
                world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState(), 3);
            }
            if (chance > 60 && chance < 70) {
                world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState(), 3);
            }
            if (chance > 70 && chance < 80) {
                world.setBlockState(pos, Blocks.LAPIS_ORE.getDefaultState(), 3);
            }
            if (chance > 80 && chance < 90) {
                world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState(), 3);
            }
            if (chance > 90 && chance < 1000) {
                world.setBlockState(pos, ModBlocks.sapphireOre.getDefaultState(), 3);
            }
        } else {
            int chance = new Random().nextInt(2);
            if (chance == 0) {
                world.setBlockState(pos, ModBlocks.frozenStone.getDefaultState(), 3);
            } else {
                world.setBlockState(pos, ModBlocks.frozenCobblestone.getDefaultState(), 3);
            }
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int dragonAge = 75 + rand.nextInt(50);
        int i1 = dragonAge / 4;
        int i2 = i1 - 2;
        int ySize = rand.nextInt(2);
        for (int i = 0; i1 >= 0 && i < 3; ++i) {
            int j = i1 + rand.nextInt(2);
            int k = i1 / 2 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 3);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = i2 / 2 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2));
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && worldIn.getBlockState(blockpos).getMaterial() == Material.ROCK) {
                    this.setOres(worldIn, blockpos);
                }
            }
        }
        for (int i = 0; i2 >= 0 && i < 3; ++i) {
            int j = i2 + rand.nextInt(2);
            int k = (i2 + rand.nextInt(2)) / 2;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && worldIn.getBlockState(blockpos.down()).getMaterial() == Material.ROCK && worldIn.getBlockState(blockpos).getMaterial() != Material.ROCK) {
                    this.setGoldPile(worldIn, blockpos);
                }
            }
        }
        EntityIceDragon dragon = new EntityIceDragon(worldIn);
        dragon.setGender(dragon.getRNG().nextBoolean());
        dragon.growDragon(dragonAge);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(new Random().nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setSleeping(true);
        dragon.setHunger(50);
        dragon.homeArea = position;
        worldIn.spawnEntity(dragon);
        return true;
    }
}
