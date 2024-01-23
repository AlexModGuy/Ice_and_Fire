package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityUtil {
    public static void updatePart(@Nullable final EntityMutlipartPart part, @NotNull final LivingEntity parent) {
        if (part == null || !(parent.level() instanceof ServerLevel serverLevel) || parent.isRemoved()) {
            return;
        }

        if (!part.shouldContinuePersisting()) {
            UUID uuid = part.getUUID();
            Entity existing = serverLevel.getEntity(uuid);

            // Update UUID if a different entity with the same UUID exists already
            if (existing != null && existing != part) {
                while (serverLevel.getEntity(uuid) != null) {
                    uuid = Mth.createInsecureUUID(parent.getRandom());
                }

                IceAndFire.LOGGER.debug("Updated the UUID of [{}] due to a clash with [{}]", part, existing);
            }

            part.setUUID(uuid);
            serverLevel.addFreshEntity(part);
        }

        part.setParent(parent);
    }
}
