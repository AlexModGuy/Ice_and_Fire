package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;


public class BlockIafOre extends Block {
    public Item itemBlock;

    public BlockIafOre(int toollevel, float hardness, float resistance) {
        super(
            Properties
                .of(Material.STONE)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops()
		);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? this.getExperience(RANDOM) : 0;
    }

    protected int getExperience(Random rand) {
        if (this == IafBlockRegistry.SAPPHIRE_ORE.get() || this == IafBlockRegistry.AMYTHEST_ORE.get()) {
            return Mth.nextInt(rand, 3, 7);
        }
        return 0;
    }
}
