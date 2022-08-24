package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.util.math.MathHelper;

public class IceAndFireTabulaModelAnimator {

    protected TabulaModel baseModel;

    public IceAndFireTabulaModelAnimator(TabulaModel baseModel) {
        this.baseModel = baseModel;
    }

    public void setRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.xRot += limbSwingAmount * distance(model.xRot, x);
        model.yRot += limbSwingAmount * distance(model.yRot, y);
        model.zRot += limbSwingAmount * distance(model.zRot, z);
    }

    public void addToRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.xRot += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationX, x);
        model.yRot += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationY, y);
        model.zRot += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationZ, z);
    }

    public boolean isRotationEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.xRot == original.defaultRotationX && pose.yRot == original.defaultRotationY && pose.zRot == original.defaultRotationZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.x == original.defaultPositionX && pose.y == original.defaultPositionY && pose.z == original.defaultPositionZ;
    }

    public void transitionTo(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime, boolean oldFashioned) {
        if (oldFashioned) {
            from.xRot += ((to.xRot - from.xRot) / maxTime) * timer;
            from.yRot += ((to.yRot - from.yRot) / maxTime) * timer;
            from.zRot += ((to.zRot - from.zRot) / maxTime) * timer;
        } else {
            transitionAngles(from, to, timer, maxTime);
        }
        from.x += ((to.x - from.x) / maxTime) * timer;
        from.y += ((to.y - from.y) / maxTime) * timer;
        from.z += ((to.z - from.z) / maxTime) * timer;

        //from.offsetX += ((to.offsetX - from.offsetX) / maxTime) * timer;
        //from.offsetY += ((to.offsetY - from.offsetY) / maxTime) * timer;
        //from.offsetZ += ((to.offsetZ - from.offsetZ) / maxTime) * timer;
    }

    public void transitionAngles(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime) {
        from.xRot += ((distance(from.xRot, to.xRot)) / maxTime) * timer;
        from.yRot += ((distance(from.yRot, to.yRot)) / maxTime) * timer;
        from.zRot += ((distance(from.zRot, to.zRot)) / maxTime) * timer;
    }

    public float distance(float rotateAngleFrom, float rotateAngleTo) {
        return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void moveToPose(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox cubeTo = modelTo.getCube(cube.boxName);
            if (!isRotationEqual(baseModel.getCube(cube.boxName), cubeTo)) {
                float toX = cubeTo.xRot;
                float toY = cubeTo.yRot;
                float toZ = cubeTo.zRot;
                model.llibAnimator.rotate(cube, distance(cube.xRot, toX), distance(cube.yRot, toY), distance(cube.zRot, toZ));
            }
            if (!isPositionEqual(baseModel.getCube(cube.boxName), cubeTo)) {
                float toX = cubeTo.x;
                float toY = cubeTo.y;
                float toZ = cubeTo.z;
                model.llibAnimator.move(cube, toX - cube.x, toY - cube.y, toZ - cube.z);
            }
        }
    }
}
