package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Random;

public class WorldGenCavePillar {
    private final boolean ice;

    public WorldGenCavePillar(boolean ice) {
        this.ice = ice;
    }

    public boolean generate(Level worldIn, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);

        return true;
    }
}
