package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class EntityUtils {
    public static boolean isEntityDead(@Nonnull Entity entity) {
        return entity.isDead || (entity instanceof EntityDragonBase && ((EntityDragonBase) entity).isModelDead());
    }

    public static boolean isEntityAlive(@Nonnull Entity entity) {
        return !isEntityDead(entity);
    }
}