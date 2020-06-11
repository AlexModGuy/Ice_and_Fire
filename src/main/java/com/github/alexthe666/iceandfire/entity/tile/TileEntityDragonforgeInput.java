package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;

public class TileEntityDragonforgeInput extends TileEntity implements ITickable {
    private static final int LURE_DISTANCE = 50;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private int ticksSinceDragonFire;
    private TileEntityDragonforge core = null;

    public TileEntityDragonforgeInput() {
        super(IafTileEntityRegistry.DRAGONFORGE_INPUT);
    }

    public void onHitWithFlame() {
        TileEntityDragonforge forge = getConnectedTileEntity();
        if (forge != null) {
            forge.transferPower(1);
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
            TileEntity tileentity = world.getTileEntity(pos);
            world.setBlockState(pos, getDeactivatedState());
            if (tileentity != null) {
                tileentity.validate();
                world.setTileEntity(pos, tileentity);
            }
        }
        lureDragons();
    }

    protected void lureDragons() {
        if (core != null && core.canSmelt()) {
            for (EntityDragonBase dragon : world.getEntitiesWithinAABB(EntityDragonBase.class, new AxisAlignedBB((double) pos.getX() - LURE_DISTANCE, (double) pos.getY() - LURE_DISTANCE, (double) pos.getZ() - LURE_DISTANCE, (double) pos.getX() + LURE_DISTANCE, (double) pos.getY() + LURE_DISTANCE, (double) pos.getZ() + LURE_DISTANCE))) {
                if (isFire() == (dragon.dragonType == DragonType.FIRE) && (dragon.isChained() || dragon.isTamed()) && canSeeInput(dragon, new Vec3d(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F, this.getPos().getZ() + 0.5F))) {
                    dragon.burningTarget = this.pos;
                }
            }
        } else {
            for (EntityDragonBase dragon : world.getEntitiesWithinAABB(EntityDragonBase.class, new AxisAlignedBB((double) pos.getX() - LURE_DISTANCE, (double) pos.getY() - LURE_DISTANCE, (double) pos.getZ() - LURE_DISTANCE, (double) pos.getX() + LURE_DISTANCE, (double) pos.getY() + LURE_DISTANCE, (double) pos.getZ() + LURE_DISTANCE))) {
                if (dragon.burningTarget == this.pos) {
                    dragon.burningTarget = null;
                    dragon.setBreathingFire(false);
                }
            }
        }
    }

    public void resetCore() {
        core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(dragon.getHeadPosition(), target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, dragon));
            if (rayTrace != null && rayTrace.getHitVec() != null) {
                BlockPos pos = new BlockPos(rayTrace.getHitVec());
                return world.getBlockState(pos).getBlock() instanceof BlockDragonforgeInput;
            }
        }
        return false;
    }

    private BlockState getDeactivatedState() {
        return isFire() ? IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.getDefaultState().with(BlockDragonforgeInput.ACTIVE, false) : IafBlockRegistry.DRAGONFORGE_ICE_INPUT.getDefaultState().with(BlockDragonforgeInput.ACTIVE, false);
    }

    private boolean isFire() {
        return world.getBlockState(pos).getBlock() == IafBlockRegistry.DRAGONFORGE_FIRE_INPUT;
    }

    private boolean isActive() {
        return world.getBlockState(pos).getBlock() instanceof BlockDragonforgeInput && world.getBlockState(pos).get(BlockDragonforgeInput.ACTIVE);
    }

    private void setActive() {
        TileEntity tileentity = world.getTileEntity(pos);
        world.setBlockState(this.pos, getDeactivatedState().with(BlockDragonforgeInput.ACTIVE, true));
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(pos, tileentity);
        }
    }


    private TileEntityDragonforge getConnectedTileEntity() {
        for (Direction facing : HORIZONTALS) {
            if (world.getTileEntity(pos.offset(facing)) != null && world.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) world.getTileEntity(pos.offset(facing));
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    @Override
    @javax.annotation.Nullable
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (getConnectedTileEntity() != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return getConnectedTileEntity().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

}
