package com.github.alexthe666.iceandfire.client.model;

import org.lwjgl.opengl.GL11;

import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelBase;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.minecraft.entity.Entity;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;

public class ModelDragonEgg extends MowzieModelBase {
	
    public MowzieModelRenderer Egg1;
    public MowzieModelRenderer Egg2;
    public MowzieModelRenderer Egg3;
    public MowzieModelRenderer Egg4;

    public ModelDragonEgg() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Egg3 = new MowzieModelRenderer(this, 0, 0);
        this.Egg3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Egg3.addBox(-2.5F, -4.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg2 = new MowzieModelRenderer(this, 22, 2);
        this.Egg2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Egg2.addBox(-2.5F, -0.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg1 = new MowzieModelRenderer(this, 0, 12);
        this.Egg1.setRotationPoint(0.0F, 19.6F, 0.0F);
        this.Egg1.addBox(-3.0F, -2.8F, -3.0F, 6, 6, 6, 0.0F);
        this.Egg4 = new MowzieModelRenderer(this, 28, 16);
        this.Egg4.setRotationPoint(0.0F, -0.9F, 0.0F);
        this.Egg4.addBox(-2.0F, -4.8F, -2.0F, 4, 4, 4, 0.0F);
        this.Egg1.addChild(this.Egg3);
        this.Egg1.addChild(this.Egg2);
        this.Egg3.addChild(this.Egg4);
        ModelUtils.doMowzieStuff(boxList, false);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        ModelUtils.renderAll(boxList);
    }

    public void renderPodium() {
        Egg1.rotateAngleX = (float)Math.toRadians(-180);
        ModelUtils.renderAll(boxList);

    }
    
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		ModelUtils.doMowzieStuff(boxList, true);
		EntityDragonEgg dragon = (EntityDragonEgg)entity;
		this.walk(Egg1, 0.3F, 0.1F, true, 1, 0, entity.ticksExisted, 1);
		this.flap(Egg1, 0.3F, 0.1F, false, 0, 0, entity.ticksExisted, 1);

	}
}
