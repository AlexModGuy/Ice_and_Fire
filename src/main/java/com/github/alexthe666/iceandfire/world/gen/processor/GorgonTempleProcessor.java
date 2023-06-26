package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.NotNull;

public class GorgonTempleProcessor extends StructureProcessor {

    public static final GorgonTempleProcessor INSTANCE = new GorgonTempleProcessor();
    public static final Codec<GorgonTempleProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public GorgonTempleProcessor() {
    }
    /* TODO:
    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, StructureTemplate.@NotNull StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {

        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        ChunkPos currentChunk = new ChunkPos(infoIn2.pos);
        if (infoIn2.state.getBlock() instanceof SimpleWaterloggedBlock) {
            if (worldReader.getFluidState(infoIn2.pos).is(FluidTags.WATER)) {
                worldReader.getChunk(currentChunk.x, currentChunk.z).setBlockState(infoIn2.pos, Blocks.AIR.defaultBlockState(), false);
            }
        }

        // Needed as waterloggable blocks will get waterlogged from neighboring chunk's water too.
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for(Direction direction : Direction.Plane.HORIZONTAL){
            mutable.set(infoIn2.pos).move(direction);
            if(currentChunk.x != mutable.getX() >> 4 || currentChunk.z != mutable.getZ() >> 4) {
                ChunkAccess sideChunk = worldReader.getChunk(mutable);
                if (sideChunk.getFluidState(mutable).is(FluidTags.WATER)) {
                    sideChunk.setBlockState(mutable, Blocks.STONE_BRICKS.defaultBlockState(), false);
                }
            }
        }

        return infoIn2;
    }*/

    @Override
    protected @NotNull StructureProcessorType getType() {
        return IafProcessors.GORGONTEMPLEPROCESSOR.get();
    }
}
