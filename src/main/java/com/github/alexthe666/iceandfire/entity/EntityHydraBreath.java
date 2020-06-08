package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EntityHydraBreath extends AbstractFireballEntity implements IDragonProjectile {

    public EntityHydraBreath(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public EntityHydraBreath(EntityType t, World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntityHydraBreath(EntityType t, World worldIn, EntityHydra shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
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

    public void tick() {
        if (this.ticksExisted > 20) {
            this.remove();
        }
        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isAlive()) && this.world.isBlockLoaded(new BlockPos(this))) {
            if (!this.world.isRemote) {
                this.setFlag(6, this.isGlowing());
            }
            this.baseTick();
            Vec3d vec3d = this.getMotion();
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), (p_213879_1_) -> {
                return !p_213879_1_.isSpectator() && p_213879_1_ != this.shootingEntity;
            }, RayTraceContext.BlockMode.OUTLINE, true);

            if (raytraceresult != null) {
                this.onImpact(raytraceresult);
            }

            accelerationX *= 0.95D;
            accelerationY *= 0.95D;
            accelerationZ *= 0.95D;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            if (this.world.isRemote) {
                for (int i = 0; i < 15; ++i) {
                    IceAndFire.PROXY.spawnParticle("hydra", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, this.getPosY() - 0.5D, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, 0.1D, 1.0D, 0.1D);
                }
            }
            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + vec3d.y;
            double d2 = this.getPosZ() + vec3d.z;
            float f = MathHelper.sqrt(horizontalMag(vec3d));
            this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
            for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }
            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
            float f1 = 0.99F;
            float f2 = 0.06F;


            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().x * 0.25D, this.getPosY() - this.getMotion().y * 0.25D, this.getPosZ() - this.getMotion().z * 0.25D, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }

                f = 0.8F;
            }
            this.setPosition(d0, d1, d2);
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        } else {
            this.playSound(SoundEvents.ENTITY_HUSK_AMBIENT, 1F, this.rand.nextFloat());
            this.remove();
        }
    }

    public boolean handleWaterMovement() {
        return true;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                if (entity != null && !entity.isEntityEqual(this.shootingEntity) && !(entity instanceof EntityHydraHead) && !(entity instanceof EntityHydraBreath)) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 1F);
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 60, 0));
                    }
                }
            }
        }
        this.remove();
    }
}

