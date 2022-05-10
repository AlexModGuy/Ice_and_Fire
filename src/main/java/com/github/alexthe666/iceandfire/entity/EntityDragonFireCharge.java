package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

public class EntityDragonFireCharge extends EntityDragonCharge {

    public EntityDragonFireCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDragonFireCharge(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), worldIn);
    }

    public EntityDragonFireCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX,
                                  double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonFireCharge(EntityType type, World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 4; ++i) {
            this.world.addParticle(ParticleTypes.FLAME, this.getPosX() + ((this.rand.nextDouble() - 0.5D) * getWidth()), this.getPosY() + ((this.rand.nextDouble() - 0.5D) * getWidth()), this.getPosZ() + ((this.rand.nextDouble() - 0.5D) * getWidth()), 0.0D, 0.0D, 0.0D);
        }
        if (this.isInWater()) {
            remove();
        }
        if (this.isFireballFiery()) {
            this.setFire(1);
        }
        super.tick();
    }

    @Override
    public DamageSource causeDamage(@Nullable Entity cause) {
        return IafDamageRegistry.causeDragonFireDamage(cause);
    }

    @Override
    public void destroyArea(World world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaFireCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafConfig.dragonAttackDamageFire;
    }

}