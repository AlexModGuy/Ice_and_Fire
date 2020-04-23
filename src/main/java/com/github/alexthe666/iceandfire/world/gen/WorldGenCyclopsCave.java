package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.block.BlockBone;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;

public class WorldGenCyclopsCave extends WorldGenerator {

    public static final ResourceLocation CYCLOPS_CHEST = LootTableList.register(new ResourceLocation("iceandfire", "cyclops_cave"));

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i1 = 16;
        int i2 = i1 - 2;
        int sheepPenCount = 0;
        int dist = 6;
        if (worldIn.isAirBlock(position.add(i1 - dist, -3, -i1 + dist)) || worldIn.isAirBlock(position.add(i1 - dist, -3, i1 - dist)) || worldIn.isAirBlock(position.add(-i1 + dist, -3, -i1 + dist)) || worldIn.isAirBlock(position.add(-i1 + dist, -3, i1 - dist))) {
            return false;
        }

        {
            int ySize = rand.nextInt(2);
            int j = i1 + rand.nextInt(2);
            int k = 12 + ySize;
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;


            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                boolean doorwayX = blockpos.getX() >= position.getX() - 2 + rand.nextInt(2) && blockpos.getX() <= position.getX() + 2 + rand.nextInt(2);
                boolean doorwayZ = blockpos.getZ() >= position.getZ() - 2 + rand.nextInt(2) && blockpos.getZ() <= position.getZ() + 2 + rand.nextInt(2);
                boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.getY() > position.getY() || blockpos.getY() > position.getY() + k - (3 + rand.nextInt(2));
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest) && worldIn.getBlockState(position).getBlock().getBlockHardness(worldIn.getBlockState(position), worldIn, position) >= 0 && isNotInDoorway) {
                        worldIn.setBlockState(blockpos, Blocks.STONE.getDefaultState(), 3);
                    }
                    if (blockpos.getY() == position.getY()) {
                        worldIn.setBlockState(blockpos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3);
                    }
                    if (blockpos.getY() <= position.getY() - 1 && !worldIn.isBlockFullCube(blockpos)) {
                        worldIn.setBlockState(blockpos, Blocks.COBBLESTONE.getDefaultState(), 3);
                    }
                }
            }


        }
        {
            int ySize = rand.nextInt(2);
            int j = i2 + rand.nextInt(2);
            int k = 10 + ySize;
            int l = i2 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && blockpos.getY() > position.getY()) {
                    if (!(worldIn.getBlockState(position).getBlock() instanceof BlockChest)) {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);

                    }
                }
            }
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && blockpos.getY() == position.getY()) {
                    if (rand.nextInt(130) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        this.genSkeleton(worldIn, blockpos.up(), rand, position, f);
                    }

                    if (rand.nextInt(130) == 0 && blockpos.distanceSq(position) <= (double) (f * f) * 0.8F && sheepPenCount < 2) {
                        this.genSheepPen(worldIn, blockpos.up(), rand, position, f);
                        sheepPenCount++;
                    }
                    if (rand.nextInt(80) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        worldIn.setBlockState(blockpos.up(), IafBlockRegistry.goldPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 8), 3);
                        worldIn.setBlockState(blockpos.up().north(), IafBlockRegistry.goldPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlockState(blockpos.up().south(), IafBlockRegistry.goldPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlockState(blockpos.up().west(), IafBlockRegistry.goldPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlockState(blockpos.up().east(), IafBlockRegistry.goldPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                        worldIn.setBlockState(blockpos.up(2), Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 2);
                        if (worldIn.getBlockState(blockpos.up(2)).getBlock() instanceof BlockChest) {
                            TileEntity tileentity1 = worldIn.getTileEntity(blockpos.up(2));
                            if (tileentity1 instanceof TileEntityChest && !tileentity1.isInvalid()) {
                                ((TileEntityChest) tileentity1).setLootTable(CYCLOPS_CHEST, rand.nextLong());
                            }
                        }

                    }


                    if (rand.nextInt(50) == 0 && isTouchingAir(worldIn, blockpos.up())) {
                        int torchHeight = rand.nextInt(2) + 1;
                        for (int fence = 0; fence < torchHeight; fence++) {
                            worldIn.setBlockState(blockpos.up(1 + fence), Blocks.OAK_FENCE.getDefaultState());
                        }
                        worldIn.setBlockState(blockpos.up(1 + torchHeight), Blocks.TORCH.getDefaultState());
                    }
                }
            }
        }
        EntityCyclops cyclops = new EntityCyclops(worldIn);
        cyclops.setVariant(rand.nextInt(3));
        cyclops.setPositionAndRotation(position.getX() + 0.5, position.getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        worldIn.spawnEntity(cyclops);
        return true;
    }

    private void genSheepPen(World worldIn, BlockPos blockpos, Random rand, BlockPos origin, float radius) {
        int width = 5 + rand.nextInt(3);
        int sheeps = 2 + rand.nextInt(3);
        int sheepsSpawned = 0;
        EnumFacing direction = EnumFacing.NORTH;
        BlockPos end = blockpos;
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                if (origin.distanceSq(end.offset(direction, side)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(end.offset(direction, side), Blocks.OAK_FENCE.getDefaultState());
                    if (worldIn.isAirBlock(end.offset(direction, side).offset(direction.rotateY())) && sheepsSpawned < sheeps) {
                        BlockPos sheepPos = end.offset(direction, side).offset(direction.rotateY());

                        EntitySheep entitySheep = new EntitySheep(worldIn);
                        entitySheep.setPosition(sheepPos.getX() + 0.5F, sheepPos.getY() + 0.5F, sheepPos.getZ() + 0.5F);
                        entitySheep.setFleeceColor(rand.nextInt(4) == 0 ? EnumDyeColor.YELLOW : EnumDyeColor.WHITE);
                        if (!worldIn.isRemote) {
                            worldIn.spawnEntity(entitySheep);
                        }
                        sheepsSpawned++;
                    }
                }
            }
            end = end.offset(direction, width);
            direction = direction.rotateY();
        }
        for (int x = 1; x < width - 1; x++) {
            for (int z = 1; z < width - 1; z++) {
                if (origin.distanceSq(end.add(x, 0, z)) <= (double) (radius * radius)) {
                    worldIn.setBlockToAir(end.add(x, 0, z));
                }
            }
        }
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

    private void genSkeleton(World worldIn, BlockPos blockpos, Random rand, BlockPos origin, float radius) {
        EnumFacing direction = EnumFacing.HORIZONTALS[new Random().nextInt(3)];
        EnumFacing.Axis oppositeAxis = direction.getAxis() == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X;
        int maxRibHeight = rand.nextInt(2);
        for (int spine = 0; spine < 5 + rand.nextInt(2) * 2; spine++) {
            BlockPos segment = blockpos.offset(direction, spine);
            if (origin.distanceSq(segment) <= (double) (radius * radius)) {
                worldIn.setBlockState(segment, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, direction.getAxis()));
            }
            if (spine % 2 != 0) {
                BlockPos rightRib = segment.offset(direction.rotateYCCW());
                BlockPos leftRib = segment.offset(direction.rotateY());
                if (origin.distanceSq(rightRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib, Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.distanceSq(rightRib.up(ribHeight).offset(direction.rotateYCCW())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(rightRib.up(ribHeight).offset(direction.rotateYCCW()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                    if (origin.distanceSq(leftRib.up(ribHeight).offset(direction.rotateY())) <= (double) (radius * radius)) {
                        worldIn.setBlockState(leftRib.up(ribHeight).offset(direction.rotateY()), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, EnumFacing.Axis.Y));
                    }
                }
                if (origin.distanceSq(rightRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(rightRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
                if (origin.distanceSq(leftRib.up(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    worldIn.setBlockState(leftRib.up(maxRibHeight + 2), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockBone.AXIS, oppositeAxis));
                }
            }

        }
    }
}
