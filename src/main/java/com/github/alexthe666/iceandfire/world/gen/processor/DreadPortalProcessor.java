package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class DreadPortalProcessor extends StructureProcessor {

    private float integrity = 1.0F;

    public DreadPortalProcessor(BlockPos position, PlacementSettings settings, Biome biome) {
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

    @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        Random random = settings.getRandom(pos);
        if (random.nextFloat() <= integrity) {
            if (blockInfoIn.state.getBlock() == Blocks.DIAMOND_BLOCK) {
                return new Template.BlockInfo(pos, IafBlockRegistry.DREAD_PORTAL.getDefaultState(), null);
            }
            if (blockInfoIn.state.getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new Template.BlockInfo(pos, state, null);
            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("dread_portal_processor"), ops.createFloat(this.integrity))));
    }
}
