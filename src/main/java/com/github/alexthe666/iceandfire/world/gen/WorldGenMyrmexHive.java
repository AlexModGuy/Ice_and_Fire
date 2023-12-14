package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockMyrmexBiolight;
import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldGenMyrmexHive extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    private static final BlockState DESERT_RESIN = IafBlockRegistry.MYRMEX_DESERT_RESIN.get().defaultBlockState();
    private static final BlockState STICKY_DESERT_RESIN = IafBlockRegistry.MYRMEX_DESERT_RESIN_STICKY.get().defaultBlockState();
    private static final BlockState JUNGLE_RESIN = IafBlockRegistry.MYRMEX_JUNGLE_RESIN.get().defaultBlockState();
    private static final BlockState STICKY_JUNGLE_RESIN = IafBlockRegistry.MYRMEX_JUNGLE_RESIN_STICKY.get().defaultBlockState();
    public MyrmexHive hive;
    private int entrances = 0;
    private int totalRooms;
    private boolean hasFoodRoom;
    private boolean hasNursery;
    private boolean small;
    private final boolean jungle;
    private BlockPos centerOfHive;

    public WorldGenMyrmexHive(boolean small, boolean jungle, Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
        this.small = small;
        this.jungle = jungle;
    }

    public boolean placeSmallGen(WorldGenLevel worldIn, RandomSource rand, BlockPos pos) {
        hasFoodRoom = false;
        hasNursery = false;
        totalRooms = 0;
        entrances = 0;
        centerOfHive = pos;
        generateMainRoom(worldIn, rand, pos);
        this.small = false;
        return false;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos pos = context.origin();
        if (!small) {
            if (rand.nextInt(IafConfig.myrmexColonyGenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, pos) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, pos, getId())) {
                return false;
            }
            if (MyrmexWorldData.get(worldIn.getLevel()) != null && MyrmexWorldData.get(worldIn.getLevel()).getNearestHive(pos, 200) != null) {
                return false;
            }
        }
        if (!small && !worldIn.getFluidState(pos.below()).isEmpty()) {
            return false;
        }
        hasFoodRoom = false;
        hasNursery = false;
        totalRooms = 0;
        int down = Math.max(15, pos.getY() - 20 + rand.nextInt(10));
        BlockPos undergroundPos = new BlockPos(pos.getX(), down, pos.getZ());
        entrances = 0;
        centerOfHive = undergroundPos;
        generateMainRoom(worldIn, rand, undergroundPos);
        this.small = false;
        return true;
    }

    private void generateMainRoom(ServerLevelAccessor world, RandomSource rand, BlockPos position) {
        hive = new MyrmexHive(world.getLevel(), position, 100);
        MyrmexWorldData.addHive(world.getLevel(), hive);
        BlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphere(world, rand, position, 14, 7, resin, sticky_resin);
        generateSphere(world, rand, position, 12, 5, Blocks.AIR.defaultBlockState());
        decorateSphere(world, rand, position, 12, 5, RoomType.QUEEN);
        generatePath(world, rand, position.relative(Direction.NORTH, 9).below(), 15 + rand.nextInt(10), Direction.NORTH, 100);
        generatePath(world, rand, position.relative(Direction.SOUTH, 9).below(), 15 + rand.nextInt(10), Direction.SOUTH, 100);
        generatePath(world, rand, position.relative(Direction.WEST, 9).below(), 15 + rand.nextInt(10), Direction.WEST, 100);
        generatePath(world, rand, position.relative(Direction.EAST, 9).below(), 15 + rand.nextInt(10), Direction.EAST, 100);
        if (!small) {
            EntityMyrmexQueen queen = new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(), world.getLevel());
            BlockPos ground = MyrmexHive.getGroundedPos(world, position);
            queen.finalizeSpawn(world, world.getCurrentDifficultyAt(ground), MobSpawnType.CHUNK_GENERATION, null, null);
            queen.setHive(hive);
            queen.setJungleVariant(jungle);
            queen.absMoveTo(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
            world.addFreshEntity(queen);

            for (int i = 0; i < 4 + rand.nextInt(3); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(),
                    world.getLevel());
                myrmex.finalizeSpawn(world, world.getCurrentDifficultyAt(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(hive);
                myrmex.absMoveTo(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                world.addFreshEntity(myrmex);
            }
            for (int i = 0; i < 2 + rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSoldier(IafEntityRegistry.MYRMEX_SOLDIER.get(),
                    world.getLevel());
                myrmex.finalizeSpawn(world, world.getCurrentDifficultyAt(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(hive);
                myrmex.absMoveTo(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                world.addFreshEntity(myrmex);
            }
            for (int i = 0; i < rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSentinel(IafEntityRegistry.MYRMEX_SENTINEL.get(),
                    world.getLevel());
                myrmex.finalizeSpawn(world, world.getCurrentDifficultyAt(ground), MobSpawnType.CHUNK_GENERATION, null, null);
                myrmex.setHive(hive);
                myrmex.absMoveTo(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                world.addFreshEntity(myrmex);
            }
        }
    }

    private void generatePath(LevelAccessor world, RandomSource rand, BlockPos offset, int length, Direction direction, int roomChance) {
        if (roomChance == 0) {
            return;
        }
        if (small) {
            length /= 2;
            if (entrances < 1) {
                for (int i = 0; i < length; i++) {
                    generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                }
                generateEntrance(world, rand, offset.relative(direction, length), 4, 4, direction);
            } else if (totalRooms < 2) {
                for (int i = 0; i < length; i++) {
                    generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                }
                generateRoom(world, rand, offset.relative(direction, length), 6, 4, roomChance / 2, direction);
                for (int i = -3; i < 3; i++) {
                    generateCircleAir(world, rand, offset.relative(direction, i), 3, 5, direction);
                    generateCircleAir(world, rand, offset.relative(direction, length + i), 3, 5, direction);
                }
                totalRooms++;
            }
        } else {
            if (rand.nextInt(100) < roomChance) {
                if (entrances < 3 && rand.nextInt(1 + entrances * 2) == 0 && hasFoodRoom && hasNursery && totalRooms > 3 || entrances == 0) {
                    generateEntrance(world, rand, offset.relative(direction, 1), 4, 4, direction);
                } else {
                    for (int i = 0; i < length; i++) {
                        generateCircle(world, rand, offset.relative(direction, i), 3, 5, direction);
                    }
                    for (int i = -3; i < 3; i++) {
                        generateCircleAir(world, rand, offset.relative(direction, length + i), 3, 5, direction);
                    }
                    totalRooms++;
                    generateRoom(world, rand, offset.relative(direction, length), 7, 4, roomChance / 2, direction);
                }
            }
        }
    }

    private void generateRoom(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, int roomChance, Direction direction) {
        BlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        RoomType type = RoomType.random(rand);
        if (!hasFoodRoom) {
            type = RoomType.FOOD;
            hasFoodRoom = true;
        } else if (!hasNursery) {
            type = RoomType.NURSERY;
            hasNursery = true;
        }
        generateSphereRespectResin(world, rand, position, size + 2, height + 2, resin, sticky_resin);
        generateSphere(world, rand, position, size, height - 1, Blocks.AIR.defaultBlockState());
        decorateSphere(world, rand, position, size, height - 1, type);
        hive.addRoom(position, type);
        if (!small) {
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.NORTH) {
                generatePath(world, rand, position.relative(Direction.NORTH, size - 2), 5 + rand.nextInt(20), Direction.NORTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.SOUTH) {
                generatePath(world, rand, position.relative(Direction.SOUTH, size - 2), 5 + rand.nextInt(20), Direction.SOUTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.WEST) {
                generatePath(world, rand, position.relative(Direction.WEST, size - 2), 5 + rand.nextInt(20), Direction.WEST, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != Direction.EAST) {
                generatePath(world, rand, position.relative(Direction.EAST, size - 2), 5 + rand.nextInt(20), Direction.EAST, roomChance);
            }
        }
    }

    private void generateEntrance(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockPos up = position.above();
        hive.getEntranceBottoms().put(up, direction);
        while (up.getY() < world.getHeightmapPos(small ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : Heightmap.Types.WORLD_SURFACE_WG, up).getY()
                && ! world.getBlockState(up).is(BlockTags.LOGS))
        {
            generateCircleRespectSky(world, rand, up, size, height, direction);
            up = up.above().relative(direction);
        }
        BlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphereRespectAir(world, rand, up, size + 4, height + 2, resin, sticky_resin);
        generateSphere(world, rand, up.above(), size, height, Blocks.AIR.defaultBlockState());
        decorateSphere(world, rand, up.above(), size, height - 1, RoomType.ENTERANCE);
        hive.getEntrances().put(up, direction);
        entrances++;
    }

    private void generateCircle(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i);
                    int z = (int) Math.floor(Mth.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlock(position.offset(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);

                    } else {
                        world.setBlock(position.offset(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 2);
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Mth.cos(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlock(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 2);
                    } else {
                        world.setBlock(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }

    private void generateCircleRespectSky(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        BlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        BlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i);
                    int z = (int) Math.floor(Mth.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        if (!world.canSeeSkyFromBelowWater(position.offset(0, x, z))) {
                            world.setBlock(position.offset(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                        }

                    } else {
                        if (!world.canSeeSkyFromBelowWater(position.offset(x, z, 0))) {
                            world.setBlock(position.offset(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin, 3);
                        }
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Mth.cos(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlock(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 3);
                    } else {
                        world.setBlock(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }


    private void generateCircleAir(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        int radius = size;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Mth.cos(j) * i * Mth.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        world.setBlock(position.offset(0, x, z), Blocks.AIR.defaultBlockState(), 2);
                    } else {
                        world.setBlock(position.offset(x, z, 0), Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }

    public void generateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (blockpos.distSqr(position) <= f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F) && !world.isEmptyBlock(blockpos)) {
                world.setBlock(blockpos, fill, 3);
            }
        }
    }

    public void generateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (blockpos.distSqr(position) <= f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)) {
                world.setBlock(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectResin(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (blockpos.distSqr(position) <= f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)
                && (!world.isEmptyBlock(blockpos) || world.isEmptyBlock(blockpos) && !hasResinUnder(blockpos, world))) {
                world.setBlock(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    public void generateSphereRespectAir(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, BlockState fill, BlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (blockpos.distSqr(position) <= f * f * Mth.clamp(rand.nextFloat(), 0.75F, 1.0F)
                && !world.isEmptyBlock(blockpos)) {
                world.setBlock(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 2);
            }
        }
    }

    private boolean hasResinUnder(BlockPos pos, LevelAccessor world) {
        BlockPos copy = pos.below();
        while (world.isEmptyBlock(copy) && copy.getY() > 1) {
            copy = copy.below();
        }
        return world.getBlockState(copy).getBlock() instanceof BlockMyrmexResin || world.getBlockState(copy).getBlock() instanceof BlockMyrmexConnectedResin;
    }

    private void decorateCircle(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, Direction direction) {
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Mth.sin(j) * i);
                    int z = (int) Math.floor(Mth.cos(j) * i);
                    if (direction == Direction.WEST || direction == Direction.EAST) {
                        if (world.isEmptyBlock(position.offset(0, x, z))) {
                            decorate(world, position.offset(0, x, z), position, size, rand, RoomType.TUNNEL);
                        }
                        if (world.isEmptyBlock(position.offset(0, x, z))) {
                            decorateTubers(world, position.offset(0, x, z), rand, RoomType.TUNNEL);
                        }
                    } else {
                        if (world.isEmptyBlock(position.offset(x, z, 0))) {
                            decorate(world, position.offset(x, z, 0), position, size, rand, RoomType.TUNNEL);
                        }
                        if (world.isEmptyBlock(position.offset(0, x, z))) {
                            decorateTubers(world, position.offset(0, x, z), rand, RoomType.TUNNEL);
                        }
                    }
                }
            }
        }
    }

    private void decorateSphere(LevelAccessor world, RandomSource rand, BlockPos position, int size, int height, RoomType roomType) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k + 1, l)).map(BlockPos::immutable).collect(Collectors.toSet())) {
            if (blockpos.distSqr(position) <= f * f) {
                if (world.getBlockState(blockpos.below()).canOcclude() && world.isEmptyBlock(blockpos)) {
                    decorate(world, blockpos, position, size, rand, roomType);
                }
                if (world.isEmptyBlock(blockpos)) {
                    decorateTubers(world, blockpos, rand, roomType);
                }
            }
        }
    }

    private void decorate(LevelAccessor world, BlockPos blockpos, BlockPos center, int size, RandomSource random, RoomType roomType) {
        switch (roomType) {
            case FOOD:
                if (random.nextInt(45) == 0 && world.getBlockState(blockpos.below()).getBlock() instanceof BlockMyrmexResin) {
                    WorldGenMyrmexDecoration.generateSkeleton(world, blockpos, center, size, random);
                }
                if (random.nextInt(13) == 0) {
                    WorldGenMyrmexDecoration.generateLeaves(world, blockpos, center, size, random, jungle);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generatePumpkins(world, blockpos, center, size, random, jungle);
                }
                if (random.nextInt(6) == 0) {
                    WorldGenMyrmexDecoration.generateMushrooms(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, jungle, jungle ? WorldGenMyrmexDecoration.JUNGLE_MYRMEX_FOOD_CHEST : WorldGenMyrmexDecoration.DESERT_MYRMEX_FOOD_CHEST);
                }
                break;
            case NURSERY:
                break;
            case SHINY:
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateGold(world, blockpos, center, size, random);
                }
                break;
            case TRASH:
                if (random.nextInt(24) == 0) {
                    WorldGenMyrmexDecoration.generateTrashHeap(world, blockpos, center, size, random);
                }
                if (random.nextBoolean()) {
                    WorldGenMyrmexDecoration.generateTrashOre(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, jungle, WorldGenMyrmexDecoration.MYRMEX_TRASH_CHEST);
                }
                break;
            default:
                break;
        }

    }

    private void decorateTubers(LevelAccessor world, BlockPos blockpos, RandomSource random, RoomType roomType) {
        if (world.getBlockState(blockpos.above()).canOcclude() && random.nextInt(roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 20 : 6) == 0) {
            int tuberLength = roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 1 : roomType == RoomType.QUEEN ? 1 + random.nextInt(5) : 1 + random.nextInt(3);
            for (int i = 0; i < tuberLength; i++) {
                if (world.isEmptyBlock(blockpos.below(i))) {
                    boolean connected = i != tuberLength - 1;
                    world.setBlock(blockpos.below(i), jungle ? IafBlockRegistry.MYRMEX_JUNGLE_BIOLIGHT.get().defaultBlockState().setValue(BlockMyrmexBiolight.CONNECTED_DOWN, connected) : IafBlockRegistry.MYRMEX_DESERT_BIOLIGHT.get().defaultBlockState().setValue(BlockMyrmexBiolight.CONNECTED_DOWN, connected), 2);
                }
            }
        }
    }


    public enum RoomType {
        DEFAULT(false),
        TUNNEL(false),
        ENTERANCE(false),
        QUEEN(false),
        FOOD(true),
        EMPTY(true),
        NURSERY(true),
        SHINY(true),
        TRASH(true);
        boolean random;

        RoomType(boolean random) {
            this.random = random;
        }

        public static RoomType random(RandomSource rand) {
            List<RoomType> list = new ArrayList<RoomType>();
            for (RoomType type : RoomType.values()) {
                if (type.random) {
                    list.add(type);
                }
            }
            return list.get(rand.nextInt(list.size()));
        }
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "myrmex_hive";
    }
}
