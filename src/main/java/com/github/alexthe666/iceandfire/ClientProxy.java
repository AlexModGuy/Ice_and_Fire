package com.github.alexthe666.iceandfire;

import java.io.IOException;

import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.model.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.ModelFireDragonArmor;
import com.github.alexthe666.iceandfire.client.model.ModelIceDragonArmor;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.particle.ParticleBlood;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonFire;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonIce;
import com.github.alexthe666.iceandfire.client.particle.ParticlePixieDust;
import com.github.alexthe666.iceandfire.client.render.entity.RenderCyclops;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonArrow;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonFireCharge;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonSkull;
import com.github.alexthe666.iceandfire.client.render.entity.RenderGorgon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHippogryph;
import com.github.alexthe666.iceandfire.client.render.entity.RenderModCapes;
import com.github.alexthe666.iceandfire.client.render.entity.RenderNothing;
import com.github.alexthe666.iceandfire.client.render.entity.RenderPixie;
import com.github.alexthe666.iceandfire.client.render.entity.RenderSnowVillager;
import com.github.alexthe666.iceandfire.client.render.entity.RenderStoneStatue;
import com.github.alexthe666.iceandfire.client.render.tile.RenderEggInIce;
import com.github.alexthe666.iceandfire.client.render.tile.RenderGorgonHead;
import com.github.alexthe666.iceandfire.client.render.tile.RenderJar;
import com.github.alexthe666.iceandfire.client.render.tile.RenderLectern;
import com.github.alexthe666.iceandfire.client.render.tile.RenderPixieHouse;
import com.github.alexthe666.iceandfire.client.render.tile.RenderPodium;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonFireCharge;
import com.github.alexthe666.iceandfire.entity.EntityDragonIceCharge;
import com.github.alexthe666.iceandfire.entity.EntityDragonIceProjectile;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.EntitySnowVillager;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHead;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDummyGorgonHeadActive;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.event.EventClient;
import com.github.alexthe666.iceandfire.event.EventNewMenu;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

	private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL = new ModelFireDragonArmor(0.5F);
	private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelFireDragonArmor(0.2F);
	private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL = new ModelIceDragonArmor(0.5F);
	private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelIceDragonArmor(0.2F);
	private FontRenderer bestiaryFontRenderer;

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.lectern), 0, new ModelResourceLocation("iceandfire:lectern", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.podium), new ResourceLocation("iceandfire:podium_oak"), new ResourceLocation("iceandfire:podium_spruce"), new ResourceLocation("iceandfire:podium_birch"), new ResourceLocation("iceandfire:podium_jungle"), new ResourceLocation("iceandfire:podium_acacia"), new ResourceLocation("iceandfire:podium_dark_oak"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 0, new ModelResourceLocation("iceandfire:podium_oak", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 1, new ModelResourceLocation("iceandfire:podium_spruce", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 2, new ModelResourceLocation("iceandfire:podium_birch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 3, new ModelResourceLocation("iceandfire:podium_jungle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 4, new ModelResourceLocation("iceandfire:podium_acacia", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.podium), 5, new ModelResourceLocation("iceandfire:podium_dark_oak", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.fire_lily), 0, new ModelResourceLocation("iceandfire:fire_lily", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frost_lily), 0, new ModelResourceLocation("iceandfire:frost_lily", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.goldPile), 0, new ModelResourceLocation("iceandfire:goldpile", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.silverOre), 0, new ModelResourceLocation("iceandfire:silver_ore", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.sapphireOre), 0, new ModelResourceLocation("iceandfire:sapphire_ore", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.silverBlock), 0, new ModelResourceLocation("iceandfire:silver_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.sapphireBlock), 0, new ModelResourceLocation("iceandfire:sapphire_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedDirt), 0, new ModelResourceLocation("iceandfire:chared_dirt", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedGrass), 0, new ModelResourceLocation("iceandfire:chared_grass", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedStone), 0, new ModelResourceLocation("iceandfire:chared_stone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedCobblestone), 0, new ModelResourceLocation("iceandfire:chared_cobblestone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedGravel), 0, new ModelResourceLocation("iceandfire:chared_gravel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charedGrassPath), 0, new ModelResourceLocation("iceandfire:chared_grass_path", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ash), 0, new ModelResourceLocation("iceandfire:ash", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenDirt), 0, new ModelResourceLocation("iceandfire:frozen_dirt", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenGrass), 0, new ModelResourceLocation("iceandfire:frozen_grass", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenStone), 0, new ModelResourceLocation("iceandfire:frozen_stone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenCobblestone), 0, new ModelResourceLocation("iceandfire:frozen_cobblestone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenGravel), 0, new ModelResourceLocation("iceandfire:frozen_gravel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenGrassPath), 0, new ModelResourceLocation("iceandfire:frozen_grass_path", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.dragon_ice), 0, new ModelResourceLocation("iceandfire:dragon_ice", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.dragon_ice_spikes), 0, new ModelResourceLocation("iceandfire:dragon_ice_spikes", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.eggInIce), 0, new ModelResourceLocation("iceandfire:egginice", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.silverPile), 0, new ModelResourceLocation("iceandfire:silverpile", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.bestiary, 0, new ModelResourceLocation("iceandfire:bestiary", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.manuscript, 0, new ModelResourceLocation("iceandfire:manuscript", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.sapphireGem, 0, new ModelResourceLocation("iceandfire:sapphire_gem", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silverIngot, 0, new ModelResourceLocation("iceandfire:silver_ingot", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silverNugget, 0, new ModelResourceLocation("iceandfire:silver_nugget", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_helmet, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_chestplate, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_leggings, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_boots, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_boots", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_sword, 0, new ModelResourceLocation("iceandfire:silver_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_shovel, 0, new ModelResourceLocation("iceandfire:silver_shovel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_pickaxe, 0, new ModelResourceLocation("iceandfire:silver_pickaxe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_axe, 0, new ModelResourceLocation("iceandfire:silver_axe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.silver_hoe, 0, new ModelResourceLocation("iceandfire:silver_hoe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.frost_stew, 0, new ModelResourceLocation("iceandfire:frost_stew", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.fire_stew, 0, new ModelResourceLocation("iceandfire:fire_stew", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_red, 0, new ModelResourceLocation("iceandfire:dragonegg_red", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_green, 0, new ModelResourceLocation("iceandfire:dragonegg_green", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_bronze, 0, new ModelResourceLocation("iceandfire:dragonegg_bronze", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_gray, 0, new ModelResourceLocation("iceandfire:dragonegg_gray", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_blue, 0, new ModelResourceLocation("iceandfire:dragonegg_blue", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_white, 0, new ModelResourceLocation("iceandfire:dragonegg_white", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_sapphire, 0, new ModelResourceLocation("iceandfire:dragonegg_sapphire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonegg_silver, 0, new ModelResourceLocation("iceandfire:dragonegg_silver", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_red, 0, new ModelResourceLocation("iceandfire:dragonscales_red", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_green, 0, new ModelResourceLocation("iceandfire:dragonscales_green", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_bronze, 0, new ModelResourceLocation("iceandfire:dragonscales_bronze", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_gray, 0, new ModelResourceLocation("iceandfire:dragonscales_gray", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_blue, 0, new ModelResourceLocation("iceandfire:dragonscales_blue", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_white, 0, new ModelResourceLocation("iceandfire:dragonscales_white", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_sapphire, 0, new ModelResourceLocation("iceandfire:dragonscales_sapphire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonscales_silver, 0, new ModelResourceLocation("iceandfire:dragonscales_silver", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone, 0, new ModelResourceLocation("iceandfire:dragonbone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.witherbone, 0, new ModelResourceLocation("iceandfire:witherbone", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.fishing_spear, 0, new ModelResourceLocation("iceandfire:fishing_spear", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.wither_shard, 0, new ModelResourceLocation("iceandfire:wither_shard", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_sword, 0, new ModelResourceLocation("iceandfire:dragonbone_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_shovel, 0, new ModelResourceLocation("iceandfire:dragonbone_shovel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_pickaxe, 0, new ModelResourceLocation("iceandfire:dragonbone_pickaxe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_axe, 0, new ModelResourceLocation("iceandfire:dragonbone_axe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_hoe, 0, new ModelResourceLocation("iceandfire:dragonbone_hoe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_sword_fire, 0, new ModelResourceLocation("iceandfire:dragonbone_sword_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_sword_ice, 0, new ModelResourceLocation("iceandfire:dragonbone_sword_ice", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_arrow, 0, new ModelResourceLocation("iceandfire:dragonbone_arrow", "inventory"));
		ModelBakery.registerItemVariants(ModItems.dragon_skull, new ResourceLocation("iceandfire:dragonbone_bow"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_1"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
		ModelBakery.registerItemVariants(ModItems.dragon_skull, new ResourceLocation("iceandfire:dragon_skull_fire"), new ResourceLocation("iceandfire:dragon_skull_ice"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_skull, 0, new ModelResourceLocation("iceandfire:dragon_skull_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_skull, 1, new ModelResourceLocation("iceandfire:dragon_skull_ice", "inventory"));
		ModelBakery.registerItemVariants(ModItems.dragon_armor_iron, new ResourceLocation("iceandfire:dragonarmor_iron_head"), new ResourceLocation("iceandfire:dragonarmor_iron_neck"), new ResourceLocation("iceandfire:dragonarmor_iron_body"), new ResourceLocation("iceandfire:dragonarmor_iron_tail"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_iron, 0, new ModelResourceLocation("iceandfire:dragonarmor_iron_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_iron, 1, new ModelResourceLocation("iceandfire:dragonarmor_iron_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_iron, 2, new ModelResourceLocation("iceandfire:dragonarmor_iron_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_iron, 3, new ModelResourceLocation("iceandfire:dragonarmor_iron_tail", "inventory"));
		ModelBakery.registerItemVariants(ModItems.dragon_armor_gold, new ResourceLocation("iceandfire:dragonarmor_gold_head"), new ResourceLocation("iceandfire:dragonarmor_gold_neck"), new ResourceLocation("iceandfire:dragonarmor_gold_body"), new ResourceLocation("iceandfire:dragonarmor_gold_tail"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_gold, 0, new ModelResourceLocation("iceandfire:dragonarmor_gold_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_gold, 1, new ModelResourceLocation("iceandfire:dragonarmor_gold_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_gold, 2, new ModelResourceLocation("iceandfire:dragonarmor_gold_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_gold, 3, new ModelResourceLocation("iceandfire:dragonarmor_gold_tail", "inventory"));
		ModelBakery.registerItemVariants(ModItems.dragon_armor_diamond, new ResourceLocation("iceandfire:dragonarmor_diamond_head"), new ResourceLocation("iceandfire:dragonarmor_diamond_neck"), new ResourceLocation("iceandfire:dragonarmor_diamond_body"), new ResourceLocation("iceandfire:dragonarmor_diamond_tail"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_diamond, 0, new ModelResourceLocation("iceandfire:dragonarmor_diamond_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_diamond, 1, new ModelResourceLocation("iceandfire:dragonarmor_diamond_neck", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_diamond, 2, new ModelResourceLocation("iceandfire:dragonarmor_diamond_body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_armor_diamond, 3, new ModelResourceLocation("iceandfire:dragonarmor_diamond_tail", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_meal, 0, new ModelResourceLocation("iceandfire:dragon_meal", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.fire_dragon_flesh, 0, new ModelResourceLocation("iceandfire:fire_dragon_flesh", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ice_dragon_flesh, 0, new ModelResourceLocation("iceandfire:ice_dragon_flesh", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.fire_dragon_heart, 0, new ModelResourceLocation("iceandfire:fire_dragon_heart", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ice_dragon_heart, 0, new ModelResourceLocation("iceandfire:ice_dragon_heart", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.fire_dragon_blood, 0, new ModelResourceLocation("iceandfire:fire_dragon_blood", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ice_dragon_blood, 0, new ModelResourceLocation("iceandfire:ice_dragon_blood", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_stick, 0, new ModelResourceLocation("iceandfire:dragon_stick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_horn, 0, new ModelResourceLocation("iceandfire:dragon_horn", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_horn_fire, 0, new ModelResourceLocation("iceandfire:dragon_horn_fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_horn_ice, 0, new ModelResourceLocation("iceandfire:dragon_horn_ice", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.dragon_flute, 0, new ModelResourceLocation("iceandfire:dragon_flute", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.hippogryph_egg, 0, new ModelResourceLocation("iceandfire:hippogryph_egg", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.iron_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:iron_hippogryph_armor", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.gold_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:gold_hippogryph_armor", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.diamond_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:diamond_hippogryph_armor", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.gorgon_head, 0, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.gorgon_head, 1, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.stone_statue, 0, new ModelResourceLocation("iceandfire:stone_statue", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.blindfold, 0, new ModelResourceLocation("iceandfire:blindfold", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 0, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 1, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 2, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 3, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 4, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.pixieHouse), 5, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.pixie_dust, 0, new ModelResourceLocation("iceandfire:pixie_dust", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ambrosia, 0, new ModelResourceLocation("iceandfire:ambrosia", "inventory"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.jar), new ResourceLocation("iceandfire:jar"), new ResourceLocation("iceandfire:jar_0"), new ResourceLocation("iceandfire:jar_1"), new ResourceLocation("iceandfire:jar_2"), new ResourceLocation("iceandfire:jar_3"), new ResourceLocation("iceandfire:jar_4"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 0, new ModelResourceLocation("iceandfire:jar", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 1, new ModelResourceLocation("iceandfire:jar_0", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 2, new ModelResourceLocation("iceandfire:jar_1", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 3, new ModelResourceLocation("iceandfire:jar_2", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 4, new ModelResourceLocation("iceandfire:jar_3", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.jar), 5, new ModelResourceLocation("iceandfire:jar_4", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.frozenSplinters), 0, new ModelResourceLocation("iceandfire:frozen_splinters", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.nest), 0, new ModelResourceLocation("iceandfire:nest", "inventory"));
		for (EnumDragonArmor armor : EnumDragonArmor.values()) {
			renderArmors(armor);
		}
		ModelLoader.setCustomModelResourceLocation(ModItems.sheep_helmet, 0, new ModelResourceLocation("iceandfire:sheep_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.sheep_chestplate, 0, new ModelResourceLocation("iceandfire:sheep_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.sheep_leggings, 0, new ModelResourceLocation("iceandfire:sheep_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.sheep_boots, 0, new ModelResourceLocation("iceandfire:sheep_boots", "inventory"));

	}

	public static void renderArmors(EnumDragonArmor armor) {
		ModelLoader.setCustomModelResourceLocation(armor.helmet, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.chestplate, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.leggings, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(armor.boots, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_boots", "inventory"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void render() {
		this.bestiaryFontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("iceandfire:textures/font/bestiary.png"), Minecraft.getMinecraft().renderEngine, false);
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.bestiaryFontRenderer);
		ModKeys.init();
		MinecraftForge.EVENT_BUS.register(new RenderModCapes());
		MinecraftForge.EVENT_BUS.register(new EventClient());
		MinecraftForge.EVENT_BUS.register(new EventNewMenu());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHead.class, new RenderGorgonHead(false));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHeadActive.class, new RenderGorgonHead(true));
		ForgeHooksClient.registerTESRItemStack(ModItems.gorgon_head, 0, TileEntityDummyGorgonHead.class);
		ForgeHooksClient.registerTESRItemStack(ModItems.gorgon_head, 1, TileEntityDummyGorgonHeadActive.class);
		renderEntities();
	}

	public void postRender() {
		EventClient.initializeStoneLayer();
	}

	@SuppressWarnings("deprecation")
	private void renderEntities() {
		EnumDragonAnimations.initializeDragonModels();
		ModelBase firedragon_model = null;
		ModelBase icedragon_model = null;

		try {
			firedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFireGround"), new FireDragonTabulaModelAnimator());
			icedragon_model = new IceAndFireTabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIceGround"), new IceDragonTabulaModelAnimator());
		} catch (IOException e) {
			e.printStackTrace();
		}
		RenderingRegistry.registerEntityRenderingHandler(EntityFireDragon.class, new RenderDragonBase(Minecraft.getMinecraft().getRenderManager(), firedragon_model, true));
		RenderingRegistry.registerEntityRenderingHandler(EntityIceDragon.class, new RenderDragonBase(Minecraft.getMinecraft().getRenderManager(), icedragon_model, false));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonEgg.class, new RenderDragonEgg(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonArrow.class, new RenderDragonArrow(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonSkull.class, new RenderDragonSkull(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonFire.class, new RenderNothing(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonFireCharge.class, new RenderDragonFireCharge(Minecraft.getMinecraft().getRenderManager(), true));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceProjectile.class, new RenderNothing(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceCharge.class, new RenderDragonFireCharge(Minecraft.getMinecraft().getRenderManager(), false));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowVillager.class, new RenderSnowVillager(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityHippogryphEgg.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ModItems.hippogryph_egg, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityHippogryph.class, new RenderHippogryph(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneStatue.class, new RenderStoneStatue(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityGorgon.class, new RenderGorgon(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, new RenderPixie(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityCyclops.class, new RenderCyclops(Minecraft.getMinecraft().getRenderManager()));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new RenderPodium());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new RenderLectern());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEggInIce.class, new RenderEggInIce());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPixieHouse.class, new RenderPixieHouse());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJar.class, new RenderJar());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 0, TileEntityPixieHouse.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 1, TileEntityPixieHouse.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 2, TileEntityPixieHouse.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 3, TileEntityPixieHouse.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 4, TileEntityPixieHouse.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 5, TileEntityPixieHouse.class);

	}

	public void spawnParticle(String name, World world, double x, double y, double z, double motX, double motY, double motZ) {
		Particle particle = null;
		if (name.equals("dragonfire")) {
			particle = new ParticleDragonFire(world, x, y, z, motX, motY, motZ);
		}
		if (name.equals("dragonice")) {
			particle = new ParticleDragonIce(world, x, y, z, motX, motY, motZ);
		}
		if (name.equals("blood")) {
			particle = new ParticleBlood(world, x, y, z);
		}
		if (name.equals("if_pixie")) {
			particle = new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
		}
		if (particle != null) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	@Override
	public void openBestiaryGui(ItemStack book) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiBestiary(book));
	}

	public Object getArmorModel(int armorId) {
		switch (armorId) {
			case 0:
				return FIRE_DRAGON_SCALE_ARMOR_MODEL;
			case 1:
				return FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
			case 2:
				return ICE_DRAGON_SCALE_ARMOR_MODEL;
			case 3:
				return ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
		}
		return null;
	}

	public Object getFontRenderer() {
		return this.bestiaryFontRenderer;
	}
}
