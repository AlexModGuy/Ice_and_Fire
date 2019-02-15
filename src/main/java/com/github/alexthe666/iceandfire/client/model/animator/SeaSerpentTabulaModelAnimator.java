package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IIceAndFireTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public class SeaSerpentTabulaModelAnimator extends IceAndFireTabulaModelAnimator implements IIceAndFireTabulaModelAnimator<EntitySeaSerpent> {
    @Override
    public void setRotationAngles(IceAndFireTabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        model.llibAnimator.update(entity);
        animate(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        IceAndFireTabulaModel[] swimPose = {EnumSeaSerpentAnimations.SWIM1.seaserpent_model, EnumSeaSerpentAnimations.SWIM3.seaserpent_model, EnumSeaSerpentAnimations.SWIM4.seaserpent_model, EnumSeaSerpentAnimations.SWIM6.seaserpent_model};
        int currentIndex = entity.swimCycle / 10;
        int prevIndex = currentIndex - 1;
        if (prevIndex < 0) {
            prevIndex = 3;
        }
        IceAndFireTabulaModel prevPosition = swimPose[prevIndex];
        IceAndFireTabulaModel currentPosition = swimPose[currentIndex];
        float delta = ((entity.swimCycle) / 10.0F) % 1.0F + (LLibrary.PROXY.getPartialTicks() / 10.0F);
        AdvancedModelRenderer[] tailParts = { model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4"), model.getCube("Tail5"), model.getCube("Tail6")};
        AdvancedModelRenderer[] neckParts = { model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Head")};

        for(AdvancedModelRenderer cube : model.getCubes().values()) {
            if (true) {
                float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
                float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
                float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
                float x = currentPosition.getCube(cube.boxName).rotateAngleX;
                float y = currentPosition.getCube(cube.boxName).rotateAngleY;
                float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
                this.addToRotateAngle(cube, limbSwingAmount, prevX + delta * distance(prevX, x), prevY + delta * distance(prevY, y), prevZ + delta * distance(prevZ, z));
            }
        }
        entity.tail_buffer.applyChainSwingBuffer(tailParts);
        entity.roll_buffer.applyChainFlapBuffer(model.getCube("BodyUpper"));
        entity.pitch_buffer.applyChainWaveBuffer(model.getCube("BodyUpper"));
        entity.head_buffer.applyChainSwingBufferReverse(neckParts);

    }

    private void animate(IceAndFireTabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
    }
}
