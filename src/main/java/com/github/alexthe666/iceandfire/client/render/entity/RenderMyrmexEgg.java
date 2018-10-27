package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMyrmexEgg extends RenderLiving<EntityMyrmexEgg> {

	public static final ResourceLocation EGG_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_egg.png");
	public static final ResourceLocation EGG_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_egg.png");

	public RenderMyrmexEgg(RenderManager renderManager) {
		super(renderManager, new ModelDragonEgg(), 0.3F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMyrmexEgg entity) {
		return entity.isJungle() ? EGG_JUNGLE : EGG_DESERT;
	}

}
