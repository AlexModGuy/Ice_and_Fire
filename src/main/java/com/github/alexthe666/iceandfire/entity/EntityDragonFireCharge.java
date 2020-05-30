package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonFireCharge extends EntityFireball implements IDragonProjectile {

    public int ticksInAir;

    public EntityDragonFireCharge(World worldIn) {
        super(worldIn);

    }

    public EntityDragonFireCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
        double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public EntityDragonFireCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public void setSizes(float width, float height) {
        this.setSize(width, height);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        for (int i = 0; i < 4; ++i) {
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + ((this.rand.nextDouble() - 0.5D) * width), this.posY + ((this.rand.nextDouble() - 0.5D) * width), this.posZ + ((this.rand.nextDouble() - 0.5D) * width), 0.0D, 0.0D, 0.0D);
        }
        if (this.isInWater()) {
            setDead();
        }

        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.onUpdate();

            if (this.isFireballFiery()) {
                this.setFire(1);
            }

            ++this.ticksInAir;
            RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, false, this.ticksInAir >= 25, this.shootingEntity);

            if (raytraceresult != null) {
                this.onImpact(raytraceresult);
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }

                f = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double) f;
            this.motionY *= (double) f;
            this.motionZ *= (double) f;
            this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            this.setDead();
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingObject) {
        boolean flag = this.world.getGameRules().getBoolean("mobGriefing");

        if (!this.world.isRemote) {
            if (movingObject.entityHit != null && movingObject.entityHit instanceof IDragonProjectile) {
                return;
            }
            EntityLivingBase shootingEntity = this.shootingEntity;
            if(shootingEntity instanceof EntityDragonBase) {
                if (movingObject.entityHit != null && shootingEntity != null && shootingEntity instanceof EntityDragonBase && movingObject.entityHit != null) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (dragon.isOnSameTeam(movingObject.entityHit) || dragon.isEntityEqual(movingObject.entityHit) || dragon.isPart(movingObject.entityHit)) {
                        return;
                    }
                }
                if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (shootingEntity != null && (movingObject.entityHit == shootingEntity || (movingObject.entityHit instanceof EntityTameable && ((EntityDragonBase) shootingEntity).isOwner(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (shootingEntity != null && IceAndFire.CONFIG.dragonGriefing != 2) {
                        IafDragonDestructionManager.destroyAreaFireCharge(world, new BlockPos(posX, posY, posZ), dragon);
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.setDead();
                }
                if (movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
                    if (shootingEntity != null && (movingObject.entityHit.isEntityEqual(shootingEntity) || (shootingEntity instanceof EntityDragonBase & movingObject.entityHit instanceof EntityTameable && ((EntityDragonBase) shootingEntity).getOwner() == ((EntityTameable) movingObject.entityHit).getOwner()))) {
                        return;
                    }
                    if (shootingEntity != null && shootingEntity instanceof EntityDragonBase) {
                        movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 10.0F);
                        if (movingObject.entityHit instanceof EntityLivingBase && ((EntityLivingBase) movingObject.entityHit).getHealth() == 0) {
                            ((EntityDragonBase) shootingEntity).randomizeAttacks();
                        }
                    }
                    movingObject.entityHit.setFire(5);
                    this.applyEnchantments(shootingEntity, movingObject.entityHit);
                    if (shootingEntity instanceof EntityDragonBase) {
                        IafDragonDestructionManager.destroyAreaFireCharge(world, new BlockPos(posX, posY, posZ), ((EntityDragonBase) shootingEntity));
                    }
                    this.setDead();
                }
            }
        }
    }

    public void setAim(Entity fireball, Entity entity, float yaw, float pitch, float a, float b, float c) {
        float f = -MathHelper.sin(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
        float f1 = -MathHelper.sin(yaw * 0.017453292F);
        float f2 = MathHelper.cos(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
        this.setThrowableHeading(fireball, (double) f, (double) f1, (double) f2, b, c);
        fireball.motionX += entity.motionX;
        fireball.motionZ += entity.motionZ;

        if (!entity.onGround) {
            fireball.motionY += entity.motionY;
        }
    }

    public void setThrowableHeading(Entity fireball, double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        fireball.motionX = x;
        fireball.motionY = y;
        fireball.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        fireball.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        fireball.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
        fireball.prevRotationYaw = fireball.rotationYaw;
        fireball.prevRotationPitch = fireball.rotationPitch;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    public float getCollisionBorderSize() {
        return 0F;
    }

}