package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EntityMutlipartPart extends PartEntity {

    public EntityMutlipartPart(EntityType t, World world) {
        super(t, world);
    }

    public EntityMutlipartPart(EntityType type, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(type, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public EntityMutlipartPart(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public boolean processInitialInteract(PlayerEntity player, Hand hand) {
        if (world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(this.parent.getEntityId(), 0));
        }
        return this.parent.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(world.isRemote && source.getTrueSource() instanceof PlayerEntity) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(this.parent.getEntityId(), damage * damageMultiplier));
        }
        return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    public LivingEntity getParent() {
        return this.parent;
    }


    @Override
    public void tick() {
        super.tick();
        if (this.parent == null || shouldNotExist()) {
            this.remove();
        }
    }

    public boolean shouldNotExist() {
        return !this.parent.isAlive();
    }
}
