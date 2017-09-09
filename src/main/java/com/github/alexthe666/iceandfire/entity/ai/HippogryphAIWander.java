package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class HippogryphAIWander extends EntityAIBase {
	private EntityHippogryph hippo;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;
	private int executionChance;
	private boolean mustUpdate;

	public HippogryphAIWander(EntityHippogryph creatureIn, double speedIn) {
		this(creatureIn, speedIn, 20);
	}

	public HippogryphAIWander(EntityHippogryph creatureIn, double speedIn, int chance) {
		this.hippo = creatureIn;
		this.speed = speedIn;
		this.executionChance = chance;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!hippo.canMove()) {
			return false;
		}
		if (hippo.isFlying() || hippo.isHovering()) {
			return false;
		}
		if (!this.mustUpdate) {
			if (this.hippo.getRNG().nextInt(executionChance) != 0) {
				return false;
			}
		}
		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.hippo, 10, 7);
		if (vec3d == null) {
			return false;
		} else {
			this.xPosition = vec3d.x;
			this.yPosition = vec3d.y;
			this.zPosition = vec3d.z;
			this.mustUpdate = false;

			return true;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.hippo.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		this.hippo.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}

	public void makeUpdate() {
		this.mustUpdate = true;
	}

	public void setExecutionChance(int newchance) {
		this.executionChance = newchance;
	}
}