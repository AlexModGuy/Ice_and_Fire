package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityAmphithereArrow extends AbstractArrow {

    public EntityAmphithereArrow(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.setBaseDamage(2.5F);
    }

    public EntityAmphithereArrow(EntityType<? extends AbstractArrow> type, Level worldIn, double x, double y,
                                 double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(2.5F);
    }

    public EntityAmphithereArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.AMPHITHERE_ARROW.get(), world);
    }

    public EntityAmphithereArrow(EntityType type, LivingEntity shooter, Level worldIn) {
        super(type, shooter, worldIn);
        this.setBaseDamage(2.5F);
    }


    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if ((tickCount == 1 || this.tickCount % 70 == 0) && !this.inGround && !this.onGround()) {
            this.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        }
        if (level().isClientSide && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getDeltaMovement().x * this.getBbWidth();
            double zRatio = this.getDeltaMovement().z * this.getBbWidth();
            this.level().addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);

        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        living.hasImpulse = true;
        double xRatio = this.getDeltaMovement().x;
        double zRatio = this.getDeltaMovement().z;
        float strength = -1.4F;
        float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        living.setDeltaMovement(living.getDeltaMovement().multiply(0.5D, 1, 0.5D).subtract(xRatio / f * strength, 0, zRatio / f * strength).add(0, 0.6, 0));
        spawnExplosionParticle();
    }

    public void spawnExplosionParticle() {
        if (this.level().isClientSide) {
            for (int height = 0; height < 1 + random.nextInt(2); height++) {
                for (int i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    double xRatio = this.getDeltaMovement().x * this.getBbWidth();
                    double zRatio = this.getDeltaMovement().z * this.getBbWidth();
                    this.level().addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * d3, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * d3, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * d3, d0, d1, d2);
                }
            }
        } else {
            this.level().broadcastEntityEvent(this, (byte) 20);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 20) {
            this.spawnExplosionParticle();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.AMPHITHERE_ARROW.get());
    }
}
