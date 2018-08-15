package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.render.entity.ICustomStoneLayer;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import com.github.alexthe666.iceandfire.entity.SirenEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

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
						try{
						Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
						if (render != null && render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
							LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
							LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
							((RenderLivingBase) render).addLayer(stoneLayer);
							((RenderLivingBase) render).addLayer(crackLayer);
						}
						}catch(NullPointerException exp){
							System.out.println("Ice and Fire: Could not apply stone render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
						}
					}

				}
			}
			if (entityRendersOld != null) {
				for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
					Render render = entry.getValue();
					if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
						LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
						LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer)render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
						((RenderLivingBase) render).addLayer(stoneLayer);
						((RenderLivingBase) render).addLayer(crackLayer);
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player.getRidingEntity() != null) {
			if(player.getRidingEntity() instanceof EntityDragonBase){
				int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
				float scale = ((EntityDragonBase) player.getRidingEntity()).getRenderSize() / 3;
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
					if(currentView == 0){
					}else if(currentView == 1){
						GL11.glTranslatef(scale * 0.5F, 0F, -scale * 3F);
					}else if(currentView == 2){
						GL11.glTranslatef(0, 0F, -scale * 3F);
					}else if(currentView == 3){
						GL11.glTranslatef(scale * 0.5F, 0F, -scale * 0.5F);
					}
				}
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
					if(currentView == 0){
					}else if(currentView == 1){
						GL11.glTranslatef(-scale  * 1.2F, 0F, 5);
					}else if(currentView == 2){
						GL11.glTranslatef(scale  * 1.2F, 0F, 5);
					}else if(currentView == 3){
						GL11.glTranslatef(scale * -1.5F, 0F, scale * 1.5F);
					}
				}
			}

		}
	}

	private Random rand = new Random();
	private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (player.world.isRemote && ModKeys.dragon_change_view.isPressed()) {
				int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
				if(currentView + 1 > 3){
					currentView = 0;
				}else{
					currentView++;
				}
				IceAndFire.PROXY.setDragon3rdPersonView(currentView);
			}

			SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), SirenEntityProperties.class);
			if (player.world.isRemote && sirenProps != null) {
				EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
				EntitySiren siren = sirenProps.getClosestSiren(event.getEntityLiving().world, event.getEntityLiving());
				if(siren == null){
					sirenProps.isCharmed = false;
				}
				if (sirenProps.isCharmed) {
					if (rand.nextInt(40) == 0) {
						IceAndFire.PROXY.spawnParticle("siren_appearance", player.world, player.posX, player.posY, player.posZ, 0, 0, 0);
					}

					if (IceAndFire.CONFIG.sirenShader && sirenProps.isCharmed && !renderer.isShaderActive()) {
						renderer.loadShader(SIREN_SHADER);
					}

				}
				if (IceAndFire.CONFIG.sirenShader && !sirenProps.isCharmed && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
					renderer.stopUseShader();
				}
			}
		}
	}

	private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/blocks/frosted_ice_0.png");
	private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/blocks/frosted_ice_1.png");
	private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/blocks/frosted_ice_2.png");
	private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/blocks/frosted_ice_3.png");

	@SubscribeEvent
	public void onPostRenderLiving(RenderLivingEvent.Post event){
		FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), FrozenEntityProperties.class);
		if(frozenProps != null && frozenProps.isFrozen){
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			float sideExpand = 0.25F;
			AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(event.getEntity().getRenderBoundingBox().minX - event.getEntity().posX + event.getX() - sideExpand, event.getEntity().getRenderBoundingBox().minY - event.getEntity().posY + event.getY(), event.getEntity().getRenderBoundingBox().minZ - event.getEntity().posZ + event.getZ() - sideExpand, event.getEntity().getRenderBoundingBox().maxX - event.getEntity().posX + event.getX() + sideExpand, event.getEntity().getRenderBoundingBox().maxY - event.getEntity().posY + event.getY() + sideExpand, event.getEntity().getRenderBoundingBox().maxZ - event.getEntity().posZ + event.getZ() + sideExpand);
			event.getRenderer().bindTexture(getIceTexture(frozenProps.ticksUntilUnfrozen));
			renderAABB(axisalignedbb1, 0, 0, 0);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}

	private static ResourceLocation getIceTexture(int ticksFrozen){
		if(ticksFrozen < 100){
			if(ticksFrozen < 50){
				if(ticksFrozen < 20){
					return TEXTURE_3;
				}
				return TEXTURE_2;
			}
			return TEXTURE_1;
		}
		return TEXTURE_0;
	}

	public static void renderAABB(AxisAlignedBB boundingBox, double x, double y, double z){
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		double maxX = boundingBox.maxX * 0.625F;
		double minX = boundingBox.minX * 0.625F;
		double maxY = boundingBox.maxY * 0.625F;
		double minY = boundingBox.minY * 0.625F;
		double maxZ = boundingBox.maxZ * 0.625F;
		double minZ = boundingBox.minZ * 0.625F;
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();

		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
		tessellator.draw();
	}
}