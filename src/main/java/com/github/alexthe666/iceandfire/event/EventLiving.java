package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;

import java.util.*;

public class EventLiving {
	@SubscribeEvent
	public void onEntityDrop (LivingDropsEvent event) {
		if (event.getEntityLiving () instanceof EntitySkeleton) {
			if (((EntitySkeleton) event.getEntityLiving ()).getSkeletonType () == SkeletonType.WITHER) {
				event.getEntityLiving ().dropItem (ModItems.witherbone, event.getEntityLiving ().getRNG ().nextInt (2));
			}
		}
	}

	@SubscribeEvent
	public void onChestGenerated (LootTableLoadEvent event) {
		if (event.getName ().equals (LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			final LootPool pool1 = event.getTable ().getPool ("pool1");
			if (pool1 != null) {
				pool1.addEntry (new LootEntryItem (ModItems.manuscript, 10, 5, new LootFunction[0], new LootCondition[0], "iceandfire:manuscript"));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLeaveEvent (PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.player != null && !event.player.getPassengers ().isEmpty ()) {
			Iterator itr = event.player.getPassengers ().iterator ();
			while (itr.hasNext ()) {
				((Entity) itr.next ()).dismountRidingEntity ();
			}
		}
	}

	@SubscribeEvent
	public void onItemEvent (PlayerEvent.ItemPickupEvent event) {
		if (event.pickedUp.getEntityItem ().getItem () == ModItems.manuscript) {
			event.player.addStat (ModAchievements.manuscript, 1);
		}
		if (event.pickedUp.getEntityItem ().getItem () == Item.getItemFromBlock ((ModBlocks.silverOre))) {
			event.player.addStat (ModAchievements.silver, 1);
		}
		if (event.pickedUp.getEntityItem ().getItem () == Item.getItemFromBlock ((ModBlocks.sapphireOre))) {
			event.player.addStat (ModAchievements.sapphire, 1);
		}
	}

	@SubscribeEvent
	public void onCraftEvent (PlayerEvent.ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		if (event.crafting.getItem () == ModItems.bestiary) {
			player.addStat (ModAchievements.bestiary, 1);
		}
		if (event.crafting.getItem () == ModItems.silver_sword || event.crafting.getItem () == ModItems.silver_pickaxe || event.crafting.getItem () == ModItems.silver_axe || event.crafting.getItem () == ModItems.silver_shovel || event.crafting.getItem () == ModItems.silver_hoe) {
			player.addStat (ModAchievements.silverTool, 1);
		}
		if (event.crafting.getItem () == ModItems.dragon_horn || event.crafting.getItem () == ModItems.dragonbone_bow || event.crafting.getItem () == ModItems.dragonbone_sword || event.crafting.getItem () == ModItems.dragonbone_pickaxe || event.crafting.getItem () == ModItems.dragonbone_axe || event.crafting.getItem () == ModItems.dragonbone_shovel || event.crafting.getItem () == ModItems.dragonbone_hoe) {
			player.addStat (ModAchievements.boneTool, 1);
		}
		if (event.crafting.getItem () instanceof ItemDragonArmor) {
			player.addStat (ModAchievements.dragonArmor, 1);
		}
		if (event.crafting.getItem () == ModItems.dragonbone_sword_fire) {
			player.addStat (ModAchievements.fireSword, 1);
		}
		if (event.crafting.getItem () == ModItems.dragonbone_sword_ice) {
			player.addStat (ModAchievements.iceSword, 1);
		}
	}
}
