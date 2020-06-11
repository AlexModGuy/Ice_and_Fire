package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldGenMausoleum extends Feature<NoFeatureConfig> {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(IceAndFire.MODID, "dread_mausoleum_forge");
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public Direction facing;

    public WorldGenMausoleum(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }


    public static boolean isPartOfRuin(BlockState state) {
        return DragonUtils.isDreadBlock(state);
    }

    public static Rotation getRotationFromFacing(Direction facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public static BlockPos getGround(BlockPos pos, World world) {
        return getGround(pos.getX(), pos.getZ(), world);
    }

    public static BlockPos getGround(int x, int z, World world) {
        BlockPos skyPos = new BlockPos(x, world.getHeight(), z);
        while ((!world.getBlockState(skyPos).isSolid() || canHeightSkipBlock(skyPos, world)) && skyPos.getY() > 1) {
            skyPos = skyPos.down();
        }
        return skyPos;
    }

    private static boolean canHeightSkipBlock(BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof LogBlock || state.getBlock() instanceof LeavesBlock || !state.getFluidState().isEmpty();
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        facing = Direction.byHorizontalIndex(rand.nextInt(3));
        position = position.add(rand.nextInt(8) - 4, 1, rand.nextInt(8) - 4);
        MinecraftServer server = worldIn.getWorld().getServer();
        BlockPos height = getGround(position, worldIn);
        BlockState dirt = worldIn.getBlockState(height.down(2));
        TemplateManager templateManager = server.getWorld(worldIn.getDimension().getType()).getStructureTemplateManager();
        Template template = templateManager.getTemplate(STRUCTURE);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing)).addProcessor(new DreadRuinProcessor(position, null));
        BlockPos pos = height.offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        if (checkIfCanGenAt(worldIn, pos, template.getSize().getX(), template.getSize().getZ(), facing)) {
            template.addBlocksToWorld(worldIn, pos, settings, 2);
            for (BlockPos underPos : BlockPos.getAllInBox(
                    height.down().offset(facing, -template.getSize().getZ() / 2).offset(facing.rotateYCCW(), -template.getSize().getX() / 2),
                    height.down(3).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2)
            ).collect(Collectors.toSet())) {
                if (!worldIn.getBlockState(underPos).isSolid() && !worldIn.isAirBlock(underPos.up())) {
                    worldIn.setBlockState(underPos, dirt);
                    worldIn.setBlockState(underPos.down(), dirt);
                    worldIn.setBlockState(underPos.down(2), dirt);
                    worldIn.setBlockState(underPos.down(3), dirt);
                }
            }
        }
        return true;
    }

    public boolean checkIfCanGenAt(World world, BlockPos middle, int x, int z, Direction facing) {
        return !isPartOfRuin(world.getBlockState(middle.offset(facing, z / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.getOpposite(), z / 2))) &&
                !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateY(), x / 2))) && !isPartOfRuin(world.getBlockState(middle.offset(facing.rotateYCCW(), x / 2)));
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        return false;
    }
}