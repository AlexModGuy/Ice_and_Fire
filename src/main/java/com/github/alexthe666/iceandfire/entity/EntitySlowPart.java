package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import net.minecraftforge.fml.network.FMLPlayMessages;

public class EntitySlowPart extends EntityMutlipartPart{

    public EntitySlowPart(EntityType<?> t, World world) {
        super(t, world);
    }

    public EntitySlowPart(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
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
