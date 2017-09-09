package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.core.ModAchievements;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class DragonAIMate extends EntityAIBase {
	private final EntityDragonBase dragon;
	World theWorld;
	int spawnBabyDelay;
	double moveSpeed;
	private EntityDragonBase targetMate;

	public DragonAIMate(EntityDragonBase dragon, double speedIn) {
		this.dragon = dragon;
		this.theWorld = dragon.world;
		this.moveSpeed = speedIn;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		if (!this.dragon.isInLove()) {
			return false;
		} else {
			this.targetMate = this.getNearbyMate();
			return this.targetMate != null;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		this.dragon.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.dragon.getVerticalFaceSpeed());
		this.dragon.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
		++this.spawnBabyDelay;
		if (this.spawnBabyDelay >= 60 && this.dragon.getDistanceSqToEntity(this.targetMate) < 18) {
			this.spawnBaby();
		}
	}

	/**
	 * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
	 * valid mate found.
	 */
	private EntityDragonBase getNearbyMate() {
		List<EntityDragonBase> list = this.theWorld.<EntityDragonBase>getEntitiesWithinAABB(this.dragon.getClass(), this.dragon.getEntityBoundingBox().expand(18.0D, 18.0D, 18.0D));
		double d0 = Double.MAX_VALUE;
		EntityDragonBase mate = null;
		for (EntityDragonBase partner : list) {
			if (this.dragon.canMateWith(partner) && this.dragon.getDistanceSqToEntity(partner) < d0) {
				mate = partner;
				break;
			}
		}

		return mate;
	}

	/**
	 * Spawns a baby animal of the same type.
	 */
	private void spawnBaby() {

		EntityDragonEgg egg = this.dragon.createEgg(this.targetMate);

		if (egg != null) {
			EntityPlayer entityplayer = this.dragon.getPlayerInLove();

			if (entityplayer == null && this.targetMate.getPlayerInLove() != null) {
				entityplayer = this.targetMate.getPlayerInLove();
			}

			if (entityplayer != null) {
				entityplayer.addStat(StatList.ANIMALS_BRED);
				entityplayer.addStat(ModAchievements.dragonBreed);
			}

			this.dragon.setGrowingAge(6000);
			this.targetMate.setGrowingAge(6000);
			this.dragon.resetInLove();
			this.targetMate.resetInLove();
			egg.setLocationAndAngles(this.dragon.posX, this.dragon.posY, this.dragon.posZ, 0.0F, 0.0F);
			this.theWorld.spawnEntity(egg);
			Random random = this.dragon.getRNG();

			for (int i = 0; i < 17; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextDouble() * (double) this.dragon.width * 2.0D - (double) this.dragon.width;
				double d4 = 0.5D + random.nextDouble() * (double) this.dragon.height;
				double d5 = random.nextDouble() * (double) this.dragon.width * 2.0D - (double) this.dragon.width;
				this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.dragon.posX + d3, this.dragon.posY + d4, this.dragon.posZ + d5, d0, d1, d2, new int[0]);
			}

			if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
				this.theWorld.spawnEntity(new EntityXPOrb(this.theWorld, this.dragon.posX, this.dragon.posY, this.dragon.posZ, random.nextInt(15) + 10));
			}
		}
	}
}