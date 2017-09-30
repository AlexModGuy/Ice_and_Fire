package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import net.minecraft.item.Item;
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

	public void renderArmors(EnumDragonArmor armor) {
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
	public static void registerItems(RegistryEvent.Register<Item> event) {
		try {
			for (Field f : ModItems.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Item) {
					event.getRegistry().register((Item) obj);
					ModItems.ITEMS.add((Item) obj);
				} else if (obj instanceof Item[]) {
					for (Item item : (Item[]) obj) {
						event.getRegistry().register(item);
						ModItems.ITEMS.add(item);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
