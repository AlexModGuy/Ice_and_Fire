package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTroll extends RenderLiving<EntityTroll> {

	public RenderTroll(RenderManager renderManager) {
		super(renderManager, new ModelTroll(), 1.1F);
		this.layerRenderers.add(new LayerTrollWeapon(this));
	}

	@Override
	public void preRenderCallback(EntityTroll entitylivingbaseIn, float partialTickTime) {
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTroll troll) {
		return troll.getType().TEXTURE;
	}

}
