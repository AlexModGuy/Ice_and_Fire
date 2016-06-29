package com.github.alexthe666.iceandfire.client.model;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public abstract class ModelDragonBase extends AdvancedModelBase {


    public void setRotateAngle(AdvancedModelRenderer model, float x, float y, float z) {
    	model.rotateAngleX = x;
    	model.rotateAngleY = y;
    	model.rotateAngleZ = z;
    }
    
    public void rotate(ModelAnimator animator, AdvancedModelRenderer model, float x, float y, float z) {
    	animator.rotate(model, (float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
    }
    
    public void progressRotation(AdvancedModelRenderer model, float progress, float rotX, float rotY, float rotZ) {
    	model.rotateAngleX += progress * (rotX - model.defaultRotationX) / 20.0F;
    	model.rotateAngleY += progress * (rotY - model.defaultRotationY) / 20.0F;
    	model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / 20.0F;
    }

    public void progressPosition(AdvancedModelRenderer model, float progress, float x, float y, float z) {
    	model.rotationPointX += progress * (x - model.defaultPositionX) / 20.0F;
    	model.rotationPointY += progress * (y - model.defaultPositionY) / 20.0F;
        model.rotationPointZ += progress * (z - model.defaultPositionZ) / 20.0F;
    }

}
