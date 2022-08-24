package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityHydraBreath extends AbstractFireballEntity implements IDragonProjectile {

    public EntityHydraBreath(EntityType<? extends AbstractFireballEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public EntityHydraBreath(EntityType<? extends AbstractFireballEntity> t, World worldIn, double posX, double posY,
                             double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntityHydraBreath(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.HYDRA_BREATH.get(), worldIn);
    }

    public EntityHydraBreath(EntityType<? extends AbstractFireballEntity> t, World worldIn, EntityHydra shooter,
                             double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.02D;
        this.yPower = accelY / d0 * 0.02D;
        this.zPower = accelZ / d0 * 0.02D;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
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
    public boolean isPickable() {
        return false;
    }


    @Override
    public void tick() {
        this.clearFire();
        if (this.tickCount > 30) {
            this.remove();
        }
        Entity shootingEntity = this.getOwner();
        if (this.level.isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level.hasChunkAt(this.blockPosition())) {
            this.baseTick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            Vector3d Vector3d = this.getDeltaMovement();
            double d0 = this.getX() + Vector3d.x;
            double d1 = this.getY() + Vector3d.y;
            double d2 = this.getZ() + Vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.level.isClientSide) {
                for (int i = 0; i < 15; ++i) {
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, this.getY() - 0.5D, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, 0.1D, 1.0D, 0.1D);
                }
            }

            this.setDeltaMovement(Vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));

            this.xPower *= 0.95F;
            this.yPower *= 0.95F;
            this.zPower *= 0.95F;
            this.push(this.xPower, this.yPower, this.zPower);

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
            }
            this.setPos(d0, d1, d2);
            this.setPos(this.getX(), this.getY(), this.getZ());
        }
    }


    public boolean handleWaterMovement() {
        return true;
    }

    @Override
    protected void onHit(RayTraceResult movingObject) {
        this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        Entity shootingEntity = this.getOwner();
        if (!this.level.isClientSide) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

                if (entity != null && entity instanceof EntityHydraHead) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityHydra) {
                    EntityHydra dragon = (EntityHydra) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity)) {
                        return;
                    }
                    entity.hurt(DamageSource.mobAttack(dragon), 2.0F);
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.POISON, 60, 0));
                    }

                }
            }
        }
    }
}

