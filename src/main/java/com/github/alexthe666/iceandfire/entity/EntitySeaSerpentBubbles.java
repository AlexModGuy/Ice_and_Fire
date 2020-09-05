package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySeaSerpentBubbles extends AbstractFireballEntity implements IDragonProjectile {

    private int ticksInAir;

    public EntitySeaSerpentBubbles(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public EntitySeaSerpentBubbles(EntityType t, World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    public EntitySeaSerpentBubbles(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.SEA_SERPENT_BUBBLES, world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntitySeaSerpentBubbles(EntityType t, World worldIn, EntitySeaSerpent shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    public void tick() {
        super.tick();
        if(this.ticksExisted > 400 ){
            this.remove();
        }
        autoTarget();
        this.accelerationX *= 0.95F;
        this.accelerationY *= 0.95F;
        this.accelerationZ *= 0.95F;
        this.addVelocity(this.accelerationX, this.accelerationY, this.accelerationZ);

        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isAlive()) && this.world.isBlockLoaded(new BlockPos(this))) {
            if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.removed) && this.world.isBlockLoaded(new BlockPos(this))) {
                ++this.ticksInAir;
                RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, true, this.ticksInAir >= 25, this.shootingEntity, RayTraceContext.BlockMode.COLLIDER);
                if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onImpact(raytraceresult);
                }

                Vec3d vec3d = this.getMotion();
                double d0 = this.getPosX() + vec3d.x;
                double d1 = this.getPosY() + vec3d.y;
                double d2 = this.getPosZ() + vec3d.z;
                ProjectileHelper.rotateTowardsMovement(this, 0.2F);
                float f = this.getMotionFactor();
                if (this.world.isRemote) {
                    for (int i = 0; i < 3; ++i) {
                        IceAndFire.PROXY.spawnParticle("serpent_bubble", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, this.getPosY() - 0.5D, this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() * 0.5F, 0, 0, 0);
                    }
                }

                this.setMotion(vec3d.add(this.accelerationX, this.accelerationY, this.accelerationZ).scale((double)f));
                this.setPosition(d0, d1, d2);
            } else {
                this.remove();
            }
            ++this.ticksInAir;
            Vec3d vec3d = this.getMotion();
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), (p_213879_1_) -> {
                return !p_213879_1_.isSpectator() && p_213879_1_ != this.shootingEntity;
            }, RayTraceContext.BlockMode.OUTLINE, true);

            if (raytraceresult != null) {
                this.onImpact(raytraceresult);
            }

            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + vec3d.y;
            double d2 = this.getPosZ() + vec3d.z;
            float f = MathHelper.sqrt(horizontalMag(vec3d));
            this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
            for (this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }
            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
            float f1 = 0.99F;
            float f2 = 0.06F;
            this.setPosition(d0, d1, d2);
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        }
        this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        if(this.ticksExisted > 20 && !isWet()){
            this.remove();
        }
    }


    public void autoTarget() {
        if (this.shootingEntity instanceof EntitySeaSerpent && ((EntitySeaSerpent) this.shootingEntity).getAttackTarget() != null) {
            Entity target = ((EntitySeaSerpent) this.shootingEntity).getAttackTarget();
            double d2 = target.getPosX() - this.getPosX();
            double d3 = target.getPosY() - this.getPosY();
            double d4 = target.getPosZ() - this.getPosZ();
            double d0 = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            this.accelerationX = d2 / d0 * 0.1D;
            this.accelerationY = d3 / d0 * 0.1D;
            this.accelerationZ = d4 / d0 * 0.1D;
        }
    }

    public boolean handleWaterMovement() {
        return true;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.BUBBLE;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    public float getCollisionBorderSize() {
        return 0F;
    }

    @Override
    protected void onImpact(RayTraceResult movingObject) {
        boolean flag = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
        if (!this.world.isRemote) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

                if (entity != null && entity instanceof EntityDeathwormPart) {
                    return;
                }
                if (this.shootingEntity != null && this.shootingEntity instanceof EntitySeaSerpent) {
                    EntitySeaSerpent dragon = (EntitySeaSerpent) this.shootingEntity;
                    if (dragon.isOnSameTeam(entity) || dragon.isEntityEqual(entity)) {
                        return;
                    }
                    entity.attackEntityFrom(DamageSource.causeMobDamage(dragon), 6.0F);

                }
            }
        }
    }

}
