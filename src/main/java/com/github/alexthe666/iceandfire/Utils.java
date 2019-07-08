package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class Utils {
    public static boolean isEntityDead(@Nonnull Entity entity) {
        return entity.isDead || (entity instanceof EntityDragonBase && ((EntityDragonBase) entity).isModelDead());
    }
}