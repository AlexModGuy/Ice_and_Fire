package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EventNewMenu {

	public static ResourceLocation[] panorama = new ResourceLocation[]{new ResourceLocation("iceandfire:textures/gui/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};

	@SubscribeEvent
	public void openMainMenu(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			GuiMainMenu mainMenu = (GuiMainMenu) event.getGui();
			Field field = ReflectionHelper.findField(GuiMainMenu.class, ObfuscationReflectionHelper.remapFieldNames(GuiMainMenu.class.getName(), new String[]{"MINECRAFT_TITLE_TEXTURES", "field_73978_o"}));
			try {
				Field modifier = Field.class.getDeclaredField("modifiers");
				modifier.setAccessible(true);
				modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				field.set(mainMenu, panorama);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}