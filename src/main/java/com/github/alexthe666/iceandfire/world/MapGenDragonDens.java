package com.github.alexthe666.iceandfire.world;

import java.util.Random;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

import com.google.common.base.Objects;

public class MapGenDragonDens extends MapGenBase {
	protected static final IBlockState BLK_LAVA = Blocks.LAVA.getDefaultState();
	protected static final IBlockState BLK_AIR = Blocks.AIR.getDefaultState();

	protected void addRoom(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double d0, double d1, double d2) {
		float width =  3.0F + this.rand.nextFloat() * 6.0F;
		this.addTunnel(seed, chunkX, chunkZ, primer, d0, d1, d2, width, 0.0F, 0.0F, -1, -1, 0.75D);
		this.addTunnelDecoration(seed, chunkX, chunkZ, primer, d0, d1, d2, width, 0.0F, 0.0F, -1, -1, 0.75D);
	}

	protected void addTunnel(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double x, double y, double z, float width, float zero, float negitive0, int negitive1, int chance, double size) {
		double d0 = (double) (chunkX * 16 + 8);
		double d1 = (double) (chunkZ * 16 + 8);
		float f = 0.0F;
		float f1 = 0.0F;
		Random random = new Random(seed);

		if (chance <= 0) {
			int i = this.range * 16 - 16;
			chance = i - random.nextInt(i / 4);
		}

		boolean flag2 = false;

		if (negitive1 == -1) {
			negitive1 = chance / 2;
			flag2 = true;
		}

		int j = random.nextInt(chance / 2) + chance / 4;

		for (boolean flag = random.nextInt(6) == 0; negitive1 < chance; ++negitive1) {
			double d2 = 1.5D + (double) (MathHelper.sin((float) negitive1 * (float) Math.PI / (float) chance) * width);
			double d3 = d2 * size;
			float f2 = MathHelper.cos(negitive0);
			float f3 = MathHelper.sin(negitive0);
			x += (double) (MathHelper.cos(zero) * f2);
			y += (double) f3;
			z += (double) (MathHelper.sin(zero) * f2);

			if (flag) {
				negitive0 = negitive0 * 0.92F;
			} else {
				negitive0 = negitive0 * 0.7F;
			}

			negitive0 = negitive0 + f1 * 0.1F;
			zero += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

			if (!flag2 && negitive1 == j && width > 1.0F && chance > 0) {
				this.addTunnel(random.nextLong(), chunkX, chunkZ, primer, x, y, z, random.nextFloat() * 0.5F + 0.5F, zero - ((float) Math.PI / 2F), negitive0 / 3.0F, negitive1, chance, 1.0D);
				this.addTunnel(random.nextLong(), chunkX, chunkZ, primer, x, y, z, random.nextFloat() * 0.5F + 0.5F, zero + ((float) Math.PI / 2F), negitive0 / 3.0F, negitive1, chance, 1.0D);
				return;
			}

			if (flag2 || random.nextInt(4) != 0) {
				double d4 = x - d0;
				double d5 = z - d1;
				double d6 = (double) (chance - negitive1);
				double d7 = (double) (width + 2.0F + 16.0F);

				if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7) {
					return;
				}

				if (x >= d0 - 16.0D - d2 * 2.0D && z >= d1 - 16.0D - d2 * 2.0D && x <= d0 + 16.0D + d2 * 2.0D && z <= d1 + 16.0D + d2 * 2.0D) {
					int k2 = MathHelper.floor_double(x - d2) - chunkX * 16 - 1;
					int k = MathHelper.floor_double(x + d2) - chunkX * 16 + 1;
					int l2 = MathHelper.floor_double(y - d3) - 1;
					int l = MathHelper.floor_double(y + d3) + 1;
					int i3 = MathHelper.floor_double(z - d2) - chunkZ * 16 - 1;
					int i1 = MathHelper.floor_double(z + d2) - chunkZ * 16 + 1;

					if (k2 < 0) {
						k2 = 0;
					}

					if (k > 16) {
						k = 16;
					}

					if (l2 < 1) {
						l2 = 1;
					}

					if (l > 248) {
						l = 248;
					}

					if (i3 < 0) {
						i3 = 0;
					}

					if (i1 > 16) {
						i1 = 16;
					}

					boolean flag3 = false;

					for (int j1 = k2; !flag3 && j1 < k; ++j1) {
						for (int k1 = i3; !flag3 && k1 < i1; ++k1) {
							for (int l1 = l + 1; !flag3 && l1 >= l2 - 1; --l1) {
								if (l1 >= 0 && l1 < 256) {
									if (isOceanBlock(primer, j1, l1, k1, chunkX, chunkZ)) {
										flag3 = true;
									}
									if (l1 != l2 - 1 && j1 != k2 && j1 != k - 1 && k1 != i3 && k1 != i1 - 1) {
										l1 = l2;
									}
								}
							}
						}
					}
					if (!flag3) {
						BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

						for (int j3 = k2; j3 < k; ++j3) {
							double d10 = ((double) (j3 + chunkX * 16) + 0.5D - x) / d2;

							for (int i2 = i3; i2 < i1; ++i2) {
								double d8 = ((double) (i2 + chunkZ * 16) + 0.5D - z) / d2;
								boolean flag1 = false;

								if (d10 * d10 + d8 * d8 < 1.0D) {
									for (int j2 = l; j2 > l2; --j2) {
										double d9 = ((double) (j2 - 1) + 0.5D - y) / d3;

										if (d9 > -0.7D && d10 * d10 + d9 * d9 + d8 * d8 < 1.0D) {
											IBlockState iblockstate1 = primer.getBlockState(j3, j2, i2);
											IBlockState iblockstate2 = (IBlockState) Objects.firstNonNull(primer.getBlockState(j3, j2 + 1, i2), BLK_AIR);

											if (isTopBlock(primer, j3, j2, i2, chunkX, chunkZ)) {
												flag1 = true;
											}

											digBlock(primer, j3, j2, i2, chunkX, chunkZ, flag1, iblockstate1, iblockstate2);
										}
									}
								}
							}
						}
						if (flag2) {
							break;
						}
					}
				}
			}
		}
	}

	protected void addTunnelDecoration(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double x, double y, double z, float width, float zero, float negitive0, int negitive1, int chance, double size) {
		double d0 = (double) (chunkX * 16 + 8);
		double d1 = (double) (chunkZ * 16 + 8);
		float f = 0.0F;
		float f1 = 0.0F;
		Random random = new Random(seed);

		if (chance <= 0) {
			int i = this.range * 16 - 16;
			chance = i - random.nextInt(i / 4);
		}

		boolean flag2 = false;

		if (negitive1 == -1) {
			negitive1 = chance / 2;
			flag2 = true;
		}

		int j = random.nextInt(chance / 2) + chance / 4;

		for (boolean flag = random.nextInt(6) == 0; negitive1 < chance; ++negitive1) {
			double d2 = 1.5D + (double) (MathHelper.sin((float) negitive1 * (float) Math.PI / (float) chance) * width);
			double d3 = d2 * size;
			float f2 = MathHelper.cos(negitive0);
			float f3 = MathHelper.sin(negitive0);
			x += (double) (MathHelper.cos(zero) * f2);
			y += (double) f3;
			z += (double) (MathHelper.sin(zero) * f2);

			if (flag) {
				negitive0 = negitive0 * 0.92F;
			} else {
				negitive0 = negitive0 * 0.7F;
			}

			negitive0 = negitive0 + f1 * 0.1F;
			zero += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

			if (!flag2 && negitive1 == j && width > 1.0F && chance > 0) {
				this.addTunnel(random.nextLong(), chunkX, chunkZ, primer, x, y, z, random.nextFloat() * 0.5F + 0.5F, zero - ((float) Math.PI / 2F), negitive0 / 3.0F, negitive1, chance, 1.0D);
				this.addTunnel(random.nextLong(), chunkX, chunkZ, primer, x, y, z, random.nextFloat() * 0.5F + 0.5F, zero + ((float) Math.PI / 2F), negitive0 / 3.0F, negitive1, chance, 1.0D);
				return;
			}

			if (flag2 || random.nextInt(4) != 0) {
				double d4 = x - d0;
				double d5 = z - d1;
				double d6 = (double) (chance - negitive1);
				double d7 = (double) (width + 2.0F + 16.0F);

				if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7) {
					return;
				}

				if (x >= d0 - 16.0D - d2 * 2.0D && z >= d1 - 16.0D - d2 * 2.0D && x <= d0 + 16.0D + d2 * 2.0D && z <= d1 + 16.0D + d2 * 2.0D) {
					int k2 = MathHelper.floor_double(x - d2) - chunkX * 16 - 1;
					int k = MathHelper.floor_double(x + d2) - chunkX * 16 + 1;
					int l2 = MathHelper.floor_double(y - d3) - 1;
					int l = MathHelper.floor_double(y + d3) + 1;
					int i3 = MathHelper.floor_double(z - d2) - chunkZ * 16 - 1;
					int i1 = MathHelper.floor_double(z + d2) - chunkZ * 16 + 1;

					if (k2 < 0) {
						k2 = 0;
					}

					if (k > 16) {
						k = 16;
					}

					if (l2 < 1) {
						l2 = 1;
					}

					if (l > 248) {
						l = 248;
					}

					if (i3 < 0) {
						i3 = 0;
					}

					if (i1 > 16) {
						i1 = 16;
					}

					boolean flag3 = false;

					for (int j1 = k2; !flag3 && j1 < k; ++j1) {
						for (int k1 = i3; !flag3 && k1 < i1; ++k1) {
							for (int l1 = l + 1; !flag3 && l1 >= l2 - 1; --l1) {
								if (l1 >= 0 && l1 < 256) {
									if (isOceanBlock(primer, j1, l1, k1, chunkX, chunkZ)) {
										flag3 = true;
									}
									if (l1 != l2 - 1 && j1 != k2 && j1 != k - 1 && k1 != i3 && k1 != i1 - 1) {
										l1 = l2;
									}
								}
							}
						}
					}
					if (!flag3) {
						BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

						for (int j3 = k2; j3 < k; ++j3) {
							double d10 = ((double) (j3 + chunkX * 16) + 0.5D - x) / d2;

							for (int i2 = i3; i2 < i1; ++i2) {
								double d8 = ((double) (i2 + chunkZ * 16) + 0.5D - z) / d2;
								boolean flag1 = false;

								if (d10 * d10 + d8 * d8 < 1.0D) {
									for (int j2 = l; j2 > l2; --j2) {
										double d9 = ((double) (j2 - 1) + 0.5D - y) / d3;

										if (d9 > -0.7D && d10 * d10 + d9 * d9 + d8 * d8 < 1.0D) {
											IBlockState iblockstate1 = primer.getBlockState(j3, j2, i2);
											IBlockState iblockstate2 = (IBlockState) Objects.firstNonNull(primer.getBlockState(j3, j2 + 1, i2), BLK_AIR);
											IBlockState iblockstate3 = primer.getBlockState(j3, j2 - 1, i2);

											if (isTopBlock(primer, j3, j2, i2, chunkX, chunkZ)) {
												flag1 = true;
											}
												decorateBlock(primer, j3, j2, i2, chunkX, chunkZ, flag1, iblockstate3, iblockstate2);
											break;
										}
									}
								}
							}
						}
						if (flag2) {
							break;
						}
					}
				}
			}
		}
	}

	protected boolean canReplaceBlock(IBlockState digBlock, IBlockState digBlock2) {
		return digBlock.getBlock() == Blocks.STONE ? true : (digBlock.getBlock() == Blocks.DIRT ? true : (digBlock.getBlock() == Blocks.GRASS ? true : (digBlock.getBlock() == Blocks.HARDENED_CLAY ? true : (digBlock.getBlock() == Blocks.STAINED_HARDENED_CLAY ? true : (digBlock.getBlock() == Blocks.SANDSTONE ? true : (digBlock.getBlock() == Blocks.RED_SANDSTONE ? true : (digBlock.getBlock() == Blocks.MYCELIUM ? true : (digBlock.getBlock() == Blocks.SNOW_LAYER ? true : (digBlock.getBlock() == Blocks.SAND || digBlock.getBlock() == Blocks.GRAVEL) && digBlock2.getMaterial() != Material.WATER))))))));
	}

	protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int x, int z, ChunkPrimer chunkPrimerIn) {
		int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);

		if (this.rand.nextInt(7) != 0) {
			i = 0;
		}

		for (int j = 0; j < i; ++j) {
			double d0 = (double) (chunkX * 16 + this.rand.nextInt(16));
			double d1 = (double) this.rand.nextInt(this.rand.nextInt(120) + 8);
			double d2 = (double) (chunkZ * 16 + this.rand.nextInt(16));
			int k = 1;
			if (this.rand.nextInt(1) == 0) {
				this.addRoom(this.rand.nextLong(), x, z, chunkPrimerIn, d0, d1, d2);
				k += this.rand.nextInt(4);
			}
		}
	}

	protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
		net.minecraft.block.Block block = data.getBlockState(x, y, z).getBlock();
		return block == Blocks.FLOWING_WATER || block == Blocks.WATER;
	}

	// Exception biomes to make sure we generate like vanilla
	private boolean isExceptionBiome(net.minecraft.world.biome.Biome biome) {
		if (biome == net.minecraft.init.Biomes.BEACH)
			return true;
		if (biome == net.minecraft.init.Biomes.DESERT)
			return true;
		return false;
	}

	private boolean isTopBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
		net.minecraft.world.biome.Biome biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState state = data.getBlockState(x, y, z);
		return (isExceptionBiome(biome) ? state.getBlock() == Blocks.GRASS : state.getBlock() == biome.topBlock);
	}


	protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up) {
		net.minecraft.world.biome.Biome biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState top = biome.topBlock;
		IBlockState filler = biome.fillerBlock;

		if (this.canReplaceBlock(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock()) {
			data.setBlockState(x, y, z, BLK_AIR);
			if(ModBlocks.goldPile.canPlaceBlockAt(worldObj, new BlockPos(x, y, z))) {
				data.setBlockState(x, y, z, ModBlocks.goldPile.getDefaultState());
			}
			if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock()) {
				data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
			}
		}
	}

	protected void decorateBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundBottom, IBlockState down, IBlockState up) {
		net.minecraft.world.biome.Biome biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState top = biome.topBlock;
		IBlockState filler = biome.fillerBlock;
		if(!down.isFullBlock() && ModBlocks.goldPile.canPlaceBlockAt(worldObj, new BlockPos(x, y, z))) {
			data.setBlockState(x, y, z, ModBlocks.goldPile.getDefaultState());
		}
	}
}