package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityAmphithereArrow extends AbstractArrowEntity {

    public EntityAmphithereArrow(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setBaseDamage(2.5F);
    }

    public EntityAmphithereArrow(EntityType<? extends AbstractArrowEntity> type, World worldIn, double x, double y,
                                 double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(2.5F);
    }

    public EntityAmphithereArrow(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.AMPHITHERE_ARROW.get(), world);
    }

    public EntityAmphithereArrow(EntityType type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
        this.setBaseDamage(2.5F);
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if ((tickCount == 1 || this.tickCount % 70 == 0) && !this.inGround && !this.onGround) {
            this.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if (level.isClientSide && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getDeltaMovement().x * this.getBbWidth();
            double zRatio = this.getDeltaMovement().z * this.getBbWidth();
            this.level.addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);

        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        living.hasImpulse = true;
        double xRatio = this.getDeltaMovement().x;
        double zRatio = this.getDeltaMovement().z;
        float strength = -1.4F;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
        living.setDeltaMovement(living.getDeltaMovement().multiply(0.5D, 1, 0.5D).subtract(xRatio / f * strength, 0, zRatio / f * strength).add(0, 0.6, 0));
        spawnExplosionParticle();
    }

    public void spawnExplosionParticle() {
        if (this.level.isClientSide) {
            for (int height = 0; height < 1 + random.nextInt(2); height++) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    double xRatio = this.getDeltaMovement().x * this.getBbWidth();
                    double zRatio = this.getDeltaMovement().z * this.getBbWidth();
                    this.level.addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);
                }
            }
        } else {
            this.level.broadcastEntityEvent(this, (byte) 20);
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.AMPHITHERE_ARROW);
    }
}
