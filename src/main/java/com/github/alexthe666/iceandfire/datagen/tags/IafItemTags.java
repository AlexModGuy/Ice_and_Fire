package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

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
                .add(IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_PICKAXE.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_AXE.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_SHOVEL.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_HOE.get());

        tag(Tags.Items.INGOTS)
                .add(IafItemRegistry.COPPER_INGOT.get())
                .add(IafItemRegistry.GHOST_INGOT.get())
                .add(IafItemRegistry.SILVER_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get());

        tag(Tags.Items.NUGGETS)
                .add(IafItemRegistry.COPPER_NUGGET.get())
                .add(IafItemRegistry.SILVER_NUGGET.get());

        tag(Tags.Items.ORES)
                .add(IafBlockRegistry.SILVER_ORE.get().asItem())
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem())
                .add(IafBlockRegistry.SAPPHIRE_ORE.get().asItem());

        tag(Tags.Items.GEMS)
                .add(IafItemRegistry.SAPPHIRE_GEM.get());

        tag(Tags.Items.BONES)
                .add(IafItemRegistry.DRAGON_BONE.get())
                .add(IafItemRegistry.WITHERBONE.get());

        tag(Tags.Items.EGGS)
                .add(IafItemRegistry.HIPPOGRYPH_EGG.get())
                .add(IafItemRegistry.DEATHWORM_EGG.get())
                .add(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get())
                .add(IafItemRegistry.MYRMEX_DESERT_EGG.get())
                .add(IafItemRegistry.MYRMEX_JUNGLE_EGG.get());

        tag(Tags.Items.STORAGE_BLOCKS)
                .add(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get().asItem())
                .add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.SILVER_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGON_BONE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_RED.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GREEN.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BRONZE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GRAY.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BLUE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_WHITE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SILVER.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_COPPER.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BLACK.get().asItem());

        TagKey<Item> fireDragonScales = createKey("scales/dragon/fire");
        tag(fireDragonScales)
                .add(IafItemRegistry.DRAGONSCALES_RED.get())
                .add(IafItemRegistry.DRAGONSCALES_GREEN.get())
                .add(IafItemRegistry.DRAGONSCALES_BRONZE.get())
                .add(IafItemRegistry.DRAGONSCALES_GRAY.get());

        TagKey<Item> iceDragonScales = createKey("scales/dragon/ice");
        tag(iceDragonScales)
                .add(IafItemRegistry.DRAGONSCALES_BLUE.get())
                .add(IafItemRegistry.DRAGONSCALES_WHITE.get())
                .add(IafItemRegistry.DRAGONSCALES_SAPPHIRE.get())
                .add(IafItemRegistry.DRAGONSCALES_SILVER.get());

        TagKey<Item> lightningDragonScales = createKey("scales/dragon/lightning");
        tag(lightningDragonScales)
                .add(IafItemRegistry.DRAGONSCALES_ELECTRIC.get())
                .add(IafItemRegistry.DRAGONSCALES_AMYTHEST.get())
                .add(IafItemRegistry.DRAGONSCALES_COPPER.get())
                .add(IafItemRegistry.DRAGONSCALES_BLACK.get());

        tag(createKey("scales/dragon"))
                .addTag(fireDragonScales)
                .addTag(iceDragonScales)
                .addTag(lightningDragonScales);

        tag(createKey("hearts"))
                .add(IafItemRegistry.FIRE_DRAGON_HEART.get())
                .add(IafItemRegistry.ICE_DRAGON_HEART.get())
                .add(IafItemRegistry.LIGHTNING_DRAGON_HEART.get())
                .add(IafItemRegistry.HYDRA_HEART.get());

        TagKey<Item> seaSerpentScales = createKey("scales/sea_serpent");

        IafItemRegistry.ITEMS.getEntries().forEach(registryObject -> {
            Item item = registryObject.get();

            if (item instanceof ItemSeaSerpentScales) {
                tag(seaSerpentScales).add(item);
            } else if (item instanceof ArrowItem) {
                tag(ItemTags.ARROWS).add(item);
            } else if (item instanceof SwordItem) {
                tag(Tags.Items.TOOLS).add(item);
            } else if (item instanceof PickaxeItem) {
                tag(Tags.Items.TOOLS).add(item);
            } else if (item instanceof AxeItem) {
                tag(Tags.Items.TOOLS).add(item);
            } else if (item instanceof ShovelItem) {
                tag(Tags.Items.TOOLS).add(item);
            } else if (item instanceof HoeItem) {
                tag(Tags.Items.TOOLS).add(item);
            } else if (item instanceof BowItem) {
                tag(Tags.Items.TOOLS_BOWS).add(item);
            } else if (item instanceof TridentItem) {
                tag(Tags.Items.TOOLS_TRIDENTS).add(item);
            } else if (item instanceof ArmorItem armorItem) {
                tag(Tags.Items.ARMORS).add(item);

                switch (armorItem.getType()) {
                    case HELMET -> tag(Tags.Items.ARMORS_HELMETS).add(item);
                    case CHESTPLATE -> tag(Tags.Items.ARMORS_CHESTPLATES).add(item);
                    case LEGGINGS -> tag(Tags.Items.ARMORS_LEGGINGS).add(item);
                    case BOOTS -> tag(Tags.Items.ARMORS_BOOTS).add(item);
                }
            }

            if (item instanceof TieredItem || item instanceof BowItem || item instanceof TridentItem) {
                tag(Tags.Items.TOOLS).add(item);
            }
        });

        // Not sure if this should be in the forge namespace or not (or if the recipes should be using tags here)
        tag(createForgeKey("ingots/dragonsteel_fire")).add(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get());
        tag(createForgeKey("ingots/dragonsteel_ice")).add(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get());
        tag(createForgeKey("ingots/dragonsteel_lightning")).add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get());
        tag(createForgeKey("storage_blocks/dragonsteel_fire")).add(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get().asItem());
        tag(createForgeKey("storage_blocks/dragonsteel_ice")).add(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get().asItem());
        tag(createForgeKey("storage_blocks/dragonsteel_lightning")).add(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get().asItem());
        tag(createForgeKey("storage_blocks/fire_dragon_scale"))
                .add(IafBlockRegistry.DRAGON_SCALE_RED.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GREEN.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BRONZE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GRAY.get().asItem());
        tag(createForgeKey("storage_blocks/ice_dragon_scale"))
                .add(IafBlockRegistry.DRAGON_SCALE_BLUE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_WHITE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SILVER.get().asItem());
        tag(createForgeKey("storage_blocks/lightning_dragon_scale"))
                .add(IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_COPPER.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BLACK.get().asItem());

        // These are also used / created by other mods
        tag(createForgeKey("ores/silver")).add(IafBlockRegistry.SILVER_ORE.get().asItem());
        tag(createForgeKey("ores/silver")).add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem());
        tag(createForgeKey("ingots/copper")).add(IafItemRegistry.COPPER_INGOT.get().asItem());
        tag(createForgeKey("ingots/silver")).add(IafItemRegistry.SILVER_INGOT.get().asItem());
        tag(createForgeKey("nuggets/copper")).add(IafItemRegistry.COPPER_NUGGET.get());
        tag(createForgeKey("nuggets/silver")).add(IafItemRegistry.SILVER_NUGGET.get());
        tag(createForgeKey("storage_blocks/silver")).add(IafBlockRegistry.SILVER_BLOCK.get().asItem());
        tag(createForgeKey("storage_blocks/sapphire")).add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem());
    }

    private static TagKey<Item> createKey(final String name) {
        return ItemTags.create(new ResourceLocation(IceAndFire.MODID, name));
    }

    private static TagKey<Item> createForgeKey(final String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("forge", name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Item Tags";
    }


}
