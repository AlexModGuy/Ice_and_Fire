package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class ModAchievements {

    public static Achievement manuscript;
    public static Achievement bestiary;
    public static Achievement silver;
    public static Achievement silverTool;
    public static Achievement sapphire;
    public static Achievement dragonEncounter;
    public static Achievement dragonKillPlayer;
    public static Achievement dragonKill;
    public static Achievement dragonHarvest;
    public static Achievement dragonHatch;
    public static Achievement dragonRide;
    public static Achievement dragonBreed;
    public static Achievement boneTool;
    public static Achievement dragonArmor;
    public static Achievement fireSword;
    public static Achievement iceSword;

    public static void init(){
        manuscript = new Achievement("achievement.manuscript", "manuscript", 0, 0, new ItemStack(ModItems.manuscript), null).registerStat();
        bestiary = new Achievement("achievement.bestiary", "bestiary", 0, -3, new ItemStack(ModItems.bestiary), manuscript).registerStat();
        silver = new Achievement("achievement.silver", "silver", -2, 0, new ItemStack(ModBlocks.silverOre), null).registerStat();
        silverTool = new Achievement("achievement.silverTool", "silverTool", -2, -3, new ItemStack(ModItems.silver_pickaxe), silver).registerStat();
        sapphire = new Achievement("achievement.sapphire", "sapphire", 4, 0, new ItemStack(ModBlocks.sapphireOre), null).registerStat();
        dragonEncounter = new Achievement("achievement.dragonEncounter", "dragonEncounter", 0, -5, new ItemStack(ModItems.dragon_skull), null).registerStat();
        dragonKillPlayer = new Achievement("achievement.dragonKillPlayer", "dragonKillPlayer", 2, -5, new ItemStack(ModItems.dragonbone), dragonEncounter).registerStat();
        dragonKill = new Achievement("achievement.dragonKill", "dragonKill", -2, -5, new ItemStack(Items.BONE), dragonEncounter).setSpecial().registerStat();
        dragonHarvest = new Achievement("achievement.dragonHarvest", "dragonHarvest", 2, -2, new ItemStack(ModItems.fire_dragon_heart), dragonKillPlayer).setSpecial().registerStat();
        dragonHatch = new Achievement("achievement.dragonHatch", "dragonHatch", 4, -2, new ItemStack(ModItems.dragonegg_red), dragonHarvest).registerStat();
        dragonRide = new Achievement("achievement.dragonRide", "dragonRide", 7, -2, new ItemStack(Items.SADDLE), dragonHatch).registerStat();
        dragonBreed = new Achievement("achievement.dragonBreed", "dragonBreed", 4, -5, new ItemStack(ModItems.fire_stew), dragonHatch).registerStat();
        boneTool = new Achievement("achievement.boneTool", "boneTool", 2, 3, new ItemStack(ModItems.dragonbone_pickaxe), dragonHarvest).registerStat();
        dragonArmor = new Achievement("achievement.dragonArmor", "dragonArmor", 4, 2, new ItemStack(EnumDragonArmor.armor_red.helmet), dragonHarvest).registerStat();
        fireSword = new Achievement("achievement.fireSword", "fireSword", 2, 5, new ItemStack(ModItems.dragonbone_sword_fire), boneTool).registerStat();
        iceSword = new Achievement("achievement.iceSword", "iceSword", 6, 5, new ItemStack(ModItems.dragonbone_sword_ice), boneTool).registerStat();

        AchievementPage.registerAchievementPage(new AchievementPage("Ice And Fire", manuscript, bestiary, silver, silverTool, sapphire, dragonEncounter, dragonKillPlayer, dragonKill, dragonHarvest, dragonHatch, dragonRide, dragonBreed, boneTool, dragonArmor, fireSword, iceSword));
    }
}
