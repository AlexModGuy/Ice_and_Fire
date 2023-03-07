package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorFly;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorWalk;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveAwayFromLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveToLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobRandomPos;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * Minecolonies async PathNavigate.
 */
public class AdvancedPathNavigate extends AbstractAdvancedPathNavigate {
    public static final double MIN_Y_DISTANCE = 0.001;
    public static final int MAX_SPEED_ALLOWED = 2;
    public static final double MIN_SPEED_ALLOWED = 0.1;

    @Nullable
    private PathResult<AbstractPathJob> pathResult;

    /**
     * The world time when a path was added.
     */
    private long pathStartTime = 0;

    /**
     * Spawn pos of minecart.
     */
    private final BlockPos spawnedPos = BlockPos.ZERO;

    /**
     * Desired position to reach
     */
    private BlockPos desiredPos;

    /**
     * Timeout for the desired pos, resets when its no longer wanted
     */
    private int desiredPosTimeout = 0;

    /**
     * The stuck handler to use
     */
    private IStuckHandler stuckHandler;

    /**
     * Whether we did set sneaking
     */
    private boolean isSneaking = true;

    private double swimSpeedFactor = 1.0;

    private float width = 1;

    private float height = 1;

    public enum MovementType {
        WALKING,
        FLYING,
        CLIMBING
    }
    /**
     * Instantiates the navigation of an ourEntity.
     *
     * @param entity the ourEntity.
     * @param world  the world it is in.
     */
    public AdvancedPathNavigate(final MobEntity entity, final World world) {
        this(entity, world, MovementType.WALKING);
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type) {
        this(entity, world, type, 1, 1);
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type, float width, float height) {
        this(entity, world, type, width, height, PathingStuckHandler.createStuckHandler().withTeleportSteps(6).withTeleportOnFullStuck());
    }

    public AdvancedPathNavigate(final MobEntity entity, final World world, MovementType type, float width, float height, PathingStuckHandler stuckHandler) {
        super(entity, world);
        switch (type) {
            case FLYING:
                this.nodeProcessor = new NodeProcessorFly();
                getPathingOptions().setIsFlying(true);
                break;
            case WALKING:
                this.nodeProcessor = new NodeProcessorWalk();
                break;
            case CLIMBING:
                this.nodeProcessor = new NodeProcessorWalk();
                getPathingOptions().setCanClimb(true);
                break;
        }
        this.nodeProcessor.setCanEnterDoors(true);
        getPathingOptions().setEnterDoors(true);
        this.nodeProcessor.setCanOpenDoors(true);
        getPathingOptions().setCanOpenDoors(true);
        this.nodeProcessor.setCanSwim(true);
        getPathingOptions().setCanSwim(true);
        this.width = width;
        this.height = height;
        this.stuckHandler = stuckHandler;
    }

    @Override
    public BlockPos getDestination() {
        return destination;
    }


    @Nullable
    public PathResult moveAwayFromXYZ(final BlockPos avoid, final double range, final double speedFactor, final boolean safeDestination) {
        final BlockPos start = AbstractPathJob.prepareStart(ourEntity);

        return setPathJob(new PathJobMoveAwayFromLocation(ourEntity.world,
                start,
                avoid,
                (int) range,
                (int) ourEntity.getAttribute(Attributes.FOLLOW_RANGE).getValue(),
                ourEntity), null, speedFactor, safeDestination);
    }

    @Nullable
    public PathResult moveToRandomPos(final double range, final double speedFactor) {
        if (pathResult != null && pathResult.getJob() instanceof PathJobRandomPos)
        {
            return pathResult;
        }

        desiredPos = BlockPos.ZERO;
        final int theRange = (int) (entity.getRNG().nextInt((int) range) + range / 2);
        final BlockPos start = AbstractPathJob.prepareStart(ourEntity);

        return setPathJob(new PathJobRandomPos(ourEntity.world,
            start,
            theRange,
            (int) ourEntity.getAttribute(Attributes.FOLLOW_RANGE).getValue(),
            ourEntity), null, speedFactor, true);
    }

    @Nullable
    public PathResult moveToRandomPosAroundX(final int range, final double speedFactor, final BlockPos pos)
    {
        if (pathResult != null
            && pathResult.getJob() instanceof PathJobRandomPos
            && ((((PathJobRandomPos) pathResult.getJob()).posAndRangeMatch(range, pos))))
        {
            return pathResult;
        }

        desiredPos = BlockPos.ZERO;
        return setPathJob(new PathJobRandomPos(ourEntity.world,
            AbstractPathJob.prepareStart(ourEntity),
            3,
            (int) ourEntity.getAttribute(Attributes.FOLLOW_RANGE).getValue(),
            range,
            ourEntity, pos), pos, speedFactor, true);
    }

    @Override
    public PathResult moveToRandomPos(final int range, final double speedFactor, final net.minecraft.util.Tuple<BlockPos, BlockPos> corners, final AbstractAdvancedPathNavigate.RestrictionType restrictionType)
    {
        if (pathResult != null && pathResult.getJob() instanceof PathJobRandomPos)
        {
            return pathResult;
        }

        desiredPos = BlockPos.ZERO;
        final int theRange = entity.getRNG().nextInt(range) + range / 2;
        final BlockPos start = AbstractPathJob.prepareStart(ourEntity);

        return setPathJob(new PathJobRandomPos(ourEntity.world,
            start,
            theRange,
            (int) ourEntity.getAttribute(Attributes.FOLLOW_RANGE).getValue(),
            ourEntity,
            corners.getA(),
            corners.getB(),
            restrictionType), null, speedFactor, true);
    }

    @Nullable
    public PathResult setPathJob(
        final AbstractPathJob job,
        final BlockPos dest,
        final double speedFactor, final boolean safeDestination)
    {
        clearPath();

        this.destination = dest;
        this.originalDestination = dest;
        if (safeDestination)
        {
            desiredPos = dest;
            if (dest != null)
            {
                desiredPosTimeout = 50 * 20;
            }
        }

        this.walkSpeedFactor = speedFactor;

        if (speedFactor > MAX_SPEED_ALLOWED || speedFactor < MIN_SPEED_ALLOWED)
        {
            IceAndFire.LOGGER.error("Tried to set a bad speed:" + speedFactor + " for entity:" + ourEntity, new Exception());
            return null;
        }

        job.setPathingOptions(getPathingOptions());
        pathResult = job.getResult();
        pathResult.startJob(Pathfinding.getExecutor());
        return pathResult;
    }

    @Override
    public boolean noPath() {
        return (pathResult == null || pathResult.isFinished() && pathResult.getStatus() != PathFindingStatus.CALCULATION_COMPLETE) && super.noPath();
    }

    @Override
    public void tick() {
        if (nodeProcessor instanceof NodeProcessorWalk) {
            ((NodeProcessorWalk) nodeProcessor).setEntitySize(width, height);
        } else {
            ((NodeProcessorFly) nodeProcessor).setEntitySize(width, height);
        }
        if (desiredPosTimeout > 0) {
            if (desiredPosTimeout-- <= 0) {
                desiredPos = null;
            }
        }

        if (pathResult != null) {
            if (!pathResult.isFinished()) {
                return;
            }
            else if (pathResult.getStatus() == PathFindingStatus.CALCULATION_COMPLETE)
            {
                try {
                    processCompletedCalculationResult();
                } catch (InterruptedException | ExecutionException e) {
                    IceAndFire.LOGGER.catching(e);
                }
            }
        }

        int oldIndex = this.noPath() ? 0 : this.getPath().getCurrentPathIndex();

        if (isSneaking) {
            isSneaking = false;
            entity.setSneaking(false);
        }
        //this.ourEntity.setMoveVertical(0);
        if (handleLadders(oldIndex)) {
            pathFollow();
            stuckHandler.checkStuck(this);
            return;
        }
        if (handleRails()) {
            stuckHandler.checkStuck(this);
            return;
        }

        // The following block replaces mojangs super.tick(). Why you may ask? Because it's broken, that's why.
        // The moveHelper won't move up if standing in a block with an empty bounding box (put grass, 1 layer snow, mushroom in front of a solid block and have them try jump up).
        ++this.totalTicks;
        if (!this.noPath())
        {
            if (this.canNavigate())
            {
                this.pathFollow();
            }
            else if (this.currentPath != null && !this.currentPath.isFinished())
            {
                Vector3d vector3d = this.getEntityPosition();
                Vector3d vector3d1 = this.currentPath.getPosition(this.entity);
                if (vector3d.y > vector3d1.y && !this.entity.isOnGround() && MathHelper.floor(vector3d.x) == MathHelper.floor(vector3d1.x) && MathHelper.floor(vector3d.z) == MathHelper.floor(vector3d1.z))
                {
                    this.currentPath.incrementPathIndex();
                }
            }

            DebugPacketSender.sendPath(this.world, this.entity, this.currentPath, this.maxDistanceToWaypoint);
            if (!this.noPath())
            {
                Vector3d vector3d2 = this.currentPath.getPosition(this.entity);
                BlockPos blockpos = new BlockPos(vector3d2);
                if (isEntityBlockLoaded(this.world, blockpos))
                {
                    this.entity.getMoveHelper()
                        .setMoveTo(vector3d2.x,
                            this.world.getBlockState(blockpos.down()).isAir() ? vector3d2.y : getSmartGroundY(this.world, blockpos),
                            vector3d2.z,
                            this.speed);
                }
            }
        }
        // End of super.tick.
        if (this.tryUpdatePath)
        {
            this.recomputePath();
        }
        if (pathResult != null && noPath()) {
            pathResult.setStatus(PathFindingStatus.COMPLETE);
            pathResult = null;
        }
        // TODO: should probably get updated
        // Make sure the entity isn't sleeping, tamed or chained when checking if it's stuck
        if (this.entity instanceof TameableEntity){
            if (((TameableEntity)this.entity).isTamed())
                return;
            if (this.entity instanceof EntityDragonBase){
                if (((EntityDragonBase) this.entity).isChained())
                    return;
                if (((EntityDragonBase) this.entity).isEntitySleeping())
                    return;
            }

        }

        stuckHandler.checkStuck(this);
    }

    /**
     * Similar to WalkNodeProcessor.getGroundY but not broken.
     * This checks if the block below the position we're trying to move to reaches into the block above, if so, it has to aim a little bit higher.
     * @param world the world.
     * @param pos the position to check.
     * @return the next y level to go to.
     */
    public static double getSmartGroundY(final IBlockReader world, final BlockPos pos)
    {
        final BlockPos blockpos = pos.down();
        final VoxelShape voxelshape = world.getBlockState(blockpos).getCollisionShape(world, blockpos);
        if (voxelshape.isEmpty() || voxelshape.getEnd(Direction.Axis.Y) < 1.0)
        {
            return pos.getY();
        }
        return blockpos.getY() + voxelshape.getEnd(Direction.Axis.Y);
    }

    @Nullable
    public PathResult moveToXYZ(final double x, final double y, final double z, final double speedFactor) {
        final int newX = MathHelper.floor(x);
        final int newY = (int) y;
        final int newZ = MathHelper.floor(z);

        if (pathResult != null && pathResult.getJob() instanceof PathJobMoveToLocation &&
            (
                pathResult.isComputing()
                    || (destination != null && isEqual(destination, newX, newY, newZ))
                    || (originalDestination != null && isEqual(originalDestination, newX, newY, newZ))
            )
        )
        {
            return pathResult;
        }

        final BlockPos start = AbstractPathJob.prepareStart(ourEntity);
        desiredPos = new BlockPos(newX, newY, newZ);

        return setPathJob(
                new PathJobMoveToLocation(ourEntity.world,
                        start,
                        desiredPos,
                        (int) ourEntity.getAttribute(Attributes.FOLLOW_RANGE).getValue(),
                        ourEntity),
                desiredPos, speedFactor, true);
    }

    @Override
    public boolean tryMoveToBlockPos(final BlockPos pos, final double speedFactor) {
        moveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speedFactor);
        return true;
    }
    //Return a new WalkNodeProcessor for safety reasons eg if the entity
    //has a passenger this method get's called and returning null is not a great idea
    @Override
    protected PathFinder getPathFinder(final int p_179679_1_) {
        return new PathFinder(new WalkNodeProcessor(),p_179679_1_);
    }

    @Override
    protected boolean canNavigate() {
        // Auto dismount when trying to path.
        if (ourEntity.getRidingEntity() != null)
        {
            final PathPointExtended pEx = (PathPointExtended) this.getPath().getPathPointFromIndex(this.getPath().getCurrentPathIndex());
            if (pEx.isRailsExit())
            {
                final Entity entity = ourEntity.getRidingEntity();
                ourEntity.stopRiding();
                entity.remove();
            }
            else if (!pEx.isOnRails())
            {
                if (destination == null || entity.getDistanceSq(destination.getX(), destination.getY(), destination.getZ()) > 2)
                {
                    ourEntity.stopRiding();
                }

            }
            else if ((Math.abs(pEx.x - entity.getPosX()) > 7 || Math.abs(pEx.z - entity.getPosZ()) > 7) && ourEntity.getRidingEntity() != null)
            {
                final Entity entity = ourEntity.getRidingEntity();
                ourEntity.stopRiding();
                entity.remove();
            }
        }
        return true;
    }


    @Override
    protected Vector3d getEntityPosition() {
        return this.ourEntity.getPositionVec();
    }

    @Override
    public Path getPathToPos(final BlockPos pos, final int accuracy) {
        return null;
    }

    @Override
    protected boolean isDirectPathBetweenPoints(final Vector3d start, final Vector3d end, final int sizeX, final int sizeY, final int sizeZ) {
        // TODO improve road walking. This is better in some situations, but still not great.
        return super.isDirectPathBetweenPoints(start, end, sizeX, sizeY, sizeZ);
    }

    public double getSpeedFactor() {

        if (ourEntity.isInWater())
        {
            speed = walkSpeedFactor * swimSpeedFactor;
            return speed;
        }

        speed = walkSpeedFactor;
        return walkSpeedFactor;
    }

    @Override
    public void setSpeed(final double speedFactor) {
        if (speedFactor > MAX_SPEED_ALLOWED || speedFactor < MIN_SPEED_ALLOWED) {
            IceAndFire.LOGGER.debug("Tried to set a bad speed:" + speedFactor + " for entity:" + ourEntity);
            return;
        }
        walkSpeedFactor = speedFactor;
    }

    /**
     * Deprecated - try to use BlockPos instead
     */
    @Override
    public boolean tryMoveToXYZ(final double x, final double y, final double z, final double speedFactor) {
        if (x == 0 && y == 0 && z == 0) {
            return false;
        }

        moveToXYZ(x, y, z, speedFactor);
        return true;
    }

    @Override
    public boolean tryMoveToEntityLiving(final Entity entityIn, final double speedFactor) {
        return tryMoveToBlockPos(entityIn.getPosition(), speedFactor);
    }

    // Removes stupid vanilla stuff, causing our pathpoints to occasionally be replaced by vanilla ones.
    @Override
    protected void trimPath() {
    }

    @Override
    public boolean setPath(@Nullable final Path path, final double speedFactor) {
        if (path == null) {
            clearPath();
            return false;
        }
        pathStartTime = world.getGameTime();
        return super.setPath(convertPath(path), speedFactor);
    }

    /**
     * Converts the given path to a minecolonies path if needed.
     *
     * @param path given path
     * @return resulting path
     */
    private Path convertPath(final Path path) {
        final int pathLength = path.getCurrentPathLength();
        Path tempPath = null;
        if (pathLength > 0 && !(path.getPathPointFromIndex(0) instanceof PathPointExtended)) {
            //  Fix vanilla PathPoints to be PathPointExtended
            final PathPointExtended[] newPoints = new PathPointExtended[pathLength];

            for (int i = 0; i < pathLength; ++i) {
                final PathPoint point = path.getPathPointFromIndex(i);
                if (!(point instanceof PathPointExtended)) {
                    newPoints[i] = new PathPointExtended(new BlockPos(point.x, point.y, point.z));
                } else {
                    newPoints[i] = (PathPointExtended) point;
                }
            }

            tempPath = new Path(Arrays.asList(newPoints), path.getTarget(), path.reachesTarget());

            final PathPointExtended finalPoint = newPoints[pathLength - 1];
            destination = new BlockPos(finalPoint.x, finalPoint.y, finalPoint.z);
        }

        return tempPath == null ? path : tempPath;
    }

    private boolean processCompletedCalculationResult() throws InterruptedException, ExecutionException {
        pathResult.getJob().synchToClient(entity);
        setPath(pathResult.getPath(), getSpeedFactor());

        if (pathResult != null)
            pathResult.setStatus(PathFindingStatus.IN_PROGRESS_FOLLOWING);
        
        return false;
    }

    private boolean handleLadders(int oldIndex) {
        //  Ladder Workaround
        if (!this.noPath())
        {
            final PathPointExtended pEx = (PathPointExtended) this.getPath().getPathPointFromIndex(this.getPath().getCurrentPathIndex());
            final PathPointExtended pExNext = getPath().getCurrentPathLength() > this.getPath().getCurrentPathIndex() + 1
                ? (PathPointExtended) this.getPath()
                .getPathPointFromIndex(this.getPath()
                    .getCurrentPathIndex() + 1) : null;



            final BlockPos pos = new BlockPos(pEx.x, pEx.y, pEx.z);
            if (pEx.isOnLadder() && pExNext != null && (pEx.y != pExNext.y || entity.getPosY() > pEx.y) && world.getBlockState(pos).isLadder(world, pos, ourEntity))
            {
                return handlePathPointOnLadder(pEx);
            }
            else if (ourEntity.isInWater())
            {
                return handleEntityInWater(oldIndex, pEx);
            }
            else if (world.rand.nextInt(10) == 0)
            {
                if (!pEx.isOnLadder() && pExNext != null && pExNext.isOnLadder())
                {
                    speed = getSpeedFactor() / 4.0;
                }
                else
                {
                    speed = getSpeedFactor();
                }
            }
        }
        return false;
    }

    /**
     * Determine what block the entity stands on
     *
     * @param parEntity the entity that stands on the block
     * @return the Blockstate.
     */
    private BlockPos findBlockUnderEntity(final Entity parEntity) {
        int blockX = (int) Math.round(parEntity.getPosX());
        int blockY = MathHelper.floor(parEntity.getPosY() - 0.2D);
        int blockZ = (int) Math.round(parEntity.getPosZ());
        return new BlockPos(blockX, blockY, blockZ);
    }

    /**
     * Handle rails navigation.
     *
     * @return true if block.
     */
    private boolean handleRails() {
        if (!this.noPath())
        {
            final PathPointExtended pEx = (PathPointExtended) this.getPath().getPathPointFromIndex(this.getPath().getCurrentPathIndex());
            PathPointExtended pExNext = getPath().getCurrentPathLength() > this.getPath().getCurrentPathIndex() + 1
                ? (PathPointExtended) this.getPath()
                .getPathPointFromIndex(this.getPath()
                    .getCurrentPathIndex() + 1): null;

            if (pExNext != null && pEx.x == pExNext.x && pEx.z == pExNext.z)
            {
                pExNext = getPath().getCurrentPathLength() > this.getPath().getCurrentPathIndex() + 2
                    ? (PathPointExtended) this.getPath()
                    .getPathPointFromIndex(this.getPath()
                        .getCurrentPathIndex() + 2): null;
            }

            if (pEx.isOnRails() || pEx.isRailsExit())
            {
                return handlePathOnRails(pEx, pExNext);
            }
        }
        return false;
    }

    /**
     * Handle pathing on rails.
     *
     * @param pEx     the current path point.
     * @param pExNext the next path point.
     * @return if go to next point.
     */
    private boolean handlePathOnRails(final PathPointExtended pEx, final PathPointExtended pExNext)
    {
        return false;
    }

    private boolean handlePathPointOnLadder(final PathPointExtended pEx)
    {
        Vector3d vec3 = this.getPath().getPosition(this.ourEntity);
        final BlockPos entityPos = new BlockPos(this.ourEntity.getPosition());
        if (vec3.squareDistanceTo(ourEntity.getPosX(), vec3.y, ourEntity.getPosZ()) < 0.6 && Math.abs(vec3.y - entityPos.getY()) <= 2.0)
        {
            //This way he is less nervous and gets up the ladder
            double newSpeed = 0.3;
            switch (pEx.getLadderFacing())
            {
                //  Any of these values is climbing, so adjust our direction of travel towards the ladder
                case NORTH:
                    vec3 = vec3.add(0, 0, 0.4);
                    break;
                case SOUTH:
                    vec3 = vec3.add(0, 0, -0.4);
                    break;
                case WEST:
                    vec3 = vec3.add(0.4, 0, 0);
                    break;
                case EAST:
                    vec3 = vec3.add(-0.4, 0, 0);
                    break;
                case UP:
                    vec3 = vec3.add(0, 1, 0);
                    break;
                //  Any other value is going down, so lets not move at all
                default:
                    newSpeed = 0;
                    entity.setSneaking(true);
                    isSneaking = true;
                    this.ourEntity.getMoveHelper().setMoveTo(vec3.x, vec3.y, vec3.z, 0.2);
                    break;
            }

            if (newSpeed > 0)
            {
                if (!(world.getBlockState(ourEntity.getPosition()).getBlock() instanceof LadderBlock))
                {
                    this.ourEntity.setMotion(this.ourEntity.getMotion().add(0, 0.1D, 0));
                }
                this.ourEntity.getMoveHelper().setMoveTo(vec3.x, vec3.y, vec3.z, newSpeed);
            }
            else
            {
                if (world.getBlockState(entityPos.down()).isLadder(world, entityPos.down(), ourEntity))
                {
                    this.ourEntity.setMoveVertical(-0.5f);
                }
                else
                {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleEntityInWater(int oldIndex, final PathPointExtended pEx) {
        //  Prevent shortcuts when swimming
        final int curIndex = this.getPath().getCurrentPathIndex();
        if (curIndex > 0
                && (curIndex + 1) < this.getPath().getCurrentPathLength()
                && this.getPath().getPathPointFromIndex(curIndex - 1).y != pEx.y) {
            //  Work around the initial 'spin back' when dropping into water
            oldIndex = curIndex + 1;
        }

        this.getPath().setCurrentPathIndex(oldIndex);

        Vector3d vec3d = this.getPath().getPosition(this.ourEntity);

        if (vec3d.squareDistanceTo(new Vector3d(ourEntity.getPosX(), vec3d.y, ourEntity.getPosZ())) < 0.1
                && Math.abs(ourEntity.getPosY() - vec3d.y) < 0.5) {
            this.getPath().incrementPathIndex();
            if (this.noPath()) {
                return true;
            }

            vec3d = this.getPath().getPosition(this.ourEntity);
        }

        this.ourEntity.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, getSpeedFactor());
        return false;
    }

    @Override
    protected void pathFollow() {
        getSpeedFactor();
        final int curNode = currentPath.getCurrentPathIndex();
        final int curNodeNext = curNode + 1;
        if (curNodeNext < currentPath.getCurrentPathLength()) {
            if (!(currentPath.getPathPointFromIndex(curNode) instanceof PathPointExtended)) {
                currentPath = convertPath(currentPath);
            }

            final PathPointExtended pEx = (PathPointExtended) currentPath.getPathPointFromIndex(curNode);
            final PathPointExtended pExNext = (PathPointExtended) currentPath.getPathPointFromIndex(curNodeNext);

            //  If current node is bottom of a ladder, then stay on this node until
            //  the ourEntity reaches the bottom, otherwise they will try to head out early
            if (pEx.isOnLadder() && pEx.getLadderFacing() == Direction.DOWN
                    && !pExNext.isOnLadder()) {
                final Vector3d vec3 = getEntityPosition();
                if ((vec3.y - (double) pEx.y) < MIN_Y_DISTANCE) {
                    this.currentPath.setCurrentPathIndex(curNodeNext);
                }
                return;
            }
        }

        this.maxDistanceToWaypoint = Math.max(1.2F, this.entity.getWidth());
        boolean wentAhead = false;
        boolean isTracking = AbstractPathJob.trackingMap.containsValue(ourEntity.getUniqueID());

        // TODO: Figure out a better way to derive this value ideally from the pathfinding code
        int maxDropHeight = 3;

        final HashSet<BlockPos> reached = new HashSet<>();
        // Look at multiple points, incase we're too fast
        for (int i = this.currentPath.getCurrentPathIndex(); i < Math.min(this.currentPath.getCurrentPathLength(), this.currentPath.getCurrentPathIndex() + 4); i++) {
            Vector3d next = this.currentPath.getVectorFromIndex(this.entity, i);
            if (Math.abs(this.entity.getPosX() - next.x) < (double) this.maxDistanceToWaypoint - Math.abs(this.entity.getPosY() - (next.y)) * 0.1
                    && Math.abs(this.entity.getPosZ() - next.z) < (double) this.maxDistanceToWaypoint - Math.abs(this.entity.getPosY() - (next.y)) * 0.1 &&
                    (Math.abs(this.entity.getPosY() - next.y) <= Math.min(1.0F, Math.ceil(this.entity.getHeight() / 2.0F)) ||
                            Math.abs(this.entity.getPosY() - next.y) <= Math.ceil(this.entity.getWidth() / 2) * maxDropHeight)) {
                this.currentPath.incrementPathIndex();
                wentAhead = true;

                if (isTracking) {
                    final PathPoint point = currentPath.getPathPointFromIndex(i);
                    reached.add(new BlockPos(point.x, point.y, point.z));
                }
            }
        }

        if (isTracking)
        {
            AbstractPathJob.synchToClient(reached, ourEntity);
            reached.clear();
        }

        if (currentPath.isFinished())
        {
            onPathFinish();
            return;
        }

        if (wentAhead)
        {
            return;
        }

        if (curNode >= currentPath.getCurrentPathLength() || curNode <= 1)
        {
            return;
        }

        // Check some past nodes case we fell behind.
        final Vector3d curr = this.currentPath.getVectorFromIndex(this.entity, curNode - 1);
        final Vector3d next = this.currentPath.getVectorFromIndex(this.entity, curNode);

        if (entity.getPosition().withinDistance(curr, 2.0) && entity.getPosition().withinDistance(next, 2.0))
        {
            int currentIndex = curNode - 1;
            while (currentIndex > 0)
            {
                final Vector3d tempoPos = this.currentPath.getVectorFromIndex(this.entity, currentIndex);
                if (entity.getPosition().withinDistance(tempoPos, 1.0))
                {
                    this.currentPath.setCurrentPathIndex(currentIndex);
                }
                else if (isTracking)
                {
                    reached.add(new BlockPos(tempoPos.x, tempoPos.y, tempoPos.z));
                }
                currentIndex--;
            }
        }

        if (isTracking)
        {
            AbstractPathJob.synchToClient(reached, ourEntity);
            reached.clear();
        }
    }

    /**
     * Called upon reaching the path end, reset values
     */
    private void onPathFinish()
    {
        clearPath();
    }

    public void recomputePath() {}

    /**
     * Don't let vanilla rapidly discard paths, set a timeout before its allowed to use stuck.
     */
    @Override
    protected void checkForStuck(final Vector3d positionVec3) {
        // Do nothing, unstuck is checked on tick, not just when we have a path
    }

    public boolean entityOnAndBelowPath(Entity entity, Vector3d slack) {
        Path path = getPath();
        if (path == null) {
            return false;
        }

        int closest = path.getCurrentPathIndex();
        //Search through path from the current index outwards to improve performance
        for (int i = 0; i < path.getCurrentPathLength() - 1; i++) {
            if (closest + i < path.getCurrentPathLength()) {
                PathPoint currentPoint = path.getPathPointFromIndex(closest + i);
                if (entityNearAndBelowPoint(currentPoint, entity, slack)) {
                    return true;
                }
            }
            if (closest - i >= 0) {
                PathPoint currentPoint = path.getPathPointFromIndex(closest - i);
                if (entityNearAndBelowPoint(currentPoint, entity, slack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean entityNearAndBelowPoint(PathPoint currentPoint, Entity entity, Vector3d slack) {
        return Math.abs(currentPoint.x - entity.getPosX()) < slack.getX()
            && currentPoint.y - entity.getPosY() + slack.getY() > 0
            && Math.abs(currentPoint.z - entity.getPosZ()) < slack.getZ();
    }



    @Override
    public void clearPath() {

        if (pathResult != null)
        {
            pathResult.cancel();
            pathResult.setStatus(PathFindingStatus.CANCELLED);
            pathResult = null;
        }

        destination = null;
        super.clearPath();
    }

    @Nullable
    @Override
    public PathResult moveToLivingEntity(final Entity e, final double speed) {
        return moveToXYZ(e.getPosX(), e.getPosY(), e.getPosZ(), speed);
    }

    @Nullable
    @Override
    public PathResult moveAwayFromLivingEntity(final Entity e, final double distance, final double speed) {
        return moveAwayFromXYZ(new BlockPos(e.getPosition()), distance, speed, true);
    }

    @Override
    public void setCanSwim(boolean canSwim) {
        super.setCanSwim(canSwim);
        getPathingOptions().setCanSwim(canSwim);
    }

    public BlockPos getDesiredPos() {
        return desiredPos;
    }

    /**
     * Sets the stuck handler
     *
     * @param stuckHandler handler to set
     */
    @Override
    public void setStuckHandler(final IStuckHandler stuckHandler) {
        this.stuckHandler = stuckHandler;
    }

    @Override
    public void setSwimSpeedFactor(final double factor)
    {
        this.swimSpeedFactor = factor;
    }

    public static boolean isEqual(final BlockPos coords, final int x, final int y, final int z)
    {
        return coords.getX() == x && coords.getY() == y && coords.getZ() == z;
    }

    public static boolean isEntityBlockLoaded(final IWorld world, final BlockPos pos)
    {
        return isEntityChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param x     chunk position
     * @param z     chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final IWorld world, final int x, final int z)
    {
        return isEntityChunkLoaded(world, new ChunkPos(x, z));
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param pos   chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final IWorld world, final ChunkPos pos)
    {
        return world.getChunkProvider().isChunkLoaded(pos);
    }
}
