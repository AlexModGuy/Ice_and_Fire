package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class IafBlockTags extends BlockTagsProvider {
    public static TagKey<Block> DRAGON_ENVIRONMENT_BLOCKS = createKey("dragon_environment_blocks");

    public static TagKey<Block> DRAGON_CAVE_RARE_ORES = createKey("dragon_cave_rare_ores");
    public static TagKey<Block> DRAGON_CAVE_UNCOMMON_ORES = createKey("dragon_cave_uncommon_ores");
    public static TagKey<Block> DRAGON_CAVE_COMMON_ORES = createKey("dragon_cave_common_ores");

    public IafBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, future, IceAndFire.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(DRAGON_ENVIRONMENT_BLOCKS)
                .add(IafBlockRegistry.CHARRED_COBBLESTONE.get())
                .add(IafBlockRegistry.CHARRED_DIRT.get())
                .add(IafBlockRegistry.CHARRED_DIRT_PATH.get())
                .add(IafBlockRegistry.CHARRED_GRASS.get())
                .add(IafBlockRegistry.CHARRED_GRAVEL.get())
                .add(IafBlockRegistry.CHARRED_STONE.get())
                .add(IafBlockRegistry.FROZEN_COBBLESTONE.get())
                .add(IafBlockRegistry.FROZEN_DIRT.get())
                .add(IafBlockRegistry.FROZEN_DIRT_PATH.get())
                .add(IafBlockRegistry.FROZEN_GRASS.get())
                .add(IafBlockRegistry.FROZEN_GRAVEL.get())
                .add(IafBlockRegistry.FROZEN_STONE.get())
                .add(IafBlockRegistry.CRACKLED_COBBLESTONE.get())
                .add(IafBlockRegistry.CRACKLED_DIRT.get())
                .add(IafBlockRegistry.CRACKLED_DIRT_PATH.get())
                .add(IafBlockRegistry.CRACKLED_GRASS.get())
                .add(IafBlockRegistry.CRACKLED_GRASS.get())
                .add(IafBlockRegistry.CRACKLED_STONE.get());

        tag(DRAGON_CAVE_RARE_ORES)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.EMERALD_ORE);

        tag(DRAGON_CAVE_UNCOMMON_ORES)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.GOLD_ORE)
                .add(IafBlockRegistry.SILVER_ORE.get());

        tag(DRAGON_CAVE_COMMON_ORES)
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.IRON_ORE);
    }

    private static TagKey<Block> createKey(final String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(IceAndFire.MODID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Block Tags";
    }
}

