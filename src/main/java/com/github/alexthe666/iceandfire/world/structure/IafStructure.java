package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.datagen.tags.IafBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class IafStructure extends Structure {

    protected final Holder<StructureTemplatePool> startPool;
    protected final Optional<ResourceLocation> startJigsawName;
    protected final int size;
    protected final HeightProvider startHeight;
    protected final Optional<Heightmap.Types> projectStartToHeightmap;
    protected final int maxDistanceFromCenter;

    public IafStructure(Structure.StructureSettings config,
                        Holder<StructureTemplatePool> startPool,
                        Optional<ResourceLocation> startJigsawName,
                        int size,
                        HeightProvider startHeight,
                        Optional<Heightmap.Types> projectStartToHeightmap,
                        int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    protected boolean isValidLandBiome(final GenerationContext context, final BlockPos position) {
        return IafBiomeTags.isValidLandBiome(context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(position.getX()), QuartPos.fromBlock(position.getY()), QuartPos.fromBlock(position.getZ()), context.randomState().sampler()));
    }

    protected @NotNull BlockPos getPosition(final GenerationContext context, int sampledY) {
        ChunkPos pos = context.chunkPos();
        int x = pos.getMiddleBlockX();
        int z = pos.getMiddleBlockZ();
        AtomicInteger y = new AtomicInteger(sampledY);
        projectStartToHeightmap.ifPresent(heightmap -> y.getAndAdd(context.chunkGenerator().getFirstFreeHeight(x, z, heightmap, context.heightAccessor(), context.randomState())));
        return new BlockPos(x, y.get(), z);
    }

    protected Optional<GenerationStub> getLandPlacement(final GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();

        int y = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos position = new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());

        return isValidLandBiome(context, /* positions does not have the actual placement height yet */ getPosition(context, y)) ?
                JigsawPlacement.addPieces(context, startPool, startJigsawName, size, position, false, projectStartToHeightmap, maxDistanceFromCenter)
                :
                Optional.empty();
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return null;
    }

}
