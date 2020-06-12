package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class EntityCyclopsEye extends EntityMutlipartPart {

    public EntityCyclopsEye(EntityType t, World world) {
        super(t, world);
    }

    public EntityCyclopsEye(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.CYCLOPS_MULTIPART, worldIn);
    }

    public EntityCyclopsEye(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.CYCLOPS_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.getParent() instanceof EntityCyclops && source.isProjectile()) {
            ((EntityCyclops) this.getParent()).onHitEye(source, damage);
            return true;
        } else {
            return this.getParent().attackEntityFrom(source, damage);
        }
    }
}
