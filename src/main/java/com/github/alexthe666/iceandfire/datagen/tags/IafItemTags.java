package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class IafItemTags extends ItemTagsProvider {
    public static TagKey<Item> MAKE_ITEM_DROPS_FIREIMMUNE = createKey("make_item_drops_fireimmune");

    public IafItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, IceAndFire.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MAKE_ITEM_DROPS_FIREIMMUNE)
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_SWORD.get())
                .add(IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get());
    }

    private static TagKey<Item> createKey(final String name) {
        return ItemTags.create(new ResourceLocation(IceAndFire.MODID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Item Tags";
    }
}
