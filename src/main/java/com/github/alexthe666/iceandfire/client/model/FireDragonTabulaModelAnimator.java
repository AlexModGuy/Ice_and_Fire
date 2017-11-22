package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IIceAndFireTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public class FireDragonTabulaModelAnimator implements IIceAndFireTabulaModelAnimator<EntityFireDragon> {

	@Override
	public void setRotationAngles(IceAndFireTabulaModel model, EntityFireDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
		model.resetToDefaultPose();
		IceAndFireTabulaModel[] walkPoses = {EnumDragonAnimations.WALK1.firedragon_model, EnumDragonAnimations.WALK2.firedragon_model, EnumDragonAnimations.WALK3.firedragon_model, EnumDragonAnimations.WALK4.firedragon_model};
		IceAndFireTabulaModel[] flyPoses = {EnumDragonAnimations.FLIGHT1.firedragon_model, EnumDragonAnimations.FLIGHT2.firedragon_model, EnumDragonAnimations.FLIGHT3.firedragon_model, EnumDragonAnimations.FLIGHT4.firedragon_model, EnumDragonAnimations.FLIGHT5.firedragon_model, EnumDragonAnimations.FLIGHT6.firedragon_model};
		boolean walking = !(entity.isFlying() || entity.isHovering()) && (entity.hoverProgress <= 0 || entity.flyProgress <= 0);
		int currentIndex = walking ? (entity.walkCycle / 10) : (entity.flightCycle / 10);
		int prevIndex = currentIndex - 1;
		if (prevIndex < 0) {
			prevIndex = walking ? 3 : 5;
		}
		IceAndFireTabulaModel prevPosition = walking ? walkPoses[prevIndex] : flyPoses[prevIndex];
		IceAndFireTabulaModel currentPosition = walking ? walkPoses[currentIndex] : flyPoses[currentIndex];
		float delta = ((walking ? entity.walkCycle : entity.flightCycle) / 10.0F) % 1.0F + (LLibrary.PROXY.getPartialTicks() / 10.0F);
		AdvancedModelRenderer[] neckParts = {model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Neck3"), model.getCube("Head")};
		AdvancedModelRenderer[] tailParts = {model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
		AdvancedModelRenderer[] tailPartsWBody = {model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
		AdvancedModelRenderer[] toesPartsL = {model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
		AdvancedModelRenderer[] toesPartsR = {model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};

		for (AdvancedModelRenderer cube : model.getCubes().values()) {
			if (walking && entity.flyProgress <= 0.0F && entity.hoverProgress <= 0.0F && entity.hoverProgress <= 0.0F && entity.modelDeadProgress <= 0.0F) {
				float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
				float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
				float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
				float x = currentPosition.getCube(cube.boxName).rotateAngleX;
				float y = currentPosition.getCube(cube.boxName).rotateAngleY;
				float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
				this.addToRotateAngle(cube, limbSwingAmount, prevX + delta * distance(prevX, x), prevY + delta * distance(prevY, y), prevZ + delta * distance(prevZ, z));
			}
			if (entity.modelDeadProgress > 0.0F) {
				if (!isPartEqual(cube, EnumDragonAnimations.DEAD.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.DEAD.firedragon_model.getCube(cube.boxName), entity.modelDeadProgress, 20);
				}
			}
			if (entity.sleepProgress > 0.0F) {
				if (!isPartEqual(cube, EnumDragonAnimations.SLEEPING_POSE.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.SLEEPING_POSE.firedragon_model.getCube(cube.boxName), entity.sleepProgress, 20);
				}
			}
			if (entity.hoverProgress > 0.0F) {
				if (!isPartEqual(cube, EnumDragonAnimations.HOVERING_POSE.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.HOVERING_POSE.firedragon_model.getCube(cube.boxName), entity.hoverProgress, 20);
				}
			}
			if (entity.flyProgress > 0.0F) {
				if (!isPartEqual(cube, EnumDragonAnimations.FLYING_POSE.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.FLYING_POSE.firedragon_model.getCube(cube.boxName), entity.flyProgress, 20);
				}
			}
			if (entity.sitProgress > 0.0F) {
				if (!isPartEqual(cube, EnumDragonAnimations.SITTING_POSE.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.SITTING_POSE.firedragon_model.getCube(cube.boxName), entity.sitProgress, 20);
				}
			}
			if (entity.tackleProgress > 0.0F) {
				if (!isPartEqual(EnumDragonAnimations.TACKLE.firedragon_model.getCube(cube.boxName), EnumDragonAnimations.FLYING_POSE.firedragon_model.getCube(cube.boxName))) {
					transitionTo(cube, EnumDragonAnimations.TACKLE.firedragon_model.getCube(cube.boxName), entity.tackleProgress, 5);
				}
			}

			if (!walking) {
				AdvancedModelRenderer flightPart = EnumDragonAnimations.FLYING_POSE.firedragon_model.getCube(cube.boxName);
				float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
				float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
				float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
				float x = currentPosition.getCube(cube.boxName).rotateAngleX;
				float y = currentPosition.getCube(cube.boxName).rotateAngleY;
				float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
				if (x != flightPart.rotateAngleX && y != flightPart.rotateAngleY && z != flightPart.rotateAngleZ) {
					this.setRotateAngle(cube, prevX + delta * distance(prevX, x), prevY + delta * distance(prevY, y), prevZ + delta * distance(prevZ, z));
				}
			}
	        /*
            transitionTo(cube, walkPoses[0].getCube(cube.boxName), MathHelper.clamp(cos, 0, 10), 10);
            transitionTo(cube, walkPoses[1].getCube(cube.boxName), MathHelper.clamp(cos, 10, 20) - 10, 10);
            transitionTo(cube, walkPoses[2].getCube(cube.boxName), MathHelper.clamp(cos, 20, 30) - 20, 10);
            transitionTo(cube, walkPoses[3].getCube(cube.boxName), MathHelper.clamp(cos, 30, 40) - 30, 10);
            */
		}
		float speed_walk = 0.2F;
		float speed_idle = 0.05F;
		float speed_fly = 0.2F;
		float degree_walk = 0.5F;
		float degree_idle = 0.5F;
		float degree_fly = 0.5F;
		if (!walking) {
			model.bob(model.getCube("BodyUpper"), -speed_fly, degree_fly * 5, false, entity.ticksExisted, 1);
			model.walk(model.getCube("BodyUpper"), -speed_fly, degree_fly * 0.1F, false, 0, 0, entity.ticksExisted, 1);
			model.chainWave(tailPartsWBody, speed_fly, degree_fly * -0.1F, 0, entity.ticksExisted, 1);
			model.chainWave(neckParts, speed_fly, degree_fly * 0.2F, -4, entity.ticksExisted, 1);
			model.chainWave(toesPartsL, speed_fly, degree_fly * 0.2F, -2, entity.ticksExisted, 1);
			model.chainWave(toesPartsR, speed_fly, degree_fly * 0.2F, -2, entity.ticksExisted, 1);
			model.walk(model.getCube("ThighR"), -speed_fly, degree_fly * 0.1F, false, 0, 0, entity.ticksExisted, 1);
			model.walk(model.getCube("ThighL"), -speed_fly, degree_fly * 0.1F, true, 0, 0, entity.ticksExisted, 1);
		}

		model.faceTarget(rotationYaw, rotationPitch, 4, neckParts);
		entity.turn_buffer.applyChainSwingBuffer(neckParts);
		entity.tail_buffer.applyChainSwingBuffer(tailPartsWBody);
	}

	public void setRotateAngle(AdvancedModelRenderer model, float x, float y, float z) {
		model.rotateAngleX += distance(model.rotateAngleX, x);
		model.rotateAngleY += distance(model.rotateAngleY, y);
		model.rotateAngleZ += distance(model.rotateAngleZ, z);
	}

	public void addToRotateAngle(AdvancedModelRenderer model, float limbSwingAmount, float x, float y, float z) {
		model.rotateAngleX += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationX, x);
		model.rotateAngleY += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationY, y);
		model.rotateAngleZ += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationZ, z);
	}

	private boolean isPartEqual(AdvancedModelRenderer original, AdvancedModelRenderer pose) {
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

	private void transitionAngles(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime) {
		from.rotateAngleX += ((distance(from.rotateAngleX, to.rotateAngleX)) / maxTime) * timer;
		from.rotateAngleY += ((distance(from.rotateAngleY, to.rotateAngleY)) / maxTime) * timer;
		from.rotateAngleZ += ((distance(from.rotateAngleZ, to.rotateAngleZ)) / maxTime) * timer;
	}

	private float distance(float rotateAngleFrom, float rotateAngleTo) {
		return (float) Math.atan2(Math.sin(rotateAngleTo - rotateAngleFrom), Math.cos(rotateAngleTo - rotateAngleFrom));
	}
}
