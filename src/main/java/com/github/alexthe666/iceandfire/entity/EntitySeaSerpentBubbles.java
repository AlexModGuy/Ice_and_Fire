package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySeaSerpentBubbles extends EntityFireball implements IDragonProjectile {


    public EntitySeaSerpentBubbles(World worldIn) {
        super(worldIn);
    }

    public EntitySeaSerpentBubbles(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntitySeaSerpentBubbles(World worldIn, EntitySeaSerpent shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.5F, 0.5F);
        double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        if (this.ticksExisted > 60) {
            this.setDead();
        }
        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
            autoTarget();
            super.onUpdate();
            RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, false, this.shootingEntity);
            if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater()) {
                if (this.world.isRemote) {
                    for (int i = 0; i < 6; ++i) {
                        IceAndFire.PROXY.spawnParticle("serpent_bubble", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1F, this.rand.nextFloat());
                this.setDead();
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double) f;
            this.motionY *= (double) f;
            this.motionZ *= (double) f;
            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1F, this.rand.nextFloat());
            this.setDead();
        }
    }

    public void autoTarget() {
        if (this.shootingEntity instanceof EntitySeaSerpent && ((EntitySeaSerpent) this.shootingEntity).getAttackTarget() != null) {
            Entity target = ((EntitySeaSerpent) this.shootingEntity).getAttackTarget();
            double d2 = target.posX - posX;
            double d3 = target.posY - posY;
            double d4 = target.posZ - posZ;
            double d0 = (double) MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            this.accelerationX = d2 / d0 * 0.1D;
            this.accelerationY = d3 / d0 * 0.1D;
            this.accelerationZ = d4 / d0 * 0.1D;
        }
    }

    public boolean isInWater() {
        return this.isInsideOfMaterial(Material.WATER);
    }

    public boolean handleWaterMovement() {
        return true;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.WATER_SPLASH;
    }


    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null && !result.entityHit.isEntityEqual(this.shootingEntity)) {
            result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 1F);
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1F, this.rand.nextFloat());
            this.setDead();
        }
    }

    public void setSizes(float width, float height) {
        this.setSize(width, height);
    }

}
