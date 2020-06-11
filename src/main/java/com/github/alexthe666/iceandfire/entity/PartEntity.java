package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class PartEntity extends Entity {
    public EntitySize multipartSize;
    protected LivingEntity parent;
    protected float radius;
    protected float angleYaw;
    protected float offsetY;
    protected float damageMultiplier;

    public PartEntity(EntityType t, World world) {
        super(t, world);
        multipartSize = t.getSize();
    }

    public PartEntity(EntityType t, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(t, parent.world);
        this.parent = parent;
        this.multipartSize = new EntitySize(sizeX, sizeY, false);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return multipartSize;
    }

    @Override
    public void tick() {
        this.setPositionAndUpdate(this.parent.getPosX() + this.radius * Math.cos(this.parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw), this.parent.getPosY() + this.offsetY, this.parent.getPosZ() + this.radius * Math.sin(this.parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw));
        if (!this.world.isRemote) {
            this.collideWithNearbyEntities();
        }
        if (!this.parent.isAlive()) {
            this.remove();
        }
        super.tick();
    }

    @Override
    protected void registerData() {

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.parent == entity;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream().filter(entity -> entity != this.parent && !(entity instanceof PartEntity) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(this.parent));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }
}