package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class DreadPortalProcessor implements ITemplateProcessor {

    private float integrity = 1.0F;
    public DreadPortalProcessor(BlockPos position, PlacementSettings settings, Biome biome) {
    }

    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (worldIn.rand.nextFloat() <= integrity) {
            if (blockInfoIn.blockState.getBlock() == Blocks.DIAMOND_BLOCK) {
                return new Template.BlockInfo(pos, IafBlockRegistry.DREAD_PORTAL.getDefaultState(), null);
            }
            if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS) {
                BlockState state = getRandomCrackedBlock(null, worldIn.rand);
                return new Template.BlockInfo(pos, state, null);
            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.getDefaultState();
        } else if (rand < 0.6) {
            return IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.getDefaultState();
        } else {
            return IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.getDefaultState();
        }
    }
}
