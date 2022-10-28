package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.util.ShapeBuilder;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class WorldGenDragonCave extends Feature<NoneFeatureConfiguration> {

    public ResourceLocation DRAGON_CHEST;
    public ResourceLocation DRAGON_MALE_CHEST;
    public WorldGenCaveStalactites CEILING_DECO;
    public BlockState PALETTE_BLOCK1;
    public BlockState PALETTE_BLOCK2;
    public BlockState PALETTE_ORE1;
    public BlockState PALETTE_ORE2;
    public BlockState TREASURE_PILE;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public boolean isMale;
    public boolean generateGemOre = false;

    protected WorldGenDragonCave(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        Random rand = context.random();
        BlockPos position = context.origin();
        if (!IafWorldRegistry.isDimensionListedForDragons(worldIn)) {
            return false;
        }
        if (!IafConfig.generateDragonDens || rand.nextInt(IafConfig.generateDragonDenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position)) {
            return false;
        }
        isMale = rand.nextBoolean();
        ChunkPos chunkPos = worldIn.getChunk(position).getPos();
        // Center the position at the "middle" of the chunk
        position = new BlockPos((chunkPos.x << 4) + 8, 20 + rand.nextInt(20), (chunkPos.z << 4) + 8);
        int dragonAge = 75 + rand.nextInt(50);
        int radius = (int) (dragonAge * 0.2F) + rand.nextInt(4);
        generateCave(worldIn, radius, 3, position, rand);
        EntityDragonBase dragon = createDragon(worldIn, rand, position, dragonAge);
        worldIn.addFreshEntity(dragon);
        return false;
    }

    public void generateCave(LevelAccessor worldIn, int radius, int amount, BlockPos center, Random rand) {
        List<SphereInfo> sphereList = new ArrayList<>();
        sphereList.add(new SphereInfo(radius, center.immutable()));
        Stream<BlockPos> sphereBlocks = ShapeBuilder.start().getAllInCutOffSphereMutable(radius, radius / 2, center).toStream(false);
        Stream<BlockPos> hollowBlocks = ShapeBuilder.start().getAllInRandomlyDistributedRangeYCutOffSphereMutable(radius - 2, (int) ((radius - 2) * 0.75), (radius - 2) / 2, rand, center).toStream(false);
        //Get shells
        //Get hollows
        for (int i = 0; i < amount + rand.nextInt(2); i++) {
            Direction direction = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
            int r = 2 * (int) (radius / 3F) + rand.nextInt(8);
            BlockPos centerOffset = center.relative(direction, radius - 2);
            sphereBlocks = Stream.concat(sphereBlocks, ShapeBuilder.start().getAllInCutOffSphereMutable(r, r, centerOffset).toStream(false));
            hollowBlocks = Stream.concat(hollowBlocks, ShapeBuilder.start().getAllInRandomlyDistributedRangeYCutOffSphereMutable(r - 2, (int) ((r - 2) * 0.75), (r - 2) / 2, rand, centerOffset).toStream(false));
            sphereList.add(new SphereInfo(r, centerOffset));
        }
        Set<BlockPos> shellBlocksSet = sphereBlocks.map(BlockPos::immutable).collect(Collectors.toSet());
        Set<BlockPos> hollowBlocksSet = hollowBlocks.map(BlockPos::immutable).collect(Collectors.toSet());
        shellBlocksSet.removeAll(hollowBlocksSet);

        //setBlocks
        createShell(worldIn, rand, shellBlocksSet);
        //removeBlocks
        hollowOut(worldIn, hollowBlocksSet);
        //decorate
        decorateCave(worldIn, rand, hollowBlocksSet, sphereList, center);
        sphereList.clear();
    }

    public void createShell(LevelAccessor worldIn, Random rand, Set<BlockPos> positions) {
        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BaseEntityBlock) && worldIn.getBlockState(blockPos).getDestroySpeed(worldIn, blockPos) >= 0) {
                boolean doOres = rand.nextInt(IafConfig.oreToStoneRatioForDragonCaves + 1) == 0;
                if (doOres) {
                    int chance = rand.nextInt(199) + 1;
                    if (chance < 30) {
                        worldIn.setBlock(blockPos, Blocks.IRON_ORE.defaultBlockState(), 2);
                    } else if (chance > 30 && chance < 40) {
                        worldIn.setBlock(blockPos, Blocks.GOLD_ORE.defaultBlockState(), 2);
                    } else if (chance > 40 && chance < 45) {
                        worldIn.setBlock(blockPos, IafConfig.generateCopperOre ? IafBlockRegistry.COPPER_ORE.get().defaultBlockState() : PALETTE_BLOCK1, 2);
                    } else if (chance > 45 && chance < 50) {
                        worldIn.setBlock(blockPos, IafConfig.generateSilverOre ? IafBlockRegistry.SILVER_ORE.get().defaultBlockState() : PALETTE_BLOCK1, 2);
                    } else if (chance > 50 && chance < 60) {
                        worldIn.setBlock(blockPos, Blocks.COAL_ORE.defaultBlockState(), 2);
                    } else if (chance > 60 && chance < 70) {
                        worldIn.setBlock(blockPos, Blocks.REDSTONE_ORE.defaultBlockState(), 2);
                    } else if (chance > 70 && chance < 80) {
                        worldIn.setBlock(blockPos, Blocks.LAPIS_ORE.defaultBlockState(), 2);
                    } else if (chance > 80 && chance < 90) {
                        worldIn.setBlock(blockPos, Blocks.DIAMOND_ORE.defaultBlockState(), 2);
                    } else if (chance > 90) {
                        worldIn.setBlock(blockPos, generateGemOre ? PALETTE_ORE1 : PALETTE_ORE2, 2);
                    }
                } else {
                    worldIn.setBlock(blockPos, rand.nextBoolean() ? PALETTE_BLOCK1 : PALETTE_BLOCK2, 2);
                }
            }
        });
    }

    public void hollowOut(LevelAccessor worldIn, Set<BlockPos> positions) {
        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BaseEntityBlock)) {
                worldIn.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
        });
    }

    public void decorateCave(LevelAccessor worldIn, Random rand, Set<BlockPos> positions, List<SphereInfo> spheres, BlockPos center) {
        for (SphereInfo sphere : spheres) {
            BlockPos pos = sphere.pos;
            int radius = sphere.radius;
            for (int i = 0; i < 15 + rand.nextInt(10); i++) {
                CEILING_DECO.generate(worldIn, rand, pos.above(radius / 2 - 1).offset(rand.nextInt(radius) - radius / 2, 0, rand.nextInt(radius) - radius / 2));
            }

        }
        int y = center.getY();
        positions.forEach(blockPos -> {
            if (blockPos.getY() < y) {
                if (worldIn.getBlockState(blockPos.below()).getMaterial() == Material.STONE && worldIn.getBlockState(blockPos).getMaterial() == Material.AIR)
                    setGoldPile(worldIn, blockPos, rand);
            }
        });
    }

    public void setGoldPile(LevelAccessor world, BlockPos pos, Random rand) {
        if (!(world.getBlockState(pos).getBlock() instanceof BaseEntityBlock)) {
            int chance = rand.nextInt(99) + 1;
            if (chance < 60) {
                int goldRand = Math.max(1, IafConfig.dragonDenGoldAmount) * (isMale ? 1 : 2);
                boolean generateGold = rand.nextInt(goldRand) == 0;
                world.setBlock(pos, generateGold ? TREASURE_PILE.setValue(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.defaultBlockState(), 3);
            } else if (chance == 61) {
                world.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[rand.nextInt(3)]), 2);
                if (world.getBlockState(pos).getBlock() instanceof ChestBlock) {
                    BlockEntity tileentity1 = world.getBlockEntity(pos);
                    if (tileentity1 instanceof ChestBlockEntity) {
                        ((ChestBlockEntity) tileentity1).setLootTable(isMale ? DRAGON_MALE_CHEST : DRAGON_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    abstract EntityDragonBase createDragon(WorldGenLevel worldIn, Random rand, BlockPos position, int dragonAge);

    private static class SphereInfo {
        int radius;
        BlockPos pos;

        private SphereInfo(int radius, BlockPos pos) {
            this.radius = radius;
            this.pos = pos;
        }
    }
}
