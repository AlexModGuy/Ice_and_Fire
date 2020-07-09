package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockMyrmexBiolight extends BushBlock {

    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("down");
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

    public BlockMyrmexBiolight(boolean jungle) {
        super(Properties.create(Material.PLANTS).notSolid().variableOpacity().hardnessAndResistance(0).lightValue(7).sound(SoundType.PLANT).tickRandomly());
        this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_biolight" : "myrmex_desert_biolight");
        this.setDefaultState(this.getStateContainer().getBaseState().with(CONNECTED_DOWN, Boolean.valueOf(false)));
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn;
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isRemote) {
            this.updateState(state, worldIn, pos, state.getBlock());
        }
        if (!worldIn.getBlockState(pos.up()).isSolid()) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        boolean flag2 = state.get(CONNECTED_DOWN);
        boolean flag3 = !worldIn.getBlockState(pos.down()).isSolid();
        if (flag2 != flag3) {
            worldIn.setBlockState(pos, state.with(CONNECTED_DOWN, Boolean.valueOf(flag3)), 3);
        }

    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN);
    }
}