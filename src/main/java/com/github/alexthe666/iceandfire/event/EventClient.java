package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.SirenEntityProperties;
import net.ilexiconn.llibrary.client.event.PlayerViewDistanceEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

public class EventClient {

	public static void initializeStoneLayer() {
		for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
			Render render = entry.getValue();
			if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
				((RenderLivingBase) render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
				((RenderLivingBase) render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));

			}
		}

		Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"INSTANCE", "INSTANCE"}));
		Field entityRendersField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderers", "entityRenderers"}));
		Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderersOld", "entityRenderersOld"}));
		RenderingRegistry registry = null;
		try {
			Field modifier = Field.class.getDeclaredField("modifiers");
			modifier.setAccessible(true);
			registry = (RenderingRegistry) renderingRegistryField.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (registry != null) {
			Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
			Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
			try {
				Field modifier1 = Field.class.getDeclaredField("modifiers");
				modifier1.setAccessible(true);
				entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
				entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(registry);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (entityRenders != null) {
				for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
					if (entry.getValue() != null) {
						Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
						if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
							((RenderLivingBase) render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
							((RenderLivingBase) render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));

						}
					}

				}
			}
			if (entityRendersOld != null) {
				for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
					Render render = entry.getValue();
					if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
						((RenderLivingBase) render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
						((RenderLivingBase) render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));

					}
				}
			}
		}

	}

	@SubscribeEvent
	public void on3rdPersonView(PlayerViewDistanceEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isRiding() && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) player.getRidingEntity();
			float newDistance = (IceAndFire.CONFIG.dragonRiding3rdPersonDistanceModifier * dragon.getRenderSize()) + 4;
			event.setViewDistance(newDistance);
		}
	}

	private Random rand = new Random();

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), SirenEntityProperties.class);
			if (player.world.isRemote && sirenProps != null) {
				EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
				EntitySiren siren = sirenProps.getClosestSiren(event.getEntityLiving().world, event.getEntityLiving());
				if(siren == null || siren.isDead || event.getEntityLiving().getDistance(siren) > 33 || player.isCreative()){
					sirenProps.isCharmed = false;
				}
				if (sirenProps.isCharmed) {
					if (rand.nextInt(40) == 0) {
						IceAndFire.PROXY.spawnParticle("siren_appearance", player.world, player.posX, player.posY, player.posZ, 0, 0, 0);
					}
					if (sirenProps.isCharmed && !renderer.isShaderActive()) {
						renderer.loadShader(new ResourceLocation("iceandfire:shaders/post/siren.json"));
					}

				}
				if (!sirenProps.isCharmed && renderer.isShaderActive()) {
					renderer.stopUseShader();
				}
			}
		}
	}
}