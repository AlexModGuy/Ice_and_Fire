package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class EntityDeathwormPart extends EntityMutlipartPart{
    public EntityDeathwormPart(EntityType t, World world) {
        super(t, world);
    }

    public EntityDeathwormPart(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.DEATHWORM_MULTIPART, worldIn);
    }

    public EntityDeathwormPart(EntityType t, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(t, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public EntityDeathwormPart(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.DEATHWORM_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }
}
