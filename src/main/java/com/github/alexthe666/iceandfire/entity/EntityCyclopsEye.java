package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

public class EntityCyclopsEye extends PartEntity {


    public EntityCyclopsEye(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.ENTITY_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.parent instanceof EntityCyclops && source.isProjectile()) {
            ((EntityCyclops) this.parent).onHitEye(source, damage);
            return true;

        } else {
            return this.parent.attackEntityFrom(source, damage);
        }
    }
}
