package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PathNavigateDeathWormSand extends WaterBoundPathNavigation {
    private final EntityDeathWorm worm;

    public PathNavigateDeathWormSand(EntityDeathWorm deathworm, Level worldIn) {
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

    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ());
    }


    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
        HitResult raytraceresult = this.level.clip(new CustomRayTraceContext(posVec31, posVec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.BLOCK) {
            return mob.level.getBlockState(new BlockPos(raytraceresult.getLocation())).getMaterial() == Material.SAND;
        }
        return false;
    }

    public boolean isStableDestination(BlockPos pos) {
        return this.level.getBlockState(pos).canOcclude();
    }

    public static class CustomRayTraceContext extends ClipContext {

        private final Block blockMode;
        private final CollisionContext context;

        public CustomRayTraceContext(Vec3 startVecIn, Vec3 endVecIn, Block blockModeIn, Fluid fluidModeIn, @Nullable Entity entityIn) {
            super(startVecIn, endVecIn, blockModeIn, fluidModeIn, entityIn);
            this.blockMode = blockModeIn;
            this.context = entityIn == null ? CollisionContext.empty() : CollisionContext.of(entityIn);
        }

        @Override
        public VoxelShape getBlockShape(BlockState blockState, BlockGetter world, BlockPos pos) {
            if (blockState.getMaterial() == Material.SAND)
                return Shapes.empty();
            return this.blockMode.get(blockState, world, pos, this.context);
        }
    }
}