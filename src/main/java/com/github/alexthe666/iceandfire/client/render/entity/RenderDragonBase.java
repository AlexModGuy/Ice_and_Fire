package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.render.entity.layer.*;
import com.github.alexthe666.iceandfire.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;

public class RenderDragonBase extends RenderLiving<EntityDragonBase> {

	public RenderDragonBase (RenderManager renderManager, ModelBase model) {
		super (renderManager, model, 0.8F);
		this.addLayer (new LayerDragonEyes (this));
		this.layerRenderers.add (new LayerDragonArmor (this, 0, model instanceof ModelFireDragon ? model instanceof ModelFireDragon ? "firedragon" : "icedragon" : "icedragon"));
		this.layerRenderers.add (new LayerDragonArmor (this, 1, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));
		this.layerRenderers.add (new LayerDragonArmor (this, 2, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));
		this.layerRenderers.add (new LayerDragonArmor (this, 3, model instanceof ModelFireDragon ? "firedragon" : "icedragon"));

	}

	@Override
	protected void preRenderCallback (EntityDragonBase entity, float f) {
		this.shadowSize = ((EntityDragonBase) entity).getRenderSize () / 3;
		GL11.glScalef (shadowSize, shadowSize, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture (EntityDragonBase entity) {
		return new ResourceLocation (((EntityDragonBase) entity).getTexture () + ".png");
	}

}
