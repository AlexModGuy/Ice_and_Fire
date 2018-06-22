package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelFireSkull;
import com.github.alexthe666.iceandfire.client.model.ModelIceSkull;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDragonSkull extends RenderLiving {

	public static final ResourceLocation SKULL_FIRE = new ResourceLocation("iceandfire:textures/models/firedragon/fire_skeleton_4.png");
	public static final ResourceLocation SKULL_ICE = new ResourceLocation("iceandfire:textures/models/icedragon/ice_skeleton_4.png");
	public static final ModelFireSkull FIRE_SKULL_MODEL = new ModelFireSkull();
	public static final ModelIceSkull ICE_SKULL_MODEL = new ModelIceSkull();

	public RenderDragonSkull(RenderManager renderManager) {
		super(renderManager, FIRE_SKULL_MODEL, 0.3F);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
		if (par1EntityLivingBase instanceof EntityDragonSkull) {
			GL11.glScalef(((EntityDragonSkull) par1EntityLivingBase).getDragonSize(), 1, ((EntityDragonSkull) par1EntityLivingBase).getDragonSize());
			GL11.glScalef(1, -((EntityDragonSkull) par1EntityLivingBase).getDragonSize(), 1);
			GL11.glRotatef(((EntityDragonSkull) par1EntityLivingBase).getYaw(), 0, 1, 0);
			super.preRenderCallback(par1EntityLivingBase, par2);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		if (entity instanceof EntityDragonSkull) {
			if(((EntityDragonSkull) entity).getType() == 0){
				return SKULL_FIRE;
			}else{
				return SKULL_ICE;
			}
		}
		return null;
	}

}
