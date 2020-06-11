package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class EntityMutlipartPart extends PartEntity {

    public EntityMutlipartPart(EntityType t, World world) {
        super(t, world);
    }

    public EntityMutlipartPart(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.MULTIPART, worldIn);
    }

    public EntityMutlipartPart(EntityType type, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(type, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public EntityMutlipartPart(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public boolean processInitialInteract(PlayerEntity player, Hand hand) {
        LivingEntity parent = getParent();
        if (world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getEntityId(), 0));
        }
        return parent.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        LivingEntity parent = getParent();
        if (world.isRemote && source.getTrueSource() instanceof PlayerEntity && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getEntityId(), damage * damageMultiplier));
        }
        return parent != null && parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public boolean shouldNotExist() {
        LivingEntity parent = getParent();
        return !parent.isAlive();
    }
}
