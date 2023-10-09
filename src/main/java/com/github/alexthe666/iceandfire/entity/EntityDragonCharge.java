package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class EntityDragonCharge extends Fireball implements IDragonProjectile {


    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn, double posX,
                              double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn,
                              EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.level().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level().hasChunkAt(this.blockPosition())) {
            super.baseTick();

            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitMob);

            if (raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
                f = 0.8F;
            }
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.level().addParticle(this.getTrailParticle(), this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        Entity shootingEntity = this.getOwner();
        if (!this.level().isClientSide) {
            if (movingObject.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();

                if (entity instanceof IDragonProjectile) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity) || dragon.isPart(entity)) {
                        return;
                    }
                }
                if (entity == null || !(entity instanceof IDragonProjectile) && entity != shootingEntity && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (shootingEntity != null && (entity == shootingEntity || (entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).isOwnedBy(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.remove(RemovalReason.DISCARDED);
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.is(shootingEntity)) {
                    if (shootingEntity != null && (entity.is(shootingEntity) || (shootingEntity instanceof EntityDragonBase && entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).getOwner() == ((TamableAnimal) entity).getOwner()))) {
                        return;
                    }
                    if (shootingEntity instanceof EntityDragonBase) {
                        float damageAmount = getDamage() * ((EntityDragonBase) shootingEntity).getDragonStage();

                        EntityDragonBase shootingDragon = (EntityDragonBase) shootingEntity;
                        Entity cause = shootingDragon.getRidingPlayer() != null ? shootingDragon.getRidingPlayer() : shootingDragon;
                        DamageSource source = causeDamage(cause);

                        entity.hurt(source, damageAmount);
                        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() == 0) {
                            ((EntityDragonBase) shootingEntity).randomizeAttacks();
                        }
                    }
                    if (shootingEntity instanceof LivingEntity) {
                        this.doEnchantDamageEffects((LivingEntity) shootingEntity, entity);
                    }
                    this.remove(RemovalReason.DISCARDED);
                }
            }
            if (movingObject.getType() != HitResult.Type.MISS) {
                if (shootingEntity instanceof EntityDragonBase && DragonUtils.canGrief((EntityDragonBase) shootingEntity)) {
                    destroyArea(level(), BlockPos.containing(this.getX(), this.getY(), this.getZ()), ((EntityDragonBase) shootingEntity));
                }
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    public abstract DamageSource causeDamage(@Nullable Entity cause);

    public abstract void destroyArea(Level world, BlockPos center, EntityDragonBase destroyer);

    public abstract float getDamage();

    @Override
    public boolean isPickable() {
        return false;
    }

    protected boolean canHitMob(Entity hitMob) {
        Entity shooter = getOwner();
        return hitMob != this && super.canHitEntity(hitMob) && !(shooter == null || hitMob.isAlliedTo(shooter)) && !(hitMob instanceof EntityDragonPart);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0F;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
