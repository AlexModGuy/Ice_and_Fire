package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EntityDeathWormEgg extends ProjectileItemEntity {

    private boolean giant;

    public EntityDeathWormEgg(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDeathWormEgg(EntityType type, LivingEntity throwerIn, World worldIn, boolean giant) {
        super(type, throwerIn, worldIn);
        this.giant = giant;
    }

    public EntityDeathWormEgg(EntityType type, double x, double y, double z, World worldIn, boolean giant) {
        super(type, x, y, z, worldIn);
        this.giant = giant;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    protected void registerData() {

    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }

    }

    protected Item getDefaultItem() {
        return giant ? IafItemRegistry.DEATHWORM_EGG_GIGANTIC : IafItemRegistry.DEATHWORM_EGG;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {

            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote && this.getThrower() != null) {
            EntityDeathWorm deathworm = new EntityDeathWorm(IafEntityRegistry.DEATH_WORM, this.world);
            deathworm.setVariant(new Random().nextInt(3));
            deathworm.setTamed(true);
            deathworm.setWormHome(new BlockPos(this));
            deathworm.setWormAge(1);
            deathworm.setDeathWormScale(giant ? (0.25F + (float) (Math.random() * 0.35F)) * 4 : 0.25F + (float) (Math.random() * 0.35F));
            deathworm.setOwnerId(this.getThrower().getUniqueID());
            deathworm.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            this.world.addEntity(deathworm);
            this.world.setEntityState(this, (byte) 3);
            this.remove();
        }
    }
}