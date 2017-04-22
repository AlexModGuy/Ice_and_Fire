package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class RenderNothing extends Render {

	public RenderNothing (RenderManager renderManager) {
		super (renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture (Entity entity) {
		return null;
	}

	@Override
	public void doRender (Entity entity, double x, double y, double z, float yee, float partialTicks) {

	}

}
