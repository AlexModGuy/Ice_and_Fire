package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
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
        super(
            AbstractBlock.Properties
                .of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(-1F, 10000F)
    			.harvestTool(ToolType.PICKAXE)
    			.harvestLevel(3)
		);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(PLAYER_PLACED, Boolean.valueOf(false)));
        setRegistryName(IceAndFire.MODID, "dread_stone_face");
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        if (state.getValue(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.getDestroyProgress(state, player, worldIn, pos);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(PLAYER_PLACED, true);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PLAYER_PLACED);
    }
}
