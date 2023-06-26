package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GraveyardProcessor extends StructureProcessor {

    private final float integrity = 1.0F;
    public static final GraveyardProcessor INSTANCE = new GraveyardProcessor();
    public static final Codec<GraveyardProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public GraveyardProcessor() {
    }

    public static BlockState getRandomCobblestone(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return Blocks.COBBLESTONE.defaultBlockState();
        } else if (rand < 0.9) {
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        } else {
            return Blocks.INFESTED_COBBLESTONE.defaultBlockState();
        }
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return Blocks.STONE_BRICKS.defaultBlockState();
        } else if (rand < 0.9) {
            return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        } else {
            return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, StructureTemplate.@NotNull StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(infoIn2.pos());
        if (infoIn2.state().getBlock() == Blocks.STONE_BRICKS) {
            BlockState state = getRandomCrackedBlock(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        }
        if (infoIn2.state().getBlock() == Blocks.COBBLESTONE) {
            BlockState state = getRandomCobblestone(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        }
        return infoIn2;
    }


    @Override
    protected @NotNull StructureProcessorType getType() {
        return IafProcessors.GRAVEYARDPROCESSOR.get();
    }

}
