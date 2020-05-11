package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockMyrmexConnectedResin extends Block {

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public BlockMyrmexConnectedResin(boolean jungle, boolean glass) {
        super(Properties.create(Material.ROCK).hardnessAndResistance(glass ? 1.5F : 3.5F).sound(glass ? SoundType.GLASS : SoundType.STONE));
        this.setDefaultState(this.getStateContainer().getBaseState().with(UP, Boolean.valueOf(false))
                .with(DOWN, Boolean.valueOf(false))
                .with(NORTH, Boolean.valueOf(false))
                .with(EAST, Boolean.valueOf(false))
                .with(SOUTH, Boolean.valueOf(false))
                .with(WEST, Boolean.valueOf(false))
        );
        if (glass) {
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_glass" : "myrmex_desert_resin_glass");
        } else {
            this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_resin_block" : "myrmex_desert_resin_block");
        }

    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.up();
        BlockPos blockpos6 = blockpos.down();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
        BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
        return super.getStateForPlacement(context)
                .with(NORTH, Boolean.valueOf(this.canFenceConnectTo(blockstate, blockstate.canBeConnectedTo(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)))
                .with(EAST, Boolean.valueOf(this.canFenceConnectTo(blockstate1, blockstate1.canBeConnectedTo(iblockreader, blockpos2, Direction.WEST), Direction.WEST)))
                .with(SOUTH, Boolean.valueOf(this.canFenceConnectTo(blockstate2, blockstate2.canBeConnectedTo(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)))
                .with(WEST, Boolean.valueOf(this.canFenceConnectTo(blockstate3, blockstate3.canBeConnectedTo(iblockreader, blockpos4, Direction.EAST), Direction.EAST)))
                .with(UP, Boolean.valueOf(this.canFenceConnectTo(blockstate4, blockstate4.canBeConnectedTo(iblockreader, blockpos5, Direction.UP), Direction.UP)))
                .with(DOWN, Boolean.valueOf(this.canFenceConnectTo(blockstate5, blockstate5.canBeConnectedTo(iblockreader, blockpos6, Direction.DOWN), Direction.DOWN)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }

    public boolean canFenceConnectTo(BlockState p_220111_1_, boolean p_220111_2_, Direction p_220111_3_) {
        return p_220111_1_.getBlock() == this;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

}
