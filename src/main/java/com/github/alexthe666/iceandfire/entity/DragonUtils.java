package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;
import java.util.List;

public class DragonUtils {

	public static BlockPos getBlockInView(EntityDragonBase dragon) {
		float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * - 7 - dragon.getRNG().nextInt(dragon.getDragonStage() * 6);
		float neg = dragon.getRNG().nextBoolean() ? 1 : -1;
		float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (dragon.getRNG().nextFloat() * neg);
		double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
		double extraZ = (double) (radius * MathHelper.cos(angle));
		BlockPos radialPos = new BlockPos(dragon.posX + extraX, 0, dragon.posZ + extraZ);
		BlockPos ground = dragon.world.getHeight(radialPos);
		int distFromGround = (int) dragon.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFire.CONFIG.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int) dragon.posY + dragon.getRNG().nextInt(16) + 1);
		BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
		if (!dragon.isTargetBlocked(new Vec3d(newPos)) && dragon.getDistanceSqToCenter(newPos) > 6) {
			return newPos;
		}
		return null;
	}

	public static BlockPos getWaterBlockInView(EntityDragonBase dragon) {
		float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * - 7 - dragon.getRNG().nextInt(dragon.getDragonStage() * 6);
		float neg = dragon.getRNG().nextBoolean() ? 1 : -1;
		float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (dragon.getRNG().nextFloat() * neg);
		double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
		double extraZ = (double) (radius * MathHelper.cos(angle));
		BlockPos radialPos = new BlockPos(dragon.posX + extraX, 0, dragon.posZ + extraZ);
		BlockPos ground = dragon.world.getHeight(radialPos);
		int distFromGround = (int) dragon.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFire.CONFIG.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int) dragon.posY + dragon.getRNG().nextInt(16) + 1);
		BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
		BlockPos surface = dragon.world.getBlockState(newPos.down(2)).getMaterial() != Material.WATER ? newPos.down(dragon.getRNG().nextInt(10) + 1) : newPos;
		if ( dragon.getDistanceSqToCenter(surface) > 6 && dragon.world.getBlockState(surface).getMaterial() == Material.WATER) {
			return surface;
		}
		return null;
	}

	public static EntityLivingBase riderLookingAtEntity(EntityLivingBase rider, double dist) {
		Vec3d vec3d = rider.getPositionEyes(1.0F);
		Vec3d vec3d1 = rider.getLook(1.0F);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist);
		double d1 = dist;
		Entity pointedEntity = null;
		List<Entity> list = rider.world.getEntitiesInAABBexcluding(rider, rider.getEntityBoundingBox().expand(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(@Nullable Entity entity) {
				return entity != null && entity.canBeCollidedWith() && entity instanceof EntityLivingBase;
			}
		}));
		double d2 = d1;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity) list.get(j);
			AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
			RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

			if (axisalignedbb.contains(vec3d)) {
				if (d2 >= 0.0D) {
					pointedEntity = entity1;
					d2 = 0.0D;
				}
			} else if (raytraceresult != null) {
				double d3 = vec3d.distanceTo(raytraceresult.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					if (entity1.getLowestRidingEntity() == rider.getLowestRidingEntity() && !rider.canRiderInteract()) {
						if (d2 == 0.0D) {
							pointedEntity = entity1;
						}
					} else {
						pointedEntity = entity1;
						d2 = d3;
					}
				}
			}
		}
		return (EntityLivingBase) pointedEntity;
	}

	public static BlockPos getBlockInViewHippogryph(EntityHippogryph hippo) {
		float radius = 0.75F * (0.7F * 8) * -3 - hippo.getRNG().nextInt(8 * 6);
		float neg = hippo.getRNG().nextBoolean() ? 1 : -1;
		float angle = (0.01745329251F * hippo.renderYawOffset) + 3.15F + (hippo.getRNG().nextFloat() * neg);
		double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
		double extraZ = (double) (radius * MathHelper.cos(angle));
		BlockPos radialPos = new BlockPos(hippo.posX + extraX, 0, hippo.posZ + extraZ);
		BlockPos ground = hippo.world.getHeight(radialPos);
		int distFromGround = (int) hippo.posY - ground.getY();
		BlockPos newPos = radialPos.up(distFromGround > 16 ? (int) Math.min(IceAndFire.CONFIG.maxDragonFlight, hippo.posY + hippo.getRNG().nextInt(16) - 8) : (int) hippo.posY + hippo.getRNG().nextInt(16) + 1);
		BlockPos pos = hippo.doesWantToLand() ? ground : newPos;
		if (!hippo.isTargetBlocked(new Vec3d(newPos)) && hippo.getDistanceSqToCenter(newPos) > 6) {
			return newPos;
		}
		return null;
	}
}
