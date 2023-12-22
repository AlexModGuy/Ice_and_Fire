package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.client.IafClientSetup;
import net.minecraft.client.renderer.Sheets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AtlasGenerator {
    @SubscribeEvent
    public static void addTextures(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location() == Sheets.CHEST_SHEET) {
            event.addSprite(IafClientSetup.GHOST_CHEST_LOCATION);
            event.addSprite(IafClientSetup.GHOST_CHEST_LEFT_LOCATION);
            event.addSprite(IafClientSetup.GHOST_CHEST_RIGHT_LOCATION);
        }
    }
}
