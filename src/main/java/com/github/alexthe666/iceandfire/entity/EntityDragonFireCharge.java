package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class EntityDragonFireCharge extends EntityDragonCharge {

    public EntityDragonFireCharge(EntityType<? extends Fireball> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDragonFireCharge(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), worldIn);
    }

    public EntityDragonFireCharge(EntityType<? extends Fireball> type, Level worldIn, double posX,
                                  double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonFireCharge(EntityType type, Level worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 4; ++i) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX() + ((this.random.nextDouble() - 0.5D) * getBbWidth()), this.getY() + ((this.random.nextDouble() - 0.5D) * getBbWidth()), this.getZ() + ((this.random.nextDouble() - 0.5D) * getBbWidth()), 0.0D, 0.0D, 0.0D);
        }
        if (this.isInWater()) {
            remove(RemovalReason.DISCARDED);
        }
        if (this.shouldBurn()) {
            this.setSecondsOnFire(1);
        }
        super.tick();
    }

    @Override
    public DamageSource causeDamage(@Nullable Entity cause) {
        return IafDamageRegistry.causeDragonFireDamage(cause);
    }

    @Override
    public void destroyArea(Level world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafConfig.dragonAttackDamageFire;
    }

}