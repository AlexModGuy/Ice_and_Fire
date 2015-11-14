package com.github.alexthe666.iceandfire.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EventNewMenu
{
    public static final String[] titlePanoramaPaths = new String[]{"titlePanoramaPaths", "field_73978_o"};
    public static final String[] splashText = new String[]{"splashText", "field_73975_c"};

    public static ResourceLocation[] panorama = new ResourceLocation[]{
        new ResourceLocation("iceandfire:textures/gui/panorama_0.png"),
        new ResourceLocation("textures/gui/title/background/panorama_1.png"),
        new ResourceLocation("textures/gui/title/background/panorama_2.png"),
        new ResourceLocation("textures/gui/title/background/panorama_3.png"),
        new ResourceLocation("textures/gui/title/background/panorama_4.png"),
        new ResourceLocation("textures/gui/title/background/panorama_5.png")};

    @SubscribeEvent
    public void openMainMenu(GuiOpenEvent event)
    {
    	
        if (event.gui instanceof GuiMainMenu)
        {
            GuiMainMenu mainMenu = (GuiMainMenu) event.gui;
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