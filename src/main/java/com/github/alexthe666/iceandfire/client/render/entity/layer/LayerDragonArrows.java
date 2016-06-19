package com.github.alexthe666.iceandfire.client.render.entity.layer;

import java.util.Random;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

@SideOnly(Side.CLIENT)
public class LayerDragonArrows implements LayerRenderer<EntityDragonBase> {
	private final RenderLivingBase<?> renderer;

	public LayerDragonArrows(RenderLivingBase<?> rendererIn) {
		this.renderer = rendererIn;
	}

	public void doRenderLayer(EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		int i = dragon.getArrowCountInEntity();
		if (i > 0) {
			Entity entity = new EntityTippedArrow(dragon.worldObj, dragon.posX, dragon.posY, dragon.posZ);
			Random random = new Random((long) dragon.getEntityId());
			for (int j = 0; j < i; ++j) {
				ModelRenderer modelrenderer = this.renderer.getMainModel().getRandomModelBox(random);
				ModelBox modelbox = (ModelBox) modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
				GlStateManager.pushMatrix();
				GL11.glScalef(1 / dragon.getRenderSize(), 1 / dragon.getRenderSize(), 1 / dragon.getRenderSize());
				GlStateManager.pushMatrix();
				modelrenderer.postRender(0.0625F);
				float f = random.nextFloat();
				float f1 = random.nextFloat();
				float f2 = random.nextFloat();
				float f3 = (modelbox.posX1) + (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16;
				float f4 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f1) / 16;
				float f5 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f2) / 16;
				GlStateManager.translate(f3, f4, f5);
				f = f * 2.0F - 1.0F;
				f1 = f1 * 2.0F - 1.0F;
				f2 = f2 * 2.0F - 1.0F;
				f = f * -1.0F;
				f1 = f1 * -1.0F;
				f2 = f2 * -1.0F;
				float f6 = MathHelper.sqrt_float(f * f + f2 * f2);
				entity.prevRotationYaw = entity.rotationYaw = (float) (Math.atan2((double) f, (double) f2) * (180D / Math.PI));
				entity.prevRotationPitch = entity.rotationPitch = (float) (Math.atan2((double) f1, (double) f6) * (180D / Math.PI));
				double d0 = 0.0D;
				double d1 = 0.0D;
				double d2 = 0.0D;
				this.renderer.getRenderManager().doRenderEntity(entity, d0, d1, d2, 0.0F, dragon.getRenderSize(), false);
				GlStateManager.popMatrix();
				GlStateManager.popMatrix();
			}

		}
	}
	
	public boolean getActualModelPosX1(ModelBox box){
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}