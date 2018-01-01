package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCyclopsEye extends PartEntity {


    public EntityCyclopsEye(EntityLiving parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(this.parent instanceof EntityCyclops){
            ((EntityCyclops)this.parent).onHitEye(source, damage);
            return true;

        }else {
            return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
        }
    }
}
