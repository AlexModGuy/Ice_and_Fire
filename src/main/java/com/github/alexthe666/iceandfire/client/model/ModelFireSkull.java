package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFireSkull extends ModelBase {
	public ModelRenderer Head;
	public ModelRenderer HeadFront;
	public ModelRenderer Jaw;
	public ModelRenderer HornR;
	public ModelRenderer HornL;
	public ModelRenderer HornR3;
	public ModelRenderer HornL3;
	public ModelRenderer Teeth1;
	public ModelRenderer Teeth2;
	public ModelRenderer HornR2;
	public ModelRenderer HornL2;

	public ModelFireSkull() {
		this.textureWidth = 256;
		this.textureHeight = 128;
		this.HornR2 = new ModelRenderer(this, 46, 36);
		this.HornR2.setRotationPoint(-0.6F, 0.3F, 3.6F);
		this.HornR2.addBox(0.01F, -0.8F, 0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornR2, -0.07504915783575616F, 0.0F, 0.0F);
		this.HornL2 = new ModelRenderer(this, 46, 36);
		this.HornL2.mirror = true;
		this.HornL2.setRotationPoint(-0.4F, 0.3F, 3.6F);
		this.HornL2.addBox(-0.01F, -0.8F, -0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornL2, -0.07504915783575616F, 0.0F, 0.0F);
		this.HornR = new ModelRenderer(this, 59, 34);
		this.HornR.setRotationPoint(-0.8F, -2.5F, -1.0F);
		this.HornR.addBox(-0.6F, -0.5F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(HornR, 0.3141592653589793F, -0.33161255787892263F, -0.19198621771937624F);
		this.HornR3 = new ModelRenderer(this, 36, 28);
		this.HornR3.setRotationPoint(-1.0F, -0.8F, -0.8F);
		this.HornR3.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
		this.setRotateAngle(HornR3, -0.05235987755982988F, -0.3141592653589793F, 0.0F);
		this.Teeth1 = new ModelRenderer(this, 6, 16);
		this.Teeth1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Teeth1.addBox(-1.6F, 0.1F, -8.9F, 2, 1, 5, 0.0F);
		this.Head = new ModelRenderer(this, 6, 54);
		this.Head.setRotationPoint(0.0F, 23.0F, 2.5F);
		this.Head.addBox(-1.5F, -3.0F, -4.0F, 4, 4, 4, 0.0F);
		this.Teeth2 = new ModelRenderer(this, 6, 16);
		this.Teeth2.mirror = true;
		this.Teeth2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Teeth2.addBox(-0.4F, 0.1F, -8.9F, 2, 1, 5, 0.0F);
		this.HornL3 = new ModelRenderer(this, 36, 28);
		this.HornL3.mirror = true;
		this.HornL3.setRotationPoint(1.9F, -0.8F, -0.8F);
		this.HornL3.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
		this.setRotateAngle(HornL3, -0.05235987755982988F, 0.3141592653589793F, 0.0F);
		this.HeadFront = new ModelRenderer(this, 6, 44);
		this.HeadFront.setRotationPoint(0.5F, 0.0F, 0.0F);
		this.HeadFront.addBox(-1.5F, -2.8F, -8.8F, 3, 3, 5, 0.0F);
		this.setRotateAngle(HeadFront, -0.03237238967054832F, -0.0F, 0.0F);
		this.Jaw = new ModelRenderer(this, 34, 56);
		this.Jaw.setRotationPoint(0.5F, 0.4F, -3.3F);
		this.Jaw.addBox(-1.5F, -0.4F, -5.5F, 3, 1, 5, 0.0F);
		this.HornL = new ModelRenderer(this, 59, 34);
		this.HornL.mirror = true;
		this.HornL.setRotationPoint(1.8F, -2.5F, -1.0F);
		this.HornL.addBox(-0.4F, -0.5F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(HornL, 0.3141592653589793F, 0.33161255787892263F, 0.19198621771937624F);
		this.HornR.addChild(this.HornR2);
		this.HornL.addChild(this.HornL2);
		this.Head.addChild(this.HornR);
		this.Head.addChild(this.HornR3);
		this.HeadFront.addChild(this.Teeth1);
		this.HeadFront.addChild(this.Teeth2);
		this.Head.addChild(this.HornL3);
		this.Head.addChild(this.HeadFront);
		this.Head.addChild(this.Jaw);
		this.Head.addChild(this.HornL);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity instanceof EntityDragonSkull && ((EntityDragonSkull) entity).getType() == 1) {
			RenderDragonSkull.ICE_SKULL_MODEL.Head.render(f5);
		}else{
			this.Head.render(f5);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
