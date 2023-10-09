package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntitySeaSerpentArrow extends AbstractArrow {

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrow> t, Level worldIn) {
        super(t, worldIn);
        this.setBaseDamage(3F);
    }

    public EntitySeaSerpentArrow(EntityType<? extends AbstractArrow> t, Level worldIn, double x, double y,
                                 double z) {
        this(t, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(3F);
    }

    public EntitySeaSerpentArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.SEA_SERPENT_ARROW.get(), world);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public EntitySeaSerpentArrow(EntityType t, Level worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setBaseDamage(3F);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getDeltaMovement().x * this.getBbHeight();
            double zRatio = this.getDeltaMovement().z * this.getBbHeight();
            this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);
            this.level().addParticle(ParticleTypes.SPLASH, this.getX() + xRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getBbHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getBbWidth() * 1.0F - this.getBbWidth() - d2 * 10.0D, d0, d1, d2);

        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.SEA_SERPENT_ARROW.get());
    }
}
