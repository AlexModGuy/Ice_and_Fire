package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadBase(Material materialIn, float hardness, float resistance, SoundType sound) {
        super(materialIn, hardness, resistance, sound);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    public BlockDreadBase(Material materialIn, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(materialIn, hardness, resistance, sound, slippery);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
        if (state.getValue(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.getDestroyProgress(state, player, worldIn, pos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PLAYER_PLACED, true);
    }

}
