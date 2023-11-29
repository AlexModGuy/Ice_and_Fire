package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PathNavigateDeathWormSand extends WaterBoundPathNavigation {

    public PathNavigateDeathWormSand(EntityDeathWorm deathworm, Level worldIn) {
        super(deathworm, worldIn);
    }

    @Override
    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int i) {
        this.nodeEvaluator = new NodeProcessorDeathWorm();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, i);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected @NotNull Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ());
    }

    @Override
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
        HitResult raytraceresult = this.level.clip(new CustomRayTraceContext(posVec31, posVec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.BLOCK) {
            Vec3 vec3i = raytraceresult.getLocation();
            return mob.level.getBlockState(WorldUtil.containing(vec3i)).is(BlockTags.SAND);
        }
        return false;
    }

    @Override
    public boolean isStableDestination(@NotNull BlockPos pos) {
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
        public @NotNull VoxelShape getBlockShape(BlockState blockState, @NotNull BlockGetter world, @NotNull BlockPos pos) {
            if (blockState.is(BlockTags.SAND))
                return Shapes.empty();
            return this.blockMode.get(blockState, world, pos, this.context);
        }
    }
}