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
    private EntityDeathWorm worm;

    public PathNavigateDeathWormSand(EntityDeathWorm deathworm, World worldIn) {
        super(deathworm, worldIn);
        worm = deathworm;
    }

    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }

    protected PathFinder getPathFinder(int i) {
        this.nodeProcessor = new NodeProcessorDeathWorm();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor, i);
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canNavigate() {
        return true;
    }

    protected Vector3d getEntityPosition() {
        return new Vector3d(this.entity.getPosX(), this.entity.getPosY() + 0.5D, this.entity.getPosZ());
    }


    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean isDirectPathBetweenPoints(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new CustomRayTraceContext(posVec31, posVec32, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
        if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
            return entity.world.getBlockState(new BlockPos(raytraceresult.getHitVec())).getMaterial() == Material.SAND;
        }
        return false;
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos).isSolid();
    }

    public static class CustomRayTraceContext extends RayTraceContext {

        private final BlockMode blockMode;
        private final ISelectionContext context;

        public CustomRayTraceContext(Vector3d startVecIn, Vector3d endVecIn, BlockMode blockModeIn, FluidMode fluidModeIn, @Nullable Entity entityIn) {
            super(startVecIn, endVecIn, blockModeIn, fluidModeIn, entityIn);
            this.blockMode = blockModeIn;
            this.context = entityIn == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(entityIn);
        }

        @Override
        public VoxelShape getBlockShape(BlockState blockState, IBlockReader world, BlockPos pos) {
            if (blockState.getMaterial() == Material.SAND)
                return VoxelShapes.empty();
            return this.blockMode.get(blockState, world, pos, this.context);
        }
    }
}