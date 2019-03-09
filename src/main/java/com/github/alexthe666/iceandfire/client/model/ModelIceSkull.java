package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelIceSkull extends AdvancedModelBase {
	public AdvancedModelRenderer Head;
	public AdvancedModelRenderer HeadFront;
	public AdvancedModelRenderer HornL;
	public AdvancedModelRenderer HornR;
	public AdvancedModelRenderer HornL3;
	public AdvancedModelRenderer HornR3;
	public AdvancedModelRenderer HornR4;
	public AdvancedModelRenderer HornL4;
	public AdvancedModelRenderer JawBottom;
	public AdvancedModelRenderer HornFront;
	public AdvancedModelRenderer HornL2;
	public AdvancedModelRenderer HornR2;
	public AdvancedModelRenderer Teeth1;
	public AdvancedModelRenderer Teeth2;

	public ModelIceSkull() {

		this.textureWidth = 256;
		this.textureHeight = 128;
		this.HornR2 = new AdvancedModelRenderer(this, 46, 36);
		this.HornR2.setRotationPoint(-0.4F, 0.3F, 3.6F);
		this.HornR2.addBox(-0.01F, -0.8F, -0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornR2, -0.07504915783575616F, 0.0F, 0.0F);
		this.HornL2 = new AdvancedModelRenderer(this, 46, 36);
		this.HornL2.mirror = true;
		this.HornL2.setRotationPoint(-0.4F, 0.3F, 3.6F);
		this.HornL2.addBox(-0.21F, -0.8F, 0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornL2, -0.07504915783575616F, 0.0F, 0.0F);
		this.HornR3 = new AdvancedModelRenderer(this, 37, 29);
		this.HornR3.setRotationPoint(-2.5F, 0.4F, -0.9F);
		this.HornR3.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornR3, -0.17453292519943295F, -0.3141592653589793F, 0.0F);
		this.HornFront = new AdvancedModelRenderer(this, 60, 35);
		this.HornFront.setRotationPoint(0.0F, -1.7F, -5.0F);
		this.HornFront.addBox(-0.5F, -0.4F, -0.4F, 1, 2, 3, 0.0F);
		this.setRotateAngle(HornFront, 0.6373942428283291F, 0.0F, 0.0F);
		this.Head = new AdvancedModelRenderer(this, 1, 100);
		this.Head.mirror = true;
		this.Head.setRotationPoint(0.0F, 21.9F, 2.5F);
		this.Head.addBox(-3.0F, -2.0F, -5.2F, 6, 4, 5, 0.0F);
		this.HornR = new AdvancedModelRenderer(this, 59, 34);
		this.HornR.setRotationPoint(-2.6F, -1.4F, -1.0F);
		this.HornR.addBox(-0.4F, -0.5F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(HornR, 0.17453292519943295F, -0.33161255787892263F, -0.19198621771937624F);
		this.JawBottom = new AdvancedModelRenderer(this, 55, 99);
		this.JawBottom.mirror = true;
		this.JawBottom.setRotationPoint(0.0F, 1.8F, -3.9F);
		this.JawBottom.addBox(-2.0F, -0.6F, -5.5F, 4, 1, 6, 0.0F);
		this.HornL = new AdvancedModelRenderer(this, 59, 34);
		this.HornL.mirror = true;
		this.HornL.setRotationPoint(2.6F, -1.4F, -1.0F);
		this.HornL.addBox(-0.6F, -0.5F, 0.0F, 1, 2, 4, 0.0F);
		this.setRotateAngle(HornL, 0.17453292519943295F, 0.33161255787892263F, 0.19198621771937624F);
		this.Teeth1 = new AdvancedModelRenderer(this, -1, 16);
		this.Teeth1.mirror = true;
		this.Teeth1.setRotationPoint(0.0F, -0.3F, 0.0F);
		this.Teeth1.addBox(-2.01F, -2.1F, -5.51F, 4, 2, 5, 0.0F);
		this.HeadFront = new AdvancedModelRenderer(this, 27, 112);
		this.HeadFront.mirror = true;
		this.HeadFront.setRotationPoint(0.0F, 0.2F, -3.5F);
		this.HeadFront.addBox(-2.0F, -2.1F, -5.7F, 4, 3, 5, 0.0F);
		this.HornR4 = new AdvancedModelRenderer(this, 36, 28);
		this.HornR4.setRotationPoint(-0.9F, -2.0F, -1.4F);
		this.HornR4.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
		this.setRotateAngle(HornR4, 0.40980330836826856F, -0.2617993877991494F, 0.0F);
		this.Teeth2 = new AdvancedModelRenderer(this, -1, 16);
		this.Teeth2.setRotationPoint(0.0F, -0.3F, 0.0F);
		this.Teeth2.addBox(-1.91F, -2.1F, -5.51F, 4, 2, 5, 0.0F);
		this.HornL3 = new AdvancedModelRenderer(this, 37, 29);
		this.HornL3.mirror = true;
		this.HornL3.setRotationPoint(2.4F, 0.4F, -0.8F);
		this.HornL3.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(HornL3, -0.17453292519943295F, 0.3141592653589793F, 0.0F);
		this.HornL4 = new AdvancedModelRenderer(this, 36, 28);
		this.HornL4.mirror = true;
		this.HornL4.setRotationPoint(0.9F, -2.0F, -1.4F);
		this.HornL4.addBox(-0.4F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
		this.setRotateAngle(HornL4, 0.40980330836826856F, 0.2617993877991494F, 0.0F);
		this.HornR.addChild(this.HornR2);
		this.HornL.addChild(this.HornL2);
		this.Head.addChild(this.HornR3);
		this.HeadFront.addChild(this.HornFront);
		this.Head.addChild(this.HornR);
		this.Head.addChild(this.JawBottom);
		this.Head.addChild(this.HornL);
		this.JawBottom.addChild(this.Teeth1);
		this.Head.addChild(this.HeadFront);
		this.Head.addChild(this.HornR4);
		this.JawBottom.addChild(this.Teeth2);
		this.Head.addChild(this.HornL3);
		this.Head.addChild(this.HornL4);
		this.updateDefaultPose();

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.resetToDefaultPose();
		if(entity instanceof EntityDragonSkull){
			EntityDragonSkull skull = (EntityDragonSkull)entity;
			this.Head.rotateAngleX = skull.isOnWall() ? (float)Math.toRadians(50F) : 0;
			this.Head.rotationPointZ = skull.isOnWall() ? 0.75F : 2.5F;
		}
		this.Head.render(f5);
	}

	public void setRotateAngle(AdvancedModelRenderer AdvancedModelRenderer, float x, float y, float z) {
		AdvancedModelRenderer.rotateAngleX = x;
		AdvancedModelRenderer.rotateAngleY = y;
		AdvancedModelRenderer.rotateAngleZ = z;
	}
}
