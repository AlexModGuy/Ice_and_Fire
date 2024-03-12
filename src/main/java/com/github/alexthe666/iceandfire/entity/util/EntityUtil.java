package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityUtil {
    public static boolean addPartToLevel(@Nullable final EntityMutlipartPart part, @NotNull final LivingEntity parent) {
        if (part == null || !(parent.level() instanceof ServerLevel serverLevel) || parent.isRemoved()) {
            return true;
        }

        if (parent.isAddedToWorld() && !part.isRemoved() && !part.isAddedToWorld()) {
            return serverLevel.addFreshEntity(part);
        }

        return true;
    }
}
