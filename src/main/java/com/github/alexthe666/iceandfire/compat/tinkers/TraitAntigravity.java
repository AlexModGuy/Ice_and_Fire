package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import javax.annotation.Nullable;

public class TraitAntigravity extends AbstractProjectileTrait {

    public TraitAntigravity() {
        super("antigravity", 0x7E5C45);
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter) {
    }

    @Override
    public void onMovement(EntityProjectileBase projectile, World world, double slowdown) {
        projectile.setNoGravity(true);
        float sqrt = MathHelper.sqrt(projectile.motionX * projectile.motionX + projectile.motionZ * projectile.motionZ);
        if (sqrt < 0.1F) {
            projectile.motionY -= 0.01F;
        }
    }
}