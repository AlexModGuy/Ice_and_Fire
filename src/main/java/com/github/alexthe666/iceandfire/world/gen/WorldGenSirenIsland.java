package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenSirenIsland extends WorldGenerator {

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int up = rand.nextInt(4) + 1;
        BlockPos center = position.up(up);
        int layer = 0;
        int sirens = 0;
        int sirensMax = 1 + rand.nextInt(3);
        while (!worldIn.getBlockState(center).isOpaqueCube() && center.getY() >= 0) {
            layer++;
            for (float i = 0; i < getRadius(layer, up); i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i + rand.nextInt(2); j += 0.5) {
                    BlockPos stonePos = new BlockPos(Math.floor(center.getX() + Math.sin(j) * i + rand.nextInt(2)), center.getY(), Math.floor(center.getZ() + Math.cos(j) * i + rand.nextInt(2)));
                    worldIn.setBlockState(stonePos, getStone(rand), 3);
                    BlockPos upPos = stonePos.up();
                    if (worldIn.isAirBlock(upPos) && worldIn.isAirBlock(upPos.east()) && worldIn.isAirBlock(upPos.north()) && worldIn.isAirBlock(upPos.north().east()) && rand.nextInt(3) == 0 && sirens < sirensMax) {
                        sirens++;
                        spawnSiren(worldIn, rand, upPos.north().east());
                    }
                }
            }
            center = center.down();
        }
        layer++;
        for (float i = 0; i < getRadius(layer, up); i += 0.5) {
            for (float j = 0; j < 2 * Math.PI * i + rand.nextInt(2); j += 0.5) {
                BlockPos stonePos = new BlockPos(Math.floor(center.getX() + Math.sin(j) * i + rand.nextInt(2)), center.getY(), Math.floor(center.getZ() + Math.cos(j) * i + rand.nextInt(2)));
                while (!worldIn.getBlockState(stonePos).isOpaqueCube() && stonePos.getY() >= 0) {
                    worldIn.setBlockState(stonePos, getStone(rand), 3);
                    stonePos = stonePos.down();
                }
            }
        }
        return false;
    }

    private int getRadius(int layer, int up) {
        return layer > up ? (int) (layer * 0.25) + up : layer;
    }

    private IBlockState getStone(Random random) {
        int chance = random.nextInt(100);
        if (chance > 90) {
            return Blocks.MOSSY_COBBLESTONE.getDefaultState();
        } else if (chance > 70) {
            return Blocks.GRAVEL.getDefaultState();
        } else if (chance > 45) {
            return Blocks.COBBLESTONE.getDefaultState();
        } else {
            return Blocks.STONE.getDefaultState();
        }
    }

    private void spawnSiren(World worldIn, Random rand, BlockPos position) {
        EntitySiren siren = new EntitySiren(worldIn);
        siren.setSinging(true);
        siren.setHairColor(rand.nextInt(2));
        siren.setSingingPose(rand.nextInt(2));
        siren.setPositionAndRotation(position.getX() + 0.5D, position.getY() + 1, position.getZ() + 0.5D, rand.nextFloat() * 360, 0);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(siren);
        }
    }
}
