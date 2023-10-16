package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class IafPOITypes {

    public static final ResourceKey<PoiType> SCRIBE_POI = registerKey("scribe");

    public static ResourceKey<PoiType> registerKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(IceAndFire.MODID, name));
    }

}
