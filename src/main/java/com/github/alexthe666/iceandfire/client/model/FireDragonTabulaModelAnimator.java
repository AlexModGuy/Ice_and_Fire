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
        model.resetToDefaultPose();

        IceAndFireTabulaModel currentPose = null;
        IceAndFireTabulaModel animationPose = null;
        IceAndFireTabulaModel[] walkPoses = {EnumDragonAnimations.WALK1.firedragon_model, EnumDragonAnimations.WALK2.firedragon_model, EnumDragonAnimations.WALK3.firedragon_model, EnumDragonAnimations.WALK4.firedragon_model};

        for(AdvancedModelRenderer cube : model.getCubes().values()){
            if(currentPose != null){
                if(!isPartEqual(cube, currentPose.getCube(cube.boxName))){
                    transitionTo(cube, currentPose.getCube(cube.boxName), entity.ticksExisted % 40, 40);
                }
            }
            if(animationPose != null) {
                if (!isPartEqual(cube, animationPose.getCube(cube.boxName))) {
                    transitionTo(cube, animationPose.getCube(cube.boxName), entity.ticksExisted % 40, 40);
                }
            }
            transitionTo(cube, walkPoses[0].getCube(cube.boxName), MathHelper.clamp(entity.ticksExisted % 80, 0, 20), 20);
            transitionTo(cube, walkPoses[1].getCube(cube.boxName), MathHelper.clamp(entity.ticksExisted % 80, 20, 40) - 20, 20);
            transitionTo(cube, walkPoses[2].getCube(cube.boxName), MathHelper.clamp(entity.ticksExisted % 80, 40, 60) - 40, 20);
            transitionTo(cube, walkPoses[3].getCube(cube.boxName), MathHelper.clamp(entity.ticksExisted % 80, 60, 80) - 60, 20);

        }

    }

    private boolean isPartEqual(AdvancedModelRenderer original, AdvancedModelRenderer pose){
        return pose.rotateAngleX == original.defaultRotationX && pose.rotateAngleY == original.defaultRotationY && pose.rotateAngleZ == original.defaultRotationZ;
    }

    public void transitionTo(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime) {
        transitionAngles(from, to, timer, maxTime);

        from.rotationPointX += ((to.rotationPointX - from.rotationPointX) / maxTime) * timer;
        from.rotationPointY += ((to.rotationPointY - from.rotationPointY) / maxTime) * timer;
        from.rotationPointZ += ((to.rotationPointZ - from.rotationPointZ) / maxTime) * timer;

        from.offsetX += ((to.offsetX - from.offsetX) / maxTime) * timer;
        from.offsetY += ((to.offsetY - from.offsetY) / maxTime) * timer;
        from.offsetZ += ((to.offsetZ - from.offsetZ) / maxTime) * timer;
    }

    private void transitionAngles(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime){
        from.rotateAngleX += ((rotateAmount(from.rotateAngleX, to.rotateAngleX)) / maxTime) * timer;
        from.rotateAngleY += ((rotateAmount(from.rotateAngleY, to.rotateAngleY)) / maxTime) * timer;
        from.rotateAngleZ += ((rotateAmount(from.rotateAngleZ, to.rotateAngleZ)) / maxTime) * timer;
    }

    private float rotateAmount(float rotateAngleFrom, float rotateAngleTo){
        double distance = Math.toDegrees(rotateAngleTo) - Math.toDegrees(rotateAngleFrom);
        double reverseDistance = Math.toDegrees(rotateAngleFrom) - Math.toDegrees(rotateAngleTo);
        float sub = rotateAngleTo - rotateAngleFrom;
        float reverseSub = rotateAngleTo + rotateAngleFrom;
        if(distance > 180){
            return reverseSub;
        }
        if(reverseDistance > 180){
            return reverseSub;
        }
        return sub;
    }
}
