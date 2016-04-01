package com.github.alexthe666.iceandfire.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class EventNewMenu
{
	public static final String[] titlePanoramaPaths = new String[]{"titlePanoramaPaths", "field_73978_o"};
	public static final String[] splashText = new String[]{"splashText", "field_73975_c"};
	public static final String[] thirdPersonDistanceNames = new String[]{"thirdPersonDistance", "field_78490_B"};
	public static ResourceLocation[] panorama = new ResourceLocation[]{
		new ResourceLocation("iceandfire:textures/gui/panorama_0.png"),
		new ResourceLocation("textures/gui/title/background/panorama_1.png"),
		new ResourceLocation("textures/gui/title/background/panorama_2.png"),
		new ResourceLocation("textures/gui/title/background/panorama_3.png"),
		new ResourceLocation("textures/gui/title/background/panorama_4.png"),
		new ResourceLocation("textures/gui/title/background/panorama_5.png")};

	@SubscribeEvent
	public void onPlayerRenderPre(RenderPlayerEvent.Pre event)
	{
		boolean b = event.getEntityPlayer().getRidingEntity() != null && event.getEntityPlayer().getRidingEntity() instanceof EntityDragonBase;
		if(event.getEntityPlayer() == Minecraft.getMinecraft().thePlayer){
				EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
				Field thirdPersonDistanceField = ReflectionHelper.findField(EntityRenderer.class, ObfuscationReflectionHelper.remapFieldNames(EntityRenderer.class.getName(), thirdPersonDistanceNames));
				float thirdPersonDistance = 4 + (b ? ((EntityDragonBase)event.getEntityPlayer().getRidingEntity()).getDragonSize() : 0);
				try
				{
					Field modifier = Field.class.getDeclaredField("modifiers");
					modifier.setAccessible(true);
					modifier.setInt(thirdPersonDistanceField, thirdPersonDistanceField.getModifiers() & ~Modifier.FINAL);
					thirdPersonDistanceField.set(renderer, thirdPersonDistance);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	@SubscribeEvent
	public void openMainMenu(GuiOpenEvent event)
	{

		if (event.getGui() instanceof GuiMainMenu)
		{
			GuiMainMenu mainMenu = (GuiMainMenu) event.getGui();
			Field field = ReflectionHelper.findField(GuiMainMenu.class, ObfuscationReflectionHelper.remapFieldNames(GuiMainMenu.class.getName(), titlePanoramaPaths));
			try
			{
				Field modifier = Field.class.getDeclaredField("modifiers");
				modifier.setAccessible(true);
				modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				field.set(mainMenu, panorama);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}