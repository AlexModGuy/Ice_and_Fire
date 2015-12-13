package com.github.alexthe666.iceandfire.client.model;

import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelBase;
import net.minecraft.entity.Entity;

import com.github.alexthe666.iceandfire.animation.AnimationBlend;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public abstract class ModelDragonBase extends MowzieModelBase{

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		EntityDragonBase dragon = (EntityDragonBase)entity;
		
		if(dragon.getAnimation().animationId == 0){
				if(!dragon.onGround){
					flightPose(false);
				}else{
					if(dragon.getSleeping() == 1){
						sleepPose(false);
					}else{
						normalPose(false);
					}
				}
		}
		if(dragon.getAnimation() instanceof AnimationBlend){
			if(!((AnimationBlend)dragon.getAnimation()).blend){
				if(!dragon.onGround){
					flightPose(false);
				}else{
					if(dragon.getSleeping() == 1){
						sleepPose(false);
					}else{
						normalPose(false);
					}
				}
			}
		}
	}
	public void caryOutPoses(){
		
	}
	public abstract void normalPose(boolean animate);

	public abstract void flightPose(boolean animate);

	public abstract void sitPose(boolean animate);

	public abstract void sleepPose(boolean animate);

	public abstract void hoverPose(boolean animate);

	public abstract void deadPose(boolean animate);


}
