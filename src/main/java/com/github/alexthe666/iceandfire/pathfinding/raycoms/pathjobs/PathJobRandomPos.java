package com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;


/**
 * Job that handles random pathing.
 */
public class PathJobRandomPos extends AbstractPathJob
{
    /**
     * Direction to walk to.
     */
    protected final BlockPos destination;

    /**
     * Required avoidDistance.
     */
    protected final int minDistFromStart;


    /**
     * Random pathing rand.
     */
    private static final RandomSource random = RandomSource.createThreadSafe();


    /**
     * Minimum distance to the goal.
     */
    private final int maxDistToDest;

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world         world the entity is in.
     * @param start         starting location.
     * @param minDistFromStart how far to move away.
     * @param range         max range to search.
     * @param entity        the entity.
     */
    public PathJobRandomPos(
        final Level world,
        final BlockPos start,
        final int minDistFromStart,
        final int range,
        final LivingEntity entity)
    {
        super(world, start, start, range, new PathResult<PathJobRandomPos>(), entity);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = range;

        final Tuple<Direction, Direction> dir = getRandomDirectionTuple(random);
        this.destination = start.relative(dir.getA(), minDistFromStart).relative(dir.getB(), minDistFromStart);
    }

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world            world the entity is in.
     * @param start            starting location.
     * @param minDistFromStart how far to move away.
     * @param searchRange            max range to search.
     * @param entity           the entity.
     */
    public PathJobRandomPos(
        final Level world,
        final BlockPos start,
        final int minDistFromStart,
        final int searchRange,
        final int maxDistToDest,
        final LivingEntity entity,
        final BlockPos dest)
    {
        super(world, start, dest, searchRange, new PathResult<PathJobRandomPos>(), entity);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = maxDistToDest;
        this.destination = dest;
    }

    /**
     * Prepares the PathJob for the path finding system.
     *
     * @param world    world the entity is in.
     * @param start    starting location.
     * @param minDistFromStart how far to move away.
     * @param range    max range to search.
     * @param entity   the entity.
     */
    public PathJobRandomPos(
        final Level world,
        final BlockPos start,
        final int minDistFromStart,
        final int range,
        final LivingEntity entity,
        final BlockPos startRestriction,
        final BlockPos endRestriction,
        final AbstractAdvancedPathNavigate.RestrictionType restrictionType)
    {
        super(world, start, startRestriction, endRestriction, range, false, new PathResult<PathJobRandomPos>(), entity, restrictionType);
        this.minDistFromStart = minDistFromStart;
        this.maxDistToDest = range;

        final Tuple<Direction, Direction> dir = getRandomDirectionTuple(random);
        this.destination = start.relative(dir.getA(), minDistFromStart).relative(dir.getB(), minDistFromStart);
    }

    /**
     * Searches a random direction.
     *
     * @param random a random object.
     * @return a tuple of two directions.
     */
    public static Tuple<Direction, Direction> getRandomDirectionTuple(final RandomSource random)
    {
        return new Tuple<>(Direction.getRandom(random), Direction.getRandom(random));
    }

    @Nullable
    @Override
    protected Path search()
    {
        if (Pathfinding.isDebug())
        {
            IceAndFire.LOGGER.info(String.format("Pathfinding from [%d,%d,%d] in the direction of [%d,%d,%d]",
              start.getX(), start.getY(), start.getZ(), destination.getX(), destination.getY(), destination.getZ()));
        }

        return super.search();
    }


    @Override
    public PathResult getResult()
    {
        return super.getResult();
    }

    @Override
    protected double computeHeuristic(final BlockPos pos)
    {
        return Math.sqrt(destination.distSqr(new BlockPos(pos.getX(), destination.getY(), pos.getZ())));
    }

    @Override
    protected boolean isAtDestination(final MNode n) {
        return random.nextInt(10) == 0 && isInRestrictedArea(n.pos) && (start.distSqr(n.pos) > minDistFromStart * minDistFromStart)
            && SurfaceType.getSurfaceType(world, world.getBlockState(n.pos.below()), n.pos.below()) == SurfaceType.WALKABLE
            && destination.distSqr(n.pos) < this.maxDistToDest * this.maxDistToDest;
    }

    @Override
    protected double getNodeResultScore(final MNode n) {
        //  For Result Score lower is better
        return destination.distSqr(n.pos);
    }

    /**
     * Checks if position and range match the given parameters
     *
     * @param range max dist to dest range
     * @param pos   dest to look from
     * @return
     */
    public boolean posAndRangeMatch(final int range, final BlockPos pos)
    {
        return destination != null && pos != null && range == maxDistToDest && destination.equals(pos);
    }


}
