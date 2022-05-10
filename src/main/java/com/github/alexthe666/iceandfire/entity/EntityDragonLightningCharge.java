package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

public class EntityDragonLightningCharge extends EntityDragonCharge implements IDragonProjectile {


    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDragonLightningCharge(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), worldIn);
    }

    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX,
                                       double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn,
                                       EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public DamageSource causeDamage(@Nullable Entity cause) {
        return IafDamageRegistry.causeDragonLightningDamage(cause);
    }

    @Override
    public void destroyArea(World world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaLightningCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafConfig.dragonAttackDamageLightning;
    }


}