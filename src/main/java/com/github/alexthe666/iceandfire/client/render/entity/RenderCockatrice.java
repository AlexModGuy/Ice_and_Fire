package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCockatrice extends RenderLiving<EntityCockatrice> {

	public static final ResourceLocation TEXTURE_ROOSTER = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_0.png");
	public static final ResourceLocation TEXTURE_HEN = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_1.png");

	public RenderCockatrice(RenderManager renderManager) {
		super(renderManager, new ModelCockatrice(), 0.6F);
	}

	@Override
	public void preRenderCallback(EntityCockatrice entitylivingbaseIn, float partialTickTime) {


	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCockatrice cockatrice) {
		return TEXTURE_ROOSTER;
	}

}
