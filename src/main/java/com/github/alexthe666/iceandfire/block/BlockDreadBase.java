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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

/*    public BlockDreadBase(float hardness, float resistance, SoundType sound) {
        super(hardness, resistance, sound);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.FALSE));
    }

    public BlockDreadBase(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(hardness, resistance, sound, slippery);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLAYER_PLACED, Boolean.FALSE));
    }*/

    public static BlockDreadBase builder(float hardness, float resistance, SoundType sound, MapColor color, NoteBlockInstrument instrument, boolean ignited) {
        Properties props = Properties.of()
                .mapColor(color)
                .sound(sound)
                .strength(hardness, resistance);
        if (instrument != null) {
            props.instrument(instrument);
        }
        if (ignited) {
            props.ignitedByLava();
        }
        return new BlockDreadBase(props);
    }

    public BlockDreadBase(Properties props) {
        super(props);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getDestroyProgress(BlockState state, @NotNull Player player, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        if (state.getValue(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.getDestroyProgress(state, player, worldIn, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PLAYER_PLACED, true);
    }

}
