package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;

public class ModelUtils {

    private static void setRotateAngle(AdvancedModelBox model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public static void rotate(ModelAnimator ModelAnimator, AdvancedModelBox box, float x, float y, float z) {
        ModelAnimator.rotate(box, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public static void rotateFrom(ModelAnimator ModelAnimator, AdvancedModelBox box, float x, float y, float z) {
        ModelAnimator.rotate(box, (float) Math.toRadians(x) - box.xRot, (float) Math.toRadians(y) - box.yRot, (float) Math.toRadians(z) - box.zRot);
    }

    public static void rotateFromRadians(ModelAnimator ModelAnimator, AdvancedModelBox box, float x, float y, float z) {
        ModelAnimator.rotate(box, x - box.xRot, y - box.yRot, z - box.zRot);
    }
}
