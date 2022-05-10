package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

public class EntityDragonIceCharge extends EntityDragonCharge {

    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);

    }

    public EntityDragonIceCharge(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.ICE_DRAGON_CHARGE.get(), worldIn);
    }

    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX,
                                 double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn,
                                 EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public void tick() {
        if (this.world.isRemote){
            for (int i = 0; i < 10; ++i) {
                IceAndFire.PROXY.spawnParticle(EnumParticles.DragonIce, this.getPosX() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosY() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosZ() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
            }
        }
        super.tick();
    }

    @Override
    public DamageSource causeDamage(@Nullable Entity cause) {
        return IafDamageRegistry.causeDragonIceDamage(cause);
    }

    @Override
    public void destroyArea(World world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaIceCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafConfig.dragonAttackDamageIce;
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }


}