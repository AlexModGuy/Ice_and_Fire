package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.*;
import net.ilexiconn.llibrary.client.util.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;

/**
 * @author rafa_mv
 * @since 1.0.0
 */
@SideOnly (Side.CLIENT)
public class RollBuffer {
	private int yawTimer;
	private float yawVariation;
	private float prevYawVariation;

	public void resetRotations () {
		this.yawVariation = 0.0F;
		this.prevYawVariation = 0.0F;
	}

	public void calculateChainFlapBuffer (float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity) {
		this.prevYawVariation = this.yawVariation;
		if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs (this.yawVariation) < maxAngle) {
			this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset) / divisor;
		}
		if (this.yawVariation > 0.7F * angleDecrement) {
			if (this.yawTimer > bufferTime) {
				this.yawVariation -= angleDecrement;
				if (MathHelper.abs (this.yawVariation) < angleDecrement) {
					this.yawVariation = 0.0F;
					this.yawTimer = 0;
				}
			} else {
				this.yawTimer++;
			}
		} else if (this.yawVariation < -0.7F * angleDecrement) {
			if (this.yawTimer > bufferTime) {
				this.yawVariation += angleDecrement;
				if (MathHelper.abs (this.yawVariation) < angleDecrement) {
					this.yawVariation = 0.0F;
					this.yawTimer = 0;
				}
			} else {
				this.yawTimer++;
			}
		}
	}

	public void calculateChainFlapBuffer (float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity) {
		this.calculateChainFlapBuffer (maxAngle, bufferTime, angleDecrement, 1.0F, entity);
	}

	public void applyChainFlapBuffer (ModelRenderer... boxes) {
		float rotateAmount = 0.01745329251F * ClientUtils.interpolate (this.prevYawVariation, this.yawVariation, LLibrary.PROXY.getPartialTicks ()) / boxes.length;
		for (ModelRenderer box : boxes) {
			box.rotateAngleZ += rotateAmount;
		}
	}
}