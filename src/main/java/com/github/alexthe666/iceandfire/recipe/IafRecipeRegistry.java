package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class IafRecipeRegistry {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD), new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT)));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD), new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT)));
        DispenserBlock.registerDispenseBehavior(IafItemRegistry.STYMPHALIAN_ARROW, new ProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.AMPHITHERE_ARROW, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.SEA_SERPENT_ARROW, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.DRAGONBONE_ARROW, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.HYDRA_ARROW, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.HIPPOGRYPH_EGG, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHippogryphEgg entityarrow = new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.ROTTEN_EGG, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityCockatriceEgg entityarrow = new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.DEATHWORM_EGG, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
                return entityarrow;
            }
        });
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

        addBanner("fire", new ItemStack(IafItemRegistry.FIRE_DRAGON_HEART));
        addBanner("ice", new ItemStack(IafItemRegistry.ICE_DRAGON_HEART));
        addBanner("fire_head", new ItemStack(IafItemRegistry.DRAGON_SKULL, 1, 0));
        addBanner("ice_head", new ItemStack(IafItemRegistry.DRAGON_SKULL, 1, 1));
        addBanner("amphithere", new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER));
        addBanner("bird", new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER));
        addBanner("eye", new ItemStack(IafItemRegistry.CYCLOPS_EYE));
        addBanner("fae", new ItemStack(IafItemRegistry.PIXIE_WINGS));
        addBanner("feather", new ItemStack(Items.FEATHER));
        addBanner("gorgon", new ItemStack(IafItemRegistry.GORGON_HEAD));
        addBanner("hippocampus", new ItemStack(IafItemRegistry.HIPPOCAMPUS_FIN));
        addBanner("hippogryph_head", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
        addBanner("mermaid", new ItemStack(IafItemRegistry.SIREN_TEAR));
        addBanner("sea_serpent", new ItemStack(IafItemRegistry.SERPENT_FANG));
        addBanner("troll", new ItemStack(IafItemRegistry.TROLL_TUSK));
        addBanner("weezer", new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM));
        addBanner("dread", new ItemStack(IafItemRegistry.DREAD_SHARD));
        GameRegistry.addSmelting(IafBlockRegistry.SILVER_ORE, new ItemStack(IafItemRegistry.SILVER_INGOT), 1);
        GameRegistry.addSmelting(IafBlockRegistry.SAPPHIRE_ORE, new ItemStack(IafItemRegistry.SAPPHIRE_GEM), 1);
        GameRegistry.addSmelting(IafBlockRegistry.MYRMEX_DESERT_RESIN_BLOCK, new ItemStack(IafBlockRegistry.MYRMEX_DESERT_RESIN_GLASS), 1);
        GameRegistry.addSmelting(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_BLOCK, new ItemStack(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_GLASS), 1);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_DIRT, new ItemStack(Blocks.DIRT), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_GRASS, new ItemStack(Blocks.GRASS), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_GRASS_PATH, new ItemStack(Blocks.GRASS_PATH), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_COBBLESTONE, new ItemStack(Blocks.COBBLESTONE), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_STONE, new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_GRAVEL, new ItemStack(Blocks.GRAVEL), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.FROZEN_SPLINTERS, new ItemStack(Items.STICK, 3), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.DREAD_STONE_BRICKS, new ItemStack(IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED), 0.1F);
        IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL.setRepairItem(new ItemStack(Items.STRING));
        IafItemRegistry.SILVER_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.SILVER_INGOT));
        IafItemRegistry.SILVER_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.SILVER_INGOT));
        IafItemRegistry.DRAGONBONE_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGON_BONE));
        IafItemRegistry.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGON_BONE));
        IafItemRegistry.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGON_BONE));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT));
        IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT));
        IafItemRegistry.SHEEP_ARMOR_MATERIAL.setRepairItem(new ItemStack(Blocks.WOOL));
        IafItemRegistry.EARPLUGS_ARMOR_MATERIAL.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        IafItemRegistry.DEATHWORM_0_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN, 1, 0));
        IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN, 1, 1));
        IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN, 1, 2));
        IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL.setRepairItem(new ItemStack(Blocks.STONE));
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairItem(new ItemStack(EnumTroll.MOUNTAIN.leather));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairItem(new ItemStack(EnumTroll.FOREST.leather));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairItem(new ItemStack(EnumTroll.FROST.leather));
        IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.HIPPOGRYPH_TALON));
        IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.SHINY_SCALES));
        IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER));
        IafItemRegistry.DRAGONSTEEL_FIRE_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT));
        IafItemRegistry.DRAGONSTEEL_ICE_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT));
        IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER));
        IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN));
        IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN));
        IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_CHITIN));
        IafItemRegistry.DREAD_SWORD_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DREAD_SHARD));
        IafItemRegistry.DREAD_KNIGHT_TOOL_MATERIAL.setRepairItem(new ItemStack(IafItemRegistry.DREAD_SHARD));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        CompoundNBT tag = new CompoundNBT();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(IafItemRegistry.SHINY_SCALES), waterBreathingPotion);

    }

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        Class<?>[] classes = {String.class, String.class, ItemStack.class};
        Object[] names = {name, "iceandfire." + name, craftingStack};
        BANNER_ITEMS.add(craftingStack);
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
    }

    public static void postInit() {
        NonNullList<ItemStack> bronzeItems = OreDictionary.getOres("nuggetBronze");
        NonNullList<ItemStack> copperItems = OreDictionary.getOres("nuggetCopper");
        if (!bronzeItems.isEmpty()) {
            for (ItemStack bronzeIngot : bronzeItems) {
                if (bronzeIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER, bronzeIngot.copy(), 1);
                    break;
                }
            }
        } else if (!copperItems.isEmpty()) {
            for (ItemStack copperIngot : copperItems) {
                if (copperIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER, copperIngot.copy(), 1);
                    break;
                }
            }
        }
    }

    public static DragonForgeRecipe getFireForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getInput(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getIceForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getInput(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getFireForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getBlood(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getIceForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getBlood(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }
}
