package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockMyrmexConnectedResin extends BreakableBlock {

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public BlockMyrmexConnectedResin(boolean jungle, boolean glass) {
        super(
            Properties
                .of(Material.STONE)
                .strength(glass ? 1.5F : 3.5F)
                .noOcclusion()
                .dynamicShape()
                .sound(glass ? SoundType.GLASS : SoundType.STONE)
        );

        this.registerDefaultState(this.getStateDefinition().any().setValue(UP, Boolean.valueOf(false))
            .setValue(DOWN, Boolean.valueOf(false))
            .setValue(NORTH, Boolean.valueOf(false))
            .setValue(EAST, Boolean.valueOf(false))
            .setValue(SOUTH, Boolean.valueOf(false))
            .setValue(WEST, Boolean.valueOf(false))
        );
        if (glass) {
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_glass" : "myrmex_desert_resin_glass");
        } else {
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_block" : "myrmex_desert_resin_block");
        }

    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getLevel();
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
            .setValue(NORTH, Boolean.valueOf(this.canFenceConnectTo(blockstate, false, Direction.SOUTH)))
            .setValue(EAST, Boolean.valueOf(this.canFenceConnectTo(blockstate1, false, Direction.WEST)))
            .setValue(SOUTH, Boolean.valueOf(this.canFenceConnectTo(blockstate2, false, Direction.NORTH)))
            .setValue(WEST, Boolean.valueOf(this.canFenceConnectTo(blockstate3, false, Direction.EAST)))
            .setValue(UP, Boolean.valueOf(this.canFenceConnectTo(blockstate4, false, Direction.UP)))
            .setValue(DOWN, Boolean.valueOf(this.canFenceConnectTo(blockstate5, false, Direction.DOWN)));
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
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
        return stateIn.setValue(connect, Boolean.valueOf(this.canFenceConnectTo(facingState, false, facing.getOpposite())));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }

    public boolean canFenceConnectTo(BlockState state, boolean isSideSolid, Direction direction) {
        return state.getBlock() == this;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

}
