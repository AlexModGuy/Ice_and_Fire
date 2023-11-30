package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class IafItemTags extends ItemTagsProvider {
    public static TagKey<Item> MAKE_ITEM_DROPS_FIREIMMUNE = createKey("make_item_drops_fireimmune");

    public IafItemTags(final DataGenerator generator, final BlockTagsProvider blockTagsProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(MAKE_ITEM_DROPS_FIREIMMUNE)
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_SWORD.get())
                .add(IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get());
    }

    private static TagKey<Item> createKey(final String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }
}
