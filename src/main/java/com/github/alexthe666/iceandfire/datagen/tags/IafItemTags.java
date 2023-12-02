package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
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
                .add(IafBlockRegistry.SAPPHIRE_ORE.get().asItem())
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem());

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
            } else if (item instanceof SwordItem) {
                tag(Tags.Items.TOOLS_SWORDS).add(item);
            } else if (item instanceof PickaxeItem) {
                tag(Tags.Items.TOOLS_PICKAXES).add(item);
            } else if (item instanceof AxeItem) {
                tag(Tags.Items.TOOLS_AXES).add(item);
            } else if (item instanceof ShovelItem) {
                tag(Tags.Items.TOOLS_SHOVELS).add(item);
            } else if (item instanceof HoeItem) {
                tag(Tags.Items.TOOLS_HOES).add(item);
            } else if (item instanceof BowItem) {
                tag(Tags.Items.TOOLS_BOWS).add(item);
            } else if (item instanceof TridentItem) {
                tag(Tags.Items.TOOLS_TRIDENTS).add(item);
            }

            if (item instanceof TieredItem || item instanceof BowItem || item instanceof TridentItem) {
                tag(Tags.Items.TOOLS).add(item);
            }
        });

        // These are also used / created by other mods
        tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "ores/silver"))).add(IafBlockRegistry.SILVER_ORE.get().asItem());
        tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "nuggets/copper"))).add(IafItemRegistry.COPPER_NUGGET.get());
        tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "nuggets/silver"))).add(IafItemRegistry.SILVER_NUGGET.get());
    }

    private static TagKey<Item> createKey(final String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }
}
