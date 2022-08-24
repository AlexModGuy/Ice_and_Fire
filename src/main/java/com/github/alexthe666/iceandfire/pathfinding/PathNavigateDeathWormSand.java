package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PathNavigateDeathWormSand extends SwimmerPathNavigator {
    private final EntityDeathWorm worm;

    public PathNavigateDeathWormSand(EntityDeathWorm deathworm, World worldIn) {
        super(deathworm, worldIn);
        worm = deathworm;
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    protected PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new NodeProcessorDeathWorm();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, i);
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canUpdatePath() {
        return true;
    }

    protected Vector3d getTempMobPos() {
        return new Vector3d(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ());
    }


    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean canMoveDirectly(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
        RayTraceResult raytraceresult = this.level.clip(new CustomRayTraceContext(posVec31, posVec32, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, mob));
        if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
            return mob.level.getBlockState(new BlockPos(raytraceresult.getLocation())).getMaterial() == Material.SAND;
        }
        return false;
    }

    public boolean isStableDestination(BlockPos pos) {
        return this.level.getBlockState(pos).canOcclude();
    }

    public static class CustomRayTraceContext extends RayTraceContext {

        private final BlockMode blockMode;
        private final ISelectionContext context;

        public CustomRayTraceContext(Vector3d startVecIn, Vector3d endVecIn, BlockMode blockModeIn, FluidMode fluidModeIn, @Nullable Entity entityIn) {
            super(startVecIn, endVecIn, blockModeIn, fluidModeIn, entityIn);
            this.blockMode = blockModeIn;
            this.context = entityIn == null ? ISelectionContext.empty() : ISelectionContext.of(entityIn);
        }

        @Override
        public VoxelShape getBlockShape(BlockState blockState, IBlockReader world, BlockPos pos) {
            if (blockState.getMaterial() == Material.SAND)
                return VoxelShapes.empty();
            return this.blockMode.get(blockState, world, pos, this.context);
        }
    }
}