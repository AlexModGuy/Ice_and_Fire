package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class TileEntityDragonforgeInput extends TileEntity implements ITickableTileEntity {
    private static final int LURE_DISTANCE = 50;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private int ticksSinceDragonFire;
    private TileEntityDragonforge core = null;

    public TileEntityDragonforgeInput() {
        super(IafTileEntityRegistry.DRAGONFORGE_INPUT.get());
    }

    public void onHitWithFlame() {
        if (core != null) {
            core.transferPower(1);
        }
    }

    @Override
    public void tick() {
        if (core == null) {
            core = getConnectedTileEntity();
        }
        if (ticksSinceDragonFire > 0) {
            ticksSinceDragonFire--;
        }
        if ((ticksSinceDragonFire == 0 || core == null) && this.isActive()) {
            TileEntity tileentity = level.getBlockEntity(worldPosition);
            level.setBlockAndUpdate(worldPosition, getDeactivatedState());
            if (tileentity != null) {
                tileentity.clearRemoved();
                level.setBlockEntity(worldPosition, tileentity);
            }
        }
        if (isAssembled())
            lureDragons();

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

    protected void lureDragons() {
        Vector3d targetPosition = new Vector3d(
            this.getBlockPos().getX() + 0.5F,
            this.getBlockPos().getY() + 0.5F,
            this.getBlockPos().getZ() + 0.5F
        );

        AxisAlignedBB searchArea = new AxisAlignedBB(
            (double) worldPosition.getX() - LURE_DISTANCE,
            (double) worldPosition.getY() - LURE_DISTANCE,
            (double) worldPosition.getZ() - LURE_DISTANCE,
            (double) worldPosition.getX() + LURE_DISTANCE,
            (double) worldPosition.getY() + LURE_DISTANCE,
            (double) worldPosition.getZ() + LURE_DISTANCE
        );

        boolean dragonSelected = false;
        for (EntityDragonBase dragon : level.getEntitiesOfClass(EntityDragonBase.class, searchArea)) {
            if (!dragonSelected &&
                // Dragon Checks
                getDragonType() == DragonType.getIntFromType(dragon.dragonType) &&
                (dragon.isChained() || dragon.isTame()) &&
                canSeeInput(dragon, targetPosition)
            ) {
                dragon.burningTarget = this.worldPosition;
                dragonSelected = true;

            } else if (dragon.burningTarget == this.worldPosition) {
                dragon.burningTarget = null;
                dragon.setBreathingFire(false);
            }
        }
    }

    public boolean isAssembled() {
        return (core != null &&
            core.assembled() &&
            core.canSmelt());
    }

    public void resetCore() {
        core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vector3d target) {
        if (target != null) {
            RayTraceResult rayTrace = this.level.clip(new RayTraceContext(dragon.getHeadPosition(), target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, dragon));
            if (rayTrace != null && rayTrace.getLocation() != null) {
                double distance = dragon.getHeadPosition().distanceTo(rayTrace.getLocation());
                return distance < 10.0F + dragon.getBbWidth();
            }
        }
        return false;
    }

    private BlockState getDeactivatedState() {
        switch (getDragonType()){
            case 0:
                return IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            case 1:
                return IafBlockRegistry.DRAGONFORGE_ICE_INPUT.defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            case 2:
                return IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            default:
                return IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE,
                    false);
        }
    }

    private int getDragonType() {
        if (level.getBlockState(worldPosition).getBlock() == IafBlockRegistry.DRAGONFORGE_FIRE_INPUT) {
            return 0;
        }
        if (level.getBlockState(worldPosition).getBlock() == IafBlockRegistry.DRAGONFORGE_ICE_INPUT) {
            return 1;
        }
        if (level.getBlockState(worldPosition).getBlock() == IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT) {
            return 2;
        }
        return 0;
    }

    private boolean isActive() {
        return level.getBlockState(worldPosition).getBlock() instanceof BlockDragonforgeInput && level.getBlockState(worldPosition).getValue(BlockDragonforgeInput.ACTIVE);
    }

    private TileEntityDragonforge getConnectedTileEntity() {
        for (Direction facing : HORIZONTALS) {
            if (level.getBlockEntity(worldPosition.relative(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) level.getBlockEntity(worldPosition.relative(facing));
            }
        }
        return null;
    }
    @Override
    @javax.annotation.Nonnull
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (core != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return core.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

}
