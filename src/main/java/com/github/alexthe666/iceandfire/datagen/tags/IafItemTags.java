package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemMobSkull;
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

import java.util.concurrent.CompletableFuture;

public class IafItemTags extends ItemTagsProvider {
    private final static String STORAGE_BLOCK_PATH = Tags.Items.STORAGE_BLOCKS.location().getPath();
    private final static String INGOTS_PATH = Tags.Items.INGOTS.location().getPath();
    private final static String NUGGETS_PATH = Tags.Items.NUGGETS.location().getPath();
    private final static String BONES_PATH = Tags.Items.BONES.location().getPath();
    private final static String ORES_PATH = Tags.Items.ORES.location().getPath();
    private final static String GEMS = Tags.Items.GEMS.location().getPath();

    // Recipes
    public static TagKey<Item> CHARRED_BLOCKS = createKey("charred_blocks");
    public static TagKey<Item> FROZEN_BLOCKS = createKey("frozen_blocks");
    public static TagKey<Item> CRACKLED_BLOCKS = createKey("crackled_blocks");
    public static TagKey<Item> DRAGON_SKULLS = createKey("dragon_skulls");
    public static TagKey<Item> MOB_SKULLS = createKey("mob_skulls");
    public static TagKey<Item> SCALES_DRAGON_FIRE = createKey("scales/dragon/fire");
    public static TagKey<Item> SCALES_DRAGON_ICE = createKey("scales/dragon/ice");
    public static TagKey<Item> SCALES_DRAGON_LIGHTNING = createKey("scales/dragon/lightning");
    public static TagKey<Item> SCALES_SEA_SERPENT = createKey("scales/sea_serpent");
    public static TagKey<Item> DRAGON_FOOD_MEAT = createKey("dragon_food_meat");

    // Forge (+ Recipes)
    public static TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_FIRE = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/fire");
    public static TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_ICE = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/ice");
    public static TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/lightning");
    public static TagKey<Item> STORAGE_BLOCKS_SILVER = createForgeKey(STORAGE_BLOCK_PATH + "/silver");
    public static TagKey<Item> GEMS_SAPPHIRE = createForgeKey(GEMS + "/sapphire");
    public static TagKey<Item> INGOTS_SILVER = createForgeKey(INGOTS_PATH + "/silver");
    public static TagKey<Item> NUGGETS_COPPER = createForgeKey(NUGGETS_PATH + "/copper");
    public static TagKey<Item> NUGGETS_SILVER = createForgeKey(NUGGETS_PATH + "/silver");
    public static TagKey<Item> BONES_WITHER = createForgeKey(BONES_PATH + "/wither");

    // Logic
    public static TagKey<Item> MAKE_ITEM_DROPS_FIREIMMUNE = createKey("make_item_drops_fireimmune");
    public static TagKey<Item> DRAGON_ARROWS = createKey("dragon_arrows");

    public static TagKey<Item> DRAGON_BLOODS = createKey("dragon_bloods");
    public static TagKey<Item> DRAGON_HEARTS = createKey("dragon_hearts");

    public static TagKey<Item> BREED_AMPITHERE = createKey("breed_ampithere");
    public static TagKey<Item> BREED_HIPPOCAMPUS = createKey("breed_hippocampus");
    public static TagKey<Item> BREED_HIPPOGRYPH = createKey("breed_hippogryph");
    public static TagKey<Item> HEAL_AMPITHERE = createKey("heal_ampithere");
    public static TagKey<Item> HEAL_COCKATRICE = createKey("heal_cockatrice");
    public static TagKey<Item> HEAL_HIPPOCAMPUS = createKey("heal_hippocampus");
    public static TagKey<Item> HEAL_PIXIE = createKey("heal_pixie");
    public static TagKey<Item> TAME_HIPPOGRYPH = createKey("tame_hippogryph");
    public static TagKey<Item> TAME_PIXIE = createKey("tame_pixie");
    public static TagKey<Item> TEMPT_DRAGON = createKey("tempt_dragon");
    public static TagKey<Item> TEMPT_HIPPOCAMPUS = createKey("tempt_hippocampus");
    public static TagKey<Item> TEMPT_HIPPOGRYPH = createKey("tempt_hippogryph");

    public IafItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, IceAndFire.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(IafBlockTags.CHARRED_BLOCKS, CHARRED_BLOCKS);
        copy(IafBlockTags.FROZEN_BLOCKS, FROZEN_BLOCKS);
        copy(IafBlockTags.CRACKLED_BLOCKS, CRACKLED_BLOCKS);

        tag(DRAGON_SKULLS)
                .add(IafItemRegistry.DRAGON_SKULL_FIRE.get())
                .add(IafItemRegistry.DRAGON_SKULL_ICE.get())
                .add(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get());

        tag(MOB_SKULLS)
                .addTag(DRAGON_SKULLS);

        tag(MAKE_ITEM_DROPS_FIREIMMUNE)
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_SWORD.get())
                .add(IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_PICKAXE.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_AXE.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_SHOVEL.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_HOE.get());

        tag(DRAGON_ARROWS)
                .add(IafItemRegistry.DRAGONBONE_ARROW.get());

        tag(DRAGON_BLOODS)
                .add(IafItemRegistry.FIRE_DRAGON_BLOOD.get())
                .add(IafItemRegistry.ICE_DRAGON_BLOOD.get())
                .add(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get());

        tag(Tags.Items.INGOTS)
//                .add(IafItemRegistry.COPPER_INGOT.get())
                .add(IafItemRegistry.GHOST_INGOT.get())
                .add(IafItemRegistry.SILVER_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get())
                .add(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get());
        tag(INGOTS_SILVER).add(IafItemRegistry.SILVER_INGOT.get().asItem());
        
        tag(Tags.Items.NUGGETS)
                .add(IafItemRegistry.COPPER_NUGGET.get())
                .add(IafItemRegistry.SILVER_NUGGET.get());

        tag(NUGGETS_COPPER).add(IafItemRegistry.COPPER_NUGGET.get());
        tag(NUGGETS_SILVER).add(IafItemRegistry.SILVER_NUGGET.get());
        
        tag(Tags.Items.ORES)
                .add(IafBlockRegistry.SILVER_ORE.get().asItem())
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem())
                .add(IafBlockRegistry.SAPPHIRE_ORE.get().asItem());
        
        tag(createForgeKey(ORES_PATH + "/silver")).add(IafBlockRegistry.SILVER_ORE.get().asItem());
        tag(createForgeKey(ORES_PATH + "/silver")).add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem());

        
        tag(Tags.Items.RAW_MATERIALS)
                .add(IafItemRegistry.RAW_SILVER.get());
        tag(createForgeKey("raw_materials/silver")).add(IafItemRegistry.RAW_SILVER.get());

        tag(Tags.Items.GEMS)
                .add(IafItemRegistry.SAPPHIRE_GEM.get());

        tag(GEMS_SAPPHIRE).add(IafItemRegistry.SAPPHIRE_GEM.get());

        tag(Tags.Items.BONES)
                .add(IafItemRegistry.DRAGON_BONE.get())
                .add(IafItemRegistry.WITHERBONE.get());
        tag(BONES_WITHER).add(IafItemRegistry.WITHERBONE.get());

        tag(Tags.Items.EGGS)
                .add(IafItemRegistry.HIPPOGRYPH_EGG.get())
                .add(IafItemRegistry.DEATHWORM_EGG.get())
                .add(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get())
                .add(IafItemRegistry.MYRMEX_DESERT_EGG.get())
                .add(IafItemRegistry.MYRMEX_JUNGLE_EGG.get());

        // Not sure if this should be in the forge namespace or not (or if the recipes should be using tags here)
        tag(STORAGE_BLOCKS_SCALES_DRAGON_FIRE)
                .add(IafBlockRegistry.DRAGON_SCALE_RED.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GREEN.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BRONZE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GRAY.get().asItem());

        tag(STORAGE_BLOCKS_SCALES_DRAGON_ICE)
                .add(IafBlockRegistry.DRAGON_SCALE_BLUE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_WHITE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SILVER.get().asItem());

        tag(STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING)
                .add(IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_COPPER.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BLACK.get().asItem());
        //

        tag(Tags.Items.STORAGE_BLOCKS)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_FIRE)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_ICE)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING)
                .add(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get().asItem())
                .add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.SILVER_BLOCK.get().asItem())
                .add(IafBlockRegistry.RAW_SILVER_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGON_BONE_BLOCK.get().asItem());
        tag(STORAGE_BLOCKS_SILVER).add(IafBlockRegistry.SILVER_BLOCK.get().asItem());
        tag(createForgeKey(STORAGE_BLOCK_PATH + "/raw_silver")).add(IafBlockRegistry.RAW_SILVER_BLOCK.get().asItem());
        tag(createForgeKey(STORAGE_BLOCK_PATH + "/sapphire")).add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem());
        
        tag(DRAGON_FOOD_MEAT)
                // Vanilla
                .add(Items.BEEF, Items.COOKED_BEEF)
                .add(Items.CHICKEN, Items.COOKED_CHICKEN)
                .add(Items.MUTTON, Items.COOKED_MUTTON)
                .add(Items.PORKCHOP, Items.COOKED_PORKCHOP)
                // Farmer's Delight
//                .addOptionalTag(new ResourceLocation("forge", "raw_fishes"))
                .addOptionalTag(new ResourceLocation("forge", "raw_mutton"))
                .addOptionalTag(new ResourceLocation("forge", "raw_pork"))
                .addOptionalTag(new ResourceLocation("forge", "raw_chicken"))
                .addOptionalTag(new ResourceLocation("forge", "raw_beef"))
//                .addOptionalTag(new ResourceLocation("forge", "cooked_fishes"))
                .addOptionalTag(new ResourceLocation("forge", "cooked_mutton"))
                .addOptionalTag(new ResourceLocation("forge", "cooked_pork"))
                .addOptionalTag(new ResourceLocation("forge", "cooked_chicken"))
                .addOptionalTag(new ResourceLocation("forge", "cooked_beef"));


        tag(BREED_AMPITHERE)
                .add(Items.COOKIE);

        tag(BREED_HIPPOCAMPUS)
                .add(Items.PRISMARINE_CRYSTALS);

        tag(BREED_HIPPOGRYPH)
                .add(Items.RABBIT_STEW);

        tag(TAME_HIPPOGRYPH)
                .add(Items.RABBIT_FOOT);

        tag(HEAL_AMPITHERE)
                .add(Items.COCOA_BEANS);

        tag(HEAL_COCKATRICE)
                .addTag(Tags.Items.SEEDS)
                .add(Items.ROTTEN_FLESH);

        tag(HEAL_HIPPOCAMPUS)
                .add(Items.KELP);

        tag(HEAL_PIXIE)
                .add(Items.SUGAR);

        tag(TAME_PIXIE)
                .add(Items.CAKE);

        tag(TEMPT_DRAGON)
                .add(IafItemRegistry.FIRE_STEW.get());

        tag(TEMPT_HIPPOCAMPUS)
                .add(Items.KELP)
                .add(Items.PRISMARINE_CRYSTALS);

        tag(TEMPT_HIPPOGRYPH)
                .add(Items.RABBIT)
                .add(Items.COOKED_RABBIT);

        tag(SCALES_DRAGON_FIRE)
                .add(IafItemRegistry.DRAGONSCALES_RED.get())
                .add(IafItemRegistry.DRAGONSCALES_GREEN.get())
                .add(IafItemRegistry.DRAGONSCALES_BRONZE.get())
                .add(IafItemRegistry.DRAGONSCALES_GRAY.get());

        tag(SCALES_DRAGON_ICE)
                .add(IafItemRegistry.DRAGONSCALES_BLUE.get())
                .add(IafItemRegistry.DRAGONSCALES_WHITE.get())
                .add(IafItemRegistry.DRAGONSCALES_SAPPHIRE.get())
                .add(IafItemRegistry.DRAGONSCALES_SILVER.get());

        tag(SCALES_DRAGON_LIGHTNING)
                .add(IafItemRegistry.DRAGONSCALES_ELECTRIC.get())
                .add(IafItemRegistry.DRAGONSCALES_AMYTHEST.get())
                .add(IafItemRegistry.DRAGONSCALES_COPPER.get())
                .add(IafItemRegistry.DRAGONSCALES_BLACK.get());

        tag(createKey("scales/dragon"))
                .addTag(SCALES_DRAGON_FIRE)
                .addTag(SCALES_DRAGON_ICE)
                .addTag(SCALES_DRAGON_LIGHTNING);

        tag(DRAGON_HEARTS)
                .add(IafItemRegistry.FIRE_DRAGON_HEART.get())
                .add(IafItemRegistry.ICE_DRAGON_HEART.get())
                .add(IafItemRegistry.LIGHTNING_DRAGON_HEART.get());

        IafItemRegistry.ITEMS.getEntries().forEach(registryObject -> {
            Item item = registryObject.get();

            if (item instanceof ItemSeaSerpentScales) {
                tag(SCALES_SEA_SERPENT).add(item);
            } else if (item instanceof ArrowItem) {
                tag(ItemTags.ARROWS).add(item);
            } else if (item instanceof SwordItem) {
                tag(ItemTags.SWORDS).add(item);
            } else if (item instanceof PickaxeItem) {
                tag(ItemTags.PICKAXES).add(item);
            } else if (item instanceof AxeItem) {
                tag(ItemTags.AXES).add(item);
            } else if (item instanceof ShovelItem) {
                tag(ItemTags.SHOVELS).add(item);
            } else if (item instanceof HoeItem) {
                tag(ItemTags.HOES).add(item);
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
            } else if (item instanceof ItemMobSkull) {
                tag(MOB_SKULLS).add(item);
            }

            if (item instanceof TieredItem || item instanceof BowItem || item instanceof TridentItem) {
                tag(Tags.Items.TOOLS).add(item);

                if (item instanceof TridentItem) {
                    tag(ItemTags.TOOLS).add(item);
                }
            }
        });
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
