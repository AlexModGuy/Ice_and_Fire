package com.github.alexthe666.iceandfire.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class EntityCyclopsEye extends EntityMutlipartPart {

    public EntityCyclopsEye(EntityType<?> t, Level world) {
        super(t, world);
    }

    public EntityCyclopsEye(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.CYCLOPS_MULTIPART.get(), worldIn);
    }

    public EntityCyclopsEye(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.CYCLOPS_MULTIPART.get(), parent, radius, angleYaw, offsetY, sizeX, sizeY,
            damageMultiplier);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity parent = this.getParent();
        // TODO :: 1.19.2 -> check isProjectile()?
        if (parent instanceof EntityCyclops entityCyclops && (source.getDirectEntity() instanceof Arrow || source.getMsgId().contains("arrow"))) {
            entityCyclops.onHitEye(source, damage);
            return true;
        } else {
            return parent != null && parent.hurt(source, damage);
        }
    }
}
