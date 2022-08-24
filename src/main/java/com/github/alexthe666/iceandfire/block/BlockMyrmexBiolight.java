package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockMyrmexBiolight extends BushBlock {

    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("down");

    public BlockMyrmexBiolight(boolean jungle) {
        super(
            Properties
                .of(Material.PLANT)
                .noOcclusion()
                .noCollission()
                .dynamicShape()
                .strength(0)
                .lightLevel((state) -> {
                    return 7;
                })
                .sound(SoundType.GRASS).randomTicks()
        );

        this.setRegistryName(IceAndFire.MODID, jungle ? "myrmex_jungle_biolight" : "myrmex_desert_biolight");
        this.registerDefaultState(this.getStateDefinition().any().setValue(CONNECTED_DOWN, Boolean.valueOf(false)));
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return worldIn.getBlockState(blockpos).getBlock() == this || worldIn.getBlockState(blockpos).canOcclude();
    }


    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        boolean flag3 = worldIn.getBlockState(currentPos.below()).getBlock() == this;
        return stateIn.setValue(CONNECTED_DOWN, flag3);
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClientSide) {
            this.updateState(state, worldIn, pos, state.getBlock());
        }
        if (!worldIn.getBlockState(pos.above()).canOcclude() && worldIn.getBlockState(pos.above()).getBlock() != this) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        boolean flag2 = state.getValue(CONNECTED_DOWN);
        boolean flag3 = worldIn.getBlockState(pos.below()).getBlock() == this;
        if (flag2 != flag3) {
            worldIn.setBlock(pos, state.setValue(CONNECTED_DOWN, Boolean.valueOf(flag3)), 3);
        }

    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN);
    }
}