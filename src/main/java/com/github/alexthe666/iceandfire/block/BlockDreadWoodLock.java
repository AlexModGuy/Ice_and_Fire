package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadWoodLock() {
        super(
            Properties
                .of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .ignitedByLava()
                .strength(-1.0F, 1000000F)
                .sound(SoundType.WOOD)
        );
        this.registerDefaultState(this.getStateDefinition().any().setValue(PLAYER_PLACED, Boolean.FALSE));
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
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult resultIn) {
        ItemStack stack = player.getItemInHand(handIn);
        if (stack.getItem() == IafItemRegistry.DREAD_KEY.get()) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            deleteNearbyWood(worldIn, pos, pos);
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1, 1, false);
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1, 2, false);
        }
        return InteractionResult.SUCCESS;
    }

    private void deleteNearbyWood(Level worldIn, BlockPos pos, BlockPos startPos) {
        if (pos.distSqr(startPos) < 32) {
            if (worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS.get() || worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK.get()) {
                worldIn.destroyBlock(pos, false);
                for (Direction facing : Direction.values()) {
                    deleteNearbyWood(worldIn, pos.relative(facing), startPos);
                }
            }
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(Level worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return this.defaultBlockState().setValue(PLAYER_PLACED, true);
    }
}
