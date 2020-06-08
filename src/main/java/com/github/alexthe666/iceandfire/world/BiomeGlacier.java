package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.SnowyTundraBiome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

import java.util.Random;

public class BiomeGlacier extends Biome {

    public BiomeGlacier() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG).precipitation(Biome.RainType.SNOW).category(Biome.Category.ICY).depth(0.125F).scale(0.05F).temperature(0.0F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        super(false, new BiomeProperties("Glacier").setBaseHeight(2.125F).setHeightVariation(0.025F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled());
        this.topBlock = Blocks.SNOW.getDefaultState();
        this.fillerBlock = Blocks.PACKED_ICE.getDefaultState();
        this.setRegistryName(IceAndFire.MODID, "Glacier");
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateGlacierBiome(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public final void generateGlacierBiome(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        BlockState BlockState = this.topBlock;
        BlockState BlockState1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;
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
                } else if (BlockState2.getBlock() == Blocks.STONE) {
                    if (j == -1) {
                        BlockState = this.topBlock;
                        BlockState1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;

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
                            BlockState = this.topBlock;
                            BlockState1 = rand.nextInt(5) == 0 ? Blocks.ICE.getDefaultState() : this.fillerBlock;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, BlockState1);
                        }
                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, BlockState1);

                        if (j == 0 && (BlockState1.getBlock() == Blocks.ICE || BlockState1.getBlock() == this.fillerBlock.getBlock()) && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            //BlockState1 = BlockState1;
                        }
                    }
                }
            }
        }
    }

}
