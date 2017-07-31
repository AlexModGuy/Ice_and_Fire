package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class EventClient {

    public static void initializeStoneLayer() {
        for(Map.Entry< Class <? extends Entity > , Render <? extends Entity >> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()){
            Render render = entry.getValue();
            if(render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())){
                ((RenderLivingBase)render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
                ((RenderLivingBase)render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));

            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
    }

}
