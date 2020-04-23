package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenCaveReplace extends WorldGenerator {

    private final Block block;
    private final Block toReplace;
    private final int startRadius;

    public WorldGenCaveReplace(Block blockIn, Block toReplace, int startRadiusIn) {
        super(false);
        this.block = blockIn;
        this.toReplace = toReplace;
        this.startRadius = startRadiusIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        while (true) {
            label50:
            {
                Block block = worldIn.getBlockState(position.down()).getBlock();
                if (position.getY() <= 3) {
                    return false;
                }

                int i1 = this.startRadius;

                for (int i = 0; i1 >= 0 && i < 3; ++i) {
                    int j = i1 + rand.nextInt(2);
                    int k = i1 + rand.nextInt(2);
                    int l = i1 + rand.nextInt(2);
                    float f = (float) (j + k + l) * 0.333F + 0.5F;

                    for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                        if (blockpos.distanceSq(position) <= (double) (f * f) && (worldIn.getBlockState(blockpos).getBlock() == toReplace)) {
                            worldIn.setBlockState(blockpos, this.block.getDefaultState());
                        }
                    }

                    position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
                }

                return true;
            }
        }
    }
}