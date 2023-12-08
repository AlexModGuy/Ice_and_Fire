package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemMobSkull;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class IafItemTags extends ItemTagsProvider {
    private final static String STORAGE_BLOCK_PATH = Tags.Items.STORAGE_BLOCKS.location().getPath();
    private final static String INGOTS_PATH = Tags.Items.INGOTS.location().getPath();
    private final static String NUGGETS_PATH = Tags.Items.NUGGETS.location().getPath();
    private final static String BONES_PATH = Tags.Items.BONES.location().getPath();
    private final static String ORES_PATH = Tags.Items.ORES.location().getPath();

    public static TagKey<Item> CHARRED_BLOCKS = createKey("charred_blocks");
    public static TagKey<Item> FROZEN_BLOCKS = createKey("frozen_blocks");
    public static TagKey<Item> CRACKLED_BLOCKS = createKey("crackled_blocks");
    public static TagKey<Item> DRAGON_SKULLS = createKey("dragon_skulls");
    public static TagKey<Item> MOB_SKULLS = createKey("mob_skulls");
    public static TagKey<Item> FIRE_DRAGON_SCALES = createKey("scales/dragon/fire");
    public static TagKey<Item> ICE_DRAGON_SCALES = createKey("scales/dragon/ice");
    public static TagKey<Item> LIGHTNING_DRAGON_SCALES = createKey("scales/dragon/lightning");
    public static TagKey<Item> FIRE_DRAGON_SCALE_STORAGE_BLOCKS = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/fire");
    public static TagKey<Item> ICE_DRAGON_SCALE_STORAGE_BLOCKS = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/ice");
    public static TagKey<Item> LIGHTNING_DRAGON_SCALE_STORAGE_BLOCKS = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/lightning");
    public static TagKey<Item> DRAGON_FOOD_MEAT = createKey("dragon_food_meat");

    public static TagKey<Item> SILVER_INGOTS = createForgeKey(INGOTS_PATH + "/silver");
    public static TagKey<Item> COPPER_NUGGETS = createForgeKey(NUGGETS_PATH + "/copper");
    public static TagKey<Item> SILVER_NUGGETS = createForgeKey(NUGGETS_PATH + "/silver");
    public static TagKey<Item> WITHER_BONES = createForgeKey(BONES_PATH + "/wither");

    public static TagKey<Item> MAKE_ITEM_DROPS_FIREIMMUNE = createKey("make_item_drops_fireimmune");

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

    public IafItemTags(final DataGenerator generator, final BlockTagsProvider blockTagsProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, IceAndFire.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
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

        tag(Tags.Items.INGOTS)
//                .add(IafItemRegistry.COPPER_INGOT.get())
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

        tag(FIRE_DRAGON_SCALE_STORAGE_BLOCKS)
                .add(IafBlockRegistry.DRAGON_SCALE_RED.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GREEN.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BRONZE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_GRAY.get().asItem());

        tag(ICE_DRAGON_SCALE_STORAGE_BLOCKS)
                .add(IafBlockRegistry.DRAGON_SCALE_BLUE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_WHITE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_SILVER.get().asItem());

        tag(LIGHTNING_DRAGON_SCALE_STORAGE_BLOCKS)
                .add(IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_COPPER.get().asItem())
                .add(IafBlockRegistry.DRAGON_SCALE_BLACK.get().asItem());

        tag(Tags.Items.STORAGE_BLOCKS)
                .addTag(FIRE_DRAGON_SCALE_STORAGE_BLOCKS)
                .addTag(ICE_DRAGON_SCALE_STORAGE_BLOCKS)
                .addTag(LIGHTNING_DRAGON_SCALE_STORAGE_BLOCKS)
                .add(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get().asItem())
                .add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem())
                .add(IafBlockRegistry.SILVER_BLOCK.get().asItem())
                .add(IafBlockRegistry.DRAGON_BONE_BLOCK.get().asItem());

        tag(DRAGON_FOOD_MEAT)
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
                .addOptionalTag(new ResourceLocation("forge", "cooked_beef"))
                //
                .add(Items.BEEF, Items.COOKED_BEEF)
                .add(Items.CHICKEN, Items.COOKED_CHICKEN)
                .add(Items.MUTTON, Items.COOKED_MUTTON)
                .add(Items.PORKCHOP, Items.COOKED_PORKCHOP);

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

        tag(FIRE_DRAGON_SCALES)
                .add(IafItemRegistry.DRAGONSCALES_RED.get())
                .add(IafItemRegistry.DRAGONSCALES_GREEN.get())
                .add(IafItemRegistry.DRAGONSCALES_BRONZE.get())
                .add(IafItemRegistry.DRAGONSCALES_GRAY.get());

        tag(ICE_DRAGON_SCALES)
                .add(IafItemRegistry.DRAGONSCALES_BLUE.get())
                .add(IafItemRegistry.DRAGONSCALES_WHITE.get())
                .add(IafItemRegistry.DRAGONSCALES_SAPPHIRE.get())
                .add(IafItemRegistry.DRAGONSCALES_SILVER.get());

        tag(LIGHTNING_DRAGON_SCALES)
                .add(IafItemRegistry.DRAGONSCALES_ELECTRIC.get())
                .add(IafItemRegistry.DRAGONSCALES_AMYTHEST.get())
                .add(IafItemRegistry.DRAGONSCALES_COPPER.get())
                .add(IafItemRegistry.DRAGONSCALES_BLACK.get());

        tag(createKey("scales/dragon"))
                .addTag(FIRE_DRAGON_SCALES)
                .addTag(ICE_DRAGON_SCALES)
                .addTag(LIGHTNING_DRAGON_SCALES);

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
            } else if (item instanceof ArmorItem armorItem) {
                tag(Tags.Items.ARMORS).add(item);

                switch (armorItem.getSlot()) {
                    case HEAD -> tag(Tags.Items.ARMORS_HELMETS).add(item);
                    case CHEST -> tag(Tags.Items.ARMORS_CHESTPLATES).add(item);
                    case LEGS -> tag(Tags.Items.ARMORS_LEGGINGS).add(item);
                    case FEET -> tag(Tags.Items.ARMORS_BOOTS).add(item);
                }
            } else if (item instanceof ItemMobSkull) {
                tag(MOB_SKULLS).add(item);
            }

            if (item instanceof TieredItem || item instanceof BowItem || item instanceof TridentItem) {
                tag(Tags.Items.TOOLS).add(item);
            }
        });

        // Not sure if this should be in the forge namespace or not (or if the recipes should be using tags here)
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

        // Might be used by other mods
        tag(createForgeKey(ORES_PATH + "/silver")).add(IafBlockRegistry.SILVER_ORE.get().asItem());
        tag(createForgeKey(ORES_PATH + "/silver")).add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get().asItem());
        tag(SILVER_INGOTS).add(IafItemRegistry.SILVER_INGOT.get().asItem());
        tag(COPPER_NUGGETS).add(IafItemRegistry.COPPER_NUGGET.get());
        tag(SILVER_NUGGETS).add(IafItemRegistry.SILVER_NUGGET.get());
        tag(createForgeKey(STORAGE_BLOCK_PATH + "/silver")).add(IafBlockRegistry.SILVER_BLOCK.get().asItem());
        tag(createForgeKey(STORAGE_BLOCK_PATH + "/sapphire")).add(IafBlockRegistry.SAPPHIRE_BLOCK.get().asItem());
        tag(WITHER_BONES).add(IafItemRegistry.WITHERBONE.get());
    }

    private static TagKey<Item> createKey(final String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(IceAndFire.MODID, name));
    }

    private static TagKey<Item> createForgeKey(final String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
    }
}
