package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockDreadStoneFace extends HorizontalBlock implements IDreadBlock, IDragonProof {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadStoneFace() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(20F, 10000F).harvestTool(ToolType.PICKAXE).harvestLevel(3));
        this.setDefaultState(this.getStateContainer().getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(PLAYER_PLACED, Boolean.valueOf(false)));
        setRegistryName(IceAndFire.MODID, "dread_stone_face");
    }

    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
        return blockState.get(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        builder.add(PLAYER_PLACED);
    }
}
