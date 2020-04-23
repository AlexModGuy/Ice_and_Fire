package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
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

public class IafRecipeRegistry {

    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public static void preInit() {
        FIRE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.fire_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_fire_ingot)));
        ICE_FORGE_RECIPES.add(new DragonForgeRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(IafItemRegistry.ice_dragon_blood), new ItemStack(IafItemRegistry.dragonsteel_ice_ingot)));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.stymphalian_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.amphithere_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.sea_serpent_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.dragonbone_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.hydra_arrow, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.hippogryph_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityHippogryphEgg entityarrow = new EntityHippogryphEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.rotten_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityCockatriceEgg entityarrow = new EntityCockatriceEgg(worldIn, position.getX(), position.getY(), position.getZ());
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(IafItemRegistry.deathworm_egg, new BehaviorProjectileDispense() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityDeathWormEgg entityarrow = new EntityDeathWormEgg(worldIn, position.getX(), position.getY(), position.getZ(), stackIn.getMetadata() == 1);
                return entityarrow;
            }
        });
        OreDictionary.registerOre("desertMyrmexEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("jungleMyrmexEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedDirt);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedGrass);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedGrassPath);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedGravel);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedCobblestone);
        OreDictionary.registerOre("charredBlock", IafBlockRegistry.charedStone);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenDirt);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenGrass);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenGrassPath);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenGravel);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenCobblestone);
        OreDictionary.registerOre("frozenBlock", IafBlockRegistry.frozenStone);
        OreDictionary.registerOre("ingotFireDragonsteel", IafItemRegistry.dragonsteel_fire_ingot);
        OreDictionary.registerOre("blockFireDragonsteel", IafBlockRegistry.dragonsteel_fire_block);
        OreDictionary.registerOre("ingotIceDragonsteel", IafItemRegistry.dragonsteel_ice_ingot);
        OreDictionary.registerOre("blockIceDragonsteel", IafBlockRegistry.dragonsteel_ice_block);
        OreDictionary.registerOre("ingotSilver", IafItemRegistry.silverIngot);
        OreDictionary.registerOre("nuggetSilver", IafItemRegistry.silverNugget);
        OreDictionary.registerOre("oreSilver", IafBlockRegistry.silverOre);
        OreDictionary.registerOre("blockSilver", IafBlockRegistry.silverBlock);
        OreDictionary.registerOre("gemSapphire", IafItemRegistry.sapphireGem);
        OreDictionary.registerOre("oreSapphire", IafBlockRegistry.sapphireOre);
        OreDictionary.registerOre("blockSapphire", IafBlockRegistry.sapphireBlock);
        OreDictionary.registerOre("boneWither", IafItemRegistry.witherbone);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.dragonscale_red);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.dragonscale_bronze);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.dragonscale_gray);
        OreDictionary.registerOre("fireDragonScaleBlock", IafBlockRegistry.dragonscale_green);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.dragonscale_blue);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.dragonscale_white);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.dragonscale_sapphire);
        OreDictionary.registerOre("iceDragonScaleBlock", IafBlockRegistry.dragonscale_silver);
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
        OreDictionary.registerOre("boneWithered", IafItemRegistry.witherbone);
        OreDictionary.registerOre("boneDragon", IafItemRegistry.dragonbone);
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            OreDictionary.registerOre("seaSerpentScales", serpent.scale);
        }
        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.hippogryph_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.deathworm_egg, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("listAllEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("objectEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("bakingEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("egg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("ingredientEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodSimpleEgg", new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolAxe", IafItemRegistry.dragonbone_axe);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.myrmex_desert_axe);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.myrmex_jungle_axe);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.silver_axe);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.dragonsteel_fire_axe);
        OreDictionary.registerOre("toolAxe", IafItemRegistry.dragonsteel_ice_axe);

        OreDictionary.registerOre("dragonSkull",  new ItemStack(IafItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("mythicalSkull",  new ItemStack(IafItemRegistry.dragon_skull, 1, OreDictionary.WILDCARD_VALUE));
        for(EnumSkullType skullType : EnumSkullType.values()){
            OreDictionary.registerOre("mythicalSkull", skullType.skull_item);
        }

        addBanner("fire", new ItemStack(IafItemRegistry.fire_dragon_heart));
        addBanner("ice", new ItemStack(IafItemRegistry.ice_dragon_heart));
        addBanner("fire_head", new ItemStack(IafItemRegistry.dragon_skull, 1, 0));
        addBanner("ice_head", new ItemStack(IafItemRegistry.dragon_skull, 1, 1));
        addBanner("amphithere", new ItemStack(IafItemRegistry.amphithere_feather));
        addBanner("bird", new ItemStack(IafItemRegistry.stymphalian_bird_feather));
        addBanner("eye", new ItemStack(IafItemRegistry.cyclops_eye));
        addBanner("fae", new ItemStack(IafItemRegistry.pixie_wings));
        addBanner("feather", new ItemStack(Items.FEATHER));
        addBanner("gorgon", new ItemStack(IafItemRegistry.gorgon_head));
        addBanner("hippocampus", new ItemStack(IafItemRegistry.hippocampus_fin));
        addBanner("hippogryph_head", new ItemStack(EnumSkullType.HIPPOGRYPH.skull_item));
        addBanner("mermaid", new ItemStack(IafItemRegistry.siren_tear));
        addBanner("sea_serpent", new ItemStack(IafItemRegistry.sea_serpent_fang));
        addBanner("troll", new ItemStack(IafItemRegistry.troll_tusk));
        addBanner("weezer", new ItemStack(IafItemRegistry.weezer_blue_album));
        addBanner("dread", new ItemStack(IafItemRegistry.dread_shard));
        GameRegistry.addSmelting(IafBlockRegistry.silverOre, new ItemStack(IafItemRegistry.silverIngot), 1);
        GameRegistry.addSmelting(IafBlockRegistry.sapphireOre, new ItemStack(IafItemRegistry.sapphireGem), 1);
        GameRegistry.addSmelting(IafBlockRegistry.myrmex_desert_resin_block, new ItemStack(IafBlockRegistry.myrmex_desert_resin_glass), 1);
        GameRegistry.addSmelting(IafBlockRegistry.myrmex_jungle_resin_block, new ItemStack(IafBlockRegistry.myrmex_jungle_resin_glass), 1);
        GameRegistry.addSmelting(IafBlockRegistry.frozenDirt, new ItemStack(Blocks.DIRT), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenGrass, new ItemStack(Blocks.GRASS), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenGrassPath, new ItemStack(Blocks.GRASS_PATH), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenCobblestone, new ItemStack(Blocks.COBBLESTONE), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenStone, new ItemStack(Blocks.STONE), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenGravel, new ItemStack(Blocks.GRAVEL), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.frozenSplinters, new ItemStack(Items.STICK, 3), 0.1F);
        GameRegistry.addSmelting(IafBlockRegistry.dread_stone_bricks, new ItemStack(IafBlockRegistry.dread_stone_bricks_cracked), 0.1F);
        IafItemRegistry.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        IafItemRegistry.silverMetal.setRepairItem(new ItemStack(IafItemRegistry.silverIngot));
        IafItemRegistry.silverTools.setRepairItem(new ItemStack(IafItemRegistry.silverIngot));
        IafItemRegistry.boneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        IafItemRegistry.fireBoneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        IafItemRegistry.iceBoneTools.setRepairItem(new ItemStack(IafItemRegistry.dragonbone));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        IafItemRegistry.dragonsteel_fire_armor.setRepairItem(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot));
        IafItemRegistry.dragonsteel_ice_armor.setRepairItem(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot));
        IafItemRegistry.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        IafItemRegistry.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        IafItemRegistry.yellow_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 0));
        IafItemRegistry.white_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 1));
        IafItemRegistry.red_deathworm.setRepairItem(new ItemStack(IafItemRegistry.deathworm_chitin, 1, 2));
        IafItemRegistry.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        IafItemRegistry.troll_mountain.setRepairItem(new ItemStack(EnumTroll.MOUNTAIN.leather));
        IafItemRegistry.troll_forest.setRepairItem(new ItemStack(EnumTroll.FOREST.leather));
        IafItemRegistry.troll_frost.setRepairItem(new ItemStack(EnumTroll.FROST.leather));
        IafItemRegistry.hippogryph_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.hippogryph_talon));
        IafItemRegistry.hippocampus_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.shiny_scales));
        IafItemRegistry.amphithere_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.amphithere_feather));
        IafItemRegistry.dragonsteel_fire_tools.setRepairItem(new ItemStack(IafItemRegistry.dragonsteel_fire_ingot));
        IafItemRegistry.dragonsteel_ice_tools.setRepairItem(new ItemStack(IafItemRegistry.dragonsteel_ice_ingot));
        IafItemRegistry.stymphalian_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.stymphalian_bird_feather));
        IafItemRegistry.myrmexChitin.setRepairItem(new ItemStack(IafItemRegistry.myrmex_desert_chitin));
        IafItemRegistry.myrmexDesert.setRepairItem(new ItemStack(IafItemRegistry.myrmex_desert_chitin));
        IafItemRegistry.myrmexJungle.setRepairItem(new ItemStack(IafItemRegistry.myrmex_jungle_chitin));
        IafItemRegistry.dread_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.dread_shard));
        IafItemRegistry.dread_knight_sword_tools.setRepairItem(new ItemStack(IafItemRegistry.dread_shard));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairItem(new ItemStack(serpent.scale));
        }
        ItemStack waterBreathingPotion = new ItemStack(Items.POTIONITEM, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Potion", "water_breathing");
        waterBreathingPotion.setTagCompound(tag);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(IafItemRegistry.shiny_scales), waterBreathingPotion);

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
                    GameRegistry.addSmelting(IafItemRegistry.stymphalian_bird_feather, bronzeIngot.copy(), 1);
                    break;
                }
            }
        } else if (!copperItems.isEmpty()) {
            for (ItemStack copperIngot : copperItems) {
                if (copperIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(IafItemRegistry.stymphalian_bird_feather, copperIngot.copy(), 1);
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
