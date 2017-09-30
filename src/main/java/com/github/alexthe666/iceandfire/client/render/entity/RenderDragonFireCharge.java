package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDragonFireCharge extends Render {

	public boolean isFire;

	public RenderDragonFireCharge(RenderManager renderManager, boolean isFire) {
		super(renderManager);
		this.isFire = isFire;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	//Todo: Make sure pulling partialTicks did not screw things up. This will come when the build runs.
	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		this.bindEntityTexture(entity);
		GlStateManager.rotate(entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate(-0.5F, 0F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness(isFire ? Blocks.MAGMA.getDefaultState() : Blocks.PACKED_ICE.getDefaultState(), entity.getBrightness(partialTicks));
		GlStateManager.translate(-1.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
