package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TileEntityDragonforgeBrick extends BlockEntity {

    public TileEntityDragonforgeBrick(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK.get(), pos, state);
    }

    @Override
    public <T> net.minecraftforge.common.util.@NotNull LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (getConnectedTileEntity() != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    private ICapabilityProvider getConnectedTileEntity() {
        for (Direction facing : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(facing)) instanceof TileEntityDragonforge) {
                return level.getBlockEntity(worldPosition.relative(facing));
            }
        }
        return null;
    }

}
