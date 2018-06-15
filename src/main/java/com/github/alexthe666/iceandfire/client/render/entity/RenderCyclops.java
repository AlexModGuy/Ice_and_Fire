package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCyclops;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCyclops extends RenderLiving<EntityCyclops> {

	public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops.png");
	public static final ResourceLocation BLINK_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_blink.png");
	public static final ResourceLocation BLINDED_TEXTURE = new ResourceLocation("iceandfire:textures/models/cyclops/cyclops_injured.png");

	public RenderCyclops(RenderManager renderManager) {
		super(renderManager, new ModelCyclops(), 1.6F);
	}

	@Override
	public void preRenderCallback(EntityCyclops entitylivingbaseIn, float partialTickTime) {
		GL11.glScalef(2.25F, 2.25F, 2.25F);

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCyclops cyclops) {
		if(cyclops.isBlinded()){
			return BLINDED_TEXTURE;
		}else if(cyclops.isBlinking()){
			return BLINK_TEXTURE;
		}else{
			return TEXTURE;
		}
	}

}
