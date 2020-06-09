package com.github.alexthe666.iceandfire.client.model;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBox;
import net.minecraft.client.model.ModelRenderer;

import java.util.List;

public class ModelUtils {

    private static void setRotateAngle(AdvancedModelBox model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public static void rotate(ModelAnimator ModelAnimator, ModelRenderer box, float x, float y, float z) {
        ModelAnimator.rotate(box, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public static void rotateFrom(ModelAnimator ModelAnimator, ModelRenderer box, float x, float y, float z) {
        ModelAnimator.rotate(box, (float) Math.toRadians(x) - box.rotateAngleX, (float) Math.toRadians(y) - box.rotateAngleY, (float) Math.toRadians(z) - box.rotateAngleZ);
    }

    public static void rotateFromRadians(ModelAnimator ModelAnimator, ModelRenderer box, float x, float y, float z) {
        ModelAnimator.rotate(box, x - box.rotateAngleX, y - box.rotateAngleY, z - box.rotateAngleZ);
    }
}
