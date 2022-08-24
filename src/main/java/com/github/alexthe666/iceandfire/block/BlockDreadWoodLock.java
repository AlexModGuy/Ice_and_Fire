package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadWoodLock() {
        super(
            Properties
                .of(Material.WOOD)
                .strength(-1.0F, 1000000F)
    			.sound(SoundType.WOOD)
		);

        this.setRegistryName(IceAndFire.MODID, "dreadwood_planks_lock");
        this.registerDefaultState(this.getStateDefinition().any().setValue(PLAYER_PLACED, Boolean.valueOf(false)));
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

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult resultIn) {
        ItemStack stack = player.getItemInHand(handIn);
        if (stack.getItem() == IafItemRegistry.DREAD_KEY) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            deleteNearbyWood(worldIn, pos, pos);
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.BLOCKS, 1, 1, false);
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_OPEN, SoundCategory.BLOCKS, 1, 2, false);
        }
        return ActionResultType.SUCCESS;
    }

    private void deleteNearbyWood(World worldIn, BlockPos pos, BlockPos startPos) {
        if (pos.distSqr(startPos) < 32) {
            if (worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS || worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK) {
                worldIn.destroyBlock(pos, false);
                for (Direction facing : Direction.values()) {
                    deleteNearbyWood(worldIn, pos.relative(facing), startPos);
                }
            }
        }
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return this.defaultBlockState().setValue(PLAYER_PLACED, true);
    }
}
