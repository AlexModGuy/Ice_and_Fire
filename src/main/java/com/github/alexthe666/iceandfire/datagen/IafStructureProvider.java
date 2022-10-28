package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class IafStructureProvider extends TagsProvider<StructureSet> {
    public IafStructureProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, BuiltinRegistries.STRUCTURE_SETS ,modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(IafWorldRegistry.IAF_STRUCTURE_SET)
            .add(ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(IceAndFire.MODID, "gorgon_temple")))
            .add(ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(IceAndFire.MODID, "mausoleum")))
            .add(ResourceKey.create(BuiltinRegistries.STRUCTURE_SETS.key(), new ResourceLocation(IceAndFire.MODID, "graveyard")));
    }

    @Override
    public String getName() {
        return "Ice and Fire structure-set provider";
    }
}
