package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class SirenAIWander extends EntityAIWander {

	public SirenAIWander(EntityCreature creatureIn, double speedIn) {
		super(creatureIn, speedIn);
	}

	public boolean shouldExecute(){
		return !this.entity.isInWater() && super.shouldExecute();
	}

	public boolean shouldContinueExecuting(){
		return !this.entity.isInWater() && super.shouldContinueExecuting();
	}
}