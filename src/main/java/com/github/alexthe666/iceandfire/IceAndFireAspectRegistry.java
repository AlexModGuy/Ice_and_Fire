package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class IceAndFireAspectRegistry {

    public static final Aspect MYTHICAL = new Aspect("mythus", 0XD9D5AB, new Aspect[]{Aspect.BEAST, Aspect.MAGIC}, new ResourceLocation("iceandfire:textures/thaumcraft/mythical.png"), 1);
    public static final Aspect DRAGON = new Aspect("draco", 0XA2271F, new Aspect[]{IceAndFireAspectRegistry.MYTHICAL, Aspect.DESIRE}, new ResourceLocation("iceandfire:textures/thaumcraft/dragon.png"), 1);

    public static void register(){
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.bestiary), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 2).add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.BEAST, 6).add(Aspect.MAGIC, 2).add(Aspect.MIND, 4).add(Aspect.WATER, 2).add(Aspect.AIR, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.manuscript), new AspectList().add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.PLANT, 3).add(Aspect.MIND, 2).add(Aspect.WATER, 2).add(Aspect.AIR, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.sapphireGem), new AspectList().add(Aspect.CRYSTAL, 15).add(Aspect.DESIRE, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fire_stew), new AspectList().add(Aspect.FIRE, 8).add(IceAndFireAspectRegistry.DRAGON, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.frost_stew), new AspectList().add(Aspect.COLD, 8).add(IceAndFireAspectRegistry.DRAGON, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_red), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_bronze), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_green), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_gray), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_blue), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_silver), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_white), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonegg_sapphire), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_red), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_bronze), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_green), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_gray), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_blue), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_silver), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_white), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonscales_sapphire), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone), new AspectList().add(Aspect.DEATH, 5).add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.witherbone), new AspectList().add(Aspect.DEATH, 6).add(Aspect.DARKNESS, 5).add(Aspect.MAGIC, 2).add(Aspect.ENTROPY, 5).add(Aspect.UNDEAD, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.wither_shard), new AspectList().add(Aspect.DEATH, 2).add(Aspect.DARKNESS, 1).add(Aspect.ENTROPY, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fishing_spear), new AspectList().add(Aspect.PLANT, 2).add(Aspect.WATER, 7).add(Aspect.BEAST, 2).add(Aspect.TOOL, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fishing_spear), new AspectList().add(Aspect.PLANT, 2).add(Aspect.WATER, 7).add(Aspect.BEAST, 2).add(Aspect.TOOL, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_sword), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_sword_fire), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5).add(Aspect.FIRE, 25).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_sword_ice), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5).add(Aspect.COLD, 25).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_pickaxe), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_axe), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_shovel), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_hoe), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_arrow), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 5).add(Aspect.DESIRE, 2).add(Aspect.TOOL, 2).add(Aspect.AIR, 5).add(Aspect.FLIGHT, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragonbone_bow), new AspectList().add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.PROTECT, 20).add(Aspect.AIR, 15).add(Aspect.FLIGHT, 10).add(Aspect.CRAFT, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_skull, 1, 0), new AspectList().add(Aspect.DEATH, 15).add(IceAndFireAspectRegistry.DRAGON, 20).add(Aspect.MAGIC, 2).add(Aspect.FIRE, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_skull, 1, 1), new AspectList().add(Aspect.DEATH, 15).add(IceAndFireAspectRegistry.DRAGON, 20).add(Aspect.MAGIC, 2).add(Aspect.COLD, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_iron, 1, 0), new AspectList().add(Aspect.METAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_iron, 1, 1), new AspectList().add(Aspect.METAL, 10).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_iron, 1, 2), new AspectList().add(Aspect.METAL, 30).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_iron, 1, 3), new AspectList().add(Aspect.METAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_gold, 1, 0), new AspectList().add(Aspect.METAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_gold, 1, 1), new AspectList().add(Aspect.METAL, 10).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_gold, 1, 2), new AspectList().add(Aspect.METAL, 30).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_gold, 1, 3), new AspectList().add(Aspect.METAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_diamond, 1, 0), new AspectList().add(Aspect.CRYSTAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_diamond, 1, 1), new AspectList().add(Aspect.CRYSTAL, 10).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));;
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_diamond, 1, 2), new AspectList().add(Aspect.CRYSTAL, 30).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_armor_diamond, 1, 3), new AspectList().add(Aspect.CRYSTAL, 20).add(IceAndFireAspectRegistry.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_meal), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 5).add(IceAndFireAspectRegistry.DRAGON, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fire_dragon_flesh), new AspectList().add(Aspect.DEATH, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.LIFE, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.ice_dragon_flesh), new AspectList().add(Aspect.DEATH, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.LIFE, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fire_dragon_heart), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.SOUL, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.ice_dragon_heart), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.COLD, 10).add(Aspect.SOUL, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.fire_dragon_blood), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.ALCHEMY, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.ice_dragon_blood), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.DRAGON, 15).add(Aspect.COLD, 10).add(Aspect.ALCHEMY, 30));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_stick), new AspectList().add(Aspect.TOOL, 10).add(Aspect.BEAST, 20).add(IceAndFireAspectRegistry.DRAGON, 35).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 40).add(Aspect.MIND, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_horn), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(IceAndFireAspectRegistry.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_horn_fire), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(IceAndFireAspectRegistry.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10).add(Aspect.FIRE, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_horn_ice), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(IceAndFireAspectRegistry.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10).add(Aspect.COLD, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.dragon_flute), new AspectList().add(Aspect.TOOL, 15).add(Aspect.BEAST, 25).add(IceAndFireAspectRegistry.MYTHICAL, 15).add(IceAndFireAspectRegistry.DRAGON, 25).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 30).add(Aspect.MIND, 15));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.hippogryph_egg), new AspectList().add(IceAndFireAspectRegistry.MYTHICAL, 10).add(Aspect.AIR, 5).add(Aspect.DESIRE, 20).add(Aspect.FLIGHT, 8).add(Aspect.BEAST, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.iron_hippogryph_armor), new AspectList().add(Aspect.METAL, 10).add(IceAndFireAspectRegistry.MYTHICAL, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 10).add(Aspect.FLIGHT, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.iron_hippogryph_armor), new AspectList().add(Aspect.METAL, 10).add(IceAndFireAspectRegistry.MYTHICAL, 10).add(Aspect.DESIRE, 15).add(Aspect.PROTECT, 15).add(Aspect.FLIGHT, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.iron_hippogryph_armor), new AspectList().add(Aspect.CRYSTAL, 10).add(IceAndFireAspectRegistry.MYTHICAL, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 20).add(Aspect.FLIGHT, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.gorgon_head), new AspectList().add(Aspect.DEATH, 50).add(IceAndFireAspectRegistry.MYTHICAL, 60).add(Aspect.DESIRE, 15).add(Aspect.MAN, 20).add(Aspect.EXCHANGE, 15).add(Aspect.EARTH, 15).add(Aspect.ENTROPY, 15).add(Aspect.TRAP, 5).add(Aspect.DARKNESS, 10).add(Aspect.UNDEAD, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.pixie_dust), new AspectList().add(Aspect.MAGIC, 30).add(IceAndFireAspectRegistry.MYTHICAL, 12).add(Aspect.DESIRE, 5).add(Aspect.MAN, 20).add(Aspect.LIFE, 15));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.ambrosia), new AspectList().add(Aspect.MAGIC, 40).add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.DESIRE, 20).add(Aspect.LIFE, 35));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.shiny_scales), new AspectList().add(Aspect.WATER, 10).add(IceAndFireAspectRegistry.MYTHICAL, 15).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.earplugs), new AspectList().add(Aspect.PLANT, 5).add(Aspect.SENSES, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.deathworm_chitin, 1, 0), new AspectList().add(Aspect.BEAST, 5).add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.deathworm_chitin, 1, 1), new AspectList().add(Aspect.BEAST, 5).add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.deathworm_chitin, 1, 2), new AspectList().add(Aspect.BEAST, 5).add(IceAndFireAspectRegistry.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.deathworm_egg, 1, 0), new AspectList().add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.MYTHICAL, 12).add(Aspect.LIFE, 7).add(Aspect.DESIRE, 7).add(Aspect.PROTECT, 7));
        ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.deathworm_egg, 1, 1), new AspectList().add(Aspect.BEAST, 10).add(IceAndFireAspectRegistry.MYTHICAL, 30).add(Aspect.LIFE, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 20));



    }
}
