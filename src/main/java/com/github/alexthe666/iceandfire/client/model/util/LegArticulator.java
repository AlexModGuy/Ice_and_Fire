package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.math.MathHelper;
/*
       Code from JurassiCraft, used with permission
       By paul101
 */

public final class LegArticulator {
	private LegArticulator() {
	}

	public static void articulateBiped(EntityDragonBase entity, LegSolverBiped legs, AdvancedModelRenderer body, AdvancedModelRenderer leftThigh, AdvancedModelRenderer leftCalf, AdvancedModelRenderer rightThigh, AdvancedModelRenderer rightCalf, float rotThigh, float rotCalf, float delta) {
		float heightLeft = legs.left.getHeight(delta);
		float heightRight = legs.right.getHeight(delta);
		if (heightLeft > 0 || heightRight > 0) {
			float sc = LegArticulator.getScale(entity);
			float avg = LegArticulator.avg(heightLeft, heightRight);
			body.rotationPointY += 16 / sc * avg;
			articulateLegPair(sc, heightLeft, heightRight, avg, 0, leftThigh, leftCalf, rightThigh, rightCalf, rotThigh, rotCalf);
		}
	}

	public static void articulateQuadruped(
			EntityDragonBase entity, LegSolverQuadruped legs, AdvancedModelRenderer body, AdvancedModelRenderer neck,
			AdvancedModelRenderer backLeftThigh, AdvancedModelRenderer backLeftCalf,
			AdvancedModelRenderer backRightThigh, AdvancedModelRenderer backRightCalf,
			AdvancedModelRenderer frontLeftThigh, AdvancedModelRenderer frontLeftCalf,
			AdvancedModelRenderer frontRightThigh, AdvancedModelRenderer frontRightCalf,
			float rotBackThigh, float rotBackCalf,
			float rotFrontThigh, float rotFrontCalf,
			float delta) {
		float heightBackLeft = legs.backLeft.getHeight(delta);
		float heightBackRight = legs.backRight.getHeight(delta);
		float heightFrontLeft = legs.frontLeft.getHeight(delta);
		float heightFrontRight = legs.frontRight.getHeight(delta);
		if (heightBackLeft > 0 || heightBackRight > 0 || heightFrontLeft > 0 || heightFrontRight > 0) {
			float sc = LegArticulator.getScale(entity);
			float backAvg = LegArticulator.avg(heightBackLeft, heightBackRight);
			float frontAvg = LegArticulator.avg(heightFrontLeft, heightFrontRight);
			float bodyLength = Math.abs(avg(legs.backLeft.forward, legs.backRight.forward) - avg(legs.frontLeft.forward, legs.frontRight.forward));
			float tilt = (float) (MathHelper.atan2(bodyLength, backAvg - frontAvg) - Math.PI / 2);
			float tiltOffset = tilt * 3.1F;
			body.rotationPointY += (16 / sc * backAvg) + tiltOffset;
			body.rotateAngleX += tilt;

			backLeftThigh.rotationPointY -= (16 / sc * backAvg) - tiltOffset;
			backLeftThigh.rotateAngleX -= tilt;

			backRightThigh.rotationPointY -= (16 / sc * backAvg) - tiltOffset;
			backRightThigh.rotateAngleX -= tilt;

			frontLeftThigh.rotateAngleX += tilt;
			frontRightThigh.rotateAngleX -= tilt;
			neck.rotateAngleX -= tilt;
			LegArticulator.articulateLegPair(sc, heightBackLeft, heightBackRight, backAvg, 0, backLeftThigh, backLeftCalf, backRightThigh, backRightCalf, rotBackThigh, rotBackCalf);
			LegArticulator.articulateLegPair(sc, heightFrontLeft, heightFrontRight, frontAvg, -frontAvg, frontLeftThigh, frontLeftCalf, frontRightThigh, frontRightCalf, rotFrontThigh, rotFrontCalf);
		}
	}

	private static void articulateLegPair(float sc, float heightLeft, float heightRight, float avg, float offsetY, AdvancedModelRenderer leftThigh, AdvancedModelRenderer leftCalf, AdvancedModelRenderer rightThigh, AdvancedModelRenderer rightCalf, float rotThigh, float rotCalf) {
		float difLeft = Math.max(0, heightRight - heightLeft);
		float difRight = Math.max(0, heightLeft - heightRight);
		leftThigh.rotationPointY += 16 / sc * (Math.max(heightLeft, avg) + offsetY);
		rightThigh.rotationPointY += 16 / sc * (Math.max(heightRight, avg) + offsetY);
		leftThigh.rotateAngleX -= rotThigh * difLeft;
		leftCalf.rotateAngleX += rotCalf * difLeft;
		rightThigh.rotateAngleX -= rotThigh * difRight;
		rightCalf.rotateAngleX += rotCalf * difRight;
	}

	private static float avg(float a, float b) {
		return (a + b) / 2;
	}

	private static float getScale(EntityDragonBase entity) {
		return entity.getRenderSize() / 3F;
	}

}
