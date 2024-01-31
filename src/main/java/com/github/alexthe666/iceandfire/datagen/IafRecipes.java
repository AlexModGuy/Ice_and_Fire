package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Generates recipes without advancements
 */
public class IafRecipes extends RecipeProvider {

    public IafRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        createShaped(consumer);
        createShapeless(consumer);

    }

    private void createShaped(@NotNull final Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.AMPHITHERE_ARROW.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('X', Items.FLINT)
                .define('Y', IafItemRegistry.AMPHITHERE_FEATHER.get())
                .unlockedBy("has_item", has(IafItemRegistry.AMPHITHERE_FEATHER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.AMPHITHERE_MACUAHUITL.get())
                .pattern("OXO")
                .pattern("FXF")
                .pattern("OSO")
                .define('X', ItemTags.PLANKS)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('O', Tags.Items.OBSIDIAN)
                .define('F', IafItemRegistry.AMPHITHERE_FEATHER.get())
                .unlockedBy("has_item", has(IafItemRegistry.AMPHITHERE_FEATHER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHARCOAL)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', IafBlockRegistry.ASH.get())
                .unlockedBy("has_item", has(IafBlockRegistry.ASH.get()))
                .save(consumer, location("ash_to_charcoal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.BLINDFOLD.get())
                .pattern("SLS")
                .define('L', Tags.Items.LEATHER)
                .define('S', Tags.Items.STRING)
                .unlockedBy("has_item", has(Tags.Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.CHAIN.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', Items.CHAIN)
                .unlockedBy("has_item", has(Items.CHAIN))
                .save(consumer);

        // FIXME :: Currently uses `minecraft` namespace
        armorSet(consumer, Items.CHAIN,
                Items.CHAINMAIL_HELMET,
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_LEGGINGS,
                Items.CHAINMAIL_BOOTS
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.ITEM_COCKATRICE_SCEPTER.get())
                .pattern("S")
                .pattern("E")
                .pattern("W")
                .define('W', IafItemTags.BONES_WITHER)
                .define('S', IafItemRegistry.WITHER_SHARD.get())
                .define('E', IafItemRegistry.COCKATRICE_EYE.get())
                .unlockedBy("has_item", has(IafItemRegistry.COCKATRICE_EYE.get()))
                .save(consumer);

        armorSet(consumer, Tags.Items.INGOTS_COPPER,
                IafItemRegistry.COPPER_HELMET.get(),
                IafItemRegistry.COPPER_CHESTPLATE.get(),
                IafItemRegistry.COPPER_LEGGINGS.get(),
                IafItemRegistry.COPPER_BOOTS.get()
        );

        toolSet(consumer, Tags.Items.INGOTS_COPPER, Tags.Items.RODS_WOODEN,
                IafItemRegistry.COPPER_SWORD.get(),
                IafItemRegistry.COPPER_PICKAXE.get(),
                IafItemRegistry.COPPER_AXE.get(),
                IafItemRegistry.COPPER_SHOVEL.get(),
                IafItemRegistry.COPPER_HOE.get()
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_RED.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .define('C', IafItemRegistry.DEATH_WORM_CHITIN_RED.get())
                .define('H', IafItemRegistry.CHAIN.get())
                .define('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_WHITE.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .define('C', IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get())
                .define('H', IafItemRegistry.CHAIN.get())
                .define('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_YELLOW.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .define('C', IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get())
                .define('H', IafItemRegistry.CHAIN.get())
                .define('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .save(consumer);

        armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_RED.get(),
                IafItemRegistry.DEATHWORM_RED_HELMET.get(),
                IafItemRegistry.DEATHWORM_RED_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_RED_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_RED_BOOTS.get()
        );

        armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get(),
                IafItemRegistry.DEATHWORM_WHITE_HELMET.get(),
                IafItemRegistry.DEATHWORM_WHITE_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_WHITE_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_WHITE_BOOTS.get()
        );

        armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get(),
                IafItemRegistry.DEATHWORM_YELLOW_HELMET.get(),
                IafItemRegistry.DEATHWORM_YELLOW_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_YELLOW_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_YELLOW_BOOTS.get()
        );

        dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_COPPER,
                IafItemRegistry.DRAGONARMOR_COPPER_0.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_1.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_2.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_3.get()
        );

        dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_IRON,
                IafItemRegistry.DRAGONARMOR_IRON_0.get(),
                IafItemRegistry.DRAGONARMOR_IRON_1.get(),
                IafItemRegistry.DRAGONARMOR_IRON_2.get(),
                IafItemRegistry.DRAGONARMOR_IRON_3.get()
        );

        dragonArmorSet(consumer, IafItemTags.STORAGE_BLOCKS_SILVER,
                IafItemRegistry.DRAGONARMOR_SILVER_0.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_1.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_2.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_3.get()
        );

        dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_DIAMOND,
                IafItemRegistry.DRAGONARMOR_DIAMOND_0.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_1.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_2.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_3.get()
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,IafItemRegistry.IRON_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .define('F', Tags.Items.FEATHERS)
                .define('D', Items.IRON_HORSE_ARMOR)
                .unlockedBy("has_item", has(Items.IRON_HORSE_ARMOR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,IafItemRegistry.GOLD_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .define('F', Tags.Items.FEATHERS)
                .define('D', Items.GOLDEN_HORSE_ARMOR)
                .unlockedBy("has_item", has(Items.GOLDEN_HORSE_ARMOR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,IafItemRegistry.DIAMOND_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .define('F', Tags.Items.FEATHERS)
                .define('D', Items.DIAMOND_HORSE_ARMOR)
                .unlockedBy("has_item", has(Items.DIAMOND_HORSE_ARMOR))
                .save(consumer);

        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, IafItemRegistry.DRAGON_BONE.get(), RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DRAGON_BONE_BLOCK.get()
                , locationString("dragon_bone_block"), null
                , locationString("dragonbone"), null);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.DRAGON_BONE_BLOCK_WALL.get())
                .pattern("BBB")
                .pattern("BBB")
                .define('B', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DRAGON_BONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.DRAGON_FLUTE.get())
                .pattern("B  ")
                .pattern(" B ")
                .pattern("  I")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DRAGON_BONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.DRAGON_HORN.get())
                .pattern("  B")
                .pattern(" BB")
                .pattern("IB ")
                .define('I', Tags.Items.RODS_WOODEN)
                .define('B', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DRAGON_BONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.DRAGON_ICE_SPIKES.get(), 4)
                .pattern("I I")
                .pattern("I I")
                .define('I', IafBlockRegistry.DRAGON_ICE.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DRAGON_ICE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.NEST.get(), 8)
                .pattern("HHH")
                .pattern("HBH")
                .pattern("HHH")
                .define('H', Blocks.HAY_BLOCK)
                .define('B', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DRAGON_BONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.DRAGON_STAFF.get())
                .pattern("S")
                .pattern("T")
                .pattern("T")
                .define('T', Tags.Items.RODS_WOODEN)
                .define('S', IafItemTags.DRAGON_SKULLS)
                .unlockedBy("has_item", has(IafItemTags.DRAGON_SKULLS))
                .save(consumer);

        toolSet(consumer, IafItemRegistry.DRAGON_BONE.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONBONE_SWORD.get(),
                IafItemRegistry.DRAGONBONE_PICKAXE.get(),
                IafItemRegistry.DRAGONBONE_AXE.get(),
                IafItemRegistry.DRAGONBONE_SHOVEL.get(),
                IafItemRegistry.DRAGONBONE_HOE.get()
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.DRAGON_BOW.get())
                .pattern(" DS")
                .pattern("W S")
                .pattern(" DS")
                .define('S', Tags.Items.STRING)
                .define('W', IafItemTags.BONES_WITHER)
                .define('D', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.DRAGON_BONE.get()))
                .save(consumer);

        forgeBrick(consumer, Items.STONE_BRICKS, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_FIRE, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get());
        forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(), IafItemRegistry.FIRE_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get());
        forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(), Tags.Items.INGOTS_IRON, IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get());

        forgeBrick(consumer, Items.STONE_BRICKS, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_ICE, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get());
        forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), IafItemRegistry.ICE_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get());
        forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), Tags.Items.INGOTS_IRON, IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get());

        forgeBrick(consumer, Items.STONE_BRICKS, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get());
        forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get(), IafItemRegistry.LIGHTNING_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get());
        forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get(), Tags.Items.INGOTS_IRON, IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.DRAGON_MEAL.get())
                .pattern("BMB")
                .pattern("MBM")
                .pattern("BMB")
                .define('B', Tags.Items.BONES)
                .define('M', IafItemTags.DRAGON_FOOD_MEAT)
                .unlockedBy("has_item", has(IafItemTags.DRAGON_FOOD_MEAT))
                .save(consumer);

        compact(consumer, IafItemRegistry.DRAGONSCALES_RED.get(), IafBlockRegistry.DRAGON_SCALE_RED.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_GREEN.get(), IafBlockRegistry.DRAGON_SCALE_GREEN.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_BRONZE.get(), IafBlockRegistry.DRAGON_SCALE_BRONZE.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_GRAY.get(), IafBlockRegistry.DRAGON_SCALE_GRAY.get());

        compact(consumer, IafItemRegistry.DRAGONSCALES_BLUE.get(), IafBlockRegistry.DRAGON_SCALE_BLUE.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_WHITE.get(), IafBlockRegistry.DRAGON_SCALE_WHITE.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_SAPPHIRE.get(), IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_SILVER.get(), IafBlockRegistry.DRAGON_SCALE_SILVER.get());

        compact(consumer, IafItemRegistry.DRAGONSCALES_ELECTRIC.get(), IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_AMYTHEST.get(), IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_COPPER.get(), IafBlockRegistry.DRAGON_SCALE_COPPER.get());
        compact(consumer, IafItemRegistry.DRAGONSCALES_BLACK.get(), IafBlockRegistry.DRAGON_SCALE_BLACK.get());

        for (EnumDragonArmor type : EnumDragonArmor.values()) {
            armorSet(consumer, type.armorMaterial.getRepairIngredient(),
                    type.helmet.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );
        }

        for (EnumSeaSerpent type : EnumSeaSerpent.values()) {
            armorSet(consumer, type.scale.get(),
                    type.helmet.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );

            compact(consumer, type.scale.get(), type.scaleBlock.get());
        }

        compact(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get());

        toolSet(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_FIRE_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_HOE.get()
        );

        armorSet(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_BOOTS.get()
        );

        dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_3.get()
        );

        compact(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get());

        toolSet(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_ICE_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_HOE.get()
        );

        armorSet(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_BOOTS.get()
        );

        dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_3.get()
        );

        compact(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get());

        toolSet(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_HOE.get()
        );

        armorSet(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_BOOTS.get()
        );

        dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_3.get()
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE.get(), 8)
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .define('S', Tags.Items.STONE)
                .define('D', IafItemRegistry.DREAD_SHARD.get())
                .unlockedBy("has_item", has(IafItemRegistry.DREAD_SHARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS.get(), 4)
                .pattern("DD")
                .pattern("DD")
                .define('D', IafBlockRegistry.DREAD_STONE.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_CHISELED.get())
                .pattern("D")
                .pattern("D")
                .define('D', IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_FACE.get(), 8)
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .define('S', Items.SKELETON_SKULL)
                .define('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get(), 6)
                .pattern("DDD")
                .define('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_STAIRS.get(), 4)
                .pattern("D  ")
                .pattern("DD ")
                .pattern("DDD")
                .define('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_TILE.get(), 8)
                .pattern("DDD")
                .pattern("D D")
                .pattern("DDD")
                .define('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_TORCH.get(), 4)
                .pattern("D")
                .pattern("S")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('D', IafItemRegistry.DREAD_SHARD.get())
                .unlockedBy("has_item", has(IafItemRegistry.DREAD_SHARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafItemRegistry.EARPLUGS.get())
                .pattern("B B")
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_item", has(ItemTags.PLANKS))
                .save(consumer);

        for (EnumTroll type : EnumTroll.values()) {
            armorSet(consumer, type.leather.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, type.helmet.get())
                    .pattern("TTT")
                    .pattern("U U")
                    .define('T', type.leather.get())
                    .define('U', IafItemRegistry.TROLL_TUSK.get())
                    .unlockedBy("has_item", has(IafItemRegistry.TROLL_TUSK.get()))
                    .save(consumer);
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.GHOST_CHEST.get())
                .pattern(" E ")
                .pattern("ECE")
                .pattern(" E ")
                .define('C', Tags.Items.RODS_WOODEN)
                .define('E', IafItemRegistry.ECTOPLASM.get())
                .unlockedBy("has_item", has(IafItemRegistry.ECTOPLASM.get()))
                .save(consumer);

        dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_GOLD,
                IafItemRegistry.DRAGONARMOR_GOLD_0.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_1.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_2.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_3.get()
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.GRAVEYARD_SOIL.get())
                .pattern(" E ")
                .pattern("ECE")
                .pattern(" E ")
                .define('C', Items.COARSE_DIRT)
                .define('E', IafItemRegistry.ECTOPLASM.get())
                .unlockedBy("has_item", has(IafItemRegistry.ECTOPLASM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.MYRMEX_DESERT_RESIN.get())
                .pattern("RR")
                .pattern("RR")
                .define('R', IafItemRegistry.MYRMEX_DESERT_RESIN.get())
                .unlockedBy("has_item", has(IafItemRegistry.MYRMEX_DESERT_RESIN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IafBlockRegistry.MYRMEX_JUNGLE_RESIN.get())
                .pattern("RR")
                .pattern("RR")
                .define('R', IafItemRegistry.MYRMEX_JUNGLE_RESIN.get())
                .unlockedBy("has_item", has(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.SEA_SERPENT_ARROW.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .define('#', Tags.Items.RODS_WOODEN)
                .define('X', IafItemRegistry.SERPENT_FANG.get())
                .define('Y', IafItemTags.SCALES_SEA_SERPENT)
                .unlockedBy("has_item", has(IafItemRegistry.SERPENT_FANG.get()))
                .save(consumer);

        armorSet(consumer, IafItemRegistry.MYRMEX_DESERT_CHITIN.get(),
                IafItemRegistry.MYRMEX_DESERT_HELMET.get(),
                IafItemRegistry.MYRMEX_DESERT_CHESTPLATE.get(),
                IafItemRegistry.MYRMEX_DESERT_LEGGINGS.get(),
                IafItemRegistry.MYRMEX_DESERT_BOOTS.get()
        );

        toolSet(consumer, IafItemRegistry.MYRMEX_DESERT_CHITIN.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.MYRMEX_DESERT_SWORD.get(),
                IafItemRegistry.MYRMEX_DESERT_PICKAXE.get(),
                IafItemRegistry.MYRMEX_DESERT_AXE.get(),
                IafItemRegistry.MYRMEX_DESERT_SHOVEL.get(),
                IafItemRegistry.MYRMEX_DESERT_HOE.get()
        );

        armorSet(consumer, IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get(),
                IafItemRegistry.MYRMEX_JUNGLE_HELMET.get(),
                IafItemRegistry.MYRMEX_JUNGLE_CHESTPLATE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_LEGGINGS.get(),
                IafItemRegistry.MYRMEX_JUNGLE_BOOTS.get()
        );

        toolSet(consumer, IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.MYRMEX_JUNGLE_SWORD.get(),
                IafItemRegistry.MYRMEX_JUNGLE_PICKAXE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_AXE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_SHOVEL.get(),
                IafItemRegistry.MYRMEX_JUNGLE_HOE.get()
        );

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(IafItemRegistry.RAW_SILVER.get()), RecipeCategory.TOOLS, IafItemRegistry.SILVER_INGOT.get(), 0.7f, 200)
                .group("raw_silver")
                .unlockedBy(getHasName(IafItemRegistry.RAW_SILVER.get()), has(IafItemRegistry.RAW_SILVER.get())).save(consumer, location(getItemName(IafItemRegistry.SILVER_INGOT.get())) + "_from_smelting_" + getItemName(IafItemRegistry.RAW_SILVER.get()));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(IafItemRegistry.RAW_SILVER.get()), RecipeCategory.TOOLS, IafItemRegistry.SILVER_INGOT.get(), 0.7f, 100)
                .group("raw_silver")
                .unlockedBy(getHasName(IafItemRegistry.RAW_SILVER.get()), has(IafItemRegistry.RAW_SILVER.get())).save(consumer, location(getItemName(IafItemRegistry.SILVER_INGOT.get())) + "_from_blasting_" + getItemName(IafItemRegistry.RAW_SILVER.get()));
        compact(consumer, IafItemRegistry.SILVER_INGOT.get(), IafBlockRegistry.SILVER_BLOCK.get());
        compact(consumer, IafItemRegistry.RAW_SILVER.get(), IafBlockRegistry.RAW_SILVER_BLOCK.get());
        compact(consumer, IafItemRegistry.SILVER_NUGGET.get(), IafItemRegistry.SILVER_INGOT.get());

        armorSet(consumer, IafItemTags.INGOTS_SILVER,
                IafItemRegistry.SILVER_HELMET.get(),
                IafItemRegistry.SILVER_CHESTPLATE.get(),
                IafItemRegistry.SILVER_LEGGINGS.get(),
                IafItemRegistry.SILVER_BOOTS.get()
        );

        toolSet(consumer, IafItemTags.INGOTS_SILVER, Tags.Items.RODS_WOODEN,
                IafItemRegistry.SILVER_SWORD.get(),
                IafItemRegistry.SILVER_PICKAXE.get(),
                IafItemRegistry.SILVER_AXE.get(),
                IafItemRegistry.SILVER_SHOVEL.get(),
                IafItemRegistry.SILVER_HOE.get()
        );

        compact(consumer, IafItemRegistry.SAPPHIRE_GEM.get(), IafBlockRegistry.SAPPHIRE_BLOCK.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, IafItemRegistry.TIDE_TRIDENT.get())
                .pattern("TTT")
                .pattern("SDS")
                .pattern(" B ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('S', IafItemTags.SCALES_SEA_SERPENT)
                .define('T', IafItemRegistry.SERPENT_FANG.get())
                .define('B', IafItemRegistry.DRAGON_BONE.get())
                .unlockedBy("has_item", has(IafItemRegistry.SERPENT_FANG.get()))
                .save(consumer);
    }

    private void createShapeless(@NotNull final Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, IafItemRegistry.AMBROSIA.get())
                .requires(IafItemRegistry.PIXIE_DUST.get())
                .requires(Items.BOWL)
                .unlockedBy("has_item", has(IafItemRegistry.PIXIE_DUST.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.ASH.get())
                .requires(Ingredient.of(IafItemTags.CHARRED_BLOCKS), 9)
                .unlockedBy("has_item", has(IafItemTags.CHARRED_BLOCKS))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IafItemRegistry.BESTIARY.get())
                .requires(IafItemRegistry.MANUSCRIPT.get(), 3)
                .unlockedBy("has_item", has(IafItemRegistry.MANUSCRIPT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IafItemRegistry.CHAIN_STICKY.get())
                .requires(Tags.Items.SLIMEBALLS)
                .requires(IafItemRegistry.CHAIN.get())
                .unlockedBy("has_item", has(IafItemRegistry.CHAIN.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_INGOT)
                .requires(Ingredient.of(IafItemTags.NUGGETS_COPPER), 9)
                .unlockedBy("has_item", has(IafItemTags.NUGGETS_COPPER))
                .save(consumer, location("copper_nuggets_to_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IafItemRegistry.COPPER_NUGGET.get(), 9)
                .requires(Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_COPPER))
                .save(consumer, location("copper_ingot_to_nuggets"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, IafBlockRegistry.COPPER_PILE.get())
                .requires(Ingredient.of(IafItemTags.NUGGETS_COPPER), 2)
                .unlockedBy("has_item", has(IafItemTags.NUGGETS_COPPER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IafBlockRegistry.DRAGON_ICE.get())
                .requires(Ingredient.of(IafItemTags.FROZEN_BLOCKS), 9)
                .unlockedBy("has_item", has(IafItemTags.FROZEN_BLOCKS))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 5)
                .requires(IafItemTags.MOB_SKULLS)
                .unlockedBy("has_item", has(IafItemTags.MOB_SKULLS))
                .save(consumer, location("skull_to_bone_meal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_ARROW.get(), 5)
                .requires(IafItemRegistry.DRAGON_BONE.get())
                .requires(IafItemRegistry.WITHER_SHARD.get())
                .unlockedBy("has_item", has(IafItemRegistry.WITHER_SHARD.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get())
                .requires(Items.VINE)
                .requires(IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREADWOOD_PLANKS.get(), 4)
                .requires(IafBlockRegistry.DREADWOOD_LOG.get())
                .unlockedBy("has_item", has(IafBlockRegistry.DREADWOOD_LOG.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, IafItemRegistry.FIRE_STEW.get())
                .requires(Items.BOWL)
                .requires(Items.BLAZE_ROD)
                .requires(IafBlockRegistry.FIRE_LILY.get())
                .unlockedBy("has_item", has(IafBlockRegistry.FIRE_LILY.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, IafItemRegistry.FROST_STEW.get())
                .requires(Items.BOWL)
                .requires(Items.PRISMARINE_CRYSTALS)
                .requires(IafBlockRegistry.FROST_LILY.get())
                .unlockedBy("has_item", has(IafBlockRegistry.FROST_LILY.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, IafBlockRegistry.GOLD_PILE.get())
                .requires(Ingredient.of(Tags.Items.NUGGETS_GOLD), 2)
                .unlockedBy("has_item", has(Tags.Items.NUGGETS_GOLD))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Items.GRAVEL)
                .requires(Ingredient.of(IafItemTags.CRACKLED_BLOCKS), 9)
                .unlockedBy("has_item", has(IafItemTags.CRACKLED_BLOCKS))
                .save(consumer, location("crackled_to_gravel"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_FIRE.get())
                .requires(IafItemRegistry.DRAGONBONE_SWORD.get())
                .requires(IafItemRegistry.FIRE_DRAGON_BLOOD.get())
                .unlockedBy("has_item", has(IafItemRegistry.FIRE_DRAGON_BLOOD.get()))
                .save(consumer, location("dragonbone_sword_fire"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_ICE.get())
                .requires(IafItemRegistry.DRAGONBONE_SWORD.get())
                .requires(IafItemRegistry.ICE_DRAGON_BLOOD.get())
                .unlockedBy("has_item", has(IafItemRegistry.ICE_DRAGON_BLOOD.get()))
                .save(consumer, location("dragonbone_sword_ice"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get())
                .requires(IafItemRegistry.DRAGONBONE_SWORD.get())
                .requires(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get())
                .unlockedBy("has_item", has(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get()))
                .save(consumer, location("dragonbone_sword_lightning"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, IafItemRegistry.GHOST_SWORD.get())
                .requires(IafItemRegistry.DRAGONBONE_SWORD.get())
                .requires(IafItemRegistry.GHOST_INGOT.get())
                .unlockedBy("has_item", has(IafItemRegistry.GHOST_INGOT.get()))
                .save(consumer, location("ghost_sword"));
    }

    private void compact(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike unpacked, final ItemLike packed) {
        String packedPath = ForgeRegistries.ITEMS.getKey(packed.asItem()).getPath();
        String unpackedPath = ForgeRegistries.ITEMS.getKey(unpacked.asItem()).getPath();


        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, unpacked, RecipeCategory.BUILDING_BLOCKS, packed
                , locationString(unpackedPath + "_to_" + packedPath), null
                , locationString(packedPath + "_to_" + unpackedPath), null);
    }

    private void toolSet(@NotNull final Consumer<FinishedRecipe> consumer, final TagKey<Item> material, final TagKey<Item> handle, final ItemLike... items) {
        toolSet(consumer, Ingredient.of(material), Ingredient.of(handle), items);
    }

    private void toolSet(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike material, final TagKey<Item> handle, final ItemLike... items) {
        toolSet(consumer, Ingredient.of(material), Ingredient.of(handle), items);
    }

    private void toolSet(@NotNull final Consumer<FinishedRecipe> consumer, final TagKey<Item> material, final ItemLike handle, final ItemLike... items) {
        toolSet(consumer, Ingredient.of(material), Ingredient.of(handle), items);
    }

    private void toolSet(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike material, final ItemLike handle, final ItemLike... items) {
        toolSet(consumer, Ingredient.of(material), Ingredient.of(handle), items);
    }

    private void toolSet(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike... results) {
        for (ItemLike result : results) {
            Item item = result.asItem();

            if (item instanceof SwordItem) {
                sword(consumer, material, handle, result);
            } else if (item instanceof PickaxeItem) {
                pickaxe(consumer, material, handle, result);
            } else if (item instanceof AxeItem) {
                axe(consumer, material, handle, result);
            } else if (item instanceof ShovelItem) {
                shovel(consumer, material, handle, result);
            } else if (item instanceof HoeItem) {
                hoe(consumer, material, handle, result);
            } else {
                throw new IllegalArgumentException("Result is not a valid tool: [" + result + "]");
            }
        }
    }

    private void armorSet(@NotNull final Consumer<FinishedRecipe> consumer, final TagKey<Item> tag, final ItemLike... results) {
        armorSet(consumer, Ingredient.of(tag), results);
    }

    private void armorSet(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike item, final ItemLike... results) {
        armorSet(consumer, Ingredient.of(item), results);
    }

    private void armorSet(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike... results) {
        for (ItemLike result : results) {
            if (result.asItem() instanceof ArmorItem armorItem) {
                switch (armorItem.getType()) {
                    case HELMET -> helmet(consumer, ingredient, result);
                    case CHESTPLATE -> chestPlate(consumer, ingredient, result);
                    case LEGGINGS -> leggings(consumer, ingredient, result);
                    case BOOTS -> boots(consumer, ingredient, result);
                    default -> throw new IllegalArgumentException("Result is not a valid armor item: [" + result + "]");
                }
            } else {
                throw new IllegalArgumentException("Result is not an armor item: [" + result + "]");
            }
        }
    }

    private void helmet(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("# #")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void chestPlate(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void leggings(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void boots(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("# #")
                .pattern("# #")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void sword(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("M")
                .pattern("M")
                .pattern("H")
                .define('M', material)
                .define('H', handle)
                .unlockedBy("has_item", has(Arrays.stream(material.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void pickaxe(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("MMM")
                .pattern(" H ")
                .pattern(" H ")
                .define('M', material)
                .define('H', handle)
                .unlockedBy("has_item", has(Arrays.stream(material.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void axe(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("MM")
                .pattern("MH")
                .pattern(" H")
                .define('M', material)
                .define('H', handle)
                .unlockedBy("has_item", has(Arrays.stream(material.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void shovel(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("M")
                .pattern("H")
                .pattern("H")
                .define('M', material)
                .define('H', handle)
                .unlockedBy("has_item", has(Arrays.stream(material.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void hoe(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient material, final Ingredient handle, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("MM")
                .pattern(" H")
                .pattern(" H")
                .define('M', material)
                .define('H', handle)
                .unlockedBy("has_item", has(Arrays.stream(material.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void dragonArmorSet(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike material, final ItemLike... results) {
        dragonArmorSet(consumer, Ingredient.of(material), results);
    }

    private void dragonArmorSet(@NotNull final Consumer<FinishedRecipe> consumer, final TagKey<Item> tag, final ItemLike... results) {
        dragonArmorSet(consumer, Ingredient.of(tag), results);
    }

    private void dragonArmorSet(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike... results) {
        for (ItemLike result : results) {
            if (result instanceof ItemDragonArmor dragonArmor) {
                switch (dragonArmor.dragonSlot) {
                    case 0 -> dragonHead(consumer, ingredient, result);
                    case 1 -> dragonNeck(consumer, ingredient, result);
                    case 2 -> dragonBody(consumer, ingredient, result);
                    case 3 -> dragonTail(consumer, ingredient, result);
                    default ->
                            throw new IllegalArgumentException("Result is not a valid dragon armor [" + result + "]");
                }
            } else {
                throw new IllegalArgumentException("Result is not a dragon armor [" + result + "]");
            }
        }
    }

    private void dragonHead(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern(" ##")
                .pattern("###")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void dragonNeck(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern("###")
                .pattern(" ##")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void dragonBody(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("###")
                .pattern("# #")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void dragonTail(@NotNull final Consumer<FinishedRecipe> consumer, final Ingredient ingredient, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern("  #")
                .pattern("## ")
                .define('#', ingredient)
                .unlockedBy("has_item", has(Arrays.stream(ingredient.getItems()).findFirst().get().getItem()))
                .save(consumer);
    }

    private void forgeBrick(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike brick, final TagKey<Item> scales, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
                .pattern("SBS")
                .pattern("BSB")
                .pattern("SBS")
                .define('S', Ingredient.of(scales))
                .define('B', brick)
                .unlockedBy("has_item", has(brick.asItem()))
                .save(consumer);
    }

    private void forgeCore(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike brick, final ItemLike heart, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("BBB")
                .pattern("BHB")
                .pattern("BBB")
                .define('H', heart)
                .define('B', brick)
                .unlockedBy("has_item", has(brick.asItem()))
                .save(consumer);
    }

    private void forgeInput(@NotNull final Consumer<FinishedRecipe> consumer, final ItemLike brick, final TagKey<Item> material, final ItemLike result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("BIB")
                .pattern("I I")
                .pattern("BIB")
                .define('I', Ingredient.of(material))
                .define('B', brick)
                .unlockedBy("has_item", has(brick.asItem()))
                .save(consumer);
    }

    private static ResourceLocation location(final String path) {
        return new ResourceLocation(IceAndFire.MODID, path);
    }

    private static String locationString(final String path) {
        return IceAndFire.MODID + ":" + path;
    }

}
