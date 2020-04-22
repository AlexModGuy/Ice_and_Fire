package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.block.IaFBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IaFItemRegistry;
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

public class IaFRecipeRegistry {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IaFItemRegistry.fire_dragon_blood), new ItemStack(IaFItemRegistry.dragonsteel_fire_ingot)));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IaFItemRegistry.ice_dragon_blood), new ItemStack(IaFItemRegistry.dragonsteel_ice_ingot)));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.stymphalian_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.amphithere_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.sea_serpent_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.dragonbone_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.hippogryph_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHippogryphEgg entityarrow = new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.rotten_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityCockatriceEgg entityarrow = new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IaFItemRegistry.deathworm_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
                return entityarrow;
            }
        });
        OreDictionary.registerOre("desertMyrmexEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("jungleMyrmexEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedDirt);
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedGrass);
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedGrassPath);
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedGravel);
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedCobblestone);
        OreDictionary.registerOre("charredBlock", IaFBlockRegistry.charedStone);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenDirt);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenGrass);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenGrassPath);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenGravel);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenCobblestone);
        OreDictionary.registerOre("frozenBlock", IaFBlockRegistry.frozenStone);
        OreDictionary.registerOre("ingotFireDragonsteel", IaFItemRegistry.dragonsteel_fire_ingot);
        OreDictionary.registerOre("blockFireDragonsteel", IaFBlockRegistry.dragonsteel_fire_block);
        OreDictionary.registerOre("ingotIceDragonsteel", IaFItemRegistry.dragonsteel_ice_ingot);
        OreDictionary.registerOre("blockIceDragonsteel", IaFBlockRegistry.dragonsteel_ice_block);
        OreDictionary.registerOre("ingotSilver", IaFItemRegistry.silverIngot);
        OreDictionary.registerOre("nuggetSilver", IaFItemRegistry.silverNugget);
        OreDictionary.registerOre("oreSilver", IaFBlockRegistry.silverOre);
        OreDictionary.registerOre("blockSilver", IaFBlockRegistry.silverBlock);
        OreDictionary.registerOre("gemSapphire", IaFItemRegistry.sapphireGem);
        OreDictionary.registerOre("oreSapphire", IaFBlockRegistry.sapphireOre);
        OreDictionary.registerOre("blockSapphire", IaFBlockRegistry.sapphireBlock);
        OreDictionary.registerOre("boneWither", IaFItemRegistry.witherbone);
        OreDictionary.registerOre("fireDragonScaleBlock", IaFBlockRegistry.dragonscale_red);
        OreDictionary.registerOre("fireDragonScaleBlock", IaFBlockRegistry.dragonscale_bronze);
        OreDictionary.registerOre("fireDragonScaleBlock", IaFBlockRegistry.dragonscale_gray);
        OreDictionary.registerOre("fireDragonScaleBlock", IaFBlockRegistry.dragonscale_green);
        OreDictionary.registerOre("iceDragonScaleBlock", IaFBlockRegistry.dragonscale_blue);
        OreDictionary.registerOre("iceDragonScaleBlock", IaFBlockRegistry.dragonscale_white);
        OreDictionary.registerOre("iceDragonScaleBlock", IaFBlockRegistry.dragonscale_sapphire);
        OreDictionary.registerOre("iceDragonScaleBlock", IaFBlockRegistry.dragonscale_silver);
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
        OreDictionary.registerOre("boneWithered", IaFItemRegistry.witherbone);
        OreDictionary.registerOre("boneDragon", IaFItemRegistry.dragonbone);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IaFItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IaFItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("listAllEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.dragonbone_axe);
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.myrmex_desert_axe);
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.myrmex_jungle_axe);
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.silver_axe);
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.dragonsteel_fire_axe);
        OreDictionary.registerOre("toolAxe", IaFItemRegistry.dragonsteel_ice_axe);

        OreDictionary.registerOre("dragonSkull",  new ItemStack(IaFItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("mythicalSkull",  new ItemStack(IaFItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        for(EnumSkullType skullType : EnumSkullType.values()){
            OreDictionary.registerOre("mythicalSkull", skullType.skull_item);
        }

        addBanner("fire", new ItemStack(IaFItemRegistry.fire_dragon_heart));
        addBanner("ice", new ItemStack(IaFItemRegistry.ice_dragon_heart));
        addBanner("fire_head", new ItemStack(IaFItemRegistry.dragon_skull, 1, 0));
        addBanner("ice_head", new ItemStack(IaFItemRegistry.dragon_skull, 1, 1));
        addBanner("amphithere", new ItemStack(IaFItemRegistry.amphithere_feather));
        addBanner("bird", new ItemStack(IaFItemRegistry.stymphalian_bird_feather));
        addBanner("eye", new ItemStack(IaFItemRegistry.cyclops_eye));
        addBanner("fae", new ItemStack(IaFItemRegistry.pixie_wings));
        addBanner("feather", new ItemStack(Items.FEATHER));
        addBanner("gorgon", new ItemStack(IaFItemRegistry.gorgon_head));
        addBanner("hippocampus", new ItemStack(IaFItemRegistry.hippocampus_fin));
        addBanner("hippogryph_head", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
        addBanner("mermaid", new ItemStack(IaFItemRegistry.siren_tear));
        addBanner("sea_serpent", new ItemStack(IaFItemRegistry.sea_serpent_fang));
        addBanner("troll", new ItemStack(IaFItemRegistry.troll_tusk));
        addBanner("weezer", new ItemStack(IaFItemRegistry.weezer_blue_album));
        addBanner("dread", new ItemStack(IaFItemRegistry.dread_shard));
        GameRegistry.addSmelting(IaFBlockRegistry.silverOre, new ItemStack(IaFItemRegistry.silverIngot), 1);
        GameRegistry.addSmelting(IaFBlockRegistry.sapphireOre, new ItemStack(IaFItemRegistry.sapphireGem), 1);
        GameRegistry.addSmelting(IaFBlockRegistry.myrmex_desert_resin_block, new ItemStack(IaFBlockRegistry.myrmex_desert_resin_glass), 1);
        GameRegistry.addSmelting(IaFBlockRegistry.myrmex_jungle_resin_block, new ItemStack(IaFBlockRegistry.myrmex_jungle_resin_glass), 1);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenDirt, new ItemStack(Blocks.DIRT), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenGrass, new ItemStack(Blocks.GRASS), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenGrassPath, new ItemStack(Blocks.GRASS_PATH), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenCobblestone, new ItemStack(Blocks.COBBLESTONE), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenStone, new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenGravel, new ItemStack(Blocks.GRAVEL), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.frozenSplinters, new ItemStack(Items.STICK, 3), 0.1F);
        GameRegistry.addSmelting(IaFBlockRegistry.dread_stone_bricks, new ItemStack(IaFBlockRegistry.dread_stone_bricks_cracked), 0.1F);
        IaFItemRegistry.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        IaFItemRegistry.silverMetal.setRepairItem(new ItemStack(IaFItemRegistry.silverIngot));
        IaFItemRegistry.silverTools.setRepairItem(new ItemStack(IaFItemRegistry.silverIngot));
        IaFItemRegistry.boneTools.setRepairItem(new ItemStack(IaFItemRegistry.dragonbone));
        IaFItemRegistry.fireBoneTools.setRepairItem(new ItemStack(IaFItemRegistry.dragonbone));
        IaFItemRegistry.iceBoneTools.setRepairItem(new ItemStack(IaFItemRegistry.dragonbone));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        IaFItemRegistry.dragonsteel_fire_armor.setRepairItem(new ItemStack(IaFItemRegistry.dragonsteel_fire_ingot));
        IaFItemRegistry.dragonsteel_ice_armor.setRepairItem(new ItemStack(IaFItemRegistry.dragonsteel_ice_ingot));
        IaFItemRegistry.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        IaFItemRegistry.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        IaFItemRegistry.yellow_deathworm.setRepairItem(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 0));
        IaFItemRegistry.white_deathworm.setRepairItem(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 1));
        IaFItemRegistry.red_deathworm.setRepairItem(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 2));
        IaFItemRegistry.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        IaFItemRegistry.troll_mountain.setRepairItem(new ItemStack(EnumTroll.MOUNTAIN.leather));
        IaFItemRegistry.troll_forest.setRepairItem(new ItemStack(EnumTroll.FOREST.leather));
        IaFItemRegistry.troll_frost.setRepairItem(new ItemStack(EnumTroll.FROST.leather));
        IaFItemRegistry.hippogryph_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.hippogryph_talon));
        IaFItemRegistry.hippocampus_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.shiny_scales));
        IaFItemRegistry.amphithere_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.amphithere_feather));
        IaFItemRegistry.dragonsteel_fire_tools.setRepairItem(new ItemStack(IaFItemRegistry.dragonsteel_fire_ingot));
        IaFItemRegistry.dragonsteel_ice_tools.setRepairItem(new ItemStack(IaFItemRegistry.dragonsteel_ice_ingot));
        IaFItemRegistry.stymphalian_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.stymphalian_bird_feather));
        IaFItemRegistry.myrmexChitin.setRepairItem(new ItemStack(IaFItemRegistry.myrmex_desert_chitin));
        IaFItemRegistry.myrmexDesert.setRepairItem(new ItemStack(IaFItemRegistry.myrmex_desert_chitin));
        IaFItemRegistry.myrmexJungle.setRepairItem(new ItemStack(IaFItemRegistry.myrmex_jungle_chitin));
        IaFItemRegistry.dread_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.dread_shard));
        IaFItemRegistry.dread_knight_sword_tools.setRepairItem(new ItemStack(IaFItemRegistry.dread_shard));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(IaFItemRegistry.shiny_scales), waterBreathingPotion);

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
                    GameRegistry.addSmelting(IaFItemRegistry.stymphalian_bird_feather, bronzeIngot.copy(), 1);
                    break;
                }
            }
        } else if (!copperItems.isEmpty()) {
            for (ItemStack copperIngot : copperItems) {
                if (copperIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(IaFItemRegistry.stymphalian_bird_feather, copperIngot.copy(), 1);
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
