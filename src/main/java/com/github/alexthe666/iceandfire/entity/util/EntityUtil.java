package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityUtil {
    public static void removeParts(final MultiPartParent parent) {
        List<EntityMutlipartPart> parts = parent.getCustomParts();

        if (parts != null) {
            for (EntityMutlipartPart part : parts) {
                part.discard();
            }

            parent.setCustomParts(null);
            parent.handleRemoveParts();
        }
    }

    public static void addPartsToLevel(final MultiPartParent parent) {
        List<EntityMutlipartPart> parts = parent.getCustomParts();

        if (parts != null) {
            boolean wasSuccessful = true;

            for (EntityMutlipartPart part : parts) {
                if (!EntityUtil.addPartToLevel(part)) {
                    IceAndFire.LOGGER.error("Failed to add multipart [{}] of entity [{}] to level - trying again", part, part.getParent());
                    wasSuccessful = false;
                }
            }

            if (!wasSuccessful) {
                parent.resetParts(parent.getPartScale());
            }
        }
    }

    private static boolean addPartToLevel(@NotNull final EntityMutlipartPart part) {
        Entity parent = part.getParent();

        if (parent == null || !(parent.level() instanceof ServerLevel serverLevel) || parent.isRemoved()) {
            return true;
        }

        if (parent.isAddedToWorld() && !part.isRemoved() && !part.isAddedToWorld()) {
            return serverLevel.addFreshEntity(part);
        }

        return true;
    }
}
