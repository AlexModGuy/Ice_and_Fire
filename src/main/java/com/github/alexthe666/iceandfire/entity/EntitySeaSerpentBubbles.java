package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySeaSerpentBubbles extends AbstractFireballEntity implements IDragonProjectile {

    public EntitySeaSerpentBubbles(EntityType<? extends AbstractFireballEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public EntitySeaSerpentBubbles(EntityType<? extends AbstractFireballEntity> t, World worldIn, double posX,
                                   double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntitySeaSerpentBubbles(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), world);
    }


    public EntitySeaSerpentBubbles(EntityType<? extends AbstractFireballEntity> t, World worldIn,
                                   EntitySeaSerpent shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.xPower = accelX / d0 * 0.1D;
        this.yPower = accelY / d0 * 0.1D;
        this.zPower = accelZ / d0 * 0.1D;
    }

    @Override
    public boolean isPickable() {
        return false;
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
    public void tick() {
        Entity shootingEntity = this.getOwner();
        if (this.tickCount > 400) {
            this.remove();
        }
        autoTarget();
        this.xPower *= 0.95F;
        this.yPower *= 0.95F;
        this.zPower *= 0.95F;
        this.push(this.xPower, this.yPower, this.zPower);

        if (this.level.isClientSide || (shootingEntity == null || !shootingEntity.isAlive()) && this.level.hasChunkAt(this.blockPosition())) {
            this.baseTick();
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
            this.remove();
        }
    }

    @Override
    protected boolean canHitEntity(Entity entityIn) {
        return super.canHitEntity(entityIn) && !(entityIn instanceof EntityMutlipartPart) && !(entityIn instanceof EntitySeaSerpentBubbles);
    }


    public void autoTarget() {
        if (!level.isClientSide) {
            Entity shootingEntity = this.getOwner();
            if (shootingEntity instanceof EntitySeaSerpent && ((EntitySeaSerpent) shootingEntity).getTarget() != null) {
                Entity target = ((EntitySeaSerpent) shootingEntity).getTarget();
                double d2 = target.getX() - this.getX();
                double d3 = target.getY() - this.getY();
                double d4 = target.getZ() - this.getZ();
                double d0 = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
                this.xPower = d2 / d0 * 0.1D;
                this.yPower = d3 / d0 * 0.1D;
                this.zPower = d4 / d0 * 0.1D;
            } else if (tickCount > 20) {
                this.remove();
            }
        }
    }

    public boolean handleWaterMovement() {
        return true;
    }

    @Override
    protected IParticleData getTrailParticle() {
        return ParticleTypes.BUBBLE;
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
    protected void onHit(RayTraceResult movingObject) {
        boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (!this.level.isClientSide) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

                if (entity != null && entity instanceof EntitySlowPart) {
                    return;
                }
                Entity shootingEntity = this.getOwner();
                if (shootingEntity != null && shootingEntity instanceof EntitySeaSerpent) {
                    EntitySeaSerpent dragon = (EntitySeaSerpent) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity)) {
                        return;
                    }
                    entity.hurt(DamageSource.mobAttack(dragon), 6.0F);

                }
            }
        }
    }

}
