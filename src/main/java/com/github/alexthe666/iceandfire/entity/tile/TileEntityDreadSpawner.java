package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TileEntityDreadSpawner extends SpawnerBlockEntity {
    private final BlockEntityType<?> type;
    private final DreadSpawnerBaseLogic spawner = new DreadSpawnerBaseLogic() {
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level p_155771_, BlockPos p_155772_, SpawnData p_155773_) {
            super.setNextSpawnData(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, blockstate, blockstate, 4);
            }

        }

        @javax.annotation.Nullable
        public net.minecraft.world.level.block.entity.BlockEntity getSpawnerBlockEntity() {
            return TileEntityDreadSpawner.this;
        }
    };

    public TileEntityDreadSpawner(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = IafTileEntityRegistry.DREAD_SPAWNER.get();
    }

    public void load(CompoundTag p_155760_) {
        super.load(p_155760_);
        this.spawner.load(this.level, this.worldPosition, p_155760_);
    }

    public CompoundTag save(CompoundTag p_59795_) {
        super.save(p_59795_);
        this.spawner.save(this.level, this.worldPosition, p_59795_);
        return p_59795_;
    }

    public static void clientTick(Level p_155755_, BlockPos p_155756_, BlockState p_155757_, TileEntityDreadSpawner p_155758_) {
        p_155758_.spawner.clientTick(p_155755_, p_155756_);
    }

    public static void serverTick(Level p_155762_, BlockPos p_155763_, BlockState p_155764_, TileEntityDreadSpawner p_155765_) {
        p_155765_.spawner.serverTick((ServerLevel) p_155762_, p_155763_);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.save(new CompoundTag());
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    public boolean triggerEvent(int p_59797_, int p_59798_) {
        return this.spawner.onEventTriggered(this.level, p_59797_) || super.triggerEvent(p_59797_, p_59798_);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }

    @Override
    public BlockEntityType<?> getType() {
        return this.type != null ? this.type : super.getType();
    }

}