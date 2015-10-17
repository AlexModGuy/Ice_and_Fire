package com.github.alexthe666.iceandfire.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class RenderDragonBase extends RenderLiving {

	public RenderDragonBase(RenderManager renderManager, ModelBase model) {
		super(renderManager, model, 0.5F);
	}

	protected void preRenderCallback(EntityLivingBase entity, float f) {
		
		if(entity instanceof EntityDragonBase){
			GL11.glScalef(((EntityDragonBase)entity).getDragonSize(), ((EntityDragonBase)entity).getDragonSize(), ((EntityDragonBase)entity).getDragonSize());
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		if(entity instanceof EntityDragonBase){
			return new ResourceLocation(((EntityDragonBase)entity).getTexture());
		}else{
			return new ResourceLocation("iceandfire:textures/models/firedragon/red_4");
		}
	}

}
