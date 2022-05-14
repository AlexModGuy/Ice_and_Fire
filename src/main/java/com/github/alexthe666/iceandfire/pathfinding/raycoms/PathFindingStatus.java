package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */
public enum PathFindingStatus
{
    IN_PROGRESS_COMPUTING,
    IN_PROGRESS_FOLLOWING,
    CALCULATION_COMPLETE,
    // Marks the path as finished by an entity, aka it walked over it and reached the end
    COMPLETE,
    CANCELLED
}