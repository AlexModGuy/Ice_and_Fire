package com.github.alexthe666.iceandfire.recipe;

import java.util.ArrayList;
import java.util.List;

import com.github.alexthe666.iceandfire.entity.EntityAmphithereArrow;
import com.github.alexthe666.iceandfire.entity.EntityCockatriceEgg;
import com.github.alexthe666.iceandfire.entity.EntityDeathWormEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.EntityHydraArrow;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpentArrow;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.world.World;

public class IafRecipeRegistry {

    public static final BannerPattern PATTERN_FIRE = addBanner("fire", new ItemStack(IafItemRegistry.FIRE_DRAGON_HEART));
    public static final BannerPattern PATTERN_ICE = addBanner("ice", new ItemStack(IafItemRegistry.ICE_DRAGON_HEART));
    public static final BannerPattern PATTERN_LIGHTNING = addBanner("lightning", new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_HEART));
    public static final BannerPattern PATTERN_FIRE_HEAD = addBanner("fire_head", new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE));
    public static final BannerPattern PATTERN_ICE_HEAD = addBanner("ice_head", new ItemStack(IafItemRegistry.DRAGON_SKULL_ICE));
    public static final BannerPattern PATTERN_LIGHTNING_HEAD = addBanner("lightning_head", new ItemStack(IafItemRegistry.DRAGON_SKULL_LIGHTNING));
    public static final BannerPattern PATTERN_AMPHITHERE = addBanner("amphithere", new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER));
    public static final BannerPattern PATTERN_BIRD = addBanner("bird", new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER));
    public static final BannerPattern PATTERN_EYE = addBanner("eye", new ItemStack(IafItemRegistry.CYCLOPS_EYE));
    public static final BannerPattern PATTERN_FAE = addBanner("fae", new ItemStack(IafItemRegistry.PIXIE_WINGS));
    public static final BannerPattern PATTERN_FEATHER = addBanner("feather", new ItemStack(Items.FEATHER));
    public static final BannerPattern PATTERN_GORGON = addBanner("gorgon", new ItemStack(IafItemRegistry.GORGON_HEAD));
    public static final BannerPattern PATTERN_HIPPOCAMPUS = addBanner("hippocampus", new ItemStack(IafItemRegistry.HIPPOCAMPUS_FIN));
    public static final BannerPattern PATTERN_HIPPOGRYPH_HEAD = addBanner("hippogryph_head", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
    public static final BannerPattern PATTERN_MERMAID = addBanner("mermaid", new ItemStack(IafItemRegistry.SIREN_TEAR));
    public static final BannerPattern PATTERN_SEA_SERPENT = addBanner("sea_serpent", new ItemStack(IafItemRegistry.SERPENT_FANG));
    public static final BannerPattern PATTERN_TROLL = addBanner("troll", new ItemStack(IafItemRegistry.TROLL_TUSK));
    public static final BannerPattern PATTERN_WEEZER = addBanner("weezer", new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM));
    public static final BannerPattern PATTERN_DREAD = addBanner("dread", new ItemStack(IafItemRegistry.DREAD_SHARD));
    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> LIGHTNING_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD), new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT)));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD), new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT)));
        LIGHTNING_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_BLOOD), new ItemStack(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT)));


        DispenserBlock.registerDispenseBehavior(IafItemRegistry.STYMPHALIAN_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(IafEntityRegistry.STYMPHALIAN_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = ArrowEntity.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.AMPHITHERE_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(IafEntityRegistry.AMPHITHERE_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityAmphithereArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.SEA_SERPENT_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(IafEntityRegistry.SEA_SERPENT_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntitySeaSerpentArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.DRAGONBONE_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW, position.getX(), position.getY(), position.getZ(), worldIn);
                entityarrow.pickupStatus = EntityDragonArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.HYDRA_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityHydraArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.HIPPOGRYPH_EGG, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHippogryphEgg entityarrow = new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG, worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.ROTTEN_EGG, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityCockatriceEgg entityarrow = new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG, position.getX(), position.getY(), position.getZ(), worldIn);
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.DEATHWORM_EGG, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, false);
                return entityarrow;
            }
        });
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.DEATHWORM_EGG_GIGANTIC, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, true);
                return entityarrow;
            }
        });
        /*
        OreDictionary.registerOre("desertMyrmexEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("jungleMyrmexEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_DIRT);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_GRASS);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_GRASS_PATH);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_GRAVEL);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_COBBLESTONE);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.CHARRED_STONE);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_DIRT);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_GRASS);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_GRASS_PATH);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_GRAVEL);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_COBBLESTONE);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.FROZEN_STONE);
        OreDictionary.registerOre("ingotFireDragonsteel", IafItemRegistry.DRAGONSTEEL_FIRE_INGOT);
        OreDictionary.registerOre("blockFireDragonsteel", IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK);
        OreDictionary.registerOre("ingotIceDragonsteel", IafItemRegistry.DRAGONSTEEL_ICE_INGOT);
        OreDictionary.registerOre("blockIceDragonsteel", IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK);
        OreDictionary.registerOre("ingotSilver", IafItemRegistry.SILVER_INGOT);
        OreDictionary.registerOre("nuggetSilver", IafItemRegistry.SILVER_NUGGET);
        OreDictionary.registerOre("oreSilver", IafBlockRegistry.SILVER_ORE);
        OreDictionary.registerOre("blockSilver", IafBlockRegistry.SILVER_BLOCK);
        OreDictionary.registerOre("gemSapphire", IafItemRegistry.SAPPHIRE_GEM);
        OreDictionary.registerOre("oreSapphire", IafBlockRegistry.SAPPHIRE_ORE);
        OreDictionary.registerOre("blockSapphire", IafBlockRegistry.SAPPHIRE_BLOCK);
        OreDictionary.registerOre("boneWither", IafItemRegistry.WITHERBONE);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_RED);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_BRONZE);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_GRAY);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_GREEN);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_BLUE);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_WHITE);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_SAPPHIRE);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.DRAGON_SCALE_SILVER);
        OreDictionary.registerOre("woolBlock", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodMeat", Items.CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.COOKED_CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.BEEF);
        OreDictionary.registerOre("foodMeat", Items.COOKED_BEEF);
        OreDictionary.registerOre("foodMeat", Items.PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.COOKED_PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.MUTTON);
        OreDictionary.registerOre("foodMeat", Items.COOKED_MUTTON);
        OreDictionary.registerOre("foodMeat", Items.RABBIT);
        OreDictionary.registerOre("foodMeat", Items.COOKED_RABBIT);
        OreDictionary.registerOre("boneWithered", IafItemRegistry.WITHERBONE);
        OreDictionary.registerOre("boneDragon", IafItemRegistry.DRAGON_BONE);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.DEATHWORM_EGG, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", IafItemRegistry.DRAGONBONE_AXE);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.MYRMEX_DESERT_AXE);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.MYRMEX_JUNGLE_AXE);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.SILVER_AXE);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.DRAGONSTEEL_FIRE_AXE);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.DRAGONSTEEL_ICE_AXE);

        OreDictionary.registerOre("dragonSkull",  new ItemStack(IafItemRegistry.DRAGON_SKULL, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("mythicalSkull",  new ItemStack(IafItemRegistry.DRAGON_SKULL, 1, OreDictionary.WILDCARD_VALUE));
        for(EnumSkullType skullType : EnumSkullType.values()){
            OreDictionary.registerOre("mythicalSkull", skullType.skull_item);
        }

         */

        IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Items.STRING)));
        IafItemRegistry.SILVER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.SILVER_INGOT)));
        IafItemRegistry.SILVER_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.SILVER_INGOT)));
        IafItemRegistry.DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGON_BONE)));
        IafItemRegistry.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGON_BONE)));
        IafItemRegistry.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGON_BONE)));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairMaterial(Ingredient.fromStacks(new ItemStack(EnumDragonArmor.getScaleItem(armor))));
        }
        IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT)));
        IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT)));
        IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT)));
        IafItemRegistry.SHEEP_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Blocks.WHITE_WOOL)));
        IafItemRegistry.EARPLUGS_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Blocks.OAK_BUTTON)));
        IafItemRegistry.DEATHWORM_0_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_YELLOW)));
        IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_RED)));
        IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_WHITE)));
        IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Blocks.STONE)));
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(EnumTroll.MOUNTAIN.leather)));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(EnumTroll.FOREST.leather)));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(EnumTroll.FROST.leather)));
        IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.HIPPOGRYPH_TALON)));
        IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.SHINY_SCALES)));
        IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER)));
        IafItemRegistry.DRAGONSTEEL_FIRE_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT)));
        IafItemRegistry.DRAGONSTEEL_ICE_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT)));
        IafItemRegistry.DRAGONSTEEL_LIGHTNING_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT)));
        IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER)));
        IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN)));
        IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN)));
        IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_CHITIN)));
        IafItemRegistry.DREAD_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DREAD_SHARD)));
        IafItemRegistry.DREAD_KNIGHT_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(IafItemRegistry.DREAD_SHARD)));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairMaterial(Ingredient.fromStacks(new ItemStack(serpent.scale)));
        }
        /*
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        CompoundNBT tag = new CompoundNBT();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(IafItemRegistry.SHINY_SCALES), waterBreathingPotion);
        */
    }

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        return BannerPattern.create(name.toUpperCase(), name, "iceandfire." + name, true);
    }

    public static DragonForgeRecipe getFireForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (recipe.getInput().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getIceForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (recipe.getInput().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getLightningForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : LIGHTNING_FORGE_RECIPES) {
            if (recipe.getInput().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }


    public static DragonForgeRecipe getFireForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (recipe.getBlood().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }


    public static DragonForgeRecipe getIceForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (recipe.getBlood().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getLightningForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : LIGHTNING_FORGE_RECIPES) {
            if (recipe.getBlood().getItem() == stack.getItem()) {
                return recipe;
            }
        }
        return null;
    }
}
