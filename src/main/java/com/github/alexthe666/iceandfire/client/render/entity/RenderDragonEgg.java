package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDragonEgg extends RenderLiving<EntityDragonEgg> {

	public static final ResourceLocation EGG_RED = new ResourceLocation("iceandfire:textures/models/firedragon/egg_red.png");
	public static final ResourceLocation EGG_GREEN = new ResourceLocation("iceandfire:textures/models/firedragon/egg_green.png");
	public static final ResourceLocation EGG_BRONZE = new ResourceLocation("iceandfire:textures/models/firedragon/egg_bronze.png");
	public static final ResourceLocation EGG_GREY = new ResourceLocation("iceandfire:textures/models/firedragon/egg_gray.png");
	public static final ResourceLocation EGG_BLUE = new ResourceLocation("iceandfire:textures/models/icedragon/egg_blue.png");
	public static final ResourceLocation EGG_WHITE = new ResourceLocation("iceandfire:textures/models/icedragon/egg_white.png");
	public static final ResourceLocation EGG_SAPPHIRE = new ResourceLocation("iceandfire:textures/models/icedragon/egg_sapphire.png");
	public static final ResourceLocation EGG_SILVER = new ResourceLocation("iceandfire:textures/models/icedragon/egg_silver.png");

	public RenderDragonEgg(RenderManager renderManager) {
		super(renderManager, new ModelDragonEgg(), 0.3F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonEgg entity) {
		switch(entity.getType()){
			default:
				return EGG_RED;
			case GREEN:
				return EGG_GREEN;
			case BRONZE:
				return EGG_BRONZE;
			case GRAY:
				return EGG_GREY;
			case BLUE:
				return EGG_BLUE;
			case WHITE:
				return EGG_WHITE;
			case SAPPHIRE:
				return EGG_SAPPHIRE;
			case SILVER:
				return EGG_SILVER;

		}
	}

}
