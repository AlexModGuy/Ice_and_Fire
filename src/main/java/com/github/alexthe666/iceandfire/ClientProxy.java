package com.github.alexthe666.iceandfire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonArrow;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonSkull;
import com.github.alexthe666.iceandfire.client.render.entity.RenderNothing;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;

public class ClientProxy extends CommonProxy{

	public void render(){
		renderItems();
		renderEntities();
	}
	private void renderEntities() {	
		RenderingRegistry.registerEntityRenderingHandler(EntityFireDragon.class, new RenderDragonBase(Minecraft.getMinecraft().getRenderManager(), new ModelFireDragon()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonEgg.class, new RenderDragonEgg(Minecraft.getMinecraft().getRenderManager(), new ModelDragonEgg()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonArrow.class, new RenderDragonArrow(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonSkull.class, new RenderDragonSkull(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonFire.class, new RenderNothing(Minecraft.getMinecraft().getRenderManager()));

	}
	public void renderItems(){
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.goldPile), 0, new ModelResourceLocation("iceandfire:goldpile", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.silverOre), 0, new ModelResourceLocation("iceandfire:silver_ore", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.sapphireOre), 0, new ModelResourceLocation("iceandfire:sapphire_ore", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.silverBlock), 0, new ModelResourceLocation("iceandfire:silver_block", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.sapphireBlock), 0, new ModelResourceLocation("iceandfire:sapphire_block", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedDirt), 0, new ModelResourceLocation("iceandfire:chared_dirt", "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedGrass), 0, new ModelResourceLocation("iceandfire:chared_grass", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.sapphireGem, 0, new ModelResourceLocation("iceandfire:sapphire_gem", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silverIngot, 0, new ModelResourceLocation("iceandfire:silver_ingot", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silverNugget, 0, new ModelResourceLocation("iceandfire:silver_nugget", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_helmet, 0, new ModelResourceLocation("iceandfire:armor_silverMetal_helmet", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_chestplate, 0, new ModelResourceLocation("iceandfire:armor_silverMetal_chestplate", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_leggings, 0, new ModelResourceLocation("iceandfire:armor_silverMetal_leggings", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_boots, 0, new ModelResourceLocation("iceandfire:armor_silverMetal_boots", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_sword, 0, new ModelResourceLocation("iceandfire:silver_sword", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_shovel, 0, new ModelResourceLocation("iceandfire:silver_shovel", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_pickaxe, 0, new ModelResourceLocation("iceandfire:silver_pickaxe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_axe, 0, new ModelResourceLocation("iceandfire:silver_axe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.silver_hoe, 0, new ModelResourceLocation("iceandfire:silver_hoe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_red, 0, new ModelResourceLocation("iceandfire:dragonegg_red", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_green, 0, new ModelResourceLocation("iceandfire:dragonegg_green", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_bronze, 0, new ModelResourceLocation("iceandfire:dragonegg_bronze", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_gray, 0, new ModelResourceLocation("iceandfire:dragonegg_gray", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_blue, 0, new ModelResourceLocation("iceandfire:dragonegg_blue", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_white, 0, new ModelResourceLocation("iceandfire:dragonegg_white", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_sapphire, 0, new ModelResourceLocation("iceandfire:dragonegg_sapphire", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonegg_silver, 0, new ModelResourceLocation("iceandfire:dragonegg_silver", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_red, 0, new ModelResourceLocation("iceandfire:dragonscales_red", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_green, 0, new ModelResourceLocation("iceandfire:dragonscales_green", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_bronze, 0, new ModelResourceLocation("iceandfire:dragonscales_bronze", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_gray, 0, new ModelResourceLocation("iceandfire:dragonscales_gray", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_blue, 0, new ModelResourceLocation("iceandfire:dragonscales_blue", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_white, 0, new ModelResourceLocation("iceandfire:dragonscales_white", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_sapphire, 0, new ModelResourceLocation("iceandfire:dragonscales_sapphire", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonscales_silver, 0, new ModelResourceLocation("iceandfire:dragonscales_silver", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone, 0, new ModelResourceLocation("iceandfire:dragonbone", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.witherbone, 0, new ModelResourceLocation("iceandfire:witherbone", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.wither_shard, 0, new ModelResourceLocation("iceandfire:wither_shard", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_sword, 0, new ModelResourceLocation("iceandfire:dragonbone_sword", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_shovel, 0, new ModelResourceLocation("iceandfire:dragonbone_shovel", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_pickaxe, 0, new ModelResourceLocation("iceandfire:dragonbone_pickaxe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_axe, 0, new ModelResourceLocation("iceandfire:dragonbone_axe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_hoe, 0, new ModelResourceLocation("iceandfire:dragonbone_hoe", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_arrow, 0, new ModelResourceLocation("iceandfire:dragonbone_arrow", "inventory"));
		ModelBakery.addVariantName(ModItems.dragonbone_bow, new String[] {"iceandfire:dragonbone_bow", "iceandfire:dragonbone_bow_pulling_0",
				"iceandfire:dragonbone_bow_pulling_1", "iceandfire:dragonbone_bow_pulling_2"});
		renderItem.getItemModelMesher().register(ModItems.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragonbone_bow, 1, new ModelResourceLocation("iceandfire:dragonbone_bow_pulling_0", "inventory"));
		ModelBakery.addVariantName(ModItems.dragon_skull, new String[] {"iceandfire:dragon_skull_fire", "iceandfire:dragon_skull_ice"});
		renderItem.getItemModelMesher().register(ModItems.dragonbone_bow, 2, new ModelResourceLocation("iceandfire:dragonbone_bow_pulling_1", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragon_skull, 0, new ModelResourceLocation("iceandfire:dragon_skull_fire", "inventory"));
		renderItem.getItemModelMesher().register(ModItems.dragon_skull, 1, new ModelResourceLocation("iceandfire:dragon_skull_ice", "inventory"));

	}
	public void renderArmors(EnumDragonArmor armor){
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(armor.helmet, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_helmet", "inventory"));
		renderItem.getItemModelMesher().register(armor.chestplate, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_chestplate", "inventory"));
		renderItem.getItemModelMesher().register(armor.leggings, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_leggings", "inventory"));
		renderItem.getItemModelMesher().register(armor.boots, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_boots", "inventory"));
	}
}

