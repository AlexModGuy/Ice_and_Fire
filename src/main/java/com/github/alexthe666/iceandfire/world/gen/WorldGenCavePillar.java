package com.github.alexthe666.iceandfire.world.gen;

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
