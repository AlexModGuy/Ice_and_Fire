package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityDreadPortal extends TileEntity implements ITickableTileEntity {
    private long age;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    public TileEntityDreadPortal() {
        super(IafTileEntityRegistry.DREAD_PORTAL.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putLong("Age", this.age);

        if (this.exitPortal != null) {
            //   compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }

        if (this.exactTeleport) {
            compound.putBoolean("ExactTeleport", this.exactTeleport);
        }

        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        this.age = compound.getLong("Age");

        if (compound.contains("ExitPortal", 10)) {
            this.exitPortal = BlockPos.ZERO;
        }

        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }

    @Override
    public double getViewDistance() {
        return 65536.0D;
    }

    @Override
    public void tick() {
        ++this.age;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        load(this.getBlockState(), packet.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }
}
