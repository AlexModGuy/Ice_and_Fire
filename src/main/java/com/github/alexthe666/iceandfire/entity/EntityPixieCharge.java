package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityPixieCharge extends Fireball {

    public int ticksInAir;
    private final float[] rgb;

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn) {
        super(t, worldIn);
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }


    public EntityPixieCharge(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn);
    }

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn, double posX, double posY,
                             double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn, Player shooter,
                             double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
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
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; ++i) {
                IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), this.getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), rgb[0], rgb[1], rgb[2]);
            }
        }
        this.clearFire();
        if (this.tickCount > 30) {
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.level().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level().hasChunkAt(this.blockPosition())) {
            this.baseTick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            ++this.ticksInAir;
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));

            this.xPower *= 0.95F;
            this.yPower *= 0.95F;
            this.zPower *= 0.95F;
            this.push(this.xPower, this.yPower, this.zPower);
            ++this.ticksInAir;

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
            }
            this.setPos(d0, d1, d2);
            this.setPos(this.getX(), this.getY(), this.getZ());
        }
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        boolean flag = false;
        Entity shootingEntity = this.getOwner();
        if (!this.level().isClientSide) {
            if (movingObject.getType() == HitResult.Type.ENTITY && !((EntityHitResult) movingObject).getEntity().is(shootingEntity)) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();
                if (shootingEntity != null && shootingEntity.equals(entity)) {
                    flag = true;
                } else {
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 0));
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                        entity.hurt(level().damageSources().indirectMagic(shootingEntity, null), 5.0F);
                    }
                    if (this.level().isClientSide) {
                        for (int i = 0; i < 20; ++i) {
                            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), this.getZ() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), rgb[0], rgb[1], rgb[2]);
                        }
                    }
                    if (shootingEntity == null || !(shootingEntity instanceof Player) || !((Player) shootingEntity).isCreative()) {
                        if (random.nextInt(3) == 0) {
                            this.spawnAtLocation(new ItemStack(IafItemRegistry.PIXIE_DUST.get(), 1), 0.45F);
                        }
                    }
                }
                if (!flag && this.tickCount > 4) {
                    this.remove(RemovalReason.DISCARDED);
                }
            }

        }
    }
}