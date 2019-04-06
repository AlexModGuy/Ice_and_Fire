package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDragonforgeInput extends TileEntity implements ITickable {
    private int ticksSinceDragonFire = 0;

    @Override
    public void update() {
        if (ticksSinceDragonFire > 0) {
            ticksSinceDragonFire--;
        }
        if ((ticksSinceDragonFire == 0 || getConnectedTileEntity() == null) && this.isActive()) {
            world.setBlockState(pos, getDeactivatedState());
        }
    }

    private IBlockState getDeactivatedState() {
        boolean fire = world.getBlockState(pos).getBlock() == ModBlocks.dragonforge_fire_input;
        return fire ? ModBlocks.dragonforge_fire_input.getDefaultState().withProperty(BlockDragonforgeInput.ACTIVE, false) : ModBlocks.dragonforge_ice_input.getDefaultState().withProperty(BlockDragonforgeInput.ACTIVE, false);
    }

    private boolean isActive() {
        return world.getBlockState(pos).getBlock() instanceof BlockDragonforgeInput && world.getBlockState(pos).getValue(BlockDragonforgeInput.ACTIVE);
    }


    private TileEntityDragonforge getConnectedTileEntity() {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getTileEntity(pos.offset(facing)) != null && world.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) world.getTileEntity(pos.offset(facing));
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (getConnectedTileEntity() != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

}
