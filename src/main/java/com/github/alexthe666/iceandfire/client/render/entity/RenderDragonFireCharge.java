package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.github.alexthe666.iceandfire.entity.EntityDragonFireCharge;

public class RenderDragonFireCharge extends Render<EntityDragonFireCharge> {

	public RenderDragonFireCharge(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonFireCharge entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender(EntityDragonFireCharge entity, double x, double y, double z, float yee, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		this.bindEntityTexture(entity);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate(-0.5F, 0F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness(Blocks.field_189877_df.getDefaultState(), entity.getBrightness(partialTicks));
		GlStateManager.translate(0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
