package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class TileEntityDragonforgeBrick extends TileEntity {

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (getConnectedTileEntity() != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return getConnectedTileEntity() != null && getConnectedTileEntity().hasCapability(capability, facing);
    }

    private ICapabilityProvider getConnectedTileEntity() {
        for (EnumFacing facing : EnumFacing.values()) {
            if (world.getTileEntity(pos.offset(facing)) != null && world.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) world.getTileEntity(pos.offset(facing));
            }
        }
        return null;
    }

}
