package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonEyes;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

	public RenderDragonBase(RenderManager renderManager, ModelBase model, boolean fire) {
		super(renderManager, model, 0.8F);
		this.addLayer(new LayerDragonEyes(this));
		this.layerRenderers.add(new LayerDragonArmor(this, 0));
		this.layerRenderers.add(new LayerDragonArmor(this, 1));
		this.layerRenderers.add(new LayerDragonArmor(this, 2));
		this.layerRenderers.add(new LayerDragonArmor(this, 3));
	}

	@Override
	protected void preRenderCallback(EntityDragonBase entity, float f) {
		this.shadowSize = ((EntityDragonBase) entity).getRenderSize() / 3;
		GL11.glScalef(shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBase entity) {
		return EnumDragonTextures.getTextureFromDragon(entity);
	}

}
