package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerMyrmexItem;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMyrmexBase extends RenderLiving<EntityMyrmexBase> {

	public RenderMyrmexBase(RenderManager renderManager, ModelBase model, float shadowSize) {
		super(renderManager, model, shadowSize);
		this.addLayer(new LayerMyrmexItem(this));
	}

	@Override
	public void preRenderCallback(EntityMyrmexBase myrmex, float partialTickTime) {
		float scale = myrmex.getModelScale();
		if(myrmex.getGrowthStage() == 0){
			scale /= 2;
		}
		if(myrmex.getGrowthStage() == 1){
			scale /= 1.5F;
		}
		GL11.glScalef(scale, scale, scale);
		if(myrmex.isRiding() && myrmex.getGrowthStage() < 2){
			GL11.glRotatef(90, 0, 1, 0);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMyrmexBase myrmex) {
		return myrmex.getTexture();
	}

}
