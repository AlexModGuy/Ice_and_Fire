package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityCockatriceEgg extends ProjectileItemEntity {

    public EntityCockatriceEgg(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ProjectileItemEntity> type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z,
        World worldIn) {
        super(type, x, y, z, worldIn);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }

    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onHit(RayTraceResult result) {
        Entity thrower = getOwner();
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) result).getEntity().hurt(DamageSource.thrown(this, thrower), 0.0F);
        }

        if (!this.level.isClientSide) {
            if (this.random.nextInt(4) == 0) {
                int i = 1;

                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    EntityCockatrice cockatrice = new EntityCockatrice(IafEntityRegistry.COCKATRICE.get(), this.level);
                    cockatrice.setAge(-24000);
                    cockatrice.setHen(this.random.nextBoolean());
                    cockatrice.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
                    if (thrower instanceof PlayerEntity) {
                        cockatrice.tame((PlayerEntity) thrower);
                    }
                    this.level.addFreshEntity(cockatrice);
                }
            }

            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return IafItemRegistry.ROTTEN_EGG;
    }
}
