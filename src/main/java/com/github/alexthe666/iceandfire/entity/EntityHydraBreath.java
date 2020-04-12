package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityHydraBreath extends EntityFireball implements IDragonProjectile {

    public EntityHydraBreath(World worldIn) {
        super(worldIn);
    }

    public EntityHydraBreath(World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityHydraBreath(World worldIn, EntityHydra shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.95F, 0.95F);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void onUpdate() {
        if (this.ticksExisted > 20) {
            this.setDead();
        }
        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this))) {
            if (!this.world.isRemote) {
                this.setFlag(6, this.isGlowing());
            }
            this.onEntityUpdate();
            RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, false, this.shootingEntity);
            if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

            accelerationX *= 0.95D;
            accelerationY *= 0.95D;
            accelerationZ *= 0.95D;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();
            if (this.world.isRemote) {
                for (int i = 0; i < 15; ++i) {
                    IceAndFire.PROXY.spawnParticle("hydra", this.posX + (double) (this.rand.nextFloat() * this.width) - (double) this.width * 0.5F, this.posY - 0.5D, this.posZ + (double) (this.rand.nextFloat() * this.width) - (double) this.width * 0.5F, 0.1D, 1.0D, 0.1D);
                }
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double)f;
            this.motionY *= (double)f;
            this.motionZ *= (double)f;
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            //this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            this.playSound(SoundEvents.ENTITY_HUSK_AMBIENT, 1F, this.rand.nextFloat());
            this.setDead();
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
        if (result.entityHit != null && !result.entityHit.isEntityEqual(this.shootingEntity) && !(result.entityHit instanceof EntityHydraHead) && !(result.entityHit instanceof EntityHydraBreath)) {
            result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 1F);
            if(result.entityHit instanceof EntityLivingBase){
                ((EntityLivingBase) result.entityHit).addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 0));
            }
            this.setDead();
        }
    }
}

