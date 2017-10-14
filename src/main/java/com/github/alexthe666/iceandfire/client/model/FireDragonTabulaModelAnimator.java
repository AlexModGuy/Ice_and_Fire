package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IIceAndFireTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.math.MathHelper;

public class FireDragonTabulaModelAnimator implements IIceAndFireTabulaModelAnimator<EntityFireDragon> {

    @Override
    public void setRotationAngles(IceAndFireTabulaModel model, EntityFireDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        for(AdvancedModelRenderer cube : model.getCubes().values()){
            transitionTo(cube, EnumDragonAnimations.SITTING_POSE.firedragon_model.getCube(cube.boxName), 1, 1);
        }
    }

    public void transitionTo(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime) {
        //transitionAngles(from, to, timer, maxTime);
        from.rotateAngleX += MathHelper.wrapDegrees(((to.rotateAngleX - from.rotateAngleX) / maxTime)) * timer;

        from.rotationPointX += ((to.rotationPointX - from.rotationPointX) / maxTime) * timer;
        from.rotationPointY += ((to.rotationPointY - from.rotationPointY) / maxTime) * timer;
        from.rotationPointZ += ((to.rotationPointZ - from.rotationPointZ) / maxTime) * timer;

        from.offsetX += ((to.offsetX - from.offsetX) / maxTime) * timer;
        from.offsetY += ((to.offsetY - from.offsetY) / maxTime) * timer;
        from.offsetZ += ((to.offsetZ - from.offsetZ) / maxTime) * timer;
    }

    private void transitionAngles(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime){
        if(to.rotateAngleX - from.rotateAngleX < -180){
            from.rotateAngleX += MathHelper.wrapDegrees(((to.rotateAngleX + from.rotateAngleX) / maxTime)) * timer;
        }else{
            from.rotateAngleX += MathHelper.wrapDegrees(((to.rotateAngleX - from.rotateAngleX) / maxTime)) * timer;
        }
        if(to.rotateAngleX - from.rotateAngleX < -180){
            from.rotateAngleY += MathHelper.wrapDegrees(((to.rotateAngleY + from.rotateAngleY) / maxTime)) * timer;
        }else{
            from.rotateAngleY += MathHelper.wrapDegrees(((to.rotateAngleY - from.rotateAngleY) / maxTime)) * timer;
        }
        if(to.rotateAngleX - from.rotateAngleX < -180){
            from.rotateAngleZ += MathHelper.wrapDegrees(((to.rotateAngleZ + from.rotateAngleZ) / maxTime)) * timer;
        }else{
            from.rotateAngleZ += MathHelper.wrapDegrees(((to.rotateAngleZ - from.rotateAngleZ) / maxTime)) * timer;
        }
    }
}
