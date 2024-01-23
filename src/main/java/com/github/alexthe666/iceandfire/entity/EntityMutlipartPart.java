package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityMutlipartPart extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> SCALE_WIDTH = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SCALE_HEIGHT = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PART_YAW = SynchedEntityData.defineId(EntityMutlipartPart.class, EntityDataSerializers.FLOAT);
    public EntityDimensions multipartSize;
    protected float radius;
    protected float angleYaw;
    protected float offsetY;
    protected float damageMultiplier;

    protected EntityMutlipartPart(EntityType<?> t, Level world) {
        super(t, world);
        multipartSize = t.getDimensions();
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void doWaterSplashEffect() {

    }

    public EntityMutlipartPart(EntityType<?> t, Entity parent, float radius, float angleYaw, float offsetY, float sizeX,
        float sizeY, float damageMultiplier) {
        super(t, parent.level());
        this.setParent(parent);
        this.setScaleX(sizeX);
        this.setScaleY(sizeY);
        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }


    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 2D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.1D);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return new EntityDimensions(getScaleX(), getScaleY(), false);
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
        return this.entityData.get(SCALE_WIDTH);
    }

    private void setScaleX(float scale) {
        this.entityData.set(SCALE_WIDTH, scale);
    }

    private float getScaleY() {
        return this.entityData.get(SCALE_HEIGHT);
    }

    private void setScaleY(float scale) {
        this.entityData.set(SCALE_HEIGHT, scale);
    }

    public float getPartYaw() {
        return this.entityData.get(PART_YAW);
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
            if (parent != null && !level().isClientSide) {
                float renderYawOffset = parent.getYRot();
                if (parent instanceof LivingEntity) {
                    renderYawOffset = ((LivingEntity) parent).yBodyRot;
                }
                if (isSlowFollow()) {
                    this.setPos(parent.xo + this.radius * Mth.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.yo + this.offsetY, parent.zo + this.radius * Mth.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    double d0 = parent.getX() - this.getX();
                    double d1 = parent.getY() - this.getY();
                    double d2 = parent.getZ() - this.getZ();
                    float f2 = -((float) (Mth.atan2(d1, Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (180F / (float) Math.PI)));
                    this.setXRot(this.limitAngle(this.getXRot(), f2, 5.0F));
                    this.markHurt();
                    this.setYRot(renderYawOffset);
                    this.setPartYaw(getYRot());
                    if (!this.level().isClientSide) {
                        this.collideWithNearbyEntities();
                    }
                } else {
                    this.setPos(parent.getX() + this.radius * Mth.cos((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)), parent.getY() + this.offsetY, parent.getZ() + this.radius * Mth.sin((float) (renderYawOffset * (Math.PI / 180.0F) + this.angleYaw)));
                    this.markHurt();
                }
                if (!this.level().isClientSide) {
                    this.collideWithNearbyEntities();
                }
                if (parent.isRemoved() && !level().isClientSide) {
                    this.remove(RemovalReason.DISCARDED);
                }
            } else if (tickCount > 20 && !level().isClientSide) {
                remove(RemovalReason.DISCARDED);
            }
        }
        super.tick();
    }

    protected boolean isSlowFollow(){
        return false;
    }

    /** Source: {@link net.minecraft.world.entity.ai.control.MoveControl#rotlerp(float, float, float)} */
    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = Mth.wrapDegrees(targetAngle - sourceAngle);
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
    public void remove(@NotNull RemovalReason reason) {
        super.remove(RemovalReason.DISCARDED);
    }

    public Entity getParent() {
        UUID id = getParentId();

        if (id != null && level() instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(id);
        }

        return null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().expandTowards(0.20000000298023224D, 0.0D, 0.20000000298023224D));
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
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        Entity parent = getParent();
        if (level().isClientSide && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), 0));
        }
        return parent != null ? parent.interact(player, hand) : InteractionResult.PASS;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        Entity parent = getParent();
        if (level().isClientSide && source.getEntity() instanceof Player && parent != null) {
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageMultipartInteract(parent.getId(), damage * damageMultiplier));
        }
        return parent != null && parent.hurt(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(source);
    }

    public boolean shouldContinuePersisting() {
        return isAddedToWorld() || this.isRemoved();
    }
}
