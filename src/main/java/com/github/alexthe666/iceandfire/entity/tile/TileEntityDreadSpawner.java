package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

public class TileEntityDreadSpawner extends MobSpawnerTileEntity {
    private final TileEntityType<?> type;
    private final DreadSpawnerBaseLogic spawnerLogic = new DreadSpawnerBaseLogic() {
        @Override
        public void broadcastEvent(int id) {
            TileEntityDreadSpawner.this.level.blockEvent(TileEntityDreadSpawner.this.worldPosition, Blocks.SPAWNER, id, 0);
        }

        @Override
        public World getLevel() {
            return TileEntityDreadSpawner.this.level;
        }

        @Override
        public BlockPos getPos() {
            return TileEntityDreadSpawner.this.worldPosition;
        }

        @Override
        public void setNextSpawnData(WeightedSpawnerEntity nextSpawnData) {
            super.setNextSpawnData(nextSpawnData);

            if (this.getLevel() != null) {
                BlockState BlockState = this.getLevel().getBlockState(this.getPos());
                this.getLevel().sendBlockUpdated(TileEntityDreadSpawner.this.worldPosition, BlockState, BlockState, 4);
            }
        }
    };

    public TileEntityDreadSpawner() {
        super();
        this.type = IafTileEntityRegistry.DREAD_SPAWNER.get();
    }

    @Override
    public void load(BlockState blockstate, CompoundNBT compound) {
        super.load(blockstate, compound);
        this.spawnerLogic.load(compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        this.spawnerLogic.save(compound);
        return compound;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void tick() {
        this.spawnerLogic.updateSpawner();
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
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


    @Override
    public boolean triggerEvent(int id, int type) {
        return this.spawnerLogic.onEventTriggered(id) || super.triggerEvent(id, type);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public AbstractSpawner getSpawner() {
        return this.spawnerLogic;
    }

    @Override
    public TileEntityType<?> getType() {
        return this.type != null ? this.type : super.getType();
    }
}