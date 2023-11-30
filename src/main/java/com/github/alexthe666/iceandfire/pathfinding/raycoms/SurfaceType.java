package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * Check if we can walk on a surface, drop into, or neither.
 */
public enum SurfaceType {
    WALKABLE,
    DROPABLE,
    NOT_PASSABLE,
    FLYABLE;

    /**
     * Is the block solid and can be stood upon.
     *
     * @param blockState Block to check.
     * @param pos        the position.
     * @return true if the block at that location can be walked on.
     */
    public static SurfaceType getSurfaceType(final BlockGetter world, final BlockState blockState, final BlockPos pos) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
                || block instanceof FenceGateBlock
                || block instanceof WallBlock
                || block instanceof FireBlock
                || block instanceof CampfireBlock
                || block instanceof BambooStalkBlock
                || block instanceof BambooSaplingBlock
                || block instanceof DoorBlock
                || block instanceof MagmaBlock
                || block instanceof PowderSnowBlock) {
            return SurfaceType.NOT_PASSABLE;
        }

        if ((block instanceof TrapDoorBlock) && !blockState.getValue(TrapDoorBlock.OPEN))
        {
            return SurfaceType.WALKABLE;
        }

        final VoxelShape shape = blockState.getShape(world, pos);
        if (shape.max(Direction.Axis.Y) > 1.0)
        {
            return SurfaceType.NOT_PASSABLE;
        }

        final FluidState fluid = world.getFluidState(pos);
        if (blockState.getBlock() == Blocks.LAVA || (fluid != null && !fluid.isEmpty() && (fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA)))
        {
            return SurfaceType.NOT_PASSABLE;
        }

        if (isWater(world, pos, blockState, fluid))
        {
            return SurfaceType.WALKABLE;
        }

        if (block instanceof SignBlock || block instanceof VineBlock)
        {
            return SurfaceType.DROPABLE;
        }

        if ((blockState.isSolid() && (shape.max(Direction.Axis.X) - shape.min(Direction.Axis.X)) > 0.75
                && (shape.max(Direction.Axis.Z) - shape.min(Direction.Axis.Z)) > 0.75)
                || (blockState.getBlock() == Blocks.SNOW && blockState.getValue(SnowLayerBlock.LAYERS) > 1)
                || block instanceof CarpetBlock)
        {
            return SurfaceType.WALKABLE;
        }

        return SurfaceType.DROPABLE;
    }

    /**
     * Check if the block at this position is actually some kind of waterly fluid.
     *
     * @param pos the pos in the world.
     * @return true if so.
     */
    public static boolean isWater(final LevelReader world, final BlockPos pos) {
        return isWater(world, pos, null, null);
    }

    /**
     * Check if the block at this position is actually some kind of waterly fluid.
     *
     * @param pos         the pos in the world.
     * @param pState      existing blockstate or null
     * @param pFluidState existing fluidstate or null
     * @return true if so.
     */
    public static boolean isWater(final BlockGetter world, final BlockPos pos, @Nullable BlockState pState, @Nullable FluidState pFluidState) {
        BlockState state = pState;
        if (state == null) {
            state = world.getBlockState(pos);
        }

        if (state.canOcclude()) {
            return false;
        }
        if (state.getBlock() == Blocks.WATER) {
            return true;
        }

        FluidState fluidState = pFluidState;
        if (fluidState == null) {
            fluidState = world.getFluidState(pos);
        }

        if (fluidState.isEmpty()) {
            return false;
        }

        if (state.getBlock() instanceof TrapDoorBlock || state.getBlock() instanceof HorizontalDirectionalBlock) {
            // getvalue() will throw an exception if the property does not exist
            if (state.hasProperty(TrapDoorBlock.OPEN) && !state.getValue(TrapDoorBlock.OPEN) && state.hasProperty(TrapDoorBlock.HALF) && state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                return false;
            }
        }

        final Fluid fluid = fluidState.getType();
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }
}
