package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class PartEntity extends Entity {
    private static final DataParameter<Integer> PARENT_ID = EntityDataManager.createKey(PartEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE_WIDTH = EntityDataManager.createKey(PartEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SCALE_HEIGHT = EntityDataManager.createKey(PartEntity.class, DataSerializers.FLOAT);
    public EntitySize multipartSize;
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
        this.setParent(parent);
        this.setScaleX(sizeX);
        this.setScaleY(sizeY);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return new EntitySize(getScaleX(), getScaleY(), false);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(PARENT_ID, Integer.valueOf(0));
        this.dataManager.register(SCALE_WIDTH, 0.5F);
        this.dataManager.register(SCALE_HEIGHT, 0.5F);
    }

    private int getParentId() {
        return this.dataManager.get(PARENT_ID).intValue();
    }

    private void setParentId(int parentId) {
        this.dataManager.set(PARENT_ID, parentId);
    }

    private float getScaleX() {
        return this.dataManager.get(SCALE_WIDTH).floatValue();
    }

    private void setScaleX(float scale) {
        this.dataManager.set(SCALE_WIDTH, scale);
    }

    private float getScaleY() {
        return this.dataManager.get(SCALE_HEIGHT).floatValue();
    }

    private void setScaleY(float scale) {
        this.dataManager.set(SCALE_HEIGHT, scale);
    }

    @Override
    public void tick() {
        LivingEntity parent = getParent();
        recalculateSize();
        if (parent != null && !world.isRemote) {
            this.setPosition(parent.getPosX() + this.radius * Math.cos(parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw), parent.getPosY() + this.offsetY, parent.getPosZ() + this.radius * Math.sin(parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw));
            this.markVelocityChanged();
            if (!this.world.isRemote) {
                this.collideWithNearbyEntities();
            }
            if (parent.removed && !world.isRemote) {
                this.remove();
            }
        } else if (ticksExisted > 20 && !world.isRemote) {
            remove();
        }
        super.tick();
    }

    public void remove() {
        this.remove(false);
    }

    public LivingEntity getParent() {
        int id = getParentId();
        Entity rawEntity = world.getEntityByID(id);
        return rawEntity instanceof LivingEntity ? (LivingEntity)rawEntity : null;
    }

    public void setParent(LivingEntity entity) {
        this.setParentId(entity.getEntityId());
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        LivingEntity parent = getParent();
        return parent != null && parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.getParent() == entity;
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

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public void collideWithNearbyEntities() {
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        Entity parent = this.getParent();
        if(parent != null){
            entities.stream().filter(entity -> entity != parent && !(entity instanceof PartEntity) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(parent));

        }
    }
}