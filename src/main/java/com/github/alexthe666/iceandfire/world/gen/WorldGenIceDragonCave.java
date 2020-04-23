package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockSilverPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenIceDragonCave extends WorldGenerator {
    public static final ResourceLocation ICEDRAGON_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "ice_dragon_female_cave"));
    public static final ResourceLocation ICEDRAGON_MALE_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "ice_dragon_male_cave"));
    private static final WorldGenCaveStalactites CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.frozenStone);
    private static boolean isMale;

    public static void setGoldPile(World world, BlockPos pos, Random rand) {
        int chance = rand.nextInt(99) + 1;
        if (!(world.getBlockState(pos).getBlock() instanceof BlockContainer)) {
            if (chance < 60) {
                int goldRand = Math.max(1, IceAndFire.CONFIG.dragonDenGoldAmount) * (isMale ? 1 : 2);
                boolean generateGold = rand.nextInt(goldRand) == 0;
                world.setBlockState(pos, generateGold ? IafBlockRegistry.silverPile.getDefaultState().withProperty(BlockSilverPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.getDefaultState(), 3);
            } else if (chance == 61) {
                world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[rand.nextInt(3)]), 2);
                if (world.getBlockState(pos).getBlock() instanceof BlockChest) {
                    TileEntity tileentity1 = world.getTileEntity(pos);
                    if (tileentity1 instanceof TileEntityChest && !tileentity1.isInvalid()) {
                        ((TileEntityChest) tileentity1).setLootTable(isMale ? ICEDRAGON_MALE_CHEST : ICEDRAGON_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        List<SphereInfo> sphereList = new ArrayList<SphereInfo>();
        isMale = rand.nextBoolean();
        int dragonAge = 75 + rand.nextInt(50);
        int radius = (int) (dragonAge * 0.2F) + rand.nextInt(8);
        createShell(worldIn, rand, position, radius, sphereList);
        for (int i = 0; i < 3 + rand.nextInt(2); i++) {
            EnumFacing direction = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length - 1)];
            createShell(worldIn, rand, position.offset(direction, radius - 2), 2 * (int) (radius / 3F) + rand.nextInt(8), sphereList);
        }
        for (SphereInfo info : sphereList) {
            hollowOut(worldIn, rand, info.pos, info.radius - 2);
            decorateCave(worldIn, rand, info.pos, info.radius + 2);
        }
        sphereList.clear();
        EntityIceDragon dragon = new EntityIceDragon(worldIn);
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setSleeping(true);
        dragon.homePos = position;
        dragon.setHunger(50);
        worldIn.spawnEntity(dragon);
        return false;
    }

    private void decorateCave(World worldIn, Random rand, BlockPos pos, int radius) {
        for (int i = 0; i < 15 + rand.nextInt(10); i++) {
            CEILING_DECO.generate(worldIn, rand, offsetRandomlyByXZ(pos.up(radius / 2 - 1), rand, rand.nextInt(radius) - radius / 2, rand.nextInt(radius) - radius / 2));
        }
        int j = radius;
        int k = radius / 2;
        int l = radius;
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-j, -k, -l), pos.add(j, k / 2, l))) {
            if (blockpos.distanceSq(pos) <= (double) (f * f) && worldIn.getBlockState(blockpos.down()).getMaterial() == Material.ROCK && worldIn.getBlockState(blockpos).getMaterial() != Material.ROCK) {
                setGoldPile(worldIn, blockpos, rand);
            }
        }

    }

    private BlockPos offsetRandomlyBy(BlockPos in, Random rand, int offset1, int offset2) {
        return in.offset(EnumFacing.values()[rand.nextInt(EnumFacing.values().length - 1)], offset1).offset(EnumFacing.values()[rand.nextInt(EnumFacing.values().length - 1)], offset2);
    }

    private BlockPos offsetRandomlyByXZ(BlockPos in, Random rand, int offset1, int offset2) {
        return in.add(offset1, 0, offset2);
    }

    private void createShell(World worldIn, Random rand, BlockPos position, int radius, List<SphereInfo> sphereList) {
        int j = radius;
        int k = radius / 2;
        int l = radius;
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f)) {
                if (!(worldIn.getBlockState(position).getBlock() instanceof BlockContainer) && worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position) >= 0) {
                    boolean doOres = rand.nextInt(IceAndFire.CONFIG.oreToStoneRatioForDragonCaves + 1) == 0;
                    if (doOres) {
                        int chance = rand.nextInt(199) + 1;
                        if (chance < 30) {
                            worldIn.setBlockState(blockpos, Blocks.IRON_ORE.getDefaultState(), 3);
                        }
                        if (chance > 30 && chance < 40) {
                            worldIn.setBlockState(blockpos, Blocks.GOLD_ORE.getDefaultState(), 3);
                        }
                        if (chance > 40 && chance < 50) {
                            worldIn.setBlockState(blockpos, IceAndFire.CONFIG.generateSilverOre ? IafBlockRegistry.silverOre.getDefaultState() : IafBlockRegistry.frozenStone.getDefaultState(), 3);
                        }
                        if (chance > 50 && chance < 60) {
                            worldIn.setBlockState(blockpos, Blocks.COAL_ORE.getDefaultState(), 3);
                        }
                        if (chance > 60 && chance < 70) {
                            worldIn.setBlockState(blockpos, Blocks.REDSTONE_ORE.getDefaultState(), 3);
                        }
                        if (chance > 70 && chance < 80) {
                            worldIn.setBlockState(blockpos, Blocks.LAPIS_ORE.getDefaultState(), 3);
                        }
                        if (chance > 80 && chance < 90) {
                            worldIn.setBlockState(blockpos, Blocks.DIAMOND_ORE.getDefaultState(), 3);
                        }
                        if (chance > 90 && chance < 1000) {
                            worldIn.setBlockState(blockpos, IceAndFire.CONFIG.generateSapphireOre ? IafBlockRegistry.sapphireOre.getDefaultState() : Blocks.EMERALD_ORE.getDefaultState(), 3);
                        }
                    } else {
                        worldIn.setBlockState(blockpos, rand.nextBoolean() ? IafBlockRegistry.frozenCobblestone.getDefaultState() : IafBlockRegistry.frozenStone.getDefaultState());
                    }
                }
            }
        }
        sphereList.add(new SphereInfo(radius, position));
    }

    private void hollowOut(World worldIn, Random rand, BlockPos position, int radius) {
        int j = radius;
        int k = radius / 2;
        int l = radius;
        float f = (float) (j + k + l) * 0.333F + 0.5F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))) {
                if (!(worldIn.getBlockState(position).getBlock() instanceof BlockContainer)) {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    private class SphereInfo {
        int radius;
        BlockPos pos;

        private SphereInfo(int radius, BlockPos pos) {
            this.radius = radius;
            this.pos = pos;
        }
    }
}
