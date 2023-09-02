package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockMyrmexConnectedResin extends HalfTransparentBlock {

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public BlockMyrmexConnectedResin(boolean jungle, boolean glass) {
        super(
            Properties
                .of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(glass ? 1.5F : 3.5F)
                .noOcclusion()
                .dynamicShape()
                .sound(glass ? SoundType.GLASS : SoundType.STONE)
        );

        this.registerDefaultState(this.getStateDefinition().any().setValue(UP, Boolean.FALSE)
            .setValue(DOWN, Boolean.FALSE)
            .setValue(NORTH, Boolean.FALSE)
            .setValue(EAST, Boolean.FALSE)
            .setValue(SOUTH, Boolean.FALSE)
            .setValue(WEST, Boolean.FALSE)
        );
    }

    static String name(boolean glass, boolean jungle) {
        String biome = jungle ? "jungle" : "desert";
        String type = glass ? "glass" : "block";
        return "myrmex_%s_resin_%s".formatted(biome, type);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter iblockreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.above();
        BlockPos blockpos6 = blockpos.below();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
        BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
        return super.getStateForPlacement(context)
            .setValue(NORTH, this.canFenceConnectTo(blockstate, false, Direction.SOUTH))
            .setValue(EAST, this.canFenceConnectTo(blockstate1, false, Direction.WEST))
            .setValue(SOUTH, this.canFenceConnectTo(blockstate2, false, Direction.NORTH))
            .setValue(WEST, this.canFenceConnectTo(blockstate3, false, Direction.EAST))
            .setValue(UP, this.canFenceConnectTo(blockstate4, false, Direction.UP))
            .setValue(DOWN, this.canFenceConnectTo(blockstate5, false, Direction.DOWN));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState stateIn, Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        BooleanProperty connect = null;
        switch (facing) {
            case NORTH:
                connect = NORTH;
                break;
            case SOUTH:
                connect = SOUTH;
                break;
            case EAST:
                connect = EAST;
                break;
            case WEST:
                connect = WEST;
                break;
            case DOWN:
                connect = DOWN;
                break;
            default:
                connect = UP;
                break;
        }
        return stateIn.setValue(connect, this.canFenceConnectTo(facingState, false, facing.getOpposite()));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }

    public boolean canFenceConnectTo(BlockState state, boolean isSideSolid, Direction direction) {
        return state.getBlock() == this;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

}
