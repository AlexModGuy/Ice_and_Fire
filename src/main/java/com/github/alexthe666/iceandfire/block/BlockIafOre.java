package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;


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
    public void tryDropExperience(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack heldItem, @NotNull IntProvider amount) {
        super.tryDropExperience(level, pos, heldItem, amount);
    }


    // FIXME :: Unused -> popExperience(...)
    protected int getExperience(RandomSource rand) {
        if (this == IafBlockRegistry.SAPPHIRE_ORE.get()) {
            return Mth.nextInt(rand, 3, 7);
        }
        return 0;
    }
}
