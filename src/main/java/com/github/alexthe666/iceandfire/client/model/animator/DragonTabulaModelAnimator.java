package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonPoses;
import com.github.alexthe666.iceandfire.client.model.util.LegArticulator;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public abstract class DragonTabulaModelAnimator<T extends EntityDragonBase> extends IceAndFireTabulaModelAnimator implements ITabulaModelAnimator<T> {

    protected TabulaModel[] walkPoses;
    protected TabulaModel[] flyPoses;
    protected TabulaModel[] swimPoses;
    protected AdvancedModelBox[] neckParts;
    protected AdvancedModelBox[] tailParts;
    protected AdvancedModelBox[] tailPartsWBody;
    protected AdvancedModelBox[] toesPartsL;
    protected AdvancedModelBox[] toesPartsR;
    protected AdvancedModelBox[] clawL;
    protected AdvancedModelBox[] clawR;

    public DragonTabulaModelAnimator(TabulaModel baseModel) {
        super(baseModel);
    }

    public void init(TabulaModel model) {
        neckParts = new AdvancedModelBox[]{model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Head")};
        tailParts = new AdvancedModelBox[]{model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        tailPartsWBody = new AdvancedModelBox[]{model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        toesPartsL = new AdvancedModelBox[]{model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
        toesPartsR = new AdvancedModelBox[]{model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};
        clawL = new AdvancedModelBox[]{model.getCube("ClawL")};
        clawR = new AdvancedModelBox[]{model.getCube("ClawR")};
    }

    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        if (neckParts == null) {
            init(model);
        }
        animate(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);

        boolean walking = !entity.isHovering() && !entity.isFlying() && entity.hoverProgress <= 0 && entity.flyProgress <= 0;
        boolean swimming = entity.isInWater() && entity.swimProgress > 0;

        int currentIndex = walking ? (entity.walkCycle / 10) : (entity.flightCycle / 10);
        if (swimming) {
            currentIndex = entity.swimCycle / 10;
        }
        int prevIndex = currentIndex - 1;
        if (prevIndex < 0) {
            prevIndex = swimming ? 4 : walking ? 3 : 5;
        }

        TabulaModel currentPosition = swimming ? swimPoses[currentIndex] : walking ? walkPoses[currentIndex] : flyPoses[currentIndex];
        TabulaModel prevPosition = swimming ? swimPoses[prevIndex] : walking ? walkPoses[prevIndex] : flyPoses[prevIndex];
        float delta = ((walking ? entity.walkCycle : entity.flightCycle) / 10.0F) % 1.0F;
        if (swimming) {
            delta = ((entity.swimCycle) / 10.0F) % 1.0F;
        }
        float partialTick = Minecraft.getInstance().getFrameTime();
        float deltaTicks = delta + (partialTick / 10.0F);
        if (delta == 0) {
            deltaTicks = 0;
        }

        float speed_walk = 0.2F;
        float speed_idle = entity.isSleeping() ? 0.025F : 0.05F;
        float speed_fly = 0.2F;
        float degree_walk = 0.5F;
        float degree_idle = entity.isSleeping() ? 0.25F : 0.5F;
        float degree_fly = 0.5F;

        if (entity.isModelDead()) {
            for (AdvancedModelBox cube : model.getCubes().values()) {
                setRotationsLoopDeath(model, entity, limbSwingAmount, walking, currentPosition, prevPosition, partialTick, deltaTicks, cube);
            }
            return;
        }

        if (entity.isAiDisabled())
            return;

        for (AdvancedModelBox cube : model.getCubes().values()) {
            setRotationsLoop(model, entity, limbSwingAmount, walking, currentPosition, prevPosition, partialTick, deltaTicks, cube);
        }
        if (entity.getAnimation() != EntityDragonBase.ANIMATION_SHAKEPREY || entity.getAnimation() != EntityDragonBase.ANIMATION_ROAR) {
            model.faceTarget(rotationYaw, rotationPitch, 2, neckParts);
        }
        if (!walking) {
            model.bob(model.getCube("BodyUpper"), -speed_fly, degree_fly * 5, false, ageInTicks, 1);
            model.walk(model.getCube("BodyUpper"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
            model.chainWave(tailPartsWBody, speed_fly, degree_fly * -0.1F, 0, ageInTicks, 1);
            model.chainWave(neckParts, speed_fly, degree_fly * 0.2F, -4, ageInTicks, 1);
            model.chainWave(toesPartsL, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
            model.chainWave(toesPartsR, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
            model.walk(model.getCube("ThighR"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
            model.walk(model.getCube("ThighL"), -speed_fly, degree_fly * 0.1F, true, 0, 0, ageInTicks, 1);
        } else {
            model.bob(model.getCube("BodyUpper"), speed_walk * 2, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
            model.bob(model.getCube("ThighR"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
            model.bob(model.getCube("ThighL"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
            model.chainSwing(tailParts, speed_walk, degree_walk * 0.25F, -2, limbSwing, limbSwingAmount);
            model.chainWave(tailParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
            model.chainSwing(neckParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
            model.chainWave(neckParts, speed_walk, degree_walk * 0.05F, -2, limbSwing, limbSwingAmount);
            model.chainSwing(tailParts, speed_idle, degree_idle * 0.25F, -2, ageInTicks, 1);
            model.chainWave(tailParts, speed_idle, degree_idle * 0.15F, -2, ageInTicks, 1);
            model.chainWave(neckParts, speed_idle, degree_idle * -0.15F, -3, ageInTicks, 1);
            model.walk(model.getCube("Neck1"), speed_idle, degree_idle * 0.05F, false, 0, 0, ageInTicks, 1);
        }
        model.bob(model.getCube("BodyUpper"), speed_idle, degree_idle * 1.3F, false, ageInTicks, 1);
        model.bob(model.getCube("ThighR"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
        model.bob(model.getCube("ThighL"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
        model.bob(model.getCube("armR1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
        model.bob(model.getCube("armL1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);

        if (entity.isActuallyBreathingFire()) {
            float speed_shake = 0.7F;
            float degree_shake = 0.1F;
            model.chainFlap(neckParts, speed_shake, degree_shake, 2, ageInTicks, 1);
            model.chainSwing(neckParts, speed_shake * 0.65F, degree_shake * 0.1F, 1, ageInTicks, 1);
        }


        if (entity.turn_buffer != null && !entity.isVehicle() && !entity.isPassenger() && entity.isBreathingFire()) {
            entity.turn_buffer.applyChainSwingBuffer(neckParts);
        }
        if (entity.tail_buffer != null && !entity.isPassenger()) {
            entity.tail_buffer.applyChainSwingBuffer(tailPartsWBody);
        }
        if (entity.roll_buffer != null && entity.pitch_buffer_body != null && entity.pitch_buffer != null) {
            if (entity.flyProgress > 0 || entity.hoverProgress > 0) {
                entity.roll_buffer.applyChainFlapBuffer(model.getCube("BodyUpper"));
                entity.pitch_buffer_body.applyChainWaveBuffer(model.getCube("BodyUpper"));
                entity.pitch_buffer.applyChainWaveBufferReverse(tailPartsWBody);
            }
        }
        if (entity.getBbWidth() >= 2 && entity.flyProgress == 0 && entity.hoverProgress == 0) {
            LegArticulator.articulateQuadruped(entity, entity.legSolver, model.getCube("BodyUpper"), model.getCube("BodyLower"), model.getCube("Neck1"),
                    model.getCube("ThighL"), model.getCube("LegL"), toesPartsL,
                    model.getCube("ThighR"), model.getCube("LegR"), toesPartsR,
                    model.getCube("armL1"), model.getCube("armL2"), clawL,
                    model.getCube("armR1"), model.getCube("armR2"), clawR,
                    1.0F, 0.5F, 0.5F, -0.15F, -0.15F, 0F,
                    Minecraft.getInstance().getFrameTime()
            );
        }
    }


    private void setRotationsLoop(TabulaModel model, T entity, float limbSwingAmount, boolean walking, TabulaModel currentPosition, TabulaModel prevPosition, float partialTick, float deltaTicks, AdvancedModelBox cube) {
        this.genderMob(entity, cube);
        if (walking && entity.flyProgress <= 0.0F && entity.hoverProgress <= 0.0F && entity.modelDeadProgress <= 0.0F) {
            AdvancedModelBox walkPart = getModel(EnumDragonPoses.GROUND_POSE).getCube(cube.boxName);
            AdvancedModelBox prevPositionCube = prevPosition.getCube(cube.boxName);
            AdvancedModelBox currPositionCube = currentPosition.getCube(cube.boxName);

            if (prevPositionCube == null || currPositionCube == null)
                return;

            float prevX = prevPositionCube.rotateAngleX;
            float prevY = prevPositionCube.rotateAngleY;
            float prevZ = prevPositionCube.rotateAngleZ;
            float x = currPositionCube.rotateAngleX;
            float y = currPositionCube.rotateAngleY;
            float z = currPositionCube.rotateAngleZ;
            if (isHorn(cube) || isWing(model, cube) && (entity.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST || entity.getAnimation() == EntityDragonBase.ANIMATION_EPIC_ROAR)) {
                this.addToRotateAngle(cube, limbSwingAmount, walkPart.rotateAngleX, walkPart.rotateAngleY, walkPart.rotateAngleZ);
            } else {
                this.addToRotateAngle(cube, limbSwingAmount, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
            }
        }
        if (entity.sleepProgress > 0.0F) {
            if (!isRotationEqual(cube, getModel(EnumDragonPoses.SLEEPING_POSE).getCube(cube.boxName))) {
                transitionTo(cube, getModel(EnumDragonPoses.SLEEPING_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[1], entity.sleepProgress), 20, false);
            }
        }
        if (entity.hoverProgress > 0.0F) {
            if (!isRotationEqual(cube, getModel(EnumDragonPoses.HOVERING_POSE).getCube(cube.boxName)) && !isWing(model, cube) && !cube.boxName.contains("Tail")) {
                transitionTo(cube, getModel(EnumDragonPoses.HOVERING_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[2], entity.hoverProgress), 20, false);
            }
        }
        if (entity.flyProgress > 0.0F) {
            if (!isRotationEqual(cube, getModel(EnumDragonPoses.FLYING_POSE).getCube(cube.boxName))) {
                transitionTo(cube, getModel(EnumDragonPoses.FLYING_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[3], entity.flyProgress) - (Mth.lerp(partialTick, entity.prevDiveProgress, entity.diveProgress)) * 2, 20, false);
            }
        }
        if (entity.sitProgress > 0.0F) {
            if (!entity.isPassenger()) {
                if (!isRotationEqual(cube, getModel(EnumDragonPoses.SITTING_POSE).getCube(cube.boxName))) {
                    transitionTo(cube, getModel(EnumDragonPoses.SITTING_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[0], entity.sitProgress), 20, false);
                }
            }
        }
        if (entity.ridingProgress > 0.0F) {
            if (!isHorn(cube) && !isRotationEqual(cube, getModel(EnumDragonPoses.SIT_ON_PLAYER_POSE).getCube(cube.boxName))) {
                transitionTo(cube, getModel(EnumDragonPoses.SIT_ON_PLAYER_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[5], entity.ridingProgress), 20, false);
                if (cube.boxName.equals("BodyUpper")) {
                    cube.offsetZ += ((-12F - cube.offsetZ) / 20) * Mth.lerp(partialTick, entity.prevAnimationProgresses[5], entity.ridingProgress);
                }

            }
        }
        if (entity.tackleProgress > 0.0F) {
            if (!isRotationEqual(getModel(EnumDragonPoses.TACKLE).getCube(cube.boxName), getModel(EnumDragonPoses.FLYING_POSE).getCube(cube.boxName)) && !isWing(model, cube)) {
                transitionTo(cube, getModel(EnumDragonPoses.TACKLE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevAnimationProgresses[6], entity.tackleProgress), 5, false);
            }
        }
        if (entity.diveProgress > 0.0F) {
            if (!isRotationEqual(cube, getModel(EnumDragonPoses.DIVING_POSE).getCube(cube.boxName))) {
                transitionTo(cube, getModel(EnumDragonPoses.DIVING_POSE).getCube(cube.boxName), Mth.lerp(partialTick, entity.prevDiveProgress, entity.diveProgress), 10, false);
            }
        }
        if (entity.fireBreathProgress > 0.0F) {
            if (!isRotationEqual(cube, getModel(EnumDragonPoses.STREAM_BREATH).getCube(cube.boxName)) && !isWing(model, cube) && !cube.boxName.contains("Finger")) {
                if (entity.prevFireBreathProgress <= entity.fireBreathProgress) {
                    transitionTo(cube, getModel(EnumDragonPoses.BLAST_CHARGE3).getCube(cube.boxName), Mth.clamp(Mth.lerp(partialTick, entity.prevFireBreathProgress, entity.fireBreathProgress), 0, 5), 5, false);
                }
                transitionTo(cube, getModel(EnumDragonPoses.STREAM_BREATH).getCube(cube.boxName), Mth.clamp(Mth.lerp(partialTick, entity.prevFireBreathProgress, entity.fireBreathProgress) - 5, 0, 5), 5, false);

            }
        }
        if (!walking) {
            AdvancedModelBox flightPart = getModel(EnumDragonPoses.FLYING_POSE).getCube(cube.boxName);
            AdvancedModelBox prevPositionCube = prevPosition.getCube(cube.boxName);
            AdvancedModelBox currPositionCube = currentPosition.getCube(cube.boxName);
            float prevX = prevPositionCube.rotateAngleX;
            float prevY = prevPositionCube.rotateAngleY;
            float prevZ = prevPositionCube.rotateAngleZ;
            float x = currPositionCube.rotateAngleX;
            float y = currPositionCube.rotateAngleY;
            float z = currPositionCube.rotateAngleZ;
            if (x != flightPart.rotateAngleX || y != flightPart.rotateAngleY || z != flightPart.rotateAngleZ) {
                this.setRotateAngle(cube, 1F, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
            }
        }
    }

    public void setRotationsLoopDeath(TabulaModel model, T entity, float limbSwingAmount, boolean walking, TabulaModel currentPosition, TabulaModel prevPosition, float partialTick, float deltaTicks, AdvancedModelBox cube) {
        if (entity.modelDeadProgress > 0.0F) {
            // TODO: Figure out what's up with custom poses
            // DON'T use this in it's current state since it heavily effects render performance due to the fact that
            // custom poses aren't being used right now
            // TabulaModel customPose = customPose(entity);
            TabulaModel pose = getModel(EnumDragonPoses.DEAD);
            if (!isRotationEqual(cube, pose.getCube(cube.boxName))) {
                transitionTo(cube, pose.getCube(cube.boxName), entity.prevModelDeadProgress + (entity.modelDeadProgress - entity.prevModelDeadProgress) * Minecraft.getInstance().getFrameTime(), 20, cube.boxName.equals("ThighR") || cube.boxName.equals("ThighL"));
            }
            //Ugly hack to make sure ice dragon models are touching the ground when dead
            if (this instanceof IceDragonTabulaModelAnimator){
                if (cube.boxName.equals("BodyUpper")) {
                    cube.rotationPointY += 0.35F * Mth.lerp(partialTick, entity.prevModelDeadProgress, entity.modelDeadProgress);
                }
            }
        }
    }

    protected boolean isWing(TabulaModel model, AdvancedModelBox modelRenderer) {
        return model.getCube("armL1") == modelRenderer || model.getCube("armR1") == modelRenderer || model.getCube("armL1").childModels.contains(modelRenderer) || model.getCube("armR1").childModels.contains(modelRenderer);
    }

    protected TabulaModel customPose(T entity) {
        try {

            return getModel(EnumDragonPoses.valueOf(entity.getCustomPose()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    protected boolean isHorn(AdvancedModelBox modelRenderer) {
        return modelRenderer.boxName.contains("Horn");
    }

    protected void genderMob(T entity, AdvancedModelBox cube) {
        if (!entity.isMale()) {
            TabulaModel maleModel = getModel(EnumDragonPoses.MALE);
            TabulaModel femaleModel = getModel(EnumDragonPoses.FEMALE);
            AdvancedModelBox femaleModelCube = femaleModel.getCube(cube.boxName);
            AdvancedModelBox maleModelCube = maleModel.getCube(cube.boxName);
            if (maleModelCube == null || femaleModelCube == null)
                return;
            float x = femaleModelCube.rotateAngleX;
            float y = femaleModelCube.rotateAngleY;
            float z = femaleModelCube.rotateAngleZ;
            if (x != maleModelCube.rotateAngleX || y != maleModelCube.rotateAngleY || z != maleModelCube.rotateAngleZ) {
                this.setRotateAngle(cube, 1F, x, y, z);
            }
        }
    }

    protected abstract TabulaModel getModel(EnumDragonPoses pose);

    protected void animate(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox modelCubeJaw = model.getCube("Jaw");
        AdvancedModelBox modelCubeBodyUpper = model.getCube("BodyUpper");
        model.llibAnimator.update(entity);
        //Firecharge
        if (model.llibAnimator.setAnimation(T.ANIMATION_FIRECHARGE)) {
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.BLAST_CHARGE3));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(5);
        }
        //Speak
        if (model.llibAnimator.setAnimation(T.ANIMATION_SPEAK)) {
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
            model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.setStaticKeyframe(5);
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
            model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(5);
        }
        //Bite
        if (model.llibAnimator.setAnimation(T.ANIMATION_BITE)) {
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.BITE1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.BITE2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.BITE3));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        //Shakeprey
        if (model.llibAnimator.setAnimation(T.ANIMATION_SHAKEPREY)) {
            model.llibAnimator.startKeyframe(15);
            moveToPose(model, getModel(EnumDragonPoses.GRAB1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.GRAB2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.GRAB_SHAKE1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.GRAB_SHAKE2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.GRAB_SHAKE3));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        //Tailwhack
        if (model.llibAnimator.setAnimation(T.ANIMATION_TAILWHACK)) {
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.TAIL_WHIP1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.TAIL_WHIP2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.TAIL_WHIP3));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        //Wingblast
        if (model.llibAnimator.setAnimation(T.ANIMATION_WINGBLAST)) {
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST1));
            model.llibAnimator.move(modelCubeBodyUpper, 0, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST2));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -2F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST3));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST4));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST5));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST6));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -4F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            moveToPose(model, getModel(EnumDragonPoses.WING_BLAST7));
            model.llibAnimator.move(modelCubeBodyUpper, 0, -2F, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        //Roar
        if (model.llibAnimator.setAnimation(T.ANIMATION_ROAR)) {
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.ROAR1));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.ROAR2));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.ROAR3));
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        //Epicroar
        if (model.llibAnimator.setAnimation(T.ANIMATION_EPIC_ROAR)) {
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.EPIC_ROAR1));
            model.llibAnimator.rotate(modelCubeBodyUpper, -0.1F, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.EPIC_ROAR2));
            model.llibAnimator.rotate(modelCubeBodyUpper, -0.2F, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.EPIC_ROAR3));
            model.llibAnimator.rotate(modelCubeBodyUpper, -0.2F, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.EPIC_ROAR2));
            model.llibAnimator.rotate(modelCubeBodyUpper, -0.2F, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(10);
            moveToPose(model, getModel(EnumDragonPoses.EPIC_ROAR3));
            model.llibAnimator.rotate(modelCubeBodyUpper, -0.1F, 0, 0);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.resetKeyframe(10);
        }
        // EATING
        if (model.llibAnimator.setAnimation(T.ANIMATION_EAT)) {
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, model.getCube("Neck1"), 18, 0, 0);
            this.rotate(model.llibAnimator, model.getCube("Neck2"), 18, 0, 0);
            model.llibAnimator.endKeyframe();
            //CODE from speak
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
            model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.setStaticKeyframe(5);
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, modelCubeJaw, 18, 0, 0);
            model.llibAnimator.move(modelCubeJaw, 0, 0, 0.2F);
            model.llibAnimator.endKeyframe();
            model.llibAnimator.startKeyframe(5);
            this.rotate(model.llibAnimator, model.getCube("Neck1"), -18, 0, 0);
            this.rotate(model.llibAnimator, model.getCube("Neck2"), -18, 0, 0);
            model.llibAnimator.endKeyframe();
        }
    }
}
