package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenMyrmexHive extends WorldGenerator {

    private static final IBlockState DESERT_RESIN = IafBlockRegistry.myrmex_resin.getDefaultState();
    private static final IBlockState STICKY_DESERT_RESIN = IafBlockRegistry.myrmex_resin_sticky.getDefaultState();
    private static final IBlockState JUNGLE_RESIN = IafBlockRegistry.myrmex_resin.getDefaultState().withProperty(BlockMyrmexResin.VARIANT, BlockMyrmexResin.EnumType.JUNGLE);
    private static final IBlockState STICKY_JUNGLE_RESIN = IafBlockRegistry.myrmex_resin_sticky.getDefaultState().withProperty(BlockMyrmexResin.VARIANT, BlockMyrmexResin.EnumType.JUNGLE);
    public MyrmexHive hive;
    private int entrances = 0;
    private int totalRooms;
    private boolean hasFoodRoom;
    private boolean hasNursery;
    private boolean small;
    private boolean jungle;
    private BlockPos centerOfHive;

    public WorldGenMyrmexHive(boolean small, boolean jungle) {
        this.small = small;
        this.jungle = jungle;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        hasFoodRoom = false;
        hasNursery = false;
        totalRooms = 0;
        BlockPos undergroundPos = new BlockPos(position.getX(), position.getY(), position.getZ());
        entrances = 0;
        centerOfHive = undergroundPos;
        generateMainRoom(worldIn, rand, undergroundPos);
        this.small = false;
        return false;
    }

    private void generateMainRoom(World world, Random rand, BlockPos position) {
        hive = new MyrmexHive(world, position, 100);
        MyrmexWorldData.addHive(world, hive);
        IBlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphere(world, rand, position, 14, 7, resin, sticky_resin);
        generateSphere(world, rand, position, 12, 5, Blocks.AIR.getDefaultState());
        decorateSphere(world, rand, position, 12, 5, RoomType.QUEEN);
        generatePath(world, rand, position.offset(EnumFacing.NORTH, 9).down(), 15 + rand.nextInt(10), EnumFacing.NORTH, 100);
        generatePath(world, rand, position.offset(EnumFacing.SOUTH, 9).down(), 15 + rand.nextInt(10), EnumFacing.SOUTH, 100);
        generatePath(world, rand, position.offset(EnumFacing.WEST, 9).down(), 15 + rand.nextInt(10), EnumFacing.WEST, 100);
        generatePath(world, rand, position.offset(EnumFacing.EAST, 9).down(), 15 + rand.nextInt(10), EnumFacing.EAST, 100);
        if (!small) {
            EntityMyrmexQueen queen = new EntityMyrmexQueen(world);
            BlockPos ground = MyrmexHive.getGroundedPos(world, position);
            queen.onInitialSpawn(world.getDifficultyForLocation(ground), null);
            queen.setHive(hive);
            queen.setJungleVariant(jungle);
            queen.setPositionAndRotation(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
            if (!world.isRemote) {
                world.spawnEntity(queen);
            }
            for (int i = 0; i < 4 + rand.nextInt(3); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexWorker(world);
                myrmex.onInitialSpawn(world.getDifficultyForLocation(ground), null);
                myrmex.setHive(hive);
                myrmex.setPositionAndRotation(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                if (!world.isRemote) {
                    world.spawnEntity(myrmex);
                }
            }
            for (int i = 0; i < 2 + rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSoldier(world);
                myrmex.onInitialSpawn(world.getDifficultyForLocation(ground), null);
                myrmex.setHive(hive);
                myrmex.setPositionAndRotation(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                if (!world.isRemote) {
                    world.spawnEntity(myrmex);
                }
            }
            for (int i = 0; i < rand.nextInt(2); i++) {
                EntityMyrmexBase myrmex = new EntityMyrmexSentinel(world);
                myrmex.onInitialSpawn(world.getDifficultyForLocation(ground), null);
                myrmex.setHive(hive);
                myrmex.setPositionAndRotation(ground.getX() + 0.5D, ground.getY() + 1D, ground.getZ() + 0.5D, 0, 0);
                myrmex.setJungleVariant(jungle);
                if (!world.isRemote) {
                    world.spawnEntity(myrmex);
                }
            }
        }
    }

    private void generatePath(World world, Random rand, BlockPos offset, int length, EnumFacing direction, int roomChance) {
        if (roomChance == 0) {
            return;
        }
        if (small) {
            length /= 2;
            if (entrances < 1) {
                for (int i = 0; i < length; i++) {
                    generateCircle(world, rand, offset.offset(direction, i), 3, 5, direction);
                }
                generateEntrance(world, rand, offset.offset(direction, length), 4, 4, direction);
            } else if (totalRooms < 2) {
                for (int i = 0; i < length; i++) {
                    generateCircle(world, rand, offset.offset(direction, i), 3, 5, direction);
                }
                generateRoom(world, rand, offset.offset(direction, length), 6, 4, roomChance / 2, direction);
                for (int i = -3; i < 3; i++) {
                    generateCircleAir(world, rand, offset.offset(direction, i), 3, 5, direction);
                    generateCircleAir(world, rand, offset.offset(direction, length + i), 3, 5, direction);
                }
                totalRooms++;
            }
        } else {
            if (rand.nextInt(100) < roomChance) {
                for (int i = 0; i < length; i++) {
                    generateCircle(world, rand, offset.offset(direction, i), 3, 5, direction);
                }
                if (entrances < 3 && rand.nextInt(1 + entrances * 2) == 0 && hasFoodRoom && hasNursery && totalRooms > 3) {
                    generateEntrance(world, rand, offset.offset(direction, length), 4, 4, direction);
                } else {
                    generateRoom(world, rand, offset.offset(direction, length), 7, 4, roomChance / 2, direction);
                    for (int i = -3; i < 3; i++) {
                        generateCircleAir(world, rand, offset.offset(direction, length + i), 3, 5, direction);
                    }
                    totalRooms++;
                }
            }
        }
    }

    private void generateRoom(World world, Random rand, BlockPos position, int size, int height, int roomChance, EnumFacing direction) {
        IBlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        RoomType type = RoomType.random(rand);
        if (!hasFoodRoom) {
            type = RoomType.FOOD;
            hasFoodRoom = true;
        } else if (!hasNursery) {
            type = RoomType.NURSERY;
            hasNursery = true;
        }
        generateSphereRespectResin(world, rand, position, size + 2, height + 2, resin, sticky_resin);
        generateSphere(world, rand, position, size, height - 1, Blocks.AIR.getDefaultState());
        decorateSphere(world, rand, position, size, height - 1, type);
        hive.addRoom(position, type);
        if (!small) {
            if (rand.nextInt(3) == 0 && direction.getOpposite() != EnumFacing.NORTH) {
                generatePath(world, rand, position.offset(EnumFacing.NORTH, size - 2), 5 + rand.nextInt(20), EnumFacing.NORTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != EnumFacing.SOUTH) {
                generatePath(world, rand, position.offset(EnumFacing.SOUTH, size - 2), 5 + rand.nextInt(20), EnumFacing.SOUTH, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != EnumFacing.WEST) {
                generatePath(world, rand, position.offset(EnumFacing.WEST, size - 2), 5 + rand.nextInt(20), EnumFacing.WEST, roomChance);
            }
            if (rand.nextInt(3) == 0 && direction.getOpposite() != EnumFacing.EAST) {
                generatePath(world, rand, position.offset(EnumFacing.EAST, size - 2), 5 + rand.nextInt(20), EnumFacing.EAST, roomChance);
            }
        }
    }

    private void generateEntrance(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        BlockPos up = position.up();
        hive.getEntranceBottoms().put(up, direction);
        while (!world.canBlockSeeSky(up)) {
            generateCircleRespectSky(world, rand, up, size, height, direction);
            up = up.up().offset(direction);
        }
        IBlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        generateSphereRespectAir(world, rand, up, size + 2, height + 2, resin, sticky_resin);
        generateSphere(world, rand, up.up(), size, height - 1, Blocks.AIR.getDefaultState());
        decorateSphere(world, rand, up.up(), size, height - 1, RoomType.ENTERANCE);
        hive.getEntrances().put(up, direction);
        entrances++;
    }

    private void generateCircle(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        IBlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        world.setBlockState(position.add(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin);

                    } else {
                        world.setBlockState(position.add(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin);
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Math.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState());
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }

    private void generateCircleRespectSky(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        IBlockState resin = jungle ? JUNGLE_RESIN : DESERT_RESIN;
        IBlockState sticky_resin = jungle ? STICKY_JUNGLE_RESIN : STICKY_DESERT_RESIN;
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        if (!world.canBlockSeeSky(position.add(0, x, z))) {
                            world.setBlockState(position.add(0, x, z), rand.nextInt(3) == 0 ? sticky_resin : resin);
                        }

                    } else {
                        if (!world.canBlockSeeSky(position.add(x, z, 0))) {
                            world.setBlockState(position.add(x, z, 0), rand.nextInt(3) == 0 ? sticky_resin : resin);
                        }
                    }
                }
            }
        }
        radius -= 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Math.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState());
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }


    private void generateCircleAir(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        int radius = size;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    int z = (int) Math.floor(Math.cos(j) * i * MathHelper.clamp(rand.nextFloat(), 0.5F, 1.0F));
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        world.setBlockState(position.add(0, x, z), Blocks.AIR.getDefaultState());
                    } else {
                        world.setBlockState(position.add(x, z, 0), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        decorateCircle(world, rand, position, size, height, direction);
    }

    public void generateSphere(World world, Random rand, BlockPos position, int size, int height, IBlockState fill) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F)) && !world.isAirBlock(blockpos)) {
                world.setBlockState(blockpos, fill, 3);
            }
        }
    }

    public void generateSphere(World world, Random rand, BlockPos position, int size, int height, IBlockState fill, IBlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 3);
            }
        }
    }

    public void generateSphereRespectResin(World world, Random rand, BlockPos position, int size, int height, IBlockState fill, IBlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))
                    && (!world.isAirBlock(blockpos) || world.isAirBlock(blockpos) && !hasResinUnder(blockpos, world))) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 3);
            }
        }
    }

    public void generateSphereRespectAir(World world, Random rand, BlockPos position, int size, int height, IBlockState fill, IBlockState fill2) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f * MathHelper.clamp(rand.nextFloat(), 0.75F, 1.0F))
                    && !world.isAirBlock(blockpos)) {
                world.setBlockState(blockpos, rand.nextInt(3) == 0 ? fill2 : fill, 3);
            }
        }
    }

    private boolean hasResinUnder(BlockPos pos, World world) {
        BlockPos copy = pos.down();
        while (world.isAirBlock(copy) && copy.getY() > 1) {
            copy = copy.down();
        }
        return world.getBlockState(copy).getBlock() instanceof BlockMyrmexResin || world.getBlockState(copy).getBlock() instanceof BlockMyrmexConnectedResin;
    }

    private void decorateCircle(World world, Random rand, BlockPos position, int size, int height, EnumFacing direction) {
        int radius = size + 2;
        {
            for (float i = 0; i < radius; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    if (direction == EnumFacing.WEST || direction == EnumFacing.EAST) {
                        if (world.isAirBlock(position.add(0, x, z))) {
                            decorate(world, position.add(0, x, z), position, size, rand, RoomType.TUNNEL);
                        }
                        if (world.isAirBlock(position.add(0, x, z))) {
                            decorateTubers(world, position.add(0, x, z), rand, RoomType.TUNNEL);
                        }
                    } else {
                        if (world.isAirBlock(position.add(x, z, 0))) {
                            decorate(world, position.add(x, z, 0), position, size, rand, RoomType.TUNNEL);
                        }
                        if (world.isAirBlock(position.add(0, x, z))) {
                            decorateTubers(world, position.add(0, x, z), rand, RoomType.TUNNEL);
                        }
                    }
                }
            }
        }
    }

    private void decorateSphere(World world, Random rand, BlockPos position, int size, int height, RoomType roomType) {
        int i2 = size;
        int ySize = rand.nextInt(2);
        int j = i2 + rand.nextInt(2);
        int k = height + ySize;
        int l = i2 + rand.nextInt(2);
        float f = (float) (j + k + l) * 0.333F;
        for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k + 2, l))) {
            if (blockpos.distanceSq(position) <= (double) (f * f)) {
                if (world.getBlockState(blockpos.down()).isFullCube() && world.isAirBlock(blockpos)) {
                    decorate(world, blockpos, position, size, rand, roomType);
                }
                if (world.isAirBlock(blockpos)) {
                    decorateTubers(world, blockpos, rand, roomType);
                }
            }
        }
    }

    private void decorate(World world, BlockPos blockpos, BlockPos center, int size, Random random, RoomType roomType) {
        switch (roomType) {
            case FOOD:
                if (random.nextInt(45) == 0 && world.getBlockState(blockpos.down()).getBlock() instanceof BlockMyrmexResin) {
                    WorldGenMyrmexDecoration.generateSkeleton(world, blockpos, center, size, random);
                }
                if (random.nextInt(13) == 0) {
                    WorldGenMyrmexDecoration.generateLeaves(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generatePumpkins(world, blockpos, center, size, random);
                }
                if (random.nextInt(6) == 0) {
                    WorldGenMyrmexDecoration.generateMushrooms(world, blockpos, center, size, random);
                }
                if (random.nextInt(12) == 0) {
                    WorldGenMyrmexDecoration.generateCocoon(world, blockpos, random, jungle, jungle ? WorldGenMyrmexDecoration.JUNGLE_MYRMEX_FOOD_CHEST : WorldGenMyrmexDecoration.DESERT_MYRMEX_FOOD_CHEST);
                }
                break;
            case NURSERY:
                if (random.nextInt(4) == 0 && !this.small) {
                    EntityMyrmexBase baby;
                    switch (random.nextInt(4)) {
                        default:
                            baby = new EntityMyrmexWorker(world);
                            break;
                        case 1:
                            baby = new EntityMyrmexSoldier(world);
                            break;
                        case 2:
                            baby = new EntityMyrmexSentinel(world);
                            break;
                        case 3:
                            baby = new EntityMyrmexRoyal(world);
                            break;
                    }
                    baby.setGrowthStage(random.nextInt(2));
                    baby.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), random.nextFloat() * 360F, 0F);
                    baby.setRotationYawHead(random.nextFloat() * 360F);
                    baby.setHive(hive);
                    baby.setJungleVariant(jungle);
                    if (!world.isRemote && !baby.isEntityInsideOpaqueBlock()) {
                        world.spawnEntity(baby);
                    }
                }
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

    private void decorateTubers(World world, BlockPos blockpos, Random random, RoomType roomType) {
        if (world.getBlockState(blockpos.up()).isOpaqueCube() && random.nextInt(roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 20 : 6) == 0) {
            int tuberLength = roomType == RoomType.ENTERANCE || roomType == RoomType.TUNNEL ? 1 : roomType == RoomType.QUEEN ? 1 + random.nextInt(5) : 1 + random.nextInt(3);
            for (int i = 0; i < tuberLength; i++) {
                if (world.isAirBlock(blockpos.down(i))) {
                    world.setBlockState(blockpos.down(i), jungle ? IafBlockRegistry.myrmex_jungle_biolight.getDefaultState() : IafBlockRegistry.myrmex_desert_biolight.getDefaultState());
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

        public static RoomType random(Random rand) {
            List<RoomType> list = new ArrayList<RoomType>();
            for (RoomType type : RoomType.values()) {
                if (type.random) {
                    list.add(type);
                }
            }
            return list.get(rand.nextInt(list.size()));
        }

    }
}
