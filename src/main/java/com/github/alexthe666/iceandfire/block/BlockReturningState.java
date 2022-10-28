package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockReturningState extends Block {
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    private final BlockState returnState;

    public BlockReturningState(Material materialIn, float hardness, float resistance, SoundType sound, BlockState returnToState) {
        super(
            BlockBehaviour.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .randomTicks()
        );

        this.returnState = returnToState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    public BlockReturningState(Material materialIn, float hardness, float resistance, SoundType sound, boolean slippery, BlockState returnToState) {
        super(BlockBehaviour.Properties.of(materialIn).sound(sound).strength(hardness, resistance).friction(0.98F).randomTicks());
        this.returnState = returnToState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, returnState);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
