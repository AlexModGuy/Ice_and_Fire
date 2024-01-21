package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonforgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TileEntityDragonforgeInput extends BlockEntity {
    private static final int LURE_DISTANCE = 50;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private int ticksSinceDragonFire;
    private TileEntityDragonforge core = null;

    public TileEntityDragonforgeInput(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_INPUT.get(), pos, state);
    }

    public void onHitWithFlame() {
        if (core != null) {
            core.transferPower(1);
        }
    }

    public static void tick(final Level level, final BlockPos position, final BlockState state, final TileEntityDragonforgeInput forgeInput) {
        if (forgeInput.core == null) {
            forgeInput.core = forgeInput.getConnectedTileEntity(position);
        }

        if (forgeInput.ticksSinceDragonFire > 0) {
            forgeInput.ticksSinceDragonFire--;
        }

        if ((forgeInput.ticksSinceDragonFire == 0 || forgeInput.core == null) && forgeInput.isActive()) {
            BlockEntity tileentity = level.getBlockEntity(position);
            level.setBlockAndUpdate(position, forgeInput.getDeactivatedState());
            if (tileentity != null) {
                tileentity.clearRemoved();
                level.setBlockEntity(tileentity);
            }
        }

        if (forgeInput.isAssembled()) {
            forgeInput.lureDragons();
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    protected void lureDragons() {
        Vec3 targetPosition = new Vec3(
            this.getBlockPos().getX() + 0.5F,
            this.getBlockPos().getY() + 0.5F,
            this.getBlockPos().getZ() + 0.5F
        );

        AABB searchArea = new AABB(
            (double) worldPosition.getX() - LURE_DISTANCE,
            (double) worldPosition.getY() - LURE_DISTANCE,
            (double) worldPosition.getZ() - LURE_DISTANCE,
            (double) worldPosition.getX() + LURE_DISTANCE,
            (double) worldPosition.getY() + LURE_DISTANCE,
            (double) worldPosition.getZ() + LURE_DISTANCE
        );

        boolean dragonSelected = false;

        for (EntityDragonBase dragon : level.getEntitiesOfClass(EntityDragonBase.class, searchArea)) {
            if (!dragonSelected && /* Dragon Checks */ getDragonType() == dragon.dragonType.getIntFromType() && (dragon.isChained() || dragon.isTame()) && canSeeInput(dragon, targetPosition)) {
                dragon.burningTarget = this.worldPosition;
                dragonSelected = true;
            } else if (dragon.burningTarget == this.worldPosition) {
                dragon.burningTarget = null;
                dragon.setBreathingFire(false);
            }
        }
    }

    public boolean isAssembled() {
        return (core != null && core.assembled() && core.canSmelt());
    }

    public void resetCore() {
        core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vec3 target) {
        if (target != null) {
            HitResult rayTrace = this.level.clip(new ClipContext(dragon.getHeadPosition(), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, dragon));
            double distance = dragon.getHeadPosition().distanceTo(rayTrace.getLocation());

            return distance < 10.0F + dragon.getBbWidth();
        }

        return false;
    }

    private BlockState getDeactivatedState() {
        return switch (getDragonType()) {
            case 0 -> IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get().defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            case 1 -> IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get().defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            case 2 -> IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get().defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
            default -> IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get().defaultBlockState().setValue(BlockDragonforgeInput.ACTIVE, false);
        };
    }

    private int getDragonType() {
        BlockState state = level.getBlockState(worldPosition);

        if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get()) {
            return 0;
        } else if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get()) {
            return 1;
        } else if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get()) {
            return 2;
        }

        return 0;
    }

    private boolean isActive() {
        BlockState state = level.getBlockState(worldPosition);
        return state.getBlock() instanceof BlockDragonforgeInput && state.getValue(BlockDragonforgeInput.ACTIVE);
    }

    private TileEntityDragonforge getConnectedTileEntity(final BlockPos position) {
        for (Direction facing : HORIZONTALS) {
            if (level.getBlockEntity(position.relative(facing)) instanceof TileEntityDragonforge forge) {
                return forge;
            }
        }

        return null;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull final Capability<T> capability, @Nullable final Direction facing) {
        if (core != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return core.getCapability(capability, facing);
        }

        return super.getCapability(capability, facing);
    }
}
