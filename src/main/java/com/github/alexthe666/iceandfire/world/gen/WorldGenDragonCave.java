package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.util.ShapeBuilder;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class WorldGenDragonCave extends Feature<NoneFeatureConfiguration> implements TypedFeature {
    public ResourceLocation DRAGON_CHEST;
    public ResourceLocation DRAGON_MALE_CHEST;
    public WorldGenCaveStalactites CEILING_DECO;
    public BlockState PALETTE_BLOCK1;
    public BlockState PALETTE_BLOCK2;
    public TagKey<Block> dragonTypeOreTag;
    public BlockState TREASURE_PILE;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public boolean isMale;

    protected WorldGenDragonCave(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        if (rand.nextInt(IafConfig.generateDragonDenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position, getId(), getFeatureType())) {
            return false;
        }
        isMale = rand.nextBoolean();
        ChunkPos chunkPos = worldIn.getChunk(position).getPos();


        int j = 40;
        // Update the position so it doesn't go above the ocean floor
        for(int k = 0; k < 20; ++k) {
            for(int l = 0; l < 20; ++l) {
                j = Math.min(j, worldIn.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, position.getX() + k, position.getZ() + l));
            }
        }

        // Offset the position randomly
        j -= 20;
        j -= rand.nextInt(30);

        // If the cave generation point is too low
        if (j < worldIn.getMinBuildHeight() + 20) {
            return false;
        }
        // Center the position at the "middle" of the chunk
        position = new BlockPos((chunkPos.x << 4) + 8, j, (chunkPos.z << 4) + 8);
        int dragonAge = 75 + rand.nextInt(50);
        int radius = (int) (dragonAge * 0.2F) + rand.nextInt(4);
        generateCave(worldIn, radius, 3, position, rand);
        EntityDragonBase dragon = createDragon(worldIn, rand, position, dragonAge);
        worldIn.addFreshEntity(dragon);
        return true;
    }

    public void generateCave(LevelAccessor worldIn, int radius, int amount, BlockPos center, RandomSource rand) {
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

    public void createShell(LevelAccessor worldIn, RandomSource rand, Set<BlockPos> positions) {
        ITagManager<Block> tagManager = ForgeRegistries.BLOCKS.tags();

        List<Block> rareOres = getBlockList(tagManager, IafBlockTags.DRAGON_CAVE_RARE_ORES);
        List<Block> uncommonOres = getBlockList(tagManager, IafBlockTags.DRAGON_CAVE_UNCOMMON_ORES);
        List<Block> commonOres = getBlockList(tagManager, IafBlockTags.DRAGON_CAVE_COMMON_ORES);
        List<Block> dragonTypeOres = getBlockList(tagManager, dragonTypeOreTag);

        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BaseEntityBlock) && worldIn.getBlockState(blockPos).getDestroySpeed(worldIn, blockPos) >= 0) {
                boolean doOres = rand.nextInt(IafConfig.oreToStoneRatioForDragonCaves + 1) == 0;

                if (doOres) {
                    Block toPlace = null;

                    if (rand.nextBoolean()) {
                        toPlace = !dragonTypeOres.isEmpty() ? dragonTypeOres.get(rand.nextInt(dragonTypeOres.size())) : null;
                    } else {
                        double chance = rand.nextDouble();

                        if (!rareOres.isEmpty() && chance <= 0.15) {
                            toPlace = rareOres.get(rand.nextInt(rareOres.size()));
                        } else if (!uncommonOres.isEmpty() && chance <= 0.45) {
                            toPlace = uncommonOres.get(rand.nextInt(uncommonOres.size()));
                        } else if (!commonOres.isEmpty()) {
                            toPlace = commonOres.get(rand.nextInt(commonOres.size()));
                        }
                    }

                    if (toPlace != null) {
                        worldIn.setBlock(blockPos, toPlace.defaultBlockState(), Block.UPDATE_CLIENTS);
                    } else {
                        worldIn.setBlock(blockPos, rand.nextBoolean() ? PALETTE_BLOCK1 : PALETTE_BLOCK2, Block.UPDATE_CLIENTS);
                    }
                } else {
                    worldIn.setBlock(blockPos, rand.nextBoolean() ? PALETTE_BLOCK1 : PALETTE_BLOCK2, Block.UPDATE_CLIENTS);
                }
            }
        });
    }

    private List<Block> getBlockList(final ITagManager<Block> tagManager, final TagKey<Block> tagKey) {
        if (tagManager == null) {
            return List.of();
        }

        return tagManager.getTag(tagKey).stream().toList();
    }

    public void hollowOut(LevelAccessor worldIn, Set<BlockPos> positions) {
        positions.forEach(blockPos -> {
            if (!(worldIn.getBlockState(blockPos).getBlock() instanceof BaseEntityBlock)) {
                worldIn.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        });
    }

    public void decorateCave(LevelAccessor worldIn, RandomSource rand, Set<BlockPos> positions, List<SphereInfo> spheres, BlockPos center) {
        for (SphereInfo sphere : spheres) {
            BlockPos pos = sphere.pos;
            int radius = sphere.radius;

            for (int i = 0; i < 15 + rand.nextInt(10); i++) {
                CEILING_DECO.generate(worldIn, rand, pos.above(radius / 2 - 1).offset(rand.nextInt(radius) - radius / 2, 0, rand.nextInt(radius) - radius / 2));
            }
        }

        positions.forEach(blockPos -> {
            if (blockPos.getY() < center.getY()) {
                BlockState stateBelow = worldIn.getBlockState(blockPos.below());

                if ((stateBelow.is(BlockTags.BASE_STONE_OVERWORLD) || stateBelow.is(IafBlockTags.DRAGON_ENVIRONMENT_BLOCKS)) && worldIn.getBlockState(blockPos).isAir()) {
                    setGoldPile(worldIn, blockPos, rand);
                }
            }
        });
    }

    public void setGoldPile(LevelAccessor world, BlockPos pos, RandomSource rand) {
        if (!(world.getBlockState(pos).getBlock() instanceof BaseEntityBlock)) {
            int chance = rand.nextInt(99) + 1;
            if (chance < 60) {
                int goldRand = Math.max(1, IafConfig.dragonDenGoldAmount) * (isMale ? 1 : 2);
                boolean generateGold = rand.nextInt(goldRand) == 0;
                world.setBlock(pos, generateGold ? TREASURE_PILE.setValue(BlockGoldPile.LAYERS, 1 + rand.nextInt(7)) : Blocks.AIR.defaultBlockState(), 3);
            } else if (chance == 61) {
                world.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, HORIZONTALS[rand.nextInt(3)]), Block.UPDATE_CLIENTS);

                if (world.getBlockState(pos).getBlock() instanceof ChestBlock) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);

                    if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
                        chestBlockEntity.setLootTable(isMale ? DRAGON_MALE_CHEST : DRAGON_CHEST, rand.nextLong());
                    }
                }
            }
        }
    }

    private EntityDragonBase createDragon(final WorldGenLevel worldGen, final RandomSource random, final BlockPos position, int dragonAge) {
        EntityDragonBase dragon = getDragonType().create(worldGen.getLevel());
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(random.nextInt(4));
        dragon.absMoveTo(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, random.nextFloat() * 360, 0);
        dragon.setInSittingPose(true);
        dragon.homePos = new HomePosition(position, worldGen.getLevel());
        dragon.setHunger(50);
        return dragon;
    }

    public abstract EntityType<? extends EntityDragonBase> getDragonType();

    private static class SphereInfo {
        int radius;
        BlockPos pos;

        private SphereInfo(int radius, BlockPos pos) {
            this.radius = radius;
            this.pos = pos;
        }
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.UNDERGROUND;
    }

    @Override
    public String getId() {
        return "dragon_cave";
    }
}
