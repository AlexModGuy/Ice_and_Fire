package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;

/*
    TODO - Myrmex trades
 */
public class IafVillagerRegistry {

    public static final IafVillagerRegistry INSTANCE = new IafVillagerRegistry();

   /* public VillagerRegistry.VillagerProfession desertMyrmexWorker;
    public VillagerRegistry.VillagerProfession jungleMyrmexWorker;
    public VillagerRegistry.VillagerProfession desertMyrmexSoldier;
    public VillagerRegistry.VillagerProfession jungleMyrmexSoldier;
    public VillagerRegistry.VillagerProfession desertMyrmexSentinel;
    public VillagerRegistry.VillagerProfession jungleMyrmexSentinel;
    public VillagerRegistry.VillagerProfession desertMyrmexRoyal;
    public VillagerRegistry.VillagerProfession jungleMyrmexRoyal;
    public VillagerRegistry.VillagerProfession desertMyrmexQueen;
    public VillagerRegistry.VillagerProfession jungleMyrmexQueen;

    */

    public void init() {
        /*
        desertMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexWorker, "desert_myrmex_worker");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.SAND), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DEADBUSH), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.IRON_ORE), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
        }

        jungleMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexWorker, "jungle_myrmex_worker");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.MELON), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.LEAVES, 1, 3), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(48, 64), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.GOLD_ORE), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(7, 10), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
        }

        desertMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSoldier, "desert_myrmex_soldier");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(1, 15)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.FEATHER), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.STRING), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.GUNPOWDER), new EntityVillager.PriceInfo(5, 7), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.RABBIT), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.DYE, 1, 2), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.IRON_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.CHICKEN), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.GOLD_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.SILVER_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.TROLL_TUSK), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 15)));
        }

        jungleMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSoldier, "jungle_myrmex_soldier");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(1, 15)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.FEATHER), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.STRING), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.GUNPOWDER), new EntityVillager.PriceInfo(5, 7), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.RABBIT), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.DYE, 1, 3), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.IRON_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.CHICKEN), new EntityVillager.PriceInfo(3, 6), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.GOLD_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.SILVER_NUGGET), new EntityVillager.PriceInfo(1, 3), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.TROLL_TUSK), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 15)));
        }

        desertMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSentinel, "desert_myrmex_sentinel");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SPIDER_EYE), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.REDSTONE), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(3, 4), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.EGG), new EntityVillager.PriceInfo(3, 5), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.PORKCHOP), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.BEEF), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.MUTTON), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.POISONOUS_POTATO), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(10, 20)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.FISH, 1, 3), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 10)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.SKULL), new EntityVillager.PriceInfo(20, 50), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSentinel, "jungle_myrmex_sentinel");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SPIDER_EYE), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.REDSTONE), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(3, 4), new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.EGG), new EntityVillager.PriceInfo(3, 5), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.PORKCHOP), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.BEEF), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.MUTTON), new EntityVillager.PriceInfo(3, 7), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.POISONOUS_POTATO), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(10, 20)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.FISH, 1, 3), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(5, 10)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.SKULL), new EntityVillager.PriceInfo(20, 50), new EntityVillager.PriceInfo(1, 1)));
        }

        desertMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexRoyal, "desert_myrmex_royal");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MANUSCRIPT), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(4, 6)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.GOLD_INGOT), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.SILVER_INGOT), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.RABBIT_FOOT), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.ENDER_PEARL), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.WITHER_SHARD), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.MAGMA_CREAM), new EntityVillager.PriceInfo(6, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.QUARTZ), new EntityVillager.PriceInfo(7, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(6, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.GOLDEN_CARROT), new EntityVillager.PriceInfo(15, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(7, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(Items.EMERALD), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexRoyal, "jungle_myrmex_royal");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MANUSCRIPT), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(4, 6)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.GOLD_INGOT), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.SILVER_INGOT), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new EntityVillager.PriceInfo(1, 4), new EntityVillager.PriceInfo(5, 8)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.RABBIT_FOOT), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.ENDER_PEARL), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.WITHER_SHARD), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.MAGMA_CREAM), new EntityVillager.PriceInfo(6, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.QUARTZ), new EntityVillager.PriceInfo(7, 15), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(6, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.GOLDEN_CARROT), new EntityVillager.PriceInfo(15, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(7, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(Items.EMERALD), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
        }

        desertMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexQueen, "desert_myrmex_queen");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, 0), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, 1), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, 2), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, 3), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), new ItemStack(IafItemRegistry.MYRMEX_DESERT_EGG, 1, 4), new EntityVillager.PriceInfo(50, 64), new EntityVillager.PriceInfo(1, 1)));
        }

        jungleMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexQueen, "jungle_myrmex_queen");
            career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, 0), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, 1), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, 2), new EntityVillager.PriceInfo(20, 30), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, 3), new EntityVillager.PriceInfo(30, 40), new EntityVillager.PriceInfo(1, 1)));
            career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_EGG, 1, 4), new EntityVillager.PriceInfo(50, 64), new EntityVillager.PriceInfo(1, 1)));
        }
        (*/
    }
}
