package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

	public RenderDragonBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.8F);
		// this.layerRenderers.add(new LayerDragonArmor(this, 1, "firedragon"));
		// this.layerRenderers.add(new LayerDragonArmor(this, 2, "firedragon"));
		// this.layerRenderers.add(new LayerDragonArmor(this, 3, "firedragon"));
		// this.layerRenderers.add(new LayerDragonArmor(this, 4, "firedragon"));
	}

	@Override
	protected void preRenderCallback(EntityDragonBase entity, float f) {
		this.shadowSize = ((EntityDragonBase) entity).getRenderSize();
		GL11.glScalef(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
		return new ResourceLocation(((EntityDragonBase) entity).getTexture() + ".png");
	}

}
