package com.github.alexthe666.iceandfire.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;


public class EntitySlowPart extends EntityMutlipartPart{

    public EntitySlowPart(EntityType<?> t, Level world) {
        super(t, world);
    }

    public EntitySlowPart(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.SLOW_MULTIPART.get(), worldIn);
    }

    public EntitySlowPart(EntityType<?> t, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX,
        float sizeY, float damageMultiplier) {
        super(t, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public EntitySlowPart(Entity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.SLOW_MULTIPART.get(), parent, radius, angleYaw, offsetY, sizeX, sizeY,
            damageMultiplier);
    }

    @Override
    protected boolean isSlowFollow(){
        return true;
    }
}
