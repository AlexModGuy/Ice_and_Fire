package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderFireDragon extends RenderLiving<EntityDragonBase> {

	public RenderFireDragon(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.8F);
	}

	@Override
	protected void preRenderCallback(EntityDragonBase entity, float f) {
		this.shadowSize = ((EntityDragonBase) entity).getRenderSize() / 3;
		GL11.glScalef(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
		return new ResourceLocation(((EntityDragonBase) entity).getTexture() + ".png");
	}

}
