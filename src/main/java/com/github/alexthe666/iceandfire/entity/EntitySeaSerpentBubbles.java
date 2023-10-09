package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

public class EntitySeaSerpentBubbles extends Fireball implements IDragonProjectile {

    public EntitySeaSerpentBubbles(EntityType<? extends Fireball> t, Level worldIn) {
        super(t, worldIn);
    }

    public EntitySeaSerpentBubbles(EntityType<? extends Fireball> t, Level worldIn, double posX,
                                   double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntitySeaSerpentBubbles(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), world);
    }


    public EntitySeaSerpentBubbles(EntityType<? extends Fireball> t, Level worldIn,
                                   EntitySeaSerpent shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.1D;
        this.yPower = accelY / d0 * 0.1D;
        this.zPower = accelZ / d0 * 0.1D;
    }

    @Override
    public boolean isPickable() {
        return false;
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
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.tickCount > 400) {
            this.remove(RemovalReason.DISCARDED);
        }
        autoTarget();
        this.xPower *= 0.95F;
        this.yPower *= 0.95F;
        this.zPower *= 0.95F;
        this.push(this.xPower, this.yPower, this.zPower);

        if (this.level().isClientSide || (shootingEntity == null || !shootingEntity.isAlive()) && this.level().hasChunkAt(this.blockPosition())) {
            this.baseTick();
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
                for (int i = 0; i < 3; ++i) {
                    IceAndFire.PROXY.spawnParticle(EnumParticles.Serpent_Bubble, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, this.getY() - 0.5D, this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, 0, 0, 0);
                }
            }

            this.setDeltaMovement(Vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.setPos(d0, d1, d2);
            this.setPos(this.getX(), this.getY(), this.getZ());
        }
        this.setPos(this.getX(), this.getY(), this.getZ());
        if (this.tickCount > 20 && !isInWaterOrRain()) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entityIn) {
        return super.canHitEntity(entityIn) && !(entityIn instanceof EntityMutlipartPart) && !(entityIn instanceof EntitySeaSerpentBubbles);
    }


    public void autoTarget() {
        if (this.level().isClientSide) {
            Entity shootingEntity = this.getOwner();
            if (shootingEntity instanceof EntitySeaSerpent && ((EntitySeaSerpent) shootingEntity).getTarget() != null) {
                Entity target = ((EntitySeaSerpent) shootingEntity).getTarget();
                double d2 = target.getX() - this.getX();
                double d3 = target.getY() - this.getY();
                double d4 = target.getZ() - this.getZ();
                double d0 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
                this.xPower = d2 / d0 * 0.1D;
                this.yPower = d3 / d0 * 0.1D;
                this.zPower = d4 / d0 * 0.1D;
            } else if (tickCount > 20) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    public boolean handleWaterMovement() {
        return true;
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.BUBBLE;
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
    protected void onHit(@NotNull HitResult movingObject) {
        boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (!this.level().isClientSide) {
            if (movingObject.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();

                if (entity != null && entity instanceof EntitySlowPart) {
                    return;
                }
                Entity shootingEntity = this.getOwner();
                if (shootingEntity != null && shootingEntity instanceof EntitySeaSerpent) {
                    EntitySeaSerpent dragon = (EntitySeaSerpent) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity)) {
                        return;
                    }
                    entity.hurt(level().damageSources().mobAttack(dragon), 6.0F);

                }
            }
        }
    }

}
