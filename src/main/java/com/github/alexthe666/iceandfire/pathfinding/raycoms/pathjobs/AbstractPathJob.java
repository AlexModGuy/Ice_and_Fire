package com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageSyncPath;
import com.github.alexthe666.iceandfire.message.MessageSyncPathReached;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Callable;

import static com.github.alexthe666.iceandfire.pathfinding.raycoms.PathfindingConstants.*;
import static com.github.alexthe666.iceandfire.pathfinding.raycoms.SurfaceType.getSurfaceType;
import static com.github.alexthe666.iceandfire.pathfinding.raycoms.SurfaceType.isWater;

/**
 * Abstract class for Jobs that run in the multithreaded path finder.
 */
public abstract class AbstractPathJob implements Callable<Path> {
    /**
     * Which citizens are being tracked by which players.
     */
    public static final Map<Player, UUID> trackingMap = new HashMap<>();
    /**
     * Start position to path from.
     */
    protected final BlockPos start;
    /**
     * The pathing cache.
     */
    protected final LevelReader world;
    /**
     * The result of the path calculation.
     */
    protected final PathResult result;
    /**
     * Queue of all open nodes.
     */
    private final Queue<MNode> nodesOpen = new PriorityQueue<>(500);
    /**
     * Queue of all the visited nodes.
     */
    private final Map<Integer, MNode> nodesVisited = new HashMap<>();
    /**
     * Type of restriction.
     */
    private final AbstractAdvancedPathNavigate.RestrictionType restrictionType;
    /**
     * Are xz restrictions hard or soft.
     */
    private final boolean hardXzRestriction;
    /**
     * Are there hard xz restrictions.
     */
    private final boolean xzRestricted = false;
    /**
     * Max range used to calculate the number of nodes we visit (square of maxrange).
     */
    protected int maxRange;
    /**
     * End position trying to reach.
     */
    protected BlockPos end = null;
    //  Debug Rendering
    protected boolean debugDrawEnabled = false;
    @Nullable
    protected Set<MNode> debugNodesVisited = new HashSet<>();
    @Nullable
    protected Set<MNode> debugNodesNotVisited = new HashSet<>();
    @Nullable
    protected Set<MNode> debugNodesPath = new HashSet<>();
    /**
     * The entity this job belongs to.
     */
    protected WeakReference<LivingEntity> entity;
    IPassabilityNavigator passabilityNavigator;
    //  May be faster, but can produce strange results
    private boolean allowJumpPointSearchTypeWalk;
    //  May be faster, but can produce strange results
    private float entitySizeXZ = 1;
    private int entitySizeY = 1;
    /**
     * The cost values for certain nodes.
     */
    private boolean circumventSizeCheck = false;
    private int totalNodesAdded = 0;
    private int totalNodesVisited = 0;
    /**
     * The cost values for certain nodes.
     */
    private PathingOptions pathingOptions = new PathingOptions();
    /**
     * The restriction parameters
     */
    private int maxX;
    private int minX;
    private int maxZ;
    private int minZ;
    private int maxY;
    private int minY;
    private double maxJumpHeight = MAX_JUMP_HEIGHT;

    /**
     * AbstractPathJob constructor.
     *
     * @param world  the world within which to path.
     * @param start  the start position from which to path from.
     * @param end    the end position to path to.
     * @param range  maximum path range.
     * @param entity the entity.
     */
    public AbstractPathJob(final Level world, final BlockPos start, final BlockPos end, final int range, final LivingEntity entity) {
        this(world, start, end, range, new PathResult<AbstractPathJob>(), entity);
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world  the world within which to path.
     * @param start  the start position from which to path from.
     * @param end    the end position to path to
     * @param range  maximum path range.
     * @param result path result.
     * @param entity the entity.
     * @see AbstractPathJob#AbstractPathJob(Level, BlockPos, BlockPos, int, LivingEntity)
     */
    public AbstractPathJob(final Level world, final BlockPos start, final BlockPos end, final int range, final PathResult result, final LivingEntity entity) {
        final int minX = Math.min(start.getX(), end.getX()) - (range / 2);
        final int minZ = Math.min(start.getZ(), end.getZ()) - (range / 2);
        final int maxX = Math.max(start.getX(), end.getX()) + (range / 2);
        final int maxZ = Math.max(start.getZ(), end.getZ()) + (range / 2);

        this.restrictionType = AbstractAdvancedPathNavigate.RestrictionType.NONE;
        this.hardXzRestriction = false;

        this.world = new ChunkCache(world, new BlockPos(minX, world.getMinBuildHeight(), minZ), new BlockPos(maxX, world.getMaxBuildHeight(), maxZ), range, world.dimensionType());

        this.start = new BlockPos(start);
        this.end = end;

        this.maxRange = range;

        this.result = result;
        result.setJob(this);
        allowJumpPointSearchTypeWalk = false;

        if (entity != null && trackingMap.containsValue(entity.getUUID())) {
            debugDrawEnabled = true;
            debugNodesVisited = new HashSet<>();
            debugNodesNotVisited = new HashSet<>();
            debugNodesPath = new HashSet<>();
        }
        setEntitySizes(entity);
        if (entity instanceof IPassabilityNavigator) {
            passabilityNavigator = (IPassabilityNavigator) entity;
            maxRange = passabilityNavigator.maxSearchNodes();
        }
        maxJumpHeight = (float) Math.floor(entity.maxUpStep() - 0.2F) + 1.3F;
        this.entity = new WeakReference<>(entity);
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world            the world within which to path.
     * @param start            the start position from which to path from.
     * @param startRestriction start of restricted area.
     * @param endRestriction   end of restricted area.
     * @param range            range^2 is used as cap for visited MNode count
     * @param hardRestriction  if <code>true</code> start has to be inside the restricted area (otherwise the search immidiately finishes) -
     *                         MNode visits outside the area are not allowed, isAtDestination is called on every node, if <code>false</code>
     *                         restricted area only applies to calling isAtDestination thus searching outside area is allowed
     * @param result           path result.
     * @param entity           the entity.
     */
    public AbstractPathJob(final Level world,
                           final BlockPos start,
                           final BlockPos startRestriction,
                           final BlockPos endRestriction,
                           final int range,
                           final boolean hardRestriction,
                           final PathResult result,
                           final LivingEntity entity,
                           final AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        this(world, start, startRestriction, endRestriction, range, Vec3i.ZERO, hardRestriction, result, entity, restrictionType);
        setEntitySizes(entity);
        if (entity instanceof IPassabilityNavigator) {
            passabilityNavigator = (IPassabilityNavigator) entity;
            maxRange = passabilityNavigator.maxSearchNodes();
        }
        maxJumpHeight = (float) Math.floor(entity.maxUpStep() - 0.2F) + 1.3F;
    }

    /**
     * AbstractPathJob constructor.
     *
     * @param world            the world within which to path.
     * @param start            the start position from which to path from.
     * @param startRestriction start of restricted area.
     * @param endRestriction   end of restricted area.
     * @param range            range^2 is used as cap for visited MNode count
     * @param grow             adjustment for restricted area, can be either shrink or grow, is applied in both of xz directions after
     *                         getting min/max box values
     * @param hardRestriction  if <code>true</code> start has to be inside the restricted area (otherwise the search immidiately finishes) -
     *                         MNode visits outside the area are not allowed, isAtDestination is called on every node, if <code>false</code>
     *                         restricted area only applies to calling isAtDestination thus searching outside area is allowed
     * @param result           path result.
     * @param entity           the entity.
     */
    public AbstractPathJob(final Level world,
                           final BlockPos start,
                           final BlockPos startRestriction,
                           final BlockPos endRestriction,
                           final int range,
                           final Vec3i grow,
                           final boolean hardRestriction,
                           final PathResult result,
                           final LivingEntity entity,
                           final AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        this.minX = Math.min(startRestriction.getX(), endRestriction.getX()) - grow.getX();
        this.minZ = Math.min(startRestriction.getZ(), endRestriction.getZ()) - grow.getZ();
        this.maxX = Math.max(startRestriction.getX(), endRestriction.getX()) + grow.getX();
        this.maxZ = Math.max(startRestriction.getZ(), endRestriction.getZ()) + grow.getZ();
        this.minY = Math.min(startRestriction.getY(), endRestriction.getY()) - grow.getY();
        this.maxY = Math.max(startRestriction.getY(), endRestriction.getY()) + grow.getY();

        this.restrictionType = restrictionType;
        this.hardXzRestriction = hardRestriction;

        this.world = new ChunkCache(world, new BlockPos(minX, world.getMinBuildHeight(), minZ), new BlockPos(maxX, world.getMaxBuildHeight(), maxZ), range, world.dimensionType());

        this.start = start;

        this.maxRange = range;

        this.result = result;
        result.setJob(this);

        this.allowJumpPointSearchTypeWalk = false;

        if (entity != null && trackingMap.containsValue(entity.getUUID())) {
            debugDrawEnabled = true;
            debugNodesVisited = new HashSet<>();
            debugNodesNotVisited = new HashSet<>();
            debugNodesPath = new HashSet<>();
        }
        this.entity = new WeakReference<>(entity);
    }

    /**
     * Set the set of reached blocks to the client.
     *
     * @param reached the reached blocks.
     * @param mob     the tracked mob.
     */
    public static void synchToClient(final HashSet<BlockPos> reached, final Mob mob) {
        if (reached.isEmpty()) {
            return;
        }

        for (final Map.Entry<Player, UUID> entry : trackingMap.entrySet()) {
            if (entry.getValue().equals(mob.getUUID())) {
                IceAndFire.sendMSGToPlayer(new MessageSyncPathReached(reached), (ServerPlayer) entry.getKey());
            }
        }
    }

    /**
     * Generates a good path starting location for the entity to path from, correcting for the following conditions. - Being in water: pathfinding in water occurs along the
     * surface; adjusts position to surface. - Being in a fence space: finds correct adjacent position which is not a fence space, to prevent starting path. from within the fence
     * block.
     *
     * @param entity Entity for the pathfinding operation.
     * @return ChunkCoordinates for starting location.
     */
    public static BlockPos prepareStart(final LivingEntity entity) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entity.getBlockX(),
            entity.getBlockY(),
            entity.getBlockZ());
        final Level world = entity.level();

        BlockState bs = world.getBlockState(pos);
        // 1 Up when we're standing within this collision shape
        final VoxelShape collisionShape = bs.getBlockSupportShape(world, pos);
        if (bs.blocksMotion() && collisionShape.max(Direction.Axis.X) > 0) {
            final double relPosX = Math.abs(entity.getX() % 1);
            final double relPosZ = Math.abs(entity.getZ() % 1);

            for (final AABB box : collisionShape.toAabbs()) {
                if (relPosX >= box.minX && relPosX <= box.maxX
                    && relPosZ >= box.minZ && relPosZ <= box.maxZ
                    && box.maxY > 0) {
                    pos.set(pos.getX(), pos.getY() + 1, pos.getZ());
                    bs = world.getBlockState(pos);
                    break;
                }
            }
        }

        BlockState down = world.getBlockState(pos.below());
        while (!bs.blocksMotion() && !down.blocksMotion() && !down.getBlock().isLadder(down, world, pos.below(), entity) && bs.getFluidState().isEmpty()) {
            pos.move(Direction.DOWN, 1);
            bs = down;
            down = world.getBlockState(pos.below());

            if (pos.getY() < 0) {
                return entity.blockPosition();
            }
        }

        final Block b = bs.getBlock();

        if (entity.isInWater()) {
            while (!bs.getFluidState().isEmpty()) {
                pos.set(pos.getX(), pos.getY() + 1, pos.getZ());
                bs = world.getBlockState(pos);
            }
        } else if (b instanceof FenceBlock || b instanceof WallBlock || bs.isSolid()) {
            //Push away from fence
            final double dX = entity.getX() - Math.floor(entity.getX());
            final double dZ = entity.getZ() - Math.floor(entity.getZ());

            if (dX < ONE_SIDE) {
                pos.set(pos.getX() - 1, pos.getY(), pos.getZ());
            } else if (dX > OTHER_SIDE) {
                pos.set(pos.getX() + 1, pos.getY(), pos.getZ());
            }

            if (dZ < ONE_SIDE) {
                pos.set(pos.getX(), pos.getY(), pos.getZ() - 1);
            } else if (dZ > OTHER_SIDE) {
                pos.set(pos.getX(), pos.getY(), pos.getZ() + 1);
            }
        }

        return pos.immutable();
    }

    /**
     * Sets the direction where the ladder is facing.
     *
     * @param world the world in.
     * @param pos   the position.
     * @param p     the path.
     */
    private static void setLadderFacing(final LevelReader world, final BlockPos pos, final PathPointExtended p) {
        final BlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();
        if (block instanceof VineBlock) {
            if (state.getValue(VineBlock.SOUTH)) {
                p.setLadderFacing(Direction.NORTH);
            } else if (state.getValue(VineBlock.WEST)) {
                p.setLadderFacing(Direction.EAST);
            } else if (state.getValue(VineBlock.NORTH)) {
                p.setLadderFacing(Direction.SOUTH);
            } else if (state.getValue(VineBlock.EAST)) {
                p.setLadderFacing(Direction.WEST);
            }
        } else if (block instanceof LadderBlock) {
            p.setLadderFacing(state.getValue(LadderBlock.FACING));
        } else {
            p.setLadderFacing(Direction.UP);
        }
    }

    /**
     * Checks if entity is on a ladder.
     *
     * @param node       the path node.
     * @param nextInPath the next path point.
     * @param pos        the position.
     * @return true if on a ladder.
     */
    private static boolean onALadder(final MNode node, @Nullable final MNode nextInPath, final BlockPos pos) {
        return nextInPath != null && node.isLadder()
            &&
            (nextInPath.pos.getX() == pos.getX() && nextInPath.pos.getZ() == pos.getZ());
    }

    /**
     * Generate a pseudo-unique key for identifying a given MNode by it's coordinates Encodes the lowest 12 bits of x,z and all useful bits of y. This creates unique keys for all
     * blocks within a 4096x256x4096 cube, which is FAR bigger volume than one should attempt to pathfind within This version takes a BlockPos
     *
     * @param pos BlockPos to generate key from
     * @return key for MNode in map
     */
    private static int computeNodeKey(final BlockPos pos) {
        return ((pos.getX() & 0xFFF) << SHIFT_X_BY)
            | ((pos.getY() & 0xFF) << SHIFT_Y_BY)
            | (pos.getZ() & 0xFFF);
    }

    private static boolean nodeClosed(@Nullable final MNode node) {
        return node != null && node.isClosed();
    }

    private static boolean calculateSwimming(final LevelReader world, final BlockPos pos, @Nullable final MNode node) {
        return (node == null) ? isWater(world, pos.below()) : node.isSwimming();
    }

    public static Direction getXZFacing(final BlockPos pos, final BlockPos neighbor) {
        final BlockPos vector = neighbor.subtract(pos);
        return Direction.getNearest(vector.getX(), 0, vector.getZ());
    }

    /**
     * Sync the path of a given mob to the client.
     *
     * @param mob the tracked mob.
     */
    public void synchToClient(final LivingEntity mob) {
        for (final Iterator<Map.Entry<Player, UUID>> iter = trackingMap.entrySet().iterator(); iter.hasNext(); ) {
            final Map.Entry<Player, UUID> entry = iter.next();
            if (entry.getKey().isRemoved()) {
                iter.remove();
            } else if (entry.getValue().equals(mob.getUUID())) {
                IceAndFire.sendMSGToPlayer(new MessageSyncPath(debugNodesVisited, debugNodesNotVisited, debugNodesPath), (ServerPlayer) entry.getKey());
            }
        }
    }

    protected boolean onLadderGoingUp(final MNode currentNode, final BlockPos dPos) {
        return currentNode.isLadder() && (dPos.getY() >= 0 || dPos.getX() != 0 || dPos.getZ() != 0);
    }

    public void setEntitySizes(LivingEntity entity) {
        if (entity instanceof ICustomSizeNavigator) {
            entitySizeXZ = ((ICustomSizeNavigator) entity).getXZNavSize();
            entitySizeY = ((ICustomSizeNavigator) entity).getYNavSize();
            circumventSizeCheck = ((ICustomSizeNavigator) entity).isSmallerThanBlock();
        } else {
            entitySizeXZ = entity.getBbWidth() / 2.0F;
            entitySizeY = Mth.ceil(entity.getBbHeight());
        }
        //TODO:
        allowJumpPointSearchTypeWalk = false;
    }

    /**
     * Compute the cost (immediate 'g' value) of moving from the parent space to the new space.
     *
     * @param dPos       The delta from the parent to the new space; assumes dx,dy,dz in range of [-1..1].
     * @param isSwimming true is the current MNode would require the citizen to swim.
     * @param onPath     checks if the MNode is on a path.
     * @param onRails    checks if the MNode is a rail block.
     * @param railsExit  the exit of the rails.
     * @param blockPos   the position.
     * @param swimStart  if its the swim start.
     * @return cost to move from the parent to the new position.
     */
    protected double computeCost(
        final BlockPos dPos,
        final boolean isSwimming,
        final boolean onPath,
        final boolean onRails,
        final boolean railsExit,
        final boolean swimStart,
        final boolean corner,
        final BlockState state,
        final BlockPos blockPos) {
        double cost = Math.sqrt(dPos.getX() * dPos.getX() + dPos.getY() * dPos.getY() + dPos.getZ() * dPos.getZ());

        if (dPos.getY() != 0 && !(Math.abs(dPos.getY()) <= 1 && world.getBlockState(blockPos).getBlock() instanceof StairBlock)) {
            if (dPos.getY() > 0) {
                cost *= pathingOptions.jumpCost * Math.abs(dPos.getY());
            } else {
                cost *= pathingOptions.dropCost * Math.abs(dPos.getY());
            }
        }

        if (world.getBlockState(blockPos).hasProperty(BlockStateProperties.OPEN)) {
            cost *= pathingOptions.traverseToggleAbleCost;
        }

        if (onPath) {
            cost *= pathingOptions.onPathCost;
        }

        if (onRails) {
            cost *= pathingOptions.onRailCost;
        }

        if (railsExit) {
            cost *= pathingOptions.railsExitCost;
        }

        if (state.getBlock() instanceof VineBlock) {
            cost *= pathingOptions.vineCost;
        }

        if (isSwimming) {
            if (swimStart) {
                cost *= pathingOptions.swimCostEnter;
            } else {
                cost *= pathingOptions.swimCost;
            }
        }

        return cost;
    }

    public PathResult getResult() {
        return result;
    }

    /**
     * Callable method for initiating asynchronous task.
     *
     * @return path to follow or null.
     */
    @Override
    public final Path call() {
        try {
            return search();
        } catch (final Exception e) {
            // Log everything, so exceptions of the pathfinding-thread show in Log
            IceAndFire.LOGGER.warn("Pathfinding Exception", e);
        }

        return null;
    }

    /**
     * Perform the search.
     *
     * @return Path of a path to the given location, a best-effort, or null.
     */
    protected Path search() {
        MNode bestNode = getAndSetupStartNode();

        double bestNodeResultScore = Double.MAX_VALUE;

        while (!nodesOpen.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            final MNode currentNode = nodesOpen.poll();

            totalNodesVisited++;

            // Limiting max amount of nodes mapped
            if (totalNodesVisited > IafConfig.maxDragonPathingNodes || totalNodesVisited > maxRange * maxRange) {
                break;
            }
            currentNode.setCounterVisited(totalNodesVisited);

            handleDebugOptions(currentNode);
            currentNode.setClosed();

            final boolean isViablePosition =
                isInRestrictedArea(currentNode.pos) && getSurfaceType(world, world.getBlockState(currentNode.pos.below()), currentNode.pos.below()) == SurfaceType.WALKABLE;
            if (isViablePosition && isAtDestination(currentNode)) {
                bestNode = currentNode;
                result.setPathReachesDestination(true);
                break;
            }

            //  If this is the closest MNode to our destination, treat it as our best node
            final double nodeResultScore =
                getNodeResultScore(currentNode);
            if (isViablePosition && nodeResultScore < bestNodeResultScore && !currentNode.isCornerNode()) {
                bestNode = currentNode;
                bestNodeResultScore = nodeResultScore;
            }

            // if xz soft-restricted we can walk outside the restricted area to be able to find ways around back to the area
            if (!hardXzRestriction || isViablePosition) {
                walkCurrentNode(currentNode);
            }
        }

        return finalizePath(bestNode);
    }

    private void handleDebugOptions(final MNode currentNode) {
        if (debugDrawEnabled) {
            addNodeToDebug(currentNode);
        }

        if (Pathfinding.isDebug()) {
            IceAndFire.LOGGER.info(String.format("Examining MNode [%d,%d,%d] ; g=%f ; f=%f",
                currentNode.pos.getX(), currentNode.pos.getY(), currentNode.pos.getZ(), currentNode.getCost(), currentNode.getScore()));
        }
    }

    private void addNodeToDebug(final MNode currentNode) {
        debugNodesNotVisited.remove(currentNode);
        debugNodesVisited.add(currentNode);
    }

    private void addPathNodeToDebug(final MNode node) {
        debugNodesVisited.remove(node);
        debugNodesPath.add(node);
    }

    private void walkCurrentNode(final MNode currentNode) {
        BlockPos dPos = BLOCKPOS_IDENTITY;
        if (currentNode.parent != null) {
            dPos = currentNode.pos.subtract(currentNode.parent.pos);
        }

        //  On a ladder, we can go 1 straight-up
        if (onLadderGoingUp(currentNode, dPos)) {
            walk(currentNode, BLOCKPOS_UP);
        }

        //  We can also go down 1, if the lower block is a ladder
        if (onLadderGoingDown(currentNode, dPos)) {
            walk(currentNode, BLOCKPOS_DOWN);
        }
        if (pathingOptions.canClimb()) {
            //If the entity can climb and it needs to climb a block higher than 1 block
            //TODO: Add code for climbing downwards
            if (getHighest(currentNode).getFirst() > 1) {
                walk(currentNode, BLOCKPOS_IDENTITY.above(getHighest(currentNode).getFirst()));
            }
            //After entity has climbed something step forward
            if (currentNode.parent != null && dPos.getX() == 0 && dPos.getZ() == 0 && dPos.getY() > 1) {
                //Step forwards into the direction we climbed from
                if (getHighest(currentNode.parent).getSecond() != null)
                    walk(currentNode, getHighest(currentNode.parent).getSecond());
            }
        }

        // Only explore downwards when dropping
        if ((currentNode.parent == null || !currentNode.parent.pos.equals(currentNode.pos.below())) && currentNode.isCornerNode()) {
            walk(currentNode, BLOCKPOS_DOWN);
            return;
        }

        // Walk downwards MNode if passable
        if ((circumventSizeCheck && isPassable(currentNode.pos.below(), false, currentNode.parent) && (!currentNode.isSwimming() && isLiquid(world.getBlockState(currentNode.pos.below()))))
            || currentNode.parent != null && isPassableBBDown(currentNode.parent.pos, currentNode.pos.below(), currentNode.parent)) {
            walk(currentNode, BLOCKPOS_DOWN);
        }

        // N
        if (dPos.getZ() <= 0) {
            walk(currentNode, BLOCKPOS_NORTH);
        }

        // E
        if (dPos.getX() >= 0) {
            walk(currentNode, BLOCKPOS_EAST);
        }

        // S
        if (dPos.getZ() >= 0) {
            walk(currentNode, BLOCKPOS_SOUTH);
        }

        // W
        if (dPos.getX() <= 0) {
            walk(currentNode, BLOCKPOS_WEST);
        }


    }

    private boolean onLadderGoingDown(final MNode currentNode, final BlockPos dPos) {
        return (dPos.getY() <= 0 || dPos.getX() != 0 || dPos.getZ() != 0) && isLadder(currentNode.pos.below());
    }


    private MNode getAndSetupStartNode() {
        MNode startNode = new MNode(start, computeHeuristic(start));
        // If the entity is Flying set the start MNode to the end node
        // Basically letting its pathfinder do the pathfinding
        if (pathingOptions.isFlying() && start.closerThan(end, maxRange)) {
            startNode = new MNode(end, computeHeuristic(end));
        }
        if (isLadder(start)) {
            startNode.setLadder();
        } else if (isLiquid(world.getBlockState(start.below()))) {
            startNode.setSwimming();
        }

        startNode.setOnRails(pathingOptions.canUseRails() && world.getBlockState(start).getBlock() instanceof BaseRailBlock);

        nodesOpen.offer(startNode);
        nodesVisited.put(computeNodeKey(start), startNode);

        ++totalNodesAdded;

        return startNode;
    }

    /**
     * Check if this is a liquid state for swimming.
     *
     * @param state the state to check.
     * @return true if so.
     */
    public boolean isLiquid(final BlockState state) {
        return state.liquid() || (!state.blocksMotion() && !state.getFluidState().isEmpty());
    }

    /**
     * Generate the path to the target node.
     *
     * @param targetNode the MNode to path to.
     * @return the path.
     */
    private Path finalizePath(final MNode targetNode) {
        //  Compute length of path, since we need to allocate an array.  This is cheaper/faster than building a List
        //  and converting it.  Yes, we have targetNode.steps, but I do not want to rely on that being accurate (I might
        //  fudge that value later on for cutoff purposes
        int pathLength = 1;
        int railsLength = 0;
        @Nullable MNode node = targetNode;
        while (node.parent != null) {
            ++pathLength;
            if (node.isOnRails()) {
                ++railsLength;
            }
            node = node.parent;
        }

        final Node[] points = new Node[pathLength];
        points[0] = new PathPointExtended(node.pos);
        if (debugDrawEnabled) {
            addPathNodeToDebug(node);
        }

        @Nullable MNode nextInPath = null;
        @Nullable Node next = null;
        node = targetNode;
        while (node.parent != null) {
            if (debugDrawEnabled) {
                addPathNodeToDebug(node);
            }

            --pathLength;

            final BlockPos pos = node.pos;

            if (node.isSwimming()) {
                //  Not truly necessary but helps prevent them spinning in place at swimming nodes
                pos.offset(BLOCKPOS_DOWN);
            }

            final PathPointExtended p = new PathPointExtended(pos);
            if (railsLength >= 8) {
                p.setOnRails(node.isOnRails());
                if (p.isOnRails() && (!node.parent.isOnRails() || node.parent.parent == null)) {
                    p.setRailsEntry();
                } else if (p.isOnRails() && points.length > pathLength + 1) {
                    final PathPointExtended point = ((PathPointExtended) points[pathLength + 1]);
                    if (!point.isOnRails()) {
                        point.setRailsExit();
                    }
                }
            }

            //  Climbing on a ladder?
            if (nextInPath != null && onALadder(node, nextInPath, pos)) {
                p.setOnLadder(true);
                if (nextInPath.pos.getY() > pos.getY()) {
                    //  We only care about facing if going up
                    //In the case of BlockVines (Which does not have Direction) we have to check the metadata of the vines... bitwise...
                    setLadderFacing(world, pos, p);
                }
            } else if (onALadder(node.parent, node.parent, pos)) {
                p.setOnLadder(true);
            }

            if (next != null) {
                next.cameFrom = p;
            }
            next = p;
            points[pathLength] = p;

            nextInPath = node;
            node = node.parent;
        }

        doDebugPrinting(points);

        return new Path(Arrays.asList(points), getPathTargetPos(targetNode), isAtDestination(targetNode));
    }

    /**
     * Creates the path for the given points
     *
     * @param finalNode
     * @return
     */
    protected BlockPos getPathTargetPos(final MNode finalNode) {
        return finalNode.pos;
    }

    /**
     * Turns on debug printing.
     *
     * @param points the points to print.
     */
    private void doDebugPrinting(final Node[] points) {
        if (Pathfinding.isDebug()) {
            IceAndFire.LOGGER.info("Path found:");

            for (final Node p : points) {
                IceAndFire.LOGGER.info(String.format("Step: [%d,%d,%d]", p.x, p.y, p.z));
            }

            IceAndFire.LOGGER.info(String.format("Total Nodes Visited %d / %d", totalNodesVisited, totalNodesAdded));
        }
    }

    /**
     * Compute the heuristic cost ('h' value) of a given position x,y,z.
     * <p>
     * Returning a value of 0 performs a breadth-first search. Returning a value less than actual possible cost to goal guarantees shortest path, but at computational expense.
     * Returning a value exactly equal to the cost to the goal guarantees shortest path and least expense (but generally. only works when path is straight and unblocked). Returning
     * a value greater than the actual cost to goal produces good, but not perfect paths, and is fast. Returning a very high value (such that 'h' is very high relative to 'g') then
     * only 'h' (the heuristic) matters as the search will be a very fast greedy best-first-search, ignoring cost weighting and distance.
     *
     * @param pos Position to compute heuristic from.
     * @return the heuristic.
     */
    protected abstract double computeHeuristic(BlockPos pos);

    /**
     * Return true if the given MNode is a viable final destination, and the path should generate to here.
     *
     * @param n MNode to test.
     * @return true if the MNode is a viable destination.
     */
    protected abstract boolean isAtDestination(MNode n);

    /**
     * Compute a 'result score' for the Node; if no destination is determined, the MNode that had the highest 'result' score is used.
     *
     * @param n MNode to test.
     * @return score for the node.
     */
    protected abstract double getNodeResultScore(MNode n);

    /**
     * "Walk" from the parent in the direction specified by the delta, determining the new x,y,z position for such a move and adding or updating a node, as appropriate.
     *
     * @param parent MNode being walked from.
     * @param dPos   Delta from parent, expected in range of [-1..1].
     * @return true if a MNode was added or updated when attempting to move in the given direction.
     */
    protected final boolean walk(final MNode parent, BlockPos dPos) {
        BlockPos pos = parent.pos.offset(dPos);

        //  Can we traverse into this node?  Fix the y up
        final int newY = getGroundHeight(parent, pos);

        if (newY < world.getMinBuildHeight()) {
            return false;
        }

        boolean corner = false;
        if (pos.getY() != newY) {
            // if the new position is above the current node, we're taking the MNode directly above
            if (!parent.isCornerNode() && newY - pos.getY() > 0 && (parent.parent == null || !parent.parent.pos.equals(parent.pos.offset(new BlockPos(0, newY - pos.getY(), 0))))) {
                dPos = new BlockPos(0, newY - pos.getY(), 0);
                pos = parent.pos.offset(dPos);
                corner = true;
            }
            // If we're going down, take the air-corner before going to the lower node
            else if (!parent.isCornerNode() && newY - pos.getY() < 0 && (dPos.getX() != 0 || dPos.getZ() != 0) && (parent.parent == null || !parent.pos.below()
                .equals(parent.parent.pos))) {
                dPos = new BlockPos(dPos.getX(), 0, dPos.getZ());
                pos = parent.pos.offset(dPos);
                corner = true;
            }
            // Fix up normal y but only if we aren't climbing
            else if (!(pathingOptions.canClimb() && dPos.getY() > 1)) {
                dPos = dPos.offset(0, newY - pos.getY(), 0);
                pos = new BlockPos(pos.getX(), newY, pos.getZ());
            }
        }

        int nodeKey = computeNodeKey(pos);
        MNode node = nodesVisited.get(nodeKey);
        if (nodeClosed(node)) {
            //  Early out on closed nodes (closed = expanded from)
            return false;
        }

        final boolean isSwimming = calculateSwimming(world, pos, node);

        if (isSwimming && !pathingOptions.canSwim()) {
            return false;
        }

        final boolean swimStart = isSwimming && !parent.isSwimming();
        final BlockState state = world.getBlockState(pos);
        final boolean onRoad = false;
        final boolean onRails = pathingOptions.canUseRails() && world.getBlockState(corner ? pos.below() : pos).getBlock() instanceof BaseRailBlock;
        final boolean railsExit = !onRails && parent != null && parent.isOnRails();
        //  Cost may have changed due to a jump up or drop
        final double stepCost = computeCost(dPos, isSwimming, onRoad, onRails, railsExit, swimStart, corner, state, pos);
        final double heuristic = computeHeuristic(pos);
        final double cost = parent.getCost() + stepCost;
        final double score = cost + heuristic;

        if (node == null) {
            node = createNode(parent, pos, nodeKey, isSwimming, heuristic, cost, score);
            node.setOnRails(onRails);
            node.setCornerNode(corner);
        } else if (updateCurrentNode(parent, node, heuristic, cost, score)) {
            return false;
        }

        nodesOpen.offer(node);

        //If we climbed something skip jumpPointSearch
        //This is a workaround so that the path generated doesn't go through blocks
        if (pathingOptions.canClimb() && dPos.getY() > 1)
            return true;

        //  Jump Point Search-ish optimization:
        // If this MNode was a (heuristic-based) improvement on our parent,
        // lets go another step in the same direction...
        performJumpPointSearch(parent, dPos, node);

        return true;
    }

    private void performJumpPointSearch(final MNode parent, final BlockPos dPos, final MNode node) {
        if (allowJumpPointSearchTypeWalk && node.getHeuristic() <= parent.getHeuristic()) {
            walk(node, dPos);
        }
    }

    private MNode createNode(
        final MNode parent, final BlockPos pos, final int nodeKey,
        final boolean isSwimming, final double heuristic, final double cost, final double score) {
        final MNode node;
        node = new MNode(parent, pos, cost, heuristic, score);
        nodesVisited.put(nodeKey, node);
        if (debugDrawEnabled) {
            debugNodesNotVisited.add(node);
        }

        if (isLadder(pos)) {
            node.setLadder();
        } else if (isSwimming) {
            node.setSwimming();
        }

        totalNodesAdded++;
        node.setCounterAdded(totalNodesAdded);
        return node;
    }

    private boolean updateCurrentNode(final MNode parent, final MNode node, final double heuristic, final double cost, final double score) {
        //  This MNode already exists
        if (score >= node.getScore()) {
            return true;
        }

        if (!nodesOpen.remove(node)) {
            return true;
        }

        node.parent = parent;
        node.setSteps(parent.getSteps() + 1);
        node.setCost(cost);
        node.setHeuristic(heuristic);
        node.setScore(score);
        return false;
    }

    /**
     * Get the height of the ground at the given x,z coordinate, within 1 step of y.
     *
     * @param parent parent node.
     * @param pos    coordinate of block.
     * @return y height of first open, viable block above ground, or -1 if blocked or too far a drop.
     */
    protected int getGroundHeight(final MNode parent, final BlockPos pos) {
        //  Check (y+1) first, as it's always needed, either for the upper body (level),
        //  lower body (headroom drop) or lower body (jump up)
        if (checkHeadBlock(parent, pos)) {
            return handleTargetNotPassable(parent, pos.above(), world.getBlockState(pos.above()));
        }

        //  Now check the block we want to move to
        final BlockState target = world.getBlockState(pos);
        if (parent != null && !isPassableBB(parent.pos, pos, parent)) {
            return handleTargetNotPassable(parent, pos, target);
        }

        //  Do we have something to stand on in the target space?
        final BlockState below = world.getBlockState(pos.below());
        if (pathingOptions.isFlying()) {
            final SurfaceType flyability = isFlyable(below, pos, parent);
            if (flyability == SurfaceType.FLYABLE) {
                return pos.getY();
            } else if (flyability == SurfaceType.NOT_PASSABLE) {
                return -1;
            }
        } else {
            final SurfaceType walkability = isWalkableSurface(below, pos);
            if (walkability == SurfaceType.WALKABLE) {
                //  Level path
                return pos.getY();
            } else if (walkability == SurfaceType.NOT_PASSABLE) {
                return -1;
            }
        }
        return handleNotStanding(parent, pos, below);
    }

    private int handleNotStanding(@Nullable final MNode parent, final BlockPos pos, final BlockState below) {
        final boolean isSwimming = parent != null && parent.isSwimming();

        if (isLiquid(below)) {
            return handleInLiquid(pos, below, isSwimming);
        }

        if (isLadder(below.getBlock(), pos.below())) {
            return pos.getY();
        }

        return checkDrop(parent, pos, isSwimming);
    }

    private int checkDrop(@Nullable final MNode parent, final BlockPos pos, final boolean isSwimming) {
        final boolean canDrop = parent != null && !parent.isLadder();
        boolean isChonker = true;

        if (pathingOptions.canClimb() && parent != null && pos.getY() > parent.pos.getY() + 1) {
            return pos.getY();
        }
        //  Nothing to stand on
        if (!isChonker) {
            if (!canDrop || isSwimming || ((parent.pos.getX() != pos.getX() || parent.pos.getZ() != pos.getZ()) && isPassableBBFull(parent.pos.below(), parent)
                && isWalkableSurface(world.getBlockState(parent.pos.below()), parent.pos.below()) == SurfaceType.DROPABLE)) {
                return -1;
            }
        }

        for (int i = 2; i <= 10; i++) {
            final BlockState below = world.getBlockState(pos.below(i));
            if (isWalkableSurface(below, pos) == SurfaceType.WALKABLE && i <= 4 || below.liquid()) {
                //  Level path
                return pos.getY() - i + 1;
            } else if (below.isAir()) {
                return -1;
            }
        }

        return -1;
    }

    private int handleInLiquid(final BlockPos pos, final BlockState below, final boolean isSwimming) {
        if (isSwimming) {
            //  Already swimming in something, or allowed to swim and this is water
            return pos.getY();
        }

        if (pathingOptions.canSwim() && isWater(world, pos.below())) {
            //  This is water, and we are allowed to swim
            return pos.getY();
        }

        //  Not allowed to swim or this isn't water, and we're on dry land
        return -1;
    }

    private int handleTargetNotPassable(@Nullable final MNode parent, final BlockPos pos, final BlockState target) {
        final boolean canJump = parent != null && !parent.isLadder() && !parent.isSwimming();
        //  Need to try jumping up one, if we can
        if (!canJump || getSurfaceType(world, target, pos) != SurfaceType.WALKABLE) {
            return -1;
        }
/*
        //  Check for headroom in the target space
        if (!isPassableBB(parent.pos, pos.up())) {
            return -1;
        }*/
        //  Check for headroom in the target space
        if (!isPassable(pos.above(2), false, parent)) {
            final VoxelShape bb1 = world.getBlockState(pos).getBlockSupportShape(world, pos);
            final VoxelShape bb2 = world.getBlockState(pos.above(2)).getBlockSupportShape(world, pos.above(2));
            if ((pos.above(2).getY() + getStartY(bb2, 1)) - (pos.getY() + getEndY(bb1, 0)) < 2) {
                return -1;
            }
        }

        //  Check for jump room from the origin space
        if (!isPassable(parent.pos.above(2), false, parent)) {
            final VoxelShape bb1 = world.getBlockState(pos).getBlockSupportShape(world, pos);
            final VoxelShape bb2 = world.getBlockState(parent.pos.above(2)).getBlockSupportShape(world, parent.pos.above(2));
            if ((parent.pos.above(2).getY() + getStartY(bb2, 1)) - (pos.getY() + getEndY(bb1, 0)) < 2) {
                return -1;
            }
        }

        final BlockState parentBelow = world.getBlockState(parent.pos.below());
        final VoxelShape parentBB = parentBelow.getBlockSupportShape(world, parent.pos.below());

        double parentY = parentBB.max(Direction.Axis.Y);
        double parentMaxY = parentY + parent.pos.below().getY();
        final double targetMaxY = target.getBlockSupportShape(world, pos).max(Direction.Axis.Y) + pos.getY();
        if (targetMaxY - parentMaxY < maxJumpHeight) {
            return pos.getY() + 1;
        }
        if (target.getBlock() instanceof StairBlock
            && parentY - HALF_A_BLOCK < maxJumpHeight
            && target.getValue(StairBlock.HALF) == Half.BOTTOM
            && getXZFacing(parent.pos, pos) == target.getValue(StairBlock.FACING)) {
            return pos.getY() + 1;
        }
        return -1;
    }

    /*
    Get's the maximum height of a climbable column and the direction the column can be climbed from
     */
    private Pair<Integer, BlockPos> getHighest(MNode node) {
        int max = 1;
        BlockPos pos = node.pos;
        BlockPos direction = null;
        if (world.getBlockState(pos.north()).canOcclude()) {
            if (climbableTop(pos.north(), Direction.SOUTH, node) > max) {
                max = climbableTop(pos.north(), Direction.SOUTH, node);
                direction = BLOCKPOS_NORTH;
            }
        }
        if (world.getBlockState(pos.east()).canOcclude()) {
            if (climbableTop(pos.east(), Direction.WEST, node) > max) {
                max = climbableTop(pos.east(), Direction.WEST, node);
                direction = BLOCKPOS_EAST;
            }
        }
        if (world.getBlockState(pos.south()).canOcclude()) {
            if (climbableTop(pos.south(), Direction.NORTH, node) > max) {
                max = climbableTop(pos.south(), Direction.NORTH, node);
                direction = BLOCKPOS_SOUTH;
            }
        }
        if (world.getBlockState(pos.west()).canOcclude()) {
            if (climbableTop(pos.west(), Direction.EAST, node) > max) {
                max = climbableTop(pos.west(), Direction.EAST, node);
                direction = BLOCKPOS_WEST;
            }
        }
        return new Pair<>(max, direction);
    }

    /*
    Keeps going up a column centered at pos checking if in the direction there's free space
     */
    private int climbableTop(BlockPos pos, Direction direction, MNode node) {
        BlockState target = world.getBlockState(pos);
        BlockState origin;
        int i = 0;
        //TODO: Use collision shapes of blocks
        while (target.canOcclude()) {
            pos = pos.above();
            target = world.getBlockState(pos);
            origin = world.getBlockState(pos.relative(direction));
            //If the climbable side can't be passed reset height to 0
            if (!isPassable(origin, pos.relative(direction), node)) {
                i = 0;
                break;
            }
            i++;
        }
        return i;
    }

    private boolean checkHeadBlock(@Nullable final MNode parent, final BlockPos pos) {
        BlockPos localPos = pos;
        final VoxelShape bb = world.getBlockState(localPos).getCollisionShape(world, localPos);
        if (bb.max(Direction.Axis.Y) < 1) {
            localPos = pos.above();
        }

        if (parent == null || !isPassableBB(parent.pos, pos.above(), parent)) {
            final VoxelShape bb1 = world.getBlockState(pos.below()).getBlockSupportShape(world, pos.below());
            final VoxelShape bb2 = world.getBlockState(pos.above()).getBlockSupportShape(world, pos.above());
            if ((pos.above().getY() + getStartY(bb2, 1)) - (pos.below().getY() + getEndY(bb1, 0)) < 2) {
                return true;
            }
            if (parent != null) {
                final VoxelShape bb3 = world.getBlockState(parent.pos.below()).getBlockSupportShape(world, pos.below());
                if ((pos.above().getY() + getStartY(bb2, 1)) - (parent.pos.below().getY() + getEndY(bb3, 0)) < 1.75) {
                    return true;
                }
            }
        }

        if (parent != null) {
            final BlockState hereState = world.getBlockState(localPos.below());
            final VoxelShape bb1 = world.getBlockState(pos).getBlockSupportShape(world, pos);
            final VoxelShape bb2 = world.getBlockState(localPos.above()).getBlockSupportShape(world, localPos.above());
            if ((localPos.above().getY() + getStartY(bb2, 1)) - (pos.getY() + getEndY(bb1, 0)) >= 2) {
                return false;
            }

            return isLiquid(hereState) && !isPassable(pos, false, parent);
        }
        return false;
    }

    /**
     * Get the start y of a voxelshape.
     *
     * @param bb  the voxelshape.
     * @param def the default if empty.
     * @return the start y.
     */
    private double getStartY(final VoxelShape bb, final int def) {
        return bb.isEmpty() ? def : bb.min(Direction.Axis.Y);
    }

    /**
     * Get the end y of a voxelshape.
     *
     * @param bb  the voxelshape.
     * @param def the default if empty.
     * @return the end y.
     */
    private double getEndY(final VoxelShape bb, final int def) {
        return bb.isEmpty() ? def : bb.max(Direction.Axis.Y);
    }

    /**
     * Is the space passable.
     *
     * @param block the block we are checking.
     * @return true if the block does not block movement.
     */
    protected boolean isPassable(final BlockState block, final BlockPos pos, final MNode parent) {
        final BlockPos parentPos = parent == null ? start : parent.pos;
        final BlockState parentBlock = world.getBlockState(parentPos);

        if (parentBlock.getBlock() instanceof TrapDoorBlock) {
            final BlockPos dir = pos.subtract(parentPos);
            if (dir.getX() != 0 || dir.getZ() != 0) {
                // Check if we can leave the current block, there might be a trapdoor or panel blocking us.
                final Direction direction = getXZFacing(parentPos, pos);
                final Direction facing = parentBlock.getValue(TrapDoorBlock.FACING);
                if (direction == facing.getOpposite()) {
                    return false;
                }
            }
        }

        if (!block.isAir()) {
            final VoxelShape shape = block.getBlockSupportShape(world, pos);
            if (block.blocksMotion() && !(shape.isEmpty() || shape.max(Direction.Axis.Y) <= 0.1)) {
                if (block.getBlock() instanceof TrapDoorBlock) {
                    final BlockPos dir = pos.subtract(parentPos);
                    if (dir.getY() != 0 && dir.getX() == 0 && dir.getZ() == 0) {
                        return true;
                    }

                    final Direction direction = getXZFacing(parentPos, pos);
                    final Direction facing = block.getValue(TrapDoorBlock.FACING);

                    // We can enter a space of a trapdoor if it's facing the same direction
                    if (direction == facing.getOpposite()) {
                        return true;
                    }

                    // We cannot enter a space of a trapdoor if its facing the opposite direction.
                    return direction != facing;
                } else {
                    return pathingOptions.canEnterDoors() && (block.getBlock() instanceof DoorBlock
                        || block.getBlock() instanceof FenceGateBlock)
                        || block.getBlock() instanceof PressurePlateBlock
                        || block.getBlock() instanceof SignBlock
                        || block.getBlock() instanceof AbstractBannerBlock;
                }
            } else if (block.getBlock() instanceof FireBlock || block.getBlock() instanceof SweetBerryBushBlock) {
                return false;
            } else {
                if (isLadder(block.getBlock(), pos)) {
                    return true;
                }

                // TODO: I'd be cool if dragons could squash multiple snow layers when walking over them
                if (shape.isEmpty() || shape.max(Direction.Axis.Y) <= 0.125 && !isLiquid((block)) && (block.getBlock() != Blocks.SNOW || block.getValue(SnowLayerBlock.LAYERS) == 1)) {
                    final BlockPathTypes pathType = block.getBlockPathType(world, pos, null);
                    return pathType == null || pathType.getDanger() == null;
                }
                return false;
            }
        }

        return true;
    }

    // TODO :: Expensive performance-wise
    protected boolean isPassable(final BlockPos pos, final boolean head, final MNode parent) {
        final BlockState state = world.getBlockState(pos);
        final VoxelShape shape = state.getBlockSupportShape(world, pos);

        if (passabilityNavigator != null && passabilityNavigator.isBlockExplicitlyNotPassable(state, pos, pos)) {
            return false;
        }

        if ((shape.isEmpty() || shape.max(Direction.Axis.Y) <= 0.1)) {
            if (passabilityNavigator != null && passabilityNavigator.isBlockExplicitlyPassable(state, pos, pos))
                return isPassable(state, pos, parent);
            return !head
                || !(state.getBlock() instanceof WoolCarpetBlock)
                || isLadder(state.getBlock(), pos);
        }
        return isPassable(state, pos, parent);
    }

    protected boolean isPassableBBFull(final BlockPos pos, MNode parent) {
        if (circumventSizeCheck) {
            return isPassable(pos, false, parent) && isPassable(pos.above(), true, parent);
        } else {
            for (int i = 0; i <= entitySizeXZ; i++) {
                for (int j = 0; j <= entitySizeY; j++) {
                    for (int k = 0; k <= entitySizeXZ; k++) {
                        if (!isPassable(pos.offset(i, j, k), false, parent)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean isPassableBB(final BlockPos parentPos, final BlockPos pos, MNode parent) {
        if (circumventSizeCheck) {
            return isPassable(pos, false, parent) && isPassable(pos.above(), true, parent);
        } else {

            Direction facingDir = getXZFacing(parentPos, pos);
            if (facingDir == Direction.DOWN || facingDir == Direction.UP)
                return false;
            facingDir = facingDir.getClockWise();
            for (int i = 0; i <= entitySizeXZ; i++) {
                for (int j = 0; j <= entitySizeY; j++) {
                    if (!isPassable(pos.relative(facingDir, i).above(j), false, parent)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
        Mobs that break blocks may consider the ground passable.
     */
    protected boolean isPassableBBDown(final BlockPos parentPos, final BlockPos pos, MNode parent) {
        if (circumventSizeCheck) {
            return isPassable(pos, true, parent);
        } else {
            Direction facingDir = getXZFacing(parentPos, pos);
            if (facingDir == Direction.DOWN || facingDir == Direction.UP)
                return false;
            facingDir = facingDir.getClockWise();
            for (int i = 0; i <= entitySizeXZ; i++) {
                for (int j = 0; j <= entitySizeY; j++) {
                    if (!isPassable(pos.relative(facingDir, i).above(j), false, parent) || pos.getY() <= parentPos.getY()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Is the block solid and can be stood upon.
     *
     * @param blockState Block to check.
     * @param pos        the position.
     * @return true if the block at that location can be walked on.
     */

    protected SurfaceType isFlyable(final BlockState blockState, final BlockPos pos, MNode parent) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
            || block instanceof FenceGateBlock
            || block instanceof WallBlock
            || block instanceof FireBlock
            || block instanceof CampfireBlock
            || block instanceof BambooStalkBlock
            || block instanceof BambooSaplingBlock
            || (blockState.getShape(world, pos).max(Direction.Axis.Y) > 1.0)) {
            return SurfaceType.NOT_PASSABLE;
        }
        final FluidState fluid = world.getFluidState(pos);
        if (fluid != null && !fluid.isEmpty() && (fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA)) {
            return SurfaceType.NOT_PASSABLE;
        }
        if (isPassable(blockState, pos, parent)) {
            return SurfaceType.FLYABLE;
        }
        return SurfaceType.DROPABLE;
    }

    protected SurfaceType isWalkableSurface(final BlockState blockState, final BlockPos pos) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
            || block instanceof FenceGateBlock
            || block instanceof WallBlock
            || block instanceof FireBlock
            || block instanceof CampfireBlock
            || block instanceof BambooStalkBlock
            || block instanceof BambooSaplingBlock
            || (blockState.getShape(world, pos).max(Direction.Axis.Y) > 1.0)) {
            return SurfaceType.NOT_PASSABLE;
        }

        final FluidState fluid = world.getFluidState(pos);
        if (fluid != null && !fluid.isEmpty() && (fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA)) {
            return SurfaceType.NOT_PASSABLE;
        }

        if (block instanceof SignBlock) {
            return SurfaceType.DROPABLE;
        }

        if (blockState.isSolid()
            || (blockState.getBlock() == Blocks.SNOW && blockState.getValue(SnowLayerBlock.LAYERS) > 1)
            || block instanceof WoolCarpetBlock) {
            return SurfaceType.WALKABLE;
        }

        return SurfaceType.DROPABLE;
    }

    /**
     * Is the block a ladder.
     *
     * @param block block to check.
     * @param pos   location of the block.
     * @return true if the block is a ladder.
     */
    protected boolean isLadder(final Block block, final BlockPos pos) {
        return block.isLadder(this.world.getBlockState(pos), world, pos, entity.get());
    }

    protected boolean isLadder(final BlockPos pos) {
        return isLadder(world.getBlockState(pos).getBlock(), pos);
    }

    /**
     * Sets the pathing options
     *
     * @param pathingOptions the pathing options to set.
     */
    public void setPathingOptions(final PathingOptions pathingOptions) {
        this.pathingOptions = pathingOptions;
    }

    /**
     * Check if in restricted area.
     *
     * @param pos the pos to check.
     * @return true if so.
     */
    public boolean isInRestrictedArea(final BlockPos pos) {
        if (restrictionType == AbstractAdvancedPathNavigate.RestrictionType.NONE) {
            return true;
        }

        final boolean isInXZ = pos.getX() <= maxX && pos.getZ() <= maxZ && pos.getZ() >= minZ && pos.getX() >= minX;
        if (!isInXZ) {
            return false;
        }

        if (restrictionType == AbstractAdvancedPathNavigate.RestrictionType.XZ) {
            return true;
        }
        return pos.getY() <= maxY && pos.getY() >= minY;
    }

}
