package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;

public class TraitArrowKnockback extends AbstractProjectileTrait {

    public TraitArrowKnockback() {
        super("arrow_knockback", 0x248055);
    }

    @Override
    public void afterHit(EntityProjectileBase projectile, World world, ItemStack ammoStack, EntityLivingBase attacker, Entity target, double impactSpeed) {
        target.isAirBorne = true;
        double xRatio = projectile.motionX;
        double zRatio = projectile.motionZ;
        float strength = -1.4F;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
        target.motionX /= 2.0D;
        target.motionZ /= 2.0D;
        target.motionX -= xRatio / (double) f * (double) strength;
        target.motionZ -= zRatio / (double) f * (double) strength;
        target.motionY = 0.6D;
        spawnExplosionParticle(world, projectile);
    }

    public void spawnExplosionParticle(World world, Entity entity) {
        if (world.isRemote) {
            for (int height = 0; height < 1 + world.rand.nextInt(2); height++) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    double xRatio = entity.motionX * height;
                    double zRatio = entity.motionZ * height;
                    world.spawnParticle(EnumParticleTypes.CLOUD, entity.posX + xRatio + (double) (world.rand.nextFloat() * entity.width * 5.0F) - (double) entity.width - d0 * 10.0D, entity.posY + (double) (world.rand.nextFloat() * entity.height) - d1 * 10.0D + height, entity.posZ + zRatio + (double) (world.rand.nextFloat() * entity.width * 5.0F) - (double) entity.width - d2 * 10.0D, d0, d1, d2);
                }
            }
        }
    }


    @Override
    public void onMovement(EntityProjectileBase projectile, World world, double slowdown) {
        if ((projectile.ticksExisted == 1 || projectile.ticksExisted % 70 == 0) && !projectile.onGround) {
            projectile.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if (world.isRemote && !projectile.onGround) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = projectile.motionX * projectile.height;
            double zRatio = projectile.motionZ * projectile.height;
            world.spawnParticle(EnumParticleTypes.CLOUD, projectile.posX + xRatio + (double) (world.rand.nextFloat() * projectile.width * 1.0F) - (double) projectile.width - d0 * 10.0D, projectile.posY + (double) (world.rand.nextFloat() * projectile.height) - d1 * 10.0D, projectile.posZ + zRatio + (double) (world.rand.nextFloat() * projectile.width * 1.0F) - (double) projectile.width - d2 * 10.0D, d0, d1, d2);

        }
    }
}