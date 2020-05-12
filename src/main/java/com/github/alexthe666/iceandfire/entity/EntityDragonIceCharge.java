package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonIceCharge extends AbstractFireballEntity implements IDragonProjectile {

    public int ticksInAir;

    public EntityDragonIceCharge(World worldIn) {
        super(worldIn);

    }

    public EntityDragonIceCharge(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public EntityDragonIceCharge(World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public void setSizes(float getWidth(), float height) {
        this.setSize(getWidth(), height);
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 14; ++i) {
                IceAndFire.PROXY.spawnParticle("dragonice", this.getPosX() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosY() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosZ() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
            }
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

            this.getPosX() += this.motionX;
            this.getPosY() += this.motionY;
            this.getPosZ() += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.world.spawnParticle(ParticleTypes.WATER_BUBBLE, this.getPosX() - this.motionX * 0.25D, this.getPosY() - this.motionY * 0.25D, this.getPosZ() - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }

                f = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
            this.world.spawnParticle(this.getParticleType(), this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
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
            if (movingObject.entityHit != null && this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && ((EntityDragonBase) this.shootingEntity).isTamed() && movingObject.entityHit instanceof PlayerEntity && ((EntityDragonBase) this.shootingEntity).isOwner((PlayerEntity) movingObject.entityHit)) {
                return;
            }
            if (movingObject.entityHit == null || !(movingObject.entityHit instanceof IDragonProjectile) && movingObject.entityHit != shootingEntity) {
                if (this.shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) this.shootingEntity;
                    if (this.shootingEntity != null && (movingObject.entityHit == this.shootingEntity || (movingObject.entityHit instanceof TameableEntity && ((EntityDragonBase) shootingEntity).isOwner(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (this.shootingEntity != null && IafConfig.dragonGriefing != 2) {
                        IafDragonDestructionManager.destroyAreaIceCharge(world, new BlockPos(posX, posY, posZ), ((EntityDragonBase) this.shootingEntity));
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                }
                this.setDead();
            }
            if (movingObject.entityHit != null && !(movingObject.entityHit instanceof IDragonProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
                if (this.shootingEntity != null && (!movingObject.entityHit.isEntityEqual(shootingEntity) || (this.shootingEntity instanceof EntityDragonBase & movingObject.entityHit instanceof TameableEntity && ((EntityDragonBase) shootingEntity).getOwner() == ((TameableEntity) movingObject.entityHit).getOwner()))) {
                    return;
                }
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
                    movingObject.entityHit.attackEntityFrom(IceAndFire.dragonFire, 10.0F);
                    if (movingObject.entityHit instanceof LivingEntity) {
                        FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(movingObject.entityHit, FrozenEntityProperties.class);
                        if (frozenProps != null) {
                            frozenProps.setFrozenFor(200);
                        }
                    }
                    if (movingObject.entityHit instanceof LivingEntity && ((LivingEntity) movingObject.entityHit).getHealth() == 0) {
                        ((EntityDragonBase) this.shootingEntity).usingGroundAttack = true;
                    }
                }
                if (movingObject.entityHit.isDead && movingObject.entityHit instanceof PlayerEntity) {
                    //((PlayerEntity) movingObject.entityHit).addStat(ModAchievements.dragonKill, 1);
                }
                this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
                if (this.shootingEntity instanceof EntityDragonBase) {
                    IafDragonDestructionManager.destroyAreaIceCharge(world, new BlockPos(posX, posY, posZ), ((EntityDragonBase) this.shootingEntity));
                }
                this.setDead();
            }
        }
        this.setDead();
    }

    public void setAim(Entity fireball, Entity entity, float yaw, float pitch, float a, float b, float c) {
        float f = -MathHelper.sin(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
        float f1 = -MathHelper.sin(yaw * 0.017453292F);
        float f2 = MathHelper.cos(pitch * 0.017453292F) * MathHelper.cos(yaw * 0.017453292F);
        this.setThrowableHeading(fireball, f, f1, f2, b, c);
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
        fireball.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
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