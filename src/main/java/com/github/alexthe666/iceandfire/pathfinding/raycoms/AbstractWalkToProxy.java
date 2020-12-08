package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWalkToProxy implements IWalkToProxy {
    /**
     * The distance the entity can path directly without the proxy.
     */
    private static final int MIN_RANGE_FOR_DIRECT_PATH = 400;

    /**
     * The min distance a entity has to have to a proxy.
     */
    private static final int MIN_DISTANCE = 25;

    /**
     * Range to the proxy.
     */
    private static final int PROXY_RANGE = 3;

    /**
     * The entity entity associated with the proxy.
     */
    private final MobEntity entity;

    /**
     * List of proxies the entity has to follow.
     */
    private final List<BlockPos> proxyList = new ArrayList<>();

    /**
     * The current proxy the citizen paths to.
     */
    private BlockPos currentProxy;

    /**
     * Current target the entity has.
     */
    private BlockPos target;

    /**
     * Creates a walkToProxy for a certain entity.
     *
     * @param entity the entity.
     */
    protected AbstractWalkToProxy(final MobEntity entity) {
        this.entity = entity;
    }

    public static boolean isLivingAtSite(final LivingEntity entityLiving, final int x, final int y, final int z, final int range) {
        return entityLiving.func_233580_cy_().distanceSq(new Vector3i(x, y, z)) < range * range;
    }

    public static long getDistanceSquared(final BlockPos block1, final BlockPos block2) {
        final long xDiff = (long) block1.getX() - block2.getX();
        final long yDiff = (long) block1.getY() - block2.getY();
        final long zDiff = (long) block1.getZ() - block2.getZ();

        final long result = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
        if (result < 0) {
            throw new IllegalStateException("max-sqrt is to high! Failure to catch overflow with "
                    + xDiff + " | " + yDiff + " | " + zDiff);
        }
        return result;
    }

    public static long getDistanceSquared2D(final BlockPos block1, final BlockPos block2) {
        final long xDiff = (long) block1.getX() - block2.getX();
        final long zDiff = (long) block1.getZ() - block2.getZ();

        final long result = xDiff * xDiff + zDiff * zDiff;
        if (result < 0) {
            throw new IllegalStateException("max-sqrt is to high! Failure to catch overflow with "
                    + xDiff + " | " + zDiff);
        }
        return result;
    }

    /**
     * Leads the entity to a certain position due to proxies.
     *
     * @param target the position.
     * @param range  the range.
     * @return true if arrived.
     */
    public boolean walkToBlock(final BlockPos target, final int range) {
        return walkToBlock(target, range, true);
    }

    /**
     * Leads the entity to a certain position due to proxies.
     *
     * @param target the target position.
     * @param range  the range.
     * @param onMove entity on move or not?
     * @return true if arrived.
     */
    public boolean walkToBlock(final BlockPos target, final int range, final boolean onMove) {
        if (!target.equals(this.target)) {
            this.resetProxyList();
            this.target = target;
        }

        final double distanceToPath = careAboutY()
                ? getDistanceSquared(entity.func_233580_cy_(), target) : getDistanceSquared2D(entity.func_233580_cy_(), target);

        if (distanceToPath <= MIN_RANGE_FOR_DIRECT_PATH) {
            if (distanceToPath <= MIN_DISTANCE) {
                currentProxy = null;
            } else {
                currentProxy = target;
            }

            proxyList.clear();
            return takeTheDirectPath(target, range, onMove);
        }

        if (currentProxy == null) {
            currentProxy = fillProxyList(target, distanceToPath);
        }

        final double distanceToProxy = getDistanceSquared(entity.func_233580_cy_(), currentProxy);
        final double distanceToNextProxy = proxyList.isEmpty() ? getDistanceSquared(entity.func_233580_cy_(), target)
                : getDistanceSquared(entity.func_233580_cy_(), proxyList.get(0));
        final double distanceProxyNextProxy = proxyList.isEmpty() ? getDistanceSquared(currentProxy, target)
                : getDistanceSquared(currentProxy, proxyList.get(0));
        if (distanceToProxy < MIN_DISTANCE || distanceToNextProxy < distanceProxyNextProxy) {
            if (proxyList.isEmpty()) {
                currentProxy = target;
                return takeTheDirectPath(target, range, onMove);
            }

            entity.getNavigator().clearPath();
            currentProxy = proxyList.get(0);
            proxyList.remove(0);
        }

        if (currentProxy != null && !isLivingAtSiteWithMove(entity, currentProxy.getX(), currentProxy.getY(), currentProxy.getZ(), PROXY_RANGE)) {
            //only walk to the block
            return !onMove;
        }

        return !onMove;
    }

    /**
     * Getter for the proxyList.
     *
     * @return a copy of the list
     */
    public List<BlockPos> getProxyList() {
        return new ArrayList<>(proxyList);
    }

    /**
     * Add an entry to the proxy list.
     *
     * @param pos the position to add.
     */
    public void addToProxyList(final BlockPos pos) {
        proxyList.add(pos);
    }

    /**
     * Method to call to detect if an entity living is at site with move.
     *
     * @param entity the entity to check.
     * @param x      the x value.
     * @param y      the y value.
     * @param z      the z value.
     * @param range  the range.
     * @return true if so.
     */
    public boolean isLivingAtSiteWithMove(final MobEntity entity, final int x, final int y, final int z, final int range) {
        if (!isLivingAtSiteWithMove(entity, x, y, z, range)) {
            tryMoveLivingToXYZ(entity, x, y, z);
            return false;
        }
        return true;
    }

    private boolean tryMoveLivingToXYZ(MobEntity entity, int x, int y, int z) {
        return entity.getNavigator().tryMoveToXYZ(x, y, z, 1.0F);
    }

    /**
     * Getter for the entity accociated with the proxy.
     *
     * @return the entity.
     */
    public MobEntity getEntity() {
        return entity;
    }

    /**
     * Take the direct path to a certain location.
     *
     * @param target the target position.
     * @param range  the range.
     * @param onMove entity on move or not?
     * @return true if arrived.
     */
    private boolean takeTheDirectPath(final BlockPos target, final int range, final boolean onMove) {
        final boolean arrived;
        if (onMove) {
            final int targetY = careAboutY() ? target.getY() : entity.func_233580_cy_().getY();
            arrived = isLivingAtSiteWithMove(entity, target.getX(), target.getY(), target.getZ(), range)
                    || isLivingAtSite(entity, target.getX(), targetY, target.getZ(), range + 1);
        } else {
            arrived = !isLivingAtSite(entity, target.getX(), target.getY(), target.getZ(), range);
        }

        if (arrived) {
            this.target = null;
        }
        return arrived;
    }

    /**
     * Calculates a list of proxies to a certain target for a entity.
     *
     * @param target         the target.
     * @param distanceToPath the complete distance.
     * @return the first position to path to.
     */

    private BlockPos fillProxyList(final BlockPos target, final double distanceToPath) {
        @Nullable BlockPos proxyPoint = getSpecializedProxy(target, distanceToPath);

        if (proxyPoint == null) {
            proxyPoint = getProxy(target, entity.func_233580_cy_(), distanceToPath);
        }

        if (!proxyList.isEmpty()) {
            proxyList.remove(0);
        }

        return proxyPoint;
    }

    /**
     * Reset the proxy.
     */
    private void resetProxyList() {
        currentProxy = null;
        proxyList.clear();
    }

    /**
     * Returns a proxy point to the goal.
     *
     * @param target         the target.
     * @param position       the position.
     * @param distanceToPath the total distance.
     * @return a proxy or, if not applicable null.
     */

    protected BlockPos getProxy(final BlockPos target, final BlockPos position, final double distanceToPath) {
        double weight = Double.MAX_VALUE;
        BlockPos proxyPoint = null;
        double distance = Double.MAX_VALUE;

        for (final BlockPos wayPoint : getWayPoints()) {
            final double simpleDistance = careAboutY() ? getDistanceSquared(position, wayPoint) : getDistanceSquared2D(position, wayPoint);
            final double targetDistance = careAboutY() ? getDistanceSquared(wayPoint, target) : getDistanceSquared2D(wayPoint, target);
            final double currentWeight = simpleDistance * simpleDistance + targetDistance + targetDistance;
            if (currentWeight < weight
                    && targetDistance < distanceToPath
                    && simpleDistance > MIN_DISTANCE
                    && simpleDistance < distanceToPath
                    && !proxyList.contains(wayPoint)) {
                proxyPoint = wayPoint;
                weight = currentWeight;
                distance = targetDistance;
            }
        }

        if (proxyList.contains(proxyPoint)) {
            return target;
        }

        if (proxyPoint != null) {
            proxyList.add(proxyPoint);

            getProxy(target, proxyPoint, distance);

            return proxyList.get(0);
        }

        //No proxy point exists.
        return target;
    }

    @Override
    public BlockPos getCurrentProxy() {
        return currentProxy;
    }

    @Override
    public void reset() {
        this.target = null;
    }
}