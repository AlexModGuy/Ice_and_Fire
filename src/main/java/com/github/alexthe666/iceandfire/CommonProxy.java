package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy {

	public void preRender() {

	}

	public void render() {
	}

	public void postRender() {
	}

	public void spawnParticle(String name, World world, double x, double y, double z, double motX, double motY, double motZ) {
	}

	public void openBestiaryGui(ItemStack book) {
	}

	public Object getArmorModel(int armorId) {
		return null;
	}

	public Object getFontRenderer() {
		return null;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		try {
			for (Field f : ModBlocks.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block) {
					event.getRegistry().register((Block) obj);
				} else if (obj instanceof Block[]) {
					for (Block block : (Block[]) obj) {
						event.getRegistry().register(block);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		try {
			for (Field f : ModBlocks.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block) {
					ItemBlock itemBlock = new ItemBlock((Block)obj);
					itemBlock.setRegistryName(((Block)obj).getRegistryName());
					event.getRegistry().register(itemBlock);
				} else if (obj instanceof Block[]) {
					for (Block block : (Block[]) obj) {
						ItemBlock itemBlock = new ItemBlock(block);
						itemBlock.setRegistryName(block.getRegistryName());
						event.getRegistry().register(itemBlock);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		try {
			for (Field f : ModItems.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Item) {
					event.getRegistry().register((Item) obj);
				} else if (obj instanceof Item[]) {
					for (Item item : (Item[]) obj) {
						event.getRegistry().register(item);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		for (EnumDragonArmor armor : EnumDragonArmor.values()) {
			event.getRegistry().register(armor.helmet);
			event.getRegistry().register(armor.chestplate);
			event.getRegistry().register(armor.leggings);
			event.getRegistry().register(armor.boots);
		}
	}
}
