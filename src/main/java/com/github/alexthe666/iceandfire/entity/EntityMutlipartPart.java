package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityMutlipartPart extends Entity {

    private static final DataParameter<Optional<UUID>> PARENT_UUID = EntityDataManager.defineId(EntityMutlipartPart.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Float> SCALE_WIDTH = EntityDataManager.defineId(EntityMutlipartPart.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SCALE_HEIGHT = EntityDataManager.defineId(EntityMutlipartPart.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> PART_YAW = EntityDataManager.defineId(EntityMutlipartPart.class, DataSerializers.FLOAT);
    public EntitySize multipartSize;
    protected float radius;
    protected float angleYaw;
    protected float offsetY;
    protected float damageMultiplier;

    protected EntityMutlipartPart(EntityType<?> t, World world) {
        super(t, world);
        multipartSize = t.getDimensions();
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    protected void doWaterSplashEffect() {

    }

    public EntityMutlipartPart(EntityType<?> t, Entity parent, float radius, float angleYaw, float offsetY, float sizeX,
        float sizeY, float damageMultiplier) {
        super(t, parent.level);
        this.setParent(parent);
        this.setScaleX(sizeX);
        this.setScaleY(sizeY);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }


    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 2D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.1D);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return new EntitySize(getScaleX(), getScaleY(), false);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARENT_UUID, Optional.empty());
        this.entityData.define(SCALE_WIDTH, 0.5F);
        this.entityData.define(SCALE_HEIGHT, 0.5F);
        this.entityData.define(PART_YAW, 0F);
    }

    @Nullable
    public UUID getParentId() {
        return this.entityData.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.entityData.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    private float getScaleX() {
        return this.entityData.get(SCALE_WIDTH).floatValue();
    }

    private void setScaleX(float scale) {
        this.entityData.set(SCALE_WIDTH, scale);
    }

    private float getScaleY() {
        return this.entityData.get(SCALE_HEIGHT).floatValue();
    }

    private void setScaleY(float scale) {
        this.entityData.set(SCALE_HEIGHT, scale);
    }

    public float getPartYaw() {
        return this.entityData.get(PART_YAW).floatValue();
    }

    private void setPartYaw(float yaw) {
        this.entityData.set(PART_YAW, yaw % 360);
    }

    @Override
    public void tick() {
        wasTouchingWater = false;
        if (this.tickCount > 10) {
            Entity parent = getParent();
            refreshDimensions();
            if (parent != null && !level.isClientSide) {
                float renderYawOffset = parent.yRot;
                if (parent instanceof LivingEntity) {
                    renderYawOffset = ((LivingEntity) parent).yBodyRot;
                }
                if (isSlowFollow()) {
                    this.setPos(parent.xo + this.radius * MathHelper.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.yo + this.offsetY, parent.zo + this.radius * MathHelper.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    double d0 = parent.getX() - this.getX();
                    double d1 = parent.getY() - this.getY();
                    double d2 = parent.getZ() - this.getZ();
                    MathHelper.atan2(d2, d0);
                    float f2 = -((float) (MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (180F / (float) Math.PI)));
                    this.xRot = this.limitAngle(this.xRot, f2, 5.0F);
                    this.markHurt();
                    this.yRot = renderYawOffset;
                    this.setPartYaw(yRot);
                    if (!this.level.isClientSide) {
                        this.collideWithNearbyEntities();
                    }
                } else {
                    this.setPos(parent.getX() + this.radius * MathHelper.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.getY() + this.offsetY, parent.getZ() + this.radius * MathHelper.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    this.markHurt();
                }
                if (!this.level.isClientSide) {
                    this.collideWithNearbyEntities();
                }
                if (parent.removed && !level.isClientSide) {
                    this.remove();
                }
            } else if (tickCount > 20 && !level.isClientSide) {
                remove();
            }
        }
        super.tick();
    }

    protected boolean isSlowFollow(){
        return false;
    }

    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);
        if (f > maximumChange) {
            f = maximumChange;
        }

        if (f < -maximumChange) {
            f = -maximumChange;
        }

        float f1 = sourceAngle + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }

        return f1;
    }


    @Override
    public void remove() {
        this.remove(false);
    }

    public Entity getParent() {
        UUID id = getParentId();
        if (id != null && !level.isClientSide) {
            return ((ServerWorld) level).getEntity(id);
        }
        return null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().expandTowards(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !sharesRider(parent, entity) && !(entity instanceof EntityMutlipartPart) && entity.isPushable()).forEach(entity -> entity.push(parent));

        }
    }

    public static boolean sharesRider(Entity parent, Entity entityIn) {
        for (Entity entity : parent.getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }

            if (sharesRider(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        Entity parent = getParent();
        if (level.isClientSide && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), 0));
        }
        return parent != null ? parent.interact(player, hand) : ActionResultType.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity parent = getParent();
        if (level.isClientSide && source.getEntity() instanceof PlayerEntity && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), damage * damageMultiplier));
        }
        return parent != null && parent.hurt(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.FALL || source == DamageSource.DROWN || source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || source == DamageSource.LAVA || source.isFire() || super.isInvulnerableTo(source);
    }

    public boolean shouldNotExist() {
        Entity parent = getParent();
        return !parent.isAlive();
    }

    public boolean shouldContinuePersisting() {
        return isAddedToWorld() || this.removed;
    }
}
