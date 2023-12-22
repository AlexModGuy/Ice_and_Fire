package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.world.IafWorldData;

public interface TypedFeature {
    IafWorldData.FeatureType getFeatureType();

    String getId();
}
