package com.github.alexthe666.iceandfire.entity.tile;

import java.util.concurrent.ThreadLocalRandom;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

public class TileEntityGhostChest extends ChestTileEntity {

    public TileEntityGhostChest() {
        super(IafTileEntityRegistry.GHOST_CHEST.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return compound;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        super.openInventory(player);
        if (this.world.getDifficulty() != Difficulty.PEACEFUL) {
            EntityGhost ghost = IafEntityRegistry.GHOST.create(world);
            ghost.setPositionAndRotation(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F,
                ThreadLocalRandom.current().nextFloat() * 360F, 0);
            if (!this.world.isRemote) {
                ghost.onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(this.pos), SpawnReason.SPAWNER, null, null);
                if (!player.isCreative()) {
                    ghost.setAttackTarget(player);
                }
                ghost.enablePersistence();
                world.addEntity(ghost);
            }
            ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
            ghost.setHomePosAndDistance(this.pos, 4);
            ghost.setFromChest(true);
        }
    }

    @Override
    protected void onOpenOrClose() {
        super.onOpenOrClose();
        this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
    }
}
