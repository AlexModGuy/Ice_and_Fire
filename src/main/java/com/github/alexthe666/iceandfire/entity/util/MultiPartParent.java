package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;

import java.util.List;

public interface MultiPartParent {
    void resetParts(float scale);
    List<EntityMutlipartPart> getCustomParts();
    void setCustomParts(final List<EntityMutlipartPart> parts);

    default void handleRemoveParts() { /* Nothing to do */ }

    default float getPartScale() {
        return 1;
    }
}
