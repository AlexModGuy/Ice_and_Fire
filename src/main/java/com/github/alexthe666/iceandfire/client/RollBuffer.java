package com.github.alexthe666.iceandfire.client;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class RollBuffer {
    private int yawTimer;
    private float yawVariation;
    private float[] yawArray;

    public RollBuffer(int numberOfParentedBoxes) {
        yawTimer = 0;
        yawVariation = 0f;
        yawArray = new float[numberOfParentedBoxes];
    }

    public void resetRotations() {
        yawVariation = 0f;
    }
    
    public void calculateChainRollBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity) {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(yawVariation) < maxAngle) {
            yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset);
        }

        if (yawVariation > 0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation -= angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else {
                yawTimer++;
            }
        } else if (yawVariation < -0.7f * angleDecrement) {
            if (yawTimer > bufferTime) {
                yawVariation += angleDecrement;
                if (MathHelper.abs(yawVariation) < angleDecrement) {
                    yawVariation = 0f;
                    yawTimer = 0;
                }
            } else {
                yawTimer++;
            }
        }

        for (int i = 0; i < yawArray.length; i++) {
            yawArray[i] = 0.01745329251f * yawVariation / yawArray.length;
        }
    }

    public void applyChainRollBuffer(MowzieModelRenderer[] boxes) {
        if (boxes.length == yawArray.length) {
            for (int i = 0; i < boxes.length; i++) {
                boxes[i].rotateAngleZ += yawArray[i];
            }
        } else {
            LLibrary.logger.error("Wrong array length being used in the buffer! (Y axis)");
        }
    }

}