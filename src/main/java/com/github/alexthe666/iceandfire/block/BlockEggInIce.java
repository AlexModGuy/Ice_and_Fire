package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.EGG_IN_ICE;

public class BlockEggInIce extends BaseEntityBlock {
    public Item itemBlock;

    @SuppressWarnings("deprecation")
    public BlockEggInIce() {
        super(
            Properties
                .of()
                .mapColor(MapColor.ICE)
                .noOcclusion()
                .dynamicShape()
                .strength(0.5F)
                .dynamicShape()
                .sound(SoundType.GLASS)
        );
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityEggInIce(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, EGG_IN_ICE.get(), TileEntityEggInIce::tickEgg);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerDestroy(@NotNull Level worldIn, Player player, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity te, @NotNull ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    @Override
    public void playerWillDestroy(Level worldIn, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (worldIn.getBlockEntity(pos) != null) {
            if (worldIn.getBlockEntity(pos) instanceof TileEntityEggInIce tile) {
                tile.spawnEgg();
            }
        }
    }

}
