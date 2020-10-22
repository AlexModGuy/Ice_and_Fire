package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;

import net.minecraft.client.Minecraft;

public class SeaSerpentTabulaModelAnimator extends IceAndFireTabulaModelAnimator implements ITabulaModelAnimator<EntitySeaSerpent> {

    public TabulaModel[] swimPose = {EnumSeaSerpentAnimations.SWIM1.seaserpent_model, EnumSeaSerpentAnimations.SWIM3.seaserpent_model, EnumSeaSerpentAnimations.SWIM4.seaserpent_model, EnumSeaSerpentAnimations.SWIM6.seaserpent_model};

    public SeaSerpentTabulaModelAnimator() {
        super(EnumSeaSerpentAnimations.T_POSE.seaserpent_model);
    }

    @Override
    public void setRotationAngles(TabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        model.getCube("BodyUpper").rotationPointY += 9;//model was made too high
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
        if (properties != null && properties.isStone()) {
            return;
        }
        model.llibAnimator.update(entity);
        animate(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        int currentIndex = entity.swimCycle / 10;
        int prevIndex = currentIndex - 1;
        if (prevIndex < 0) {
            prevIndex = 3;
        }
        TabulaModel prevPosition = swimPose[prevIndex];
        TabulaModel currentPosition = swimPose[currentIndex];
        float delta = ((entity.swimCycle) / 10.0F) % 1.0F + (Minecraft.getInstance().getRenderPartialTicks() / 10.0F);
        AdvancedModelBox[] tailParts = {model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4"), model.getCube("Tail5"), model.getCube("Tail6")};
        AdvancedModelBox[] neckParts = {model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Head")};

        for (AdvancedModelBox cube : model.getCubes().values()) {
            if (entity.jumpProgress > 0.0F) {
                if (!isPartEqual(cube, EnumSeaSerpentAnimations.JUMPING2.seaserpent_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumSeaSerpentAnimations.JUMPING2.seaserpent_model.getCube(cube.boxName), entity.jumpProgress, 5, false);
                }
            }
            if (entity.wantJumpProgress > 0.0F) {
                if (!isPartEqual(cube, EnumSeaSerpentAnimations.JUMPING1.seaserpent_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumSeaSerpentAnimations.JUMPING1.seaserpent_model.getCube(cube.boxName), entity.wantJumpProgress, 10, false);
                }
            }
            float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
            float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
            float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
            float x = currentPosition.getCube(cube.boxName).rotateAngleX;
            float y = currentPosition.getCube(cube.boxName).rotateAngleY;
            float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
            this.addToRotateAngle(cube, limbSwingAmount, prevX + delta * distance(prevX, x), prevY + delta * distance(prevY, y), prevZ + delta * distance(prevZ, z));

        }
        if (entity.breathProgress > 0.0F) {
            progressRotation(model.getCube("Head"), entity.breathProgress, (float) Math.toRadians(-15F), 0, 0);
            progressRotation(model.getCube("HeadFront"), entity.breathProgress, (float) Math.toRadians(-20F), 0, 0);
            progressRotation(model.getCube("Jaw"), entity.breathProgress, (float) Math.toRadians(60F), 0, 0);
        }
        if (entity.jumpRot > 0.0F) {
            float turn = (float) entity.getMotion().y * -4F;
            model.getCube("BodyUpper").rotateAngleX += (float) Math.toRadians(22.5F * turn) * entity.jumpRot;
            model.getCube("Tail1").rotateAngleX -= (float) Math.toRadians(turn) * entity.jumpRot;
            model.getCube("Tail2").rotateAngleX -= (float) Math.toRadians(turn) * entity.jumpRot;
            model.getCube("Tail3").rotateAngleX -= (float) Math.toRadians(turn) * entity.jumpRot;
            model.getCube("Tail4").rotateAngleX -= (float) Math.toRadians(turn) * entity.jumpRot;
        }
        if(entity.isInWater()){
            entity.roll_buffer.applyChainFlapBuffer(model.getCube("BodyUpper"));
            entity.pitch_buffer.applyChainWaveBuffer(model.getCube("BodyUpper"));
            entity.head_buffer.applyChainSwingBufferReverse(neckParts);
        }
        entity.tail_buffer.applyChainSwingBuffer(tailParts);

    }

    public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ) {
        model.rotateAngleX += progress * (rotX - model.defaultRotationX) / 20.0F;
        model.rotateAngleY += progress * (rotY - model.defaultRotationY) / 20.0F;
        model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / 20.0F;
    }

    private void animate(TabulaModel model, EntitySeaSerpent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_SPEAK);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 25, 0, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.setStaticKeyframe(5);
        model.llibAnimator.resetKeyframe(5);
        model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_BITE);
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumSeaSerpentAnimations.BITE1.seaserpent_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumSeaSerpentAnimations.BITE2.seaserpent_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.setStaticKeyframe(2);
        model.llibAnimator.resetKeyframe(3);

        model.llibAnimator.setAnimation(EntitySeaSerpent.ANIMATION_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumSeaSerpentAnimations.ROAR1.seaserpent_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumSeaSerpentAnimations.ROAR2.seaserpent_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumSeaSerpentAnimations.ROAR3.seaserpent_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
    }
}
