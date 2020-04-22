package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Map;
import java.util.Random;

public class IafVillagerRegistry {

    public static final IafVillagerRegistry INSTANCE = new IafVillagerRegistry();

    public VillagerRegistry.VillagerProfession fisherman;
    public VillagerRegistry.VillagerProfession craftsman;
    public VillagerRegistry.VillagerProfession shaman;
    public VillagerRegistry.VillagerProfession desertMyrmexWorker;
    public VillagerRegistry.VillagerProfession jungleMyrmexWorker;
    public VillagerRegistry.VillagerProfession desertMyrmexSoldier;
    public VillagerRegistry.VillagerProfession jungleMyrmexSoldier;
    public VillagerRegistry.VillagerProfession desertMyrmexSentinel;
    public VillagerRegistry.VillagerProfession jungleMyrmexSentinel;
    public VillagerRegistry.VillagerProfession desertMyrmexRoyal;
    public VillagerRegistry.VillagerProfession jungleMyrmexRoyal;
    public VillagerRegistry.VillagerProfession desertMyrmexQueen;
    public VillagerRegistry.VillagerProfession jungleMyrmexQueen;
    public Map<Integer, VillagerRegistry.VillagerProfession> professions = Maps.newHashMap();

    public void init() {
        fisherman = new VillagerRegistry.VillagerProfession("iceandfire:fisherman", "iceandfire:textures/models/snowvillager/fisherman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(fisherman, "fisherman");
            career.addTrade(1, new SapphireForItems(Items.FISH, new EntityVillager.PriceInfo(1, 10)));
            career.addTrade(2, new ListItemForSapphires(Items.FISHING_ROD, new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new ListItemForSapphires(IafItemRegistry.fishing_spear, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(2, new ListItemForSapphires(Items.COOKED_FISH, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(2, new ListItemForSapphires(Items.COOKED_FISH, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(3, new ListItemForSapphires(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()), new EntityVillager.PriceInfo(2, 1)));
            career.addTrade(3, new ListItemForSapphires(new ItemStack(Blocks.TRIPWIRE_HOOK), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new ListItemForSapphires(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), new EntityVillager.PriceInfo(1, 4)));
            register(fisherman, 0);
        }
        craftsman = new VillagerRegistry.VillagerProfession("iceandfire:craftsman", "iceandfire:textures/models/snowvillager/craftsman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(craftsman, "craftsman");
            //over 30 words for ice
            career.addTrade(1, new SapphireForItems(Item.getItemFromBlock(Blocks.SNOW), new EntityVillager.PriceInfo(1, 32)));
            career.addTrade(2, new ListItemForSapphires(Item.getItemFromBlock(Blocks.PACKED_ICE), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new ListItemForSapphires(Item.getItemFromBlock(IafBlockRegistry.dragon_ice), new EntityVillager.PriceInfo(4, 1)));
            career.addTrade(1, new ListItemForSapphires(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new ListItemForSapphires(IafItemRegistry.silver_shovel, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(3, new ListItemForSapphires(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(1, 9)));
            career.addTrade(2, new ListItemForSapphires(Items.LEATHER, new EntityVillager.PriceInfo(10, 1)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_BOOTS, new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_HELMET, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(1, 6)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(1, 6)));
            career.addTrade(3, new ListItemForSapphires(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(1, 7)));
            career.addTrade(3, new ListItemForSapphires(EnumTroll.FROST.leather, new EntityVillager.PriceInfo(1, 5)));
            register(craftsman, 1);
        }
        shaman = new VillagerRegistry.VillagerProfession("iceandfire:shaman", "iceandfire:textures/models/snowvillager/shaman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(shaman, "shaman");
            career.addTrade(1, new SapphireForItems(Items.BLAZE_POWDER, new EntityVillager.PriceInfo(2, 3)));
            career.addTrade(1, new SapphireForItems(Items.GHAST_TEAR, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new SapphireForItems(Items.BREWING_STAND, new EntityVillager.PriceInfo(9, 1)));
            career.addTrade(1, new SapphireForItems(IafItemRegistry.dragonbone, new EntityVillager.PriceInfo(1, 8)));
            ItemStack stack = new ItemStack(IafItemRegistry.bestiary);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal(), EnumBestiaryPages.ICEDRAGON.ordinal(), EnumBestiaryPages.ICEDRAGONEGG.ordinal(), EnumBestiaryPages.MATERIALS.ordinal(), EnumBestiaryPages.VILLAGERS.ordinal()});
            career.addTrade(2, new ListItemForSapphires(stack, new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new ListItemForSapphires(IafItemRegistry.manuscript, new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(3, new ListItemForSapphires(IafItemRegistry.ice_dragon_flesh, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(3, new ListItemForSapphires(IafItemRegistry.ice_dragon_blood, new EntityVillager.PriceInfo(1, 12)));
            career.addTrade(3, new ListItemForSapphires(IafItemRegistry.dragon_flute, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(2, new ListItemForSapphires(Items.ENDER_EYE, new EntityVillager.PriceInfo(2, 5)));
            career.addTrade(2, new ListItemForSapphires(IafItemRegistry.witherbone, new EntityVillager.PriceInfo(2, 5)));
            career.addTrade(2, new ListItemForSapphires(IafItemRegistry.wither_shard, new EntityVillager.PriceInfo(3, 2)));
            register(shaman, 2);
        }

        desertMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexWorker, "desert_myrmex_worker");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.SAND), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DEADBUSH), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.IRON_ORE), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
        }

        jungleMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexWorker, "jungle_myrmex_worker");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.MELON), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.LEAVES, 1, 3), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(48, 64), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.GOLD_ORE), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(7, 10), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
        }

        desertMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSoldier, "desert_myrmex_soldier");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(1, 15)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.FEATHER), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.STRING), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.GUNPOWDER), new EntityVillager.PriceInfo(5, 7), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.RABBIT), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.DYE, 1, 2), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.IRON_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.CHICKEN), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.GOLD_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.silverNugget), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.troll_tusk), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 15)));
        }

        jungleMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSoldier, "jungle_myrmex_soldier");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(1, 15)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.FEATHER), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.STRING), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.GUNPOWDER), new EntityVillager.PriceInfo(5, 7), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.RABBIT), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.DYE, 1, 3), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.IRON_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.CHICKEN), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.GOLD_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.silverNugget), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.troll_tusk), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 15)));
        }

        desertMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSentinel, "desert_myrmex_sentinel");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SPIDER_EYE), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.REDSTONE), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(3, 4), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.EGG), new EntityVillager.PriceInfo(3, 5), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.PORKCHOP), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.BEEF), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.MUTTON), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.POISONOUS_POTATO), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(10, 20)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.FISH, 1, 3), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 10)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.SKULL), new EntityVillager.PriceInfo(20, 50), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSentinel, "jungle_myrmex_sentinel");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SPIDER_EYE), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.REDSTONE), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(3, 4), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.EGG), new EntityVillager.PriceInfo(3, 5), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.PORKCHOP), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.BEEF), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.MUTTON), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.POISONOUS_POTATO), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(10, 20)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.FISH, 1, 3), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 10)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.SKULL), new EntityVillager.PriceInfo(20, 50), new EntityVillager.PriceInfo(1, 1)));
        }

        desertMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexRoyal, "desert_myrmex_royal");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.manuscript), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(4, 6)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.GOLD_INGOT), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.silverIngot), new ItemStack(IafItemRegistry.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.RABBIT_FOOT), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.ENDER_PEARL), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.wither_shard), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.MAGMA_CREAM), new EntityVillager.PriceInfo(6, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.QUARTZ), new EntityVillager.PriceInfo(7, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(6, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.GOLDEN_CARROT), new EntityVillager.PriceInfo(15, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(7, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(Items.EMERALD), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexRoyal, "jungle_myrmex_royal");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.manuscript), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(4, 6)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.GOLD_INGOT), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.silverIngot), new ItemStack(IafItemRegistry.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.RABBIT_FOOT), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.ENDER_PEARL), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.wither_shard), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.MAGMA_CREAM), new EntityVillager.PriceInfo(6, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.QUARTZ), new EntityVillager.PriceInfo(7, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(6, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.GOLDEN_CARROT), new EntityVillager.PriceInfo(15, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(7, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(Items.EMERALD), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
        }

        desertMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexQueen, "desert_myrmex_queen");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 0), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 1), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 2), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 3), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_desert_resin), new ItemStack(IafItemRegistry.myrmex_desert_egg, 1, 4), new EntityVillager.PriceInfo(50, 64), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexQueen, "jungle_myrmex_queen");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, 0), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, 1), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, 2), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, 3), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.myrmex_jungle_resin), new ItemStack(IafItemRegistry.myrmex_jungle_egg, 1, 4), new EntityVillager.PriceInfo(50, 64), new EntityVillager.PriceInfo(1, 1)));
        }
    }

    public void setRandomProfession(EntityVillager entity, Random rand) {
        entity.setProfession(professions.get(rand.nextInt(professions.size())));
    }

    private void register(VillagerRegistry.VillagerProfession prof, int id) {
        professions.put(id, prof);
    }

    /**
     * Sell items for sapphires
     */
    public static class SapphireForItems implements EntityVillager.ITradeList {
        /**
         * The item that is being sold for emeralds
         */
        public Item buyingItem;
        public EntityVillager.PriceInfo price;

        public SapphireForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
            this.buyingItem = itemIn;
            this.price = priceIn;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int i = 1;
            if (this.price != null) {
                i = this.price.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), IafItemRegistry.sapphireGem));
        }
    }

    /**
     * Buy items for sapphires
     */
    public static class ListItemForSapphires implements EntityVillager.ITradeList {
        /**
         * The item that is being bought for emeralds
         */
        public ItemStack itemToBuy;
        public EntityVillager.PriceInfo priceInfo;

        public ListItemForSapphires(Item par1Item, EntityVillager.PriceInfo priceInfo) {
            this.itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }

        public ListItemForSapphires(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int i = 1;

            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack;
            ItemStack itemstack1;
            if (i < 0) {
                itemstack = new ItemStack(IafItemRegistry.sapphireGem);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            } else {
                itemstack = new ItemStack(IafItemRegistry.sapphireGem, i, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }
            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }


}
