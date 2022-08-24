package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class WorldGenSirenIsland extends Feature<NoFeatureConfig> {


    public WorldGenSirenIsland(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    private int getRadius(int layer, int up) {
        return layer > up ? (int) (layer * 0.25) + up : layer;
    }

    private BlockState getStone(Random random) {
        int chance = random.nextInt(100);
        if (chance > 90) {
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        } else if (chance > 70) {
            return Blocks.GRAVEL.defaultBlockState();
        } else if (chance > 45) {
            return Blocks.COBBLESTONE.defaultBlockState();
        } else {
            return Blocks.STONE.defaultBlockState();
        }
    }

    private void spawnSiren(IServerWorld worldIn, Random rand, BlockPos position) {
        EntitySiren siren = new EntitySiren(IafEntityRegistry.SIREN.get(), worldIn.getLevel());
        siren.setSinging(true);
        siren.setHairColor(rand.nextInt(2));
        siren.setSingingPose(rand.nextInt(2));
        siren.absMoveTo(position.getX() + 0.5D, position.getY() + 1, position.getZ() + 0.5D, rand.nextFloat() * 360, 0);
        worldIn.addFreshEntity(siren);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator p_230362_3_, Random rand, BlockPos position, NoFeatureConfig p_230362_6_) {
        if (!IafWorldRegistry.isDimensionListedForFeatures(worldIn)) {
            return false;
        }
        if (!IafConfig.generateSirenIslands || rand.nextInt(IafConfig.generateSirenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position)) {
            return false;
        }
        position = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, position);

        int up = rand.nextInt(4) + 1;
        BlockPos center = position.above(up);
        int layer = 0;
        int sirens = 0;
        int sirensMax = 1 + rand.nextInt(3);
        while (!worldIn.getBlockState(center).canOcclude() && center.getY() >= 0) {
            layer++;
            for (float i = 0; i < getRadius(layer, up); i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i + rand.nextInt(2); j += 0.5) {
                    BlockPos stonePos = new BlockPos(Math.floor(center.getX() + MathHelper.sin(j) * i + rand.nextInt(2)), center.getY(), Math.floor(center.getZ() + MathHelper.cos(j) * i + rand.nextInt(2)));
                    worldIn.setBlock(stonePos, getStone(rand), 3);
                    BlockPos upPos = stonePos.above();
                    if (worldIn.isEmptyBlock(upPos) && worldIn.isEmptyBlock(upPos.east()) && worldIn.isEmptyBlock(upPos.north()) && worldIn.isEmptyBlock(upPos.north().east()) && rand.nextInt(3) == 0 && sirens < sirensMax) {
                        sirens++;
                        spawnSiren(worldIn, rand, upPos.north().east());
                    }
                }
            }
            center = center.below();
        }
        layer++;
        for (float i = 0; i < getRadius(layer, up); i += 0.5) {
            for (float j = 0; j < 2 * Math.PI * i + rand.nextInt(2); j += 0.5) {
                BlockPos stonePos = new BlockPos(Math.floor(center.getX() + MathHelper.sin(j) * i + rand.nextInt(2)), center.getY(), Math.floor(center.getZ() + MathHelper.cos(j) * i + rand.nextInt(2)));
                while (!worldIn.getBlockState(stonePos).canOcclude() && stonePos.getY() >= 0) {
                    worldIn.setBlock(stonePos, getStone(rand), 3);
                    stonePos = stonePos.below();
                }
            }
        }
        return true;
    }
}
