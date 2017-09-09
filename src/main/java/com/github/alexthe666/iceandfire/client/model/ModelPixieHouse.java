package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPixieHouse extends ModelBase {
	public ModelRenderer stalk;
	public ModelRenderer cap1;
	public ModelRenderer grass;
	public ModelRenderer grass2;
	public ModelRenderer cap2;
	public ModelRenderer stalk2;

	public ModelPixieHouse() {
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.stalk2 = new ModelRenderer(this, 4, 24);
		this.stalk2.setRotationPoint(-4.4F, -3.1F, 0.8F);
		this.stalk2.addBox(-1.0F, -5.5F, -1.0F, 2, 6, 2, 0.0F);
		this.setRotateAngle(stalk2, 0.091106186954104F, -0.045553093477052F, -1.2292353921796064F);
		this.cap2 = new ModelRenderer(this, 0, 44);
		this.cap2.setRotationPoint(0.0F, -1.9F, 0.0F);
		this.cap2.addBox(-6.0F, -8.0F, -6.0F, 12, 3, 12, 0.0F);
		this.grass = new ModelRenderer(this, 72, 45);
		this.grass.mirror = true;
		this.grass.setRotationPoint(-2.8F, -1.4F, 5.9F);
		this.grass.addBox(-2.1F, -6.5F, -6.9F, 2, 8, 7, 0.0F);
		this.setRotateAngle(grass, 0.0F, 0.5462880558742251F, 0.0F);
		this.cap1 = new ModelRenderer(this, 0, 21);
		this.cap1.setRotationPoint(0.0F, -5.0F, 0.0F);
		this.cap1.addBox(-8.0F, -8.0F, -8.0F, 16, 3, 16, 0.0F);
		this.grass2 = new ModelRenderer(this, 48, 43);
		this.grass2.mirror = true;
		this.grass2.setRotationPoint(4.4F, -1.4F, -6.0F);
		this.grass2.addBox(-0.9F, -6.5F, -6.0F, 3, 8, 8, 0.0F);
		this.setRotateAngle(grass2, 0.0F, -2.6406831582674206F, 0.0F);
		this.stalk = new ModelRenderer(this, 0, 0);
		this.stalk.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.stalk.addBox(-4.5F, -10.0F, -4.5F, 9, 10, 9, 0.0F);
		this.cap1.addChild(this.stalk2);
		this.cap1.addChild(this.cap2);
		this.stalk.addChild(this.grass);
		this.stalk.addChild(this.cap1);
		this.stalk.addChild(this.grass2);
	}

	public void render(float f5) {
		this.stalk.render(f5);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
