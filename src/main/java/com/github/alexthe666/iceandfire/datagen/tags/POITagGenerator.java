package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.IafPOITypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class POITagGenerator extends PoiTypeTagsProvider {

    public POITagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(IafPOITypes.SCRIBE_POI);
    }

    private static TagKey<PoiType> create(String name) {
        return TagKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire POI Type Tags";
    }
}

