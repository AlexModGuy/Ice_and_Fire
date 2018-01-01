package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class HippogryphAIAirTarget extends EntityAIBase {
	private EntityHippogryph hippogryph;

	public HippogryphAIAirTarget(EntityHippogryph dragon) {
		this.hippogryph = dragon;
	}

	public boolean shouldExecute() {
		if (hippogryph != null) {
			if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
				return false;
			}
			if (hippogryph.isSitting()) {
				return false;
			}
			if (hippogryph.isChild()) {
				return false;
			}
			if (hippogryph.getOwner() != null && hippogryph.getPassengers().contains(hippogryph.getOwner())) {
				return false;
			}
			if (hippogryph.airTarget != null && hippogryph.getDistanceSquared(new Vec3d(hippogryph.airTarget.getX(), hippogryph.posY, hippogryph.airTarget.getZ())) > 3) {
				hippogryph.airTarget = null;
			}

			if (hippogryph.airTarget != null) {
				return false;
			} else {
				Vec3d vec = this.findAirTarget();

				if (vec == null) {
					return false;
				} else {
					hippogryph.airTarget = new BlockPos(vec.x, vec.y, vec.z);
					return true;
				}
			}
		}
		return false;
	}

	public boolean continueExecuting() {
		StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(hippogryph, StoneEntityProperties.class);
		if (!hippogryph.isFlying() && !hippogryph.isHovering()) {
			return false;
		}
		if (hippogryph.isSitting()) {
			return false;
		}
		if (hippogryph.isChild()) {
			return false;
		}
		if (hippogryph.isChild()) {
			return false;
		}
		if (properties != null && properties.isStone) {
			return false;
		}
		return hippogryph.airTarget != null;
	}

	public Vec3d findAirTarget() {
		return new Vec3d(getNearbyAirTarget());
	}

	public BlockPos getNearbyAirTarget() {
		Random random = this.hippogryph.getRNG();

		if (hippogryph.getAttackTarget() == null) {
			for (int i = 0; i < 10; i++) {
				BlockPos pos = DragonUtils.getBlockInViewHippogryph(hippogryph);
				if (pos != null && hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
					return pos;
				}
			}
		} else {
			BlockPos pos = new BlockPos((int) hippogryph.getAttackTarget().posX, (int) hippogryph.getAttackTarget().posY, (int) hippogryph.getAttackTarget().posZ);
			if (hippogryph.world.getBlockState(pos).getMaterial() == Material.AIR) {
				return pos;
			}
		}
		return hippogryph.getPosition();
	}

}