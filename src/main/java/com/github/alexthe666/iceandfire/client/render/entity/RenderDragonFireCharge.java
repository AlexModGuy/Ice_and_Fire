package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;

public class RenderDragonFireCharge extends Render {

	public boolean isFire;

	public RenderDragonFireCharge (RenderManager renderManager, boolean isFire) {
		super (renderManager);
		this.isFire = isFire;
	}

	@Override
	protected ResourceLocation getEntityTexture (Entity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public void doRender (Entity entity, double x, double y, double z, float yee, float partialTicks) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft ().getBlockRendererDispatcher ();
		GL11.glPushMatrix ();
		GL11.glTranslated (x, y, z);
		this.bindEntityTexture (entity);
		GlStateManager.rotate (entity.ticksExisted * 7, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate (-0.5F, 0F, 0.5F);
		blockrendererdispatcher.renderBlockBrightness (isFire ? Blocks.MAGMA.getDefaultState () : Blocks.PACKED_ICE.getDefaultState (), entity.getBrightness (partialTicks));
		GlStateManager.translate (-1.0F, 0.0F, 1.0F);
		GL11.glPopMatrix ();
	}

}
