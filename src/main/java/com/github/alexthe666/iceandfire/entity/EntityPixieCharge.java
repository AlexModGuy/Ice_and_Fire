package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityPixieCharge extends AbstractFireballEntity {

    public int ticksInAir;
    private final float[] rgb;

    public EntityPixieCharge(EntityType<? extends AbstractFireballEntity> t, World worldIn) {
        super(t, worldIn);
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }


    public EntityPixieCharge(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn);
    }

    public EntityPixieCharge(EntityType<? extends AbstractFireballEntity> t, World worldIn, double posX, double posY,
                             double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }

    public EntityPixieCharge(EntityType<? extends AbstractFireballEntity> t, World worldIn, PlayerEntity shooter,
                             double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.07D;
        this.yPower = accelY / d0 * 0.07D;
        this.zPower = accelZ / d0 * 0.07D;
        rgb = EntityPixie.PARTICLE_RGB[random.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
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
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.level.isClientSide) {
            for (int i = 0; i < 5; ++i) {
                IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), this.getZ() + this.random.nextDouble() * 0.15F * (this.random.nextBoolean() ? -1 : 1), rgb[0], rgb[1], rgb[2]);
            }
        }
        this.clearFire();
        if (this.tickCount > 30) {
            this.remove();
        }
        if (this.level.isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.level.hasChunkAt(this.blockPosition())) {
            this.baseTick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            ++this.ticksInAir;
            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));

            this.xPower *= 0.95F;
            this.yPower *= 0.95F;
            this.zPower *= 0.95F;
            this.push(this.xPower, this.yPower, this.zPower);
            ++this.ticksInAir;

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * 0.25D, this.getY() - this.getDeltaMovement().y * 0.25D, this.getZ() - this.getDeltaMovement().z * 0.25D, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
            }
            this.setPos(d0, d1, d2);
            this.setPos(this.getX(), this.getY(), this.getZ());
        }
    }

    @Override
    protected void onHit(RayTraceResult movingObject) {
        boolean flag = false;
        Entity shootingEntity = this.getOwner();
        if (!this.level.isClientSide) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY && !((EntityRayTraceResult) movingObject).getEntity().is(shootingEntity)) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();
                if (shootingEntity != null && shootingEntity.equals(entity)) {
                    flag = true;
                } else {
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.LEVITATION, 100, 0));
                        ((LivingEntity) entity).addEffect(new EffectInstance(Effects.GLOWING, 100, 0));
                        entity.hurt(DamageSource.indirectMagic(shootingEntity, null), 5.0F);
                    }
                    if (this.level.isClientSide) {
                        for (int i = 0; i < 20; ++i) {
                            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.getX() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), this.getZ() + this.random.nextDouble() * 1F * (this.random.nextBoolean() ? -1 : 1), rgb[0], rgb[1], rgb[2]);
                        }
                    }
                    if (shootingEntity == null || !(shootingEntity instanceof PlayerEntity) || !((PlayerEntity) shootingEntity).isCreative()) {
                        if (random.nextInt(3) == 0) {
                            this.spawnAtLocation(new ItemStack(IafItemRegistry.PIXIE_DUST, 1), 0.45F);
                        }
                    }
                }
                if (!flag && this.tickCount > 4) {
                    this.remove();
                }
            }

        }
    }
}