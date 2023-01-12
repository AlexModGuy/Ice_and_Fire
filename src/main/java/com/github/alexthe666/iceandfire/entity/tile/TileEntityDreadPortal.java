package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityDreadPortal extends BlockEntity {
    private long age;
    private BlockPos exitPortal;
    private boolean exactTeleport;

    public TileEntityDreadPortal(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DREAD_PORTAL.get(), pos, state);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putLong("Age", this.age);

        if (this.exitPortal != null) {
            //   compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }

        if (this.exactTeleport) {
            compound.putBoolean("ExactTeleport", this.exactTeleport);
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.age = compound.getLong("Age");

        if (compound.contains("ExitPortal", 10)) {
            this.exitPortal = BlockPos.ZERO;
        }

        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityDreadPortal dreadPortal) {
        ++dreadPortal.age;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean shouldRenderFace(Direction face) {
        return true;
    }
}
