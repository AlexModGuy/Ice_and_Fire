package com.github.alexthe666.iceandfire.structures;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenCavePillar {
    private boolean ice;

    public WorldGenCavePillar(boolean ice) {
        this.ice = ice;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);

        return true;
    }
}
