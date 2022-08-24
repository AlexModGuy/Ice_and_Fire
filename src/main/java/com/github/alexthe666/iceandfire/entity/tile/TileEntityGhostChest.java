package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends ChestTileEntity {

    public TileEntityGhostChest() {
        super(IafTileEntityRegistry.GHOST_CHEST.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        return compound;
    }

    @Override
    public void startOpen(PlayerEntity player) {
        super.startOpen(player);
        if (this.level.getDifficulty() != Difficulty.PEACEFUL) {
            EntityGhost ghost = IafEntityRegistry.GHOST.get().create(level);
            ghost.absMoveTo(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 0.5F, this.worldPosition.getZ() + 0.5F,
                ThreadLocalRandom.current().nextFloat() * 360F, 0);
            if (!this.level.isClientSide) {
                ghost.finalizeSpawn((ServerWorld) level, level.getCurrentDifficultyAt(this.worldPosition), SpawnReason.SPAWNER, null, null);
                if (!player.isCreative()) {
                    ghost.setTarget(player);
                }
                ghost.setPersistenceRequired();
                level.addFreshEntity(ghost);
            }
            ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
            ghost.restrictTo(this.worldPosition, 4);
            ghost.setFromChest(true);
        }
    }

    @Override
    protected void signalOpenCount() {
        super.signalOpenCount();
        this.level.updateNeighborsAt(this.worldPosition.below(), this.getBlockState().getBlock());
    }
}
