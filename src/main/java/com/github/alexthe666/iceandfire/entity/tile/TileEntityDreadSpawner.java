package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.Nullable;

public class TileEntityDreadSpawner extends TileEntity implements ITickable {
    private final DreadSpawnerBaseLogic spawnerLogic = new DreadSpawnerBaseLogic() {
        public void broadcastEvent(int id) {
            TileEntityDreadSpawner.this.world.addBlockEvent(TileEntityDreadSpawner.this.pos, Blocks.SPAWNER, id, 0);
        }

        public World getWorld() {
            return TileEntityDreadSpawner.this.world;
        }

        public BlockPos getSpawnerPosition() {
            return TileEntityDreadSpawner.this.pos;
        }

        public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
            super.setNextSpawnData(p_184993_1_);

            if (this.getWorld() != null) {
                BlockState BlockState = this.getWorld().getBlockState(this.getSpawnerPosition());
                this.getWorld().notifyBlockUpdate(TileEntityDreadSpawner.this.pos, BlockState, BlockState, 4);
            }
        }
    };

    public TileEntityDreadSpawner() {
        super(IafTileEntityRegistry.DREAD_SPAWNER);
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.spawnerLogic.read(compound);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        this.spawnerLogic.write(compound);
        return compound;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void tick() {
        this.spawnerLogic.updateSpawner();
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        read(packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }


    public boolean receiveClientEvent(int id, int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }

    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public AbstractSpawner getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}