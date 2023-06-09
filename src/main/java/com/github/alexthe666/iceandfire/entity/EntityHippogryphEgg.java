package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityHippogryphEgg extends EggEntity {

    private ItemStack itemstack;

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World world) {
        super(type, world);
    }

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World worldIn, double x, double y, double z,
        ItemStack stack) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.itemstack = stack;
    }

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World worldIn, LivingEntity throwerIn,
        ItemStack stack) {
        this(type, worldIn);
        this.setPosition(throwerIn.getPosX(), throwerIn.getPosYEye() - 0.1F, throwerIn.getPosZ());
        this.itemstack = stack;
        this.setShooter(throwerIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        Entity thrower = getShooter();
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) result).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0.0F);
        }

        if (!this.world.isRemote) {
            EntityHippogryph hippogryph = new EntityHippogryph(IafEntityRegistry.HIPPOGRYPH, this.world);
            hippogryph.setGrowingAge(-24000);
            hippogryph.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            if (itemstack != null) {
                int variant = 0;
                CompoundNBT tag = itemstack.getTag();
                if (tag != null) {
                    variant = tag.getInt("EggOrdinal");
                }
                hippogryph.setVariant(variant);
            }

            if (thrower instanceof PlayerEntity) {
                hippogryph.setTamedBy((PlayerEntity) thrower);
            }

            this.world.addEntity(hippogryph);
        }

        this.world.setEntityState(this, (byte) 3);
        this.remove();
    }

    @Override
    protected Item getDefaultItem() {
      return IafItemRegistry.HIPPOGRYPH_EGG;
    }
}
