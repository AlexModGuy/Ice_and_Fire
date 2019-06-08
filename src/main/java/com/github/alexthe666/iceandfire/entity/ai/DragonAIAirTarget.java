package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DragonAIAirTarget extends EntityAIBase {
	private EntityDragonBase dragon;
	private World theWorld;

	public DragonAIAirTarget(EntityDragonBase dragon) {
		this.dragon = dragon;
		this.theWorld = dragon.world;
	}

	public boolean shouldExecute() {
		if (dragon != null) {
			if (!dragon.isFlying() && !dragon.isHovering() || dragon.onGround) {
				return false;
			}
			if (dragon.isSleeping()) {
				return false;
			}
			if (dragon.isChild()) {
				return false;
			}
			if (dragon.getOwner() != null && dragon.getPassengers().contains(dragon.getOwner())) {
				return false;
			}
			if (dragon.airTarget != null && (dragon.isTargetBlocked(new Vec3d(dragon.airTarget)))) {
				dragon.airTarget = null;
			}

			if (dragon.airTarget != null) {
				return false;
			} else {
				Vec3d vec = this.findAirTarget();

				if (vec == null) {
					return false;
				} else {
					dragon.airTarget = new BlockPos(vec.x, vec.y, vec.z);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(dragon, StoneEntityProperties.class);
		if (!dragon.isFlying() && !dragon.isHovering()) {
			return false;
		}
		if (dragon.isSleeping()) {
			return false;
		}
		if (dragon.isChild()) {
			return false;
		}
		if (properties != null && properties.isStone) {
			return false;
		}
		return dragon.airTarget != null;
	}

	public Vec3d findAirTarget() {
		return new Vec3d(getNearbyAirTarget());
	}

	public BlockPos getNearbyAirTarget() {
		if (dragon.getAttackTarget() == null) {
			BlockPos pos = DragonUtils.getBlockInView(dragon);
			if (pos != null && dragon.world.getBlockState(pos).getMaterial() == Material.AIR) {
				return pos;
			}
		} else {
			return new BlockPos((int) dragon.getAttackTarget().posX, (int) dragon.getAttackTarget().posY, (int) dragon.getAttackTarget().posZ);
		}
		return dragon.getPosition();
	}


}