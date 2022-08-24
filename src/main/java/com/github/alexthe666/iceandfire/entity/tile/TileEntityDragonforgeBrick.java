package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class TileEntityDragonforgeBrick extends TileEntity {

    public TileEntityDragonforgeBrick() {
        super(IafTileEntityRegistry.DRAGONFORGE_BRICK.get());
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (getConnectedTileEntity() != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
