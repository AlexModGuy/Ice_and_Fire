package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DreadPortalProcessor extends StructureProcessor {

    private final float integrity = 1.0F;

    public DreadPortalProcessor(BlockPos position, StructurePlaceSettings settings, Biome biome) {
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.3) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.get().defaultBlockState();
        } else if (rand < 0.6) {
            return IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get().defaultBlockState();
        } else {
            return IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get().defaultBlockState();
        }
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockPos p_230386_3_, StructureTemplate.@NotNull StructureBlockInfo blockInfoIn, StructureTemplate.@NotNull StructureBlockInfo p_230386_5_, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(pos);
        if (random.nextFloat() <= integrity) {
            if (blockInfoIn.state().getBlock() == Blocks.DIAMOND_BLOCK) {
                return new StructureTemplate.StructureBlockInfo(pos, IafBlockRegistry.DREAD_PORTAL.get().defaultBlockState(), null);
            }
            if (blockInfoIn.state().getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS.get()) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new StructureTemplate.StructureBlockInfo(pos, state, null);
            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }

    @Override
    protected @NotNull StructureProcessorType getType() {
        return StructureProcessorType.BLOCK_ROT;
    }

}
