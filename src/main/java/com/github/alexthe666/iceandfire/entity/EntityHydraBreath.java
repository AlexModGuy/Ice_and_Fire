package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityHydraBreath extends Fireball implements IDragonProjectile {

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn) {
        super(t, worldIn);
    }

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn, double posX, double posY,
                             double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntityHydraBreath(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.HYDRA_BREATH.get(), worldIn);
    }

    public EntityHydraBreath(EntityType<? extends Fireball> t, Level worldIn, EntityHydra shooter,
                             double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.02D;
        this.yPower = accelY / d0 * 0.02D;
        this.zPower = accelZ / d0 * 0.02D;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
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
    public boolean isPickable() {
        return false;
    }


    @Override
    public void tick() {
        this.clearFire();
        if (this.tickCount > 30) {
            this.remove(RemovalReason.DISCARDED);
        }
        Entity shootingEntity = this.getOwner();
        if (this.level().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level().hasChunkAt(this.blockPosition())) {
            this.baseTick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            Vec3 Vector3d = this.getDeltaMovement();
            double d0 = this.getX() + Vector3d.x;
            double d1 = this.getY() + Vector3d.y;
            double d2 = this.getZ() + Vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.level().isClientSide) {
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
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
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
    protected void onHit(@NotNull HitResult movingObject) {
        this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        Entity shootingEntity = this.getOwner();
        if (!this.level().isClientSide) {
            if (movingObject.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();

                if (entity != null && entity instanceof EntityHydraHead) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityHydra) {
                    EntityHydra dragon = (EntityHydra) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity)) {
                        return;
                    }
                    entity.hurt(level().damageSources().mobAttack(dragon), 2.0F);
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                    }

                }
            }
        }
    }
}

