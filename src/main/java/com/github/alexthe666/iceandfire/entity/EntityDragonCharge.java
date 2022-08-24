package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class EntityDragonCharge extends AbstractFireballEntity implements IDragonProjectile {


    public EntityDragonCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDragonCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX,
                              double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
    }

    public EntityDragonCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn,
                              EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.level.isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level.hasChunkAt(this.blockPosition())) {
            super.baseTick();

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitMob);

            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
                f = 0.8F;
            }
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.level.addParticle(this.getTrailParticle(), this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.remove();
        }
    }

    @Override
    protected void onHit(RayTraceResult movingObject) {
        Entity shootingEntity = this.getOwner();
        if (!this.level.isClientSide) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

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
                    if (shootingEntity != null && (entity == shootingEntity || (entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).isOwnedBy(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.remove();
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.is(shootingEntity)) {
                    if (shootingEntity != null && (entity.is(shootingEntity) || (shootingEntity instanceof EntityDragonBase & entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).getOwner() == ((TameableEntity) entity).getOwner()))) {
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
                    this.remove();
                }
            }
            if (movingObject.getType() != RayTraceResult.Type.MISS) {
                if (shootingEntity instanceof EntityDragonBase && IafConfig.dragonGriefing != 2) {
                    destroyArea(level, new BlockPos(this.getX(), this.getY(), this.getZ()), ((EntityDragonBase) shootingEntity));
                }
                this.remove();
            }
        }

    }

    public abstract DamageSource causeDamage(@Nullable Entity cause);

    public abstract void destroyArea(World world, BlockPos center, EntityDragonBase destroyer);

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
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0F;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
