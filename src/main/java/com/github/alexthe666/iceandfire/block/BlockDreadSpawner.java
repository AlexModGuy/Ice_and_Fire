package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockDreadSpawner extends SpawnerBlock implements IDreadBlock {

    public BlockDreadSpawner() {
        super(
            BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(10.0F, 10000F)
                .sound(SoundType.METAL)
                .noOcclusion()
                .dynamicShape()
        );
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDreadSpawner(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_154683_, @NotNull BlockState p_154684_, @NotNull BlockEntityType<T> p_154685_) {
        return createTickerHelper(p_154685_, IafTileEntityRegistry.DREAD_SPAWNER.get(), p_154683_.isClientSide ? TileEntityDreadSpawner::clientTick : TileEntityDreadSpawner::serverTick);
    }

}
