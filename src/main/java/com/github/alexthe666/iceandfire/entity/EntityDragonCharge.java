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
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public EntityDragonCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn,
                              EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.getShooter();
        if (this.world.isRemote || (shootingEntity == null || shootingEntity.isAlive()) && this.world.isBlockLoaded(this.getPosition())) {
            super.baseTick();

            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::canHitMob);

            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

            this.doBlockCollisions();
            Vector3d vector3d = this.getMotion();
            double d0 = this.getPosX() + vector3d.x;
            double d1 = this.getPosY() + vector3d.y;
            double d2 = this.getPosZ() + vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().x * 0.25D, this.getPosY() - this.getMotion().y * 0.25D, this.getPosZ() - this.getMotion().z * 0.25D, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }
                f = 0.8F;
            }
            this.setMotion(vector3d.add(this.accelerationX, this.accelerationY, this.accelerationZ).scale(f));
            this.world.addParticle(this.getParticle(), this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
            this.setPosition(d0, d1, d2);
        } else {
            this.remove();
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingObject) {
        Entity shootingEntity = this.getShooter();
        if (!this.world.isRemote) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

                if (entity instanceof IDragonProjectile) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (dragon.isOnSameTeam(entity) || dragon.isEntityEqual(entity) || dragon.isPart(entity)) {
                        return;
                    }
                }
                if (entity == null || !(entity instanceof IDragonProjectile) && entity != shootingEntity && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (shootingEntity != null && (entity == shootingEntity || (entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).isOwner(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.remove();
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.isEntityEqual(shootingEntity)) {
                    if (shootingEntity != null && (entity.isEntityEqual(shootingEntity) || (shootingEntity instanceof EntityDragonBase && entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).getOwner() == ((TameableEntity) entity).getOwner()))) {
                        return;
                    }
                    if (shootingEntity instanceof EntityDragonBase) {
                        float damageAmount = getDamage() * ((EntityDragonBase) shootingEntity).getDragonStage();

                        EntityDragonBase shootingDragon = (EntityDragonBase) shootingEntity;
                        Entity cause = shootingDragon.getRidingPlayer() != null ? shootingDragon.getRidingPlayer() : shootingDragon;
                        DamageSource source = causeDamage(cause);

                        entity.attackEntityFrom(source, damageAmount);
                        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() == 0) {
                            ((EntityDragonBase) shootingEntity).randomizeAttacks();
                        }
                    }
                    if (shootingEntity instanceof LivingEntity) {
                        this.applyEnchantments((LivingEntity) shootingEntity, entity);
                    }
                    this.remove();
                }
            }
            if (movingObject.getType() != RayTraceResult.Type.MISS) {
                if (shootingEntity instanceof EntityDragonBase && IafConfig.dragonGriefing != 2) {
                    destroyArea(world, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), ((EntityDragonBase) shootingEntity));
                }
                this.remove();
            }
        }

    }

    public abstract DamageSource causeDamage(@Nullable Entity cause);

    public abstract void destroyArea(World world, BlockPos center, EntityDragonBase destroyer);

    public abstract float getDamage();

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    protected boolean canHitMob(Entity hitMob) {
        Entity shooter = getShooter();
        return hitMob != this && super.func_230298_a_(hitMob) && !(shooter == null || hitMob.isOnSameTeam(shooter)) && !(hitMob instanceof EntityDragonPart);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getCollisionBorderSize() {
        return 0F;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
