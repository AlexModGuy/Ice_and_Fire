package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDeathWormEgg extends ProjectileItemEntity implements IEntityAdditionalSpawnData {

    private boolean giant;

    public EntityDeathWormEgg(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDeathWormEgg(EntityType<? extends ProjectileItemEntity> type, LivingEntity throwerIn, World worldIn,
        boolean giant) {
        super(type, throwerIn, worldIn);
        this.giant = giant;
    }

    public EntityDeathWormEgg(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z,
        World worldIn, boolean giant) {
        super(type, x, y, z, worldIn);
        this.giant = giant;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeBoolean(this.giant);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.giant = additionalData.readBoolean();
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
            float wormSize = 0.25F + (float) (Math.random() * 0.35F);

            EntityDeathWorm deathworm = new EntityDeathWorm(IafEntityRegistry.DEATH_WORM.get(), this.level);
            deathworm.setVariant(random.nextInt(3));
            deathworm.setTame(true);
            deathworm.setWormHome(blockPosition());
            deathworm.setWormAge(1);
            deathworm.setDeathWormScale(giant ? (wormSize * 4) : wormSize);
            deathworm.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            if (thrower instanceof PlayerEntity) {
                deathworm.setOwnerUUID(thrower.getUUID());
            }
            this.level.addFreshEntity(deathworm);

            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return giant ? IafItemRegistry.DEATHWORM_EGG_GIGANTIC : IafItemRegistry.DEATHWORM_EGG;
    }
}
