package com.github.alexthe666.iceandfire.client.model;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelRenderer;

import java.util.List;

public class ModelUtils {
    public static void renderAll(List boxList) {
        for (Object element : boxList) {
            if (element instanceof AdvancedModelRenderer) {
                AdvancedModelRenderer box = (AdvancedModelRenderer) element;
                if (box.getParent() == null) {
                    box.render(0.0625F);
                }
            }
        }
    }

    public static void animateOrRotate(ModelAnimator ModelAnimator, boolean animate, AdvancedModelRenderer box, float x, float y, float z) {
        if (animate) {
            ModelAnimator.rotate(box, x == box.rotateAngleX ? 0 : x, y == box.rotateAngleY ? 0 : y, z == box.rotateAngleZ ? 0 : z);
        } else {
            ModelUtils.setRotateAngle(box, x, y, z);
        }
    }

    public static void animateOrRotateIgnore(ModelAnimator ModelAnimator, boolean animate, AdvancedModelRenderer box, float x, float y, float z, boolean ignoreX, boolean ignoreY, boolean ignoreZ) {
        if (animate) {
            ModelAnimator.rotate(box, ignoreX ? 0 : x, ignoreY ? y : 0, ignoreZ ? z : 0);
        } else {
            ModelUtils.setRotateAngle(box, x, y, z);
        }
    }

    private static void setRotateAngle(AdvancedModelRenderer model, float x, float y, float z) {
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
