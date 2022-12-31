package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockFallingReturningState extends FallingBlock {
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    private final BlockState returnState;

    public BlockFallingReturningState(Material materialIn, float hardness, float resistance, SoundType sound, BlockState revertState) {
        super(
            BlockBehaviour.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .randomTicks()
        );

        this.returnState = revertState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    public BlockFallingReturningState(Material materialIn, float hardness, float resistance, SoundType sound, boolean slippery, BlockState revertState) {
        super(
            BlockBehaviour.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .randomTicks()
        );

        this.returnState = revertState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, returnState);
            }
        }
    }


    public int getDustColor(BlockState blkst) {
        return -8356741;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
