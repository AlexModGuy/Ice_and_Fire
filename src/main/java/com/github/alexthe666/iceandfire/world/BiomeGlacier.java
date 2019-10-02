package com.github.alexthe666.iceandfire.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGlacier extends BiomeSnow {

    public BiomeGlacier() {
        super(false, new BiomeProperties("Glacier").setBaseHeight(2.125F).setHeightVariation(0.025F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled());
        this.topBlock = Blocks.SNOW.getDefaultState();
        this.fillerBlock = Blocks.PACKED_ICE.getDefaultState();
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateGlacierBiome(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public final void generateGlacierBiome(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
                if (iblockstate2.getMaterial() == Material.AIR) {
                    j = -1;
                } else if (iblockstate2.getBlock() == Blocks.STONE) {
                    if (j == -1) {
                        iblockstate = this.topBlock;
                        iblockstate1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
                                iblockstate = ICE;
                            } else {
                                iblockstate = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1) {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        } else if (j1 < i - 7 - k) {
                            iblockstate = this.topBlock;
                            iblockstate1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && (iblockstate1.getBlock() == Blocks.ICE || iblockstate1.getBlock() == this.fillerBlock.getBlock()) && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            //iblockstate1 = iblockstate1;
                        }
                    }
                }
            }
        }
    }

}
