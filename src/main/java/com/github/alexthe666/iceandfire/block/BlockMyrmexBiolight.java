package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockMyrmexBiolight extends BushBlock {

    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("down");

    public BlockMyrmexBiolight() {
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

        this.registerDefaultState(this.getStateDefinition().any().setValue(CONNECTED_DOWN, Boolean.valueOf(false)));
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return worldIn.getBlockState(blockpos).getBlock() == this || worldIn.getBlockState(blockpos).canOcclude();
    }


    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        boolean flag3 = worldIn.getBlockState(currentPos.below()).getBlock() == this;
        return stateIn.setValue(CONNECTED_DOWN, flag3);
    }

    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClientSide) {
            this.updateState(state, worldIn, pos, state.getBlock());
        }
        if (!worldIn.getBlockState(pos.above()).canOcclude() && worldIn.getBlockState(pos.above()).getBlock() != this) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public void updateState(BlockState state, Level worldIn, BlockPos pos, Block blockIn) {
        boolean flag2 = state.getValue(CONNECTED_DOWN);
        boolean flag3 = worldIn.getBlockState(pos.below()).getBlock() == this;
        if (flag2 != flag3) {
            worldIn.setBlock(pos, state.setValue(CONNECTED_DOWN, Boolean.valueOf(flag3)), 3);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN);
    }
}