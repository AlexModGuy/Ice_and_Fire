package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ModRecipes {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.fire_dragon_blood), new ItemStack(ModItems.dragonsteel_fire_ingot)));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.ice_dragon_blood), new ItemStack(ModItems.dragonsteel_ice_ingot)));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.stymphalian_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.amphithere_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.sea_serpent_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.dragonbone_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.hippogryph_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHippogryphEgg entityarrow = new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.rotten_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityCockatriceEgg entityarrow = new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.deathworm_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
                return entityarrow;
            }
        });
        OreDictionary.registerOre("desertMyrmexEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("jungleMyrmexEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("charredBlock", ModBlocks.charedDirt);
        OreDictionary.registerOre("charredBlock", ModBlocks.charedGrass);
        OreDictionary.registerOre("charredBlock", ModBlocks.charedGrassPath);
        OreDictionary.registerOre("charredBlock", ModBlocks.charedGravel);
        OreDictionary.registerOre("charredBlock", ModBlocks.charedCobblestone);
        OreDictionary.registerOre("charredBlock", ModBlocks.charedStone);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenDirt);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenGrass);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenGrassPath);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenGravel);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenCobblestone);
        OreDictionary.registerOre("frozenBlock", ModBlocks.frozenStone);
        OreDictionary.registerOre("ingotFireDragonsteel", ModItems.dragonsteel_fire_ingot);
        OreDictionary.registerOre("blockFireDragonsteel", ModBlocks.dragonsteel_fire_block);
        OreDictionary.registerOre("ingotIceDragonsteel", ModItems.dragonsteel_ice_ingot);
        OreDictionary.registerOre("blockIceDragonsteel", ModBlocks.dragonsteel_ice_block);
        OreDictionary.registerOre("ingotSilver", ModItems.silverIngot);
        OreDictionary.registerOre("nuggetSilver", ModItems.silverNugget);
        OreDictionary.registerOre("oreSilver", ModBlocks.silverOre);
        OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
        OreDictionary.registerOre("gemSapphire", ModItems.sapphireGem);
        OreDictionary.registerOre("oreSapphire", ModBlocks.sapphireOre);
        OreDictionary.registerOre("blockSapphire", ModBlocks.sapphireBlock);
        OreDictionary.registerOre("boneWither", ModItems.witherbone);
        OreDictionary.registerOre("fireDragonScaleBlock", ModBlocks.dragonscale_red);
        OreDictionary.registerOre("fireDragonScaleBlock", ModBlocks.dragonscale_bronze);
        OreDictionary.registerOre("fireDragonScaleBlock", ModBlocks.dragonscale_gray);
        OreDictionary.registerOre("fireDragonScaleBlock", ModBlocks.dragonscale_green);
        OreDictionary.registerOre("iceDragonScaleBlock", ModBlocks.dragonscale_blue);
        OreDictionary.registerOre("iceDragonScaleBlock", ModBlocks.dragonscale_white);
        OreDictionary.registerOre("iceDragonScaleBlock", ModBlocks.dragonscale_sapphire);
        OreDictionary.registerOre("iceDragonScaleBlock", ModBlocks.dragonscale_silver);
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
        OreDictionary.registerOre("boneWithered", ModItems.witherbone);
        OreDictionary.registerOre("boneDragon", ModItems.dragonbone);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("listAllEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(ModItems.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", ModItems.dragonbone_axe);
        OreDictionary.registerOre("toolAxe", ModItems.myrmex_desert_axe);
        OreDictionary.registerOre("toolAxe", ModItems.myrmex_jungle_axe);
        OreDictionary.registerOre("toolAxe", ModItems.silver_axe);
        OreDictionary.registerOre("toolAxe", ModItems.dragonsteel_fire_axe);
        OreDictionary.registerOre("toolAxe", ModItems.dragonsteel_ice_axe);

        addBanner("fire", new ItemStack(ModItems.fire_dragon_heart));
        addBanner("ice", new ItemStack(ModItems.ice_dragon_heart));
        addBanner("fire_head", new ItemStack(ModItems.dragon_skull, 1, 0));
        addBanner("ice_head", new ItemStack(ModItems.dragon_skull, 1, 1));
        addBanner("amphithere", new ItemStack(ModItems.amphithere_feather));
        addBanner("bird", new ItemStack(ModItems.stymphalian_bird_feather));
        addBanner("eye", new ItemStack(ModItems.cyclops_eye));
        addBanner("fae", new ItemStack(ModItems.pixie_wings));
        addBanner("feather", new ItemStack(Items.FEATHER));
        addBanner("gorgon", new ItemStack(ModItems.gorgon_head));
        addBanner("hippocampus", new ItemStack(ModItems.hippocampus_fin));
        addBanner("hippogryph_head", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
        addBanner("mermaid", new ItemStack(ModItems.siren_tear));
        addBanner("sea_serpent", new ItemStack(ModItems.sea_serpent_fang));
        addBanner("troll", new ItemStack(ModItems.troll_tusk));

        GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silverIngot), 1);
        GameRegistry.addSmelting(ModBlocks.sapphireOre, new ItemStack(ModItems.sapphireGem), 1);
        GameRegistry.addSmelting(ModBlocks.myrmex_desert_resin_block, new ItemStack(ModBlocks.myrmex_desert_resin_glass), 1);
        GameRegistry.addSmelting(ModBlocks.myrmex_jungle_resin_block, new ItemStack(ModBlocks.myrmex_jungle_resin_glass), 1);
        GameRegistry.addSmelting(ModBlocks.frozenDirt, new ItemStack(Blocks.DIRT), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenGrass, new ItemStack(Blocks.GRASS), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenGrassPath, new ItemStack(Blocks.GRASS_PATH), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenCobblestone, new ItemStack(Blocks.COBBLESTONE), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenStone, new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenGravel, new ItemStack(Blocks.GRAVEL), 0.1F);
        GameRegistry.addSmelting(ModBlocks.frozenSplinters, new ItemStack(Items.STICK, 3), 0.1F);
        ModItems.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        ModItems.silverMetal.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.boneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        ModItems.dragonsteel_fire_armor.setRepairItem(new ItemStack(ModItems.dragonsteel_fire_ingot));
        ModItems.dragonsteel_ice_armor.setRepairItem(new ItemStack(ModItems.dragonsteel_ice_ingot));
        ModItems.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        ModItems.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        ModItems.yellow_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 0));
        ModItems.white_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 1));
        ModItems.red_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 2));
        ModItems.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        ModItems.troll_mountain.setRepairItem(new ItemStack(EnumTroll.MOUNTAIN.leather));
        ModItems.troll_forest.setRepairItem(new ItemStack(EnumTroll.FOREST.leather));
        ModItems.troll_frost.setRepairItem(new ItemStack(EnumTroll.FROST.leather));
        ModItems.hippogryph_sword_tools.setRepairItem(new ItemStack(ModItems.hippogryph_talon));
        ModItems.hippocampus_sword_tools.setRepairItem(new ItemStack(ModItems.shiny_scales));
        ModItems.amphithere_sword_tools.setRepairItem(new ItemStack(ModItems.amphithere_feather));
        ModItems.dragonsteel_fire_tools.setRepairItem(new ItemStack(ModItems.dragonsteel_fire_ingot));
        ModItems.dragonsteel_ice_tools.setRepairItem(new ItemStack(ModItems.dragonsteel_ice_ingot));
        ModItems.stymphalian_sword_tools.setRepairItem(new ItemStack(ModItems.stymphalian_bird_feather));
        ModItems.myrmexChitin.setRepairItem(new ItemStack(ModItems.myrmex_desert_chitin));
        ModItems.myrmexDesert.setRepairItem(new ItemStack(ModItems.myrmex_desert_chitin));
        ModItems.myrmexJungle.setRepairItem(new ItemStack(ModItems.myrmex_jungle_chitin));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(ModItems.shiny_scales), waterBreathingPotion);

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
                    GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, bronzeIngot.copy(), 1);
                    break;
                }
            }
        } else if (!copperItems.isEmpty()) {
            for (ItemStack copperIngot : copperItems) {
                if (copperIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, copperIngot.copy(), 1);
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
