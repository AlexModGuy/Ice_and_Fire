package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * General walkToProxy for all entities.
 */
public class GeneralEntityWalkToProxy extends AbstractWalkToProxy
{

    /**
     * Creates a walkToProxy for a certain worker.
     *
     * @param entity the entity.
     */
    public GeneralEntityWalkToProxy(final MobEntity entity)
    {
        super(entity);
    }

    @Override
    public Set<BlockPos> getWayPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean careAboutY()
    {
        return false;
    }

    @Nullable
    @Override
    public BlockPos getSpecializedProxy(final BlockPos target, final double distanceToPath)
    {
        return null;
    }
}
