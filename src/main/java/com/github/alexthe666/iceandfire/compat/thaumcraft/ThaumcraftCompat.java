package com.github.alexthe666.iceandfire.compat.thaumcraft;

import com.github.alexthe666.iceandfire.block.IaFBlockRegistry;
import com.github.alexthe666.iceandfire.item.IaFItemRegistry;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class ThaumcraftCompat {

    static final Aspect MYTHICAL = new Aspect("mythus", 0XD9D5AB, new Aspect[]{Aspect.BEAST, Aspect.MAGIC}, new ResourceLocation("iceandfire:textures/thaumcraft/mythical.png"), 1);
    static final Aspect DRAGON = getOrCreateAspect("draco", 0XA2271F, new Aspect[]{ThaumcraftCompat.MYTHICAL, Aspect.DESIRE}, new ResourceLocation("iceandfire", "textures/thaumcraft/dragon.png"), 1);
    private static final ThaumcraftCompat INSTANCE = new ThaumcraftCompat();
    private static boolean registered = false;

    static Aspect getOrCreateAspect(String tag, int color, Aspect[] components, ResourceLocation image, int blend) {
        Aspect a = Aspect.getAspect(tag);
        if (a != null) return a;
        return new Aspect(tag, color, components, image, blend);
    }

    @Deprecated
    public static void register() {
        if (!registered) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(INSTANCE);
        } else {
            throw new RuntimeException("You can only call ThaumcraftCompat.register() once");
        }
    }

    @SubscribeEvent
    public void aspectRegistrationEvent(AspectRegistryEvent evt) {
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.bestiary), new AspectList().add(ThaumcraftCompat.DRAGON, 2).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.BEAST, 6).add(Aspect.MAGIC, 2).add(Aspect.MIND, 4).add(Aspect.WATER, 2).add(Aspect.AIR, 1));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.manuscript), new AspectList().add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.PLANT, 3).add(Aspect.MIND, 2).add(Aspect.WATER, 2).add(Aspect.AIR, 1));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.sapphireGem), new AspectList().add(Aspect.CRYSTAL, 15).add(Aspect.DESIRE, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fire_stew), new AspectList().add(Aspect.FIRE, 8).add(ThaumcraftCompat.DRAGON, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.frost_stew), new AspectList().add(Aspect.COLD, 8).add(ThaumcraftCompat.DRAGON, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_red), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_bronze), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_green), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_gray), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_blue), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_silver), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_white), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonegg_sapphire), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_red), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_bronze), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_green), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_gray), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.FIRE, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_blue), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_silver), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_white), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonscales_sapphire), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 5).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone), new AspectList().add(Aspect.DEATH, 5).add(ThaumcraftCompat.DRAGON, 5).add(Aspect.MAGIC, 2));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.witherbone), new AspectList().add(Aspect.DEATH, 6).add(Aspect.DARKNESS, 5).add(Aspect.MAGIC, 2).add(Aspect.ENTROPY, 5).add(Aspect.UNDEAD, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.wither_shard), new AspectList().add(Aspect.DEATH, 2).add(Aspect.DARKNESS, 1).add(Aspect.ENTROPY, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fishing_spear), new AspectList().add(Aspect.PLANT, 2).add(Aspect.WATER, 7).add(Aspect.BEAST, 2).add(Aspect.TOOL, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fishing_spear), new AspectList().add(Aspect.PLANT, 2).add(Aspect.WATER, 7).add(Aspect.BEAST, 2).add(Aspect.TOOL, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_sword), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_sword_fire), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5).add(Aspect.FIRE, 25).add(Aspect.MAGIC, 2));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_sword_ice), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.PROTECT, 20).add(Aspect.DEATH, 5).add(Aspect.COLD, 25).add(Aspect.MAGIC, 2));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_pickaxe), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_axe), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_shovel), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_hoe), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.BEAST, 6).add(Aspect.TOOL, 20).add(Aspect.DEATH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_arrow), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.DESIRE, 2).add(Aspect.TOOL, 2).add(Aspect.AIR, 5).add(Aspect.FLIGHT, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonbone_bow), new AspectList().add(ThaumcraftCompat.DRAGON, 15).add(Aspect.DESIRE, 15).add(Aspect.PROTECT, 20).add(Aspect.AIR, 15).add(Aspect.FLIGHT, 10).add(Aspect.CRAFT, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_skull, 1, 0), new AspectList().add(Aspect.DEATH, 15).add(ThaumcraftCompat.DRAGON, 20).add(Aspect.MAGIC, 2).add(Aspect.FIRE, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_skull, 1, 1), new AspectList().add(Aspect.DEATH, 15).add(ThaumcraftCompat.DRAGON, 20).add(Aspect.MAGIC, 2).add(Aspect.COLD, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_iron, 1, 0), new AspectList().add(Aspect.METAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_iron, 1, 1), new AspectList().add(Aspect.METAL, 10).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_iron, 1, 2), new AspectList().add(Aspect.METAL, 30).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_iron, 1, 3), new AspectList().add(Aspect.METAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 20));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_gold, 1, 0), new AspectList().add(Aspect.METAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_gold, 1, 1), new AspectList().add(Aspect.METAL, 10).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_gold, 1, 2), new AspectList().add(Aspect.METAL, 30).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_gold, 1, 3), new AspectList().add(Aspect.METAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_diamond, 1, 0), new AspectList().add(Aspect.CRYSTAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_diamond, 1, 1), new AspectList().add(Aspect.CRYSTAL, 10).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_diamond, 1, 2), new AspectList().add(Aspect.CRYSTAL, 30).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_armor_diamond, 1, 3), new AspectList().add(Aspect.CRYSTAL, 20).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.DESIRE, 30).add(Aspect.PROTECT, 40));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_meal), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 5).add(ThaumcraftCompat.DRAGON, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fire_dragon_flesh), new AspectList().add(Aspect.DEATH, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.LIFE, 3));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.ice_dragon_flesh), new AspectList().add(Aspect.DEATH, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.LIFE, 3));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fire_dragon_heart), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.SOUL, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.ice_dragon_heart), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.COLD, 10).add(Aspect.SOUL, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.fire_dragon_blood), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.FIRE, 10).add(Aspect.ALCHEMY, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.ice_dragon_blood), new AspectList().add(Aspect.LIFE, 10).add(Aspect.BEAST, 10).add(ThaumcraftCompat.DRAGON, 15).add(Aspect.COLD, 10).add(Aspect.ALCHEMY, 30));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_stick), new AspectList().add(Aspect.TOOL, 10).add(Aspect.BEAST, 20).add(ThaumcraftCompat.DRAGON, 35).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 40).add(Aspect.MIND, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_horn), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(ThaumcraftCompat.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_horn_fire), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(ThaumcraftCompat.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10).add(Aspect.FIRE, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_horn_ice), new AspectList().add(Aspect.LIFE, 20).add(Aspect.BEAST, 25).add(ThaumcraftCompat.DRAGON, 50).add(Aspect.MAGIC, 20).add(Aspect.CRAFT, 15).add(Aspect.ORDER, 20).add(Aspect.EXCHANGE, 20).add(Aspect.SOUL, 10).add(Aspect.COLD, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragon_flute), new AspectList().add(Aspect.TOOL, 15).add(Aspect.BEAST, 25).add(ThaumcraftCompat.MYTHICAL, 15).add(ThaumcraftCompat.DRAGON, 25).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 30).add(Aspect.MIND, 15));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.iron_hippogryph_armor), new AspectList().add(Aspect.METAL, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DESIRE, 10).add(Aspect.PROTECT, 10).add(Aspect.FLIGHT, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.iron_hippogryph_armor), new AspectList().add(Aspect.METAL, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DESIRE, 15).add(Aspect.PROTECT, 15).add(Aspect.FLIGHT, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.iron_hippogryph_armor), new AspectList().add(Aspect.CRYSTAL, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 20).add(Aspect.FLIGHT, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.gorgon_head), new AspectList().add(Aspect.DEATH, 50).add(ThaumcraftCompat.MYTHICAL, 60).add(Aspect.DESIRE, 15).add(Aspect.MAN, 20).add(Aspect.EXCHANGE, 15).add(Aspect.EARTH, 15).add(Aspect.ENTROPY, 15).add(Aspect.TRAP, 5).add(Aspect.DARKNESS, 10).add(Aspect.UNDEAD, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.pixie_dust), new AspectList().add(Aspect.MAGIC, 30).add(ThaumcraftCompat.MYTHICAL, 12).add(Aspect.DESIRE, 5).add(Aspect.MAN, 20).add(Aspect.LIFE, 15));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.ambrosia), new AspectList().add(Aspect.MAGIC, 40).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.DESIRE, 20).add(Aspect.LIFE, 35));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.shiny_scales), new AspectList().add(Aspect.WATER, 10).add(ThaumcraftCompat.MYTHICAL, 15).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.earplugs), new AspectList().add(Aspect.PLANT, 5).add(Aspect.SENSES, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 0), new AspectList().add(Aspect.BEAST, 5).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 1), new AspectList().add(Aspect.BEAST, 5).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_chitin, 1, 2), new AspectList().add(Aspect.BEAST, 5).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.PROTECT, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_egg, 1, 0), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 12).add(Aspect.LIFE, 7).add(Aspect.DESIRE, 7).add(Aspect.PROTECT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_egg, 1, 1), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.LIFE, 10).add(Aspect.DESIRE, 20).add(Aspect.PROTECT, 20));
        for (int i = 0; i < EnumHippogryphTypes.values().length; i++) {
            evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.hippogryph_egg, 1, i), new AspectList().add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.AIR, 5).add(Aspect.DESIRE, 20).add(Aspect.FLIGHT, 8).add(Aspect.BEAST, 6));
        }
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.rotten_egg), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.LIFE, 5).add(Aspect.DEATH, 5).add(Aspect.UNDEAD, 5).add(Aspect.ENTROPY, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.stymphalian_bird_feather), new AspectList().add(Aspect.BEAST, 5).add(ThaumcraftCompat.MYTHICAL, 15).add(Aspect.DESIRE, 5).add(Aspect.METAL, 7).add(Aspect.FLIGHT, 3));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.stymphalian_arrow), new AspectList().add(Aspect.BEAST, 2).add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.DESIRE, 2).add(Aspect.PROTECT, 2).add(Aspect.METAL, 7).add(Aspect.FLIGHT, 3).add(Aspect.CRAFT, 3));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.troll_tusk), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DEATH, 5).add(Aspect.BEAST, 5).add(Aspect.AVERSION, 2).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.stone_statue), new AspectList().add(Aspect.BEAST, 20).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DEATH, 10).add(Aspect.MAGIC, 5).add(Aspect.AVERSION, 2).add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5));

        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
            evt.register.registerObjectTag(new ItemStack(weapon.item), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.DEATH, 5).add(Aspect.PROTECT, 15).add(Aspect.CRAFT, 2).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5));
        }
        evt.register.registerObjectTag(new ItemStack(EnumTroll.MOUNTAIN.leather), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.PROTECT, 5).add(Aspect.DESIRE, 2).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5).add(Aspect.EARTH, 7));
        evt.register.registerObjectTag(new ItemStack(EnumTroll.FROST.leather), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.PROTECT, 5).add(Aspect.DESIRE, 2).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5).add(Aspect.COLD, 7));
        evt.register.registerObjectTag(new ItemStack(EnumTroll.FOREST.leather), new AspectList().add(Aspect.BEAST, 10).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.PROTECT, 5).add(Aspect.DESIRE, 2).add(Aspect.MAGIC, 5).add(Aspect.ENTROPY, 5).add(Aspect.PLANT, 7));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.lectern), new AspectList().add(Aspect.PLANT, 8).add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.SENSES, 10).add(Aspect.MIND, 12));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 0), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 1), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 2), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 3), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 4), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.podium, 1, 5), new AspectList().add(Aspect.PLANT, 7).add(ThaumcraftCompat.MYTHICAL, 3).add(Aspect.SENSES, 4).add(Aspect.DESIRE, 20));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.fire_lily), new AspectList().add(Aspect.FIRE, 5).add(Aspect.PLANT, 7).add(ThaumcraftCompat.DRAGON, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frost_lily), new AspectList().add(Aspect.COLD, 5).add(Aspect.PLANT, 7).add(ThaumcraftCompat.DRAGON, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.sapphireOre), new AspectList().add(Aspect.CRYSTAL, 15).add(Aspect.DESIRE, 10).add(Aspect.EARTH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.sapphireOre), new AspectList().add(Aspect.CRYSTAL, 15).add(Aspect.DESIRE, 10).add(Aspect.EARTH, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedDirt), new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedGrass), new AspectList().add(Aspect.EARTH, 5).add(Aspect.PLANT, 2).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedGrassPath), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ORDER, 2).add(Aspect.PLANT, 2).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedStone), new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedCobblestone), new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 2).add(Aspect.ENTROPY, 1));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.charedGravel), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 2).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.ash), new AspectList().add(Aspect.ENTROPY, 5).add(Aspect.FIRE, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenDirt), new AspectList().add(Aspect.EARTH, 5).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenGrass), new AspectList().add(Aspect.EARTH, 5).add(Aspect.PLANT, 2).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenGrassPath), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ORDER, 2).add(Aspect.PLANT, 2).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenStone), new AspectList().add(Aspect.EARTH, 5).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenGravel), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 2).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.dragon_ice), new AspectList().add(Aspect.ENTROPY, 2).add(Aspect.COLD, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.frozenCobblestone), new AspectList().add(Aspect.EARTH, 5).add(Aspect.FIRE, 2).add(Aspect.ENTROPY, 1));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.dragon_ice_spikes), new AspectList().add(Aspect.DEATH, 2).add(Aspect.TRAP, 2).add(Aspect.ENTROPY, 5).add(Aspect.COLD, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.nest), new AspectList().add(Aspect.EARTH, 5).add(Aspect.LIFE, 2).add(ThaumcraftCompat.DRAGON, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 0), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 1), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 2), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 3), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 4), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.pixieHouse, 1, 5), new AspectList().add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1).add(Aspect.PLANT, 3).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.MAGIC, 3));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_empty), new AspectList().add(Aspect.CRYSTAL, 3).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_pixie, 1, 0), new AspectList().add(Aspect.CRYSTAL, 3).add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.MAGIC, 10).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_pixie, 1, 1), new AspectList().add(Aspect.CRYSTAL, 3).add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.MAGIC, 10).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_pixie, 1, 2), new AspectList().add(Aspect.CRYSTAL, 3).add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.MAGIC, 10).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_pixie, 1, 3), new AspectList().add(Aspect.CRYSTAL, 3).add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.MAGIC, 10).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jar_pixie, 1, 4), new AspectList().add(Aspect.CRYSTAL, 3).add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.MAGIC, 10).add(Aspect.AIR, 5));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.eggInIce), new AspectList().add(ThaumcraftCompat.DRAGON, 5).add(Aspect.COLD, 20).add(Aspect.DESIRE, 20).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, 0), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, 1), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, 2), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, 3), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_egg, 1, 4), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 25).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, 0), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, 1), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, 2), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, 3), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_egg, 1, 4), new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 25).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_resin), new AspectList().add(Aspect.EXCHANGE, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 7).add(Aspect.BEAST, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_resin), new AspectList().add(Aspect.EXCHANGE, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 7).add(Aspect.BEAST, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_chitin), new AspectList().add(Aspect.BEAST, 5).add(Aspect.PROTECT, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_chitin), new AspectList().add(Aspect.BEAST, 5).add(Aspect.PROTECT, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_stinger), new AspectList().add(Aspect.BEAST, 15).add(Aspect.TRAP, 5).add(Aspect.DEATH, 5).add(ThaumcraftCompat.MYTHICAL, 7));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_desert_staff), new AspectList().add(Aspect.TOOL, 10).add(Aspect.BEAST, 20).add(ThaumcraftCompat.MYTHICAL, 15).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 40).add(Aspect.MIND, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.myrmex_jungle_staff), new AspectList().add(Aspect.TOOL, 10).add(Aspect.BEAST, 20).add(ThaumcraftCompat.MYTHICAL, 15).add(Aspect.CRAFT, 5).add(Aspect.MAGIC, 15).add(Aspect.MOTION, 10).add(Aspect.ORDER, 40).add(Aspect.MIND, 10));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_resin, 1, 0), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_resin, 1, 1), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_resin_sticky, 1, 0), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_resin_sticky, 1, 1), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(ThaumcraftCompat.MYTHICAL, 2).add(Aspect.TRAP, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.desert_myrmex_cocoon), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(Aspect.CRAFT, 5).add(Aspect.MECHANISM, 5).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.jungle_myrmex_cocoon), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 5).add(Aspect.CRAFT, 5).add(Aspect.MECHANISM, 5).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_desert_biolight), new AspectList().add(Aspect.EARTH, 5).add(Aspect.PLANT, 5).add(Aspect.AURA, 2).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_jungle_biolight), new AspectList().add(Aspect.EARTH, 5).add(Aspect.PLANT, 5).add(Aspect.AURA, 2).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_desert_resin_block), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ORDER, 2).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_jungle_resin_block), new AspectList().add(Aspect.EARTH, 5).add(Aspect.ORDER, 2).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_desert_resin_glass), new AspectList().add(Aspect.EARTH, 5).add(Aspect.AIR, 2).add(ThaumcraftCompat.MYTHICAL, 2));
        evt.register.registerObjectTag(new ItemStack(IaFBlockRegistry.myrmex_jungle_resin_glass), new AspectList().add(Aspect.EARTH, 5).add(Aspect.AIR, 2).add(ThaumcraftCompat.MYTHICAL, 2));

        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonsteel_fire_ingot), new AspectList().add(Aspect.METAL, 40).add(ThaumcraftCompat.MYTHICAL, 20).add(DRAGON, 10).add(Aspect.FIRE, 10).add(Aspect.MAGIC, 25).add(Aspect.DESIRE, 45));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.dragonsteel_ice_ingot), new AspectList().add(Aspect.METAL, 40).add(ThaumcraftCompat.MYTHICAL, 20).add(DRAGON, 10).add(Aspect.WATER, 10).add(Aspect.MAGIC, 25).add(Aspect.DESIRE, 45));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.hippogryph_talon), new AspectList().add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.AIR, 5).add(Aspect.DESIRE, 20).add(Aspect.FLIGHT, 8).add(Aspect.BEAST, 6));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.pixie_wings), new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.FLIGHT, 5).add(Aspect.MAN, 10).add(Aspect.AIR, 10).add(Aspect.MAGIC, 30).add(Aspect.DESIRE, 5).add(Aspect.EXCHANGE, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.cyclops_eye), new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.MAN, 10).add(Aspect.DEATH, 10).add(Aspect.MAGIC, 30).add(Aspect.DESIRE, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.siren_tear), new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.MAN, 10).add(Aspect.DESIRE, 25).add(Aspect.MAGIC, 30).add(Aspect.WATER, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.hippocampus_fin), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.WATER, 25));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.deathworm_tounge), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.TRAP, 5));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.cockatrice_eye), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.DEATH, 35));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.amphithere_feather), new AspectList().add(ThaumcraftCompat.MYTHICAL, 10).add(Aspect.BEAST, 10).add(Aspect.FLIGHT, 10));
        evt.register.registerObjectTag(new ItemStack(IaFItemRegistry.sea_serpent_fang), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.WATER, 10));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            evt.register.registerObjectTag(new ItemStack(serpent.scale), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.WATER, 10));
        }
        for (EnumSkullType skull : EnumSkullType.values()) {
            evt.register.registerObjectTag(new ItemStack(skull.skull_item), new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.BEAST, 10).add(Aspect.DEATH, 10));
        }
        ThaumcraftApi.registerEntityTag("dragonegg", new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerEntityTag("dragonskull", new AspectList().add(Aspect.DEATH, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.DRAGON, 10).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerEntityTag("firedragon", new AspectList().add(Aspect.FIRE, 25).add(Aspect.BEAST, 15).add(Aspect.FLIGHT, 25).add(Aspect.MIND, 5).add(ThaumcraftCompat.DRAGON, 50).add(Aspect.MAGIC, 10));
        ThaumcraftApi.registerEntityTag("icedragon", new AspectList().add(Aspect.COLD, 25).add(Aspect.BEAST, 15).add(Aspect.FLIGHT, 25).add(Aspect.MIND, 5).add(ThaumcraftCompat.DRAGON, 50).add(Aspect.MAGIC, 10));
        ThaumcraftApi.registerEntityTag("snowvillager", new AspectList().add(Aspect.COLD, 20).add(Aspect.MAN, 10).add(Aspect.MIND, 7));
        ThaumcraftApi.registerEntityTag("hippogryph", new AspectList().add(ThaumcraftCompat.MYTHICAL, 35).add(Aspect.FLIGHT, 15).add(Aspect.BEAST, 10).add(Aspect.AIR, 7));
        ThaumcraftApi.registerEntityTag("stonestatue", new AspectList().add(ThaumcraftCompat.MYTHICAL, 5).add(Aspect.EARTH, 10).add(Aspect.MAGIC, 5).add(Aspect.DEATH, 5).add(Aspect.ELDRITCH, 5));
        ThaumcraftApi.registerEntityTag("gorgon", new AspectList().add(ThaumcraftCompat.MYTHICAL, 50).add(Aspect.DEATH, 20).add(Aspect.MAN, 10).add(Aspect.BEAST, 10).add(Aspect.UNDEAD, 10).add(Aspect.DESIRE, 15).add(Aspect.EARTH, 5).add(Aspect.SENSES, 10));
        ThaumcraftApi.registerEntityTag("if_pixie", new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.FLIGHT, 5).add(Aspect.MAN, 10).add(Aspect.AIR, 10).add(Aspect.MAGIC, 30).add(Aspect.DESIRE, 5).add(Aspect.EXCHANGE, 5));
        ThaumcraftApi.registerEntityTag("cyclops", new AspectList().add(ThaumcraftCompat.MYTHICAL, 50).add(Aspect.MAN, 10).add(Aspect.MIND, 2).add(Aspect.ELDRITCH, 5).add(Aspect.LIFE, 5).add(Aspect.EARTH, 10).add(Aspect.DEATH, 2).add(Aspect.SENSES, 10));
        ThaumcraftApi.registerEntityTag("siren", new AspectList().add(ThaumcraftCompat.MYTHICAL, 50).add(Aspect.MAN, 10).add(Aspect.WATER, 20).add(Aspect.DESIRE, 150).add(Aspect.DEATH, 5).add(Aspect.SENSES, 40));
        ThaumcraftApi.registerEntityTag("hippocampus", new AspectList().add(ThaumcraftCompat.MYTHICAL, 35).add(Aspect.WATER, 15).add(Aspect.DESIRE, 5).add(Aspect.BEAST, 5));
        ThaumcraftApi.registerEntityTag("deathworm", new AspectList().add(ThaumcraftCompat.MYTHICAL, 20).add(Aspect.DEATH, 15).add(Aspect.MOTION, 5).add(Aspect.BEAST, 20).add(Aspect.PROTECT, 10).add(Aspect.ELDRITCH, 15));
        ThaumcraftApi.registerEntityTag("stymphalianbird", new AspectList().add(ThaumcraftCompat.MYTHICAL, 35).add(Aspect.FLIGHT, 20).add(Aspect.METAL, 20).add(Aspect.BEAST, 20).add(Aspect.PROTECT, 15).add(Aspect.AVERSION, 15));
        ThaumcraftApi.registerEntityTag("if_cockatrice", new AspectList().add(ThaumcraftCompat.MYTHICAL, 35).add(Aspect.FLIGHT, 20).add(Aspect.ALCHEMY, 20).add(Aspect.DARKNESS, 20).add(Aspect.AVERSION, 15));
        ThaumcraftApi.registerEntityTag("if_troll", new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.BEAST, 20).add(Aspect.AVERSION, 10).add(Aspect.DARKNESS, 20).add(Aspect.MAGIC, 15).add(Aspect.MAN, 5));
        ThaumcraftApi.registerEntityTag("myrmex_egg", new AspectList().add(Aspect.LIFE, 5).add(Aspect.DESIRE, 5).add(ThaumcraftCompat.MYTHICAL, 5));
        ThaumcraftApi.registerEntityTag("myrmex_worker", new AspectList().add(Aspect.BEAST, 15).add(Aspect.EXCHANGE, 5).add(ThaumcraftCompat.MYTHICAL, 10));
        ThaumcraftApi.registerEntityTag("myrmex_soldier", new AspectList().add(Aspect.BEAST, 15).add(Aspect.PROTECT, 5).add(ThaumcraftCompat.MYTHICAL, 15));
        ThaumcraftApi.registerEntityTag("myrmex_sentinel", new AspectList().add(Aspect.BEAST, 20).add(Aspect.PROTECT, 10).add(ThaumcraftCompat.MYTHICAL, 20));
        ThaumcraftApi.registerEntityTag("myrmex_royal", new AspectList().add(Aspect.BEAST, 20).add(Aspect.SENSES, 10).add(Aspect.FLIGHT, 10).add(ThaumcraftCompat.MYTHICAL, 20));
        ThaumcraftApi.registerEntityTag("myrmex_queen", new AspectList().add(Aspect.BEAST, 35).add(Aspect.EXCHANGE, 10).add(Aspect.LIFE, 10).add(ThaumcraftCompat.MYTHICAL, 30));
        ThaumcraftApi.registerEntityTag("myrmex_swarmer", new AspectList().add(Aspect.BEAST, 10).add(Aspect.SENSES, 5).add(Aspect.FLIGHT, 5).add(ThaumcraftCompat.MYTHICAL, 5));
        ThaumcraftApi.registerEntityTag("seaserpent", new AspectList().add(ThaumcraftCompat.MYTHICAL, 40).add(Aspect.BEAST, 30).add(Aspect.WATER, 20).add(Aspect.DEATH, 25).add(Aspect.DARKNESS, 10));
        ThaumcraftApi.registerEntityTag("amphithere", new AspectList().add(ThaumcraftCompat.MYTHICAL, 30).add(Aspect.FLIGHT, 40).add(Aspect.BEAST, 10).add(Aspect.AIR, 20));

    }
}
