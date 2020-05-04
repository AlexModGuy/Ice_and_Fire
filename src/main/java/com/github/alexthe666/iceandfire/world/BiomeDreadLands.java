package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockReturningState;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.world.gen.WorldGenDreadRuin;
import com.github.alexthe666.iceandfire.world.gen.WorldGenDreadSpike;
import com.github.alexthe666.iceandfire.world.gen.WorldGenDreadwoodTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeDreadLands extends Biome {
    protected static final BlockState STONE = IafBlockRegistry.FROZEN_STONE.getDefaultState().with(BlockReturningState.REVERTS, false);
    protected static final BlockState ICE = IafBlockRegistry.DRAGON_ICE.getDefaultState();
    protected static final BlockState DIRT = IafBlockRegistry.FROZEN_DIRT.getDefaultState().with(BlockReturningState.REVERTS, false);
    protected static final BlockState GRASS = IafBlockRegistry.FROZEN_GRASS.getDefaultState().with(BlockReturningState.REVERTS, false);
    protected static final BlockState GRAVEL = IafBlockRegistry.FROZEN_GRAVEL.getDefaultState().with(BlockReturningState.REVERTS, false);
    protected static final BlockState DRAGON_ICE = IafBlockRegistry.DRAGON_ICE.getDefaultState();
    private static final WorldGenDreadwoodTree DREADWOOD_TREE = new WorldGenDreadwoodTree();
    private static final WorldGenDreadSpike DREAD_SPIKE = new WorldGenDreadSpike();
    private static final WorldGenDreadRuin DREAD_RUIN = new WorldGenDreadRuin();

    public BiomeDreadLands() {
        super(new BiomeProperties("dreadlands").setTemperature(-1.0F).setBaseHeight(1.125F).setHeightVariation(0.025F).setTemperature(0.0F).setRainfall(3.5F).setSnowEnabled());
        this.topBlock = IafBlockRegistry.FROZEN_GRASS.getDefaultState();
        this.fillerBlock = IafBlockRegistry.FROZEN_DIRT.getDefaultState();
        this.setRegistryName(IceAndFire.MODID, "Dreadlands");
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.decorator.treesPerChunk = 1;
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {

        float k1 = decorator.treesPerChunk;

        if (rand.nextFloat() < decorator.extraTreeChance) {
            ++k1;
        }

        if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, pos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE))
            if (rand.nextFloat() < 0.1F) {
                for (int j2 = 0; j2 < k1; ++j2) {
                    int k6 = rand.nextInt(16) + 8;
                    int l = rand.nextInt(16) + 8;
                    BlockPos blockpos = worldIn.getHeight(pos.add(k6, 0, l));
                    DREADWOOD_TREE.generate(worldIn, rand, blockpos);
                }
            }
        if (rand.nextFloat() < 0.2F) {
            int k6 = rand.nextInt(16) + 8;
            int l = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(k6, -1, l));
            DREAD_SPIKE.generate(worldIn, rand, blockpos);
        }
        if (rand.nextFloat() < 0.05F) {
            int k6 = rand.nextInt(16) + 8;
            int l = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(k6, -1, l));
            DREAD_RUIN.generate(worldIn, rand, blockpos);
        }
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateGlacierBiome(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public final void generateGlacierBiome(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        BlockState BlockState = this.topBlock;
        BlockState BlockState1 = this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                BlockState BlockState2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (BlockState2.getMaterial() == Material.AIR) {
                    j = -1;
                } else if (BlockState2.getBlock() == IafBlockRegistry.FROZEN_STONE) {
                    if (j == -1) {
                        if (k <= 0) {
                            BlockState = AIR;
                            BlockState1 = STONE;
                        } else if (j1 >= i - 4 && j1 <= i + 1) {
                            BlockState = this.topBlock;
                            BlockState1 = this.fillerBlock;
                        }

                        if (j1 < i && (BlockState == null || BlockState.getMaterial() == Material.AIR)) {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
                                BlockState = ICE;
                            } else {
                                BlockState = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1) {
                            chunkPrimerIn.setBlockState(i1, j1, l, BlockState);
                        } else if (j1 < i - 7 - k) {
                            BlockState = AIR;
                            BlockState1 = STONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, BlockState1);
                        }
                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, BlockState1);

                        if (j == 0 && BlockState1.getBlock() == IafBlockRegistry.FROZEN_DIRT && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            BlockState1 = STONE;
                        }
                    }
                }
            }
        }
    }

}
